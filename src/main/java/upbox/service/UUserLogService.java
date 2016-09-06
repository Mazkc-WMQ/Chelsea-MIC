package upbox.service;

import upbox.model.UUser;

/**
 * 前端用户操作记录接口
 */
public interface UUserLogService {
	/**
	 * 
	 * 
	   TODO -  注册时，记录用户操作 【2.0.0】
	   @param uUser
	   		uUser对象
	   @param regiResource
	   		1:用户登录
	   		2：第三方登录
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public void saveUserLog(UUser uUser,String regiResource) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 登录时，更新用户操作记录表 【2.0.0】
	   @param uUser
	   		uUser对象
	   2015年12月28日
	   dengqiuru
	 * @throws Exception 
	 */
	public void updateUuserLog(UUser uUser) throws Exception;
}
