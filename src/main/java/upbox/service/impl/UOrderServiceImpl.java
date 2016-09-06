package upbox.service.impl;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UBrCourt;
import upbox.model.UBrCourtsession;
import upbox.model.UEquipment;
import upbox.model.UMall;
import upbox.model.UMallCourt;
import upbox.model.UOrder;
import upbox.model.UOrderCourt;
import upbox.model.UOrderMall;
import upbox.model.UOrderRefund;
import upbox.model.UUser;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.UChallengeService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UMallService;
import upbox.service.UOrderService;
import upbox.service.UUserService;
import upbox.util.YHDCollectionUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 前端端用户订单接口实现类
 *
 */
@Service("uorderService")
public class UOrderServiceImpl implements UOrderService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private RedisOperDAOImpl redisDao;
	@Resource
	private PublicService publicService;
	@Resource
	private UUserService userService;
	@Resource
	private UCourtService uCourtService;
	@Resource
	private UMallService uMallService;
	@Resource
	private UDuelService uduelServiceImpl;
	@Resource
	private UChallengeService uchallengeService;
	@Resource
	private MessageService messageService;
	@Resource
	private PublicPushService publicPush;
	DecimalFormat df = new DecimalFormat("######0.0");
	HashMap<String, Object> hashMap = new HashMap<>();

	/**
	 * 
	 * TODO 初始的订单列表语句
	 * 
	 * @param map
	 *            orderTypePage-订单页面类型、orderType-订单类型
	 * @return String xiao 2016年3月23日
	 */
	private String getOrderInitSql(HashMap<String, String> map) {
		String orderType = this.getOrderTypeInfo(map);
		String sql = "select o.order_id as orderId,o.ordernum,o.orderstatus,o.title,"
				+ "ifnull(bc.name,(select ubc.name from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as name,o.relation_type as relationType,"
				+ "ifnull(bc.subcourt_type,(select ubc.subcourt_type from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as subcourtType,"
				+ "date_format(ifnull(oc.stdate,(select uoc.stdate from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)),'%Y-%m-%d') stdate,"
				+ "ifnull(oc.week,(select uoc.week from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)) as week,"
				+ "min(ifnull(oc.sttime,(select min(uoc.sttime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as sttime ,"
				+ "max(ifnull(oc.endtime,(select max(uoc.endtime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as endtime,"
				+ "o.order_type as orderType,ft.name as duelFName,xt.name as duelXName,"
				+ "ft1.name as challFName,xt1.name as challXName,om.name as mallName,om.count from u_order o "
				+ "left join u_order_court oc on o.order_id = oc.order_id "
				+ "left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ "left join u_duel d on d.order_id=o.order_id " + "left join u_duel_resp dr on dr.duel_id=d.duel_id "
				+ "left join u_team ft on ft.team_id=d.f_team_id " + "left join u_team xt on xt.team_id=dr.team_id "
				+ "left join u_challenge c on c.order_id =o.order_id "
				+ "left join u_team ft1 on ft1.team_id=c.f_team_id "
				+ "left join u_team xt1 on xt1.team_id=c.x_team_id "
				+ "left join u_order_mall om on om.order_id=o.order_id " + "where o.order_type in(" + orderType
				+ ") and o.user_id=:loginUserId and o.display_status='1' "
				+ "group by o.order_id order by o.createdate desc";
		return sql;
	}

	/**
	 * 
	 * TODO 初始的订单列表语句-缓存使用
	 * 
	 * @param map
	 *            orderTypePage-订单页面类型、orderType-订单类型
	 * @return String xiao 2016年4月7日
	 */
	private String getOrderSql(HashMap<String, String> map) {
		String orderType = this.getOrderTypeInfo(map);
		String sql = "select o.order_id as orderId,o.ordernum,o.orderstatus,o.title,"
				+ "ifnull(bc.name,(select ubc.name from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as name,o.relation_type as relationType,"
				+ "ifnull(bc.subcourt_type,(select ubc.subcourt_type from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as subcourtType,"
				+ "date_format(ifnull(oc.stdate,(select uoc.stdate from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)),'%Y-%m-%d') stdate,"
				+ "ifnull(oc.week,(select uoc.week from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)) as week,"
				+ "min(ifnull(oc.sttime,(select min(uoc.sttime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as sttime ,"
				+ "max(ifnull(oc.endtime,(select max(uoc.endtime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as endtime,"
				+ "o.order_type as orderType,ft.name as duelFName,xt.name as duelXName,"
				+ "ft1.name as challFName,xt1.name as challXName,om.name as mallName,om.count from u_order o "
				+ "left join u_order_court oc on o.order_id = oc.order_id "
				+ "left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ "left join u_duel d on d.order_id=o.order_id " + "left join u_duel_resp dr on dr.duel_id=d.duel_id "
				+ "left join u_team ft on ft.team_id=d.f_team_id " + "left join u_team xt on xt.team_id=dr.team_id "
				+ "left join u_challenge c on c.order_id =o.order_id "
				+ "left join u_team ft1 on ft1.team_id=c.f_team_id "
				+ "left join u_team xt1 on xt1.team_id=c.x_team_id "
				+ "left join u_order_mall om on om.order_id=o.order_id " + "where o.order_type in(" + orderType
				+ ") and o.user_id=" + map.get("loginUserId") + " and o.display_status='1' "
				+ "group by o.order_id order by o.createdate desc";
		return sql;
	}

	@Override
	public HashMap<String, Object> findOrderlist(HashMap<String, String> map) throws Exception {
		PageLimit page = new PageLimit(0 >= Integer.parseInt(map.get("page")) ? 0 : Integer.parseInt(map.get("page")),
				0);
		String sql = this.getOrderInitSql(map);// 获取查询语句
		List<HashMap<String, Object>> uOrderlist = baseDAO.findSQLMapHRedis(map, sql,
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ListPageSQL"), "orderId", page.getLimit(),
				page.getOffset());
		// 订单状态和球场规格码表值设置
		if(uOrderlist!=null&&uOrderlist.size()>0){
			uOrderlist.removeAll(YHDCollectionUtils.nullCollection());//去空
			for (HashMap<String, Object> hashMap : uOrderlist) {
                String week="";
                if(hashMap.get("week")!=null){
                    week=hashMap.get("week").toString();
                }
                hashMap.put("week", WebPublicMehod.checkWeekString(week));
				hashMap.put("orderstatusName", Public_Cache.HASH_PARAMS("orderstatus").get(hashMap.get("orderstatus")));//支付状态码表值
				hashMap.put("subcourtTypeName",
						Public_Cache.HASH_PARAMS("subcourt_type").get(hashMap.get("subcourtType")));//球场类型码表值（几人制）
			}
		}
		return WebPublicMehod.returnRet("orderlist", uOrderlist);

	}

	@Override
	public HashMap<String, Object> findOrderinfo(HashMap<String, String> map) throws Exception {
		UOrder uOrder = null;
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			//如果是待支付订单，并且超过30分钟，那么修改状态
			UOrder order =baseDAO.get(map,"from UOrder where orderId=:orderId and (orderstatus='4' or orderstatus='3') ");
			if(order!=null){
				boolean isExpired = isExpired(order.getCreatedate(),order.getCreatetime(), Public_Cache.ORDER_TIME);
				if (isExpired == true) {
					order.setOrderstatus("2");
					baseDAO.update(order);
					uduelServiceImpl.updateDuelByTimeOut();// 约战过期操作
					uchallengeService.updateChallengeByTimeOut();// 挑战过期操作
					this.orderExpired(map, order);
				}
			}
			//订单缓存
			uOrder = baseDAO.getHRedis(UOrder.class, map.get("orderId"),
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));
			if (uOrder != null) {
				//数据处理
				baseDAO.getSessionFactory().getCurrentSession().clear();
				if (uOrder.getUUser() != null) {
					uOrder.setNickname(uOrder.getUUser().getNickname());
					uOrder.setRealname(uOrder.getUUser().getRealname());
				}
				uOrder.setUUser(null);// 用户没有使用 设置空
				this.getCourtAndMall(map, uOrder);// 商品和球场数据处理
				this.displayData(map, uOrder);// 订单数据处理
				
			}
		} else {
			return WebPublicMehod.returnRet("error", "订单号不能空！");
		}
		return WebPublicMehod.returnRet("success", uOrder);
	}

	@Override
	public HashMap<String, Object> findOrderinfoResp(HashMap<String, String> map) throws Exception {
		UOrder uOrder = null;
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			uOrder = baseDAO.getHRedis(UOrder.class, map.get("orderId"),
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));
			if (uOrder != null) {
				baseDAO.getSessionFactory().getCurrentSession().clear();
				if (uOrder.getUUser() != null) {
					uOrder.setNickname(uOrder.getUUser().getNickname());
					uOrder.setRealname(uOrder.getUUser().getRealname());
				}
				uOrder.setUUser(null);// 用户没有使用 设置空
				this.getCourtAndMallResp(map, uOrder);// 商品和球场数据处理
				this.displayData(map, uOrder);// 订单数据处理
			}
		} else {
			return WebPublicMehod.returnRet("error", "订单号不能空！");
		}
		return WebPublicMehod.returnRet("success", uOrder);
	}

	@Override
	public HashMap<String, Object> findOrderinfoDuelAndDek(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap1 = new HashMap<>();
		hashMap = this.findOrderinfo(map);
		UOrder uOrder = (UOrder) hashMap.get("success");
		List<HashMap<String, Object>> list = new ArrayList<>();
		HashMap<String, Object> mapList = null;
		if (uOrder != null) {
			// 球场
			if (uOrder.getUOrderCourt() != null && uOrder.getUOrderCourt().size() > 0) {
				List<HashMap<String, Object>> listOrderCourt = uOrder.getUOrderCourt();
				int count = 0;
				String dateTime = "";// 日期和时间
				for (HashMap<String, Object> hashMap : listOrderCourt) {
					//第一条数据
					if (count <= 0) {
						// 名称
						mapList = new HashMap<>();
						mapList.put("imgurl", "");
						mapList.put("content", hashMap.get("name"));
						mapList.put("name", "name");
						list.add(mapList);
						// 地址
						mapList = new HashMap<>();
						mapList.put("imgurl", "");
						mapList.put("content", hashMap.get("address"));
						mapList.put("name", "address");
						list.add(mapList);
						// 日期时间拼接
						if (hashMap.get("stdate") != null && !"".equals(hashMap.get("stdate"))) {
							dateTime += hashMap.get("stdate");
						}
						if (hashMap.get("sttime") != null && !"".equals(hashMap.get("sttime"))) {
							dateTime += " " + hashMap.get("sttime").toString();
						}
					}
					//最后一条数据（如果只有一条，该也会进入）
					if (count >= listOrderCourt.size() - 1) {
						if (hashMap.get("endtime") != null && !"".equals(hashMap.get("endtime"))) {
							dateTime += "-" + hashMap.get("endtime").toString();
						}
					}
					count++;
				}
				// 时间日期
				mapList = new HashMap<>();
				mapList.put("imgurl", "");
				mapList.put("content", dateTime);
				mapList.put("name", "dateTime");
				list.add(mapList);
			}
			// 商品数据拼接  查询该订单预定了那些商品
			String sql = "select om.product_subtype productSubtype,om.product_detailtype productDetailtype,count(*) num "
					+ " from u_order_mall om  where om.order_id=:orderId "
					+ "group by  om.product_subtype,om.product_detailtype";
			List<HashMap<String, Object>> hashMapMall = baseDAO.findSQLMap(map, sql);
			String type = "";
			if (uOrder.getUOrderMall() != null && uOrder.getUOrderMall().size() > 0) {
				// 获取数据
				List<HashMap<String, Object>> listOrderMall = uOrder.getUOrderMall();//订单下的商品集合
				HashMap<String, Object> hashMapOrder = new HashMap<>();
				for (HashMap<String, Object> hashMap : listOrderMall) {
					type = "productDetailtype" + hashMap.get("productSubtype") + hashMap.get("productDetailtype");
					if (hashMapOrder.get(type) != null) {// 商品类型和商品子类型相同的
						hashMapOrder.put(type,
								hashMapOrder.get(type) + "+" + hashMap.get("count").toString() + hashMap.get("name"));
					} else {
						hashMapOrder.put(type, hashMap.get("productDetailtypeName") + ":"
								+ hashMap.get("count").toString() + hashMap.get("name"));
					}
				}
				// 去掉重复的类型数据
				for (HashMap<String, Object> hashMap : hashMapMall) {
					type = "productDetailtype" + hashMap.get("productSubtype") + hashMap.get("productDetailtype");
					mapList = new HashMap<>();
					mapList.put("imgurl", this.getImgurlType(hashMap.get("productSubtype").toString()));
					mapList.put("content", hashMapOrder.get(type));
					mapList.put("name", "mallInfo");
					list.add(mapList);
				}
			}
			//2.0.3使用
//			if(map.get("appCode")!=null&&!"".equals(map.get("appCode"))){
//				// 总价
//				if (uOrder.getAllprice() != null) {
//					mapList = new HashMap<>();
//					mapList.put("imgurl", "");
//					mapList.put("content", "总价"+uOrder.getAllprice()+"元");
//					mapList.put("name", "allprice");
//					list.add(mapList);
//				}
//				// 支付方式- 挑战或约战
//				if (uOrder.getDuelPayTypeName() != null && !"".equals(uOrder.getDuelPayTypeName())) {
//					mapList = new HashMap<>();
//					mapList.put("imgurl", "");
//					mapList.put("content", uOrder.getDuelPayTypeName()+" 发起方已支付"+uOrder.getPaymentRatio()+"%");
//					mapList.put("name", "duelPayType");
//					list.add(mapList);
//				}
//			}

			// 应支付- 挑战或约战
			if (uOrder.getDuelPayType() != null && !"".equals(uOrder.getDuelPayType())) {
				mapList = new HashMap<>();
				mapList.put("imgurl", "");
				mapList.put("content", "");
				//2.0.3使用
//				if("1".equals(uOrder.getDuelPayType())){
//					mapList.put("content", "发起方请客");
//				}
//				if("2".equals(uOrder.getDuelPayType())||"3".equals(uOrder.getDuelPayType())){
//					mapList.put("content", "应战需支付");
//				}
				mapList.put("content", uOrder.getDuelPayTypeName());
				mapList.put("name", "duelPayTypeName");
				hashMap1.put("duelPayTypeNameList", mapList);
			}
			
			HashMap<String, Object> respHashMap=this.findOrderinfoResp(map);
			// 应支付－价格
			UOrder order=(UOrder) respHashMap.get("success");
			if (order != null) {
				mapList = new HashMap<>();
				mapList.put("imgurl", "");
				mapList.put("content", "");
				if(order.getPrice()!=null){
				mapList.put("content", df.format(order.getPrice()));
				}
				mapList.put("name", "price");
				hashMap1.put("priceList", mapList);
			}
			
		}
		list.removeAll(YHDCollectionUtils.nullCollection());
		hashMap1.put("list", list);// 球场和商品
		return hashMap1;
	}

	@Override
	public HashMap<String, Object> uOrderExpired(HashMap<String, String> map) throws Exception {
		// 查询所有未支付的记录
		List<UOrder> uOrderlist = baseDAO
				.find("from UOrder where (orderstatus='4' or orderstatus='3') and CONCAT(outdate,' ',outtime) < now() order by ordernum desc");
		baseDAO.executeHql(
				"update UOrder set orderstatus='2' where (orderstatus='4' or orderstatus='3') and CONCAT(outdate,' ',outtime) < now() ");
		uduelServiceImpl.updateDuelByTimeOut();// 约战过期操作
		uchallengeService.updateChallengeByTimeOut();// 挑战过期操作
		if (null != uOrderlist && uOrderlist.size() > 0) {
			for (UOrder uOrder : uOrderlist) {
				this.orderExpired(map, uOrder);
			}
		}
		return WebPublicMehod.returnRet("success", uOrderlist);
	}

	@Override
	public HashMap<String, Object> orderPayExpired(HashMap<String, String> map) throws Exception {
		// 查询所有未支付的记录
		UOrder uOrder = baseDAO.get(UOrder.class, map.get("orderId"));
		this.orderExpired(map,uOrder);
		return null;
		
	}
	

	@Override
	public HashMap<String, Object> uOrderAboutExpired(HashMap<String, String> map) throws Exception {
		// 查询所有未支付的记录
		List<UOrder> uOrderlist = baseDAO
				.find("from UOrder where orderstatus='4' or orderstatus='3' order by ordernum desc");
		if (null != uOrderlist && uOrderlist.size() > 0) {
			for (UOrder uOrder : uOrderlist) {
				// 判断是否超过25分钟
				boolean isExpired = isExpired(uOrder.getCreatedate(),uOrder.getCreatetime(), Public_Cache.ORDER_TIME_STATUS);
				if (isExpired == true) {
					map.put("mes_type", "orTimeQu");// 订单即将过期通知
					this.orderMessage(map, uOrder.getOrderId(), uOrder.getUUser().getUserId(), null);
					// 推送
					this.orderPush(map, uOrder, "5");
				}
			}
		}
		return WebPublicMehod.returnRet("success", uOrderlist);
	}
	@Override
	public HashMap<String, Object> ifLogic(HashMap<String, String> map, List<UBrCourtsession> sessionList)
			throws Exception {
		List<UBrCourtsession> listBrCourtsession = null;
		List<UBrCourtsession> listAllBrCourtsession = null;
		List<UBrCourtsession> listSession = new ArrayList<>();
		List<String> listError = new ArrayList<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		int count = 0;// 预定场次的场数
		int number = 0;// 记录最大的场数
		int ifCount = 0;// 预定场次最终场数
		int numberSeries = 0;// 控制场次数
		int ifCounts = 0;// 预定最终的连续场数
		int counts = 0;// 预定连续场数
		int numbers = 0;// 记录最大连续场数
		int numberSing = 0;// 控制场次连续
		hashMap.put("loginUserId", map.get("loginUserId"));
		hashMap.put("subcourtId", map.get("subcourtId"));

		UBrCourt brCount = baseDAO.get(UBrCourt.class, map.get("subcourtId"));
		// 多笔订单未处理
		List<UOrder> listOrder = baseDAO.find(map,
				"from UOrder where UUser.userId=:loginUserId and  (orderstatus='3' or orderstatus='4') and displayStatus='1' ");
		if (listOrder != null && listOrder.size() >= 3) {
			WebPublicMehod.returnRet("error", "你有多笔订单尚未完成支付请先前往个人中心处理");
		}
//		this.upgradePrompt(map);//订单类型2-挑战，3-约战，才能进入2.0.2版本后
		int ifDuel = 0;//约战冲突
		if (sessionList != null && sessionList.size() > 0) {
			// 预定订单起冲突
			for (UBrCourtsession uBrCourtsession : sessionList) {
				hashMap.put("stdate", uBrCourtsession.getStdate());
				hashMap.put("sttime", uBrCourtsession.getSttime());
				hashMap.put("endtime", uBrCourtsession.getEndtime());
				//约战类型才进入判断
				if (map.get("orderType") != null && "3".equals(map.get("orderType"))) {
//					if(PublicMethod.daysBetween(new Date(), uBrCourtsession.getStdate())<6){
//						return WebPublicMehod.returnRet("error", "约战只可使用一周以后的时间发起！");
//					}
					if(map.get("paymentRatio")!=null &&!"100".equals(map.get("paymentRatio"))&&!"100.0".equals(map.get("paymentRatio"))){
						if(PublicMethod.daysBetween(new Date(), uBrCourtsession.getStdate())<6){//--------7天限制
							return WebPublicMehod.returnRet("error", "7天内场次用于约战必须全款支付：\n1.您可以选择全款支付当前场次\n2.或者选择七天后的场次，非全款发起约战");
						}
					}
					ifDuel = uduelServiceImpl.checkDuelDteAdd(hashMap);
					if (ifDuel >0 ) {
						return WebPublicMehod.returnRet("error", "你有未完成的约战与当前选择时间重合不可以该时间发起约战");
					}
				}
				//挑战类型才进入判断
				if(map.get("orderType") != null && "2".equals(map.get("orderType"))){
					if(PublicMethod.daysBetween(new Date(), uBrCourtsession.getStdate())<6){
						return WebPublicMehod.returnRet("error", "守擂战只可使用一周以后的时间发起");
					}
				}
				// 预定不是空场的场次
				if (uBrCourtsession.getOrderStatus() != null && !"2".equals(uBrCourtsession.getOrderStatus())) {
					if (map.get("orderType") != null && "3".equals(map.get("orderType"))) {
						return WebPublicMehod.returnRet("error", "你所选时间在" + brCount.getName() + "不可发起约战，请重新选择时间");
					}
					return WebPublicMehod.returnRet("error", "所选场次中存在被预定的场次请重新选择场次");
				}
				// 预定时间是否与其他已预订场次时间起冲突
				listError = this.ifSameDate(map, hashMap, listBrCourtsession, listError);// 时间是否冲突
			}

			// 查询该球场全部场次
			listAllBrCourtsession = baseDAO.find(
					"from UBrCourtsession  where UBrCourt.subcourtId=:subcourtId and stdate=:stdate order by sttime ",
					hashMap);
			// 查询已预定场次
			listBrCourtsession = baseDAO.find(
					"from UBrCourtsession  where UBrCourt.subcourtId=:subcourtId and  orderId in(select orderId from UOrder where  UUser.userId=:loginUserId) and  stdate=:stdate order by sttime ",
					hashMap);
			// 将已订场和新加入的场次整合到新的listSession中
			listSession.addAll(listBrCourtsession);
			listSession.addAll(sessionList);
			if (listAllBrCourtsession != null && listAllBrCourtsession.size() > 0) {
				for (UBrCourtsession allSession : listAllBrCourtsession) {
					numberSing = 0;
					// 选中的球场大于等于2
					if (sessionList.size() >= 2) {
						for (UBrCourtsession session : sessionList) {
							numberSing++;
							if (allSession.getSttime().equals(session.getSttime())) {
								counts++;
								break;
							} else {
								// numberSing大于2的时候，说明连续中断，那么couts=0,numberSing=0
								if (numberSing >= sessionList.size()) {
									// 记录最大连续值连续场次数numbers
									if (numbers < counts) {
										numbers = counts;
									}
									counts = 0;
									numberSing = 0;
									break;
								}

							}
						}
					}
					if (listBrCourtsession != null && listBrCourtsession.size() > 0) {
						numberSeries = 0;
						// 选中的球场大于等于2
						for (UBrCourtsession session : listSession) {
							numberSeries++;
							if (allSession.getSttime().equals(session.getSttime())) {
								count++;
								break;
							} else {
								// numberSing大于2的时候，说明连续中断，那么couts=0,numberSing=0
								if (numberSeries >= listSession.size()) {
									// 记录最大连续值连续场次数numbers
									if (number < count) {
										number = count;
									}
									count = 0;
									numberSeries = 0;
									break;
								}

							}
						}
					}

				}
				// 取最大连续数
				ifCounts = numbers;
				if (counts > numbers) {
					ifCounts = counts;
				}
				// 预定场次大于等于2，并且最大连续场数小于预定场次数，那么预定的场次不是连续的
				if (sessionList.size() >= 2 && ifCounts < sessionList.size()) {
					return WebPublicMehod.returnRet("error", "单笔订单只支持连续时间预定");
				}

				// 取最大连续数(之前预定的和现在预订的)
				ifCount = number;
				if (count > number) {
					ifCount = count;
				}
				// 预定最大连续数大于3，那么超过预定场次
				if (ifCount > 3) {
					return WebPublicMehod.returnRet("error", "你想订的场次太多啦 请直接拨打大户热线 400-805-1718");
				}
			}
		}
		// 判断预定时间是否起冲突的
		if (listError != null && listError.size() > 0) {
			if(ifDuel<=0){
				return WebPublicMehod.returnRet("error", "60_" + listError);
			}
		}
		return hashMap;
	}

	@Override
	public HashMap<String, Object> saveDuelOrder(HashMap<String, String> map) throws Exception {
		// 初始数据
		map.put("orderType", "3");// 约战
		this.upgradePrompt(map);//订单类型2-挑战，3-约战，才能进入2.0.2版本后
		List<UMall> listMall = new ArrayList<>();
		List<UBrCourtsession> sessionList = new ArrayList<>();
		this.buildDate(map, listMall, sessionList);
		
		HashMap<String, Object> hashMapOrder = this.saveOrder(map, sessionList, listMall);
		hashMap = new HashMap<>();
		if (sessionList != null && sessionList.size() > 0) {
			hashMap.put("sessionId", sessionList.get(0).getSessionId());
		}
		hashMap.put("orderId", hashMapOrder.get("orderId"));
		hashMap.put("ordernum", hashMapOrder.get("ordernum"));
		hashMap.put("brCourtNam", hashMapOrder.get("brCourtNam"));

		hashMap.put("price", hashMapOrder.get("price"));
		return hashMap;
	}

	@Override
	public HashMap<String, Object> saveChallengeOrder(HashMap<String, String> map) throws Exception {
		// 初始数据
		map.put("orderType", "2");// 挑战
		map.put("paymentRatio", "50");// 挑战百分比 固定50%
		map.put("fchallenge", "1");// 通知判断使用
		this.upgradePrompt(map);//订单类型2-挑战，3-约战，才能进入2.0.2版本后
		List<UMall> listMall = new ArrayList<>();
		List<UBrCourtsession> sessionList = new ArrayList<>();
		this.buildDate(map, listMall, sessionList);// 获取处理过的商品和场次集合
//		UChampion champion= uchallengeService.getNowCh(map.get("subcourtId"));
//		if(champion!=null&&champion.getUTeam()!=null&&champion.getUTeam().getTeamId()!=null&&!"".equals(champion.getUTeam().getTeamId())){
//			map.put("teamId", champion.getUTeam().getTeamId());
//		}else{
//			return WebPublicMehod.returnRet("error", "该擂主没有符合条件的球队，不能发起守擂战！");
//		}
		if (sessionList != null && sessionList.size() > 0) {// 后台控制插入随意裁判
			List<UMall> umallList1 = baseDAO.find("from UMall where proId in("+Public_Cache.CHALLENGE_REFEREE+")");
			// 只加入一个裁判和摄影到场次中
			baseDAO.getSessionFactory().getCurrentSession().clear();
			for (UMall uMall : umallList1) {
				uMall.setPrice(0.0);//挑战裁判和摄像价格为免费
				uMall.setSaleCount(1);//数量都为1
			}
			sessionList.get(0).setUmallList(umallList1);
		}
		HashMap<String, Object> hashMapOrder = this.saveChallengeOrder(map, sessionList, listMall);
		hashMap = new HashMap<>();
		if (sessionList != null && sessionList.size() > 0) {
			hashMap.put("sessionId", sessionList.get(0).getSessionId());
		}
		hashMap.put("orderId", hashMapOrder.get("orderId"));
		hashMap.put("ordernum", hashMapOrder.get("ordernum"));
		hashMap.put("brCourtNam", hashMapOrder.get("brCourtNam"));
		hashMap.put("price", hashMapOrder.get("price"));
		return hashMap;
	}

	@Override
	public HashMap<String, Object> saveChallengeOrder(HashMap<String, String> map, List<UBrCourtsession> sessionList,
			List<UMall> listMall) throws Exception {
		if (sessionList != null && sessionList.size() > 0) {
			sessionList.removeAll(YHDCollectionUtils.nullCollection());
			UBrCourt ubr = baseDAO.get(UBrCourt.class, map.get("subcourtId"));
			UUser user = baseDAO.get(UUser.class, map.get("loginUserId"));
			// 订单流程约束
			this.ifLogic(map, sessionList);
			this.judgeChallengeOrder(map);//挑战进入
			// 订单插入
			UOrder uo = this.saveOrderInfo(map, ubr, user);
			// 进行订单记录信息的copy、库存的减少
			hashMap = this.copyInfo(sessionList, uo, map);
			uo.setPrice((Double) hashMap.get("price"));// 场次和服务的价格
			// AA支付价格处理
			this.displayOrderPrice(map, uo);
			uo.setPrice(0.0);// 挑战时 订单时间支付价格为0元
			uo.setOrderstatus("1");// 已支付
			// 将场次状态改为已预订
			uCourtService.saveCourtSessionOrderStatus(uo.getOrderId());
			// 将新生成的订单加入到缓存中
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.getOrderHRedisKey(uo, map);

			hashMap = new HashMap<>();
			hashMap.put("brCourtNam", ubr.getName());
			hashMap.put("price", uo.getPrice());
			hashMap.put("orderId", uo.getOrderId());
			hashMap.put("ordernum", uo.getOrdernum());
			// hashMap.put("order",uo);
			return hashMap;
		}
		return WebPublicMehod.returnRet("error", "没有选定场次！");

	}

	@Override
	public HashMap<String, Object> saveOrder(HashMap<String, String> map, List<UBrCourtsession> sessionList,
			List<UMall> listMall) throws Exception {
		// this.buildDate(map, listMall, sessionList);
		if (sessionList != null && sessionList.size() > 0) {
			sessionList.removeAll(YHDCollectionUtils.nullCollection());
			UBrCourt ubr = baseDAO.get(UBrCourt.class, map.get("subcourtId"));
			UUser user = userService.getUserinfoByToken(map);
			// 订单流程约束
			this.ifLogic(map, sessionList);
			// 订单插入
			UOrder uo = this.saveOrderInfo(map, ubr, user);
			// 进行订单记录信息的copy、库存的减少
			hashMap = this.copyInfo(sessionList, uo, map);
			uo.setPrice((Double) hashMap.get("price"));// 场次和服务的价格
			// AA支付价格处理
			this.displayOrderPrice(map, uo);
			// 普通商品加入
			if(sessionList!=null&&sessionList.size()==1){
				hashMap = this.copyMallInfo(listMall, uo, map);//只有一个场次的
			}else{
				hashMap = this.copyAddOrderMallInfo(listMall, uo, map);//多个场次
			}
			uo.setPrice(Double.valueOf(df.format(uo.getPrice() + (Double) hashMap.get("price"))));// 普通商品的价格
			// 将新生成的订单加入到缓存中
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.getOrderHRedisKey(uo, map);
			hashMap = new HashMap<>();
			hashMap.put("brCourtNam", ubr.getName());
			hashMap.put("price", uo.getPrice());

			hashMap.put("orderId", uo.getOrderId());
			hashMap.put("ordernum", uo.getOrdernum());
			// hashMap.put("order",uo);
			return hashMap;
		}
		return WebPublicMehod.returnRet("error", "没有选定场次！");

	}

	@Override
	public HashMap<String, Object> saveAppendOrder(HashMap<String, String> map) throws Exception {
		// 初始数据
		map.put("orderType", "6");// 商城
		List<UMall> listMall = new ArrayList<>();
		this.buildDate(map, listMall, null);
		HashMap<String, Object> hashMapOrder = this.saveAddOrder(map, listMall);
		hashMap = new HashMap<>();
		hashMap.put("orderId", hashMapOrder.get("orderId"));
		hashMap.put("ordernum", hashMapOrder.get("ordernum"));
		if (hashMapOrder.get("brCourtNam") != null) {
			hashMap.put("brCourtNam", hashMapOrder.get("brCourtNam"));
		}
		hashMap.put("price", hashMapOrder.get("price"));
		return hashMap;
	}
	

	@Override
	public HashMap<String, Object> saveAddOrder(HashMap<String, String> map, List<UMall> listMall) throws Exception {
		// 插入订单信息
		UOrder uoo = baseDAO.getHRedis(UOrder.class, map.get("orderId"),
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));// 主订单查询
		UUser user = userService.getUserinfoByToken(map);

		if (!"-1".equals(uoo.getPerId()) && "1".equals(uoo.getRelationType())) {
			UOrder orderPer = baseDAO.getHRedis(UOrder.class, uoo.getPerId(),
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));// 查出发起方预定的订单
			map.put("orderId", orderPer.getOrderId());
		}
		UBrCourt ubr = this.getOrderBrCourt(map);
		if ("1".equals(uoo.getOrderstatus())) {
			map.put("perId", uoo.getOrderId());
			// 订单流程约束
			this.ifLogic(map, null);
			UOrder uo = this.saveOrderInfo(map, ubr, user);// 生成订单
			uo.setPaymentRatio("100");// 支付百分比
			HashMap<String, Object> hashMap = this.copyAddOrderMallInfo(listMall, uo, map);
			// 更新主订单总价
			// uoo.setPrice(Double.valueOf(df.format(uoo.getPrice() + (Double)
			// hashMap.get("price"))));// 主订单价格加上子订单价格
			// baseDAO.update(uoo);
			// // 删除主订单缓存(作用：更新缓存，删除的缓存会被重新查询出来)
			// this.removeRedis(map, uoo);
			uo.setPrice((Double) hashMap.get("price"));// 子订单价格
			// 将新生成的订单加入到缓存中
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.getOrderHRedisKey(uo, map);

			hashMap = new HashMap<>();
			if (ubr != null) {
				hashMap.put("brCourtNam", ubr.getName());
			}
			hashMap.put("price", uo.getPrice());
			hashMap.put("orderId", uo.getOrderId());
			hashMap.put("ordernum", uo.getOrdernum());
			return hashMap;
		}
		return WebPublicMehod.returnRet("error", "你的订单出现异常\n如有疑问拨打400-805-1719 联系客服小激！");
	}

	@Override
	public HashMap<String, Object> saveAppendDuelAndChallOrder(HashMap<String, String> map) throws Exception {
		// 初始数据
		List<UMall> listMall = new ArrayList<>();
//		this.upgradePrompt(map);//订单类型2-挑战，3-约战，才能进入2.0.2版本后
		this.buildDate(map, listMall, null);
		HashMap<String, Object> hashMapOrder = this.saveAddDuelAndChallOrder(map, listMall);
		return hashMapOrder;
	}
	

	@Override
	public HashMap<String, Object> saveAddDuelAndChallOrder(HashMap<String, String> map, List<UMall> listMall)
			throws Exception {
		// 插入订单信息
		UOrder uoo = baseDAO.getHRedis(UOrder.class, map.get("orderId"),
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));// 主订单查询
		List<UOrder> listOrder =this.judgeReplenishmentOrder(map);
		if(listOrder!=null&&listOrder.size()>=1){
			return WebPublicMehod.returnRet("error", "该约战已存在待支付的补款订单，不能响应！");
		}
		if (!"-1".equals(uoo.getPerId()) && "1".equals(uoo.getRelationType())) {
			UOrder orderPer = baseDAO.getHRedis(UOrder.class, uoo.getPerId(),
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));// 查出发起方预定的订单
			map.put("orderId", orderPer.getOrderId());
		}
		UBrCourt ubr = this.getOrderBrCourt(map);
		this.judgeOrderResp(map,"不能响应！");
		
		if ("1".equals(uoo.getOrderstatus())) {
			UUser user = userService.getUserinfoByToken(map);
			map.put("perId", uoo.getOrderId());
			map.put("orderType", uoo.getOrderType());
			// 订单流程约束
			this.ifLogic(map, null);
			UOrder uo = this.saveOrderInfo(map, ubr, user);// 生成订单
			uo.setPrice(0.0);// 初始价格
			//跟随主订单支付状态
			uo.setPayTypeStatus(uoo.getPayTypeStatus());
			// 约战方AA付款的时候进入
			if (null != uoo.getPaymentRatio() && !"".equals(uoo.getPaymentRatio())) {
				uo.setPrice(Double.valueOf(uoo.getAllprice()));
				Integer paymentRatio = 100 - Integer.valueOf(uoo.getPaymentRatio());
				map.put("paymentRatio", paymentRatio.toString());
				this.displayOrderPrice(map, uo);
			}
			// 商品追加
			hashMap = this.copyAddOrderMallInfo(listMall, uo, map);
			uo.setPrice(Double.valueOf(df.format(uo.getPrice() + (Double) hashMap.get("price"))));// 子订单价格

			// 参数
			hashMap = new HashMap<>();
			if (ubr != null) {
				hashMap.put("brCourtNam", ubr.getName());
			}
			hashMap.put("price", uo.getPrice());
			hashMap.put("orderId", uo.getOrderId());
			hashMap.put("ordernum", uo.getOrdernum());
			hashMap.put("order", uo);
			String ifPay = "-1";// 不支付
			if (uo.getPrice() > 0) {
				ifPay = "1";// 支付
			} else {
				uo.setOrderstatus("1");// 订单支付
				uo.setPaytype("4");// 无须支付免费
			}
			hashMap.put("ifPay", ifPay);
			// 将新生成的订单加入到缓存中
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.getOrderHRedisKey(uo, map);
			return hashMap;
		}
		return WebPublicMehod.returnRet("error", "你的订单出现异常\n如有疑问拨打400-805-1719 联系客服小激！");
	}
	@Override
	public List<UOrder> judgeReplenishmentOrder(HashMap<String, String> map) throws Exception{
		return baseDAO.find(map,"from UOrder where perId =:orderId and (orderstatus ='4' or orderstatus='3') and relationType='3' ");
	}
	@Override
	public HashMap<String, Object> saveReplenishmentOrder(HashMap<String, String> map) throws Exception {
//		 插入订单信息
		UOrder uoo = baseDAO.get(UOrder.class, map.get("orderId"));// 主订单查询
		List<UOrder> listOrder=this.judgeReplenishmentOrder(map);
		
			if (uduelServiceImpl.complementDuelPay(map) == 2) {
				return WebPublicMehod.returnRet("error", "你的约战不满足补款条件！");
			}
			this.judgeOrderResp(map,"不能补款！");
			if (!"-1".equals(uoo.getPerId()) && "1".equals(uoo.getRelationType())) {
				UOrder orderPer = baseDAO.getHRedis(UOrder.class,uoo.getPerId(),
						PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));// 查出发起方预定的订单
				map.put("orderId", orderPer.getOrderId());
			}
			UBrCourt ubr = this.getOrderBrCourt(map);
			
			if(listOrder!=null&&listOrder.size()==0){
				if ("1".equals(uoo.getOrderstatus())) {
					UUser user = userService.getUserinfoByToken(map);
					map.put("perId", uoo.getOrderId());
					map.put("orderType", "1");// 将订单类型设置为球场类型
					UOrder uo = this.saveOrderInfo(map, ubr, user);// 生成订单
					uo.setPrice(0.0);// 初始价格
					uo.setRelationType("3");//补款
					// 约战方AA付款的时候进入
					if (null != uoo.getPaymentRatio() && !"".equals(uoo.getPaymentRatio())) {
						uo.setPrice(Double.valueOf(uoo.getAllprice()));
						Integer paymentRatio = 100 - Integer.valueOf(uoo.getPaymentRatio());
						map.put("paymentRatio", paymentRatio + "");
						this.displayOrderPrice(map, uo);
					}
		
					// 参数
					hashMap = new HashMap<>();
					if (ubr != null) {
						hashMap.put("brCourtNam", ubr.getName());
					}
					hashMap.put("price", uo.getPrice());
					hashMap.put("orderId", uo.getOrderId());
					hashMap.put("ordernum", uo.getOrdernum());
					if (uo.getPrice() <= 0) {
						uo.setOrderstatus("1");// 订单支付
						uo.setPaytype("4");// 无须支付免费
						
						uoo.setOrderType("1");// 将订单类型更改为球场类型
						baseDAO.getSessionFactory().getCurrentSession().flush();
						this.removeRedis(map, uoo);// 更改主订单数据的缓存
		
						map.put("orderid",uoo.getOrderId());
						uduelServiceImpl.addDuelAgain(map);//取消约战
					}
					baseDAO.getSessionFactory().getCurrentSession().flush();
					this.getOrderHRedisKey(uo, map);// 将子订单数据添加到缓存中
					return hashMap;
				}
				return WebPublicMehod.returnRet("error", "你的订单出现异常\n如有疑问拨打400-805-1719 联系客服小激！");
			}
			return WebPublicMehod.returnRet("error", "你存在一笔补款订单，请前往个人中心查看！");
	}

	@Override
	public HashMap<String, Object> cancelOrder(HashMap<String, String> map) throws Exception {
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("orderId", map.get("orderId"));
			UOrder order = baseDAO.get(UOrder.class, map.get("orderId"));
			if(order!=null){
				// 待定和待支付才能取消
				if ("4".equals(order.getOrderstatus()) || "3".equals(order.getOrderstatus())) {
					if ("-1".equals(order.getPerId())) { // 主订单取消
						// 空城-主订单才会关联球场
						uCourtService.updateCourtSession(order.getOrderId());
					}
					// 取消订单--自身
					order.setOrderstatus("5");
					baseDAO.update(order);
					baseDAO.getSessionFactory().getCurrentSession().flush();
					map.put("loginUserId", "");
					if(order.getUUser()!=null&&!"".equals(order.getUUser().getUserId())){
						map.put("loginUserId", order.getUUser().getUserId());
					}
					this.removeRedis(map, order);// 删除查询详情里已经变更的缓存
					return WebPublicMehod.returnRet("success", "取消订单成功");
				}
			}
		}
		return WebPublicMehod.returnRet("error", "你的订单出现异常\n如有疑问拨打400-805-1719 联系客服小激！");
	}

	@Override
	public HashMap<String, Object> cancelChallengeOrder(HashMap<String, String> map) throws Exception {
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("orderId", map.get("orderId"));
			UOrder order = baseDAO.get(UOrder.class, map.get("orderId"));
			if ("-1".equals(order.getPerId())) { // 主订单取消
				int count = baseDAO.count("select count(orderId) from UOrder where perId =:orderId and orderstatus='1'",
						false);
				if (count <= 0) {
					// 退擂主订单
					order.setOrderstatus("7");
					baseDAO.update(order);
					// 空城-主订单才会关联球场
					uCourtService.updateCourtSession(order.getOrderId());
					baseDAO.getSessionFactory().getCurrentSession().flush();
					this.removeRedis(map, order);// 删除查询详情里已经变更的缓存
					// 删除主订单缓存
					List<UOrder> listOrder = baseDAO.find(
							"from UOrder where UUser.userId=:loginUserId and perId =:orderId and (orderstatus='3' or orderstatus='4')",
							hashMap);
					baseDAO.executeHql(
							"update UOrder set orderStatus ='5' where UUser.userId=:loginUserId and perId =:orderId and (orderstatus='3' or orderstatus='4')",
							hashMap);// 所有子订单同时取消
					baseDAO.getSessionFactory().getCurrentSession().flush();
					if (listOrder != null && listOrder.size() > 0) {
						for (UOrder uOrder : listOrder) {
							this.removeRedis(map, uOrder);// 删除查询详情里已经变更的缓存
						}
					}
				} else {
					return WebPublicMehod.returnRet("error", "-50_你取消的订单存在已支付的关联订单，请拨打400-805-1718电话取消");
				}
			}
			return WebPublicMehod.returnRet("success", "取消订单成功");
		}
		return WebPublicMehod.returnRet("error", "请选择取消的订单");
	}

	@Override
	public HashMap<String, Object> displayOrder(HashMap<String, String> map) throws Exception {
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			UOrder order = baseDAO.get(UOrder.class, map.get("orderId"));
			if (null != order) {
				this.handlingOrder(map,order);
//				// 待定和待支付才能取消
//				if ("4".equals(order.getOrderstatus()) || "3".equals(order.getOrderstatus())) {
//					if ("-1".equals(order.getPerId())) { // 主订单取消
//						if ("2".equals(order.getRelationType())) {
//							// 空城-主订单才会关联球场
//							uCourtService.updateCourtSession(order.getOrderId());
//							hashMap = PublicMethod.Maps_Mapo(map);
//							baseDAO.executeHql("update UOrder set orderStatus ='5' where perId =:orderId", hashMap);// 所有子订单同时取消
//							// 删除主订单缓存
//							List<UOrder> listOrder = baseDAO.find("from UOrder where perId =:orderId", hashMap);
//							baseDAO.getSessionFactory().getCurrentSession().flush();
//							if (listOrder != null && listOrder.size() > 0) {
//								for (UOrder uOrder : listOrder) {
//									this.removeRedis(map, uOrder);// 删除查询详情里已经变更的缓存
//								}
//							}
//						}
//					}
//					order.setOrderstatus("5");
//				}
//
//				order.setDisplayStatus("-1");
//				baseDAO.update(order);
//				baseDAO.getSessionFactory().getCurrentSession().flush();
//				this.removeDelRedis(map, order);

				return WebPublicMehod.returnRet("orderId", order.getOrderId());
			}
			return WebPublicMehod.returnRet("error", "没有此订单！");
		}
		return WebPublicMehod.returnRet("error", "请选择侧滑删除的订单");
	}

	@Override
	public HashMap<String, Object> displayAllOrder(HashMap<String, String> map) throws Exception {
		if (null != map.get("orderIds") && !"".equals(map.get("orderIds"))) {
			HashMap<String,List<Object>> mapList=new HashMap<>();
			List<Object> list=new ArrayList<>();
			String[] orderIds=map.get("orderIds").split(",");
			if(orderIds!=null&&orderIds.length>0){
				for (int i = 0; i < orderIds.length; i++) {
					list.add(orderIds[i]);
				}
			}
			mapList.put("orderIdAll", list);
			List<UOrder> listUOrder=baseDAO.find(map, "from UOrder where orderId in(:orderIdAll)", mapList);
			if(listUOrder!=null&&listUOrder.size()>0){
				for (UOrder order : listUOrder) {
					map.put("orderId", order.getOrderId());
					this.handlingOrder(map, order);
				}
				return WebPublicMehod.returnRet("success", "批量删除成功");
			}
		}
		return WebPublicMehod.returnRet("error", "缺少请求参数！");	
	}

	@Override
	public void editRelationTypeOrder(HashMap<String, String> map) throws Exception {
		if (map.get("orderId") != null && !"".equals(map.get("orderId"))) {
			UOrder order = baseDAO.get(UOrder.class, map.get("orderId"));
			order.setOrderType("3");
			order.setRelationType("1");
			order.setTeamId(map.get("teamId"));
			baseDAO.update(order);
			// 清空缓存
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.removeRedis(map, order);
		}
	}

	@Override
	public void editStatusOrder(HashMap<String, String> map) throws Exception {
		if (map.get("orderId") != null && !"".equals(map.get("orderId"))) {
			UOrder order = baseDAO.get(UOrder.class, map.get("orderId"));
			order.setOrderstatus("1");
			baseDAO.update(order);
			// 清空缓存
			baseDAO.getSessionFactory().getCurrentSession().flush();
			this.removeRedis(map, order);
		}
	}
	@Override
	public HashMap<String, Object> judgeOrderResp(HashMap<String, String> map,String...str) throws Exception {
		HashMap<String, String> map1=new HashMap<>();
		List<UOrder> listOrder = baseDAO.find(map,
				"from UOrder where perId=:orderId and relationType='1' and orderstatus ='4' ");
		if (listOrder != null && listOrder.size() > 0) {
			for (UOrder uOrder : listOrder) {
				// 判断是否超过5分钟
				boolean isExpired = isExpired(uOrder.getCreatedate(),uOrder.getCreatetime(), Public_Cache.ORDER_RESP_TIME);
				if (isExpired == true) {
					uOrder.setOrderstatus("2");
					baseDAO.update(uOrder);
					baseDAO.getSessionFactory().getCurrentSession().flush();
					map1.putAll(map);
					map1.put("loginUserId", "");
					if(uOrder.getUUser()!=null&&!"".equals(uOrder.getUUser().getUserId())){
						map1.put("loginUserId", uOrder.getUUser().getUserId());
					}
					this.removeRedis(map1, uOrder);// 删除查询详情里已经变更的缓存
				} else {
					if(str.length > 1 && null != str[1] && !"".equals(str[1])){
						HashMap<String, Object> hash = new HashMap<String, Object>();
						hash.put("error", "-1");
						return hash;
					}else{
						return WebPublicMehod.returnRet("error", "已存在待支付的响应方,"+str[0]);
					}
				}
			}
		}
		return null;
	}
	/**
	 * 未确定业务
	 */
	@Override
	public HashMap<String, Object> refundOrderInfo(HashMap<String, String> map) throws Exception {

		// // 如果有取消订单的权限
		// UOrderRefund orderRefund = baseDAO.get(map, "from UOrderRefund where
		// UOrder.orderId = :orderId");
		// return WebPublicMehod.returnRet("orderRefund", orderRefund);
		return null;
	}

	@Override
	public void removeRedis(HashMap<String, String> map, UOrder order) throws Exception {
		if(order!=null){
//			List<Serializable> removelist = new ArrayList<Serializable>();
//			removelist.add(order.getOrderId());
			map.put("orderType", order.getOrderType());
			publicService.removeHRedis_HSET(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"),order.getOrderId());// 删除查询详情里已经变更的缓存
			publicService.removeHRedis_HSET(
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ObjectSQL", this.getOrderSql(map)),
					order.getOrderId());// 删除查询详情里已经变更的缓
	
			this.addRedisInit(order, map);// 添加数据到缓存列表中
		}
	}

	@Override
	public void getOrderHRedisKey(UOrder uo, HashMap<String, String> map) throws Exception {
		if(uo!=null){
		map.put("orderType", uo.getOrderType());
		List<Serializable> removeList = new ArrayList<>();
		removeList.add(uo.getOrderId());
		redisDao.removeListValue(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ListPageSQL", this.getOrderSql(map)),removeList);//先删除
		redisDao.addList(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ListPageSQL", this.getOrderSql(map)),removeList);//后添加
		this.addRedisInit(uo, map);
		}
	}
	@Override
	public HashMap<String, Object> savePureMallOrder(HashMap<String, String> map) throws Exception {
		// 初始数据
		map.put("orderType", "6");// 商品
		List<UMall> listMall = new ArrayList<>();
		List<UOrderCourt> listOrderCourt = new ArrayList<>();
		this.buildDatePureMall(map, listMall, listOrderCourt);//数据处理
		//订单生成
		HashMap<String, Object> hashMapOrder = this.savePureMallOrder(map, listOrderCourt, listMall);
		hashMap = new HashMap<>();
		hashMap.put("orderId", hashMapOrder.get("orderId"));
		hashMap.put("ordernum", hashMapOrder.get("ordernum"));
		hashMap.put("brCourtNam", hashMapOrder.get("brCourtNam"));
		hashMap.put("price", hashMapOrder.get("price"));
		return hashMap;
	}
	
	@Override
	public HashMap<String, Object> savePureMallOrder(HashMap<String, String> map, List<UOrderCourt> listOrderCourt,
			List<UMall> listMall) throws Exception{
		// 初始数据
		map.put("orderType", "6");// 商品
		listOrderCourt.removeAll(YHDCollectionUtils.nullCollection());
		UBrCourt ubr = baseDAO.get(UBrCourt.class, map.get("subcourtId"));
		UUser user = userService.getUserinfoByToken(map);
		// 订单流程约束
		this.ifLogic(map, null);
		// 订单插入
		UOrder uo = this.saveOrderInfo(map, ubr, user);
		uo.setPaymentRatio("100");
		uo.setAllprice(0.0);
		// 进行订单记录信息的copy、库存的减少
		hashMap = this.copyInfoPureMall(listOrderCourt, uo, map);
		uo.setPrice((Double) hashMap.get("price"));// 场次和服务的价格
		// AA支付价格处理
		this.displayOrderPrice(map, uo);
		// 普通商品加入
		hashMap = this.copyAddOrderMallInfo(listMall, uo, map);//纯商品
		uo.setPrice(Double.valueOf(df.format(uo.getPrice() + (Double) hashMap.get("price"))));// 普通商品的价格
		// 将新生成的订单加入到缓存中
		baseDAO.getSessionFactory().getCurrentSession().flush();
		this.getOrderHRedisKey(uo, map);
		hashMap = new HashMap<>();
		hashMap.put("brCourtNam", ubr.getName());
		hashMap.put("price", uo.getPrice());

		hashMap.put("orderId", uo.getOrderId());
		hashMap.put("ordernum", uo.getOrdernum());
		// hashMap.put("order",uo);
		return hashMap;
	}

	/**
	 * 
	 * TODO 删除缓存列表
	 * 
	 * @param map
	 * @param order
	 * @throws Exception
	 *             void xiao 2016年4月19日
	 */
	private void removeDelRedis(HashMap<String, String> map, UOrder order) throws Exception {
		if(order!=null){
		List<Serializable> removelist = new ArrayList<Serializable>();
		removelist.add(order.getOrderId());
		map.put("orderType", order.getOrderType());
		redisDao.removeListValue(
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ListPageSQL", this.getOrderSql(map)), removelist);// 删除待支付里面已过期的缓存
		publicService.removeHRedis_HSET(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"),order.getOrderId());// 删除查询详情里已经变更的缓存
		publicService.removeHRedis_HSET(
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ObjectSQL", this.getOrderSql(map)),
				order.getOrderId());// 删除查询详情里已经变更的缓
		}
	}

	/**
	 * 
	 * TODO 修改状态后添加缓存
	 * 
	 * @param order
	 * @param map
	 *            void xiao 2016年4月19日
	 */
	private void addRedisInit(UOrder order, HashMap<String, String> map) throws Exception {
		map.put("orderId", order.getOrderId());
		String sql = "select o.order_id as orderId,o.ordernum,o.orderstatus,o.title,"
				+ "ifnull(bc.name,(select ubc.name from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as name,o.relation_type as relationType,"
				+ "ifnull(bc.subcourt_type,(select ubc.subcourt_type from u_order_court uoc left join u_br_court ubc on uoc.subcourt_id=ubc.subcourt_id  where uoc.order_id=o.per_id group by o.order_id)) as subcourtType,"
				+ "date_format(ifnull(oc.stdate,(select uoc.stdate from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)),'%Y-%m-%d') stdate,"
				+ "ifnull(oc.week,(select uoc.week from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id)) as week,"
				+ "min(ifnull(oc.sttime,(select min(uoc.sttime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as sttime ,"
				+ "max(ifnull(oc.endtime,(select max(uoc.endtime) from u_order_court uoc where uoc.order_id=o.per_id group by o.order_id))) as endtime,"
				+ "o.order_type as orderType,ft.name as duelFName,xt.name as duelXName,"
				+ "ft1.name as challFName,xt1.name as challXName,om.name as mallName,om.count from u_order o "
				+ "left join u_order_court oc on o.order_id = oc.order_id "
				+ "left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ "left join u_duel d on d.order_id=o.order_id " + "left join u_duel_resp dr on dr.duel_id=d.duel_id "
				+ "left join u_team ft on ft.team_id=d.f_team_id " + "left join u_team xt on xt.team_id=dr.team_id "
				+ "left join u_challenge c on c.order_id =o.order_id "
				+ "left join u_team ft1 on ft1.team_id=c.f_team_id "
				+ "left join u_team xt1 on xt1.team_id=c.x_team_id "
				+ "left join u_order_mall om on om.order_id=o.order_id "
				+ "where o.order_id =:orderId group by o.order_id ";
		baseDAO.findSQLMapHRedis(map, sql, PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "ObjectSQL"),
				this.getOrderInitSql(map), "orderId");
	}

	/**
	 * 
	 * TODO 订单-挑战约束
	 * 
	 * @param map
	 *            loginUserId-当前用户
	 * @throws Exception
	 *             void xiao 2016年4月19日
	 */
	private HashMap<String, Object> judgeChallengeOrder(HashMap<String, String> map) throws Exception {
		String orderId = "";
//		List<UOrder> listOrder = baseDAO.find(map,
//				"from UOrder where orderType='2' and orderstatus='1' and UUser.userId=:loginUserId order by createdate desc,createtime desc");
//		//最近一笔订单
//		if (listOrder != null && listOrder.size() > 0) {
//			orderId = listOrder.get(0).getOrderId();
//			map.put("orderId", orderId);
//			List<UBrCourtsession> list=baseDAO.find(map,"from UBrCourtsession where orderId=:orderId and  (stdate>date_format(now(),'%Y-%m-%d') or (stdate=date_format(now(),'%Y-%m-%d') and sttime>date_format(now(),'%H:%i'))) order by stdate ,sttime");
//			//如果场次过期，那么最近一笔订单Id不传过过去
//			if((list!=null&&list.size()==0)||(list==null)){
//				orderId="";
//			}	
//		}
		String sql="select o.order_id as orderId from u_order o "
				+ " left join u_br_courtsession bc on bc.order_id=o.order_id "
				+ " where o.order_type='2' and o.orderstatus='1'  and o.user_id=:loginUserId and bc.subcourt_id=:subcourtId "
				+ " and (bc.stdate>date_format(now(),'%Y-%m-%d') or (bc.stdate=date_format(now(),'%Y-%m-%d') and bc.sttime>date_format(now(),'%H:%i'))) "
				+ " order by o.createdate desc,o.createtime desc";
		List<HashMap<String, Object>> listOrder=baseDAO.findSQLMap(map, sql);
		if (listOrder != null && listOrder.size() >= 1) {
			orderId = listOrder.get(0).get("orderId").toString();
		}
		// 挑战判断
		if (!uchallengeService.returnOrderStatus(orderId)) {
			WebPublicMehod.returnRet("error", "-50_你最近已生成了一笔待使用的订单或待响应的挑战，不能再次下单！");
		}
		return null;
	}

	/**
	 * 
	 * TODO 根据订单Id查询下属球场对象
	 * 
	 * @param map
	 *            orderId-订单Id
	 * @return
	 * @throws Exception
	 *             UBrCourt xiao 2016年4月9日
	 */
	private UBrCourt getOrderBrCourt(HashMap<String, String> map) throws Exception {
		List<UOrderCourt> listOrderCourt = baseDAO.find(map, "from UOrderCourt where UOrder.orderId=:orderId");
		if (listOrderCourt != null && listOrderCourt.size() > 0) {
			map.put("subcourtId", listOrderCourt.get(0).getUBrCourt().getSubcourtId());
			return baseDAO.get(UBrCourt.class, map.get("subcourtId"));
		}
		return null;
	}

	/**
	 * 
	 * TODO 商品图片地址
	 * 
	 * @param productSubtype
	 * @return String xiao 2016年3月30日
	 */
	private String getImgurlType(String productSubtype) {
		String subtype = "";
		if ("1".equals(productSubtype)) {
			subtype = "";
		}
		if ("2".equals(productSubtype)) {
			subtype = "";
		}
		if ("3".equals(productSubtype)) {
			subtype = "";
		}
		return subtype;
	}

	/**
	 * 
	 * TODO 订单关联的球场和商品 以及数据设置
	 * 
	 * @param map
	 * @param uOrder
	 * @throws Exception
	 *             void xiao 2016年3月21日
	 */
	private void getCourtAndMallResp(HashMap<String, String> map, UOrder uOrder) throws Exception {
		double paymentRatio = 0;
		// 约战方AA付款的时候进入
		if (null != uOrder.getPaymentRatio() && !"".equals(uOrder.getPaymentRatio())) {
			paymentRatio = (100 - Double.parseDouble(uOrder.getPaymentRatio())) / 100;
		}
		List<HashMap<String, Object>> listOrderMall = new ArrayList<>();// 商品集合
		List<HashMap<String, Object>> listOrderCourt = new ArrayList<>();// 球场集合
		String sql = "select om.size,om.sale_type saleType,om.count,om.price yprice,om.product_subtype productSubtype,"
				+ " om.product_detailtype productDetailtype,om.name,mi.imgurl,m1.price,om.remark from u_order_mall om "
				+ " left join u_mall m on m.pro_id = om.pro_p_id "
				+ " left join u_mall m1 on m1.similar_id=m.similar_id and m1.price_type='1'"
				+ " left join u_mall_img mi on mi.similar_id=m.similar_id "
				+ " and mi.img_size_typwe='1' and mi.mimg_using_type='1' "
				+ " where om.order_id=:orderId and om.product_subtype='3'";
		listOrderMall = baseDAO.findSQLMap(map, sql);
		sql = "select oc.session_id sessionId,oc.subcourt_id subcourtId,bc.name,bc.subcourt_type subcourtType,date_format(oc.stdate,'%m月%d日') stdate,oc.week,oc.sttime,oc.endtime,c.address,"
				+ " oc.session_price sessionPrice,oc.discount_price discountPrice,oc.member_price memberPrice,"
				+ " CASE WHEN oc.activity_price IS NOT NULL THEN oc.activity_price WHEN oc.member_price IS NOT NULL THEN oc.member_price "
				+ " WHEN oc.discount_price IS NOT NULL THEN oc.discount_price WHEN oc.fav_price IS NOT NULL THEN oc.fav_price "
				+ " WHEN oc.session_price IS NOT NULL THEN oc.session_price ELSE 0.0 END AS favPrice,"
				+ " oc.activity_price activityPrice,ci.imgurl from u_order_court oc "
				+ " left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ " left join u_court c on c.court_id =bc.court_id"
				+ " left join u_br_courtimage ci on ci.subcourt_id=bc.subcourt_id "
				+ " and ci.img_size_type='1' and ci.cimg_using_type='2'"
				+ " where oc.order_id=:orderId group by oc.order_id order by sttime";
		listOrderCourt = baseDAO.findSQLMap(map, sql);
		// 商品总价
		Double mallPrice = 0.0;
		Double price = 0.0;
		if (listOrderMall != null && listOrderMall.size() > 0) {
			for (HashMap<String, Object> orderMallMap : listOrderMall) {
//				orderMallMap.put("yprice", df.format(orderMallMap.get("yprice")));
//				orderMallMap.put("price", df.format(orderMallMap.get("price")));
				if (null != orderMallMap.get("yprice") && !"".equals(orderMallMap.get("yprice"))
						&& null != orderMallMap.get("count") && !"".equals(orderMallMap.get("count"))) {
					mallPrice = Double.valueOf(orderMallMap.get("yprice").toString())
							* Integer.valueOf(orderMallMap.get("count").toString());
					price+=mallPrice * paymentRatio;
					orderMallMap.put("yprice", df.format(mallPrice * paymentRatio));// 商品总价
				} else {
					if (null != orderMallMap.get("price") && !"".equals(orderMallMap.get("price"))
							&& null != orderMallMap.get("count") && !"".equals(orderMallMap.get("count"))) {
						mallPrice = Double.valueOf(orderMallMap.get("price").toString())
								* Integer.valueOf(orderMallMap.get("count").toString());
						orderMallMap.put("price", df.format(mallPrice * paymentRatio));// 商品总价
						price+=mallPrice * paymentRatio;
					}
				}
				// price+=mallPrice*paymentRatio;
				orderMallMap.put("mallSumPrice", df.format(mallPrice * paymentRatio));// 商品总价
				orderMallMap.put("productSubtypeName",
						Public_Cache.HASH_PARAMS("product_subtype").get(orderMallMap.get("productSubtype")));
				orderMallMap.put("productDetailtypeName",
						Public_Cache.HASH_PARAMS("product_detailtype").get(orderMallMap.get("productDetailtype")));
			}
		}
		// 球场人数规格
		String sessions = "";
		if (listOrderCourt != null && listOrderCourt.size() > 0) {
			boolean ifAct = true;
			for (HashMap<String, Object> hashMap : listOrderCourt) {
				ifAct = true;
				// 优惠价
				if (hashMap.get("favPrice") != null && !"".equals(hashMap.get("favPrice"))) {
					hashMap.put("favPrice",
							df.format(Double.valueOf(hashMap.get("favPrice").toString()) * paymentRatio));
					price += Double.valueOf(hashMap.get("favPrice").toString());
					ifAct = false;
				}
				// 场次单价 （原价）
				if (hashMap.get("sessionPrice") != null && !"".equals(hashMap.get("sessionPrice"))) {
					hashMap.put("sessionPrice",
							df.format(Double.valueOf(hashMap.get("sessionPrice").toString()) * paymentRatio));
					if (ifAct) {
						price += Double.valueOf(hashMap.get("sessionPrice").toString());
					}
				}
				if (hashMap.get("discountPrice") != null && !"".equals(hashMap.get("discountPrice"))) {
					hashMap.put("discountPrice",
							df.format(Double.valueOf(hashMap.get("discountPrice").toString()) * paymentRatio));
				}
				if (hashMap.get("memberPrice") != null && !"".equals(hashMap.get("memberPrice"))) {
					hashMap.put("memberPrice",
							df.format(Double.valueOf(hashMap.get("memberPrice").toString()) * paymentRatio));
				}
				if (hashMap.get("activityPrice") != null && !"".equals(hashMap.get("activityPrice"))) {
					hashMap.put("activityPrice",
							df.format(Double.valueOf(hashMap.get("activityPrice").toString()) * paymentRatio));
				}
		        String week="";
                if(hashMap.get("week")!=null){
                    week=hashMap.get("week").toString();
                }
                hashMap.put("week", WebPublicMehod.checkWeekString(week));
				hashMap.put("subcourtTypeName",
						Public_Cache.HASH_PARAMS("subcourt_type").get(hashMap.get("subcourtType")));
				sessions = hashMap.get("sessionId").toString() + ",";
			}
		}
		// 加入订单中
		uOrder.setUOrderMall(listOrderMall);
		uOrder.setUOrderCourt(listOrderCourt);
		uOrder.setPrice(price);// 计算价格
		if (sessions.length() > 0) {
			sessions = sessions.substring(0, sessions.length() - 1);
		}
		map.put("sessionids", sessions);
		uOrder.setProductType(uCourtService.queryProductTypeBySession(map));
	}

	/**
	 * 
	 * TODO 订单关联的球场和商品 以及数据设置
	 * 
	 * @param map
	 * @param uOrder
	 * @throws Exception
	 *             void xiao 2016年3月21日
	 */
	private void getCourtAndMall(HashMap<String, String> map, UOrder uOrder) throws Exception {
		double paymentRatio = 0;
		// 约战方AA付款的时候进入
		if (null != uOrder.getPaymentRatio() && !"".equals(uOrder.getPaymentRatio())) {
			paymentRatio = Double.parseDouble(uOrder.getPaymentRatio()) / 100;
		}
		List<HashMap<String, Object>> listOrderMall = new ArrayList<>();// 商品集合
		List<HashMap<String, Object>> listOrderCourt = new ArrayList<>();// 球场集合
		String sql = "select om.size,om.sale_type saleType,om.count,om.price yprice,om.product_subtype productSubtype,"
				+ " om.product_detailtype productDetailtype,om.name,mi.imgurl,m1.price,om.remark from u_order_mall om "
				+ " left join u_mall m on m.pro_id = om.pro_p_id "
				+ " left join u_mall m1 on m1.similar_id=m.similar_id and m1.price_type='1'"
				+ " left join u_mall_img mi on mi.similar_id=m.similar_id "
				+ " and mi.img_size_typwe='1' and mi.mimg_using_type='1' " + " where om.order_id=:orderId ";
		listOrderMall = baseDAO.findSQLMap(map, sql);
		sql = "select oc.session_id sessionId,oc.subcourt_id subcourtId,bc.name,bc.subcourt_type subcourtType,"
				+ "date_format(oc.stdate,'%m月%d日') stdate,date_format(oc.stdate,'%Y-%m-%d') ystdate,oc.week,oc.sttime,oc.endtime,c.address,"
				+ " oc.session_price sessionPrice,oc.discount_price discountPrice,oc.member_price memberPrice,"
				+ " CASE WHEN oc.activity_price IS NOT NULL THEN oc.activity_price WHEN oc.member_price IS NOT NULL THEN oc.member_price "
				+ " WHEN oc.discount_price IS NOT NULL THEN oc.discount_price WHEN oc.fav_price IS NOT NULL THEN oc.fav_price "
				+ " WHEN oc.session_price IS NOT NULL THEN oc.session_price ELSE 0.0 END AS favPrice,"
				+ " oc.activity_price activityPrice,ci.imgurl,cib.imgurl imgurlBig from u_order_court oc "
				+ " left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ " left join u_court c on c.court_id =bc.court_id"
				+ " left join u_br_courtimage ci on ci.subcourt_id=bc.subcourt_id "
				+ " and ci.img_size_type='1' and ci.cimg_using_type='2' "
				+ "left join u_br_courtimage cib on cib.subcourt_id=bc.subcourt_id "
				+ "and cib.img_size_type='1' and cib.cimg_using_type='1'"
				+ " where oc.order_id=:orderId group by oc.order_id order by oc.order_id";
		listOrderCourt = baseDAO.findSQLMap(map, sql);
		if (listOrderCourt != null && listOrderCourt.size() == 0 && !"6".equals(uOrder.getOrderType())) {
			map.put("orderId", uOrder.getPerId());
			HashMap<String, Object> hashMapResp = this.findOrderinfoResp(map);
			UOrder order = (UOrder) hashMapResp.get("success");
			if(order!=null){
				if(order.getUOrderCourt()!=null){
					listOrderCourt = order.getUOrderCourt();// 发起方的球场
				}
				if(order.getUOrderCourt()!=null){
					listOrderMall.addAll(order.getUOrderMall());// AA支付的商品
				}
			}
		}

		// 商品总价
		Double mallPrice = 0.0;
		if (listOrderMall != null && listOrderMall.size() > 0) {
			for (HashMap<String, Object> orderMallMap : listOrderMall) {
				// orderMallMap.put("yprice",
				// df.format(Double.valueOf(orderMallMap.get("yprice").toString())));
				// orderMallMap.put("price",
				// df.format(orderMallMap.get("price")));
				if (null != orderMallMap.get("yprice") && !"".equals(orderMallMap.get("yprice"))
						&& null != orderMallMap.get("count") && !"".equals(orderMallMap.get("count"))) {
					mallPrice = Double.valueOf(orderMallMap.get("yprice").toString())
							* Integer.valueOf(orderMallMap.get("count").toString());
				} else {
					if (null != orderMallMap.get("price") && !"".equals(orderMallMap.get("price"))
							&& null != orderMallMap.get("count") && !"".equals(orderMallMap.get("count"))) {
						mallPrice = Double.valueOf(orderMallMap.get("price").toString())
								* Integer.valueOf(orderMallMap.get("count").toString());
					}
				}

				orderMallMap.put("mallSumPrice", df.format(mallPrice));// 商品总价
				if ("-1".equals(uOrder.getPerId())) {// 若果是发起的订单详情，那么乘以百分比
					if ("3".equals(uOrder.getOrderType())) {// 挑战发起方价格设置为0.0
						orderMallMap.put("mallSumPrice", df.format(mallPrice * paymentRatio));// 商品总价
					}
					if ("2".equals(uOrder.getOrderType())) {// 挑战发起方价格设置为0.0
						orderMallMap.put("mallSumPrice", df.format(0.0));// 商品总价
					}
				}
				orderMallMap.put("productSubtypeName",
						Public_Cache.HASH_PARAMS("product_subtype").get(orderMallMap.get("productSubtype")));
				orderMallMap.put("productDetailtypeName",
						Public_Cache.HASH_PARAMS("product_detailtype").get(orderMallMap.get("productDetailtype")));
			}
		}
		// 球场人数规格
		if (listOrderCourt != null && listOrderCourt.size() > 0) {
			for (HashMap<String, Object> hashMap : listOrderCourt) {
				if (hashMap.get("sessionPrice") != null && !"".equals(hashMap.get("sessionPrice"))) {
					hashMap.put("sessionPrice", df.format(Double.valueOf(hashMap.get("sessionPrice").toString())));
					if ("-1".equals(uOrder.getPerId()) && "1".equals(uOrder.getRelationType())) {// 若果是发起的订单详情，那么乘以百分比
						hashMap.put("sessionPrice",
								df.format(Double.valueOf(hashMap.get("sessionPrice").toString()) * paymentRatio));
					}
				}
				if (hashMap.get("discountPrice") != null && !"".equals(hashMap.get("discountPrice"))) {
					hashMap.put("discountPrice", df.format(Double.valueOf(hashMap.get("discountPrice").toString())));
					if ("-1".equals(uOrder.getPerId()) && "1".equals(uOrder.getRelationType())) {// 若果是发起的订单详情，那么乘以百分比
						hashMap.put("discountPrice",
								df.format(Double.valueOf(hashMap.get("discountPrice").toString()) * paymentRatio));
					}
				}
				if (hashMap.get("memberPrice") != null && !"".equals(hashMap.get("memberPrice"))) {
					hashMap.put("memberPrice", df.format(Double.valueOf(hashMap.get("memberPrice").toString())));
					if ("-1".equals(uOrder.getPerId()) && "1".equals(uOrder.getRelationType())) {// 若果是发起的订单详情，那么乘以百分比
						hashMap.put("memberPrice",
								df.format(Double.valueOf(hashMap.get("memberPrice").toString()) * paymentRatio));
					}
				}
				if (hashMap.get("favPrice") != null && !"".equals(hashMap.get("favPrice"))) {
					hashMap.put("favPrice", df.format(Double.valueOf(hashMap.get("favPrice").toString())));
					if ("-1".equals(uOrder.getPerId()) && "1".equals(uOrder.getRelationType())) {// 若果是发起的订单详情，那么乘以百分比
						hashMap.put("favPrice",
								df.format(Double.valueOf(hashMap.get("favPrice").toString()) * paymentRatio));
					}
				}
				if (hashMap.get("activityPrice") != null && !"".equals(hashMap.get("activityPrice"))) {
					hashMap.put("activityPrice", df.format(Double.valueOf(hashMap.get("activityPrice").toString())));
					if ("-1".equals(uOrder.getPerId()) && "1".equals(uOrder.getRelationType())) {// 若果是发起的订单详情，那么乘以百分比
						hashMap.put("activityPrice",
								df.format(Double.valueOf(hashMap.get("activityPrice").toString()) * paymentRatio));
					}
				}
				if ("-1".equals(uOrder.getPerId()) && "2".equals(uOrder.getOrderType())) {// 挑战发起方价格设置为0.0
					hashMap.put("favPrice", df.format(0.0));// 场次 总价
				}
				hashMap.put("subcourtTypeName",
						Public_Cache.HASH_PARAMS("subcourt_type").get(hashMap.get("subcourtType")));
		        String week="";
                if(hashMap.get("week")!=null){
                    week=hashMap.get("week").toString();
                }
                hashMap.put("week", WebPublicMehod.checkWeekString(week));
			}
		}
		// 加入订单中
		uOrder.setUOrderMall(listOrderMall);
		uOrder.setUOrderCourt(listOrderCourt);
	}

	/**
	 * 
	 * TODO 订单详细数据处理
	 * 
	 * @param uOrder
	 * @throws Exception
	 *             void xiao 2016年3月29日
	 */
	private void displayData(HashMap<String, String> map, UOrder uOrder) throws Exception {
		// 订单支付方式
		// this.getPayTypeStatus(map, uOrder);
		// 退款价格
		UOrderRefund orderRefund = baseDAO.get(UOrderRefund.class, uOrder.getOrderId());
		if (orderRefund != null) {
			uOrder.setRefundPrice(orderRefund.getPrice());
		}
		// 码表值
		uOrder.setOrderTypeName(Public_Cache.HASH_PARAMS("order_type").get(uOrder.getOrderType()));
		if ("1".equals(uOrder.getOrderType())) {
			uOrder.setOrderTypeName("普通订场");
		}
		if ("2".equals(uOrder.getOrderType())) {
			uOrder.setOrderTypeName("擂主战");
		}
		if ("6".equals(uOrder.getOrderType())) {
			uOrder.setOrderTypeName("服务");
		}
		// 订单类型
		uOrder.setOrderstatusName(Public_Cache.HASH_PARAMS("orderstatus").get(uOrder.getOrderstatus()));
		// 支付方式
		uOrder.setDuelPayType(uOrder.getPayTypeStatus());
		uOrder.setDuelPayTypeName(Public_Cache.HASH_PARAMS("duel_pay_type").get(uOrder.getPayTypeStatus()));
		// 订单支付方式
		uOrder.setPaytypeName(Public_Cache.HASH_PARAMS("paytype").get(uOrder.getPaytype()));
		// 关联订单
		List<HashMap<String, Object>> listOrderRelate = new ArrayList<>();
		if ("-1".equals(uOrder.getPerId())) {
			// 查出该用户的子订单
			List<UOrder> list = baseDAO.find(map, "from UOrder where perId=:orderId and UUser.userId=:loginUserId");
			for (UOrder order : list) {
				hashMap = new HashMap<>();
				hashMap.put("orderId", order.getOrderId());
				hashMap.put("ordernum", order.getOrdernum());
				listOrderRelate.add(hashMap);
			}
		} else {
			UOrder order = baseDAO.getHRedis(UOrder.class, uOrder.getPerId(),
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Order"));
			if (order != null) {
				// 响应方关联的主订单，否者普通订单关联的主订单
				if("1".equals(uOrder.getRelationType())){
					if (map.get("loginUserId") != null && order.getUUser() != null
							&& map.get("loginUserId").equals(order.getUUser().getUserId())) {
						hashMap = new HashMap<>();
						hashMap.put("orderId", order.getOrderId());
						hashMap.put("ordernum", order.getOrdernum());
						listOrderRelate.add(hashMap);
					}
				}else{
					hashMap = new HashMap<>();
					hashMap.put("orderId", order.getOrderId());
					hashMap.put("ordernum", order.getOrdernum());
					listOrderRelate.add(hashMap);
				}
			}
		}
		uOrder.setListOrderRelate(listOrderRelate);

	}

	/**
	 * 
	 * TODO 挑战约战支付方式--------后期可能使用
	 * 
	 * @param map
	 * @param uOrder
	 * @throws Exception
	 *             void xiao 2016年4月12日
	 */
	// private void getPayTypeStatus(HashMap<String, String> map, UOrder uOrder)
	// throws Exception {
	// if ("3".equals(uOrder.getOrderType())) {
	// // 约战订单支付方式
	// UDuel duel = baseDAO.get(map, "from UDuel where UOrder.orderId=:orderId
	// ");
	// if (duel != null && duel.getUDuelCh() != null) {
	// uOrder.setDuelPayType(duel.getUDuelCh().getDuelPayType());
	// uOrder.setDuelPayTypeName(
	// Public_Cache.HASH_PARAMS("duel_pay_type").get(duel.getUDuelCh().getDuelPayType()));//
	// // 约战支付方式码表值
	// }
	// }
	// if ("2".equals(uOrder.getOrderType())) {
	// // 约战订单支付方式
	// UChallenge challenge = baseDAO.get(map, "from UChallenge where
	// uorder.orderId=:orderId");
	// if (challenge != null && challenge.getUChallengeCh() != null) {
	// uOrder.setDuelPayType(challenge.getUChallengeCh().getChallengePayType());
	// uOrder.setDuelPayTypeName(Public_Cache.HASH_PARAMS("challenge_pay_type")
	// .get(challenge.getUChallengeCh().getChallengePayType()));//
	// // 约战支付方式码表值
	// }
	// }
	// }

	/**
	 * 
	 * TODO 约战挑战订单数据处理
	 * 
	 * @param params
	 *            listMall-商品集合json、listSesseion-场次集合json、orderType-订单类型（2=挑战、3=
	 *            约战）、paymentRatio-支付比例
	 * @param listMall
	 *            商品集合
	 * @param sessionList
	 *            场次集合
	 * @return
	 * @throws Exception
	 *             boolean xiao 2016年3月31日
	 */
	private boolean buildDate(HashMap<String, String> params, List<UMall> listMall, List<UBrCourtsession> sessionList)
			throws Exception {
		UMall mall = null;
		if (params.get("listmall") != null && !"".equals(params.get("listmall").toString())) {
			// 商品
			JSONArray arraylist = JSONArray.fromObject(params.get("listmall").toString());
			if (arraylist.size() > 0) {
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject object = arraylist.getJSONObject(i);
					String similarId = object.get("similarid").toString();
					String priceType = object.get("pricetype").toString();
					String salecount = object.get("salecount").toString();
					params.put("similarId", similarId);
					params.put("typePrice", priceType);
					mall = baseDAO.get(params, "from UMall where similarId=:similarId and priceType=:typePrice ");
					mall.setSaleCount(Integer.parseInt(salecount));
					listMall.add(mall);
				}
			}
		}
		// 场次
		if (params.get("listSession") != null && !"".equals(params.get("listSession").toString())) {
			JSONArray sessionarraylist = JSONArray.fromObject(params.get("listSession").toString());
			if (sessionarraylist.size() > 0) {
				UBrCourtsession session = null;
				for (int i = 0; i < sessionarraylist.size(); i++) {
					JSONObject object = sessionarraylist.getJSONObject(i);
					// 根据场次Id将场次信息查出并放入到sessionList中
					session = baseDAO.get(UBrCourtsession.class, object.get("sessionid").toString());
					sessionList.add(session);
				}
			}
		}

		// 服务
		if (sessionList != null && sessionList.size() == 1) {
			List<UMall> sessionMallList = new ArrayList<>();
			if (listMall != null && listMall.size() > 0) {
				for (UMall uMall : listMall) {
					// 插入商品、订单记录信息
					params.put("similarId", uMall.getSimilarId());
					params.put("typePrice", uMall.getPriceType());
					mall = baseDAO.get(params, "from UMall where similarId=:similarId and priceType=:typePrice ");
					if ("3".equals(mall.getProductSubtype())) {
						// mall.setSaleCount(uMall.getSaleCount());
						sessionMallList.add(mall);
					}
				}
				sessionList.get(0).setUmallList(sessionMallList);
			}

		}
		// 球场Id和父级球场Id获取
		if (sessionList != null && sessionList.size() > 0) {
			params.put("subcourtId", "");
			if (sessionList.get(0) != null && sessionList.get(0).getUBrCourt() != null) {
				params.put("subcourtId", sessionList.get(0).getUBrCourt().getSubcourtId());
			}
			UBrCourt brCount = baseDAO.get(UBrCourt.class, params.get("subcourtId"));
			if (brCount != null && brCount.getUCourt() != null) {
				params.put("courtId", brCount.getUCourt().getCourtId());
			}
		}
		return true;
	}
	/**
	 * 
	 * TODO 约战挑战订单数据处理
	 * 
	 * @param params
	 *            listMall-商品集合json、listSesseion-场次集合json、orderType-订单类型（2=挑战、3=
	 *            约战）、paymentRatio-支付比例
	 * @param listMall
	 *            商品集合
	 * @param orderCourtList
	 *            球场信息
	 * @return
	 * @throws Exception
	 *             boolean 
	 *             xiao 2016年6月18日
	 */
	private boolean buildDatePureMall(HashMap<String, String> params, List<UMall> listMall, List<UOrderCourt> orderCourtList)
			throws Exception {
		UMall mall = null;
		if (params.get("listmall") != null && !"".equals(params.get("listmall").toString())) {
			// 商品
			JSONArray arraylist = JSONArray.fromObject(params.get("listmall").toString());
			arraylist.removeAll(YHDCollectionUtils.nullCollection());//去空
			if (arraylist.size() > 0) {
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject object = arraylist.getJSONObject(i);
					String similarId = object.get("similarid").toString();
					String priceType = object.get("pricetype").toString();
					String salecount = object.get("salecount").toString();
					params.put("similarId", similarId);
					params.put("typePrice", priceType);
					mall = baseDAO.get(params, "from UMall where similarId=:similarId and priceType=:typePrice ");
					mall.setSaleCount(Integer.parseInt(salecount));
					listMall.add(mall);
				}
			}
		}
		// 场次
		if (params.get("listOrderCounrt") != null && !"".equals(params.get("listOrderCounrt").toString())) {
			JSONArray orderCounrtArraylist = JSONArray.fromObject(params.get("listOrderCounrt").toString());
			orderCounrtArraylist.removeAll(YHDCollectionUtils.nullCollection());//去空
			if (orderCounrtArraylist.size() > 0) {
				UOrderCourt orderCourt = null;
				for (int i = 0; i < orderCounrtArraylist.size(); i++) {
					JSONObject object = orderCounrtArraylist.getJSONObject(i);
					orderCourt=new UOrderCourt();
					if(object.get("subcourtId")!=null){
						orderCourt.setUBrCourt(baseDAO.get(UBrCourt.class,object.getString("subcourtId")));
					}
					orderCourt.setCreateuser(params.get("loginUserId"));
					orderCourt.setStdate(PublicMethod.getStringToDate(object.getString("stdate"), "yyyy-MM-dd"));
					orderCourt.setSttime(object.getString("sttime"));
					orderCourt.setEnddate(PublicMethod.getStringToDate(object.getString("enddate"), "yyyy-MM-dd"));
					orderCourt.setEndtime(object.getString("endtime"));
					orderCourt.setSessionDuration(null);
					orderCourt.setSessionPrice(0.0);
					orderCourt.setDiscountPrice(0.0);
					orderCourt.setMemberPrice(0.0);
					orderCourt.setFavPrice(0.0);
					orderCourt.setActivityPrice(0.0);
					orderCourt.setSessionStatus("1");//1=可用
					orderCourt.setSessionId(null);
					orderCourt.setWeek(this.getWeekOfDate(PublicMethod.getStringToDate(object.getString("stdate"), "yyyy-MM-dd")));
					orderCourt.setSessionUseingStatus("2");//2=球场
					orderCourtList.add(orderCourt);
				}
			}
		}
		
		// 球场Id和父级球场Id获取
		if (orderCourtList != null && orderCourtList.size() > 0) {
			params.put("subcourtId", "");
			if (orderCourtList.get(0) != null && orderCourtList.get(0).getUBrCourt() != null) {
				params.put("subcourtId", orderCourtList.get(0).getUBrCourt().getSubcourtId());
			}
			UBrCourt brCount = baseDAO.get(UBrCourt.class, params.get("subcourtId"));
			if (brCount != null && brCount.getUCourt() != null) {
				params.put("courtId", brCount.getUCourt().getCourtId());
			}
		}
		return true;
	}
	/**
	 * 日期转换星期数字
	 * @param date 日期
	 * @return String 星期
	 */
	private  String getWeekOfDate(Date date) throws Exception{
		
		String[] weekOfDays = { "7", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		
		if (date != null) {
			calendar.setTime(date);
		}
		
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];	
	}

	/**
	 * 
	 * TODO 价格处理-支付比例AA
	 * 
	 * @param map
	 * @param uo
	 *            void xiao 2016年3月29日
	 */
	private void displayOrderPrice(HashMap<String, String> map, UOrder uo) {
		// AA支付价格处理
		// 百分比
		if (null != map.get("paymentRatio") && !"".equals(map.get("paymentRatio"))) {
			uo.setAllprice(uo.getPrice());
			double paymentRatio = Double.parseDouble(map.get("paymentRatio")) * 0.01;
			uo.setPrice(Double.valueOf(df.format(uo.getPrice() * paymentRatio)));
			uo.setPaymentRatio(map.get("paymentRatio"));
			// 如果挑战、约战订单，那么如果他们进入百分比支付，那么认为他们是关联订单，而不是父子订单
			if (uo.getOrderType() != null && ("2".equals(uo.getOrderType()) || "3".equals(uo.getOrderType()))) {
				uo.setRelationType("1");
			}
		}
		
	}

	/**
	 * 
	 * TODO 父级球场获取Id
	 * 
	 * @param map
	 * @throws Exception
	 *             void xiao 2016年3月21日
	 */
	private void getOrderCourtId(HashMap<String, String> map) throws Exception {
		String sql = "select bc.court_id courtId from  u_order_court oc"
				+ " left join u_br_court bc on bc.subcourt_id=oc.subcourt_id "
				+ " where oc.order_id=:orderId group by oc.order_id ";
		List<HashMap<String, Object>> listCount = baseDAO.findSQLMap(map, sql);
		String countId = "";
		if (listCount != null && listCount.size() > 0) {
			countId = (String) listCount.get(0).get("courtId");
		}
		map.put("courtId", countId);
	}

	/**
	 * 
	 * TODO 生成订单部分
	 * 
	 * @param map
	 * @param ubr
	 * @param user
	 * @return
	 * @throws Exception
	 *             UOrder xiao 2016年3月1日
	 */
	private UOrder saveOrderInfo(HashMap<String, String> map, UBrCourt ubr, UUser user) throws Exception {
		UOrder uo = new UOrder();
		uo.setOrderId(WebPublicMehod.getUUID());
		// 下属球场不为空，那么拼接订单标题
		if (ubr != null) {
			uo.setTitle(ubr.getName() + "球场预定");
		}
		uo.setUUser(user);
		uo.setTeamId(map.get("teamId"));//球队ID
		uo.setOrderType(map.get("orderType"));
		uo.setCreatedate(new Date());
		uo.setCreatetime(new Date());
//		uo.setPaydate(new Date());
//		uo.setPaytime(new Date());
		uo.setJpusho("-1");
		uo.setJpushp("-1");
		uo.setDisplayStatus("1");
		uo.setResource("1");
		uo.setOrderstatus("4");// 代支付
		uo.setPerId("-1");
		// map.get("perId")不为空，或者不等于-1，那么订单为子订单，因此title设置为追加订单商品
		if (null != map.get("perId") && !"-1".equals(map.get("perId"))) {
			uo.setPerId(map.get("perId"));
			if ("2".equals(map.get("orderType")) || "3".equals(map.get("orderType"))) {
				uo.setTitle(uo.getTitle() + "-响应订单");
			} else if ("1".equals(map.get("orderType"))) {
				uo.setTitle(uo.getTitle() + "-补款订单");
			} else {
				uo.setTitle(uo.getTitle() + "-追加订单商品");
			}
		}
		uo.setRelationType("2");
		uo.setOrdernum(WebPublicMehod.getOrderNum());
		uo.setPayTypeStatus("1");// 发起方支付
		uo.setPaymentRatio("100");
		if ("2".equals(map.get("orderType"))) {
			uo.setPayTypeStatus("2");// 线上AA
		}
		if ("3".equals(map.get("orderType"))&&map.get("paymentRatio")!=null&&!"100".equals(map.get("paymentRatio"))&&!"100.0".equals(map.get("paymentRatio"))) {
			uo.setPayTypeStatus("2");// 线上AA
		}
		// 过期时间
		String outdate = PublicMethod.getLastDate(Public_Cache.ORDER_TIME);
		uo.setOutdate(PublicMethod.getStringToDate(outdate.substring(0, 10), "yyyy-MM-dd"));
		uo.setOuttime(PublicMethod.getStringToDate(outdate.substring(11), "HH:mm:ss"));
		baseDAO.save(uo);
		// // 发起挑战不走该通知
		// if (null == map.get("fchallenge")) {
		// map.put("mes_type", "orWaitPay");// 订单待支付
		// this.orderMessage(map, uo.getOrderId(), user.getUserId(), ubr);
		// // 推送
		// this.orderPush(map, uo, "6");
		// }
		return uo;

	}

	/**
	 * 
	 * 
	 * TODO - 判断过订单生成时间超过30分钟
	 * 
	 * @param uOrder
	 * @return 2015年12月25日 dengqiuru
	 * @throws ParseException
	 */
	private boolean isExpired(Date createdate,Date createtime, int orderTime) throws ParseException {
		boolean isExpired = false;
		if (null != createdate && null != createtime) {
			String ordercreate = PublicMethod.getDateToString(createdate, "yyyy-MM-dd") + " "
					+ PublicMethod.getDateToString(createtime, "HH:mm:ss");// 将订单生成日期和时间拼接
			Date orderCreateDT = PublicMethod.getStringToDate(ordercreate, "yyyy-MM-dd HH:mm:ss");// 将字符串类型转成时间类型
			Long createCodeT = (new Date().getTime() - orderCreateDT.getTime()) / (1000 * 60);
			// 如果大于30分钟，那么返回true,即修改订单状态，否者不修改
			if (createCodeT > orderTime) {
				isExpired = true;
			} else {
				isExpired = false;
			}
		} else {
			isExpired = false;
		}
		return isExpired;
	}

	/**
	 * 
	 * TODO 订单记录信息的copy、库存的减少
	 * 
	 * @param listMall
	 * @param uo
	 * @param map
	 * @return
	 * @throws Exception
	 *             HashMap<String,Object> xiao 2016年3月1日
	 */
	private HashMap<String, Object> copyMallInfo(List<UMall> listMall, UOrder uo, HashMap<String, String> map)
			throws Exception {
		double price = 0.0;
		if (listMall != null && listMall.size() > 0) {
			listMall.removeAll(YHDCollectionUtils.nullCollection());
			UMall um = null;
			UOrderMall uom = null;
			// 减少预定商品库存,copy商品信息
			for (UMall vm : listMall) {
				if (!"3".equals(vm.getProductSubtype())) {// 去除服务商品,如果是追加
					if (0 == vm.getSaleCount()) {
						return WebPublicMehod.returnRet("error", "购买商品数量不可为0");
					}
					// 插入商品、订单记录信息
					um = baseDAO.get(UMall.class, vm.getProId());
					uom = new UOrderMall();
					BeanUtils.copyProperties(um, uom);
					uom.setPkId(WebPublicMehod.getUUID());
					uom.setUOrder(uo);
					uom.setCount(vm.getSaleCount());
					uom.setProPId(um.getProId());
					baseDAO.save(uom);
					price += (um.getPrice() * vm.getSaleCount());
				}
			}

		}
		price = Double.valueOf(df.format(price));
		return WebPublicMehod.returnRet("price", price);
	}

	/**
	 * 
	 * TODO 追加的商品信息
	 * 
	 * @param listMall
	 *            商品集合
	 * @param uo
	 *            订单
	 * @param map
	 * @return
	 * @throws Exception
	 *             HashMap<String,Object> xiao 2016年5月4日
	 */
	private HashMap<String, Object> copyAddOrderMallInfo(List<UMall> listMall, UOrder uo, HashMap<String, String> map)
			throws Exception {
		double price = 0.0;
		if (listMall != null && listMall.size() > 0) {
			listMall.removeAll(YHDCollectionUtils.nullCollection());
			UMall um = null;
			UOrderMall uom = null;
			// 减少预定商品库存,copy商品信息
			for (UMall vm : listMall) {
				if (0 == vm.getSaleCount()) {
					return WebPublicMehod.returnRet("error", "购买商品数量不可为0");
				}
				// 插入商品、订单记录信息
				um = baseDAO.get(UMall.class, vm.getProId());
				uom = new UOrderMall();
				BeanUtils.copyProperties(um, uom);
				uom.setPkId(WebPublicMehod.getUUID());
				uom.setUOrder(uo);
				uom.setCount(vm.getSaleCount());
				uom.setProPId(um.getProId());
				baseDAO.save(uom);
				price += (um.getPrice() * vm.getSaleCount());
			}

		}
		price = Double.valueOf(df.format(price));
		return WebPublicMehod.returnRet("price", price);
	}

	/**
	 * copy场次订单记录信息、商品订单记录信息
	 * 
	 * @param type
	 * @param obj
	 * @throws Exception
	 */
	private HashMap<String, Object> copyInfo(List<UBrCourtsession> obj, UOrder order, HashMap<String, String> map)
			throws Exception {
		double price = 0.0;
		hashMap = new HashMap<>();
		// copy场次订单记录信息
		if (null != obj) {
			UBrCourtsession ubcs = null;
			UOrderCourt ucc = null;
			UMall um = null;
			UOrderMall uom = null;
			UMallCourt umc = null;
			for (UBrCourtsession v : obj) {

				// 插入场次记录信息
				ubcs = baseDAO.get(UBrCourtsession.class, v.getSessionId());
				ucc = new UOrderCourt();
				BeanUtils.copyProperties(ubcs, ucc);
				ucc.setOrderCourtId(WebPublicMehod.getUUID());
				ucc.setUOrder(order);
				baseDAO.save(ucc);
				// 修改场次预定状态
				ubcs.setOrderStatus("3");// 预定未付款
				ubcs.setOrderId(order.getOrderId());
				baseDAO.update(ubcs);

				// 场次取的价格
				price += uCourtService.getFavPrice(v.getSessionId(), true);
				// // 如果优惠价存在，那么去优惠价，否者取球场原价
				// if (ucc.getFavPrice() != null &&
				// !"".equals(ucc.getFavPrice())) {
				// price += ucc.getFavPrice();// 场次价格
				// } else {
				// price += ucc.getSessionPrice();// 场次价格
				// }

				if (PublicMethod.checkNull(v.getUmallList())) {
					for (UMall vm : v.getUmallList()) {
						// 插入商品、订单记录信息
						// map.put("similarId", vm.getSimilarId());
						// map.put("typePrice", vm.getPriceType());
						um = baseDAO.get(UMall.class, vm.getProId());
						// um = baseDAO.get(map, "from UMall where
						// similarId=:similarId and priceType=:typePrice ");
						uom = new UOrderMall();
						BeanUtils.copyProperties(um, uom);
						uom.setPkId(WebPublicMehod.getUUID());
						uom.setUOrder(order);
						uom.setCount(vm.getSaleCount());
						uom.setProPId(um.getProId());
						uom.setSessionId(v.getSessionId());
						baseDAO.save(uom);
						// 商品价格
						price += (um.getPrice() * vm.getSaleCount());
						// 判断商品是否属于球场
						map.put("mallId", vm.getProId());
						umc = uMallService.getUMallCourt(map);
						if (null != umc) { // 更新球场商品库存
							// if (checkCourtCount(umc.getCourtCount(), um, 1) >
							// 0) {
							umc.setCourtCount(umc.getCourtCount() - vm.getSaleCount());
							// }
							baseDAO.update(umc);
						}
					}

				}

			}
		}
		price = Double.valueOf(df.format(price));
		hashMap.put("price", price);
		return hashMap;
	}
	/**
	 * copy场次订单记录信息、商品订单记录信息
	 * 
	 * @param type
	 * @param obj
	 * @throws Exception
	 */
	private HashMap<String, Object> copyInfoPureMall(List<UOrderCourt> obj, UOrder order, HashMap<String, String> map)
			throws Exception {
		double price = 0.0;
		hashMap = new HashMap<>();
		// copy场次订单记录信息
		if (null != obj) {
			UOrderCourt ucc = null;
			for (UOrderCourt v : obj) {
				// 插入场次记录信息
				ucc = new UOrderCourt();
				BeanUtils.copyProperties(v, ucc);
				ucc.setOrderCourtId(WebPublicMehod.getUUID());
				ucc.setUOrder(order);
				baseDAO.save(ucc);

				// 场次取的价格
				price += ucc.getSessionPrice();
			}
		}
		price = Double.valueOf(df.format(price));
		hashMap.put("price", price);
		return hashMap;
	}

	/**
	 * 
	 * TODO 判断预定球场场次与其他球场场次是否起冲突
	 * 
	 * @param map same－冲突后是否继续标识
	 * @param hashMap loginUserId－登录人ID 
	 * @param listBrCourtsession 相同时间段球场集合
	 * @param listError 错误集合
	 * @param uBrCourtsession 
	 * @return
	 * @throws Exception
	 *             List<String> xiao 2016年2月29日
	 */
	private List<String> ifSameDate(HashMap<String, String> map, HashMap<String, Object> hashMap,
			List<UBrCourtsession> listBrCourtsession, List<String> listError)
					throws Exception {
		// 是否进入订单冲突的判断，1进入，其他不进入
		if (null == map.get("same") || "".equals(map.get("same"))) {
			// 是否存在预定了相同时间段的球场
			listBrCourtsession = baseDAO.find(
					"from UBrCourtsession b where b.orderId in(select orderId from UOrder where  UUser.userId=:loginUserId and orderstatus='1') and  b.stdate=:stdate and b.sttime=:sttime and b.endtime=:endtime ",
					hashMap);
			if (listBrCourtsession.size() > 0) {
				for (UBrCourtsession brCourtsession : listBrCourtsession) {
					listError.add("你已经在" + brCourtsession.getUBrCourt().getName() + "预定了" + brCourtsession.getStdate()
							+ " " + brCourtsession.getSttime() + "-" + brCourtsession.getEndtime() + " 与当前所选时间冲突！\n是否继续？");
				}
			}
		}
		return listError;

	}
	/**
	 * 
	 * TODO 升级提示[2.0.2]   
	 * @param map appCode-版本号，teamId-球队ID
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月28日
	 */
	private HashMap<String, Object> upgradePrompt(HashMap<String, String> map) throws Exception{
//		if(map.get("orderType") != null && ("3".equals(map.get("orderType"))||"2".equals(map.get("orderType")))){
			if(map.get("appCode")!=null&&!"".equals(map.get("appCode"))){
				if(map.get("teamId")==null||"".equals(map.get("teamId"))){
					return WebPublicMehod.returnRet("error", "请选择球队！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本！");
			}
//		}
		return null;
	}

	/**
	 * 验证库存大小 ---------后期可能使用
	 * 
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private int checkCourtCount(int count, UMall um, int type) throws Exception {
		if (type == 1) {
			if (count >= 0) {
				return count;
			} else {
				throw new Exception(um.getName() + "库存不足");
			}
		} else if (type == 2) {
			if (count > 0) {
				return count;
			} else {
				throw new Exception(um.getName() + "库存不足");
			}
		}
		return 0;
	}

	/**
	 * 验证库存大小---------后期可能使用
	 * 
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private int checkCourtCount(int count, UMall um) throws Exception {
		if (count >= 0) {
			return count;
		} else {
			throw new Exception(um.getName() + "库存不足");
		}
	}

	/**
	 * 
	 * TODO 获取订单类型，划分球场和服务，默认为球场
	 * 
	 * @param map
	 *            orderTypePage-订单类型（页面上的，即球场、服务）
	 * @return String xiao 2016年2月25日
	 */
	private String getOrderTypeInfo(HashMap<String, String> map) {
		if (null != map.get("orderType") && !"".equals(map.get("orderType"))) {
			map.put("orderTypePage", "2");// 默认为服务
			// 如果订单类型是1、2、3，那么就是球场
			if ("1".equals(map.get("orderType")) || "2".equals(map.get("orderType"))
					|| "3".equals(map.get("orderType"))) {
				map.put("orderTypePage", "1");
			}

		}
		// 订单类型-球场
		String orderType = Public_Cache.ORDER_TYPE_COURT;
		if ("2".equals(map.get("orderTypePage"))) {// 订单类型-服务
			orderType = Public_Cache.ORDER_TYPE_SERVICE;
		}
		return orderType;
	}
	

	/**
	 * 
	 * TODO 订单支付成功后，更新库存
	 * 
	 * @param listOrderMall
	 *            订单下的商品预定
	 * @param mall
	 *            商品对象
	 * @param uOrder
	 *            订单对象
	 * @param map
	 *            courtId-球场Id、mallId-商品iId
	 * @throws Exception
	 *             void xiao 2016年3月1日
	 */
	private void updateCount(List<UOrderMall> listOrderMall, UMall mall, UOrder uOrder, HashMap<String, String> map,
			UMallCourt mallCourt) throws Exception {
		this.getOrderCourtId(map);
		for (UOrderMall orderMall : listOrderMall) {
			map.put("mallId", orderMall.getProPId());
			// 判断商品是否属于球场
			mallCourt = baseDAO.get(map, "from UMallCourt where courtId = :courtId and UMall.proId = :mallId");
			if (null != mallCourt) { // 更新球场商品库存
				mallCourt.setCourtCount(mallCourt.getCourtCount() - orderMall.getCount());
				baseDAO.update(mallCourt);
			}
			// 减少商品库存
			mall = baseDAO.get(UMall.class, orderMall.getProPId());
			// 更新通用商城商品库存
			uMallService.updateMallCount(map, mall, orderMall.getCount(), 1);
		}
	}

	@Override
	public String savePayOrderStatus(String ordernum, String attach,String payTpe) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ordernum", ordernum);
		UOrder uOrder = baseDAO.get(map, "from UOrder where ordernum = :ordernum ");
		// 扣除库存
		UMall mall = null;
		UMallCourt mallCourt = null;
		if (uOrder != null) {
			boolean isExpired = isExpired(uOrder.getCancedate(),uOrder.getCancetime(), Public_Cache.ORDER_TIME);
			if (uOrder.getOrderstatus().equals("4")||isExpired) {// 为待支付时进入
				map.put("orderId", uOrder.getOrderId());
				List<UOrderMall> listOrderMall = baseDAO.find(map, "from UOrderMall where UOrder.orderId = :orderId ");
				uOrder.setOrderstatus("1");
				uOrder.setPaytype(payTpe);// 支付类型
				uOrder.setPaydate(new Date());
				uOrder.setPaytime(new Date());
				baseDAO.save(uOrder);
				this.updateCount(listOrderMall, mall, uOrder, map, mallCourt);// 修改库存
				//补款处理
				if("3".equals(uOrder.getRelationType())){
					UOrder uoo = baseDAO.get(UOrder.class, uOrder.getPerId());// 主订单查询
					uoo.setOrderType("1");//球场类型
					baseDAO.getSessionFactory().getCurrentSession().flush();
					this.removeRedis(map, uoo);// 更改主订单数据的缓存
					map.put("orderid",uoo.getOrderId());
					uduelServiceImpl.addDuelAgain(map);//取消约战
				}

				uCourtService.saveCourtSessionOrderStatus(uOrder.getOrderId());
				uduelServiceImpl.respDuelPayCallBack(map);
				uchallengeService.respChallengePayCallBack(map);
				if (uOrder.getUUser() != null) {
					map.put("loginUserId", uOrder.getUUser().getUserId());
				}
				map.put("mes_type", "orPay");// 订单支付
				this.orderMessage(map, uOrder.getOrderId(), uOrder.getUUser().getUserId(), null);
				// 推送
				this.orderPush(map, uOrder, "7");
				baseDAO.getSessionFactory().getCurrentSession().flush();
				this.removeRedis(map, uOrder);
			}
			return "success";
		} else {
			return "此金额的订单不存在！";
		}
	}

	@Override
	public HashMap<String, Object> PaySuccessOrder(HashMap<String, String> map) throws Exception {
		if (map.get("ordernum") != null && !"".equals(map.get("ordernum"))) {
			UOrder uOrder = baseDAO.get(map, "from UOrder where ordernum = :ordernum ");
			if (uOrder != null) {
				hashMap.put("orderId", uOrder.getOrderId());
				hashMap.put("ordernum", uOrder.getOrdernum());
				hashMap.put("orderstatus", uOrder.getOrderstatus());
				hashMap.put("orderstatusName",

						Public_Cache.HASH_PARAMS("orderstatus").get(uOrder.getOrderstatus()));
				return hashMap;
			}
			return WebPublicMehod.returnRet("error", "订单不存在");
		}
		return WebPublicMehod.returnRet("error", "订单不存在");
	}
	/**
	 * 
	 * TODO 订单过期通知
	 * @param map
	 * @param uOrder
	 * void
	 * xiao
	 * 2016年5月4日
	 */
	private void orderExpired(HashMap<String, String> map, UOrder uOrder) throws Exception{
		if ("-1".equals(uOrder.getPerId())) { // 主订单取消
			// 空城-主订单才会关联球场
			uCourtService.updateCourtSession(uOrder.getOrderId());
		}
		map.put("loginUserId", "");
		if (uOrder.getUUser() != null) {
			map.put("loginUserId", uOrder.getUUser().getUserId());
		}
		map.put("mes_type", "orTimeOver");// 过期通知
		this.orderMessage(map, uOrder.getOrderId(), map.get("loginUserId"), null);

		// 推送
		this.orderPush(map, uOrder, "1");

		baseDAO.getSessionFactory().getCurrentSession().flush();
		this.removeRedis(map, uOrder);// 清空修改的缓存
	}

	/**
	 * 
	 * TODO 极关推送
	 * 
	 * @param map
	 *            type 事件类型、event 具体事件、object_id 事件Id、push_type 推送类型、push_status
	 *            推送状态
	 * @param uOrder
	 *            订单对象
	 * @param type
	 *            事件类型
	 * @param event
	 *            具体事件
	 * @throws Exception
	 *             void xiao 2016年4月15日
	 */
	private void orderPush(HashMap<String, String> map, UOrder uOrder, String event) throws Exception {
		// 生成推送记录-极光推送
		if (!"7".equals(event)) {
			map.put("type", "1");
			map.put("event", event);
			map.put("object_id", uOrder.getOrderId());
			map.put("push_type", "1");// push_type 推送类型 1.手机 2.推送 3.邮箱
			map.put("push_status", "-1");
			if (publicPush.getPushStatus(map)) {
				publicPush.addPushMessage(map);// 生成推送记录
				// 极光推送
				UEquipment equipment = null;
				if (uOrder.getUUser() != null && uOrder.getUUser().getNumberid() != null
						&& !"".equals(uOrder.getUUser().getNumberid())) {
					equipment = baseDAO.get(UEquipment.class, uOrder.getUUser().getNumberid());
					if (equipment != null) {
						map.put("code", equipment.getCode());
					}
				}
				map.put("content", map.get("contentName"));
				map.put("orderId", uOrder.getOrderId());
				map.put("jump", "b06");
				publicPush.publicAppPush(map);
			}
		}
		// 生成推送记录-短信推送
		map.put("type", "1");
		map.put("event", event);
		map.put("object_id", uOrder.getOrderId());
		map.put("push_type", "2");// push_type 推送类型 1.手机 2.推送 3.邮箱
		map.put("push_status", "-1");
		if (publicPush.getPushStatus(map)) {
			publicPush.addPushMessage(map);// 生成推送记录
			// 短信推送
			if (uOrder.getUUser() != null && uOrder.getUUser().getPhone() != null) {
				map.put("phone", uOrder.getUUser().getPhone());
			}
			map.put("product", map.get("contentName"));
			publicPush.publicSendMessage(map);
		}
	}

	/**
	 * 
	 * TODO 订单通知的设置-应用内
	 * 
	 * @param map
	 * @param userId
	 *            void xiao 2016年4月9日
	 */
	private void orderMessage(HashMap<String, String> map, String orderId, String userId, UBrCourt ubr)
			throws Exception {
		map.put("orderId", orderId);
		if (ubr == null) {
			ubr = this.getOrderBrCourt(map);
		}
		String contentName = "";
		if (ubr != null) {
			contentName = ubr.getName();
		}
		// 通知
		map.put("type", "order");
		map.put("contentName", contentName);
		map.put("params", "{\"jump\":\"b06\",\"orderId\":\"" + orderId + "\"}");
		map.put("userId", userId);
		messageService.addTheMessageByType(map);
	}
	/**
	 * 
	 * TODO 删除订单后-数据处理(空场、删除缓存、更改订单状态)--注：侧滑删除有调用，不能随意修改
	 * @param map
	 * @param order
	 * void
	 * xiao
	 * 2016年6月6日
	 */
	private void handlingOrder(HashMap<String, String> map, UOrder order) throws Exception{
		// 待定和待支付才能取消
		if ("4".equals(order.getOrderstatus()) || "3".equals(order.getOrderstatus())) {
			if ("-1".equals(order.getPerId())) { // 主订单取消
				if ("2".equals(order.getRelationType())) {
					// 空城-主订单才会关联球场
					uCourtService.updateCourtSession(order.getOrderId());
					hashMap = PublicMethod.Maps_Mapo(map);
					baseDAO.executeHql("update UOrder set orderStatus ='5' where perId =:orderId", hashMap);// 所有子订单同时取消
					// 删除主订单缓存
					List<UOrder> listOrder = baseDAO.find("from UOrder where perId =:orderId", hashMap);
					baseDAO.getSessionFactory().getCurrentSession().flush();
					if (listOrder != null && listOrder.size() > 0) {
						for (UOrder uOrder : listOrder) {
							this.removeRedis(map, uOrder);// 删除查询详情里已经变更的缓存
						}
					}
				}
			}
			order.setOrderstatus("5");
		}

		order.setDisplayStatus("-1");
		baseDAO.update(order);
		baseDAO.getSessionFactory().getCurrentSession().flush();
		this.removeDelRedis(map, order);
	}

}
