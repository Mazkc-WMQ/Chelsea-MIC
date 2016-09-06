package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import upbox.model.UUpay;
import upbox.service.UPayService;

/**
 * 支付请求action
 * @author yuancao
 *
 */
@Controller("upayAction")
@Scope("prototype")
public class PayAction extends OperAction implements ModelDriven<UUpay>{
	
	private static final long serialVersionUID = -5285078738424922573L;
	private HashMap<String,Object> hashMap;
	private UUpay upay;
	@Resource
	private UPayService upayService;
	
	
	
	/**
	 * 微信 或 支付宝 支付接口
	 * @return
	 * @throws Exception 
	 */
	public String allPayMethod() throws Exception{
		try
		{
			hashMap=upayService.returnPaySign(super.getParams());
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 微信JS支付接口
	 * @return
	 */
	/*public String jsPayMethod(){
		try
		{
			request.setAttribute("sign", upayService.returnJsPaySign(super.getParams(), upay));
			return "jsPay";
		} catch (Exception e)
		{
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}*/
	
	/**
	 * 微信支付异步回调地址 JS
	 * @return
	 * @throws Exception 
	 */
	/*public String payJsCallBackMethod() throws Exception{
		Map<String, String> requestMap = MessageUtils.parseXml_Stream(request);
		try
		{
			upayService.jsApiCallBack(requestMap,super.getResponse());
			return null;
		} catch (Exception e)
		{
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}*/
	
	@Override
	public UUpay getModel()
	{
		if(null == upay){
			upay = new UUpay();
		}
		return upay;
	}

	public UUpay getUpay()
	{
		return upay;
	}

	public void setUpay(UUpay upay)
	{
		this.upay = upay;
	}
}
