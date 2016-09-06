package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.CornerBean;
import upbox.pub.Public_Cache;
import upbox.service.CornerService;
import upbox.service.UUserService;

/**
 * 
 * @TODO 角标service实现类
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午4:07:24 
 * @version 1.0
 */
@Service("cornerService")
public class CornerServiceImpl implements CornerService {

	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private UUserService uuserService;

	/**
	 * 
	 * @TODO 获取球员角标
	 * @Title: getPlayerCorner 
	 * @param map
	 * 		playerId 球员主键
	 * @param playerIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:50:48
	 */
	@Override
	public List<List<CornerBean>> getPlayerCorner(HashMap<String, String> map,List<Object> playerIdList) throws Exception {
		// TODO Auto-generated method stub
		List<List<CornerBean>> result = new ArrayList<List<CornerBean>>();
		List<CornerBean> pubList = new ArrayList<CornerBean>();
		
		List<CornerBean> teammate = this.getCornerByTeammate(map,playerIdList);
		List<CornerBean> signPlayer = this.getCornerBySignPlayer(map,playerIdList);
		
		for (int i = 0; i < playerIdList.size(); i++) {
			if(null!=teammate.get(i)){
				pubList = new ArrayList<CornerBean>();
				pubList.add(teammate.get(i));
			}else if(null!=signPlayer.get(i)){
				pubList = new ArrayList<CornerBean>();
				pubList.add(signPlayer.get(i));
			}
			result.add(pubList);
		}
		return result;
	}

