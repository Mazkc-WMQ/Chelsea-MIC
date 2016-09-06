package upbox.service.impl;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UParameterInfo;
import upbox.model.UPlayerRoleLimit;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 前端球员身份接口实现类
 * @author mercideng
 *
 */

@Service("uPlayerRoleLimitService")
public class UPlayerRoleLimitServiceImpl implements UPlayerRoleLimitService {
	@Resource
	OperDAOImpl baseDAO;
	
	@Resource
	PublicService publicService;
	
	@Resource
	UPlayerService uPlayerService;
	
	@Resource
	UParameterService uParameterService;
	/**
	 * 
	 * 
	   TODO - 在角色权限表填充初始值
	   @param map
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setPlayerRoleLimit(HashMap<String, String> map) throws Exception {
		map.put("name", "member_type");
		List<UParameterInfo> uParameterInfos = uParameterService.getMemberTypeList202(map);
		if (null != uParameterInfos && uParameterInfos.size() > 0) {
			for (UParameterInfo uParameterInfo : uParameterInfos) {
				String updateUteamInfo = "1";
				String transferMemberType = "1";
				String duel = "1";
				String challenge = "1";
				String match = "1";
				String excludePlayer = "1";
				String disbandPlayer = "1";
				String invitePlayer = "1";
				String recruitPlayer = "1";
				String removeUteamImg = "1";
				String updateFightingForce = "1";
				String updatePlayerinfoInteam = "1";
				String assignMemberTypeManager = "1";
				String assignMemberTypeOperater = "1";
				String updateFormation = "1";
				String setTrain = "1";
				String announce = "1";
				String financeManager = "1";
				String applyMemberType = "1";
				String duelRemind = "1";
				String challengeRemind = "1";
				String matchRemind = "1";
				String releaseDynamic = "1";
				String commentDynamic = "1";
				String contributeUteamCost = "1";
				String inviteOtherUser = "1";
				String signUteam = "1";
				String signPlayer = "1";
				String signDuel = "1";
				String signChallenge = "1";
				String signMatch = "1";
				String signStands = "1";
				String rankRole = "1";
				if ("队长".equals(uParameterInfo.getName())) {
					updateFightingForce="-1";
					updatePlayerinfoInteam="-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					financeManager = "-1";
					rankRole = "4";
				}else if("主教练".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					removeUteamImg = "-1";
					disbandPlayer = "-1";
					excludePlayer = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					financeManager = "-1";
					rankRole = "5";
				}else if("财务后勤".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					rankRole = "6";
				}else if("队务".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "13";
				}else if("队医".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					financeManager = "-1";
					rankRole = "7";
				}else if("新闻官".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					financeManager = "-1";
					rankRole = "8";
				}else if("队员".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "10";
				}else if("拉拉队员".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "11";
				}else if("赞助商".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "12";
				}else if("助理教练".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "9";
				}else if("主席".equals(uParameterInfo.getName())){
					rankRole = "1";
				}else if("总经理".equals(uParameterInfo.getName())){
					rankRole = "2";
				}else if("领队".equals(uParameterInfo.getName())){
					rankRole = "3";
				}else if("无角色".equals(uParameterInfo.getName())){
					updateUteamInfo = "-1";
					transferMemberType = "-1";
					duel = "-1";
					challenge = "-1";
					match = "-1";
					excludePlayer = "-1";
					disbandPlayer = "-1";
					removeUteamImg = "-1";
					updateFightingForce = "-1";
					updatePlayerinfoInteam = "-1";
					assignMemberTypeManager = "-1";
					assignMemberTypeOperater = "-1";
					updateFormation = "-1";
					setTrain = "-1";
					announce = "-1";
					financeManager = "-1";
					rankRole = "14";
				}
				
				UPlayerRoleLimit uPlayerRoleLimit = new UPlayerRoleLimit();
				uPlayerRoleLimit.setKeyId(WebPublicMehod.getUUID());
				uPlayerRoleLimit.setMemberType(uParameterInfo.getParams());
				uPlayerRoleLimit.setCreateDate(new Date());
				uPlayerRoleLimit.setUpdateUteamInfo(updateUteamInfo);
				uPlayerRoleLimit.setTransferMemberType(transferMemberType);
				uPlayerRoleLimit.setDuel(duel);
				uPlayerRoleLimit.setChallenge(challenge);
				uPlayerRoleLimit.setMatched(match);
				uPlayerRoleLimit.setExcludePlayer(excludePlayer);
				uPlayerRoleLimit.setDisbandPlayer(disbandPlayer);
				uPlayerRoleLimit.setInvitePlayer(invitePlayer);
				uPlayerRoleLimit.setRecruitPlayer(recruitPlayer);
				uPlayerRoleLimit.setRemoveUteamImg(removeUteamImg);
				uPlayerRoleLimit.setUpdateFightingForce(updateFightingForce);
				uPlayerRoleLimit.setUpdatePlayerinfoInteam(updatePlayerinfoInteam);
				uPlayerRoleLimit.setAssignMemberTypeManager(assignMemberTypeManager);
				uPlayerRoleLimit.setAssignMemberTypeOperater(assignMemberTypeOperater);
				uPlayerRoleLimit.setUpdateFormation(updateFormation);
				uPlayerRoleLimit.setSetTrain(setTrain);
				uPlayerRoleLimit.setAnnounce(announce);
				uPlayerRoleLimit.setFinanceManager(financeManager);
				uPlayerRoleLimit.setApplyMemberType(applyMemberType);
				uPlayerRoleLimit.setDuelRemind(duelRemind);
				uPlayerRoleLimit.setChallengeRemind(challengeRemind);
				uPlayerRoleLimit.setMatchRemind(matchRemind);
				uPlayerRoleLimit.setReleaseDynamic(releaseDynamic);
				uPlayerRoleLimit.setCommentDynamic(commentDynamic);
				uPlayerRoleLimit.setContributeUteamCost(contributeUteamCost);
				uPlayerRoleLimit.setInviteOtherUser(inviteOtherUser);
				uPlayerRoleLimit.setSignUteam(signUteam);
				uPlayerRoleLimit.setSignPlayer(signPlayer);
				uPlayerRoleLimit.setSignDuel(signDuel);
				uPlayerRoleLimit.setSignChallenge(signChallenge);
				uPlayerRoleLimit.setSignMatch(signMatch);
				uPlayerRoleLimit.setSignStands(signStands);
				uPlayerRoleLimit.setRankRole(rankRole);
				baseDAO.save(uPlayerRoleLimit);
			}
		}
		return null;
	}

	/**
	 * 
	 * 
	   TODO - 根据类型判断用户是否有相关类型的权限
	   @param map
	   		type 			1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募;10:分配 11:修改阵型 12：修改队内成员
	   		teamId			球队Id
	   		loginUserId		当前用户Id
	   @return
	   @throws Exception
	   2016年6月18日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> playerIsRoleByType(HashMap<String, String> map)throws Exception{
		List<HashMap<String, Object>> playerIdList = null;
		StringBuffer sql = new StringBuffer("select p.player_id playerId,p.team_id teamId from u_player p "
				+" LEFT JOIN u_player_role r on r.player_id=p.player_id and r.member_type_use_status='1' "
				+" LEFT JOIN u_player_role_limit l on r.member_type=l.member_type "
				+" where p.user_id=:loginUserId and p.in_team='1'");
		if (publicService.StringUtil(map.get("teamId"))) {
			sql.append(" and p.team_id=:teamId ");
		}
		if ("1".equals(map.get("type"))) {//踢人权限
			sql.append(" and l.exclude_player='1' ");
		}else if ("2".equals(map.get("type"))) {//修改球队信息权限
			sql.append(" and l.update_uteam_info='1' ");
		}else if ("3".equals(map.get("type"))) {//删除相册
			sql.append(" and l.remove_uteam_img='1' ");
		}else if ("4".equals(map.get("type"))) {//球队解散
			sql.append(" and l.disband_player='1' ");
		}else if ("5".equals(map.get("type"))) {//发起约战
			sql.append(" and l.duel='1' ");
		}else if ("6".equals(map.get("type"))) {//发起挑战
			sql.append(" and l.challenge='1' ");
		}else if ("7".equals(map.get("type"))) {//赛事
			sql.append(" and l.matched='1' ");
		}else if ("8".equals(map.get("type"))) {//邀请
			sql.append(" and l.invite_player='1' ");
		}else if ("9".equals(map.get("type"))) {//招募
			sql.append(" and l.recruit_player='1' ");
		}else if ("10".equals(map.get("type"))) {//分配
			sql.append(" and l.assign_member_type_manager='1' and l.assign_member_type_operater='1' ");
		}else if ("11".equals(map.get("type"))) {//修改阵型
			sql.append(" and l.update_formation='1' ");
		}else if ("12".equals(map.get("type"))) {//修改队内成员
			sql.append(" and l.update_playerinfo_inteam='1' ");
		}else{
			sql.append(" and l.transfer_member_type='1' ");
		}
		sql.append(" group by p.player_id ORDER BY l.rank_role asc ");
		playerIdList = baseDAO.findSQLMap(map, sql.toString());
		return playerIdList;
		
	}
	/**
	 * 
	 * 
	   TODO - 根据角色获取角色权限对象
	   @param map
	   		memberType		角色值
	   @return
	   @throws Exception
	   2016年6月18日
	   dengqiuru
	 */
	@Override
	public UPlayerRoleLimit getUplayerRoleLimitByMemberType(HashMap<String, String> map)throws Exception{
		UPlayerRoleLimit uPlayerRoleLimit = baseDAO.get(map, "from UPlayerRoleLimit where member_type=:memberType");
		return uPlayerRoleLimit;
	}
}
