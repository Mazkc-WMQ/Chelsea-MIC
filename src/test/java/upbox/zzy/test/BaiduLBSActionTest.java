package upbox.zzy.test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;

import net.sf.json.JSONObject;
import upbox.dao.impl.OperDAOImpl;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.OLDLBSService;
import upbox.service.URegionService;

@ContextConfiguration("/applicationContext.xml")
public class BaiduLBSActionTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private URegionService uRegionService;
	@Resource
	private OLDLBSService oldlbsService;
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private LBSService lBSService;
	
	private List<HashMap<String,Object>> infoList;
	private List<HashMap<String,Object>> lbsDataList;
	private HashMap<String,Object> hashMap;
	private HttpRespons response;
	
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
	
//	@Test
//	public void addLbsDuelChallengeTest(){
//		try {
//			this.oldlbsService.addLbsDuelChallenge(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	
//	/**
//	 * id到经纬度到geotable数据和本地库数据
//	 */
//	@Ignore
//	public void testFromIdToData() {
//		try {
//			HashMap<String, String> map = new HashMap<>();
//			map.put("userId", "00297b6a-d7ae-45f3-9278-a611e85a4b92");
//			infoList = lbsService.getGpsByUserId(map);
//			for (HashMap<String, Object> h : infoList) {
//				h.put("coord_type", "3");
//				h.put("geotable_id", "143657");
//			}
//			lbsDataList = lbsService.createGeodata(infoList);
//			lbsService.createUbaidulbsData(lbsDataList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Ignore
//	public void testWriteFile() {
//		HashMap<String, String> map = new HashMap<>();
//		map.put("userId", "00297b6a-d7ae-45f3-9278-a611e85a4b92");
//		File f = new File("E:/1.csv");
//
//		try {
//			OutputStream fos=new FileOutputStream(f);
//			OutputStreamWriter writer = new OutputStreamWriter(fos, "gb2312");
//			StringBuffer strb = new StringBuffer();
//			writer.write("title,address,longitude,latitude,coord_type,tags,{column key} \r\n");
//			infoList = lbsService.getGpsByUserId(map);
//			for (HashMap<String, Object> h : infoList) {
//				h.put("coord_type", "3");
//				h.put("geotable_id", "143657");
//				h.put("title", "0617");
//				h.put("{column key}", map);
//				strb.append(h.get("title") + "," 
//				+ h.get("address")  + ","
//				+ h.get("lng") + "," 
//				+ h.get("lat") + "," 
//				+ h.get("coord_type") + ","
//				+h.get("tags") + "," 
//				+ h.get("{column key}")+"\r\n");
//				writer.write(strb.toString());
//				
//				HttpClient client = new HttpClient();
//			    PostMethod postMethod = new PostMethod(
//			            "http://api.map.baidu.com/geodata/v3/poi/upload?");
//			    // 参数设置
//			    postMethod.setParameter("geotable_id", "143657");
//			    postMethod.setParameter("poi_list", "E:/1.csv");
//			    postMethod.setParameter("ak", "9NW8FraFOVsCT6LF7RZz2WolpusChbjx");
//				// 执行postMethod
//				client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//			    // 执行并返回状态
//			    int status = client.executeMethod(postMethod);
//			    String getUrl = postMethod.getResponseBodyAsString();
//			    System.out.println(getUrl);
//			}
//			writer.close();
//			fos.close();
//			
//		} catch (Exception e) {
//			
//		}
//	}
//	
//	@Ignore
//	public void testWTF() {
//		HashMap<String, Object> hashMap = new HashMap<>();
//		HashMap<String, Object> hash = new HashMap<>();
//		HttpUtil http = new HttpUtil();
//		ArrayList<String> list = new ArrayList<String>(Arrays.asList("上海市浦东新区", "上海市黄浦区", "上海市宝山区", "上海市徐汇区", "上海市普陀区",
//				"上海市闸北区", "上海市长宁区", "上海市嘉定区", "上海市虹口区", "四川省成都市武侯区", "上海市杨浦区", "上海市静安区", "浙江省舟山市普陀区", "上海市闵行区",
//				"江苏省苏州市吴中区", "上海市奉贤区", "上海市松江区", "内蒙古自治区鄂尔多斯市准格尔旗", "上海市青浦区", "上海市金山区", "新疆维吾尔自治区克拉玛依市市辖区", "北京市朝阳区",
//				"广东省广州市市辖区", "黑龙江省哈尔滨市市辖区", "四川省成都市成华区", "湖北省武汉市洪山区", "广东省深圳市南山区", "四川省宜宾市兴文县", "上海市崇明县", "浙江省杭州市拱墅区",
//				"吉林省通化市东昌区", "江苏省苏州市昆山市", "四川省成都市金牛区", "江苏省徐州市云龙区", "辽宁省盘锦市兴隆台区", "山东省烟台市莱山区", "浙江省绍兴市越城区"));
//		for (String str : list) {
//			System.out.println(str);
//			hashMap.put("output", "json");
//			hashMap.put("ak", "3XwpaWy00qbyKY3R4f5u4l4YuZIFpYl6");
//			hashMap.put("address", str);
//
//			try {
//				HttpRespons response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_addressToApi.do",
//						hashMap);
//				System.out.println(response.getContent());
//				hash = WebPublicMehod.checkLBS_Response(response.getContent());
//				JSONObject json = JSONObject.fromObject(hash.get("result"));
//				hash = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json.getString("location"));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
	@Test
	public void testOnMymark() throws Exception{
		HashMap<String, String> map = new HashMap<>();
		map.put("title", "用户数据");	//title区分注意
		map.put("coord_type", "3");
		map.put("ak", "3XwpaWy00qbyKY3R4f5u4l4YuZIFpYl6");
		map.put("output", "json");
		map.put("geotable_id", "142434");
		//map.put("geotable_id", "143996");
		infoList = oldlbsService.getGpsByTeamId(map);
		//infoList = oldlbsService.getGpsByUserId(map);
	}
	
	/**
	 * 上传LBS约战数据
	 */
	
	public void insertDuelGeoData() throws Exception {
		HashMap<String, Object> geoParam = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();

		List<HashMap<String, Object>> geoInfoList= baseDAO.findSQLMap(map, 
														"select uc.court_id_int cIdInt, ubc.subcourt_id_int scIdInt, ud.duel_id_int duIdInt "
														+ " from u_court uc "
														+ " join u_br_court ubc on uc.court_id = ubc.court_id "
														+ " join u_duel_ch udc on udc.court_id = ubc.subcourt_id "
														+ " join u_duel ud on ud.duel_id = udc. key_id "
														+ " and udc.key_id = :fchId ");
		for (HashMap<String, Object> geoInfo: geoInfoList) {
			geoParam.put("title", "约战数据");
			geoParam.put("coord_type", "3");
			geoParam.put("geotable_id", "142434");
			geoParam.put("address", geoInfo.get("address"));
			geoParam.put("latitude", geoInfo.get("bd_poi"));
			geoParam.put("longitude", geoInfo.get("bd_position"));
			geoParam.put("tags", "duel");
			geoParam.put("object_id",map.get("objectId"));
			geoParam.put("params_type","4"); //约战
			geoParam.put("duel_intid","duIdInt"); //约战数字Id
			geoParam.put("subcourt_intid","scIdInt"); //子球场数字Id
			geoParam.put("court_intid","duIdInt"); //父球场数字Id
		}

		String str = lBSService.createGeodata(geoParam);
		System.out.println(str);
		
	}
}