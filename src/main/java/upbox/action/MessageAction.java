package upbox.action;


import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import upbox.model.UMessage;
import upbox.service.MessageService;

/**
 * 
 * @TODO 通知action
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午4:45:01 
 * @version 1.0
 */
@Controller("messageAction")
@Scope("prototype")
public class MessageAction extends OperAction implements ModelDriven<UMessage>{
	
	private static final long serialVersionUID = 2346124569599881556L;
	private UMessage mes;
	@Resource
	private MessageService mesService;
	
	private HashMap<String,Object> hashMap;

	/**
	 * 
	 * 
	   TODO - 新增通知【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String getAddMessageMethod(){
		try {
			mesService.addTheMessageByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -通知列表 - 通知分类【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String getMesListByTypeMethod(){
		try {
			hashMap = mesService.getMesListByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -根据类型获取未读信息数量【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String getUnreadCountByTypeMethod(){
		try {
			hashMap = mesService.getUnreadCountByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -获取未读信息数量【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String getUnreadCountMethod(){
		try {
			hashMap = mesService.getUnreadCount(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 改变通知阅读状态【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String updateMessageStautsMethod(){
		try {
			mesService.updateMessageStauts(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 改变通知显示状态【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String updateMessageIsShowMethod(){
		try {
			mesService.updateMessageIsShow(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 批量已读
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String updateMessageStautsByBatchMethod(){
		try {
			mesService.updateMessageStautsByBatch(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 批量删除
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String delMessageByBatchMethod(){
		try {
			mesService.delMessageByBatch(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO 获取通知开关状态
	 * @Title: getMessageSwitch 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月23日 下午4:18:21
	 */
	public String getMessageSwitchMethod(){
		try {
			hashMap = mesService.getMessageSwitch(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO 修改通知开关状态
	 * @Title: updMessageSwitchMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月24日 下午6:06:38
	 */
	public String updMessageSwitchMethod(){
		try {
			hashMap = mesService.updMessageSwitch(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	@Override
	public UMessage getModel()
	{
		if(null == mes){
			mes = new UMessage();
		}
		return mes;
	}

	public UMessage getMes()
	{
		return mes;
	}

	public void setMes(UMessage mes)
	{
		this.mes = mes;
	}
}
