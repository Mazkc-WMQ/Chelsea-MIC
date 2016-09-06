package upbox.xiao.test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.org.pub.PublicMethod;
import com.sun.javafx.collections.MappingChange.Map;

import upbox.model.UBrCourt;
import upbox.model.UBrCourtsession;
import upbox.model.UMall;
import upbox.model.UOrderCourt;
import upbox.service.FilterService;
import upbox.service.UCourtService;
import upbox.service.UOrderService;

@ContextConfiguration("applicationContext.xml")
public class UOrderServiceImplTest  extends AbstractJUnit4SpringContextTests{
	@Resource
	private UOrderService uorderService;
	@Resource
	private FilterService filterService;
	@Resource
	private UCourtService  ucourtService;
	private HashMap<String, Object> hashMap;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 订单生成日期转换

	DecimalFormat df = new DecimalFormat("######0.0");
	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	 */
	public void printInfo(String temp,Object obj){
		//System.out.println(temp + JSON.toJSONString(obj));
	}
	@Test
	public void findOrderinfo() throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderId", "6cbfc98d-0178-4858-bbfa-b714f41e02e9");
		hashMap = uorderService.findOrderinfo(map);
		printInfo("------------------------------",map);
	}
	@Test
	public void findOrderinfoDuelAndDek() throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderId", "c1720705-aa12-4827-baef-9a54ec08d37a");
//		List<HashMap<String, Object>> list = uorderService.findOrderinfoDuelAndDek(map);
//		printInfo("---",list);
	}
	@Test
	public void findOrderinfo1() throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("loginUserId", "91d93ca0-fcd3-4ec0-ac02-9e7f79baa866");
//		map.put("userId", "226311d3-5572-4036-b013-ed469aa49034");
		map.put("page", "1");
		map.put("orderTypePage", "1");
		//map.put("orderstatus","");
		hashMap = uorderService.findOrderlist(map);
		printInfo("---------------1---------------",map);
	}
	@Test
	public void uOrderExpired() throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userId", "772b7130-bc4d-41f1-814d-e3cac61acce5");
