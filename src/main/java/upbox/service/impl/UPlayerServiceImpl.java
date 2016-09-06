package upbox.service.impl;

import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;
import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.CornerBean;
import upbox.model.PageLimit;
import upbox.model.UFollow;
import upbox.model.UInviteteam;
import upbox.model.UParameterInfo;
import upbox.model.UPlayer;
import upbox.model.UPlayerHonor;
import upbox.model.UPlayerRole;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.UUserBehavior;
import upbox.model.UUserImg;
import upbox.outModel.OutPlayerList;
import upbox.outModel.OutUbCourtMap;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.CornerService;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.RankingListService;
import upbox.service.UFollowService;
import upbox.service.UInviteteamService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.URegionService;
import upbox.service.UTeamImgService;
import upbox.service.UTeamService;
import upbox.service.UUserBehaviorService;
import upbox.service.UUserImgService;
import upbox.service.UUserService;
import upbox.service.UWorthService;
import upbox.util.YHDCollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 前端端用户接口实现类
 *
 */
@Service("uplayerService")
public class UPlayerServiceImpl implements UPlayerService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UTeamService uTeamService;
	
	@Resource
	private UUserService uUserService;
	
	@Resource
	private UUserImgService uUserImgService;
	
	@Resource
	private UTeamImgService uTeamImgService;
	
	@Resource
	private UUserBehaviorService uUserBehaviorService;
	
	@Resource
	private RankingListService rankingListService;
	
	@Resource
	private UFollowService uFollowService;
	
	@Resource
	private UInviteteamService uInviteteamService;
	
	@Resource
	private CornerService cornerService;
	
	@Resource
	private URegionService 	uRegionService;
	
	@Resource
	private UPlayerRoleService uPlayerRoleService;
	
	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	
	@Resource
	private PublicPushService publicPushService;
	
	@Resource
	private MessageService messageService;
	
	@Resource
	private LBSService lBSService;
	
	@Resource
	private UParameterService uParameterService;
	
	@Resource
	private UWorthService uworthService;
	
	HashMap<String, Object> hashMap=new HashMap<>();
	int count = 0;
	
	// 球队未解散 阵容--sql
	private StringBuffer getUplayerNoInteamByTeamIds_sql = new StringBuffer(
			" select p.player_id  playerId,p.user_id userId,p.position position,p.number number,i.imgurl imgurl ,u.realname realname,u.nickname nickname "
			+ " from u_player p "
			+ " left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+ " left JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') "
			+ " left join u_user u on p.user_id = u.user_id "
			+ " left join u_user_img i on p.user_id=i.user_id  and i.uimg_using_type='1' and i.img_size_type='1' "
			+ " where p.team_id=:teamId and (p.exit_type='3' or p.exit_type is null) and r.member_type in ('1','2','9','11') "
			+ " GROUP by p.player_id order by r.member_type=1 desc,p.player_id desc,p.adddate desc,p.addtime desc ");

	// 球队已解散解散 阵容--sql
	private StringBuffer getUplayerInteamByTeamIds_sql = new StringBuffer("select p.player_id  playerId,p.user_id userId,p.position position,p.number number,i.imgurl imgurl ,u.realname realname,u.nickname nickname "
			+ " from u_player p "
			+ " left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+ " left JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') "
			+ " left join u_user u on p.user_id = u.user_id "
			+ " left join u_user_img i on p.user_id=i.user_id  and i.uimg_using_type='1' and i.img_size_type='1' "
			+ " where p.team_id=:teamId and p.in_team='1' and r.member_type in ('1','2','9','11') "
			+ " GROUP by p.player_id order by r.member_type=1 desc,count(p.player_id)=2 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc ");
	
	//球员概况--球员头部
	private StringBuffer roughlyStateOfUPlayerHead_sql = new StringBuffer(
		"select p.player_id playerId,p.team_id teamId,u1.user_id userId ,u1.realname realname,u1.nickname nickname,u1.sex sex,u1.age age,u1.weight weight,u1.height height,i.imgurl imgurl,u1.birthday birthday,"
		+" p.expert_position expertPosition,p.can_position canPosition,p.practice_foot practiceFoot,p.position position,p.number number,p.exit_type exitType,p.in_team inTeam"
		+ ",u1.remark remark,f.follow_status followStatus from u_player p"
		+" left join u_user u1 on u1.user_id=p.user_id "
		+" LEFT JOIN u_follow f on f.object_id=p.user_id and f.user_follow_type='3' and f.user_id=:userId "
		+" LEFT JOIN u_user_img i on i.user_id=p.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
		+" WHERE p.player_id=:playerId ");
	
	//我关注的球员
	private StringBuffer myFollowUPlayer_sql = new StringBuffer(
		"select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,u.user_id userId "
		+" ,u.sex sex,u.age age,u.height height,u.weight weight,p.number number,p.position position, "
		+" p.can_position canPosition,p.expert_position expertPosition,p.practice_foot practiceFoot "
		+" from u_follow f "
		+" left join u_user u on f.object_id=u.user_id  "
		+" left join u_player p on u.user_id=p.user_id  "
		+" LEFT JOIN u_user_img i on i.user_id=p.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
		+" where f.user_id=:userId and p.player_id is not null "
		+" and f.follow_status='1' and f.user_follow_type='3' GROUP BY f.key_id order by f.createdate desc ");

	//球队概况-球员列表
	private StringBuffer getUplayerListInRoughly_sql = new StringBuffer(
			" select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,p.number number,p.position position,u.user_id userId"
			+ ",u.username username,r.member_type memberType"
			+" from u_player p "
			+ " left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' and r.member_type<>'11' "
			+ " left JOIN u_player_role_limit l on l.member_type=r.member_type "
			+ " LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type')  "
			+" left join u_team t on p.team_id=t.team_id and t.team_status='3' and t.team_use_status='2'" 
			+" left join u_user u on u.user_id=p.user_id "
			+" left join u_user_img i on p.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
			+" where p.team_id=:teamId  and p.in_team='1' "
			+ " GROUP by p.player_id order by r.member_type=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,p.player_id desc ");
	
	private StringBuffer getUplayerListInRoughly_sql_size = new StringBuffer(
			" select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,p.number number,p.position position,u.user_id userId"
			+ ",u.username username,r.member_type memberType"
			+" from u_player p "
			+ " left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' and r.member_type<>'11' "
			+ " left JOIN u_player_role_limit l on l.member_type=r.member_type "
			+ " LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type')  "
			+" left join u_team t on p.team_id=t.team_id and t.team_status='3' and t.team_use_status='2'" 
			+" left join u_user u on u.user_id=p.user_id "
			+" left join u_user_img i on p.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
			+" where p.team_id=:teamId  and p.in_team='1' "
			+ " GROUP by p.player_id order by r.member_type=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,p.player_id desc ");
	
	//球员分享 信息
	private StringBuffer getUplayerinfoInShare_sql = new StringBuffer(
			"select p.player_id playerId,p.user_id userId,u.username username,u.realname realname,u.nickname nickname,u.sex sex ,u.age age "
			+" ,u.height height,u.weight weight,u.remark uRemark,p.remark pRemark,i.imgurl userImgurl,p.can_position canPosition, "
			+" p.number number,p.position position, "
			+" p.practice_foot practiceFoot,p.expert_position expertPosition,t.name teamName,t.team_id teamId,ti.imgurl teamImgurl,t.short_name shortName,t.team_class teamClass "
			+" from u_player p "
			+" LEFT JOIN u_user u on u.user_id=p.user_id "
			+" LEFT JOIN u_user_img i on i.user_id=u.user_id and i.img_size_type='1' and i.uimg_using_type='1' "
			+" left JOIN u_team t on t.team_id=p.team_id and t.team_use_status='2' and t.team_status='3' "
			+" LEFT JOIN u_team_img ti on ti.team_id=t.team_id and ti.img_size_type='1' and ti.timg_using_type='1' "
			+" where p.player_id=:playerId "
			+" group by p.player_id ");
	
	//球员转让时，选择的球员列表
	private StringBuffer transferPlayerList202_sql = new StringBuffer(
			"select p.player_id playerId,u.user_id userId,u.realname realname,u.nickname nickname,u.sex sex,u.age age,p.position position,"
			+" u.height height,u.weight weight,i.imgurl imgurl "
			+" from u_player p LEFT JOIN " 
			+" u_user u on p.user_id=u.user_id "
			+" LEFT JOIN u_user_img i on p.user_id=i.user_id "
			+" where p.team_id=:teamId and p.user_id <> :loginUserId and p.in_team='1' "
			+" group by p.player_id ");
	
	//查询为队长的球员信息
	private StringBuffer getUteamIdByuserId_sql = new StringBuffer("select p.team_id teamId from u_player p,u_player_role r "
			+" where p.player_id=r.player_id and r.member_type='1' and p.in_team='1'"
			+" and r.member_type_use_status='1' and p.user_id=:userId "
			+" and p.team_id is not null "
			+" ORDER BY r.change_date desc");
	
	
	//根据userId查询为非队长对球员Idlist
	private StringBuffer getNoMemberTypeUplayerByUserId_sql = new StringBuffer("select p.player_id playerId,upi.is_unique from u_player p "
			+" left join u_player_role r on r.player_id=p.player_id "
			+" left JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') "
			+" where p.user_id=:userId  and p.in_team='1' and r.member_type_use_status='1' "
			+" group by p.player_id  order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc ");
	
	//根据userId查询球队信息
	private StringBuffer getUTeaminfoforuserId_sql = new StringBuffer("select p.team_id teamId from u_player p "
			+" left join u_player_role r on r.player_id=p.player_id "
			+" left join u_team t on t.team_id=p.team_id and (t.team_use_status='1' or t.team_use_status='2') and (t.team_status='1' or t.team_status='2' or t.team_status='3')"
			+" where p.user_id=:loginUserId and r.member_type='1' and p.in_team='1' and r.member_type_use_status='1' group by p.player_id");
	
	private StringBuffer findPlayerListByType202_sql = new StringBuffer("select * from (select r.key_id keyId,p.player_id playerId,u.user_id userId,t.team_id teamId,t.home_team_shirts homeTeamShirts,p.number number, " 
			+" uui.imgurl userImgurl,u.realname realname,u.nickname nickname,u.sex sex,u.age age,u.birthday birthday,u.height height,u.weight weight,p.position position ,r.member_type "
			+" ,upi.rank_weight,p.adddate,p.addtime from u_player p  "
			+" LEFT JOIN u_player_role r on p.player_id=r.player_id and p.in_team='1' and r.member_type_use_status='1' "
			+" LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type')  "
			+" LEFT JOIN u_user u on u.user_id=p.user_id  "
			+" LEFT JOIN u_user_img uui on u.user_id=uui.user_id and uui.img_size_type='1' and uui.uimg_using_type='1'  "
			+" LEFT JOIN u_team t on t.team_id=p.team_id and t.team_use_status='2' and t.team_status='3'   "
			+" where t.team_id=:teamId and p.in_team='1' "
			+" and p.in_team='1'  "
			+" and (r.member_type in ('1','2','9','11') or r.member_type  is null) "
			+" GROUP BY p.player_id ,r.member_type "
			+" order by  case when ifnull(upi.rank_weight,'')='' then 100 else 111 end desc,upi.rank_weight ASC,"
			+" count(p.player_id)=2 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc) t  "
			+" group by t.playerId "
			+" order by  case when ifnull(t.rank_weight,'')='' then 100 else 111 end desc,t.rank_weight ASC,t.member_type=1 desc,t.member_type=2 desc,t.member_type=9 desc, "
			+" count(t.playerId)=2 desc,case when ifnull(t.adddate,'')='' then t.playerId else 1 end desc,t.adddate desc,t.addtime desc");
	
	//战队详情--官员列表
