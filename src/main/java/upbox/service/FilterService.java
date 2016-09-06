package upbox.service;

import java.util.HashMap;

/**
 * 筛选接口
 * @author xiao
 *
 */
public interface FilterService {
	
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
	public HashMap<String, Object> findFilterTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 球员筛选
	 * @param map teamId-球队Id、loginUserId-用户Id、expertPosition-擅长位置、age-年龄、 height-身高 、weight-体重、page-分页
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月7日
	 */
	public HashMap<String, Object> findFilterPlayer(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 筛选可配置
	 * @param map configType-筛选的类型（1-用户 2-球员 3-球队 4-订单 5-约战 6-挑战 7-通知 8-赛事 9-球场 ）
	 * @return
	 * HashMap<String,Object>
	 * xiao
	 * 2016年6月8日
	 */
	public HashMap<String, Object> findFilterOption(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 球队地区筛选－项目启动时调用
	 * @param map
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年6月8日
	 */
	public HashMap<String, Object> findFileTeamAreaBuffter(HashMap<String, String> map) throws Exception;
	
	/**
	 *  球员（用户）地区筛选－项目启动时调用
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	public HashMap<String, Object> findFilePlayerAreaBuffter(HashMap<String, String> map) throws Exception;
	/**
	 * 球场地区缓存－项目启动时调用
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	public HashMap<String, Object> findFileCourtAreaBuffter(HashMap<String, String> map) throws Exception;
	
	/**
	 * 挑战地区缓存－项目启动时调用
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	public HashMap<String, Object> findFileChallengeAreaBuffter(HashMap<String, String> map) throws Exception;
	
	/**
	 * 约战地区缓存－项目启动时调用
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	public HashMap<String, Object> findFileDuelAreaBuffter(HashMap<String, String> map) throws Exception;
	
}