////		map.put("userId", "226311d3-5572-4036-b013-ed469aa49034");
//		map.put("page", "1");
//		map.put("orderType", "1");
//		map.put("orderstatus","");
		hashMap = uorderService.uOrderExpired(map);
		printInfo("---------------1---------------",map);
	}
	
	@Test
	public void savePayOrderStatus() throws Exception{
		String orderId="201603312144119399";
		String price="2124";
		String pay = uorderService.savePayOrderStatus(orderId, price,"1");
		printInfo("---------------1---------------",pay);
	}
	
	
	@Test
	public void saveDuelInfoOrder()  throws Exception{
//		sessionList=new ArrayList<>();
//		UBrCourtsession session=new UBrCourtsession();
//		session.setStdate(PublicMethod.getStringToDate("2016-03-10", "yyyy-MM-dd"));
//		session.setSttime("12:00");
//		session.setEndtime("14:00");
//		session.setSessionId("6e7a16d3-794c-4691-a6d2-7fd896e19914");
//		session.setIsOrder("1");
//		
////		session.setSttime("18:00");
////		session.setEndtime("20:00");
////		session.setSessionId("0d25f0ed-40a6-419d-9d99-146ae432633c");
////		session.setIsOrder("1");
//		
////		session.setSttime("16:00");
////		session.setEndtime("18:00");
////		session.setSessionId("8911a9c0-5876-4b99-b2c3-74207773705c");
////		session.setIsOrder("1");
//		
//		List<UMall> mallList=new ArrayList<>();
//		UMall mall=new UMall();
//		mall.setSaleCount(4);
//		mall.setProId("e894d91d-4c9c-48d5-b97e-0c1166ad1c39");
//		mallList.add(mall);
//		session.setUmallList(mallList);
//		
//		sessionList.add(session);
//		
//		
//		UBrCourtsession session1=new UBrCourtsession();
//
//		session.setStdate(PublicMethod.getStringToDate("2016-03-10", "yyyy-MM-dd"));
////		session1.setSttime("16:00");
////		session1.setEndtime("18:00");
////		session1.setSessionId("8911a9c0-5876-4b99-b2c3-74207773705c");
////		session1.setIsOrder("1");
//		
////		session1.setSttime("14:00");
////		session1.setEndtime("16:00");
////		session1.setSessionId("b7dd3748-5675-47f7-84a7-5679a413269c");
////		session1.setIsOrder("1");
//		
//		session1.setSttime("10:00");
//		session1.setEndtime("12:00");
//		session1.setSessionId("243a97de-0a05-4a79-ad56-a090e1b90e8d");
//		session1.setIsOrder("1");
//		
//		sessionList.add(session1);
		
//		UOrder order=new UOrder();
//		order.setOrderType("1");
//		order.setPrice(500.0);
//		order.setPaytype("3");
//		order.setResource("1");
//		order.setTitle("title");
		
		HashMap<String, String> map=new HashMap<>();
		map.put("courtId", "fe8545d8-2002-4668-96cb-57c5c3f0a73f");
		map.put("subcourtId", "e00ded79-9fa7-4188-bbf9-e65f3d782d5d");
		map.put("orderType", "1");
		map.put("resource", "1");
		map.put("price", "500");
		map.put("paymentRatio", "50");
		map.put("loginUserId", "fbf1fd43-87c7-474c-9505-595011bcb469");
		List<UBrCourtsession> sessionList=new ArrayList<>();
		
		UBrCourtsession session=new UBrCourtsession();
		session.setStdate(sdf.parse("2016-06-18"));
		session.setSttime("12:00");
		session.setEndtime("14:00");
		session.setSessionId("04434869-8553-4745-8ddf-c98457dc67fb");
		session.setIsOrder("1");
		
//		session.setSttime("18:00");
//		session.setEndtime("20:00");
//		session.setSessionId("0d25f0ed-40a6-419d-9d99-146ae432633c");
//		session.setIsOrder("1");
		
//		session.setSttime("16:00");
//		session.setEndtime("18:00");
//		session.setSessionId("8911a9c0-5876-4b99-b2c3-74207773705c");
//		session.setIsOrder("1");
		
//		List<UMall> mallList=new ArrayList<>();
//		UMall mall=new UMall();
//		mall.setSaleCount(2);
//		mall.setProId("00c866aa-862d-456e-bb57-a339d06facd8");
//		mall.setSimilarId("b8c60bdc-b77a-43f7-8211-a7db58c70723");
//		mall.setPriceType("2");
//		mallList.add(mall);
//		session.setUmallList(mallList);
		
		sessionList.add(session);
		
		
//		UBrCourtsession session1=new UBrCourtsession();
//		
//		session1.setStdate(sdf.parse("2016-03-30"));
//		session1.setSttime("16:00");
//		session1.setEndtime("18:00");
//		session1.setSessionId("8911a9c0-5876-4b99-b2c3-74207773705c");
//		session1.setIsOrder("1");
		
//		session1.setSttime("14:00");
//		session1.setEndtime("16:00");
//		session1.setSessionId("b7dd3748-5675-47f7-84a7-5679a413269c");
//		session1.setIsOrder("1");
		
//		session1.setSttime("10:00");
//		session1.setEndtime("12:00");
//		session1.setSessionId("7b8f2ec8-9016-4b3b-8e14-a616cbb38f07");
//		session1.setIsOrder("1");
//		
//		sessionList.add(session1);
//		
//		mallList=new ArrayList<>();
//		UMall mall1=new UMall();
//		mall1.setSaleCount(2);
//		mall1.setProId("00c866aa-862d-456e-bb57-a339d06facd8");
//		mall1.setSimilarId("678e40a6-65cd-46c4-8fc7-0c3b61f43cb5");
//		mall1.setPriceType("1");
//		mallList.add(mall1);
		hashMap=uorderService.saveOrder(map, sessionList,null);
		//System.out.println("hashMap");
			
	}
	@Test
	public void saveAddOrder()  throws Exception{
//		HashMap<String, String> map=new HashMap<>();
//		map.put("orderType", "6");
//		map.put("resource", "1");
//		map.put("price", "5");
//		map.put("orderId", "2fba5399-041c-4d61-bd38-7fb049b6339a");
//		List<UMall> listMall=new ArrayList<>();
//		UMall mall=new UMall();
//		mall.setSaleCount(4);
//		mall.setProId("90c89f55-c7e6-4445-b261-f495fa59799c");
//		listMall.add(mall);
//		
//		hashMap=uorderService.saveAddOrder(map, listMall);
		
		HashMap<String, String> map=new HashMap<>();
		map.put("orderType", "6");
		map.put("resource", "1");
		map.put("orderId", "24230ed9-2959-4741-82ca-e4a497660176");
		List<UMall> listMall=new ArrayList<>();
		UMall mall=new UMall();
		mall.setSaleCount(4);
		mall.setProId("062e74d7-42bd-4577-9f9e-b42bde970ed4");
		listMall.add(mall);
		
		hashMap=uorderService.saveAddOrder(map, listMall);
			
	}
	@Test
	public void displayOrder()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
		map.put("orderId", "6bfe4285-f964-4595-a692-b81959a68118");
		hashMap=uorderService.displayOrder(map);
			
	}
	@Test
	public void cancelOrder()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
		map.put("orderId", "072eb426-e852-4236-9f85-a2316d5c028c");
		hashMap=uorderService.cancelOrder(map);
			
	}
	@Test
	public void findFilterTeam()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
		map.put("orderId", "6bfe4285-f964-4595-a692-b81959a68118");
		List<String> chancesList=new ArrayList<>();
		chancesList.add("0-5");
		chancesList.add("5-10");
		
