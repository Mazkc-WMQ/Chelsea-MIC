package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import com.alipay.util.AlipayNotify;
import com.org.bean.HttpRespons;
import com.org.dao.BaseDAO;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;

import upbox.model.UOrder;
import upbox.model.UUpay;
import upbox.model.UUser;
import upbox.outModel.ReturnPay;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.UOrderService;
import upbox.service.UPayService;
import upbox.service.UUserService;

/**
 * 支付接口实现类
 * @author yuancao
 *
 */
@Service("upayService")
public class UPayServiceImpl implements UPayService {
	@Resource
	private BaseDAO baseDao;
	@Resource
	private UOrderService uOrderService;
	@Resource
	private UUserService userService;
	
	@Override
	public HashMap<String, Object> returnPaySign(HashMap<String, String> map) throws Exception
	{
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		if(map.get("body")!=null&&!"".equals(map.get("body"))&&map.get("fee_type")!=null&&!"".equals(map.get("fee_type"))&&
				map.get("total_fee")!=null&&!"".equals(map.get("total_fee"))&&map.get("spbill_create_ip")!=null&&!"".equals(map.get("spbill_create_ip"))&&
				map.get("trade_type")!=null&&!"".equals(map.get("trade_type"))&&map.get("out_trade_no")!=null&&!"".equals(map.get("out_trade_no"))&&
				map.get("payType")!=null&&!"".equals(map.get("payType"))){
			HttpUtil hp = new HttpUtil();
			String sendUrl="";
			String callBackurl="";
			String payType=map.get("payType");
			Double price=Double.parseDouble(map.get("total_fee"));
			hashMap.put("ordernum", map.get("out_trade_no"));
			hashMap.put("price", price);
			UOrder uo=baseDao.get("from UOrder where ordernum=:ordernum and price=:price ", hashMap);
			
			if(uo!=null){
				if(WebPublicMehod.compare_date(PublicMethod.getStringToDate(PublicMethod.getDateToString(
						uo.getOutdate(), "yyyy-MM-dd") + " " + PublicMethod.getDateToString(
								uo.getOuttime(), "HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"),
						new Date()) == -1){
					map.put("orderId", uo.getOrderId());
					uo.setOrderstatus("2");
					baseDao.save(uo);
					uOrderService.orderPayExpired(map);//更新订单过期缓存
					return WebPublicMehod.returnRet("success", "此订单已过期！");
				}
				
				if(uo.getOrderstatus().equals("4")){
					int priceStr=(new Double(price*1000000/10000)).intValue(); 
					map.put("topenid", "gh_d1a4d3595e62");
					map.put("total_fee",String.valueOf(priceStr));//第三方接口需要分为单位
					if(payType.equals("ali")){//支付宝单独处理
						sendUrl="aliPay_getAliPaySign.do";
						callBackurl="interceptor!payAliCallBackMethod.do";
						map.put("OrdernumInput", map.get("out_trade_no"));
						map.put("TotalpriceInput", Double.parseDouble(map.get("total_fee"))/100+"");//支付宝元为单位
						map.put("bodyInput", map.get("body"));
						map.put("callBack", Public_Cache.PAY_CALLBACK+callBackurl);
					}else if(payType.equals("wx")){//微信单独处理
						sendUrl="pay_payApp.do";
						callBackurl="interceptor!payWxCallBackMethod.do";
					}else if(payType.equals("applePay")){//applePay单独处理
						callBackurl="interceptor!payAppleCallBackMethod.do";
					}
					
					if(payType.equals("ali")||payType.equals("wx")){
						map.put("notify_url",Public_Cache.PAY_CALLBACK+callBackurl);
						savePay(map,userService.getUserinfoByToken(map));
						map.put("subjectInput",uo.getOrdernum() + "&&" + uo.getUUser().getNickname() + "&&" + uo.getUUser().getRealname() + "&&" + uo.getUUser().getPhone());
						HttpRespons hps = hp.sendPost(Public_Cache.JAVAWX_CODE+sendUrl, map);
						map.clear();
						return returnPayMsg(hps, payType, hashMap);
					}else if(payType.equals("applePay")){
						map.put("notify_url",Public_Cache.PAY_CALLBACK+callBackurl);
						savePay(map,userService.getUserinfoByToken(map));
						return returnPayMsg(null, payType, hashMap);
					}else {
						return WebPublicMehod.returnRet("error", "payType支付类型错误");
					}
				}else if(uo.getOrderstatus().equals("1")){
					return WebPublicMehod.returnRet("error", "此订单已支付");
				}else{
					return WebPublicMehod.returnRet("error", "此订单状态不可支付");
				}
			}else{
				return WebPublicMehod.returnRet("error", "此订单不存在或价格不匹配");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}
	
	/**
	 * 保存pay表记录
	 * @param map
	 * @throws Exception
	 */
	private void savePay(HashMap<String, String> map,UUser user) throws Exception{
		UUpay upay=new UUpay();
		setUUpay(map, upay);
		//判断是否记录过订单支付信息
		UUpay up = (UUpay) baseDao.get(map, "from UUpay where outTradeNo=:out_trade_no");
		if(null == up){
			up = new UUpay();
			BeanUtils.copyProperties(up, upay);
			up.setId(WebPublicMehod.getUUID());
		}
		up.setCreatetime(new Date());
		up.setNotifyUrl(map.get("notify_url"));
		up.setUserId(user.getUserId());
		baseDao.saveOrUpdate(up);
	}
	
	/**
	 * 根据wx还是支付宝返回对应json信息
	 * @param hps
	 * @param payType
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> returnPayMsg(HttpRespons hps,String payType,HashMap<String, Object> hashMap) throws Exception{
		List<Map<String,Object>> li = new ArrayList<Map<String,Object>>();
		Map<String,Object> _mp = new HashMap<String, Object>();
		String json = "";
		if(payType.equals("ali")){
			if(JSONArray.fromObject(hps.getContent())!=null&&JSONArray.fromObject(hps.getContent()).size()>0){
				_mp=(Map<String, Object>)JSONArray.fromObject(hps.getContent()).get(0);
				hashMap.clear();
				String a=(String) _mp.get("result");
				a=a.replace("UPBOX�?战联盟", "UPBOX激战联盟");
				hashMap.put("returnAli", a);
			}else{
				hashMap.put("error", "支付宝中间组件接口异常!");
			}
		}else if(payType.equals("wx")){
			li = PublicMethod.parseJSON2List(hps.getContent());
			if(li!=null&&li.size()>0){
				_mp = li.get(0);
				json=_mp.get("result").toString();
				if(!json.equals("-2")&&!json.equals("-1")){
					ReturnPay rp=(ReturnPay) PublicMethod.jsonTOobject(json, 1, new ReturnPay());
					hashMap.clear();
					hashMap.put("returnWx", rp);
				}else{
					hashMap.put("error", "微信支付中间组件接口异常 下标："+json);
				}
			}else{
				hashMap.put("error", "微信支付中间组件接口异常!");
			}
		}else if(payType.equals("applePay")){
			hashMap.put("returnApplePay", "支付日志保存成功!");
		}
		return hashMap;
	}
	/**
	 * json放入对象
	 * @param map
	 * @param upay
	 */
	private void setUUpay(HashMap<String, String> map,UUpay upay){
		upay.setFeeType(map.get("fee_type"));
		upay.setBody(map.get("body"));
		upay.setOutTradeNo(map.get("out_trade_no"));
		upay.setSpbillCreateIp(map.get("spbill_create_ip"));
		upay.setTradeType(map.get("trade_type"));
		upay.setPaySuccessUrl(map.get("pay_success_url"));
		upay.setPayFailUrl(map.get("pay_fail_url"));
		upay.setTopenid(map.get("topenid"));
		upay.setOpenid(map.get("openid"));
		if(map.get("total_fee")!=null){
			upay.setTotalFee(Double.valueOf(map.get("total_fee")));
		}
	}
	
	@Override
	public String appWxApiCallBack(Map<String, String> map) throws Exception{
		if("SUCCESS".equals(map.get("return_code"))){ //如果交易成功
			return uOrderService.savePayOrderStatus(map.get("out_trade_no"),map.get("attach"),"2");
		}else{
			return "微信支付处理失败";
		}
	}


	@Override
	public String appAliPayCallBack(HashMap<String, String> map) throws Exception {
		//通知状态
		String trade_status =map.get("trade_status");
		String out_trade_no = map.get("out_trade_no");
		String attach=map.get("attach");
		if (AlipayNotify.verify(map)) {
			if (null!=trade_status&&"TRADE_SUCCESS".equals(trade_status)) {
				return uOrderService.savePayOrderStatus(out_trade_no,attach,"1");
			}else{
				return "支付宝支付处理失败";
			}
		}else {
			return "支付宝支付处理失败";
		}
	}

	@Override
	public HashMap<String,String> appApplePayPayCallBack(JSONObject json) throws Exception {
		HashMap<String,String> reMap=new HashMap<String,String>();
		String strJson="";
		String result_pay=json.getString("result_pay");//支付回调返回状态值
		String no_order = json.getString("no_order");
		if(result_pay!=null&&result_pay.equals("SUCCESS")){
			if(uOrderService.savePayOrderStatus(no_order,"","5").equals("success")){
				reMap.put("ret_code", "0000");
				reMap.put("ret_msg", "交易成功");
				return reMap;
			}else{
				reMap.put("error", "applePay支付处理失败");
				return reMap;
			}
		}else{
			reMap.put("error", "applePay支付处理失败");
			return reMap;
		}
	}

	

	
}

