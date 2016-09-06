package upbox.service;

import java.util.HashMap;

import upbox.model.UEquipment;
import upbox.model.UUser;

/**
 * 前端设备接口
 */
public interface UEquipmentService {

	/**
	 * 
	 * 
	   TODO - 根据code获取设备Id 【2.0.0】
	   @param map
	   		code    设备号
	   @return
	   		equipmentId  设备Id
	   2016年4月7日
	   dengqiuru
	 * @throws Exception 
	 */
	public String getEquipmentIdByCode(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 将设备等信息录入前端用户设备表内 【2.0.0】
	   @param uUser
	   		uUser 		实体
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @return
	   		UEquipment 对象
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	public UEquipment insertEquipmentUser(UUser uUser,HashMap<String, String> map) throws Exception;
//	
//	/**
//	 * 
////	 * 
////	   TODO - 第三方登录、关联时 设备绑定用户表 【2.0.0】
////	   @param uUser
////	   		uUser 		实体
////	   @param map
////	   		userId		用户Id
////	   		code		设备号
////	   		phoneType	设备类型
////	   		ip			客户端IP地址
////	   @param type
////	   		状态：1--注册  2--登录  3--第三方登录   4--第三方关联
////	   @return
////	   		UEquipment 对象
////	   @throws Exception
////	   2016年2月22日
////	   dengqiuru
////	 */
////	public UEquipment updateEquipmentByUserId(UUser uUser,HashMap<String, String> map,String type) throws Exception;
//	
	/**
	 * 
	 * 
	   TODO - 用户没登录、没注册获取用户设备信息 【2.0.0】
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @throws Exception
	   2016年3月16日
	   dengqiuru
	 */
	public void insertEquipment(HashMap<String, String> map)throws Exception;
}