//		HashMap<String, Object> hashMap=filterService.findFilterTeam(map, chancesList, null, null, null, null);
		//System.out.println(hashMap);
			
	}
	@Test
	public void savePureMallOrder()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
		List<UMall> listMall=new ArrayList<>();
		UMall mall=new UMall();
		mall.setSaleCount(2);
		mall.setSimilarId("678e40a6-65cd-46c4-8fc7-0c3b61f43cb5");
		mall.setPriceType("1");
		mall.setProId("0365f30c-ef12-494f-9f97-c6b16a011217");
		listMall.add(mall);
		List<UOrderCourt> listOrderCourt=new ArrayList<>();
		UOrderCourt orderCourt=new UOrderCourt();
		UBrCourt brCourt =new UBrCourt();
		brCourt.setSubcourtId("04d6e40b-0cc0-43fc-a3b5-9b7e336b0cfa");
		orderCourt.setUBrCourt(brCourt);
		orderCourt.setCreateuser("0f1a74cc-c119-4e1e-b12e-d277b8606309");
		orderCourt.setStdate(PublicMethod.getStringToDate("2016-07-11", "yyyy-MM-dd"));
		orderCourt.setSttime("10:00");
		orderCourt.setEnddate(PublicMethod.getStringToDate("2016-07-11", "yyyy-MM-dd"));
		orderCourt.setEndtime("12:00");
		orderCourt.setSessionDuration(null);
		orderCourt.setSessionPrice(900.0);
		orderCourt.setDiscountPrice(null);
		orderCourt.setMemberPrice(null);
		orderCourt.setFavPrice(null);
		orderCourt.setActivityPrice(null);
		orderCourt.setSessionStatus("1");//1=可用
		orderCourt.setSessionId(null);
		orderCourt.setWeek("1");
		orderCourt.setSessionUseingStatus("2");//2=球场
		listOrderCourt.add(orderCourt);
		map.put("token", "c45f1045-903c-4baa-be0c-5c2b17c7e13f");
		map.put("subcourtId", "04d6e40b-0cc0-43fc-a3b5-9b7e336b0cfa");
		map.put("loginUserId", "fbf1fd43-87c7-474c-9505-595011bcb469");
		hashMap=uorderService.savePureMallOrder(map, listOrderCourt, null);
		
//		HashMap<String, Object> hashMap=filterService.findFilterTeam(map, chancesList, null, null, null, null);
		//System.out.println(hashMap);
			
	}
	@Test
	public void test1()  throws Exception{
		ucourtService.saveCourtSessionOrderStatus("test");
	}
	@Test
	public void test()  throws Exception{
//		Date newDate=new Date();
//		Date orderCreateDT = PublicMethod.getStringToDate("2016-03-07 10:27:50", "yyyy-MM-dd HH:mm:ss");
//		//System.out.println(newDate.getTime()+"-=-="+orderCreateDT.getTime());
//		Long createCodeT = (newDate.getTime() - orderCreateDT.getTime()) / (1000 * 60);
//		//System.out.println(createCodeT);
//		//System.out.println(h.get("a"));
//		double a=Double.valueOf(map.get("a"));
//		double b=50;
//		double c=100;
//		double d=a/100;
//		//System.out.println(a+"---"+d);
		String current = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd hh:mm");
		//过期 当前时间和预订时间比较
		int ret = current.compareTo("2016-07-18 11:00");
		  
	System.out.println(ret);
		
			
	}

}
