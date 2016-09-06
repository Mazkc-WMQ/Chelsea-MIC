package upbox.service;

import java.util.HashMap;

/**
 * 身价相关接口
 * @author xiao
 *
 */
public interface UWorthService {
	
	/**
	 * 
	 * TODO 根据登录用户获取身价值
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	public HashMap<String, Object> findUserIdWorthCount(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * TODO 根据用户获取完成的身价信息
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	public HashMap<String, Object> findUserIdCompleteWorthInfo(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * TODO 根据用户获取所有身价信息
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	public HashMap<String, Object> findUserIdAllWorthInfo(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * TODO 根据用户获取所有身价信息-WEB
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	public HashMap<String, Object> findUserIdAllWorthInfoWeb(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO  添加完成的任务的身价记录(入驻UPBOX、成为球员、首次建立球队、首次加入球队)
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	public HashMap<String, Object> saveTaskInfo(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * TODO  是否存在待领取的身价任务-红点通知
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月25日
	 */
	public HashMap<String, Object> findUserIdTaskInfo(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 修复已完成任务的身价信息
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月29日
	 */
	public HashMap<String, Object> repairTaskInfo(HashMap<String, String> map) throws Exception;
	
}
