package upbox.service;

import java.util.HashMap;
import upbox.model.UEquipment;
import upbox.model.UUser;

/**
 * 前端用户接口
 */
public interface UUserService {
	/**
	 * 
	 * 
	   TODO - 查询所有正常使用的用户列表 【2.0.0】
	   @param map
			page  	分页
	   @return
	   		uUserList 的hashMap
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	public HashMap<String, Object> findAll(HashMap<String,String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 获取验证码 【2.0.0】
	   @param map
	   		phone  手机号码
	   		rt	   标示  标示暂时分两种
	   			1、null/直接不传：注册时获取验证码
	   			2、forgetPwd：忘记密码时，获取验证码
	   @return
	   		发送短信的提示：success
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	public HashMap<String, Object> getVerCodeMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户注册 【2.0.0】
	   @param map
       		 source			登录来源：1、微信；2、微博；3、QQ
       		 logintoken		第三方登录token
			 phone  		手机号码 
			 phonecode		 验证码  
			 code 			设备号  
			 realName 		真实姓名  
			 phoneType 		设备类型  
			 loginpassword 	登录密码 
			 ip  			客户端IP地址  
	   @return
	   		uUser 的hashMap<String, Object>
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	public HashMap<String, Object> registerMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 注册后录入基本信息  【2.0.0】
	   @param map
	   		userId			用户Id
	   		nickName		真实姓名
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		expertPosition  擅长位置
	   		canPosition		可踢位置
	   		number			背号
	   		practiceFoot	惯用脚
	   		imgSizeType		图片尺寸类型
	   		imgurl			图片显示地址
	   		weight			图片权重
	   		saveurl 		图片存储地址
	   @return
	   		uUser 对象的hashMap<String,Object>
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	public HashMap<String, Object> insertUserinfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 忘记密码 【2.0.0】
	   @param map
	   		phone			手机号码
	   		phonecode		验证码
	   		loginpassword	设置密码
	   @return
	   		uUser 的hashMap<String, Object>
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	public HashMap<String, Object> getPasswordMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改密码 【2.0.0】
	   @param map
	   		phone				手机号码
	   		oldloginpassword	原始密码
	   		newloginpassword	新密码
	   @return
	   		uUser 的hashMap<String, Object>
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	public HashMap<String, Object> updatePasswordMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 登录 【2.0.0】
	   @param map
	   		phone			手机号码
	   		loginpassword	密码
	   		ip				客户端IP地址
	   		code			设备号
	   		phoneType		设备类型
	   @return
	   		uUser 的hashMap<String, Object>
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	public HashMap<String, Object> loginMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询用户基本信息 【2.0.0】
	   @param map
	   		loginUserId		用户Id
	   @return
	   		uUser 的hashMap<String, Object>
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	public HashMap<String, Object> getUserInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改个人信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户Id
	   		nickname		姓名
	   		birthday		生日
	   		userId			登录人的id
	   		area			区域Id
	   		sex				性别
	   		remark			简介
	   		email			邮箱
	   @return
	   		uUserTemp 的hashMap<String, Object>
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	public HashMap<String, Object> editUserInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 第三方登录 【2.0.0】
	   @param map
	   		source		登录来源：1、微信；2、微博；3、QQ
	   		logintoken	第三方登录userId
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户IP
	   @return
	   		uUserTemp 的hashMap<String, Object>
	   @throws Exception
	   2015年12月18日
	   dengqiuru
	 */
	public HashMap<String, Object> loginAuthMethod(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO -  第三方登录关联 【2.0.0】
	   @param map
	   		source			来源：1=微信；2=微博；3=QQ
	   		logintoken		第三方登录userId
	   		openid			微信用户openid
	   		phone			手机号
	   		loginpassword	密码
	   		code			设备号
	   		phoneType		设备类型
	   		ip				客户IP
	   @return
	   		uUser 的hashMap
	   @throws Exception
	   2015年12月21日
	   dengqiuru
	 */
	public HashMap<String, Object> loginAuthTiedMethod(HashMap<String, String> map,UUser uUser) throws Exception;	
	
	/**
	 * 
	 * 
	   TODO - 查询用户头部信息 【2.0.0】
	   @param map
	   @return
	   		uUser 的hashMap
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	public HashMap<String, Object> uuserHeadinfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 订单中，预定人手机号为新号，库内无数据，那么新增此人信息 【2.0.0】
	   @param map
	   		phone		手机号码
	   		realName	真实姓名
	   @return
	   		uUser 对象
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	public UUser insertOrderNewUser(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 每个列表的头部信息 【2.0.0】
	   @param map
	   @return
	   		UUser对象
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	public UUser getUserinfoByUserId(HashMap<String, String> map) throws Exception;

	/**
	 * TODO - 通过userId 获取用户对象
	 * @param map userId 用户Id
	 * @return
	 * @throws Exception
	 */
	public UUser getUserByUserId(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 编辑球员信息时，修改用户体重，身高，年龄信息 【2.0.0】
	   @param map
	   		height		身高
	   		weight		体重
	   		birthday	出生日期
	   @param uUser
	   		UUser对象
	   2016年2月16日
	   dengqiuru
	 * @throws Exception 
	 */
	public void inPlayerUpdateUUser(HashMap<String, String> map, UUser uUser) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 更新设备号ID到u_user表里面 【2.0.0】
	   @param uUser
	   		uUser  对象
	   @param equipment
	   		equipment	对象
	   @throws Exception
	   2016年2月23日
	   dengqiuru
	 */
	public void updateUuserEquip(UUser uUser,UEquipment equipment) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 邀请球员时，用户列表 【2.0.0】
	   @param map
	   		userId
	   @return
	   		uUserlist 的hashMap<String,Object>
	   @throws Exception
	   2016年3月1日
	   dengqiuru
	 */
	public HashMap<String, Object> getUserlistOfInvite(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据token获取uuser对象 【2.0.0】
	   @param map
	   		token
	   @return
	   		UUser 对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UUser getUserinfoByToken(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 建队时，判断队长信息是否填写完整 【2.0.0】
	   @param uUser
	   		uUser 对象
	   @return
	   		String  提示消息
	   2016年3月11日
	   dengqiuru
	 */
	public String isPlayerinfoComplete(UUser uUser)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 后台用来测试，用userId获取Token 【2.0.0】
	   @param map
	   		loginUserId		用户Id
	   @return
	   @throws Exception
	   2016年3月30日
	   dengqiuru
	 */
	public String getUserinfoByUserIdCreatetoken(HashMap<String, String> map) throws Exception;

	/**
	 *
	 * 
	   TODO - 检查该设备号是否存在，如果存在就清除 【2.0.0】
	   @param map
	   		equipmentId   设备号Id
	   @throws Exception
	   2016年4月18日
	   dengqiuru
	 */
	public void updateUserEquipment(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查询别人的用户对象 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年4月18日
	   dengqiuru
	 */
	public HashMap<String, String> getUserinfoByOtherUserId(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 将用户对象在缓存删除后在更新到缓存中去 【2.0.0】
	   @param uUser
	   		uUser对象
	   @throws Exception
	   2016年4月27日
	   dengqiuru
	 */
	public void updateRedisUuserinfo(UUser uUser) throws Exception;

	/**
	 * 
	 * 
	   TODO - 未读通知 【2.0.0】
	   @param map
	   		loginUserId    当前用户Id
	   @return
	   @throws Exception
	   2016年5月4日
	   dengqiuru
	 */
	public HashMap<String, Object> setIsRed(HashMap<String, String> map) throws Exception;
	
//	/**
//	 * 
//	 * 
//	   TODO - 用户获取lbs
//	   @param map
//	   		userId   用户Id
//	   @return
//	   @throws Exception
//	   2016年6月12日
//	   dengqiuru
//	 */
//	public HashMap<String, Object> getBaidulbs(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 新增用户百度云LBS数据
	 * @Title: insUserBaiduLBSData 
	 * @param map
	 * 	userId 用户主键
	 * 	address 地区
	 *  user_id_int 用户数值类型ID
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月29日 下午2:01:37
	 */
	public void insUserBaiduLBSData(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 修改用户百度云LBS数据
	 * @Title: updUserBaiduLBSData 
	 * @param map
	 * 	userId 用户主键
	 * 	address 地区
	 *  user_id_int 用户数值类型ID
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月29日 下午2:09:23
	 */
	public void updUserBaiduLBSData(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * @TODO 用户登录记录
	 * @Title: insLoginLog
	 * @param map
	 * 	token 		
	 * 	resource 	设备来源
	 * 	appcode		app版本号
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月21日 下午3:06:55
	 */
	public void insLoginLog(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据手机号查询用户信息,若没有则新增
	   @param map
	   		phone		手机号
	   		realName	真实姓名
	   		nickName	昵称
	   		sex			性别
	   		birthday	出生年月
	   		height		身高
	   		weight		体重
	   		area		区域
	   @return
	   2016年7月8日
	   dengqiuru
	 * @throws Exception 
	 */
	public UUser getUserinfoByPhone(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改年龄为空的数据
	   @param map
	   @return
	   @throws Exception
	   2016年7月14日
	   dengqiuru
	 */
	public HashMap<String, Object> setAge(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改年龄，擅长位置，加队数时更新lbs
	   @param uUser
	   2016年7月22日
	   dengqiuru
	 * @throws Exception 
	 */
	public void setAreaToBaiduLBS(UUser uUser) throws Exception;
	/**
	 * 
	 * TODO 修改球员信息－包括其他球员的
	 * @param map
	 * @param uUser
	 * @throws Exception
	 * xiaoying 2016年7月26日
	 */
	public void updateUserInfo(HashMap<String, String> map, UUser uUser) throws Exception;
	
	/**
	 * 
	 * @TODO 用户数据更新至百度云【跑批】
	 * @Title: pubUpdAllUserLBS 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月27日 下午5:55:30
	 */
	public void pubUpdAllUserLBS(HashMap<String, String> map) throws Exception;

	/**
	 * TODO - 通过球员获取用户对象
	 * @param map
	 * @throws Exception
	 */
	public UUser getUUserByPlayerId(HashMap<String,String> map) throws Exception;

	/**
	 * TODO - 通过球员查用户对象
	 * @param playerId
	 * @return
	 * @throws Exception
	 */
	public UUser getUUserByPlayerId(String playerId) throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新以前没有球队的用户信息
	   @param map
	   @return
	   @throws Exception
	   2016年8月23日
	   dengqiuru
	 */
	public HashMap<String, Object> setPlayer(HashMap<String, String> map) throws Exception;
	
}
