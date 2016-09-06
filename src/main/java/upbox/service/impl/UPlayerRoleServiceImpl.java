package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UEquipment;
import upbox.model.UParameterInfo;
import upbox.model.UPlayer;
import upbox.model.UPlayerApply;
import upbox.model.UPlayerRole;
import upbox.model.UPlayerRoleLimit;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.zhetao.ParameterInfos;
import upbox.outModel.OutPlayerList;
import upbox.outModel.OutUteamList;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerRoleLogService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.UUserService;
import upbox.util.YHDCollectionUtils;
/**
 * 前端球员身份接口实现类
 * @author mercideng
 *
 */

@Service("uPlayerRoleService")
public class UPlayerRoleServiceImpl implements UPlayerRoleService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UPlayerService uPlayerService;
	
	@Resource
	private UParameterService uParameterService;
	
	@Resource
	private UPlayerRoleLogService uPlayerRoleLogService;
	
	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	
	@Resource
	private PublicPushService publicPushService;
	
	@Resource
	private MessageService messageService;
	@Resource
	private UUserService uUserService;

	//选择角色时，判断该角色是否已被别人选了
	private StringBuffer isOtherSelected202_sql = new StringBuffer(
			"select p.player_id playerId,r.key_id keyId from u_player p,u_player_role r "
			+"where p.player_id=r.player_id and r.member_type_use_status='1' and p.team_id=:teamId and r.member_type=:memberType and p.user_id<>:userId and p.in_team='1'");
	
	//选择角色时，判断该角色是否已被自己选了
	private StringBuffer isMyselfSelected202_sql = new StringBuffer(
			"select p.player_id playerId,r.key_id keyId from u_player p,u_player_role r "
			+"where p.player_id=r.player_id and r.member_type_use_status='1' and p.team_id=:teamId and r.member_type=:memberType and p.user_id=:userId and p.in_team='1'");
		
	/**
	 * 
	 * 
	   TODO - 身份转让
	   @param map
	   		memberType  转让者角色
	   		teamId		球队Id
	   		playerId	被转让者的球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> memberTypeTransfer202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//首先球队Id不能为空
		if (publicService.StringUtil(map.get("teamId"))) {
			//查询转让者是否是这个球队的
			String type = "1";
			UPlayer uPlayer2 = uPlayerService.getUteamUplayerinfoByTeamId(map, type);
			if (null != uPlayer2) {
				//查询被转让者是否跟我是同一球队
				if (publicService.StringUtil(map.get("playerId"))) {
					//查询被转让者的球员信息
					UPlayer uPlayerTemp1 = baseDAO.get(map, "from UPlayer where playerId=:playerId");
					if (null != uPlayerTemp1) {
						if (null != uPlayerTemp1.getUTeam()) {
							if (map.get("teamId").equals(uPlayerTemp1.getUTeam().getTeamId())) {
								//查询被转让者是否存在该角色
								UPlayerRole uPlayerRole= baseDAO.get(map,
										"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and UPlayer.UUser.userId=:loginUserId and memberTypeUseStatus='1' and UPlayer.inTeam='1' ");
								if (null != uPlayerRole) {
									return WebPublicMehod.returnRet("error", "您的 “"+map.get("memberTypeName")+"”角色已不属于您，不能进行转让");
								}
								uPlayerRole = baseDAO.get(map, "from UPlayerRole where UPlayer.playerId=:playerId and memberType=:memberType");
								if (null != uPlayerRole) {
									if("1".equals(uPlayerRole.getMemberTypeUseStatus())){
										return WebPublicMehod.returnRet("error", "你的 “"+map.get("memberTypeName")+"”角色已成功转让给球队中的成员");
									}
								}
								//查询身份是否是唯一，可转让的
								map.put("name", "member_type");
								map.put("params",map.get("memberType"));
								UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
								if (null != uParameterInfo) {
									if (publicService.StringUtil(uParameterInfo.getIsUnique())) {
										//1:唯一，可转让
										if (!"3".equals(uParameterInfo.getIsUnique())) {
											//获取被转让者姓名
											String userName = null;
											if (publicService.StringUtil(uPlayerTemp1.getUUser().getRealname())) {
												userName = uPlayerTemp1.getUUser().getRealname();
											}else {
												userName = uPlayerTemp1.getUUser().getNickname();
											}
											if (null == userName || "".equals(userName)) {
												userName = "-";
											}
											//判断被转让者有几个角色，1一个可以转让  2  不可以转让
											List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(map);
											if (UPlayerRoles.size() < 2) {
												UPlayerApply playerApply=null;
												List<UPlayerApply> playerApplyList=null;
												if(uParameterInfo!=null){
													map.put("memberTypeName",uParameterInfo.getName());//通知用
												}
												map.put("bplayerid", uPlayer2.getPlayerId());// 分配中球员ID
												this.judgePlayerApply(map, playerApply, playerApplyList);
												//第一步：被转让这新增一条身份记录
												this.createNewPlayRole202(uPlayerTemp1,map);
												//判断转让者是否有两个角色  是：删除转让的角色  否：将角色修改为无角色
												this.updateMemberTypeByPlayerId202(uPlayer2,map);
												//在转让角色记录表中记录转让记录
												uPlayerRoleLogService.createNewLog202(map,uPlayerTemp1);
												//发送通知  暂无
												String messageType = "1";//角色转让通知
												this.setMessageParams202(messageType,uPlayer2,uPlayerTemp1,map);
												resultMap.put("success", "你的 “"+map.get("memberTypeName")+"”角色已成功转让给了"+userName+"成员");
											}else{
												return WebPublicMehod.returnRet("error", "-3_当前球队中的"+userName+"成员已拥有了两个角色，不可再拥有更多的角色了");
											}
										}else{
											return WebPublicMehod.returnRet("error", "你当前的角色暂时不可转让");
										}
									}else{
										return WebPublicMehod.returnRet("error", "你当前的角色暂时不可转让");
									}
								}else{
									return WebPublicMehod.returnRet("error", "当前角色不存在");
								}
							}else{
								return WebPublicMehod.returnRet("error", "当前球员跟你不属于同一个球队");
							}
						}
					}else{
						return WebPublicMehod.returnRet("error", "当前球员不存在");
					}
				}else{
					return WebPublicMehod.returnRet("error", "请求参数playerId为空");
				}
			}else{
				return WebPublicMehod.returnRet("error", "你还未加入当前球队");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数teamId为空");
		}
		return resultMap;
	}
	

	/**
	 * 
	 * 
	   TODO - 角色转让通知
	   @param messageType	通知类型    1-转让通知
	   @param uPlayer		转让者球员对象
	   @param uPlayerTemp 	被转让者球员对象
	   @param map
	   		teamId			球队Id
	   2016年6月12日
	   dengqiuru
	 * @throws Exception 
	 */
	private void setMessageParams202(String messageType, UPlayer uPlayer, UPlayer uPlayerTemp, HashMap<String, String> map) throws Exception {
		String mesType = null;//发送的短信类型
		String userName = null;//用户名称
		String teamName = null;//球队名称
		String msg = null;//发送的短信内容
		String userId = null;//给谁发送
		String params = null;//跳转参数
		String jump = null;//跳转类型
		String code = null;//推送code
		String repetition = "1";
		UTeam uTeam = baseDAO.get(map, "from UTeam where teamId=:teamId");
		if (null != uTeam) {
			if (publicService.StringUtil(uTeam.getName())) {
				teamName = uTeam.getName();
			}else{
				teamName = uTeam.getShortName();
			}
		}
		if ("1".equals(messageType)) {//加队通知
			mesType = "tmTransfer";
			if (null != uPlayerTemp) {
				if (null != uPlayerTemp.getUUser()) {
					userId = uPlayerTemp.getUUser().getUserId();
				}
			}
			jump = "b01";
			if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
				userName = uPlayer.getUUser().getRealname();
			}else{
				userName = uPlayer.getUUser().getNickname();
			}
			msg = teamName +"球队的"+userName+"成员已把"+map.get("memberTypeName")+"转让给你了";
			params = "{\"jump\":\"" + jump + "\",\"playerId\":\""+ uPlayer.getPlayerId() + "\",\"teamId\":\""+ uTeam.getTeamId() + "\",\"userId\":\""+ userId + "\"}";
			//极光推送参数
			map.put("jump", jump);
			map.put("playerId", uPlayer.getPlayerId());
			map.put("teamId",  uTeam.getTeamId());
			map.put("userId",  userId);
		}
		//根据userId 查code
		if (publicService.StringUtil(userId)) {
			UUser uUser = baseDAO.getHRedis(UUser.class,userId,PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			if (null != uUser) {
				if (publicService.StringUtil(uUser.getNumberid())) {
					map.put("keyId", uUser.getNumberid());
					UEquipment uEquipment = baseDAO.get(map, "from UEquipment where keyId=:keyId");
					if (null != uEquipment) {
						code = uEquipment.getCode();
					}
				}
			}
		}
		this.setMessage202(map, mesType, msg, userId, params,repetition);//发送通知
		this.publicAppPush202(map,mesType,msg,code);//发送极光推送
		
	}


	/**
	 * 
	 * 
	   TODO - 通知
	   @param map
	   @param mesType		通知类型
	 * @param msg 			通知内容
	   @param uTeam			球队对象
	   @param uPlayer		球员对象
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	private void setMessage202(HashMap<String, String> map, String mesType, String msg, String userId, String params,String repetition) throws Exception {
		map.put("type", "team");
		map.put("mes_type", mesType);
		map.put("contentName", msg);
		map.put("params", params);
		map.put("userId", userId);
		map.put("repetition", repetition);
		messageService.addTheMessageByType(map);
	}
	/**
	 * 
	 * 
	   TODO - 极光推送  参数
	   @param map
	   @param mesType		推送类型
	   @param msg			推送内容
	   @param code			推送的code
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void publicAppPush202(HashMap<String, String> map, String mesType, String msg, String code) throws Exception {
		map.put("mes_type", mesType);
		map.put("code", code);
		map.put("content", msg);
		publicPushService.publicAppPush(map);
	}

	/**
	 * 
	 * 
	   TODO - 转让角色时，判断转让者有几个角色， 1一个：角色变为无角色  2：删除转换角色
	   @param uPlayer
	   		转让者的球员信息
	   @param map
	   		memberType   角色
	   2016年6月3日
	   dengqiuru
	 * @throws Exception 
	 */
	private void updateMemberTypeByPlayerId202(UPlayer uPlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		hashMap.put("playerId", uPlayer.getPlayerId());
		//查询转让者有几个角色
		List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerIdInTeam202(hashMap);
//		if (null != UPlayerRoles && UPlayerRoles.size() < 2) {//一个 角色变为无角色 
//			UPlayerRole UPlayerRole = UPlayerRoles.get(0);
//			UPlayerRole.setMemberType("11");
//			UPlayerRole.setChangeDate(new Date());
//			UPlayerRole.setMemberTypeUseStatus("1");
//			baseDAO.getSessionFactory().getCurrentSession().flush();
//			baseDAO.update(UPlayerRole);
//		}else{//不只一个  删除转换角色
//			for (UPlayerRole UPlayerRole : UPlayerRoles) {
//				if (map.get("memberType").equals(UPlayerRole.getMemberType())) {
//					baseDAO.getSessionFactory().getCurrentSession().flush();
//					baseDAO.delete(UPlayerRole);
//				}
//			}
//		}
		if (null != UPlayerRoles && UPlayerRoles.size() > 0) {//一个 角色变为无角色 
			for (UPlayerRole UPlayerRole : UPlayerRoles) {
				if (map.get("memberType").equals(UPlayerRole.getMemberType())) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
					baseDAO.delete(UPlayerRole);
				}
			}
		}
	}


	/**
	 * 
	 * 
	   TODO - 当前球员新增一条角色
	   @param uPlayerTemp
	   		被转让者对象
	   @param map
	   		memberType		身份
	   2016年6月3日
	   dengqiuru
	 * @throws Exception 
	 */
	private void createNewPlayRole202(UPlayer uPlayerTemp, HashMap<String, String> map) throws Exception {
		UPlayerRole UPlayerRole = new UPlayerRole();
		UPlayerRole.setKeyId(WebPublicMehod.getUUID());
		UPlayerRole.setMemberType(map.get("memberType"));
		UPlayerRole.setUPlayer(uPlayerTemp);
		UPlayerRole.setChangeDate(new Date());
		UPlayerRole.setMemberTypeUseStatus("1");
		baseDAO.save(UPlayerRole);
	}


	/**
	 * 
	 * 
	   TODO - 查询当前用球员有几个角色
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	@Override
	public List<UPlayerRole> getMemberTypeByPlayerId202(HashMap<String, String> map) throws Exception {
		List<UPlayerRole> UPlayerRoles = null;
		if (publicService.StringUtil(map.get("playerId"))) {
			UPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='1' and memberType<>'11' order by memberType+0 asc");
		}else if(publicService.StringUtil(map.get("playerId1"))){
			UPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId1 and memberTypeUseStatus='1' and memberType<>'11' order by memberType+0 asc");
		}
		return UPlayerRoles;
	}
	
	/**
	 * 
	 * 
	   TODO - 角色显示顺序【返回队内所有角色】
	   @param map
	   @return
	   @throws Exception
	   2016年7月6日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getMemberTypeByPlayerIdOrderByReankWeight202(HashMap<String, String> map)throws Exception {
		List<HashMap<String, Object>> uPlayerRoles = null;
		StringBuffer newsql = new StringBuffer("select r.member_type memberType,r.player_id,i.rank_weight from u_player_role r "
				+" LEFT JOIN u_parameter_info i on r.member_type=i.params and i.pkey_id in (select pkey_id from u_parameter p where p.params='member_type') "
				+" where r.member_type_use_status='1' and r.player_id=:playerId and r.member_type<>'11' " 
				+" ORDER BY i.rank_weight");
		uPlayerRoles = baseDAO.findSQLMap(map, newsql.toString());
		return uPlayerRoles;
	}
	/**
	 * 
	 * 
	   TODO - 查询当前用球员有几个角色(有球队的球员信息可以显示无角色)
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	private List<UPlayerRole> getMemberTypeByPlayerIdInTeam202(HashMap<String, String> map) throws Exception {
		List<UPlayerRole> UPlayerRoles = null;
		if (publicService.StringUtil(map.get("playerId"))) {
			UPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='1' order by memberType+0 asc");
		}
		return UPlayerRoles;
	}

	/**
	 * 
	 * 
	   TODO - 查询当前用球员有几个角色需要根据码表排序字段排序(有球队的球员信息可以显示无角色)
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getMemberTypeByPlayerIdInTeamOrderByRank202(HashMap<String, String> map) throws Exception {
		List<HashMap<String, Object>> uPlayerRoles = null;
		StringBuffer newsql = new StringBuffer("select r.member_type memberType,r.player_id,i.rank_weight from u_player_role r "
				+" LEFT JOIN u_parameter_info i on r.member_type=i.params and i.pkey_id in (select pkey_id from u_parameter p where p.params='member_type') "
				+" where r.member_type_use_status='1' and r.player_id=:playerId " 
				+" ORDER BY i.rank_weight ASC");
		uPlayerRoles = baseDAO.findSQLMap(map, newsql.toString());
		return uPlayerRoles;
	}

	/**
	 * 
	 * 
	   TODO - 查询并填充球员的角色
	   @param hashMap2
	   		playerId   球员Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setMembertype202(HashMap<String, Object> hashMap2)throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		HashMap<String, String> hashMapTemp = new HashMap<>();//转让者map
		//第一个身份
		String memberType = null;
		//第一个身份对应的名称
		String membertypeName = null;
		//第二个身份
		String memberType2 = null;
		//第二个身份对应的名称
		String membertypeName2 = null;
//		hashMap.put("isMyself", "1");
//		String isMyself = "2";
		//第一个身份权限
		String memberTypeRoleLimit1 = null;
		//第二个身份权限
		String memberTypeRoleLimit2 = null;
		//前端返回权限
		String memberTypeRoleLimit = null;
		//当前球员角色不为空
		if (null != hashMap2.get("playerId")) {
			//查询当前球员的角色
			hashMap.put("playerId", hashMap2.get("playerId").toString());
//			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			List<HashMap<String, Object>> UPlayerRoles=this.getMemberTypeByPlayerIdOrderByReankWeight202(hashMap); 
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
//				isMyself = "1";
				memberType = UPlayerRoles.get(0).get("memberType").toString();
				if (!"11".equals(UPlayerRoles.get(0).get("memberType").toString())) {
					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//第一个身份权限
					hashMap.put("memberType", memberType);
					UPlayerRoleLimit uPlayerRoleLimit = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMap);
					if (null != uPlayerRoleLimit) {
						memberTypeRoleLimit1 = uPlayerRoleLimit.getRankRole();
						memberTypeRoleLimit = memberTypeRoleLimit1;
					}
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
						memberType2 = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(memberType2);
						}
						//第二个身份权限
						hashMapTemp.put("memberType", memberType2);
						UPlayerRoleLimit uPlayerRoleLimit2 = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMapTemp);
						if (null != uPlayerRoleLimit2) {
							memberTypeRoleLimit2 = uPlayerRoleLimit2.getRankRole();
							//如果第一个权限排序大于第二  那就取第二
							if (Integer.parseInt(memberTypeRoleLimit1) > Integer.parseInt(memberTypeRoleLimit2)) {
								memberTypeRoleLimit = memberTypeRoleLimit2;
							}else{
								memberTypeRoleLimit = memberTypeRoleLimit1;
							}
						}
					}
				}else{//第一个角色为无角色时，将第二角色填充到第一角色中去
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
						memberType = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
						}
						//第二个身份权限
						hashMapTemp.put("memberType", memberType);
						UPlayerRoleLimit uPlayerRoleLimit = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMapTemp);
						if (null != uPlayerRoleLimit) {
							memberTypeRoleLimit = uPlayerRoleLimit.getRankRole();
						}
					}
				}
			}else{
				 memberTypeRoleLimit = "10";
			}
		}
//		hashMap2.put("isMyself", isMyself);
		hashMap2.put("memberType", memberType);
		hashMap2.put("memberTypeName", membertypeName);
		hashMap2.put("memberType2", memberType2);
		hashMap2.put("memberTypeName2", membertypeName2);
		hashMap2.put("memberTypeRoleLimit", memberTypeRoleLimit);
		return hashMap2;
	}

	/**
	 * 
	 * 
	   TODO - 查询并填充球员的角色(无顺序)
	   @param hashMap2
	   		playerId   球员Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setMembertypeNoOrderByRank202(HashMap<String, Object> hashMap2)throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		HashMap<String, String> hashMapTemp = new HashMap<>();//转让者map
		//第一个身份
		String memberType = null;
		//第一个身份对应的名称
		String membertypeName = null;
		//第二个身份
		String memberType2 = null;
		//第二个身份对应的名称
		String membertypeName2 = null;
//		hashMap.put("isMyself", "1");
//		String isMyself = "2";
		//第一个身份权限
		String memberTypeRoleLimit1 = null;
		//第二个身份权限
		String memberTypeRoleLimit2 = null;
		//前端返回权限
		String memberTypeRoleLimit = null;
		//当前球员角色不为空
		if (null != hashMap2.get("playerId")) {
			//查询当前球员的角色
			hashMap.put("playerId", hashMap2.get("playerId").toString());
			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
//				isMyself = "1";
				memberType = UPlayerRoles.get(0).getMemberType();
				if (!"11".equals(UPlayerRoles.get(0).getMemberType())) {
					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//第一个身份权限
					hashMap.put("memberType", memberType);
					UPlayerRoleLimit uPlayerRoleLimit = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMap);
					if (null != uPlayerRoleLimit) {
						memberTypeRoleLimit1 = uPlayerRoleLimit.getRankRole();
						memberTypeRoleLimit = memberTypeRoleLimit1;
					}
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
						memberType2 = UPlayerRoles.get(1).getMemberType();
						if (!"11".equals(UPlayerRoles.get(1).getMemberType())) {
							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(membertypeName2);
						}
						//第二个身份权限
						hashMapTemp.put("memberType", memberType2);
						UPlayerRoleLimit uPlayerRoleLimit2 = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMapTemp);
						if (null != uPlayerRoleLimit2) {
							memberTypeRoleLimit2 = uPlayerRoleLimit2.getRankRole();
							//如果第一个权限排序大于第二  那就取第二
							if (Integer.parseInt(memberTypeRoleLimit1) > Integer.parseInt(memberTypeRoleLimit2)) {
								memberTypeRoleLimit = memberTypeRoleLimit2;
							}else{
								memberTypeRoleLimit = memberTypeRoleLimit1;
							}
						}
					}
				}else{//第一个角色为无角色时，将第二角色填充到第一角色中去
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
						memberType = UPlayerRoles.get(1).getMemberType();
						if (!"11".equals(UPlayerRoles.get(1).getMemberType())) {
							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
						}
						//第二个身份权限
						hashMapTemp.put("memberType", memberType);
						UPlayerRoleLimit uPlayerRoleLimit = uPlayerRoleLimitService.getUplayerRoleLimitByMemberType(hashMapTemp);
						if (null != uPlayerRoleLimit) {
							memberTypeRoleLimit = uPlayerRoleLimit.getRankRole();
						}
					}
				}
			}else{
				 memberTypeRoleLimit = "10";
			}
		}
//		hashMap2.put("isMyself", isMyself);
		hashMap2.put("memberType", memberType);
		hashMap2.put("memberTypeName", membertypeName);
		hashMap2.put("memberType2", memberType2);
		hashMap2.put("memberTypeName2", membertypeName2);
		hashMap2.put("memberTypeRoleLimit", memberTypeRoleLimit);
		return hashMap2;
	}

	/**
	 * 
	 * 
	   TODO - 查询并填充球员的角色(无顺序)
	   @param hashMap2
	   		playerId   球员Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setMembertypebyZhenrong202(HashMap<String, Object> hashMap2)throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		//第一个身份
		String memberType = null;
		//第一个身份对应的名称
//		String membertypeName = null;
		//第二个身份
		String memberType2 = null;
		//第二个身份对应的名称
//		String membertypeName2 = null;
		//当前球员角色不为空
		if (null != hashMap2.get("playerId")) {
			//查询当前球员的角色
			hashMap.put("playerId", hashMap2.get("playerId").toString());
			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
//				isMyself = "1";
				memberType = UPlayerRoles.get(0).getMemberType();
				if (!"11".equals(UPlayerRoles.get(0).getMemberType())) {
//					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//第一个身份权限
					hashMap.put("memberType", memberType);
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
//						memberType2 = UPlayerRoles.get(1).getMemberType();
//						if (!"11".equals(UPlayerRoles.get(1).getMemberType())) {
//							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(membertypeName2);
//						}
					}
				}else{//第一个角色为无角色时，将第二角色填充到第一角色中去
					if (UPlayerRoles.size() > 1) {
						hashMap.put("memberType", memberType);
//						memberType = UPlayerRoles.get(1).getMemberType();
//						if (!"11".equals(UPlayerRoles.get(1).getMemberType())) {
//							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
//						}
					}
				}
			}
		}
//		hashMap2.put("isMyself", isMyself);
		hashMap2.put("memberType", memberType);
//		hashMap2.put("memberTypeName", membertypeName);
		hashMap2.put("memberType2", memberType2);
//		hashMap2.put("memberTypeName2", membertypeName2);
		return hashMap2;
	}
	/**
	 * 
	 * 
	   TODO - 选择角色时，判断该角色是否已被别人选了
	   @param map
	   		memberType   	角色
	   		loginUserId		当前用户Id
	   		teamId			当前球队Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public Boolean isOtherSelected202(HashMap<String, String> map) throws Exception {
		//是否被别人选择
		Boolean isOtherSelected = false;
		if (publicService.StringUtil(map.get("teamId"))) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("memberType", map.get("memberType"));//角色
			hashMap.put("teamId", map.get("teamId"));//球队Id
			hashMap.put("userId",  map.get("loginUserId"));//当前用户
			StringBuffer sql = this.isOtherSelected202_sql;
			//查询
			List<HashMap<String, Object>> outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);
			if (null != outPlayerLists && outPlayerLists.size() > 0) {
				isOtherSelected = true;
			}
		}
		return isOtherSelected;
	}

	/**
	 * 
	 * 
	   TODO - 选择角色时，判断该角色是否已被自己选了
	   @param map
	   		memberType   	角色
	   		loginUserId		当前用户Id
	   		teamId			当前球队Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public Boolean isMyselfSelected202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("memberType", map.get("memberType"));//角色
		hashMap.put("teamId", map.get("teamId"));//球队Id
		hashMap.put("userId",  map.get("loginUserId"));//当前用户
		//是否被自己选择
		Boolean isOtherSelected = false;
		StringBuffer sql = this.isMyselfSelected202_sql;
		//查询
		List<HashMap<String, Object>> outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);
		if (null != outPlayerLists && outPlayerLists.size() > 0) {
			isOtherSelected = true;
		}
		return isOtherSelected;
	}

	/**
	 * 
	 * 
	   TODO - 添加数据
	   @param uPlayer	对象
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public void insertNew(UPlayer uPlayer) throws Exception {
		UPlayerRole uPlayerRole = new UPlayerRole();
		uPlayerRole.setKeyId(WebPublicMehod.getUUID());
		uPlayerRole.setMemberType(uPlayer.getMemberType());
		uPlayerRole.setUPlayer(uPlayer);
		uPlayerRole.setChangeDate(new Date());
		uPlayerRole.setMemberTypeUseStatus("1");
		baseDAO.save(uPlayerRole);
	}

	/**
	 * 
	 * 
	   TODO - 加队时，球员新增角色模块
	   @param uPlayer    
	   @param map
	   2016年6月13日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> setNewMemberTypeForJoinTeam(UPlayer uPlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != uPlayer) {
			//检测角色是否被别人选择了,如没有，则更新到角色表
			this.checkMemberTypeIsOther(uPlayer,map);
		}
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 获取球员信息时，将角色填充到OutPlayerList中
	 * @param outPlayerList 
	   @param uPlayer
	   		对象
	   @param map
	   @return
	   2016年6月13日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public OutPlayerList setMemberTypeByGetUplayerinfo202(OutPlayerList outPlayerList,UPlayer uPlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		if (null != uPlayer) {
			hashMap.put("playerId", uPlayer.getPlayerId());
			//查询当前球员的角色
//			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			List<HashMap<String, Object>> UPlayerRoles=this.getMemberTypeByPlayerIdOrderByReankWeight202(hashMap); 
			UParameterInfo uParameterInfo = null;
			//第一个身份
			String memberType = null;
			//第一个身份对应的名称
			String membertypeName = null;
			//第一个身份是否唯一
			String memberTypeIsUnique = "2";
			//第二个身份
			String memberType2 = null;
			//第二个身份对应的名称
			String membertypeName2 = null;
			//第二个身份是否唯一
			String memberTypeIsUnique2 = "2";
			//当前球员角色不为空
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
				memberType = UPlayerRoles.get(0).get("memberType").toString();
				if (!"11".equals(UPlayerRoles.get(0).get("memberType").toString())) {
					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//查询码表对象，并查看该角色是否唯一
					//查询身份是否是唯一，可转让的
					map.put("name", "member_type");
					map.put("params",memberType);
					uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
					if (null != uParameterInfo) {
						//是唯一
						if ("1".equals(uParameterInfo.getIsUnique())) {
							memberTypeIsUnique = "1";
						}else if("2".equals(uParameterInfo.getIsUnique())){
							memberTypeIsUnique = "1";
						}else{
							memberTypeIsUnique = "2";
						}
					}
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType2 = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(memberType2);
						}
						map.put("params",memberType2);
						uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
						if (null != uParameterInfo) {
							//是唯一
							if ("1".equals(uParameterInfo.getIsUnique())) {
								memberTypeIsUnique2 = "1";
							}else if("2".equals(uParameterInfo.getIsUnique())){
								memberTypeIsUnique2 = "1";
							}else{
								memberTypeIsUnique2 = "2";
							}
						}
					}
				}else{
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
						}
						map.put("params",memberType);
						uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
						if (null != uParameterInfo) {
							//是唯一
							if ("1".equals(uParameterInfo.getIsUnique())) {
								memberTypeIsUnique = "1";
							}else if("2".equals(uParameterInfo.getIsUnique())){
								memberTypeIsUnique = "1";
							}else{
								memberTypeIsUnique = "2";
							}
						}
					}
				}
			}
			outPlayerList.setMemberType(memberType);
			outPlayerList.setMemberTypeName(membertypeName);
			outPlayerList.setMemberTypeIsUnique(memberTypeIsUnique);
			outPlayerList.setMemberType2(memberType2);
			outPlayerList.setMemberTypeName2(membertypeName2);
			outPlayerList.setMemberTypeIsUnique2(memberTypeIsUnique2);
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO - 给有球队的球员实体类填充角色
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年6月15日
	   dengqiuru
	 */
	@Override
	public OutPlayerList setMemberTypeByGetUplayerinfoInteam202(OutPlayerList outPlayerList,UPlayer uPlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		if (null != uPlayer) {
			hashMap.put("playerId", uPlayer.getPlayerId());
			//查询当前球员的角色
//			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerIdInTeam202(hashMap);
			List<HashMap<String, Object>> UPlayerRoles= this.getMemberTypeByPlayerIdInTeamOrderByRank202(hashMap);
			UParameterInfo uParameterInfo = null;
			//第一个身份
			String memberType = null;
			//第一个身份对应的名称
			String membertypeName = null;
			//第一个身份是否唯一
			String memberTypeIsUnique = "2";
			//第二个身份
			String memberType2 = null;
			//第二个身份对应的名称
			String membertypeName2 = null;
			//第二个身份是否唯一
			String memberTypeIsUnique2 = "2";
			//当前球员角色不为空
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
				memberType = UPlayerRoles.get(0).get("memberType").toString();
				membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
				//查询码表对象，并查看该角色是否唯一
				//查询身份是否是唯一，可转让的
				map.put("name", "member_type");
				map.put("params",memberType);
				uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
				if (null != uParameterInfo) {
					//是唯一
					if ("1".equals(uParameterInfo.getIsUnique())) {
						memberTypeIsUnique = "1";
					}else if("2".equals(uParameterInfo.getIsUnique())){
						memberTypeIsUnique = "1";
					}else{
						memberTypeIsUnique = "2";
					}
				}
				//第二个角色
				if (UPlayerRoles.size() > 1) {
					memberType2 = UPlayerRoles.get(1).get("memberType").toString();
					membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(memberType2);
					map.put("params",memberType2);
					uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
					if (null != uParameterInfo) {
						//是唯一
						if ("1".equals(uParameterInfo.getIsUnique())) {
							memberTypeIsUnique2 = "1";
						}else if("2".equals(uParameterInfo.getIsUnique())){
							memberTypeIsUnique2 = "1";
						}else{
							memberTypeIsUnique2 = "2";
						}
					}
				}
			}
			outPlayerList.setMemberType(memberType);
			outPlayerList.setMemberTypeName(membertypeName);
			outPlayerList.setMemberTypeIsUnique(memberTypeIsUnique);
			outPlayerList.setMemberType2(memberType2);
			outPlayerList.setMemberTypeName2(membertypeName2);
			outPlayerList.setMemberTypeIsUnique2(memberTypeIsUnique2);
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO - 给球员实体类填充角色
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年6月15日
	   dengqiuru
	 */
	@Override
	public UPlayer setMemberTypeByGetNoOutUplayerinfo202(UPlayer uPlayer) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		if (null != uPlayer) {
			hashMap.put("playerId", uPlayer.getPlayerId());
			//查询当前球员的角色
//			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			List<HashMap<String, Object>> UPlayerRoles = this.getMemberTypeByPlayerIdOrderByReankWeight202(hashMap);
			//第一个身份
			String memberType = null;
			//第一个身份对应的名称
			String membertypeName = null;
			//第二个身份
			String memberType2 = null;
			//第二个身份对应的名称
			String membertypeName2 = null;
			//当前球员角色不为空
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
				memberType = UPlayerRoles.get(0).get("memberType").toString();
				if (!"11".equals(UPlayerRoles.get(0).get("memberType").toString())) {
					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType2 = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(memberType2);
						}
					}
				}else{//第一角色为无角色
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
						}
					}
				}
			}
			uPlayer.setMemberType(memberType);
			uPlayer.setMemberTypeName(membertypeName);
			uPlayer.setMemberType2(memberType2);
			uPlayer.setMemberTypeName2(membertypeName2);
		}
		return uPlayer;
	}
	/**
	 * 
	 * 
	   TODO - 修改球员信息时，如果参数角色不为空，就更新角色表
	   @param map
	   		memberType		角色
	   @param updatePlayer		对象
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> setMemberTypeByEditplayer202(HashMap<String, String> map, UPlayer updatePlayer) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		if (null != updatePlayer) {
			map.put("playerId", updatePlayer.getPlayerId());
			//查询该球员有没有角色
			List<UPlayerRole> uPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='1' and memberType<>'11' order by memberType+0 asc");
			//有角色
			if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
				//先获取球员修改之前已有角色
				if (publicService.StringUtil(map.get("oldMemberType"))) {
					String beforeMemberTypes = map.get("oldMemberType");
					String[] beforeMemberType = beforeMemberTypes.split(",");
					if (uPlayerRoles.size() > beforeMemberType.length) {
						return WebPublicMehod.returnRet("error", "当前球队成员有将角色转让给你\n请先查看再进行修改球员信息");
					}
					
				}
				//先循环将所有已存在的角色作为标记- 99
				this.updateMemberTypeStatusSet99(uPlayerRoles);
				//循环传过来的角色,将存在的角色设置为使用中，不存在的角色新增到角色表
				this.setNewMemberTypeForEditplayer(map,updatePlayer);
				//最后查出所有该球员标记的角色
				List<UPlayerRole> uPlayerRoles99 = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='99' order by memberType+0 asc");
				if (null != uPlayerRoles99 && uPlayerRoles99.size() > 0) {
					for (UPlayerRole uPlayerRole : uPlayerRoles99) {
						map.put("name", "member_type");
						map.put("params",uPlayerRole.getMemberType());
						UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							return WebPublicMehod.returnRet("error", "你的管理角色只可转让\n不能修改");
						}
						this.updateMemberTypeStatus(uPlayerRole);
					}
				}
				//删除他为无角色的记录
				this.deleteNoMemberType(map);
			}else{//无角色
				//检测角色是否被别人选择了,如没有，则更新到角色表
				this.checkMemberTypeIsOther(updatePlayer,map);
			}
		}
		
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 删除他为无角色的记录
	   @param map
	   		playerId		球员Id
	   @throws Exception
	   2016年7月14日
	   dengqiuru
	 */
	private void deleteNoMemberType(HashMap<String, String> map) throws Exception {
		//查询当前球员为无角色的角色记录
		List<UPlayerRole> uPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='1' and memberType='11' order by memberType+0 asc");
		if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
			for (UPlayerRole uPlayerRole : uPlayerRoles) {
				this.updateMemberTypeStatus(uPlayerRole);
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 循环传过来的角色,将存在的角色设置为使用中，不存在的角色新增到角色表
	   @param map
	   		memberType		
	   @param updatePlayer
	   @return
	   @throws Exception
	   2016年6月14日
	   dengqiuru
	 */
	private HashMap<String, Object> setNewMemberTypeForEditplayer(HashMap<String, String> map, UPlayer updatePlayer) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, String> hashMap = new HashMap<>();
		//获取传过来的角色参数
		String memberTypes = map.get("memberType");
		String[]  memberType = memberTypes.split(",");
		for (String member : memberType) {
			//判断该用户是否存在该角色
			map.put("membertype", member);
			UPlayerRole uPlayerRole = baseDAO.get(map, "from UPlayerRole where player_id=:playerId and memberType=:membertype and memberTypeUseStatus='99'");
			if (null != uPlayerRole) {//存在:就将已有角色变为使用中
				uPlayerRole.setMemberTypeUseStatus("1");
				baseDAO.update(uPlayerRole);
			}else {//不存在:就在角色表里面新增一条记录
				//查询码表对象，并查看该角色是否唯一
				hashMap.put("name", "member_type");
				hashMap.put("params",member);
				hashMap.put("memberType", member);
				UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
				if (null != uParameterInfo) {
					//判断该角色是否唯一  是
					if (!"3".equals(uParameterInfo.getIsUnique())) {
						if (null != updatePlayer.getUTeam()) {
							map.put("teamId", updatePlayer.getUTeam().getTeamId());
							//判断该角色是否被其他人选了
							Boolean isOtherSelected = this.isOtherSelected202(map);
							//被别人选了，报错
							if (true == isOtherSelected) {
								return WebPublicMehod.returnRet("error", "-3_你选择的角色“"+uParameterInfo.getName()+"”已被别人选择了");
							}
						}
					}
				}
				this.createNewPlayRole202(updatePlayer, hashMap);
			}
		}
		return resultMap;
		
	}


	/**
	 * 
	 * 
	   TODO - 将该球员所有有效的角色做标记
	   @param uPlayerRole
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	private void updateMemberTypeStatusSet99(List<UPlayerRole> uPlayerRoles) throws Exception {
		for (UPlayerRole uPlayerRole : uPlayerRoles) {
			uPlayerRole.setMemberTypeUseStatus("99");//99：标记
			baseDAO.update(uPlayerRole);
		}
	}


	/**
	 * 
	 * 
	   TODO - 修改角色时，将角色变为已废弃
	   @param uPlayerRole
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	private void updateMemberTypeStatus(UPlayerRole uPlayerRole) throws Exception {
		uPlayerRole.setChangeDate(new Date());
		uPlayerRole.setMemberTypeUseStatus("2");//2：已废弃
		baseDAO.update(uPlayerRole);
	}


	/**
	 * 
	 * 
	   TODO - 检测角色是否被别人选择了,如没有，则更新到角色表
	   @param updatePlayer		对象
	   @param map
	   		memberType			角色
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	private HashMap<String, Object> checkMemberTypeIsOther(UPlayer updatePlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		String ret = "1";//1:没有被人选择
		if (null != updatePlayer) {
			map.put("playerId", updatePlayer.getPlayerId());
			//获取传过来的角色参数
			String memberTypes = map.get("memberType");
			if (publicService.StringUtil(memberTypes)) {
				//以数组分开
				String[]  memberType = memberTypes.split(",");
				//循环遍历所有角色
				for (String membertype : memberType) {
					//查询码表对象，并查看该角色是否唯一
					map.put("name", "member_type");
					map.put("params",membertype);
					map.put("memberType", membertype);
					UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
					if (null != uParameterInfo) {
						//判断该角色是否唯一  是
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							if (null != updatePlayer.getUTeam()) {
								map.put("teamId", updatePlayer.getUTeam().getTeamId());
								//判断该角色是否被其他人选了
								Boolean isOtherSelected = this.isOtherSelected202(map);
								//被别人选了，报错
								if (true == isOtherSelected) {
									if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
										return WebPublicMehod.returnRet("error", "你选择的角色“"+uParameterInfo.getName()+"”已被别人选择了");
									}else{
										return WebPublicMehod.returnRet("error", "-3_你选择的角色“"+uParameterInfo.getName()+"”已被别人选择了");
									}
								}
							}
						}
						//判断他是否已经存在该角色
						Boolean IsThisMemberType = this.IsThisMemberType(updatePlayer, map);
						//将角色填充到角色表
						if (false == IsThisMemberType) {
							this.createNewPlayRole202(updatePlayer, map);
						}
					}
				}
			}
		}
		hashMap.put("ret", ret);
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 判断该球员是否存在该球队
	   @param updatePlayer
	   @param map
	   2016年7月1日
	   dengqiuru
	 * @throws Exception 
	 */
	private Boolean IsThisMemberType(UPlayer updatePlayer, HashMap<String, String> map) throws Exception {
		Boolean IsThisMemberType = false;
		map.put("uplayerId", updatePlayer.getPlayerId());
		List<UPlayerRole> uPlayerRoles = baseDAO.find(map, "from UPlayerRole where UPlayer.playerId=:uplayerId and memberTypeUseStatus='1'");
		if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
			for (UPlayerRole uPlayerRole : uPlayerRoles) {
				if (null != uPlayerRole) {
					if (publicService.StringUtil(uPlayerRole.getMemberType())) {
						if (map.get("memberType").equals(uPlayerRole.getMemberType())) {
							IsThisMemberType = true;
						}
					}
				}
			}
		}
		return IsThisMemberType;
	}

	/**
	 * 
	 * 
	   TODO - 创建球队时，队长新增一条角色记录
	   @param uPlayer	对象
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void createNewMembertypeByInsertNewTeam(UPlayer uPlayer) throws Exception {
		UPlayerRole uPlayerRole = new UPlayerRole();
		uPlayerRole.setKeyId(WebPublicMehod.getUUID());
		uPlayerRole.setMemberType("1");
		uPlayerRole.setUPlayer(uPlayer);
		uPlayerRole.setChangeDate(new Date());
		uPlayerRole.setMemberTypeUseStatus("1");
		baseDAO.save(uPlayerRole);
		
	}

	/**
	 * 
	 * 
	   TODO - 查看球队信息时，填充球队球员角色
	   @param outUteamList		对象
	   @param map		
	   		loginUserId			当前用户
	   @return
	   2016年6月15日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public OutUteamList setMemberTypeByGetUTeaminfo202(OutUteamList outUteamList, HashMap<String, String> map) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//转让者map
		map.put("teamId", outUteamList.getTeamId());
		String type = "1";
		UPlayer uPlayer = uPlayerService.getUteamUplayerinfoByTeamId(map, type);
		if (null != uPlayer) {
			hashMap.put("playerId", uPlayer.getPlayerId());
			//查询当前球员的角色
//			List<UPlayerRole> UPlayerRoles = this.getMemberTypeByPlayerId202(hashMap);
			List<HashMap<String, Object>> UPlayerRoles = this.getMemberTypeByPlayerIdOrderByReankWeight202(hashMap);
			//第一个身份
			String memberType = null;
			//第一个身份对应的名称
			String membertypeName = null;
			//第二个身份
			String memberType2 = null;
			//第二个身份对应的名称
			String membertypeName2 = null;
			//当前球员角色不为空
			if (null != UPlayerRoles && UPlayerRoles.size() > 0) {
				memberType = UPlayerRoles.get(0).get("memberType").toString();
				if (!"11".equals(UPlayerRoles.get(0).get("memberType").toString())) {
					membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType2 = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName2 = Public_Cache.HASH_PARAMS("member_type").get(memberType2);
						}
					}
				}else{//第一个角色为无角色
					//第二个角色
					if (UPlayerRoles.size() > 1) {
						memberType = UPlayerRoles.get(1).get("memberType").toString();
						if (!"11".equals(UPlayerRoles.get(1).get("memberType").toString())) {
							membertypeName = Public_Cache.HASH_PARAMS("member_type").get(memberType);
						}
					}
				}
			}
			outUteamList.setMemberType(memberType);
			outUteamList.setMemberTypeName(membertypeName);
			outUteamList.setMemberType2(memberType2);
			outUteamList.setMemberTypeName2(membertypeName2);
		}
		return outUteamList;
	}

	/**
	 * 
	 * 
	   TODO - 查询当前用户是否存在管理角色
	   @param uPlayer
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public Boolean isUniqueByuserId(UPlayer uPlayer) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		Boolean isUnique = false;
		hashMap.put("playerId", uPlayer.getPlayerId());
		List<UPlayerRole> uPlayerRoles = baseDAO.find(hashMap, "from UPlayerRole where UPlayer.playerId=:playerId and memberTypeUseStatus='1'");
		if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
			for (UPlayerRole uPlayerRole : uPlayerRoles) {
				//查询码表对象，并查看该角色是否唯一
				hashMap.put("name", "member_type");
				hashMap.put("params",uPlayerRole.getMemberType());
				UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
				if (null != uParameterInfo) {
					if (publicService.StringUtil(uParameterInfo.getIsUnique())) {
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							isUnique = true;
							break;
						}
					}
				}
			}
		}
		return isUnique;
	}
	
	@Override
	public Boolean isUniqueByuserId(HashMap<String, String> map) throws Exception {
		Boolean isUnique = false;
		List<UPlayerRole> uPlayerRoles = baseDAO.find(map, "from UPlayerRole where UPlayer.playerId=:playerId and memberTypeUseStatus='1'");
		if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
			for (UPlayerRole uPlayerRole : uPlayerRoles) {
				//查询码表对象，并查看该角色是否唯一
				map.put("name", "member_type");
				map.put("params",uPlayerRole.getMemberType());
				UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
				if (null != uParameterInfo) {
					if (publicService.StringUtil(uParameterInfo.getIsUnique())) {
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							isUnique = true;
							break;
						}
					}
				}
			}
		}
		return isUnique;
	}

	/**
	 * 
	 * 
	   TODO - 填充当前球队为球员的身份
	   @param hashMap2
	   		playerId		球员Id
	   2016年6月25日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void setMembertypeByplayer202(HashMap<String, Object> hashMap2) throws Exception {
		if (null != hashMap2) {
			//查询playerId是否为空
			if (null != hashMap2.get("playerId")) {
				//根据球员去他为球员的身份记录
				List<HashMap<String, Object>> uPlayerRoles = this.getMembertypeByplayerOrderByRank202(hashMap2);
				String memberTypeName1 = null;
				String memberTypeName2 = null;
				hashMap2.put("memberType1", null);//xiao
				hashMap2.put("memberType2", null);//xiao
				String isCaptain = "2";//是否为队长
				if (null != uPlayerRoles) {
					if (uPlayerRoles.size()>0) {
						if (!"11".equals(uPlayerRoles.get(0).get("memberType").toString())) {
							memberTypeName1 = Public_Cache.HASH_PARAMS("member_type").get(uPlayerRoles.get(0).get("memberType").toString());
							hashMap2.put("memberType1", uPlayerRoles.get(0).get("memberType"));//xiao
							if ("1".equals(uPlayerRoles.get(0).get("memberType").toString())) {
								isCaptain = "1";
							}
						}
						if (uPlayerRoles.size() >= 2) {
							if (!"11".equals(uPlayerRoles.get(1).get("memberType").toString())) {
								memberTypeName2 = Public_Cache.HASH_PARAMS("member_type").get(uPlayerRoles.get(1).get("memberType").toString());
								hashMap2.put("memberType2", uPlayerRoles.get(1).get("memberType"));//xiao
								if ("1".equals(uPlayerRoles.get(1).get("memberType").toString())) {
									isCaptain = "1";
								}
							}
						}
					}
				}
				hashMap2.put("memberTypeName1", memberTypeName1);
				hashMap2.put("memberTypeName2", memberTypeName2);
				hashMap2.put("isCaptain", isCaptain);
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 队内成员列表显示角色排序（球员）
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getMembertypeByplayerOrderByRank202(HashMap<String, Object> map) throws Exception {
		List<HashMap<String, Object>> uPlayerRoles = null;
		StringBuffer newsql = new StringBuffer("select r.member_type memberType,r.player_id,i.rank_weight from u_player_role r "
				+" LEFT JOIN u_parameter_info i on r.member_type=i.params and i.pkey_id in (select pkey_id from u_parameter p where p.params='member_type') "
				+" where r.member_type_use_status='1' and r.player_id=:playerId and r.member_type in ('1','2','9','11')" 
				+" ORDER BY i.rank_weight ASC");
		uPlayerRoles = baseDAO.findSQLMap(newsql.toString(),map);
		return uPlayerRoles;
	}
	/**
	 * 
	 * 
	   TODO - 填充当前球队为官员的身份
	   @param hashMap2
	   		playerId		球员Id
	   2016年6月25日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void setMembertypeByManager202(HashMap<String, Object> hashMap2) throws Exception {
		if (null != hashMap2) {
			//查询playerId是否为空
			if (null != hashMap2.get("playerId")) {
				//根据球员去他为球员的身份记录
//				List<UPlayerRole> uPlayerRoles = baseDAO.find("from UPlayerRole where UPlayer.playerId=:playerId and memberType in ('3','4','7','8','10','12','13','14') and memberTypeUseStatus='1' order by memberType+0", hashMap2);
				List<HashMap<String, Object>> uPlayerRoles = this.getMembertypeByManagerOrderByRank202(hashMap2);
				String memberTypeName1 = null;
				String memberTypeNameUrl1 = null;
				String memberTypeName2 = null;
				String memberTypeNameUrl2 = null;
				hashMap2.put("memberType1", null);//xiao
				hashMap2.put("memberType2", null);//xiao
				if (null != uPlayerRoles) {
					if (uPlayerRoles.size()>0) {
						if (!"11".equals(uPlayerRoles.get(0).get("memberType").toString())) {
							HashMap<String, String> memberTypeName1Map = new HashMap<>();
							memberTypeName1 = Public_Cache.HASH_PARAMS("member_type").get(uPlayerRoles.get(0).get("memberType").toString());
							hashMap2.put("memberType1", uPlayerRoles.get(0).get("memberType"));//xiao
							//查询码表对象，并查看该角色是否唯一
							//查询身份是否是唯一，可转让的
							memberTypeName1Map.put("name", "member_type");
							memberTypeName1Map.put("params",uPlayerRoles.get(0).get("memberType").toString());
							UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(memberTypeName1Map);
							if (null != uParameterInfo) {
								memberTypeNameUrl1 = uParameterInfo.getImgurl();
							}
							if (uPlayerRoles.size() >= 2) {
								if (!"11".equals(uPlayerRoles.get(1).get("memberType").toString())) {
									HashMap<String, String> memberTypeName2Map = new HashMap<>();
									memberTypeName2 = Public_Cache.HASH_PARAMS("member_type").get(uPlayerRoles.get(1).get("memberType").toString());
									hashMap2.put("memberType2", uPlayerRoles.get(1).get("memberType"));//xiao
									//查询码表对象，并查看该角色是否唯一
									//查询身份是否是唯一，可转让的
									memberTypeName2Map.put("name", "member_type");
									memberTypeName2Map.put("params",uPlayerRoles.get(1).get("memberType").toString());
									UParameterInfo uParameterInfo1 = uParameterService.getMemberTypeByTeamId202(memberTypeName2Map);
									if (null != uParameterInfo) {
										memberTypeNameUrl2 = uParameterInfo1.getImgurl();
									}
								}
							}
						}else{
							//超过了三个角色
							if (uPlayerRoles.size() >= 2) {
								if (!"11".equals(uPlayerRoles.get(1).get("memberType").toString())) {
									HashMap<String, String> memberTypeName1Map = new HashMap<>();
									memberTypeName1 = Public_Cache.HASH_PARAMS("member_type").get(uPlayerRoles.get(1).get("memberType").toString());
									hashMap2.put("memberType1", uPlayerRoles.get(0).get("memberType"));//xiao
									//查询码表对象，并查看该角色是否唯一
									//查询身份是否是唯一，可转让的
									memberTypeName1Map.put("name", "member_type");
									memberTypeName1Map.put("params",uPlayerRoles.get(1).get("memberType").toString());
									UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(memberTypeName1Map);
									if (null != uParameterInfo) {
										memberTypeNameUrl1 = uParameterInfo.getImgurl();
									}
								}
							}
						}
					}
				}
				hashMap2.put("memberTypeName1", memberTypeName1);
				hashMap2.put("memberTypeName2", memberTypeName2);
				hashMap2.put("memberTypeNameUrl1", memberTypeNameUrl1);
				hashMap2.put("memberTypeNameUrl2", memberTypeNameUrl2);
			}
		}
	}
	

	/**
	 * 
	 * 
	   TODO - 队内成员列表显示角色排序（球员）
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getMembertypeByManagerOrderByRank202(HashMap<String, Object> map) throws Exception {
		List<HashMap<String, Object>> uPlayerRoles = null;
		StringBuffer newsql = new StringBuffer("select r.member_type memberType,r.player_id,i.rank_weight from u_player_role r "
				+" LEFT JOIN u_parameter_info i on r.member_type=i.params and i.pkey_id in (select pkey_id from u_parameter p where p.params='member_type') "
				+" where r.member_type_use_status='1' and r.player_id=:playerId and r.member_type in ('3','4','5','6','7','8','10','12','13','14')" 
				+" ORDER BY i.rank_weight ASC");
		uPlayerRoles = baseDAO.findSQLMap(newsql.toString(),map);
		return uPlayerRoles;
	}
	/**
	 * 
	 * 
	   TODO - 修改球员信息时，如果参数角色不为空，就更新角色表(第三方系统)
	   @param map
	   		memberType		角色
	   @param updatePlayer		对象
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> setEventsMemberTypeByEditplayer202(HashMap<String, String> map, UPlayer updatePlayer) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		if (null != updatePlayer) {
			map.put("playerId", updatePlayer.getPlayerId());
			//查询该球员有没有角色
			List<UPlayerRole> uPlayerRoles = baseDAO.find(map, "from UPlayerRole where player_id=:playerId and memberTypeUseStatus='1' and memberType<>'11' order by memberType+0 asc");
			//有角色
			if (null == uPlayerRoles ) {
				//检测角色是否被别人选择了,如没有，则更新到角色表
				this.checkEventsMemberTypeIsOther(updatePlayer,map);
			}
		}
		
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 检测角色是否被别人选择了,如没有，则更新到角色表（第三方系统）
	   @param updatePlayer		对象
	   @param map
	   		memberType			角色
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	private HashMap<String, Object> checkEventsMemberTypeIsOther(UPlayer updatePlayer, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		String ret = "1";//1:没有被人选择
		if (null != updatePlayer) {
			map.put("playerId", updatePlayer.getPlayerId());
			//获取传过来的角色参数
			String memberTypes = map.get("memberType");
			if (publicService.StringUtil(memberTypes)) {
				//以数组分开
				String[]  memberType = memberTypes.split(",");
				//循环遍历所有角色
				for (String membertype : memberType) {
					//查询码表对象，并查看该角色是否唯一
					map.put("name", "member_type");
					map.put("params",membertype);
					map.put("memberType", membertype);
					UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
					if (null != uParameterInfo) {
						//判断该角色是否唯一  是
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							if (null != updatePlayer.getUTeam()) {
								map.put("teamId", updatePlayer.getUTeam().getTeamId());
								//判断该角色是否被其他人选了
								Boolean isOtherSelected = this.isOtherSelected202(map);
								//被别人选了，报错
								if (true == isOtherSelected) {
									return WebPublicMehod.returnRet("error", "你选择的角色“"+uParameterInfo.getName()+"”已被别人选择了");
								}
							}
						}
						//判断他是否已经存在该角色
						Boolean IsThisMemberType = this.IsThisMemberType(updatePlayer, map);
						//将角色填充到角色表
						if (false == IsThisMemberType) {
							this.createNewPlayRole202(updatePlayer, map);
						}
					}
				}
			}
		}
		hashMap.put("ret", ret);
		return hashMap;
	}

	@Override
	public HashMap<String, Object> editMemberTypeAllot(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("playerId", map.get("playerId"));
		List<UPlayerApply> listPlayerApply=null;
		UPlayerRole playerRole=null;
		UParameterInfo uParameterInfo = null;
		map.put("name", "member_type");
		List<UPlayerRole> playerRolesList=null;//
		HashMap<String,List<Object>> mapList=new HashMap<>();
		List<Object> listStr = new ArrayList<>();
		UPlayerApply playerApply=null;
		boolean flag=true;
		String memberTypeName="";
		UPlayer uPlayerTemp1=null;
		// 首先球队Id不能为空
		if (publicService.StringUtil(map.get("teamId"))) {
			if(publicService.StringUtil(map.get("oldMemberType"))){
				String[] strs = map.get("oldMemberType").split(",");
				for (int i = 0; i < strs.length; i++) {
					listStr.add(strs[i]);
				}
				map.remove("oldMemberType");
				mapList.put("oldMemberType", listStr);
				playerRolesList = baseDAO.find(map,
						"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  UPlayer.playerId=:playerId and memberType not in (:oldMemberType) and memberTypeUseStatus='1' and UPlayer.inTeam='1' and memberType !='11'",mapList);
				if((playerRolesList!=null&&playerRolesList.size()>=1)){
					return WebPublicMehod.returnRet("error", "该球员正在被其他人管理，请稍后再！");
				}
			}else{
				playerRolesList = baseDAO.find(map,
						"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  UPlayer.playerId=:playerId and  memberTypeUseStatus='1' and UPlayer.inTeam='1' and memberType !='11'");
				if((playerRolesList!=null&&playerRolesList.size()>=1)){
					return WebPublicMehod.returnRet("error", "该球员正在被其他人管理，请稍后再试");
				}
			}
			
			List<UPlayerRole> listPlayerRoles=baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and UPlayer.playerId=:playerId and memberTypeUseStatus='1' and UPlayer.inTeam='1'");
			for (UPlayerRole uPlayerRole : listPlayerRoles) {
				map.put("memberType", uPlayerRole.getMemberType());
				map.put("params", map.get("memberType"));
				uParameterInfo=uParameterService.getMemberTypeByTeamId202(map);
				// 如果是唯一角色
				if (uParameterInfo != null && !"1".equals(uParameterInfo.getIsUnique())) {
					uPlayerRole.setMemberTypeUseStatus("2");
					baseDAO.update(uPlayerRole);
				}
			}
			// 查询分配置者是否是这个球队的
			String type = "1";
			UPlayer uPlayer2 = uPlayerService.getUteamUplayerinfoByTeamId(map, type);
			if (null != uPlayer2) {
				// 查询被转让者是否跟我是同一球队
				if (publicService.StringUtil(map.get("playerId"))) {
					// 查询球员信息
					uPlayerTemp1 = baseDAO.get(map, "from UPlayer where playerId=:playerId and inTeam='1'");
					if (null != uPlayerTemp1 && null != uPlayerTemp1.getUTeam()) {
						if (map.get("teamId").equals(uPlayerTemp1.getUTeam().getTeamId())) {
							if (map.get("memberTypes") != null && !"".equals("memberType")) {
								String[] memberTypes = map.get("memberTypes").split(",");
								if (memberTypes != null && memberTypes.length > 0) {
									for (String memberType : memberTypes) {
										map.put("memberType", memberType);
										map.put("params", map.get("memberType"));
										uParameterInfo=uParameterService.getMemberTypeByTeamId202(map);
										//如果是唯一角色
										if(uParameterInfo!=null&&uParameterInfo.getIsUnique()!=null&&"1".equals(uParameterInfo.getIsUnique())){
											playerRolesList=baseDAO.find(map,"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and UPlayer.inTeam='1' and memberTypeUseStatus='1' and UPlayer.playerId!=:playerId");
											if(mapList.size()>0){
												playerRolesList=baseDAO.find(map,"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and memberType not in (:oldMemberType) and UPlayer.inTeam='1' and memberTypeUseStatus='1' ",mapList);
											}
											if(playerRolesList!=null&&playerRolesList.size()>0){
												return WebPublicMehod.returnRet("error", "“"+uParameterInfo.getName()+"”已被占，请重新分配");
											}
										}
										// 重复的
										if (listStr != null) {
											for (int j = 0; j < listStr.size(); j++) {
												if (memberType != null && memberType.equals(listStr.get(j))) {
													playerRole = baseDAO.get(map,
															"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and UPlayer.playerId=:playerId and memberTypeUseStatus='2'");
													if (playerRole != null) {
														playerRole.setMemberTypeUseStatus("1");
														baseDAO.update(playerRole);
													}
													flag = false;
													break;
												}
											}
										}
										if (flag) {
											map.put("memberType", memberType);
											map.put("bplayerid", uPlayer2.getPlayerId());// 分配中球员ID
											// 添加被分配者的角色
											this.createNewPlayRole202(uPlayerTemp1, map);
											// 更改申请者状态
											map.put("memberTypeName", uParameterInfo != null?uParameterInfo.getName():"");// 通知用
											this.judgePlayerApply(map, playerApply, listPlayerApply);
											if(map.get("isApplyPush")!=null&&"2".equals(map.get("isApplyPush"))){
												memberTypeName+=map.get("memberTypeName")+"、";
											}
										}
										flag=true;
									}
									//分配通知
									if(memberTypeName!=null&&memberTypeName.length()>0){
										memberTypeName=memberTypeName.substring(0, memberTypeName.length()-1);
										if(uPlayerTemp1!=null){
											// 通知
											map.put("memberTypeName", memberTypeName);// 通知用
											this.applyPush(map, uPlayerTemp1.getUTeam(), uPlayerTemp1, uPlayerTemp1.getUUser()!=null?uPlayerTemp1.getUUser().getUserId():"", "tmApplyAllot","b01");
										}
									}
								}
							}else{
								return WebPublicMehod.returnRet("error", "请选择角色");
							}
						} else {
							return WebPublicMehod.returnRet("error", "当前球员跟你不属于同一个球队");
						}
					}else{
						return WebPublicMehod.returnRet("error", "你所选的成员已经离队，无法继续操作");
					}
				} else {
					return WebPublicMehod.returnRet("error", "请求参数playerId为空");
				}
			} else {
				return WebPublicMehod.returnRet("error", "你还未加入当前球队");
			}
		} else {
			return WebPublicMehod.returnRet("error", "缺少请求参数");
		}
		return WebPublicMehod.returnRet("sueccess", "分配成功！");
	}
	/**
	 * 
	 * TODO 球员管理分配限制
	 * @param map
	 * @param playerRolesLists
	 * @param uPlayer2
	 * @param uPlayerTemp1
	 * @param listStr
	 * @param playerRolesList
	 * @param mapList
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月25日
	 */
	private HashMap<String, Object> editCheckAllot(HashMap<String, String> map, List<UPlayerRole> playerRolesLists, UPlayer uPlayer2, UPlayer uPlayerTemp1, List<Object> listStr, List<UPlayerRole> playerRolesList, HashMap<String, List<Object>> mapList) throws Exception{
		// 首先球队Id不能为空
		if (publicService.StringUtil(map.get("teamId")) && publicService.StringUtil(map.get("playerId"))) {
			return WebPublicMehod.returnRet("error", "缺少请求参数！");
		}
		if (publicService.StringUtil(map.get("oldMemberType"))) {
			String[] strs = map.get("oldMemberType").split(",");
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			map.remove("oldMemberType");
			mapList.put("oldMemberType", listStr);
			playerRolesLists = baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  UPlayer.playerId=:playerId and memberType not in (:oldMemberType) and memberTypeUseStatus='1' and UPlayer.inTeam='1' and memberType !='11'",
					mapList);
			if ((playerRolesLists != null && playerRolesLists.size() >= 1)) {
				return WebPublicMehod.returnRet("error", "该球员正在被其他人管理，请稍后再试！");
			}
		} else {
			if ((playerRolesList != null && playerRolesList.size() >= 1)) {
				return WebPublicMehod.returnRet("error", "该球员正在被其他人管理，请稍后再试！");
			}
		}
		if (null == uPlayer2) {
			return WebPublicMehod.returnRet("error", "你所选的成员已经离队，无法继续操作");
		}
		if (null != uPlayerTemp1 && null != uPlayerTemp1.getUTeam()) {
			if (!map.get("teamId").equals(uPlayerTemp1.getUTeam().getTeamId())) {
				return WebPublicMehod.returnRet("error", "当前球员跟你不属于同一个球队");
			}
		} else {
			return WebPublicMehod.returnRet("error", "你还未加入当前球队");
		}
		return null;
	}


	@Override
	public HashMap<String, Object> checkApplyPlayerMemberType(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap=new HashMap<>();
		List<UPlayerApply> playerApplyList=null;//所有申请这个球队的这个身份的球员
		UPlayerRole playerRole=null;//获取球队中唯一
		UParameterInfo uParameterInfo = null;//
		map.put("name", "member_type");
		map.put("params", map.get("memberType"));
		List<UPlayerRole> playerRolesList=null;//
		String realname="";
		// 首先球队Id不能为空
		if (publicService.StringUtil(map.get("teamId"))) {
			if(map.get("userId")==null||"".equals("userId")||"1".equals(map.get("act"))){
				map.put("userId", map.get("loginUserId"));
			}
			playerRolesList = baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  UPlayer.UUser.userId=:userId  and memberTypeUseStatus='1' and memberType !='11' ");
			if((playerRolesList!=null&&playerRolesList.size()<2)||playerRolesList==null||"3".equals(map.get("act"))){
				if(playerRolesList!=null){
					for (UPlayerRole uPlayerRole : playerRolesList) {
						if(!"1".equals(uPlayerRole.getUPlayer().getInTeam())&&!"3".equals(map.get("act"))){
							return WebPublicMehod.returnRet("error", "你所选的球员已经离队，无法继续操作");
						}
					}
				}
				uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
				// 如果是唯一角色
				if (uParameterInfo != null && uParameterInfo.getIsUnique() != null
						&& "1".equals(uParameterInfo.getIsUnique())) {
					//申请－弹出提示
					if("1".equals(map.get("act"))){
						playerApplyList=baseDAO.find(map,"from UPlayerApply where applyStatus='1' and userId=:loginUserId  and UTeam.teamId=:teamId and memberType=:memberType and memberType !='11' and UPlayer.inTeam='1'");
						if(playerApplyList==null||(playerApplyList!=null&&playerApplyList.size()==0)){
							playerRole = baseDAO.get(map,
									"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  memberType=:memberType and memberTypeUseStatus='1' and memberType !='11' and UPlayer.inTeam='1'");
							// 唯一角色有人占领，只向占有人发出申请，并通知，否者向所有一级发出申请，并通知所有一级人员
							if (playerRole != null) {
								//转让人名称
								if(playerRole.getUPlayer().getUUser()!=null){
									if(playerRole.getUPlayer().getUUser().getRealname()!=null&&!"".equals(playerRole.getUPlayer().getUUser().getRealname())){
										realname=playerRole.getUPlayer().getUUser().getRealname();
									}else{
										if(playerRole.getUPlayer().getUUser().getNickname()!=null&&!"".equals(playerRole.getUPlayer().getUUser().getNickname())){
											realname=playerRole.getUPlayer().getUUser().getNickname();
										}
									}
								}
								hashMap.put("success", uParameterInfo.getName()+"已属于"+realname+"，\n你可申请该职位\n成功后将拥有更多球队操作权限");
							} else {
								//等出文本－－未能确定
								hashMap.put("success", uParameterInfo.getName()+"还不属于任何人，\n你可申请该职位\n成功后将拥有更多球队操作权限");
							}
							hashMap.put("code", "1");
						}else{
							hashMap.put("success", "你已提交过职位申请\n在上次申请处理完毕前无法重复提交");
							hashMap.put("code", "5");
						}
						
					}
					//申请人名称
					UUser user = baseDAO.getHRedis(UUser.class,map.get("userId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
					if(user!=null){
						if(user.getRealname()!=null&&!"".equals(user.getRealname())){
							realname=user.getRealname();
						}else{
							if(user.getNickname()!=null&&!"".equals(user.getNickname())){
								realname=user.getNickname();
							}
						}
					}
					//申请审核－同意－弹出提示
					if("2".equals(map.get("act"))){
						hashMap.put("success", "你确认同意"+uParameterInfo.getName()+"角色给"+realname+"吗?");
						hashMap.put("code", "2");
					}
					//申请审核－转让－弹出提示
					if("4".equals(map.get("act"))){
						hashMap.put("success", "你确认将"+uParameterInfo.getName()+"角色转让给"+realname+"吗?这样的话你会失去很多权限");
						hashMap.put("code", "4");
					}
					//申请审核－拒绝－弹出提示
					if("3".equals(map.get("act"))){
						hashMap.put("success", "你真的要拒绝"+realname+"的任职申请吗？");
						hashMap.put("code", "3");
					}
					
					return hashMap;
				} 
				return WebPublicMehod.returnRet("teamId", map.get("teamId"));
			}
			if("1".equals(map.get("act"))){
				return WebPublicMehod.returnRet("error", "最多拥有两个角色，您不能申请角色");
			}
			if("2".equals(map.get("act"))){
				return WebPublicMehod.returnRet("error", "对方已经拥有两个角色，您不能将角色给对方");
			}
			if("4".equals(map.get("act"))){
				return WebPublicMehod.returnRet("error", "对方已经拥有两个角色，您不能将角色转让出去");
			}
		}
		return WebPublicMehod.returnRet("error", "请求参数teamId为空");
	}
	@Override
	public HashMap<String, Object> applyPlayerMemberType(HashMap<String, String> map) throws Exception {
		UPlayerRole playerRole=null;//获取球队中唯一
		UPlayerApply playerApply=null;//
		UParameterInfo uParameterInfo = null;//
		map.put("name", "member_type");
		map.put("params", map.get("memberType"));
		List<UPlayerRole> playerRolesList=null;//
		String memberType=Public_Cache.MEMBER_TYPE_LVL1;//默认一级申请，向一级发通知
		// 首先球队Id不能为空
		if (publicService.StringUtil(map.get("teamId"))&&publicService.StringUtil(map.get("memberType"))) {
			UTeam team=baseDAO.get(UTeam.class,map.get("teamId"));
			UPlayer player =baseDAO.get(map,"from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1' ");
			playerRolesList = baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  UPlayer.UUser.userId=:loginUserId  and memberTypeUseStatus='1' and memberType !='11' and UPlayer.inTeam='1'");
			if((playerRolesList!=null&&playerRolesList.size()<2)||playerRolesList==null){
				uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
				// 如果是唯一角色
				if (uParameterInfo != null && uParameterInfo.getIsUnique() != null
						&& "1".equals(uParameterInfo.getIsUnique())) {
					Integer lvl=uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(map.get("memberType")));
					map.put("memberTypeName",uParameterInfo.getName());//通知用
					this.applyMemberTypePlayer(map, playerRole, lvl, playerRolesList, playerApply, player, team, memberType);
				} 
				return WebPublicMehod.returnRet("success", "你已经申请“"+uParameterInfo.getName()+"”职位\n请等待处理");
			}
			return WebPublicMehod.returnRet("error", "最多拥有两个角色，您不能申请角色");
		}
		return WebPublicMehod.returnRet("error", "请求参数为空！");
	}
	@Override
	public HashMap<String, Object> applyCheckAgreePlayerMemberType(HashMap<String, String> map) throws Exception {
		List<UPlayerRole> playerRolesList=null;//
		UPlayer player=baseDAO.get(map,"from UPlayer where UTeam.teamId=:teamId and UUser.userId=:loginUserId ");
		UPlayerRole playerRole=baseDAO.get(map,"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  memberType=:memberType and UPlayer.UUser.userId=:loginUserId and memberType !='11' and UPlayer.inTeam='1'");//申请的角色
		UPlayerApply playerApply=baseDAO.get(map,"from UPlayerApply where applyStatus='1' and keyId=:keyId ");//
		List<UPlayerApply> playerApplyList=null;//所有申请这个球队的这个身份的球员
		if(playerApply!=null){
			map.put("name", "member_type");
			map.put("params",playerApply.getMemberType());
			UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
			map.put("memberTypeName",uParameterInfo!=null?uParameterInfo.getName():"");//通知用
			if(playerApply.getUPlayer()!=null&&playerApply.getUPlayer().getInTeam()!=null&&!"1".equals(playerApply.getUPlayer().getInTeam())){
				return WebPublicMehod.returnRet("error", "你所选的成员已经离队，无法继续操作");
			}
			map.put("userId", playerApply.getUserId());
			playerRolesList = baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and UPlayer.UUser.userId=:userId  and memberTypeUseStatus='1' and memberType !='11' and UPlayer.inTeam='1'");
			if((playerRolesList!=null&&playerRolesList.size()<2)||playerRolesList==null){
				playerRolesList=baseDAO.get(map,"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  memberType=:memberType and UPlayer.UUser.userId!=:loginUserId and memberType !='11' and UPlayer.inTeam='1'");//申请的角色
				if(playerRolesList!=null&&playerRolesList.size()>0){
					return WebPublicMehod.returnRet("error", "该职位已被其他人占有");
				}
				if(playerRole!=null){
					playerRole.setMemberTypeUseStatus("2");
					baseDAO.update(playerRole);
					uPlayerRoleLogService.createNewLog202(map,player);
				}
				// 添加角色
				this.createNewPlayRole202(playerApply.getUPlayer(), map);
				playerApply.setApplyStatus("2");
				playerApply.setAgreedate(new Date());
				playerApply.setBplayerId(player.getPlayerId());
				playerApply.setBuserId(map.get("loginUserId"));
				baseDAO.update(playerApply);
				if(playerRole!=null){//转让通知
					String messageType = "1";//角色转让通知
					this.setMessageParams202(messageType,player,playerApply.getUPlayer(),map);
				}else{//审批通知-同意通知
					if(playerApply.getUPlayer()!=null){
						// 通知
						this.applyPush(map, playerApply.getUTeam(), playerApply.getUPlayer(), playerApply.getUPlayer().getUUser()!=null?playerApply.getUPlayer().getUUser().getUserId():"", "tmApplyAgree","b01");
					}
				}
				//拒绝 通知及更改状态
				map.put("bplayerId", player.getPlayerId());
				this.judgeApplyRefuse(playerApplyList,map);
				return WebPublicMehod.returnRet("success", playerRole!=null?"转让成功！":"同意成功！");
			}
			return WebPublicMehod.returnRet("error", "对方已拥有两个角色，不能"+playerRole!=null?"转让！":"同意！");
		}
		return WebPublicMehod.returnRet("error", "您没有待"+playerRole!=null?"转让！":"同意！"+"的申请角色");
	}


	@Override
	public HashMap<String, Object> applyCheckRefusePlayerMemberType(HashMap<String, String> map) throws Exception {
		UPlayerApply playerApply=baseDAO.get(map,"from UPlayerApply where applyStatus='1' and keyId=:keyId ");//
		if (playerApply != null) {
//			if(playerApply.getUPlayer()!=null&&playerApply.getUPlayer().getInTeam()!=null&&!"1".equals(playerApply.getUPlayer().getInTeam())){
//				return WebPublicMehod.returnRet("error", "你所选的成员已退队，无法继续操作");
//			}
			map.put("name", "member_type");
			map.put("params",playerApply.getMemberType());
			UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
			UPlayer player=baseDAO.get(map,"from UPlayer where UTeam.teamId=:teamId and UUser.userId=:loginUserId");
			// 拒绝其他申请该角色的人
			playerApply.setApplyStatus("3");
			playerApply.setRefusedate(new Date());
			playerApply.setBplayerId(player.getPlayerId());
			playerApply.setBuserId(map.get("loginUserId"));
			baseDAO.update(playerApply);
			// 拒绝通知－－后期预留
			if(playerApply.getUPlayer()!=null){
				//通知
				if(uParameterInfo!=null){
				map.put("memberTypeName",uParameterInfo.getName());//通知用
				this.applyPush(map, playerApply.getUTeam(), playerApply.getUPlayer(), playerApply.getUPlayer().getUUser()!=null?playerApply.getUPlayer().getUUser().getUserId():"", "tmApplyRefuse","b01");
				}
			}
			return WebPublicMehod.returnRet("success", "拒绝成功！");
		}
		return WebPublicMehod.returnRet("error", "您没有待审核的申请角色");
	}
	@Override
	public HashMap<String, Object> applyPlayerJoinTeamMemberType(UPlayer player,UTeam team,HashMap<String, String> map) throws Exception {
		UPlayerRole playerRole=null;//获取球队中唯一
		UPlayerApply playerApply=null;
		List<UPlayerRole> playerRolesList=null;//
		Integer lvl=0;
		String memberType1=Public_Cache.MEMBER_TYPE_LVL1;//默认一级申请，向一级发通知
		List<ParameterInfos> listApplyMemberType=new Gson().fromJson(map.get("listApplyMemberType"), new TypeToken<List<ParameterInfos>>() {}.getType());
		List<ParameterInfos> listSelectMemberType=new Gson().fromJson(map.get("listSelectMemberType"), new TypeToken<List<ParameterInfos>>() {}.getType());
		// 参数不能没有
		if (publicService.StringUtil(map.get("teamId"))&&(listApplyMemberType==null&&listSelectMemberType==null)) {
			return WebPublicMehod.returnRet("error", "请求参数不全!");
		}
		//勾选不能超过两个
		if (listSelectMemberType != null && listSelectMemberType.size() > 2) {
			return WebPublicMehod.returnRet("error", "你只可选择最多两个角色");
		}
		//勾选
		if (listSelectMemberType != null&&listSelectMemberType.size()>0) {
			listSelectMemberType.removeAll(YHDCollectionUtils.nullCollection());
			for (ParameterInfos parameterInfos : listSelectMemberType) {
				map.put("memberType", parameterInfos.getParams());
				// 如果是唯一角色
				this.createNewPlayRole202(player, map);
				
			}
		}
		//申请
		if (listApplyMemberType != null) {
			listApplyMemberType.removeAll(YHDCollectionUtils.nullCollection());
			for (ParameterInfos parameterInfos : listApplyMemberType) {
				map.put("memberType", parameterInfos.getParams());
				map.put("memberTypeName", parameterInfos.getName());// 通知用
				this.applyMemberTypePlayer(map, playerRole, lvl, playerRolesList, playerApply, player, team,
						memberType1);
			}
		}
		return WebPublicMehod.returnRet("success", "success");
	}
	
	@Override
	public HashMap<String, Object> editPlayerTeamMemberType(HashMap<String, String> map,UPlayer player) throws Exception {
		List<Object> listStr = new ArrayList<>();
		List<ParameterInfos> listApplyMemberType=new Gson().fromJson(map.get("listApplyMemberType"), new TypeToken<List<ParameterInfos>>() {}.getType());
		List<ParameterInfos> listSelectMemberType=new Gson().fromJson(map.get("listSelectMemberType"), new TypeToken<List<ParameterInfos>>() {}.getType());
		// 参数不能没有
		if (publicService.StringUtil(map.get("teamId"))&&(listApplyMemberType==null&&listSelectMemberType==null)) {
			return WebPublicMehod.returnRet("error", "请求参数不全!");
		}
		//勾选不能超过两个
		if (listSelectMemberType != null && listSelectMemberType.size() > 2) {
			return WebPublicMehod.returnRet("error", "你只可选择最多两个角色");
		}
		List<UPlayerRole> listPlayerRoles = baseDAO.find(map,
				"from UPlayerRole where UPlayer.UTeam.teamId=:teamId  and UPlayer.UUser.userId=:loginUserId and memberTypeUseStatus='1' and UPlayer.inTeam='1' and  memberType!='11'");
		// 先获取球员修改之前已有角色
		if (publicService.StringUtil(map.get("oldMemberType"))) {
			String beforeMemberTypes = map.get("oldMemberType");
			String[] beforeMemberType = beforeMemberTypes.split(",");

			for (int i = 0; i < beforeMemberType.length; i++) {
				listStr.add(beforeMemberType[i]);
			}
			if (listPlayerRoles.size() > beforeMemberType.length) {
				return WebPublicMehod.returnRet("error", "当前球队成员有将角色转让给你\n请先查看再进行修改球员信息");
			}
		}
		UPlayerRole playerRole = null;// 获取球队中唯一
		UPlayerApply playerApply = null;//
		map.put("name", "member_type");
		List<UPlayerRole> playerRolesList = null;//
		UParameterInfo uParameterInfo = null;//
		Integer lvl = 0;
		String memberType1 = Public_Cache.MEMBER_TYPE_LVL1;// 默认一级申请，向一级发通知
		UTeam team = baseDAO.get(UTeam.class, map.get("teamId"));
		List<UPlayerApply> listPlayerApply=null;
		//将自己角色状态改成不可用
		if(listPlayerRoles!=null&&listPlayerRoles.size()>0){
			for (UPlayerRole uPlayerRole : listPlayerRoles) {
				map.put("memberType", uPlayerRole.getMemberType());
				map.put("params", map.get("memberType"));
				uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
				// 如果是唯一角色
				if (uParameterInfo != null && !"1".equals(uParameterInfo.getIsUnique())) {
					uPlayerRole.setMemberTypeUseStatus("2");
					baseDAO.update(uPlayerRole);
				}
			}
		}
		//勾选
		if (listSelectMemberType != null&&listSelectMemberType.size()>0) {
			listSelectMemberType.removeAll(YHDCollectionUtils.nullCollection());
			//循环出自己的角色
			for (ParameterInfos parameterInfos : listSelectMemberType) {
				if (parameterInfos.getParams() != null) {
					parameterInfos.setAct("2");// 2、修改的角色和以前的不一样，1、同一个角色
					if (listPlayerRoles != null && listPlayerRoles.size() > 0) {
						for (int j = 0; j < listStr.size(); j++) {
							if (parameterInfos.getParams().equals(listStr.get(j))) {
								parameterInfos.setAct("1");
								break;
							}
						}
					}
				}
			}
			for (ParameterInfos parameterInfos : listSelectMemberType) {
				map.put("memberType", parameterInfos.getParams());
				//如果角色是自己的将状态改成可用
				if ("1".equals(parameterInfos.getAct())) {
					playerRole = baseDAO.get(map,
							"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and UPlayer.UUser.userId=:loginUserId and memberTypeUseStatus='2'");
					if (playerRole != null) {
						playerRole.setMemberTypeUseStatus("1");
						baseDAO.update(playerRole);
					}
				}
				//否者新增一条角色数
				if ("2".equals(parameterInfos.getAct())) {
					playerRole=baseDAO.get(map,"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and UPlayer.inTeam='1' and memberTypeUseStatus='1' and UPlayer.UUser.userId!=:loginUserId");
					if(playerRole!=null&&parameterInfos.getIsUnique()!=null&&"1".equals(parameterInfos.getIsUnique())){
						return WebPublicMehod.returnRet("error", "“"+parameterInfos.getName()+"”已被占，请重新选择！");
					}
					// 添加角色
					this.createNewPlayRole202(player, map);
					
					map.put("bplayerid", player.getPlayerId());// 分配中球员ID
					map.put("memberTypeName", uParameterInfo != null?uParameterInfo.getName():"");// 通知用
					this.judgeApplyRefuse(listPlayerApply,map);
				}
			}
			
		}
		// 申请
		if (listApplyMemberType != null) {
			listApplyMemberType.removeAll(YHDCollectionUtils.nullCollection());
			for (ParameterInfos parameterInfos : listApplyMemberType) {
				map.put("memberType", parameterInfos.getParams());
				map.put("memberTypeName", parameterInfos.getName());// 通知用
				this.applyMemberTypePlayer(map, playerRole, lvl, playerRolesList, playerApply, player, team,
						memberType1);
			}
		}
		return WebPublicMehod.returnRet("success", "success");
	}
	
	@Override
	public HashMap<String, Object> batchApplyRefusePlayerMemberType(HashMap<String, String> map) throws Exception {
//		List<UPlayerApply> listPlayerApply=baseDAO.find(map,"from UPlayerApply where applyStatus='1' and applydate < date_sub(date_format(now(),'%Y-%m-%d'), interval "+Public_Cache.APPLAY_REFUSE+" day)");
		List<UPlayerApply> listPlayerApply=baseDAO.find(map,"from UPlayerApply where applyStatus='1' ");
		UParameterInfo uParameterInfo=null;
		if(listPlayerApply!=null&&listPlayerApply.size()>0){
			for (UPlayerApply playerApply : listPlayerApply) {
				//7天都没人审核的，拒绝申请
				if(playerApply.getApplydate()!=null&&PublicMethod.daysBetween(playerApply.getApplydate(),new Date())>Public_Cache.APPLAY_REFUSE){
					playerApply.setApplyStatus("3");
					playerApply.setRefusedate(new Date());
					baseDAO.update(playerApply);
					// 拒绝通知－－后期预留
					if (playerApply.getUPlayer() != null) {
						// 通知
						map.put("name", "member_type");
						map.put("params", playerApply.getMemberType());
						uParameterInfo = uParameterService.getMemberTypeByTeamId202(map);
						if (uParameterInfo != null) {
							map.put("memberTypeName", uParameterInfo.getName());// 通知用
							this.applyPush(map, playerApply.getUTeam(), playerApply.getUPlayer(),playerApply.getUPlayer().getUUser() != null? playerApply.getUPlayer().getUUser().getUserId() : "","tmApplyRefuse","b01");
						}
					}
				}
			}
			return WebPublicMehod.returnRet("success", "success");
		}
		return null;
	}
	/**
	 * 
	 * TODO 申请并发出通知-加队角色-修改角色-申请角色 ［2.0.3］
	 * @param map
	 * @param playerRole 角色对象
	 * @param lvl 自身等级
	 * @param playerRolesList 根据等级查询出一级或一二级的角色列表
	 * @param playerApply 申请对象
	 * @param player 申请人球员对象
	 * @param team 申请的球队对象
	 * @param memberType1 一级、或一二级码表值
	 * @throws Exception
	 * xiaoying 2016年8月23日
	 */
	private void applyMemberTypePlayer(HashMap<String, String> map, UPlayerRole playerRole, Integer lvl,
			List<UPlayerRole> playerRolesList, UPlayerApply playerApply, UPlayer player, UTeam team, String memberType1)  throws Exception{
		playerRole = baseDAO.get(map,
				"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and memberType=:memberType and memberTypeUseStatus='1' and UPlayer.inTeam='1'");
		// 唯一角色有人占领，只向占有人发出申请，并通知，否者向所有一级发出申请，并通知所有一级人员
		if (playerRole != null) {
			map.put("buserId", null);
			if (playerRole.getUPlayer().getUUser() != null) {
				map.put("buserId", playerRole.getUPlayer().getUUser().getUserId());
			}
			map.put("bplayerId", playerRole.getUPlayer().getPlayerId());
			this.insertPlayerApply(playerApply, player, team, map);
			// 发出申请通知
			this.applyPush(map,team,player,map.get("buserId"),"tmApplyYet","e03");
		} else {
			lvl = uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(map.get("memberType")));
			if (lvl == 2) {// 如果是二级申请,那么向一级和二级发送通知
				memberType1 += "," + Public_Cache.MEMBER_TYPE_LVL2;
			}
			// 获取该球队所有一级球员
			playerRolesList = baseDAO.find(map,
					"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and  memberType in("
							+ memberType1 + ") and memberTypeUseStatus='1' and UPlayer.inTeam='1'");
			this.insertPlayerApply(playerApply, player, team, map);// 只发一条数据，不进入循环
			if (playerRolesList != null && playerRolesList.size() > 0) {
				for (UPlayerRole uPlayerRole : playerRolesList) {
					// 发出申请通知
					if(uPlayerRole.getUPlayer()!=null){
						this.applyPush(map,team,player,uPlayerRole.getUPlayer().getUUser()!=null?uPlayerRole.getUPlayer().getUUser().getUserId():"","tmApply","e03");
					}
				}
			}
		}
		
	}

	/**
	 * 
	 * TODO 角色申请通知 ［2.0.3］
	 * @param map 
	 * @param team 申请的球队
	 * @param player 被申请人球员对象，通知对象
	 * @param userId 通知的用户ID
	 * @param mesType 通知类型 tmApply-申请通知，tmApplyYet-申请通知【已被人申请】，tmApplyRefuse-申请－拒绝，tmApplyAgree-申请－同意
	 * @param jump 
	 * @throws Exception
	 * xiaoying 2016年8月23日
	 */
	private void applyPush(HashMap<String, String> map, UTeam team, UPlayer player,String userId,String mesType, String jump)  throws Exception{
		if (team != null && player != null) {
			
			UUser uUser=null;
			String teamName = "";
			String userName = "";
			String buserName = "";
			String msg ="";
			String repetition = "1";
			String params = "{\"jump\":\"" + jump + "\",\"playerId\":\"" + player.getPlayerId() + "\",\"teamId\":\""
					+ team.getTeamId() + "\",\"userId\":\"" + userId + "\"}";
			if (publicService.StringUtil(userId)) {
				uUser = baseDAO.getHRedis(UUser.class, userId,
						PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));
				if (uUser != null) {
					if (uUser.getRealname() != null && !"".equals(uUser.getRealname())) {
						buserName = uUser.getRealname();
					} else {
						if (uUser.getNickname() != null && !"".equals(uUser.getNickname())) {
							buserName = uUser.getNickname();
						}
					}
				}
			}
			if (team.getName() != null && !"".equals(team.getName())) {
				teamName = team.getName();
			}
			if (player.getUUser() != null) {
				if (player.getUUser().getRealname() != null && !"".equals(player.getUUser().getRealname())) {
					userName = player.getUUser().getRealname();
				} else {
					if (player.getUUser().getNickname() != null && !"".equals(player.getUUser().getNickname())) {
						userName = player.getUUser().getNickname();
					}
				}
			}
			//向申请人发通知
			if("tmApplyYet".equals(mesType)){
//				msg =Public_Cache.getMessageCon("team", "tmApplyYet").replaceFirst(regex, replacement);
				msg = teamName + "球队中的" + userName + "正向你申请" + map.get("memberTypeName") + "角色";
			}
			//向一二级发通知
			if("tmApply".equals(mesType)){
				msg = teamName + "球队的" + userName + "已申请了" + map.get("memberTypeName") + "角色，请求审批";
			}
			//向同意人发通知
			if("tmApplyAgree".equals(mesType)){
				msg = "你在"+teamName + "中申请的" + map.get("memberTypeName") + "角色已申请成功";
			}
			//向拒绝人发通知
			if("tmApplyRefuse".equals(mesType)){
				msg = "你在"+teamName + "中申请的" + map.get("memberTypeName") + "角色已被拒绝";
			}
			//向拒绝人发通知
			if("tmApplyAllot".equals(mesType)){
				msg = "你已被"+buserName + "赋予了" + map.get("memberTypeName") + "角色，你现在可对队伍进行更多操作";
			}
			HashMap<String, String> map1=new HashMap<>();
			// //极光推送参数
			map1.put("jump", jump);
			map1.put("playerId", player.getPlayerId());
			map1.put("teamId", team.getTeamId());
			map1.put("userId", userId);
			String code = "";
			// 根据userId 查code
				if (null != uUser && uUser.getNumberid() != null && !"".equals(uUser.getNumberid())) {
					map.put("keyId", uUser.getNumberid());
					UEquipment uEquipment = baseDAO.get(map, "from UEquipment where keyId=:keyId");
					if (null != uEquipment) {
						code = uEquipment.getCode();
					}
				}
			
			this.setMessage202(map1, mesType, msg, userId, params, repetition);// 发送通知
			this.publicAppPush202(map1, mesType, msg, code);// 发送极光推送
		}
	}


	/**
	 * 
	 * TODO 添加申请数据 ［2.0.3］
	 * @param playerApply 申请角色对象
	 * @param player 球员对象
	 * @param team 球队对象
	 * @param map loginUserId－登录用户（申请人）、buserId－被申请者、bplayerId－被申请的球员ID  
	 * @throws Exception
	 * xiaoying 2016年7月30日
	 */
	private void insertPlayerApply(UPlayerApply playerApply, UPlayer player, UTeam team, HashMap<String, String> map)  throws Exception{
		playerApply =new UPlayerApply();
		playerApply.setKeyId(WebPublicMehod.getUUID());
		playerApply.setUserId(map.get("loginUserId"));
		playerApply.setUPlayer(player);
		playerApply.setUTeam(team);
		playerApply.setMemberType(map.get("memberType"));
		playerApply.setApplyStatus("1");//1-待确认 2-同意 3-拒绝
		playerApply.setBuserId(map.get("buserId"));
		playerApply.setBplayerId(map.get("bplayerId"));
		playerApply.setApplydate(new Date());
		baseDAO.save(playerApply);
	}


	/**
	 * 
	 * TODO 判断分配的角色，是否存被人申请的角色-拒绝+同意 ［2.0.3］
	 * @param map teamId-球队ID loginUserId-分配人(登录人),bplayerid-分配人球员ID,memberType-被分配的角色
	 * @param playerApply 
	 * @param listPlayerApply 
	 * @param uParameterInfo 
	 * @throws Exception
	 * xiaoying 2016年7月28日
	 */
	private void judgePlayerApply(HashMap<String, String> map, UPlayerApply playerApply, List<UPlayerApply> listPlayerApply)  throws Exception{
		map.put("isApplyPush", "2");
		playerApply=baseDAO.get(map,"from UPlayerApply where UTeam.teamId=:teamId and UPlayer.playerId=:playerId and memberType=:memberType and applyStatus='1'");
		if(playerApply!=null){
			playerApply.setApplyStatus("2");
			playerApply.setAgreedate(new Date());
			playerApply.setBplayerId(map.get("bplayerid"));
			playerApply.setBuserId(map.get("loginUserId"));
			baseDAO.update(playerApply);
			if(playerApply.getUPlayer()!=null){
				// 通知
				this.applyPush(map, playerApply.getUTeam(), playerApply.getUPlayer(), playerApply.getUPlayer().getUUser()!=null?playerApply.getUPlayer().getUUser().getUserId():"", "tmApplyAgree","b01");
				map.put("isApplyPush", "1");
			}
		}
		this.judgeApplyRefuse(listPlayerApply,map);
	}
	/**
	 * 
	 * TODO 拒绝部分 ［2.0.3］
	 * @param listPlayerApply 需要拒绝的申请集合
	 * @param map
	 * @throws Exception
	 * xiaoying 2016年8月23日
	 */
	private void judgeApplyRefuse(List<UPlayerApply> listPlayerApply, HashMap<String, String> map) throws Exception{
		if(map.get("keyId")!=null&&!"".equals(map.get("keyId"))){
			listPlayerApply=baseDAO.find(map,"from UPlayerApply where UTeam.teamId=:teamId and keyId!=:keyId and memberType=:memberType and applyStatus='1'");
		}else{
			if(map.get("bplayerid")!=null&&!"".equals(map.get("bplayerid"))){
				listPlayerApply=baseDAO.find(map,"from UPlayerApply where UTeam.teamId=:teamId and UPlayer.playerId !=:playerId and memberType=:memberType and applyStatus='1'");
			}
		}
		//如果被分配的角色，已经存在别人申请的数据，那么更新申请数据的状态
		if(listPlayerApply!=null&&listPlayerApply.size()>0){
			for (UPlayerApply uPlayerApply : listPlayerApply) {
				if(uPlayerApply.getUPlayer()!=null){
					uPlayerApply.setApplyStatus("3");
					uPlayerApply.setRefusedate(new Date());
					uPlayerApply.setBplayerId(map.get("bplayerid"));
					uPlayerApply.setBuserId(map.get("loginUserId"));
					baseDAO.update(uPlayerApply);
					if(uPlayerApply.getUPlayer()!=null){
						//通知
						this.applyPush(map, uPlayerApply.getUTeam(), uPlayerApply.getUPlayer(), uPlayerApply.getUPlayer().getUUser()!=null?uPlayerApply.getUPlayer().getUUser().getUserId():"", "tmApplyRefuse","b01");
					}
				}
			}
		}
	}
	

	/**
	 * 
	 * 
	   TODO - 查询当前用户在这个球队中有哪些角色，并拼接成字符串
	   @param uPlayer
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public String memberTypeStrByPlayerId(UPlayer uPlayer) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		String memberTypeStr = "";
		hashMap.put("playerId", uPlayer.getPlayerId());
		List<UPlayerRole> uPlayerRoles = baseDAO.find(hashMap, "from UPlayerRole where UPlayer.playerId=:playerId and memberTypeUseStatus='1'");
		if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
			for (UPlayerRole uPlayerRole : uPlayerRoles) {
				//查询码表对象，并查看该角色是否唯一
				hashMap.put("name", "member_type");
				hashMap.put("params",uPlayerRole.getMemberType());
				UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
				if (null != uParameterInfo) {
					if (publicService.StringUtil(uParameterInfo.getIsUnique())) {
						if (!"3".equals(uParameterInfo.getIsUnique())) {
							if (publicService.StringUtil(memberTypeStr)) {
								memberTypeStr += "、"+uParameterInfo.getName();
							}else {
								memberTypeStr = uParameterInfo.getName();
							}
						}
					}
				}
			}
		}
		return memberTypeStr;
	}
}