//	private StringBuffer findManagerListByType202_sql = new StringBuffer("select r.key_id keyId,p.player_id playerId,u.user_id userId,t.team_id teamId,t.home_team_shirts homeTeamShirts, "
//			+" uui.imgurl userImgurl,u.realname realname,u.nickname nickname,u.sex sex,u.age age,u.height height,u.weight weight,p.position position "
// 			+" from u_player_role r  "
//			+" LEFT JOIN u_player p on p.player_id=r.player_id and p.in_team='1' "
//			+" LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') "
//			+" LEFT JOIN u_user u on u.user_id=p.user_id "
//			+" LEFT JOIN u_user_img uui on u.user_id=uui.user_id and uui.img_size_type='1' and uui.uimg_using_type='1' "
//			+" LEFT JOIN u_team t on t.team_id=p.team_id and t.team_use_status='2' and t.team_status='3' "
//			+" where r.member_type_use_status='1' and t.team_id=:teamId and r.member_type in ('3','4','7','8','10','12','13','14')"
//			+" GROUP BY p.player_id order by case when ifnull(upi.rank_weight,'')='' then 100 else 111 end desc,upi.rank_weight ASC,upi.is_unique,r.member_type+0,p.adddate desc ");

	private StringBuffer findManagerListByType202_sql = new StringBuffer("select * from ( "
			+" select r.key_id keyId,p.player_id playerId,u.user_id userId,t.team_id teamId,t.home_team_shirts homeTeamShirts, " 
			+" uui.imgurl userImgurl,u.realname realname,u.nickname nickname,u.sex sex,u.age age,u.birthday birthday,u.height height,u.weight weight,p.position position,upi.rank_weight,p.adddate,p.addtime  "
			+"  from u_player_role r   "
			+" LEFT JOIN u_player p on p.player_id=r.player_id and p.in_team='1' " 
			+" LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') " 
			+" LEFT JOIN u_user u on u.user_id=p.user_id  "
			+" LEFT JOIN u_user_img uui on u.user_id=uui.user_id and uui.img_size_type='1' and uui.uimg_using_type='1' " 
			+" LEFT JOIN u_team t on t.team_id=p.team_id and t.team_use_status='2' and t.team_status='3'  "
			+" where r.member_type_use_status='1' and t.team_id=:teamId "
			+"  and r.member_type in ('3','4','5','6','7','8','10','12','13','14') "
			+"  and p.in_team='1'   "
			+"  GROUP BY p.player_id ,r.member_type  order by upi.rank_weight ASC,upi.is_unique,r.member_type+0,p.adddate desc) t "  
			+"  group by t.playerId  "
			+"  order by  t.rank_weight ASC,case when ifnull(t.adddate,'')='' then t.playerId else 1 end desc,t.adddate desc,t.addtime desc ");
	/**
	 * 
	 * 
	   TODO - 编辑球员信息、编辑有球队的球员【2.0.0.1】
	   @param map
	   		playerId		球员ID
	   		teamId			球队ID【编辑有球队的信息，必输项】
	   @param uUser	
	   		height 			身高
	   		weight 			体重
	   		birthday		生日
	   @param uPlayer
	   		position		球队中位置
	   		memberType		球员用户身份
	   		number			球队中背号
	   		nickname		外号
	   		remark			简介
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		adddate			加入日期
			addtime			加入时间
			exitdate		退出日期
			exittime		退出时间
			inTeam			是否在队
	   @return
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> editPlayerInfo(HashMap<String, String> map)throws Exception {
		UUser uUser=null;
		List<UPlayer> listPlayers=null;
		//判断userId是否为空
		if (publicService.StringUtil(map.get("loginUserId"))) {
			listPlayers = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
			//查询用户个人信息
			 uUser = uUserService.getUserinfoByUserId(map);
			if (null != uUser) {
				//如果有传角色，将角色更新到角色表里
				if (publicService.StringUtil(map.get("memberType"))) {
					if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
						return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本");
					}
				}
				//判断teamId是否为空  不为空
				if (publicService.StringUtil(map.get("teamId"))) {//有球队的球员Id
					this.editplayerInteamId(map,uUser);
				}else{//无球队的球员
					//球队信息为空，判断球员信息是否为空
					if (publicService.StringUtil(map.get("playerId"))) {
						this.editplayerNoInteamIdInPlayerId(map,uUser);
					}else{
						List<UPlayer> listPlayer = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
						if (listPlayer.size() <= 0) {
							UPlayer uPlayer = this.createUplayer(map, uUser, null);
							if (null != uPlayer) {
								//如果有传角色，将角色更新到角色表里
								if (publicService.StringUtil(map.get("memberType"))) {
									if (publicService.StringUtil(map.get("appCode"))) {
										if(WebPublicMehod.intAppCode(map.get("appCode"))<203){
											return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
										}
										if("2.0.2".equals(map.get("appCode"))){
											uPlayerRoleService.setMemberTypeByEditplayer202(map,uPlayer);
										}else{
											if(map.get("listApplyMemberType")!=null||map.get("listSelectMemberType")!=null){
												uPlayerRoleService.editPlayerTeamMemberType(map,uPlayer);
											}
										}
										
									}else{
										return WebPublicMehod.returnRet("error", "请先升级!");
									}
								}
							}
						}else{
							return WebPublicMehod.returnRet("error", "该用户存在球员信息,playerId为空！");
						}
					}
				}
				if (publicService.StringUtil(map.get("imgurl"))) {
					//上传头像
					//将头像上传到用户图片表中
					String msg = publicService.isConnect(map.get("imgurl"));
					if ("".equals(msg) || null == msg) {
						return WebPublicMehod.returnRet("error", "非有效头像url");
					}
				}
				uUserService.inPlayerUpdateUUser(map,uUser);
			}else{
				return WebPublicMehod.returnRet("error", "该用户不存在！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请使用正常用户执行此操作");
		}
		hashMap=new HashMap<>();
		if(listPlayers!=null&&listPlayers.size()>=1){
			UPlayer uPlayer=listPlayers.get(0);
			//保存首次加入球队身价信息
			if(uUser!=null&&publicService.StringUtil(uUser.getRealname())&&publicService.StringUtil(uUser.getNickname())&&publicService.StringUtil(uUser.getSex())&&uUser.getBirthday()!=null&&publicService.StringUtil(uUser.getWeight())&&publicService.StringUtil(uUser.getHeight())){
				if(uPlayer!=null&&publicService.StringUtil(uPlayer.getPracticeFoot())&&publicService.StringUtil(uPlayer.getExpertPosition())&&publicService.StringUtil(uPlayer.getCanPosition())){
					map.put("taskBehavior", "2");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
					hashMap.putAll(uworthService.saveTaskInfo(map));	
				}
			}
		}
		hashMap.put("success", "修改成功！");
		return hashMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 修改无球队Id的球员信息
	   @param map
	   		playerId		球员Id
	   		loginUserId		当前的用户Id
	   @param uUser
	   @return
	   		updatePlayer的resultMap<String,Object>
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	private HashMap<String, Object> editplayerNoInteamIdInPlayerId(HashMap<String, String> map, UUser uUser) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		UPlayer updatePlayer = null;//返回参数
		//查询球员信息
		UPlayer uPlayerTemp = baseDAO.get(map,"from UPlayer where playerId=:playerId");
		if (null != uPlayerTemp) {//不为空更新
			updatePlayer = updatePlayer(map,uPlayerTemp,uUser,uPlayerTemp.getUTeam());
			if (null == updatePlayer) {
				return WebPublicMehod.returnRet("error", "你的背号在该队中已经存在！");
			}
			publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object",map.get("playerId")));		
		}else{//如果根据playerId找不到信息，然后根据userId查找，没找到：新增；找到提示错误信息
			List<UPlayer> listPlayer = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null ");
			if (null != listPlayer && listPlayer.size() > 0) {
				return WebPublicMehod.returnRet("error", "你已存在球员信息！");
			}else{
				updatePlayer = createUplayer(map, uUser, null);
			}
		}
		if (null != updatePlayer) {
			//如果有传角色，将角色更新到角色表里
			if (publicService.StringUtil(map.get("memberType"))) {
				uPlayerRoleService.setMemberTypeByEditplayer202(map,updatePlayer);
			}
		}
		resultMap.put("updatePlayer", updatePlayer);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 修改有球队的球员信息
	   @param map
	   		teamId		球队Id
	   		loginUserId	当前的用户Id
	   @param uUser
	   @return
	   		updatePlayer的resultMap<String,Object>
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	private HashMap<String, Object> editplayerInteamId(HashMap<String, String> map, UUser uUser) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		UPlayer updatePlayer = null;//返回参数
		//查询球队信息
		UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
		if (null != uTeam) {
			//根据teamId和userId查询球员信息
			UPlayer uPlayerTemp = baseDAO.get(map, " from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
			if (null != uPlayerTemp) {//存在：更新；不存在:新增
				updatePlayer = updatePlayer(map, uPlayerTemp,uUser,uTeam);
				if (null == updatePlayer) {
					if (publicService.StringUtil(map.get("number"))) {
						return WebPublicMehod.returnRet("error", "你的背号在该队中已经存在！");
					}
				}else{
					if(WebPublicMehod.intAppCode(map.get("appCode"))<203){
						return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
					}
					if("2.0.2".equals(map.get("appCode"))){
						//如果有传角色，将角色更新到角色表里
						if (publicService.StringUtil(map.get("memberType"))) {
							uPlayerRoleService.setMemberTypeByEditplayer202(map,updatePlayer);
						}
					}else{
						if(map.get("listApplyMemberType")!=null||map.get("listSelectMemberType")!=null){
							uPlayerRoleService.editPlayerTeamMemberType(map,updatePlayer);
						}
					}
				}
			}else{
				return WebPublicMehod.returnRet("error", "你没有加入该球队！");
			}
			resultMap.put("success", "修改成功！");
		}else{//为空：提示
			return WebPublicMehod.returnRet("error", "找不到球队！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 编辑球员信息时更新球员信息
	   @param map	playerId  球员ID
	   @param uPlayer
	   		position		球队中位置
	   		memberType		球员用户身份
	   		number			球队中背号
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	private UPlayer updatePlayer(HashMap<String, String> map, UPlayer uPlayer,UUser uUser,UTeam uTeam) throws Exception {
		if (null != uPlayer.getUTeam()) {
			//修改球员
			if (publicService.StringUtil(map.get("position"))) {
				uPlayer.setPosition(map.get("position"));//球队中的位置
			}
			if (publicService.StringUtil(map.get("number"))) {
				if ("100".equals(map.get("number"))) {
					uPlayer.setNumber(null);
				}else{
					//判断球队中是否存在该位置
					boolean isNumber = isExitUteamNumber(map,uTeam);
					if (isNumber == false) {
						uPlayer.setNumber(Integer.parseInt(map.get("number")));//背号
					}else{
						uPlayer = null;
						return uPlayer;
					}
				}
			}
		}
		baseDAO.update(uPlayer);
		updateNoTeamPlayerinfo(map,uUser);//将可踢位置，擅长位置 惯用脚 更新到厨师求援信息中
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO -  新增球员信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户Id
	   		position		球队中位置
	   		number			球队中背号
	   		memberType		球队中身份
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   @param uUser
	   		uUser对象
	   @param uTeam
	   		uTeam对象
	   @return
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	@Override
	public UPlayer createUplayer(HashMap<String, String> map,UUser uUser,UTeam uTeam) throws Exception {
		UPlayer uPlayerTemp = null;
		if (null != uUser) {
			map.put("userId", uUser.getUserId());
			uPlayerTemp = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null");
			if (null == uPlayerTemp) {
				uPlayerTemp = new UPlayer();
				uPlayerTemp.setPlayerId(WebPublicMehod.getUUID());
				uPlayerTemp.setUUser(uUser);
				uPlayerTemp.setAdddate(new Date());
				uPlayerTemp.setAddtime(new Date());
				if ( null != uTeam) {
					uPlayerTemp.setUTeam(uTeam);
					uPlayerTemp.setTeamBelonging("1");//有球队
					uPlayerTemp.setInTeam("1");//在队
					uPlayerTemp.setMemberType("2");
				}else{
					uPlayerTemp.setInTeam("2");//在队
					uPlayerTemp.setTeamBelonging("2");//无球队
				}
				if (publicService.StringUtil(map.get("position"))) {
					uPlayerTemp.setPosition(map.get("position"));//球队中的位置
				}
				if (publicService.StringUtil(map.get("number"))) {
					//判断球队中是否存在该位置
					boolean isNumber = isExitUteamNumber(map,uTeam);
					if (isNumber == false) {
						uPlayerTemp.setNumber(Integer.parseInt(map.get("number")));//背号
					}else{
						uPlayerTemp = new UPlayer();
						return uPlayerTemp;
					}
					uPlayerTemp.setNumber(Integer.parseInt(map.get("number")));//背号
				}
				uPlayerTemp.setAddqd("APP");
				baseDAO.save(uPlayerTemp);
			}
			this.updateNoTeamPlayerinfo(map,uUser);//将可踢位置，擅长位置 惯用脚 更新到初始球员信息中 
		}
		return uPlayerTemp;
	}

	/**
	 * 
	 * 
	   TODO - 将可踢位置，擅长位置 惯用脚 更新到初始求援信息中
	   @param map
	   		loginUserId			当前用户Id
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void updateNoTeamPlayerinfo(HashMap<String, String> map, UUser uUser) throws Exception {
		if (null != uUser) {
			map.put("userId", uUser.getUserId());
			UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null ");//查看是否存在初始信息
			if (null != uPlayer) {//存在:只做更新
				if (null != map.get("practiceFoot") && !"".equals(map.get("practiceFoot"))) {
					uPlayer.setPracticeFoot(map.get("practiceFoot"));//惯用脚
				}
				if (publicService.StringUtil(map.get("expertPosition"))) {
					uPlayer.setExpertPosition(map.get("expertPosition"));//擅长位置
				}
				if (publicService.StringUtil(map.get("canPosition"))) {
					uPlayer.setCanPosition(map.get("canPosition"));//可踢位置
				}
				baseDAO.update(uPlayer);
			}else{//存在:新增
				uPlayer = new UPlayer();
				uPlayer.setPlayerId(WebPublicMehod.getUUID());
				uPlayer.setUUser(uUser);
				uPlayer.setTeamBelonging("2");
				uPlayer.setInTeam("2");
				if (publicService.StringUtil(map.get("practiceFoot"))) {
					uPlayer.setPracticeFoot(map.get("practiceFoot"));//惯用脚
				}
				if (publicService.StringUtil(map.get("expertPosition"))) {
					uPlayer.setExpertPosition(map.get("expertPosition"));//擅长位置
				}
				if (publicService.StringUtil(map.get("canPosition"))) {
					uPlayer.setCanPosition(map.get("canPosition"));//可踢位置
				}
				uPlayer.setAddqd("APP");
				baseDAO.save(uPlayer);
			}
			//更新lbs
			uUserService.setAreaToBaiduLBS(uUser);
			//添加成为球员里程碑事件
			this.setUUserBehavior(uUser,map,uPlayer);
		}
	}

	/**
	 * 
	 * 
	   TODO - 设置成为球员事件
	   @param uUser
	   2016年8月12日
	   dengqiuru
	 * @throws Exception 
	 */
	private void setUUserBehavior(UUser uUser,HashMap<String, String> map,UPlayer uPlayer) throws Exception {
		map.put("type", "1");
		map.put("behaviorType", "6");
		map.put("objectId", uPlayer.getPlayerId());
		map.put("userId", uUser.getUserId());
		publicService.updateBehavior(map);
	}

	/**
	 * 
	 * 
	   TODO - 根据userId查询球员是否为队长
	   @param map:
	   		loginUserId		当前用户
	   		teamId			球队Id
	   @return
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	@Override
	public boolean isHeaderByUserId(HashMap<String, String> map,String type) throws Exception {
		boolean isHeader = false;
		UPlayer uPlayer = new UPlayer();
		if ("1".equals(type)) {//所有球队查找
			uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and inTeam='1'");
		}else if ("2".equals(type)){//本球队查找
			uPlayer = baseDAO.get(map, "from UPlayer up where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
		}
		if (null != uPlayer) {
			map.put("playerId", uPlayer.getPlayerId());
			List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
			if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
				for (UPlayerRole uPlayerRole : uPlayerRoles) {
					if ("1".equals(uPlayerRole.getMemberType())) {
						isHeader = true;
						break;
					}
				}
			}
		}
		return isHeader;
	}
	
	/**
	 * 
	 * 
	   TODO - 加入球队，新增球员信息 【2.0.0】
	   @param uUser
	   		uUser对象
	   @param uTeam
	   		uTeam对象
	   @param memberType
	   		1：队长；2：队员
	   @param map
	   		position		球队中位置
	   		number			球队中背号
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		memberType      球员用户身份  1.队长、 2.总经理、3.财务后勤、4.主教练、5.队员、6.助理教练、7.队医、8.新闻官、9.啦啦队员、10.赞助商
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	@Override
	public UPlayer insertPlayerInTeam(UUser uUser, UTeam uTeam,HashMap<String, String> map) throws Exception {
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
		if (null == uPlayer) {
			uPlayer = new UPlayer();
			uPlayer.setPlayerId(WebPublicMehod.getUUID());
			uPlayer.setUUser(uUser);
			uPlayer.setUTeam(uTeam);
			//球队背号
			if (publicService.StringUtil(map.get("number"))) {
				if ("100".equals(map.get("number"))) {
					uPlayer.setNumber(null);
				}else{
					//判断球队中是否存在该位置
					boolean isNumber = isExitUteamNumber(map,uTeam);
					if (isNumber == false) {
						uPlayer.setNumber(Integer.parseInt(map.get("number")));
					}else{
						uPlayer = null;
						return uPlayer;
					}
				}
			}else{
				uPlayer.setNumber(null);
			}
			//球队位置
			if (publicService.StringUtil(map.get("position"))) {
				uPlayer.setPosition(map.get("position"));
			}
			uPlayer.setAddqd("APP");
			baseDAO.save(uPlayer);
		}
		//修改初始球员信息
		this.updateNoTeamPlayerinfo(map, uUser);
		//更新生日、体重、身高
		uUserService.inPlayerUpdateUUser(map, uUser);
		//修改球员信息的基础信息
		this.updateUplayer(uPlayer,uUser,uTeam);
		
		return uPlayer;
	}
	
	/**
	 * 
	 * 
	   TODO - 设置球员基本信息
	   @param uPlayer
	   @param uUser
	   @param uTeam
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	private void updateUplayer(UPlayer uPlayer, UUser uUser, UTeam uTeam) throws Exception {
		uPlayer.setTeamBelonging("1");
		uPlayer.setAdddate(new Date());
		uPlayer.setAddtime(new Date());
		uPlayer.setInTeam("1");
		baseDAO.update(uPlayer);
		
	}

	/**
	 * 
	 * 
	   TODO - 判断该背号在球队中是否存在
	   @param map
	   		loginUserId		当前用户
	   		number			背号
	   @param uTeam
	   2016年3月7日
	   dengqiuru
	 * @throws Exception 
	 */
	private Boolean isExitUteamNumber(HashMap<String, String> map, UTeam uTeam) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("teamId", uTeam.getTeamId());
		hashMap.put("number", Integer.parseInt(map.get("number")));
		hashMap.put("userId", map.get("loginUserId"));
		boolean isNumber = false;
		UPlayer uPlayer = baseDAO.get("from UPlayer where UTeam.teamId=:teamId and number=:number and inTeam='1' and UUser.userId<>:userId",hashMap);
		if (null != uPlayer) {
			isNumber = true;
		}
		return isNumber;
	}

	/**
	 * 
	 * 
	   TODO - 加入球队时，查询该用户是否存在该球队
	   @param map
	   		loginUserId		当前用户
	   		teamId			球队Id
	   		playerId		球员Id
	   @return
	   @throws Exception
	   2016年2月1日
	   dengqiuru
	 */
	@Override
	public UPlayer getUteamUplayerinfoByTeamId(HashMap<String, String> map,String type) throws Exception {
		UPlayer uPlayer = new UPlayer();
		if ("1".equals(type)) {//根据用户Id和teamId查找球员信息
			uPlayer = baseDAO.get(map, "from UPlayer up where UUser.userId=:loginUserId and UTeam.teamId=:teamId and in_team='1'");
		}else if ("2".equals(type)) {//根据playerId和teamId查找是否为该球队队员
			uPlayer = baseDAO.get(map, "from UPlayer up where playerId=:playerId and UTeam.teamId=:teamId and in_team='1'");
		}else if ("3".equals(type)) {//根据用户Id和teamId查找球员信息,查看是否曾经加入过该球队
			uPlayer = baseDAO.get(map, "from UPlayer up where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
		}else if ("4".equals(type)) {//根据用户Id和teamId查找球员信息,查看是否曾经加入过该球队
			uPlayer = baseDAO.get(map, "from UPlayer up where UUser.userId=:loginUserId and UTeam.teamId=:teamId and in_team='2'");
		}
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 根据球员Id获取球员信息，以及他所在的球队列表
	   @param map
	   		page		分页
	   		playerId	球员Id
	   @return
	   @throws Exception
	   2016年1月29日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUTeamListByPlayerId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		PageLimit pageLimit = new PageLimit(Integer.parseInt(map.get("page")), 0);
		if (publicService.StringUtil(map.get("playerId"))) {
			UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
			resultMap.put("uPlayer", uPlayer);
			if (null != uPlayer) {
				map.put("userId", uPlayer.getUUser().getUserId());
				String hql = "from UPlayer where UTeam.teamId is not null order by adddate desc,addtime desc";
				List<UPlayer> uPlayerlist = baseDAO.findByPage(map, hql, pageLimit.getLimit(), pageLimit.getOffset());
				List<UTeam> uTeamList = uTeamService.getUteamNameList(uPlayerlist);
				resultMap.put("uTeamList", uTeamList);
			}else{
				return WebPublicMehod.returnRet("error", "球员不存在！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "playerId不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 获取球员列表 【2.0.0】
	   @param map
	   		page		分页
	   		searchStr	搜索内容
	   @return
	   		uPlayerList的hashMap<String,Object>
	   @throws Exception
	   2016年1月29日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUPlayerList(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, String> hashTemp=null;
		OutPlayerList outPlayerList = new OutPlayerList();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		ArrayList<Object> returnList = new ArrayList<Object>();
		UPlayer uPlayer = getUplayerByUserId(map);
		if (null != uPlayer) {
			outPlayerList = setOutPlayerList(null,uPlayer,map);
		}
		resultMap.put("outPlayerList", outPlayerList);
		StringBuffer newSql = getUPlayerListSql(map);
		List<HashMap<String, Object>> listTeam=new ArrayList<HashMap<String, Object>>();
		if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				hashMap.put("teamId", map.get("teamId"));
				if ("1".equals(map.get("type"))) {
					HashMap<String, Object> teamStatusMap = uTeamService.checkAllUTeamStatus(map);
					if ("3".equals(teamStatusMap.get("success").toString())) {
						return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
					}
				}
			}
		}
		if ("1".equals(map.get("type"))) {//1:代表邀请人的球员列表
//			//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
//			map.put("type", "8");
//			List<HashMap<String, Object>> playerIdList = uPlayerRoleLimitService.playerIsRoleByType(map);
//			if (null != playerIdList && playerIdList.size() > 0) {
			String ordersql = " order by  case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ";
			if (publicService.StringUtil(map.get("searchStr"))) {//搜索
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append(" and (u.nickname like :searchStr or u.realname like :searchStr ) ");
			}
			newSql.append(" group by p.user_id  ");
//			if(publicService.StringUtil(map.get("location"))){
//				String inSql=lbsScreenInSql(page,li, map);//拼接lbs sql
//				if(inSql.length()>0){
//					newSql.append(" and  "+inSql);
//					ArrayList<Object> newli = (ArrayList<Object>) baseDAO.findSQLMap(map,newLbsSql.toString(),mapList);
//					if (null != newli && newli.size() > 0) {
//						for (int index = 0; index < newli.size(); index++) { 
//							hashTemp = (HashMap<String, String>) newli.get(index);
//							hashTemp.put("is_my", checkTeamIsMy(hashTemp,arrayTeamList));
//							returnList.add(hashTemp);
//						}
//					}
//				}
//			}else{
////				locationSql=this.lbsOrderSql(map,"team");
////				if(locationSql!=null&&!"".equals(locationSql)){
////					sql.append("field(t.team_id,"+locationSql+"),");
////				}
//			}
			newSql.append(" order by  case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
				
//			}else{
//				return WebPublicMehod.returnRet("error","你的权限不足\n暂时不能邀请球员");
//			}
		}else if ("2".equals(map.get("type"))) {//管理球员列表
			if (publicService.StringUtil(map.get("searchStr"))) {//搜索
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append(" and (u.nickname like :searchStr or u.realname like :searchStr ) group by p.player_id  order by r.member_type,p.player_id desc,p.adddate desc,p.addtime desc ");
			}else{
				newSql.append(" group by p.player_id  order by r.member_type,p.player_id desc ,p.adddate desc,p.addtime desc ");
			}
		}else{//其他
			if (publicService.StringUtil(map.get("searchStr"))) {//搜索
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append(" and (u.nickname like :searchStr or u.realname like :searchStr )  group by p.user_id  order by abs(month(now())-month(u.createdate)),abs(day(now())-day(u.createdate)),p.team_id asc ");
			}else{
				newSql.append(" group by p.user_id  order by abs(month(now())-month(u.createdate)),abs(day(now())-day(u.createdate)),p.team_id asc ");
			}
		}
		

//		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,newSql.toString());
//		if(publicService.StringUtil(map.get("location"))){
//			String inSql=lbsScreenInSql(page,li, map);//拼接lbs sql
//			if(inSql.length()>0){
//				newSql.append(" and  "+inSql);
//				ArrayList<Object> newli = (ArrayList<Object>) baseDAO.findSQLMap(map,newSql.toString());
//				if (null != newli && newli.size() > 0) {
//					for (int index = 0; index < newli.size(); index++) { 
//						hashTemp = (HashMap<String, String>) newli.get(index);
//						hashTemp.put("is_my", checkTeamIsMy(hashTemp,arrayTeamList));
//						listTeam.add(hashTemp);
//					}
//				}
//			}
//		}else{
////			locationSql=this.lbsOrderSql(map,"team");
////			if(locationSql!=null&&!"".equals(locationSql)){
////				sql.append("field(t.team_id,"+locationSql+"),");
////			}
//		}
		//分页
		List<Object> listPlayer = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if(null != map.get("page")&& !"".equals(map.get("page"))){
			hashMap.put("teamId", map.get("teamId"));
			listTeam = this.getPageLimit(newSql.toString(), map, hashMap);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					String playerId = hashMap2.get("playerId").toString();
					listPlayer.add(playerId);
					//身价
					map.put("userId", (String)hashMap2.get("userId"));
					hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
				}
			}
			//角标
			if (null != listPlayer && listPlayer.size() > 0) {
				cornerList = cornerService.getPlayerCorner(map, listPlayer);
			}
		}
		resultMap.put("outPlayerLists", listTeam);
		resultMap.put("cornerList", cornerList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 球员列表  查询语句   查询信息
	   @param map
	   		teamId			球队Id
	   		type			1：邀请列表  2：管理球员列表
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private StringBuffer getUPlayerListSql(HashMap<String, String> map) throws Exception {
		StringBuffer newSql=new StringBuffer();
		String sql = "select p.user_id userId,p.player_id playerId,u.sex sex,u.nickname nickname,u.realname realname,u.age age,u.height height,u.weight weight,u.birthday birthday,  "
				+" u.remark remark,p.expert_position expertPosition,p.practice_foot practiceFoot, "
				+" p.can_position canPosition,p.position position,p.number number,i.imgurl imgurl  from u_player p "
				+" LEFT JOIN u_user u on u.user_id=p.user_id "
				+" LEFT JOIN u_user_img i on u.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' ";
		if (publicService.StringUtil(map.get("teamId"))) {
			if ("1".equals(map.get("type"))) {//1:代表邀请人的球员列表
				newSql.append( sql + " where p.user_id not in (select pp.user_id from u_player pp where pp.team_id=:teamId and pp.in_team='1')  and p.team_id is null ");
			}else if ("2".equals(map.get("type"))){//2:代表管理球员的球员列表
				newSql.append( sql + "LEFT JOIN u_player_role r on p.player_id=r.player_id and p.in_team='1' and r.member_type_use_status='1'"
				+" LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') "
				+ " where p.team_id=:teamId and p.in_team='1' ");
			}
		}else{
			newSql.append( sql + " where p.team_id is null and u.user_id is not null ");
		}
		return newSql;
	}

	/**
	 * 
	 * TODO 分页代码
	 * @param sql  语句
	 * @param hashMap 查询参数
	 * @param map 分页参数
	 * @param count 总记录数
	 * @return
	 * @throws Exception
	 * List<Object>
	 * xiao
	 * 2016年3月8日
	 */
	private List<HashMap<String, Object>> getPageLimit(String sql, HashMap<String, String> map,HashMap<String, Object> hashMap) throws Exception{
		hashMap.put("page", map.get("page"));
		PageLimit pa = new PageLimit(Integer.parseInt(map.get("page")), 0);
		hashMap.put("limit", pa.getLimit());
		hashMap.put("offset", pa.getOffset());
		StringBuffer newSql = new StringBuffer();
		newSql.append( sql + " limit :limit offset :offset" );
		List<HashMap<String, Object>>  list = baseDAO.findSQLMap(newSql.toString(),hashMap);
		// 更改类型和状态的页面显示值
		if (list != null && list.size() > 0) {
			for (HashMap<String, Object> hashMap2 : list) {
				this.displayData(hashMap2,map);
				//填充角色
				uPlayerRoleService.setMembertype202(hashMap2);
			}
		}
		return list;
	}
	/**
	 * 
	 * TODO 位置处理【2.0.0】
	 * @param hashMap
	 * 		memberType		角色
	 * 		position		位置
	 * 		age				年龄
	 * 		userId			用户Id
	 * 		practiceFoot	惯用脚
	 * 		expertPosition	擅长位置
	 * 		canPosition		可踢位置
	 * 		sex				性别
	 * 		followStatus	关注状态
	 * @param map
	   		loginUserId		当前用户
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	@Override
	public void displayData(HashMap<String, Object> hashMap, HashMap<String, String> map) throws Exception {
		String objectName = null;
		String object = null;
		if (null != hashMap) {
//			//身份
//			if (hashMap.get("memberType") != null && !"".equals(hashMap.get("memberType"))) {
//				hashMap.put("memberTypeName", Public_Cache.HASH_PARAMS("member_type").get(hashMap.get("memberType")));
//			}else{
//				hashMap.put("memberTypeName", null);
//			}
			//位置
			if (hashMap.get("position") != null && !"".equals(hashMap.get("position"))) {
				if (!"16".equals(hashMap.get("position"))) {
					hashMap.put("positionName",Public_Cache.HASH_PARAMS("position").get(hashMap.get("position")));
				}else{
					hashMap.put("positionName", null);
				}
			}else{
				hashMap.put("positionName", null);
			}
			//年龄
			if (publicService.isNullOrEmpty(hashMap.get("age"))) {
				if (null != hashMap.get("birthday")) {
					hashMap.put("age",getAgeByBirthday(PublicMethod.getStringToDate(hashMap.get("birthday").toString(), "yyyy-MM-dd")));
				}
			}
			if (null != hashMap.get("userId")) {//设置初始球员的信息
				if (hashMap.get("practiceFoot") != null) {//如果查询的惯用脚不为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);//查看当前用户的初始信息
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getPracticeFoot())) {
							object = uPlayerTemp.getPracticeFoot();
							if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
								objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("practiceFoot", object);//惯用脚
					hashMap.put("practiceFootName", objectName);
				}else{//为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getPracticeFoot())) {
							object = uPlayerTemp.getPracticeFoot();
							if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
								objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("practiceFoot", object);
					hashMap.put("practiceFootName", objectName);
				}
				if (hashMap.get("expertPosition") != null) {//查询擅长位置不为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);//查看当前用户的初始信息
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getExpertPosition())) {
							object = uPlayerTemp.getExpertPosition();
							if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
								objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("expertPosition", object);
					hashMap.put("expertPositionName", objectName);
				}else{//为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getExpertPosition())) {
							object = uPlayerTemp.getExpertPosition();
							if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
								objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("expertPosition", object);
					hashMap.put("expertPositionName", objectName);
				}
				if (hashMap.get("canPosition") != null) {//可踢位置是否为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);//查看当前用户的初始信息
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getCanPosition())) {
							object = uPlayerTemp.getCanPosition();
							if (!"15".equals(uPlayerTemp.getCanPosition())) {
								objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("canPosition", object);
					hashMap.put("canPositionName", objectName);
				}else{//为空
					UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);
					if (null != uPlayerTemp) {
						if (publicService.StringUtil(uPlayerTemp.getCanPosition())) {
							object = uPlayerTemp.getCanPosition();
							if (!"15".equals(uPlayerTemp.getCanPosition())) {
								objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
							}else{
								objectName = null;
							}
						}else{
							object = null;
							objectName = null;
						}
					}else{
						object = null;
						objectName = null;
					}
					hashMap.put("canPosition", object);
					hashMap.put("canPositionName", objectName);
				}
			}else{
				hashMap.put("practiceFoot", null);
				hashMap.put("practiceFootName", null);
				hashMap.put("expertPosition", null);
				hashMap.put("expertPositionName", null);
				hashMap.put("canPosition", null);
				hashMap.put("canPositionName", null);
			}
			if (hashMap.get("sex") != null) {//性别
				hashMap.put("sexName",Public_Cache.HASH_PARAMS("sex").get(hashMap.get("sex")));
			}else{
				hashMap.put("sexName", null);
			}
			if (hashMap.get("followStatus") != null) {//关注状态
				hashMap.put("isFollow",hashMap.get("followStatus"));
			}else{
				hashMap.put("isFollow","2");
			}
			if (null != map) {//设置状态
				String isMyself = "2";
				if (publicService.StringUtil(map.get("loginUserId"))) {
					if (hashMap.get("userId") != null) {
						String loginUserId = map.get("loginUserId");
						if (null != hashMap.get("userId")) {
							String userId = hashMap.get("userId").toString();
							if (loginUserId.equals(userId)) {
								isMyself = "1";
							}
						}
					}
				}
				hashMap.put("isMyself", isMyself);//是否是我的球队
			}else{
				hashMap.put("isMyself","2");
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 将球员信息添加到输出球员信息 【2.0.0】
	   @param uPlayer
	   		uPlayer		 uPlayer
	   @param map
	   		loginUserId		  当前用户Id
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	@Override
	public OutPlayerList setOutPlayerList(UUser uUser,UPlayer uPlayer,HashMap<String, String> map) throws Exception {
		OutPlayerList outPlayerList = new OutPlayerList();
		String objectName = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			map.put("userId", map.get("loginUserId"));
		}
		if (null != uPlayer) {
			outPlayerList.setPlayerId(uPlayer.getPlayerId());//球员Id
			baseDAO.getSessionFactory().getCurrentSession().flush();
			UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
			if (null != uUserImg) {
				outPlayerList.setImgurl(uUserImg.getImgurl());//头像
			}
			HashMap<String, Object> worthMap = uworthService.findUserIdWorthCount(map);//该球员的身价
			if (null != worthMap) {
				if (null != worthMap.get("count")) {
					outPlayerList.setWorthCount(worthMap.get("count")+"");
				}
			}
			//关注数
			HashMap<String, Object> followMap = new HashMap<>();
			followMap.put("userId", uPlayer.getUUser().getUserId());
			Integer followCount = this.pubGetUserFollowCountByUserId(followMap);
			if (null != worthMap) {
				outPlayerList.setFollowCount(followCount);
			}
			if (null != uPlayer.getUUser()) {
				if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
					outPlayerList.setRealname(uPlayer.getUUser().getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
					outPlayerList.setNickname(uPlayer.getUUser().getNickname());//昵称
				}
				if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
					outPlayerList.setSex(uPlayer.getUUser().getSex());//性别
					String sex = uPlayer.getUUser().getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uPlayer.getUUser().getAge() != null) {
					outPlayerList.setAge(uPlayer.getUUser().getAge().toString());//年龄
				}else{
					if (uPlayer.getUUser().getBirthday() != null) {
						outPlayerList.setAge(getAgeByBirthday(uPlayer.getUUser().getBirthday()).toString());//年龄
					}
				}
				if (publicService.StringUtil(uPlayer.getUUser().getWeight())) {
					outPlayerList.setWeight(uPlayer.getUUser().getWeight());//体重
				}
				if (uPlayer.getUUser().getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uPlayer.getUUser().getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uPlayer.getUUser().getHeight())) {
					outPlayerList.setHeight(uPlayer.getUUser().getHeight());//身高
				}
				if (publicService.StringUtil(uPlayer.getUUser().getRemark())) {
					outPlayerList.setRemark(uPlayer.getUUser().getRemark());//备注
				}
			}
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
						outPlayerList.setExpertPositionName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
						outPlayerList.setExpertPositionName(objectName);
					}
				}
			}
			if (publicService.StringUtil(uPlayer.getCanPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					if (!"15".equals(uPlayerTemp.getCanPosition())) {
						outPlayerList.setCanPositionName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					if (!"15".equals(uPlayerTemp.getCanPosition())) {
						outPlayerList.setCanPositionName(objectName);
					}
				}
			}
			if (publicService.StringUtil(uPlayer.getPracticeFoot())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
						outPlayerList.setPracticeFootName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
						outPlayerList.setPracticeFootName(objectName);
					}
				}
			}
			if (null != uPlayer.getNumber()) {
				outPlayerList.setNumber(uPlayer.getNumber().toString());//背号
			}
//			if (publicService.StringUtil(uPlayer.getMemberType())) {
//				outPlayerList.setMemberType(uPlayer.getMemberType());//球员身份
//				objectName = Public_Cache.HASH_PARAMS("member_type").get(uPlayer.getMemberType());
//				outPlayerList.setMemberTypeName(objectName);
//			}
			if (publicService.StringUtil(uPlayer.getPosition())) {
				if (!"16".equals(uPlayer.getPosition())) {
					objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				}else{
					objectName = null;
				}
				outPlayerList.setPosition(uPlayer.getPosition());//位置
				outPlayerList.setPositionName(objectName);
			}
			if (null != uPlayer.getUUser()) {//设置是否是我的球队
				if (publicService.StringUtil(map.get("loginUserId"))) {
					map.put("userId", map.get("loginUserId"));
					if (map.get("userId").equals(uPlayer.getUUser().getUserId())) {
						outPlayerList.setIsMyself("1");
					}else{
						outPlayerList.setIsMyself("2");
					}
				}else{
					outPlayerList.setIsMyself("2");
				}
			}else{
				outPlayerList.setIsMyself("2");
			}
		}else{
			if (null != uUser) {
				baseDAO.getSessionFactory().getCurrentSession().flush();
				UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
				if (null != uUserImg) {
					outPlayerList.setImgurl(uUserImg.getImgurl());//头像
				}
				if (publicService.StringUtil(uUser.getRealname())) {
					outPlayerList.setRealname(uUser.getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uUser.getNickname())) {
					outPlayerList.setNickname(uUser.getNickname());//昵称
				}
				if (publicService.StringUtil(uUser.getSex())) {
					outPlayerList.setSex(uUser.getSex());//性别
					String sex = uUser.getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uUser.getAge() != null) {
					outPlayerList.setAge(uUser.getAge().toString());//年龄
				}
				if (publicService.StringUtil(uUser.getWeight())) {
					outPlayerList.setWeight(uUser.getWeight());//体重
				}
				if (uUser.getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uUser.getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uUser.getHeight())) {
					outPlayerList.setHeight(uUser.getHeight());//身高
				}
				if (publicService.StringUtil(uUser.getRemark())) {
					outPlayerList.setRemark(uUser.getRemark());//备注
				}
			}
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO - 将球员信息添加到输出球员信息 【2.0.0】
	   @param uPlayer
	   		uPlayer		 uPlayer
	   @param map
	   		loginUserId		  当前用户Id
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	private OutPlayerList setOutPlayerListInTeam(UUser uUser,UPlayer uPlayer,HashMap<String, String> map) throws Exception {
		OutPlayerList outPlayerList = new OutPlayerList();
		String objectName = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			map.put("userId", map.get("loginUserId"));
		}
		if (null != uPlayer) {
			outPlayerList.setPlayerId(uPlayer.getPlayerId());//球员Id
			baseDAO.getSessionFactory().getCurrentSession().flush();
			UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
			if (null != uUserImg) {
				outPlayerList.setImgurl(uUserImg.getImgurl());//头像
			}
			HashMap<String, Object> worthMap = uworthService.findUserIdWorthCount(map);//该球员的身价
			if (null != worthMap) {
				if (null != worthMap.get("count")) {
					outPlayerList.setWorthCount(worthMap.get("count")+"");
				}
			}
			//关注数
			HashMap<String, Object> followMap = new HashMap<>();
			followMap.put("userId", uPlayer.getUUser().getUserId());
			Integer followCount = this.pubGetUserFollowCountByUserId(followMap);
			if (null != worthMap) {
				outPlayerList.setFollowCount(followCount);
			}
			if (null != uPlayer.getUUser()) {
				if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
					outPlayerList.setRealname(uPlayer.getUUser().getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
					outPlayerList.setNickname(uPlayer.getUUser().getNickname());//昵称
				}
				if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
					outPlayerList.setSex(uPlayer.getUUser().getSex());//性别
					String sex = uPlayer.getUUser().getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uPlayer.getUUser().getAge() != null) {
					outPlayerList.setAge(uPlayer.getUUser().getAge().toString());//年龄
				}else{
					if (uPlayer.getUUser().getBirthday() != null) {
						outPlayerList.setAge(getAgeByBirthday(uPlayer.getUUser().getBirthday()).toString());//年龄
					}
				}
				if (publicService.StringUtil(uPlayer.getUUser().getWeight())) {
					outPlayerList.setWeight(uPlayer.getUUser().getWeight());//体重
				}
				if (uPlayer.getUUser().getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uPlayer.getUUser().getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uPlayer.getUUser().getHeight())) {
					outPlayerList.setHeight(uPlayer.getUUser().getHeight());//身高
				}
				if (publicService.StringUtil(uPlayer.getUUser().getRemark())) {
					outPlayerList.setRemark(uPlayer.getUUser().getRemark());//备注
				}
			}
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					outPlayerList.setExpertPositionName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					outPlayerList.setExpertPositionName(objectName);
				}
			}
			if (publicService.StringUtil(uPlayer.getCanPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					outPlayerList.setCanPositionName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					outPlayerList.setCanPositionName(objectName);
				}
			}
			if (publicService.StringUtil(uPlayer.getPracticeFoot())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					outPlayerList.setPracticeFootName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					outPlayerList.setPracticeFootName(objectName);
				}
			}
			if (null != uPlayer.getNumber()) {
				outPlayerList.setNumber(uPlayer.getNumber().toString());//背号
			}
			if (publicService.StringUtil(uPlayer.getPosition())) {
				objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				outPlayerList.setPosition(uPlayer.getPosition());//位置
				outPlayerList.setPositionName(objectName);
			}
			if (null != uPlayer.getUUser()) {//设置是否是我的球队
				if (publicService.StringUtil(map.get("loginUserId"))) {
					map.put("userId", map.get("loginUserId"));
					if (map.get("userId").equals(uPlayer.getUUser().getUserId())) {
						outPlayerList.setIsMyself("1");
					}else{
						outPlayerList.setIsMyself("2");
					}
				}else{
					outPlayerList.setIsMyself("2");
				}
			}else{
				outPlayerList.setIsMyself("2");
			}
		}else{
			if (null != uUser) {
				baseDAO.getSessionFactory().getCurrentSession().flush();
				UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
				if (null != uUserImg) {
					outPlayerList.setImgurl(uUserImg.getImgurl());//头像
				}
				if (publicService.StringUtil(uUser.getRealname())) {
					outPlayerList.setRealname(uUser.getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uUser.getNickname())) {
					outPlayerList.setNickname(uUser.getNickname());//昵称
				}
				if (publicService.StringUtil(uUser.getSex())) {
					outPlayerList.setSex(uUser.getSex());//性别
					String sex = uUser.getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uUser.getAge() != null) {
					outPlayerList.setAge(uUser.getAge().toString());//年龄
				}
				if (publicService.StringUtil(uUser.getWeight())) {
					outPlayerList.setWeight(uUser.getWeight());//体重
				}
				if (uUser.getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uUser.getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uUser.getHeight())) {
					outPlayerList.setHeight(uUser.getHeight());//身高
				}
				if (publicService.StringUtil(uUser.getRemark())) {
					outPlayerList.setRemark(uUser.getRemark());//备注
				}
			}
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO - 根据生日计算年龄
	   @param birthday
	   @return
	   2016年6月2日
	   dengqiuru
	 */
	private Integer getAgeByBirthday(Date birthday) {
		Integer age;
		if (null == birthday) {
			age = null;
		}else{
			Calendar cal = Calendar.getInstance();

			if (cal.before(birthday)) {
				throw new IllegalArgumentException(
						"The birthDay is before Now.It's unbelievable!");
			}

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					// monthNow==monthBirth 
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					// monthNow>monthBirth 
					age--;
				}
			}
		}
		
		return age;
	}
	/**
	 * 
	 * 
	   TODO - 查询当前用户的初始球员信息
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   		uPlayer	对象
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private UPlayer getUplayerByuserIdNoInteam(HashMap<String, String> map) throws Exception {
		UPlayer uPlayer = null;
		if (null != map) {
			if (publicService.StringUtil(map.get("loginUserId"))) {
				uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
			}
		}
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 查询固定用户的初始信息
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private UPlayer getUplayerByuserIdInteam(HashMap<String, Object> map) throws Exception {
		UPlayer uPlayer = null;
		if (null != map) {
			if (null != map.get("userId")) {
				uPlayer = baseDAO.get("from UPlayer where UUser.userId=:userId and UTeam.teamId is null",map);
			}
		}
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 将头像填充到球员中
	   @param uPlayer	对象
	   @throws Exception
	   2016年3月8日
	   dengqiuru
	 */
	public void setUUserImg(UPlayer uPlayer) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		Set<UUserImg> uUserImg = new HashSet<>();
		if (uPlayer.getUUser().getUserId() != null && !"".equals(uPlayer.getUUser().getUserId())) {
			map.put("userId", uPlayer.getUUser().getUserId());
			uUserImg = uUserImgService.getHeadPicByuserId(map);//获取每个球员的头像
			UUser uUser = uPlayer.getUUser();
			uUser.setUUserImg(uUserImg);//先将头像set到user对象中
			uPlayer.setUUser(uUser);//最后将用户set到球员中
		}
	}

	/**
	 * 
	 * 
	   TODO - 球员列表--头部信息
	   @param uPlayer	对象
	   @throws Exception
	   2016年3月8日
	   dengqiuru
	 */
	public UPlayer getUplayerByUserId(HashMap<String, String> map) throws Exception {
		UPlayer uPlayer = null;
		String playerId = null;
		//当前球队的球员信息
		if (publicService.StringUtil(map.get("teamId"))) {
			uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and in_team='1' ");
		}else{
			if (publicService.StringUtil(map.get("loginUserId"))) {
				//为队长的信息
				map.put("userId", map.get("loginUserId"));
				List<HashMap<String, Object>> playerIdList = this.getMemberTypeUplayerByUserId(map);
				if (null != playerIdList && playerIdList.size() > 0) {
					playerId = playerIdList.get(0).get("playerId").toString();
				}else{
					playerIdList = this.getNoMemberTypeUplayerByUserId(map);
					if (null != playerIdList && playerIdList.size() > 0) {
						playerId = playerIdList.get(0).get("playerId").toString();
					}
				}
				//球员Id不为null
				if (publicService.StringUtil(playerId)) {
					map.put("playerId", playerId);
					uPlayer =  baseDAO.get(map, "from UPlayer where playerId = :playerId");
				}else{
					//初始球员信息
					uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
				}
			}
		}
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 查询当前用户的初始球员信息
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   		uPlayer	对象
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	public UPlayer getUplayerByUserIdInJoinTeam(HashMap<String, String> map) throws Exception {
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 判断是否加入了两支球队
	   @param map
	   @return
	   2016年2月3日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public Boolean isJoinDoubleTeam(HashMap<String, String> map) throws Exception {
		boolean isJoinDoubleTeam = false;
		List<UPlayer> uPlayerlist = baseDAO.find(map, "from UPlayer where UUser.userId = :loginUserId and in_team='1' and UTeam.teamId is not null order by adddate desc,addtime desc");
		if (null != uPlayerlist && uPlayerlist.size() >= 2) {
			isJoinDoubleTeam = true;
		}
		return isJoinDoubleTeam;
	}

	/**
	 * 
	 * 
	   TODO - 根据球队Id获取该球队的球员列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月15日
	   dengqiuru
	 */
	@Override
	public List<UPlayer> getPlayerListByTeamId(HashMap<String, String> map) throws Exception {
		List<UPlayer> uPlayerList = new ArrayList<UPlayer>();
		if (publicService.StringUtil(map.get("teamId"))) {
			uPlayerList = baseDAO.find(map, "from UPlayer where UTeam.teamId=:teamId and in_team='1' order by adddate desc,addtime desc");
		}
		return uPlayerList;
	}

	@Override
	public UPlayer getPlayerByUserAndTeam(HashMap<String, String> param) throws Exception {
		if(StringUtils.isEmpty(param.get("userId")))
			return null;
		if(StringUtils.isEmpty(param.get("teamId")))
			return null;
		UPlayer player = this.baseDAO.get(param,"from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
		return player;
	}

	/**
	 * 
	 * 
	   TODO - 编辑用户信息时，体重 身高 年龄修改后再计算球队平均值
	   @param uUser
	   2016年2月16日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void updateTeamAvg(UUser uUser) throws Exception {
		if (null != uUser) {
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userId", uUser.getUserId());
			//查询该用户所有有球队的队员信息
			List<UPlayer> uPlayerList = getUPlayerListByUserId(hashMap);
			if (null != uPlayerList && uPlayerList.size() > 0) {
				for (UPlayer uPlayer : uPlayerList) {
					UTeam uTeam = baseDAO.get(UTeam.class, uPlayer.getUTeam().getTeamId());
					if (null != uTeam) {
						//查询该队的所有球员
						hashMap.put("teamId", uTeam.getTeamId());
						List<UPlayer> uteamUplayerList = baseDAO.find(hashMap, "from UPlayer up where up.UTeam.teamId=:teamId and inTeam='1' ");
						uTeamService.updateAvgWHAge(uteamUplayerList, uTeam);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 我的球队信息，根据userId获取该用户存在球队的球员列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
	@Override
	public List<UPlayer> getUPlayerListByUserId(HashMap<String, String> map) throws Exception {
		String hql = "from UPlayer up where up.UUser.userId = :userId and in_team='1' and up.UTeam.teamId is not null";
		List<UPlayer> uPlayerList = baseDAO.find(map, hql);
		return uPlayerList;
	}

	/**
	 *
	 *
	 TODO - 我的球队信息，根据userId获取该用户存在球队的球员列表
	 @param userId
	 @return
	 @throws Exception
	 2016年2月17日
	 xhy
	 */
	@Override
	public List<UPlayer> getUPlayerListByUserId(String userId) throws Exception {
		String hql = "from UPlayer up where up.UUser.userId = '"+userId+"' and in_team='1' and up.UTeam.teamId is not null";
		List<UPlayer> uPlayerList = baseDAO.find( hql);
		return uPlayerList;
	}


	
	/**
	 * 
	 * 
	   TODO - 战队详情  队员列表
	   @param params
	   @return
	   2016年2月17日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> listPlayerInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("teamId"))) {
			hashMap.put("teamId", map.get("teamId"));
			List<UPlayer> uPlayerList = baseDAO.find(map, "from UPlayer where UTeam.teamId=:teamId and in_team='1' order by adddate desc,addtime desc");
			for (UPlayer uPlayer : uPlayerList) {
				setUUserImg(uPlayer);
			}
			resultMap.put("uPlayerList", uPlayerList);
		}
		return resultMap;
	}

	
	/**
	 * 
	 * 
	   TODO - 加入球队时，判断用户是否退队超过24小时 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		boolean  true:是；false：否
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	@Override
	public Boolean isExceedOneDay(HashMap<String, String> map) throws Exception {
		boolean isExpired = false;
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer up where up.UUser.userId=:loginUserId and up.UTeam.teamId=:teamId and up.in_team='2'");
		if (null != uPlayer) {
			if (null != uPlayer.getExitdate() && null != uPlayer.getExittime()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// 退队日期转换
				SimpleDateFormat simpleDateFormathh = new SimpleDateFormat("HH:mm:ss");// 退队时间转换
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date teamExitDateD = uPlayer.getExitdate();// 退队日期
				Date teamExitTimeT = uPlayer.getExittime();// 退队时间
				Date newDate = format.parse(format.format(new Date()));// 现在时间
				String teamExit = simpleDateFormat.format(teamExitDateD) + " " + simpleDateFormathh.format(teamExitTimeT);// 将退队日期和时间拼接
				Date teamExitDT = format.parse(teamExit);// 讲拼接的日期时间转换成date
				long cha = newDate.getTime() - teamExitDT.getTime();
		        if(cha<0){
		        	isExpired = false; 
		        }
		        //计算
				double result = cha / (1000 * 60 * 60);
				if(result<=24){ 
					isExpired = true; 
		        }else{ 
		        	isExpired = false; 
		        } 
			}
		}
		return isExpired;
	}

	/**
	 * 
	 * 
	   TODO - 球员详情  概况 【2.0.0】
	   @param map
	   		playerId   球员Id
	   @return
	   		uPlayer，uPlayerList的hashMap<String,Object>
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUplayerinfoByPlayerId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
		if (null != uPlayer) {
			map.put("userId", uPlayer.getUUser().getUserId());
			List<UPlayer> uPlayerList = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and in_team='1' order by adddate desc,addtime desc");
			resultMap.put("uPlayerList", uPlayerList);
		}
		resultMap.put("uPlayer", uPlayer);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 球员详情  相册列表 【2.0.0】
	   @param map
	   		playerId   球员Id
	   @return
	   		galleryList的hashMap<String,Object>
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUplayerGalleryList(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("playerId"))) {
			UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
			if (null != uPlayer) {
				map.put("userId", uPlayer.getUUser().getUserId());
				//查询该球员的相册列表
				resultMap = uUserImgService.getGalleryList(map);
			}
		}else{
			return WebPublicMehod.returnRet("error", "playerId不能为空！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 球员详情  编辑相册 【2.0.0】
	   @param map
	   		playerId		球员Id
	   		pkId			相册Id（只删除时传此参数）
	   		imgSizeType		图片尺寸类型
			imgurl			图片显示地址
			imgWeight		图片权重
			saveurl			图片存放地址
	   @return
	   		uUserImg的hashMap
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateUplayerGallery(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap1 = new HashMap<>();
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("playerId"))) {
				//查询当前用户是否存在
				UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
				if (null != uPlayer) {
					//判断当前用户是否与上传的一致
					if (map.get("loginUserId").equals(uPlayer.getUUser().getUserId())) {
						hashMap1 = uUserImgService.uploadGallery(map);
						resultMap.put(hashMap1.get("msg").toString(), hashMap1.get("msg"));
					}else{
						return WebPublicMehod.returnRet("error", "只有球员本身才能编辑相册！");
					}
				}
			}else{
				return WebPublicMehod.returnRet("error", "playerId不能为空！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "loginUserId不能为空！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 个人中心头部 【2.0.0】
	   @param map
	   @return
	   		OutPlayerList对象
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUTeamforuserId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		OutPlayerList outPlayerList = new OutPlayerList();
		UPlayer uPlayer = null;
		String playerId = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			map.put("userId", map.get("loginUserId"));
			UUser uUser = baseDAO.get(map, "from UUser where userId=:userId");
			//为队长的信息
			List<HashMap<String, Object>> playerIdList = this.getMemberTypeUplayerByUserId(map);
			if (null != playerIdList && playerIdList.size() > 0) {
				playerId = playerIdList.get(0).get("playerId").toString();
			}else{
				playerIdList = this.getNoMemberTypeUplayerByUserId(map);
				if (null != playerIdList && playerIdList.size() > 0) {
					playerId = playerIdList.get(0).get("playerId").toString();
				}
			}
			//球员Id不为null
			if (publicService.StringUtil(playerId)) {
				map.put("playerId", playerId);
				uPlayer =  baseDAO.get(map, "from UPlayer where playerId = :playerId");
			}else{
				//初始球员信息
				uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
			}
		
			//将球员信息set到球员输出表
			if (null == uPlayer) {
				outPlayerList = new OutPlayerList();
				outPlayerList = setOutPlayerList(uUser,uPlayer, map);
			}else{
				outPlayerList = setOutPlayerList(uUser,uPlayer, map);
			}
			//填充球员的身份
			outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
		}
		resultMap.put("outPlayerList", outPlayerList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 根据userId查询球队信息 【2.0.0】
	   @param map
	   		loginUserId		登录人的Id
	   @return
	   		UTeam 对象
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	@Override
	public UTeam getUTeaminfoforuserId(HashMap<String, String> map) throws Exception {
		UTeam uTeam = null;
		StringBuffer sql = this.getUTeaminfoforuserId_sql;
		List<HashMap<String, Object>> teamIds = baseDAO.findSQLMap(map,sql.toString());
		if (null != teamIds && teamIds.size() > 0) {
			for (HashMap<String, Object> hashMap : teamIds) {
				if (null != hashMap.get("teamId")) {
					map.put("teamId", hashMap.get("teamId").toString());
					uTeam = uTeamService.findPlayerInfoById(map);
				}
			}
		}
		return uTeam;
		
	}

	/**
	 * 
	 * 
	   TODO - 退出队伍时，更新数据库
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	public UPlayer exitTeam(UPlayer uPlayer,String exitType) throws Exception {
		uPlayer.setInTeam("2");
		uPlayer.setExitdate(new Date());
		uPlayer.setExittime(new Date());
		uPlayer.setExitType(exitType);
		baseDAO.update(uPlayer);
		return uPlayer;
	}
	
	/**
	 * 
	 * 
	   TODO - 解散球队
	   @param uTeam
	   @throws Exception
	   2016年2月29日
	   dengqiuru
	 */
	@Override
	public void disBandUTeam(UTeam uTeam) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("teamId", uTeam.getTeamId());
		List<UPlayer> uPlayerList = baseDAO.find(hashMap, " from UPlayer where UTeam.teamId=:teamId and inTeam = '1' ");
		if (null != uPlayerList) {
			for (UPlayer uPlayer : uPlayerList) {
				uPlayer.setExitdate(new Date());
				uPlayer.setExittime(new Date());
				uPlayer.setInTeam("2");
				uPlayer.setExitType("3");
				baseDAO.update(uPlayer);
				//更新球员lbs加队数
				if (null != uPlayer.getUUser()) {
					uUserService.setAreaToBaiduLBS(uPlayer.getUUser());
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 有球队的球员查询【2.0.0.1】
	   @param map
	   		playerId
	   @return
	   @throws Exception
	   2016年3月1日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUPlayerinfoInUteam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		OutPlayerList outPlayerList = new OutPlayerList();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
					if (publicService.StringUtil(map.get("teamId"))) {
						HashMap<String, Object> teamStatusMap = uTeamService.checkAllUTeamStatus(map);
						if ("3".equals(teamStatusMap.get("success").toString())) {
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
						}
					}
				}
				baseDAO.getSessionFactory().getCurrentSession().flush();
				UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
				if (null != uPlayer) {
					map.put("userId", map.get("loginUserId"));
					UUser uUser = baseDAO.get(map, "from UUser where userId=:userId");
					//填充球员基础数据
					outPlayerList = setOutPlayerListInTeam(uUser,uPlayer, map);
					//填充球员的身份
					outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
					//填充位置
					if (publicService.StringUtil(outPlayerList.getPosition())) {
						String objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
						outPlayerList.setPosition(uPlayer.getPosition());//位置
						outPlayerList.setPositionName(objectName);
					}
				}
				resultMap.put("outPlayerList", outPlayerList);
			}else{
				return WebPublicMehod.returnRet("error", "teamId不能为空！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请登录正常账户在执行此操作！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 无球队的球员查询 【2.0.0】
	   @param map
	   		playerId  球员的Id
	   @return
	   		uPlayer 的hashMap<String,Object>
	   @throws Exception
	   2016年3月1日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUPlayerinfoOutUteam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		OutPlayerList outPlayerList = new OutPlayerList();
		UUser uUser = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId is null");
			if (null != uPlayer) {
				//将球队信息都set null,以防错误数据
				uPlayer.setNumber(null);
				uPlayer.setPosition(null);
				uPlayer.setMemberType(null);
				map.put("userId", map.get("loginUserId"));
			}else{
				uUser = baseDAO.get(map,"from UUser where userId=:loginUserId");
			}
			outPlayerList = setOutPlayerListOutTeam(uUser,uPlayer, map);
			resultMap.put("outPlayerList", outPlayerList);
		}else{
			resultMap.put("outPlayerList", outPlayerList);
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 设置不在队的球员信息
	   @param uUser
	   @param uPlayer
	   @param map
	   		loginUserId  当前用户Id
	   @return
	   @throws Exception
	   2016年7月4日
	   dengqiuru
	 */
	private OutPlayerList setOutPlayerListOutTeam(UUser uUser, UPlayer uPlayer, HashMap<String, String> map) throws Exception {
		OutPlayerList outPlayerList = new OutPlayerList();
		String objectName = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			map.put("userId", map.get("loginUserId"));
		}
		if (null != uPlayer) {
			outPlayerList.setPlayerId(uPlayer.getPlayerId());//球员Id
			baseDAO.getSessionFactory().getCurrentSession().flush();
			UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
			if (null != uUserImg) {
				outPlayerList.setImgurl(uUserImg.getImgurl());//头像
			}
			HashMap<String, Object> worthMap = uworthService.findUserIdWorthCount(map);//该球员的身价
			if (null != worthMap) {
				if (null != worthMap.get("count")) {
					outPlayerList.setWorthCount(worthMap.get("count")+"");
				}
			}
			//关注数
			HashMap<String, Object> followMap = new HashMap<>();
			followMap.put("userId", uPlayer.getUUser().getUserId());
			Integer followCount = this.pubGetUserFollowCountByUserId(followMap);
			if (null != worthMap) {
				outPlayerList.setFollowCount(followCount);
			}
			if (null != uPlayer.getUUser()) {
				if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
					outPlayerList.setRealname(uPlayer.getUUser().getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
					outPlayerList.setNickname(uPlayer.getUUser().getNickname());//昵称
				}
				if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
					outPlayerList.setSex(uPlayer.getUUser().getSex());//性别
					String sex = uPlayer.getUUser().getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uPlayer.getUUser().getAge() != null ) {
					outPlayerList.setAge(uPlayer.getUUser().getAge().toString());//年龄
				}else{
					if (uPlayer.getUUser().getBirthday() != null ) {
						outPlayerList.setAge(getAgeByBirthday(uPlayer.getUUser().getBirthday()).toString());//年龄
					}
				}
				if (publicService.StringUtil(uPlayer.getUUser().getWeight())) {
					outPlayerList.setWeight(uPlayer.getUUser().getWeight());//体重
				}
				if (uPlayer.getUUser().getBirthday() != null ) {
					String birthday = PublicMethod.getDateToString(uPlayer.getUUser().getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uPlayer.getUUser().getHeight())) {
					outPlayerList.setHeight(uPlayer.getUUser().getHeight());//身高
				}
				if (publicService.StringUtil(uPlayer.getUUser().getRemark())) {
					outPlayerList.setRemark(uPlayer.getUUser().getRemark());//备注
				}
			}
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					outPlayerList.setExpertPositionName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					outPlayerList.setExpertPositionName(objectName);
				}
			}
			if (publicService.StringUtil(uPlayer.getCanPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					outPlayerList.setCanPositionName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					outPlayerList.setCanPositionName(objectName);
				}
			}
			if (publicService.StringUtil(uPlayer.getPracticeFoot())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					outPlayerList.setPracticeFootName(objectName);
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					outPlayerList.setPracticeFootName(objectName);
				}
			}
			if (null != uPlayer.getNumber()) {
				outPlayerList.setNumber(uPlayer.getNumber().toString());//背号
			}
			if (publicService.StringUtil(uPlayer.getPosition())) {
				if (!"16".equals(uPlayer.getPosition())) {
					objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				}else{
					objectName = null;
				}
				outPlayerList.setPosition(uPlayer.getPosition());//位置
				outPlayerList.setPositionName(objectName);
			}
			if (null != uPlayer.getUUser()) {//设置是否是我的球队
				if (publicService.StringUtil(map.get("loginUserId"))) {
					map.put("userId", map.get("loginUserId"));
					if (map.get("userId").equals(uPlayer.getUUser().getUserId())) {
						outPlayerList.setIsMyself("1");
					}else{
						outPlayerList.setIsMyself("2");
					}
				}else{
					outPlayerList.setIsMyself("2");
				}
			}else{
				outPlayerList.setIsMyself("2");
			}
		}else{
			if (null != uUser) {
				baseDAO.getSessionFactory().getCurrentSession().flush();
				UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
				if (null != uUserImg) {
					outPlayerList.setImgurl(uUserImg.getImgurl());//头像
				}
				if (null != uUser) {
					if (publicService.StringUtil(uUser.getRealname())) {
						outPlayerList.setRealname(uUser.getRealname());//球员真实姓名
					}
					if (publicService.StringUtil(uUser.getNickname())) {
						outPlayerList.setNickname(uUser.getNickname());//昵称
					}
					if (publicService.StringUtil(uUser.getSex())) {
						outPlayerList.setSex(uUser.getSex());//性别
						String sex = uUser.getSex();
						objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
						outPlayerList.setSexName(objectName);
					}
					if (null != uUser.getAge()) {
						outPlayerList.setAge(uUser.getAge().toString());//年龄
					}
					if (publicService.StringUtil(uUser.getWeight())) {
						outPlayerList.setWeight(uUser.getWeight());//体重
					}
					if (null != uUser.getBirthday()) {
						String birthday = PublicMethod.getDateToString(uUser.getBirthday(), "yyyy-MM-dd");
						outPlayerList.setBirthday(birthday);//出生日期
					}
					if (publicService.StringUtil(uUser.getHeight())) {
						outPlayerList.setHeight(uUser.getHeight());//身高
					}
					if (publicService.StringUtil(uUser.getRemark())) {
						outPlayerList.setRemark(uUser.getRemark());//备注
					}
				}
			}
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO -  战队列表头部，查询用户在球队里的球员信息 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		Set<UPlayer>
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	@Override
	public Set<UPlayer> getUplayerinfoByuserIdteamId(HashMap<String, String> map) throws Exception {
		Set<UPlayer> uPlayers = new HashSet<>();
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1' ");
		if (null != uPlayer) {
			uPlayer.setUUser(null);
			setAllName(uPlayer);
			uPlayer = uPlayerRoleService.setMemberTypeByGetNoOutUplayerinfo202(uPlayer);
			uPlayers.add(uPlayer);
		}else{
			uPlayer = new UPlayer();
			uPlayers.add(uPlayer);
		}
		return uPlayers;
	}
	/**
	 * 
	 * 
	   TODO - 球员表的参数字段，填充对应的名称
	   @param uPlayer
	   @throws Exception
	   2016年7月4日
	   dengqiuru
	 */
	private void setAllName(UPlayer uPlayer) throws Exception {
		if (null != uPlayer) {
			String objectName = null;
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayer.getExpertPosition());
				uPlayer.setExpertPositionName(objectName);//擅长位置
			}
			if (publicService.StringUtil(uPlayer.getPracticeFoot())) {
				objectName = null;
				objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayer.getPracticeFoot());
				uPlayer.setPracticeFootName(objectName);//惯用脚
			}
			if (publicService.StringUtil(uPlayer.getPosition())) {
				objectName = null;
				if (!"16".equals(uPlayer.getPosition())) {
					objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				}
				uPlayer.setPositionName(objectName);//位置
			}
			if (null != uPlayer.getUUser()) {
				if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
					objectName = null;
					UUser uUser = uPlayer.getUUser();
					objectName = Public_Cache.HASH_PARAMS("sex").get(uUser.getSex());
					uUser.setSexName(objectName);
					uPlayer.setUUser(uUser);//性别
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 战队列表查询球员信息《头部》  20160319 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	@Override
	public UPlayer getUplayerinfoByuserIdteamIdNotSet(HashMap<String, String> map,String type) throws Exception {
		UPlayer uPlayer = null;
		if ("1".equals(type)) {//固定用户在这个球队在队的信息
			uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
		}else if("3".equals(type)){//当前用户为队长的信息
			map.put("userId", map.get("loginUserId"));
			List<HashMap<String, Object>> listTeam = this.getMemberTypeUplayerByUserId(map);
			if (null != listTeam && listTeam.size() > 0) {
				if (null != listTeam.get(0).get("playerId")) {
					map.put("playerId", listTeam.get(0).get("playerId").toString());
					uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId");
				}
			}
		}else{//当前用户在这个球队在队的信息
			uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
		}
		if (null != uPlayer) {
			setAllName(uPlayer);
			uPlayer = uPlayerRoleService.setMemberTypeByGetNoOutUplayerinfo202(uPlayer);
		}
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO -  修改球员信息，加队球员曾经加入过该球队
	   @param uPlayer
	   @param map
	   @param uUser
	   @return
	   2016年3月7日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public UPlayer updateUPlayerInTeam(UPlayer uPlayer, HashMap<String, String> map, UUser uUser,UTeam uTeam) throws Exception {
		boolean isNumber = isExitUteamNumber(map, uTeam);
		if (isNumber == false) {
			uPlayer.setInTeam("1");
			uPlayer.setExitdate(null);
			uPlayer.setExittime(null);
			uPlayer.setExitType(null);
			//球队角色
			if (publicService.StringUtil(map.get("memberType"))) {
				uPlayer.setMemberType(map.get("memberType"));
			}
			if (publicService.StringUtil(map.get("number"))) {
				//判断球队中是否存在该位置
				boolean isNumber2 = isExitUteamNumber(map,uTeam);
				if (isNumber2 == false) {
					uPlayer.setNumber(Integer.parseInt(map.get("number")));
				}else{
					uPlayer = null;
					return uPlayer;
				}
			}
			//球队位置
			if (publicService.StringUtil(map.get("position"))) {
				uPlayer.setPosition(map.get("position"));
			}
			baseDAO.update(uPlayer);
		}else{
			uPlayer = null;
		}

		this.updateNoTeamPlayerinfo(map, uUser);
		//修改用户信息
		baseDAO.getSessionFactory().getCurrentSession().flush();
		uUserService.inPlayerUpdateUUser(map,uUser);
		return uPlayer;
	}
	/**
	 * 
	 * 
	   TODO - 球员详情   概况 【2.0.0】
	   @param map
	   		playerId	球员Id
	   @return
	   		galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> roughlyStateOfUPlayer(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//判断playerId是否为空
		if (publicService.StringUtil(map.get("playerId"))) {
			UPlayer uPlayer = baseDAO.get(map,"from UPlayer where playerId=:playerId");
			if (null != uPlayer) {
				if (null !=  uPlayer.getUUser()) {
					map.put("userId", uPlayer.getUUser().getUserId());
					//该球员相册
					List<HashMap<String, Object>> uUserImglist =  new ArrayList<HashMap<String, Object>>();
					uUserImglist = uUserImgService.getGalleryListInroughly(map);
					resultMap.put("outUuserImgs", uUserImglist);

					//战队球场轴
					List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
					ubCourtMap = this.getBrCourtlist(map);
					resultMap.put("ubCourtMap", ubCourtMap);
					//时间轴
					List<UUserBehavior> UUserBehavior =  new ArrayList<UUserBehavior>();
					List<HashMap<String, Object>> UUserBehaviorList = new ArrayList<HashMap<String, Object>>();
					map.put("type", "1");
					List<Object> objectList = publicService.getBehaviorList(map);
					if (null != objectList && objectList.size() > 0) {
						for (Object object : objectList) {
							UUserBehavior uUserBehavior = (UUserBehavior)object;
							uUserBehavior.setUUser(null);
							if (null != uUserBehavior.getRemark() && !"".equals(uUserBehavior.getRemark())) {
								uUserBehavior.setUserBehaviorTypeName(uUserBehavior.getRemark());
							}else{
								uUserBehavior.setUserBehaviorTypeName(Public_Cache.HASH_PARAMS("user_behavior_type").get(uUserBehavior.getUserBehaviorType()));
							}
							UUserBehavior.add(uUserBehavior);
						}
						resultMap.put("outuUserBehaviors", UUserBehavior);
					}else{
						UUserBehaviorList = uUserBehaviorService.getuUserBehaviorList(map);
						resultMap.put("outuUserBehaviors", UUserBehaviorList);
					}
					
					//该用户的球队列表
					List<HashMap<String, Object>> listTeam =  new ArrayList<HashMap<String, Object>>();
					//角标
					List<Object> listTeamId = new ArrayList<Object>();
					List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
					listTeam = uTeamService.getUteamListInroughly(map);
					if(null != map.get("page")&& !"".equals(map.get("page"))){
						if (null != listTeam && listTeam.size() > 0) {
							for (HashMap<String, Object> hashMap2 : listTeam) {
								String teamId = hashMap2.get("teamId").toString();
								listTeamId.add(teamId);
							}
						}
						if (null != listTeamId && listTeamId.size() > 0) {
							cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
						}
					}
					resultMap.put("cornerList", cornerList);
					resultMap.put("outUteamLists", listTeam);
				}
				
				//判断是否是自己看自己
				HashMap<String, String> hashMap= this.setUTeamMyself(map,uPlayer);
				resultMap.put("isMyself", hashMap.get("isMyself"));
				resultMap.put("isTeamLeader", hashMap.get("isTeamLeader"));
				resultMap.put("isMyTeam", hashMap.get("isMyTeam"));
			}
					
		}
		return resultMap;
	}

	
	@Override
	public HashMap<String, Object> uPlayerInfoDetail203(HashMap<String,String> map) throws Exception {

		HashMap<String, Object> retMap = new HashMap<String, Object>();
		if (publicService.StringUtil(map.get("playerId"))) {
			HashMap<String,String > params = new HashMap<String,String>();
			List<HashMap<String, Object>> uUserImglist =  new ArrayList<HashMap<String, Object>>();
			UPlayer uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId");

			if(uPlayer == null){
				return WebPublicMehod.returnRet("error","未找到球员信息");
			}

			params.put("playerId",uPlayer.getPlayerId());
			params.put("userId",uPlayer.getUUser().getUserId());
			map.put("userId",uPlayer.getUUser().getUserId());

			//身价
			HashMap<String, Object> hashMap1=new HashMap<>();
			hashMap1=uworthService.findUserIdAllWorthInfo(map);
			hashMap = uworthService.findUserIdCompleteWorthInfo(map);
			hashMap.put("listTask", hashMap1.get("listTask"));
			retMap.put("sj",hashMap);

			//关于我部分
			retMap.put("aboutPlayer",this.getAboutPlayer(map,uPlayer));

			//荣誉墙
			retMap.put("honorList", this.getPlayerInfoHonor(params));
			//获取概况里面的里程碑和相册（5条）
			params.put("roughly", "1");
			//里程碑
			retMap.putAll(this.uplayerBehaviorType(params));

			//查询该用户有多少只球队
			List<UPlayer> uPlayers = baseDAO.find(params, " from UPlayer where UUser.userId=:userId and inTeam='1' and (UTeam.teamStatus='1' or UTeam.teamStatus='2' or UTeam.teamStatus='3') and UTeam.teamUseStatus='2' ");

			//该用户的球队列表
			List<HashMap<String, Object>> listTeam =  new ArrayList<HashMap<String, Object>>();
			//角标
			List<Object> listTeamId = new ArrayList<Object>();
			List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
			listTeam = uTeamService.getUteamListInroughly(params);
			if(null != map.get("page")&& !"".equals(map.get("page"))){
				if (null != listTeam && listTeam.size() > 0) {
					for (HashMap<String, Object> hashMap2 : listTeam) {
						String teamId = hashMap2.get("teamId").toString();
						listTeamId.add(teamId);
					}
				}
				if (null != listTeamId && listTeamId.size() > 0) {
					cornerList = cornerService.getAllTeamCornerList(params, listTeamId);
				}
			}
			//该球员相册
			uUserImglist = uUserImgService.getGalleryListInroughly(map);
			retMap.put("outUuserImgs", uUserImglist);
			HashMap<String, String> hashMap= this.setUTeamMyself(map,uPlayer);
			retMap.put("isMyself", hashMap.get("isMyself"));
			retMap.put("cornerList", cornerList);
			retMap.put("outUteamLists", listTeam);
			retMap.put("playerTeamCount", uPlayers.size());
		}
		return retMap;
	}

	@Override
	public HashMap<String, Object> getAboutPlayerInfo(HashMap<String,String> params) throws Exception {
		if(StringUtils.isEmpty(params.get("playerId"))){
			return WebPublicMehod.returnRet("error","未找到球员Id");
		}
		UPlayer uPlayer = baseDAO.get(params, "from UPlayer where playerId=:playerId");

		if(uPlayer == null){
			return WebPublicMehod.returnRet("error","未找到球员信息");
		}

		params.put("teamId",uPlayer.getUTeam().getTeamId());
		params.put("playerId",uPlayer.getPlayerId());
		params.put("userId",uPlayer.getUUser().getUserId());


		return WebPublicMehod.returnRet("info",this.getAboutPlayer(params,uPlayer));
	}

	private List<Object> getUserTeam(HashMap<String,String> param) throws Exception {
		List<Object> list = new ArrayList<Object>();
		List<UPlayer> players = this.baseDAO.find(param," from UPlayer p where p.UUser.userId=:userId ");
		return list;
	}

	/**
	 * 
	 * 
	   TODO - 查询球场轴  --主场
	   @param map
	   2016年3月7日
	   dengqiuru
	 * @throws Exception 
	 */
	public List<OutUbCourtMap> getBrCourtlist(HashMap<String, String> map) throws Exception {
		UUser uUser = baseDAO.getHRedis(UUser.class,map.get("userId"),
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));;
		List<OutUbCourtMap> ubCourtMapList = new ArrayList<OutUbCourtMap>();
		if (null != uUser) {
			if (null != uUser.getuRegion()) {
				OutUbCourtMap ubCourtMap = this.setUbCourtMap1(uUser.getuRegion(),map,uUser);
				ubCourtMapList.add(ubCourtMap);
			}
		}
		return ubCourtMapList;
	}
	
	/**
	 * 
	 * 
	   TODO - 球队概况 --球场轴--区域
	   @param getuRegion	对象
	   @param map
	   @param uUser
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private OutUbCourtMap setUbCourtMap1(URegion getuRegion,HashMap<String, String> map,UUser uUser) throws Exception {
		OutUbCourtMap ubCourtMap1 = new OutUbCourtMap();
		map.put("keyId", uUser.getuRegion().get_id());
		ubCourtMap1.setIdStr("区域");
		Set<URegion> uRegions = uRegionService.getURegionSet(map);
		String province = null;
		String city = null;
		String county = null;
		for (URegion uRegion : uRegions) {
			if ("1".equals(uRegion.getType())) {
				province = uRegion.getName();
			}
			if ("2".equals(uRegion.getType())) {
				city = uRegion.getName();
			}
			if ("3".equals(uRegion.getType())) {
				county = uRegion.getName();
			}
		}
		ubCourtMap1.setNameStr(province+"-"+city+"-"+county);//将省市区拼接一起
		return ubCourtMap1;
	}
	/**
	 * 
	 * 
	   TODO - 设置是否是自己的队伍
	   @param map
	   		loginUserId		当前用户Id
	   		teamId			球队Id
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private HashMap<String , String> setUTeamMyself(HashMap<String, String> map, UPlayer uPlayer) throws Exception {
		HashMap<String , String> resultMap = new HashMap<>();
		String isMyTeam = "2";//被查看人是否是自己球队  2：不是
		String isMyself = "2";//是否是我自己
		String isTeamLeader = "2";//查看人是否为队长  2：不是
		if (publicService.StringUtil(map.get("loginUserId"))) {
			UPlayer uPlayerTemp = null;
			if (publicService.StringUtil(map.get("teamId"))) {
				map.put("teamId", map.get("teamId"));
				uPlayerTemp = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
			}else{
				map.put("userId", map.get("loginUserId"));
				List<HashMap<String, Object>> listTeam = this.getMemberTypeUplayerByUserId(map);
				if (null != listTeam && listTeam.size() > 0) {
					if (null != listTeam.get(0).get("playerId")) {
						map.put("playerId", listTeam.get(0).get("playerId").toString());
						uPlayerTemp = baseDAO.get(map, "from UPlayer where playerId=:playerId");
					}
				}
			}
			if (null != uPlayerTemp) {
				if (null != uPlayer.getUUser()) {
					if (map.get("loginUserId").equals(uPlayer.getUUser().getUserId())) {//判断是否是我自己
						isMyself = "1";
					}
				}
				if ("1".equals(uPlayerTemp.getMemberType())) {//判断被查看人是否是队长
					isTeamLeader = "1";
				}
				if (null != uPlayer.getUTeam()) {
					if (uPlayerTemp.getUTeam().getTeamId().equals(uPlayer.getUTeam().getTeamId())) {//查看人是否是我的队
						isMyTeam = "1";
					}
				}
			}
		}
		resultMap.put("isMyTeam", isMyTeam);//被查看人是否是自己球队  2：不是
		resultMap.put("isTeamLeader", isTeamLeader);//查看人是否为队长  2：不是
		resultMap.put("isMyself", isMyself);
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 球员详情   头部 【2.0.0】
	   @param map
	   		playerId		球员Id
	   @return
	   		uplayer的resultMap<String,Object>
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> roughlyStateOfUPlayerHead(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> uPlayerList=new ArrayList<HashMap<String, Object>>();
		List<Object> listPlayer = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
		}else{
			hashMap.put("userId", "");
		}
		StringBuffer sql = this.roughlyStateOfUPlayerHead_sql;
		hashMap.put("playerId", map.get("playerId"));
		uPlayerList = baseDAO.findSQLMap(sql.toString(),hashMap);
		// 更改类型和状态的页面显示值
		if (uPlayerList != null && uPlayerList.size() > 0) {
			for (HashMap<String, Object> hashMap2 : uPlayerList) {
				this.displayData(hashMap2,map);
				//填充角色
				uPlayerRoleService.setMembertype202(hashMap2);
				//设置查看人与被查看人之间的关系
				this.setisMyTeam(hashMap2,map);
				this.setIsInvite(hashMap2,map);//设置是否已邀请
				String playerId = hashMap2.get("playerId").toString();
				listPlayer.add(playerId);
				this.setIsExculde(hashMap2,map);//查看踢人按钮
				
				//xiao
				this.setIsAllot(hashMap2,map);//查看分配对内角色按钮
				this.setIsEdit(hashMap2, map);//查看修改信息按钮
				map.put("userId", hashMap2.get("userId").toString());
				hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count"));
				hashMap2.put("followCount", this.getPlayerFollowCount(hashMap2));
			}
			if (null != listPlayer && listPlayer.size() > 0) {
				cornerList = cornerService.getPlayerCorner(map, listPlayer);
			}
		}
		resultMap.put("cornerList", cornerList);
		resultMap.put("uPlayerList", uPlayerList);
		
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 踢人按钮
	   @param hashMap2
	   @param map
	   		teamId		球队Id
	   @throws Exception
	   2016年7月3日
	   dengqiuru
	 */
	private void setIsExculde(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		HashMap<String, String> roleMap = new HashMap<>();
		String isExculde = "2";//被查看人是否能被踢出  2：不能
		if ("1".equals(hashMap2.get("isMyTeam"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				//查询我是不是有权限踢人
				//查找踢人的用户是否有权限
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
				roleMap.put("type", "1");
				roleMap.put("teamId", map.get("teamId"));
				roleMap.put("loginUserId", map.get("loginUserId"));
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(roleMap);
				if (null != playerIds && playerIds.size() > 0) {
					if (null != hashMap2.get("memberTypeRoleLimit")) {
						//判断权限值
						String memberTypeRoleLimit = hashMap2.get("memberTypeRoleLimit").toString();
						if ("1".equals(memberTypeRoleLimit) || "2".equals(memberTypeRoleLimit) || "3".equals(memberTypeRoleLimit) || "4".equals(memberTypeRoleLimit) || "5".equals(memberTypeRoleLimit)|| "6".equals(memberTypeRoleLimit)) {
							isExculde = "2";
						}else{
							isExculde = "1";
						}
					}
				}
			}
		}
		hashMap2.put("isExculde", isExculde);
	}

	/**
	 * 
	 * 
	   TODO - 设置是否已邀请
	   @param hashMap2
	   @param map
	   		loginUserId			当前用户Id
	   		playerId			球员Id
	   		teamId				球队Id
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void setIsInvite(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		String isInvite = "2";//是否已邀请  2：不是
		if (null != hashMap2) {
			if (publicService.StringUtil(map.get("loginUserId"))) {
				if (publicService.StringUtil(map.get("playerId"))) {
					if (publicService.StringUtil(map.get("teamId"))) {
						UInviteteam uInviteteam = uInviteteamService.getInvateTeam(map);
						if (null != uInviteteam) {
							isInvite = uInviteteam.getInvStatus();
						}
					}
					
				}
			}
		}
		hashMap2.put("isInvite", isInvite);//是否已邀请  2：不是
	}

	/**
	 * 
	 * 
	   TODO - 设置查看人与被查看人之间的关系
	   @param hashMap2
	   		playerId			球员Id
	   @param map
	   		loginUserId			当前用户Id
	   		teamId				球队Id
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void setisMyTeam(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		String isMyTeam = "2";//被查看人是否是自己球队  2：不是
		String isTeamLeader = "2";//查看人是否为队长  2：不是
		String inTeamByUserId = "2";
		String myTeamId = null;//我创建的球队Id
		if (null != hashMap2) {
			if (null != hashMap2.get("playerId")) {
				if (null != map.get("teamId")) {
					map.put("playerId", hashMap2.get("playerId").toString());
					UPlayer uPlayerTemp = baseDAO.get(map, "from UPlayer where playerId=:playerId and UTeam.teamId=:teamId and inTeam='1'");
					if (null != uPlayerTemp) {//我为队长的球队Id
						UPlayer uPlayerTemp1 = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
						if (null != uPlayerTemp1) {
							isMyTeam = "1";
							inTeamByUserId = "1";
							HashMap<String, String> playerIdMap = new HashMap<>();
							playerIdMap.put("playerId1", uPlayerTemp1.getPlayerId());
							List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(playerIdMap);
							if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
								for (UPlayerRole uPlayerRole : uPlayerRoles) {
									if ("1".equals(uPlayerRole.getMemberType())) {
										myTeamId = uPlayerTemp1.getUTeam().getTeamId();
										isTeamLeader = "1";
										break;
									}
								}
							}
						}
					}else{//判断我是不是在当前球队中
						//先查询球员信息
						UPlayer uPlayerTemp1 = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam='1'");
						if (null != uPlayerTemp1) {
							inTeamByUserId = "1";
							HashMap<String, String> playerIdMap = new HashMap<>();
							playerIdMap.put("playerId1", uPlayerTemp1.getPlayerId());
							//在查询角色信息
							List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(playerIdMap);
							if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
								for (UPlayerRole uPlayerRole : uPlayerRoles) {
									if ("1".equals(uPlayerRole.getMemberType())) {
										myTeamId = uPlayerTemp1.getUTeam().getTeamId();
										isTeamLeader = "1";
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		hashMap2.put("inTeamByUserId", inTeamByUserId);//查看人是否是当前球队的成员
		hashMap2.put("isMyTeam", isMyTeam);//被查看人是否是自己球队  2：不是
		hashMap2.put("isTeamLeader", isTeamLeader);//查看人是否为队长  2：不是
		hashMap2.put("myTeamId", myTeamId);//设置查看人的球队Id
	}

	/**
	 * 
	 * 
	   TODO - 根据多个teamId获取球员信息 【2.0.0】
	   @param map
	   		teamIds  球队Id，多个以“，”隔开
	   		page 	 分页
	   @return
	   		team[0]的resultMap<String,Object>
	   @throws Exception
	   2016年3月9日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUplayerByTeamIds(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> teamMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		if (publicService.StringUtil(map.get("teamIds"))) {
			//将多个球队Id分割
			String[] teamIds = map.get("teamIds").split(",");
			for (int i = 0; i < teamIds.length; i++) {
				
				//循环
				hashMap.put("teamId", teamIds[i]);
				UTeam uTeam = baseDAO.get(UTeam.class, teamIds[i]);
				StringBuffer sql = null;
				if (null != uTeam) {
					if ("3".equals(uTeam.getTeamUseStatus())) {
						sql = this.getUplayerNoInteamByTeamIds_sql;
					}else{
						sql = this.getUplayerInteamByTeamIds_sql;
					}
				}else{
					sql = this.getUplayerNoInteamByTeamIds_sql;
				}
				List<HashMap<String, Object>> uPlayers = baseDAO.findSQLMap(sql.toString(),hashMap);
				// 更改类型和状态的页面显示值
				if (uPlayers != null && uPlayers.size() > 0) {
					for (HashMap<String, Object> hashMap2 : uPlayers) {
						this.displayData(hashMap2,null);
						//填充角色
						uPlayerRoleService.setMembertypebyZhenrong202(hashMap2);
					}
				}
				teamMap.put("Team"+i, uPlayers);
			}
			resultMap.put("teams", teamMap);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 关注基友 【2.0.0】
	   @param map
	   		playerId		被关注球员的Id
	   		type			事件类型   1：关注；2：取消关注
	   @return
	   		uFollow的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> followUPlayer(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//判断被关注球员的playerId是否为空
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("playerId"))) {
				//关注人的信息
				UUser uUser = uUserService.getUserinfoByUserId(map);
				//先查询传过来的playerId是否有对应的球员
				UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
				if (null != uPlayer.getUUser()) {
					map.put("userId", uPlayer.getUUser().getUserId());
				}
				String type = map.get("type");//事件类型  1：关注；2：取消关注
				if (type != null) {
					//查询当前用户与被关注的球员是否存在关注关系
					UFollow uFollow = baseDAO.get(map,"from UFollow where user_follow_type='3' and user_id=:loginUserId and object_id=:userId");
					//判断是否是取消关注，是：uFollow为空就提示你还未关注，
					if ("2".equals(type)) {
						if (null == uFollow) {
							return WebPublicMehod.returnRet("error", "你还未关注该球员！");
						}
					}
					//uFollow不为空，就存在关注关系
					if (null != uFollow) {
						//存在，就只是修改
						uFollow.setFollowStatus(type);
						baseDAO.update(uFollow);
					}else{
						if (null != uPlayer.getUUser()) {
							saveUFollow(uUser,uFollow,map,type);
						}else{
							return WebPublicMehod.returnRet("error", "球员未关联用户信息！");
						}
					}
					if ("1".equals(type)) {
						resultMap.put("success", "关注成功！");
					}else{
						resultMap.put("success", "取消关注成功！");
					}
				}
			}
		}else{
			return WebPublicMehod.returnRet("error", "请使用正常用户执行此操作");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 新增关注信息
	   @param uUser
	   		uUser 		对象
	   @param uFollow
	   		uFollow 	对象
	   @param map
	   		playerId 	球员Id
	   @param type
	   		type 		关注状态
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	private void saveUFollow(UUser uUser, UFollow uFollow, HashMap<String, String> map, String type) throws Exception {
		//没有 新增
		uFollow = new UFollow();
		uFollow.setKeyId(WebPublicMehod.getUUID());
		uFollow.setUserFollowType("3");
		uFollow.setUUser(uUser);
		uFollow.setCreatedate(new Date());
		uFollow.setObjectId(map.get("userId"));
		uFollow.setFollowStatus(type);
		baseDAO.save(uFollow);
		
	}
	/**
	 * 
	 * 
	   TODO - 我关注的球员 【2.0.0】
	   @param map
	   		page		分页
	   @return
	   		uPlayerList的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> myFollowUPlayer(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> uPlayerList=new ArrayList<HashMap<String, Object>>();
		List<Object> listPlayer = new ArrayList<Object>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			StringBuffer sql = this.myFollowUPlayer_sql;
			//分页
			hashMap.put("userId", map.get("loginUserId"));
			if(null != map.get("page")&& !"".equals(map.get("page"))){
				uPlayerList = this.getPageLimit(sql.toString(), map, hashMap);
				if (null != uPlayerList && uPlayerList.size() > 0) {
					for (HashMap<String, Object> hashMap2 : uPlayerList) {
						if (null != hashMap2.get("playerId")) {
							String playerId = hashMap2.get("playerId").toString();
							listPlayer.add(playerId);
						}
						//身价
						map.put("userId", (String)hashMap2.get("userId"));
						hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
					}
				}
			}
		}
		resultMap.put("uPlayerList", uPlayerList);
		//角标
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if (null != listPlayer && listPlayer.size() > 0) {
			cornerList = cornerService.getPlayerCorner(map, listPlayer);
		}
		resultMap.put("cornerList", cornerList);
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 建队前，检测用户信息是否填写完成 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> checkUplayerInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		UUser uUser = uUserService.getUserinfoByUserId(map);
		if (null != uUser) {
			//判断球队队长的球员信息是否都已填写完整
			String msg = uUserService.isPlayerinfoComplete(uUser);
			if (msg == null || "".equals(msg)) {
				resultMap.put("success", "success");
			}else{
				return WebPublicMehod.returnRet("error","你的"+msg+"没有填写，请填写完整后再进行建队！");
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 判断当前用户是否是该球队成员 【2.0.0】
	   @param uTeam
	   		uTeam对象
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年3月23日
	   dengqiuru
	 */
	@Override
	public UPlayer isMyTeam(UTeam uTeam, HashMap<String, String> map) throws Exception {
		//是否是我的球队
		UPlayer uPlayer = new UPlayer();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			map.put("teamId", uTeam.getTeamId());
			map.put("userId", map.get("loginUserId"));
			uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
		}
		return uPlayer;
	}
	/**
	 * 
	 * 
	   TODO - 战队概况  球员列表 限制8人【2.0.3】
	   @param map
	   		teamId     球队Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUplayerListInRoughly(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
		//该用户的球队列表
		hashMap.put("teamId", map.get("teamId"));
		StringBuffer sql = this.getUplayerListInRoughly_sql_size();
		//限制参数
		if (publicService.StringUtil(map.get("roughly"))) {
			if ("1".equals(map.get("roughly"))) {
//				hashMap.put("limit", Public_Cache.BEHAVIORCOUNT_INPLAYER);
				hashMap.put("limit", 8);
				sql.append(" limit :limit ");
			}
		}
		//分页
		outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);

		if (outPlayerLists != null && outPlayerLists.size() > 0) {
			for (HashMap<String, Object> hashMap2 : outPlayerLists) {
				this.displayDataByRoughly(hashMap2,null);
//				uPlayerRoleService.setMembertype202(hashMap2);
			}
		}
		return outPlayerLists;
	}
	
	/**
	 * 
	 * @TODO 战队概况  球员列表 sql
	 * @Title: getUplayerListInRoughly_sql_size 
	 * @return
	 * @author charlesbin
	 * @date 2016年9月1日 下午3:31:50
	 */
	public StringBuffer getUplayerListInRoughly_sql_size(){
		StringBuffer getUplayerListInRoughly_sql_size = new StringBuffer(
				" select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,p.number number,p.position position,u.user_id userId"
				+ ",u.username username,r.member_type memberType"
				+" from u_player p "
				+ " left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' and r.member_type<>'11' "
				+ " left JOIN u_player_role_limit l on l.member_type=r.member_type "
				+ " LEFT JOIN u_parameter_info upi on r.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type')  "
				+" left join u_team t on p.team_id=t.team_id and t.team_status='3' and t.team_use_status='2'" 
				+" left join u_user u on u.user_id=p.user_id "
				+" left join u_user_img i on p.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
				+" where p.team_id=:teamId  and p.in_team='1' "
				+ " GROUP by p.player_id order by r.member_type=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,p.player_id desc ");
		return getUplayerListInRoughly_sql_size;
	}
	
	/**
	 * 
	 * 
	   TODO - 战队概况  球员列表总人数 【2.0.3】
	   @param map
	   		teamId     球队Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public Integer getUplayerListInRoughlySize(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
		//该用户的球队列表
		hashMap.put("teamId", map.get("teamId"));
		StringBuffer sql =this.getUplayerListInRoughly_sql_size();
		
		//分页
		outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);

//		if (outPlayerLists != null && outPlayerLists.size() > 0) {
//			for (HashMap<String, Object> hashMap2 : outPlayerLists) {
//				this.displayDataByRoughly(hashMap2,null);
////				uPlayerRoleService.setMembertype202(hashMap2);
//			}
//		}
		return outPlayerLists.size();
	}

	/**
	 * 
	 * TODO 位置处理【2.0.0】
	 * @param hashMap
	 * 		memberType		角色
	 * 		position		位置
	 * 		age				年龄
	 * 		userId			用户Id
	 * 		practiceFoot	惯用脚
	 * 		expertPosition	擅长位置
	 * 		canPosition		可踢位置
	 * 		sex				性别
	 * 		followStatus	关注状态
	 * @param map
	   		loginUserId		当前用户
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	private void displayDataByRoughly(HashMap<String, Object> hashMap, HashMap<String, String> map) throws Exception {
		if (null != hashMap) {
			//位置
			if (hashMap.get("position") != null && !"".equals(hashMap.get("position"))) {
				if (!"16".equals(hashMap.get("position"))) {
					hashMap.put("positionName",Public_Cache.HASH_PARAMS("position").get(hashMap.get("position")));
				}else{
					hashMap.put("positionName", null);
				}
			}else{
				hashMap.put("positionName", null);
			}
			if (hashMap.get("followStatus") != null && !"".equals(hashMap.get("followStatus"))) {//关注状态
				hashMap.put("isFollow",hashMap.get("followStatus"));
			}else{
				hashMap.put("isFollow","2");
			}
			if (null != map) {//设置状态
				String isMyself = "2";
				if (publicService.StringUtil(map.get("loginUserId"))) {
					if (hashMap.get("userId") != null) {
						String loginUserId = map.get("loginUserId");
						if (null != hashMap.get("userId")) {
							String userId = hashMap.get("userId").toString();
							if (loginUserId.equals(userId)) {
								isMyself = "1";
							}
						}
					}
				}
				hashMap.put("isMyself", isMyself);//是否是我的球队
			}else{
				hashMap.put("isMyself","2");
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 根据userId查询自己的队长信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户的Id
	   @return
	   		UPlayer   对象
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public UPlayer getUteamByuserId(HashMap<String, String> map) throws Exception {
		//根据当前用户和球队Id查询
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and in_team='1' and UTeam.teamId=:teamId");
		return uPlayer;
	}
	/**
	 * 
	 * 
	   TODO - 根据userId查看他是否为队长，并获取他创建的球队Id 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	@Override
	public String getUteamIdByuserId(HashMap<String, Object> hashMap) throws Exception {
		//查询当前用户为队长的记录
		String teamId = null;
		if (null != hashMap) {
			if (null != hashMap.get("userId")) {
				//该用户的球队列表
				StringBuffer sql = this.getUteamIdByuserId_sql;
				//分页
				List<HashMap<String, Object>> outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);
				if (null != outPlayerLists && outPlayerLists.size() > 0) {
					teamId = outPlayerLists.get(0).get("teamId").toString();
				}
			}
		}
		return teamId;
	}
	/**
	 * 
	 * 
	   TODO - 根据userId查看他是否为队长，并获取他所有创建的球队Id 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUteamIdListByuserId(HashMap<String, Object> hashMap) throws Exception {
		//查询当前用户为队长的记录
		List<HashMap<String, Object>> outPlayerLists = null;
		if (null != hashMap) {
			if (null != hashMap.get("userId")) {
				//该用户的球队列表
				StringBuffer sql = this.getUteamIdByuserId_sql;
				//分页
				outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);
			}
		}
		return outPlayerLists;
	}
	/**
	 * 
	 * 
	   TODO - 球队新建后，新增队长信息 【2.0.0】
	   @param uUser
	   @param uTeam
	   @param memberType		身份
	   @param map
	   2016年4月4日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void insertTeamLeader(UUser uUser, UTeam uTeam, String memberType, HashMap<String, String> map) throws Exception {
		UPlayer uPlayer = null;
		if (null != uTeam) {
			uPlayer = new UPlayer();
			uPlayer.setPlayerId(WebPublicMehod.getUUID());
			uPlayer.setUUser(uUser);
			uPlayer.setUTeam(uTeam);
//			uPlayer.setMemberType(memberType);
			uPlayer.setTeamBelonging("1");
			uPlayer.setInTeam("1");
			uPlayer.setAdddate(new Date());
			uPlayer.setAddtime(new Date());
			uPlayer.setAddqd("APP");
			baseDAO.save(uPlayer);
			uPlayerRoleService.createNewMembertypeByInsertNewTeam(uPlayer);
		}
	}

	/**
	 * 
	 * 
	   TODO - 球员分享 【2.0.0】
	   @param map
	   		playerId   球员的Id
	   @throws Exception
	   2016年4月5日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> uPlayerinfoShare(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("playerId"))) {
			List<HashMap<String, Object>> uplayerinfo = new ArrayList<HashMap<String, Object>>();
			uplayerinfo= this.getUplayerinfoInShare(map);//分享用户球员基本信息
			resultMap.put("uplayerinfo", uplayerinfo);
		}else{
			return WebPublicMehod.returnRet("error", "playerId不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 分享用户球员基本信息
	   @param map
	   		playerId		球员Id
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getUplayerinfoInShare(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
		//该用户的球队列表
		StringBuffer sql = this.getUplayerinfoInShare_sql;
		//分页
		hashMap.put("playerId", map.get("playerId"));
		outPlayerLists = baseDAO.findSQLMap(sql.toString(),hashMap);
		//循环设置参数对应的名称
		if (outPlayerLists != null && outPlayerLists.size() > 0) {
			for (HashMap<String, Object> hashMap2 : outPlayerLists) {
				this.displayData(hashMap2,null);
				//填充角色
				uPlayerRoleService.setMembertype202(hashMap2);
				//查询该球员有多少球队
				this.getTeamCount(hashMap2);
			}
		}
		return outPlayerLists;
	}
	
	/**
	 * 
	 * 
	   TODO - 查询该球员有多少球队
	   @param hashMap2
	   		userId		用户Id
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void getTeamCount(HashMap<String, Object> hashMap2) throws Exception {
		Integer teamCount = 0;
		if (null != hashMap2) {
			if (null != hashMap2.get("userId")) {
				List<UPlayer> uPlayerList = baseDAO.find("from UPlayer where UUser.userId=:userId and UTeam.teamId is not null and inTeam='1'", hashMap2);
				if (null != uPlayerList && uPlayerList.size()>0) {
					teamCount = uPlayerList.size();
				}
			}
		}
		hashMap2.put("teamCount", teamCount);
	}
	
	/**
	 * 
	 * 
	   TODO - 激战详情--对手--球员列表 【2.0.0】
	   @param map
	   		teamId 		球队Id
	   @return
	   @throws Exception
	   2016年4月12日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUplayerListInCompetitor(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
		//该用户的球队列表
		hashMap.put("XteamId", map.get("XteamId"));
		UTeam uTeam = baseDAO.get(UTeam.class, map.get("XteamId"));
		String sql = null;
		if (null != uTeam) {
			if ("3".equals(uTeam.getTeamUseStatus())) {//解散后的球员信息
				sql = "select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,p.number number,p.position position,u.user_id userId"
						+ ",u.username username,r.member_type memberType"
						+" from u_player p "
						+" left join u_player_role r on r.player_id=p.player_id "
						+" LEFT JOIN u_parameter_info up on r.member_type=up.params and up.pkey_id in (select upi.pkey_id from u_parameter upi where upi.params='member_type') "
						+" left join u_team t on p.team_id=t.team_id and (t.team_status='3' or t.team_status='1') and t.team_use_status='2'" 
						+" left join u_user u on u.user_id=p.user_id "
						+" left join u_user_img i on p.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
						+" where p.team_id=:XteamId and p.exit_type='3' GROUP BY p.player_id "
						+" order by r.member_type=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc";
				
			}else{//未解散后的球员信息
				sql = "select p.player_id playerId,i.imgurl imgurl,u.realname realname,u.nickname nickname,p.number number,p.position position,u.user_id userId"
						+ ",u.username username,r.member_type memberType"
						+" from u_player p "
						+" left join u_player_role r on r.player_id=p.player_id "
						+" LEFT JOIN u_parameter_info up on r.member_type=up.params and up.pkey_id in (select upi.pkey_id from u_parameter upi where upi.params='member_type') "
						+" left join u_team t on p.team_id=t.team_id and (t.team_status='3' or t.team_status='1') and t.team_use_status='2'" 
						+" left join u_user u on u.user_id=p.user_id "
						+" left join u_user_img i on p.user_id=i.user_id and i.uimg_using_type='1' and i.img_size_type='1' "
						+" where p.team_id=:XteamId  and p.in_team='1' GROUP BY p.player_id "
						+" order by r.member_type=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc";
			}
			//分页
			outPlayerLists = baseDAO.findSQLMap(sql,hashMap);

			if (outPlayerLists != null && outPlayerLists.size() > 0) {
				for (HashMap<String, Object> hashMap2 : outPlayerLists) {
					//设置参数对应名称
					this.displayDataByRoughly(hashMap2,null);
					//填充角色
//					uPlayerRoleService.setMembertypeNoOrderByRank202(hashMap2);
				}
			}
		}
		return outPlayerLists;
	}

	/**
	 * 
	 * 
	   TODO - 根据用户Id获取用户为队员的球员信息 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年4月14日
	   dengqiuru
	 */
	@Override
	public List<UPlayer> getUPlayerIsNotTeamLeader(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<UPlayer> uPlayerList = null;
		List<UPlayer> uPlayers = new ArrayList<>();
		if (null != map) {
			if (publicService.StringUtil(map.get("loginUserId"))) {
				hashMap.put("userId", map.get("loginUserId"));
				uPlayerList = baseDAO.find("from UPlayer where UUser.userId=:userId and UTeam.teamId is not null and inTeam='1'",hashMap);
				if (null != uPlayerList && uPlayerList.size() > 0) {
					for (UPlayer uPlayer : uPlayerList) {
						String memberType = "1";
						map.put("playerId", uPlayer.getPlayerId());
						List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
						if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
							for (UPlayerRole uPlayerRole : uPlayerRoles) {
								if (!"1".equals(uPlayerRole.getMemberType())) {
									memberType = uPlayerRole.getMemberType();
									break;
								}
							}
						}
						if (!"1".equals(memberType)) {
							uPlayers.add(uPlayer);
						}
					}
				}
			}
		}
		return uPlayers;
	}

	/**
	 * 
	 * 
	   TODO - 转让时，球员列表
	   @param map
	   		loginUserId	当前用户Id
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> transferPlayerList202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		//球员列表
		List<HashMap<String, Object>> transferPlayerList = null;
		//球员
		List<Object> listPlayer = new ArrayList<Object>();
		//转让时的球员列表
		StringBuffer sql = this.transferPlayerList202_sql;
		
		hashMap.put("loginUserId", map.get("loginUserId"));
		hashMap.put("teamId", map.get("teamId"));
		//分页
		transferPlayerList = baseDAO.findSQLMap(sql.toString(),hashMap);
		if (null != transferPlayerList && transferPlayerList.size() > 0) {
			for (HashMap<String, Object> hashMap2 : transferPlayerList) {
				//设置对应名称
				this.displayDataTransfer202(hashMap2);
				//填充角色
				hashMap2 = uPlayerRoleService.setMembertype202(hashMap2);
				//角标需要对应的角标
				if (null != hashMap2.get("playerId")) {
					String playerId = hashMap2.get("playerId").toString();
					listPlayer.add(playerId);
				}
				//身价
				map.put("userId", (String)hashMap2.get("userId"));
				hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
			}
		}
		
		//角标
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if (null != transferPlayerList && transferPlayerList.size() > 0) {
			cornerList = cornerService.getPlayerCorner(map, listPlayer);
		}
		resultMap.put("transferPlayerList", transferPlayerList);
		resultMap.put("cornerList", cornerList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 转让选择球员列表，填充对应的名称
	   @param hashMap
	   		position		位置
	   		userId			用户Id
	   		sex				性别
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	private void displayDataTransfer202(HashMap<String, Object> hashMap) throws Exception {
		String practiceFootName = null;
		String practiceFoot = null;
		String expertPosition = null;
		String expertPositionName = null;
		String canPosition = null;
		String canPositionName = null;
		if (null != hashMap) {
			//位置
			if (hashMap.get("position") != null && !"".equals(hashMap.get("position"))) {
				if (!"16".equals(hashMap.get("position"))) {
					hashMap.put("positionName",Public_Cache.HASH_PARAMS("position").get(hashMap.get("position")));
				}else{
					hashMap.put("positionName", null);
				}
			}else{
				hashMap.put("positionName", null);
			}
			if (null != hashMap.get("userId")) {//设置初始球员的信息
				UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap);//查看当前用户的初始信息
				if (null != uPlayerTemp) {
					//惯用脚
					if (publicService.StringUtil(uPlayerTemp.getPracticeFoot())) {
						practiceFoot = uPlayerTemp.getPracticeFoot();
						if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
							practiceFootName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
						}
					}
					//擅长位置
					if (publicService.StringUtil(uPlayerTemp.getExpertPosition())) {
						expertPosition = uPlayerTemp.getExpertPosition();
						if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
							expertPositionName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
						}
					}
					//可踢位置
					if (publicService.StringUtil(uPlayerTemp.getCanPosition())) {
						canPosition = uPlayerTemp.getCanPosition();
						if (!"15".equals(uPlayerTemp.getCanPosition())) {
							canPositionName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
						}
					}
				}
				hashMap.put("practiceFoot", practiceFoot);//惯用脚
				hashMap.put("practiceFootName", practiceFootName);
				hashMap.put("expertPosition", expertPosition);//擅长位置
				hashMap.put("expertPositionName", expertPositionName);
				hashMap.put("canPosition", canPosition);//可踢位置
				hashMap.put("canPositionName", canPositionName);
				
			}
			if (hashMap.get("sex") != null && !"".equals(hashMap.get("sex"))) {//性别
				hashMap.put("sexName",Public_Cache.HASH_PARAMS("sex").get(hashMap.get("sex")));
			}else{
				hashMap.put("sexName", null);
			}
		}
		
		
	
	}

	/**
	 * 
	 * 
	   TODO - 将球员身份添加到身份表里去
	   @param map
	   @return
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateMemberType202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//查询所有有球队的球员
		List<UPlayer> uPlayers = baseDAO.find("from UPlayer where UTeam.teamId is not null");
		//判断是否为空
		if (null != uPlayers && uPlayers.size() > 0) {
			//循环遍历所有球员
			for (UPlayer uPlayer : uPlayers) {
				//判断身份是否为空
				if (publicService.StringUtil(uPlayer.getMemberType())) {
					uPlayerRoleService.insertNew(uPlayer);
				}
			}
		}
		resultMap.put("success", "更新成功");
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 根据userId查询为队长对球员Idlist
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getMemberTypeUplayerByUserId(HashMap<String, String> map)throws Exception{
		//查询用户对象
//		StringBuffer sql = this.getMemberTypeUplayerByUserId_sql;
		StringBuffer sql = new StringBuffer("select p.player_id playerId from u_player p "
				+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
				+" where p.user_id=:userId and p.in_team='1' ");
		if (publicService.StringUtil(map.get("teamId"))) {
			sql.append(" and p.team_id=:teamId ");
		}
		sql.append(" order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc ");
		
		List<HashMap<String, Object>> listTeam = baseDAO.findSQLMap(map,sql.toString());
		return listTeam;
	}
	
	/**
	 * 
	 * 
	   TODO - 根据userId查询为非队长对球员Idlist
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getNoMemberTypeUplayerByUserId(HashMap<String, String> map)throws Exception{
		//查询用户对象
		StringBuffer sql = this.getNoMemberTypeUplayerByUserId_sql;
		List<HashMap<String, Object>> listTeam = baseDAO.findSQLMap(map,sql.toString());
		return listTeam;
	}
	

	/**
	 * 
	 * 
	   TODO - 设置默认球队
	   @param map
	   		playerId		球员Id
	   		loginUserId		当前用户Id
	   @return
	   @throws Exception
	   2016年6月20日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setDefaultUteam202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("playerId"))) {
			//先将所有默认球队设为非默认
			List<UPlayer> uPlayers = baseDAO.find(map,"from UPlayer where defaultUteam='1' and inTeam='1' and UUser.userId=:loginUserId and UTeam.teamId is not null");
			if (null != uPlayers && uPlayers.size() > 0) {
				for (UPlayer uPlayer : uPlayers) {
					uPlayer.setDefaultUteam(null);
					baseDAO.update(uPlayer);
				}
			}
			//再将前端传过来的球队设置为默认
			UPlayer uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId");
			if (null != uPlayer) {
				uPlayer.setDefaultUteam("1");
				baseDAO.update(uPlayer);
				resultMap.put("success", "默认球队设置成功");
			}
		}
		return resultMap;
		
	}

	/**
	 * 
	 * 
	   TODO - 招募球员
	   @param map
	   		teamId			球队Id
	   @param addressBooks  通讯录计划
	   @return
	   @throws Exception
	   2016年6月21日
	   dengqiuru
	 */
	@SuppressWarnings("unused")
	@Override
	public HashMap<String, Object> recruitPlayerList202(HashMap<String, String> map,
			List<Map<String, Object>> addressBooks) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, Object> allResultMap = new HashMap<>();
		HashMap<String, Object> resultPhoneMap = new HashMap<>();
		HashMap<String, String> resultMap = new HashMap<>();
		//推送的集合
		HashMap<String, String> pushMap = new HashMap<>();
		String name = null;//姓名
		String imgurl = null;//头像
		String upboxName = null;//Upbox的真实名字
		String status = null;//1:未进入Upbox，2：已加入Upbox；3：已加入当前球队
			//先获取缓过来的list
		if (null != addressBooks && addressBooks.size() > 0) {
			for (Map<String, Object> map2 : addressBooks) {
				resultMap = new HashMap<>();
				name = map2.get("name").toString();//姓名
				imgurl = null;//头像
				upboxName = null;//Upbox的真实名字
				status = "1";//1-未加入Upbox，未招募；2-未加入Upbox，已招募；3-已加入Upbox，未招募  4-已加入Upbox，已招募  5-已加入当前球队
				if (null != map2.get("cellPhone")) {
					String phone = map2.get("cellPhone").toString();
					map.put("phone", phone);
					//判断是否招募过
					pushMap.put("object_id", map.get("teamId")+phone);
					pushMap.put("type", "4");
					pushMap.put("event", "9");
					pushMap.put("push_type", "1");
					pushMap.put("push_status", "-1");
					boolean isPush = publicPushService.getPushStatus(pushMap);
					//先查询手机号是否存在系统内
					UUser uUser = baseDAO.get(map, "from UUser where phone=:phone");
					//存在
					if (null != uUser) {//
						map.put("userId", uUser.getUserId());
						//在查询该用户是否在当前球队中
						UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
						//在当前球队中
						if (null != uPlayer) {//已加入
							//状态
							status = "5";
						}else{//未加入
							//已招募
							if (false == isPush) {
								status = "4";//1-未加入Upbox，未招募；2-未加入Upbox，已招募；3-已加入Upbox，未招募  4-已加入Upbox，已招募  5-已加入当前球队
							}else{
								//未招募
								status = "3";//1-未加入Upbox，未招募；2-未加入Upbox，已招募；3-已加入Upbox，未招募  4-已加入Upbox，已招募  5-已加入当前球队
							}
						}
						//名字
						if (publicService.StringUtil(uUser.getRealname())) {
							upboxName = uUser.getRealname();
						}else{
							upboxName = uUser.getNickname();
						}
						//头像
						UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
						if (null != uUserImg) {
							imgurl = uUserImg.getImgurl();
						}
					}else{
						//已招募
						if (false == isPush) {
							status = "2";//1-未加入Upbox，未招募；2-未加入Upbox，已招募；3-已加入Upbox，未招募  4-已加入Upbox，已招募  5-已加入当前球队
						}else{
							//未招募
							status = "1";//1-未加入Upbox，未招募；2-未加入Upbox，已招募；3-已加入Upbox，未招募  4-已加入Upbox，已招募  5-已加入当前球队
						}
					}
					resultMap.put("name", name);
					resultMap.put("imgurl", imgurl);
					resultMap.put("phone", phone);
					resultMap.put("upboxName", upboxName);
					resultMap.put("status", status);
					resultPhoneMap.put(phone, resultMap);
					
				}
			}
		}
		try {
			HttpUtil http = new HttpUtil();
			hashMap.put("code", map.get("code"));
			hashMap.put("userId", map.get("loginUserId"));
			hashMap.put("phoneList", map.get("addressbooks"));
			HttpRespons response = http.sendPost(Public_Cache.UPBOX_LOG_URL+"/uuPhoneLog_insPhoneList.do", hashMap);
			HashMap<String, Object> map1 = (HashMap<String, Object>) PublicMethod.parseJSON2Map(response.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		allResultMap.put("addressBooks", resultPhoneMap);
		return resultPhoneMap;
	}

	/**
	 * 
	 * 
	   TODO - 招募球员发送通知
	   @param map
	   		cellPhones		手机号
	   		teamId			球队Id
	   @return
	   @throws Exception
	   2016年6月22日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> recruitPlayer202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, String> hashMap = new HashMap<>();
		if (publicService.StringUtil(map.get("teamId"))) {
			if (publicService.StringUtil(map.get("cellPhones"))) {
				UTeam uTeam = uTeamService.findPlayerInfoById(map);
				if (null != uTeam) {
					UUser uUserTemp = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
					String userName = "";
					if (null != uUserTemp) {
						//名称不能超过十个字
						if (publicService.StringUtil(uUserTemp.getRealname())) {
							userName = uUserTemp.getRealname();
							int userNameLength = userName.length();
							if (userNameLength > 10) {
								userName = uUserTemp.getRealname().substring(0, 10);
							}
						}else if (publicService.StringUtil(uUserTemp.getNickname())) {
							userName = uUserTemp.getNickname();
							int userNameLength = userName.length();
							if (userNameLength > 10) {
								userName = uUserTemp.getNickname().substring(0, 10);
							}
						}
					}
					hashMap.put("teamId", uTeam.getTeamId());
					hashMap.put("phoneList", map.get("cellPhones"));
					System.out.println("234="+userName);
					hashMap.put("content", Public_Cache.getSPCon("tmRecruitPlayer")
							.replace("XXX", userName));
					Boolean isSuccess = messageService.sendRecruitPlayer(hashMap);
					if (isSuccess) {
						resultMap.put("success", "招募短信发送成功");
					}else{
						return WebPublicMehod.returnRet("error", "招募短信发送失败");
					}
				}else{
					return WebPublicMehod.returnRet("error", "当前球队有误");
				}
			}else{
				return WebPublicMehod.returnRet("error", "请求参数cellPhones不能为空");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数teamId不能为空");
		}
		return resultMap;
	}
	

	@Override
	public HashMap<String, Object> findFilterPlayer(HashMap<String, String> map) throws Exception {
		HashMap<String,List<Object>> mapList=new HashMap<>();
		List<Object> listStr=null;
		HashMap<String, Object> hashMap = PublicMethod.Maps_Mapo(map);
		StringBuffer sql = new StringBuffer(
				"select u.user_id userId,p.player_id  playerId,u.sex sex,u.nickname nickname,u.realname realname,(year(now())-year(u.birthday)-1) + ( DATE_FORMAT(u.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) age,u.height,");
		sql.append(
				"u.weight,u.birthday birthday,u.remark remark,p.expert_position expertPosition,p.practice_foot practiceFoot,");
		sql.append(
				"p.can_position canPosition,p.member_type memberType,p.position position,p.number number,uui.imgurl imgurl ");
		sql.append(" from u_player p left join u_user u on p.user_id = u.user_id");
		sql.append(" left join u_user_img uui on uui.user_id=p.user_id and uui.uimg_using_type='1' ");
		this.playerInfoRemove(sql,map);//排除条件
		
		// 擅长位子
		if (null != map.get("expertPosition") && !"".equals(map.get("expertPosition")) ) {
			String expertPosition = map.get("expertPosition");
			String[] strs = expertPosition.split(",");
			listStr = new ArrayList<>();
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			mapList.put("position", listStr);
			sql.append(" and expert_position in (:position ) ");
		}

		// 年龄
//		this.filterInfoOption(map, count, sql, map.get("age"), "u.age ");
		this.filterInfoOption(map, count, sql, map.get("age"), "(year(now())-year(u.birthday)-1) + ( DATE_FORMAT(u.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) ");

		// 身高
		this.filterInfoOption(map, count, sql, map.get("height"), "u.height ");

		// 体重
		this.filterInfoOption(map, count, sql, map.get("weight"), "u.weight ");

		// 加入球队数-特殊处理未加入球队、加入一支球队
		if (null != map.get("count") && !"".equals(map.get("count"))) {
			String[] countList = map.get("count").split(",");
			if (countList.length > 0) {
				count = 0;
				for (String string : countList) {
					count++;
					if (count == 1) {
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (!"0".equals(string) && !"1".equals(string)) {// 没有加入过队伍
						this.filterInfoOptionPart(map,sql,string,"(select count(p1.user_id) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ) ");
					} else {
						sql.append("(select count(p1.user_id) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ) = "+string);
//						if ("0".equals(string)) {
//							sql.append(
//									"( p.user_id not in (select user_id from u_player p1 where  p1.in_team ='1' ) ) ");
//						}
//						if ("1".equals(string)) {
//							sql.append(
//									"( p.user_id not in (select user_id from (select user_id,count(user_id) count from u_player p1 where  p1.in_team ='1' group by user_id ) temp_player where count <=1) ) ");
//						}
					}
					if (count >= countList.length) {

						sql.append(" ) ");
					}

				}
			}
		}
		// 地区
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			hashMap.put("area", map.get("area"));
			URegion region = baseDAO.get(URegion.class, map.get("area"));
			if (region != null && "2".equals(region.getType())) {
				sql.append(" and u.area in (select _id from u_region where parent=:area ) ");
			} else {
				String area = map.get("area");
				String[] strs = area.split(",");
				listStr = new ArrayList<>();
				for (int i = 0; i < strs.length; i++) {
					listStr.add(strs[i]);
				}
				map.remove("area");
				hashMap.remove("area");
				mapList.put("area", listStr);
				sql.append(" and u.area in (:area ) ");
			}
		}
		// 球队归属
		if (null != map.get("teamBelonging") && !"".equals(map.get("teamBelonging"))) {
			String teamBelonging = map.get("teamBelonging");
			String[] strs = teamBelonging.split(",");
			listStr = new ArrayList<>();
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			mapList.put("belonging", listStr);
			sql.append(" and team_belonging in (:belonging) ");

		}
		
		this.playerOrderByInfo(map,sql);
		// 分页
		List<HashMap<String, Object>> listPlayer = this.getFilterPageLimit(sql, hashMap, map,mapList);
		//角标
		List<Object> listPlayerid = new ArrayList<>();
		List<List<CornerBean>> cornerList = new ArrayList<>();
		OutPlayerList outPlayerList = new OutPlayerList();
		if(map.get("loginUserId")!=null&&!"".equals(map.get("loginUserId"))){
			if (null != listPlayer && listPlayer.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listPlayer) {
					String playerId = hashMap2.get("playerId").toString();
					listPlayerid.add(playerId);
					this.displayData(hashMap2,map);//数据处理
					//身价
					map.put("userId", (String)hashMap2.get("userId"));
					hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
				}
			}
			if (null != listPlayer && listPlayer.size() > 0) {
				cornerList = cornerService.getPlayerCorner(map, listPlayerid);
			}
		}
		
		UPlayer uPlayer = this.getUplayerByUserId(map);
		if (null != uPlayer) {
			outPlayerList = this.setOutPlayerList(null,uPlayer,map);
			//填充球员的身份
			outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
		}
		
		hashMap = new HashMap<>();
		
		hashMap.put("cornerList", cornerList);//角标
		hashMap.put("outPlayerList", outPlayerList);
		hashMap.put("outPlayerLists", listPlayer);// 球员筛选
		return hashMap;
	}

	/**
	 * 球员数据排除下项
	 * @param sql 主SQL
	 * @param map teamId－球队ID，type－标示
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	private void playerInfoRemove(StringBuffer sql, HashMap<String, String> map) throws Exception{
		String sql1=" where p.team_id is null and u.user_id is not null  ";
		if (publicService.StringUtil(map.get("teamId"))) {
			if ("1".equals(map.get("type"))) {//1:代表邀请人的球员列表
				sql1= " where p.user_id not in (select pp.user_id from u_player pp where pp.team_id=:teamId and pp.in_team='1') and u.user_id is not null and p.team_id is null ";//排除自己加入过的球队
			}else if ("2".equals(map.get("type"))){//2:代表管理球员的球员列表
				sql1= "  where p.team_id=:teamId and p.in_team='1' ";
			}
		}
		sql.append(sql1);
		
	}
	/**
	 * 球员筛选排序
	 * @param map
	 * @param sql
	 */
	private void playerOrderByInfo(HashMap<String, String> map,StringBuffer sql) throws Exception{
//		String locationSql="";
		sql.append(" group by p.user_id order by ");//分组
		//排序
		if (map.get("orderByPlayer") != null && !"".equals(map.get("orderByPlayer"))) {
			// 加入时间
			if ("1".equals(map.get("orderByPlayer"))) {
//				sql.append(" abs(datediff(u.createdate,date_format(now(),'%Y-%m-%d'))) desc,p.user_id asc");
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}
			// 离我最近
			if ("2".equals(map.get("orderByPlayer"))) {
//				if(map.get("region")!=null&&!"".equals(map.get("region"))){
//					locationSql=this.checkScreen(map,"user");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(p.user_id,"+locationSql+"),");
//					}
//				}else{
//					locationSql=this.lbsOrderSql(map,"user");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(p.user_id,"+locationSql+"),");
//					}
//				}
//				sql.append(" abs(datediff(u.createdate,date_format(now(),'%Y-%m-%d'))) desc,p.user_id asc ");
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}
			// 球队活跃
			if ("3".equals(map.get("orderByPlayer"))) {
				 sql.append(" p.user_id asc");
			}
			// 积分排名
			if ("4".equals(map.get("orderByPlayer"))) {
				sql.append(" p.user_id asc");
			}
			// 地区
			if ("5".equals(map.get("orderByPlayer"))) {
				sql.append(" u.area desc,p.user_id asc ");
			}
			// 擅长位置
			if ("6".equals(map.get("orderByPlayer"))) {
				sql.append(" expert_position ,p.user_id asc ");
			}
			// 年龄
			if ("7".equals(map.get("orderByPlayer"))) {
				sql.append(" u.age+0 desc,p.user_id asc ");
			}
			// 加入球队数
			if ("8".equals(map.get("orderByPlayer"))) {
				sql.append(" (select count(*) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ),p.user_id asc ");
			}
		} else {
			if("1".equals(map.get("type"))){
//				if(map.get("region")!=null&&!"".equals(map.get("region"))){
//					locationSql=this.checkScreen(map,"user");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(p.user_id,"+locationSql+"),");
//					}
//				}else{
//					locationSql=this.lbsOrderSql(map,"user");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(p.user_id,"+locationSql+"),");
//					}
//				}
//				sql.append(" abs(datediff(u.createdate,date_format(now(),'%Y-%m-%d'))) desc,p.user_id asc ");
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}else{
//				sql.append(" abs(datediff(u.createdate,date_format(now(),'%Y-%m-%d'))) desc,p.user_id asc ");//
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}
		}
	}
	/**
	 * 筛选共同
	 * @param map －－扩展用
	 * @param count 初始化数据
	 * @param sql 主SQL
	 * @param strFilterOption 筛选项
	 * @param strSql 筛选项的SQL
	 * xiaoying 2016年6月17日
	 */
	private void filterInfoOption(HashMap<String, String> map, int count, StringBuffer sql,String strFilterOption,String strSql) {
		// 胜率 如果胜率chances没有值就跟着条件查询，否者就查询全部(等于不加条件)
		if (null != strFilterOption && !"".equals(strFilterOption)) {
			String[] chancesList = strFilterOption.split(",");
			if (chancesList.length > 0) {
				for (String string : chancesList) {
					count++;
					if (count == 1) { // 控制并且关系 （ 胜率和排名 and关系 胜率和胜率 or关系）
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (null != string.split("-")[0] && !"".equals(string.split("-")[0])) {
						hashMap.put("startValue", Integer.parseInt(string.split("-")[0]));
						if (string.split("-").length >= 2) {// 正常区间
							hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
							sql.append("( " + strSql + " >= " + hashMap.get("startValue"));
							sql.append(" and " + strSql + " <= " + hashMap.get("endValue") + ") ");
						} else {// 以上
							sql.append("( " + strSql + " > " + hashMap.get("startValue") + ") ");
						}
					} else {// 以下
						if (null != string.split("-")[1] && !"".equals(string.split("-")[1])) {
							hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
							sql.append("( " + strSql + " < " + hashMap.get("endValue") + ") ");
						}
					}
					if (count >= chancesList.length) {
						sql.append(" ) ");
					}
				}
			}
		}
	}
	/**
	 * 筛选共同-部分
	 * @param map －－扩展用
	 * @param sql 主SQL
	 * @param strFilterOption 筛选项
	 * @param strSql 筛选项的SQL
	 * xiaoying 2016年6月17日
	 */
	private void filterInfoOptionPart(HashMap<String, String> map, StringBuffer sql, String string, String strSql) {
		if (null != string.split("-")[0] && !"".equals(string.split("-")[0])) {
			hashMap.put("startValue", Integer.parseInt(string.split("-")[0]));
			if (string.split("-").length >= 2) {// 正常区间
				hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
				sql.append("( " + strSql + " >= " + hashMap.get("startValue"));
				sql.append(" and " + strSql + " <=" + hashMap.get("endValue") + ") ");
			} else {// 以上
				sql.append("( " + strSql + " > " + hashMap.get("startValue") + ") ");
			}
		} else {// 以下
			if (null != string.split("-")[1] && !"".equals(string.split("-")[1])) {
				hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
				sql.append("( " + strSql + " < " + hashMap.get("endValue") + ") ");
			}
		}
		
	}/**
	 * 
	 * TODO 分页代码
	 * 
	 * @param sql
	 *            语句
	 * @param hashMap
	 *            查询参数
	 * @param map
	 *            分页参数
	 * @param count
	 *            总记录数
	 * @return
	 * @throws Exception
	 *             List<Object> xiao 2016年3月8日
	 */
	private List<HashMap<String, Object>> getFilterPageLimit(StringBuffer sql, HashMap<String, Object> hashMap,
			HashMap<String, String> map,HashMap<String,List<Object>> mapList) throws Exception {
		// 分页
		if (null != map.get("page") && !"".equals(map.get("page"))) {
			List<HashMap<String, Object>> list = null;
			if(mapList!=null){
				list =baseDAO.findSQLMap(sql.toString(), hashMap, mapList);
			}else{
				list =baseDAO.findSQLMap(sql.toString(), hashMap);
			}
			if (list != null && list.size() > 0) {
				PageLimit pa = new PageLimit(Integer.parseInt(map.get("page")), list.size());
				hashMap.put("limit", pa.getLimit());
				hashMap.put("offset", pa.getOffset());
				sql.append(" limit :limit offset :offset");
				if(mapList!=null){
					return baseDAO.findSQLMap(sql.toString(), hashMap, mapList);
				}
				return baseDAO.findSQLMap(sql.toString(), hashMap);
			}
		}
		return null;
	}
	/**
	 * 检索某个城市下信息
	 * @param map location－百度经纬度、region－地址（上海市，宝山区）
	 * @param tags   team－球队，user-用户（球员）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月23日
	 */
//	private String checkScreen(HashMap<String, String> map,String tags) throws Exception {
//		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
//		String lbsBack="";
//		String orderSql="";
//		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
//		lbsMap.put("location", map.get("location"));//百度经纬度
//		lbsMap.put("coord_type", "3");//百度坐标系
//		//lbsMap.put("radius", "10000000000");//半径范围
//		lbsMap.put("tags", tags);//搜索球场
//		lbsMap.put("region", map.get("region"));//地区地址
//		lbsMap.put("sortby", "distance:1");//距离升序
//		lbsMap.put("url", Public_Cache.LBS_LOCATION);
//		lbsBack=lBSService.getNearByCity(lbsMap);//按区域搜索
//		
//		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);//组装接口返回对象
//		for(BdLbsBean lbstemp:lbsList){
//			orderSql+="'"+lbstemp.getObject_id()+"',";
//		}
//		//如果没有球场排序就使用默认排序
//		if (null != orderSql) {
//			if(orderSql.length()>0){
//				orderSql=orderSql.substring(0, orderSql.length()-1);
//			}
//		}
//		return orderSql;
//	}
	/**
	 * 检索中心点多少公里下信息
	 * @param map location－百度经纬度
	 * @param tags  team－球队，user-用户（球员）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月23日
	 */
//	private String lbsOrderSql (HashMap<String, String> map,String tags) throws Exception{
//		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
//		String orderSql="";
//		String lbsBack="";
//		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
//		lbsMap.put("location", map.get("location"));//百度经纬度
//		lbsMap.put("coord_type", "3");//百度坐标系
//		lbsMap.put("radius", "500000");//半径范围
//		lbsMap.put("tags", tags);//搜索球场
//		lbsMap.put("sortby", "distance:1");//距离升序
//		lbsMap.put("url", Public_Cache.LBS_LOCATION);
//		lbsBack=lBSService.getNearBy(lbsMap);
//		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);
//		for(BdLbsBean lbstemp:lbsList){
//			orderSql+="'"+lbstemp.getObject_id()+"',";
//		}
//		//如果没有球场排序就使用默认排序
//		if (null != orderSql) {
//			if(orderSql.length()>0){
//				orderSql=orderSql.substring(0, orderSql.length()-1);
//			}
//		}
//		return orderSql;
//	}
	
	/**
	 * 
	 * 
	   TODO - 球员列表2.0.2
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月25日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findPlayerListByType202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		List<HashMap<String, Object>> playerList = null;
		if (publicService.StringUtil(map.get("teamId"))) {
			//xiao -踢人权限增加新需求
			Integer lvl1=this.getTeamIdAndUserIdPlayer(map);//登录人权限等级
			Integer lvl2=0;//球员列表中球员等级
			List<HashMap<String, Object>> listPlayerRoleLimit=null;
			//--end
			StringBuffer sql = this.findPlayerListByType202_sql;
			//分页
			playerList = baseDAO.findSQLMap(map,sql.toString());
			if (playerList != null && playerList.size() > 0) {
				for (HashMap<String, Object> hashMap : playerList) {
					//设置参数对应名称
					this.displayDataByType202(hashMap);
					//填充球员为球员的角色
					uPlayerRoleService.setMembertypeByplayer202(hashMap);
					//xiao -
					map.put("playerId", (String)hashMap.get("playerId"));
					lvl2=this.getPlayerIdPlayer(map,listPlayerRoleLimit,lvl2);
					hashMap.put("lvl", lvl2);
					hashMap.put("isMyTeam", "1");
					this.setIsExculdes(hashMap,map);//查看踢人按钮
					this.setIsAllot(hashMap,map);//查看分配对内角色按钮
					this.setIsEdit(hashMap, map);//查看修改信息按钮
					this.compareLvl(hashMap,lvl1,lvl2,map);
					//身价
					map.put("userId", (String)hashMap.get("userId"));
					hashMap.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
				}
			}
			resultMap.put("playerList", playerList);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数teamId不能为空");
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 球队详情--球员列表对应字段
	   @param hashMap2
	   2016年6月25日
	   dengqiuru
	 * @throws Exception 
	 */
	private void displayDataByType202(HashMap<String, Object> hashMap2) throws Exception {
		if (null != hashMap2) {
			String praticeFootName = null;
			String homeTeamShirtsUrl = null;
			String homeTeamShirtsColor = null;
			String positionName = null;
			String sexName = null;
			if (null != hashMap2.get("userId")) {//惯用脚
				UPlayer uPlayerTemp = this.getUplayerByuserIdInteam(hashMap2);//查看当前用户的初始信息
				if (null != uPlayerTemp) {
					if (publicService.StringUtil(uPlayerTemp.getPracticeFoot())) {
						if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
							praticeFootName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
						}
					}
				}
			}
			//位置
			if (null != hashMap2.get("position")) {
				if (!"16".equals(hashMap2.get("position"))) {
					positionName = Public_Cache.HASH_PARAMS("position").get(hashMap2.get("position"));
				}
			}
			//性别
			if (null != hashMap2.get("sex")) {
				sexName = Public_Cache.HASH_PARAMS("sex").get(hashMap2.get("sex"));
			}
			//年龄
			if (null == hashMap.get("age")) {
				if (null != hashMap.get("birthday") && !"".equals(hashMap.get("birthday"))) {
					hashMap.put("age",getAgeByBirthday(PublicMethod.getStringToDate(hashMap.get("birthday").toString(), "yyyy-MM-dd")));
				}
			}
			if (null != hashMap2.get("homeTeamShirts")) {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("name", "home_team_shirts");
				hashMap.put("params", hashMap2.get("homeTeamShirts").toString());
				UParameterInfo uParameterInfo  = uParameterService.getMemberTypeByTeamId202(hashMap);
				if (null != uParameterInfo) {
					homeTeamShirtsColor = uParameterInfo.getName();
					if (publicService.StringUtil(uParameterInfo.getImgurl())) {
						homeTeamShirtsUrl = uParameterInfo.getImgurl();
					}
				}
			}
			hashMap2.put("praticeFootName", praticeFootName);
			hashMap2.put("homeTeamShirtsUrl", homeTeamShirtsUrl);
			hashMap2.put("homeTeamShirtsColor", homeTeamShirtsColor);
			hashMap2.put("positionName", positionName);
			hashMap2.put("sexName", sexName);
		}
	}
	/**
	 * 
	 * 
	   TODO - 官员列表2.0.2
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月25日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findManagerListByType202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		List<HashMap<String, Object>> playerList = null;
		if (publicService.StringUtil(map.get("teamId"))) {
			//xiao -踢人权限增加新需求
			Integer lvl1=this.getTeamIdAndUserIdPlayer(map);//登录人权限等级
			Integer lvl2=0;//球员列表中球员等级
			List<HashMap<String, Object>> listPlayerRoleLimit=null;
			//---end
			StringBuffer sql = this.findManagerListByType202_sql;
			//分页
			playerList = baseDAO.findSQLMap(map,sql.toString());
			if (playerList != null && playerList.size() > 0) {
				for (HashMap<String, Object> hashMap2 : playerList) {
					//设置参数对应名称
					this.displayDataByType202(hashMap2);
					//填充球员为球员的角色
					uPlayerRoleService.setMembertypeByManager202(hashMap2);
					
					//xiao -
					map.put("playerId", (String)hashMap2.get("playerId"));
					lvl2=this.getPlayerIdPlayer(map,listPlayerRoleLimit,lvl2);
					hashMap2.put("lvl", lvl2);
					hashMap2.put("isMyTeam", "1");
					this.setIsExculdes(hashMap2,map);//查看踢人按钮
					this.setIsAllot(hashMap2,map);//查看分配对内角色按钮
					this.setIsEdit(hashMap2, map);//查看修改信息按钮
					this.compareLvl(hashMap2,lvl1,lvl2,map);
					//身价
					map.put("userId", (String)hashMap2.get("userId"));
					hashMap2.put("worthCount", uworthService.findUserIdWorthCount(map).get("count").toString());
				}
			}
			resultMap.put("playerList", playerList);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数teamId不能为空");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 更新初始球员信息
	   @param map
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @return
	   2016年7月13日
	   dengqiuru
	 */
	@Override
	public UPlayer setNoTeamPlayerInfoByUser(HashMap<String, String> map, UUser uUser) throws Exception {
		map.put("userId", uUser.getUserId());
		UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null ");//查看是否存在初始信息
		if (null != uPlayer) {//存在:只做更新
			if (publicService.StringUtil(map.get("practiceFoot"))) {
				uPlayer.setPracticeFoot(map.get("practiceFoot"));//惯用脚
			}
			if (publicService.StringUtil(map.get("expertPosition"))) {
				uPlayer.setExpertPosition(map.get("expertPosition"));//擅长位置
			}
			if (publicService.StringUtil(map.get("canPosition"))) {
				uPlayer.setCanPosition(map.get("canPosition"));//可踢位置
			}
			baseDAO.update(uPlayer);
		}else{//存在:新增
			uPlayer = new UPlayer();
			uPlayer.setPlayerId(WebPublicMehod.getUUID());
			uPlayer.setUUser(uUser);
			uPlayer.setTeamBelonging("2");
			uPlayer.setInTeam("2");
			if (publicService.StringUtil(map.get("practiceFoot"))) {
				uPlayer.setPracticeFoot(map.get("practiceFoot"));//惯用脚
			}
			if (publicService.StringUtil(map.get("expertPosition"))) {
				uPlayer.setExpertPosition(map.get("expertPosition"));//擅长位置
			}
			if (publicService.StringUtil(map.get("canPosition"))) {
				uPlayer.setCanPosition(map.get("canPosition"));//可踢位置
			}
			uPlayer.setAddqd("第三方系统");
			baseDAO.save(uPlayer);
		}
		//更新擅长位置lbs数据
		uUserService.setAreaToBaiduLBS(uUser);
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 更新有球队的球员信息
	   @param map
	   		number			背号
	   		position		位置
	   		memberType		角色
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @return
	   2016年7月14日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public UPlayer setInTeamPlayerInfoByUser(HashMap<String, String> map, UUser uUser,UTeam uTeam) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		map.put("userId", uUser.getUserId());
		map.put("teamId", uTeam.getTeamId());
		UPlayer uPlayer = baseDAO.get(map,"from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
		if (null != uPlayer) {//存在:只做更新
			if (publicService.StringUtil(map.get("number"))) {
				//判断球队中是否存在该位置
				hashMap.put("teamId", uTeam.getTeamId());
				hashMap.put("number", map.get("number"));
				hashMap.put("loginUserId", map.get("userId"));
				boolean isNumber = isExitUteamNumber(hashMap,uTeam);
				if (isNumber == false) {
					uPlayer.setNumber(Integer.parseInt(map.get("number")));//背号
				}else{
					uPlayer = null;
					return uPlayer;
				}
			}
			if (publicService.StringUtil(map.get("position"))) {
				uPlayer.setPosition(map.get("position"));//擅长位置
			}
			uPlayer.setAddqd("第三方系统");
			baseDAO.update(uPlayer);
		}else{//存在:新增
			uPlayer = new UPlayer();
			uPlayer.setPlayerId(WebPublicMehod.getUUID());
			uPlayer.setUUser(uUser);
			uPlayer.setUTeam(uTeam);
			uPlayer.setTeamBelonging("1");
			uPlayer.setInTeam("1");
			uPlayer.setAdddate(new Date());
			uPlayer.setAddtime(new Date());
			if (publicService.StringUtil(map.get("number"))) {
				//判断球队中是否存在该位置
				hashMap.put("teamId", uTeam.getTeamId());
				hashMap.put("number", map.get("number"));
				hashMap.put("loginUserId", map.get("userId"));
				boolean isNumber = isExitUteamNumber(hashMap,uTeam);
				if (isNumber == false) {
					uPlayer.setNumber(Integer.parseInt(map.get("number")));//背号
				}else{
					uPlayer = null;
					return uPlayer;
				}
			}
			if (publicService.StringUtil(map.get("position"))) {
				uPlayer.setPosition(map.get("position"));//擅长位置
			}
			uPlayer.setAddqd("第三方系统");
			baseDAO.save(uPlayer);
		}
		//更新角色信息
		if (publicService.StringUtil(map.get("memberType"))) {
			uPlayerRoleService.setEventsMemberTypeByEditplayer202(map, uPlayer);
		}
		this.setNoTeamPlayerInfoByUser(map, uUser);
		return uPlayer;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队信息（赛事系统）
	   @param map
	   		playerId		球员id 
	   		teamId			球队Id
	   		eventsPlayerId	赛事系统球员Id
	   		phone			手机号
	   		realName		真实姓名
	   		nickName		昵称
	   		sex				性别
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		area			区域
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		canPosition		可踢位置
	   		number			背号
	   		position		位置
	   		memberType		角色
	   @return
	   @throws Exception
	   2016年7月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateUPlayerInfoByEvents(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		//根据球员id查询球员信息
		if (publicService.StringUtil(map.get("playerId")) && publicService.StringUtil(map.get("teamId")) && publicService.StringUtil(map.get("eventsPlayerId"))) {
			if (publicService.StringUtil(map.get("phone"))) {
				UPlayer uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId");
				UUser uUser = null;
				if (null == uPlayer) {
					//为空，根据手机号查询
					uUser = uUserService.getUserinfoByPhone(map);//查用户
				}else{
					map.put("loginUserId", uPlayer.getUUser().getUserId());
					uUser = uUserService.getUserinfoByUserId(map);//查用户
				}
				if (null != uUser) {
					map.put("userId", uUser.getUserId());
					uPlayer = this.setNoTeamPlayerInfoByUser(map, uUser);
					if (null != uUser) {
						if (publicService.StringUtil(map.get("uUserImgurl"))) {
							uUserImgService.uploadHeadPicByEvents(map, uUser);
						}
					}
					if (publicService.StringUtil(map.get("teamId"))) {//再新增有球队的球员信息
						UTeam uTeam = uTeamService.findPlayerInfoById(map);
						if (null != uTeam) {
							uPlayer = this.setInTeamPlayerInfoByUser(map, uUser, uTeam);
						}
					}
					resultMap.put("eventsPlayerId", map.get("eventsPlayerId"));
					resultMap.put("playerId", uPlayer.getPlayerId());
				}
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数必填项不能为空");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 新增球员信息（赛事系统）
	   @param map
	   		teamId			球队Id
	   		eventsPlayerId	赛事系统球员Id
	   		phone			手机号
	   		realName		真实姓名
	   		nickName		昵称
	   		sex				性别
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		area			区域
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		canPosition		可踢位置
	   		number			背号
	   		position		位置
	   		memberType		角色
	   @return
	   @throws Exception
	   2016年7月19日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> createNewPlayerByEvents(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		UPlayer uPlayer = new UPlayer();
		if (publicService.StringUtil(map.get("eventsPlayerId"))) {
			if (publicService.StringUtil(map.get("phone"))){
				UUser uUser = uUserService.getUserinfoByPhone(map);//查用户
				if (null != uUser) {
					if (publicService.StringUtil(map.get("uUserImgurl"))) {
						uUserImgService.uploadHeadPicByEvents(map, uUser);
					}
					//添加初始球员数据
					uPlayer = this.setNoTeamPlayerInfoByUser(map, uUser);
				}
				if (publicService.StringUtil(map.get("teamId"))) {
					//查询球队信息
					UTeam uTeam = uTeamService.findPlayerInfoById(map);
					if (null != uTeam) {
						//添加有球队的球员数据
						uPlayer = this.setInTeamPlayerInfoByUser(map, uUser, uTeam);
					}else{
						return WebPublicMehod.returnRet("error", "teamId无效，查不到有效球队");
					}
				}else{
					return WebPublicMehod.returnRet("error", "请求参数teamId不能为空");
				}
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数必填项eventsPlayerId不能为空");
		}
		resultMap.put("eventsPlayerId", map.get("eventsPlayerId"));
		resultMap.put("playerId", uPlayer.getPlayerId());
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 用户LBS获取球员信息
	   @param userId	用户Id
	   @return
	   2016年7月21日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, String> getLBSUplayerinfo(String userId) throws Exception {
		//返回参数
		HashMap<String, String> resultMap = new HashMap<>();
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("userId", userId);
		String expertPosition = null;
		int addteam = 0;
		//查询初始球员的擅长位置
		List<UPlayer> uPlayers = baseDAO.find(hashMap, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null");
		if (null != uPlayers && uPlayers.size() > 0) {
			UPlayer uPlayer = uPlayers.get(0);
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				expertPosition = uPlayer.getExpertPosition();
			}
		}
		//查询当前球员的加队数
		List<UPlayer> inTeamUplayers = baseDAO.find(hashMap, "from UPlayer where UUser.userId=:userId and inTeam='1' and UTeam.teamId is not null");
		addteam = inTeamUplayers.size();
		resultMap.put("expertPosition", expertPosition);
		resultMap.put("addteam", addteam+"");
		return resultMap;
	}
	




	@Override
	public HashMap<String, Object> playerHonorListPage(HashMap<String, String> hashMap) throws Exception {
		UUser user= null;
		if(StringUtils.isNotEmpty(hashMap.get("playerId"))){
			user = this.uUserService.getUUserByPlayerId(hashMap);
		}else if(StringUtils.isNotEmpty(hashMap.get("token"))){
			user = this.uUserService.getUserinfoByToken(hashMap);
		}else {
			return WebPublicMehod.returnRet("error","token和playerId都为空");
		}
		if(user == null){
			return WebPublicMehod.returnRet("error","未找到用户");
		}
		PageLimit pageLimit = new PageLimit(StringUtils.isEmpty(hashMap.get("page")) ? 1 : Integer.parseInt(hashMap.get("page")), 0);
		String hql = "from UPlayerHonor where user.userId = '" + user.getUserId() + "' order by num desc ";
		List<UPlayerHonor> honors = this.baseDAO.findByPage(hql, null, pageLimit.getLimit() ,pageLimit.getOffset());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("honor", this.formatterPlayerHonorList(honors, pageLimit.getOffset()));
		return retMap;
	}
	//封装格式化个人荣誉
	private List<Map<String,Object>> formatterPlayerHonorList(List<UPlayerHonor> honors,int offset) throws ParseException {
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,Object> map = null;
		for(UPlayerHonor honor : honors){
			offset++;
			map = new HashMap<String,Object>();
			map.put("keyId",honor.getKeyId());
			map.put("remark",honor.getRemark());
			map.put("createDate",honor.getCreateDate());
			map.put("num",honor.getNum());
			map.put("honorDateE",honor.getHonorDateEStr());
			map.put("honorDateC",honor.getHonorDateCStr());
			map.put("index",offset);
			list.add(map);
		}
		return list;
	}

	/**
	 * TODO - 新增球员荣誉墙
	 * @param hashMap
	 * fuserId 新增荣誉墙用户Id
	 * remark 事件描述
	 * @return
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> savePlayerHonor(HashMap<String, String> hashMap) throws Exception {

		//通过token获取获取用户
		UUser user = this.uUserService.getUserinfoByToken(hashMap);

		if(user == null || user.getUserId() == null)
			return WebPublicMehod.returnRet("error","未找到登录用户");

//		//检查是否有用户Id
//		if (StringUtils.isEmpty(hashMap.get("fuserId")))
//			return WebPublicMehod.returnRet("error", "未找到用户");
		//检查球员荣誉描述是否为空
		if(StringUtils.isEmpty(hashMap.get("remark"))){
			return WebPublicMehod.returnRet("error", "荣誉墙事件描述不能为空");
		}
		if(StringUtils.isEmpty(hashMap.get("honorDate"))){
			return WebPublicMehod.returnRet("error","荣誉时间不能为空");
		}



		hashMap.put("fuserId",user.getUserId());

		//疯长球员荣誉对象
		UPlayerHonor honor = new UPlayerHonor();
		honor.setKeyId(WebPublicMehod.getUUID());
		honor.setCreateDate(new Date());
		//通过Token获取用户对象
		honor.setUser(user);
		honor.setRemark(hashMap.get("remark"));
		//荣誉发生时间
		honor.setHonorDate(PublicMethod.getStringToDate(hashMap.get("honorDate"),"yyyy-MM-dd"));
		//获取排序值
		honor.setNum(this.getMaxHonorNum(hashMap));
		this.baseDAO.save(honor);

		return WebPublicMehod.returnRet("success","新增个人荣誉成功");
	}

	/**
	 * TODO - 修改球员荣誉墙
	 * @param hashMap
	 * fuserId 修改荣誉墙用户Id
	 * remark 事件描述
	 * @return
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> updatePlayerHonor(HashMap<String, String> hashMap) throws Exception {
		//检查是否有球员荣誉
		if(StringUtils.isEmpty(hashMap.get("keyId"))){
			return WebPublicMehod.returnRet("error", "未找到球员荣誉");
		}

		UPlayerHonor honor = this.baseDAO.get(UPlayerHonor.class,hashMap.get("keyId"));
		if(honor==null){
			return WebPublicMehod.returnRet("error", "未找到球员荣誉");
		}

		//检查球员荣誉描述是否为空
		if(StringUtils.isEmpty(hashMap.get("remark"))){
			return WebPublicMehod.returnRet("error", "荣誉墙事件描述不能为空");
		}
		if(StringUtils.isEmpty(hashMap.get("honorDate"))){
			return WebPublicMehod.returnRet("error","荣誉时间不能为空");
		}

		//荣誉发生时间
		honor.setHonorDate(PublicMethod.getStringToDate(hashMap.get("honorDate"),"yyyy-MM-dd"));

		honor.setRemark(hashMap.get("remark"));
		this.baseDAO.update(honor);
		return WebPublicMehod.returnRet("success","修改个人荣誉成功");
	}

	@Override
	public HashMap<String, Object> sortPlayerHonor(HashMap<String, String> hashMap) throws Exception {
		//通过token获取获取用户
		UUser user= null;
		if(StringUtils.isNotEmpty(hashMap.get("playerId"))){
			user = this.uUserService.getUUserByPlayerId(hashMap);
		}else if(StringUtils.isNotEmpty(hashMap.get("token"))){
			user = this.uUserService.getUserinfoByToken(hashMap);
		}else {
			return WebPublicMehod.returnRet("error","token和playerId都为空");
		}
		if(user == null){
			return WebPublicMehod.returnRet("error","未找到用户");
		}

		hashMap.put("fuserId",user.getUserId());
		Integer maxNum = this.getMaxHonorNum(hashMap) - 1;
		List<String> keys = (List<String>) JSONArray.toCollection(JSONArray.fromObject(hashMap.get("keys")), String.class);
		for(String key :keys){
			this.baseDAO.executeSql("update u_player_honor set num=" + maxNum + " where key_id = '" + key + "' ");
			maxNum--;
		}
		return WebPublicMehod.returnRet("success","球员荣誉排序成功");
	}



	/**
	 * TODO - 删除球员荣誉墙
	 * @param hashMap
	 * 	keyId 荣誉墙Id
	 * @return
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> deletePlayerHonor(HashMap<String, String> hashMap) throws Exception {
		//检查是否有球员荣誉
		if(StringUtils.isEmpty(hashMap.get("keyId"))){
			return WebPublicMehod.returnRet("error", "未找到球员荣誉");
		}
		this.baseDAO.executeHql("delete from UPlayerHonor where keyId='"+hashMap.get("keyId")+"'");

		return WebPublicMehod.returnRet("success","删除球员个人荣誉成功");
	}

	/**
	 * TODO - 获取球员荣誉墙最大的排序值
	 * @param hashMap
	 * @return
	 */
	private Integer getMaxHonorNum(HashMap<String,String> hashMap) throws Exception {
		String hql = "select max(num) from UPlayerHonor where user.userId=:fuserId";
		List<Object> list = this.baseDAO.find( hashMap,hql);
		if(list.get(0)==null){
			return 0;
		}

		return Integer.parseInt(list.get(0).toString())+1;
	}


	/**
	 * TODO - 获取球员总场次、获胜场次、战平场次、战败场次
	 * @return
	 */
	private HashMap<String,String> getPlayerCourtInfo(HashMap<String,String> param) throws Exception {

		HashMap<String,String> retMap = new HashMap<String,String>();
//		List<UTeam> list = this.uTeamService.getUTeamListByUserId(param);
//		StringBuffer teams = new StringBuffer();
//		for(UTeam team : list){
//			teams.append("'".concat(team.getTeamId()).concat("'").concat(","));
//		}
//		if(StringUtils.isEmpty(teams.toString()))
//			return retMap;
//		String t = teams.substring(0,teams.length()-1);

		StringBuffer sql = new StringBuffer(" select te.type as type, sum(te.count) as sum from ( ");
		//约战发起方胜利场次
		sql.append(" select '发起方胜利场次' as name, 'win' as type , count(bs_id) as count from u_duel_bs where fteam_id =:teamId and (f_goal+0) >(k_goal+0) ");
		sql.append(" union all ");
		//约战发起方战平场次
		sql.append(" select '发起方战平场次' as name, 'draw' as type , count(bs_id) as count from u_duel_bs where fteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//约战发起方战败场次
		sql.append(" select '发起方战败场次' as name, 'fail' as type , count(bs_id) as count from u_duel_bs where fteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//约战相应方胜利
		sql.append(" select '相应方胜利' as name, 'win' as type ,count(bs_id) as count from u_duel_bs where xteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//约战相应方战平
		sql.append(" select '相应方战平' as name, 'draw' as type ,count(bs_id) as count from u_duel_bs where xteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//约战相应方战败
		sql.append(" select '相应方战败' as name, 'fail' as type ,count(bs_id) as count from u_duel_bs where xteam_id =:teamId and (f_goal+0) >(k_goal+0) ");
		sql.append(" union all ");

		//挑战发起方胜利场次
		sql.append(" select '发起方胜利场次' as name, 'win' as type , count(bs_id) as count from u_challenge_bs where fteam_id =:teamId and (f_goal+0) >(k_goal+0) ");
		sql.append(" union all ");
		//挑战发起方战平场次
		sql.append(" select '发起方战平场次' as name, 'draw' as type , count(bs_id) as count from u_challenge_bs where fteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//挑战发起方战败场次
		sql.append(" select '发起方战败场次' as name, 'fail' as type , count(bs_id) as count from u_challenge_bs where fteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//挑战相应方胜利场次
		sql.append(" select '相应方胜利' as name, 'win' as type ,count(bs_id) as count from u_challenge_bs where xteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//挑战相应方战平场次
		sql.append(" select '相应方战平' as name, 'draw' as type ,count(bs_id) as count from u_challenge_bs where xteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//挑战响应方战败场次
		sql.append(" select '相应方战败' as name, 'fail' as type ,count(bs_id) as count from u_challenge_bs where xteam_id =:teamId and (f_goal+0) >(k_goal+0) ");
		sql.append(" union all ");

		//赛事发起方胜利场次
		sql.append(" select '发起方胜利场次' as name, 'win' as type , count(bs_id) as count from u_match_bs where fteam_id =:teamId and (f_goal+0) >(k_goal+0) ");
		sql.append(" union all ");
		//赛事发起方战平场次
		sql.append(" select '发起方战平场次' as name, 'draw' as type , count(bs_id) as count from u_match_bs where fteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//赛事发起方战败场次
		sql.append(" select '发起方战败场次' as name, 'fail' as type , count(bs_id) as count from u_match_bs where fteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//赛事相应方胜利场次
		sql.append(" select '相应方胜利' as name, 'win' as type ,count(bs_id) as count from u_match_bs where xteam_id =:teamId and (f_goal+0) < (k_goal+0) ");
		sql.append(" union all ");
		//赛事相应方战平场次
		sql.append(" select '相应方战平' as name, 'draw' as type ,count(bs_id) as count from u_match_bs where xteam_id =:teamId and (f_goal+0) = (k_goal+0) ");
		sql.append(" union all ");
		//赛事响应方战败场次
		sql.append(" select '相应方战败' as name, 'fail' as type ,count(bs_id) as count from u_match_bs where xteam_id =:teamId and (f_goal+0) >(k_goal+0) ");

		sql.append(") te GROUP BY type;");
		List<HashMap<String,Object>> courtMap = this.baseDAO.findSQLMap(param ,sql.toString());

		//计算总场次数
		Double sumCourt = 0D;
		for(HashMap<String,Object> m : courtMap){
			retMap.put(m.get("type").toString(),m.get("sum").toString());
			sumCourt+=new BigDecimal(retMap.get(m.get("type"))).doubleValue();
			System.out.println();
		}
		Double win = new BigDecimal(retMap.get("win")).doubleValue();
		int winRatio = (int)((win / sumCourt) * 100);
		retMap.put("sumCourt",String.valueOf(sumCourt.intValue()));
		retMap.put("winRatio",winRatio+"%");
		return retMap;
	}

	/**
	 * TODO - 获取用户球员的比赛数据 进球数、助攻数、球队数、抢断数
	 * @param param
	 * @return
	 */
	private HashMap<String,String> getPlayerInfoData(HashMap<String,String> param) throws Exception {
		HashMap<String,String> hashMap = new HashMap<String,String>();
		//获取球员进球总数
		StringBuffer sql = new StringBuffer(" select sum(t.sum) as go  from ( ");
		sql.append(" select count(key_id) as sum from u_duel_playerinfo where duel_bs_type = 1 and user_id=:userId and fteam_id=:teamId ");//约战
		sql.append(" UNION all ");
		sql.append(" select count(key_id) as sum from u_challenge_playerinfo where challenge_player_type = 1 and user_id=:userId and fteam_id=:teamId ");//挑战
		sql.append(" UNION all ");
		sql.append(" select count(key_id) as sum from u_match_playerinfo where match_player_type = 1 and user_id=:userId and fteam_id=:teamId ");//赛事
		sql.append(" ) t ");
		List<HashMap<String, BigDecimal>> go = this.baseDAO.findSQLMap(param, sql.toString());
		if (CollectionUtils.isNotEmpty(go)) {
			hashMap.put("goNum", go.get(0).get("go") == BigDecimal.ZERO ? "-" :String.valueOf(go.get(0).get("go").intValue()));
		}
		//获取球员赛事的助攻数
		HashMap<String, Object> oMap = new HashMap<String, Object>();
		oMap.put("userId", param.get("userId"));
		sql = new StringBuffer("select count(key_id) as zg from u_match_playerinfo where heuser_id=:userId ");
		List<HashMap<String, BigInteger>> zg = this.baseDAO.findSQLMap(sql.toString(), oMap);
		if (CollectionUtils.isNotEmpty(zg)) {
			hashMap.put("zg", zg.get(0).get("zg") == BigInteger.ZERO ? "-" : String.valueOf(zg.get(0).get("zg").intValue()));
		}
		return hashMap;
	}

	//关于我模块信息
	private List<Object> getAboutPlayer(HashMap<String,String> params,UPlayer uPlayer) throws Exception {
		params.put("teamId", uPlayer.getUTeam() == null ? "" : uPlayer.getUTeam().getTeamId());
		params.put("userId", uPlayer.getUUser() == null ? "" : uPlayer.getUUser().getUserId());
		List<Object> about = new  ArrayList<Object>();
		//活跃地区
		HashMap<String, String> p = new HashMap<String, String>();
		HashMap<String,Object> c =  new HashMap<String,Object>();
		String address = "";
		if(uPlayer.getUUser()!=null && uPlayer.getUUser().getuRegion()!=null){
			c.put("area", uPlayer.getUUser().getuRegion().get_id());
			address = uRegionService.getURegionInfoByArea(c);
		}
		p.put("idStr","活跃地区");
		p.put("nameStr",address);
		p.put("iconType","1");
		about.add(p);

		//随队出征
		p = new HashMap<String,String>();
		HashMap<String,String> m = this.getPlayerCourtInfo(params);
		p.put("idStr","随队出征");
		p.put("nameStr", m.get("sumCourt").concat("场 ").concat(m.get("win")).concat("胜 ").concat(m.get("draw")).concat(" 平").concat(m.get("fail")).concat(" 负 "));
		p.put("iconType","2");
		about.add(p);

		//个人战绩
		p = new HashMap<String,String>();
		m = this.getPlayerInfoData(params);
		p.put("idStr","个人战绩");
		p.put("nameStr"," - 射门 ".concat(m.get("goNum")).concat(" 进球 ").concat(m.get("zg")).concat(" 助攻 ").concat("- 抢断"));
		p.put("iconType","3");
		about.add(p);

		//关注指数
		p = new HashMap<String,String>();
		p.put("idStr","关注指数");
		if(uPlayer.getUUser()!=null){
			c.put("userId",uPlayer.getUUser().getUserId());
			p.put("nameStr", String.valueOf(this.getPlayerFollowCount(c)).concat("人"));
		}else{
			p.put("nameStr","-");
		}

		p.put("iconType","4");
		about.add(p);

		return about;
	}


	/**
	 * TODO - 获取球员关注总数
	 * @param param
	 * @return
	 */
	private int getPlayerFollowCount(HashMap<String,Object> param) throws Exception {
		String hql = "select count(keyId) from UFollow where userFollowType='3' and followStatus='1' and objectId=:userId ";
		int count = this.baseDAO.count(hql,param,Boolean.FALSE);
		return count;
	}

	/**
	 * TODO - 球员详细页面荣誉墙模块 显示3条荣誉墙信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> getPlayerInfoHonor(HashMap<String,String> param) throws Exception {
		String hql = "from UPlayerHonor where user.userId=:userId order by num desc";
		List<UPlayerHonor> list = this.baseDAO.findByPage(param,hql,3,0);
		return this.formatterPlayerHonorList(list,0);
	}

//
//	/**
//	 * 
//	 * 
//	   TODO - 更新有球队的球员信息
//	   @param map
//	   		number			背号
//	   		position		位置
//	   		memberType		角色
//	   		practiceFoot		惯用脚
//	   		expertPosition		擅长位置
//	   		canPosition			可踢位置
//	   @param uUser
//	   @return
//	   2016年7月14日
//	   dengqiuru
//	 * @throws Exception 
//	 */
//	@Override
//	public UPlayer setInTeamPlayerInfoByUserByEnter(Map<String, Object> map, UUser uUser,UTeam uTeam) throws Exception {
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", uUser.getUserId());
//		hashMap.put("teamId", map.get("teamId").toString());
//		UPlayer uPlayer = baseDAO.get(hashMap,"from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId and inTeam='1'");
//		if (null != uPlayer) {//存在:只做更新
//			if (publicService.StringUtil(map.get("number").toString())) {
//				//判断球队中是否存在该位置
//				hashMap.put("teamId", uTeam.getTeamId());
//				hashMap.put("number", map.get("number").toString());
//				hashMap.put("loginUserId", map.get("userId").toString());
//				boolean isNumber = isExitUteamNumber(hashMap,uTeam);
//				if (isNumber == false) {
//					uPlayer.setNumber(Integer.parseInt(map.get("number").toString()));//背号
//				}else{
//					uPlayer = null;
//					return uPlayer;
//				}
//			}
//			if (publicService.StringUtil(map.get("position").toString())) {
//				uPlayer.setPosition(map.get("position").toString());//擅长位置
//			}
//			uPlayer.setAddqd("第三方系统");
//			baseDAO.update(uPlayer);
//		}else{//存在:新增
//			uPlayer = new UPlayer();
//			uPlayer.setPlayerId(WebPublicMehod.getUUID());
//			uPlayer.setUUser(uUser);
//			uPlayer.setUTeam(uTeam);
//			uPlayer.setTeamBelonging("1");
//			uPlayer.setInTeam("1");
//			uPlayer.setAdddate(new Date());
//			uPlayer.setAddtime(new Date());
//			if (publicService.StringUtil(map.get("number").toString())) {
//				//判断球队中是否存在该位置
//				hashMap.put("teamId", uTeam.getTeamId());
//				hashMap.put("number", map.get("number").toString());
//				hashMap.put("loginUserId", map.get("userId").toString());
//				boolean isNumber = isExitUteamNumber(hashMap,uTeam);
//				if (isNumber == false) {
//					uPlayer.setNumber(Integer.parseInt(map.get("number").toString()));//背号
//				}else{
//					uPlayer = null;
//					return uPlayer;
//				}
//			}
//			if (publicService.StringUtil(map.get("position").toString())) {
//				uPlayer.setPosition(map.get("position").toString());//擅长位置
//			}
//			uPlayer.setAddqd("第三方系统");
//			baseDAO.save(uPlayer);
//		}
//		if (null != map.get("memberType")) {
//			hashMap.put("memberType", map.get("memberType").toString());
//			uPlayerRoleService.setEventsMemberTypeByEditplayer202(hashMap, uPlayer);
//		}
//		return uPlayer;
//	}
	
	@Override
	public Integer getTeamIdAndUserIdPlayer(HashMap<String, String> map)  throws Exception{
		Integer lvl1=0;
		String sqlMemberType="select pr.member_type memberType from u_player p "
				+ "left join u_player_role pr on pr.player_id=p.player_id "
				+ "left join u_player_role_limit prl on pr.member_type=prl.member_type "
				+ "where p.user_id=:loginUserId and p.team_id=:teamId and pr.member_type!='11' and pr.member_type_use_status='1' and p.in_team='1'"
				+ "order by prl.rank_role+0 asc";
		List<HashMap<String, Object>> listPlayerRoleLimit=baseDAO.findSQLMap(map, sqlMemberType);
		if(listPlayerRoleLimit!=null&&listPlayerRoleLimit.size()>0){
			listPlayerRoleLimit.removeAll(YHDCollectionUtils.nullCollection());
			if(listPlayerRoleLimit.get(0)!=null&&listPlayerRoleLimit.get(0).get("memberType")!=null&&!"".equals(listPlayerRoleLimit.get(0).get("memberType"))){
				lvl1=uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(listPlayerRoleLimit.get(0).get("memberType").toString()));
			}
		}
		return lvl1;
	}
	
	@Override
	public HashMap<String, Object> getUPlayerinfoManage(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		OutPlayerList outPlayerList =null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("playerId"))) {
				baseDAO.getSessionFactory().getCurrentSession().clear();
				UPlayer uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId ");
				if (null != uPlayer) {
					//填充球员基础数据
					outPlayerList = displayData(uPlayer, map);
					//填充球员的身份
					outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
					//角色是否可选
//					if(uPlayer.getUTeam()!=null&&uPlayer.getUTeam().getTeamId()!=null&&!"".equals(uPlayer.getUTeam().getTeamId())){
//						map.put("teamId", uPlayer.getUTeam().getTeamId());
//						this.setMemBerTypeLvl(map,outPlayerList,resultMap);
//					}
				}
				resultMap.put("outPlayerList", outPlayerList);
			}else{
				return WebPublicMehod.returnRet("error", "缺少请求参数！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请登录正常账户在执行此操作！");
		}
		return resultMap;
	}
	

	@Override
	public HashMap<String, Object> editPlayerInfoManage(HashMap<String, String> map)throws Exception {
//		//判断userId是否为空
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("playerId"))) {
				
				baseDAO.getSessionFactory().getCurrentSession().clear();
				UPlayer uPlayer = baseDAO.get(UPlayer.class, map.get("playerId"));
				map.put("teamId","");
				if(uPlayer!=null&&uPlayer.getUTeam()!=null&&uPlayer.getUTeam().getTeamId()!=null&&!"".equals(uPlayer.getUTeam().getTeamId())){
					map.put("teamId",uPlayer.getUTeam().getTeamId());
				}
				
				if(uPlayer!=null&&"1".equals(uPlayer.getInTeam())){
					if(map.get("number")!=null&&!"".equals(map.get("number"))){
						hashMap=new HashMap<>();
						hashMap.put("number", Integer.parseInt(map.get("number")));
						hashMap.put("teamId", map.get("teamId"));
						hashMap.put("playerId", map.get("playerId"));
						UPlayer player=baseDAO.get("from UPlayer where UTeam.teamId=:teamId and playerId!=:playerId and number=:number and inTeam='1' ",hashMap);
						if(player!=null){
							return WebPublicMehod.returnRet("error", "背号在该队中已经存在,不能重复");
						}
						uPlayer.setNumber(Integer.parseInt(map.get("number")));
						if("100".equals(map.get("number"))){
							uPlayer.setNumber(null);
						}
					}
					if(map.get("position")!=null&&!"".equals(map.get("position"))){
						uPlayer.setPosition(map.get("position"));
					}	
					baseDAO.update(uPlayer);
					publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object",map.get("playerId")));
					if(uPlayer.getUUser()!=null){
						uUserService.updateUserInfo(map, uPlayer.getUUser());
					}
					hashMap.put("success", "修改成功！");
					return WebPublicMehod.returnRet("success", "修改成功！");
				}
				return WebPublicMehod.returnRet("error", "你所选的球员已经离队，无法继续操作");
			}
			return WebPublicMehod.returnRet("error", "缺少请求参数！");
		}
		return WebPublicMehod.returnRet("error", "请使用正常用户执行此操作");
	}
	/**
	 * 
	 * TODO 将球员信息添加到输出球员信息 【2.0.3】
	 * @param uPlayer
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月26日
	 */
	private OutPlayerList displayData(UPlayer uPlayer,HashMap<String, String> map) throws Exception {
		OutPlayerList outPlayerList = new OutPlayerList();
		String objectName = null;
		//获取球员最初始的球员信息
//		HashMap<String, String> loginUserIdMap=new HashMap<>();
//		loginUserIdMap.put("loginUserId", uPlayer.getUUser().getUserId());
//		UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(loginUserIdMap);
		
		baseDAO.getSessionFactory().getCurrentSession().flush();
		outPlayerList.setPlayerId(uPlayer.getPlayerId());// 球员Id
		if (uPlayer.getNumber() != null) {
			outPlayerList.setNumber(uPlayer.getNumber().toString());// 背号
		}
		if (publicService.StringUtil(uPlayer.getPosition())) {
			objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
			outPlayerList.setPosition(uPlayer.getPosition());// 位置
			outPlayerList.setPositionName(objectName);
		}
		
		if (null != uPlayer.getUUser()) {
			map.put("userId", uPlayer.getUUser().getUserId());
			UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
			if (null != uUserImg) {
				outPlayerList.setImgurl(uUserImg.getImgurl());// 头像
			}
			if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
				outPlayerList.setRealname(uPlayer.getUUser().getRealname());// 球员真实姓名
			}
			if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
				outPlayerList.setNickname(uPlayer.getUUser().getNickname());// 昵称
			}
			if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
				outPlayerList.setSex(uPlayer.getUUser().getSex());// 性别
				String sex = uPlayer.getUUser().getSex();
				objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
				outPlayerList.setSexName(objectName);
			}
			if (publicService.StringUtil(uPlayer.getUUser().getWeight())) {
				outPlayerList.setWeight(uPlayer.getUUser().getWeight());// 体重
			}
			if (uPlayer.getUUser().getAge() != null ) {
				outPlayerList.setAge(uPlayer.getUUser().getAge().toString());//年龄
			}else{
				if (uPlayer.getUUser().getBirthday() != null ) {
					outPlayerList.setAge(getAgeByBirthday(uPlayer.getUUser().getBirthday()).toString());//年龄
				}
			}
			if (uPlayer.getUUser().getBirthday() != null) {
				String birthday = PublicMethod.getDateToString(uPlayer.getUUser().getBirthday(), "yyyy-MM-dd");
				outPlayerList.setBirthday(birthday);// 出生日期
			}
			if (publicService.StringUtil(uPlayer.getUUser().getHeight())) {
				outPlayerList.setHeight(uPlayer.getUUser().getHeight());// 身高
			}
			if (publicService.StringUtil(uPlayer.getUUser().getRemark())) {
				outPlayerList.setRemark(uPlayer.getUUser().getRemark());// 备注
			}
		}
		return outPlayerList;
	}
	/**
	 * 
	 * TODO 角色等级 是否可选，可选进行分配【2.0.3】
	 * @param map loginUserId－登录用户，teamId 登录用户所在球队ID
	 * @param outPlayerList 用户球员信息
	 * @param resultMap  将可选不可选角色存放到Map中
	 * @throws Exception
	 * xiaoying 2016年7月27日
	 */
	private void setMemBerTypeLvl(HashMap<String, String> map,OutPlayerList outPlayerList, HashMap<String, Object> resultMap) throws Exception{
		Integer lvl1=this.getTeamIdAndUserIdPlayer(map);//登录人权限等级
		Integer lvl2=0;//第一个角色等级
		Integer lvl3=0;//第二个角色等级
		if(outPlayerList.getMemberType()!=null&&!"".equals(outPlayerList.getMemberType())){
			lvl2=uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(outPlayerList.getMemberType()));
		}
		if (outPlayerList.getMemberType2() != null && !"".equals(outPlayerList.getMemberType2())) {
			lvl3=uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(outPlayerList.getMemberType2()));
		}
//		resultMap.put("myLvl", lvl1);
//		resultMap.put("lvl", lvl2);
//		resultMap.put("lvl2", lvl3);
		//默认两个角色是否可选，2-不可选，1-可选
		resultMap.put("isChoice", "2");
		resultMap.put("isChoice2", "2");
		if(lvl1!=0){//自身角色不能为空
			if(lvl2!=0){
				if(lvl1<lvl2){//如果自身等级权限大于第一个角色，即：1<2 ,那么可以选择角色，进行分配
					resultMap.put("isChoice", "1");
					//如果第一角色等于2，同时是唯一的角色，那么就不能选择角色
					if(lvl1==2&&null!=outPlayerList.getMemberTypeIsUnique()&&"1".equals(outPlayerList.getMemberTypeIsUnique())){
						resultMap.put("isChoice", "2");
					}
				}else{
					resultMap.put("isChoice", "2");
				}
			}
			if(lvl3!=0){
				if(lvl1<lvl3){
					resultMap.put("isChoice2", "1");
					if(lvl3==2&&null!=outPlayerList.getMemberTypeIsUnique2()&&"1".equals(outPlayerList.getMemberTypeIsUnique2())){
						resultMap.put("isChoice2", "2");
					}
				}else{
					resultMap.put("isChoice2", "2");
				}
			}
		}
	}
	
	@Override
	public void compareLvl(HashMap<String, Object> hashMap, Integer lvl1, Integer lvl2,HashMap<String, String> map) throws Exception{
		if("1".equals(hashMap.get("isExculde"))){
			Boolean isUnique = uPlayerRoleService.isUniqueByuserId(map);
			if(!isUnique){
				if(lvl1 != 0 && lvl2 != 0){
					if(lvl1==3){
						hashMap.put("isExculde", "2");//踢人1，可以，2不可踢
					}else{
						if(lvl1>=lvl2){//＝同级 >低级
							hashMap.put("isExculde", "2");
						}
					}
				}else{
					hashMap.put("isExculde", "2");
				}
			}else{
				hashMap.put("isExculde", "2");
			}
		}
		if("1".equals(hashMap.get("isAllot"))){
			if(lvl1 != 0 && lvl2 != 0){
				if(lvl1==3){
					hashMap.put("isAllot", "2");//分配1，可以，2不可踢
				}else{
					if(lvl1>lvl2){// >低级
						hashMap.put("isAllot", "2");
					}
				}
			}else{
				hashMap.put("isAllot", "2");
			}
		}
//		if("1".equals(hashMap.get("isEdit"))){
//			if(lvl1 != 0 && lvl2 != 0){
//				if(lvl1==3){
//					hashMap.put("isEdit", "2");//修改1，可以，2不可踢
//				}else{
//					if(lvl1>lvl2){// >低级
//						hashMap.put("isEdit", "2");
//					}
//				}
//			}else{
//				hashMap.put("isEdit", "2");
//			}
//		}
	}
	
	@Override
	public Integer getPlayerIdPlayer(HashMap<String, String> map, List<HashMap<String, Object>> listPlayerRoleLimit, Integer lvl2)  throws Exception{
		listPlayerRoleLimit=baseDAO.findSQLMap(map, "select pr.member_type memberType from u_player p "
				+ "left join u_player_role pr on pr.player_id=p.player_id "
				+ "left join u_player_role_limit prl on pr.member_type=prl.member_type "
				+ "where p.player_id=:playerId and pr.member_type!='11' and pr.member_type_use_status='1' and p.in_team='1' "
				+ "order by prl.rank_role+0 asc");
		if(listPlayerRoleLimit!=null&&listPlayerRoleLimit.size()>0){
			listPlayerRoleLimit.removeAll(YHDCollectionUtils.nullCollection());
			if(listPlayerRoleLimit.get(0)!=null&&listPlayerRoleLimit.get(0).get("memberType")!=null&&!"".equals(listPlayerRoleLimit.get(0).get("memberType"))){
				lvl2=uParameterService.getPlayerRoleLimitLvl(Integer.parseInt(listPlayerRoleLimit.get(0).get("memberType").toString()));
			}
		}
		return lvl2;
	}
	/**
	 * 
	 * TODO 查看分配对内角色按钮 [2.0.3]
	 * @param hashMap2
	 * @param map
	 * @throws Exception
	 * xiaoying 2016年7月26日
	 */
	private void setIsAllot(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		HashMap<String, String> roleMap = new HashMap<>();
		String isAllot = "2";//被查看分配对内角色  2：不能
		if ("1".equals(hashMap2.get("isMyTeam"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募;10:分配
				roleMap.put("type", "10");
				roleMap.put("teamId", map.get("teamId"));
				roleMap.put("loginUserId", map.get("loginUserId"));
				//获取自身是否有分配权限
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(roleMap);
				if (null != playerIds && playerIds.size() > 0) {
					isAllot = "1";
				}
			}
		}
		hashMap2.put("isAllot", isAllot);
	}
	/**
	 * 
	 * 
	   TODO - 踢人按钮
	   @param hashMap2
	   @param map
	   		teamId		球队Id
	   @throws Exception
	   2016年7月3日
	   dengqiuru
	 */
	private void setIsExculdes(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		HashMap<String, String> roleMap = new HashMap<>();
		String isExculde = "2";//被查看人是否能被踢出  2：不能
		if ("1".equals(hashMap2.get("isMyTeam"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				//查询我是不是有权限踢人
				//查找踢人的用户是否有权限
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
				roleMap.put("type", "1");
				roleMap.put("teamId", map.get("teamId"));
				roleMap.put("loginUserId", map.get("loginUserId"));
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(roleMap);
				if (null != playerIds && playerIds.size() > 0) {
					isExculde = "1";
				}
			}
		}
		hashMap2.put("isExculde", isExculde);
	}
	/**
	 * 
	 * TODO  查看修改球员信息按钮 [2.0.3]
	 * @param hashMap2
	 * @param map
	 * @throws Exception
	 * xiaoying 2016年8月4日
	 */
	private void setIsEdit(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		HashMap<String, String> roleMap = new HashMap<>();
		String isEdit = "2";//被查看分配对内角色  2：不能
		if ("1".equals(hashMap2.get("isMyTeam"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募;10:分配
				roleMap.put("type", "12");
				roleMap.put("teamId", map.get("teamId"));
				roleMap.put("loginUserId", map.get("loginUserId"));
				//获取自身是否有分配权限
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(roleMap);
				if (null != playerIds && playerIds.size() > 0) {
					isEdit = "1";
				}
			}
		}
		hashMap2.put("isEdit", isEdit);
	}
	@Override
	public HashMap<String, Object> updateUplayerFollowObject(HashMap<String, String> map) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		List<UFollow> uFollows = baseDAO.find("from UFollow where userFollowType='3'");
		if (null != uFollows && uFollows.size() > 0) {
			for (UFollow uFollow : uFollows) {
				if (null != uFollow.getObjectId() && !"".equals(uFollow.getObjectId())) {
					map.put("playerId", uFollow.getObjectId());
					UPlayer uPlayer = baseDAO.get(map, "from UPlayer where playerId=:playerId");
					if (null != uPlayer) {
						if (null != uPlayer.getUUser()) {
							uFollow.setObjectId(uPlayer.getUUser().getUserId());
							baseDAO.update(uFollow);
						}
					}
				}
			}
		}
		resultMap.put("success", "修改成功");
		return resultMap;
	}
	/**
	 * 
	 * TODO 球员关注数--基友关注
	 * @param map 登录ID   
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月15日
	 */
	public Integer pubGetUserFollowCount(HashMap<String, String> map) throws Exception {
		List<HashMap<String, Object>> followList = new ArrayList<HashMap<String, Object>>();
		followList = baseDAO.findSQLMap(map, "select * from u_follow where user_follow_type = '3' and object_id =:loginUserId and follow_status = '1' ");
		if(followList.size()>0){
			return followList.size();
		}
		return 0;
	}
	
	/**
	 * 
	 * 
	   TODO - 球员里程碑【2.0.3】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年8月11日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> uplayerBehaviorType(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		//时间轴
		List<HashMap<String, Object>> UUserBehaviorList = uUserBehaviorService.getuUserBehaviorList(map);
		if (null != UUserBehaviorList && UUserBehaviorList.size() > 0) {
			for (HashMap<String, Object> hashMap2 : UUserBehaviorList) {
				hashMap2.put("jsonStr", uUserBehaviorService.getUTeamBehaviorJsonStr(hashMap2));
			}
		}
		resultMap.put("outuUserBehaviors", UUserBehaviorList);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情-新的球队列表
	   @param map
	   @return
	   @throws Exception
	   2016年8月23日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUTeamListInRoungh(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		//里程碑
		resultMap.putAll(this.getPlayerinfoInRoungh(map));
		//该用户的球队列表
		List<HashMap<String, Object>> listTeam =  new ArrayList<HashMap<String, Object>>();
		//角标
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		listTeam = uTeamService.getUteamListInplayerRoughly(map);
//		if(null != map.get("page")&& !"".equals(map.get("page"))){
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
				}
			}
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
//		}
		resultMap.put("cornerList", cornerList);
		resultMap.put("outUteamLists", listTeam);
		return resultMap;
	}

	@Override
	public List<UPlayer> getTeamManagePlayer(HashMap<String, String> param) throws Exception {
		String lvl = Public_Cache.MEMBER_TYPE_LVL1;
		//没有球队id或者没有缓存最高权限的时候返回null
		if(StringUtils.isEmpty(lvl) || StringUtils.isEmpty(param.get("teamId")))
			return null;
		String[] lvls = lvl.split(",");
		StringBuffer lvlStr = new StringBuffer();
		for(int i =0 ;i<lvls.length;i++){
			lvlStr.append(lvls[i]+",");
		}
		List<UPlayerRole> roles = this.baseDAO.find("from UPlayerRole where UPlayer is not null and UPlayer.UTeam is not null and  UPlayer.UTeam.teamId = '" + param.get("teamId") + "' and UPlayer.inTeam = '1' and memberType in (" + lvlStr.substring(0, lvlStr.length() - 1) + ") and memberTypeUseStatus = '1' ");
		List<UPlayer> players = new ArrayList<UPlayer>();
		HashMap<String,String> disPlayer = new HashMap<String,String>();
		for(UPlayerRole role : roles){
			//球员不存在
			if(role.getUPlayer() == null)
				continue;
			//球员已经添加到返回的list中
			if(StringUtils.isNotEmpty(disPlayer.get(role.getUPlayer().getPlayerId())))
				continue;
			players.add(role.getUPlayer());
			disPlayer.put(role.getUPlayer().getPlayerId(),role.getUPlayer().getPlayerId());

		}
		return players;
	}

	/**
	 * 
	 * 
	   TODO - 将球员信息添加到输出球员信息 【2.0.0】
	   @param uPlayer
	   		uPlayer		 uPlayer
	   @param map
	   		loginUserId		  当前用户Id
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	private OutPlayerList setOutPlayerListInRoungh(UUser uUser,UPlayer uPlayer,HashMap<String, String> map) throws Exception {
		OutPlayerList outPlayerList = new OutPlayerList();
		String objectName = null;
		if (null != uPlayer) {
			outPlayerList.setPlayerId(uPlayer.getPlayerId());//球员Id
			baseDAO.getSessionFactory().getCurrentSession().flush();
			UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
			if (null != uUserImg) {
				outPlayerList.setImgurl(uUserImg.getImgurl());//头像
			}
			HashMap<String, Object> worthMap = uworthService.findUserIdWorthCount(map);//该球员的身价
			if (null != worthMap) {
				if (null != worthMap.get("count")) {
					outPlayerList.setWorthCount(worthMap.get("count")+"");
				}
			}
			//关注数
			HashMap<String, Object> followMap = new HashMap<>();
			followMap.put("userId", uPlayer.getUUser().getUserId());
			Integer followCount = this.pubGetUserFollowCountByUserId(followMap);
			if (null != worthMap) {
				outPlayerList.setFollowCount(followCount);
			}
			if (null != uPlayer.getUUser()) {
				if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
					outPlayerList.setRealname(uPlayer.getUUser().getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
					outPlayerList.setNickname(uPlayer.getUUser().getNickname());//昵称
				}
				if (publicService.StringUtil(uPlayer.getUUser().getSex())) {
					outPlayerList.setSex(uPlayer.getUUser().getSex());//性别
					String sex = uPlayer.getUUser().getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uPlayer.getUUser().getAge() != null) {
					outPlayerList.setAge(uPlayer.getUUser().getAge().toString());//年龄
				}else{
					if (uPlayer.getUUser().getBirthday() != null) {
						outPlayerList.setAge(getAgeByBirthday(uPlayer.getUUser().getBirthday()).toString());//年龄
					}
				}
				if (publicService.StringUtil(uPlayer.getUUser().getWeight())) {
					outPlayerList.setWeight(uPlayer.getUUser().getWeight());//体重
				}
				if (uPlayer.getUUser().getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uPlayer.getUUser().getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uPlayer.getUUser().getHeight())) {
					outPlayerList.setHeight(uPlayer.getUUser().getHeight());//身高
				}
				if (publicService.StringUtil(uPlayer.getUUser().getRemark())) {
					outPlayerList.setRemark(uPlayer.getUUser().getRemark());//备注
				}
			}
			if (publicService.StringUtil(uPlayer.getExpertPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
						outPlayerList.setExpertPositionName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setExpertPosition(uPlayerTemp.getExpertPosition());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("expert_position").get(uPlayerTemp.getExpertPosition());
					if (!"16".equals(uPlayerTemp.getExpertPosition()) && !"15".equals(uPlayerTemp.getExpertPosition())) {
						outPlayerList.setExpertPositionName(objectName);
					}
				}
			}
			if (publicService.StringUtil(uPlayer.getCanPosition())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					if (!"15".equals(uPlayerTemp.getCanPosition())) {
						outPlayerList.setCanPositionName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setCanPosition(uPlayerTemp.getCanPosition());///可踢位置
					objectName = WebPublicMehod.getCacheParams("can_position",uPlayerTemp.getCanPosition());
					if (!"15".equals(uPlayerTemp.getCanPosition())) {
						outPlayerList.setCanPositionName(objectName);
					}
				}
			}
			if (publicService.StringUtil(uPlayer.getPracticeFoot())) {
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
						outPlayerList.setPracticeFootName(objectName);
					}
				}
			}else{
				objectName = null;
				UPlayer uPlayerTemp = this.getUplayerByuserIdNoInteam(map);
				if (null != uPlayerTemp) {
					outPlayerList.setPracticeFoot(uPlayerTemp.getPracticeFoot());//擅长位置
					objectName = Public_Cache.HASH_PARAMS("practice_foot").get(uPlayerTemp.getPracticeFoot());
					if (!"4".equals(uPlayerTemp.getPracticeFoot())) {
						outPlayerList.setPracticeFootName(objectName);
					}
				}
			}
			if (null != uPlayer.getNumber()) {
				outPlayerList.setNumber(uPlayer.getNumber().toString());//背号
			}
			if (publicService.StringUtil(uPlayer.getPosition())) {
				if (!"16".equals(uPlayer.getPosition())) {
					objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				}else{
					objectName = null;
				}
				outPlayerList.setPosition(uPlayer.getPosition());//位置
				outPlayerList.setPositionName(objectName);
			}
		}else{
			if (null != uUser) {
				baseDAO.getSessionFactory().getCurrentSession().flush();
				UUserImg uUserImg = uUserImgService.getHeadPicNotSetByuserId(map);
				if (null != uUserImg) {
					outPlayerList.setImgurl(uUserImg.getImgurl());//头像
				}
				if (publicService.StringUtil(uUser.getRealname())) {
					outPlayerList.setRealname(uUser.getRealname());//球员真实姓名
				}
				if (publicService.StringUtil(uUser.getNickname())) {
					outPlayerList.setNickname(uUser.getNickname());//昵称
				}
				if (publicService.StringUtil(uUser.getSex())) {
					outPlayerList.setSex(uUser.getSex());//性别
					String sex = uUser.getSex();
					objectName = Public_Cache.HASH_PARAMS("sex").get(sex);
					outPlayerList.setSexName(objectName);
				}
				if (uUser.getAge() != null) {
					outPlayerList.setAge(uUser.getAge().toString());//年龄
				}
				if (publicService.StringUtil(uUser.getWeight())) {
					outPlayerList.setWeight(uUser.getWeight());//体重
				}
				if (uUser.getBirthday() != null) {
					String birthday = PublicMethod.getDateToString(uUser.getBirthday(), "yyyy-MM-dd");
					outPlayerList.setBirthday(birthday);//出生日期
				}
				if (publicService.StringUtil(uUser.getHeight())) {
					outPlayerList.setHeight(uUser.getHeight());//身高
				}
				if (publicService.StringUtil(uUser.getRemark())) {
					outPlayerList.setRemark(uUser.getRemark());//备注
				}
			}
		}
		return outPlayerList;
	}

	/**
	 * 
	 * 
	   TODO - 个人中心头部 【2.0.0】
	   @param map
	   @return
	   		OutPlayerList对象
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	private HashMap<String, Object> getPlayerinfoInRoungh(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		OutPlayerList outPlayerList = null;
		UPlayer uPlayer = null;
		String playerId = null;
		if (publicService.StringUtil(map.get("userId"))) {
			UUser uUser = baseDAO.get(map, "from UUser where userId=:userId");
			//为队长的信息
			List<HashMap<String, Object>> playerIdList = this.getMemberTypeUplayerByUserId(map);
			if (null != playerIdList && playerIdList.size() > 0) {
				playerId = playerIdList.get(0).get("playerId").toString();
			}else{
				playerIdList = this.getNoMemberTypeUplayerByUserId(map);
				if (null != playerIdList && playerIdList.size() > 0) {
					playerId = playerIdList.get(0).get("playerId").toString();
				}
			}
			//球员Id不为null
			if (publicService.StringUtil(playerId)) {
				map.put("playerId", playerId);
				uPlayer =  baseDAO.get(map, "from UPlayer where playerId = :playerId");
			}else{
				//初始球员信息
				uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null");
			}
		
			//将球员信息set到球员输出表
			if (null == uPlayer) {
				outPlayerList = new OutPlayerList();
				outPlayerList = setOutPlayerListInRoungh(uUser,uPlayer, map);
			}else{
				outPlayerList = setOutPlayerListInRoungh(uUser,uPlayer, map);
			}
			//填充球员的身份
			outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
		}
		resultMap.put("outPlayerList", outPlayerList);
		return resultMap;
	}

	/**
	 * TODO - 获取球员关注总数
	 * @param param
	 * @return
	 */
	private int pubGetUserFollowCountByUserId(HashMap<String,Object> param) throws Exception {
		String hql = "select count(keyId) from UFollow where userFollowType='3' and followStatus='1' and objectId=:userId ";
		int count = this.baseDAO.count(hql,param,Boolean.FALSE);
		return count;
	}

}
