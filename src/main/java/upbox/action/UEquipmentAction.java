package upbox.action;


import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;

import upbox.model.UEquipment;
import upbox.service.UEquipmentService;

/**
 * 前端设备action
 * @author dengqiuru
 *
 */
@Controller("uEquipmentAction")
@Scope("prototype")
public class UEquipmentAction extends OperAction implements ModelDriven<UEquipment>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UEquipmentService uEquipmentService;
	private UEquipment uEquipment;
	

	/**
	 * 
	 * 
	   TODO - 用户没登录、没注册获取用户设备信息
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @throws Exception
	   2016年3月16日
	   dengqiuru
	 */
	public String insertEquipmentMethod(){
		try {
			uEquipmentService.insertEquipment(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(null, null,"success");
	}

	@Override
	public UEquipment getModel() {
		if(null == uEquipment)
			uEquipment = new UEquipment();
		return uEquipment;
	}
	
	
}
