package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import upbox.service.FilterService;

/**
 * 前端用户action
 * @author xiao
 *
 */
@Controller("filterAction")
@Scope("prototype")
public class FilterAction extends OperAction {
	private static final long serialVersionUID = -933464370417457070L;
//	@Resource
//	private BaseDAO baseDAO;
//	@Resource
//	private UOrderService uOrderService;
	@Resource
	private FilterService filterService;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * TODO 筛选-可配置
	 * @return
	 * String
	 * xiao
	 * 2016年6月8日
	 */
	public String findFilterOptionMethod(){
		try {
			hashMap=filterService.findFilterOption(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
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
