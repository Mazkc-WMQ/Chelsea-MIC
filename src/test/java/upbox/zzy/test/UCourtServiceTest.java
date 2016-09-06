package upbox.zzy.test;

import com.alibaba.fastjson.JSON;
import com.org.pub.PublicMethod;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import upbox.model.UCourt;
import upbox.service.UCourtService;
import upbox.service.UDuelService;

/**
 * 
 * @author zzy
 *
 * 15252481515
 */

@ContextConfiguration("/applicationContext.xml")
public class UCourtServiceTest  extends AbstractJUnit4SpringContextTests{
	
	@Resource
	private UCourtService uCourtService;
	@Resource
	private UDuelService uDuelService;
	private HashMap<String, Object> ret;
	
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
		System.out.println(temp + JSON.toJSONString(obj));
	}
	
	@Test
	public void testUCourtQueryCourtList() throws Exception {
		
		HashMap<String,String> params = new HashMap<String,String>();
		
		params.put("page", null);
		params.put("userStatus", "1");
		params.put("iconFirst", ""); 
//		params.put("location", "121.50270545464817,31.355562104730566"); 
//		params.put("user_id", "ede2af47-81b7-4f31-8913-ed1591328884");
		
		ret = uCourtService.queryCourtList(params);
		printInfo("===============", ret);
		
	}
	
	@Test
	public void testQuerySubscribedCourtList() {
		
	}
	
	public static String getWeekOfDate(Date date) {
		
		String[] weekOfDays = {"","周日","周一", "周二", "周三", "周四", "周五","周六"};
		Calendar calendar = Calendar.getInstance();
		
		if (date != null) {
			calendar.setTime(date);
		}
		
		int w = calendar.get(Calendar.DAY_OF_WEEK);
		
		return weekOfDays[w] + w;	
	}
	
	/**
	 * 获取第三方球场商品
	 * @throws Exception
	 */
	@Test
	public void testGet3rdCourtServiceById() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sessionids", "10000,10010");
		params.put("productsubtype", "3");
		
		ret = uCourtService.get3rdCourtServiceById(params);
		printInfo("===============", ret);
	}
	
	/**
	 * 场次商品分类
	 * @throws Exception
	 */
	@Test
	public void testQueryProductTypeBySession() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sessionids", "10000,10010");
		
		ret = uCourtService.queryProductTypeBySession(params);
		printInfo("===============", ret);
	}
	
	/**
	 * 我关注 过 的球场列表
	 * @throws Exception
	 */
	@Test
	public void testQueryCourtListSubscribed() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sessionids", "10000,10010");
		
		ret = uCourtService.queryCourtListSubscribed(params);
		printInfo("===============", ret);
	}
	
	/**
	 * 查询约战概况详情
	 * @throws Exception
	 */
	@Test
	public void testGetDuelGKInfo() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("duelId", "000855e6-ee29-4f0d-92bf-14b94d34075b");
		params.put("bs_id", "");
//		UDuel duel = new UDuel();
//		duel.setDuelId("000855e6-ee29-4f0d-92bf-14b94d34075b");
		try {
			ret = uDuelService.getDuelGKInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printInfo("===============", ret);
	}
	
	@Test
	public void testQueryCourtDetail() {
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("page", null);
		params.put("userStatus", "");
		params.put("iconFirst", ""); 
		params.put("subcourt_id", "70598249-b93e-4f56-9ada-a71d3ac57da4");

		try {
			ret = uCourtService.queryCourtDetail(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printInfo("===============", ret);
	}
	
	@Test
	public void testGetCourtBysubCourtId() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("subCourtId", "64f6a04f-8add-42f8-a773-0fcfcf92753c");
		UCourt uCourt = uCourtService.getCourtBysubCourtId(params);
		printInfo("UCOURTTTTTTTTTTT",uCourt.getAddress());
		printInfo("UCOURTTTTTTTTTTT",uCourt.getBdPoi());
		printInfo("UCOURTTTTTTTTTTT",uCourt.getBdPosition());

	}
//	public static final void main (String[] args) throws ParseException {
//		String weekDay = getWeekOfDate(new Date());
//		System.out.println(weekDay);
//		System.out.println(PublicMethod.getDateToString(new Date(),"HH:mm:ss"));
//		System.out.println(PublicMethod.getDateToString(new Date(),"yyyy-MM-dd"));
//	}
}
