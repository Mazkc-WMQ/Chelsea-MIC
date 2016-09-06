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
import upbox.model.UDuelChallengeImg;
import upbox.model.UMall;
import upbox.model.UOrderCourt;
import upbox.service.FilterService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UOrderService;
import upbox.service.UParameterService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.service.UWorthService;

@ContextConfiguration("applicationContext.xml")
public class PlayerServiceImplTest  extends AbstractJUnit4SpringContextTests{
	@Resource
	private UOrderService uorderService;
	@Resource
	private FilterService filterService;
	@Resource
	private UCourtService  ucourtService;
	@Resource
	private UPlayerService uplayerService;
	@Resource
	private UParameterService uParameterService;
	@Resource
	private UTeamService uteamService;
	@Resource
	private UWorthService uworthService;
	private HashMap<String, Object> hashMap=new HashMap<>();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 订单生成日期转换

	DecimalFormat df = new DecimalFormat("######0.0");
	@Ignore
	/**
	 * 
	 * TODO
	 * @param temp
	 * @param obj
	 * xiaoying 2016年7月20日
	 */
	public void printInfo(String temp,Object obj){
		//System.out.println(temp + JSON.toJSONString(obj));
	}
	@Test
	public void getUPlayerinfoManage() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
//		hash.put("userId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("loginUserId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("playerId", "0006a66e-bff7-4a68-bc41-4341ba290ca2");
		hash.put("userId", "7aa1ae4d-e4fc-40be-b85a-5101f7ee213b");
		hash.put("loginUserId", "7aa1ae4d-e4fc-40be-b85a-5101f7ee213b");
		hash.put("playerId", "0022b03e-e764-4236-9149-63ac61b5f16c");
		
		hash.put("teamId", "1358d5d2-dab5-4bdc-a3b8-7b9873e1eea6");
		hash.put("memberType", "1");
		hashMap=uplayerService.getUPlayerinfoManage(hash);
	}
	
	@Test
	public void editPlayerInfoManage() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
		hash.put("loginUserId", "77fdbd43-f540-4316-a2e5-dea57f522cbc");
		hash.put("playerId", "0006a66e-bff7-4a68-bc41-4341ba290ca2");
		hash.put("number", "100");
		hash.put("position", "1");
		hash.put("realname", "张三");
//		hash.put("nickname", );
		hash.put("height", "179");
		hash.put("weight", "70");
//		hash.put("birthday", );
		hashMap=uplayerService.editPlayerInfoManage(hash);
		printInfo("------------------------------",hashMap);
	}
	
	@Test
	public void getMemberTypeListByTeamId202() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
//		hash.put("userId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("loginUserId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("playerId", "0006a66e-bff7-4a68-bc41-4341ba290ca2");
		hash.put("userId", "460b1ebb-a5d5-43f6-8e96-b04a136a8ae1");
		hash.put("loginUserId", "460b1ebb-a5d5-43f6-8e96-b04a136a8ae1");
		hash.put("teamId", "1358d5d2-dab5-4bdc-a3b8-7b9873e1eea6");
		hashMap=uParameterService.getMemberTypeListByTeamId202(hash);
	}
	
	@Test
	public void findPlayerListByType202() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
//		hash.put("userId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("loginUserId", "5b612bb2-0b16-4d08-99fa-f3306fcf6014");
//		hash.put("playerId", "0006a66e-bff7-4a68-bc41-4341ba290ca2");
		hash.put("userId", "7aa1ae4d-e4fc-40be-b85a-5101f7ee213b");
		hash.put("loginUserId", "7aa1ae4d-e4fc-40be-b85a-5101f7ee213b");
		hash.put("teamId", "1358d5d2-dab5-4bdc-a3b8-7b9873e1eea6");
		hashMap=uplayerService.findPlayerListByType202(hash);
		System.out.println(hashMap);
	}
	
	@Test
	public void saveTaskInfo() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
		hash.put("loginUserId", "7aa1ae4d-e4fc-40be-b85a-5101f7ee213b");
		hash.put("taskBehavior", "1");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
		hashMap=uworthService.saveTaskInfo(hash);
		System.out.println(hashMap);
	}
	@Test
	public void findUserIdTaskInfo() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
		hash.put("loginUserId", "31c8a355-54f1-4f06-99ab-6dea5d68b8ae");
		hashMap=uworthService.findUserIdTaskInfo(hash);
		System.out.println(hashMap);
	}
	@Test
	public void repairTaskInfo() throws Exception{
		HashMap<String,String> hash = new HashMap<String, String>();
		System.out.println(new Date()+"%%%%%%%%%%%%%%%");
		hash.put("act", "1");
		hashMap=uworthService.repairTaskInfo(hash);
		System.out.println(new Date()+"----");
		System.out.println(hashMap);
	}


	@Test
	public void testDate() throws Exception{
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    long between = 0;
	    try {
	        java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
	        java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
	        between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    long day = between / (24 * 60 * 60 * 1000);
	    long hour = (between / (60 * 60 * 1000) - day * 24);
	    long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	    long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	    long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
	            - min * 60 * 1000 - s * 1000);
	    System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
	            + "毫秒");
	}
	
}
