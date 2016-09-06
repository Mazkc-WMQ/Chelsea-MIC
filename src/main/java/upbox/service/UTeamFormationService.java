package upbox.service;

import java.util.HashMap;

/**
 * 赛事信息接口
 * 
 * @author yc
 *
 */
public interface UTeamFormationService {
	
	/**
	 * 获取此球队所有阵型的下拉选内容
	 * @param map
	 * @return 
	 * @throws Exception
	 */
	public HashMap<String, Object> getAllFormationSelList(HashMap<String, String> map) throws Exception;
	
	/**
	 * 获取当前阵型的详细内容
	 * @param map
	 * @return 
	 * @throws Exception
	 */
	public HashMap<String, Object> getFormationInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 保存初始化的当前阵型
	 * @param map
	 * @return 
	 * @throws Exception
	 */
	public HashMap<String, Object> saveInitFormationInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 修改当前阵型
	 * @param map
	 * @return 
	 * @throws Exception
	 */
	public HashMap<String, Object> updateFormationInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 删除当前阵型
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> delFormationInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 查询符合添加阵型的球员
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> selectFormationPlayers(HashMap<String, String> map) throws Exception;
}
