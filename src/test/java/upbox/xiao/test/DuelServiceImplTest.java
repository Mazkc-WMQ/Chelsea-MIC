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

@ContextConfiguration("applicationContext.xml")
public class DuelServiceImplTest  extends AbstractJUnit4SpringContextTests{
	@Resource
	private UOrderService uorderService;
	@Resource
	private FilterService filterService;
	@Resource
	private UCourtService  ucourtService;
	@Resource
	private UDuelService uduelServiceImpl;
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
	public void addDuelOrder() throws Exception{
		List<String> duelTeamList=new ArrayList<>();
//		List<UDuelChallengeImg> duelImgList=new ArrayList<>();
		HashMap<String,String> hash = new HashMap<String, String>();
		hash.put("userId", "77fdbd43-f540-4316-a2e5-dea57f522cbc");
		hash.put("loginUserId", "77fdbd43-f540-4316-a2e5-dea57f522cbc");
		hash.put("teamId", "825a8847-bf9d-4920-b8be-b3ea465cf0d4");
		hash.put("token", "7662d2de-024b-4b63-af2f-48178d21ce07");
		hash.put("userStatus", "1");
		hash.put("stdate", "2016-07-30");
		hash.put("sttime", "10:00");
		hash.put("endtime", "12:00");
		hash.put("teamBehaviorType", "3");
		hash.put("subCourtId", "a3cf41fb-9379-42ea-a308-1beeba5de32e");
		hash.put("page", "1");
		hash.put("duelPayType", "1");  
		hash.put("payProportion", "1");
		hash.put("remark", "12342342");
		hash.put("orderId", "00527e68-4dbb-445b-8851-0e551c9a2518");
		hash.put("appCode", "2.0.2");
		hashMap =uduelServiceImpl.addDuelOrder(hash, duelTeamList, null);
		printInfo("------------------------------",hashMap);
	}
	

}
