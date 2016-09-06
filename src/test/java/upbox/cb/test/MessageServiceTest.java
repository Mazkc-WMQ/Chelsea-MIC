package upbox.cb.test;

import java.util.HashMap;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.UTeamService;
import upbox.service.UUserService;


/**
 * 通知test -- messageService
 * @author charlescai
 *
 */
@ContextConfiguration("file:src/main/resources/applicationContext.xml")
public class MessageServiceTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private MessageService messageService;
	@Resource
	private UUserService uuserService;
	@Resource
	private UTeamService uteamService;
	
	@Resource
	private PublicPushService publicPush; 
	
	private HashMap<String, Object> resultMap;
	
	
	
	@Ignore
	/**
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	 */
	public void printInfo(String temp,Object obj){
		System.out.println(temp + JSON.toJSONString(obj));
	}
	
	/**
	 * test
	 * @throws Exception
	 */
//	@Test
//	public void addOrderMessage() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("mobile", "13120501633,13120501633,13120501633");
//		hashMap.put("content", "激战联盟APP当前版本正式退役，更强更全能的2.0版本已全面上线！赶快更新换他上场吧！");
//		publicPush.publicSendPhoneByHuax(hashMap);
//		printInfo("------",resultMap);
//	}
	
	/**
	 * test
	 * @throws Exception
	 */
//	@Test
//	public void testLBSTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("address", "上海市宝山区长江路415弄");
////		hashMap.put("address", "上海市虹口区新市北路415弄");
////		hashMap.put("content", "激战联盟APP当前版本正式退役，更强更全能的2.0版本已全面上线！赶快更新换他上场吧！");
//		
//		hashMap.put("teamId", "002ffae1-04eb-4ba8-afb0-16f4f3fa9e2a");
//		hashMap.put("team_id_int", "1");
//		uteamService.insTeamBaiduLBSData(hashMap);
////		uteamService.updTeamBaiduLBSData(hashMap);
//		printInfo("------",resultMap);
//	}
	
	
	/**
	 * test
	 * @throws Exception
	 */
	@Test
	public void testLBSUser() throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("address", "上海市宝山区长江路415弄");
		hashMap.put("address", "上海市虹口区新市北路415弄");
//		hashMap.put("content", "激战联盟APP当前版本正式退役，更强更全能的2.0版本已全面上线！赶快更新换他上场吧！");
		
		hashMap.put("userId", "57aedeb6-16bf-45e6-9f6a-0d28e4adbd48");
		hashMap.put("user_id_int", "3459");
		uuserService.insUserBaiduLBSData(hashMap);
//		uuserService.updUserBaiduLBSData(hashMap);
//		printInfo("------",resultMap);
	}

}
