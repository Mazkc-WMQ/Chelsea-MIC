package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UUser;
import upbox.model.UUserBehavior;

/**
 * 
 * 用户首次行为记录接口
 * @author mercideng
 *
 */
public interface UUserBehaviorService {
	/**
	 * 
	 * 
	   TODO - 根据用户和事件类型，查询该用户的事件类型是否存在 【2.0.0】
	   @param map
	   		userId		当前用户Id
	   		userFollowType		
	   				事件类型
	   					1-注册 
	   					2-首次建立球队 
	   					3-首次加入球队 
	   					4-首次发布动态 
	   					5-首次关注球队			
	   @return
	   		UUserBehavior对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UUserBehavior getuUserBehavior(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 判断事件为空时，将事件存入数据库 【2.0.0】
	   @param map
	   		BehaviorType 	
	   				事件类型
	   					1-注册 
	   					2-首次建立球队 
	   					3-首次加入球队 
	   					4-首次发布动态 
	   					5-首次关注球队
	   		objectId	对应事件类型的Id
	   @param uUser
	   @return
	   		UUserBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UUserBehavior insertuUserBehavior(HashMap<String, String> map,UUser uUser)throws Exception;
	/**
	 * 
	 * 
	   TODO - 球员详情时间轴 【2.0.0】
	   @param map
	   		page    分页
	   		userId	用户Id
	   @return
	   		uUserBehaviorlist集合
	   @throws Exception
	   2016年3月9日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getuUserBehaviorList(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 获取当前球队所有的里程碑 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年5月5日
	   dengqiuru
	 */
	public List<UUserBehavior> getuUserBehaviors(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 获取球队里程碑字段详细 [从数据库]
	   @param hashMap2
	   @return
	   @throws Exception
	   2016年8月17日
	   dengqiuru
	 */
	public String getUTeamBehaviorJsonStr(HashMap<String, Object> hashMap2) throws Exception;

}
