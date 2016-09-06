package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import upbox.model.URegion;
import upbox.service.URegionService;
/**
 * 前端区域接口
 * @author mercideng
 *
 */
@Controller("uRegionAction")
@Scope("prototype")
public class URegionAction extends OperAction implements ModelDriven<URegion> {
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private URegionService uRegionService;
	private URegion uRegion;
	private HashMap<String, Object> hashMap;

	/**
	 * 
	 * 
	   TODO - 获取区域(一层一层选择)
	   @return
	   2016年3月12日
	   dengqiuru
	 */
	public String getURegionListByTypeMethod() {
		try {
			 hashMap = uRegionService.getURegionListByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URegionAction:" + e.getMessage());
			System.out.println("e"+e);
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	public URegion getuRegion() {
		return uRegion;
	}


	public void setuRegion(URegion uRegion) {
		this.uRegion = uRegion;
	}

	@Override
	public URegion getModel() {
		if(null == uRegion)
			uRegion = new URegion();
		return uRegion;
	}

}
