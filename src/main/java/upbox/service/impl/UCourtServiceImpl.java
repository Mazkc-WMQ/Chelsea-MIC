package upbox.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.BdLbsBean;
import upbox.model.PageLimit;
import upbox.model.PageLimitPhoto;
import upbox.model.UBrCourt;
import upbox.model.UBrCourtimage;
import upbox.model.UBrCourtsession;
import upbox.model.UCourt;
import upbox.model.UFollow;
import upbox.model.UMall;
import upbox.model.UOrder;
import upbox.model.UUser;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.UChallengeService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UOrderService;
import upbox.service.UUserService;

/**
 * 球场接口
 *
 */
@Service("ucourtService")
public class UCourtServiceImpl implements UCourtService {
	
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private UOrderService uOrderService;
	
	@Resource 
	private UChallengeService uchallengeService;
	
	@Resource 
	private UDuelService uduelServiceImpl;
	
	@Resource
	private UUserService uuserService;
	
	@Resource
	private LBSService lBSService;
	
	//??咋改
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String today = sdf.format(new Date());
	
	//查询球场列表select部分
	private StringBuffer selSql = new StringBuffer("select 2 as labelb, 0 as emptys, 0 as duels, 0 as challenges, 0 as matches, "
			+"bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime, bct.court_id, bct.score, bct.court_status,"
			+ "bct.court_br_type, " //1-自建 2-第三方其他 3-第三方推荐 4-第三方联营			
			+ "bct.bus_status, "
			+"ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic,"
			+"img.image_court_id, img.img_size_type, img_weight, img.imgurl, img.saveurl,"
			+"case when bct.recommend_court = '1' then 4 "	//推荐球场
			+"when bct.popularity_court = '1' then 3 " //人气球场
			+"when ucc.subcourt_id is not null then 2 "	//我预定过的球场
			+"else -10000 "
			+"end as labela ");
	//查询球场列表join部分
	private StringBuffer joinSql = new StringBuffer("from u_br_court bct "
			+"left join u_court ct on bct.court_id = ct.court_id "
			+"left join u_br_courtimage img on bct.subcourt_id = img.subcourt_id "
			+"left join ("
			+ "select uoc.subcourt_id "
			+ "from u_order_court uoc "
			+ "inner join u_order od on od.order_id = uoc.order_id "
			+ " and od.order_type = '1' "
			+ "and od.user_id = :user_id "
			+ " and (od.orderstatus = '1' "
			+ " or od.orderstatus = '4') "
			+ "group by uoc.subcourt_id) "
			+ "as ucc on ucc.subcourt_id = bct.subcourt_id ");
	//球场分组SQL
	private StringBuffer groupbySql = new StringBuffer("group by bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime,"
			+ " bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status,"
			+"ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, "
			+"img.image_court_id, img.img_size_type, img_weight, img.imgurl, img.saveurl ");
	
	@Override
	public HashMap<String, Object> queryCourtList(HashMap<String, String> params) throws Exception {
		String courtServiceSql = "select * from u_br_courtstaticservice where subcourt_id = ";//初始化球场服务SQL
		String courtImgHql = "from UBrCourtimage img where img.UBrCourt.subcourtId=:subcourt_id_tmp and img.imgSizeType=1 and img.cimgUsingType= 1";//根据下属球场获取球场图片
//		Integer page = params.get(Constant.PAGE) != null ? new Integer(params.get(Constant.PAGE)) : 1;
		PageLimit page = new PageLimit(Integer.parseInt(params.get("page")), 0);//分页
		
		String userStatus = IsNull(params.get(Constant.USER_STATUS));//用户状态
		String iconFirst = IsNull(params.get(Constant.ICON_FIRST));//是否有特殊处理过的球场
		int courtTotalNum = 0;
		int serviceTotalNum = 0;
		List<HashMap<String,Object>> courtList = null;
		StringBuilder courtInfoSql = new StringBuilder();//球场信息
		//球场条件
		StringBuffer whereSql = new StringBuffer(" where bct.court_status =1 "
				+ " and bct.court_br_type <> '2' " //第三方（其他）不显示
				+ "and img.img_size_type = 1"
				+ " and img.cimg_using_type = 2 ");
		StringBuffer orderbySql = new StringBuffer("order by labela desc ");//球场排序
		
		//用户或游客
		if (Constant.NORMAL_USER.equals(userStatus)) {
			params.put(Constant.USER_ID, uuserService.getUserinfoByToken(params).getUserId());
		} else {
			params.put(Constant.USER_ID, Constant.VISITOR);
		}
		//是否能获取到经纬度
		if (params.get("location") != null && !"".equals(params.get("location"))) {
			//离我最近排序－不需分页，有百度LBS分页
			Boolean isBool = this.getCourtListByloc(params,page,whereSql,orderbySql);
			//有百度LBS，就按百度LBS排序
			if (false == isBool) {
				courtInfoSql.append(selSql.toString()
						+ joinSql.toString()//查询球场列表join部分
						+ whereSql.toString()//条件球场列表join部分
						+ groupbySql.toString()//分组球场列表join部分
//						+ orderbySql.toString()//排序球场列表join部分
//						+ " ,bct.createdate,bct.createtime ,bct.court_br_type = '1',bct.court_br_type = '4',bct.court_br_type = '3', bct.score desc "
//						+ ",bct.name asc,bct.subcourt_id "
						);
			}else{
				courtInfoSql.append(selSql.toString()
						+ joinSql.toString()//查询球场列表join部分
						+ whereSql.toString()//条件球场列表join部分
						+ groupbySql.toString()//分组球场列表join部分
//						+ orderbySql.toString()//排序球场列表join部分
						+ "order by bct.score desc "
//						+ " ,bct.createdate,bct.createtime ,bct.court_br_type = '1',bct.court_br_type = '4',bct.court_br_type = '3', bct.score desc "
//						+ ",bct.name asc,bct.subcourt_id "
						);
			}
		} else {
			//获取球场信息
			courtInfoSql.append(
					selSql.toString()
					+ joinSql.toString()
					+whereSql.toString()
					+ groupbySql.toString()
					+ "order by bct.score desc "
//					+ orderbySql.toString()
//					+" , bct.createdate, bct.createtime ,bct.score desc ,bct.court_br_type = '1', bct.court_br_type = '4',bct.court_br_type = '3',bct.score desc "
//					+ ",bct.name asc,bct.subcourt_id "
					+ " limit " + page.getLimit() + " offset " + page.getOffset());
		}
		//球场信息
		courtList = baseDAO.findSQLMap(params, courtInfoSql.toString());
		//获取本页subcourt_id
		List<HashMap<String,String>> serviceList = null;
		
		StringBuilder ucrtsSql = new StringBuilder();
//		球场服务SQL拼接,同时连接自身SQL 如：select * from u_br_courtstaticservice where subcourt_id = '7d2890e8-9857-4750-94fa-e641c3cbcf78' union all select * from u_br_courtstaticservice where subcourt_id = 'a99fcdd7-cc63-4fd5-acaa-89fc31bd2f89'
		for (HashMap<String,?> hashmap : courtList) {
			ucrtsSql.append("select * from ("+courtServiceSql + "'" + hashmap.get(Constant.SUBCOURT_ID) + "' order by orderbyno asc ) a " + "union all ");
		}
		String crtsSql = ucrtsSql.toString();//
		//ucrtsSql连接自己会身union all ，因此将它截取掉
		if(crtsSql.length()>0){
			crtsSql = crtsSql.substring(0, crtsSql.length() - 10);
			//根据subcourt_id获取球场服务
			serviceList = baseDAO.findSQLMap(crtsSql);//最后使用截取掉SQL，查询出球场服务
			courtTotalNum = courtList.size();//球场列表记录数
			serviceTotalNum = serviceList.size();//球场服务列表记录数
		}
		
		String courtType;//球场类型
		String firstSubcourtId;//列表中第一个球场ID
		String subcourtId;//球场ID
		//保证serviceList循环一次
		int w = 0;//没有实际使用上
		List<Object> list=null;// 服务根据subcourt_id再分配给对应球场
		List<UBrCourtimage> imgs=null;//球场图片集合
		for (int i = 0; i < courtTotalNum; i++) {
			subcourtId = courtList.get(i).get(Constant.SUBCOURT_ID).toString();
			courtType = courtList.get(i).get(Constant.COURT_BR_TYPE).toString();
			
			// 第一个球场特殊处理 
			if (page.getOffset() == 0 && i == 0 && iconFirst == null) {
				firstSubcourtId = courtList.get(i).get(Constant.SUBCOURT_ID).toString();
				params.put(Constant.FIRST_COURT_SUBCOURT_ID, firstSubcourtId);//球场图片所需参数
				imgs = baseDAO.find(params, courtImgHql);//球场图片集合
				//更新球场信息中imgurl球场图片地址
				if (imgs != null && imgs.size() > 0) {
					courtList.get(0).put(Constant.IMG_URL, imgs.get(i).getImgurl());
				}
				//若果是自营或者联营球场，那么同时更新空场数、挑战数、约战数，否者是第三方球场只更新约战数
				if (courtType.equals(Constant.UPBOX) || courtType.equals(Constant.ASSOCIATES)) {
					courtList.get(i).put(Constant.EMPTYS, querySessionCountThreeWeeks(subcourtId, Constant.EMPTYS));//空场数
					courtList.get(i).put(Constant.CHALLENGES, querySessionCountThreeWeeks(subcourtId, Constant.CHALLENGES));//挑战数
					courtList.get(i).put(Constant.DUELS, querySessionCountThreeWeeks(subcourtId, Constant.DUELS));//约战数
					// 第一个球场为第三方推荐球场
				} else {
					courtList.get(i).put(Constant.DUELS, query3rdSessionCountThreeWeeks(subcourtId, Constant.DUELS));//约战数
				}
			}

			// 服务根据subcourt_id再分配给对应球场
			list = new ArrayList<>();
			//根据球场服务记录数循环
			for (int j = 0; j < serviceTotalNum; j++) {
				j = w;
				//球场ID等于球场服务ID，并且不只一条，那么将list保存下来
				if (courtList.get(i).get(Constant.SUBCOURT_ID).equals(serviceList.get(j).get(Constant.SUBCOURT_ID))
						&& j + 1 != serviceTotalNum) {
					list.add(serviceList.get(j));
					w++;
				} else if (j + 1 == serviceTotalNum){//如果只有一条数据，那么将list保存下来，并且将球场信息servicelist更新
					list.add(serviceList.get(j));
					courtList.get(i).put(Constant.SERVICE_LIST, list);
				} else {//将球场信息servicelist更新
					courtList.get(i).put(Constant.SERVICE_LIST, list);
					w = j;
					break;
				}
			}
			//折扣场次数
			courtList.get(i).put(Constant.LABEL_B, countLabelBcourt(subcourtId));
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, courtList);
	}

