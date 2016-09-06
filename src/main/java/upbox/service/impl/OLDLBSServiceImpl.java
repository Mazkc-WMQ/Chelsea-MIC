package upbox.service.impl;

import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.UBaidulbs;
import upbox.model.UBrCourt;
import upbox.model.UChallenge;
import upbox.model.UCourt;
import upbox.model.UDuel;
import upbox.model.ULoginlbs;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.outModel.OutUbCourtMap;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.OLDLBSService;
import upbox.service.PublicService;
import upbox.service.URegionService;
import upbox.service.UUserService;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//LBS 接口实现
@Service("oldlbsService")
public class OLDLBSServiceImpl implements OLDLBSService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UUserService uuserService;
	@Resource
	private URegionService uRegionService;
    @Resource
    private LBSService lbsService;
	private HashMap<String, Object> location;
	private HashMap<String, Object> info;
	private List<UUser> uUserList;
	private List<UTeam> uTeamList;
	private HttpRespons response;
	@Resource
	private PublicService publicService;

	@Override
	public HashMap<String, Object> poiToAdress(HashMap<String, String> map)
			throws Exception {
		String userId = map.get("loginUserId");
		String[] location = map.get("location").split(",");
		HashMap<String, Object> hash = new HashMap<String, Object>();
		ULoginlbs ulbs = null;
		if (null != userId && !"".equals(userId)) { // 用户ID不为空
			// 记录本次登陆区域位置
			ulbs = new ULoginlbs();
			ulbs.setCreatetime(new Date());
			ulbs.setKeyId(WebPublicMehod.getUUID());
			ulbs.setLbsLogType(map.get("lbsLogType"));
			ulbs.setUserId(userId);
			ulbs.setPosition(location[1]);
			ulbs.setPoi(location[0]);

			// 验证本次区域位置和上次登陆区域位置是否一样
			hash = checkPosition(map,ulbs,location);
			ulbs = (ULoginlbs) hash.get("ulbs");
			baseDAO.save(ulbs);
		}
		hash.remove("ulbs");
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
	private HashMap<String, Object> checkPosition(HashMap<String, String> map,ULoginlbs ulbs,String[] location)
			throws Exception {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		String hql = "from ULoginlbs where userId = :loginUserId order by createtime desc";
		List<ULoginlbs> ub = baseDAO.findByPage(map, hql, 1, 0);
		ULoginlbs ulbs_ = new ULoginlbs();
		HttpUtil hp = new HttpUtil();
		HttpRespons resp = null;
		URegion ur = null;
		// 获取本次登录位置
		map.put("output", "json");
		map.put("ak", Public_Cache.BAIDU_LBS);
		resp = hp.sendPost(Public_Cache.LBS_LOCATION + "/baidulbs_poiToAdress.do",
				map);
		ulbs = getLBSReturn(resp.getContent(),ulbs);
		hash.put("isSame", "false");
		if (null != ub && ub.size() > 0) {
			// 找出上次登录的区域地址
			ulbs_ = ub.get(0);
			if(ulbs.getCountry().equals(ulbs_.getCountry()) && ulbs.getCity().equals(ulbs_.getCity())){
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
		//算出本地登录的城市ID
		if(ulbs.getCity().indexOf("市") != -1){
			ur = baseDAO.get("from URegion where type = '1' and name = :curCityName", hash);
			baseDAO.getSessionFactory().getCurrentSession().clear();
			ur = baseDAO.get("from URegion where type = '2' and parent = '" + ur.get_id() + "' and name = '市辖区'");
		}else{
			ur = baseDAO.get("from URegion where type = '2' and name = :curCityName", hash);
		}
		hash.put("curCityId", ur.get_id());
		ulbs.setCityid(ur.get_id());
		hash.put("ulbs", ulbs);
		return hash;
	}
	
	/**
	 * 
	 * 
	   TODO - 解析LBS组件的数据返回
	   @param json
	   @return
	   2016年6月14日
	   mazkc
	 */
	private ULoginlbs getLBSReturn(String json,ULoginlbs ulbs){
		HashMap<String, Object> jsonMap = (HashMap<String, Object>) PublicMethod.parseJSON2Map(PublicMethod.parseJSON2Map(json).get("result").toString());
		JSONObject jsonO = JSONObject.fromObject(jsonMap.get("result"));
		ulbs.setAddress(jsonO.getString("formatted_address"));
		ulbs.setCity(jsonO.getJSONObject("addressComponent").getString("city"));
		ulbs.setCountry(jsonO.getJSONObject("addressComponent").getString("country"));
		ulbs.setDistrict(jsonO.getJSONObject("addressComponent").getString("district"));
		return ulbs;
	}
	
	@Override
	public List<HashMap<String, Object>> getGpsByUserId(HashMap<String, String> map) throws Exception {
		List<HashMap<String, Object>> infoList = new ArrayList<>();
		List<HashMap<String, Object>> lbsDataList;
		Object area = null;
		JSONObject json = null;
		HttpUtil http = new HttpUtil();
		uUserList = getUserInfoByUserId(map);
		if (null != uUserList && uUserList.size() > 0) {
			for (int i = 0; i < uUserList.size(); i++) {
				//第一条数据
				if (i == 0) {
					OutUbCourtMap ubCourtMap = this.setUbCourtMap(uUserList.get(i).getuRegion(), map);
					map.put("address", ubCourtMap.getNameStr());
					response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_poiToAdress.do", PublicMethod.Maps_Mapo(map));
					System.out.println(response.getContent());
					location = WebPublicMehod.checkLBS_Response(response.getContent());
					json = JSONObject.fromObject(location.get("result"));
					info = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json.getString("location"));// 获取经纬度
					info.put("address", ubCourtMap.getNameStr());
					info.put("objectId", uUserList.get(i).getUserId());
					info.put("tags", "user");
					info.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
					map.put("id", uUserList.get(i).getUserId());
					info.put("user_intid",publicService.getIntKeyId(1,map));
					infoList.add(info);
					for (HashMap<String, Object> info : infoList) {
						info.put("title", map.get("title"));
						info.put("coord_type", map.get("coord_type"));
						info.put("geotable_id", map.get("geotable_id"));
					}
					lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
					createUbaidulbsData(lbsDataList); // 创建本地表数据
					area = uUserList.get(i).getuRegion();
				} else {
					infoList.clear();
					
					//与已存在数据比较
					if (uUserList.get(i).getuRegion().equals(area)) {
						info.put("objectId", uUserList.get(i).getUserId());
						map.put("id", uUserList.get(i).getUserId());
						info.put("user_intid",publicService.getIntKeyId(1,map));
						infoList.add(info);
						for (HashMap<String, Object> info : infoList) {
							info.put("title", map.get("title"));
							info.put("coord_type", map.get("coord_type"));
							info.put("geotable_id", map.get("geotable_id"));
						}
						lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
						createUbaidulbsData(lbsDataList); // 创建本地表数据
					} else {
						OutUbCourtMap ubCourtMap = this.setUbCourtMap(uUserList.get(i).getuRegion(), map);
						map.put("address", ubCourtMap.getNameStr());
						response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_poiToAdress.do", PublicMethod.Maps_Mapo(map));
						System.out.println(response.getContent());
						location = WebPublicMehod.checkLBS_Response(response.getContent());
						json = JSONObject.fromObject(location.get("result"));
						info = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json.getString("location"));// 获取经纬度
						info.put("address", ubCourtMap.getNameStr());
						info.put("objectId", uUserList.get(i).getUserId());
						info.put("tags", "user");
						info.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
						map.put("id", uUserList.get(i).getUserId());
						info.put("user_intid",publicService.getIntKeyId(1,map));
						infoList.add(info);
						for (HashMap<String, Object> info : infoList) {
							info.put("title", map.get("title"));
							info.put("coord_type", map.get("coord_type"));
							info.put("geotable_id", map.get("geotable_id"));
						}
						lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
						createUbaidulbsData(lbsDataList); // 创建本地表数据

						area = uUserList.get(i).getuRegion();
					}
				}
			}
		}
		return infoList;
	}
	
	@Override
	public List<HashMap<String, Object>> getGpsByTeamId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hash = new HashMap<>();
		List<HashMap<String, Object>> infoList = new ArrayList<>();
		List<HashMap<String, Object>> lbsDataList;
		JSONObject json = null;
		Object area = null;
		HttpUtil http = new HttpUtil();
		int x = 0;
		uTeamList = getTeamInfoByTeamId(map);
		hash.put("output", "json");
		hash.put("ak", "3XwpaWy00qbyKY3R4f5u4l4YuZIFpYl6");
		if (null != uTeamList && uTeamList.size() > 0) {
			for (int i = 0; i < uTeamList.size(); i++) {
				System.out.println("x:" + x + " " + uTeamList.get(i).getTeamId());
				
				//第一条数据
				if (i == 0) {
					OutUbCourtMap ubCourtMap = this.setUbCourtMap(uTeamList.get(i).getuRegion(), map);
					hash.put("address", ubCourtMap.getNameStr());

					response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_addressToApi.do", hash);
					System.out.println(response.getContent());
					location = WebPublicMehod.checkLBS_Response(response.getContent());
					json = JSONObject.fromObject(location.get("result"));
					info = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json.getString("location"));// 获取经纬度
					info.put("address", ubCourtMap.getNameStr());
					info.put("objectId", uTeamList.get(i).getTeamId());
					info.put("tags", "team");
					info.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
					map.put("id", uTeamList.get(i).getTeamId());
					info.put("team_intid",publicService.getIntKeyId(2,map));
					infoList.add(info);
					for (HashMap<String, Object> info : infoList) {
						info.put("title", map.get("title"));
						info.put("coord_type", map.get("coord_type"));
						info.put("geotable_id", map.get("geotable_id"));
					}
					lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
					createUbaidulbsData(lbsDataList); // 创建本地表数据

					area = ubCourtMap.getNameStr();
				} else {
					infoList.clear();
					
					//与已存在area比较
					if (uTeamList.get(i).getuRegion().equals(area)) {
						info.put("objectId", uTeamList.get(i).getTeamId());
						map.put("id", uUserList.get(i).getUserId());
						info.put("user_intid",publicService.getIntKeyId(1,map));
						infoList.add(info);
						lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
						createUbaidulbsData(lbsDataList); // 创建本地表数据
					} else {
						OutUbCourtMap ubCourtMap = this.setUbCourtMap(uTeamList.get(i).getuRegion(), map);
						hash.put("address", ubCourtMap.getNameStr());
						response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_addressToApi.do", hash);
						System.out.println(response.getContent());
						location = WebPublicMehod.checkLBS_Response(response.getContent());
						json = JSONObject.fromObject(location.get("result"));
						info = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json.getString("location"));// 获取经纬度
						info.put("address", ubCourtMap.getNameStr());
						info.put("objectId", uTeamList.get(i).getTeamId());
						info.put("tags", "team");
						info.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
						map.put("id", uTeamList.get(i).getTeamId());
						info.put("team_intid", info.put("team_intid",publicService.getIntKeyId(2,map)));
						infoList.add(info);
						for (HashMap<String, Object> info : infoList) {
							info.put("title", map.get("title"));
							info.put("coord_type", map.get("coord_type"));
							info.put("geotable_id", map.get("geotable_id"));
						}
						lbsDataList = createGeodata(infoList); // 创建GEOTABLE数据
						createUbaidulbsData(lbsDataList); // 创建本地表数据

						area = ubCourtMap.getNameStr();
					}
				}
				x++;
			}
		}
		return infoList;
	}
	
	@Override
	public List<HashMap<String, Object>> createGeodata(List<HashMap<String, Object>> infoList) throws Exception {
		HashMap<String, Object> geoParam = new HashMap<String, Object>();
		List<HashMap<String, Object>> lbsDataList = new ArrayList<>();
		HashMap<String, Object> idHashMap = null;
		HttpUtil http = new HttpUtil();
		int y = 0;
		
		geoParam.put("ak", "3XwpaWy00qbyKY3R4f5u4l4YuZIFpYl6");
		for (HashMap<String, Object> info : infoList) {
			try{
				System.out.println("y:" + y + " " + info.get("objectId"));
				geoParam.put("title", info.get("title"));
				geoParam.put("coord_type", info.get("coord_type"));
				geoParam.put("geotable_id", info.get("geotable_id"));
				geoParam.put("address", info.get("address"));
				geoParam.put("latitude", info.get("lat"));
				geoParam.put("longitude", info.get("lng"));
				geoParam.put("tags", info.get("tags"));
				geoParam.put("object_id",info.get("objectId"));
				geoParam.put("date", info.get("date"));
				if (info.get("tags").equals("user")) {
					geoParam.put("user_intid", info.get("user_intid"));
					geoParam.put("params_type","1");
				} else if (info.get("tags").equals("team")) {
					geoParam.put("params_type","2");
					geoParam.put("team_intid", info.get("team_intid"));
				}
				System.out.println(geoParam.get("geotable_id"));
				response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_createGeodata.do", geoParam);
				System.out.println(response.getContent());
				idHashMap = WebPublicMehod.checkLBS_Response(response.getContent()); // 获取GeoTable对应key
				idHashMap.put("objectId", info.get("objectId"));
				idHashMap.put("geotableId", geoParam.get("geotable_id"));
				idHashMap.put("tags", geoParam.get("tags"));
				lbsDataList.add(idHashMap);
				y++;
			}catch(Exception e){
				response = http.sendPost("http://183.131.153.79:9125/Position/baidulbs_createGeodata.do", geoParam);
				System.out.println(response.getContent());
				idHashMap = WebPublicMehod.checkLBS_Response(response.getContent()); // 获取GeoTable对应key
				idHashMap.put("objectId", info.get("objectId"));
				idHashMap.put("geotableId", geoParam.get("geotable_id"));
				idHashMap.put("tags", geoParam.get("tags"));
				lbsDataList.add(idHashMap);
				y++;
			}
		}
		return lbsDataList;
	}
	
	
	
	/**
	 * 获取用户信息
	 * @param map userId 用户ID
	 * @return
	 * @throws Exception
	 */
	private List<UUser> getUserInfoByUserId(HashMap<String, String> map) throws Exception {
		if (map.get("userId") != "" && map.get("userId") != null) {
			uUserList = baseDAO.find(map, "from UUser where userId=:userId");
		} else {
			uUserList = baseDAO.find("from UUser u where u.uRegion is not null group by u.uRegion,userId");
		}
		return uUserList;
	}
	
	/**
	 * 获取球队信息
	 * @param map teamId 球队ID
	 * @return
	 * @throws Exception
	*/
	private List<UTeam> getTeamInfoByTeamId(HashMap<String, String> map) throws Exception {
		if (map.get("teamId") != "" && map.get("teamId") != null) {
			uTeamList = baseDAO.find(map, "from UTeam where teamId=:teamId");
		} else {
			uTeamList = baseDAO.find("from UTeam u where u.uRegion is not null group by u.uRegion");
		}
		return uTeamList;
	} 
	
	/**
	 * 
	   球队概况 --球场轴--区域
	   @param getuRegion  区域对象
	   @param map
	   		keyId		区域主键Id
	   @param uTeam			对象
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private OutUbCourtMap setUbCourtMap(URegion getuRegion, HashMap<String, String> map) throws Exception {
		OutUbCourtMap ubCourtMap = new OutUbCourtMap();
		map.put("keyId", getuRegion.get_id());
		ubCourtMap.setIdStr("区域");
		Set<URegion> uRegions = uRegionService.getURegionSet(map);
		String province = null;
		String city = null;
		String county = null;
		for (URegion uRegion : uRegions) {
			if ("1".equals(uRegion.getType())) {
				province = uRegion.getName();
			}
			if ("2".equals(uRegion.getType())) {
				city = uRegion.getName();
			}
			if ("3".equals(uRegion.getType())) {
				county = uRegion.getName();
			}
		}
		if ("市".equals(province.substring(province.length() - 1, province.length()))) {
			ubCourtMap.setNameStr(province + county);// 将省市区拼接
		} else {
			ubCourtMap.setNameStr(province + city + county);// 将省市区拼接
		}
		return ubCourtMap;
	}
	
	@Override
	public void createUbaidulbsData(List<HashMap<String, Object>> lbsDataList) throws Exception {
		UBaidulbs ubaidulbs = null;
		for (HashMap<String, Object> lbsData : lbsDataList) {
			ubaidulbs = new UBaidulbs();
			ubaidulbs.setCreatetime(new Date());
			ubaidulbs.setKeyId(WebPublicMehod.getUUID());
			ubaidulbs.setObjectId(lbsData.get("objectId").toString());
			ubaidulbs.setLbsData(lbsData.get("id").toString());
			ubaidulbs.setLbsTable(lbsData.get("geotableId").toString());
			if (lbsData.get("tags").equals("user")) {
				ubaidulbs.setLbsType("1"); // 1-用户 2-球队 3-球场
			} else if (lbsData.get("tags").equals("team")) {
				ubaidulbs.setLbsType("2");
			}
			baseDAO.save(ubaidulbs);
		}
	}

	@Override
	public void getNearBy(HashMap<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		
	}




	/*************************补充百度LBS，约战挑战数据***********************/
	@Override
	public void addLbsDuelChallenge(HashMap<String, Object> map) throws Exception {
		System.out.println("-----约战LBS数据补充-----");
		this.addLbsDuel();
		System.out.println("-----挑战LBS数据补充-----");
		this.addLbsChallenge();
	}
	//获取所有约战数据
	private void addLbsDuel() throws Exception {
		String hql = "from UDuel";
		List<UDuel> list = this.baseDAO.find(hql);
		for (UDuel duel : list){
			this.saveLbsDuel(duel);
		}
	}
	//保存约战数据到百度LBS
	private void saveLbsDuel(UDuel duel) throws Exception {
		if (duel.getUDuelCh() != null) {
			if (duel.getUDuelCh().getUBrCourt() != null) {
				UBrCourt brCourt = duel.getUDuelCh().getUBrCourt();
				UCourt court = brCourt.getUCourt();

				int duelIdInt = this.getEntityIntId("select duel_id_int as intId from u_duel where duel_id = '"+duel.getDuelId()+"'");
				int brCourtIdInt = this.getEntityIntId("select subcourt_id_int as intId from u_br_court where subcourt_id = '"+brCourt.getSubcourtId()+"'");
				int courtIdInt = this.getEntityIntId("select court_id_int as intId from u_court where court_id = '"+court.getCourtId()+"'");


				if(!"1".equals(court.getCourtType()))return;
				Map<String,Object> map = PublicMethod.parseJSON2Map(this.lbsService.createGeodata(this.getLbsMap(court,courtIdInt,brCourtIdInt,duelIdInt,duel.getDuelId(),"约战数据","duel","4")));
				Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
				System.out.println("百度ID==" + retMap.get("id") + "   约战Id=" + duel.getDuelId() + "   球场名=" + court.getName() + "  状态=" + retMap.get("status") + "  经度=" + court.getBdPoi() + "   纬度=" + court.getBdPosition());

				if("0".equals(retMap.get("status") != null ? retMap.get("status").toString() : "-1")){
					this.saveBaiduLbs(duel.getDuelId(), retMap.get("id").toString(), "4");
				}else{
					System.out.println("保存失败：" + map.get("result"));
				}

			}
		}


	}
	//获取所有挑战数据
	private void addLbsChallenge() throws Exception {
		String hql = "from UChallenge";
		List<UChallenge> list = this.baseDAO.find(hql);
		for (UChallenge challenge : list){
			this.saveLbsChallenge(challenge);
		}
	}
	//保存挑战数据到百度LBS
	private void saveLbsChallenge(UChallenge challenge) throws Exception {

		if (challenge.getUChallengeCh() != null) {
			if (challenge.getUChallengeCh().getUBrCourt() != null) {
				UBrCourt brCourt =challenge.getUChallengeCh().getUBrCourt();
				UCourt court = brCourt.getUCourt();
				if(!"1".equals(court.getCourtType())){
					return;
				}

				int challengeIdInt = this.getEntityIntId("select challenge_id_int as intId from u_challenge where challenge_id = '"+challenge.getChallengeId()+"'");
				int brCourtIdInt = this.getEntityIntId("select subcourt_id_int as intId from u_br_court where subcourt_id = '"+brCourt.getSubcourtId()+"'");
				int courtIdInt = this.getEntityIntId("select court_id_int as intId from u_court where court_id = '"+court.getCourtId()+"'");

				Map<String,Object> map = PublicMethod.parseJSON2Map(this.lbsService.createGeodata(this.getLbsMap(court,courtIdInt,brCourtIdInt,challengeIdInt,challenge.getChallengeId(),"挑战数据","challege","5")));
				Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
				System.out.println("百度ID==" + retMap.get("id") + "   挑战Id=" + challenge.getChallengeId() + "   球场名=" + court.getName() + "  状态=" + retMap.get("status") + "  经度=" + court.getBdPoi() + "   纬度=" + court.getBdPosition());
				if("0".equals(retMap.get("status")!=null?retMap.get("status").toString():"-1")){
					this.saveBaiduLbs(challenge.getChallengeId(),retMap.get("id").toString(),"5");
				}else{
					System.out.println("保存失败：" + map.get("result"));
				}

			}
		}
	}
	//封装百度Lbs需要的数据
	private HashMap<String,Object> getLbsMap(UCourt court,int courtIdInt,int brCourtIdInt,Integer intId,String objectId,String title,String tags,String paramType) throws ParseException, UnsupportedEncodingException {
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("address",court.getAddress());
		hashMap.put("url",Public_Cache.LBS_LOCATION);
		hashMap.put("title", title);
		hashMap.put("tags",tags);
		hashMap.put("latitude",court.getBdPoi());
		hashMap.put("longitude",court.getBdPosition());
		hashMap.put("params_type",paramType);
		hashMap.put("coord_type","3");
		hashMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
		if("4".equals(paramType)){
			hashMap.put("duel_intid",intId);
		}else if("5".equals(paramType)){
			hashMap.put("challege_intid",intId);
		}
		hashMap.put("subcourt_intid",brCourtIdInt);
		hashMap.put("court_intid",courtIdInt);

		hashMap.put("object_id",objectId);
		hashMap.put("date",PublicMethod.getDateToString(new Date(),"yyyy-MM-dd"));
		return hashMap;
	}
	//打印保存lbs信息，并封装错误信息返回
	private HashMap<String,Object> getLbsSaveInfo(HashMap<String,Object> map,String objectId,UCourt court,String type) throws Exception {
		Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
		System.out.println("百度ID==" + retMap.get("id") + "   objectId=" + objectId + "   球场名=" + court.getName() + "  状态=" + retMap.get("status") + "  经度=" + court.getBdPoi() + "   纬度=" + court.getBdPosition());
		if("0".equals(retMap.get("status")!=null?retMap.get("status").toString():"-1")){
			this.saveBaiduLbs(objectId,retMap.get("id").toString(),type);
		}
		else{
			System.out.println("保存失败：" + map.get("result"));
		}
		return null;
	}
	//将lbs百度的数据存到本地库
	private void saveBaiduLbs(String objectId,String lbsData,String lbsType) throws Exception {
		UBaidulbs lbs = new UBaidulbs();
		lbs.setKeyId(WebPublicMehod.getUUID());
		lbs.setObjectId(objectId);
		lbs.setCreatetime(new Date());
		lbs.setLbsType(lbsType);
		lbs.setLbsTable(Constant.BAIDU_TABLE_ID);
		lbs.setLbsData(lbsData);
		this.baseDAO.save(lbs);
	}
	//获取表中对应的int ID字段,sql中对应 intId
	private int getEntityIntId(String sql) throws Exception {
		List<Map<String,Object>> list = this.baseDAO.findSQLMap(sql);
		if(CollectionUtils.isNotEmpty(list)){
			return Integer.parseInt(list.get(0).get("intId").toString());
		}
		return 0;
	}
	/*************************补充百度LBS，约战挑战数据END***********************/

}
