package upbox.service;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @TODO 通知service
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午3:22:02 
 * @version 1.0
 */
public interface MessageService {
	
	/**
	 * 
	 * @TODO 应用通知：生成一条通知
	 * @Title: addTheMessageByType 
	 * @param map
	 * 		必填参数
	 * 		type 通知类型
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数  
	 * 		params 点击通知跳转所需参数
	 * 		userId 与通知关联的用户id(不一定是当前登录用户)
	 * 		可选参数
	 * 		repetition 是否去重 1 是 0否  
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:29:42
	 */
	public boolean addTheMessageByType(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 应用通知：生成多条通知
	 * @Title: addMoreMessageByType 
	 * @param map
	 * 		type 通知类型
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数    
	 * 		params 点击通知跳转所需参数
	 * 		teamId 球队id(用于通知队内所有球员)
	 * 		可选参数
	 * 		repetition 是否去重 1 是 0否  
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:30:31
	 */
	public boolean addMoreMessageByType(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 个人通知列表 - 通知分类 【球队、订场、激战】
	 * @Title: getMesListByType 
	 * @param map
	 * 		type 通知类型
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:30:52
	 */
	public HashMap<String, Object> getMesListByType(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 根据类型获取未读信息数量
	 * @Title: getUnreadCountByType 
	 * @param map
	 * 		type 通知类型
	 * 		userId 当前用户
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:31:18
	 */
	public HashMap<String, Object> getUnreadCountByType(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 获取未读信息数量
	 * @Title: getUnreadCount 
	 * @param map
	 * 		userId 当前用户
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:14
	 */
	public HashMap<String, Object> getUnreadCount(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 改变通知状态
	 * @Title: updateMessageStauts 
	 * @param map
	 * 		mesId 通知主键
	 * 		type 通知类型
	 * 		userId 通知关联的用户Id 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:31
	 */
	public void updateMessageStauts(HashMap<String,String> map)throws Exception;

	/**
	 * 
	 * @TODO 删除通知
	 * @Title: updateMessageIsShow 
	 * @param map
	 * 		mesId 通知主键
	 * 	 	type 通知类型
	 * 		userId 通知关联的用户Id 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:34:15
	 */
	public void updateMessageIsShow(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 激战无人响应的推送
	 * @Title: fightingPushByNoResponse 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:03
	 */
	public void fightingPushByNoResponse(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 订单提醒到场推送
	 * @Title: orderPushByRemindReach 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:21
	 */
	public void orderPushByRemindReach(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 订单未约战推送
	 * @Title: orderPushByNoSponsorFight 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:30
	 */
	public void orderPushByNoSponsorFight(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 获取球队队员的设备号
	 * @Title: getTeamPlayerCodeAndPhone 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:06:09
	 */
	public List<HashMap<String, String>> getTeamPlayerCodeAndPhone(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 给队内所有人发推送
	 * @Title: pushFightToPlayerOnTeamByType 
	 * @param map
	 * 		teamId 球队主键
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:06:18
	 */
	public void pushFightToPlayerOnTeamByType(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 给队内所有人发推送 【解散球队】
	 * @Title: pushTeamToPlayerOnByDissolve 
	 * @param map
	 * 		teamId 球队主键
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:17
	 */
	public void pushTeamToPlayerOnByDissolve(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 给APP老用户推：发布新的版本
	 * @Title: pushOldUserByNewVersions 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:33
	 */
	public void pushOldUserByNewVersions(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * @TODO 给APP所有用户且没有补全球员信息推：补全球员信息
	 * @Title: pushUserByRepairPlayerInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:43
	 */
	public void pushUserByRepairPlayerInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO  给APP所有用户且没有补全球队信息推：补全并约战
	 * @Title: pushUserByRepairTeamInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:56
	 */
	public void pushUserByRepairTeamInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 给APP所有用户且没有补全球队球员信息推：补全球队球员信息
	 * @Title: pushUserByRepairTeamInfoAndPlayerInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午5:46:57
	 */
	public void pushUserByRepairTeamInfoAndPlayerInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 批量已读
	 * @Title: updateMessageStautsByBatch 
	 * @param map
	 * 		mesId 通知主键
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:31
	 */
	public void updateMessageStautsByBatch(HashMap<String,String> map)throws Exception;

	/**
	 * 
	 * @TODO 批量删除
	 * @Title: delMessageByBatch 
	 * @param map
	 * 		mesId 通知主键
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:34:15
	 */
	public void delMessageByBatch(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO  招募球员发送短信
	 * @Title: sendRecruitPlayer 
	 * @param map
	 * 	phoneList 手机号码  多个以，分隔
	 * 	content 发送内容
	 * 	teamId 球队id
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月22日 下午4:14:58
	 */
	public boolean sendRecruitPlayer(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 根据年龄发短信
	 * @Title: pushUserByAge 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月29日 下午12:56:07
	 */
	public void pushUserByAge(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 通知开关状态
	 * @Title: getMessageSwitch 
	 * @param map
	 * 	userId
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月22日 下午5:00:56
	 */
	public HashMap<String, Object> getMessageSwitch(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 修改通知开关状态
	 * @Title: updMessageSwitch 
	 * @param map
	 * 	userId
	 * 	type
	 *  statues
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月23日 下午4:37:44
	 */
	public HashMap<String, Object> updMessageSwitch(HashMap<String, String> map) throws Exception;
}
