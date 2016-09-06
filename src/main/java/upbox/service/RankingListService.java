package upbox.service;

import java.util.HashMap;

/**
 * 
 * @TODO 天梯service
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午4:07:56 
 * @version 1.0
 */
public interface RankingListService {
	
	/**
	 * 
	 * @TODO 排行榜接口
	 * @Title: getRankingList 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:09:34
	 */
	public HashMap<String, Object> getRankingList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 更新排行榜排名
	 * @Title: updateRankByList 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:10:02
	 */
	public void updateRankByList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 天梯搜索接口
	 * @Title: getRankingSearch 
	 * @param map
	 * 		searchStr 搜索条件
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:10:36
	 */
	public HashMap<String, Object> getRankingSearch(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 通用胜率数据接口
	 * @Title: getPublicRankingInfo 
	 * @param teamId 球队id
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:11:14
	 */
	public HashMap<String, Object> getPublicRankingInfo(String teamId)throws Exception;
	
}
