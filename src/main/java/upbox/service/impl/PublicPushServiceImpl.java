package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UPushMessage;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicPushService;

/**
 * 
 * @TODO 推送service实现类
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月2日 下午4:54:56 
 * @version 1.0
 */
@Service("publicPush")
public class PublicPushServiceImpl implements PublicPushService {
	
	@Resource
	private OperDAOImpl baseDAO;

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
	@Override
	public void publicAppPush(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if(null!=map.get("code")&&null!=map.get("mes_type")){
			
			StringBuffer sql = new StringBuffer();
			sql.append("select ums.* from u_message_switch ums ");
			sql.append("left join u_user uu on uu.user_id = ums.user_id ");
			sql.append("left JOIN u_equipment ue on ue.key_id = uu.numberid ");
			sql.append("where ums.type = (select umsi.type from u_message_switchinfo umsi where umsi.mes_type = :mes_type)  ");
			sql.append("and ums.switch_status = '0'  ");
			sql.append("and ue.code = :code ");
			List<HashMap<String, Object>> umsList = baseDAO.findSQLMap(map,sql.toString());
			if(umsList.size()<=0){
				HashMap<String, String> pushMap = new HashMap<String, String>();
				HttpUtil http = new HttpUtil();
				//极光APPKEY
				pushMap.put("appKey",Public_Cache.JPUSH_APPKEY);
				//极光密码
				pushMap.put("masterSecret",Public_Cache.JPUSH_MASTERSC);
				//true 生产  false 开发
				pushMap.put("IOS_OPTIONS",Public_Cache.IOS_OPTIONS);
				
				//获取设备ID
				pushMap.put("registerAtionID",map.get("code"));
				
				//推送内容
				if(null!=map.get("content")){
					pushMap.put("alert",Public_Cache.getPushCon(map.get("mes_type")).replace("XXX",map.get("content")));
				}else{
					pushMap.put("alert",Public_Cache.getPushCon(map.get("mes_type")));
				}
				
				pushMap.put("msgContent",JSON.toJSON(map).toString());
				
				//推送标识 
				//1 推送所有设备 2. 根据设备号 3.根据所以设备别名组推送  
				//4.根据android-ios设备标签组推送 5.根据ios设备的标签组推送
				//6.根据ios-android设备的标签组别名组推送
				pushMap.put("pushMethod","2");
				
				//极光推送
				HttpRespons response = http.sendPost(Public_Cache.PUSH_SERVER + "/push_jpush.do", pushMap);
				HashMap<String, Object> maps = (HashMap<String, Object>) PublicMethod.parseJSON2Map(response.getContent());
				if ("操作成功".equals(maps.get("msg"))) {
					System.out.println("极光推送发送成功！");
				}
			}
		}
	}

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
	@Override
	public void publicSendPhoneByHuax(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if(null!=map.get("phone")&&null!=map.get("content")){
			HttpUtil http = new HttpUtil();
			HashMap<String, String> sendMap = new HashMap<String, String>();
			
			sendMap.put("mobile", map.get("phone"));
			sendMap.put("content", map.get("content"));
			
			HttpRespons response = http.sendPost(Public_Cache.PUSH_SERVER + "/push_pushMes.do", sendMap);
//			HashMap<String, Object> maps = (HashMap<String, Object>) PublicMethod.parseJSON2Map(response.getContent());
//			if ("操作成功".equals(maps.get("msg"))) {
//				System.out.println("华信短信发送成功！");
//			}
		}
	}
	
	/**
	 *  发送短信 【大鱼】 [通用]
	 */ 
	@Override
	public void publicSendMessage(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if(null!=map.get("phone")&&null!=map.get("mes_type")){
			String mobile = new String();
			HashMap<String, String> sendMap = new HashMap<String, String>();
			HashMap<String, String> hashMap = new HashMap<String, String>();
			//短信推送内容参数
			if(null!=map.get("product")){
				sendMap.put("product",map.get("product"));
			}else{
				sendMap.put("product","");
			}
			hashMap.put("extend","激战联盟"); //回调参数 [暂时用不到]
			hashMap.put("rec_num", map.get("phone")); //手机号
			hashMap.put("sms_free_sign_name","激战联盟");//短信签名(模板名)
			hashMap.put("sms_template_code",Public_Cache.getPhoneTemplateCode(map.get("mes_type")));//短信模板ID
			WebPublicMehod.send(mobile, sendMap, hashMap);
			//System.out.println("---------------------------------->手机推送");
		}
	}

	/**
	 * 
	 * @TODO 生成推送记录
	 * @Title: addPushMessage
	 * @param map
	 *            object_id 事件ID type 事件类型 1.订单 2.约战 3.挑战 4.球队 event 具体事件
	 *            1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期 6.生成订单 7.支付成功
	 *            push_type 推送类型 1.手机 2.推送 3.邮箱 push_status 推送状态 1-未推 -1-已推
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:50:50
	 */
	@Override
	public synchronized boolean addPushMessage(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if (null != map.get("type") && null != map.get("event") && null != map.get("push_status")
				&& null != map.get("push_type") && null != map.get("object_id")) {
			String object_id = map.get("object_id");
			String type = map.get("type");
			String event = map.get("event");
			String push_status = map.get("push_status");
			String push_type = map.get("push_type");

			UPushMessage uPushMessage = null;
			uPushMessage = baseDAO.get(map,"from UPushMessage where object_id = :object_id and type = :type and event = :event and push_type = :push_type ");
			if(null==uPushMessage){
				if (!Constant.PUSH_CHECK_LIST.contains(map.get("object_id") + "_" + map.get("event") + "_"
						+ map.get("push_type") + "_" + map.get("type"))) {
					Constant.PUSH_CHECK_LIST.add(map.get("object_id") + "_" + map.get("event") + "_" + map.get("push_type")
							+ "_" + map.get("type"));
					uPushMessage = new UPushMessage();
					uPushMessage.setPush_id(WebPublicMehod.getUUID());
					uPushMessage.setObject_id(object_id);
					uPushMessage.setType(type);
					uPushMessage.setEvent(event);
					uPushMessage.setPush_status(push_status);
					uPushMessage.setPush_type(push_type);
					uPushMessage.setCreatetime(new Date());

					baseDAO.save(uPushMessage);
					baseDAO.getSessionFactory().getCurrentSession().flush();
//					System.out.println(JSON.toJSONString(Constant.PUSH_CHECK_LIST));
					return true;
				}
			}
		}
		return false;
	}

	
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
	@Override
	public boolean getPushStatus(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		boolean result = false;
		
		if(null!=map.get("type")&&null!=map.get("event")&&null!=map.get("push_status")&&null!=map.get("push_type")&&null!=map.get("object_id")){
			
			UPushMessage uPushMessage = null;
			String hql = " from UPushMessage where object_id =:object_id and type = :type and push_type = :push_type and push_status = :push_status ";
			uPushMessage = baseDAO.get(map,hql);
			if(null==uPushMessage){
				result = true;
			}
		}
		
		return result;
	}

	
}
