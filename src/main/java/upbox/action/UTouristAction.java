package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;

import upbox.model.UTourist;
import upbox.service.UTouristService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uTouristAction")
@Scope("prototype")
public class UTouristAction extends OperAction implements ModelDriven<UTourist>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UTouristService uTouristService;
	private UTourist uTourist;
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
	   TODO - 游客登录
	   @return
	   2015年12月9日
	   dengqiuru
	 */
	public String touristLoginMethod(){
		try {
			hashMap = uTouristService.touristLogin(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTouristAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	@Override
	public UTourist getModel()
	{
		if(null == uTourist)
			uTourist = new UTourist();
		return uTourist;
	}

	public UTourist getuTourist() {
		return uTourist;
	}
	public void setuTourist(UTourist uTourist) {
		this.uTourist = uTourist;
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
}
