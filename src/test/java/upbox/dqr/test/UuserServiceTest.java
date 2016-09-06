package upbox.dqr.test;

import java.util.HashMap;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.model.UPlayer;
import upbox.model.UUser;
import upbox.service.UUserService;

/**
 * 测试前端用户接口 -- uUserService
 * @author mercideng
 *
 */
@ContextConfiguration("applicationContext.xml")
public class UuserServiceTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private UUserService uUserService;
	private HashMap<String, Object> resultMap;
	
	private UUser uUser;
	
	private UPlayer uPlayer;
	//UpYunFileBucket.deleteFile(ubi.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[1]);
	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	   dengqiuru
	 */
	public void printInfo(String temp,Object obj){
		//System.out.println(temp + JSON.toJSONString(obj));
	}
	/**
	 * 
	 * 
	   TODO - 测试前端用户获取验证码
	   @throws Exception
	   2015年12月14日
	   dengqiuru
	 */
//	@Test
//	public void testVerCodeMethod() throws Exception {
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("phone", "11618038746");
////		hashMap.put("rt", "forgetPwd");
//		resultMap = uUserService.getVerCodeMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 测试前端用户注册接口
	   		phone		手机
	   		phonecode	验证码
	   		code		设备号
	   		phoneType	设备类型
	   		nickname	昵称
	   		loginpassword	密码
	   		ip			客户端IP地址
	   @throws Exception
	   2015年12月14日
	   dengqiuru
	 */
//	@Test
//	public void registerMethod() throws Exception {
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("phone", "11618038746");
//		hashMap.put("phonecode", "160196");
//		hashMap.put("code", "1234");
//		hashMap.put("phoneType", "2");
//		hashMap.put("nickname", "xiaoxiao");
//		hashMap.put("loginpassword", "000000");
//		hashMap.put("ip", "123.123.123");
//		resultMap = uUserService.registerMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	
//	/**
//	 * 
//	 * 
//	   TODO - 测试前端用户忘记密码接口
//	   		phone			手机
//	   		phonecode		验证码
//	   		loginpassword	密码
//	   @throws Exception
//	   2015年12月15日
//	   dengqiuru
//	 */
//	@Test
//	public void getPasswordMethod() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("phone", "123123123");
//		hashMap.put("phonecode", "172813");
//		hashMap.put("loginpassword", "111111");
//		resultMap = uUserService.getPasswordMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	/**
	 * 
	 * 
	   TODO - 测试前端用户登录接口
	   		phone			手机
	   		code			设备号
	   		phoneType		设备类型
	   		loginpassword	密码
	   		ip				客户端IP地址
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
//	@Test
//	public void loginMethod() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("phone", "11618038746");
//		hashMap.put("loginpassword", "000000");
//		hashMap.put("ip", "127.0.0.1");
//		hashMap.put("code", "123123123");
//		hashMap.put("phoneType", "123");
//		hashMap.put("token", "b65de29c-3922-4b28-9c01-4b720c7fba48");
//		resultMap = uUserService.loginMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 测试前端用户个人信息接口
	   		userId			
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
//	@Test
//	public void getUserInfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		resultMap = uUserService.getUserInfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	/**
	 * 
	 * 
	   TODO - 测试前端用户修改个人信息接口
	   		nickname		姓名
	   		birthday		生日
	   		userId			登录人的id
	   		sex				性别
	   		remark			简介
	   		email			邮箱
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
//	@Test
//	public void editUserInfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		uUser = new UUser();
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		hashMap.put("nickname", "07eae762-819f-4120-a184-56043e56ff76");
//		uUser.setNickname("小邓邓22");
////		uUser.setBirthday(new Date());
////		uUser.setSex("1");
////		uUser.setEmail("123");
//		resultMap = uUserService.editUserInfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	

	/**
	 * 
	 * 
	   TODO - 修改密码
	   @param map
	   		phone				手机号码
	   		oldloginpassword	原始密码
	   		newloginpassword	新密码
	   @return
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	    http://localhost:8080/ssh/uuser_updatePassword.do?phone=15618038746&oldloginpassword=654321&newloginpassword=111111
	 */
//	@Test
//	public void updatePasswordMethod() throws Exception {
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("phone", "11618038746");
//		hashMap.put("oldloginpassword", "123456");
//		hashMap.put("newloginpassword", "000000");
//		resultMap = uUserService.updatePasswordMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	/**
	 * 
	 * 
	   TODO - 第三方登录
	   @param map
	   		source		来源：1=微信；2=微博；3=QQ
	   		logintoken	第三方登录userId
	   		code
	   		phoneType
	   		ip
	   		
	   @return
	   @throws Exception
	   2015年12月18日
	   dengqiuru
	 */
//	public void loginAuthMethod() throws Exception {
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("source", "1");
//		hashMap.put("logintoken", "123");
//		hashMap.put("code", "121212");
//		hashMap.put("phoneType", "11111");
//		hashMap.put("ip", "123.0.1");
//		resultMap = uUserService.loginAuthMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}


	/**
	 * 
	 * 
	   TODO -  第三方登录关联
	   @param map
	   		source			来源：1=微信；2=微博；3=QQ
	   		logintoken		第三方登录userId
	   		openid			微信用户openid
	   		phone			手机号
	   		loginpassword	密码
	   @return
	   @throws Exception
	   2015年12月21日
	   dengqiuru
	 */
//	@Test
//	public void loginAuthTiedMethod() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("source", "1");
//		hashMap.put("logintoken", "123");
//		hashMap.put("openid", "123");
//		hashMap.put("loginpassword", "123456");
//		hashMap.put("phone", "15618038746");
//		resultMap = uUserService.loginAuthTiedMethod(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	/**
	 * 
	 * 
	   TODO - 用户头部信息
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
//	@Test
//	public void uuserHeadinfoMethod() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("userId", "6b3f0ac2-1eaa-4d0a-b044-4a23ffe860c6");
//		resultMap = uUserService.uuserHeadinfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	
	/**
	 * 
	 * 
	   TODO - 邀请球员，用户列表
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
//	@Test
//	public void getUserlistOfInvite() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("userId", "13923fd1-75ba-4301-9feb-8ade66ad86a3");
//		resultMap = uUserService.getUserlistOfInvite(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 邀请球员，用户列表
//	   @throws Exception
//	   2016年1月26日
//	   dengqiuru
//	 */
//	@Test
//	public void getUserinfoByToken() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("token", "c106090d-303c-4e5f-a21c-0d2bbab1f233");
//		uUser = uUserService.getUserinfoByToken(hashMap);
//		printInfo("------------------------------",uUser);
//	}
//	 */
//	@Test
//	public void getUserinfoByUserId() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("loginUserId", "058ebac5-4425-470b-a804-adb73c63ab73");
//		String token = uUserService.getUserinfoByUserIdCreatetoken(hashMap);
//		printInfo("------------------------------",token);
//	}
//	uuserHeadinfo

//	@Test
//	public void uuserHeadinfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("loginUserId", "0a018770-5796-431d-a080-a733580e3032");
//		resultMap = uUserService.uuserHeadinfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	

	@Test
	public void getUserinfoByOtherUserId() throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userId", "dad4897d-8c61-451e-b3f3-dc8cb83fc147");
		hashMap = uUserService.getUserinfoByOtherUserId(hashMap);
		printInfo("------------------------------",resultMap);
	}
}
