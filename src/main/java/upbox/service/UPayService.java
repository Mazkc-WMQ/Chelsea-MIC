package upbox.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 支付模块接口
 * @author mazkc
 *
 */
public interface UPayService {
	/**
	 * 微信支付APP模块接口
	 * @param map
	 * 		body 产品内容
	 * 		fee_type 货币类型
	 * 		total_fee 交易金额
	 * 		spbill_create_ip 客户端IP
	 * 		trade_type API类型
	 * 		pay_success_url 支付成功后跳转的URL链接
	 *	    pay_fail_url 支付失败后跳转的URL链接
	 *		out_trade_no 订单号
	 *		topenid 公众号openid（微信用）
	 *		openid 个人openid（微信用）
	 *		payType 支付方式【如：wx（微信）ali（支付宝）】
	 * @return
	 */
	public HashMap<String,Object> returnPaySign(HashMap<String,String> map)throws Exception;

	/**
	 * 微信支付回调 APP
	 * @param map
	 * 		out_trade_no 订单号
	 * 		fee_type 货币类型
	 * 		total_fee 交易金额
	 * 		return_code 交易状态
	 * @return 
	 * @throws Exception
	 */
	public String appWxApiCallBack(Map<String, String> map)throws Exception;
	

	/**
	 * 支付宝支付回调 APP
	 * @param map
	 * 		out_trade_no 订单号
	 * 		fee_type 货币类型
	 * 		total_fee 交易金额
	 * 		return_code 交易状态
	 * @return 
	 * @throws Exception
	 */
	public String appAliPayCallBack(HashMap<String, String> map) throws Exception; 
	
	
	/**
	 * applePay支付回调 APP
	 * @param map
	 * @param ret_code 回调状态
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> appApplePayPayCallBack(JSONObject json) throws Exception; 
	
	/**
	 * 微信支付JS模块接口
	 * body	产品内容
	 * fee_type	货币类型
	 * total_fee 交易金额
	 * spbill_create_ip	客户端IP
	 * trade_type API类型
	 * notify_url 回调地址
	 * openid 支付人openid
	 * @param map
	 * @param upay
	 * @return
	 */
	//public HashMap<String,Object> returnJsPaySign(HashMap<String,String> map,UUpay upay)throws Exception;
	
	/**
	 * 微信支付回调 JS
	 * @param map
	 * @return
	 * @throws Exception
	 */
	//public void jsApiCallBack(Map<String, String> map,HttpServletResponse response)throws Exception;
}
