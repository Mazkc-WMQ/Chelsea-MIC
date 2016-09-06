package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;

import upbox.model.UWorth;
import upbox.service.UWorthService;

/**
 * 身价任务Action
 * @author xiaoying
 *
 */
@Controller("uworthAction")
@Scope("prototype")
public class UWorthAction extends OperAction implements ModelDriven<UWorth>{
	private static final long serialVersionUID = -933464370417457070L;
	
	@Resource
	private UWorthService uworthService;
	private UWorth worth;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * TODO 获取完成的身价信息＋获取所有身价信息
	 * @return
	 * xiaoying 2016年8月6日
	 */
	public String findUserIdWorthAndTaskMethod(){
		try {
			HashMap<String, Object> hashMap1=new HashMap<>();
			hashMap1=uworthService.findUserIdAllWorthInfo(super.getParams());
			hashMap = uworthService.findUserIdCompleteWorthInfo(super.getParams());
			hashMap.put("listTask", hashMap1.get("listTask"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 获取完成的身价信息
	 * @return
	 * xiaoying 2016年8月6日
	 */
	public String findUserIdCompleteWorthInfoMethod(){
		try {
			hashMap = uworthService.findUserIdCompleteWorthInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 获取所有身价信息
	 * @return
	 * xiaoying 2016年8月6日
	 */
	public String findUserIdAllWorthInfoMethod(){
		try {
			hashMap = uworthService.findUserIdAllWorthInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 获取所有身价信息-Web
	 * @return
	 * xiaoying 2016年8月6日
	 */
	public String findUserIdAllWorthInfoWebMethod(){
		try {
			hashMap = uworthService.findUserIdAllWorthInfoWeb(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 添加完成的任务的身价记录
	 * @return
	 * xiaoying 2016年8月6日
	 */
	public String saveTaskInfoMethod(){
		try {
			hashMap = uworthService.saveTaskInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 修复已完成任务的身价信息
	 * @return
	 * xiaoying 2016年8月30日
	 */
	public String repairTaskInfoMethod(){
		try {
			hashMap = uworthService.repairTaskInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UWorthAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	@Override
	public UWorth getModel() {
		if(null == worth)
			worth = new UWorth();
		return worth;
	}
	public UWorth getWorth() {
		return worth;
	}
	public void setWorth(UWorth worth) {
		this.worth = worth;
	}
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}

	
}
