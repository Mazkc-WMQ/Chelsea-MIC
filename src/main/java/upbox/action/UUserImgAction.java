package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;

import upbox.model.UUserImg;
import upbox.service.UUserImgService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uUserImgAction")
@Scope("prototype")
public class UUserImgAction extends OperAction implements ModelDriven<UUserImg>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UUserImgService uUserImgService;
	private UUserImg uUserImg;
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
	   TODO - 上传头像
	   @return
	   2015年12月9日
	   dengqiuru
	 */
	public String uploadHeadPicMethod(){
		try {
			hashMap = uUserImgService.uploadHeadPic(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 编辑相册
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String uploadGalleryMethod(){
		try {
			hashMap = uUserImgService.uploadGallery(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 获取相册列表
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String getGalleryListMethod(){
		try {
			hashMap = uUserImgService.getGalleryList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	@Override
	public UUserImg getModel()
	{
		if(null == uUserImg)
			uUserImg = new UUserImg();
		return uUserImg;
	}

	public UUserImg getuUserImg() {
		return uUserImg;
	}

	public void setuUserImg(UUserImg uUserImg) {
		this.uUserImg = uUserImg;
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
