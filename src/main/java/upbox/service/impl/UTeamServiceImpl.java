package upbox.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.org.pub.PublicMethod;

import net.sf.json.JSONArray;
import upbox.dao.impl.OperDAOImpl;
//import upbox.model.BdLbsBean;
import upbox.model.CornerBean;
import upbox.model.PageLimit;
import upbox.model.UBaidulbs;
import upbox.model.UBehaviorInfo;
import upbox.model.UBrCourt;
import upbox.model.UChallenge;
import upbox.model.UChallengeBs;
import upbox.model.UChampion;
import upbox.model.UDuel;
import upbox.model.UDuelBs;
import upbox.model.UDuelChallengeImg;
import upbox.model.UDuelResp;
import upbox.model.UEquipment;
import upbox.model.UFollow;
import upbox.model.UParameterInfo;
import upbox.model.UPlayer;
import upbox.model.UPlayerRole;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UTeamBehavior;
import upbox.model.UTeamHonor;
import upbox.model.UTeamImg;
import upbox.model.UUser;
import upbox.model.UUserImg;
import upbox.outModel.OutUbCourtMap;
import upbox.outModel.OutUteamList;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.CornerService;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.RankingListService;
import upbox.service.UChallengeService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UFollowService;
import upbox.service.UInviteteamService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.URegionService;
import upbox.service.UTeamBehaviorService;
import upbox.service.UTeamImgService;
import upbox.service.UTeamService;
import upbox.service.UUserImgService;
import upbox.service.UUserService;
import upbox.service.UWorthService;

/**
 * 前端端用户接口实现类
 *
 */
@Service("uteamService")
public class UTeamServiceImpl implements UTeamService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UPlayerService uplayerService;
	
	@Resource
	private UUserService uuserService;
	
	@Resource
	private UTeamImgService uteamImgService;
	
	@Resource
	private RankingListService rankingListService;
	
	@Resource
	private UTeamBehaviorService uTeamBehaviorService;
	
	@Resource
	private UCourtService uCourtService;

	@Resource
	private UChallengeService uchallengeService;
	
	@Resource
	private URegionService 	uRegionService;
	
	@Resource
	private UFollowService 	uFollowService;
	
	@Resource
	private MessageService 	messageService;
	
	@Resource
	private UInviteteamService 	uInviteteamService;
	
	@Resource
	private UParameterService uParameterService;
	
	@Resource
	private UDuelService uDuelService;
	
	@Resource
	private CornerService cornerService;
	
	@Resource
	private PublicPushService publicPushService;
	
	@Resource
	private UPlayerRoleService uPlayerRoleService;
	
	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	
	@Resource
	private LBSService lBSService;
	
	@Resource
	private LBSService lbsService;

	@Resource
	private UUserImgService uUserImgService;
	
	@Resource
	private UWorthService uworthService;
	
	
	//战队详情头部sql
	private StringBuffer findUteaminfoHead_sql = new StringBuffer(
			"select t.team_id teamId,t.name name,t.team_class teamClass,t.team_count teamCount,t.hold_date holdDate,u.user_id userId,"
			+ " t.home_team_shirts homeTeamShirts,t.away_team_shirts awayTeamShirts,c.name uBrCourtName,t.team_status teamStatus,t.team_use_status teamUseStatus "
			+" ,r1.name county,r2.`name` city,r3.`name` province,t.short_name shortName,t.maximum maximum,"
			+" t.remark remark,i.imgurl imgurl,i.team_img_id teamImgId,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.rank rank,t.integral integral,p.exit_type exitType,p.player_id playerId,p.number number,p.position position,f.follow_status followStatus"
			+" from u_team t "
			+" left join u_region r1 on t.area=r1._id and r1.type='3'"
			+" left join u_region r2 on r1.parent=r2._id and  r2.type='2'"
			+" left join u_region r3 on r2.parent=r3._id and  r3.type='1'"
			+" left join u_br_court c on c.subcourt_id=t.subcourt_id "
			+" LEFT JOIN u_player p on p.team_id=t.team_id and p.user_id=:userId and p.in_team='1' "
			+" left join u_user u on u.user_id=p.user_id  and u.user_id=:userId"
			+" LEFT JOIN u_follow f on f.object_id=t.team_id and f.user_follow_type='2' and f.user_id=:userId"
			+" left join u_team_img i on i.team_id=t.team_id and i.timg_using_type='1' and i.img_size_type='1' "
			+" left join u_team_dek d on t.team_id=d.team_id "
			+" left join u_team_duel l on t.team_id= l.team_id" 
			+" where t.team_id=:teamId " // and (t.team_status='1' or t.team_status='2' or t.team_status='3') and (t.team_use_status='1' or t.team_use_status='2')
			+" group by t.team_id");
	
	//我关注的球队sql
	private StringBuffer myFollowUTeam_sql = new StringBuffer(
			"select  t.team_id teamId,t.name name,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+" t.integral integral,t.rank rank,p.player_id playerId ,i.imgurl imgurl,p.position position,p.number number"
			+" from u_follow f "
			+" left join u_team t on t.team_id=f.object_id "
			+" LEFT join u_team_img i on t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" left join u_team_dek d on f.object_id=d.team_id "
			+" left join u_team_duel l on f.object_id= l.team_id "
			+" LEFT JOIN u_player p on  p.team_id=f.object_id and p.user_id=:userId and p.in_team='1'"
			+" where f.user_id=:userId  "
			+" and f.user_follow_type='2' and f.follow_status='1' and (t.team_status='1' or t.team_status='2' or t.team_status='3') and t.team_use_status='2' "
			+" GROUP BY f.key_id order by f.createdate desc ");
	
	//球员概况--战队列表 按加入顺序从新到老  不改了  
	private StringBuffer getUteamListInroughly_sql = new StringBuffer(
			"select t.team_id teamId,t.name name,t.team_class teamClass,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight, "
			+" t.integral integral,t.rank rank,t.remark remark,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+" t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,p.number number,p.position position  from u_team t "
			+" left join u_player p on p.team_id=t.team_id and in_team='1' "
			+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+" left JOIN u_parameter_info up on r.member_type=up.params and up.pkey_id in (select upi.pkey_id from u_parameter upi where upi.params='member_type')"
			+" left join u_team_dek d on t.team_id=d.team_id "
			+" left join u_team_duel l on t.team_id= l.team_id "
			+" left join u_team_img i on t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" where p.user_id=:userId and (t.team_status='1' or t.team_status='2' or t.team_status='3') and t.team_use_status='2' "
			+" group by t.team_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc ,up.is_unique");
	
	//推荐的对手
	private StringBuffer getrecommendTeam_sql = new StringBuffer(
			" select t.team_id teamId,t.name name,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+" t.integral integral,t.rank rank,t.remark remark, "
			+" t.team_count teamCount,t.recommend_team recommendTeam,i.imgurl imgurl "
			+" from u_team t "
			+" left join u_team_dek d on t.team_id=d.team_id " 
			+" left join u_team_duel l on t.team_id= l.team_id " 
			+" left join u_team_img i on t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1' " 
			+" where t.team_status='3' and team_use_status='2' "
			+" and recommend_team='1' and t.team_count>=7 "
			+" and t.team_id not in (select team_id from u_player p where p.user_id=:userId and p.team_id is not null and p.in_team='1' ) "
			+" group by t.team_id order by case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc ");
	
	//推荐的对手2.0.2
	private StringBuffer getrecommendTeam202_sql = new StringBuffer(
			" select t.team_id teamId,t.name name,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+" t.integral integral,t.rank rank,t.remark remark, "
			+" t.team_count teamCount,t.recommend_team recommendTeam,i.imgurl imgurl "
			+" from u_team t "
			+" left join u_team_dek d on t.team_id=d.team_id " 
			+" left join u_team_duel l on t.team_id= l.team_id " 
			+" left join u_team_img i on t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1' " 
			+" where t.team_status='3' and team_use_status='2' "
			+" and recommend_team='1' and t.team_count>=7 "
			+" and t.team_id not in (:teamId) "
			+" group by t.team_id order by case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc ");
	
	//球队分享
	private StringBuffer getUteaminfoInShare_sql = new StringBuffer(
			"select t.team_id teamId,t.name teamName,i.imgurl imgurl,t.short_name shortName,t.team_class teamClass,t.remark remark,t.team_status teamStatus,t.team_use_status teamUseStatus,"
			+" u.username username,u.nickname nickname,u.realname realname,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.integral integral,t.rank rank from u_team t "
			+" LEFT JOIN u_player p on p.team_id=t.team_id and p.in_team='1'"
			+" left join u_player_role r on r.player_id=p.player_id "
			+" LEFT JOIN u_user u on p.user_id=u.user_id "
			+" LEFT JOIN u_team_img i on i.team_id=t.team_id and i.timg_using_type='1' and i.img_size_type='1' "
			+" where t.team_id=:teamId and r.member_type='1' "
			+" GROUP BY t.team_id");
	
	//球队列表 约过的对手
	private StringBuffer getUteamList_sql = new StringBuffer(
			"select t.team_id teamId,t.name name,t.team_class teamClass,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+ " t.integral integral,t.rank rank,t.remark remark,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+ " t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,p.number number,p.position position "
			+ " from u_team t "
			+ " left join u_team_dek d on t.team_id=d.team_id "
			+ " left join u_team_duel l on t.team_id= l.team_id "
			+ " left join u_team_img i on  t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+ " left join u_player p on p.team_id=t.team_id and p.user_id=:userId"
			+ " where t.team_status='3' and t.team_use_status='2' ");
	
	//对手
	private StringBuffer getCompetitor_sql = new StringBuffer(
			"select t.team_id teamId,t.name name,i.imgurl imgurl,t.rank rank,t.integral integral,"
			+" t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
			+" t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,f.follow_status isFollow"
			+" from u_team t"
			+" left join u_team_img i on t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1' "
			+" left join u_team_dek d on t.team_id=d.team_id "
			+" left join u_team_duel l on t.team_id= l.team_id "
			+" LEFT JOIN u_follow f on f.object_id=t.team_id and  f.follow_status='1' and f.user_follow_type='2' "
			+" where t.team_id=:XteamId "
			+" group by t.team_id ");

	//我的球队信息
	private StringBuffer myTeamInfo_sql = new StringBuffer(
			" select p.player_id playerId,p.team_id teamId,t.name name,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight"
			+" ,t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus "
			+" ,t.integral integral,t.rank rank,i.imgurl imgurl,p.default_uteam defaultUteam from u_player p "
			+" left join u_team t on p.team_id=t.team_id "
			+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+" left JOIN u_parameter_info up on r.member_type=up.params and up.pkey_id in (select upi.pkey_id from u_parameter upi where upi.params='member_type')"
			+" LEFT join u_team_img i on p.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" left join u_team_dek d on t.team_id=d.team_id "
			+" left join u_team_duel l on t.team_id= l.team_id " 
			+" where p.user_id=:userId and (t.team_status='1' or t.team_status='2' or t.team_status='3') and (t.team_use_status='1' or t.team_use_status='2') and p.in_team='1' "
			+" group by p.player_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,up.is_unique,r.member_type ");
	
	//约战指定更多对手 2.0.0
	private StringBuffer addMoreCompetitor_sql = new StringBuffer(
			" select t.team_id teamId,t.name name,t.team_class teamClass,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.integral integral,t.rank rank,t.remark remark,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
 			+" t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,p.number number,p.position position "
 			+" from u_team t "
 			+" left join u_team_dek d on t.team_id=d.team_id "
 			+" left join u_team_duel l on t.team_id= l.team_id "
			+" left join u_team_img i on  t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" left join u_player p on p.team_id=t.team_id and p.user_id=:userId"
			+" where t.team_status='3' and t.team_use_status='2'  and t.team_count>=7  "
			+" and t.team_id not in (select pp.team_id from u_player pp,u_player_role r where pp.player_id=r.player_id and pp.user_id=:userId and pp.team_id is not null and pp.in_team='1' and r.member_type='1' and r.member_type_use_status='1') ");

	//约战指定更多对手 2.0.2
	private StringBuffer addMoreCompetitor202_sql = new StringBuffer(
			" select t.team_id teamId,t.name name,t.team_class teamClass,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight,"
			+" t.integral integral,t.rank rank,t.remark remark,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus, "
 			+" t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,p.number number,p.position position "
 			+" from u_team t "
 			+" left join u_team_dek d on t.team_id=d.team_id "
 			+" left join u_team_duel l on t.team_id= l.team_id "
			+" left join u_team_img i on  t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" left join u_player p on p.team_id=t.team_id and p.user_id=:userId"
			+" where t.team_status='3' and t.team_use_status='2'  and t.team_count>=7  ");

	//我默认球队信息
	private StringBuffer myDefaultTeamInfo_sql = new StringBuffer(
			" select p.player_id playerId,p.team_id teamId,t.name name,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight"
			+" ,t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus "
			+" ,t.integral integral,t.rank rank,i.imgurl imgurl,p.default_uteam defaultUteam,p.number number,p.position position,t.remark remark from u_player p "
			+" left join u_team t on p.team_id=t.team_id "
			+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+" left JOIN u_parameter_info up on r.member_type=up.params and up.pkey_id in (select upi.pkey_id from u_parameter upi where upi.params='member_type')"
			+" LEFT join u_team_img i on p.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'"
			+" left join u_team_dek d on t.team_id=d.team_id "
			+" left join u_team_duel l on t.team_id= l.team_id " 
			+" where p.user_id=:userId and (t.team_status='1' or t.team_status='2' or t.team_status='3') and (t.team_use_status='1' or t.team_use_status='2') and p.in_team='1' "
			+" group by p.player_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,up.is_unique ");
	
	//我的约战列表
	private StringBuffer getDuelUteamList_sql = new StringBuffer(" select p.player_id playerId,p.team_id teamId,t.name name,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight "
			+" ,t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus  "
			+" ,t.integral integral,t.rank rank,i.imgurl imgurl,p.default_uteam defaultUteam,p.number number,p.position position,c.br_court_id brCourtId from u_player p  "
			+" left join u_team t on p.team_id=t.team_id  "
			+"  LEFT JOIN u_champion c on t.team_id=c.team_id  "
			+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1'  "
			+" LEFT JOIN u_player_role_limit l on l.member_type=r.member_type "
			+" LEFT join u_team_img i on p.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1' "
			+" where p.user_id=:userId  "
			+" and (t.team_status='1' or t.team_status='2' or t.team_status='3')  "
			+" and (t.team_use_status='1' or t.team_use_status='2')  "
			+" and p.in_team='1' and l.duel='1' and t.team_count >= :teamCount "
			+" group by p.player_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
	
	//我的挑战列表
	private StringBuffer getChallengeUteamList_sql = new StringBuffer(" select p.player_id playerId,p.team_id teamId,t.name name,t.team_count teamCount,concat(cast(t.avg_age as decimal(18,1)),'') as avgAge "
			+" ,concat(cast(t.avg_height as decimal(18,1)),'') as avgHeight,concat(cast(t.avg_weight as decimal(18,1)),'') as avgWeight "
			+" ,t.team_class teamClass,t.short_name shortName,t.team_status teamStatus,t.team_use_status teamUseStatus  "
			+" ,t.integral integral,t.rank rank,i.imgurl imgurl,p.default_uteam defaultUteam,p.number number,p.position position,c.br_court_id brCourtId from u_champion c "
			+" LEFT JOIN u_team t on t.team_id=c.team_id  "
			+" left join u_player p on p.team_id=t.team_id and p.in_team='1' "
			+" left join u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
			+" LEFT JOIN u_player_role_limit l on l.member_type=r.member_type "
			+" LEFT join u_team_img i on p.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1' "
			+" where p.user_id=:userId  and c.is_champion='1' "
			+" and (t.team_status='1' or t.team_status='2' or t.team_status='3')  "
			+" and (t.team_use_status='1' or t.team_use_status='2')  "
			+" and l.challenge='1' and t.team_count >= :teamCount ");

	/**
	 * 
	 * 
	   TODO - 更新队徽时，查看球队是否存在 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		uTeam对象
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	@Override
	public UTeam findPlayerInfoById(HashMap<String, String> map) throws Exception {
		UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
		return uTeam;
	}
	/**
	 * 
	 * 
	   TODO - 战队详情 头部
	   @param map
	   		teamId		球员Id
	   @return
	   		uTeam的HashMap<String, Object>
	   @throws Exception
	   2016年1月25日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findPlayerInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		UTeam uTeam = baseDAO.get(UTeam.class, map.get("teamId"));
		if (null != uTeam) {
			//获取胜率
			HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(uTeam.getTeamId());
			String chances = hashMap.get("chances").toString();
			uTeam.setChances(chances);
		}
		resultMap.put("uTeam", uTeam);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情 获取战队列表
	   @param arrayList  球员列表
	   @return
	   		uTeam的HashMap<String, Object>
	   @throws Exception
	   2016年1月25日
	   dengqiuru
	 */
	public List<UTeam> getUteamNameList(List<UPlayer> arrayList) throws Exception{
		List<UTeam> resultList = new ArrayList<UTeam>();
		if (null != arrayList && arrayList.size() > 0) {
			for (UPlayer uPlayer : arrayList) {
				if (null != uPlayer.getUTeam()) {
					UTeam uTeam = (UTeam)baseDAO.get(UTeam.class, uPlayer.getUTeam().getTeamId());
					resultList.add(uTeam);
				}
			}
		}
		return resultList;
	}

	/**
	 * 建立球队
	 * map:
	 *  teamName		球队全名
	 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
	 *  teamSimpleName	简称
	 *  holdTime		球队成立时间
	 *  area			地区
	 *  subcourtId		主场
	 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
	 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
	 *  remark			简介
	 *  imgSizeType 	图片尺寸类型
	 *  imgurl			图片显示地址
	 *  imgWeight		图片权重
	 *  saveurl			图片保存地址
	 *  
	 */
	@Override
	public HashMap<String, Object> insertNewTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		UTeam uTeam = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			//获取该用户的信息
			UUser uUser = uuserService.getUserinfoByUserId(map);
			if (null != uUser) {
				//查询当前球员是否有资格可以创建球队
				HashMap<String, String> statusMap = this.checkUuserUteamStatus(map);
				if (!"5".equals(statusMap.get("uuserUteamStatus"))) {//不是
					//判断球队队长的球员信息是否都已填写完整
					String msg = "";
//					String msg = uuserService.isPlayerinfoComplete(uUser);
					if (msg == null || "".equals(msg)) {
						//验证球队全名称是否重复
						map.put("teamId", "");
						boolean isDoubleName = this.isDoubleName(map);
						if (isDoubleName == false) {
//							boolean isDoubleShortName = this.isDoubleShortName(map);
//							if (isDoubleShortName == false) {
							uTeam = this.createUTeam(uUser, map);
							if (null != uTeam) {
								map.put("teamId", uTeam.getTeamId());
								// 新建球队后，在球员表里新增队长记录
								String memberType = "1";
								uplayerService.insertTeamLeader(uUser, uTeam, memberType, map);
								// 新增球队后，添加队徽
								if (publicService.StringUtil(map.get("imgurl"))) {
									String msgUrl = publicService.isConnect(map.get("imgurl"));
									if ("".equals(msgUrl) || null == msgUrl) {
										return WebPublicMehod.returnRet("error", "非有效队徽url");
									}
									uteamImgService.insertTeamLogo(map, uUser, uTeam);
								}
								// 用户首次建立球队
								 this.setBehavior(uTeam,map);
								this.setUserBehaviorTypeParam(map, uTeam, uUser);
								// 更新球队区域到百度云
								this.setAreaToBaiduLBSCreateTeam(uTeam, map);

								resultMap.put("teamId", uTeam.getTeamId());
								// 保存首次建立球队身价信息
								map.put("taskBehavior", "3");// 1、入驻UPBOX平台2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
								resultMap.putAll(uworthService.saveTaskInfo(map));	
							} else {
								return WebPublicMehod.returnRet("error", "主队队衣颜色不能与客队队衣颜色一致！");
							}
//							}else{
//								return WebPublicMehod.returnRet("error","球队简称不能与其他球队的简称一致！");
//							}
						}else{
							return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
						}
					}else{
						return WebPublicMehod.returnRet("error","你的"+msg+"没有填写，请填写完整后再进行建队！");
					}
				}else{
					return WebPublicMehod.returnRet("error","只能创建一支球队！");
				}
			}else{
				return WebPublicMehod.returnRet("error","该用户不存在！");
			}
		}else{
			return WebPublicMehod.returnRet("error","请使用正常用户执行此操作！");
		}
		
		return resultMap;
	}


	/**
	 * 
	 * @TODO 新建或更新球队百度LBS数据
	 * @Title: setAreaToBaiduLBSCreateTeam 
	 * @param uTeam
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月26日 下午5:33:30
	 */
	@Override
	public void setAreaToBaiduLBSCreateTeam(UTeam uTeam, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		if (null != uTeam.getuRegion()) {
			hashMap.put("area", uTeam.getuRegion().get_id());
			String address = uRegionService.getURegionInfoByArea(hashMap);
			map.put("address", address);
			map.put("teamId", uTeam.getTeamId());
//			map.put("id", map.get("duelId"));
			this.insOrUpdTeamBaiduLBSData(map,uTeam);
		}
	}
	
