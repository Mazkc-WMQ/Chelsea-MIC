package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.CornerBean;

/**
 * 
 * @TODO 角标service
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午3:24:58 
 * @version 1.0
 */
public interface CornerService {
	
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
	public List<HashMap<String, Object>> getCornerByDuelStatusList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerByChallengeStatusList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerByMyTeamRelationList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerBySignTeamRelationList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerByDuelRecommendList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerByChallengeRecommendList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
	/**
	 * 
	 * @TODO 根据类型获取单独球队角标
	 * @Title: getAloneTeamCornerListByType 
	 * @param map
	 * 		type 球队角标类型
	 * 		teamId 球队主键 
	 * @param teamIdList
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:48:11
	 */
	public List<List<CornerBean>> getAloneTeamCornerListByType(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<List<CornerBean>> getAllTeamCornerList(HashMap<String, String> map,List<Object> teamIdList)throws Exception;
	
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
	public List<List<CornerBean>> getPlayerCorner(HashMap<String, String> map,List<Object> playerIdList)throws Exception;
	
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
	public List<CornerBean> getCornerByTeammate(HashMap<String, String> map,List<Object> playerIdList) throws Exception;
	
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
	public List<CornerBean> getCornerBySignPlayer(HashMap<String, String> map,List<Object> playerIdList) throws Exception;
	
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
	public List<HashMap<String, Object>> getCornerByJoinRecommendList(HashMap<String, String> map,List<Object> teamIdList) throws Exception;
	
}
