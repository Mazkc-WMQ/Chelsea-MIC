package upbox.service;

import java.util.HashMap;

/**
 * 
 * @TODO 推送service
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月2日 下午4:54:32 
 * @version 1.0
 */
public interface PublicPushService {
	
	/**
	 * 
	 * @TODO 极光推送
	 * @Title: publicAppPush 
	 * @param map
	 *		code 设备号
	 * 		content 推送内容
	 * 		mes_type 通知类型子集
	 * 		... 点击推送跳转参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:54:16
	 */
	public void publicAppPush(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 发送短信 【华信】[发营销短信]
	 * @Title: publicSendPhoneByHuax 
	 * @param map
	 * 		mobile 手机号
	 * 		content 短信内容
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:52:52
	 */
	public void publicSendPhoneByHuax(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 发送短信 【大鱼】[发验证码]
	 * @Title: publicSendMessage 
	 * @param map
	 * 		phone 手机号
	 * 		mes_type 通知类型子集
	 * 		product 短信内容参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:51:56
	 */
	public void publicSendMessage(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * @TODO 生成推送记录
	 * @Title: addPushMessage 
	 * @param map
	 * 		object_id 事件ID
	 *		type 事件类型  1.订单 2.约战 3.挑战 4.球队
	 *		event 具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期 6.生成订单 7.支付成功
	 *		push_type 推送类型  1.手机 2.推送 3.邮箱
	 *		push_status 推送状态 1-未推 -1-已推 		
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:50:50
	 */
	public boolean addPushMessage(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 判断是否推送
	 * @Title: getPushStatus 
	 * @param map
	 * 		object_id 事件ID
	 *		type 事件类型  1.订单 2.约战 3.挑战 4.球队
	 *		event 具体事件  1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期 6.生成订单 7.支付成功
	 *		push_type 推送类型  1.手机 2.推送 3.邮箱
	 * 		push_status 推送状态 1-未推 -1-已推 
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:51:28
	 */
	public boolean getPushStatus(HashMap<String, String> map) throws Exception;
} 
