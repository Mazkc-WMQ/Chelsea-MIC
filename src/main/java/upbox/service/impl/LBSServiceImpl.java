package upbox.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.BdLbsBean;
import upbox.model.PageLimit;
import upbox.model.UBaidulbs;
import upbox.model.ULbserrorLog;
import upbox.model.ULoginlbs;
import upbox.model.URegion;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.URegionService;
import upbox.service.UUserService;

//LBS 接口实现
@Service("lbsService")
public class LBSServiceImpl implements LBSService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UUserService uuserService;
	@Resource
	private URegionService uRegionService;
	private HttpRespons response;
	private HttpUtil hp = new HttpUtil();
	private final static String OUTPUT = "json";

	@Override
	public HashMap<String, Object> poiToAdress(HashMap<String, String> map)
			throws Exception {
		String userId = map.get("loginUserId");
		String[] location = map.get("location").split(",");
		HashMap<String, Object> hash = new HashMap<String, Object>();
		ULoginlbs ulbs = new ULoginlbs();
		ulbs.setCreatetime(new Date());
		ulbs.setKeyId(WebPublicMehod.getUUID());
		ulbs.setLbsLogType(map.get("lbsLogType"));
		ulbs.setPosition(location[1]);
		ulbs.setPoi(location[0]);
		if (null != userId && !"".equals(userId)) { // 用户ID不为空
			ulbs.setUserId(userId);
		}
		// 验证本次区域位置和上次登陆区域位置是否一样
		hash = checkPosition(map, ulbs, location);
		if(null != hash){
			ulbs = (ULoginlbs) hash.get("ulbs");
			baseDAO.save(ulbs);
			hash.remove("ulbs");
		}
		return hash;
	}

	/**
	 * 
	 * 
	 TODO - 验证登录区域是否相同
	 * 
	 * @param map
	 * @return true 是，false 否 2016年6月14日 mazkc
	 * @throws Exception
	 */
	private HashMap<String, Object> checkPosition(HashMap<String, String> map,
			ULoginlbs ulbs, String[] location) throws Exception {
			HashMap<String, Object> hash = new HashMap<String, Object>();
		try{
			
			String hql = "from ULoginlbs where userId = :loginUserId order by createtime desc";
			List<ULoginlbs> ub = baseDAO.findByPage(map, hql, 1, 0);
			ULoginlbs ulbs_ = new ULoginlbs();
			URegion ur = null;
			// 获取本次登录位置
			map.put("output", OUTPUT);
			map.put("ak", Public_Cache.BAIDU_LBS);
			response = hp.sendPost(Public_Cache.LBS_LOCATION
					+ "/baidulbs_poiToAdress.do", map);
			ulbs = getLBSReturn(response.getContent(), ulbs);
			hash.put("isSame", "false");
			if(null != map.get("loginUserId") && !"".equals(map.get("loginUserId"))){
				if (null != ub && ub.size() > 0) {
					// 找出上次登录的区域地址
					ulbs_ = ub.get(0);
					if (ulbs.getCountry().equals(ulbs_.getCountry())
							&& ulbs.getCity().equals(ulbs_.getCity())) {
						hash.put("isSame", "true");
					}
				} else {
					hash.put("isSame", "true");
				}
			}else{
				hash.put("isSame", "true");
			}
			hash.put("msg", "当前定位城市在" + ulbs.getCity() + ",和您上次填写城市有出入，是否更换?");
			hash.put("oldCityName", ulbs_.getCity());
			hash.put("oldLat", ulbs_.getPoi());
			hash.put("oldLng", ulbs_.getPosition());
			hash.put("oldCityName", ulbs_.getCity());
			hash.put("oldCityId", ulbs_.getCityid());
			hash.put("curCityName", ulbs.getCity());
			hash.put("curLat", location[0]);
			hash.put("curLng", location[1]);
			// 算出本地登录的城市ID
			if (ulbs.getCity().equals("北京市")||ulbs.getCity().equals("上海市")||ulbs.getCity().equals("天津市")||ulbs.getCity().equals("重庆市")) {
				ur = baseDAO.get(
						"from URegion where type = '1' and name = :curCityName",
						hash);
				baseDAO.getSessionFactory().getCurrentSession().clear();
				ur = baseDAO.get("from URegion where type = '2' and parent = '"
						+ ur.get_id() + "' and name = '市辖区'");
			} else {
				ur = baseDAO.get(
						"from URegion where type = '2' and name = :curCityName",
						hash);
			}
			hash.put("curCityId", ur.get_id());
			ulbs.setCityid(ur.get_id());
			hash.put("ulbs", ulbs);
			return hash;
		}catch(Exception e){
			e.printStackTrace();
			return hash;
		}
	}

	/**
	 * 
	 * 
	 TODO - 解析LBS组件的数据返回
	 * 
	 * @param json
	 * @return 2016年6月14日 mazkc
	 */
	private ULoginlbs getLBSReturn(String json, ULoginlbs ulbs) {
		HashMap<String, Object> jsonMap = (HashMap<String, Object>) PublicMethod
				.parseJSON2Map(PublicMethod.parseJSON2Map(json).get("result")
						.toString());
		JSONObject jsonO = JSONObject.fromObject(jsonMap.get("result"));
		ulbs.setAddress(jsonO.getString("formatted_address"));
		ulbs.setCity(jsonO.getJSONObject("addressComponent").getString("city"));
		ulbs.setCountry(jsonO.getJSONObject("addressComponent").getString(
				"country"));
		ulbs.setDistrict(jsonO.getJSONObject("addressComponent").getString(
				"district"));
		return ulbs;
	}

	@Override
	public String getNearBy(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_getNearBy.do", map);
		return response.getContent();
	}

	@Override
	public void createUbaidulbsData(String objectId,String lbsType,String lbsId)throws Exception {		
		UBaidulbs bdLbs = null;
		String lbsTable="";
		if (lbsType.equals("1")) {// 1-用户 2-球队 3-球场 4-约战 5-挑战
			lbsTable=Constant.BAIDU_PLAYER_TABLE_ID;
		} else if (lbsType.equals("2")) {
			lbsTable=Constant.BAIDU_TABLE_ID;
		} else if (lbsType.equals("3")) {
			lbsTable=Constant.BAIDU_COURT_TABLE_ID;
		} else if (lbsType.equals("4")) {
			lbsTable=Constant.BAIDU_DUEL_TABLE_ID;
		} else if (lbsType.equals("5")) {
			lbsTable=Constant.BAIDU_CHALLENGE_TABLE_ID;
		}
		//验证是否重复数据
		bdLbs = baseDAO.get("from UBaidulbs where objectId = '"+objectId+"' and lbsTable = '"+lbsTable+"' and lbsType = '"+lbsType+"'");
		if(bdLbs==null){
			bdLbs=new UBaidulbs();
			bdLbs.setKeyId(WebPublicMehod.getUUID());
		}
		bdLbs.setLbsTable(lbsTable);
		bdLbs.setCreatetime(new Date());
		bdLbs.setLbsData(lbsId);
		bdLbs.setLbsType(lbsType);
		bdLbs.setObjectId(objectId);
		baseDAO.save(bdLbs);
	}

	@Override
	public String getNearByCity(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_getNearByCity.do", map);
		return response.getContent();
	}

	@Override
	public String addressToApi(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_addressToApi.do", map);
		return response.getContent();
	}

	@Override
	public String createGeodata(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_createGeodata.do", map);
		return response.getContent();
	}

	@Override
	public List<BdLbsBean> packLbsDate(String json) throws Exception {
		List<BdLbsBean> lbsList = new ArrayList<BdLbsBean>();
		JSONObject all = JSONObject.fromObject(json);
		String resultStr = (String) all.get("result");
		JSONObject result = JSONObject.fromObject(resultStr);
		String status=checkJson(result.get("status"));
		if(status.equals("0")){//否则表示接口有错误
			JSONArray contents = result.getJSONArray("contents");
			for(int i=0;i<contents.size();i++){
				BdLbsBean temp=new BdLbsBean();
				JSONObject jsobj = (JSONObject) contents.get(i);
				temp=(BdLbsBean)JSONObject.toBean(jsobj, BdLbsBean.class);
				lbsList.add(temp);
			}
		}
		return lbsList;
	}

	/**
	 * json对象判断
	 * 
	 * @param str
	 * @return
	 */
	private String checkJson(Object str) {
		if (str != null && !"".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}

	@Override
	public String updateGeodata(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_updatePoiData.do", map);
		return response.getContent();
	}
	@Override
	public UBaidulbs getBaidulbs(String lbsType,String objectId) throws Exception {
		String lbsTable="";
		if (lbsType.equals("1")) {// 1-用户 2-球队 3-球场 4-约战 5-挑战
			lbsTable=Constant.BAIDU_PLAYER_TABLE_ID;
		} else if (lbsType.equals("2")) {
			lbsTable=Constant.BAIDU_TABLE_ID;
		} else if (lbsType.equals("3")) {
			lbsTable=Constant.BAIDU_COURT_TABLE_ID;
		} else if (lbsType.equals("4")) {
			lbsTable=Constant.BAIDU_DUEL_TABLE_ID;
		} else if (lbsType.equals("5")) {
			lbsTable=Constant.BAIDU_CHALLENGE_TABLE_ID;
		}
		return baseDAO.get("from UBaidulbs where objectId = '"+objectId+"' and lbsTable = '"+lbsTable+"' and lbsType = '"+lbsType+"'");
		
	}
	@Override
	public HashMap<String, Object> packLbsDateAddress(String json) throws Exception {
//		json
//		{
//			  "errorMsg": null,
//			  "result": "{\"status\":0,\"result\":{\"location\":{\"lng\":121.49607206403445,\"lat\":31.227203440768869},\"precise\":0,\"confidence\":25,\"level\":\"区县\"}}\r\n",
//			  "ret": 1,
//			  "successMsg": null
//		}
		HashMap<String, Object> hashMap=new HashMap<>();
		JSONObject all = JSONObject.fromObject(json);
		String resultStr = (String) all.get("result");
		JSONObject result = JSONObject.fromObject(resultStr);
		String status = checkJson(result.get("status"));
		if (status.equals("0")) {// 否则表示接口有错误
			JSONObject contents = (JSONObject) result.get("result");
			JSONObject location = (JSONObject) contents.get("location");
			hashMap.put("lng", checkJson(location.get("lng")));
			hashMap.put("lat", checkJson(location.get("lat")));
			hashMap.put("status", checkJson(result.get("status")));
			return hashMap;
		}
		return hashMap;
	}
	@Override
	public HashMap<String, Object> packLbsDateCreateGeodata(String json) throws Exception {
//		json
//		{
//			  "errorMsg": null,
//			  "result": "{\"status\":0,\"result\":{\"location\":{\"lng\":121.49607206403445,\"lat\":31.227203440768869},\"precise\":0,\"confidence\":25,\"level\":\"区县\"}}\r\n",
//			  "ret": 1,
//			  "successMsg": null
//		}
		HashMap<String, Object> hashMap=new HashMap<>();
		JSONObject all = JSONObject.fromObject(json);
		String resultStr = (String) all.get("result");
		JSONObject result = JSONObject.fromObject(resultStr);
		String status = checkJson(result.get("status"));
		if (status.equals("0")) {// 否则表示接口有错误
			hashMap.put("id", checkJson(result.get("id")));
			hashMap.put("status", checkJson(result.get("status")));
			return hashMap;
		}
		return hashMap;
	}

	@Override  
	public List<BdLbsBean> sendBdLbs(PageLimit page,String sendType,String location,String radius,String tags,String sortby,String region,String filterType,String filter) throws Exception{
		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
		String lbsBack="";
		lbsMap.put("location", location);//百度经纬度
		lbsMap.put("coord_type", "3");//百度坐标系
		lbsMap.put("radius", radius);//半径范围
		lbsMap.put("tags", tags);//搜索球场
		lbsMap.put("sortby", sortby);//距离升序
		lbsMap.put("url", Public_Cache.LBS_LOCATION);
		if(region!=null){
			lbsMap.put("region", URLEncoder.encode(region, "UTF-8"));
		}
		if(filter!=null&&!"".equals(filter)){
			lbsMap.put("filter", filter);
			switch (filterType){
				
				case "1" : lbsMap.put("geotable_id", Constant.BAIDU_PLAYER_TABLE_ID);;
				break; 

				case "2" : lbsMap.put("geotable_id", Constant.BAIDU_COURT_TABLE_ID);;
				break; 

				case "3" : lbsMap.put("geotable_id", Constant.BAIDU_TEAM_TABLE_ID);;
				break; 
				
				case "4" : lbsMap.put("geotable_id", Constant.BAIDU_DUEL_TABLE_ID);;
				break; 
				
				case "5" : lbsMap.put("geotable_id", Constant.BAIDU_CHALLENGE_TABLE_ID);;
				break; 
			} 
		}
		
		if(page!=null){//搜索时交给百度分页
			lbsMap.put("page_index",page.getPage()-1);
			lbsMap.put("page_size",page.getLimit());
		}else{//筛选时全取  最多传入10个主键id 也不会返回超过50个对象
			lbsMap.put("page_index","0");
			lbsMap.put("page_size","50");
		}
		
		if(sendType!=null&&"1".equals(sendType)){
			System.out.println(lbsMap);
			lbsBack=this.getNearBy(lbsMap);//周边检索
		}else if(sendType!=null&&"2".equals(sendType)){
			lbsBack=this.getNearByCity(lbsMap);//区域检索
		}
		//处理lbs返回json
		List<BdLbsBean> lbsList=this.packLbsDate(lbsBack);
		return lbsList;
	}	

	@Override
	public String deleteDateByID(HashMap<String, Object> map) throws Exception {
		map.put("ak", Public_Cache.BAIDU_LBS);
		response = hp.sendPost(map.get("url").toString()
				+ "/baidulbs_deletePoiData.do", map);
		return response.getContent();
	}

	@Override
	public void delUbaidulbsData(HashMap<String, Object> map) throws Exception {
		baseDAO.executeHql("delete from UBaidulbs where objectId = :objectId", map);
	}

	@Override
	public void createUbaidulbsDataError(String objectId,String lbsType,String errorMsg)throws Exception {
		ULbserrorLog ulbserror = null;
		String lbsTable="";
		if (lbsType.equals("1")) {// 1-用户 2-球队 3-球场 4-约战 5-挑战
			lbsTable=Constant.BAIDU_PLAYER_TABLE_ID;
		} else if (lbsType.equals("2")) {
			lbsTable=Constant.BAIDU_TABLE_ID;
		} else if (lbsType.equals("3")) {
			lbsTable=Constant.BAIDU_COURT_TABLE_ID;
		} else if (lbsType.equals("4")) {
			lbsTable=Constant.BAIDU_DUEL_TABLE_ID;
		} else if (lbsType.equals("5")) {
			lbsTable=Constant.BAIDU_CHALLENGE_TABLE_ID;
		}
		//验证是否重复数据
		ulbserror = baseDAO.get("from ULbserrorLog where objectId = '"+objectId+"' and lbsTable = '"+lbsTable+"' and lbsType = '"+lbsType+"'");
		if(ulbserror==null){
			ulbserror=new ULbserrorLog();
			ulbserror.setKeyId(WebPublicMehod.getUUID());
		}
		ulbserror.setLbsTable(lbsTable);
		ulbserror.setCreatetime(new Date());
		ulbserror.setLbsType(lbsType);
		ulbserror.setObjectId(objectId);
		ulbserror.setErrorMsg(errorMsg);
		baseDAO.save(ulbserror);
	}
}
