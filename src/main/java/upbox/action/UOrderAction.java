package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import upbox.model.UOrder;
import upbox.service.UOrderService;

/**
 * 前端用户action
 * @author xiao
 *
 */
@Controller("uorderAction")
@Scope("prototype")
public class UOrderAction extends OperAction implements ModelDriven<UOrder>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UOrderService uOrderService;

	private UOrder uOrder;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * 
	   TODO - 订单详细
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String findOrderinfoMethod(){
		try {
			hashMap = uOrderService.findOrderinfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 订单列表
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String findOrderlistMethod(){
		try {
			hashMap = uOrderService.findOrderlist(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 响应方看到的订单详情
	 * @return
	 * String
	 * xiao
	 * 2016年4月19日
	 */
	public String findOrderinfoResprMethod(){
		try {
			hashMap = uOrderService.findOrderinfoResp(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
		
	}
	/**
	 * 
	 * TODO 订单过期
	 * @return
	 * String
	 * xiao
	 * 2016年5月15日
	 */
	public String uOrderExpiredMethod(){
		try {
			hashMap = uOrderService.uOrderExpired(super.getParams());//已过期
			//uOrderService.uOrderAboutExpired(super.getParams());//即将过期
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	/**
	 * 
	 * TODO 约战挑战订单
	 * @return
	 * String
	 * xiao
	 * 2016年3月31日
	 */
	public String saveDuelOrderMethod(){
		try {
			hashMap = uOrderService.saveDuelOrder(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 约战挑战订单
	 * @return
	 * String
	 * xiao
	 * 2016年4月2日
	 */
	public String saveChallengeOrderMethod(){
		try {
			hashMap = uOrderService.saveChallengeOrder(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 追加订单服务
	 * @return
	 * String
	 * xiao
	 * 2016年3月7日
	 */
	public String saveAppendOrderMethod(){
		try {
			hashMap = uOrderService.saveAppendOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 订单补款
	 * @return
	 * String
	 * xiao
	 * 2016年4月28日
	 */
	public String saveReplenishmentOrderMethod(){
		try {
			hashMap = uOrderService.saveReplenishmentOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 取消订单
	 * @param map
	 * @return
	 * String
	 * xiao
	 * 2016年3月7日
	 */
	public String cancelOrderMethod(){
		try {
			hashMap = uOrderService.cancelOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	/**
	 * 
	 * TODO 侧滑删除
	 * @return
	 * String
	 * xiao
	 * 2016年3月7日
	 */
	public String displayOrderMethod(){
		try {
			hashMap = uOrderService.displayOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 批量删除
	 * @return
	 * String
	 * xiao
	 * 2016年3月7日
	 */
	public String displayAllOrderMethod(){
		try {
			hashMap = uOrderService.displayAllOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 支付成功返回信息
	 * @return
	 * String
	 * xiao
	 * 2016年4月1日
	 */
	public String PaySuccessOrderMethod() {
		try {
			hashMap = uOrderService.PaySuccessOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	/**
	 * 
	 * TODO 第三方球场生成订单
	 * @return
	 * xiaoying 2016年6月24日
	 */
	public String savePureMallOrderMethod() {
		try {
			hashMap = uOrderService.savePureMallOrder(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	@Override
	public UOrder getModel()
	{
		if(null == uOrder)
			uOrder = new UOrder();
		return uOrder;
	}


	public UOrder getuOrder() {
		return uOrder;
	}

	public void setuOrder(UOrder uOrder) {
		this.uOrder = uOrder;
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
