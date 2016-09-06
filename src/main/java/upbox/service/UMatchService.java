package upbox.service;

import upbox.model.UMatchBs;

import java.util.HashMap;

/**
 * 赛事信息接口
 * 
 * @author yc
 *
 */
public interface UMatchService {
	
	/**
	 * 获取关于球队的所有赛事小场次信息
	 * @return teamId 球队id
	 * @throws Exception
	 */
	public HashMap<String, Object> getUMatchBsListWithTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 获取关于球员的所有赛事小场次信息
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUMatchBsListWithPlayer(HashMap<String, String> map) throws Exception;
	
	/**
	 * 我关注的赛事列表
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUMatchBsListWithFollow(HashMap<String, String> map) throws Exception;
	
	
	/**
	 * 获取赛事小场次头部信息
	 * @param bsId 小场次id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUMatchBs(HashMap<String, String> map) throws Exception;

	/**
	 * TODO - 获取赛事小场次对象
	 * @param bsId 场次Id
	 * @return
	 */
	public UMatchBs getUMatchBs(String bsId) throws Exception;
	
	/**
	 * 获取赛事小场次中部信息
	 * @param bsId 小场次id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getUMatchBsBody(HashMap<String, String> map) throws Exception;
	
	/**
	 * 关注或取消关注赛事
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> isFollowMatch(HashMap<String, String> map) throws Exception;
	
	/**
	 * 获得第三方的赛事列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getOtherMatchAllList(HashMap<String, String> map) throws Exception;
}
