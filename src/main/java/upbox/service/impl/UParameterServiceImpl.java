package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UParameter;
import upbox.model.UParameterInfo;
import upbox.model.UPlayerApply;
import upbox.model.UPlayerRole;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.util.YHDCollectionUtils;

@Service("uParameterService")
public class UParameterServiceImpl implements UParameterService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UPlayerRoleService uPlayerRoleService;
	@Resource
	private UPlayerService uplayerService;
	/**
	 * 
	 * 
	   TODO - 获取参数明细 【2.0.0】
	   @param uParameterList
	   		param的uParameterList
	   @return
	   		
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUParameterInfoList(List<Map<String, Object>> uParameterList) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != uParameterList && uParameterList.size() > 0) {
			for (Map<String, Object> map : uParameterList) {
				String params = map.get("params").toString();
				if (publicService.StringUtil(params)) {
					hashMap.put("param", params);
					//先根据参数查到父级参数
					UParameter parameter = baseDAO.get(hashMap, "from UParameter where params=:param");
					if (null != parameter) {
						hashMap.put("pkeyId", parameter.getPkeyId());
						String queryParam= null;
						if ("member_type".equals(parameter.getParams())) {
							queryParam=" SELECT u.name,u.params,u.pikey_id,u.remark,u.imgurl FROM u_parameter_info u WHERE u.pkey_id = :pkeyId and u.params != '1' and u.params != '11' ORDER BY u.rank_weight ASC";
						}else{
							queryParam=" SELECT u.name,u.params,u.pikey_id,u.remark,u.imgurl FROM u_parameter_info u WHERE u.pkey_id = :pkeyId ORDER BY u.rank_weight ASC";
							
						}
//						//根据父级的id，找到明细的参数明细
//						List<UParameterInfo> uParameterInfoList = baseDAO.find(hashMap, "from UParameterInfo where UParameter.pkeyId=:pkeyId");
						
						List<HashMap<String ,Object>> list = baseDAO.findSQLMap(hashMap,queryParam);
						resultMap.put(params, list);
					}
				}
			}
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 用户在加队，修改信息，转让时的角色列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getMemberTypeListByTeamId202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		List<HashMap<String ,Object>> uParameterInfos = null;
		//xiao
		UPlayerApply playerApply=null;
		List<HashMap<String ,Object>> listApply = new ArrayList<>();
		Integer lvl=uplayerService.getTeamIdAndUserIdPlayer(map);
		Integer lvl1=0;
		
		UParameter uParameter = baseDAO.get("from UParameter where params='member_type'");
		if (null != uParameter) {
			String queryParam=" SELECT u.name,u.params params,u.pikey_id,u.remark,u.imgurl,u.is_unique isUnique FROM u_parameter_info u WHERE u.pkey_id = :pKeyId and u.params<>'11' ORDER BY u.rank_weight ASC";
			hashMap.put("pKeyId", uParameter.getPkeyId());
			uParameterInfos = baseDAO.findSQLMap(queryParam, hashMap);
			if (null != uParameterInfos && uParameterInfos.size() > 0) {
				for (HashMap<String, Object> hashMap2 : uParameterInfos) {
					map.put("memberType", hashMap2.get("params").toString());
					String isMyselfSelect = "2";
					String isOtherSelect = "2";
					String isUnique = "2";
					//判断此角色是否唯一
					if (null != hashMap2.get("isUnique")) {
						if ("1".equals(hashMap2.get("isUnique").toString()) || "2".equals(hashMap2.get("isUnique").toString())) {
							//判断是否被自己选择
							Boolean isMyselfSelected = uPlayerRoleService.isMyselfSelected202(map);
							Boolean isOtherSelected = uPlayerRoleService.isOtherSelected202(map);
							if (true == isMyselfSelected) { 
								isMyselfSelect = "1";
							}else{//如果没有被别人选择，判断是否自己选择了
								if (true == isOtherSelected) {
									isOtherSelect = "1";
								}
							}
							isUnique = "1";
						}else if ("3".equals(hashMap2.get("isUnique").toString())) {
							//判断是否被自己选择
							Boolean isMyselfSelected = uPlayerRoleService.isMyselfSelected202(map);
							Boolean isOtherSelected = uPlayerRoleService.isOtherSelected202(map);
							if (true == isMyselfSelected) { 
								isMyselfSelect = "1";
							}else{//如果没有被别人选择，判断是否自己选择了
								if (true == isOtherSelected) {
									isOtherSelect = "1";
								}
							}
						}else{
							Boolean isMyselfSelected = uPlayerRoleService.isMyselfSelected202(map);
							if (true == isMyselfSelected) {
								isMyselfSelect = "1";
							}
						}
					}else{
						Boolean isMyselfSelected = uPlayerRoleService.isMyselfSelected202(map);
						if (true == isMyselfSelected) {
							isMyselfSelect = "1";
						}
					}
					hashMap2.put("isOtherSelect", isOtherSelect);
					hashMap2.put("isMyselfSelect", isMyselfSelect);
					hashMap2.put("isUnique", isUnique);
					//xiao
					if(hashMap2.get("params")!=null&&!"".equals(hashMap2.get("params"))){
						lvl1=this.getPlayerRoleLimitLvl(Integer.parseInt(hashMap2.get("params").toString()));
						hashMap2.put("lvl", lvl1);
					}
					//查看申请的列表，唯一角色才能申请，因此只有唯一角色才能进入
					hashMap2.put("alreadyApply", "2");//没有申请
					hashMap2.put("listApply", listApply);
					if("1".equals(isUnique)){
						//角色被自身占了，那么查询出被我占了的申请列表
						if("1".equals(isMyselfSelect)){
//							listPlayerApply=baseDAO.find(map,"from UPlayerApply where UTeam.teamId=:teamId and memberType=:memberType and  buserId=:loginUserId and applyStatus='1'");
							listApply=baseDAO.findSQLMap(map,"select pa.keyid as keyId,u.realname,u.nickname from u_player_apply pa left join u_user u on u.user_id=pa.userid where pa.buserid=:loginUserId and pa.teamid=:teamId and pa.member_type=:memberType and pa.apply_status='1'");
						}
						//角色没被人占了，那么所有一级都能看到申请列表
						if("2".equals(isMyselfSelect)&&"2".equals(isOtherSelect)&&1==lvl){
							listApply=baseDAO.findSQLMap(map,"select pa.keyid as keyId,u.realname,u.nickname from u_player_apply pa left join u_user u on u.user_id=pa.userid where pa.teamid=:teamId and pa.member_type=:memberType and pa.apply_status='1'");
						}
						if(listApply!=null&&listApply.size()>=1){
							listApply.removeAll(YHDCollectionUtils.nullCollection());
							hashMap2.put("listApply", listApply);
						}
						//角色是否已申请
						playerApply=baseDAO.get(map,"from UPlayerApply where userId=:loginUserId and UTeam.teamId=teamId and memberType=:memberType and applyStatus='1'");
						if(playerApply!=null){
							hashMap2.put("alreadyApply", "1");//已申请
						}
						
					}
					//分配页面＋申请页面
					hashMap2.put("isChoice", "2"); //2，不能分配
					if (lvl != 0) {// 自身角色不能为空
						if(lvl==1){//一级可以分配所有角色
							hashMap2.put("isChoice", "1");
						}
						if(lvl==2&&lvl<=lvl1){//可分配2，3级可以
							hashMap2.put("isChoice", "1");
						}
					}
					hashMap2.put("isApply", "2");//申请 1，能申请
					if("1".equals(isUnique)){
						hashMap2.put("isApply", "1");//申请 1，能申请
						if (lvl != 0) {// 自身角色不能为空
							if(lvl==1){//一级所有都不能申请
								hashMap2.put("isApply", "2");
							}
							if(lvl==2&&lvl1>=2){//二级不能申请2级和3级
								hashMap2.put("isApply", "2");
							}
						}
					}
				}
				
			}
		}
		resultMap.put("uParameterInfos", uParameterInfos);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 查询某个角色的参数实体（查询该角色是否唯一）
	   @param map
	   		name    member_type
	   		params  1
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	@Override
	public UParameterInfo getMemberTypeByTeamId202(HashMap<String, String> map) throws Exception {
		//先查询是否存在这个参数
		UParameter uParameter = baseDAO.get(map,"from UParameter where params=:name");
		UParameterInfo uParameterInfo = null;
		if (null != uParameter) {
			map.put("pKeyId", uParameter.getPkeyId());
			//在查询对应参数的明细
			uParameterInfo = baseDAO.get(map, "from UParameterInfo where UParameter.pkeyId=:pKeyId and params=:params");
		}
		return uParameterInfo;
	}

	/**
	 * 
	 * 
	   TODO - 获取所有身份集合
	   @param map
	   		name     参数对应的名称  例如：身份=member_type
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	@Override
	public List<UParameterInfo> getMemberTypeList202(HashMap<String, String> map) throws Exception {
		//先查询是否存在这个参数
		UParameter uParameter = baseDAO.get(map,"from UParameter where params=:name");
		List<UParameterInfo> uParameterInfos = null;
		if (null != uParameter) {
			map.put("pKeyId", uParameter.getPkeyId());
			//在查询对应参数的明细
			uParameterInfos = baseDAO.find(map, "from UParameterInfo where UParameter.pkeyId=:pKeyId and params<>'11' ORDER BY rankWeight ASC ");
		}
		return uParameterInfos;
	}
	@Override
	public HashMap<String, Object> getMemberTypeListByApplyJoinTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		List<HashMap<String ,Object>> uParameterInfos = null;
		//xiao
		UPlayerApply playerApply=null;
		List<HashMap<String ,Object>> listApply = null;
		Integer lvl=uplayerService.getTeamIdAndUserIdPlayer(map);
		Integer lvl1=0;
		UPlayerRole playerRole=null;
		
		UParameter uParameter = baseDAO.get("from UParameter where params='member_type'");
		if (null != uParameter) {
			
			String queryParam=" SELECT u.name,u.params params,u.pikey_id,u.remark,u.imgurl,u.is_unique isUnique FROM u_parameter_info u WHERE u.pkey_id = :pKeyId and u.params<>'11'  ORDER BY u.is_unique, u.rank_weight ASC";
			if(map.get("act")!=null&&"2".equals(map.get("act"))){
				queryParam=" SELECT u.name,u.params params,u.pikey_id,u.remark,u.imgurl,u.is_unique isUnique FROM u_parameter_info u WHERE u.pkey_id = :pKeyId and u.params<>'11' and u.is_unique='1'  ORDER BY u.is_unique, u.rank_weight ASC";
			}
			hashMap.put("pKeyId", uParameter.getPkeyId());
			uParameterInfos = baseDAO.findSQLMap(queryParam, hashMap);
			if (null != uParameterInfos && uParameterInfos.size() > 0) {
				for (HashMap<String, Object> hashMap2 : uParameterInfos) {
					map.put("memberType", hashMap2.get("params").toString());
					hashMap2.put("isOtherSelect", "2");
					hashMap2.put("isMyselfSelect", "2");
					hashMap2.put("isSelect", "2");
					listApply = new ArrayList<>();
					//默认
					if(map.get("loginUserId")!=null&&!"".equals(map.get("loginUserId"))){
						playerRole = baseDAO.get(map,
								"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and UPlayer.UUser.userId=:loginUserId and  memberType=:memberType and memberTypeUseStatus='1' and UPlayer.inTeam='1' ");
						if(playerRole!=null){
							hashMap2.put("isMyselfSelect", "1");
						}else{
							playerRole = baseDAO.get(map,
									"from UPlayerRole where UPlayer.UTeam.teamId=:teamId and UPlayer.UUser.userId !=:loginUserId and  memberType=:memberType and memberTypeUseStatus='1' and UPlayer.inTeam='1' ");
							if(playerRole!=null){
								hashMap2.put("isOtherSelect", "1");
							}
						}
					}
					//分配时
					if(map.get("playerId")!=null&&!"".equals(map.get("playerId"))){
						playerRole = baseDAO.get(map,
								"from UPlayerRole where UPlayer.playerId=:playerId and  memberType=:memberType and memberTypeUseStatus='1' and UPlayer.inTeam='1' ");
						if(playerRole!=null){
							hashMap2.put("isSelect", "1");
						}
					}
					//xiao
					if(hashMap2.get("params")!=null&&!"".equals(hashMap2.get("params"))){
						lvl1=this.getPlayerRoleLimitLvl(Integer.parseInt(hashMap2.get("params").toString()));
						hashMap2.put("lvl", lvl1);
					}
					//查看申请的列表，唯一角色才能申请，因此只有唯一角色才能进入
					hashMap2.put("alreadyApply", "2");//没有申请
					hashMap2.put("listApply", listApply);
					if(hashMap2.get("isUnique")!=null&&"1".equals(hashMap2.get("isUnique"))){
						if(map.get("act")!=null&&"2".equals(map.get("act"))){
							//角色被自身占了，那么查询出被我占了的申请列表
							if("1".equals(hashMap2.get("isMyselfSelect"))){
								listApply=baseDAO.findSQLMap(map,"select pa.keyid as keyId,u.realname,u.nickname,ui.imgurl,pa.playerid as playerId,pa.userid as userId,'1' as isTransfer from u_player_apply pa left join u_user u on u.user_id=pa.userid left join u_user_img ui on ui.user_id=u.user_id and ui.img_size_type='1' and uimg_using_type='1' left join u_player p on p.player_id=pa.playerid where pa.buserid=:loginUserId and pa.teamid=:teamId and pa.member_type=:memberType and pa.apply_status='1'  and p.in_team='1'");
							}
							//角色没被人占了，那么所有一级都能看到申请列表
							if("2".equals(hashMap2.get("isMyselfSelect"))&&"2".equals(hashMap2.get("isOtherSelect"))){
								if (lvl1 == 2) {
									if (lvl == 1 || lvl == 2) {
										listApply = baseDAO.findSQLMap(map,
												"select pa.keyid as keyId,u.realname,u.nickname,ui.imgurl,pa.playerid as playerId,pa.userid as userId,'2' as isTransfer from u_player_apply pa left join u_user u on u.user_id=pa.userid left join u_user_img ui on ui.user_id=u.user_id and ui.img_size_type='1' and uimg_using_type='1' left join u_player p on p.player_id=pa.playerid where pa.teamid=:teamId and pa.member_type=:memberType and pa.buserid is null and pa.apply_status='1' and p.in_team='1'");
									}
								}
								if (lvl1 == 1) {
									if (lvl == 1) {
										listApply = baseDAO.findSQLMap(map,
												"select pa.keyid as keyId,u.realname,u.nickname,ui.imgurl,pa.playerid as playerId,pa.userid as userId,'2' as isTransfer from u_player_apply pa left join u_user u on u.user_id=pa.userid left join u_user_img ui on ui.user_id=u.user_id and ui.img_size_type='1' and uimg_using_type='1' left join u_player p on p.player_id=pa.playerid where pa.teamid=:teamId and pa.member_type=:memberType and pa.buserid is null and pa.apply_status='1' and p.in_team='1'");
									}
								}
							}
							if(listApply!=null){
								listApply.removeAll(YHDCollectionUtils.nullCollection());
							}
							hashMap2.put("listApply", listApply);
						}
						//角色是否已申请
						playerApply=baseDAO.get(map,"from UPlayerApply where userId=:loginUserId and UTeam.teamId=:teamId and memberType=:memberType and applyStatus='1' and UPlayer.inTeam='1' ");
						if(playerApply!=null){
							hashMap2.put("alreadyApply", "1");//已申请
						}
						
					}
					//分配页面＋申请页面
					hashMap2.put("isChoice", "2"); //2，不能分配
					if (lvl != 0) {// 自身角色不能为空
						if(lvl==1){//一级可以分配所有角色
							hashMap2.put("isChoice", "1");
						}
						if(lvl==2&&lvl<=lvl1){//可分配2，3级可以
							hashMap2.put("isChoice", "1");
						}
					}
					hashMap2.put("isApply", "2");//申请 1，能申请
					if(hashMap2.get("isUnique")!=null&&"1".equals(hashMap2.get("isUnique"))){
						hashMap2.put("isApply", "1");//申请 1，能申请
						if (lvl != 0) {// 自身角色不能为空
							if(lvl==1){//一级所有都不能申请
								hashMap2.put("isApply", "2");
							}
							if(lvl==2&&lvl1>=2){//二级不能申请2级和3级
								hashMap2.put("isApply", "2");
							}
						}
					}
				}
				
			}
		}
		resultMap.put("uParameterInfos", uParameterInfos);
		return resultMap;
	}
	
	@Override
	public Integer getPlayerRoleLimitLvl(Integer memberType) {
		Integer lvl=3;
//		1=队长、2=队员 3-总经理 4-财务后勤 5-主教练 6-助理教练 7-队医 8-新闻官 9-拉拉队员 10-赞助商 11-无角色 12-队务 13-主席 14-领队
//		一级：1=队长 3-总经理 13-主席  14-领队
//		二级：4-财务后勤 5-主教练 6-助理教练 10-赞助商 
//		三级：2=队员 7-队医 8-新闻官 9-拉拉队员
		switch (memberType) {
		case 13:
			lvl=1;
			break;
		case 1:
			lvl=1;
			break;
		case 3:
			lvl=1;
			break;
		case 14:
			lvl=1;
			break;
		case 6:
			lvl=2;
			break;
		case 4:
			lvl=2;
			break;
		case 5:
			lvl=2;
			break;
		case 10:
			lvl=2;
			break;
		case 2:
			lvl=3;
			break;
		case 9:
			lvl=3;
			break;
		case 7:
			lvl=3;
			break;
		case 8:
			lvl=3;
			break;
		default:
			lvl=3;
			break;
		}
		return lvl;
	}
	@Override
	public Integer getPlayerRoleMemberTypeIsUnique(Integer memberType) {
		Integer isUnique=2;
//		1=队长、2=队员 3-总经理 4-财务后勤 5-主教练 6-助理教练 7-队医 8-新闻官 9-拉拉队员 10-赞助商 11-无角色 12-队务 13-主席 14-领队
//		一级：1=队长 3-总经理 13-主席  14-领队
//		二级：4-财务后勤 5-主教练 6-助理教练 10-赞助商 
//		三级：2=队员 7-队医 8-新闻官 9-拉拉队员
		switch (memberType) {
		case 13:
			isUnique=1;
			break;
		case 1:
			isUnique=1;
			break;
		case 3:
			isUnique=1;
			break;
		case 14:
			isUnique=1;
			break;
		case 4:
			isUnique=1;
			break;
		case 5:
			isUnique=1;
			break;
		case 10:
			isUnique=3;
			break;
		case 2:
			isUnique=3;
			break;
		case 9:
			isUnique=3;
			break;
		case 7:
			isUnique=3;
			break;
		case 8:
			isUnique=3;
			break;
		case 12:
			isUnique=3;
			break;
		default:
			isUnique=2;
			break;
		}
		return isUnique;
	}
	
	@Override
	public HashMap<String, Object> updateRank(HashMap<String, String> map) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		List<UParameterInfo> uParameterInfos = baseDAO.find("from UParameterInfo where rankWeight is null");
		if (null != uParameterInfos && uParameterInfos.size() > 0) {
			for (UParameterInfo uParameterInfo : uParameterInfos) {
				if (StringUtils.isNotEmpty(uParameterInfo.getParams())) {
					String params = uParameterInfo.getParams();
					uParameterInfo.setRankWeight(Integer.parseInt(params));
					baseDAO.save(uParameterInfo);
				}
			}
			resultMap.put("success", "修改成功");
		}
		return resultMap;
	}
}
