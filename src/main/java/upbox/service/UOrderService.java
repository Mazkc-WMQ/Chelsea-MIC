package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UBrCourtsession;
import upbox.model.UMall;
import upbox.model.UOrder;
import upbox.model.UOrderCourt;

/**
 * 前端用户订单接口
 * @author xiao
 *
 */
public interface UOrderService {
	/**
	 * 
	 * 
	   TODO - 查询订单列表
	   @param map
	   		loginUserId:前端用户ID(登录用户Id)
	   		page  :每页多少条
	   		orderTypePage:订单类型  1=球场、2=服务
	   @return
	   @throws Exception
	   2015年12月24日
	   xiao
	 */
	public HashMap<String, Object> findOrderlist(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询订单详情,附带修改过期订单，过期约战，过期挑战
	   @param map
	   		orderId    订单Id
	   @return
	   @throws Exception
	   2015年12月24日
	   xiao
	 */
	public HashMap<String, Object> findOrderinfo(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * TODO 查询订单详情-约战挑战概况
	 * @param map orderId    订单Id(发起方订单Id)
	 * @return
	 * @throws Exception
	 * List<HashMap<String,Object>>
	 * xiao
	 * 2016年3月30日
	 */
	public HashMap<String, Object> findOrderinfoDuelAndDek(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 过期订单
	   @param map 
	   @return
	   @throws Exception
	   2015年12月25日
	   xiao
	 */
	public HashMap<String, Object> uOrderExpired(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * TODO 订单即将过期
	 * @param map
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月7日
	 */
	public HashMap<String, Object> uOrderAboutExpired(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * TODO 订单流程判断
	 * @param map
	 * @param sessionList
	 * @param listMall
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月31日
	 */
	public HashMap<String, Object> ifLogic(HashMap<String, String> map, List<UBrCourtsession> sessionList)throws Exception;
	
	/**
	 * 
	 * TODO 约战订单
	 * @param map  listMall-商品集合、listSesseion-场次集合、orderType-订单类型（2=挑战、3=约战）、paymentRatio-支付比例
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月31日
	 */
	public HashMap<String, Object> saveDuelOrder(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * TODO 挑战订单
	 * @param map  listMall-商品集合、listSesseion-场次集合、orderType-订单类型（2=挑战、3=约战）、paymentRatio-支付比例
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月2日
	 */
	public HashMap<String, Object> saveChallengeOrder(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * TODO 生成订单
	 * @param map subcourtId-下属球场Id、courtId-球场Id、phone-预订人手机号码、realName-预订人外号、orderTypePage-订单类型（页面上的，即球场、服务）
	 * 			 orderType-预定类型、resource-订单生成来源、price-总价格、paymentRatio-约战AA支付百分比
	 * @param sessionList 预定场次集合
	 * @param listMall 预订商品
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月2日
	 */
	public HashMap<String, Object> saveChallengeOrder(HashMap<String, String> map, List<UBrCourtsession> sessionList,List<UMall> listMall)throws Exception;
	
	/**
	 * 
	 * TODO 生成订单
	 * @param map subcourtId-下属球场Id、courtId-球场Id、phone-预订人手机号码、realName-预订人外号、orderTypePage-订单类型（页面上的，即球场、服务）
	 * 			 orderType-预定类型、resource-订单生成来源、price-总价格、paymentRatio-约战AA支付百分比
	 * @param sessionList 预定场次集合
	 * @param listMall 预订商品
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月24日
	 */
	public HashMap<String, Object> saveOrder(HashMap<String, String> map, List<UBrCourtsession> sessionList,List<UMall> listMall)throws Exception;
	
	/**
	 * 
	 * TODO 追加订单服务
	 * @param map orderId-订单Id、orderType-预定类型、resource-订单生成来源、price-总价格、orderTypePage-订单类型（页面上的，即球场、服务）
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月9日
	 */
	public HashMap<String, Object> saveAppendOrder(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 追加订单服务
	 * @param map  orderId-订单Id、orderType-预定类型、resource-订单生成来源、price-总价格、orderTypePage-订单类型（页面上的，即球场、服务）
	 * @param listMall 预定商品集合
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年2月29日
	 */
	public HashMap<String, Object> saveAddOrder(HashMap<String, String> map,List<UMall> listMall) throws Exception;
	
	/**
	 * 
	 * TODO  约战、挑战  响应方生成订单
	 * @param map orderId-订单Id、loginUserId-登录用户、orderType-订单类型
	 * @param listMall  预定商品集合   商品同类Id、商品购买数量、商品价格类型
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月18日
	 */
	public HashMap<String, Object> saveAddDuelAndChallOrder(HashMap<String, String> map,List<UMall> listMall) throws Exception;
	
	/**
	 * 
	 * TODO 取消订单
	 * @param map  orderId-订单Id、orderStatus=5(1=已支付、2=已过期、3=待定、4=待支付、5=已取消、6=退款中、7=已退款、8=退款失败)
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月1日
	 */
	public HashMap<String, Object> cancelOrder(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 侧滑删除
	 * @param map  orderId-订单Id、orderTypePage-订单类型（页面上的，即球场、服务）、displayStatus=-1(1-显示  -1-不显示)
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月2日
	 */
	public HashMap<String, Object> displayOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 约战订单退款
	 * @param map
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月14日
	 */
	public HashMap<String,Object> refundOrderInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 如支付成功 回调方法置订单状态为已支付
	 * @param ordernum 订单编号
	 * @param price 订单价格
	 * @param payType 支付类型
	 * @return  根据返回string提示错误或成功
	 * @throws Exception
	 */
	public String savePayOrderStatus(String ordernum,String attach,String payType)throws Exception;

	/**
	 * 
	 * TODO 修改订单关联状态-约战挑战用
	 * @param map orderId-订单Id,orderType-订单类型
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年4月1日
	 */
	public void editRelationTypeOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 支付成功后返回参数
	 * @param ordernum
	 * @return
	 * @throws Exception
	 * String
	 * xiao
	 * 2016年4月1日
	 */
	public HashMap<String,Object> PaySuccessOrder(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * TODO 清空订单更改的缓存
	 * @param map
	 * @param order  orderType-订单类型
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年4月14日
	 */
	public void removeRedis(HashMap<String, String> map, UOrder order) throws Exception;
	
	/**
	 * 
	 * TODO 生成订单的缓存加入到查询列中
	 * @param uo 订单 orderType-订单类型
	 * @param map
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年4月14日
	 */
	public void getOrderHRedisKey(UOrder uo, HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 响应方计算价格的详细信息
	 * @param map
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月19日
	 */
	public HashMap<String, Object> findOrderinfoResp(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 擂主取消订单
	 * @param map orderId-订单Id
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月23日
	 */
	public HashMap<String, Object> cancelChallengeOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 更改待支付状态
	 * @param map orderId-订单Id
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年4月25日
	 */
	public void editStatusOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 订单补款-其他类型订单转成球场类型订单
	 * @param map orderId-订单Id、loginUserId-登录用户、orderType-订单类型
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年4月28日
	 */
	public HashMap<String, Object> saveReplenishmentOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 过期订单-支付
	 * @param map orderId-订单Id
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年5月4日
	 */
	public HashMap<String, Object> orderPayExpired(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * TODO 响应方生成订单
	 * @param map
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年5月5日
	 */
	public HashMap<String, Object> saveAppendDuelAndChallOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 补款约束
	 * @return
	 * @throws Exception
	 * List<UOrder>
	 * xiao
	 * 2016年5月10日
	 */
	public List<UOrder> judgeReplenishmentOrder(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 响应订单5分钟时效判断
	 * 
	 * @param map orderId 订单Id
	 * @str 补款 this.judgeOrderResp(map,"不能补款！");
	 * @return
	 * @throws Exception
	 *             HashMap<String,Object> xiao 2016年4月5日
	 */
	public HashMap<String, Object> judgeOrderResp(HashMap<String, String> map, String...str) throws Exception;
	/**
	 * 
	 * TODO 订单批量删除[2.0.3]
	 * @param map orderIds 订单号（Id1，Id2）
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年6月6日
	 */
	public HashMap<String, Object> displayAllOrder(HashMap<String, String> map) throws Exception;
	/**
	 * TODO 第三方球场生成订单[2.0.3]
	 * @param map 
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月18日
	 */
	public HashMap<String, Object> savePureMallOrder(HashMap<String, String> map) throws Exception;
	/**
	 * TODO  纯商品订单生成[2.0.3]
	 * @param map
	 * @param listOrderCourt 预定球场信息
	 * @param listMall 预定商品集合
	 * @return
	 * xiaoying 2016年6月21日
	 */
	public HashMap<String, Object> savePureMallOrder(HashMap<String, String> map, List<UOrderCourt> listOrderCourt,
			List<UMall> listMall) throws Exception;
}