	/**
	 * 参数经纬度不为空，获取球场ID
	 * @param location 经纬度
	 * @return
	 */
	private boolean getCourtListByloc(HashMap<String, String> map, PageLimit page, StringBuffer whereSql,
			StringBuffer orderbySql) throws Exception {
		StringBuffer newSql = new StringBuffer(" select  ct.court_id_int " + joinSql.toString() + whereSql.toString());//获取球场数字类型ID
		String locSql = "";
		List<HashMap<String, Object>> li = baseDAO.findSQLMap(map,newSql.toString());//球场集合
		
		List<Integer> listIntId = new ArrayList<Integer>();//筛选时的最后lbs排序条件传入的int主键
		if (null != li && li.size() > 0) {
			//单独提取球场数字类型的ID
			for (HashMap<String, Object> court : li) {
				listIntId.add((Integer) court.get("court_id_int"));
			}
		}
		//百度LBS查询获取球场信息
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "5000000", "court", "distance:1", null,"2",null);
		//将百度LBS返回的ID进行拼接
		for (BdLbsBean lbstemp : lbsList) {
			locSql += "'" + lbstemp.getObject_id() + "',";
		}
		//若果有返回ID，那么拼接返回的ID作为条件，并且分组，否者拼接球场ID null
		if (locSql.length() > 0) {
			locSql = locSql.substring(0, locSql.length() - 1);
			whereSql.append(" and ct.court_id in (" + locSql + ") ");
			orderbySql.append(" ,field (ct.court_id," + locSql + ") ");
			return false;
		}else{
			whereSql.append(" and ct.court_id in ('null') ");
		}
		return true;
	}
	
	/**
	 * 
	 * 获取场次类型对应场数
	 * @param subCourtId 球场ID
	 * @param querytype 空场，挑战，约战
	 * @return 场次类型对应场数
	 * @throws Exception
	 */
	private Integer querySessionCountThreeWeeks(String subCourtId, String querytype) throws Exception {
		
		HashMap<String, String> params = new HashMap<>();
		String endDate = PublicMethod.getFeuDateString(Public_Cache.SESSION_WEEK - 1);//未来28（SESSION_WEEK）日期
		params.put(Constant.TODAY, today);
		params.put(Constant.END_DATE, endDate);
		params.put(Constant.SUBCOURT_ID, subCourtId);
		
		StringBuilder courtCount = new StringBuilder("select count(s.session_id) as cntno from u_br_courtsession s "); //获取球场场次ID

		switch (querytype) {
		//空场
		case Constant.EMPTYS :
			courtCount.append("where 1 =1");
			courtCount.append(" and s.stdate>= :today");
			courtCount.append(" and s.stdate <= :enddate");
			courtCount.append(" and s.order_status =2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			courtCount.append(" order by stdate desc");
			break;
		//挑战
		case Constant.CHALLENGES :
			courtCount.append(" left join u_order o on s.order_id = o.order_id");
			courtCount.append(" left join u_challenge ch on o.order_id = ch.order_id");
			courtCount.append(" where 1 = 1");
			courtCount.append(" and	ch.challenge_status = '1'");
			courtCount.append(" and o.order_type = 2");
			courtCount.append(" and s.stdate >= :today");
			courtCount.append(" and s.stdate <= :enddate");
			courtCount.append(" and s.order_status != 2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			courtCount.append(" order by s.stdate desc");
			break;
		//约战
		case Constant.DUELS :
			courtCount.append(" left join u_order o on s.order_id = o.order_id");
			courtCount.append(" left join u_duel d on o.order_id = d.order_id");
			courtCount.append(" where 1 = 1");
			courtCount.append(" and	d.duel_status = '1'");
			courtCount.append(" and o.order_type = 3");
			courtCount.append(" and s.stdate >= :today");
			courtCount.append(" and s.stdate <= :enddate");
			courtCount.append(" and s.order_status != 2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			courtCount.append(" order by s.stdate desc");
			break;
			
		default:
			break;
		}
		
		List<HashMap<String, Object>> list = new ArrayList<>();
		list = baseDAO.findSQLMap(params, courtCount.toString());//球场场次ID集合
		//返回不同状态的球场场次数
		if (list != null && list.size() > 0) {
			Integer cntno = Integer.parseInt(list.get(0).get(Constant.SQL_COUNT_NUM)
					.toString());
			return cntno;
		}
		return 0;
	}
	
	/**
	 * 
	 * 获取场次订单对应场数（第三方）
	 * @param subCourtId 球场ID
	 * @param querytype 约战
	 * @return 场次类型对应场数
	 * @throws Exception
	 */
	private Integer query3rdSessionCountThreeWeeks(String subCourtId, String querytype) throws Exception {
		
		HashMap<String, String> params = new HashMap<>();
		String endDate = PublicMethod.getFeuDateString(Public_Cache.SESSION_WEEK - 1);//未来28（SESSION_WEEK）日期
		params.put(Constant.TODAY, today);
		params.put(Constant.END_DATE, endDate);
		params.put(Constant.SUBCOURT_ID, subCourtId);
		
		StringBuilder courtCount = new StringBuilder("select count(s.order_court_id) as cntno from u_order_court s ");//获取球场场次ID
		//约战
		if (Constant.DUELS  == querytype) {
			courtCount.append(" left join u_order o on s.order_id = o.order_id");
			courtCount.append(" left join u_duel d on o.order_id = d.order_id");
			courtCount.append(" where 1 = 1");
			courtCount.append(" and	d.duel_status = '1'");
			courtCount.append(" and o.order_type = 3");
			courtCount.append(" and s.stdate >= :today");
			courtCount.append(" and s.stdate <= :enddate");
			courtCount.append(" and s.session_useing_status = 2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
		}
		
		List<HashMap<String, Object>> list = new ArrayList<>();
		list = baseDAO.findSQLMap(params, courtCount.toString());
		//返回不同状态的球场场次数
		if (list != null && list.size() > 0) {
			Integer cntno = Integer.parseInt(list.get(0).get(Constant.SQL_COUNT_NUM)
					.toString());
			return cntno;
		}
		return 0;
	}
	
	/**
	 * 
	 * 获取用户信息
	 * @param userInfoList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void getSessionUseingInfo(List<HashMap> userInfoList) throws Exception {

		String home_teamname ;
		String home_short_teamname ;
		String home_teamimgurl ;
		String away_teamname ;
		String away_short_teamname ;
		String away_teamimgurl ;
		String f_team_id ;
		String x_team_id ;
		String wincount ;
		String responseprice ;
		String status ;
		String champid ;
		String nickname ;
		String username ;
		String imgurl ;
		
		int listSize = userInfoList.size();
		UBrCourtsession csession = new UBrCourtsession();
		HashMap<String, String> retmap = new HashMap<String, String>();
		
		retmap.put(Constant.HOME_TEAM_FULL_NAME, null);
		retmap.put(Constant.HOME_TEAM_IMG, null);
		retmap.put(Constant.HOME_TEAM_ID , null);
		retmap.put(Constant.AWAY_TEAM_FULL_NAME, null);
		retmap.put(Constant.AWAY_TEAM_IMG, null);
		retmap.put(Constant.AWAY_TEAM_ID, null);
		retmap.put(Constant.USER_NAME, null);
		retmap.put(Constant.USER_IMG, null);
		retmap.put(Constant.VS_SCORE, null);
		retmap.put(Constant.RESPONSE_PRICE, null);
		retmap.put(Constant.STATUS, null);
		retmap.put(Constant.CHAMP_ID, null);

		// List<HashMap> arrayList = l.stream().filter(v ->
		// !v.containsKey(Constant.SESSION_ID)).collect(Collectors.toList());
		// //System.out.println(JSON.toJSONString(arrayList));

		// qiqi 约战详情
		List<HashMap<String, Object>> duelList = uduelServiceImpl.getDuelInfoBySession(userInfoList);
		for (int i = 0; i < listSize; i++) {
			retmap = new HashMap<String, String>();
			home_teamname = IsNull(duelList.get(i).get(Constant.HOME_TEAM_NAME));
			home_short_teamname = IsNull(duelList.get(i).get(Constant.HOME_TEAM_SHORT_NAME));
			home_teamimgurl = IsNull(duelList.get(i).get(Constant.HOME_TEAM_IMG));
			away_teamname = IsNull(duelList.get(i).get(Constant.AWAY_TEAM_NAME));
			away_short_teamname = IsNull(duelList.get(i).get(Constant.AWAY_TEAM_SHORT_NAME));
			away_teamimgurl = IsNull(duelList.get(i).get(Constant.AWAY_TEAM_IMG));
			f_team_id = IsNull(duelList.get(i).get(Constant.F_TEAM_ID));
			x_team_id = IsNull(duelList.get(i).get(Constant.X_TEAM_ID));
			wincount = IsNull(duelList.get(i).get(Constant.WIN_COUNT));
			responseprice = IsNull(duelList.get(i).get(Constant.RESPONSE_PRICE));
			status = IsNull(duelList.get(i).get(Constant.STATUS));
			champid = IsNull(duelList.get(i).get(Constant.CHAMP_ID));
			nickname = IsNull(duelList.get(i).get(Constant.NICKNAME));
			username = IsNull(duelList.get(i).get(Constant.USER_NAME));
			imgurl = IsNull(duelList.get(i).get(Constant.IMG_URL));

			retmap.put(Constant.HOME_TEAM_FULL_NAME, home_short_teamname != null 
					? home_short_teamname : (home_teamname != null ? home_teamname : null));
			retmap.put(Constant.HOME_TEAM_IMG, home_teamimgurl);
			retmap.put(Constant.HOME_TEAM_ID , f_team_id);
			retmap.put(Constant.AWAY_TEAM_FULL_NAME, away_short_teamname != null 
					? away_short_teamname : (away_teamname != null ? away_teamname : null));
			retmap.put(Constant.AWAY_TEAM_IMG, away_teamimgurl);
			retmap.put(Constant.AWAY_TEAM_ID, x_team_id);
			retmap.put(Constant.USER_NAME, username != null ? username : nickname);
			retmap.put(Constant.USER_IMG, imgurl);
			retmap.put(Constant.VS_SCORE, null);
			retmap.put(Constant.RESPONSE_PRICE, responseprice);
			retmap.put(Constant.STATUS, status);
			retmap.put(Constant.CHAMP_ID, champid);
//			System.out.println(JSON.toJSONString(retmap));
			userInfoList.get(i).put(Constant.SESSION_ORDER_INFO, retmap);
			
		}

		// yc 挑战
		List<HashMap<String, Object>> challengeList = uchallengeService.getWithCourtInfo(userInfoList);
		for (int i = 0; i < listSize; i++) {
			home_teamname = IsNull(challengeList.get(i).get(Constant.HOME_TEAM_NAME));
			home_short_teamname = IsNull(challengeList.get(i).get(Constant.HOME_TEAM_SHORT_NAME));
			home_teamimgurl = IsNull(challengeList.get(i).get(Constant.HOME_TEAM_IMG));
			away_teamname = IsNull(challengeList.get(i).get(Constant.AWAY_TEAM_NAME));
			away_short_teamname = IsNull(challengeList.get(i).get(Constant.AWAY_TEAM_SHORT_NAME));
			away_teamimgurl = IsNull(challengeList.get(i).get(Constant.AWAY_TEAM_IMG));
			f_team_id = IsNull(challengeList.get(i).get(Constant.F_TEAM_ID));
			x_team_id = IsNull(challengeList.get(i).get(Constant.X_TEAM_ID));
			wincount = IsNull(challengeList.get(i).get(Constant.WIN_COUNT));
			responseprice = IsNull(challengeList.get(i).get(Constant.RESPONSE_PRICE));
			status = IsNull(challengeList.get(i).get(Constant.STATUS));
			champid = IsNull(challengeList.get(i).get(Constant.CHAMP_ID));
			
			//有挑战方
			if (f_team_id != null) {
				retmap = new HashMap<String, String>();
				retmap.put(Constant.HOME_TEAM_FULL_NAME, home_short_teamname != null ? home_short_teamname: (home_teamname != null 
						? home_teamname : null));
				retmap.put(Constant.HOME_TEAM_IMG, home_teamimgurl);
				retmap.put(Constant.HOME_TEAM_ID , f_team_id);
				retmap.put(Constant.AWAY_TEAM_FULL_NAME, away_short_teamname != null ? away_short_teamname: (away_teamname != null 
						? away_teamname : null));
				retmap.put(Constant.AWAY_TEAM_IMG, away_teamimgurl);
				retmap.put(Constant.AWAY_TEAM_ID, x_team_id);
				retmap.put(Constant.USER_NAME, null);
				retmap.put(Constant.USER_IMG, null);
				retmap.put(Constant.VS_SCORE, wincount);
				retmap.put(Constant.RESPONSE_PRICE, responseprice);
				retmap.put(Constant.STATUS, status);
				retmap.put(Constant.CHAMP_ID, champid);

				userInfoList.get(i).put(Constant.SESSION_ORDER_INFO, retmap);
			}
		}

		// dqr 用户
		String sessionidtmp;
		HashMap<String, String> map = null;
		for (int i = 0; i < listSize; i++) {
			sessionidtmp = IsNull(userInfoList.get(i).get(Constant.SESSION_ID));
			csession = baseDAO.get(UBrCourtsession.class, sessionidtmp);
			if (csession != null && csession.getOrderId() != null && csession.getOrderId().length() > 0) {
				UOrder uorder = baseDAO.get(UOrder.class, csession.getOrderId());
				if (uorder != null && Constant.COURT.equals(uorder.getOrderType())) {
					// 球场预定
					if (uorder.getUUser() != null && uorder.getUUser().getUserId() != null) {
						map = new HashMap<String, String>();
						map.put(Constant.USERID, uorder.getUUser().getUserId());
						HashMap<String, String> userMap = uuserService.getUserinfoByOtherUserId(map);
						if (userMap != null) {
							retmap = new HashMap<String, String>();
							String nicknameSring = userMap.get(Constant.NICKNAME);
							retmap.put(Constant.HOME_TEAM_FULL_NAME, null);
							retmap.put(Constant.HOME_TEAM_IMG, null);
							retmap.put(Constant.HOME_TEAM_ID, null);
							retmap.put(Constant.AWAY_TEAM_FULL_NAME, null);
							retmap.put(Constant.AWAY_TEAM_IMG, null);
							retmap.put(Constant.AWAY_TEAM_ID, null);
							retmap.put(Constant.USER_NAME, null);
							retmap.put(Constant.USER_IMG, null);
							retmap.put(Constant.VS_SCORE, null);
							retmap.put(Constant.RESPONSE_PRICE, null);
							retmap.put(Constant.STATUS, null);
							retmap.put(Constant.CHAMP_ID, null);
							retmap.put(Constant.USER_NAME, userMap.get(Constant.USER_NAME) != null
									? userMap.get(Constant.USER_NAME) : nicknameSring);
							retmap.put(Constant.USER_IMG, IsNull(userMap.get(Constant.USER_IMG)));
							
							userInfoList.get(i).put(Constant.SESSION_ORDER_INFO, retmap);
						}
						// retmap.put(Constant.USER_IMG, null);
					}
				}
			}
		}

		// List<HashMap<String, Object>>
		// dueList=uduelServiceImpl.getDuelInfoBySession(l);
		// for (int i = 0; i < l.size(); i++) {
		// String home_teamname =
		// dueList.get(i).get(Constant.HOME_TEAM_NAME)!=null?dueList.get(i).get(Constant.HOME_TEAM_NAME).toString():null;
		// String home_short_teamname =
		// dueList.get(i).get(Constant.HOME_TEAM_SHORT_NAME)!=null?dueList.get(i).get(Constant.HOME_TEAM_SHORT_NAME).toString():null;
		// String home_teamimgurl =
		// dueList.get(i).get(Constant.HOME_TEAM_IMG)!=null?dueList.get(i).get(Constant.HOME_TEAM_IMG).toString():null;
		// String away_teamname =
		// dueList.get(i).get(Constant.AWAY_TEAM_NAME)!=null?dueList.get(i).get(Constant.AWAY_TEAM_NAME).toString():null;
		// String away_short_teamname =
		// dueList.get(i).get(Constant.AWAY_TEAM_SHORT_NAME)!=null?dueList.get(i).get(Constant.AWAY_TEAM_SHORT_NAME).toString():null;
		// String away_teamimgurl =
		// dueList.get(i).get(Constant.AWAY_TEAM_IMG)!=null?dueList.get(i).get(Constant.AWAY_TEAM_IMG).toString():null;
		// String f_team_id =
		// dueList.get(i).get(Constant.F_TEAM_ID)!=null?dueList.get(i).get(Constant.F_TEAM_ID).toString():null;
		// String x_team_id =
		// dueList.get(i).get(Constant.X_TEAM_ID)!=null?dueList.get(i).get(Constant.X_TEAM_ID).toString():null;
		// String wincount =
		// dueList.get(i).get(Constant.WIN_COUNT)!=null?dueList.get(i).get(Constant.WIN_COUNT).toString():null;
		// }
		//

		//

		// return true;
	}
	/**
	 * 判断null
	 * @param obj
	 * @return
	 */
	private String IsNull(Object obj) {
		return obj != null ? obj.toString() : null;
	}
	
	/**
	 * 
	 * 查询场次数（按类型）
	 * @param subcourtid 下属球场ID
	 * @param querytype 场次类型
	 * @param date 日期
	 * @return Integer 场次数（按类型）
	 * @throws Exception
	 */
	private Integer querySessionCountOneDay(String subcourtid, String querytype, String date) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		List<HashMap<String, Object>> courtCountList = new ArrayList<>();
		params.put(Constant.DATE, date);
		params.put(Constant.SUBCOURT_ID, subcourtid);
		
		StringBuilder courtCount = new StringBuilder("select count(s.session_id) as cntno from u_br_courtsession s ");//获取球场场次ID
		switch (querytype) {
		//空场
		case Constant.EMPTYS:
			courtCount.append(" where 1=1");
			courtCount.append(" and s.stdate = :date ");
			courtCount.append(" and s.order_status =2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			break;
		//挑战
		case Constant.CHALLENGES:
			courtCount.append(" left join u_order o on s.order_id = o.order_id");
			courtCount.append(" where 1=1");
			courtCount.append(" and s.stdate = :date");
			courtCount.append(" and o.order_type = '2'");
			courtCount.append(" and s.order_status != 2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			break;
		//约战
		case Constant.DUELS:
			courtCount.append(" left join u_order o on s.order_id = o.order_id");
			courtCount.append(" where 1=1");
			courtCount.append(" and s.stdate = :date");
			courtCount.append(" and o.order_type = '3'");
			courtCount.append(" and s.order_status != 2");
			courtCount.append(" and s.subcourt_id = :subcourt_id");
			break;

		default:
			break;
		}

		// 无法订过期场次
		String today = sdf.format(new Date());
		//如果日期是当天，那么预订场次的时间不能小于场次结束日期
		if (today.equals(date)) {
			SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
			String ctime = stf.format(new Date());
			params.put(Constant.CURRENT_TIME, ctime);
			courtCount.append(" and s.endtime > :ctime");//拼接语句
		}
		courtCount.append(" order by stdate desc");//排序，可以去掉，只使用了记录数
		//不同类型下球场场次集合
		courtCountList = baseDAO.findSQLMap(params, courtCount.toString());
		//返回不同状态的球场场次数
		if (courtCountList != null && courtCountList.size() > 0) {
			Integer cntno = Integer.parseInt(courtCountList.get(0).get(Constant.SQL_COUNT_NUM).toString());
			return cntno;
		} else {
			return 0;
		}
	}

	@Override
	public boolean saveCourtSessionOrderStatus(String orderId) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Constant.ORDER_ID, orderId);//订单ID
		List<UBrCourtsession> listBrCourtsession = baseDAO.find(map, "from UBrCourtsession where orderId = :orderId ");
		//订单存在
		if (listBrCourtsession != null&&listBrCourtsession.size()>0) {
			for (UBrCourtsession uBrCourtsession : listBrCourtsession) {
				uBrCourtsession.setOrderStatus(Constant.ORDER_BOOKED);
				baseDAO.update(uBrCourtsession);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public HashMap<String, Object> queryCourtDetail(HashMap<String, String> params) throws Exception {
		// 球场详细信息
		String courtDetailSql = selSql.toString()
				+ joinSql.toString()
				+ " where bct.subcourt_id = :subcourt_id" 
				+ " and bct.court_status =1 "
				+ " and bct.court_br_type <> '4' " 
				+ " and img.img_size_type = 1 " 
				+ " and img.cimg_using_type = 1 "
				+ groupbySql.toString()
				+ " order by labela asc ,bct.score desc";
		// 球场服务
		String courtServiceSql = "SELECT * from u_br_courtstaticservice where subcourt_id = :scourtid order by orderbyno asc";
		// 球场图片URL
		String iconSql = "select img.imgurl from u_br_courtimage img where img.img_size_type =1 and img.cimg_using_type = 2 and img.subcourt_id = :scourtid";
		//是否是游客标示
		String userStatus = IsNull(params.get(Constant.USER_STATUS));
		//如果是登录用户，根据token获取用户ID，否者返给前端-10000
		if (Constant.NORMAL_USER.equals(userStatus)) {
			params.put(Constant.USER_ID, uuserService.getUserinfoByToken(params).getUserId());
		} else {
			params.put(Constant.USER_ID, Constant.VISITOR);
		}
		//球场详情list
		List<HashMap<String, Object>> courtDetailList = baseDAO.findSQLMap(params, courtDetailSql);

		String subcourtid;
		String courtType;
		List<Object> serviceList=null;// 球场服务list
		List<HashMap<String, Object>> iconList =null;//球场图片list
		int courtDetailListSize = courtDetailList.size();
		for (int i = 0; i < courtDetailListSize; i++) {
			subcourtid = courtDetailList.get(i).get(Constant.SUBCOURT_ID).toString();
			courtType = courtDetailList.get(i).get(Constant.COURT_BR_TYPE).toString();
			//若果是自营或者联营球场，那么同时更新空场数、挑战数、约战数，否者是第三方球场只更新约战数
			if (courtType.equals(Constant.UPBOX ) || courtType.equals(Constant.ASSOCIATES)) {
				courtDetailList.get(i).put(Constant.EMPTYS, querySessionCountThreeWeeks(subcourtid, Constant.EMPTYS));//空场数
				courtDetailList.get(i).put(Constant.CHALLENGES, querySessionCountThreeWeeks(subcourtid, Constant.CHALLENGES));//挑战数
				courtDetailList.get(i).put(Constant.DUELS, querySessionCountThreeWeeks(subcourtid, Constant.DUELS));//约战数
			} else {
				courtDetailList.get(i).put(Constant.DUELS, query3rdSessionCountThreeWeeks(subcourtid, Constant.DUELS));//约战数
			}

			params.put(Constant.SUBCOURT_SERVICE_BY_ID, subcourtid);
			serviceList = baseDAO.findSQLMap(params, courtServiceSql);// 球场服务
			iconList = baseDAO.findSQLMap(params, iconSql);//球场图片

			courtDetailList.get(i).put(Constant.SERVICE_LIST, serviceList);// 球场服务
			courtDetailList.get(i).put(Constant.LABEL_B, countLabelBcourt(subcourtid));//折扣场次数
			courtDetailList.get(i).put(Constant.ICON_URL, IsNull(iconList.get(0).get(Constant.IMG_URL)));//球场图片连接地址
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, courtDetailList.get(0));
	}

	@Override
	public HashMap<Object, Object> queryCourtDetailByOrderid(HashMap<String, String> params) throws Exception {
		
		// 根据订单ID查询
		String sqlgengeralinfo = "SELECT bct.name,ct.address,ct.position,ct.poi,img.cimg_using_type, img.saveurl, img.imgurl, img_size_type, bct.subcourt_id, ct.traffic, ct.telephone, ct.court_id FROM u_court ct LEFT JOIN u_br_court bct ON ct.court_id = bct.court_id LEFT JOIN u_br_courtimage img ON img.subcourt_id = bct.subcourt_id WHERE img.img_size_type=1 AND img.cimg_using_type=5   and bct.subcourt_id in (select cs.subcourt_id from u_br_courtsession cs where  cs.order_id = :orderId) ";
		// 根据球场ID查询 -- 第三方球场
		String sqlgengeralinfo_ = "SELECT bct.name,ct.address,img.cimg_using_type, img.saveurl, img.imgurl, img_size_type,ct.position,ct.poi, bct.subcourt_id, ct.traffic, ct.telephone, ct.court_id "
				+ "FROM u_court ct LEFT JOIN u_br_court bct ON ct.court_id = bct.court_id  "
				+ "LEFT JOIN u_br_courtimage img ON img.subcourt_id = bct.subcourt_id "
				+ "WHERE bct.subcourt_id = :subcourt_id and ((img.img_size_type=1 AND img.cimg_using_type=5) or (1 = 1 ))";
		//根据球场ID查询-自营、联营
		String sqlgengeralinfo_self = "SELECT bct.name,ct.address,img.cimg_using_type, img.saveurl, img.imgurl, img_size_type,ct.position,ct.poi, bct.subcourt_id, ct.traffic, ct.telephone, ct.court_id "
				+ "FROM u_court ct LEFT JOIN u_br_court bct ON ct.court_id = bct.court_id  "
				+ "LEFT JOIN u_br_courtimage img ON img.subcourt_id = bct.subcourt_id "
				+ "WHERE bct.subcourt_id = :subcourt_id and img.img_size_type=1 AND img.cimg_using_type=5 ";
		
		List<HashMap<Object, Object>> generallist=null;//球场集合
		//如果订单ID不为空，那么根据订单ID查询出球场集合
		if (null != params.get("orderId") && !"".equals(params.get("orderId"))) {
			generallist = baseDAO.findSQLMap(params, sqlgengeralinfo);//根据订单ID查询球场信息
			
		//根据球场ID，查询球场集合，如果球场是自营的，图片条件必需，否者第三方，图片条件不必需
		} else if (null != params.get("self") && !"".equals(params.get("self")) && "self".equals(params.get("self"))) {
			generallist = baseDAO.findSQLMap(params, sqlgengeralinfo_self);
		} else {
			generallist = baseDAO.findSQLMap(params, sqlgengeralinfo_);
		}
		//如果球场信息不为空，那么取球场信息第一条，否者初始化信息
		if (generallist != null && generallist.size() > 0) {
			return generallist.get(0);
		} else {
			HashMap<Object, Object> mapret = new HashMap<>();
			mapret.put("name", null);
			mapret.put("address", null);
			mapret.put("position", null);
			mapret.put("poi", null);
			mapret.put("cimg_using_type", null);
			mapret.put("saveurl", null);
			mapret.put(Constant.IMG_URL, null);
			mapret.put("img_size_type", null);
			mapret.put(Constant.SUBCOURT_ID, null);
			mapret.put("traffic", null);
			mapret.put("telephone", null);
			mapret.put("court_id", null);
			return mapret;
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> querySessionByDate(HashMap<String, String> params) throws Exception {
		
		//场次预订信息
		String sessionBookingInfo = "select o.order_type,bct.name,img.imgurl,s.session_id,s.subcourt_id,s.createuser,s.stdate,s.sttime,s.endtime,"
												+ " s.session_duration,s.session_price,s.discount_price,s.member_price,s.activity_price,s.session_status,s.order_status,s.enddate,s.createdate,s.week,"
												+ " s.session_br_useing_status,s.sort,s.sing_update_status,s.order_id,"
												+ " case when s.activity_price is not null then s.activity_price "
												+ " when s.member_price is not null then s.member_price "
												+ " when s.discount_price is not null then s.discount_price "
												+ " when s.fav_price is not null then s.fav_price "
												+ " else null "
												+ " end as fav_price,"
												+ " date_format(s.stdate,'%m月%d日')as showdate,date_format(s.stdate,'%Y-%m-%d')as formatdate "
												+ " from u_br_courtsession s "
												+ " left join u_order o on s.order_id = o.order_id "
												+ " left join u_br_court bct on bct.subcourt_id = s.subcourt_id "
												+ " left join u_br_courtimage img on img.subcourt_id = bct.subcourt_id "
												+ " where img.img_size_type = 1 "
												+ " and img.cimg_using_type = 2 "
												+ " and s.subcourt_id = :subcourt_id "
												+ " and s.stdate = :stdate "
												+ " order by sttime asc";
		
		List<HashMap> sessionBookingInfoList = baseDAO.findSQLMap(params, sessionBookingInfo);//场次预订集合

		if (sessionBookingInfoList.size() > 0) {
			getSessionUseingInfo(sessionBookingInfoList);
		}
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
		// 周几转换
		String week="";
		String session_id="";// 球场球场ID
		String stdate = "";// 预订场次开始日期
		String sttime = "";// 预订场次开始时间
		String endtime = "";// 预订场次结束日期
		String ctime = "";// 预订场次结束时间
		String today = "";// 当前时间
		for (int i = 0; i < sessionBookingInfoList.size(); i++) {
			// 周几转换
		    week = IsNull(sessionBookingInfoList.get(i).get("week"));
			if (week != null) {
				sessionBookingInfoList.get(i).put("week", WebPublicMehod.checkWeekString(week));//更新星期
			}

			session_id = sessionBookingInfoList.get(i).get(Constant.SESSION_ID).toString();
			sessionBookingInfoList.get(i).put(Constant.LABEL_B, countLabelBSession(session_id));//更新折扣商品数

			/***** 去掉过期场次 ****/
		    stdate = IsNull(sessionBookingInfoList.get(i).get("stdate"));
			sttime = IsNull(sessionBookingInfoList.get(i).get("sttime"));
			endtime = IsNull(sessionBookingInfoList.get(i).get("endtime"));
			ctime = stf.format(new Date());
			today = sdf.format(new Date());
			//如果是当天，同时预订结束时间小于当前时间，那么订单状态为-1（过期）
			if (stdate != null && stdate.equals(today) && (sttime.compareTo(ctime) < 0)
					&& (endtime.compareTo(ctime) < 0)) {
				sessionBookingInfoList.get(i).put("order_status", "-1");
			}
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, sessionBookingInfoList);
	}

	/**
	 * 
	 * 查询折扣商品数
	 * @param session_id 场次ID
	 * @return Integer 折扣商品数
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Integer countLabelBSession(String session_id) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		params.put(Constant.SESSION_ID, session_id);
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		params.put(Constant.CURRENT_DATE, cdate);
		params.put(Constant.CURRENT_TIME, ctime);
		// 场次信息
		UBrCourtsession session = baseDAO.get(UBrCourtsession.class, session_id);
		Integer mallTJ = 0;
		Integer mallXM = 0;

		// 特价商品数
		String tjmallsql = "SELECT count(*) AS cntnopro FROM u_mall m "
								+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
								+ "WHERE m.price>0 AND m.price_type = '3' "
								+ "AND img.img_size_typwe = 1 "
								+ "AND img.mimg_using_type = 1 "
								+ "AND ((m.stdate< :cdate AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
								+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) "
								+ "AND m.product_status = 1 "
								+ "AND pro_id IN (SELECT product_id FROM u_br_courtpro WHERE session_id = :session_id)";
		//限时商品数
		String xmmallsql = "SELECT count(*) AS cntnopro FROM u_mall m "
								+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
								+ "WHERE m.price=0 AND m.price_type = '3' "
								+ "AND img.img_size_typwe = 1 "
								+ "AND img.mimg_using_type = 1 "
								+ "AND ((m.stdate< :cdate AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
								+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) "
								+ "AND m.product_status = 1 "
								+ "AND pro_id IN (SELECT product_id FROM u_br_courtpro WHERE session_id = :session_id)";

		// 查询有多少 特价 商品
		List<HashMap> tjmalllist = baseDAO.findSQLMap(params, tjmallsql);
		if (tjmalllist != null && tjmalllist.size() > 0) {
			mallTJ = Integer.parseInt(tjmalllist.get(0).get("cntnopro").toString());
		}

		// 查训有多少 限时免费 商品
		List<HashMap> xmmalllist = baseDAO.findSQLMap(params, xmmallsql);
		if (xmmalllist != null && xmmalllist.size() > 0) {
			mallXM = Integer.parseInt(xmmalllist.get(0).get("cntnopro").toString());
		}
		if (session.getFavPrice() != null && session.getFavPrice() >= 0 && mallXM > 1) {
			return 1; // 大促
		}

		if ((session.getFavPrice() != null && session.getFavPrice() == 0) || mallXM > 0) {
			return 2; // 限免
		}

		if ((session.getFavPrice() != null && session.getFavPrice() > 0) || mallTJ > 0) {
			return 3; // 特价
		}
		return 10000;
	}
	
	/**
	 * 查询折扣场次数
	 * @param subcourtid 子球场ID
	 * @return Integer 折扣场次数
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Integer countLabelBcourt(String subcourtid) throws Exception {

		HashMap<String, String> params = new HashMap<>();
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		params.put(Constant.TODAY, sdf.format(new Date()));
		params.put(Constant.SUBCOURT_ID, subcourtid);
		params.put(Constant.CURRENT_DATE, cdate);
		params.put(Constant.CURRENT_TIME, ctime);
		Integer countsessionTJ = 0;
		Integer countsessionXM = 0;
		Integer mallTJ = 0;
		Integer mallXM = 0;
		
		//特价场次数
		String sqltjsession = " SELECT count(*) AS cntnofav FROM u_br_courtsession s "
									+ "WHERE s.stdate>= :today "
									+ "AND s.stdate <= (SELECT DATE_ADD(:today, INTERVAL "
									+ (Public_Cache.SESSION_WEEK - 1)
									+ "   DAY)) "
									+ "AND   (s.fav_price > 0 or s.discount_price> 0 or s.member_price >0 or s.activity_price>0)  "
									+ "AND s.subcourt_id =  :subcourt_id union all ";
		//限时免费场次数
		String sqlxmsession = " SELECT count(*) AS cntnofav FROM u_br_courtsession s "
									+ "WHERE s.stdate>= :today "
									+ "AND s.stdate <= (SELECT DATE_ADD(:today, INTERVAL "
									+ (Public_Cache.SESSION_WEEK - 1)
									+ "   DAY)) "
									+ "AND   (s.fav_price = 0 or s.discount_price= 0 or s.member_price = 0 or s.activity_price = 0)  "
									+ "AND s.subcourt_id =  :subcourt_id  ";

		// 查训有多少场 特价 场次
		List<HashMap> tjlist = baseDAO.findSQLMap(params, sqltjsession + sqlxmsession);
		if (null != tjlist && tjlist.size() > 0) {
			countsessionTJ = Integer.parseInt(String.valueOf(tjlist.get(0).get("cntnofav")));
			countsessionXM = Integer.parseInt(String.valueOf(tjlist.get(1).get("cntnofav")));
		}

		//特价商品数
		String tjmallsql = "SELECT count(*) as cntnopro FROM u_mall m "
								+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
								+ "WHERE m.price>0 and  m.price_type = '3'  "
								+ "and img.img_size_typwe = 1  "
								+ "AND img.mimg_using_type = 1 "
								+ "AND ((m.stdate< :cdate AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
								+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) AND m.product_status = 1 "
								+ "AND pro_id IN (SELECT product_id FROM u_br_courtpro WHERE session_id IN "
								+ "( select session_id from u_br_courtsession s "
								+ "where s.subcourt_id = :subcourt_id "
								+ "and s.stdate>= :cdate "
								+ "AND s.stdate <= (SELECT DATE_ADD(:cdate, INTERVAL "
								+ (Public_Cache.SESSION_WEEK - 1) + " DAY))  )) union all ";
		//限时免费商品数
		String xmmallsql = "SELECT count(*) as cntnopro FROM u_mall m "
								+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
								+ "WHERE m.price=0 and  m.price_type = '3'  "
								+ "and img.img_size_typwe = 1  "
								+ "AND img.mimg_using_type = 1 "
								+ "AND ((m.stdate< :cdate AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
								+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) "
								+ "AND m.product_status = 1 "
								+ "AND pro_id IN "
								+ "(SELECT product_id FROM u_br_courtpro "
								+ "WHERE session_id IN "
								+ "( select session_id from u_br_courtsession s "
								+ "where s.subcourt_id = :subcourt_id and s.stdate>= :cdate "
								+ "AND s.stdate <= (SELECT DATE_ADD(:cdate, INTERVAL "
								+ (Public_Cache.SESSION_WEEK - 1) + " DAY))  ))";

		List<HashMap> tjmalllist = baseDAO.findSQLMap(params, tjmallsql + xmmallsql);
		if (null != tjmalllist && tjmalllist.size() > 0) {
			mallTJ = Integer.parseInt(String.valueOf(tjmalllist.get(0).get("cntnopro")));
			mallXM = Integer.parseInt(String.valueOf(tjmalllist.get(1).get("cntnopro")));
		}

		if (mallXM > 1 && countsessionXM > 1) {
			return 1; // 大促
		}

		if (mallXM > 0 || countsessionXM > 0) {
			return 2; // 限免
		}

		if (countsessionTJ > 0 || mallTJ > 0) {
			return 3; // 特价
		}
		return 10000;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public HashMap<String, Object> querySessionLeft(HashMap<String, String> params) throws Exception {
		params.put(Constant.TODAY, sdf.format(new Date()));
		//获取剩余场次
		String sqlString = "SELECT 0 as emptys,0 as notemptys,"
								+ " (SELECT MIN(cc.fav_price) FROM "
								+ "(SELECT CASE WHEN ss.activity_price IS NOT NULL THEN ss.activity_price "
								+ "WHEN ss.member_price IS NOT NULL THEN ss.member_price "
								+ "WHEN ss.discount_price IS NOT NULL THEN ss.discount_price "
								+ "WHEN ss.fav_price IS NOT NULL THEN ss.fav_price "
								+ "ELSE ss.session_price "
								+ "END AS fav_price "
								+ "FROM u_br_courtsession AS ss "
								+ "WHERE ss.stdate>= :today "
								+ "AND order_status = 2 "
								+ "GROUP BY ss.stdate) AS cc) AS minprice, "
								+ "(SELECT Max(cc.fav_price) FROM "
								+ "(SELECT CASE WHEN ss.activity_price IS NOT NULL THEN ss.activity_price "
								+ "WHEN ss.member_price IS NOT NULL THEN ss.member_price "
								+ "WHEN ss.discount_price IS NOT NULL THEN ss.discount_price "
								+ "WHEN ss.fav_price IS NOT NULL THEN ss.fav_price "
								+ "ELSE ss.session_price "
								+ "END AS fav_price "
								+ "FROM u_br_courtsession AS ss "
								+ "WHERE ss.stdate>= :today AND order_status = 2 "
								+ "GROUP BY ss.stdate) AS cc) AS maxprice, "
								+ "date_format(s.stdate,'%Y-%m-%d') AS formatdate, "
								+ "date_format(s.stdate,'%m月%d日')AS stdate, s.order_status , "
								+ " (SELECT COUNT(sss.stdate) FROM u_br_courtsession sss "
								+ "WHERE sss.order_status = 2 "
								+ "AND sss.subcourt_id = s.subcourt_id "
								+ "AND sss.stdate = s.stdate) AS leftno, "
								+ "s.subcourt_id  "
								+ "FROM u_br_courtsession s "
								+ "WHERE s.stdate>= :today "
								+ "and subcourt_id = :subcourt_id "
//								+ "AND order_status = 2 "
								+ "GROUP BY s.stdate "
								+ "ORDER BY s.stdate ASC limit "
								+ Public_Cache.SESSION_WEEK + " offset 0";
		
		List<HashMap> sessionLeftList = baseDAO.findSQLMap(params, sqlString);//可使用场次集合
		String formatdate="";
		String subcourtid = "";
		String date = "";
		Integer total = 0;
		String dateString=""; 
		if(sessionLeftList!=null&&sessionLeftList.size()>0){
			for (int i = 0; i < sessionLeftList.size(); i++) {
				formatdate = sessionLeftList.get(i).get("formatdate").toString();
				sessionLeftList.get(i).put("formatweek", getWeekOfDate(sdf.parse(formatdate)));
				subcourtid = params.get(Constant.SUBCOURT_ID); // l.get(i).get(Constant.SUBCOURT_ID).toString();
				date = sessionLeftList.get(i).get("formatdate").toString();
				//空场数
				sessionLeftList.get(i).put(Constant.EMPTYS, querySessionCountOneDay(subcourtid, Constant.EMPTYS, date));
				//非空场数
				total = querySessionCountOneDay(subcourtid, Constant.DUELS, date) + querySessionCountOneDay(subcourtid, Constant.CHALLENGES, date);
				sessionLeftList.get(i).put("notemptys", total);
				//场次价格区间
				dateString = IsNull(sessionLeftList.get(i).get("formatdate"));
				sessionLeftList.get(i).putAll(getMaxAndMin(dateString, subcourtid));
			}
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, sessionLeftList);
	}

	/**
	 * 获取场次价格极值
	 * @param date 日期
	 * @param subcourtid 子球场ID
	 * @return map 最高价，最低价
	 * @throws Exception
	 */
	public HashMap<String, String> getMaxAndMin(String date, String subcourtid) throws Exception {
		
		HashMap<String, String> qmap = new HashMap<String, String>();
		qmap.put("stdate", date);
		qmap.put("subcourtid", subcourtid);
		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("minprice", "900.00");
		ret.put("maxprice", "1500.00");
		Double minprice = 10000.00;
		Double maxprice = 0.00;
		//球场场次价格
		String sqlmax = "select   s.session_price as sprice , "
								+ "CASE when  (s.activity_price) is not NULL then  (s.activity_price) "
								+ "when  (s.member_price) is not NULL then  (s.member_price) "
								+ "when  (s.discount_price) is not NULL then  (s.discount_price)  "
								+ "when  (s.fav_price) is not NULL   then  (s.fav_price) "
								+ "WHEN (s.session_price) IS NOT NULL THEN (s.session_price)  "
								+ "end  as fprice "
								+ "from u_br_courtsession  s "
								+ "where s.stdate = :stdate "
								+ "and s.subcourt_id = :subcourtid ";
		List<HashMap<String, BigDecimal>> maxList = baseDAO.findSQLMap(qmap, sqlmax);//球场价格集合
		Double fprice=0.00;
		if (maxList != null && maxList.size() > 0) {
			for (int i = 0; i < maxList.size(); i++) {
				 fprice = maxList.get(i).get("fprice") != null ? maxList.get(i).get("fprice").doubleValue() : null;

				// max
				if (fprice != null && fprice > maxprice) {
					maxprice = fprice;
				}

				// min
				if (fprice != null && fprice < minprice) {
					minprice = fprice;
				}
			}
		}
		ret.put("minprice", minprice.toString());
		ret.put("maxprice", maxprice.toString());

		return ret;
	}

	/**
	 * 价格比较---未使用
	 * @param sprice
	 * @param fprice
	 * @param min
	 * @return String 低价
	 */
	private String dcompare(Double sprice, Double fprice,boolean min) {
		
		if (fprice == null) {
			return sprice.toString();
		}
		if (sprice == null) {
			return fprice.toString();
		}

		if (min) {
			return sprice>fprice?fprice.toString():sprice.toString();
		}else {
			return sprice>fprice?sprice.toString():fprice.toString();
		}
	}

	@Override
	public UBrCourtimage queryBrCourtimg(HashMap<String, String> map) throws Exception {
		
		return baseDAO.get(map,
				"from UBrCourtimage where UBrCourt.subcourtId=:subcourtId and imgSizeType=:imgSizeType and cimgUsingType=:cimgUsingType ");
	}

	@Override
	public HashMap<String, Object> queryCourtGeneral(HashMap<String, String> params) throws Exception {
		
		//球场概括信息
		String sqlgengeralinfo = "SELECT bct.name,ct.address,ct.position,ct.poi,img.cimg_using_type,"
											+ " img.saveurl, img.imgurl, img_size_type,"
											+ " bct.subcourt_id, ct.traffic, ct.telephone, ct.court_id "
											+ "FROM u_court ct "
											+ "LEFT JOIN u_br_court bct ON ct.court_id = bct.court_id "
											+ "LEFT JOIN u_br_courtimage img ON img.subcourt_id = bct.subcourt_id "
											+ "WHERE img.img_size_type=1 "
											+ "AND img.cimg_using_type=5 "
											+ "AND bct.subcourt_id = :subcourt_id ";
		//球场服务
		String sqlservice = "select * from u_br_courtstaticservice where subcourt_id = :subcourt_id order by orderbyno asc";
		//球场相册
		String sqlalbum = "SELECT * FROM u_br_courtimage img "
											+ "WHERE img_size_type =1 "
											+ "AND img.cimg_using_type = 4 "
											+ "AND img.subcourt_id = :subcourt_id "
											+ "ORDER BY img.img_weight DESC "
											+ "limit 4 offset 0;";
		
		List<Object> generallist = baseDAO.findSQLMap(params, sqlgengeralinfo);//球场概括集合
		List<Object> servicelist = baseDAO.findSQLMap(params, sqlservice);//球场服务集合
		List<Object> albumlist = baseDAO.findSQLMap(params, sqlalbum);//球场相册集合
		HashMap<String, Object> result = new HashMap<String, Object>();
		//如果球场概括集合不为空，那么将第一条数据赋值给result集合
		if (generallist != null && generallist.size() > 0) {
			result.put("generallist", generallist.get(0));
		} else {
			result.put("generallist", null);
		}
		result.put(Constant.SERVICE_LIST, servicelist);//result加入球场服务
		result.put("albumlist", albumlist);//result加入球场相册
		
		return WebPublicMehod.returnRet(Constant.SUCCESS, result);
	}

	@Override
	public HashMap<String, Object> subscribeCourt(HashMap<String, String> params) throws Exception {
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USER_ID, user.getUserId());
		String follow_status = params.get("follow_status");
		String sql = "select * from u_follow f where f.user_follow_type = '1'  and f.user_id = :user_id and f.object_id = :subcourt_id ";
		
		// 查询关注记录
		if (("100".equals(follow_status))) {		
			List<Object> list = baseDAO.findSQLMap(params, sql);	
			return WebPublicMehod.returnRet(Constant.SUCCESS, list);
			
		// 曾经关注过
		} else if (("1".equals(follow_status)) || ("2".equals(follow_status))) {
			HashMap<String, Object> qmapHashMap = new HashMap<>();
			qmapHashMap = PublicMethod.Maps_Mapo(params);
			qmapHashMap.put(Constant.USER_ID, user.getUserId());
			qmapHashMap = PublicMethod.Maps_Mapo(params);
			
			//获取关注记录
			List<Object> list = baseDAO.findSQLMap(params, sql);
			
			//关注或取消关注
			if (list!=null&&list.size() > 0) {
				String updateHql = "UPDATE UFollow f SET f.followStatus = :follow_status   where f.userFollowType = '1' and f.UUser.userId = :user_id  and f.objectId = :subcourt_id ";
				baseDAO.executeHql(updateHql, qmapHashMap);
			} else {
				String key_id = UUID.randomUUID().toString();
				UUser u = new UUser();
				u.setUserId(user.getUserId());
				UFollow uFollow = new UFollow();
				uFollow.setUUser(u);
				uFollow.setKeyId(key_id);
				uFollow.setCreatedate(new Date());
				uFollow.setFollowStatus(follow_status);
				uFollow.setObjectId(params.get(Constant.SUBCOURT_ID));
				uFollow.setUserFollowType("1");
				baseDAO.save(uFollow);
			}
			return WebPublicMehod.returnRet(Constant.SUCCESS, follow_status);
		}
		return null;
	}

	@Override
	public HashMap<String, Object> queryProductBySession(HashMap<String, String> params) throws Exception {
		//可预订商品
		String sql = "SELECT 0 AS salecount, m.pro_id,m.product_type,m.product_subtype,m.product_detailtype,"
						+ "m.name,m.remark,m.size,m.is_user,m.user_id,m.sale_type,m.product_status,"
						+ "m.createuser,m.createdate,m.createtime,m.price_type,m.price,m.sale_ceiling,m.stdate,m.sttime,m.enddate,m.endtime,m.similar_id, "
						+ "img.imgurl, img.img_size_typwe, img.mimg_using_type,"
						+ "case when mc.court_id is null then m.all_stock "
						+ "else mc.court_count "
						+ "end as all_stock "
						+ "FROM u_mall m "
						+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
						+ "left join u_mall_court as mc on m.pro_id = mc.mall_id  "
						+ "WHERE m.price_type = '1' "	//原价
						+ "AND m.product_subtype = :productsubtype "
						+ "AND img.img_size_typwe = 1 "
						+ "AND img.mimg_using_type = 1 "
						+ "AND ((m.stdate < :cdate AND :cdate < m.enddate) or((m.stdate = :cdate OR :cdate < m.enddate) "
						+ "AND ((sttime < :ctime) AND ( :ctime < endtime)))) "
						+ "AND m.product_status = 1 AND (m.pro_id IN "
						+ "(SELECT product_id FROM u_br_courtpro "
						+ "WHERE session_id IN (:sids) OR m.similar_id IN "
						+ "(SELECT m.similar_id FROM u_mall AS m "
						+ "WHERE m.similar_id IN "
						+ "(SELECT mm.similar_id FROM u_mall AS mm "
						+ "LEFT JOIN u_mall_court AS cc ON mm.pro_id = cc.mall_id "
						+ "WHERE cc.court_id IS NULL "
						+ "GROUP BY mm.similar_id ))))";
		//优惠价
		String sqlSub = " SELECT m.price AS yprice FROM u_mall m "
						+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
						+ "WHERE m.price_type = '3' " //优惠价
						+ "AND m.similar_id = :similar_id_tmp "
						+ "AND m.product_subtype = :productsubtype "
						+ "AND img.img_size_typwe = 1 "
						+ "AND img.mimg_using_type = 1 "
						+ "AND ((m.stdate< :cdate AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
						+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) "
						+ "AND m.product_status = 1 AND (pro_id IN "
						+ "(SELECT product_id FROM u_br_courtpro "
						+ "WHERE session_id IN (:sids)) OR m.similar_id IN "
						+ "(SELECT m.similar_id FROM u_mall AS m "
						+ "WHERE m.similar_id IN "
						+ "(SELECT mm.similar_id FROM u_mall AS mm "
						+ "LEFT JOIN u_mall_court AS cc ON mm.pro_id = cc.mall_id "
						+ "WHERE cc.court_id IS NULL "
						+ "GROUP BY mm.similar_id ))) ";
		
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		String sid = params.get("sessionids");
		HashMap<String, List<Object>> sids = new HashMap<>();
		List<Object> list = new ArrayList<Object>();
		String[] strings = sid.split(",");
		params.put(Constant.CURRENT_DATE, cdate);
		params.put(Constant.CURRENT_TIME, ctime);
		
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		
		//获取可预订商品
		sids.put("sids", list);
		List<HashMap<String, String>> lists = baseDAO.findSQLMap(params, sql, sids);
		String similar_id="";
		String yprice="";
		List<HashMap<String, String>> sublists=null;//优惠价商品集合
		//添加商品价格
		for (int i = 0; i < lists.size(); i++) {
			similar_id = IsNull(lists.get(i).get("similar_id"));
			params.put("similar_id_tmp", similar_id);
			sublists = baseDAO.findSQLMap(params, sqlSub, sids);
			if (sublists != null && sublists.size() > 0) {
				yprice = IsNull(sublists.get(0).get("yprice"));
				lists.get(i).put("yprice", yprice);
			} else {
				lists.get(i).put("yprice", null);
			}
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, lists);
	}

	@Override
	public HashMap<String, Object> queryProductTypeBySession(HashMap<String, String> params) throws Exception {
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		String product_subtype = IsNull(params.get("product_subtype"));
		String sid = params.get("sessionids");
		params.put(Constant.CURRENT_DATE, cdate);
		params.put(Constant.CURRENT_TIME, ctime);
		HashMap<String, List<Object>> sids = new HashMap<>();
		List<Object> list = new ArrayList<Object>();
		String[] strings = sid.split(",");
		
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		sids.put("sids", list);

		//查询单位整体库存
		String sql = "SELECT count(m.all_stock) as all_stock,info.remark AS mremark, info.imgurl, info.name, m.product_subtype "
						+ "FROM u_mall m "
						+ "LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id "
						+ "LEFT JOIN u_parameter_info info ON info.params = m.product_subtype "
						+ "LEFT JOIN u_parameter p ON p.pkey_id = info.pkey_id "
						+ "WHERE p.params = 'product_subtype' ";
		//是否优惠
		if ("3".equals(product_subtype)) {
			sql += " AND m.product_subtype = 3 ";
		}

		sql += "  AND img.img_size_typwe = 1 "
				+ "AND img.mimg_using_type = 1 "
				+ "AND ((m.stdate< :cdate "
				+ "AND :cdate< m.enddate) or((m.stdate = :cdate OR :cdate< m.enddate) "
				+ "AND ((sttime<:ctime) AND (:ctime<endtime)))) "
				+ "AND m.product_status = 1 AND (pro_id IN "
				+ "(SELECT product_id FROM u_br_courtpro "
				+ "WHERE session_id IN (:sids)) OR m.similar_id IN "
				+ "(SELECT m.similar_id FROM u_mall AS m "
				+ "WHERE m.similar_id IN "
				+ "(SELECT mm.similar_id FROM u_mall AS mm "
				+ "LEFT JOIN u_mall_court AS cc ON mm.pro_id = cc.mall_id "
				+ "WHERE cc.court_id IS NULL "
				+ "GROUP BY mm.similar_id))) "
				+ "GROUP BY m.product_subtype";

		List<HashMap<String, String>> lists = baseDAO.findSQLMap(params, sql, sids);//商品信息集合
		
		return WebPublicMehod.returnRet(Constant.SUCCESS, lists);
	}

	@Override
	public HashMap<String, Object> addCourt(HashMap<String, String> params, UBrCourt uBrCourt) throws Exception {
		
		HashMap<String, Object> qmp = new HashMap<String, Object>();
		qmp.put("name", uBrCourt.getName());
		String queryCourt = "from UBrCourt u where u.name = :name and u.courtStatus = '1'";
		List<UBrCourt> list = baseDAO.find(queryCourt, qmp);//根据下属球场名称和球场状态(1-可用)获取下属球场信息
		UBrCourt uBrCourtTemp = null;//下属球场对象
		
		//如果没有对应主球场，就添加关联，否者获取球场信息
		if (list == null || list.size() == 0) {
			UCourt uCourt = this.savaUCourt(params, qmp);//添加第三球场
			if (null != uCourt) {
				uBrCourtTemp = this.saceuBrCourt(qmp, uCourt);//添加下属球场
			}
		} else {
			uBrCourtTemp = list.get(0);
		}

		HashMap<Object, Object> retmap = new HashMap<>();//返回球场ID、球场名称
		retmap.put("id", uBrCourtTemp.getSubcourtId());
		retmap.put("name", uBrCourtTemp.getName());
		return WebPublicMehod.returnRet(Constant.SUCCESS, retmap);
	}
 
	/**
	 * 为子球场bean设定球场bean（第三方其他）
	 * @param qmp
	 * @param uCourt 球场
	 * @return uBrCourt 子球场bean
	 * @throws Exception
	 */
	private UBrCourt saceuBrCourt(HashMap<String, Object> qmp, UCourt uCourt) throws Exception {
		
		UBrCourt uBrCourt = new UBrCourt();
		uBrCourt.setSubcourtId(WebPublicMehod.getUUID());
		uBrCourt.setName(qmp.get("name").toString());
		uBrCourt.setCourtType("2");
		uBrCourt.setCourtStatus("1");
		uBrCourt.setCreatedate(new Date());
		uBrCourt.setCreatetime(new Date());
		uBrCourt.setUCourt(uCourt);
		baseDAO.save(uBrCourt);
		return uBrCourt;
	}

	/**
	 * 
	 * 设定球场bean（第三方其他）
	 * @param params
	 * @param qmp
	 * @return
	 * @throws Exception
	 */
	private UCourt savaUCourt(HashMap<String, String> params, HashMap<String, Object> qmp) throws Exception {
		
		UCourt uCourt = new UCourt();
		uCourt.setCourtId(WebPublicMehod.getUUID());
		uCourt.setCourtType("2");
		uCourt.setName(qmp.get("name").toString());
		uCourt.setArea(Integer.parseInt(params.get("area")));
		uCourt.setAddress(params.get("address"));
		uCourt.setCreatedate(new Date());
		uCourt.setCreatetime(new Date());
		baseDAO.save(uCourt);
		return uCourt;
	}

	@Override
	public HashMap<String, Object> queryCourtListShort(HashMap<String, String> params) throws Exception {

		String queryCourt = " SELECT u.name,u.subcourt_id,u.court_br_type,u.createdate "
									+ "FROM u_br_court u "
									+ "WHERE u.court_status = '1' "
									+ "ORDER BY u.court_br_type ASC, u.createdate DESC";
		List<HashMap<String, Object>> list = baseDAO.findSQLMap(queryCourt);//获取可用的下属球场集合
		return WebPublicMehod.returnRet(Constant.SUCCESS, list);
	}

	@Override
	public boolean sessionBeOrdered(String uBrCourtsessionId) throws Exception {
		UBrCourtsession session =baseDAO.get(UBrCourtsession.class, uBrCourtsessionId);
		if (session != null && "1".equals(session.getSessionStatus()) && "2".equals(session.getOrderStatus())) {
			return false;
		}
		return true;
	}

	@Override
	public HashMap<String, Object> querySubscribedCourtList(HashMap<String, String> params) throws Exception {
		
		//球场信息
		String sqlString = "SELECT bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime, bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status, "
								+ "ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, "
								+ "img.image_court_id,img.img_size_type,img_weight,img.imgurl,img.saveurl "
								+ "FROM u_br_court bct "
								+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
								+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
								+ "WHERE bct.court_status =1 "
								+ "AND img.img_size_type = 2 "
								+ "AND img.cimg_using_type = 2 "
								+ " and bct.subcourt_id in (select object_id from u_follow where follow_status = '1' and user_id = :user_id) "
								+ " ORDER BY bct.score DESC ";
		//球场基本服务
		String sql = "SELECT * from u_br_courtstaticservice where subcourt_id = :scourtid order by orderbyno  asc";
		String subcourtid;
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USER_ID, user.getUserId());
		
		List<HashMap<String, Object>> l = baseDAO.findSQLMap(params, sqlString);//球场信息
		List<Object> servicelist=null;
		if(l!=null&&l.size()>0){
			for (int i = 0; i < l.size(); i++) {
				subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();
				params.put(Constant.SUBCOURT_SERVICE_BY_ID, subcourtid);	
				servicelist = baseDAO.findSQLMap(params, sql);
				l.get(i).put(Constant.SERVICE_LIST, servicelist);//球场信息中添加球场服务
			}
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}

	@Override
	public boolean updateCourtSession(String orderId) throws Exception {
		
		HashMap<String, Object> qmapHashMap = new HashMap<>();
		qmapHashMap.put("orderId", orderId);
		String updateHql = "update UBrCourtsession cs set cs.orderStatus='2',cs.orderId = null where cs.orderId = :orderId";
		baseDAO.executeHql(updateHql, qmapHashMap);
		
		return true;
	}

	@Override
	public UBrCourt getUCourt(String subcourtid) throws Exception {
		
		return baseDAO.get(UBrCourt.class, subcourtid);
	}

	// yuancao
	@Override
	public HashMap<String, Object> saveOrder(HashMap<String, String> params) throws Exception {
		
		List<UMall> listMall = new ArrayList<>();
		List<UBrCourtsession> sessionList = new ArrayList<>();
		
//		//??这个ifelse
//		if (sessionList != null && sessionList.size() > 0) {
//		} else {
//			// System.out.println("courtid----0000000000000000000ekse-------");
//		}
		
		//默认普通订场
		if (params.get("orderType") == null || "".equals(params.get("orderType"))) {
			params.put("orderType","1");
		}
		
		buildDate(params, listMall, sessionList);//处理数据
		HashMap<String, Object> ret = uOrderService.saveOrder(params, sessionList, listMall);
		return ret;
	}

	/**
	 * 处理前端传过json数据
	 * @param params 
	 * @param listMall 商品集合
	 * @param sessionList 球场球场集合
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private boolean buildDate(HashMap<String, String> params, List<UMall> listMall, List<UBrCourtsession> sessionList) throws Exception {
		// 商品
		String malllist = params.get("listMall").toString();
		String similarId;
		String priceType;
		String salecount;
		JSONObject object;
		UMall mall;
		JSONArray arraylist = JSONArray.fromObject(malllist);
		//商品集合数据处理
		if (arraylist.size() > 0) {
			for (int i = 0; i < arraylist.size(); i++) {
				object = arraylist.getJSONObject(i);
				mall = new UMall();
				similarId = object.get("similarid").toString();
				priceType = object.get("pricetype").toString();
				salecount = object.get("salecount").toString();
				params.put("similarId", similarId);
				params.put("typePrice", priceType);
				mall = baseDAO.get(params, "from UMall where similarId=:similarId and priceType=:typePrice ");
				mall.setSaleCount(Integer.parseInt(salecount));
				listMall.add(mall);
			}
		}
		// 场次
		String sessionlist = params.get("listSession").toString();
		JSONArray sessionarraylist = JSONArray.fromObject(sessionlist);
		//球场球场集合数据处理
		if (sessionarraylist.size() > 0) {
			for (int i = 0; i < sessionarraylist.size(); i++) {
				object = sessionarraylist.getJSONObject(i);
				UBrCourtsession session = new UBrCourtsession();
				session.setSessionId(object.get("sessionid").toString());
				sessionList.add(session);
			}
		}
		//根据前端传过来的sessionid,查询球场球场对象信息
		for (int i = 0; i < sessionList.size(); i++) {
			sessionList.set(i, baseDAO.get(UBrCourtsession.class, sessionList.get(i).getSessionId()));
		}
		UMall tmall;
		// 服务－只有一个场次的时候进入
		if (sessionList != null && sessionList.size() == 1) {
			List<UMall> sessionMallList = new ArrayList<>();
			if (listMall != null && listMall.size() > 0) {
				for (UMall uMall : listMall) {
					// 插入商品、订单记录信息
					params.put("similarId", uMall.getSimilarId());
					params.put("typePrice", uMall.getPriceType());
					tmall = baseDAO.get(params, "from UMall where similarId=:similarId and priceType=:typePrice ");
					if (tmall.getProductSubtype().equals("3")) {
						tmall.setSaleCount(uMall.getSaleCount());
						sessionMallList.add(tmall);
					}
				}
				sessionList.get(0).setUmallList(sessionMallList);
			}
		}
		String sqlString = "select subcourt_id as subcourtId from u_br_courtsession where session_id = :sid";//获取下属球场ID
		params.put("sid", sessionList.get(0).getSessionId());
		List<HashMap> l = baseDAO.findSQLMap(params, sqlString);
		if (l != null && l.size() > 0) {
			params.put("subcourtId", l.get(0).get("subcourtId").toString());
		}
		UBrCourt brCount = baseDAO.get(UBrCourt.class, params.get("subcourtId"));//获取球场ID
		if (brCount != null && brCount.getUCourt() != null) {
			params.put("courtId", brCount.getUCourt().getCourtId());
		}
		params.put("paymentRatio", "100");// 普通订场百分比 100%
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public HashMap<String, Object> querySessionListBySubcourtId(HashMap<String, String> params) throws Exception {
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USERID, user.getUserId());
		PageLimit page = new PageLimit(Integer.parseInt(params.get("page")), 0);

		//已预订场次
		String sql = "SELECT s.session_id,o.price,date_format(s.stdate,'%Y-%m-%d') as formatdate,0 as timeout,o.user_id, "
						+ "bct.name, img.imgurl, s.stdate, date_format(s.stdate,'%m月%d日')AS showdate, s.sttime, s.endtime, "
						+ "o.order_id , o.ordernum, o.orderstatus , o.order_type as session_br_useing_status "
						+ "FROM u_br_courtsession s "
						+ "LEFT JOIN u_br_court bct ON s.subcourt_id = bct.subcourt_id "
						+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
						+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
						+ "LEFT JOIN u_order o ON o.order_id = s.order_id "
						+ "WHERE bct.court_status =1 "
						+ "AND img.img_size_type = 1 "
						+ "AND img.cimg_using_type = 2 "
						+ "AND o.order_type = '1' "
						+ "AND s.subcourt_id = :subcourtid "
						+ "AND o.user_id = :userId "
						+ "ORDER BY s.stdate DESC,sttime DESC"
						+" limit " + Public_Cache.PAGE_LIMIT + " offset " + page.getOffset();
		//场次对应基本服务
		String sqls = "SELECT * from u_br_courtstaticservice where subcourt_id = :subcourtid order by orderbyno  asc";		
		String formatdate;
		String sttime;
		String queryBegin;
		String current;
		int ret=0;
		String stdate = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd");
		params.put("stdate", stdate);
		List<HashMap> l = baseDAO.findSQLMap(params, sql);
		//为场次匹配服务
		List<Object> servicelist=null;
		for (int i = 0; i < l.size(); i++) {
			formatdate = l.get(i).get("formatdate").toString();
			l.get(i).put("formatweek", getWeekOfDate(sdf.parse(formatdate)));
			sttime = l.get(i).get("formatdate").toString();
			queryBegin = formatdate + " " + sttime;
			current = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd hh:mm");
			ret = current.compareTo(queryBegin);//当前时间和预订时间比较
			//若果ret大于0，那么当前时间大于预订时间，设置timeout＝1（没有过期）
			if (ret > 0) {
				l.get(i).put("timeout", 1);
			}
			servicelist = baseDAO.findSQLMap(params, sqls);//球场服务集合
			l.get(i).put(Constant.SERVICE_LIST, servicelist);//球场列表中，加入球场服务信息
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}
	@Override
	public HashMap<String, Object> querySessionListAllSubcourtDuel(HashMap<String, String> params, String type) throws Exception {
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USERID, user.getUserId());
		Integer page = 1;
		String sql = "";
		String sqls = "SELECT * from u_br_courtstaticservice where subcourt_id = :subcourtid order by orderbyno  asc";
		String sqlOrderMall = "select m.name,m.size,m.count from u_order_mall m  where m.order_id = :order_id  order by product_type asc";
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		params.put(Constant.CURRENT_DATE, cdate);
		params.put(Constant.CURRENT_TIME, ctime);
		
		//预订场次类型（约战，挑战）
		if (type.equals("duel")) {
			sql += "SELECT pay_type_status, o.payment_ratio, bct.score, s.subcourt_id, o.price, date_format(s.stdate,'%Y-%m-%d') AS formatdate, 0 AS timeout, "
						+ "o.user_id, bct.name, img.imgurl, s.stdate, date_format(s.stdate,'%m月%d日')AS showdate, "
						+ "o.order_id , o.ordernum, o.orderstatus, min(s.sttime) AS sttime, max(s.endtime) AS endtime "
						+ "FROM u_br_courtsession s "
						+ "LEFT JOIN u_br_court bct ON s.subcourt_id = bct.subcourt_id "
						+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
						+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
						+ "LEFT JOIN u_order o ON o.order_id = s.order_id "
						+ "left join u_duel dl on dl.order_id = o.order_id  "
						+ "WHERE bct.court_status =1 "
						+" AND (s.stdate > :cdate or (s.stdate= :cdate and s.sttime > :ctime))  "
						+ "AND img.img_size_type = 1 "
						+ "AND img.cimg_using_type = 2 "
						+ "AND ((o.order_type = '1' and o.payment_ratio = 100) "
						+ "OR (o.order_type ='3'        "
						+ "and  (dl.duel_id IS NULL or dl.duel_status = '5' )   ))  "
						+ "and o.order_id not in ("
						+ "select dul.order_id from u_duel dul "
						+ "where dul.duel_status in ('1','2','3','4') "
						+ " and dul.order_id is not null ) "
						+ "AND o.user_id = :userId  "
						+ "AND o.orderstatus= '1'   "
						+ "GROUP BY pay_type_status, o.payment_ratio, bct.score, s.subcourt_id, "
						+ "o.price, o.user_id, bct.name, img.imgurl, s.stdate, o.order_id , o.ordernum, o.orderstatus "
						+ "ORDER BY s.stdate DESC,sttime DESC";
			sql += " limit " + Public_Cache.PAGE_LIMIT + " offset " + ((page - 1) * Public_Cache.PAGE_LIMIT);
		} else if (type.equals("challeage")) {
			//2.0.2
			if (params.get("subcourtId") != null && !"".equals(params.get("subcourtId"))) {
				sql += "SELECT s.session_id,pay_type_status,o.payment_ratio,bct.score,s.subcourt_id,o.price,date_format(s.stdate,'%Y-%m-%d') as formatdate,0 as timeout,"
						+ "o.user_id, bct.name, img.imgurl, s.stdate, date_format(s.stdate,'%m月%d日') AS showdate, s.sttime, s.endtime, "
						+ "o.order_id , o.ordernum, o.orderstatus "
						+ "FROM u_br_courtsession s "
						+ "LEFT JOIN u_br_court bct ON s.subcourt_id = bct.subcourt_id "
						+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
						+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
						+ "LEFT JOIN u_order o ON o.order_id = s.order_id "
						+ "WHERE o.orderstatus= '1' "
						+ "and    (s.stdate > :cdate or (s.stdate= :cdate and s.sttime > :ctime)) "
						+ "and s.subcourt_id = :subcourtId "
						+ "and  bct.court_status =1 "
						+ "AND img.img_size_type = 1"
						+ " AND img.cimg_using_type = 2 "
						+ "AND  o.order_type = '2'   "
						+ "AND o.user_id = :userId "
						+ "ORDER BY o.createdate desc,o.createtime desc limit 1 offset 0";
			} else {//老版本
				sql += "SELECT s.session_id,pay_type_status,o.payment_ratio,bct.score,s.subcourt_id,o.price,date_format(s.stdate,'%Y-%m-%d') as formatdate,0 as timeout,"
						+ "o.user_id, bct.name, img.imgurl, s.stdate, date_format(s.stdate,'%m月%d日') AS showdate, s.sttime, s.endtime, "
						+ "o.order_id , o.ordernum, o.orderstatus "
						+ "FROM u_br_courtsession s "
						+ "LEFT JOIN u_br_court bct ON s.subcourt_id = bct.subcourt_id "
						+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
						+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
						+ "LEFT JOIN u_order o ON o.order_id = s.order_id "
						+ "WHERE o.orderstatus= '1' "
						+ "and    (s.stdate > :cdate or (s.stdate= :cdate and s.sttime > :ctime)) "
						+ "and  bct.court_status =1 "
						+ "AND img.img_size_type = 1"
						+ " AND img.cimg_using_type = 2 "
						+ "AND  o.order_type = '2'   "
						+ "AND o.user_id = :userId "
						+ "ORDER BY o.createdate desc,o.createtime desc limit 1 offset 0";
			}
		}
		
		String formatdate;
		String sttime;
		String queryBegin;
		String current;
		String subcourtid;
		String order_id;
		List<Object> servicelist;
		List<Object> ordermalllist;
		
		//匹配服务，订单
		List<HashMap<String, Object>> retList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> l = baseDAO.findSQLMap(params, sql);
		for (int i = 0; i < l.size(); i++) {
			formatdate = l.get(i).get("formatdate").toString();
			l.get(i).put("formatweek", getWeekOfDate(sdf.parse(formatdate)));
			sttime = l.get(i).get("formatdate").toString();
			queryBegin = formatdate + " " + sttime;
			current = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd hh:mm");
			//过期 当前时间和预订时间比较 
			int ret = current.compareTo(queryBegin);
			if (ret <= 0) {
				retList.add(l.get(i));
				subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();
				params.put("subcourtid", subcourtid);
				servicelist = baseDAO.findSQLMap(params, sqls);
				l.get(i).put(Constant.SERVICE_LIST, servicelist);
				order_id = l.get(i).get("order_id").toString();
				params.put("order_id", order_id);
				ordermalllist = baseDAO.findSQLMap(params, sqlOrderMall);
				l.get(i).put("ordermalllist", ordermalllist);
			}
			
		}

		//挑战已支付订单移除
		Iterator<HashMap<String, Object>> iterator = retList.iterator();
		if (type.equals("challeage")) {
			HashMap<String, Object> ll=null;
			while (iterator.hasNext()) {
			ll = iterator.next();
				order_id = ll.get("order_id").toString();
				if (uchallengeService.returnOrderStatus(order_id)) {
					iterator.remove();
				}
			}
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, retList);
	}

	@Override
	public HashMap<String, Object> saveOrderCheck(HashMap<String, String> params) throws Exception { 	
		
		List<UMall> listMall = new ArrayList<>();
		List<UBrCourtsession> sessionList = new ArrayList<>();
		
//		if (sessionList != null && sessionList.size() > 0) {
//		} else {
//			//System.out.println("courtid----0000000000000000000ekse-------");
//		}
		//前端页面传过来数据 进行数据处理
		this.buildDate(params, listMall, sessionList);
		
		//orderType默认1（普通订场）
        if (params.get("orderType") == null || "".equals(params.get("orderType"))) {
        	params.put("orderType", "1");
        }
		
		HashMap<String, Object> ret = uOrderService.ifLogic(params, sessionList);
		//System.out.println("return params" + ret);
		return ret;
	}
	@Override
	public Double getFavPrice(String sessionid, boolean sprice) throws Exception {

		// 11 单场价格 session_price DECIMAL /
		// 12 单场折扣价 discount_price DECIMAL /
		// 13 单场会员价 member_price DECIMAL /
		// 14 单场优惠价 fav_price DECIMAL /
		// 15 单场活动价 activity_price DECIMAL /
		// 活动价>会员价>折扣价>优惠价>原价
		UBrCourtsession brCourtsession = new UBrCourtsession();
		brCourtsession = baseDAO.get(UBrCourtsession.class, sessionid);
		if (brCourtsession != null && brCourtsession.getActivityPrice() != null) {
			return brCourtsession.getActivityPrice();
		}

		if (brCourtsession != null && brCourtsession.getMemberPrice() != null) {
			return brCourtsession.getMemberPrice();
		}

		if (brCourtsession != null && brCourtsession.getDiscountPrice() != null) {
			return brCourtsession.getDiscountPrice();
		}

		if (brCourtsession != null && brCourtsession.getFavPrice() != null) {
			return brCourtsession.getFavPrice();
		}
		if (sprice) {
			if (null != brCourtsession.getSessionPrice()) {
				return brCourtsession.getSessionPrice();
			} else {
				return 0.0;
			}
		}
		return null;
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> queryCourtListSubscribed(HashMap<String, String> params) throws Exception {
		Integer page = params.get(Constant.PAGE) != null 
				? new Integer(params.get(Constant.PAGE)) : 1;
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USER_ID, user.getUserId());
		//球场信息
		String sqlString = "SELECT 2 AS labelb, 0 AS emptys, 0 AS duels, 0 AS challenges, 0 AS matches, bct.subcourt_id, bct.name, bct.bus_sttime, "
								+ "bct.bus_endtime, bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status, "
								+ "ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, "
								+ "img.image_court_id, img.img_size_type, img_weight, img.imgurl, img.saveurl , "
								+ "case when bct.recommend_court = '1' then 4 "
								+ "when bct.popularity_court = '1' then 3 "
								+ "when bct.subcourt_id in ( ("
								+ "SELECT bcs.subcourt_id FROM u_br_courtsession bcs "
								+ "WHERE bcs.order_id IN ("
								+ "SELECT od.order_id FROM u_order od "
								+ "WHERE od.user_id = :user_id)) ) "
								+ "then 2 "
								+ "else -10000 "
								+ "end as labela "
								+ "FROM u_br_court bct"
								+ " left join u_br_courtsession s on s.subcourt_id = bct.subcourt_id "
								+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
								+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
								+ "WHERE bct.court_status =1 "
								+ "AND img.img_size_type = 1 "
								+ "AND img.cimg_using_type = 2 "
								+ "AND bct.subcourt_id IN ("
								+ "SELECT f.object_id FROM u_follow f"
								+ " WHERE f.user_follow_type = '1' "
								+ "AND f.follow_status= '1' "
								+ "AND f.user_id = :user_id) "
								+ "group by bct.subcourt_id, bct.name, bct.bus_sttime,"
								+ " bct.bus_endtime, bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status, "
								+ "ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, img.image_court_id,"
								+ " img.img_size_type, img_weight, img.imgurl, img.saveurl "
								+ "order by labela DESC ,bct.score DESC";
		sqlString += " limit " + Public_Cache.PAGE_LIMIT + " offset " + ((page - 1) * Public_Cache.PAGE_LIMIT);
		//球场服务
		String sql = "SELECT * from u_br_courtstaticservice where subcourt_id = :scourtid order by orderbyno  asc";
		
		@SuppressWarnings("rawtypes")
		List<HashMap> l;
		l = baseDAO.findSQLMap(params, sqlString);
		
		List<Object> servicelist = null;//球场服务集合
		//为球场匹配服务
		for (int i = 0; i < l.size(); i++) {
			// 查询球场21天空余场次总数
			String subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();

			//第一个球场特殊处理
			if (i == 0) {
				l.get(i).put(Constant.EMPTYS, querySessionCountThreeWeeks(subcourtid, Constant.EMPTYS));
				l.get(i).put(Constant.CHALLENGES, querySessionCountThreeWeeks(subcourtid, Constant.CHALLENGES));
				l.get(i).put(Constant.DUELS, querySessionCountThreeWeeks(subcourtid, Constant.DUELS));
			}

			params.put(Constant.SUBCOURT_SERVICE_BY_ID, subcourtid);
			servicelist = baseDAO.findSQLMap(params, sql);
			l.get(i).put(Constant.SERVICE_LIST, servicelist);

			// 折扣场次数
			l.get(i).put(Constant.LABEL_B, countLabelBcourt(subcourtid));

		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> queryCourtListOrdered(HashMap<String, String> params) throws Exception {
		
		Integer page = params.get(Constant.PAGE) != null 
				? new Integer(params.get(Constant.PAGE)) : 1;
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USER_ID, user.getUserId());
		//球场信息
		String sqlString = "SELECT 1 as labela, 2 as labelb,0 as emptys, 0 as duels,0 as challenges,0 as matches,"
								+ "bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime, bct.court_id, bct.score, bct.court_status, "
								+ "bct.court_br_type, bct.bus_status,"
								+ " ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, "
								+ "img.image_court_id,img.img_size_type,img_weight,img.imgurl,img.saveurl "
								+ "FROM u_br_court bct "
								+ "LEFT JOIN u_court ct ON bct.court_id = ct.court_id "
								+ "LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id "
								+ "WHERE bct.court_status =1 "
								+ "AND img.img_size_type = 1 "
								+ "AND img.cimg_using_type = 2 "
								+ "AND bct.subcourt_id in  ("
								+ "select bcs.subcourt_id from u_br_courtsession bcs "
								+ "where bcs.order_id in  ("
								+ "select od.order_id from u_order od   "
								+ "where od.user_id = :user_id)) "
								+ "ORDER BY bct.score DESC";
		sqlString += " limit " + Public_Cache.PAGE_LIMIT + " offset " + ((page - 1) * Public_Cache.PAGE_LIMIT);
		//球场服务
		String sql = "SELECT * from u_br_courtstaticservice where subcourt_id = :scourtid order by orderbyno  asc";
		
		@SuppressWarnings("rawtypes")
		List<HashMap> l;
		l = baseDAO.findSQLMap(params, sqlString);
		List<Object> servicelist = null;//球场服务集合
		//为球场匹配服务
		for (int i = 0; i < l.size(); i++) {
			// 查询球场21天空余场次总数
			String subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();

			//第一个球场特殊处理
			if (i == 0) {
				l.get(i).put(Constant.EMPTYS, querySessionCountThreeWeeks(subcourtid, Constant.EMPTYS));
				l.get(i).put(Constant.CHALLENGES, querySessionCountThreeWeeks(subcourtid, Constant.CHALLENGES));
				l.get(i).put(Constant.DUELS, querySessionCountThreeWeeks(subcourtid, Constant.DUELS));
			}

			params.put(Constant.SUBCOURT_SERVICE_BY_ID, subcourtid);
			servicelist = baseDAO.findSQLMap(params, sql);//球场服务集合
			l.get(i).put(Constant.SERVICE_LIST, servicelist);
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, Object> modifyCourtScore(HashMap<String, String> params) throws Exception {
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USER_ID, user.getUserId());
		String status = params.get(Constant.STATUS);
		
		if (("100".equals(status))) { // 查询是否评分
			
			String sql = "SELECT * from u_br_courtscore where user_id = :user_id and subcourt_id = :subcourt_id ";
			List<Object> list = baseDAO.findSQLMap(params, sql); //用户对球场的评分
			
			return WebPublicMehod.returnRet(Constant.SUCCESS, list);
			
		} else if (("1".equals(status))) { // 更新评分
			
			HashMap<String, Object> qmapHashMap = new HashMap<>();
			qmapHashMap = PublicMethod.Maps_Mapo(params);
			qmapHashMap = PublicMethod.Maps_Mapo(params);
			qmapHashMap.put(Constant.USER_ID, user.getUserId());
			
			String sql = "SELECT * from u_br_courtscore where user_id = :user_id and subcourt_id = :subcourt_id";
			List<HashMap> list = baseDAO.findSQLMap(params, sql);
			
			//存在：更新 不存在：新建
			if (list.size() > 0) {
				String updateSql = "UPDATE u_br_courtscore set score = :score where user_id = :user_id and subcourt_id = :subcourt_id";
				baseDAO.executeSql(updateSql, qmapHashMap);
			} else {
				String updateSql = "insert into u_br_courtscore (id,user_id,subcourt_id,score)values(:randomid,:user_id,:subcourt_id,:score);";
				qmapHashMap.put("randomid", WebPublicMehod.getUUID());
				baseDAO.executeSql(updateSql, qmapHashMap);
			}
			return WebPublicMehod.returnRet(Constant.SUCCESS, params.get("score"));
		}
		return null;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> querySessionOrderSubcourt(HashMap<String, String> params) throws Exception {
		
		UUser user = uuserService.getUserinfoByToken(params);
		params.put(Constant.USERID, user.getUserId());
		Integer page = 1;
		//场次预订信息
		String sql = "SELECT pay_type_status, o.payment_ratio, bct.score, s.subcourt_id, o.price, date_format(s.stdate,'%Y-%m-%d') AS formatdate, o.user_id, bct.name, img.imgurl, s.stdate, date_format(s.stdate,'%m月%d日')AS showdate, min(s.sttime) as sttime, max(s.endtime) as endtime, o.order_id , o.ordernum, o.orderstatus FROM u_br_courtsession s LEFT JOIN u_br_court bct ON s.subcourt_id = bct.subcourt_id LEFT JOIN u_court ct ON bct.court_id = ct.court_id LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id LEFT JOIN u_order o ON o.order_id = s.order_id WHERE bct.court_status =1 AND img.img_size_type = 1 AND img.cimg_using_type = 2 and o.order_id = :order_id  group by pay_type_status, o.payment_ratio, bct.score,   s.subcourt_id,       o.price,     o.user_id, bct.name, img.imgurl,  s.stdate,    o.order_id ,    o.ordernum,       o.orderstatus   ORDER BY s.stdate DESC,sttime ASC";
		sql += " limit " + Public_Cache.PAGE_LIMIT + " offset " + ((page - 1) * Public_Cache.PAGE_LIMIT);
		//场次基本服务信息
		String sqls = "SELECT * from u_br_courtstaticservice where subcourt_id = :subcourtid order by orderbyno  asc";
		//场次商品订单信息
		String sqlOrderMall = "select m.name,m.size,m.count from u_order_mall m  where m.order_id = :order_id  order by product_type asc";
		List<HashMap> l = baseDAO.findSQLMap(params, sql);
		
		//匹配服务，商品订单信息
		for (int i = 0; i < l.size(); i++) {
			String formatdate = l.get(i).get("formatdate").toString();
			l.get(i).put("formatweek", getWeekOfDate(sdf.parse(formatdate)));
			String sttime = l.get(i).get("formatdate").toString();
			String queryBegin = formatdate + " " + sttime;
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String current = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd hh:mm");
			//当前时间和预订时间比较
			int ret = current.compareTo(queryBegin);
			//如果ret大于0，那么当前时间大于预订时间，设置timeout＝1（没有过期），否者过期
			if (ret > 0) {
				l.get(i).put("timeout", 1);
			} else {
				l.get(i).put("timeout", 0);
			}

			String subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();
			params.put("subcourtid", subcourtid);

			List<Object> servicelist = baseDAO.findSQLMap(params, sqls);//场次基本服务信息
			l.get(i).put(Constant.SERVICE_LIST, servicelist);
			String order_id = null != l.get(i).get("order_id") ? l.get(i).get("order_id").toString() : "";
			params.put("order_id", order_id);

			List<Object> ordermalllist = baseDAO.findSQLMap(params, sqlOrderMall);//场次商品订单信息
			l.get(i).put("ordermalllist", ordermalllist);
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> queryCourtDetailIcon(HashMap<String, String> params) throws Exception {

		String userStatus = params.get(Constant.USER_STATUS) != null 
				? params.get(Constant.USER_STATUS).toString(): null;

		if ("1".equals(userStatus)) {
			params.put(Constant.USER_ID, uuserService.getUserinfoByToken(params).getUserId());
		} else {
			params.put(Constant.USER_ID, Constant.VISITOR);
		}

		String sqlString = "SELECT 2 AS labelb, 0 AS emptys, 0 AS duels, 0 AS challenges, 0 AS matches, bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime, bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status, ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, img.image_court_id, img.img_size_type, img_weight, img.imgurl, img.saveurl , CASE WHEN bct.recommend_court = '1' THEN 4 WHEN bct.popularity_court = '1' THEN 3 WHEN bct.subcourt_id IN ( (SELECT bcs.subcourt_id FROM u_br_courtsession bcs WHERE bcs.order_id IN (SELECT od.order_id FROM u_order od WHERE od.user_id = :user_id))) THEN 2 ELSE -10000 END AS labela FROM u_br_court bct LEFT JOIN u_br_courtsession s ON s.subcourt_id = bct.subcourt_id LEFT JOIN u_court ct ON bct.court_id = ct.court_id LEFT JOIN u_br_courtimage img ON bct.subcourt_id = img.subcourt_id WHERE bct.subcourt_id = :subcourt_id and bct.court_status =1 AND img.img_size_type = 1 AND img.cimg_using_type = 2 GROUP BY bct.subcourt_id, bct.name, bct.bus_sttime, bct.bus_endtime, bct.court_id, bct.score, bct.court_status, bct.court_br_type, bct.bus_status, ct.area, ct.address, ct.position, ct.poi, ct.telephone, ct.traffic, img.image_court_id, img.img_size_type, img_weight, img.imgurl, img.saveurl ORDER BY labela desc ,bct.score DESC";
		String sql = "SELECT * from u_br_courtstaticservice where subcourt_id = :scourtid order by orderbyno  asc";
		List<HashMap> l = baseDAO.findSQLMap(params, sqlString);
		
		if (l != null && l.size() > 0) {
			for (int i = 0; i < l.size(); i++) { // 第一个球场特殊处理
				// 查询球场21天空余场次总数
				String subcourtid = l.get(i).get(Constant.SUBCOURT_ID).toString();

				if (i == 0) {
					l.get(i).put(Constant.EMPTYS, querySessionCountThreeWeeks(subcourtid, Constant.EMPTYS));
					l.get(i).put(Constant.CHALLENGES, querySessionCountThreeWeeks(subcourtid, Constant.CHALLENGES));
					l.get(i).put(Constant.DUELS, querySessionCountThreeWeeks(subcourtid, Constant.DUELS));

				}

				params.put(Constant.SUBCOURT_SERVICE_BY_ID, subcourtid);
				List<Object> servicelist = baseDAO.findSQLMap(params, sql);
				l.get(i).put(Constant.SERVICE_LIST, servicelist);

				// 折扣场次数
				l.get(i).put(Constant.LABEL_B, countLabelBcourt(subcourtid));

			}
			return WebPublicMehod.returnRet(Constant.SUCCESS, l.get(0));
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, null);
	}

	
	/**
	 * 
	 * 按当前日期查询子球场信息
	 * @param params page 页码
	 * @return map 子球场信息
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> querySessionByDateFight(HashMap<String, String> params) throws Exception {
		Integer page = params.get(Constant.PAGE) != null ? new Integer(params.get(Constant.PAGE)) : 1;
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		params.put(Constant.TODAY, cdate);
		params.put(Constant.CURRENT_TIME, ctime);

		String sqlString = "SELECT o.order_type,bct.name, img.imgurl, s.session_id,s.subcourt_id,s.createuser,s.stdate,s.sttime,s.endtime,s.session_duration,s.session_price,s.discount_price,s.member_price,s.activity_price,s.session_status,s.order_status,s.enddate,s.createdate,s.week,s.session_br_useing_status,s.sort,s.sing_update_status,s.order_id,case when s.activity_price is not NULL THEN s.activity_price when s.member_price is not NULL THEN s.member_price WHEN s.discount_price is not NULL THEN s.discount_price  WHEN s.fav_price is not NULL THEN s.fav_price ELSE NULL END as fav_price, date_format(s.stdate,'%m月%d日')AS showdate, date_format(s.stdate,'%Y-%m-%d')AS formatdate FROM u_br_courtsession s left join u_order o on o.order_id = s.order_id  LEFT JOIN u_br_court bct ON bct.subcourt_id = s.subcourt_id LEFT JOIN u_br_courtimage img ON img.subcourt_id = bct.subcourt_id   LEFT JOIN u_duel dl on dl.order_id = o.order_id LEFT JOIN u_challenge chg on chg.order_id = o.order_id    WHERE  (dl.duel_status = 1 or dl.duel_status is NULL) and ( chg.challenge_status = 1 or chg.challenge_status is NULL)  AND  img.img_size_type = 1 AND img.cimg_using_type = 2 AND s.subcourt_id = :subcourt_id AND ( (s.order_status = 2 and (s.activity_price is NOT NULL or s.discount_price is NOT NULL or s.member_price is NOT NULL or s.fav_price is not NULL)) OR o.order_type IN ('2','3') ) AND ((s.stdate> :today AND s.stdate <= (SELECT DATE_ADD(:today, INTERVAL "
				+ (Public_Cache.SESSION_WEEK - 1)
				+ " DAY))) OR(s.stdate = :today AND s.sttime > :ctime AND s.endtime > :ctime)) ORDER BY stdate ASC,sttime ASC";
		sqlString += " limit " + Public_Cache.PAGE_LIMIT + " offset " + ((page - 1) * Public_Cache.PAGE_LIMIT);

		List<HashMap>  l = baseDAO.findSQLMap(params, sqlString);
		if (l!=null&&l.size() > 0) {
			this.getSessionUseingInfo(l);
			for (int i = 0; i < l.size(); i++) {

				// 周几转换
				String week = l.get(i).get("week") != null ? l.get(i).get("week").toString() : null;
				if (week != null) {
					l.get(i).put("week", WebPublicMehod.checkWeekString(week));
				}
				String session_id = l.get(i).get(Constant.SESSION_ID).toString();
				l.get(i).put(Constant.LABEL_B, countLabelBSession(session_id));//查询折扣商品数
				l.get(i).put("fav_price", getFavPrice(session_id, false));//查询优惠价
			}
		}
		
		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}

	/**
	 * 
	 * 按订单ID查询子球场信息
	 * @param order_id 订单ID
	 * @return map 子球场信息
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> querySessionByOrderId(HashMap<String, String> params) throws Exception {
		String hql = "select s.sessionId from UBrCourtsession s where s.orderId = :order_id";
		HashMap<String ,String> retMap = new HashMap<String ,String>();
		List<Object> sessionlist = baseDAO.find(params, hql);
		if (sessionlist!=null&&sessionlist.size()==1) {
			retMap.put("canAddMall", "1");
			retMap.put("sessionid", sessionlist.get(0).toString());
			
		}else {
			retMap.put("canAddMall", "-1");
			retMap.put("sessionid", null);
		
		}
		return WebPublicMehod.returnRet(Constant.SUCCESS, retMap);
	}

	@Override
	public HashMap<String, Object> queryCourtAlbum(HashMap<String, String> params) throws Exception {
		Integer page = params.get(Constant.PAGE) != null ? new Integer(params.get(Constant.PAGE)) : 1;

		//球场相册
		String sqlalbum = "SELECT * FROM u_br_courtimage img WHERE img_size_type =1 AND img.cimg_using_type = 4 AND img.subcourt_id = :subcourt_id ORDER BY img.img_weight DESC ";
		PageLimitPhoto pt = new PageLimitPhoto(page, 0);
		sqlalbum += " limit " + pt.getOffset() + "," + pt.getLimit();
		List<Object> albumlist;
		albumlist = baseDAO.findSQLMap(params, sqlalbum);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("albumlist", albumlist);
		return WebPublicMehod.returnRet(Constant.SUCCESS, result);
		
	}

	/**
	 * 
	 * 根据剩余场次数
	 * 
	 * @param map
	 *            subcourt_id 子球场ID
	 * @return map 子球场信息
	 * @throws Exception
	 * @author kevinzhang
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public HashMap<String, Object> querySessionLeftDelayOneWeek(HashMap<String, String> params) throws Exception {

		Date d = PublicMethod.getDateAfter(new Date(), 7);//得到7天后的时间
		params.put(Constant.TODAY, sdf.format(d));
		String sqlString = "SELECT 0 as emptys,0 as notemptys,500 as minprice,1200 as maxprice, COUNT(s.stdate) AS leftno, s.subcourt_id, date_format(s.stdate,'%Y-%m-%d') as formatdate,date_format(s.stdate,'%m月%d日')as stdate, s.order_status FROM u_br_courtsession s WHERE s.stdate>= :today and s.subcourt_id = :subcourt_id AND s.order_status = 2 GROUP BY s.stdate ORDER BY s.stdate ASC limit "
				+ "0," + Public_Cache.SESSION_WEEK;


		List<HashMap> l= baseDAO.findSQLMap(params, sqlString);
		String formatdate ="";
		String subcourtid  ="";
		String date = "";
		Integer total = 0;
		String dateString ="";
		if(l!=null&&l.size()>0){
			for (int i = 0; i < l.size(); i++) {
				formatdate = l.get(i).get("formatdate").toString();
				l.get(i).put("formatweek", getWeekOfDate(sdf.parse(formatdate)));
				subcourtid = params.get(Constant.SUBCOURT_ID); // l.get(i).get(Constant.SUBCOURT_ID).toString();
			    date = l.get(i).get("formatdate").toString();
				l.get(i).put(Constant.EMPTYS, querySessionCountOneDay(subcourtid, Constant.EMPTYS, date));
				total = querySessionCountOneDay(subcourtid, Constant.DUELS, date)
						+ querySessionCountOneDay(subcourtid, Constant.CHALLENGES, date);
				l.get(i).put("notemptys", total);
				dateString = l.get(i).get("formatdate") != null ? l.get(i).get("formatdate").toString() : null;
				l.get(i).putAll(getMaxAndMin(dateString, subcourtid));
			}
		}

		return WebPublicMehod.returnRet(Constant.SUCCESS, l);
	}

	/**
	 * 查询商城信息
	 */
	@Override
	public HashMap<String, Object> queryProductBySessionCPSTATIC(HashMap<String, String> params) throws Exception {

		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		String cdate = sdf.format(new Date());
		String ctime = stf.format(new Date());
		params.put(Constant.CURRENT_DATE, cdate);//当前日期
		params.put(Constant.CURRENT_TIME, ctime);//当前时间
		HashMap<String, List<Object>> sids = new HashMap<>();//存放sql in里面所需的参数
		// 查询出挑战固定商品
		String sql = "SELECT 1 AS salecount, m.pro_id, m.product_type, m.product_subtype, m.product_detailtype, m.name, m.remark, m.size, m.is_user, m.user_id, m.sale_type, m.product_status, m.createuser, m.createdate, m.createtime, m.price_type, m.price, m.sale_ceiling, m.stdate, m.sttime, m.enddate, m.endtime, m.similar_id, img.imgurl, img.img_size_typwe, img.mimg_using_type, CASE WHEN mc.court_id IS NULL THEN m.all_stock ELSE mc.court_count END AS all_stock FROM u_mall m LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id LEFT JOIN u_mall_court AS mc ON m.pro_id = mc.mall_id WHERE m.pro_id in (" + Public_Cache.CHALLENGE_REFEREE + " ) and img.mimg_using_type = '2'";
		//根据商品同类ID和价格类型查询出价格，（为什么要连接商品图片表？）
		String sqlSub = "   SELECT m.price AS yprice FROM u_mall m LEFT JOIN u_mall_img img ON m.similar_id = img.similar_id WHERE m.price_type = '3' AND m.similar_id = :similar_id_tmp  ";
		List<HashMap<String, String>> lists = baseDAO.findSQLMap(params, sql);
		String similar_id="";//获取同类ID
		List<HashMap<String, Object>> sublists=null;//商品价格集合
		Double yprice=null;//价格-最终使用的
		// 循环商品 获取同类ID
		for (int i = 0; i < lists.size(); i++) {
			similar_id = lists.get(i).get("similar_id") != null ? lists.get(i).get("similar_id").toString()
					: null;
			params.put("similar_id_tmp", similar_id);
			sublists = baseDAO.findSQLMap(params, sqlSub, sids);//sids没有使用上可以去掉
			//更改商品集合中yprice值
			if (sublists != null && sublists.size() > 0) {
				//如果挑战固定商品有价格，那么将价格赋值给最近价格
				if(sublists.get(0).get("yprice")!=null){
					yprice=(Double) sublists.get(0).get("yprice");
				}
				//如果最终价格不为空，那么替换价格并且toString,否则替换价格为null
				if (yprice != null) {
					lists.get(i).put("yprice", yprice.toString());
				} else {
					lists.get(i).put("yprice", null);
				}
			} else {
				lists.get(i).put("yprice", null);
			}
		}	
		return WebPublicMehod.returnRet(Constant.SUCCESS, lists);
	}
	
	@Override
	public HashMap<String, Object> get3rdCourtServiceById(HashMap<String, String> params) throws Exception {
		StringBuilder courtServiceSql = new StringBuilder(
				"select m.price, m_disc.price as discprice, 0 as salecount,m.pro_id, m.product_type,  m.product_subtype, m.product_detailtype, "
				+ "m. name,  m.remark, m.size, m.is_user, m.user_id,	m.sale_type, m.product_status, m.createuser,  m.createdate, m.createtime, "
				+ "m.price_type, m.sale_ceiling, m.stdate, m.sttime, m.enddate, m.endtime,  m.similar_id, "
				+ " img.imgurl, img.img_size_typwe, img.mimg_using_type, "
				+ "case when mc.court_id is null then m.all_stock  "
				+ "else mc.court_count  "
				+ "end as all_stock  "
				+ "from  u_mall m  "
				+ "left join u_mall_img img on m.similar_id = img.similar_id "
				+ " left join u_mall_court as mc on m.pro_id = mc.mall_id  "
				+ "left join u_br_courtpro as ubrcpro on ubrcpro.product_id = m.pro_id  "
				+ "left join u_mall m_disc on m.similar_id = m_disc.similar_id  "
				+ "and m_disc.price_type = '3'  " //优惠价
				+ "where m.price_type = '1'  "	//原价
				+ "and img.img_size_typwe = 1  "
				+ "and m.product_status = 1  "
				+"and ( ( m.stdate < :cdate and :cdate < m.enddate ) "
				+ "or ( m.stdate = :cdate and :ctime >= m.sttime ) "
				+ "or ( :cdate = m.enddate and :ctime < m.endtime ) ) "
				+ "and m.product_subtype = :productsubtype "
				+ "and (ubrcpro.session_id in ( :sessionids )  " //按球场分配的商品
				+ "or mc.court_id is null)  "//不按球场分配的商品
				+ "group by m.similar_id;");  //现版本不精确到pro_id
		
		params.put(Constant.CURRENT_DATE, PublicMethod.getDateToString(new Date(),"yyyy-MM-dd"));//当前时间
		params.put(Constant.CURRENT_TIME, PublicMethod.getDateToString(new Date(),"HH:mm:ss"));//当前日期
		
		Object[] sessionIds = (Object[])params.get(Constant.SESSION_IDS).split(",");//切割场次ID
		List<Object> sIdList = Arrays.asList(sessionIds);//将场次ID，存放到集合中
		HashMap<String, List<Object>> sids = new HashMap<>();//in语句参数使用
		//获取可预订商品及原价，优惠价
		sids.put(Constant.SESSION_IDS, sIdList);
		//第三方可使用的商品列表
		List<HashMap<String, String>> courtService = baseDAO.findSQLMap(params, courtServiceSql.toString(), sids);

		return WebPublicMehod.returnRet(Constant.SUCCESS, courtService);
	}
	
	@Override
	public UCourt getCourtBysubCourtId(HashMap<String, String> params) throws Exception {
		String hql = "select uc from UBrCourt ubrc join ubrc.UCourt uc where ubrc.subcourtId = :subCourtId";
		UCourt uCourt = baseDAO.get(params, hql);
		return uCourt;

	}
 
	/**
	 * 日期转换星期中文
	 * @param date 日期
	 * @return String 星期
	 */
	public static String getWeekOfDate(Date date) {
		
		String[] weekOfDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
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
}