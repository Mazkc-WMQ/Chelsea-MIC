package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import upbox.model.UTeamImg;
import upbox.service.UTeamImgService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uTeamImgAction")
@Scope("prototype")
public class UTeamImgAction extends OperAction implements ModelDriven<UTeamImg>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UTeamImgService uTeamImgService;
	private UTeamImg uTeamImg;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * 
	   TODO - 更新队徽
	   @return
	   2016年2月3日
	   dengqiuru
	 */
	public String updateTeamLogoMethod(){
		try {
			hashMap = uTeamImgService.UpdateTeamLogo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 更新、编辑相册
	   @return
	   2016年2月3日
	   dengqiuru
	 */
	public String uploadTeamGalleryMethod(){
		try {
			hashMap = uTeamImgService.uploadTeamGallery(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO -  获取相册列表
	   @return
	   2016年2月3日
	   dengqiuru
	 */
	public String getTeamGalleryListMethod(){
		try {
			hashMap = uTeamImgService.getTeamGalleryList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO -  获取当前用户在该球队上传的相册列表
	   @return
	   2016年2月3日
	   dengqiuru
	 */
	public String getGalleryListByUserIdMethod(){
		try {
			hashMap = uTeamImgService.getGalleryListByUserId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
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


	@Override
	public UTeamImg getModel() {
		if(null == uTeamImg)
			uTeamImg = new UTeamImg();
		return uTeamImg;
	}


	public UTeamImg getuTeamImg() {
		return uTeamImg;
	}


	public void setuTeamImg(UTeamImg uTeamImg) {
		this.uTeamImg = uTeamImg;
	}
	
}
