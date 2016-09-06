package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;

import upbox.model.UPlayer;
import upbox.model.UUser;
import upbox.service.UUserService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uUserAction")
@Scope("prototype")
public class UUserAction extends OperAction implements ModelDriven<UUser>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UUserService userService;
	private UUser uUser;
	private UPlayer uPlayer;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 前端用户退出登陆
	 * @return
	 */
//	public String backLoginMethod(){
//		try
//		{
//			//System.out.println(JSON.toJSONString(baseDAO.get(UBUser.class, "6a3a1b4a-b150-4f37-bd3a-10904521250d")));
//			return returnRet(WebPublicMehod.returnRet("sucess", "ok"),null);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			logger.error("BUserAction:" + e.getMessage());
//			return returnRet(null,e);
//		}
//	}
	/**
	 * 
	 * 
	   TODO - 查询前端用户列表【2.0.0.1】
	   @return
	   2015年12月9日
	   dengqiuru
	 */
	public String getUserListMethod(){
		try {
			hashMap = userService.findAll(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	
	/**
	 * 
	 * 
	   TODO - 注册后，填写基本信息
	   @return
	   2016年3月4日
	   dengqiuru
	 */
	public String insertUserinfoMethod(){
		try {
			hashMap = userService.insertUserinfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	/**
	 * 
	 * 
	   TODO - 获得个人信息【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String getUserInfoMethod(){
		try {
			hashMap = userService.getUserInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 编辑个人信息【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	   http://localhost:8080/ssh/uUser_editUserInfo.do
	   ?userId=6c6630eb-7e56-4983-8f4e-32af5d9188e8&nickname=测试&sex=1&remark=测试的&email=123.com&token=369&userStatus=1
	 */
	public String editUserInfoMethod(){
		try {
			hashMap = userService.editUserInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改密码【2.0.0.1】
	   @return
	   2015年12月18日
	   dengqiuru
	 */
	public String updatePasswordMethod(){
		try {
			hashMap = userService.updatePasswordMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	
	/**
	 * 
	 * 
	   TODO - 用户头部信息【2.0.0.1】
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String uuserHeadinfoMethod(){
		try {
			hashMap = userService.uuserHeadinfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 邀请球员时，用户列表【2.0.0.1】
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String getUserlistOfInviteMethod(){
		try {
			hashMap = userService.getUserlistOfInvite(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}	
	/**
	 * 
	 * 
	   TODO - 当前用户红点信息
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String setIsRedMethod(){
		try {
			hashMap = userService.setIsRed(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO 用户登录记录
	 * @Title: insLoginLogMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年7月4日 下午5:39:00
	 */
	public String insLoginLogMethod(){
		try {
			userService.insLoginLog(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uuLoginLogAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(null,null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改年龄为空的数据
	   @return
	   2016年7月14日
	   dengqiuru
	 */
	public String setAgeMethod(){
		try {
			hashMap = userService.setAge(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uuLoginLogAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(null,null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 球员里程碑
	   @return
	   2016年8月11日
	   dengqiuru
	 */
	public String setPlayerMethod(){
		try {
			this.hashMap = this.userService.setPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null, "success");
	}
	@Override
	public UUser getModel()
	{
		if(null == uUser)
			uUser = new UUser();
		return uUser;
	}

	public UUser getUUser()
	{
		return uUser;
	}

	public void setUser(UUser uUser)
	{
		this.uUser = uUser;
	}

	/**
	 * @return the hashMap
	 */
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	/**
	 * @param hashMap the hashMap to set
	 */
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}
	
	public UPlayer getuPlayer() {
		return uPlayer;
	}

	public void setuPlayer(UPlayer uPlayer) {
		this.uPlayer = uPlayer;
	}
}
