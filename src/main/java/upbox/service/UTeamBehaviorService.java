package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UTeam;
import upbox.model.UTeamBehavior;

/**
 * 
 * 队伍首次行为记录接口
 * @author mercideng
 *
 */
public interface UTeamBehaviorService {
	/**
	 * 
	 * 
	   TODO - 根据球队和事件类型，查询该球队的事件类型是否存在 【2.0.0】
	   @param map
	   		teamId			球队Id
	   		userFollowType	
	   			事件类型  
	   				1-球队建立时间 
	   				2-球队加入upbox时间 
	   				3-首次发起约战 
	   				4-首次成功响应约战 
	   				5-首次响应约战 
	   				6-首次约战成功 
	   				7-首次成为擂主 
	   				8-首次攻擂成功 
	   				9-首次守擂成功			
	   @return
	   		uTeamBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UTeamBehavior getuTeamBehavior(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 判断事件为空时，将事件存入数据库 【2.0.0】
	   @param map
	   		BehaviorType 
	   			事件类型  
	   				1-球队建立时间 
	   				2-球队加入upbox时间 
	   				3-首次发起约战 
	   				4-首次成功响应约战 
	   				5-首次响应约战 
	   				6-首次约战成功 
	   				7-首次成为擂主 
	   				8-首次攻擂成功 
	   				9-首次守擂成功		
	   		objectId	对应事件类型的Id
	   @param uTeam
	   @return
	   		uTeamBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UTeamBehavior insertuTeamBehavior(HashMap<String, String> map, UTeam uTeam)throws Exception;

	/**
	 * 
	 * 
	   TODO - 成立日期变化时，更新数据库 【2.0.0】
	   @param uTeamBehavior
	   		uTeamBehavior对象
	   @param map
	 * 		createDate 事件时间  不是当前时间  例如：球队成立时间
	   @param uTeam
	   		uTeam  对象
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	public void updateDate(UTeamBehavior uTeamBehavior, HashMap<String, String> map,UTeam uTeam)throws Exception;

	/**
	 * 
	 * 
	   TODO - 战队详情--概况--时间轴 【2.0.0】
	   @param map
	   		page    分页
	   @return
	   		uTeamBehaviorList
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getuTeamBehaviorList(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据球队获取该球队所有里程碑的数据 【2.0.0】
	   @param map
	   		teamId			球队Id
	   @return
	   		uTeamBehaviors  集合
	   @throws Exception
	   2016年5月5日
	   dengqiuru
	 */
	public List<UTeamBehavior> getuTeamBehaviors(HashMap<String, String> map) throws Exception;
	
}