//	/**
//	 * 
//	 * 
//	   TODO - 更新百度LBS
//	   @param uTeam
//	   @param map
//	   @throws Exception
//	   2016年7月2日
//	   dengqiuru
//	 */
//	private void setAreaToBaiduLBS(UTeam uTeam, HashMap<String, String> map) throws Exception {
//		HashMap<String, Object> hashMap = new HashMap<>();
//		if (publicService.StringUtil(map.get("area"))) {
//			hashMap.put("area", map.get("area"));
//			String address = uRegionService.getURegionInfoByArea(hashMap);
//			map.put("address", address);
//			map.put("teamId", uTeam.getTeamId());
//			if (null != uTeam.getuRegion()) {
//				this.updTeamBaiduLBSData(map);
//			}else{
//				this.insTeamBaiduLBSData(map,uTeam);
//			}
//		}
//	}

	/**
	 * 
	 * 
	   TODO - 根据用户Id检测用户与球队的各种状态
	   @param map
	   		loginUserId			当前用户Id
	   @return
	   		userUteamStatus		
	   		1:他不是队长，但是没有创建过球队；
	   		2：他是队长，但是没有创建过球队；
	   		3：他创建过球队，但是他不是队长 ；
	   		4：他创建过球队，他也是队长，但是为队长的球队中不存在他创建的球队
	   		5：他创建过球队，他也是队长，但是为队长的球队中存在他创建的球队  （这一状态不可以在创建球队）
	   2016年6月16日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, String> checkUuserUteamStatus(HashMap<String, String> map) throws Exception {
		HashMap<String, String> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		String uuserUteamStatus = "5";//默认他不可以建队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			//查询他是否创建过球队
			List<UTeam> uTeams = baseDAO.find(map, "from UTeam where UUser.userId=:loginUserId and teamStatus='3' and teamUseStatus='2'");
			List<String> teamIds = new ArrayList<>();
			if (null != uTeams && uTeams.size() > 0) {
				for (UTeam uTeam : uTeams) {
					teamIds.add(uTeam.getTeamId());
				}
				//有创建过球队
				//查询他是否有为队长的球员信息
				List<HashMap<String, Object>> outPlayerLists = uplayerService.getUteamIdListByuserId(hashMap);
				if (null != outPlayerLists && outPlayerLists.size() > 0) {
					//循环为队长的记录
					String type = "1";//没有相同的
					for (HashMap<String, Object> hashMap2 : outPlayerLists) {
						//判断是否包含
						if (teamIds.contains(hashMap2.get("teamId").toString())) {
							type = "2";
							break;
						}
					}
					if ("1".equals(type)) {
						uuserUteamStatus = "4";
					}
				}else{
					uuserUteamStatus = "3";
				}
			}else{
				//没有创建过球队
				//查询他是否为队长的球队
				String teamId = uplayerService.getUteamIdByuserId(hashMap);
				if (publicService.StringUtil(teamId)) {//有为队长的球队
					uuserUteamStatus = "2";
				}else{//没有为队长的球队
					uuserUteamStatus = "1";
				}
			}
		}
		resultMap.put("uuserUteamStatus", uuserUteamStatus);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 设置时间轴
	   @param map
	   		loginUserId  当前用户Id
	   @param uTeam
	   		uTeam 对象
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	private void setUserBehaviorTypeParam(HashMap<String, String> map, UTeam uTeam,UUser uUser) throws Exception {
		//用户首次加入球队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			//设置时间轴
			String userOrTeamType = "1";
			String behaviorType = "2";
			String objectType = null;
			if (publicService.StringUtil(map.get("imgurl"))) {
				objectType = map.get("imgurl");
			}
			setBehaviorType(userOrTeamType,objectType, behaviorType, uTeam, map,null,uUser);
		}
	}

	/**
	 * 
	 * 
	   TODO - 判断球队名是否与其他对重复
	   @param map
	   		teamName    球队名
	   		teamId		球队Id
	   @return
	   		isDoubleName	是否重复  true/false
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private boolean isDoubleName(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		boolean isDoubleName = false;
		if (publicService.StringUtil(map.get("teamName"))) {
			hashMap.put("teamName", map.get("teamName"));
			hashMap.put("teamId", map.get("teamId"));
			int count = baseDAO.count("select count(*) from UTeam where name=:teamName and teamId != :teamId", hashMap, false);
			if (count > 0) { // 不存在相同时间约战
				isDoubleName = true;
			}
		}
		return isDoubleName;
	}

	/**
	 * 
	 * 
	   TODO - 球队建立和成立时间轴，设置参数
	   @param uTeam    对象
	   @param map
	   		imgurl		对象显示地址
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setBehavior(UTeam uTeam, HashMap<String, String> map) throws Exception {
		String userOrUteamType = null ;
		String behaviorType = null ;
		String objectType = null ;
		String createDate = null;
		//球队建立时间
		if (publicService.StringUtil(uTeam.getTeamId())) {//球队建立时间
			map.put("createDate", null);
			//设置时间轴
			userOrUteamType = "2";
			behaviorType = "2";
			objectType = null;
			map.put("teamId", uTeam.getTeamId());
			createDate = null;
			setBehaviorType(userOrUteamType,objectType, behaviorType, uTeam,map,createDate,null);
		}
		if (publicService.StringUtil(uTeam.getTeamId())) {//球队加入upbox时间
			if (null != uTeam.getHoldDate()) {
				//设置时间轴
				userOrUteamType = "2";
				behaviorType = "1";
				if (publicService.StringUtil(map.get("imgurl"))) {
					objectType = map.get("imgurl");
				}
				map.put("teamId", uTeam.getTeamId());
				createDate = PublicMethod.getDateToString(uTeam.getHoldDate(), "yyyy-MM-dd hh:mm:ss");
				setBehaviorType(userOrUteamType,objectType, behaviorType, uTeam,map,createDate,null);
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 设置时间轴
	   @param type
	   		1:用户 2：球队
	   @param behaviorType
	   		事件类型
	   @param uTeam
	   		uTeam 对象
	   @param map
	   		loginUserId登录用户Id
	   		createDate		成立时间
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	private void setBehaviorType(String type,String objectType,String behaviorType,UTeam uTeam,HashMap<String, String> map,String createDate,UUser uUser) throws Exception{
		map.put("type", type);
		map.put("behaviorType", behaviorType);
		if (publicService.StringUtil(map.get("teamId"))) {
			map.put("teamId", map.get("teamId"));
		}
		if (null != uUser) {
			map.put("userId", uUser.getUserId());
		}
		map.put("objectId", uTeam.getTeamId());
		map.put("objectType", objectType);
		if (publicService.StringUtil(createDate)) {
			map.put("createdate", createDate);
		}
		publicService.updateBehavior(map);
	}
	/**
	 * 
	 * 
	   TODO - 新增球队
	   @param uUser
	   @param map 
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
	   @return
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	private UTeam createUTeam(UUser uUser,HashMap<String, String> map) throws Exception {
		//将传过来的成立日期转Date
		String type = "1";//验证主队队衣颜色是否选择
		//新增球队数据
		UTeam uTeam = new UTeam();
		uTeam.setTeamId(WebPublicMehod.getUUID());//球队全名
		if (publicService.StringUtil(map.get("teamName"))) {
			uTeam.setName(map.get("teamName"));
		}
		//球队简称
		if (publicService.StringUtil(map.get("teamSimpleName"))) {
			uTeam.setShortName(map.get("teamSimpleName"));
		}
		//所在区域1
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uTeam.setuRegion(uRegion);//区域
		}
		//球队分类 : 俱乐部、FC、公司
		if (publicService.StringUtil(map.get("teamClass"))) {
			uTeam.setTeamClass(map.get("teamClass"));
		}
		//主队球衣
		if (publicService.StringUtil(map.get("homeTeamShirts"))) {
			type = "2";//选择了主队球衣，在客队时，判断主队与客队颜色是否一致
			uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
		}
		//客队球衣
		if (publicService.StringUtil(map.get("awayTeamShirts"))) {
			if ("2".equals(type)) {
				if (map.get("homeTeamShirts").equals(map.get("awayTeamShirts"))) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
				}
			}
		}
		//常驻球场
		if (publicService.StringUtil(map.get("subcourtId"))) {
			uTeam.setuBrCourt(uCourtService.getUCourt(map.get("subcourtId")));
		}
		//简介
		if (publicService.StringUtil(map.get("remark"))) {
			uTeam.setRemark(map.get("remark"));
		}
		if (publicService.StringUtil(map.get("holddate"))) {
			Date holdDate = PublicMethod.getStringToDate( map.get("holddate"), "yyyy-MM-dd");
			uTeam.setHoldDate(holdDate);//成立日期
		}
//		String createDate = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd");
		uTeam.setCreatedate(new Date());//创建日期
		uTeam.setCreatetime(new Date());//创建时间
		uTeam.setCreateQd("APP");
		baseDAO.save(uTeam);
		this.setUteamParams(map,uTeam,uUser);//建队是设置球队表里的基础信息
		return uTeam;
	}
	/**
	 * 
	 * 
	   TODO - 建队是设置球队表里的基础信息
	   @param map
	   @param uTeam   对象
	   @param uUser	  对象
	   @return
	   		uTeam对象
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private UTeam setUteamParams(HashMap<String, String> map,UTeam uTeam,UUser uUser) throws Exception {
		//球队类型 : 1=青年队（25岁以下）、2=壮年队（25-35岁）、3=中年队（36-45岁）、4=老年队(45岁以上)
		String teamType = updateUTeamType(uUser.getAge());
		uTeam.setTeamType(teamType);
		uTeam.setUUser(uUser);//建队人
		uTeam.setTeamCount(1);//人数
		uTeam.setHistoryCount(1);//历史人数
		if (null != uUser.getAge()) {
			uTeam.setAvgAge(uUser.getAge().toString());//平均年龄
		}else{
			if (null != uUser.getBirthday()) {
				uTeam.setAvgAge(this.getAgeByBirthday(uUser.getBirthday()).toString());
			}else{
				uTeam.setAvgAge("0");//平均年龄
			}
		}
		if (null != uUser.getHeight() && !"".equals(uUser.getHeight())) {
			uTeam.setAvgHeight(uUser.getHeight().toString());//平均身高
		}else{
			uTeam.setAvgHeight("0");//平均身高
		}
		if (null != uUser.getWeight() && !"".equals(uUser.getWeight())) {
			uTeam.setAvgWeight(uUser.getWeight().toString());//平均体重
		}else{
			uTeam.setAvgWeight("0");//平均身高
		}
		uTeam.setTeamStatus("3");//队伍审核状态 1=待审核、2=审核不通过、3=审核通过、4=队长解散、6-后端解散
		uTeam.setTeamUseStatus("2");//队伍状态 1=禁用、2=正常使用、3-解散
		uTeam.setTeamsource("1");//队伍创建来源 1-前端创建 2-后端创建 3 活动建立
		uTeam.setMaximum(Public_Cache.TEAM_MAXIMUM);//设置最高限制人数
		uTeam.setRecommendTeam("0");//设置推荐状态 0：未设置，1：推荐
		uTeam.setOld_key_id(-1);
		baseDAO.update(uTeam);
		return uTeam;
	}

	/**
	 * 
	 * 
	   TODO - 根据出生日期计算年龄
	   @param birthday	出生日期
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public Integer getAgeByBirthday(Date birthday) {
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
	   TODO - 根据年龄定义队伍类型
	   @param age		年龄
	   @return
	   		teamType	球队类型
	   2016年6月1日
	   dengqiuru
	 */
	private String updateUTeamType(Integer age) {
		String teamType = "";
		if (null != age && age != 0) {
			if (0 < age && age < 25) {
				teamType = "1";//1=青年队（25岁以下）
			}else if (25 <= age && age <= 35) {
				teamType = "2";//2=壮年队（25-35岁）
			}else if (36 <= age && age <= 45) {
				teamType = "3";//3=中年队（36-45岁）
			}else if (age > 45) {
				teamType = "4";//4=老年队(45岁以上)
			}
		}
		return teamType;
	}


	/**
	 * 
	 * 
	   TODO - 战队列表 及搜索 【2.0.0】
	   @param map
	   		searchStr   搜索内容
	   		page 		分页
	   		teamListId		约战的战队Id列表
	   		searchType		天梯搜索
	   @return
	   		uTeamList的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUteamList(HashMap<String, String> map,HashMap<String,List<Object>> mapList) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回集合
		HashMap<String, Object> hashMap = new HashMap<>();//参数集合
		OutUteamList outUteamList = new OutUteamList();
		//站队列表用户信息 列表头部信息
		outUteamList  = findUteaminfoListHead(map);
		//分页
		List<Object> listHeadTeamId = new ArrayList<Object>();
		//角标
		List<List<CornerBean>> cornerHeadList = new ArrayList<List<CornerBean>>();
		if (null != outUteamList) {
			listHeadTeamId.add(outUteamList.getTeamId());
			cornerHeadList = cornerService.getAllTeamCornerList(map, listHeadTeamId);
		}
		resultMap.put("outUteamList", outUteamList);
		resultMap.put("cornerHeadList", cornerHeadList);
		//判断搜素内容是否为空
		StringBuffer newSql=new StringBuffer();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
		}else{
			hashMap.put("userId", "");
		}
		//约过的对手查询
		if (null != mapList) {
			if (null != mapList.get("teamListId") && mapList.get("teamListId").size() > 0) {
				newSql.append( getUteamList_sql +" and t.team_id in (:teamListId) and t.team_count>=7 ");
				//排序发起球队
				if (publicService.StringUtil(map.get("teamId"))) {
					hashMap.put("teamId", map.get("teamId"));
					newSql.append(" and t.team_id not in (:teamId)");
				}else{
					newSql.append(" and t.team_id not in (select pp.team_id from u_player pp,u_player_role r where pp.player_id=r.player_id and pp.user_id=:userId and pp.team_id is not null and pp.in_team='1' and r.member_type='1' and r.member_type_use_status='1'");
				}
				//搜索查询
				if (publicService.StringUtil(map.get("searchStr"))) {
					hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
					newSql.append( " and (name like :searchStr or short_name  like :searchStr or t.remark like :searchStr "
							+ " or t.team_id in (select up.team_id from u_player up,u_user uu,u_player_role r where up.player_id=r.player_id and uu.user_id=up.user_id and r.member_type='1' and r.member_type_use_status='1' and up.in_team='1' and (uu.nickname like :searchStr or uu.realname like :searchStr ))) "
							+ " group by t.team_id order by t.createdate desc,t.createtime desc "
							);
				}else{
					newSql.append( " group by t.team_id order by t.createdate desc,t.createtime desc ");
				}
			}
		}else if(publicService.StringUtil(map.get("searchType"))){//天梯列表搜索
			if (publicService.StringUtil(map.get("searchStr"))) {
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append( getUteamList_sql +" and (name like :searchStr or short_name  like :searchStr or t.remark like :searchStr "
						+ " or t.team_id in (select up.team_id from u_player up,u_user uu,u_player_role r where up.player_id=r.player_id and uu.user_id=up.user_id and r.member_type='1' and r.member_type_use_status='1' and up.in_team='1' and (uu.nickname like :searchStr or uu.realname like :searchStr ))) "
						+ " group by t.team_id order by t.createdate desc,t.createtime desc "
						);
			}else{
				newSql.append( getUteamList_sql + " group by t.team_id order by t.createdate desc,t.createtime desc ");
			}
		}else{
			//球队人数限制
			int teamMaximum = Public_Cache.TEAM_MAXIMUM;
			if (0 == teamMaximum) {
				teamMaximum = 50;
			}
			hashMap.put("teamMaximum", teamMaximum);
			//搜索查询
			newSql.append( getUteamList_sql + " and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:userId and pp.team_id is not null and pp.in_team='1' )  and t.team_count<:teamMaximum");
			if (publicService.StringUtil(map.get("searchStr"))) {
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append( " and (name like :searchStr or short_name  like :searchStr or t.remark like :searchStr "
						+ " or t.team_id in (select up.team_id from u_player up,u_user uu,u_player_role r where up.player_id=r.player_id and uu.user_id=up.user_id and r.member_type='1' "
						+ " and r.member_type_use_status='1' and up.in_team='1' and (uu.nickname like :searchStr or uu.realname like :searchStr ))) "
						);
			
			}
			newSql.append(" group by t.team_id order BY ");
			newSql.append(" t.recommend_team=1 desc,case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_count>=7 desc,t.team_count<7 desc ");
			
		}
		//自己的球队不应该在球队列表内

		List<HashMap<String, Object>> listTeam=new ArrayList<HashMap<String, Object>>();
		//分页
		List<Object> listTeamId = new ArrayList<Object>();
		//角标
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if(null != map.get("page")&& !"".equals(map.get("page"))){
			listTeam = this.getPageLimit(newSql.toString(), map, hashMap,mapList);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
					//关注数
					map.put("teamId", teamId);
					HashMap<String, Object> teamFollowCount = this.pubGetUTeamFollowCount(map);
					if(teamFollowCount.size()>0){
						hashMap2.put("followCount", teamFollowCount.size());
					}else{
						hashMap2.put("followCount", 0);
					}
				}
			}
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
		}

		//站队列表用户信息
		resultMap.put("outUteamLists", listTeam);
		resultMap.put("cornerList", cornerList);
		return resultMap;
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
	private List<HashMap<String, Object>> getPageLimit(String sql, HashMap<String, String> map,HashMap<String, Object> hashMap,HashMap<String,List<Object>> mapList) throws Exception{
		List<HashMap<String, Object>> list;
		hashMap.put("page", map.get("page"));
		PageLimit pa = new PageLimit(Integer.parseInt(map.get("page")), 0);
		hashMap.put("limit", pa.getLimit());
		hashMap.put("offset", pa.getOffset());
		StringBuffer newSql = new StringBuffer();
		if (null != mapList) {
			if (null != mapList.get("teamListId") && mapList.get("teamListId").size() > 0) {
				newSql.append( sql + " limit 10 " );
				list = baseDAO.findSQLMap(newSql.toString(), hashMap, mapList);
			}else{
				list = null;
			}
		}else{
			newSql.append( sql + " limit :limit offset :offset" );
			list = baseDAO.findSQLMap(newSql.toString(),hashMap);
		}
		// 更改类型和状态的页面显示值
		if (list != null && list.size() > 0) {
			for (HashMap<String, Object> hashMap2 : list) {
				this.displayData(hashMap2,map);
				this.setUteamChances(hashMap2);
				//填充角色
				uPlayerRoleService.setMembertype202(hashMap2);
			}
		}
		return list;
	}
	

	/**
	 * 
	 * TODO 球员身份处理 【2.0.0】
	 * @param hashMap
	 * 		teamCount		球队人数
	 * 		maximum			球队最高人数
	 * 		memberType		球员身份
	 * 		teamClass		球队分类
	 * 		homeTeamShirts	主队球衣
	 * 		awayTeamShirts	客队球衣
	 * 		position		球队中的位置
	 * 		teamBehaviorType	球队时间类型
	 * 		teamUseStatus	球队状态
	 * 		avgAge			球队平均年龄
	 * 		followStatus	关注状态
	 * @param map
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	@Override
	public void displayData(HashMap<String, Object> hashMap,HashMap<String, String> map) throws Exception {
		if (null != hashMap) {
			if (null != hashMap.get("teamCount")) {//设置是否满员
				String isMax = "2";//不是满员
				Integer teamCount = Integer.parseInt(hashMap.get("teamCount").toString());
				Integer maximum = 0;
				if (null != hashMap.get("maximum")) {
					maximum = Integer.parseInt(hashMap.get("maximum").toString());
					if (maximum != 0 ) {
						maximum = Public_Cache.TEAM_MAXIMUM;
					}
					if (teamCount >= maximum) {
						isMax = "1";//是满员
					}
				}else{
					if (maximum == 0 || null == maximum ) {
						maximum = Public_Cache.TEAM_MAXIMUM;
					}
					if (teamCount >= maximum) {
						isMax = "1";//是满员
					}
				}
				hashMap.put("isMax",isMax);
			}
			if (null != hashMap.get("teamClass") && !"".equals(hashMap.get("teamClass")) &&  !"null".equals(hashMap.get("teamClass"))) {//球队分类
				hashMap.put("teamClassName", Public_Cache.HASH_PARAMS("team_class").get(hashMap.get("teamClass")));
			}else{
				hashMap.put("teamClassName", null);
			}
			if (null != hashMap.get("homeTeamShirts") && !"".equals(hashMap.get("homeTeamShirts")) &&  !"null".equals(hashMap.get("homeTeamShirts"))) {//主队球衣
				hashMap.put("homeTeamShirtsName", Public_Cache.HASH_PARAMS("home_team_shirts").get(hashMap.get("homeTeamShirts")));
			}else{
				hashMap.put("homeTeamShirtsName", null);
			}
			if (null != hashMap.get("awayTeamShirts") && !"".equals(hashMap.get("awayTeamShirts")) &&  !"null".equals(hashMap.get("awayTeamShirts"))) {//客队球衣
				hashMap.put("awayTeamShirtsName", Public_Cache.HASH_PARAMS("away_team_shirts").get(hashMap.get("awayTeamShirts")));
			}else{
				hashMap.put("awayTeamShirtsName", null);
			}
			if (null != hashMap.get("position") && !"".equals(hashMap.get("position")) &&  !"null".equals(hashMap.get("position"))) {//位置
				if (!"16".equals(hashMap.get("position"))) {
					hashMap.put("positionName",Public_Cache.HASH_PARAMS("position").get(hashMap.get("position")));
				}else{
					hashMap.put("positionName", null);
				}
			}else{
				hashMap.put("positionName", null);
			}
			if (null != hashMap.get("teamBehaviorType") && !"".equals(hashMap.get("teamBehaviorType")) &&  !"null".equals(hashMap.get("teamBehaviorType"))) {//事件类型名称
				hashMap.put("teamBehaviorTypeName",Public_Cache.HASH_PARAMS("team_behavior_type").get(hashMap.get("teamBehaviorType")));
			}else{
				hashMap.put("teamBehaviorTypeName", null);
			}
			if (null != hashMap.get("teamUseStatus") && !"".equals(hashMap.get("avgAge")) &&  !"null".equals(hashMap.get("avgAge"))) {//事件类型名称
				if ("1".equals(hashMap.get("teamUseStatus"))) {
					hashMap.put("integral",null);
					hashMap.put("rank",null);
				}
			}
			if (null != hashMap.get("followStatus") && !"".equals(hashMap.get("followStatus")) &&  !"null".equals(hashMap.get("followStatus"))) {//是否关注
				hashMap.put("isFollow", hashMap.get("followStatus"));
			}else{
				hashMap.put("isFollow", "2");
			}//设置满员状态
		}
		this.displayDataByMap(hashMap, map);
	}

	/**
	 * 
	 * TODO 球队概况-球员在这个球员的各种状态 【2.0.0】
	 * @param hashMap
	 * @param map
	 * 		loginUserId		当前用户
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	private void displayDataByMap(HashMap<String, Object> hashMap, HashMap<String, String> map) throws Exception {
		if (null != map) {
			if (publicService.StringUtil(map.get("loginUserId"))) {
				String type = "3";//3：代表该用户没加入或创建球队
				UPlayer uPlayer = uplayerService.getUplayerinfoByuserIdteamIdNotSet(map, type);
				if (null != uPlayer) {//不为空：当前用户是球队的球员
					hashMap.put("isTeamLeader", "1");//1:我有创建的球队
					hashMap.put("myTeamId", uPlayer.getUTeam().getTeamId());
				}else{
					List<UPlayer> uPlayerList = uplayerService.getUPlayerIsNotTeamLeader(map);
					if (null != uPlayerList && uPlayerList.size() > 0) {
						hashMap.put("isTeamLeader", "2");//2:有我加入的球队
						hashMap.put("myTeamId", uPlayerList.get(0).getUTeam().getTeamId());
					}else{
						hashMap.put("myTeamId", null);
						hashMap.put("isTeamLeader", "3");
					}
				}
			}else{
				hashMap.put("myTeamId", null);
				hashMap.put("isTeamLeader", "3");
			}
		}else{
			hashMap.put("myTeamId", null);
			hashMap.put("isTeamLeader", "3");
		}
	}


	/**
	 * 
	 * TODO - 球队列表填充该返回的数据
	 * @param uTeam		对象
	 * @param map
	 * 		loginUserId		当前用户
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	public OutUteamList setOutUTeamList(UTeam uTeam,HashMap<String, String> map) throws Exception {
		OutUteamList outUteamList = new OutUteamList();
		if (null != uTeam) {
			outUteamList.setTeamId(uTeam.getTeamId());
			map.put("teamId", uTeam.getTeamId());
			UTeamImg uTeamImg = uteamImgService.getHeadPicNotSetByTeamId(map);
			String objectName = null;
			if (null != uTeamImg) {
				outUteamList.setImgurl(uTeamImg.getImgurl());//头像
			}
			if (publicService.StringUtil(uTeam.getName())) {
				outUteamList.setName(uTeam.getName());//球队名称
			}
			if (publicService.StringUtil(uTeam.getShortName())) {
				outUteamList.setShortName(uTeam.getShortName());//球队简称
			}
			if (publicService.StringUtil(uTeam.getTeamClass())) {
				outUteamList.setTeamClass(uTeam.getTeamClass());//球队分类
				objectName = Public_Cache.HASH_PARAMS("team_class").get(uTeam.getTeamClass());
				outUteamList.setTeamClassName(objectName);//球队分类对应的名称
			}
			if (publicService.StringUtil(uTeam.getAvgAge())) {
				outUteamList.setAvgAge(this.roundNumber(Double.parseDouble(uTeam.getAvgAge())));//平均年龄
			}
			if (publicService.StringUtil(uTeam.getAvgWeight())) {
				outUteamList.setAvgWeight(this.roundNumber(Double.parseDouble(uTeam.getAvgWeight())));//平均体重
			}
			if (publicService.StringUtil(uTeam.getAvgHeight())) {
				outUteamList.setAvgHeight(this.roundNumber(Double.parseDouble(uTeam.getAvgHeight())));//平均身高
			}
			if (publicService.StringUtil(uTeam.getRemark())) {
				outUteamList.setRemark(uTeam.getRemark());//简介
			}
			if (null != uTeam) {
				HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(uTeam.getTeamId());
				if (null != hashMap.get("chances")) {
					objectName =  hashMap.get("chances").toString();
					outUteamList.setChances(objectName);//胜率
				}
				//设置该球员在球队中的位置和背号
				map.put("teamId", uTeam.getTeamId());
			}
			if (uTeam.getRank() != null ) {
				outUteamList.setRank(uTeam.getRank().toString());//天梯
			}
			if (uTeam.getIntegral() != null ) {
				outUteamList.setIntegral(uTeam.getIntegral().toString());//激数
			}
			if (uTeam.getTeamCount() != null) {
				outUteamList.setTeamCount(uTeam.getTeamCount());//队伍人数
			}
			//查找解散的用户是否在本队为队长
			String type = "2";
			Boolean isHeaderByUserId = uplayerService.isHeaderByUserId(map, type);
			if (isHeaderByUserId == true) {//是队长
				outUteamList.setIsTeamLeader("1");
			}else{
				outUteamList.setIsTeamLeader("2");
			}
			this.setUplayerInoutUteamList(outUteamList,map);//将球员信息set到球队输出model中
		}
		return outUteamList;
	}

	/**
	 * 
	 * 
	   TODO - 将球员信息set到球队输出model中
	   @param outUteamList  球队输出表
	   @param map
	   		userId		用户Id
	   		teamId 		球队Id
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setUplayerInoutUteamList(OutUteamList outUteamList,HashMap<String, String> map) throws Exception {
		String type = "1";
		UPlayer uPlayer = uplayerService.getUplayerinfoByuserIdteamIdNotSet(map,type);
		String objectName = null;
		if (null != uPlayer) {
			if (publicService.StringUtil(uPlayer.getPosition())) {//球员位置
				if (!"16".equals(uPlayer.getPosition())) {
					objectName = Public_Cache.HASH_PARAMS("position").get(uPlayer.getPosition());
				}
				outUteamList.setPosition(uPlayer.getPosition());
				outUteamList.setPositionName(objectName);
			}
			if (uPlayer.getNumber() != null) {//球员背号
				outUteamList.setNumber(uPlayer.getNumber().toString());
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 加入一个球队
	   @param map
		   	teamId		球队Id
		   	loginUserId	当前用户Id
		   	memberType	角色
		   	position	位置
		   	number		背号
	   @return
	   @throws Exception
	   2016年2月1日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> joinInTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("appCode"))) {
			if(WebPublicMehod.intAppCode(map.get("appCode"))<203){
				return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
			}
			resultMap = this.joinInTeamAppCode(map);
//			resultMap = this.joinInTeam202(map);
		}else{
			return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
//			resultMap = this.joinInTeam200(map);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 202之前的加队
	   @param map
	   @return
	   @throws Exception
	   2016年6月28日
	   dengqiuru
	 */
	private HashMap<String, Object> joinInTeam200(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String type = "1";//1:根据用户Id和teamId查找球员信息
		UPlayer uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map,type);
		UUser uUser = uuserService.getUserinfoByUserId(map);
		UTeam uTeam = new UTeam();
		//判断是否加入了当前球队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (null == uPlayer) {
				if (publicService.StringUtil(map.get("teamId"))) {
					uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
					if (null != uTeam) {
						if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
							if ("1".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
							}else if ("3".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
							}else{
								int max = 50;
								if (null != uTeam.getMaximum()) {
									max = uTeam.getMaximum();
								}
								//判断当前球队是否超过球队规定人数
								if (uTeam.getTeamCount() < max) {
									if (!"1".equals(map.get("memberType"))) {
										type = "3";
//										//查看是否加入了当前球队
										uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map,type);
										if (null == uPlayer) {//null:没有  可以加队
											uPlayer = uplayerService.insertPlayerInTeam(uUser,uTeam,map);
											if (null != uPlayer) {
												//更新角色表
												uPlayerRoleService.setNewMemberTypeForJoinTeam(uPlayer,map);
												//修改球队信息
												uTeam = updateTeamInfo(map);
												if (null != uTeam) {
													uplayerService.updateTeamAvg(uUser);
													//通知
													String messageType = "1";//1:加队通知
													this.setMessageParams(map,uPlayer,uTeam,messageType);
													this.recallFollow(map,uTeam);//取消关注
													//修改邀请状态
													uInviteteamService.updateInviteStatus(map,uPlayer,uTeam);
													//设置时间轴
													this.setBehaviorTypeParam(map,uTeam,uUser);
													
													this.setAreaToBaiduLBSCreateTeam(uTeam, map);//更新百度LBS
													//加队 加队数变化更新球员lbs
													uuserService.setAreaToBaiduLBS(uUser);
													// 保存首次加入球队身价信息
													map.put("taskBehavior", "4");// 1、入驻UPBOX平台  2、成为UPBOX球员  3、首次建立球队 4、首次加入球队
													resultMap.putAll(uworthService.saveTaskInfo(map));	
													
													resultMap.put("success", "加队成功！");
												}else{
													resultMap.put("error", "加队失败！");
												}
											}else{
												return WebPublicMehod.returnRet("error","你的背号在球队中已经存在！");
											}
										}else{//不为null，不可以加队
											return WebPublicMehod.returnRet("error","你已加入当前球队！");
										}
									}else{
										return WebPublicMehod.returnRet("error","加入球队不能选择队长！");
									}
								}else{
									return WebPublicMehod.returnRet("error","当前球队球员人数已满，可以考虑加入其他球队！");
								}
							}
						}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
						}else{
							return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
						}
					}else{
						return WebPublicMehod.returnRet("error","当前球队不存在！");
					}
				}else{
					return WebPublicMehod.returnRet("error","teamId不能为空！");
				}
			}else{
				map.put("player", uPlayer.getPlayerId());
				List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
				if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
					return WebPublicMehod.returnRet("error","你已经加入了当前球队！");
				}else{
					//更新角色表
					uPlayerRoleService.setNewMemberTypeForJoinTeam(uPlayer,map);
					uplayerService.updateTeamAvg(uUser);
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","请登录正常账户在执行此操作！");
		}

		return resultMap;
	}
	/**
	 * 
	 * TODO 加队 -2.0.3
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月29日
	 */
	private HashMap<String, Object> joinInTeamAppCode(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String type = "1";//1:根据用户Id和teamId查找球员信息
		UPlayer uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map,type);
		UUser uUser = uuserService.getUserinfoByUserId(map);
		UTeam uTeam = new UTeam();
		//判断是否加入了当前球队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (null == uPlayer) {
				if (publicService.StringUtil(map.get("teamId"))) {
					uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
					if (null != uTeam) {
						if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
							if ("1".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
							}else if ("3".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
							}else{
								int max = 50;
								if (null != uTeam.getMaximum()) {
									max = uTeam.getMaximum();
								}
								// 判断当前球队是否超过球队规定人数
								if (uTeam.getTeamCount() < max) {
									uPlayer = uplayerService.insertPlayerInTeam(uUser, uTeam, map);
									if (null != uPlayer) {
										// 更新角色表 2.0.3
										uPlayerRoleService.applyPlayerJoinTeamMemberType(uPlayer, uTeam, map);
										// 修改球队信息
										uTeam = updateTeamInfo(map);
										if (null != uTeam) {
											uplayerService.updateTeamAvg(uUser);
											// 通知
											String messageType = "1";// 1:加队通知
											this.setMessageParams(map, uPlayer, uTeam, messageType);
											this.recallFollow(map, uTeam);// 取消关注
											// 修改邀请状态
											uInviteteamService.updateInviteStatus(map, uPlayer, uTeam);
											// 设置时间轴
											this.setBehaviorTypeParam(map, uTeam, uUser);

											this.setAreaToBaiduLBSCreateTeam(uTeam, map);// 更新百度LBS
											// 加队 加队数变化更新球员lbs
											uuserService.setAreaToBaiduLBS(uUser);

											// 保存首次加入球队身价信息
											map.put("taskBehavior", "4");// 1、入驻UPBOX平台2、成为UPBOX球员3、首次建立球队4、首次加入球队
											resultMap.putAll(uworthService.saveTaskInfo(map));

											resultMap.put("success", "加队成功！");
										} else {
											resultMap.put("error", "加队失败！");
										}
									} else {
										return WebPublicMehod.returnRet("error", "你的背号在球队中已经存在！");
									}

								} else {
									return WebPublicMehod.returnRet("error", "当前球队球员人数已满，可以考虑加入其他球队！");
								}
							}
						}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
						}else{
							return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
						}
					}else{
						return WebPublicMehod.returnRet("error","当前球队不存在！");
					}
				}else{
					return WebPublicMehod.returnRet("error","teamId不能为空！");
				}
			}else{
				map.put("playerId", uPlayer.getPlayerId());
				List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
				if (null == uPlayerRoles || uPlayerRoles.size() <= 0) {
					//更新角色表
					uPlayerRoleService.setNewMemberTypeForJoinTeam(uPlayer,map);
					uplayerService.updateTeamAvg(uUser);
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","请登录正常账户在执行此操作！");
		}

		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 202版本加队
	   @param map
	   @return
	   @throws Exception
	   2016年6月28日
	   dengqiuru
	 */
	private HashMap<String, Object> joinInTeam202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String type = "1";//1:根据用户Id和teamId查找球员信息
		UPlayer uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map,type);
		UUser uUser = uuserService.getUserinfoByUserId(map);
		UTeam uTeam = new UTeam();
		//判断是否加入了当前球队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (null == uPlayer) {
				if (publicService.StringUtil(map.get("teamId"))) {
					uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
					if (null != uTeam) {
						if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
							if ("1".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
							}else if ("3".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
							}else{
								int max = 50;
								if (null != uTeam.getMaximum()) {
									max = uTeam.getMaximum();
								}
								//判断当前球队是否超过球队规定人数
								if (uTeam.getTeamCount() < max) {
									int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));
									if (!"1".equals(map.get("memberType"))&&!"2.0.2".equals(map.get("appCode"))) {
										uPlayer = uplayerService.insertPlayerInTeam(uUser,uTeam,map);
										if (null != uPlayer) {
											if(intAppCode > 201){
												//更新角色表
												uPlayerRoleService.setNewMemberTypeForJoinTeam(uPlayer,map);
											}else{
												//更新角色表
												uPlayerRoleService.applyPlayerJoinTeamMemberType(uPlayer,uTeam,map);
											}
											//修改球队信息
											uTeam = updateTeamInfo(map);
											if (null != uTeam) {
												uplayerService.updateTeamAvg(uUser);
												//通知
												String messageType = "1";//1:加队通知
												this.setMessageParams(map,uPlayer,uTeam,messageType);
												this.recallFollow(map,uTeam);//取消关注
												//修改邀请状态
												uInviteteamService.updateInviteStatus(map,uPlayer,uTeam);
												//设置时间轴
												this.setBehaviorTypeParam(map,uTeam,uUser);
												
												this.setAreaToBaiduLBSCreateTeam(uTeam, map);//更新百度LBS
												//加队 加队数变化更新球员lbs
												uuserService.setAreaToBaiduLBS(uUser);
												
												// 保存首次加入球队身价信息
												map.put("taskBehavior", "4");// 1、入驻UPBOX平台  2、成为UPBOX球员 / 3、首次建立球队 4、首次加入球队
												resultMap.putAll(uworthService.saveTaskInfo(map));	
												
												resultMap.put("success", "加队成功！");
											}else{
												resultMap.put("error", "加队失败！");
											}
										}else{
											return WebPublicMehod.returnRet("error","你的背号在球队中已经存在！");
										}
									}else{
										return WebPublicMehod.returnRet("error","加入球队不能选择队长！");
									}
								}else{
									return WebPublicMehod.returnRet("error","当前球队球员人数已满，可以考虑加入其他球队！");
								}
							}
						}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
						}else{
							return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
						}
					}else{
						return WebPublicMehod.returnRet("error","当前球队不存在！");
					}
				}else{
					return WebPublicMehod.returnRet("error","teamId不能为空！");
				}
			}else{
				map.put("playerId", uPlayer.getPlayerId());
				List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
				if (null == uPlayerRoles || uPlayerRoles.size() <= 0) {
					//更新角色表
					uPlayerRoleService.setNewMemberTypeForJoinTeam(uPlayer,map);
					uplayerService.updateTeamAvg(uUser);
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","请登录正常账户在执行此操作！");
		}

		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 球员加入球队后，若关注过当前球队就取消关注
	   @param map
	   		loginUserId 	当前用户Id
	   		teamId			当前球队Id
	   @param uTeam
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void recallFollow(HashMap<String, String> map, UTeam uTeam) throws Exception {
		if (null != uTeam) {
			map.put("teamId", uTeam.getTeamId());
			//查看该用户和球队的关注事件是否存在
			UFollow uFollow = baseDAO.get(map, "from UFollow where UUser.userId=:loginUserId and objectId=:teamId and userFollowType='2'");
			if (null != uFollow) {//存在
				//判断是否已关注
				if ("1".equals(uFollow.getFollowStatus())) {//已关注：就取消关注
					uFollow.setFollowStatus("2");
					baseDAO.update(uFollow);
				}
			}
		}
	} 

	/**
	 * 
	 * 
	   TODO - 统一方法：设置通知参数
	   @param map
	   @param uPlayer
	   @param uTeam
	   @param messageType
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	@SuppressWarnings("unused")
	private void setMessageParams(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam, String messageType) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		String mesType = null;//发送的短信类型
		String msg = "";//发送的短信内容
		String userId = null;//给谁发送
		String params = null;//跳转参数
		String jump = null;//跳转类型
		String code = null;//推送code
		String repetition = "1";
		List<UPlayer> uPlayerTemps = null;
		List<HashMap<String, Object>> listTeam= null;
		if (null != uTeam) {
			//根据球队Id查询队长的userId
			hashMap.put("teamLeaderTeamId", uTeam.getTeamId());
			hashMap.put("msgTeam", Public_Cache.MEMBER_TYPE_LVL1);
			String memberType1 = Public_Cache.MEMBER_TYPE_LVL1;
			StringBuffer sql = new StringBuffer("select p.user_id userId,p.player_id playerId,u.realname realname,u.nickname nickname from u_player p "
					+" LEFT JOIN u_user u on u.user_id=p.user_id "
					+" left join u_player_role r on r.player_id=p.player_id "
					+" where p.team_id=:teamLeaderTeamId "
					+" and r.member_type in ("+memberType1+")"
					+" and p.in_team='1' "
					+" and r.member_type_use_status='1'"
					+" group by p.player_id ");
			listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
		}
		if ("1".equals(messageType)) {//加队通知
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					if (null != hashMap2) {
						mesType = "tmJoin";
						if (null != hashMap2.get("userId")) {
							userId = hashMap2.get("userId").toString();
						}
						jump = "b02";
						if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
							msg = uPlayer.getUUser().getRealname();
						}else if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
							msg = uPlayer.getUUser().getNickname();
						}
						params = "{\"jump\":\"" + jump + "\",\"playerId\":\""+ uPlayer.getPlayerId() + "\",\"teamId\":\""+ uTeam.getTeamId() + "\",\"userId\":\""+ userId + "\"}";
						//极光推送参数
						map.put("jump", jump);
						map.put("playerId", uPlayer.getPlayerId());
						map.put("teamId",  uTeam.getTeamId());
						map.put("userId",  userId);
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
						
						this.setMessage(map, mesType, msg, userId, params,repetition);//发送通知
						this.publicAppPush(map,mesType,msg,code);//发送极光推送
						
					}
				}
			}
		}else if ("2".equals(messageType)) {//踢人通知
			mesType = "tmKick";
			if (null != uPlayer.getUUser()) {
				userId = uPlayer.getUUser().getUserId();
			}
			jump = "b01";
			if (publicService.StringUtil(uTeam.getName())) {
				msg = uTeam.getName();
			}else if (publicService.StringUtil(uTeam.getShortName())) {
				msg = uTeam.getShortName();
			}
			params = "{\"jump\":\"" + jump + "\",\"teamId\":\""+ uTeam.getTeamId() + "\"}";
			//极光推送参数
			map.put("jump", jump);
			map.put("teamId",  uTeam.getTeamId());
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
			
			this.setMessage(map, mesType, msg, userId, params,repetition);//发送通知
			this.publicAppPush(map,mesType,msg,code);//发送极光推送
		}else if ("3".equals(messageType)) {//退出通知//加队通知
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					if (null != hashMap2) {
						mesType = "tmExit";
//						userId = uTeam.getUUser().getUserId();
						if (null != hashMap2.get("userId")) {
							userId = hashMap2.get("userId").toString();
						}
						jump = "b02";
						if (publicService.StringUtil(uPlayer.getUUser().getRealname())) {
							msg = uPlayer.getUUser().getRealname();
						}else if (publicService.StringUtil(uPlayer.getUUser().getNickname())) {
							msg = uPlayer.getUUser().getNickname();
						}
						params = "{\"jump\":\"" + jump + "\",\"playerId\":\""+ uPlayer.getPlayerId() + "\",\"teamId\":\""+ uTeam.getTeamId() + "\",\"userId\":\""+ userId + "\"}";
						//极光推送参数
						map.put("jump", jump);
						map.put("playerId", uPlayer.getPlayerId());
						map.put("teamId",  uTeam.getTeamId());
						map.put("userId",  userId);
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
						
						this.setMessage(map, mesType, msg, userId, params,repetition);//发送通知
						this.publicAppPush(map,mesType,msg,code);//发送极光推送}
					}
				}
			}
		}else if ("4".equals(messageType)) {//邀请通知
			mesType = "tmInvite";
			if (null != uPlayer.getUUser()) {
				userId = uPlayer.getUUser().getUserId();
			}
			jump = "b01";
			if (publicService.StringUtil(uTeam.getName())) {
				msg = uTeam.getName();
			}else if (publicService.StringUtil(uTeam.getShortName())) {
				msg = uTeam.getShortName();
			}
			params = "{\"jump\":\"" + jump + "\",\"teamId\":\""+ uTeam.getTeamId() + "\"}";
			//极光推送参数
			map.put("jump", jump);
			map.put("teamId",  uTeam.getTeamId());
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
			
			this.setMessage(map, mesType, msg, userId, params,repetition);//发送通知
			this.publicAppPush(map,mesType,msg,code);//发送极光推送
		}
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
	private void publicAppPush(HashMap<String, String> map, String mesType, String msg, String code) throws Exception {
		map.put("mes_type", mesType);
		map.put("code", code);
		map.put("content", msg);
		publicPushService.publicAppPush(map);
	}

	/**
	 * 
	 * 
	   TODO - 设置时间轴
	   @param map
	   		loginUserId  当前用户Id
	   @param uTeam
	   		uTeam 对象
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	private void setBehaviorTypeParam(HashMap<String, String> map, UTeam uTeam,UUser uUser) throws Exception {
		//用户首次加入球队
		if (publicService.StringUtil(map.get("loginUserId"))) {
			//设置时间轴
			String userOrTeamType = "1";
			String behaviorType = "3";
			String objectType = null;
			map.put("teamId", uTeam.getTeamId());
			UTeamImg uTeamImg = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and imgSizeType='1' and timgUsingType='1'");
			if (null != uTeamImg) {
				objectType = uTeamImg.getImgurl();
			}
			setBehaviorType(userOrTeamType,objectType, behaviorType, uTeam, map,null,uUser);
		}
	}

	/**
	 * 
	 * 
	   TODO - 球员加入球队时，修改球队信息
	   @param uTeam
	   @return
	   2016年2月1日
	   dengqiuru
	 * @throws Exception 
	 */
	private UTeam updateTeamInfo(HashMap<String, String> map) throws Exception {
		UTeam uTeam = baseDAO.get(UTeam.class, map.get("teamId"));
		uTeam.setTeamId(uTeam.getTeamId());
		uTeam.setTeamCount(uTeam.getTeamCount() + 1);
		if (uTeam.getTeamCount() > uTeam.getHistoryCount()) {
			uTeam.setHistoryCount(uTeam.getTeamCount());
		}
		baseDAO.update(uTeam);
		return uTeam;
	}
	
	/**
	 * 
	 * 
	   TODO - 计算并更新平均体重，平均身高，平均年龄
	   @param players
	   2016年2月15日
	   dengqiuru
	 * @throws Exception 
	 */
	public void updateAvgWHAge(List<UPlayer> players,UTeam uTeam) throws Exception {
		DecimalFormat df2  = new DecimalFormat("###.0");
		if (null != uTeam) {
			uTeam.setTeamId(uTeam.getTeamId());
			Double weight = 0.0;//总体重
			Double height = 0.0;//总身高
			Double age = 0.0;//总年龄
			Double AvgWeight = 0.0;//平均体重
			Double AvgHeight = 0.0;//平均身高
			Double AvgAge = 0.0;//平均年龄
			int count = 0;
			if (uTeam.getTeamCount() != null) {
				count = uTeam.getTeamCount();//人数
			}
			if (null != players && players.size() > 0) {
				for (UPlayer uPlayer : players) {
					if (null != uPlayer.getUUser()) {
						if (uPlayer.getUUser().getWeight() != null && !"".equals(uPlayer.getUUser().getWeight())) {
							weight += Double.parseDouble(uPlayer.getUUser().getWeight());
						}else{
							weight += 0;
						}
						//身高总和
						if (uPlayer.getUUser().getHeight() != null && !"".equals(uPlayer.getUUser().getHeight())) {
							height += Double.parseDouble(uPlayer.getUUser().getHeight());
						}else{
							height += 0;
						}
						//年龄总和
						if (uPlayer.getUUser().getAge() != null) {
							age += uPlayer.getUUser().getAge();
						}else{
							if (null != uPlayer.getUUser().getBirthday()) {
								age += this.getAgeByBirthday(uPlayer.getUUser().getBirthday());
							}else{
								age += 0;
							}
						}
					}
				}
			}
			if (0 == count ) {
				AvgWeight = 0.0;
				AvgHeight = 0.0;
				AvgAge = 0.0;
			}else{
				AvgWeight = weight/count;
				AvgHeight = height/count;
				AvgAge = age/count;
			}
			uTeam.setAvgHeight(df2.format(AvgHeight));//平均身高
			uTeam.setAvgWeight(df2.format(AvgWeight));//平均体重
			uTeam.setAvgAge(df2.format(AvgAge));//平均年龄

			//球队类型 : 1=青年队（25岁以下）、2=壮年队（25-35岁）、3=中年队（36-45岁）、4=老年队(45岁以上)
			String teamType = updateUTeamType(AvgAge.intValue());
			uTeam.setTeamType(teamType);
			baseDAO.update(uTeam);
		}
	}




	/**
	 * 
	 * 
	   TODO - 我的球队信息
	   @param map
	   		loginUserId   当前用户
	   @return
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> myTeamInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> listTeam=new ArrayList<HashMap<String, Object>>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		String uuserUteamStatus = "5";
		if (null != map.get("loginUserId") || !"".equals(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			//sql
			StringBuffer sql = this.myTeamInfo_sql;
			//分页
			List<Object> listTeamId = new ArrayList<Object>();
			if(null != map.get("page")&& !"".equals(map.get("page"))){
				listTeam = this.getPageLimit(sql.toString(), map, hashMap,null);
				if (null != listTeam && listTeam.size() > 0) {
					for (HashMap<String, Object> hashMap2 : listTeam) {
//						//填充角色
//						uPlayerRoleService.setMembertype202(hashMap2);
						//角标需要的id
						String teamId = hashMap2.get("teamId").toString();
						listTeamId.add(teamId);
					}
				}
				//角标
				if (null != listTeamId && listTeamId.size() > 0) {
					cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
				}

				//用户状态
				//查询当前球员是否有资格可以创建球队
				HashMap<String, String> statusMap = this.checkUuserUteamStatus(map);
				uuserUteamStatus = statusMap.get("uuserUteamStatus");
			}
		}
		resultMap.put("uTeamList", listTeam);
		resultMap.put("uuserUteamStatus", uuserUteamStatus);
		resultMap.put("cornerList", cornerList);
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 队长踢人
	   @param map
	   	userId 踢人的用户Id
	   	playerId  被踢人的球员Id
	   	teamId	踢人的队伍id
	   @return
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> excludeByTeamLeader(HashMap<String, String> map) throws Exception {
		if (WebPublicMehod.intAppCode(map.get("appCode")) < 203) {
			return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
		}
		HashMap<String, Object> resultMap = new HashMap<>();//返回集合
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
			if (null != uTeam) {
				if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
					HashMap<String, Object> teamStatusMap = this.checkAllUTeamStatus(map);
					if ("3".equals(teamStatusMap.get("success").toString())) {
						return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
					}
				}
				//查找踢人的用户是否有权限
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
				map.put("type", "1");
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
				if (null != playerIds && playerIds.size() > 0) {//有权限
					//判断球员是否为该队球员
					String findType = "2";
					UPlayer uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map, findType);
					if (null != uPlayer) {//为该队球员
						if("2".equals(uPlayer.getInTeam())){
							return WebPublicMehod.returnRet("error","你所选择的队员已经离队，无法继续操作");
						}
						//判断该球队是否为唯一管理球员
						Boolean isUnique = uPlayerRoleService.isUniqueByuserId(uPlayer);
						Boolean isExculde=true;
						if (false == isUnique) {
							List<HashMap<String, Object>> listPlayerRoleLimit=null;
							Integer lvl1=uplayerService.getTeamIdAndUserIdPlayer(map);//登录人权限等级
							Integer lvl2=0;
							lvl2=uplayerService.getPlayerIdPlayer(map,listPlayerRoleLimit,lvl2);;//球员列表中球员等级
							if(lvl1 != 0 && lvl2 != 0){
								if(lvl1==3){
									isExculde=false;
								}else{
									if(lvl1>=lvl2){//＝同级 >低级
										isExculde=false;
									}
								}
							}else{
								isExculde=false;
							}
							if(!isExculde){
								return WebPublicMehod.returnRet("error","你的权限不足\n暂时无法踢出球员！");
							}
							
							if (null != uPlayer.getUUser()) {
								if (!map.get("loginUserId").equals(uPlayer.getUUser().getUserId())) {
									String exitType = "2";//1-自己退队 2-队长踢出 3-球队解散
									uPlayer = uplayerService.exitTeam(uPlayer,exitType);
									//修改球队人数、各种平均值？
									uTeam = updateTeamCount(uTeam);
									uplayerService.updateTeamAvg(uPlayer.getUUser());
									//通知
									String messageType = "2";//队长踢人
									this.setMessageParams(map, uPlayer, uTeam, messageType);
									//退出清空邀请记录
									uInviteteamService.delInviteStatus(map, uPlayer, uTeam);
									resultMap.put("success", "踢人成功！");
								}else{
									return WebPublicMehod.returnRet("error","队长不能踢出队长！");
								}
							}else{
								String exitType = "2";//1-自己退队 2-队长踢出 3-球队解散
								uPlayer = uplayerService.exitTeam(uPlayer,exitType);
//									//修改球队人数、各种平均值？
								uTeam = updateTeamCount(uTeam);
								resultMap.put("success", "踢人成功！");
							}
						}else{
							return WebPublicMehod.returnRet("error","当前球员存在球队管理角色\n你不能将当前球员踢出球队");
						}
					}else {//不为该队球员
						return WebPublicMehod.returnRet("error","该球员不为当前球队球员！");
					}
				}else{
					return WebPublicMehod.returnRet("error","你的权限不足\n暂时无法踢出球员");
				}
			}else{
				return WebPublicMehod.returnRet("error","队伍不存在！");
			}
		}
		return resultMap;
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
	private void setMessage(HashMap<String, String> map, String mesType, String msg, String userId, String params,String repetition) throws Exception {
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
	   TODO - 队长踢人或自己退队时，球队人数减一并重新计算平均值
	   @param uTeam
	   @throws Exception
	   2016年2月29日
	   dengqiuru
	 */
	private UTeam updateTeamCount(UTeam uTeam) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//参数集合
		uTeam.setTeamCount(uTeam.getTeamCount() - 1);
		baseDAO.update(uTeam);
		//计算平均体重，平均身高，平均年龄
		hashMap.put("teamId", uTeam.getTeamId());
		List<UPlayer> uPlayers = uplayerService.getPlayerListByTeamId(hashMap);
		updateAvgWHAge(uPlayers,uTeam);
		return uTeam;
	}

	/**
	 * 
	 * 
	   TODO - 退出队伍 
	   @param map
	   		teamId  队伍Id
	   @return
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> exitTeamByUserId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回集合
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
			if (null != uTeam) {
				if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
					HashMap<String, Object> teamStatusMap = this.checkAllUTeamStatus(map);
					if ("3".equals(teamStatusMap.get("success").toString())) {
						return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
					}
				}
				//判断球员是否为该队球员
				String typeStr = "1";
				UPlayer uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map, typeStr);
				if (null != uPlayer) {//为该队球员
					if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
						if (publicService.StringUtil(uPlayer.getExitType())) {
							if ("3".equals(uPlayer.getExitType())) {
								return WebPublicMehod.returnRet("error","-2^<}}球队已解散！");
							}else if ("2".equals(uPlayer.getExitType())) {
								return WebPublicMehod.returnRet("error","-2^<}}你已经被队长踢出球队，不能执行此操作！");
							}
						}
					}
					//判断该球队是否为唯一管理球员
//					Boolean isUnique = uPlayerRoleService.isUniqueByuserId(uPlayer);
					String memberTypeStr = uPlayerRoleService.memberTypeStrByPlayerId(uPlayer);
					if (StringUtils.isEmpty(memberTypeStr)) {
						//通知队长
						String exitType = "1";//1-自己退队 2-队长踢出 3-球队解散
						uPlayer = uplayerService.exitTeam(uPlayer,exitType);
						uTeam = updateTeamCount(uTeam);//更新球员人数和平均值
						String messageType = "3";//退队通知
						this.setMessageParams(map, uPlayer, uTeam, messageType);
						//退出清空邀请记录
						uInviteteamService.delInviteStatus(map, uPlayer, uTeam);
						//更新球员lbs加队数
						uuserService.setAreaToBaiduLBS(uuserService.getUserinfoByUserId(map));
						resultMap.put("code", "1");
						String uteamName = this.getRealName(uTeam);
						resultMap.put("success", "你已正式退出‘"+uteamName+"’,你可以建立自己的球队或向其他球队递交转会申请");
					}else{
						resultMap.put("code", "2");
						resultMap.put("success", "球队核心角色无法退队，你可将职位转让给队友后再做操作");
					}
				}else {//不为该队球员
					return WebPublicMehod.returnRet("error","你不是当前球队球员！");
				}
			}else{
				return WebPublicMehod.returnRet("error","队伍不存在！");
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 将赛事传过来的球队名的分类去掉，并返回
	   @param map
	   		teamName		球队名称
	   @return
	   2016年7月13日
	   dengqiuru
	 * @throws Exception 
	 */
	private String getRealName(UTeam uTeam) throws Exception {
		String teamClassName1 = null;
		if (publicService.StringUtil(uTeam.getTeamClass())) {
			teamClassName1 = Public_Cache.HASH_PARAMS("team_class").get(uTeam.getTeamClass());
		}
		//队名
		String uteamName = uTeam.getName();
		if (null == uteamName || "".equals(uteamName)) {
			uteamName = uTeam.getShortName();
		}
		//获取赛事的球队名
		String teamRealName = uteamName;
		//查询这些球队名是否包含球队分类
		if (publicService.StringUtil(teamRealName)) {
			if (null != teamClassName1) {
				if(teamRealName.contains(teamClassName1)){
					uteamName = teamRealName;
				}else{
					uteamName = teamRealName + teamClassName1;
				}
			}
		}
		return uteamName;
	}
	/**
	 * 
	 * 
	   TODO - 天梯列表头部 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年4月4日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findUteamListHead(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回集合
		OutUteamList outUteamList = new OutUteamList();
		UTeam uTeam = null;
		String isNull = "2";
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uTeam = uplayerService.getUTeaminfoforuserId(map);
			if (null != uTeam) {
				if ("3".equals(uTeam.getTeamUseStatus())) {
					uTeam = null;
				}else{
					//获取胜率
					if (null != uTeam) {
						map.put("userId", map.get("loginUserId"));
						outUteamList = setOutUTeamList(uTeam, map);
						//填充球队信息的身份
						outUteamList = uPlayerRoleService.setMemberTypeByGetUTeaminfo202(outUteamList,map);
						if (publicService.StringUtil(outUteamList.getMemberType())) {
							if ("1".equals(outUteamList.getMemberType()) || "1".equals(outUteamList.getMemberType2())) {
								if (null == outUteamList.getNumber() || "".equals(outUteamList.getNumber())) {
									isNull = "1";
								}else if (null == outUteamList.getPosition() || "".equals(outUteamList.getPosition())) {
									isNull = "1";
								}
							}
						}
						outUteamList.setIsNull(isNull);
					}
				}
			}
		}
		//分页
		List<Object> listHeadTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerHeadList = new ArrayList<List<CornerBean>>();
		if (null != outUteamList) {
			if (publicService.StringUtil(outUteamList.getTeamId())) {
				listHeadTeamId.add(outUteamList.getTeamId());
				cornerHeadList = cornerService.getAllTeamCornerList(map, listHeadTeamId);
			}
		}
		resultMap.put("cornerHeadList", cornerHeadList);
		resultMap.put("outUteamList", outUteamList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 战队列表头部及天梯头部,还有个人中心头部信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户
	   @return
	   		uPlayer/uTeam 的hashMap <String,Object>
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	@Override
	public OutUteamList findUteaminfoListHead(HashMap<String, String> map) throws Exception {
		OutUteamList outUteamList = new OutUteamList();
		UTeam uTeam = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uTeam = uplayerService.getUTeaminfoforuserId(map);
				//获取胜率
				if (null != uTeam) {
					map.put("userId", map.get("loginUserId"));
					outUteamList = setOutUTeamList(uTeam, map);
				}
		}
		return outUteamList;
	}

	/**
	 * 
	 * @TODO 获取球队里程碑字段详细
	 * @Title: getLCBJsonStr 
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月8日 下午3:56:35
	 */
	public String getUTeamBehaviorJsonStr(UTeamBehavior uTeamBehavior) throws Exception {
		String jsonstr = "";
		
		if(uTeamBehavior.getTeamBehaviorType().equals("1")){//首次建队
			UTeam uTeam2 = baseDAO.get(UTeam.class,uTeamBehavior.getObjectId());
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			if(null!=uTeam2.getUUser()){
				hashMap.put("userId", uTeam2.getUUser().getUserId());
				hashMap.put("left","由");
				UUserImg userImg = uUserImgService.getHeadPicNotSetByuserId(hashMap);
				if(userImg!=null){
					hashMap.put("img",userImg.getImgurl());
				}else{
					hashMap.put("img","");
				}
				hashMap.put("name",uTeam2.getUUser().getUsername());
				if(null!=uTeam2.getuRegion()&&!"".equals(uTeam2.getuRegion().getName())){
					hashMap.put("right","组织成立于"+uTeam2.getuRegion().getName());
				}else{
					hashMap.put("right","组织成立");
				}
			}
			
			jsonstr = JSON.toJSONString(hashMap);
			
		}else if(uTeamBehavior.getTeamBehaviorType().equals("14")){//首次成功响应约战
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			UDuel uDuel = baseDAO.get(UDuel.class, uTeamBehavior.getObjectId());
			if(uDuel!=null){
				//发起方
				if(null!=uDuel.getUTeam()){
					hashMap.put("fname",uDuel.getUTeam().getName());
					hashMap.put("teamId",uDuel.getUTeam().getTeamId());
					UTeamImg uImg = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImg!=null){
						hashMap.put("fimg",uImg.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				}else{
					hashMap.put("fname","");
					hashMap.put("fimg","");
				}
			}else{
				hashMap.put("fname","");
				hashMap.put("fimg","");
			}
			//响应方
			UDuelResp uResp = baseDAO.get(UDuelResp.class, uTeamBehavior.getObjectId());
			if(uResp!=null){
				if(null!=uResp.getUTeam()){
					hashMap.put("xname",uResp.getUTeam().getName());
					hashMap.put("teamId",uResp.getUTeam().getTeamId());
					UTeamImg uImg = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImg!=null){
						hashMap.put("ximg",uImg.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
				}else{
					hashMap.put("xname","");
					hashMap.put("ximg","");
				}
			}else{
				hashMap.put("xname","");
				hashMap.put("ximg","");
			}
			hashMap.put("center"," VS ");
			jsonstr = JSON.toJSONString(hashMap);
			
		}else if(uTeamBehavior.getTeamBehaviorType().equals("7")){//首次约战胜利
			HashMap<String, String> hashMap = new HashMap<String, String>();
			//根据比赛id 获取具体获胜的场次
			UDuelBs uBs = baseDAO.get(UDuelBs.class, uTeamBehavior.getObjectId());
			if(null!=uBs){
				//发起方
				if(null!=uBs.getUTeam()){
					hashMap.put("fname",uBs.getUTeam().getName());
					hashMap.put("teamId",uBs.getUTeam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				}else{
					hashMap.put("fname","");
					hashMap.put("fimg","");
				}
				
				//响应方
				if(null!=uBs.getXUTeam()){
					hashMap.put("xname",uBs.getXUTeam().getName());
					hashMap.put("teamId",uBs.getXUTeam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
				}else{
					hashMap.put("xname","");
					hashMap.put("ximg","");
				}
				hashMap.put("center"," "+uBs.getFgoal()+":"+uBs.getKgoal()+" ");
				hashMap.put("details","");
			}else{
				hashMap.put("fname","");
				hashMap.put("fimg","");
				hashMap.put("xname","");
				hashMap.put("ximg","");
				hashMap.put("center","");
				hashMap.put("details","");
			}
			
			//发起方
//			UDuel uDuel = baseDAO.get(UDuel.class, uTeamBehavior.getObjectId());
//			if(uDuel!=null){
//				hashMap.put("fname",uDuel.getUTeam().getName());
//				hashMap.put("teamId",uDuel.getUTeam().getTeamId());
//				UTeamImg uImg = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
//				if(uImg!=null){
//					hashMap.put("fimg",uImg.getImgurl());
//				}else{
//					hashMap.put("fimg","");
//				}
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//			}
//			//响应方
//			UDuelResp uResp = baseDAO.get(UDuelResp.class, uTeamBehavior.getObjectId());
//			if(uResp!=null){
//				hashMap.put("xname",uResp.getUTeam().getName());
//				hashMap.put("teamId",uResp.getUTeam().getTeamId());
//				UTeamImg uImg = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
//				if(uImg!=null){
//					hashMap.put("ximg",uImg.getImgurl());
//				}else{
//					hashMap.put("ximg","");
//				}
//			}else{
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//			}
//			
//			//判断是否有多场比赛
//			hashMap.put("duelId",uTeamBehavior.getObjectId());
//			List<UDuelBs> uBsLsit = baseDAO.find(hashMap," from UDuelBs where UDuel.duelId = :duelId ");
//			if(uBsLsit.size()>1){
//				hashMap.put("center"," VS ");
//				String details = "";
//				
//				HashMap<String, String> hashMap2 = new HashMap<String, String>();
//				//分别显示每场比分
//				for(int i=0;i<uBsLsit.size();i++){
//					
//					details = details + "第"+(i+1)+"场 "+uBsLsit.get(i).getFgoal()+" : "+uBsLsit.get(i).getKgoal()+"    ";
////					hashMap2.put("bfName", "第"+(i+1)+"场 ");
////					hashMap2.put("bf", uBsLsit.get(i).getFgoal()+":"+uBsLsit.get(i).getKgoal());
////					details = JSON.toJSONString(hashMap2);
//				}
//				hashMap.put("details",details);
//			}else if(uBsLsit.size()==1){
//				//1.一场比赛
//				hashMap.put("center"," "+uBsLsit.get(0).getFgoal()+":"+uBsLsit.get(0).getKgoal()+" ");
//				
//			}else{
//				//没有比赛场次
//				hashMap.put("center"," VS ");
//			}
			jsonstr = JSON.toJSONString(hashMap);
			
		}else if(uTeamBehavior.getTeamBehaviorType().equals("15")){//首次成功响应挑战
			HashMap<String, String> hashMap = new HashMap<String, String>();
			UChallenge uBs = baseDAO.get(UChallenge.class, uTeamBehavior.getObjectId());
			
			if(uBs!=null){
				//发起方
				if(null!=uBs.getFteam()){
					hashMap.put("fname",uBs.getFteam().getName());
					hashMap.put("teamId",uBs.getFteam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				}else{
					hashMap.put("fname","");
					hashMap.put("fimg","");
				}
				
				//响应方
				if(null!=uBs.getXteam()){
					hashMap.put("xname",uBs.getXteam().getName());
					hashMap.put("teamId",uBs.getXteam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
				}else{
					hashMap.put("xname","");
					hashMap.put("ximg","");
				}
			}else{
				hashMap.put("fname","");
				hashMap.put("fimg","");
				hashMap.put("xname","");
				hashMap.put("ximg","");
			}
			hashMap.put("center"," VS ");
			jsonstr = JSON.toJSONString(hashMap);
			
		}else if(uTeamBehavior.getTeamBehaviorType().equals("16")){//首次挑战胜利
			HashMap<String, String> hashMap = new HashMap<String, String>();
			//根据比赛ID 获得比赛小场次
			UChallengeBs uBs = baseDAO.get(UChallengeBs.class,uTeamBehavior.getObjectId());
			if(null!=uBs){
				//发起方
				if(null!=uBs.getBsFteam()){
					hashMap.put("fname",uBs.getBsFteam().getName());
					hashMap.put("teamId",uBs.getBsFteam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				}else{
					hashMap.put("fname","");
					hashMap.put("fimg","");
				}
				
				//响应方
				if(null!=uBs.getBsXteam()){
					hashMap.put("xname",uBs.getBsXteam().getName());
					hashMap.put("teamId",uBs.getBsXteam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
				}else{
					hashMap.put("xname","");
					hashMap.put("ximg","");
				}
				
				hashMap.put("center"," "+uBs.getFqGoal()+":"+uBs.getXyGoal()+" ");
				hashMap.put("details","");
			}else{
				hashMap.put("fname","");
				hashMap.put("fimg","");
				hashMap.put("xname","");
				hashMap.put("ximg","");
				hashMap.put("center","");
				hashMap.put("details","");
			}
			
//			UChallenge uBs = baseDAO.get(UChallenge.class, uTeamBehavior.getObjectId());
//			
//			if(uBs!=null){
//				//发起方
//				hashMap.put("fname",uBs.getFteam().getName());
//				hashMap.put("teamId",uBs.getFteam().getTeamId());
//				UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
//				if(uImgf!=null){
//					hashMap.put("fimg",uImgf.getImgurl());
//				}else{
//					hashMap.put("fimg","");
//				}
//				//响应方
//				hashMap.put("xname",uBs.getXteam().getName());
//				hashMap.put("teamId",uBs.getXteam().getTeamId());
//				UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
//				if(uImgx!=null){
//					hashMap.put("ximg",uImgx.getImgurl());
//				}else{
//					hashMap.put("ximg","");
//				}
//				
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//			}
//			
//			//判断是否有多场比赛
//			hashMap.put("challengeId",uTeamBehavior.getObjectId());
//			List<UChallengeBs> uBsLsit = baseDAO.find(hashMap," from UChallengeBs where UChallenge.challengeId = :challengeId ");
//			if(uBsLsit.size()>1){
//				hashMap.put("center"," VS ");
//				String details = "";
//				HashMap<String, String> hashMap2 = new HashMap<String, String>();
//				//分别显示每场比分
//				for(int i=0;i<uBsLsit.size();i++){
//					details = details + "第"+(i+1)+"场 "+uBsLsit.get(i).getFqGoal()+" : "+uBsLsit.get(i).getXyGoal()+"    ";
////					hashMap2.put("bfName", "第"+(i+1)+"场 ");
////					hashMap2.put("bf", uBsLsit.get(i).getFqGoal()+":"+uBsLsit.get(i).getXyGoal());
////					details = JSON.toJSONString(hashMap2);
//				}
//				hashMap.put("details",details);
//			}else if(uBsLsit.size()==1){
//				//1.一场比赛
//				hashMap.put("center"," "+uBsLsit.get(0).getFqGoal()+":"+uBsLsit.get(0).getXyGoal()+" ");
//				hashMap.put("details","");
//			}else{
//				//没有比赛场次
//				hashMap.put("center"," VS ");
//				hashMap.put("details","");
//			}
			
			jsonstr = JSON.toJSONString(hashMap);
		}else if(uTeamBehavior.getTeamBehaviorType().equals("11")){//赛事
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("behaviorId", uTeamBehavior.getKeyId());
			UBehaviorInfo uInfo = baseDAO.get(hashMap,"from UBehaviorInfo where behaviorId = :behaviorId ");
			if(null!=uInfo){
				if("1".equals(uInfo.getEventsType())){
					hashMap.put("details", "报名参赛");
				}else{
					String details = uInfo.getEventsTypeName() +"  "+uInfo.getScore()+"分 "+uInfo.getWcount()+"胜 "+uInfo.getPcount()+"平 "+uInfo.getScount()+"败 得"+uInfo.getGintegral()+" 失"+uInfo.getSintegral();
					hashMap.put("details", details);
				}
			}else{
				hashMap.put("details","");
			}
			
		}
		
		return jsonstr;
	}	
	
	
	/**
	 * 
	 * 
	   TODO - 战队详情   概况【2.0.0.1】
	   @param map
	   		teamId
	   @return
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> roughlyStateOfUteam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
			if (null != uTeam) {
				this.settUteamChances(uTeam);//设置球队胜率和队徽
				
				List<HashMap<String, Object>> outUTeamImgs = new ArrayList<HashMap<String, Object>>();
				outUTeamImgs = uteamImgService.getGalleryListInroughly(map);//获取球队相册
				resultMap.put("outUTeamImgs", outUTeamImgs);
				//战队球场轴
				map.put("XteamId", map.get("teamId"));
				List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
				ubCourtMap = this.getBrCourtlist(map);
				resultMap.put("ubCourtMap", ubCourtMap);
				//2:获取战队时间轴 【里程碑】
//				List<UTeamBehavior> uTeamBehaviors =  new ArrayList<UTeamBehavior>();
				List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<HashMap<String, Object>>();
				map.put("type", "2");
				
				//从缓存获取里程碑LIST
//				List<Object> objectList = publicService.getBehaviorList(map);
//				
//				if (null != objectList && objectList.size() > 0) {
//					for (Object object : objectList) {
//						UTeamBehavior uTeamBehavior = (UTeamBehavior)object;
//						uTeamBehavior.setuTeam(null);
//						if (null != uTeamBehavior.getRemark() && !"".equals(uTeamBehavior.getRemark())) {
//							uTeamBehavior.setTeamBehaviorTypeName(uTeamBehavior.getRemark());
//						}else{
//							uTeamBehavior.setTeamBehaviorTypeName(Public_Cache.HASH_PARAMS("team_behavior_type").get(uTeamBehavior.getTeamBehaviorType()));
//						}
//						//里程碑字段详细
//						uTeamBehavior.setJsonStr(this.getUTeamBehaviorJsonStr(uTeamBehavior));	
//						
//						uTeamBehaviors.add(uTeamBehavior);
//					}
//					resultMap.put("outuTeamBehaviorlist", uTeamBehaviors);
//				}else{
//					map.put("roughly","1");
//					//从数据库取
//					uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(map);
//					resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
//				}
				//里程碑从数据库取 
				//里程碑限制标识
				map.put("roughly","1");
				uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(map);
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
				
				//获取球队成员用户信息及头像
				List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
				outPlayerLists = uplayerService.getUplayerListInRoughly(map);
				resultMap.put("outPlayerLists", outPlayerLists);
				resultMap.put("outPlayerListsSize", uplayerService.getUplayerListInRoughlySize(map));
				
				//获取荣誉墙
				List<Map<String, Object>> honors = this.getUteamHonorByGK(map);
				resultMap.put("outHonorsLists", honors);
			
				//判断是否是自己的球队
				map = this.setUTeamMyself(uTeam,map);
				//是否是自己的球队
				resultMap.put("isMyself", map.get("isMyself"));
				//是否是队长
				resultMap.put("isTeamLeader", map.get("isTeamLeader"));
			}
		}else{
			return WebPublicMehod.returnRet("error","teamId不能为空！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 对手【2.0.0.1】
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> competitorOfUteam(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, Object> resultAllMap = new HashMap<>();//输出
		if (publicService.StringUtil(hashMap.get("appResouce"))) {
			if ("2".equals(hashMap.get("appResouce"))) {
				resultAllMap = this.competitorOfUteam202(hashMap);
			}
		}else{
			resultAllMap = this.competitorOfUteam200(hashMap);
		}
		return resultAllMap;
	}
	/**
	 * 
	 * 
	   TODO - 2.0.2对手接口
	   @param hashMap2
	   		teamId
	   @return
	   2016年6月24日
	   dengqiuru
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> competitorOfUteam202(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, Object> resultAllMap = new HashMap<>();//输出
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(hashMap.get("teamIds"))) {
			String[] teamid = hashMap.get("teamIds").split(",");
			for (int i = 0; i < teamid.length; i++) {
				resultMap = new HashMap<>();
				if (publicService.StringUtil(teamid[i])) {
					hashMap.put("XteamId",teamid[i]);
					hashMap.put("teamId",teamid[i]);
					UTeam uTeam = baseDAO.get(UTeam.class, hashMap.get("XteamId"));
					this.settUteamChances(uTeam);//设置球队胜率和队徽
					//对手
					List<HashMap<String, Object>> competitor= new ArrayList<>();
					competitor = this.getCompetitor(hashMap);
					resultMap.put("competitor", competitor);
					
					//获取球队成员用户信息及头像
					List<HashMap<String, Object>> outPlayerLists = new ArrayList<>();
					outPlayerLists = uplayerService.getUplayerListInCompetitor(hashMap);
					resultMap.put("outPlayerLists", outPlayerLists);
					
					List<Object> gList= null;
					List<String> remarkList = new ArrayList<String>();
					List<String> urlList = new ArrayList<String>();
					List<HashMap<String, Object>> gList1= new ArrayList<>();
					HashMap<String, Object> cMap = new HashMap<>();
					if ("0".equals(hashMap.get("objectType"))) {//约战
						hashMap.put("duelId", hashMap.get("objectId"));
						gList= uDuelService.getDuel(hashMap);
						if (null != gList && gList.size() > 0) {
							for (Object hashMap1 : gList) {
								HashMap<String, Object> map1 = (HashMap<String, Object>)hashMap1;
								if (null != map1.get("remark")) {
									remarkList.add(map1.get("remark").toString());
								}
								urlList.add(map1.get("imgurl").toString());
								gList1.add(cMap);
							}
							if (null != remarkList) {
								cMap.put("remark", remarkList.get(0));
							}
							cMap.put("imgList", urlList);
						}
					}else if("3".equals(hashMap.get("objectType"))) {//挑战
						hashMap.put("challengeId", hashMap.get("objectId"));
						HashMap<String, Object> hMap= uchallengeService.getChallengeOtherInfo(hashMap);
						if (null != hMap) {
							List<UDuelChallengeImg> imgList = (List<UDuelChallengeImg>) hMap.get("imgList");
							if (null != imgList && imgList.size() > 0) {
								for (UDuelChallengeImg uDuelChallengeImg : imgList) {
									if (null != uDuelChallengeImg) {
										if (null != uDuelChallengeImg.getImgurl()) {
											urlList.add(uDuelChallengeImg.getImgurl());
										}
									}
								}
							}
							cMap.put("remark", hMap.get("remark"));
						}else{
							cMap.put("remark", null);
						}
						cMap.put("imgList", urlList);
					}else if("2".equals(hashMap.get("objectType"))) {//赛事
						cMap.put("imgList", null);
					}else{//其他
						cMap.put("imgList", null);
					}
					resultMap.put("outUTeamImgs", cMap);
					//战队球场轴
					List<OutUbCourtMap> ubCourtMap = new ArrayList<>();
					ubCourtMap = getBrCourtlist(hashMap);
					resultMap.put("ubCourtMap", ubCourtMap);
					//2:获取战队时间轴
					List<UTeamBehavior> uTeamBehaviors = new ArrayList<>();
					List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<>();
					hashMap.put("type", "2");
					hashMap.put("teamId", hashMap.get("XteamId"));
					
					List<Object> objectList = publicService.getBehaviorList(hashMap);
					if (null != objectList && objectList.size() > 0) {
						for (Object object : objectList) {
							UTeamBehavior uTeamBehavior = (UTeamBehavior)object;
							uTeamBehavior.setuTeam(null);
							uTeamBehavior.setTeamBehaviorTypeName(Public_Cache.HASH_PARAMS("team_behavior_type").get(uTeamBehavior.getTeamBehaviorType()));
							uTeamBehaviors.add(uTeamBehavior);
						}
						resultMap.put("outuTeamBehaviorlist", uTeamBehaviors);
					}else{
						uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(hashMap);
						resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
					}
				
					//判断是否是自己的球队
					hashMap = this.setUTeamMyself(uTeam,hashMap);
					//是否是自己的球队
					resultMap.put("isMyself", hashMap.get("isMyself"));
					//是否是队长
					resultMap.put("isTeamLeader", hashMap.get("isTeamLeader"));
					//对手角标
					List<Object> listTeamId = new ArrayList<Object>();
					List<List<CornerBean>> cornerList = new ArrayList<>();
					String teamId = hashMap.get("XteamId");
					listTeamId.add(teamId);
					if (null != listTeamId && listTeamId.size() > 0) {
						cornerList = cornerService.getAllTeamCornerList(hashMap, listTeamId);
					}
					//站队列表用户信息
					resultMap.put("cornerList", cornerList);

				}
				resultAllMap.put("Team"+i, resultMap);
				
			}
		}
		return resultAllMap;
	}

	/**
	 * 
	 * 
	   TODO - 2.0.2之前对手
	   @param hashMap
	   @return
	   @throws Exception
	   2016年6月24日
	   dengqiuru
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> competitorOfUteam200(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(hashMap.get("teamId"))) {
			hashMap.put("XteamId", hashMap.get("teamId"));
			UTeam uTeam = baseDAO.get(UTeam.class, hashMap.get("XteamId"));
			this.settUteamChances(uTeam);//设置球队胜率和队徽
			//对手
			List<HashMap<String, Object>> competitor= new ArrayList<HashMap<String, Object>>();
			competitor = this.getCompetitor(hashMap);
			resultMap.put("competitor", competitor);
			
			//获取球队成员用户信息及头像
			List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
			outPlayerLists = uplayerService.getUplayerListInCompetitor(hashMap);
			resultMap.put("outPlayerLists", outPlayerLists);
			
			List<Object> gList= null;
			List<String> remarkList = new ArrayList<String>();
			List<String> urlList = new ArrayList<String>();
			List<HashMap<String, Object>> gList1=  new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> cMap = new HashMap<>();
			if ("0".equals(hashMap.get("objectType"))) {//约战
				hashMap.put("duelId", hashMap.get("objectId"));
				gList= uDuelService.getDuel(hashMap);
				if (null != gList && gList.size() > 0) {
					for (Object hashMap1 : gList) {
						HashMap<String, Object> map1 = (HashMap<String, Object>)hashMap1;
						if (null != map1.get("remark")) {
							remarkList.add(map1.get("remark").toString());
						}
						urlList.add(map1.get("imgurl").toString());
						gList1.add(cMap);
					}
					if (null != remarkList) {
						cMap.put("remark", remarkList.get(0));
					}
					cMap.put("imgList", urlList);
				}
			}else if("1".equals(hashMap.get("objectType"))) {//挑战
				hashMap.put("challengeId", hashMap.get("objectId"));
				HashMap<String, Object> hMap= uchallengeService.getChallengeOtherInfo(hashMap);
				if (null != hMap) {
					List<UDuelChallengeImg> imgList = (List<UDuelChallengeImg>) hMap.get("imgList");
					if (null != imgList && imgList.size() > 0) {
						for (UDuelChallengeImg uDuelChallengeImg : imgList) {
							if (null != uDuelChallengeImg) {
								if (null != uDuelChallengeImg.getImgurl()) {
									urlList.add(uDuelChallengeImg.getImgurl());
								}
							}
						}
					}
					cMap.put("remark", hMap.get("remark"));
				}else{
					cMap.put("remark", null);
				}
				cMap.put("imgList", urlList);
			}else if("2".equals(hashMap.get("objectType"))) {//赛事
				cMap.put("imgList", null);
			}else{//其他
				cMap.put("imgList", null);
			}
			resultMap.put("outUTeamImgs", cMap);
			//战队球场轴
			List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
			ubCourtMap = getBrCourtlist(hashMap);
			resultMap.put("ubCourtMap", ubCourtMap);
			//2:获取战队时间轴
			List<UTeamBehavior> uTeamBehaviors =  new ArrayList<UTeamBehavior>();
			List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<HashMap<String, Object>>();
			hashMap.put("type", "2");
			hashMap.put("teamId", hashMap.get("XteamId"));
			
			List<Object> objectList = publicService.getBehaviorList(hashMap);
			if (null != objectList && objectList.size() > 0) {
				for (Object object : objectList) {
					UTeamBehavior uTeamBehavior = (UTeamBehavior)object;
					uTeamBehavior.setuTeam(null);
					uTeamBehavior.setTeamBehaviorTypeName(Public_Cache.HASH_PARAMS("team_behavior_type").get(uTeamBehavior.getTeamBehaviorType()));
					uTeamBehaviors.add(uTeamBehavior);
				}
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviors);
			}else{
				uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(hashMap);
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
			}
		
			//判断是否是自己的球队
			hashMap = this.setUTeamMyself(uTeam,hashMap);
			//是否是自己的球队
			resultMap.put("isMyself", hashMap.get("isMyself"));
			//是否是队长
			resultMap.put("isTeamLeader", hashMap.get("isTeamLeader"));
			//对手角标
			List<Object> listTeamId = new ArrayList<Object>();
			List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
			String teamId = hashMap.get("XteamId");
			listTeamId.add(teamId);
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(hashMap, listTeamId);
			}
			//站队列表用户信息
			resultMap.put("cornerList", cornerList);
			
		}else{
			resultMap.put("competitor", null);
			resultMap.put("outPlayerLists", null);
			resultMap.put("outUTeamImgs", null);
			resultMap.put("ubCourtMap", null);
			resultMap.put("outuTeamBehaviorlist", null);
			resultMap.put("isMyself", "2");
			resultMap.put("isTeamLeader", "2");
			resultMap.put("cornerList", null);
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 获取对手详细信息
	   @param map
	   		XteamId		球队Id
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getCompetitor(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> competitor = new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("XteamId"))) {
			hashMap.put("XteamId", map.get("XteamId"));
			//查询对手明细
			StringBuffer sql = this.getCompetitor_sql;
			competitor = baseDAO.findSQLMap(sql.toString(), hashMap);
			if (null != competitor && competitor.size() > 0) {
				for (HashMap<String, Object> hashMap2 : competitor) {
					//填充对应的名称
					this.displayData(hashMap2,map);
					//设置胜率
					this.setUteamAllChancesByCompetitor(hashMap2);
					//填充角色
					uPlayerRoleService.setMembertype202(hashMap2);
				}
			}
			
		}
		return competitor;
	}

	/**
	 * 
	 * 
	   TODO - 获取胜率  胜平负
	   @param hashMap2
	   		teamId   球队Id
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setUteamAllChancesByCompetitor(HashMap<String, Object> hashMap2) throws Exception {
		if (null != hashMap2) {
			String ver = "0";
			String draw = "0";
			String fail = "0";
			String chances = "0";
			if (null != hashMap2.get("teamId") && !"".equals(hashMap2.get("teamId")) &&  !"null".equals(hashMap2.get("teamId"))) {//事件类型名称
				//获取胜率
				HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(hashMap2.get("teamId").toString());
				if (null != hashMap) {
					if (null != hashMap.get("chances")) {
						chances = hashMap.get("chances").toString();//胜率
					}
					if (hashMap.get("verMatchCount") != null) {
						ver = hashMap.get("verMatchCount").toString();//总胜场次
					}
					if (hashMap.get("drawMatchCount") != null) {
						draw = hashMap.get("drawMatchCount").toString();//总平场次
					}
					if (hashMap.get("failMatchCount") != null) {
						fail = hashMap.get("failMatchCount").toString();//总负场次
					}
				}
			}
			hashMap2.put("chances",chances);
			hashMap2.put("ver",ver);
			hashMap2.put("draw",draw);
			hashMap2.put("fail",fail);
		}
	}
	/**
	 * 
	 * 
	   TODO - 设置球队胜率和队徽
	   @param uTeam  对象
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void settUteamChances(UTeam uTeam) throws Exception {
		if (null != uTeam) {
			//设置胜率
			setUteamChances(uTeam);
			//设置队徽
			setUTeamImgs(uTeam);
		}
	}

	/**
	 * 
	 * 
	   TODO - 设置是否是自己的队伍
	   @param uTeam  对象
	   @param map
	   		loginUserId   当前用户Id
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private HashMap<String , String> setUTeamMyself(UTeam uTeam, HashMap<String, String> map) throws Exception {
		HashMap<String , String> resultMap = new HashMap<>();
		String isMyself = "2";//是否是我的球队 1:是；2：不是
		String isTeamLeader = "2";//是否是队长  1：是；2：不是
		if (publicService.StringUtil(map.get("loginUserId"))) {
			UPlayer uPlayer = uplayerService.isMyTeam(uTeam,map);
			if (null != uPlayer) {
				isMyself = "1";
				if (publicService.StringUtil(uPlayer.getMemberType())) {
					if ("1".equals(uPlayer.getMemberType())) {
						isTeamLeader = "1";
					}
				}
			}else{
				isMyself = "2";
			}
		}else{
			isMyself = "2";
		}
		resultMap.put("isTeamLeader", isTeamLeader);
		resultMap.put("isMyself", isMyself);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 根球队设置队徽
	   @param uTeam
	   2016年3月11日
	   dengqiuru
	 * @throws Exception 
	 */
	private void setUTeamImgs(UTeam uTeam) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		map.put("teamId", uTeam.getTeamId());
		Set<UTeamImg> uTeamImg = uteamImgService.getTeamLogoByTeamId(map);
		uTeam.setUTeamImgs(uTeamImg);
	}

	/**
	 * 
	 * 
	   TODO - 设置球队胜率、胜平负场次
	   @param uTeam
	   2016年3月11日
	   dengqiuru
	 * @throws Exception 
	 */
	private void setUteamChances(UTeam uTeam) throws Exception {
		HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(uTeam.getTeamId());
		String chances = "0"; 
		if (null != hashMap.get("chances")) {//胜率
			chances = hashMap.get("chances").toString();
		}
		String verMatchCount = "0";
		String drawMatchCount = "0";
		String failMatchCount = "0";
		String allMatchCount = "0";
		if (hashMap.get("verMatchCount") != null) {//胜场次
			verMatchCount = hashMap.get("verMatchCount").toString();
		}
		if (hashMap.get("drawMatchCount") != null) {//平场次
			drawMatchCount = hashMap.get("drawMatchCount").toString();
		}
		if (hashMap.get("failMatchCount") != null) {//负场次
			failMatchCount = hashMap.get("failMatchCount").toString();
		}
		if (hashMap.get("allMatchCount") != null) {//总场次
			allMatchCount = hashMap.get("allMatchCount").toString();
		}
		uTeam.setChances(chances);
		uTeam.setVer(verMatchCount);
		uTeam.setDraw(drawMatchCount);
		uTeam.setFail(failMatchCount);
		uTeam.setEvent(allMatchCount);
	}

	/**
	 * 
	 * 
	   TODO - 查询球场轴  --主场
	   @param map
	   		XteamId  球队Id
	   2016年3月7日
	   dengqiuru
	 * @throws Exception 
	 */
	public List<OutUbCourtMap> getBrCourtlist(HashMap<String, String> map) throws Exception {
		UTeam uTeam = baseDAO.get(UTeam.class,map.get("XteamId"));
		List<OutUbCourtMap> ubCourtMapList = new ArrayList<OutUbCourtMap>();
		if (null != uTeam) {
			if (null != uTeam.getuRegion()) {//区域
				OutUbCourtMap ubCourtMap = this.setUbCourtMap1(uTeam.getuRegion(),map,uTeam);
				ubCourtMapList.add(ubCourtMap);
			}
			if (null != uTeam.getuBrCourt()) {//主场
				OutUbCourtMap ubCourtMap2 = new OutUbCourtMap();
				ubCourtMap2.setIdStr("主场");
				ubCourtMap2.setNameStr(uTeam.getuBrCourt().getName());
				ubCourtMap2.setIconType("2");
				ubCourtMapList.add(ubCourtMap2);
			}
			List<UChampion> uChampionList = uchallengeService.getTeamChList(uTeam.getTeamId());
			if (uChampionList != null && uChampionList.size() > 0) {//擂主
				if (null != uChampionList.get(0).getUBrCourt()) {
					OutUbCourtMap ubCourtMap3 = new OutUbCourtMap();
					ubCourtMap3.setIdStr("擂台");
					ubCourtMap3.setNameStr(uChampionList.get(0).getUBrCourt().getName());
					ubCourtMap3.setIconType("3");
					ubCourtMapList.add(ubCourtMap3);
				}
			}
			
			
			HashMap<String, Object> teamInfo = rankingListService.getPublicRankingInfo(uTeam.getTeamId());
			if(teamInfo!=null){
				//胜 平 负
				OutUbCourtMap ubCourtMap4 = new OutUbCourtMap();
				ubCourtMap4.setIdStr("出征");
				ubCourtMap4.setNameStr(teamInfo.get("allMatchCount").toString()+"场 "+teamInfo.get("verMatchCount").toString()+"胜 "+teamInfo.get("drawMatchCount").toString()+"平 "+teamInfo.get("failMatchCount").toString()+"负");
				ubCourtMap4.setIconType("4");
				ubCourtMapList.add(ubCourtMap4);
				
				//进球 失球数 胜率 
				OutUbCourtMap ubCourtMap5 = new OutUbCourtMap();
				ubCourtMap5.setIdStr("战绩");
				ubCourtMap5.setNameStr(teamInfo.get("allGoalCount").toString()+"进球 "+teamInfo.get("allFumbleCount").toString()+"失球 "+teamInfo.get("chances").toString()+"%胜率 ");
				ubCourtMap5.setIconType("5");
				ubCourtMapList.add(ubCourtMap5);
			}
			
			map.put("teamId", uTeam.getTeamId());
			
			HashMap<String, Object> teamFollowCount = this.pubGetUTeamFollowCount(map); 
			if(teamFollowCount!=null){
				//关注数
				OutUbCourtMap ubCourtMap = new OutUbCourtMap();
				ubCourtMap.setIdStr("关注指数");
				ubCourtMap.setNameStr(teamFollowCount.get("followCount").toString()+"人");
				ubCourtMap.setIconType("6");
				ubCourtMapList.add(ubCourtMap);
			}
		}
		return ubCourtMapList;
	}
	
	/**
	 * 
	 * 
	   TODO - 球队概况 --球场轴--区域
	   @param getuRegion  区域对象
	   @param map
	   		keyId		区域主键Id
	   @param uTeam			对象
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private OutUbCourtMap setUbCourtMap1(URegion getuRegion,HashMap<String, String> map,UTeam uTeam) throws Exception {
		OutUbCourtMap ubCourtMap1 = new OutUbCourtMap();
		map.put("keyId", uTeam.getuRegion().get_id());
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
		ubCourtMap1.setNameStr(province+"-"+city+"-"+county);//将省市区拼接
		ubCourtMap1.setIconType("1");
		return ubCourtMap1;
	}

	/**
	 * 
	 * 
	   TODO - 解散队伍 【2.0.0】
	   @param map
	   		teamId  队伍Id
	   @return
	   		uTeam 的hashMap<String,Object>
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> disbandOfUTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
			if (null != uTeam) {
				if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
					this.checkAllUTeamStatus(map);
				}
//				HashMap<String, Object> teamStatusMap = this.checkAllUTeamStatus(map);
//				if ("1".equals(teamStatusMap.get("success").toString())) {
					//查找解散的用户是否有权限
					//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
					map.put("type", "4");
					List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
					if (null != playerIds && playerIds.size() > 0) {//有权限
						//先将球员解散
						uplayerService.disBandUTeam(uTeam);
						//解散球队更改球队状态
						uTeam = disbandUTeam(uTeam);
						if (null != uTeam) {
							 //根据类型生成多条通知
							this.setDisbandMessage(uTeam,map);
							//更新排名
							rankingListService.updateRankByList(map);
							resultMap.put("success", "球队解散成功！");
						}else{
							resultMap.put("success", "球队解散失败！");
						}
					}else{
						return WebPublicMehod.returnRet("error","你的权限不足\n暂时无法解散球队");
					}
//				}
			}else{
				return WebPublicMehod.returnRet("error","队伍不存在！");
			}
		}else{
			return WebPublicMehod.returnRet("error","teamId不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 解散通知
	   @param uTeam		对象
	   @param map
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setDisbandMessage(UTeam uTeam, HashMap<String, String> map) throws Exception {
		String mesType = "tmDissolve";
		String msg = "";
		if (publicService.StringUtil(uTeam.getName())) {
			msg = uTeam.getName();
		}else if (publicService.StringUtil(uTeam.getShortName())) {
			msg = uTeam.getShortName();
		}
		map.put("type", "team");//类型
		map.put("mes_type", mesType);//推送类型
		map.put("contentName",msg);//推送内容
		map.put("params", "{\"jump\":\"\",\"teamId\":\""+ uTeam.getTeamId() + "\"}");//跳转参数
		map.put("jump", "");
		map.put("teamId", uTeam.getTeamId());
		messageService.addMoreMessageByType(map);//发送通知
		messageService.pushTeamToPlayerOnByDissolve(map);//发送极光推送通知
	}

	/**
	 * 
	 * 
	   TODO - 球队解散，更新球队状态
	   @param uTeam
	   2016年2月29日
	   dengqiuru
	 * @throws Exception 
	 */
	private UTeam disbandUTeam(UTeam uTeam) throws Exception {
		uTeam.setDisDate(new Date());
		uTeam.setDisTime(new Date());
		uTeam.setTeamStatus("4");
		uTeam.setTeamUseStatus("3");
		uTeam.setTeamCount(0);
		baseDAO.update(uTeam);
		return uTeam;
	}
	
	/**
	 * 
	 * 
	   TODO - 根据teamId查询队伍详情 【2.0.0】
	   @param map
	   		teamId  球队Id
	   @return
	   		UTeam对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UTeam getUteaminfoByteamId(HashMap<String, String> map) throws Exception {
		UTeam uTeam = new UTeam();
		if (publicService.StringUtil(map.get("teamId"))) {
			uTeam = baseDAO.get(UTeam.class, map.get("teamId"));
			if (null != uTeam) {
				//获取胜率
				HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(uTeam.getTeamId());
				if (null != hashMap.get("chances")) {
					String chances = hashMap.get("chances").toString();
					uTeam.setChances(chances);
				}
			}
		}
		return uTeam;
	}
	/**
	 * 
	 * 
	   TODO - 战队详情头部 【2.0.0】
	   @param map
	   		teamId  	查看的球队Id
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findUteaminfoHead(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		HashMap<String, Object> hashMap = new HashMap<>();//参数
		List<HashMap<String, Object>> uTeam = new ArrayList<HashMap<String, Object>>();
		List<Object> listHeadTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerHeadinfo = new ArrayList<List<CornerBean>>();
		String ret = "1";
		String msg = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
		}else{
			hashMap.put("userId", "");
		}
		if (publicService.StringUtil(map.get("teamId"))) {
			StringBuffer sql = this.findUteaminfoHead_sql;
			hashMap.put("teamId", map.get("teamId"));
			uTeam = baseDAO.findSQLMap(sql.toString(), hashMap);
			if (uTeam != null && uTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : uTeam) {
					if (null != hashMap2) {
						//填充角色
						uPlayerRoleService.setMembertype202(hashMap2);
						this.displayData(hashMap2,map);//参数对应名称
						this.setIsNull(hashMap2,map);//设置是否已邀请
						this.setTeamShirts(hashMap2,map);//设置球队队衣
						this.setUteamChances(hashMap2);//设置球队胜率
						this.updateTeamCount(hashMap2,map);//修改球队人数
						if (null != hashMap2) {
							if (null != hashMap2.get("holdDate")) {
								hashMap2.put("holdDate", hashMap2.get("holdDate").toString().subSequence(0, 10));
							}
							if (null != hashMap2.get("playerId")) {
								hashMap2.put("isMyself", "1");
							}else{
								hashMap2.put("isMyself", "2");
							}
						}
						//角标
						listHeadTeamId.add(hashMap2.get("teamId"));
						cornerHeadinfo = cornerService.getAllTeamCornerList(map, listHeadTeamId);
						//关注数
						HashMap<String, Object> teamFollowCount = this.pubGetUTeamFollowCount(map);
						if(teamFollowCount.size()>0){
							hashMap2.put("followCount",teamFollowCount.get("followCount"));
						}else{
							hashMap2.put("followCount", 0);
						}
					}
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","teamId不能为空！");
		}
		//分页
		resultMap.put("ret", ret);
		resultMap.put("msg", msg);
		resultMap.put("cornerHeadinfo", cornerHeadinfo);
		resultMap.put("uTeam", uTeam);
		
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队人数--以防球队人数和实际人数不符
	   @param hashMap2
	   @param map
	   		teamId			球队Id
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void updateTeamCount(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		UTeam uTeam = baseDAO.get(map, "from UTeam where teamId = :teamId");
		List<UPlayer> uPlayers = baseDAO.find(map, "from UPlayer where UTeam.teamId=:teamId and inTeam = '1' ");
		//更新人数
		if (null != uTeam) {
			if (uPlayers.size() > 0) {//球员人数不为空
				if (uTeam.getTeamCount() != null) {//人数不为空
					if (uPlayers.size() != uTeam.getTeamCount()) {//球队人数与球队实际人数不相等
						uTeam.setTeamCount(uPlayers.size());
						hashMap2.put("teamCount", uPlayers.size());
						if (uTeam.getHistoryCount() != null) {//球队历史人数不为空，更新历史人数
							if (uPlayers.size() > uTeam.getHistoryCount()) {
								uTeam.setHistoryCount(uPlayers.size());
							}
						}else{//相等
							uTeam.setHistoryCount(uPlayers.size());
						}
					}else{//球队人数为空
						if (uTeam.getHistoryCount() != null) {
							if (uPlayers.size() > uTeam.getHistoryCount()) {
								uTeam.setHistoryCount(uPlayers.size());
							}
						}else{
							uTeam.setHistoryCount(uPlayers.size());
						}
					}
				}else{//实际人数为空
					uTeam.setTeamCount(uPlayers.size());
					hashMap2.put("teamCount", uPlayers.size());
					if (uTeam.getHistoryCount() != null) {
						if (uPlayers.size() > uTeam.getHistoryCount()) {
							uTeam.setHistoryCount(uPlayers.size());
						}
					}else{
						uTeam.setHistoryCount(uPlayers.size());
					}
				}
			}else{//球员人数
				hashMap2.put("teamCount", uPlayers.size());
				uTeam.setTeamCount(uPlayers.size());
				if (uTeam.getHistoryCount() != null) {
					if (uPlayers.size() > uTeam.getHistoryCount()) {
						uTeam.setHistoryCount(uPlayers.size());
					}
				}else{
					uTeam.setHistoryCount(uPlayers.size());
				}
			}
		}
		baseDAO.getSessionFactory().getCurrentSession().flush();
		baseDAO.update(uTeam);
		//修改球队平均数
		this.updateAvgWHAge(uPlayers, uTeam);
	}

	/**
	 * 
	 * 
	   TODO - 设置队衣颜色
	   @param hashMap
	   		homeTeamShirts   主队队衣
	   		awayTeamShirts		客队球衣
	   @param map
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setTeamShirts(HashMap<String, Object> hashMap, HashMap<String, String> map) throws Exception {
		if (null != hashMap) {
			if (null != hashMap.get("homeTeamShirts") && !"".equals(hashMap.get("homeTeamShirts")) &&  !"null".equals(hashMap.get("homeTeamShirts"))) {//主队球衣
				UParameterInfo uParameterInfo =  Public_Cache.HASH_PARAMS_OBJECT("home_team_shirts").get(hashMap.get("homeTeamShirts"));
				if (null != uParameterInfo) {
					if (publicService.StringUtil(uParameterInfo.getImgurl())) {
						hashMap.put("homeTeamShirtsImgurl", uParameterInfo.getImgurl());
					}else{
						hashMap.put("homeTeamShirtsImgurl", null);
					}
				}
			}else{
				hashMap.put("homeTeamShirtsImgurl", null);
			}
			if (null != hashMap.get("awayTeamShirts") && !"".equals(hashMap.get("awayTeamShirts")) &&  !"null".equals(hashMap.get("awayTeamShirts"))) {//客队球衣
				UParameterInfo uParameterInfo =  Public_Cache.HASH_PARAMS_OBJECT("away_team_shirts").get(hashMap.get("awayTeamShirts"));
				if (null != uParameterInfo) {
					if (publicService.StringUtil(uParameterInfo.getImgurl())) {
						hashMap.put("awayTeamShirtsImgurl", uParameterInfo.getImgurl());
					}else{
						hashMap.put("awayTeamShirtsImgurl", null);
					}
				}
			}else{
				hashMap.put("awayTeamShirtsImgurl", null);
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 查看用户在当前球队中是否求未填写的信息
	   @param hashMap2
	   		playerId		球员Id
	   		position		位置
	   		number			背号
	   		memberType		身份
	   		userId			用户ID
	   @param map
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setIsNull(HashMap<String, Object> hashMap2, HashMap<String, String> map) throws Exception {
		String isNull = "2";//是否没有填写完整 2：不是
		String isOtherNull = "2";//是否没有填写完整 2：不是
		if (null != hashMap2) {
			if (null != hashMap2.get("playerId")) {
				if (null == hashMap2.get("position") || null == hashMap2.get("number") ) {
					isNull = "1";
				}else if(null == hashMap2.get("memberType")){
					isOtherNull = "1";
				}else{
					if (null != hashMap2.get("userId")) {
						UPlayer uPlayer = baseDAO.get("from UPlayer where UUser.userId=:userId and UTeam.teamId is null",hashMap2);
						if (null != uPlayer) {
							if(null == uPlayer.getUUser().getWeight() || "".equals(uPlayer.getUUser().getWeight()) || "null".equals(uPlayer.getUUser().getWeight())){
								isNull = "1";
								isOtherNull = "1";
							}else if(null == uPlayer.getUUser().getHeight() || "".equals(uPlayer.getUUser().getHeight()) || "null".equals(uPlayer.getUUser().getHeight())){
								isNull = "1";
								isOtherNull = "1";
							}else if(null == uPlayer.getUUser().getBirthday()){
								isNull = "1";
								isOtherNull = "1";
							}
						}else{
							isNull = "1";
							isOtherNull = "1";
						}
					}
				}
			}
		}
		hashMap2.put("isOtherNull", isOtherNull);//必填信息是否为空
		hashMap2.put("isNull", isNull);//是否已邀请  2：不是
	}
	/**
	 * 
	 * 
	   TODO - 根据userId查询用户自己创建的球队 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		uTeam的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUteaminfoByUserId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		HashMap<String, Object> hashMap = new HashMap<>();//参数
		UTeam uTeam = null;
		if (publicService.StringUtil(map.get("userId"))) {
			//在球员里面查询该用户是否存在队长信息
			hashMap.put("userId", map.get("userId"));
			String teamId = uplayerService.getUteamIdByuserId(hashMap);
			//查询正常使用且审核通过的队伍
			if (publicService.StringUtil(teamId)) {
				hashMap.put("teamId", teamId);
				uTeam = baseDAO.get("from UTeam where teamId=:teamId",hashMap);
			}
		}
		resultMap.put("uTeam", uTeam);
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 关注战队 【2.0.0】
	   @param map
	   		teamId			被关注球队的Id
	   		type			事件类型   1：关注；2：取消关注
	   @return
	   		uFollow的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> followUTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//判断被关注球队的teamId是否为空
		if (publicService.StringUtil(map.get("loginUserId"))) {
			if (publicService.StringUtil(map.get("teamId"))) {
				UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
				if (null != uTeam) {
					if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
						if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
							if ("1".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
							}else if ("3".equals(uTeam.getTeamUseStatus())) {
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
							}
						}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
						}else{
							return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
						}
					}
					//关注人的信息
					UUser uUser = uuserService.getUserinfoByUserId(map);
					String type = map.get("type");//事件类型  1：关注；2：取消关注
					if (type != null) {
						//查询当前用户与被关注球队是否存在关注关系
						UFollow uFollow = baseDAO.get(map,"from UFollow where userFollowType='2' and UUser.userId=:loginUserId and object_id=:teamId");
						//判断是否是取消关注，是：uFollow为空就提示你还未关注，
						if ("2".equals(type)) {
							if (null == uFollow) {
								return WebPublicMehod.returnRet("error", "你还未关注当前球队！");
							}
						}
						//uFollow不为空，就存在关注关系
						if (null != uFollow) {
							//存在，就只是修改
							uFollow.setFollowStatus(type);
							baseDAO.update(uFollow);
						}else{
							this.saveUfollow(uUser,type,uFollow,map);
						}
						//设置用户首次关注球队
						if (publicService.StringUtil(map.get("loginUserId"))) {
							//设置时间轴
							String userOrTeamType = "1";
							String behaviorType = "5";
							String objectType = null;
							baseDAO.getSessionFactory().getCurrentSession().flush();
							UTeam uTeamTemp = baseDAO.get(UTeam.class,map.get("teamId"));
							UTeamImg uTeamImg = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and imgSizeType='1' and timgUsingType='1'");
							if (null != uTeamImg) {
								objectType = uTeamImg.getImgurl();
							}
							setBehaviorType(userOrTeamType,objectType, behaviorType, uTeamTemp, map,null,uUser);
						}
						if ("1".equals(type)) {
							resultMap.put("success", "关注成功！");
						}else{
							resultMap.put("success", "取消关注成功！");
						}
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
	   TODO - 新增球队关注信息
	   @param uUser
	   @param type
	   @param uFollow
	   @param map
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void saveUfollow(UUser uUser, String type, UFollow uFollow, HashMap<String, String> map) throws Exception {
		//没有 新增
		uFollow = new UFollow();
		uFollow.setKeyId(WebPublicMehod.getUUID());
		uFollow.setUserFollowType("2");
		uFollow.setUUser(uUser);
		uFollow.setCreatedate(new Date());
		uFollow.setObjectId(map.get("teamId"));
		uFollow.setFollowStatus(type);
		baseDAO.save(uFollow);
		
	}
	
	/**
	 * 
	 * 
	   TODO - 我关注的战队 【2.0.0】
	   @param map
	   		page		分页
	   @return
	   		uTeamList的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> myFollowUTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		StringBuffer sql = this.myFollowUTeam_sql;
		List<HashMap<String, Object>> uTeamList=new ArrayList<HashMap<String, Object>>();
		//角标
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		//分页
		if (null != map.get("loginUserId") && !"".equals(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
		}else{
			hashMap.put("userId", "");
		}
		if(null != map.get("page")&& !"".equals(map.get("page"))){
			uTeamList = this.getPageLimit(sql.toString(), map, hashMap,null);
			if (null != uTeamList && uTeamList.size() > 0) {
				for (HashMap<String, Object> hashMap2 : uTeamList) {
//					//填充角色
//					uPlayerRoleService.setMembertype202(hashMap2);
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
				}
			}
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
		}
		resultMap.put("cornerList", cornerList);
		resultMap.put("uTeamList", uTeamList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队信息 【2.0.0】
	   @param map
		 *  teamId			球队Id
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
		 *  imgSizeType 	图片尺寸类型
		 *  imgurl			图片显示地址
		 *  imgWeight		图片权重
		 *  saveurl			图片保存地址
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateUTeaminfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			//获取该用户的信息
			UUser uUser = uuserService.getUserinfoByUserId(map);
			//查找踢人的用户是否有权限
			//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
			map.put("type", "3");
			List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
			if (null != playerIds && playerIds.size() > 0) {//有权限
				UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
				if (null != uTeam) {
					if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
						this.checkAllUTeamStatus(map);
					}
//					if ("1".equals(teamStatusMap.get("success").toString()) || "3".equals(teamStatusMap.get("success").toString()) ) {
						//验证球队全名称是否重复
						boolean isDoubleName = this.isDoubleName(map);
						if (isDoubleName == false) {
//							boolean isDoubleShortName = this.isDoubleShortName(map);
//							if (isDoubleShortName == false) {
							
//								this.setAreaToBaiduLBS(uTeam, map);//上传百度数据
								if (publicService.StringUtil(map.get("imgurl"))) {
									String msgUrl = publicService.isConnect(map.get("imgurl"));
									if ("".equals(msgUrl) || null == msgUrl) {
										return WebPublicMehod.returnRet("error", "非有效队徽url");
									}
								}
								uTeam = this.updateUTeam(uTeam,uUser, map);
								baseDAO.getSessionFactory().getCurrentSession().flush();
								if (publicService.StringUtil(map.get("holddate"))) {
									//用户首次建立球队
									this.setBehaviorHoldDate(uTeam,map);
								}
								if (null == uTeam) {
									return WebPublicMehod.returnRet("error","主队球衣颜色不能与客队颜色一致！");
								}else{
									this.setAreaToBaiduLBSCreateTeam(uTeam, map);//上传或修改LBS百度数据
									resultMap.put("success", "球队信息修改成功！");
								}
//							}else{
//								return WebPublicMehod.returnRet("error","球队简称不能与其他球队的简称一致！");
//							}
						}else{
							return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
						}
//					}
				}else{
					return WebPublicMehod.returnRet("error","查不到球队信息！");
				}
			}else{
				return WebPublicMehod.returnRet("error","你的权限不足\n暂时无法修改球队信息");
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队成立日期，并更新球队时间轴
	   @param uTeam
	   @param map
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setBehaviorHoldDate(UTeam uTeam, HashMap<String, String> map) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();
		if (null != uTeam) {
			if (publicService.StringUtil(uTeam.getTeamId())) {//球队加入upbox时间
				//设置时间轴
				String userOrUteamType = "2";
				String behaviorType = "1";
				String objectType = null;
				//查询队徽
				hashMap.put("teamId", uTeam.getTeamId());
				UTeamImg uTeamImg = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
				if (null != uTeamImg) {
					if (publicService.StringUtil(uTeamImg.getImgurl())) {
						objectType = uTeamImg.getImgurl();
					}
				}
				map.put("teamId", uTeam.getTeamId());
				String createDate = map.get("holddate");
				setBehaviorType(userOrUteamType,objectType, behaviorType, uTeam,map,createDate,null);
			}
		}
	}
	/**
	 * 
	 * 
	   TODO - 更新球队排名 【2.0.0】
	   @param map
	   		rank  排名
	   		teamId  球队Id
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	@Override
	public void updateUTeamRank(HashMap<String, String> map) throws Exception {
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(UTeam.class, map.get("teamId"));
			baseDAO.getSessionFactory().getCurrentSession().evict(uTeam);
			if (null != uTeam) {
				if (publicService.StringUtil(map.get("rank"))) {
					uTeam = this.updateUTeaminfoRank(uTeam,map);
					//上传或修改LBS百度数据
					if (uTeam!=null&&uTeam.getuRegion()!=null&&publicService.StringUtil(uTeam.getuRegion().get_id())) {
						map.put("area", uTeam.getuRegion().get_id());
						this.setAreaToBaiduLBSCreateTeam(uTeam, map);//上传或修改LBS百度数据
					}
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 修改球队排名
	   @param uTeam
	   		uTeam  对象
	   @param map
	   		rank   排名
	   @return
	   2016年4月6日
	   dengqiuru
	 * @throws Exception 
	 */
	private UTeam updateUTeaminfoRank(UTeam uTeam, HashMap<String, String> map) throws Exception {
		if (null != uTeam) {
			//历史排名
			if (null != uTeam.getHistoryRank()) {
				if (publicService.StringUtil(uTeam.getHistoryRank().toString())) {
					Integer historyRank = uTeam.getHistoryRank();
					if (Integer.parseInt(map.get("rank")) < historyRank) {
						uTeam.setHistoryRank(Integer.parseInt(map.get("rank")));
					}
				}else{
					uTeam.setHistoryRank(Integer.parseInt(map.get("rank")));
				}
			}else{
				uTeam.setHistoryRank(Integer.parseInt(map.get("rank")));
			}
			//排名
			uTeam.setRank(Integer.parseInt(map.get("rank")));
			baseDAO.update(uTeam);
			//session未结束，强制将缓存中的数据与数据库同步
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		return uTeam;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队
	   @param uUser
	   @param map
	   		imgurl			球队队徽
	   		holddate		成立日期
	   		teamName		球队名称
	   		shortName		球队简称
	   		area			球队区域
	   		teamClass		球队分类
	   		homeTeamShirts	主队球衣
	   		awayTeamShirts	客队球衣
	   		subcourtId		主场Id
	   		remark			简介
	   @return
	   @throws Exception
	   2016年3月18日
	   dengqiuru
	 */
	private UTeam updateUTeam(UTeam uTeam,UUser uUser,HashMap<String, String> map) throws Exception {
		if (publicService.StringUtil(map.get("imgurl"))) {
			uteamImgService.UpdateTeamLogo(map);
		}
		//将传过来的成立日期转Date
		if (publicService.StringUtil(map.get("holddate"))) {
			Date holdDate = PublicMethod.getStringToDate( map.get("holddate"), "yyyy-MM-dd");
			uTeam.setHoldDate(holdDate);
		}
		//球队全名
		if (publicService.StringUtil(map.get("teamName"))) {
			uTeam.setName(map.get("teamName"));
		}
		//球队简称
		if (publicService.StringUtil(map.get("shortName"))) {
			uTeam.setShortName(map.get("shortName"));
		}
		//所在区域1
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uTeam.setuRegion(uRegion);//区域
		}
		//球队分类 : 俱乐部、FC、公司
		if (publicService.StringUtil(map.get("teamClass"))) {
			uTeam.setTeamClass(map.get("teamClass"));
		}
		//主队球衣
		if (publicService.StringUtil(map.get("homeTeamShirts"))) {
			if (publicService.StringUtil(uTeam.getAwayTeamShirts())) {//判断客队球衣是否与主队球衣是否一致
				if (map.get("homeTeamShirts").equals(uTeam.getAwayTeamShirts())) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
				}
			}else{
				uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
			}
		}
		//客队球衣
		if (publicService.StringUtil(map.get("awayTeamShirts"))) {
			if (publicService.StringUtil(uTeam.getHomeTeamShirts())) {//判断客队球衣是否与主队球衣是否一致
				if (map.get("awayTeamShirts").equals(uTeam.getHomeTeamShirts())) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
				}
			}else{
				uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
			}
		}
		//常驻球场
		if (publicService.StringUtil(map.get("subcourtId"))) {
			UBrCourt uBrCourt = uCourtService.getUCourt(map.get("subcourtId"));
			uTeam.setuBrCourt(uBrCourt);

		}
		//简介
		if (publicService.StringUtil(map.get("remark"))) {
			uTeam.setRemark(map.get("remark"));
		}
		uTeam.setTeamStatus("3");
		baseDAO.update(uTeam);
		return uTeam;
	}
	/**
	 * 
	 * 
	   TODO - 球员概况查询球队列表根据球员信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户
	   @return
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	@Override
	public List<UTeam> getUTeamListByUserId(HashMap<String, String> map) throws Exception {
		List<UPlayer> uPlayers = uplayerService.getUPlayerListByUserId(map);
		List<UTeam> uTeams = new ArrayList<UTeam>();
		if (null != uPlayers && uPlayers.size() > 0) {
			for (UPlayer uPlayer : uPlayers) {
				if (null != uPlayer.getUTeam()) {
					uTeams.add(uPlayer.getUTeam());
				}
			}
		}
		return uTeams;
	}
	/**
	 * 
	 * 
	   TODO - 球队设置排名 【2.0.0】
	   @param map
	   		reank     	排名
	   		teamId		球队Id
	   @throws Exception
	   2016年3月22日
	   dengqiuru
	 */
	@Override
	public void setOutUTeamRank(HashMap<String, String> map) throws Exception {
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(UTeam.class, map.get("teamId"));
			if (null != uTeam) {
				if (publicService.StringUtil(map.get("rank"))) {
					if (null != uTeam.getRank()) {
						if (uTeam.getHistoryRank() > uTeam.getRank()) {
							uTeam.setHistoryRank(uTeam.getRank());
						}
						uTeam.setRank(Integer.parseInt(map.get("rank")));
						baseDAO.update(uTeam);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 查看球员详情的战队列表 【2.0.0】
	   @param map
	   		userId			查看球员的userId
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUteamListInroughly(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> listTeam = new ArrayList<HashMap<String, Object>>();
		StringBuffer sql = this.getUteamListInroughly_sql;
		//分页
		hashMap.put("userId", map.get("userId"));
		listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
		if (listTeam != null && listTeam.size() > 0) {
			for (HashMap<String, Object> hashMap2 : listTeam) {
				this.displayData(hashMap2,map);
				this.setUteamChances(hashMap2);
				//填充角色
				uPlayerRoleService.setMembertype202(hashMap2);
			}
		}
//		listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);1

		return listTeam;
	}

	/**
	 * 
	 * 
	   TODO - 设置胜率
	   @param hashMap2
	   		teamId		球队Id
	   		teamUseStatus		球队状态
	   		chances		胜率
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void setUteamChances(HashMap<String, Object> hashMap2) throws Exception {
		if (null != hashMap2) {
			String chances = null;
			if (null != hashMap2.get("teamId") && !"".equals(hashMap2.get("teamId")) &&  !"null".equals(hashMap2.get("teamId"))) {//事件类型名称
				//获取胜率
				HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(hashMap2.get("teamId").toString());
				if (null != hashMap) {
					if ("1".equals(hashMap.get("teamUseStatus"))) {
						chances = null;
					}else{
						if (null != hashMap.get("chances")) {
							chances = hashMap.get("chances").toString();
						}
					}
				}
			}
			hashMap2.put("chances",chances);
		}
	
	}

	/**
	 * 
	 * 
	   TODO - 邀请球员 【2.0.0】
	   @param map
	   		loginUserId		当前用户Id
	   		teamId			进入的球队Id
	   		playerId		被邀请人的球员Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> invitePlayer(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != map.get("loginUserId") && !"".equals(map.get("loginUserId"))) {
			if (null != map.get("playerId") && !"".equals(map.get("playerId"))) {
				UPlayer uPlayer = baseDAO.get(map,"from UPlayer where playerId=:playerId");
				if (null != uPlayer) {
					UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
					if (null != uTeam) {
						if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
							if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
								if ("1".equals(uTeam.getTeamUseStatus())) {
									return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
								}else if ("3".equals(uTeam.getTeamUseStatus())) {
									return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
								}
							}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
								return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
							}else{
								return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
							}
						}
						String messageType = "4";//邀请通知
						this.setMessageParams(map, uPlayer, uTeam, messageType);
						//队员被邀请加入队伍信息表
						uInviteteamService.saveUinviteInfo(map, uPlayer, uTeam);
						resultMap.put("success", "邀人成功！");
					}else{
						return WebPublicMehod.returnRet("error","你的球队不存在！");
					}
				}else{
					return WebPublicMehod.returnRet("error","该球员的账号存在异常！");
				}
			}else{
				return WebPublicMehod.returnRet("error","请选择你要邀请的球员！");
			}
		}else{
			return WebPublicMehod.returnRet("error","请使用账号登录后，执行此操作！");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 推荐的对手 【2.0.0】
	   @param map
	   		loginUserId		登录人的Id
	   @return
	   		outUteamLists 的resultMap<String,Object>
	   @throws Exception
	   2016年3月28日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getrecommendTeam(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		HashMap<String, Object> hashMap = new HashMap<>();//参数
		List<HashMap<String, Object>> listTeam = new ArrayList<HashMap<String, Object>>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			hashMap.put("teamId", map.get("teamId"));
			StringBuffer sql = null;
			if (publicService.StringUtil(map.get("teamId"))) {
				hashMap.put("teamId", map.get("teamId"));
				sql = this.getrecommendTeam202_sql;
			}else{
				sql = this.getrecommendTeam_sql;
			}
			listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
			List<Object> listTeamId = new ArrayList<Object>();
			if (listTeam != null && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					this.displayData(hashMap2,map);//设置参数对应的名称
					this.setUteamChances(hashMap2);//设置胜率
					//填充角色
					uPlayerRoleService.setMembertype202(hashMap2);
					String teamId = hashMap2.get("teamId").toString();//角标
					listTeamId.add(teamId);
				}
			}
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);//角标list
			}
			resultMap.put("cornerList", cornerList);
			resultMap.put("outUteamLists", listTeam);
		}else{
			return WebPublicMehod.returnRet("error","请使用账号登录后，执行此操作！");
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 球队分享 【2.0.0】
	   @param map
	   		playerId   球员的Id
	   @throws Exception
	   2016年4月5日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> uTeaminfoShare(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("teamId"))) {
			List<HashMap<String, Object>> outUTeamImgs = new ArrayList<HashMap<String, Object>>();
			outUTeamImgs = uteamImgService.getGalleryListInroughly(map);//获取球队相册
			resultMap.put("outUTeamImgs", outUTeamImgs);
			
			List<HashMap<String, Object>> uTeaminfo = new ArrayList<HashMap<String, Object>>();
			uTeaminfo= this.getUteaminfoInShare(map);//分享用户球员基本信息
			resultMap.put("uTeaminfo", uTeaminfo);
			
			//战队球场轴
			List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
			map.put("XteamId", map.get("teamId"));
			ubCourtMap = this.getBrCourtlist(map);
			resultMap.put("ubCourtMap", ubCourtMap);
			
			//2:获取战队时间轴
			List<UTeamBehavior> uTeamBehaviors =  new ArrayList<UTeamBehavior>();
			List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<HashMap<String, Object>>();
			map.put("type", "2");
			List<Object> objectList = publicService.getBehaviorList(map);
			if (null != objectList && objectList.size() > 0) {
				for (Object object : objectList) {
					UTeamBehavior uTeamBehavior = (UTeamBehavior)object;
					uTeamBehavior.setuTeam(null);
					uTeamBehavior.setTeamBehaviorTypeName(Public_Cache.HASH_PARAMS("team_behavior_type").get(uTeamBehavior.getTeamBehaviorType()));
					
					//里程碑字段详细
					uTeamBehavior.setJsonStr(this.getUTeamBehaviorJsonStr(uTeamBehavior));	
					
					uTeamBehaviors.add(uTeamBehavior);
				}
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviors);
			}else{
				uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(map);
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
			}
			
			//获取球队成员用户信息及头像
			List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
			outPlayerLists = uplayerService.getUplayerListInRoughly(map);
			
			//获取荣誉墙
			List<Map<String, Object>> honors = this.getUteamHonorByGK(map);
			resultMap.put("outHonorsLists", honors);
			
			resultMap.put("outPlayerLists", outPlayerLists);
		}else{
			return WebPublicMehod.returnRet("error", "teamId不能为空！");
		}
		return resultMap;
	}
	

	/**
	 * 
	 * 
	   TODO - 分享用户球员基本信息
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private List<HashMap<String, Object>> getUteaminfoInShare(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> teamLists = new ArrayList<HashMap<String, Object>>();
		//该用户的球队列表
		hashMap.put("teamId", map.get("teamId"));
		StringBuffer sql = this.getUteaminfoInShare_sql;
		//分页
		teamLists = baseDAO.findSQLMap(sql.toString(),hashMap);

		if (teamLists != null && teamLists.size() > 0) {
			for (HashMap<String, Object> hashMap2 : teamLists) {
				this.displayData(hashMap2,null);//参数对应名称
				this.setUteamAllChances(hashMap2);//设置胜率
				//填充角色
//				uPlayerRoleService.setMembertype202(hashMap2);
				//关注数
				HashMap<String, Object> teamFollowCount = this.pubGetUTeamFollowCount(map);
				if(teamFollowCount.size()>0){
					hashMap2.put("followCount", teamFollowCount.size());
				}else{
					hashMap2.put("followCount", 0);
				}
			}
		}
		return teamLists;
	}

	/**
	 * 
	 * 
	   TODO - 设置胜率
	   @param hashMap2
	   		teamId			球队Id
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void setUteamAllChances(HashMap<String, Object> hashMap2) throws Exception {
		String verMatchCount = "0";
		String drawMatchCount = "0";
		String failMatchCount = "0";
		String allMatchCount = "0";
		String chances = "0";
		if (null != hashMap2) {
			if (null != hashMap2.get("teamId")) {//事件类型名称
				//获取胜率
				HashMap<String, Object> hashMap = rankingListService.getPublicRankingInfo(hashMap2.get("teamId").toString());
				if (null != hashMap) {
					if (null != hashMap.get("chances")) {
						chances = hashMap.get("chances").toString();
					}
					if (hashMap.get("verMatchCount") != null) {
						verMatchCount = hashMap.get("verMatchCount").toString();
					}
					if (hashMap.get("drawMatchCount") != null) {
						drawMatchCount = hashMap.get("drawMatchCount").toString();
					}
					if (hashMap.get("failMatchCount") != null) {
						failMatchCount = hashMap.get("failMatchCount").toString();
					}
					if (hashMap.get("allMatchCount") != null) {
						allMatchCount = hashMap.get("allMatchCount").toString();
					}
				}
			}
		}
		hashMap2.put("chances",chances);
		hashMap2.put("verMatchCount",verMatchCount);
		hashMap2.put("drawMatchCount",drawMatchCount);
		hashMap2.put("failMatchCount",failMatchCount);
		hashMap2.put("allMatchCount",allMatchCount);
	}

	/**
	 * 
	 * 
	   TODO - 赛事对手 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		competitor、galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年3月28日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> eventCompetitorOfUteam(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(hashMap.get("teamId"))) {
			hashMap.put("XteamId", hashMap.get("teamId"));
			UTeam uTeam = baseDAO.get(UTeam.class, hashMap.get("XteamId"));
			this.settUteamChances(uTeam);//设置球队胜率和队徽
			//对手
			List<HashMap<String, Object>> competitor= new ArrayList<HashMap<String, Object>>();
			competitor = this.getCompetitor(hashMap);
			resultMap.put("competitor", competitor);
			
			//获取球队成员用户信息及头像
			List<HashMap<String, Object>> outPlayerLists = new ArrayList<HashMap<String, Object>>();
			outPlayerLists = uplayerService.getUplayerListInCompetitor(hashMap);
			resultMap.put("outPlayerLists", outPlayerLists);
			//战队球场轴
			List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
			ubCourtMap = getBrCourtlist(hashMap);
			resultMap.put("ubCourtMap", ubCourtMap);
			//2:获取战队时间轴
			List<UTeamBehavior> uTeamBehaviors =  new ArrayList<UTeamBehavior>();
			List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<HashMap<String, Object>>();
			hashMap.put("type", "2");
			hashMap.put("teamId", hashMap.get("XteamId"));
			
			List<Object> objectList = publicService.getBehaviorList(hashMap);
			if (null != objectList && objectList.size() > 0) {
				for (Object object : objectList) {
					UTeamBehavior uTeamBehavior = (UTeamBehavior)object;
					uTeamBehavior.setuTeam(null);
					uTeamBehavior.setTeamBehaviorTypeName(Public_Cache.HASH_PARAMS("team_behavior_type").get(uTeamBehavior.getTeamBehaviorType()));
					uTeamBehaviors.add(uTeamBehavior);
				}
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviors);
			}else{
				uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(hashMap);
				resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
			}
		
			//判断是否是自己的球队
			hashMap = this.setUTeamMyself(uTeam,hashMap);
			//是否是自己的球队
			resultMap.put("isMyself", hashMap.get("isMyself"));
//			//是否是队长
//			resultMap.put("isTeamLeader", hashMap.get("isTeamLeader"));
			//对手角标
			List<Object> listTeamId = new ArrayList<Object>();
			List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
			String teamId = hashMap.get("XteamId");
			listTeamId.add(teamId);
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(hashMap, listTeamId);
			}
			//站队列表用户信息
			resultMap.put("cornerList", cornerList);
			
		}else{
			resultMap.put("competitor", null);
			resultMap.put("outPlayerLists", null);
			resultMap.put("ubCourtMap", null);
			resultMap.put("outuTeamBehaviorlist", null);
//			resultMap.put("isMyself", "2");
//			resultMap.put("isTeamLeader", "2");
			resultMap.put("cornerList", null);
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 约战指定更多对手 【2.0.0】
	   @param map
	   		token
	   @return
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> addMoreCompetitor(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//返回集合
		HashMap<String, Object> hashMap = new HashMap<>();//参数集合
		//判断搜素内容是否为空
		StringBuffer newSql=new StringBuffer();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
		}else{
			hashMap.put("userId", "");
		}
		//搜索查询
		if (publicService.StringUtil(map.get("appCode"))) {
			int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));//int值的版本号
			if (intAppCode>=202) {
				//接受到参数teamId
				newSql.append(this.addMoreCompetitor202_sql);
				if (publicService.StringUtil(map.get("teamId"))) {
					hashMap.put("teamId", map.get("teamId"));
					newSql.append(" and t.team_id not in (:teamId) ");
				}
				if (publicService.StringUtil(map.get("searchStr"))) {
					hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
					newSql.append(" and (name like :searchStr or short_name  like :searchStr or t.remark like :searchStr "
							+ " or t.team_id in (select up.team_id from u_player up,u_user uu,u_player_role r where up.player_id=r.player_id and uu.user_id=up.user_id and r.member_type='1' and r.member_type_use_status='1' and up.in_team='1' and (uu.nickname like :searchStr or uu.realname like :searchStr ))) "
							+ " group by t.team_id order BY t.createdate desc,t.recommend_team=1 desc,abs(month(now())-month(t.createdate)),abs(day(now())-day(t.createdate)),t.team_count>=7 desc,t.team_count<7 desc "
							);
				}else{
					newSql.append(" group by t.team_id order BY t.createdate desc,t.recommend_team=1 desc,abs(month(now())-month(t.createdate)),abs(day(now())-day(t.createdate)),t.team_count>=7 desc,t.team_count<7 desc ");
				}
			}
		}else{//2.0.2之前版本
			if (publicService.StringUtil(map.get("searchStr"))) {
				hashMap.put("searchStr", "%"+map.get("searchStr")+"%");
				newSql.append( this.addMoreCompetitor_sql +" and (name like :searchStr or short_name  like :searchStr or t.remark like :searchStr "
						+ " or t.team_id in (select up.team_id from u_player up,u_user uu,u_player_role r where up.player_id=r.player_id and uu.user_id=up.user_id and r.member_type='1' and r.member_type_use_status='1' and up.in_team='1' and (uu.nickname like :searchStr or uu.realname like :searchStr ))) "
						+ " group by t.team_id order BY t.createdate desc,t.recommend_team=1 desc,abs(month(now())-month(t.createdate)),abs(day(now())-day(t.createdate)),t.team_count>=7 desc,t.team_count<7 desc "
						);
			}else{
				newSql.append( this.addMoreCompetitor_sql + " group by t.team_id order BY t.createdate desc,t.recommend_team=1 desc,abs(month(now())-month(t.createdate)),abs(day(now())-day(t.createdate)),t.team_count>=7 desc,t.team_count<7 desc ");
			}
		}
		//自己的球队不应该在球队列表内
		List<HashMap<String, Object>> listTeam=new ArrayList<HashMap<String, Object>>();
		//分页
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if(null != map.get("page")&& !"".equals(map.get("page"))){
			listTeam = this.getPageLimit(newSql.toString(), map, hashMap,null);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
				}
			}
			//角标
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
		}

		//站队列表用户信息
		resultMap.put("outUteamLists", listTeam);
		resultMap.put("cornerList", cornerList);
		return resultMap;
	}

	
	/**
	 * 
	 * 
	   TODO - 小数保留一位小数
	   @param obj
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	public String roundNumber(Double obj) throws Exception{ 
        BigDecimal bg = new BigDecimal(obj); 
        String f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        return f1;
    }
	/**
	 * 
	 * 
	   TODO - 检测球队是否已解散和禁用（统一） 【2.0.0】
	   @param map
	   		token
	   @return
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> checkUTeamStatus(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String success = "球队正常";
		 UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
		if (null != uTeam) {
			if (publicService.StringUtil(map.get("appCode"))) {
				if ("2.0.2".equals(map.get("appCode"))) {
					if ("1".equals(uTeam.getTeamUseStatus())) {
						return WebPublicMehod.returnRet("error","-2_当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
					}else if ("3".equals(uTeam.getTeamUseStatus())) {
						return WebPublicMehod.returnRet("error","-2_当前球队已解散！");
					}else{
						success = "球队正常";
					}
				}
			}else{
				if ("1".equals(uTeam.getTeamUseStatus())) {
					return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
				}else if ("3".equals(uTeam.getTeamUseStatus())) {
					return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
				}else{
					success = "球队正常";
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","当前球队不存在！");
		}
		resultMap.put("success", success);
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 球队详情内，所有接口判断球队是否正常（每个接口分开） 【2.0.0】
	   @param map
	   		token    登录token
	   		teamId   当前的球队Id
	   @return
	   @throws Exception
	   2016年5月2日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> checkAllUTeamStatus(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		List<UPlayer> uPlayers = new ArrayList<UPlayer>();
		UPlayer uPlayer = null;
		String success = "2";
		 UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
		 if (publicService.StringUtil(map.get("appCode"))) {
			int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));
			if (intAppCode > 201) {
				if (null != uTeam) {
					if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
						if ("1".equals(uTeam.getTeamUseStatus())) {
							return WebPublicMehod.returnRet("error","-2_当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
						}else if ("3".equals(uTeam.getTeamUseStatus())) {
							return WebPublicMehod.returnRet("error","-2_当前球队已解散！");
						}else{
							if (publicService.StringUtil(map.get("loginUserId"))) {
								uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam = '1'");
								if (null != uPlayer) {
									success = "1";//1:球队正常
								}else{
									uPlayers = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId order by adddate desc,addtime desc ");
									if (null != uPlayers && uPlayers.size() > 0) {
										uPlayer = uPlayers.get(0);
										if (publicService.StringUtil(uPlayer.getInTeam())) {
											if ("2".equals(uPlayer.getInTeam())) {
												if (publicService.StringUtil(uPlayer.getExitType())) {
													if ("1".equals(uPlayer.getExitType())) {
														return WebPublicMehod.returnRet("error","-2_你已退出当前球队");
													}else if ("2".equals(uPlayer.getExitType())) {
														return WebPublicMehod.returnRet("error","-2_队长已将你踢出当前球队");
													}else if ("3".equals(uPlayer.getExitType())) {
														return WebPublicMehod.returnRet("error","-2_当前球队已被解散");
													}else{
														return WebPublicMehod.returnRet("error","-2_你已不在当前球队内");
													}
												}else{
													return WebPublicMehod.returnRet("error","-2_你已不在当前球队内");
												}
											}else{
												success = "1";
											}
										}
									}
								}
							}
						}
					}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
						if (publicService.StringUtil(map.get("loginUserId"))) {
							map.put("userId", map.get("loginUserId"));
							String type = "1";
							uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map, type);
							if (null != uPlayer) {
								if (null != uPlayer.getMemberType()) {
									if ("1".equals(uPlayer.getMemberType())) {
										success = "3";
//										return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
									}else{
										return WebPublicMehod.returnRet("error","-2_当前球队审核未通过\n请队长重新登记信息");
									}
								}else{
									return WebPublicMehod.returnRet("error","-2_当前球队审核未通过\n请队长重新登记信息");
								}
							}else{
								return WebPublicMehod.returnRet("error","-2_当前球队审核未通过");
							}
						}else{
							return WebPublicMehod.returnRet("error","-2_当前球队审核未通过");
						}
					}else{
						return WebPublicMehod.returnRet("error","-2_当前球队已解散");
					}
				}else{
					return WebPublicMehod.returnRet("error","当前球队不存在！");
			}
		}
		 }else{
			if (null != uTeam) {
				if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
					if ("1".equals(uTeam.getTeamUseStatus())) {
						return WebPublicMehod.returnRet("error","-2^<}}当前球队已被禁用\n如有疑问拨打400-805-1719 联系客服小激");
					}else if ("3".equals(uTeam.getTeamUseStatus())) {
						return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散！");
					}else{
						if (publicService.StringUtil(map.get("loginUserId"))) {
							uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId and inTeam = '1'");
							if (null != uPlayer) {
								success = "1";//1:球队正常
							}else{
								uPlayers = baseDAO.find(map, "from UPlayer where UUser.userId=:loginUserId and UTeam.teamId=:teamId order by adddate desc,addtime desc ");
								if (null != uPlayers && uPlayers.size() > 0) {
									uPlayer = uPlayers.get(0);
									if (publicService.StringUtil(uPlayer.getInTeam())) {
										if ("2".equals(uPlayer.getInTeam())) {
											if (publicService.StringUtil(uPlayer.getExitType())) {
												if ("1".equals(uPlayer.getExitType())) {
													return WebPublicMehod.returnRet("error","-2^<}}你已退出当前球队");
												}else if ("2".equals(uPlayer.getExitType())) {
													return WebPublicMehod.returnRet("error","-2^<}}队长已将你踢出当前球队");
												}else if ("3".equals(uPlayer.getExitType())) {
													return WebPublicMehod.returnRet("error","-2^<}}当前球队已被解散");
												}else{
													return WebPublicMehod.returnRet("error","-2^<}}你已不在当前球队内");
												}
											}else{
												return WebPublicMehod.returnRet("error","-2^<}}你已不在当前球队内");
											}
										}else{
											success = "1";
										}
									}
								}
							}
						}
					}
				}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
					if (publicService.StringUtil(map.get("loginUserId"))) {
						map.put("userId", map.get("loginUserId"));
						String type = "1";
						uPlayer = uplayerService.getUteamUplayerinfoByTeamId(map, type);
						if (null != uPlayer) {
							if (null != uPlayer.getMemberType()) {
								if ("1".equals(uPlayer.getMemberType())) {
									success = "3";
//									return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
								}else{
									return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请队长重新登记信息");
								}
							}else{
								return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请队长重新登记信息");
							}
						}else{
							return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
						}
					}else{
						return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过");
					}
				}else{
					return WebPublicMehod.returnRet("error","-2^<}}当前球队已解散");
				}
			}else{
				return WebPublicMehod.returnRet("error","当前球队不存在！");
			}
		}
		resultMap.put("success", success);
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 修改球队信息 【2.0.0】
	   @param map
		 *  teamId			球队Id
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
		 *  imgSizeType 	图片尺寸类型
		 *  imgurl			图片显示地址
		 *  imgWeight		图片权重
		 *  saveurl			图片保存地址
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateUTeaminfoAndroid(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			//获取该用户的信息
			UUser uUser = uuserService.getUserinfoByUserId(map);
			//查找踢人的用户是否有权限
			//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
			map.put("type", "3");
			List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
			if (null != playerIds && playerIds.size() > 0) {//有权限
				UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
				if (null != uTeam) {
					if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
						this.checkAllUTeamStatus(map);
					}
//					if ("1".equals(teamStatusMap.get("success").toString()) || "3".equals(teamStatusMap.get("success").toString()) ) {
						//验证球队全名称是否重复
						boolean isDoubleName = this.isDoubleName(map);
						if (isDoubleName == false) {
//							boolean isDoubleShortName = this.isDoubleShortName(map);
//							if (isDoubleShortName == false) {
//								this.setAreaToBaiduLBS(uTeam, map);//上传或修改LBS百度数据
								if (publicService.StringUtil(map.get("imgurl"))) {
									String msgUrl = publicService.isConnect(map.get("imgurl"));
									if ("".equals(msgUrl) || null == msgUrl) {
										return WebPublicMehod.returnRet("error", "非有效队徽url");
									}
								}
								uTeam = this.updateUTeamAndroid(uTeam,uUser, map);
								baseDAO.getSessionFactory().getCurrentSession().flush();
								if (publicService.StringUtil(map.get("holddate"))) {
									//用户首次建立球队
									this.setBehaviorHoldDate(uTeam,map);
								}
								if (null == uTeam) {
									return WebPublicMehod.returnRet("error","主队球衣颜色不能与客队颜色一致！");
								}else{
									this.setAreaToBaiduLBSCreateTeam(uTeam, map);//上传或修改LBS百度数据
									resultMap.put("success", "球队信息修改成功！");
								}
//							}else{
//								return WebPublicMehod.returnRet("error","球队简称不能与其他球队的简称一致！");
//							}
						}else{
							return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
						}
//					}
				}else{
					return WebPublicMehod.returnRet("error","查不到球队信息！");
				}
			}else{
				return WebPublicMehod.returnRet("error","你的权限不足\n暂时无法修改当前球队信息！");
			}
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 修改球队
	   @param uUser
	   @param map
	   @return
	   @throws Exception
	   2016年3月18日
	   dengqiuru
	 */
	private UTeam updateUTeamAndroid(UTeam uTeam,UUser uUser,HashMap<String, String> map) throws Exception {
		if (publicService.StringUtil(map.get("imgurl"))) {
			uteamImgService.UpdateTeamLogo(map);
		}
		//将传过来的成立日期转Date
		if (publicService.StringUtil(map.get("holddate"))) {
			Date holdDate = PublicMethod.getStringToDate( map.get("holddate"), "yyyy-MM-dd");
			uTeam.setHoldDate(holdDate);
		}
		//球队全名
		if (publicService.StringUtil(map.get("teamName"))) {
			uTeam.setName(map.get("teamName"));
		}
		//球队简称
		if (publicService.StringUtil(map.get("shortName"))) {
			uTeam.setShortName(map.get("shortName"));
		}
		//所在区域1
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uTeam.setuRegion(uRegion);//区域
		}
		//球队分类 : 俱乐部、FC、公司
		if (publicService.StringUtil(map.get("teamClass"))) {
			uTeam.setTeamClass(map.get("teamClass"));
		}
		//主队球衣
		if (publicService.StringUtil(map.get("homeTeamShirts"))) {
			uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
		}
		//客队球衣
		if (publicService.StringUtil(map.get("awayTeamShirts"))) {
			uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
		}
		//常驻球场
		if (publicService.StringUtil(map.get("subcourtId"))) {
			UBrCourt uBrCourt = uCourtService.getUCourt(map.get("subcourtId"));
			uTeam.setuBrCourt(uBrCourt);

		}
		//简介
		if (publicService.StringUtil(map.get("remark"))) {
			uTeam.setRemark(map.get("remark"));
		}
		uTeam.setTeamStatus("3");
		baseDAO.update(uTeam);
		return uTeam;
	}
	
	/**
	 * 
	 * 
	   TODO - 根据userId和teamId获取他有权限的球队
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   		teamId  球队Id 
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	@Override
	public UTeam getUteamByUserIdTeamId(HashMap<String, String> map) throws Exception {
		UTeam uTeam = new UTeam();
		List<HashMap<String, Object>> playerIdList = uPlayerRoleLimitService.playerIsRoleByType(map);
		if (null != playerIdList && playerIdList.size() > 0) {
			uTeam = baseDAO.get(map, "from UTeam where teamId=:teamId");
		}
		return uTeam;
	}

	/**
	 * 
	 * 
	   TODO - 根据userId获取他有权限的球队列表
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	@Override
	public UTeam getUteamListByUserId(HashMap<String, String> map) throws Exception {
		List<UTeam> uTeams = new ArrayList<>();
		List<HashMap<String, Object>> playerIdList = uPlayerRoleLimitService.playerIsRoleByType(map);
		if (null != playerIdList && playerIdList.size() > 0) {
			for (HashMap<String, Object> hashMap : playerIdList) {
				if (null != hashMap.get("teamId")) {
					hashMap.put("brCourtId", map.get("brCourtId"));
					UChampion chs = baseDAO.get("from UChampion where UTeam.teamId=:teamId and isChampion='1' and  UBrCourt.subcourtId=:brCourtId ",hashMap);
					if(chs!=null){
						UTeam uTeam=chs.getUTeam();
						uTeams.add(uTeam);
					}
				}
			}
		}
		if(uTeams.size()>0){
			return uTeams.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * 
	   TODO - 根据userId获取他有权限的球队列表
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUteamRoleByUserId(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> hashMap = new HashMap<>();
		StringBuffer sql = new StringBuffer("select p.player_id playerId,p.team_id teamId ,p.user_id userId ");
		if ("5".equals(map.get("type"))) {
			sql.append(" ,l.duel duel ");
		}else if ("6".equals(map.get("type"))) {
			sql.append(" ,l.challenge challenge ");
		}else{
			sql.append(" ,l.matched matched ");
		}
		sql.append(" from u_player p "
				+" LEFT JOIN u_team t on p.team_id=t.team_id "
				+" LEFT JOIN u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
				+" LEFT JOIN u_player_role_limit l on r.member_type=l.member_type "
 				+" where p.user_id=:userId and p.in_team='1' and t.team_count >= :teamCount "
				+"");

		if ("5".equals(map.get("type"))) {
			sql.append(" and l.duel=1 ");
		}else if ("6".equals(map.get("type"))) {
			sql.append(" and l.challenge=1 ");
		}else{
			sql.append(" and l.matched=1 ");
		}
		sql.append(" group by p.player_id ORDER BY l.rank_role asc ");
		hashMap.put("teamCount", Public_Cache.TEAM_COUNT);
		hashMap.put("userId", map.get("userId"));
		List<HashMap<String, Object>> uteamList = baseDAO.findSQLMap(sql.toString(),hashMap);
		return uteamList;
	}

	/**
	 * 
	 * 
	   TODO - 我的球队信息
	   @param map
	   		loginUserId   当前用户
	   @return
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> myDefaultTeamInfo202(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> listTeam=null;
		//分页
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
//		List<HashMap<String, Object>> defaultTeamHead=null;
		String uuserUteamStatus = "5";
		if (null != map.get("loginUserId") || !"".equals(map.get("loginUserId"))) {
//			defaultTeamHead = this.getDefaultTeamHead202(map);
			hashMap.put("userId", map.get("loginUserId"));
			//sql
			StringBuffer sql = this.myDefaultTeamInfo_sql;
			listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
			// 更改类型和状态的页面显示值
			if (listTeam != null && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					//分页
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
					this.displayData(hashMap2,map);
					this.setUteamChances(hashMap2);
					//填充角色
					uPlayerRoleService.setMembertype202(hashMap2);
					//关注数
					map.put("teamId", teamId);
					HashMap<String, Object> teamFollowCount = this.pubGetUTeamFollowCount(map);
					if(teamFollowCount.size()>0){
						hashMap2.put("followCount",teamFollowCount.get("followCount"));
					}else{
						hashMap2.put("followCount", 0);
					}
				}
			}
			//角标
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
			//用户状态
			//查询当前球员是否有资格可以创建球队
			HashMap<String, String> statusMap = this.checkUuserUteamStatus(map);
			uuserUteamStatus = statusMap.get("uuserUteamStatus");
			
		}
		resultMap.put("uTeamList", listTeam);
		resultMap.put("uuserUteamStatus", uuserUteamStatus);
		resultMap.put("cornerList", cornerList);
//		resultMap.put("defaultTeamHead", defaultTeamHead);
		return resultMap;
	}
//
//	/**
//	 * 
//	 * 
//	   TODO - 默认球队列表头部
//	   @param map
//	   		loginUserId		当前用户Id
//	   @return
//	   2016年6月20日
//	   dengqiuru
//	 * @throws Exception 
//	 */
//	private List<HashMap<String, Object>> getDefaultTeamHead202(HashMap<String, String> map) throws Exception {
//		List<HashMap<String, Object>> defaultTeamHead = null;
//		//首先查询默认球队
//		String sql = "select t.team_id teamId,p.player_id playerId,t.`name` `name`,t.short_name shortName, "
//				+" t.team_class teamClass,t.avg_age avgAge,t.avg_height avgHeight,t.avg_weight avgWeight, "
//				+" t.rank rank,t.integral integral,uti.imgurl imgurl,p.position position,p.number number,p.default_uteam defaultUteam from u_team t "
//				+" LEFT join u_team_img uti on t.team_id=uti.team_id "
//				+" LEFT JOIN u_player p on p.team_id=t.team_id "
//				+" where p.default_uteam='1' and p.user_id=:loginUserId"
//				+" and (t.team_status='1' or t.team_status='2' or t.team_status='3') and (t.team_use_status='1' or t.team_use_status='2') and p.in_team='1' limit 1 " ;
//		defaultTeamHead = baseDAO.findSQLMap(map, sql);
//		if (defaultTeamHead.size() <= 0) {
//			//默认球队为空，求查出创建且为队长的球队
//			sql = "select t.team_id teamId,p.player_id playerId,t.`name` `name`,t.short_name shortName, "
//				+" t.team_class teamClass,t.avg_age avgAge,t.avg_height avgHeight,t.avg_weight avgWeight, "
//				+" t.rank rank,t.integral integral,uti.imgurl imgurl,p.position position,p.number number,p.default_uteam defaultUteam from u_team t "
//				+" LEFT join u_team_img uti on t.team_id=uti.team_id "
//				+" LEFT JOIN u_player p on p.team_id=t.team_id "
//				+" LEFT JOIN u_player_role r on r.player_id=p.player_id  "
//				+" where t.user_id=:loginUserId and r.member_type='1' and r.member_type_use_status='1' "
//				+" and (t.team_status='1' or t.team_status='2' or t.team_status='3') and (t.team_use_status='1' or t.team_use_status='2') and p.in_team='1' "
//				+" GROUP BY t.team_id ,t.user_id "
//				+" ORDER BY t.createdate desc,t.createtime desc limit 1";
//			defaultTeamHead = baseDAO.findSQLMap(map, sql);
//		}
//		if (null != defaultTeamHead && defaultTeamHead.size() > 0) {
//			for (HashMap<String, Object> hashMap : defaultTeamHead) {
//				this.displayDataForDefault202(hashMap);
//				//填充角色
//				uPlayerRoleService.setMembertype202(hashMap);
//			}
//		}
//		return defaultTeamHead;
//	}
//
//	/**
//	 * 
//	 * 
//	   TODO - 默认列表头部填充值对应的名称
//	   @param hashMap
//	   2016年6月20日
//	   dengqiuru
//	 * @throws Exception 
//	 */
//	private void displayDataForDefault202(HashMap<String, Object> hashMap) throws Exception {
//		if (null != hashMap) {
//			if (null != hashMap.get("teamClass") && !"".equals(hashMap.get("teamClass")) &&  !"null".equals(hashMap.get("teamClass"))) {//球队分类
//				hashMap.put("teamClassName", Public_Cache.HASH_PARAMS("team_class").get(hashMap.get("teamClass")));
//			}else{
//				hashMap.put("teamClassName", null);
//			}
//			if (null != hashMap.get("position") && !"".equals(hashMap.get("position")) &&  !"null".equals(hashMap.get("position"))) {//球队分类
//				hashMap.put("positionName", Public_Cache.HASH_PARAMS("position").get(hashMap.get("position")));
//			}else{
//				hashMap.put("positionName", null);
//			}
//			if(null != hashMap.get("teamId") && !"".equals(hashMap.get("teamId")) &&  !"null".equals(hashMap.get("teamId"))){
//				HashMap<String, Object> hashMapTemp = rankingListService.getPublicRankingInfo(hashMap.get("teamId").toString());
//				String chances = "0"; 
//				if (null != hashMapTemp.get("chances")) {//胜率
//					chances = hashMapTemp.get("chances").toString();
//				}
//				hashMap.put("chances", chances);
//			}else{
//				hashMap.put("chances", null);
//			}
//		}
//	}

	/**
	 * 
	 * 
	   TODO - 用户可发起约战的球队列表
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> myDuelUteam202(HashMap<String, String> map) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		//分页
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		List<HashMap<String, Object>> listTeam = null;
		String uuserUteamStatus = "5";
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			hashMap.put("teamCount", Public_Cache.TEAM_COUNT);
			StringBuffer sql = this.getDuelUteamList_sql;
			listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					this.displayData(hashMap2,map);
					//角标需要的id
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
				}
			}
			//角标
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
			//用户状态
			//查询当前球员是否有资格可以创建球队
			HashMap<String, String> statusMap = this.checkUuserUteamStatus(map);
			uuserUteamStatus = statusMap.get("uuserUteamStatus");
		}
		resultMap.put("cornerList", cornerList);
		resultMap.put("uuserUteamStatus", uuserUteamStatus);
		resultMap.put("listTeam", listTeam);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 用户可发起挑战的球队列表
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> myChallengeUteamList202(HashMap<String, String> map) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		//分页
		List<Object> listTeamId = new ArrayList<Object>();
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		List<HashMap<String, Object>> listTeam = null;
		String uuserUteamStatus = "5";
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			hashMap.put("teamCount", Public_Cache.TEAM_COUNT);
			StringBuffer newsql = new StringBuffer();
//			if (publicService.StringUtil(map.get("brCourtId"))) {//根据球场查询
//				hashMap.put("brCourtId", map.get("brCourtId"));
//				newsql.append( this.getChallengeUteamList_sql + " and c.br_court_id=:brCourtId  group by c.br_court_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
//			}else{//所有可以发起挑战的列表
//				//ios应战没有球队
//				if ("2".equals(map.get("resource"))) {
//					newsql = this.getDuelUteamList_sql;
//				}else{
//					newsql.append( this.getChallengeUteamList_sql + " group by c.br_court_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
//				}
//			}
			//2.0.2补丁 --ios应战没有球队，调错接口
			if (map.containsKey("brCourtId")) {
				if (publicService.StringUtil(map.get("brCourtId"))) {//根据球场查询
					hashMap.put("brCourtId", map.get("brCourtId"));
					newsql.append( this.getChallengeUteamList_sql + " and c.br_court_id=:brCourtId  group by c.br_court_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
				}else{//所有可以发起挑战的列表
					//ios应战没有球队
					newsql.append( this.getChallengeUteamList_sql + " group by c.br_court_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
				}
			}else{
				//ios应战没有球队
				if ("2".equals(map.get("resource"))) {
					newsql = this.getDuelUteamList_sql;
				}else{
					newsql.append( this.getChallengeUteamList_sql + " group by c.br_court_id order by p.default_uteam=1 desc,case when ifnull(p.adddate,'')='' then p.player_id else 1 end desc,p.adddate desc,p.addtime desc,l.rank_role asc ");
				}
			}
			System.out.println("sql="+newsql);
			listTeam = baseDAO.findSQLMap(newsql.toString(),hashMap);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					this.displayData(hashMap2,map);
					//角标需要的id
					String teamId = hashMap2.get("teamId").toString();
					listTeamId.add(teamId);
				}
			}
			//角标
			if (null != listTeamId && listTeamId.size() > 0) {
				cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
			}
			//用户状态
			//查询当前球员是否有资格可以创建球队
			HashMap<String, String> statusMap = this.checkUuserUteamStatus(map);
			uuserUteamStatus = statusMap.get("uuserUteamStatus");
		}
		resultMap.put("cornerList", cornerList);
		resultMap.put("uuserUteamStatus", uuserUteamStatus);
		resultMap.put("listTeam", listTeam);
		return resultMap;
	}

	HashMap<String, Object> hashMap=new HashMap<>();
	int count = 0;

	/**
	 * 
	 * TODO 战队筛选
	 * @param map  teamId-球队Id、loginUserId-用户Id、chances-胜率((约战+挑战)/2)、rank-排名、 avgAge-平均年龄、 avgHeight-平均身高 、avgWeight-平均体重、page-分页
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月4日
	 */
	@Override
	public HashMap<String, Object> findFilterTeam(HashMap<String, String> map) throws Exception {
		HashMap<String,List<Object>> mapList=new HashMap<>();
		List<Object> listStr=null;
		if (map.get("loginUserId") == null) {
			map.put("loginUserId", "");
		}
		HashMap<String, Object> hashMap = PublicMethod.Maps_Mapo(map);
		StringBuffer sql = new StringBuffer("select t.team_id teamId,t.name name,t.team_class teamClass,");
		sql.append("concat(cast(t.avg_age as decimal(18,1)),'') avgAge,");
		sql.append("concat(cast(t.avg_height as decimal(18,1)),'') avgHeight,");
		sql.append("concat(cast(t.avg_weight as decimal(18,1)),'') avgWeight,");
		sql.append("concat(format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,1),'') chances,");
		sql.append("t.remark remark, t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,");
		sql.append("p.number,p.position,(d.all_match_count+l.all_match_count) allMatchCount,t.integral,t.rank,");
		sql.append("(d.ver_match_count+l.ver_match_count) verMatchCount,(d.draw_match_count+l.draw_match_count) drawMatchCount,");
		sql.append("(d.fail_match_count+l.fail_match_count) failMatchCount from u_team t");
		sql.append(" left join u_team_dek d on t.team_id=d.team_id ");
		sql.append(" left join u_team_duel l on t.team_id= l.team_id");
		sql.append(" left join u_team_match m on t.team_id= m.team_id ");
		sql.append(" left join u_team_img i on  t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'");
		sql.append(" left join u_player p on p.team_id=t.team_id ");
		if(map.get("loginUserId")!=null&&!"".equals(map.get("loginUserId"))){
			sql.append(" and p.user_id=:loginUserId ");
		}
		//默认排除条件
		this.teamInfoRemove(sql,map);
		
		// 胜率
		this.filterInfoOption(map, count, sql, map.get("chances"),
				"format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,0) ");
		// 排名－特殊处理未进入，不能使用公共方法
		if (null != map.get("rank")  && !"".equals(map.get("rank"))) {
			String[] rankList = map.get("rank").split(",");
			if (rankList.length > 0) {
				count = 0;
				for (String string : rankList) {
					count++;
					if (count == 1) {
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (!"0".equals(string)) {
						map.put("orderByRank", "rank");
						this.filterInfoOptionPart(map,sql,string,"t.rank ");
					} else {
						sql.append("(t.rank is null ) ");
					}
					if (count >= rankList.length) {
						sql.append(" ) ");
					}

				}
			}
		}
		// 平均年龄
		this.filterInfoOption(map, count, sql, map.get("avgAge"), "format(t.avg_age,0) ");
		// 平均身高
		this.filterInfoOption(map, count, sql, map.get("avgHeight"), "format(t.avg_height,0) ");
		// 平均体重
		this.filterInfoOption(map, count, sql, map.get("avgWeight"), "format(t.avg_weight,0) ");
		// 球队人数
		this.filterInfoOption(map, count, sql, map.get("teamCount"), "t.team_count ");
		// 地区
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			hashMap.put("area", map.get("area"));
			URegion region = baseDAO.get(URegion.class, map.get("area"));
			if (region != null && "2".equals(region.getType())) {
				sql.append(" and t.area in (select _id from u_region where parent=:area ) ");
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
				sql.append(" and t.area in (:area ) ");
			}
		}
		// 擂主
		if (null != map.get("champion")&&!"".equals(map.get("champion"))) {
			if ("1".equals(map.get("champion"))) {// 是擂主
				sql.append(" and t.team_id in(select team_id from u_champion where is_champion='1') ");
			} else {// 不是擂主
				sql.append(" and t.team_id not in(select team_id from u_champion where is_champion='1') ");
			}
		}
		// 加入我们应用时间
		if (null != map.get("createdate")&&!"".equals(map.get("createdate"))) {
			// 今天
			if ("1".equals(map.get("createdate"))) {
				sql.append("and t.createdate = date_format(now(),'%Y-%m-%d')");
			}
			// 7天内
			if ("2".equals(map.get("createdate"))) {
				sql.append("and t.createdate>date_sub(date_format(now(),'%Y-%m-%d'), interval "
						+ Public_Cache.FILTER_TEAM_TIME + " day)");
			}
			// 30天内
			if ("3".equals(map.get("createdate"))) {
				sql.append("and t.createdate>date_sub(date_format(now(),'%Y-%m-%d'), interval "
						+ Public_Cache.FILTER_TEAM_TIMES + " day)");
			}
		}
		this.teamOrderByInfo(map,sql,mapList);// 分组+排序

		List<HashMap<String, Object>> listTeam = this.getPageLimitFilter(sql, hashMap, map,mapList);//分页
		List<Object> listTeamId = new ArrayList<>();
		List<List<CornerBean>> cornerList = new ArrayList<>();
		if (null != listTeam && listTeam.size() > 0) {
			for (HashMap<String, Object> hashMap2 : listTeam) {
				String teamId = hashMap2.get("teamId").toString();
				listTeamId.add(teamId);
				this.displayData(hashMap2,map);//数据处理
//				//填充角色
				if(hashMap2.get("playerId")!=null&&!"".equals(hashMap2.get("playerId"))){
					uPlayerRoleService.setMembertype202(hashMap2);
				}
				
//				//更新胜率－LBS百度上传数据
//				this.editChancesBaidulbs((String)hashMap2.get("teamId"), (String)hashMap2.get("chances"));
			}
		}
		if (null != listTeamId && listTeamId.size() > 0) {
			cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
		}
		// 站队列表用户信息
		hashMap = new HashMap<>();
		// 站队列表用户信息
		OutUteamList outUteamList = this.findUteaminfoListHead(map);
		hashMap.put("outUteamList", outUteamList);
		hashMap.put("outUteamLists", listTeam);
		hashMap.put("cornerList", cornerList);
		return hashMap;
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
	 * 默认排除数据
	 * @param sql
	 * @param map lvlParent－二级城市ID，duelFilter－约战标示
	 * @throws Exception 
	 */
	private void teamInfoRemove(StringBuffer sql, HashMap<String, String> map) throws Exception {
		sql.append(" where team_status='3' and team_use_status='2'  ");
		if(!"rank".equals(map.get("orderByRank"))){
			sql.append(" and t.team_id not in (select team_id from u_team where team_count>50)");//排除满员
		}
		if (publicService.StringUtil(map.get("typeStr"))) {
			if (map.get("duelFilter")!=null&&"uteam".equals(map.get("duelFilter"))) {
				//球队人数限制
				int teamMaximum = Public_Cache.TEAM_MAXIMUM;
				if (0 == teamMaximum) {
					teamMaximum = 50;
				}
				map.put("teamMaximum", teamMaximum+"");
				sql.append( " and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:userId and pp.team_id is not null and pp.in_team='1' )  and t.team_count<:teamMaximum"
						+ " group by t.team_id");
			}else if(map.get("duelFilter")!=null&&"duel".equals(map.get("duelFilter"))) {
				sql.append( " and t.team_count>=7 and t.team_id not in (select pp.team_id from u_player pp,u_player_role r where pp.player_id=r.player_id and pp.user_id=:loginUserId and pp.team_id is not null and pp.in_team='1' and r.member_type='1' and r.member_type_use_status='1') ");
			}else{ //约战筛选执行
				if(!"rank".equals(map.get("orderByRank"))){
					sql.append(" and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:loginUserId  and pp.team_id is not null and pp.in_team='1' ) ");//排除加入队伍和自己队
				}
			}
		}else{
			sql.append(" and t.team_id not in(select team_id from u_team where team_count>50)");//排除满员
			if(map.get("lvlParent")!=null&&!"".equals(map.get("lvlParent"))){//如果有二级城市，那么排除不是该城市的球队
				sql.append(" and t.area not is (select _id from u_region where type='3' and parent=:lvlParent )");
			}
			//约战
			if(map.get("duelFilter")!=null&&"duel".equals(map.get("duelFilter"))){
				if (publicService.StringUtil(map.get("teamId"))) {
					sql.append(" and t.team_id not in (:teamId) ");
				}
				sql.append(" and t.team_count>=7 and t.team_id not in (select pp.team_id from u_player pp,u_player_role r where pp.player_id=r.player_id and pp.user_id=:loginUserId and pp.team_id is not null and pp.in_team='1' and r.member_type='1' and r.member_type_use_status='1') ");
			}else{ //约战筛选执行
				if(!"rank".equals(map.get("orderByRank"))){//天梯不排除自己的球队
					if (publicService.StringUtil(map.get("teamId"))) {
						sql.append(" and t.team_id not in (:teamId) ");
					}
//					sql.append(" and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:loginUserId  and pp.team_id is not null and pp.in_team='1' ) ");//排除加入队伍和自己队
				}
			}
		}
		
		
	}
	/**
	 * 球队排序
	 * @param map
	 * @param sql
	 */
	private void teamOrderByInfo(HashMap<String, String> map, StringBuffer sql,HashMap<String, List<Object>> mapList) throws Exception {
//		String locationSql= "";
		sql.append(" group by t.team_id order by ");// 分组
		//排序
		if(map.get("orderByTeam")!=null&&!"".equals(map.get("orderByTeam"))){
			//加入时间
			if("1".equals(map.get("orderByTeam"))){
//				sql.append(" abs(datediff(t.createdate,date_format(now(),'%Y-%m-%d'))),t.team_id ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			}
			//离我最近
			if("2".equals(map.get("orderByTeam"))){
//				if(map.get("region")!=null&&!"".equals(map.get("region"))){
//					locationSql=this.checkScreen(map,"team");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(t.team_id,"+locationSql+"),");
//					}
//				}else{
//					locationSql=this.lbsOrderSql(map,"team");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(t.team_id,"+locationSql+"),");
//					}
//				}
//				sql.append(" t.createdate desc,t.createtime desc,t.team_id ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			}
			//球队活跃
			if("3".equals(map.get("orderByTeam"))){
				sql.append(" t.team_id ");
			}
			//激数排名
			if("4".equals(map.get("orderByTeam"))){
				sql.append(" case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,t.team_id ");
			}
			//地区
			if("5".equals(map.get("orderByTeam"))){
				sql.append(" t.area desc,t.team_id ");
			}
			//胜率
			if("6".equals(map.get("orderByTeam"))){
				sql.append(" cast(format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,1)as signed) desc,t.team_id ");
			}
			//平均年龄
			if("7".equals(map.get("orderByTeam"))){
				sql.append(" t.avg_age+0 desc,t.team_id ");
			}
			//人数
			if("8".equals(map.get("orderByTeam"))){
				sql.append(" t.team_count+0 desc,t.team_id ");
			}
			//是否是擂主
			if("9".equals(map.get("orderByTeam"))){
				sql.append(" t.team_id in(select team_id from u_champion where is_champion='1') desc,t.team_id ");
			}
		}else{
			// 如果有排名就按排名排序或者天梯筛选列表排序
			if (map.get("orderByRank") != null && "rank".equals(map.get("orderByRank"))) {
//				sql.append(" case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,abs(datediff(t.createdate,date_format(now(),'%Y-%m-%d'))),t.team_id ");
				sql.append(" case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			} else if (map.get("orderByRank") != null && "fduel".equals(map.get("orderByRank"))) {//发起约战－选择对手 离我最近
//				if(map.get("region")!=null&&!"".equals(map.get("region"))){
//					locationSql=this.checkScreen(map,"team");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(t.team_id,"+locationSql+"),");
//					}
//				}else{
//					locationSql=this.lbsOrderSql(map,"team");
//					if(locationSql!=null&&!"".equals(locationSql)){
//						sql.append("field(t.team_id,"+locationSql+"),");
//					}
//				}
//				sql.append(" t.createdate desc,t.createtime desc,t.team_id ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			}else if (map.get("orderByRank") != null && "uteam".equals(map.get("orderByRank"))) {//发起约战－选择对手 离我最近
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
//				sql.append(" t.recommend_team=1 desc,abs(datediff(t.createdate,date_format(now(),'%Y-%m-%d'))) desc,t.team_count>=7 desc,t.team_count<7 desc,p.user_id asc ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			} else{
//				sql.append(" abs(datediff(t.createdate,date_format(now(),'%Y-%m-%d'))),case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,t.team_id  ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,t.team_id  ");
			}
		}
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
//		if(orderSql.length()>0){
//			orderSql=orderSql.substring(0, orderSql.length()-1);
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
//		if(orderSql.length()>0){
//			orderSql=orderSql.substring(0, orderSql.length()-1);
//		}
//		return orderSql;
//	}

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
		
	}

	/**
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
	private List<HashMap<String, Object>> getPageLimitFilter(StringBuffer sql, HashMap<String, Object> hashMap,
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
	 * 
	 * @TODO 新增球队区域百度云LBS数据＋修改 updTeamBaiduLBSData
	 * @Title: insTeamBaiduLBSData 
	 * @param map 
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年6月29日 下午7:30:30
	 */
	@Override
	public void insOrUpdTeamBaiduLBSData(HashMap<String, String> map,UTeam team) throws Exception{
		if(null!=map.get("address")&&null!=map.get("teamId")){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("address", map.get("address"));
			hashMap.put("url",Public_Cache.LBS_LOCATION);
			hashMap.put("output", "json");//数据的返回格式
			hashMap.put("object_id",map.get("teamId"));
				//获得经纬度
				HashMap<String, Object> localhostHashMap=this.getLocalhost(hashMap);
				if(localhostHashMap.get("status")!=null&&"0".equals(localhostHashMap.get("status"))){
					this.addOrEditBaidulbs(hashMap,localhostHashMap,map,team);//上报LBS百度数据＋修改
				}else{
					lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP根据地址:"+hashMap.get("address")+"获取不到经纬度");
				}
		}
	}
	
	/**
	 * 
	 * TODO 上报LBS百度数据＋修改
	 * @param localhostHashMap 经纬度数据 lat－纬度 lng－经度
	 * @param map teamId－球队ID
	 * @param team 球队对象
	 * @throws Exception
	 * xiaoying 2016年7月21日
	 */
	private void addOrEditBaidulbs(HashMap<String, Object>  hashMap,HashMap<String, Object> localhostHashMap, HashMap<String, String> map, UTeam team) throws Exception{
		baseDAO.getSessionFactory().getCurrentSession().flush();
		// 创建位置数据表数据
		hashMap.put("title","球队数据");
		hashMap.put("tags","team");
		hashMap.put("coord_type",3);
		hashMap.put("geotable_id",Constant.BAIDU_TEAM_TABLE_ID);
		hashMap.put("latitude", localhostHashMap.get("lat"));//用户上传的纬度
		hashMap.put("longitude", localhostHashMap.get("lng"));//用户上传的经度
		hashMap.put("date",new Date());
		hashMap.put("params_type","2");
		//上报数据到LBS
		map.put("id", map.get("teamId"));
		hashMap.put("team_intid",publicService.getIntKeyId(2, map));
		hashMap.put("areaid",team.getuRegion().get_id());//地区ID
//		hashMap.put("win",0.0);//胜率
		hashMap.put("pm",team.getRank()==null?0:team.getRank());//排名
		hashMap.put("avg_age",team.getAvgAge());//平均年龄
		hashMap.put("count",team.getTeamCount());//球队人数
		UChampion champion =baseDAO.get(map,"from UChampion where isChampion='1' and UTeam.teamId=:teamId");
		hashMap.put("is_l",champion!=null?1:2);//是否是擂主，1是,2不是
		hashMap.put("teamid",map.get("teamId"));
		//获取位置数据表数据 id
		UBaidulbs baidulbs=lbsService.getBaidulbs("2",map.get("teamId"));
		String lbsDataStr = "";
		if (baidulbs != null && !"".equals(baidulbs.getLbsData())) {// 修改－上传LBS百度数据
			hashMap.put("id", baidulbs.getLbsData());
			try {
				lbsDataStr = lbsService.updateGeodata(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
				lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP球队更新失败");
			}
		} else {// 上传LBS百度数据
			hashMap.put("win", 0.0);// 胜率
			lbsDataStr = lbsService.createGeodata(hashMap);
			hashMap.put("add", "1");
			try {
				lbsDataStr = lbsService.updateGeodata(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
				lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP球队上报失败");
			}
		}
		
		//ret不为空，添加本地数据
		if (!"".equals(lbsDataStr)&&"1".equals(hashMap.get("add"))) {
			HashMap<String, Object> geotableHashMap=lbsService.packLbsDateCreateGeodata(lbsDataStr);
			if (geotableHashMap.get("status")!=null&&"0".equals(geotableHashMap.get("status"))) {
				lbsService.createUbaidulbsData(map.get("teamId"),"2",(String)geotableHashMap.get("id"));
			}else{
				lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP球队上报失败");
			}
		}else{
			if(hashMap.get("add")!=null){
				lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP球队上报失败");
			}else{
				lbsService.createUbaidulbsDataError(map.get("teamId"), "2", "APP球队更新失败");
			}
		}
	}
	
	/**
	 * 
	 * TODO 根据地区获取经纬度
	 * @param hashMap output－json数据返回，url－球场数据的地址，address-地址
	 * @return localhostHashMap lat－纬度，lng－经度
	 * @throws Exception
	 * xiaoying 2016年6月30日
	 */
	private HashMap<String, Object> getLocalhost(HashMap<String, Object> hashMap)  throws Exception{
		HashMap<String, Object> hashMap2=new HashMap<>();
		try {
			String location=lbsService.addressToApi(hashMap);//经纬度获取 json格式的
			if(!"".equals(location)){
				return lbsService.packLbsDateAddress(location);//处理过的经纬度
			}else{
				//获取经纬度失败
				lbsService.createUbaidulbsDataError((String)hashMap.get("object_id"), "2", "APP根据地址:"+hashMap.get("address")+"获取不到经纬度");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//获取经纬度失败
			lbsService.createUbaidulbsDataError((String)hashMap.get("object_id"), "2", "APP根据地址:"+hashMap.get("address")+"获取不到经纬度");
		
		}
		return hashMap2;
	}
	
	@Override
	public void editChancesBaidulbs(String teamId,String chances) throws Exception{
		baseDAO.getSessionFactory().getCurrentSession().flush();
		// 创建位置数据表数据
		hashMap.put("geotable_id",Constant.BAIDU_TEAM_TABLE_ID);
		hashMap.put("url",Public_Cache.LBS_LOCATION);
		hashMap.put("output", "json");//数据的返回格式
		//上报数据到LBS
		hashMap.put("win",chances);//胜率
		//获取位置数据表数据 id
		UBaidulbs baidulbs=lbsService.getBaidulbs("2",teamId);
		String lbsDataStr = "";
		if (baidulbs != null && !"".equals(baidulbs.getLbsData())) {// 修改－上传LBS百度数据
			hashMap.put("id", baidulbs.getLbsData());
			try {
				lbsDataStr = lbsService.updateGeodata(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
				lbsService.createUbaidulbsDataError(teamId, "2", "APP球队更新胜率失败本地数据");
			}
		}
		//ret为空，更新胜率失败本地数据
		if ("".equals(lbsDataStr)) {
			lbsService.createUbaidulbsDataError(teamId, "2", "APP球队更新胜率失败本地数据");
		}
	}

//	/**
//	 * 
//	 * @TODO 修改球队区域百度云LBS数据
//	 * @Title: updTeamBaiduLBSData 
//	 * @param map
//	 * @throws Exception 
//	 * @author charlesbin
//	 * @date 2016年6月29日 下午7:30:44
//	 */
//	@Override
//	public void updTeamBaiduLBSData(HashMap<String, String> map)throws Exception{
//		if(null!=map.get("address")&&null!=map.get("teamId")){
//			HashMap<String, Object> hashMap = new HashMap<String, Object>();
//			hashMap.put("address", map.get("address"));
//			hashMap.put("url",Public_Cache.LBS_LOCATION);
//			
////			try {
//				//获得经纬度
//				String lbsApiStr = lbsService.addressToApi(hashMap);
//				JSONObject json = new JSONObject(lbsApiStr);
//				json.getString("result");
//				
//				Document document = DocumentHelper.parseText(json.getString("result"));
//				Element element = document.getRootElement();
//				Element result = element.element("result");
//				Element location = result.element("location");
//				
//				//获得用户对应百度LBS数据表ID
//				UBaidulbs uBaidulbs = null;
//				uBaidulbs = baseDAO.get(map,"from UBaidulbs where objectId = :teamId ");
//				if(null!=uBaidulbs){
//					// 修改位置数据表数据
//					hashMap.put("id",uBaidulbs.getLbsData());
//					hashMap.put("title","球队数据");
//					hashMap.put("tags","team");
//					hashMap.put("coord_type",3);
//					hashMap.put("geotable_id",Constant.BAIDU_TEAM_TABLE_ID);
//					hashMap.put("latitude", Double.parseDouble(location.elementText("lat")));
//					hashMap.put("longitude", Double.parseDouble(location.elementText("lng")));
//					
//					hashMap.put("object_id",map.get("teamId"));
//					hashMap.put("date",new Date());
//					map.put("id", map.get("teamId"));
//					hashMap.put("team_intid",publicService.getIntKeyId(2, map));
//					hashMap.put("params_type","2");
//					
//					//获取位置数据表数据 id
//					lbsService.updateGeodata(hashMap);
////					String lbsUpdStr = lbsService.updateGeodata(hashMap);
////					json = new JSONObject(lbsUpdStr);
////					json.getString("result");
////					json = new JSONObject(json.getString("result"));
////					System.out.println(json);
//				}
////			} catch (Exception e) {
////				// TODO: handle exception
////				e.printStackTrace();
////			}
//			
//		}
//	}
	/**
	 * 
	 * 
	   TODO - 赛事导入球队球员数据
	   @param map
	   		phone		手机号
	   		realName	真实姓名
	   		nickName	昵称
	   		sex			性别
	   		birthday	出生年月
	   		height		身高
	   		weight		体重
	   		area		区域
	   @return
	   @throws Exception
	   2016年7月7日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> entryUteaminfo(HashMap<String, String> map,
			List<Map<String, Object>> playerList) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap1 = new HashMap<>();
		UUser uUser = null;
		UPlayer uPlayer = null;
		UTeam uTeam = null;
		List<Map<String, Object>> players = new ArrayList<>();
		if (publicService.StringUtil(map.get("phone"))) {
			uUser = uuserService.getUserinfoByPhone(map);
			if (null != uUser) {
				if (publicService.StringUtil(map.get("uUserImgurl"))) {
					uUserImgService.uploadHeadPicByEvents(map, uUser);
				}
				//添加初始球员数据
				uplayerService.setNoTeamPlayerInfoByUser(map, uUser);
				//新建球队信息
				uTeam = (UTeam)this.createUTeamByEvent(uUser, map).get("uTeam");
				if (null != playerList && playerList.size() > 0) {
					for (Map<String, Object> map2 : playerList) {
						hashMap1 = new HashMap<>();
						HashMap<String, String> paramMap = this.setHashMap(map2);
						if (publicService.StringUtil(paramMap.get("phone"))) {
							uUser = uuserService.getUserinfoByPhone(paramMap);
							if (null != uUser) {
								if (null != paramMap.get("uUserImgurl")) {
									uUserImgService.uploadHeadPicByEvents(paramMap, uUser);
								}
							}
							//更新球员信息
							if (null != uTeam) {
								map.put("teamId", uTeam.getTeamId());
							}
							uplayerService.setNoTeamPlayerInfoByUser(paramMap,uUser);
							if (publicService.StringUtil(map.get("phone"))) {
								String createPhone = map.get("phone");
								if (createPhone.equals(paramMap.get("phone"))) {
									paramMap.put("memberType", "1");
								}
							}
							//更新球员信息
							if (null != uTeam) {
								uPlayer = uplayerService.setInTeamPlayerInfoByUser(paramMap, uUser, uTeam);
								if (null != uPlayer) {
									hashMap1.put("eventsPlayerId", paramMap.get("eventsPlayerId"));
									hashMap1.put("playerId", uPlayer.getPlayerId());
									resultMap.put("teamId", uTeam.getTeamId());
								}else{
									return WebPublicMehod.returnRet("error", "当前球员的背号在该队中已经存在！");
								}
							}
						
						}
						players.add(hashMap1);
					}
				}
			}
		}
		resultMap.put("eventsTeamId", map.get("eventsTeamId"));
		resultMap.put("playerLists", players);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 球员数据
	   @param map
	   		phone		手机号
	   		realName	真实姓名
	   		nickName	昵称
	   		sex			性别
	   		birthday	出生年月
	   		height		身高
	   		weight		体重
	   		area		区域
	   		number			背号
	   		position		位置
	   		memberType		角色
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @return
	   2016年7月20日
	   dengqiuru
	 */
	private HashMap<String, String> setHashMap(Map<String, Object> map) {
		HashMap<String, String> hashMap = new HashMap<>();
		if (null != map) {
			//手机号
			if (null != map.get("phone")) {
				hashMap.put("phone", map.get("phone").toString());
			}
			//真实姓名
			if (null != map.get("realName")) {
				hashMap.put("realName", map.get("realName").toString());
			}
			//昵称
			if (null != map.get("nickName")) {
				hashMap.put("nickName", map.get("nickName").toString());
			}
			//性别
			if (null != map.get("sex")) {
				hashMap.put("sex", map.get("sex").toString());
			}
			//出生年月
			if (null != map.get("birthday")) {
				hashMap.put("birthday", map.get("birthday").toString());
			}
			//身高
			if (null != map.get("height")) {
				hashMap.put("height", map.get("height").toString());
			}
			//体重
			if (null != map.get("weight")) {
				hashMap.put("weight", map.get("weight").toString());
			}
			//区域
			if (null != map.get("area")) {
				hashMap.put("area", map.get("area").toString());
			}
			//头像
			if (null != map.get("uUserImgurl")) {
				hashMap.put("uUserImgurl", map.get("uUserImgurl").toString());
			}
			//惯用脚
			if (null != map.get("practiceFoot")) {
				hashMap.put("practiceFoot", map.get("practiceFoot").toString());
			}
			//擅长位置
			if (null != map.get("expertPosition")) {
				hashMap.put("expertPosition", map.get("expertPosition").toString());
			}
			//可踢位置
			if (null != map.get("canPosition")) {
				hashMap.put("canPosition", map.get("canPosition").toString());
			}
			//背号
			if (null != map.get("number")) {
				hashMap.put("number", map.get("number").toString());
			}
			//位置
			if (null != map.get("position")) {
				hashMap.put("position", map.get("position").toString());
			}
			//角色
			if (null != map.get("memberType")) {
				hashMap.put("memberType", map.get("memberType").toString());
			}
			//赛事系统
			if (null != map.get("eventsPlayerId")) {
				hashMap.put("eventsPlayerId", map.get("eventsPlayerId").toString());
			}
		}
		return hashMap;
	}
	/**
	 * 
	 * 
	   TODO - 赛事外包导入球队信息
	   @param uUser
	   @param map
	   		imgurl			球队队徽
	   		holddate		成立日期
	   		teamName		球队名称
	   		shortName		球队简称
	   		area			球队区域
	   		teamClass		球队分类
	   		homeTeamShirts	主队球衣
	   		awayTeamShirts	客队球衣
	   		subcourtId		主场Id
	   		remark			简介
	   @return
	   2016年7月13日
	   dengqiuru
	 * @throws Exception 
	 */
	private HashMap<String, Object> createUTeamByEvent(UUser uUser, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != uUser) {
			map.put("userId", uUser.getUserId());
			StringBuffer sql = new StringBuffer("from UTeam where UUser.userId=:userId");
			if (publicService.StringUtil(map.get("teamName"))) {
				sql.append(" and name = :teamName ");
			}
			UTeam uTeam = baseDAO.get(map, sql.toString());
			if (null != uTeam) {
				map.put("teamId", uTeam.getTeamId());
				//不为空，修改信息
				boolean isDoubleName = this.isDoubleName(map);
				if (isDoubleName == false) {
					uTeam = this.updateUTeamByEvents(uTeam,null, map);
					baseDAO.getSessionFactory().getCurrentSession().flush();
					if (publicService.StringUtil(map.get("holddate"))) {
						//用户首次建立球队
						this.setBehaviorHoldDate(uTeam,map);
					}
					if (null == uTeam) {
						return WebPublicMehod.returnRet("error","主队球衣颜色不能与客队颜色一致！");
					}
				}else{
					return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
				}
			}else{
				//验证球队全名称是否重复
				map.put("teamId", "");
				boolean isDoubleName = this.isDoubleName(map);
				if (isDoubleName == false) {
					uTeam = this.createUTeamByEvents(uUser,map);
					if (null != uTeam) {
						map.put("teamId", uTeam.getTeamId());
						//新建球队后，在球员表里新增队长记录
						String memberType = "1";
						uplayerService.insertTeamLeader(uUser,uTeam,memberType,map);
						//新增球队后，添加队徽
						if (publicService.StringUtil(map.get("imgurl"))) {
							uteamImgService.updateTeamLogoByEvents(map);
						}
						//用户首次建立球队
						this.setBehavior(uTeam,map);
						this.setUserBehaviorTypeParam(map, uTeam,uUser);
					}else{
						return WebPublicMehod.returnRet("error","主队队衣颜色不能与客队队衣颜色一致！");
					}
				}else{
					return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
				}
			}
			resultMap.put("uTeam", uTeam);
			this.setAreaToBaiduLBSCreateTeam(uTeam, map);//上传或修改LBS百度数据
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 新增球队
	   @param uUser
	   @param map 
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  shortName		简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
	   @return
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	private UTeam createUTeamByEvents(UUser uUser,HashMap<String, String> map) throws Exception {
		//将传过来的成立日期转Date
		String type = "1";//验证主队队衣颜色是否选择
		//新增球队数据
		UTeam uTeam = new UTeam();
		uTeam.setTeamId(WebPublicMehod.getUUID());//球队全名
		if (publicService.StringUtil(map.get("teamName"))) {
			uTeam.setName(map.get("teamName"));
		}
		//球队简称
		if (publicService.StringUtil(map.get("shortName"))) {
			uTeam.setShortName(map.get("shortName"));
		}
		//所在区域1
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uTeam.setuRegion(uRegion);//区域
		}
		//球队分类 : 俱乐部、FC、公司
		if (publicService.StringUtil(map.get("teamClass"))) {
			uTeam.setTeamClass(map.get("teamClass"));
		}
		//主队球衣
		if (publicService.StringUtil(map.get("homeTeamShirts"))) {
			type = "2";//选择了主队球衣，在客队时，判断主队与客队颜色是否一致
			uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
		}
		//客队球衣
		if (publicService.StringUtil(map.get("awayTeamShirts"))) {
			if ("2".equals(type)) {
				if (map.get("homeTeamShirts").equals(map.get("awayTeamShirts"))) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
				}
			}
		}
		//常驻球场
		if (publicService.StringUtil(map.get("subcourtId"))) {
			uTeam.setuBrCourt(uCourtService.getUCourt(map.get("subcourtId")));
		}
		//简介
		if (publicService.StringUtil(map.get("remark"))) {
			uTeam.setRemark(map.get("remark"));
		}
		if (publicService.StringUtil(map.get("holddate"))) {
			Date holdDate = PublicMethod.getStringToDate( map.get("holddate"), "yyyy-MM-dd");
			uTeam.setHoldDate(holdDate);//成立日期
		}
//		String createDate = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd");
		uTeam.setCreatedate(new Date());//创建日期
		uTeam.setCreatetime(new Date());//创建时间
		uTeam.setCreateQd("第三方系统");
		baseDAO.save(uTeam);
		this.setUteamParams(map,uTeam,uUser);//建队是设置球队表里的基础信息
		return uTeam;
	}
	/**
	 * 
	 * 
	   TODO - 将赛事传过来的球队名的分类去掉，并返回
	   @param map
	   		teamName		球队名称
	   @return
	   2016年7月13日
	   dengqiuru
	 */
//	private String getRealName(HashMap<String, String> map) {
//		String teamClassName1 = Public_Cache.HASH_PARAMS("team_class").get("1");
//		String teamClassName2 = Public_Cache.HASH_PARAMS("team_class").get("2");
//		String teamClassName3 = Public_Cache.HASH_PARAMS("team_class").get("3");
//		//获取赛事的球队名
//		String teamRealName = map.get("teamName");
//		//查询这些球队名是否包含球队分类
//		if(teamRealName.contains(teamClassName1)){
//			teamRealName = teamRealName.replace(teamClassName1, "");
//		}else if (teamRealName.contains(teamClassName2)) {
//			teamRealName = teamRealName.replace(teamClassName2, "");
//		}else if(teamRealName.contains(teamClassName3)) {
//			teamRealName = teamRealName.replace(teamClassName3, "");
//		}
//		return teamRealName;
//	}
	/**
	 * 
	 * 
	   TODO - 赛事系统修改球队信息
	   @param map
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  shortName		简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
	   @return
	   @throws Exception
	   2016年7月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updateUteamInfoByEvents(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		//根据id查询球队信息
		UTeam uTeam = baseDAO.get(map, "from UTeam where teamId=:teamId");
		UUser uUser = null;
		if (null != uTeam) {//不为空，修改信息
			boolean isDoubleName = this.isDoubleName(map);
			if (isDoubleName == false) {
//				this.setAreaToBaiduLBS(uTeam, map);
				uTeam = this.updateUTeamByEvents(uTeam,null, map);
				baseDAO.getSessionFactory().getCurrentSession().flush();
				if (publicService.StringUtil(map.get("holddate"))) {
					//用户首次建立球队
					this.setBehaviorHoldDate(uTeam,map);
				}
				if (null == uTeam) {
					return WebPublicMehod.returnRet("error","主队球衣颜色不能与客队颜色一致！");
				}
			}else{
				return WebPublicMehod.returnRet("error","球队名称不能与其他球队的名称一致！");
			}
		}else{//为空，新增球队信息
			if (publicService.StringUtil(map.get("phone"))) {
				uUser = uuserService.getUserinfoByPhone(map);
				if (null != uUser) {
					if (publicService.StringUtil(map.get("uUserImgurl"))) {
						uUserImgService.uploadHeadPicByEvents(map, uUser);
					}
				}
				uTeam = (UTeam)this.createUTeamByEvent(uUser, map).get("uTeam");
			}else{
				return WebPublicMehod.returnRet("error","请输入创建人的手机号");
			}
		}
		resultMap.put("eventsTeamId", map.get("eventsTeamId"));
		resultMap.put("teamId", uTeam.getTeamId());
		this.setAreaToBaiduLBSCreateTeam(uTeam, map);//上传或修改LBS百度数据
		return resultMap;
		
	}

	/**
	 * 
	 * 
	   TODO - 修改球队(赛事系统)
	   @param uUser
	   @param map
	   		imgurl			球队队徽
	   		holddate		成立日期
	   		teamName		球队名称
	   		shortName		球队简称
	   		area			球队区域
	   		teamClass		球队分类
	   		homeTeamShirts	主队球衣
	   		awayTeamShirts	客队球衣
	   		subcourtId		主场Id
	   		remark			简介
	   @return
	   @throws Exception
	   2016年3月18日
	   dengqiuru
	 */
	private UTeam updateUTeamByEvents(UTeam uTeam,UUser uUser,HashMap<String, String> map) throws Exception {
		if (publicService.StringUtil(map.get("imgurl"))) {
			uteamImgService.updateTeamLogoByEvents(map);
		}
		//将传过来的成立日期转Date
		if (publicService.StringUtil(map.get("holddate"))) {
			Date holdDate = PublicMethod.getStringToDate( map.get("holddate"), "yyyy-MM-dd");
			uTeam.setHoldDate(holdDate);
		}
		//球队全名
		if (publicService.StringUtil(map.get("teamName"))) {
			uTeam.setName(map.get("teamName"));
		}
		//球队简称
		if (publicService.StringUtil(map.get("shortName"))) {
			uTeam.setShortName(map.get("shortName"));
		}
		//所在区域1
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uTeam.setuRegion(uRegion);//区域
		}
		//球队分类 : 俱乐部、FC、公司
		if (publicService.StringUtil(map.get("teamClass"))) {
			uTeam.setTeamClass(map.get("teamClass"));
		}
		//主队球衣
		if (publicService.StringUtil(map.get("homeTeamShirts"))) {
			if (publicService.StringUtil(uTeam.getAwayTeamShirts())) {//判断客队球衣是否与主队球衣是否一致
				if (map.get("homeTeamShirts").equals(uTeam.getAwayTeamShirts())) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
				}
			}else{
				uTeam.setHomeTeamShirts(map.get("homeTeamShirts"));
			}
		}
		//客队球衣
		if (publicService.StringUtil(map.get("awayTeamShirts"))) {
			if (publicService.StringUtil(uTeam.getHomeTeamShirts())) {//判断客队球衣是否与主队球衣是否一致
				if (map.get("awayTeamShirts").equals(uTeam.getHomeTeamShirts())) {
					uTeam = null;
					return uTeam;
				}else{
					uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
				}
			}else{
				uTeam.setAwayTeamShirts(map.get("awayTeamShirts"));
			}
		}
		//常驻球场
		if (publicService.StringUtil(map.get("subcourtId"))) {
			UBrCourt uBrCourt = uCourtService.getUCourt(map.get("subcourtId"));
			uTeam.setuBrCourt(uBrCourt);

		}
		//简介
		if (publicService.StringUtil(map.get("remark"))) {
			uTeam.setRemark(map.get("remark"));
		}
		uTeam.setTeamStatus("3");
		baseDAO.update(uTeam);
		return uTeam;
	}

	/**
	 * 
	 * @TODO 战队关注数
	 * @Title: pubGetUTeamFollowCount 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月25日 下午9:05:15
	 */
	@Override
	public HashMap<String, Object> pubGetUTeamFollowCount(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		
		List<HashMap<String, Object>> followList = new ArrayList<HashMap<String, Object>>();
		followList = baseDAO.findSQLMap(map, "select * from u_follow where user_follow_type = '2' and object_id = :teamId and follow_status = '1' ");
//		followList = baseDAO.findSQLMap(map, "select count(*) as count from u_follow where user_follow_type = 2 and object_id = :teamId and follow_status = '1' ");
		if(followList.size()>0){
			resultMap.put("followCount", followList.size());
		}else{
			resultMap.put("followCount", 0);
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @TODO 球队数据更新至百度云【跑批】
	 * @Title: pubUpdAllTeamLBS 
	 * @param teamId
	 * @param chances
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月27日 下午4:25:18
	 */
	@Override
	public void pubUpdAllTeamLBS(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select * from u_team where area is not null ");
//		List<HashMap<String, Object>> teamList = new ArrayList<HashMap<String, Object>>();
		List<UTeam> teamsList = new ArrayList<UTeam>();
		teamsList = baseDAO.find(" from UTeam where UTeam.uRegion is not null ");
		
		if(0<teamsList.size()){
//			int teamSize = teamsList.size();
//			System.out.println("总数="+teamSize);
				// 计算限制页数
//				Double a = 1.0*teamSize/300;
//				Double b = Math.ceil(a);
//				Integer pageNum = b.intValue();
			for(UTeam team : teamsList){
				//更新球队
				this.setAreaToBaiduLBSCreateTeam(team,map);
			}
			
		}
	}
	
	/**
	 * TODO - 获取球员荣誉墙最大的排序值
	 * @param hashMap
	 * @return
	 */
	private Integer getMaxHonorNum(HashMap<String,String> map) throws Exception {
		String hql = "select max(num) from UTeamHonor where teamId=:teamId";
		List<Object> list = this.baseDAO.find(map,hql);
		if(list.get(0)==null)
			return 0;
		return Integer.parseInt(list.get(0).toString())+1;
	}
	
	/**
	 * 
	 * @TODO 添加球队荣誉墙
	 * @Title: insUTeamHonor 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月28日 下午3:31:04
	 */
	@Override
	public HashMap<String, Object> insUTeamHonor(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		if(null!=map.get("remark")&&!"".equals(map.get("remark"))&&null!=map.get("teamId")&&!"".equals(map.get("teamId"))&&null!=map.get("honorDate")&&!"".equals(map.get("honorDate"))){
			UTeamHonor uHonor = new UTeamHonor();
			uHonor.setKeyId(WebPublicMehod.getUUID());
			uHonor.setTeamId(map.get("teamId"));
			uHonor.setRemark(map.get("remark"));
			uHonor.setCreatedate(new Date());
			uHonor.setHonorDate(PublicMethod.getStringToDate(map.get("honorDate"),"yyyy-MM-dd"));
			uHonor.setNum(this.getMaxHonorNum(map));
			baseDAO.save(uHonor);
			resultMap = WebPublicMehod.returnRet("success","新增球队荣誉成功");
		}else{
			resultMap =  WebPublicMehod.returnRet("error","请求参数不全");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @TODO 封装格式化个人荣誉
	 * @Title: formatterPlayerHonorList 
	 * @param honors
	 * @param offset
	 * @return
	 * @throws ParseException
	 * @author charlesbin
	 * @date 2016年8月3日 下午8:33:33
	 */
	private List<Map<String, Object>> formatterPlayerHonorList(List<UTeamHonor> honors, int offset)
			throws ParseException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (UTeamHonor honor : honors) {
			offset++;
			map = new HashMap<String, Object>();
			map.put("keyId", honor.getKeyId());
			map.put("remark", honor.getRemark());
			map.put("createDate", honor.getCreatedate());
			map.put("num", honor.getNum());
			map.put("honorDateE", honor.getHonorDateEStr());
			map.put("honorDateC", honor.getHonorDateCStr());
			map.put("index", offset);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 
	 * @TODO 战队概况 获取球队荣誉墙
	 * @Title: getUteamHonorByGK 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月11日 下午3:40:34
	 */
	public List<Map<String, Object>> getUteamHonorByGK(HashMap<String, String> map) throws Exception {
		List<Map<String, Object>> honors = new ArrayList<Map<String, Object>>();
		if(null!=map.get("teamId")&&!"".equals(map.get("teamId"))){
			String hql = "from UTeamHonor where teamId = :teamId order by num desc ";
//			Session session = baseDAO.getSessionFactory().openSession();
//			Query query = session.createQuery(hql);
//			query.setFirstResult(0);  
//            query.setMaxResults(5); 
//            honors = query.list();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("teamId", map.get("teamId"));
			PageLimit pageLimit = new PageLimit(StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt(map.get("page")), 0);
			pageLimit.setLimit(5);
			List<UTeamHonor> honorList = this.baseDAO.findByPage(hql, hashMap, pageLimit.getLimit() ,pageLimit.getOffset());
			honors = this.formatterPlayerHonorList(honorList, pageLimit.getOffset());
		}
		return honors;
	}
	
	/**
	 * 
	 * @TODO 查询球队荣誉墙
	 * @Title: getUteamHonor 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月28日 下午3:31:08
	 */
	@Override
	public HashMap<String, Object> getUteamHonor(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		
		if(null!=map.get("teamId")&&!"".equals(map.get("teamId"))){
			
			PageLimit pageLimit = new PageLimit(StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt(map.get("page")), 0);
			String hql = "from UTeamHonor where teamId = :teamId order by num desc ";
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("teamId", map.get("teamId"));
			List<UTeamHonor> honors = this.baseDAO.findByPage(hql, hashMap, pageLimit.getLimit() ,pageLimit.getOffset());
			resultMap.put("honor", this.formatterPlayerHonorList(honors, pageLimit.getOffset()));
//			List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
			
//			List<UTeamHonor> honorList = new ArrayList<UTeamHonor>();
//			honorList = baseDAO.find(map, " from UTeamHonor where teamId = :teamId ");
//			if(honorList.size()>0){
//				for(int i = 0;i<honorList.size();i++){
//					
//					HashMap<String,Object> hashmap = new HashMap<String,Object>();
//					hashmap.put("keyId",honorList.get(i).getKeyId());
//					hashmap.put("remark",honorList.get(i).getRemark());
//					hashmap.put("createDate",honorList.get(i).getCreatedate());
//					hashmap.put("num",honorList.get(i).getNum());
//					hashmap.put("honorDateE",honorList.get(i).getHonorDateEStr());
//					hashmap.put("honorDateC",honorList.get(i).getHonorDateCStr());
//					hashmap.put("index",i);
//					resultList.add(hashmap);
//				}
//				resultMap.put("", value);
//			}
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @TODO 修改球队荣誉墙
	 * @Title: updUteamHonor
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月3日 下午8:43:40
	 */
	@Override
	public HashMap<String, Object> updUteamHonor(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		// 检查是否有球员荣誉
		if (StringUtils.isEmpty(map.get("keyId")))
			return WebPublicMehod.returnRet("error", "未找到球队荣誉");
		UTeamHonor honor = baseDAO.get(UTeamHonor.class, map.get("keyId"));
		if (honor == null)
			return WebPublicMehod.returnRet("error", "未找到球队荣誉");
		// 检查球员荣誉描述是否为空
		if (StringUtils.isEmpty(map.get("remark"))){
			return WebPublicMehod.returnRet("error", "荣誉墙事件描述不能为空");
		}
		if(StringUtils.isEmpty(map.get("honorDate"))){
			return WebPublicMehod.returnRet("error","荣誉时间不能为空");
		}

		//荣誉发生时间
		honor.setHonorDate(PublicMethod.getStringToDate(map.get("honorDate"),"yyyy-MM-dd"));
		honor.setRemark(map.get("remark"));
		baseDAO.update(honor);
//		HashMap<String, Object> retMap = new HashMap<String, Object>();
//		retMap.put("honor", honor);
		return WebPublicMehod.returnRet("success","修改球队荣誉成功");
	}
	
	/**
	 * 
	 * @TODO 删除球队荣誉墙
	 * @Title: delUteamHonor
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月3日 下午8:43:42
	 */
	@Override
	public HashMap<String, Object> delUteamHonor(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		// 检查是否有球员荣誉
		if (StringUtils.isEmpty(map.get("keyId")))
			return WebPublicMehod.returnRet("error", "未找到球队荣誉");
		this.baseDAO.executeHql("delete from UTeamHonor where keyId='" + map.get("keyId") + "'");
//		HashMap<String, Object> retMap = new HashMap<String, Object>();
//		retMap.put("keyId", map.get("keyId"));
		return WebPublicMehod.returnRet("success","删除球队荣誉成功");
	}
	
	/**
	 * 
	 * @TODO 球队荣誉排序
	 * @Title: sortTeamHonor
	 * @param hashMap
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月12日 下午7:56:15
	 */
	@Override
	public HashMap<String, Object> sortTeamHonor(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if (null==(map.get("teamId"))||"".equals(map.get("teamId"))) {
			return WebPublicMehod.returnRet("error", "未找到teamId");
		}
//		if(null!=(map.get("keys"))){
//			return WebPublicMehod.returnRet("error", "未找到keys");
//		}
//		System.out.println("teamId="+map.get("teamId"));
//		System.out.println("keys="+map.get("keys"));
		
		Integer maxNum = this.getMaxHonorNum(map) - 1;
		@SuppressWarnings("unchecked")
		List<String> keys = (List<String>) JSONArray.toCollection(JSONArray.fromObject(map.get("keys")),
				String.class);
		for (String key : keys) {
			this.baseDAO.executeSql("update u_team_honor set num=" + maxNum + " where key_id = '" + key + "' ");
			maxNum--;
		}
		return WebPublicMehod.returnRet("success", "球队荣誉排序成功");
	}
	
	/**
	 * 
	 * @TODO 关于球队
	 * @Title: getAboutTeamList 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月18日 下午2:43:26
	 */
	@Override
	public HashMap<String, Object> getAboutTeamList(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();//返回数据集合
		map.put("XteamId", map.get("teamId"));
		List<OutUbCourtMap> ubCourtMap = new ArrayList<OutUbCourtMap>();
		ubCourtMap = this.getBrCourtlist(map);
		resultMap.put("ubCourtMap", ubCourtMap);
		return resultMap;
	}
	

	/**
	 * 
	 * 
	   TODO - 查看球员详情的战队列表 【2.0.0】
	   @param map
	   		userId			查看球员的userId
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getUteamListInplayerRoughly(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> listTeam = new ArrayList<HashMap<String, Object>>();
		StringBuffer sql = this.getUteamListInroughly_sql;
		//分页
		hashMap.put("userId", map.get("userId"));
//		listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);
//		if (listTeam != null && listTeam.size() > 0) {
//			for (HashMap<String, Object> hashMap2 : listTeam) {
//				this.displayData(hashMap2,map);
//				this.setUteamChances(hashMap2);
//				//填充角色
//				uPlayerRoleService.setMembertype202(hashMap2);
//			}
//		}
		listTeam = baseDAO.findSQLMap(sql.toString(),hashMap);

//		if(null != map.get("page")&& !"".equals(map.get("page"))){
//			listTeam = this.getPageLimit(sql.toString(), map, hashMap,null);
			if (null != listTeam && listTeam.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listTeam) {
					this.displayData(hashMap2,map);
					this.setUteamChances(hashMap2);
					//填充角色
					uPlayerRoleService.setMembertype202(hashMap2);
				}
			}
//		}
		return listTeam;
	}
	
	/**
	 * 球队里程碑列表
	 * @TODO
	 * @Title: getUTeamBehaviorList 
	 * @param map
	 * 	teamId
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年8月30日 下午5:44:47
	 */
	@Override
	public HashMap<String, Object> getUTeamBehaviorList(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("teamId"))) {
			List<HashMap<String, Object>> uTeamBehaviorlist = new ArrayList<HashMap<String, Object>>();
			//限制标识
			map.put("roughly","");
			uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviorList(map);
			resultMap.put("outuTeamBehaviorlist", uTeamBehaviorlist);
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @TODO 解散球队弹框接口
	 * @Title: getDissolveTeamBtuText 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年9月5日 上午11:58:44
	 */
	@Override
	public HashMap<String, Object> getDissolveTeamBtuText(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @TODO 发送解散通知至唯一角色 【多个】
	 * @Title: getDissolveTeamSendManagement 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年9月5日 上午11:59:03
	 */
	@Override
	public HashMap<String, Object> getDissolveTeamSendManagement(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @TODO  解散球队申请响应 同意/拒绝
	 * @Title: getDissolveTeamResponse 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年9月5日 下午1:00:49
	 */
	@Override
	public HashMap<String, Object> getDissolveTeamResponse(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
