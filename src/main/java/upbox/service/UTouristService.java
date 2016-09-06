package upbox.service;

import java.util.HashMap;

import upbox.model.UEquipment;
import upbox.model.UTourist;
import upbox.model.UUser;

/**
 * 游客接口
 * @author mercideng
 *
 */
public interface UTouristService {
	/**
	 * 
	 * 
	   TODO - 游客登录 【2.0.0】
	   @param map
	   		code 		设备号
	   		codeType	 设备类型
	   		ip	  		客户端IP地址	
	   @return
	   		uTourist的hashMap<String,Object>  
	   @throws Exception
	   2015年12月28日
	   dengqiuru
	 */
	public HashMap<String, Object> touristLogin(HashMap<String, String> map)throws Exception;
	

	/**
	 * 
	 * 
	   TODO -  用户登录 注册  关联时，进行记录[用户表与设备表关联的操作日志 【2.0.0】
	   @param uUserTemp
	   		uUserTemp 对象
	   @param equipment
	   		equipment对象
	   @param type
	   		状态：1--注册  2--登录  3--第三方登录   4--第三方关联
	   @return
	   		UTourist对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public UTourist inTourist(UUser uUserTemp, UEquipment equipment,String type) throws Exception;
}