	/**
	 * 
	 * @TODO 是否是我的队友
	 * @Title: getCornerByTeammate 
	 * @param map
	 * 		playerId 球员主键
	 * @param playerIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:51:04
	 */
	@Override
	public List<CornerBean> getCornerByTeammate(HashMap<String, String> map,List<Object> playerIdList) throws Exception {
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> countList = new ArrayList<HashMap<String, Object>>();

		if(null!=map.get("loginUserId")){
			map.put("userId",map.get("loginUserId"));
		}else{
			map.put("userId"," ");
		}
		
		String sql = "select count(1) as count from  (select * from u_player where team_id in (select distinct team_id from u_player as up where up.in_team = '1' and up.team_belonging = '1' and user_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < playerIdList.size(); i++) {
			if (i == playerIdList.size() - 1) {
				sb.append(sql + "'"+map.get("userId")+"' ) and player_id = '" + playerIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'"+map.get("userId")+"' ) and player_id = '" + playerIdList.get(i) + "' limit 1 offset 0) as countTable  union all ");
			}
		}
		countList = baseDAO.findSQLMap(sb.toString());
		for (int i = 0; i < playerIdList.size(); i++) {
			CornerBean cornerBean = null;
			if(countList.get(i).get("count").toString().equals("1")){
				cornerBean = new CornerBean();
				cornerBean.setCorId(1);
				cornerBean.setCorType(1);
				cornerBean.setCorName("我的队友");
			}
			result.add(cornerBean);
		}
		
		return result;
	}

	/**
	 * 
	 * @TODO 是否有签约球队（签约球员）
	 * @Title: getCornerBySignPlayer 
	 * @param map
	 * 		playerId 球员主键
	 * @param playerIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:54:03
	 */
	@Override
	public List<CornerBean> getCornerBySignPlayer(HashMap<String, String> map,List<Object> playerIdList) throws Exception {
		// TODO Auto-generated method stub
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> countList = new ArrayList<HashMap<String, Object>>();

		String sql = "select count(*) as count from u_player where user_id = (select user_id from u_player where  player_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < playerIdList.size(); i++) {
			if (i == playerIdList.size() - 1) {
				sb.append(sql + "'"+playerIdList.get(i)+"') and exit_type is null");
			} else {
				sb.append(sql + "'"+playerIdList.get(i)+"') and exit_type is null  union all ");
			}
		}
		countList = baseDAO.findSQLMap(sb.toString());
		for (int i = 0; i < playerIdList.size(); i++) {
			CornerBean cornerBean = null;
			if(countList.get(i).get("count").toString().equals("1")){
				cornerBean = new CornerBean();
				cornerBean.setCorId(3);
				cornerBean.setCorType(1);
				cornerBean.setCorName("自由球友");
			}else{
				cornerBean = new CornerBean();
				cornerBean.setCorId(2);
				cornerBean.setCorType(1);
				cornerBean.setCorName("签约球员");
			}
			result.add(cornerBean);
		}
		
		return result;
	}

	/**
	 * 
	 * @TODO 是否是约战状态角标
	 * @Title: getCornerByDuelStatusList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:31:39
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByDuelStatusList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count from (select * from u_duel where duel_status = '1' and effective_status = '1' and f_team_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "'" + " limit 1 offset 0) as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 是否是挑战状态角标
	 * @Title: getCornerByChallengeStatusList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:32:00
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByChallengeStatusList(HashMap<String, String> map,
			List<Object> teamIdList) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count,countTable.wincount from (select uc.*,ucc.wincount from u_challenge uc left join u_challenge_ch ucc on ucc.key_id = uc.fch_id where uc.challenge_status = '1' and uc.effective_status = '1' and uc.f_team_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "'" + " limit 1 offset 0) as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 是否是我创建的球队
	 * @Title: getCornerByMyTeamRelationList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:32:17
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByMyTeamRelationList(HashMap<String, String> map,
			List<Object> teamIdList) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count,countTable.position,countTable.number,countTable.member_type from (select up.position,up.number,upr.member_type from u_player up left join u_player_role upr on upr.player_id = up.player_id where up.in_team = '1' and upr.member_type_use_status = '1' and  up.team_belonging = '1' and upr.member_type ='1' and up.team_id =  ";
		StringBuffer sb = new StringBuffer();
		
		if(null!=map.get("loginUserId")){
			map.put("userId",map.get("loginUserId"));
		}else{
			map.put("userId"," ");
		}
			
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' and user_id = '" + map.get("userId")
				+ "') as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "' and user_id = '" + map.get("userId")
				+ "') as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 是否是我签约的球队
	 * @Title: getCornerBySignTeamRelationList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:43:13
	 */
	@Override
	public List<HashMap<String, Object>> getCornerBySignTeamRelationList(HashMap<String, String> map,
			List<Object> teamIdList) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count,countTable.position,countTable.number,countTable.member_type from (select up.position,up.number,upr.member_type from u_player up left join u_player_role upr on upr.player_id = up.player_id where up.in_team = '1' and upr.member_type_use_status = '1' and  up.team_belonging = '1' and upr.member_type <> '1' and upr.member_type <> '11' and up.team_id =  ";
		StringBuffer sb = new StringBuffer();
		
		if(null!=map.get("loginUserId")){
			map.put("userId",map.get("loginUserId"));
		}else{
			map.put("userId"," ");
		}
		
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' and up.user_id = '" + map.get("loginUserId")
						+ "') as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "' and up.user_id = '" + map.get("loginUserId")
						+ "') as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 是否是推荐约战
	 * @Title: getCornerByDuelRecommendList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:43:32
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByDuelRecommendList(HashMap<String, String> map,
			List<Object> teamIdList) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count from (select * from u_duel where duel_recommend_status = '1' and effective_status = '1' and f_team_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "'" + " limit 1 offset 0) as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		//System.out.println(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 是否是推荐挑战(擂主)
	 * @Title: getCornerByChallengeRecommendList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:47:50
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByChallengeRecommendList(HashMap<String, String> map,
			List<Object> teamIdList) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count,countTable.wincount from (select uc.*,ucc.wincount from u_challenge uc left join u_challenge_ch ucc on ucc.key_id = uc.fch_id where uc.ch_recommend_status = '1' and uc.effective_status = '1' and f_team_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "'" + " limit 1 offset 0) as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		//System.out.println(sb.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 返回约战角标
	 * @Title: getStatusDuelCornerList 
	 * @param map
	 * @param teamIdList 球队主键List
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:26:09
	 */
	public List<CornerBean> getStatusDuelCornerList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> duelStatus = new ArrayList<HashMap<String, Object>>();

		duelStatus = this.getCornerByDuelStatusList(map, teamIdList);

		for (int i = 0; i < teamIdList.size(); i++) {
			CornerBean cornerBean = null;
			if (duelStatus.get(i).get("count").toString().equals("1")) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(2);
				cornerBean.setCorType(2);
				cornerBean.setCorName("约战");
				cornerBean.setCorPriorityId(2);
			}
			result.add(cornerBean);
		}

		return result;
	}

	/**
	 * 
	 * @TODO 返回挑战角标
	 * @Title: getStatusChallengeCornerList 
	 * @param map
	 * @param teamIdList 球队主键LIST
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:28:52
	 */
	public List<CornerBean> getStatusChallengeCornerList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> challengeStatus = new ArrayList<HashMap<String, Object>>();

		// 状态显示逻辑 挑战>约战
		challengeStatus = this.getCornerByChallengeStatusList(map, teamIdList);
		for (int i = 0; i < teamIdList.size(); i++) {
			CornerBean cornerBean = null;
			if (challengeStatus.get(i).get("count").toString().equals("1")) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(1);
				cornerBean.setCorType(2);
				cornerBean.setCorName("挑战");
				cornerBean.setCorPriorityId(1);
				if(null!=challengeStatus.get(i).get("wincount")){
					cornerBean.setCorWinCount(Integer.parseInt(challengeStatus.get(i).get("wincount").toString()));
				}else{
					cornerBean.setCorWinCount(0);
				}
			}
			result.add(cornerBean);
		}

		return result;
	}

	/**
	 * 
	 * @TODO 返回我的球队/签约球队角标
	 * @Title: getRelationCornerList 
	 * @param map
	 * @param teamIdList 球队主键LIST
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:32:22
	 */
	public List<CornerBean> getRelationCornerList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> createStatus = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> joinStatus = new ArrayList<HashMap<String, Object>>();

		boolean isCaptainAndTwoRole=false;
		
		// 关系角标显示逻辑 创建>签约
		createStatus = this.getCornerByMyTeamRelationList(map, teamIdList);
		joinStatus = this.getCornerBySignTeamRelationList(map, teamIdList);

		for (int i = 0; i < teamIdList.size(); i++) {
			CornerBean cornerBean = null;
			
			//签约球队
			if (1<=Integer.parseInt(joinStatus.get(i).get("count").toString())) {
				isCaptainAndTwoRole = true;
				cornerBean = new CornerBean();
				cornerBean.setCorId(2);
				cornerBean.setCorType(1);
				cornerBean.setCorName("签约球队");
				cornerBean.setCorPriorityId(2);
				if(null!=joinStatus.get(i).get("position")){
					if("无位置".equals(Public_Cache.HASH_PARAMS("position").get(joinStatus.get(i).get("position").toString()))){
						cornerBean.setCorPositionName(null);
					}else{
						cornerBean.setCorPositionName(Public_Cache.HASH_PARAMS("position").get(joinStatus.get(i).get("position").toString()));
					}
				}
				if(null!=joinStatus.get(i).get("number")){
					cornerBean.setCorNumber(Integer.parseInt(joinStatus.get(i).get("number").toString()));
				}
				//获取角色 
				if(2==Integer.parseInt(joinStatus.get(i).get("count").toString())){
					StringBuffer sb = new StringBuffer();
					sb.append("select upr.member_type from u_player up left join u_player_role upr on upr.player_id = up.player_id  LEFT JOIN u_parameter_info upi on upr.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') where up.in_team = '1' and upr.member_type_use_status = '1' and upr.member_type <> '11' and  up.team_belonging = '1' and up.team_id = '" + teamIdList.get(i) + "' and up.user_id = '" + map.get("loginUserId")+"' order by upi.rank_weight ASC");
//					sb.append("select upr.member_type from u_player up left join u_player_role upr on upr.player_id = up.player_id where up.in_team = '1' and upr.member_type_use_status = '1' and upr.member_type <> '11' and  up.team_belonging = '1' and up.team_id = '" + teamIdList.get(i) + "' and up.user_id = '" + map.get("loginUserId")+"' order by upr.change_date DESC");
					List<HashMap<String, Object>> memberTypeList = baseDAO.findSQLMap(sb.toString());
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(0).get("member_type").toString()))){
						cornerBean.setCorMemberTypeName(null);
					}else{
						cornerBean.setCorMemberTypeName(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(0).get("member_type").toString()));
					}
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(1).get("member_type").toString()))){
						cornerBean.setCorMemberTypeName1(null);
					}else{
						cornerBean.setCorMemberTypeName1(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(1).get("member_type").toString()));
					}
					
				}else if(null!=joinStatus.get(i).get("member_type")){
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(joinStatus.get(i).get("member_type")))){
						cornerBean.setCorMemberTypeName(null);
					}else{
						cornerBean.setCorMemberTypeName(Public_Cache.HASH_PARAMS("member_type").get(joinStatus.get(i).get("member_type").toString()));
					}
					
				}
				

			}
			
			//我的球队
			if (1<=Integer.parseInt(createStatus.get(i).get("count").toString())) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(1);
				cornerBean.setCorType(1);
				cornerBean.setCorName("我的球队");
				cornerBean.setCorPriorityId(1);
				if(null!=createStatus.get(i).get("position")){
					if("无位置".equals(Public_Cache.HASH_PARAMS("position").get(createStatus.get(i).get("position").toString()))){
						cornerBean.setCorPositionName(null);
					}else{
						cornerBean.setCorPositionName(Public_Cache.HASH_PARAMS("position").get(createStatus.get(i).get("position").toString()));
					}
				}
				if(null!=createStatus.get(i).get("number")){
					cornerBean.setCorNumber(Integer.parseInt(createStatus.get(i).get("number").toString()));
				}
				//获取角色
				if(2==Integer.parseInt(createStatus.get(i).get("count").toString())||(isCaptainAndTwoRole&&null!=joinStatus.get(i).get("member_type"))){
					StringBuffer sb = new StringBuffer();
					sb.append("select upr.member_type from u_player up left join u_player_role upr on upr.player_id = up.player_id  LEFT JOIN u_parameter_info upi on upr.member_type=upi.params and upi.pkey_id in (select up.pkey_id from u_parameter up where up.params='member_type') where up.in_team = '1' and upr.member_type_use_status = '1' and upr.member_type <> '11' and  up.team_belonging = '1' and up.team_id = '" + teamIdList.get(i) + "' and up.user_id = '" + map.get("loginUserId")+"' order by upi.rank_weight ASC");
					List<HashMap<String, Object>> memberTypeList = baseDAO.findSQLMap(sb.toString());
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(0).get("member_type").toString()))){
						cornerBean.setCorMemberTypeName(null);
					}else{
						cornerBean.setCorMemberTypeName(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(0).get("member_type").toString()));
					}
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(1).get("member_type").toString()))){
						cornerBean.setCorMemberTypeName1(null);
					}else{
						cornerBean.setCorMemberTypeName1(Public_Cache.HASH_PARAMS("member_type").get(memberTypeList.get(1).get("member_type").toString()));
					}
					
				}else if(null!=createStatus.get(i).get("member_type")){
					if("无角色".equals(Public_Cache.HASH_PARAMS("member_type").get(createStatus.get(i).get("member_type")))){
						cornerBean.setCorMemberTypeName(null);
					}else{
						cornerBean.setCorMemberTypeName(Public_Cache.HASH_PARAMS("member_type").get(createStatus.get(i).get("member_type").toString()));
					}
					
				}
			}
			result.add(cornerBean);
		}

		return result;
	}

	
	/**
	 * 
	 * @TODO 推荐约返回战/推荐挑战角标
	 * @Title: getRecommendCornerList 
	 * @param map
	 * @param teamIdList 球队主键LIST
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:36:55
	 */
	public List<CornerBean> getRecommendCornerList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		List<CornerBean> result = new ArrayList<CornerBean>();
		List<HashMap<String, Object>> joinRecommend = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> challengeRecommend = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> duelRecommend = new ArrayList<HashMap<String, Object>>();

		// 关系角标显示逻辑 推荐加入>推荐挑战>推荐约战
		joinRecommend = this.getCornerByJoinRecommendList(map, teamIdList);
		challengeRecommend = this.getCornerByChallengeRecommendList(map, teamIdList);
		duelRecommend = this.getCornerByDuelRecommendList(map, teamIdList);
		baseDAO.getSessionFactory().getCurrentSession().clear();
		for (int i = 0; i < teamIdList.size(); i++) {
			CornerBean cornerBean = null;
			if (duelRecommend.get(i).get("count").toString().equals("1")) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(4);
				cornerBean.setCorType(1);
				cornerBean.setCorName("推荐约战");
				cornerBean.setCorPriorityId(2);
			}
			if (challengeRecommend.get(i).get("count").toString().equals("1")) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(3);
				cornerBean.setCorType(1);
				cornerBean.setCorName("推荐挑战");
				cornerBean.setCorPriorityId(1);
				if(null!=challengeRecommend.get(i).get("wincount")){
					cornerBean.setCorWinCount(Integer.parseInt(challengeRecommend.get(i).get("wincount").toString()));
				}else{
					cornerBean.setCorWinCount(0);
				}
			}
			if (joinRecommend.get(i).get("count").toString().equals("1")) {
				cornerBean = new CornerBean();
				cornerBean.setCorId(5);
				cornerBean.setCorType(1);
				cornerBean.setCorName("推荐加入");
				cornerBean.setCorPriorityId(0);
			}
			result.add(cornerBean);
		}

		return result;
	}

	/**
	 * 
	 * @TODO 根据类型获取单独角标【暂时不用】
	 * @Title: getAloneTeamCornerListByType 
	 * @param map
	 * @param teamIdList
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:37:15
	 */
	@Override
	public List<List<CornerBean>> getAloneTeamCornerListByType(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		// TODO Auto-generated method stub

		List<List<CornerBean>> resultList = new ArrayList<List<CornerBean>>();
		List<CornerBean> pubList = new ArrayList<CornerBean>();

		List<CornerBean> cornerRelation = this.getRelationCornerList(map, teamIdList);
		List<CornerBean> cornerRecommend = this.getRecommendCornerList(map, teamIdList);
		List<CornerBean> challengeStatus = this.getStatusChallengeCornerList(map, teamIdList);
		List<CornerBean> duelStatus = this.getStatusDuelCornerList(map, teamIdList);
		
		if (null != map.get("type")) {
			for (int i = 0; i < teamIdList.size(); i++) {
				switch (map.get("type")) {
				case "status":
					if (null != challengeStatus.get(i)) {
						pubList.add(challengeStatus.get(i));
					} else if (null != duelStatus.get(i)) {
						pubList.add(duelStatus.get(i));
					}
					break;
				case "relation":
					if (null != cornerRelation.get(i)) {
						pubList.add(cornerRelation.get(i));
					}
					break;
				case "recommend":
					if (null != cornerRecommend.get(i)) {
						pubList.add(cornerRecommend.get(i));
					}
					break;
				default:
					break;
				}
				resultList.add(pubList);
			}
		}

		return resultList;
	}
	
	/**
	 * 
	 * @TODO 生成角标标识list 【暂时不用】
	 * @Title: getCorPriorityIdList 
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月18日 下午4:37:41
	 */
	public List<Integer> getCorPriorityIdList(List<Object> teamIdList) throws Exception {
		List<Integer> corPriorityId = new ArrayList<Integer>();
		for (int i = 0; i < teamIdList.size(); i++) {
			corPriorityId.add(0);
		}
		return corPriorityId;
	}

	
	/**
	 * 
	 * @TODO 获取所有球队角标(排序有规则)
	 * @Title: getAllTeamCornerList 
	 * @param map
	 * 		teamId 球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:48:32
	 */
	@Override
	public List<List<CornerBean>> getAllTeamCornerList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		// TODO Auto-generated method stub

		List<List<CornerBean>> result = new ArrayList<List<CornerBean>>();
		List<CornerBean> pubList = new ArrayList<CornerBean>();

		List<CornerBean> cornerRelation = this.getRelationCornerList(map, teamIdList); //签约球队、我的球队
		List<CornerBean> cornerRecommend = this.getRecommendCornerList(map, teamIdList);//推荐约战、推荐挑战
		List<CornerBean> challengeStatus = this.getStatusChallengeCornerList(map, teamIdList); //挑战
		List<CornerBean> duelStatus = this.getStatusDuelCornerList(map, teamIdList); //约战

		baseDAO.getSessionFactory().getCurrentSession().clear();
		for (int i = 0; i < teamIdList.size(); i++) {
			// 1.关系角标一级优先
			if (null != cornerRelation.get(i)) {
				pubList = new ArrayList<CornerBean>();
				pubList.add(cornerRelation.get(i));
				if (null != challengeStatus.get(i)) {
					pubList.add(challengeStatus.get(i));
				} else if (null != duelStatus.get(i)) {
					pubList.add(duelStatus.get(i));
				}
				result.add(pubList);
			} else if (null != cornerRecommend.get(i)) {
				// 2.推荐角标二级优先
				pubList = new ArrayList<CornerBean>();
				pubList.add(cornerRecommend.get(i));
				if(cornerRecommend.get(i).getCorPriorityId()==1){
					if(null != duelStatus.get(i)){
						pubList.add(duelStatus.get(i));
					}
				}else{
					if (null != challengeStatus.get(i)) {
						pubList.add(challengeStatus.get(i));
					}
				}
				result.add(pubList);
			} else {
				pubList = new ArrayList<CornerBean>();
				// 3.状态角标三级优先
				if (null != challengeStatus.get(i)) {
					pubList.add(challengeStatus.get(i));
				} else if (null != duelStatus.get(i)) {
					pubList.add(duelStatus.get(i));
				}
				result.add(pubList);
			}

		}

		return result;
	}

	
	/**
	 * 
	 * @TODO 判断推荐加入
	 * @Title: getCornerByJoinRecommendList 
	 * @param map
	 * 		teamId  球队主键
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:54:16
	 */
	@Override
	public List<HashMap<String, Object>> getCornerByJoinRecommendList(HashMap<String, String> map, List<Object> teamIdList)
			throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		String sql = " select count(1) as count from (select * from u_team where recommend_team = '1' and team_id = ";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < teamIdList.size(); i++) {
			if (i == teamIdList.size() - 1) {
				sb.append(sql + "'" + teamIdList.get(i) + "' limit 1 offset 0) as countTable");
			} else {
				sb.append(sql + "'" + teamIdList.get(i) + "'" + " limit 1 offset 0) as countTable  union all ");
			}
		}
		result = baseDAO.findSQLMap(sb.toString());
		return result;
	}

}
