package upbox.zzy.test;

import com.alibaba.fastjson.JSON;
import com.org.pub.PublicMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import upbox.model.UCourt;
import upbox.model.UDuelChallengeImg;
import upbox.model.UTeam;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.service.LBSService;
import upbox.service.PublicService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UTeamService;

/**
 * 
 * @author zzy
 *
 * 15252481515
 */

@ContextConfiguration("/applicationContext.xml")
public class UDuelServiceTest  extends AbstractJUnit4SpringContextTests{
	@Resource
	private UDuelService uDuelService;
	@Resource
	private UTeamService uteamService;
	@Resource
	private UCourtService uCourtService;
	@Resource
	private PublicService publicService;
	@Resource
	private LBSService lBSService;
	private List<String> duelTeamList = new ArrayList<String>();
	private List<UDuelChallengeImg> duelImgList = new ArrayList<UDuelChallengeImg>();
	UDuelChallengeImg uci = new UDuelChallengeImg();
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
	public void testGetTeamId() throws Exception {
		HashMap<String, String> map = new HashMap<>();
		map.put("userId", "178ade7f-9a0d-43de-a52a-29414d02432f");
		UTeam team = (UTeam) uteamService.getUteaminfoByUserId(map)
				.get("uTeam");
		
		System.out.println("Over!");

	}
	
	/**
	 * 上传LBS约战数据
	 * @param map
	 * 	duelId 约战ID
	 * subCourtId 子球场ID
	 */
	@Test
	public void insertDuelGeoData() throws Exception {
		HashMap<String, String>map = new HashMap<>();
		map.put("duelId", "35ca933d-4919-41ed-af76-476f50659732");
		map.put("subCourtId", "a3cf41fb-9379-42ea-a308-1beeba5de32e");
		HashMap<String, Object> geoParam = new HashMap<String, Object>();
		UCourt uCourt = uCourtService.getCourtBysubCourtId(map);
		map.put("id", map.get("duelId"));
		int duelIdInt = publicService.getIntKeyId(5,map);
		map.put("id", map.get("subCourtId"));
		int subcourtIdInt = publicService.getIntKeyId(4,map);
		map.put("id", uCourt.getCourtId());
		int courtIdInt = publicService.getIntKeyId(3,map);
			geoParam.put("title", "约战数据");
			geoParam.put("address", uCourt.getAddress());
			geoParam.put("tags", "duel");
//			geoParam.put("latitude", uCourt.getBdPosition());
//			geoParam.put("longitude", uCourt.getBdPoi());
			geoParam.put("latitude", "31");
			geoParam.put("longitude", "121");
			geoParam.put("coord_type", "3");
			geoParam.put("geotable_id", "142434");
			geoParam.put("url", Public_Cache.LBS_LOCATION);
//			geoParam.put("ak", Public_Cache.BAIDU_LBS);
			geoParam.put("object_id",map.get("duelId"));
			geoParam.put("params_type","4"); //约战
			geoParam.put("duel_intid",duelIdInt); //约战数字Id
			geoParam.put("subcourt_intid",subcourtIdInt); //子球场数字Id
			geoParam.put("court_intid",courtIdInt); //父球场数字Id
			geoParam.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
			
			String ret = "";
			try {
				ret = lBSService.createGeodata(geoParam);
				System.out.println("ret: " + ret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (!"".equals(ret)) {
			JSONObject result = JSONObject.fromObject(ret);
			String geoId = result.getString("result");
			JSONObject json = JSONObject.fromObject(geoId);
			String status = json.getString("status");
			if ("0".equals(status)) {
				String id = json.getString("id");
				geoParam.put("objectId", map.get("duelId"));
				geoParam.put("id", id);
				geoParam.put("geotableId", Constant.BAIDU_TABLE_ID);
				lBSService.createUbaidulbsData(geoParam);
			}
		}
//			System.out.println(id);
	}
}
