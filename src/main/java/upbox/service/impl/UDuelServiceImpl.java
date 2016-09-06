package upbox.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;

import net.sf.json.JSONObject;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.BdLbsBean;
import upbox.model.PageLimit;
import upbox.model.PageLimitPhoto;
import upbox.model.UBaidulbs;
import upbox.model.UBrCourt;
import upbox.model.UCourt;
import upbox.model.UDuel;
import upbox.model.UDuelBs;
import upbox.model.UDuelCh;
import upbox.model.UDuelChallengeImg;
import upbox.model.UDuelResp;
import upbox.model.UFollow;
import upbox.model.UOrder;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicService;
import upbox.service.TopicService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UOrderService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 约战接口实现
 * 
 * @author wmq
 *
 *         15618777630
 */
@Service("uduelServiceImpl")
public class UDuelServiceImpl implements UDuelService {
	@Resource
	private OperDAOImpl baseDAO;
	private HashMap<String, Object> hash;
	@Resource
	private UUserService uuserService;
	@Resource
	private UTeamService uteamService;
	@Resource
	private UOrderService uorderService;
	@Resource
	private UCourtService ucourtService;
	@Resource
	private MessageService messageService;
	@Resource
	private UPlayerService uplayerService;
	@Resource
	private PublicService publicService;
	@Resource
	private UTeamService uTeamService;
	@Resource
	private LBSService lBSService;
	@Resource
	private UCourtService uCourtService;
	private ArrayList<Object> returnList = null;
	private String sql_temp = "";
	// 约战列表通用SQL前部分查询语句
	private StringBuffer sbSelect = new StringBuffer(
			"select date_format(udc.stdate,'%y年%m月%d日') as stdate,date_format(udc.stdate,'%Y-%m-%d') as stdate_name,case when ubs.bs_id is null then MIN(udc.sttime) else ubs.sttime end as sttime,case when ubs.bs_id is null then MAX(udc.endtime) else ubs.endtime end as endtime,"
					+ "uc.name as courtname,uc.court_br_type,"
					+ "t.name as home_teamname,t.short_name as home_shortname,(select timg1.imgurl from u_team_img as timg1 where timg1.team_id =  udc.team_id and timg1.timg_using_type='1' and timg1.img_size_type='1' ) as home_teamimgurl,case when uc.court_br_type = '2' then udc.price else uo.allprice end as allprice,"
					+ "(select t1.name from u_team as t1 where t1.team_id =  dusp.team_id and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_teamname,du.duel_status,"
					+ "(select t1.short_name from u_team as t1 where t1.team_id =  dusp.team_id and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_shortname,"
					+ "(select upai.name from u_parameter upa left join u_parameter_info upai on upa.pkey_id = upai.pkey_id where upa.params = 'duel_status' and upai.params = du.duel_status) duel_status_name,"
					+ "(select timg1.imgurl from u_team_img as timg1 where timg1.team_id =  dusp.team_id and timg1.timg_using_type='1' and timg1.img_size_type='1' and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_teamimgurl,uo.order_id,"
					+ "ubs.f_goal,udc.pay_proportion,du.effective_status,ubs.k_goal,ubs.f_fj,ubs.bs_id,ubs.k_fj,du.duel_id,du.f_team_id as fteam_id,du.f_user_id as fuser_id,case when ubs.xteam_id is null then dusp.team_id else ubs.xteam_id end as xteam_id,du.duel_recommend_status,udc.duel_pay_type");
	// 关注通用SQL前部分查询语句
	private StringBuffer sbSelect_follow = new StringBuffer(
			"select case when uo.order_id is not null then date_format(sess.stdate,'%y年%m月%d日')  else date_format(udc.stdate,'%y年%m月%d日') end as stdate,"
					+ "case when uo.order_id is not null then MIN(sess.sttime) else udc.sttime end as sttime,"
					+ "case when uo.order_id is not null then MAX(sess.endtime) else udc.endtime end as endtime,uc.name as courtname,"
					+ "t.name as home_teamname,t.short_name as home_shortname,(select timg1.imgurl from u_team_img as timg1 where timg1.team_id =  udc.team_id and timg1.timg_using_type='1' and timg1.img_size_type='1' ) as home_teamimgurl,case when uc.court_br_type = '2' then udc.price else uo.allprice end as allprice,"
					+ "(select t1.name from u_team as t1 where t1.team_id =  dusp.team_id) as away_teamname,du.duel_status,udc.duel_pay_type,"
					+ "(select t1.short_name from u_team as t1 where t1.team_id =  dusp.team_id and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_shortname,"
					+ "(select upai.name from u_parameter upa left join u_parameter_info upai on upa.pkey_id = upai.pkey_id where upa.params = 'duel_status' and upai.params = du.duel_status) duel_status_name,"
					+ "(select timg1.imgurl from u_team_img as timg1 where timg1.team_id =  dusp.team_id and timg1.timg_using_type='1' and timg1.img_size_type='1') as away_teamimgurl,uo.order_id,"
					+ "du.duel_id,udc.pay_proportion,du.f_team_id as fteam_id,dusp.team_id as xteam_id");
	// 约战列表通用SQL前部分join语句
	private StringBuffer sbJoin = new StringBuffer(
			" from u_duel as du left join u_team as t on du.f_team_id = t.team_id "
					+ "left join u_duel_resp as dusp on du.duel_id = dusp.duel_id "
					+ "left join u_team_img as timg on t.team_id = timg.team_id "
					+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
					+ "left join u_duel_bs as ubs on du.duel_id = ubs.duel_id "
					+ "left join u_order as uo on du.order_id = uo.order_id "
					+ "left join u_duel_ch as udc on du.fch_id = udc.key_id "
					+ "left join u_br_court as uc on uc.subcourt_id = udc.court_id ");   
	// 非通用-仅限我关注的约战列表使用
	private StringBuffer sbJoin_follow = new StringBuffer(
			" from u_follow as uf left join u_duel as du on du.duel_id = uf.object_id "
					+ "left join u_duel_ch as udc on du.fch_id = udc.key_id "
					+ "left join u_team as t on du.f_team_id = t.team_id "
					+ "left join u_duel_resp as dusp on du.duel_id = dusp.duel_id "
					+ "left join u_team_img as timg on t.team_id = timg.team_id "
					+ "left join u_br_courtsession as sess on du.order_id = sess.order_id "
					+ "left join u_br_court as uc on udc.court_id = uc.subcourt_id "
					+ "left join u_order as uo on du.order_id = uo.order_id ");
	// 关注通用SQL GROUP
	private StringBuffer sbGroup_follow = new StringBuffer(
			"group by courtname,home_teamname,home_teamimgurl,du.duel_id,udc.duel_pay_type");
	private StringBuffer sbGroup = new StringBuffer(
			"group by courtname,home_teamname,home_teamimgurl,ubs.f_goal,ubs.k_goal,"
					+ "ubs.f_fj,ubs.k_fj,du.duel_id,ubs.fteam_id,ubs.xteam_id,du.duel_recommend_status,udc.duel_pay_type,ubs.bs_id ");
	private String sql = "select team_id from u_player where user_id = :userId and team_id is not null and in_team = '1' "; // 查询球员所在的所有球队
	private StringBuffer selectSql = null;

	@Override
	public HashMap<String, Object> checkTeamDuel(HashMap<String, String> map)
			throws Exception {
		hash = new HashMap<String, Object>();
		// System.out.println(PublicMethod.Maps_Mapo(map));
		int count = baseDAO
				.count("select count(*) from UDuel where UTeam.teamId = :teamId and duelStatus = '1'",
						PublicMethod.Maps_Mapo(map), false);
		if (count > 0) { // 判断发起方约战是否待响应
			hash.put("bool", 1);
		} else {
			hash.put("bool", -1);
		}
		return hash;
	}

	@Override
	public HashMap<String, Object> duelFrist(HashMap<String, String> map)
			throws Exception {
		map.put("userId", map.get("loginUserId"));
		List<UDuel> li = baseDAO
				.findByPage(
						map,
						"from UDuel where duelStatus = '2' and UUser.userId = :userId ",
						1, 0);
		if (null != li && li.size() > 0) {
			return WebPublicMehod.returnRet("success", "1"); // 发起过
		}
		return WebPublicMehod.returnRet("success", "-1"); // 未发起过
	}

	@Override
	public HashMap<String, Object> findMyDuelList(HashMap<String, String> map)
			throws Exception {
		returnList = new ArrayList<Object>();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		selectSql = new StringBuffer();
		map.put("userId", checkNullUser(map).getUserId());
		selectSql
				.append(sbSelect.toString()
						+ sbJoin.toString()
						+ "where (du.f_team_id in ("
						+ sql
						+ ") or dusp.team_id in ("
						+ sql
						+ "))  "
//						+ "and du.duel_status = '1'"
//						+ " and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						
						+ sbGroup.toString()
						+ " order by du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit());
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hash = (HashMap<String, Object>) li.get(index);
				hash.put(
						"duel_pay_type_name",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hash.get("duel_pay_type")));
				hash.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hash.get("duel_recommend_status")));
				returnList.add(hash);
			}
		}
		return WebPublicMehod.returnRet("MyDuelList", returnList);
	}

	@Override
	public HashMap<String, Object> findTeamDuelList(HashMap<String, String> map)
			throws Exception {
		returnList = new ArrayList<Object>();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		selectSql = new StringBuffer();
		selectSql
				.append(sbSelect.toString()
						+ sbJoin.toString()
						+ "where du.effective_status='1' and (du.duel_status='1' or du.duel_status='2' or du.duel_status = '4' ) and (du.f_team_id = :teamId or dusp.team_id = :teamId) "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						+ sbGroup.toString()+",ubs.bs_id "
						+ " order by du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit());
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hash = (HashMap<String, Object>) li.get(index);
				hash.put(
						"duel_pay_type_name",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hash.get("duel_pay_type")));
				hash.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hash.get("duel_recommend_status")));
				returnList.add(hash);
			}
		}
		return WebPublicMehod.returnRet("TeamDuelList", returnList);
	}

	@Override
	public HashMap<String, Object> findPlayerDuelList(
			HashMap<String, String> map) throws Exception {
		selectSql = new StringBuffer();
		returnList = new ArrayList<Object>();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		selectSql
				.append(sbSelect.toString()
						+ sbJoin.toString()
						+ "where du.duel_status <> '3' and du.duel_status <> '5' and du.effective_status='1' and (du.f_team_id in ("
						+ sql
						+ ") or dusp.team_id in ("
						+ sql
						+ ")) "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						+ sbGroup.toString()
						+ " order by du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit());
		// System.out.println("selectSql.toString()=" + selectSql.toString());
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hash = (HashMap<String, Object>) li.get(index);
				hash.put(
						"duel_pay_type_name",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hash.get("duel_pay_type")));
				hash.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hash.get("duel_recommend_status")));
				returnList.add(hash);
			}
		}
		return WebPublicMehod.returnRet("PlayerDuelList", returnList);
	}

	/**
	 * 
	 * 
	 TODO - 判断约战列表筛选条件
	 * 
	 * @param map
	 *            courtType 球场类型 1-自建 2-第三方 payType 支付类型 1=发起方支付、2=线上AA、3=线下AA
	 * @return 2016年3月9日 mazkc
	 */
	private HashMap<String, List<Object>> checkDuelScreen(
			HashMap<String, String> map) {
		StringBuffer sbf = new StringBuffer();
		HashMap<String, List<Object>> hash_ = new HashMap<String, List<Object>>();
		List<Object> courtType = (List<Object>) JSON
				.parse(map.get("courtType"));
		List<Object> payType = (List<Object>) JSON.parse(map.get("payType"));
		if (null != courtType && courtType.size() > 0) { // 球场筛选不为空
			if (!"-1".equals(courtType.get(0).toString())
					&& !"".equals(courtType.get(0).toString())) {
				sbf.append(" and uc.court_br_type in :courttype ");
				hash_.put("courttype", courtType);
			}
		}
		if (null != payType && payType.size() > 0) { // 支付方式筛选不为空
			if (!"-1".equals(payType.get(0).toString())
					&& !"".equals(payType.get(0).toString())) {
				sbf.append(" and udc.duel_pay_type in :duelPayType ");
				hash_.put("duelPayType", payType);
			}
		}
		sql_temp = sbf.toString();
		return hash_;
	}

	/**
	 * 
	 * 
	 TODO - 判断约战列表搜索条件
	 * 
	 * @param map
	 *            serach 队名
	 * @return 2016年3月9日 mazkc
	 */
	private HashMap<String, String> checkDuelSerach(HashMap<String, String> map) {
		StringBuffer sbf = new StringBuffer();
		if (null != map.get("serach")) { // 球场筛选不为空
			sbf.append(" and (t.name like :serch or t.short_name like :serch or (select t1.name from u_team as t1 where t1.team_id =  dusp.team_id) like :serch "
					+ "or (select t1.short_name from u_team as t1 where t1.team_id =  dusp.team_id) like :serch ) ");
			map.put("serch", "%" + map.get("serach") + "%");
		}
		sql_temp = sbf.toString();
		return map;
	}

	@Override
	public HashMap<String, Object> findAllDuelList(HashMap<String, String> map) throws Exception {
		boolean bool = false;
		ArrayList<Object> returnList = new ArrayList<Object>();
		HashMap<String, List<Object>> mapList = new HashMap<String, List<Object>>();
		HashMap<String, Object> hashMap = null;
		List<HashMap<String, Object>> arrayTeamList = baseDAO.findSQLMap(map, "select team_id from u_player where user_id = :loginUserId and team_id is not null and in_team = '1' ");
		StringBuffer selectSql = new StringBuffer();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		if (map.get("appCode") != null && !"".equals(map.get("appCode"))) {
			// 搜索，筛选，排序界面
			if (map.get("search") != null || map.get("filter") != null || map.get("orderBy") != null) {
				getDuelSqlByQuery(map, mapList, page, selectSql);

			// 初始约战界面
			} else {
				if (null != map.get("serachType") && "1".equals(map.get("serachType"))) {
					map = checkDuelSerach(map);
				} else {
					mapList = checkDuelScreen(map);
				}
				selectSql.append(sbSelect.toString() + sbJoin.toString()
						+ "where du.duel_status <> '3' and du.duel_status <> '5'  and du.effective_status = '1' "
						// + "and ((timg.img_size_type = '1' and
						// timg.timg_using_type = '1') or (timg.timg_using_type is
						// null)) "
						+ sql_temp + sbGroup.toString()
						+ "order by du.duel_recommend_status desc, FIELD(du.duel_status,'1','4','2' ),du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit());
			}
		} else {
			if (null != map.get("serachType") && "1".equals(map.get("serachType"))) {
				map = checkDuelSerach(map);
			} else {
				mapList = checkDuelScreen(map);
			}
			selectSql.append(sbSelect.toString() + sbJoin.toString()
					+ "where du.duel_status <> '3' and du.duel_status <> '5' and du.effective_status = '1' "
					// + "and ((timg.img_size_type = '1' and
					// timg.timg_using_type = '1') or (timg.timg_using_type is
					// null)) "
					+ sql_temp + sbGroup.toString()
					+ "order by du.duel_recommend_status desc, FIELD(du.duel_status,'1','4','2' ),du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
					+ page.getOffset() + "," + page.getLimit());
		}
		
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, selectSql.toString(), mapList);
		System.out.println(li.size()+"---------");
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hashMap = (HashMap<String, Object>) li.get(index);
				bool = checkDuelTimeOut(String.valueOf(hashMap.get("duel_id")),
						String.valueOf(hashMap.get("duel_status")),
						PublicMethod.getStringToDate(String.valueOf(hashMap.get("stdate_name")), "yyyy-MM-dd"),
						String.valueOf(hashMap.get("sttime")));
				if (true) {
					hashMap.put("duel_pay_type_name",
							Public_Cache.HASH_PARAMS("duel_pay_type").get(hashMap.get("duel_pay_type")));
					hashMap.put("duel_recommend_status_name", Public_Cache.HASH_PARAMS("duel_recommend_status")
							.get(hashMap.get("duel_recommend_status")));
					hashMap.put("is_my", checkTeamIsMy(hashMap, arrayTeamList));
					returnList.add(hashMap);
				}
			}
		}
		return WebPublicMehod.returnRet("AllDuelList", returnList);
	}
	
	/**
	 * 参数经纬度不为空，获取约战球场ID
	 * @param str 
	 * @param location 经纬度
	 * @return
	 */
	private boolean getDuelListByloc(HashMap<String, String> map, PageLimit page, StringBuffer whereSql,
			StringBuffer orderbySql, HashMap<String, List<Object>> mapList, String str) throws Exception {
	 
//		StringBuffer newSql = new StringBuffer(" select  du.duel_id_int " + sbJoin.toString() + whereSql.toString() + orderbySql.toString());
		StringBuffer newSql = new StringBuffer(" select  du.duel_id_int " + sbJoin.toString());
		if ((mapList.get("area") != null) || (map.get("area") != null && !"".equals(map.get("area")))) {
			newSql.append("  left join u_court ut on ut.court_id = uc.court_id  ");
		}
		if (map.get("service") != null && !"-1".equals(map.get("service"))) {
			newSql.append(" left join u_br_courtstaticservice as ucs on uc.subcourt_id = ucs.subcourt_id ");
		}
		newSql.append(whereSql.toString() +"group by duel_id_int "+ orderbySql.toString());
		String locSql = "";
//		List<HashMap<String, Object>> li = baseDAO.findSQLMap(map,newSql.toString(),mapList);
//		
//		List<Integer> listIntId = new ArrayList<Integer>();//筛选时的最后lbs排序条件传入的int主键
//		if (null != li && li.size() > 0) {
//			for (HashMap<String, Object> duel : li) {
//				listIntId.add((Integer) duel.get("duel_id_int"));
//			}
//		}
//		String str="duel_status:[1]|pay_type:[1]";
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "5000000", "duel", "distance:1", null,"4",str);
		
		for (BdLbsBean lbstemp : lbsList) {
			if(lbstemp.getObject_id()!=null&&!"".equals(lbstemp.getObject_id())){
				locSql += "'" + lbstemp.getObject_id() + "',";
			}else{
				locSql += "'" + lbstemp.getDuelid() + "',";
			}
			
		}
	
		if (locSql.length() > 0) {
			locSql = locSql.substring(0, locSql.length() - 1);
			whereSql.append(" and du.duel_id in (" + locSql + ") ");
			if (orderbySql == null || orderbySql.length() == 0) {
				orderbySql.append(" order by field (du.duel_id," + locSql + ") ");
			} else {
				orderbySql.append(" ,field (du.duel_id," + locSql + ") ");
			}
			return false;
		} else {
			if (map.get("status") != null && !"2".equals(map.get("status"))) {
				whereSql.append(" and du.duel_id in ('null') ");
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 TODO - 验证约战是否是自己发起的
	 * 
	 * @param hashMap
	 * @return 1-是 -1 否 2016年4月22日 mazkc
	 */
	private String checkTeamIsMy(HashMap<String, Object> hashMap,
			List<HashMap<String, Object>> arrayTeamList) {
		long count = 0;
		String fteamid = String.valueOf(hashMap.get("fteam_id")); // 发起方队伍ID
		String xteamid = String.valueOf(hashMap.get("xteam_id")); // 响应放队伍ID
		if (null != arrayTeamList && arrayTeamList.size() > 0) {
			count = arrayTeamList
					.stream()
					.filter(v -> v.get("team_id").equals(fteamid)
							|| v.get("team_id").equals(xteamid)).count();
			if (count > 0) {
				return "1";
			}
		}
		return "-1";
	}

	/**
	 * 
	 * 
	 TODO - 获取TeamId
	 * 
	 * @param map
	 *            userId 用户ID
	 * @return 2016年3月8日 mazkc
	 * @throws Exception
	 */
	private UTeam getTeamId(HashMap<String, String> map) throws Exception {
		map.put("userId", map.get("loginUserId"));
		UTeam team = (UTeam) uteamService.getUteaminfoByUserId(map)
				.get("uTeam");
		if (null == team)
			team = new UTeam();
		return team;
	}

	@Override
	public UDuelCh addDuelCourt(HashMap<String, String> map, UDuelCh ch)
			throws Exception {
		UBrCourt uBrCourt = new UBrCourt();
		uBrCourt.setSubcourtId(map.get("subCourtId")); // 球场ID
		ch.setUBrCourt(uBrCourt);
		return ch;
	}

	@Override
	public UDuelCh addDuelDate(HashMap<String, Object> map, UDuelCh ch)
			throws Exception {
		ch.setStdate((Date) map.get("stdate"));
		ch.setSttime(map.get("sttime").toString());
		ch.setEndtime(map.get("endtime").toString());
		return ch;
	}

	@Override
	public UDuelCh addDuelTeam(HashMap<String, String> map,
			List<String> duelTeamList, UDuelCh ch, UDuel duel) throws Exception {
		if (null != duelTeamList && duelTeamList.size() > 0) {
			ch.setDuelAppoint("1");
			duelTeamList.forEach(v -> {
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("teamId", v);
				UDuelResp resp = new UDuelResp();
				try {
					UTeam team = uteamService.getUteaminfoByteamId(hash);
					// 传入teamId获取球队对象
					resp.setKeyId(WebPublicMehod.getUUID());
					resp.setUUser(team.getUUser());
					resp.setUTeam(team);
					resp.setStdate(new Date());
					resp.setSttime(new Date());
					resp.setChallengeRespStatus("2");
					resp.setRespDuelStatus("1");
					resp.setrRespDuelStatus("2");
					resp.setFchId(ch.getKeyId());
					resp.setUDuel(duel);
					baseDAO.save(resp); // 保存约战响应表信息
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			// for (String v : duelTeamList) {
			// HashMap<String, String> hash = new HashMap<String, String>();
			// hash.put("teamId", v);
			// UDuelResp resp = new UDuelResp();
			// UTeam team = uteamService.getUteaminfoByteamId(hash); //
			// 传入teamId获取球队对象
			// resp.setKeyId(WebPublicMehod.getUUID());
			// resp.setUUser(team.getUUser());
			// resp.setUTeam(team);
			// resp.setStdate(new Date());
			// resp.setSttime(new Date());
			// resp.setChallengeRespStatus("2");
			// resp.setRespDuelStatus("1");
			// resp.setrRespDuelStatus("2");
			// resp.setFchId(ch.getKeyId());
			// resp.setUDuel(duel);
			// baseDAO.save(resp); // 保存约战响应表信息
			// }
		} else {
			ch.setDuelAppoint("2");
		}
		return ch;
	}

	@Override
	public UDuelCh addDuelAccount(HashMap<String, String> map, UDuelCh ch)
			throws Exception {
		ch.setDuelPayType(map.get("duelPayType"));
		ch.setPayProportion(map.get("payProportion"));
		return ch;
	}

	@Override
	public UDuelCh addDuelTalk(HashMap<String, String> map, UDuelCh ch)
			throws Exception {
		ch.setRemark(map.get("remark"));
		return ch;
	}

	@Override
	public UDuel addDuelImg(HashMap<String, String> map, UDuel duel,
			List<UDuelChallengeImg> duelImgList) throws Exception {
		map.put("objectId", duel.getDuelId());
		if (null != duelImgList && duelImgList.size() > 0) {
			duelImgList
					.forEach(uci -> {
						try {
							uci.setKeyId(WebPublicMehod.getUUID());
							uci.setObjectId(duel.getDuelId());
							uci.setImgSaveType("1");
							uci.setCreatetime(new Date());
							uci.setDuelChallUsingType(map
									.get("duel_chall_using_type"));
							baseDAO.save(uci);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
		}
		return duel;
	}

	@Override
	public HashMap<String, Object> addDuelOrder(HashMap<String, String> map,
			List<String> duelTeamList, List<UDuelChallengeImg> duelImgList)
			throws Exception {
		UOrder uo = null;
		UDuelCh ch = new UDuelCh();
		hash = PublicMethod.Maps_Mapo(map);
		String addDuelType = map.get("addDuelType");
		hash.put("stdate",
				PublicMethod.getStringToDate(map.get("stdate"), "yyyy-MM-dd"));
			UTeam team = getTeamById(map);
//		if (null == team || null == team.getTeamId()) {
//			WebPublicMehod.returnRet("error", "虽然您有发起约战的权限，但是前端没把球队ID给我，所以您不能发起约战");
//		}
//		if (team.getTeamCount() < Public_Cache.TEAM_COUNT) {
//			WebPublicMehod.returnRet("error", "您的队伍人数符合条件，但是发起约战使用的球队ID传错了，所以您不能发起约战");
//		}
		if (null != addDuelType && "1".equals(addDuelType)) {
			addDuelAgain(map);
		}
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
			uo = (UOrder) uorderService.findOrderinfo(map).get("success"); // 验证订单是否已经支付成功

			if ("1".equals(uo.getOrderstatus())) { // 订单已支付
				hash.put("subcourtId", map.get("subCourtId"));
				hash.put("sttime", map.get("sttime"));
				int check = checkDuelDteAdd(hash);
				if (check == 1) {
					WebPublicMehod.returnRet("error", "已经存在相同时间、相同地点的约战");
				} /*
				 * else if (check == 2) { WebPublicMehod.returnRet("error",
				 * "已经存在相同时间的约战,是否确认继续"); }
				 */
				map.put("orderId", uo.getOrderId());
				uorderService.editRelationTypeOrder(map);
			} else {
				WebPublicMehod.returnRet("error", "订单未支付,请确认");
			}

			if (!"1".equals(uo.getOrderstatus())) {
				WebPublicMehod.returnRet("error", "订单未支付,请确认");
			}
		}
		ch.setKeyId(WebPublicMehod.getUUID());
		map.put("fchId", ch.getKeyId());
		map.put("teamId", team.getTeamId());
		ch.setUUser(uuserService.getUserinfoByToken(map));
		ch.setTeamId(map.get("teamId"));
		ch = addDuelCourt(map, ch);
		ch = addDuelDate(hash, ch);
		ch = addDuelAccount(map, ch);
		ch = addDuelTalk(map, ch);
		if (map.get("price") != null && !"".equals(map.get("price"))) {
			ch.setPrice(map.get("price"));
		} else {
			ch.setPrice("0.0");
		}
		UDuel duel = insertUduel(map, ch, uo);
		ch = addDuelTeam(map, duelTeamList, ch, duel);
		baseDAO.save(ch);
		map.put("duelId", duel.getDuelId());
		int is3rdCourt = check3rdCourt(hash);
		if (is3rdCourt > 0) {
			this.insertDuelGeoData(map,duel);
		}
		map.put("duel_chall_using_type", "1");
		addDuelImg(map, duel, duelImgList);
//		updateBehavior(map, duel, "球队首次发起约战", "4");
		updateBehavior(map, duel, null, "4");
		publicService.addTeamActivity("1", team.getTeamId());
		hash.clear();
		hash.put("duel", duel.getDuelId());
		hash.put("teamname", team.getName());
		return hash;
	}

	/**
	 * 
	 * TODO 上传LBS约战数据、修改上传的LBS约战数据,创建本地数据库(u_baidulbs)表数据
	 * @param map
	 * @param duel 约战对象
	 * @throws Exception
	 */
	private void insertDuelGeoData(HashMap<String, String> map,UDuel duel) throws Exception {
		HashMap<String, Object> geoParam = new HashMap<String, Object>();
		UCourt uCourt = uCourtService.getCourtBysubCourtId(map);
		map.put("id", map.get("duelId"));
		int duelIdInt = publicService.getIntKeyId(5, map);
		geoParam.put("title", "约战数据");
		geoParam.put("address", uCourt.getAddress());
		geoParam.put("tags", "duel");
		geoParam.put("latitude", uCourt.getBdPoi());
		geoParam.put("longitude", uCourt.getBdPosition());
		geoParam.put("coord_type", "3");
		geoParam.put("geotable_id", Constant.BAIDU_DUEL_TABLE_ID);
		geoParam.put("url", Public_Cache.LBS_LOCATION);
		geoParam.put("object_id", map.get("duelId"));
		geoParam.put("params_type", "4"); // 约战
		geoParam.put("date", PublicMethod.getDateToString(new Date(), "yyyy-MM-dd"));
		geoParam.put("duel_intid", duelIdInt); // 约战数字Id
		geoParam.put("duelid", map.get("duelId"));// 约战UUID
		geoParam.put("areaid", uCourt.getArea());// 约战区域ID
		geoParam.put("court_type", uCourt.getCourtType());// 球场类型
		geoParam.put("pay_type", map.get("duelPayType"));// 支付方式
		if (duel.getUOrder() != null) {
			geoParam.put("price", duel.getUOrder().getAllprice());// 价格
		}
		geoParam.put("duel_status", duel.getDuelStatus());//约战状态 

		String ret = "";
		
		try {
			lBSService.createGeodata(geoParam);
		} catch (Exception e) {
			e.printStackTrace();
			lBSService.createUbaidulbsDataError(map.get("duelId"),"4","APP发起约战上报失败");
		}
		//ret不为空，添加本地数据
		if (!"".equals(ret)) {
			JSONObject result = JSONObject.fromObject(ret);
			String geoId = result.getString("result");
			JSONObject json = JSONObject.fromObject(geoId);
			String status = json.getString("status");
			if ("0".equals(status)) {
				String id = json.getString("id");
				lBSService.createUbaidulbsData(map.get("duelId"),"4",id);
			}else{
				lBSService.createUbaidulbsDataError(map.get("duelId"),"4","APP发起约战上报失败");
			}
		}else{
			lBSService.createUbaidulbsDataError(map.get("duelId"),"4","APP发起约战上报失败");
		}
	}
	/**
	 * 
	 * TODO 上传LBS约战数据、修改上传的LBS约战数据,创建本地数据库(u_baidulbs)表数据
	 * @param map
	 * @param act 1=上传（添加），2=修改
	 * @throws Exception
	 */
	private void updateDuelGeoData(HashMap<String, String> map,String act) throws Exception {
		HashMap<String, Object> geoParam = new HashMap<String, Object>();
		geoParam.put("geotable_id", Constant.BAIDU_DUEL_TABLE_ID);
		geoParam.put("url", Public_Cache.LBS_LOCATION);
		//修改上传到百度LBS数据
		geoParam.put("duel_status", act);//约战状态 
		UBaidulbs baidulbs = lBSService.getBaidulbs("4", map.get("duelId"));
		// 如果根据ID找到对应的LBS上的ID，那么根据LBS上的ID修改LBS百度数据，否者上传数据
		if (baidulbs != null && !"".equals(baidulbs.getLbsData())) {// 修改
			geoParam.put("id", baidulbs.getLbsData());
			try {
				lBSService.updateGeodata(geoParam);// 上传到百度返回的ID json格式的
			} catch (Exception e) {
				e.printStackTrace();
				lBSService.createUbaidulbsDataError(map.get("duelId"),"4","APP发起约战更新失败");
			}
			
		}
		
	}
	
	/**
	 * 查看约战是否由第三方（其他）球场发起
	 * @param map
	 * @return 0 是 >0 不是
	 * @throws Exception
	 */
	private int check3rdCourt(HashMap<String, Object> map ) throws Exception {
		int count = baseDAO.count("select count(*) from UBrCourt as ubrc where ubrc.subcourtId = :subCourtId",
						map, false);
		return count;
	}

	private void updateBehavior(HashMap<String, String> map, UDuel duel,
			String objectType, String behaviorType) throws Exception {
		map.put("type", "2");
		map.put("objectId", duel.getDuelId());
		map.put("objectType", objectType);
		map.put("behaviorType", behaviorType);
		publicService.updateBehavior(map);
	}

	/**
	 * 
	 * 
	 TODO - 生成约战发起方
	 * 
	 * @param map
	 * @param ch
	 *            2016年3月9日 mazkc
	 * @throws Exception
	 */
	private UDuel insertUduel(HashMap<String, String> map, UDuelCh ch, UOrder uo)
			throws Exception {
		UDuel duel = new UDuel();
		duel.setDuelId(WebPublicMehod.getUUID());
		duel.setDuelType("1");
		duel.setUDuelCh(ch);
		duel.setUTeam(getTeamById(map));
		duel.setUUser(uuserService.getUserinfoByToken(map));
		duel.setStdate(new Date());
		duel.setSttime(new Date());
		duel.setDuelStatus("1");
		duel.setUOrder(uo);
		duel.setDuelRecommendStatus("0");
		duel.setEffectiveStatus("1");
		duel.setJpusho("-1");
		baseDAO.save(duel);
		baseDAO.getSessionFactory().getCurrentSession().flush();
		return duel;
	}

	/**
	 * 
	 * 
	 TODO - 验证约战ID是否为主ID，如果不是那么获取主ID
	 * 
	 * @param map
	 * @return 2016年3月10日 mazkc
	 * @throws Exception
	 */
	private String checkDuelBs(HashMap<String, String> map) throws Exception {
		// 确认是否是主约战信息ID
		int count = baseDAO.count(
				"select count(*) from UDuel where duelId = :duelId",
				PublicMethod.Maps_Mapo(map), false);
		if (count > 0) { // 是主约战信息ID
			return map.get("duelId");
		} else {
			UDuelBs ubs = baseDAO.get(map, "from UDuelBs where bsId = :duelId");
			return ubs.getUDuel().getDuelId();
		}
	}

	@Override
	public HashMap<String, Object> followDuel(HashMap<String, String> map)
			throws Exception {
		UFollow flo = baseDAO
				.get(map,
						"from UFollow where objectId = :duelId and userFollowType = '4' and UUser.userId = :loginUserId");
		if (null == flo) {
			flo = new UFollow();
			flo.setKeyId(WebPublicMehod.getUUID());
			flo.setUserFollowType("4");
			flo.setUUser(uuserService.getUserinfoByToken(map));
			flo.setCreatedate(new Date());
			flo.setObjectId(checkDuelBs(map));
		}
		flo.setFollowStatus("1");
		baseDAO.saveOrUpdate(flo);
		return WebPublicMehod.returnRet("success", "关注成功");
	}

	@Override
	public HashMap<String, Object> delFollowDuel(HashMap<String, String> map)
			throws Exception {
		UFollow flo = baseDAO
				.get(map,
						"from UFollow where objectId = :duelId and userFollowType = '4' and UUser.userId = :loginUserId");
		flo.setFollowStatus("2");
		baseDAO.update(flo);
		return WebPublicMehod.returnRet("success", "取消关注成功");
	}

	@Override
	public HashMap<String, Object> myFollowDuelList(HashMap<String, String> map)
			throws Exception {
		returnList = new ArrayList<Object>();
		selectSql = new StringBuffer();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		map.put("userId", checkNullUser(map).getUserId());
		selectSql
				.append(sbSelect_follow.toString()
						+ sbJoin_follow.toString()
						+ " where uf.user_follow_type = '4' and uf.follow_status = '1' "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or (timg.timg_using_type is null)) "
						+ "and uf.user_id = :loginUserId "
						+ sbGroup_follow.toString()
						+ " order by du.stdate desc,du.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit()).toString();
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hash = (HashMap<String, Object>) li.get(index);
				hash.put(
						"duel_pay_type_name",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hash.get("duel_pay_type")));
				hash.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hash.get("duel_recommend_status")));
				returnList.add(hash);
			}
		}
		return WebPublicMehod.returnRet("MyFollowDuelList", returnList);
	}

	@Override
	public HashMap<String, Object> duelInfo(HashMap<String, String> map)
			throws Exception {
		selectSql = new StringBuffer();
		map.put("userId", checkNullUser(map).getUserId());
		selectSql
				.append(sbSelect.toString()
						+ ",du.duel_recommend_status,uo.allprice,udc.duel_pay_type,dci.imgurl "
						+ sbJoin.toString()
						+ " left join u_duel_challenge_img as dci on dci.object_id = du.fch_id "
						+ "where dci.duel_chall_using_type = '1' "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						+ "and du.duel_id = :duelId ");
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		return WebPublicMehod.returnRet("duelInfo", li);
	}

	@Override
	public HashMap<String, Object> addDuelAgain(HashMap<String, String> map)
			throws Exception {
		
		if(null != map.get("orderid") && !"".equals(map.get("orderid"))){
			// 使约战失效
						baseDAO.executeHql(
								"update UDuel set effectiveStatus = '2',duel_status = '5' where UOrder.orderId = :orderid",
								PublicMethod.Maps_Mapo(map));
		}else{
			// 使约战失效
			delDuel(map);
			//查询响应方订单ID
			List<UDuelResp> list = baseDAO.find(map, "from UDuelResp where UDuel.duelId = :duelId"); 
			HashMap<String,String> _map = new HashMap<String, String>();
			if(null != list && list.size() > 0){
				for(UDuelResp resp : list){
					if(null != resp && null != resp.getUOrder() && ("3".equals(resp.getUOrder().getOrderstatus()) || "4".equals(resp.getUOrder().getOrderstatus()))){
						_map.put("orderId", resp.getUOrder().getOrderId());
						uorderService.cancelOrder(_map);
					}
				}
			}
		}
		//修改LBS百度上传的约战状态
		baseDAO.getSessionFactory().getCurrentSession().flush();
		UDuel duel=baseDAO.get(UDuel.class,map.get("duelId"));
		this.updateDuelGeoData(map, duel.getDuelStatus());//修改LBS百度上传的约战状态
		return null;
	}

	@Override
	public HashMap<String, Object> delDuel(HashMap<String, String> map)
			throws Exception {
		// 使约战失效
		baseDAO.executeHql(
				"update UDuel set effectiveStatus = '2',duel_status = '5' where duelId = :duelId",
				PublicMethod.Maps_Mapo(map));
		return WebPublicMehod.returnRet("success", "ok");
	}

	@Override
	public HashMap<String, Object> respDuel(HashMap<String, String> map)
			throws Exception {
		hash = new HashMap<String, Object>();
		UOrder uo = null;
		UTeam team = getTeamById(map);
		map.put("teamId", team.getTeamId());
		UDuel du = baseDAO.get(UDuel.class, map.get("duelId"));
		int checkDuelType = checkRespDuel(map, du, team);
		if (0 != checkDuelType) { // 判断约战状态
			WebPublicMehod.returnRet("success", checkDuelType);
		}
		/*
		 * if (null != map.get("mallList") && !"".equals(map.get("mallList"))) {
		 * listMall = (List<UMall>) SerializeUtil.unSerializeToList(
		 * map.get("mallList"), UMall.class); }
		 */
		UDuelResp ur = baseDAO
				.get(map,
						"from UDuelResp where UDuel.duelId = :duelId and UTeam.teamId = :teamId ");
		if (null == ur) {
			ur = new UDuelResp();
			ur.setKeyId(WebPublicMehod.getUUID());
			ur.setUDuel(du);
			ur.setUUser(uuserService.getUserinfoByToken(map));
			ur.setUTeam(getTeamById(map));
			ur.setRespDuelStatus("1");
			ur.setFchId(du.getUDuelCh().getKeyId());
		}
		ur.setStdate(new Date());
		ur.setSttime(new Date());
		if (null != map.get("orderId") && !"".equals(map.get("orderId"))) { // 自营球场约战响应
			hash = uorderService.saveAppendDuelAndChallOrder(map);
			uo = (UOrder) hash.get("order");
			// 删除存在的订单缓存
			uorderService.removeRedis(map, uo);
			// 添加新的订单缓冲
			uorderService.getOrderHRedisKey(uo, map);
			ur.setUOrder(uo);
			if ("-1".equals(hash.get("ifPay"))) {
				ur.setrRespDuelStatus("1");
				du.setDuelStatus("4");
				ur.setChallengeRespStatus("1");
				SendDuelMes(map, ur);
				//战达成里程碑数据添加
				this.behaviorTypeDule(map,du,ur);
				this.behaviorTypeTeamDule(map,du,ur);
			} else {
				ur.setrRespDuelStatus("2");
			}
		} else { // 第三方球场约战响应
			ur.setUOrder(uo);
			ur.setChallengeRespStatus("1");
			respOutherDuel(map, du, ur);
		}
		baseDAO.saveOrUpdate(ur);
		baseDAO.update(du);
		hash.put("duelId", du.getDuelId());
		hash.remove("order");
		hash.put("teamname", team.getName());
		publicService.addTeamActivity("1", team.getTeamId());
		
		baseDAO.getSessionFactory().getCurrentSession().flush();
		this.updateDuelGeoData(map, du.getDuelStatus());//修改LBS百度上传的约战状态
		
		return WebPublicMehod.returnRet("success", hash);
	}

	// 判断约战是否过期 过期那么设置成过期
	private boolean checkDuelTimeOut(String duelId, String duel_status,
			Date stdate, String sttime) throws ParseException, Exception {
		if ("1".equals(duel_status)
				&& WebPublicMehod
						.compare_date(PublicMethod.getStringToDate(
								PublicMethod.getDateToString(stdate,
										"yyyy-MM-dd") + " " + sttime,
								"yyyy-MM-dd HH:mm"), new Date()) == -1) {
			baseDAO.executeHql("update UDuel set duelStatus = '1' where duelId = '"
					+ duelId + "'");
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 TODO - 第三方约战响应
	 * 
	 * @param map
	 *            2016年4月20日 mazkc
	 * @throws Exception
	 */
	private void respOutherDuel(HashMap<String, String> map, UDuel du,
			UDuelResp ur) throws Exception {
		List<UDuelChallengeImg> duelImgList = (List<UDuelChallengeImg>) SerializeUtil
				.unSerializeToList(map.get("duelImgList"),
						UDuelChallengeImg.class);
		addDuelImg(map, du, duelImgList);
		ur.setrRespDuelStatus("1");
		ur.setRemark(map.get("remark"));
		du.setDuelStatus("4");
		SendDuelMes(map, ur);
		map.put("teamId", map.get("guest_teamId"));
//		updateBehavior(map, du, "球队首次成功响应约战", "5");
//		updateBehavior(map, du, "球队首次响应其他球队约战", "6");
		updateBehavior(map, du, null, "5");
		updateBehavior(map, du, null, "6");
		//战达成里程碑数据添加
		this.behaviorTypeDule(map,du,ur);
		this.behaviorTypeTeamDule(map,du,ur);
	}

	// 判断约战是否可以响应
	// -1已过期 -2已响应 -3只有队长才可响应 -4非指定队伍响应 -5队伍人数需要大于7人 -6同一个队伍无法响应自己发起的约战 0-可响应
	private int checkRespDuel(HashMap<String, String> map, UDuel duel,
			UTeam team) throws Exception {
		List<UDuelResp> li = null;
		int returnType = 0; // 默认可响应
		if (null == duel) {
			duel = baseDAO.get(UDuel.class, map.get("duelId"));
		}
		if ("3".equals(duel.getDuelStatus())
				|| WebPublicMehod.compare_date(PublicMethod.getStringToDate(
						PublicMethod.getDateToString(duel.getUDuelCh()
								.getStdate(), "yyyy-MM-dd")
								+ " " + duel.getUDuelCh().getSttime(),
						"yyyy-MM-dd HH:mm"), new Date()) == -1) {
			if ("1".equals(duel.getDuelStatus())) {
				duel.setDuelStatus("3");
				baseDAO.update(duel);
			}
			returnType = -1;
		} else {
			if (null == team || null == team.getTeamId()
					|| "".equals(team.getTeamId())) {
				returnType = -3;
			} else {

				if (team.getTeamCount() < Public_Cache.TEAM_COUNT) {
					returnType = -5;
				} else {

					if ("4".equals(duel.getDuelStatus())
							|| "2".equals(duel.getDuelStatus())) {
						returnType = -2;
					} else {
						if ("1".equals(duel.getUDuelCh().getDuelAppoint())) {
							li = baseDAO
									.find(map,
											"from UDuelResp where UDuel.duelId = :duelId and respDuelStatus = '1' and UTeam.teamId = :teamId ");
							if (null == li || li.size() == 0) { // 如果是指定约战判断是否是指定队伍响应
								return -4;
							} 
						} else {
							if (map.get("teamId").equals(duel.getUTeam().getTeamId())) {
								return -6;
							}
						}
					}
				}
			}
		}
		return returnType;
	}

	@Override
	public UDuel recommendDuel(HashMap<String, String> map) throws Exception {
		return baseDAO.get(UDuel.class, map.get("duelId"));
	}

	// 验证用户对象是否为null
	private UUser checkNullUser(HashMap<String, String> map) throws Exception {
		UUser user = uuserService.getUserinfoByUserId(map);
		if (null == user)
			return new UUser();
		return user;
	}

	@Override
	public HashMap<String, Object> getDuelTeamList(HashMap<String, String> map)
			throws Exception {
		HashMap<String, List<Object>> mapList = new HashMap<String, List<Object>>();
		map.put("userId", checkNullUser(map).getUserId());
		map.put("teamId", getTeamById(map).getTeamId());
		String sql = "select dur.team_id as team_id from u_duel as du,u_duel_resp as dur where du.duel_id = dur.duel_id "
				+ "and du.f_user_id = :userId and du.f_team_id = :teamId and du.effective_status = '1' and (du.duel_status = '1' or du.duel_status = '4') "
				+ "order by dur.stdate desc limit 0," + Public_Cache.PAGE_LIMIT;
		List<HashMap> li = baseDAO.findSQLMap(map, sql);
		List<Object> arrayList = li.stream().map(v -> v.get("team_id"))
				.collect(Collectors.toList());
		mapList.put("teamListId", arrayList);
		return uteamService.getUteamList(map, mapList);
	}

	@Override
	public HashMap<String, Object> getDuelInfo(HashMap<String, String> map)
			throws Exception {
		UFollow uf = null;
		ArrayList<Object> li = null;
		hash = new HashMap<String, Object>();
		String floowStatus = "2";
//		map.put("teamId", getTeamById(map).getTeamId());
		UUser user = checkNullUser(map); // 根据用户ID查询用户对象
		if (null != user) {
			map.put("userId", checkNullUser(map).getUserId());
			uf = baseDAO
					.get(map,
							"from UFollow where UUser.userId = :userId and userFollowType = '4' and objectId = :duelId"); // 查询关注表
		}
		if (null != uf) {
			floowStatus = uf.getFollowStatus();
		}

		selectSql = new StringBuffer();
		selectSql.append(sbSelect.toString() + sbJoin.toString()
				+ "where du.duel_id = :duelId  ");
		// 判断是否查询小场次详情
		if (null != map.get("bs_id") && !"".equals(map.get("bs_id"))) {
			selectSql.append(" and ubs.bs_id = :bs_id ");
		}
		li = (ArrayList<Object>) baseDAO.findSQLMap(map, selectSql.toString());
		if (null != li && li.size() > 0) {
			hash = (HashMap<String, Object>) li.get(0);
			hash.put(
					"duel_pay_type_name",
					Public_Cache.HASH_PARAMS("duel_pay_type").get(
							hash.get("duel_pay_type")));
			hash.put(
					"duel_recommend_status_name",
					Public_Cache.HASH_PARAMS("duel_recommend_status").get(
							hash.get("duel_recommend_status")));
		}
		baseDAO.getSessionFactory().getCurrentSession().clear();
		hash.put(
				"respDuel",
				checkRespDuel(map, String.valueOf(hash.get("fteam_id")),
						String.valueOf(hash.get("xteam_id"))));
		//是否是约战发起者本人
		if (hash.get("fuser_id") != null && !"".equals(hash.get("fuser_id"))) {
			if (hash.get("fuser_id").toString().equals(map.get("userId"))) {
				hash.put("fuser", "1");
			} else {
				hash.put("fuser", "0");
			}
		} else {
			hash.put("fuser", "0");
		}
		// follow = 1-关注 2-未关注
		hash.put("follow", floowStatus);
		return WebPublicMehod.returnRet("DuelInfo", hash);
	}
	
	/**
	 * 根据版本号验证进入页面人的响应状态
	 * @param map
	 * @param fteamId  发起方队伍ID
	 * @param xteamId 响应放队伍ID
	 * @return
	 * @throws Exception
	 */
	private String checkRespDuel(HashMap<String, String> map, String fteamId, String xteamId) throws Exception {
		if (map.get("appCode") != null && !"".equals(map.get("appCode"))) {
			return checkRespDuelNew(map,fteamId,xteamId);
		} else {
			return checkRespDuelOld(map,fteamId,xteamId);
		}
	}

	/**
	 * 
	 * 
	 TODO - 验证进入页面人的响应状态（旧版）
	 * 
	 * @param map
	 * @param fteamId
	 *            发起方队伍ID
	 * @param xteamId
	 *            响应放队伍ID
	 * @return
	 * @throws Exception
	 */
	private String checkRespDuelOld(HashMap<String, String> map, String fteamId, String xteamId) throws Exception {
		// respDuel = -1 发起方队长进入 -2响应方队长进入 -3第三方进入 -4发起方队员 -5响应方队员
		String checkTemp = "";
		// 发起方队长
		boolean ftop = false;
		// 发起方队员
		boolean flow = false;
		// 响应方队长
		boolean xtop = false;
		// 响应方队员
		boolean xlow = false;
		// 路人
		boolean zteam = false;
		map.put("type", "5");// 约战权限
		List<HashMap<String, Object>> teamMap = uTeamService.getUteamRoleByUserId(map);
		for (HashMap<String, Object> temp : teamMap) {
			String teamId = (String) temp.get("teamId");
			String duel = (String) temp.get("duel");
			if (teamId.equals(fteamId)) {
				if (duel.equals("1")) {
					ftop = true;
				} else if (duel.equals("-1")) {
					flow = true;
				}
			} else if (teamId.equals(xteamId)) {
				if (duel.equals("1")) {
					xtop = true;
				} else if (duel.equals("-1")) {
					xlow = true;
				}
			} else {
				zteam = true;
			}
		}
		// 老接口优先级排序
		if (ftop) {
			checkTemp = "-1";
		} else if (xtop) {
			checkTemp = "-2";
		} else if (flow) {
			checkTemp = "-4";
		} else if (xlow) {
			checkTemp = "-5";
		} else if (zteam) {
			checkTemp = "-3";
		}

		return checkTemp;

		/*
		 * // 查询进入页面人所见的响应状态 String respDuelSql =
		 * "select case when du.f_team_id = :teamId then '-1' else '0' end as fdu_status,"
		 * +
		 * "case dur.team_id when :teamId then '-2' else '-3' end as xdu_status "
		 * +
		 * "from u_duel as du left join u_duel_resp as dur on du.duel_id = dur.duel_id "
		 * + "where du.duel_id = :duelId " + "group by fdu_status"; List<Object>
		 * respDuelList = baseDAO.findSQLMap(map, respDuelSql); HashMap<String,
		 * Object> hash = null; if (null != respDuelList && respDuelList.size()
		 * > 0) { hash = (HashMap<String, Object>) respDuelList.get(0); String
		 * fdu_status = (String) hash.get("fdu_status"); String xdu_status =
		 * (String) hash.get("xdu_status"); if ("-1".equals(fdu_status)) {
		 * returnStr = "-1"; } else { returnStr = xdu_status; // -2 or -3 } }
		 */
	}
	
	/**
	 * 
	 * 
	 TODO - 验证进入页面人的响应状态（新版）
	 * 
	 * @param map
	 * @param fteamId
	 *            发起方队伍ID
	 * @param xteamId
	 *            响应放队伍ID
	 * @return
	 * @throws Exception
	 */
	private String checkRespDuelNew(HashMap<String, String> map, String fteamId, String xteamId) throws Exception {
		// respDuel = -1 发起者本人 -2非发起方队长 -3路人
		String checkTemp="";
		// 发起方队长
		boolean ftop = false;
		// 发起方队员
		boolean flow = false;
		// 响应方队长
		boolean xtop = false;
		// 响应方队员
		boolean xlow = false;
		// 路人
		boolean zteam = false;
		map.put("type", "5");// 约战权限
		List<HashMap<String, Object>> teamMap=uTeamService.getUteamRoleByUserId(map);
		for(HashMap<String, Object> temp:teamMap){
			String teamId=(String) temp.get("teamId");
			String duel=(String) temp.get("duel");
			if(teamId.equals(fteamId)){
				if(duel.equals("1")){
					ftop=true;
				}else if(duel.equals("-1")){
					flow=true;
				}
			}else if(teamId.equals(xteamId)){
				if(duel.equals("1")){
					xtop=true;
				}else if(duel.equals("-1")){
					xlow=true;
				}
			}else{
				zteam=true;
			}
		}
		if(ftop){
			checkTemp+="-1,";
		}
		if(xtop){
			checkTemp+="-2,";
		}
		if(zteam){
			checkTemp+="-3,";
		}
		if(flow){
			checkTemp+="-4,";
		}
		if(xlow){
			checkTemp+="-5,";
		}
		if(checkTemp.length()>0){
			checkTemp=checkTemp.substring(0, checkTemp.length()-1);
		}
		return checkTemp;
	}

	private HashMap<String, Object> checkDuelStdate(
			HashMap<String, String> map, UDuel duel) throws Exception {
		List<HashMap<String, Object>> li = null;
		HashMap<String, Object> hash = null;
		String sql = "select date_format(udc.stdate,'%y年%m月%d日') as stdate,case when ubs.bs_id is null then MIN(udc.sttime) else ubs.sttime end as sttime,"
				+ "case when ubs.bs_id is null then MAX(udc.endtime) else ubs.endtime end as endtime "
				+ " from u_duel as du left join u_duel_bs as ubs on du.duel_id = ubs.duel_id "
				+ "left join u_duel_ch as udc on du.fch_id = udc.key_id ";
		if ("2".equals(duel.getDuelStatus()) && map.get("bs_id") != null && !"".equals(map.get("bs_id"))) {
			sql += "where ubs.bs_id = :bs_id ";
		} else {
			sql += "where du.duel_id = :duelId ";
		}
		li = baseDAO.findSQLMap(map, sql);
		if (null != li && li.size() > 0) {
			hash = li.get(0);
			hash.put("content", hash.get("stdate") + " " + hash.get("sttime")
					+ " - " + hash.get("endtime"));
			hash.put("name", "dateTime");
		}
		return hash;
	}

	@Override
	public HashMap<String, Object> getDuelGKInfo(HashMap<String, String> map)
			throws Exception {
		List<HashMap<String, Object>> list = null;
		hash = new HashMap<String, Object>();
		HashMap<String, Object> orderList = new HashMap<String, Object>();
		List<UDuelChallengeImg> imgList = null;
		UDuel duel = baseDAO.get(UDuel.class,map.get("duelId"));
		if (null != duel) {
			map.put("subcourt_id", duel.getUDuelCh().getUBrCourt()
					.getSubcourtId());
			if (null != duel.getUOrder()) { // 自营球场
				map.put("orderId", duel.getUOrder().getOrderId());
				orderList = uorderService.findOrderinfoDuelAndDek(map);
				if ("2".equals(duel.getDuelStatus())) {
					list = (List<HashMap<String, Object>>) orderList
							.get("list");
					list.remove(2);
					list.add(checkDuelStdate(map, duel));
					orderList.put("list", list);
				}
			} else { // 第三方球场
				list = new ArrayList<>();
				HashMap<String, Object> mapList = null;
				for (int i = 0; i < 3; i++) {
					mapList = new HashMap<String, Object>();
					mapList.put("imgurl", "");
					if (i == 0) { // 球场名
						mapList.put("content", duel.getUDuelCh().getUBrCourt()
								.getName());
						mapList.put("name", "name");
					} else if (i == 1) { // 地址
						mapList.put("content", duel.getUDuelCh().getUBrCourt()
								.getUCourt().getAddress());
						mapList.put("name", "address");
					} else if (i == 2) { // 时间
						mapList = checkDuelStdate(map, duel);
					}
					list.add(mapList);
					orderList.put("list", list);
				}
				// 支付方式
				mapList = new HashMap<String, Object>();
				mapList.put("imgurl", "");
				mapList.put(
						"content",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								duel.getUDuelCh().getDuelPayType()));
				mapList.put("name", "duelPayTypeName");
				orderList.put("duelPayTypeNameList", mapList);
				// 价格
				mapList = new HashMap<String, Object>();
				mapList.put("imgurl", "");
				mapList.put("content", duel.getUDuelCh().getPrice());
				mapList.put("name", "price");
				orderList.put("priceList", mapList);
			}
			map.put("self", "self");
			hash = ucourtService.queryCourtDetailByOrderid(map);
			// 发起方图片
			imgList = baseDAO
					.find(map,
							"from UDuelChallengeImg where objectId = :duelId and duelChallUsingType = '1' and imgSaveType = '1'");
			hash.put("remark", duel.getUDuelCh().getRemark());
		} else {
			hash.put("remark", "");
		}
		hash.put("imgList", imgList);
		hash.put("orderList", orderList);
		return hash;
	}

	/**
	 * 
	 * 
	 TODO - 内部调用通知service封装
	 * 
	 * @param map
	 * @param duelResp
	 * @throws Exception
	 *             2016年5月5日 mazkc
	 */
	private void SendDuelMes(HashMap<String, String> map, UDuelResp duelResp)
			throws Exception {
		addMoreMessageByType(map, duelResp); // 发出响应约战通知
		map.put("teamId", duelResp.getUDuel().getUTeam().getTeamId());
		map.put("mes_type", "ftAnswerduel");
		map.put("is_phone", "1");
		map.put("contentName", duelResp.getUTeam().getName());
		map.put("guest_teamId", duelResp.getUTeam().getTeamId()); // 客队ID
		map.put("duelId", duelResp.getUDuel().getDuelId());
		map.put("duelStatus", duelResp.getUDuel().getDuelStatus()); // 约战状态
		map.put("bs_id", "");// 比赛ID
		map.put("jump", "b03");// 跳转类型
		messageService.pushFightToPlayerOnTeamByType(map);
	}

	// 响应约战应用内通知
	private void addMoreMessageByType(HashMap<String, String> map,
			UDuelResp duelResp) throws Exception {
		HashMap<String, String> sendMes = new HashMap<String, String>();
		sendMes.put("guest_teamId", duelResp.getUTeam().getTeamId()); // 客队ID
		sendMes.put("teamId", duelResp.getUDuel().getUTeam().getTeamId()); // 主队ID
		sendMes.put("duelId", duelResp.getUDuel().getDuelId()); // 约战ID
		sendMes.put("duelStatus", duelResp.getUDuel().getDuelStatus()); // 约战状态
		sendMes.put("bs_id", "");// 小场次ID
		sendMes.put("jump", "b03");// 跳转类型
		map.put("params", JSON.toJSONString(sendMes));
		map.put("type", "fight");
		map.put("mes_type", "ftAnswerduel");
		map.put("contentName", duelResp.getUTeam().getName());
		map.put("teamId", duelResp.getUDuel().getUTeam().getTeamId());
		messageService.addMoreMessageByType(map); // 发出响应约战通知
	}

	@Override
	public HashMap<String, Object> respDuelPayCallBack(
			HashMap<String, String> map) throws Exception {
		UDuelResp duelResp = baseDAO.get(map,
				"from UDuelResp where UOrder.orderId = :orderId");
		if (null != duelResp) {
			duelResp.setChallengeRespStatus("1");
			duelResp.setrRespDuelStatus("1");
			duelResp.setChallengeRespStatus("1");
			baseDAO.update(duelResp);

			map.put("duelId", duelResp.getUDuel().getDuelId());
			UDuel duel = baseDAO.get(map, "from UDuel where duelId = :duelId");
			duel.setDuelStatus("4");
			baseDAO.update(duel);
			SendDuelMes(map, duelResp);
			//战达成里程碑数据添加
			this.behaviorTypeDule(map,duel,duelResp);
			this.behaviorTypeTeamDule(map,duel,duelResp);
			
		}
		return WebPublicMehod.returnRet("success", "ok");
	}

	@Override
	public HashMap<String, Object> getDuelSList(HashMap<String, String> map)
			throws Exception {
		HashMap<String, String> hashMap = null;
		selectSql = new StringBuffer();
		returnList = new ArrayList<Object>();
		selectSql
				.append(sbSelect.toString()
						+ sbJoin.toString()
						+ "where du.duel_status = '2' and du.duel_id = :duelId and du.effective_status = '1' "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						+ sql_temp
						+ sbGroup.toString()
						+ "order by du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc");
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString());
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hashMap = (HashMap<String, String>) li.get(index);
				hashMap.put(
						"duel_pay_type",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hashMap.get("duel_pay_type")));
				hashMap.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hashMap.get("duel_recommend_status")));
				returnList.add(hashMap);
			}
		}
		return WebPublicMehod.returnRet("DuelSList", li);
	}

	@Override
	public HashMap<String, Object> getDuelAgainInfo(HashMap<String, String> map)
			throws Exception {
		hash = new HashMap<String, Object>();
		// 球场信息
		selectSql = new StringBuffer(
				"select du.order_id, du.f_team_id as fteam_id, (select uci.imgurl from u_br_courtimage as uci where uci.subcourt_id = uc.subcourt_id and uci.img_size_type = '1' and uci.cimg_using_type = '2') as imgurl,uc.subcourt_id,uc.name,uc.score,ch.duel_pay_type,date_format(ch.stdate,'%y年%m月%d日') as stdate,date_format(ch.stdate,'%Y-%m-%d') as stdate_name,ch.sttime,ch.endtime,ch.price,ch.pay_proportion,ch.remark "
						+ "from u_duel as du left join u_duel_ch as ch on du.fch_id = ch.key_id "
						+ "left join u_br_court as uc on ch.court_id = uc.subcourt_id "
						+ "where du.duel_id = :duelId");
		List<UDuelChallengeImg> imgList = baseDAO
				.find(map,
						"from UDuelChallengeImg where objectId = :duelId and duelChallUsingType = '1' and imgSaveType = '1'");
		List<Object> li = baseDAO.findSQLMap(map, selectSql.toString());
		hash.put("courtList", li);
		// 指定对手信息
		selectSql = new StringBuffer(
				"select t.name,t.short_name,tmi.imgurl,t.team_id,t.team_count "
						+ "from u_duel as du left join u_duel_resp as resp on resp.duel_id = du.duel_id left join u_team as t  on t.team_id = resp.team_id left join u_team_img as tmi "
						+ "on t.team_id = tmi.team_id where du.duel_id = :duelId and ((tmi.img_size_type = '1' and tmi.timg_using_type = '1') or 1 = 1) and resp.resp_duel_status = '1' "
						+ "and resp.r_resp_duel_status = '2' and tmi.img_size_type = '1' and tmi.timg_using_type = '1' "
						+ "group by t.name,t.remark,tmi.imgurl,t.team_id,t.team_count");
		li = baseDAO.findSQLMap(map, selectSql.toString());
		hash.put("imgList", imgList);
		hash.put("teamList", li);
		return WebPublicMehod.returnRet("DuelInfo", hash);
	}

	@Override
	public HashMap<String, Object> getDuelShareInfo(HashMap<String, String> map)
			throws Exception {
		String sql = "select du.f_team_id,dusp.team_id from u_duel as du left join u_duel_resp as dusp on du.duel_id = dusp.duel_id where du.duel_id = :duelId ";
		String sql_select = "select uu.nickname,uu.realname,udp.time,p.number,"
				+ "(select upai.name from u_parameter upa left join u_parameter_info upai on upa.pkey_id = upai.pkey_id where upa.params = 'duel_bs_type' and upai.params = udp.duel_bs_type) as duel_bs_type "
				+ "from u_duel_playerinfo as udp left join u_user as uu on udp.user_id = uu.user_id "
				+ "left join u_player as p on uu.user_id = p.user_id "
				+ "left join u_duel_bs as bs on bs.bs_id = udp.bs_id "
				+ "where bs.duel_id = :duelId and udp.fteam_id = :teamId order by udp.time asc";
		hash = (HashMap<String, Object>) baseDAO.findSQLMap(map, sql).get(0);
		map.put("teamId", hash.get("f_team_id").toString());
		List<Object> array = baseDAO.findSQLMap(map, sql_select);
		hash.put("home", array); // 主队事件
		map.put("teamId", hash.get("team_id").toString());
		array = baseDAO.findSQLMap(map, sql_select);
		hash.put("away", array); // 客队事件
		return hash;
	}

	@Override
	public HashMap<String, Object> getDuelSuccessList(
			HashMap<String, String> map) throws Exception {
		returnList = new ArrayList<Object>();
		HashMap<String, List<Object>> mapList = null;
		HashMap<String, String> hashMap = null;
		selectSql = new StringBuffer();
		PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
		selectSql
				.append(sbSelect.toString()
						+ sbJoin.toString()
						+ "where du.duel_status = '2' and du.effective_status = '1' "
						//+ "and ((timg.img_size_type = '1' and timg.timg_using_type = '1') or  (timg.timg_using_type is null)) "
						+ "and du.f_user_id = :loginUserId "
						+ sbGroup.toString()
						+ "order by du.stdate desc,du.sttime desc,ubs.stdate desc,ubs.sttime desc limit "
						+ page.getOffset() + "," + page.getLimit());
		ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
				selectSql.toString(), mapList);
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { // 循环装取支付枚举参数
				hashMap = (HashMap<String, String>) li.get(index);
				hashMap.put(
						"duel_pay_type",
						Public_Cache.HASH_PARAMS("duel_pay_type").get(
								hashMap.get("duel_pay_type")));
				hashMap.put("duel_recommend_status_name",
						Public_Cache.HASH_PARAMS("duel_recommend_status")
								.get(hashMap.get("duel_recommend_status")));
				returnList.add(hashMap);
			}
		}
		return WebPublicMehod.returnRet("DuelSuccessList", returnList);
	}

	@Override
	public int checkDuelDteAdd(HashMap<String, Object> map) throws Exception {
		int count = baseDAO
				.count("select count(*) from UDuelCh as ch,UDuel as du where ch.keyId = du.UDuelCh.keyId and "
						+ "ch.UBrCourt.subcourtId = :subcourtId and ch.stdate = :stdate and ch.sttime = :sttime and du.effectiveStatus = '1'",
						map, false);
		if (count > 0) { // 存在相同时间、相同地点的约战
			return 1;
		} else { // 不存在相同时间、相同地点的约战
			count = baseDAO
					.count("select count(*) from UDuelCh as ch,UDuel as du where ch.keyId = du.UDuelCh.keyId and "
							+ "ch.stdate = :stdate and ch.sttime = :sttime and du.effectiveStatus = '1'",
							map, false);
			if (count > 0) { // 不存在相同时间约战
				return 2;
			}
		}
		return -1;
	}

	@Override
	public HashMap<String, Object> getDuelImgList(HashMap<String, String> map)
			throws Exception {
		PageLimitPhoto page = new PageLimitPhoto(Integer.parseInt(map
				.get("page")), 0);
		String type = map.get("type");
		List<UDuelChallengeImg> array = null;
		StringBuffer sb = new StringBuffer(
				"from UDuelChallengeImg where imgSaveType = '1' and objectId = :duelId ");
		if ("1".equals(type)) {
			array = baseDAO.findByPage(map,
					sb.append(" and duelChallUsingType = '1' order by weight ")
							.toString(), page.getLimit(), page.getOffset());
		} else if ("2".equals(type)) {
			array = baseDAO.findByPage(map,
					sb.append(" and duelChallUsingType = '2' order by weight ")
							.toString(), page.getLimit(), page.getOffset());
		}
		return WebPublicMehod.returnRet("success", array);
	}

	@Override
	public HashMap<String, Object> checkRespDuelStatus(
			HashMap<String, String> map) throws Exception {
		UTeam team = getTeamById(map);
		map.put("teamId", team.getTeamId());
		UDuel du = baseDAO.get(UDuel.class, map.get("duelId"));
		return WebPublicMehod
				.returnRet("success", checkRespDuel(map, du, team));
	}

	@Override
	public List<Object> getDuel(HashMap<String, String> map) throws Exception {
		UTeam team = getTeamById(map);
		String sql = "select du.remark,ci.imgurl from u_duel_resp as du left join u_duel_challenge_img as ci on du.duel_id = ci.object_id "
				+ "where ci.duel_chall_using_type = :userType and ci.img_save_type = '1' and ci.object_id = :duelId order by ci.weight ";
		map.put("teamId", team.getTeamId());
		// -1已过期 -2已响应 -3只有队长才可响应 -4非指定队伍响应 -5队伍人数需要大于7人 0-可响应
		UDuelResp resp = baseDAO.get(map,
				"from UDuelResp where UDuel.duelId = :duelId");
		String userType = String.valueOf(checkRespDuel(map, null == resp ? null
				: resp.getUDuel(), team));
		if ("-1".equals(userType)) {
			map.put("userType", "2");
		} else if ("-2".equals(userType)) {
			map.put("userType", "1");
			map.put("duelId", resp.getKeyId());
		} else {
			return null;
		}
		// System.out.println(JSON.toJSONString(baseDAO.findSQLMap(map, sql)));
		return baseDAO.findSQLMap(map, sql);
	}

	@Override
	public HashMap<String, Object> addRemarkImgDuel(
			HashMap<String, String> map, List<UDuelChallengeImg> duelImgList)
			throws Exception {
		hash = new HashMap<String, Object>();
		map.put("duel_chall_using_type", "2");
		UDuelResp resp = baseDAO.get(map,
				"from UDuelResp as du where du.UOrder.ordernum = :ordernum ");
		resp.setRemark(map.get("remark"));
		baseDAO.update(resp);
		addDuelImg(map, resp.getUDuel(), duelImgList);
		hash.put("duelId", resp.getUDuel().getDuelId());
		hash.put("home_teamid", resp.getUDuel().getUTeam().getTeamId());
		hash.put("away_teamid", resp.getUTeam().getTeamId());
		hash.put("teamname", resp.getUTeam().getName());
		return WebPublicMehod.returnRet("success", hash);
	}

	@Override
	public HashMap<String, Object> getDuelRespInfo(HashMap<String, String> map)
			throws Exception {
		hash = new HashMap<String, Object>();
		// 球场信息
		selectSql = new StringBuffer(
				"select du.order_id,(select uci.imgurl from u_br_courtimage as uci where uci.subcourt_id = uc.subcourt_id and uci.img_size_type = '1' and uci.cimg_using_type = '2') as imgurl,uc.subcourt_id,uc.name,uc.score,ch.duel_pay_type,date_format(ch.stdate,'%y年%m月%d日') as stdate,ch.sttime,ch.endtime,ch.price,ch.pay_proportion,ch.remark "
						+ "from u_duel as du left join u_duel_ch as ch on du.fch_id = ch.key_id "
						+ "left join u_br_court as uc on ch.court_id = uc.subcourt_id "
						+ "where  du.duel_id = :duelId");
		List<Object> li = baseDAO.findSQLMap(map, selectSql.toString());
		hash.put("courtList", li);
		return WebPublicMehod.returnRet("DuelInfo", hash);
	}

	@Override
	public List<HashMap<String, Object>> getDuelInfoBySession(
			List<HashMap> sessionList) throws Exception {
		HashMap<String, Object> map = null;
		String sql = "select t.name as home_teamname,t.short_name as home_short_teamname,t.team_id as f_team_id,"
				+ "(select timg1.imgurl from u_team_img as timg1 where timg1.team_id = u.f_team_id	 and timg1.timg_using_type='1' and timg1.img_size_type='1' ) as home_teamimgurl,"
				+ "(select t1.name from u_team as t1 where t1.team_id = dusp.team_id and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_teamname,"
				+ "(select t1.short_name from u_team as t1 where t1.team_id =  dusp.team_id and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_short_teamname,"
				+ "(select timg1.imgurl from u_team_img as timg1 where timg1.team_id =  dusp.team_id and timg1.timg_using_type='1' and timg1.img_size_type='1' and dusp.resp_duel_status = '1' and dusp.r_resp_duel_status = '1' ) as away_teamimgurl,"
				+ "(select uimg.imgurl from u_user as uu left join u_user_img as uimg on uu.user_id = uimg.user_id where uimg.uimg_using_type = '1' and uimg.img_size_type = '1' and uu.user_id = us.user_id ) as imgurl,"
				+ "dusp.team_id as x_team_id,u.duel_status as status,u.duel_id as champid,"
				+ "case when u.duel_id is not null then (uo.allprice * 0.01 * (100 - CAST(uo.payment_ratio as SIGNED))) else uo.allprice end as responseprice,us.realname username,us.nickname,us.user_id "
				+ "from u_br_courtsession as sess left join u_order as uo on sess.order_id = uo.order_id "
				+ "left join u_duel as u on u.order_id = uo.order_id "
				+ "left join u_duel_resp as dusp on u.duel_id = dusp.duel_id "
				+ "left join u_team as t on u.f_team_id = t.team_id "
				+ "left join u_user as us on uo.user_id = us.user_id "
				+ "where "
				+ "sess.session_id = ";
		selectSql = new StringBuffer("");
		if (null != sessionList && sessionList.size() > 0) {
			for (int i = 0; i < sessionList.size(); i++) {
				map = sessionList.get(i);
				if (i == (sessionList.size() - 1)) {
					selectSql.append(sql + "'" + map.get("session_id") + "'");
				} else {
					selectSql.append(sql + "'" + map.get("session_id") + "'"
							+ " union all ");
				}
			}
		}
		return baseDAO.findSQLMap(selectSql.toString());
	}

	@Override
	public void updateDuelByTimeOut() throws Exception {
		List<HashMap<String, Object>> listDuel=baseDAO.findSQLMap("select u.duel_id,u.duel_status from u_duel_ch as ch left join u_duel as u on ch.key_id = u.fch_id where u.duel_status = '1' and DATE_FORMAT(CONCAT(ch.stdate,' ',ch.sttime),'%Y-%c-%d %H:%i:%s') < now()");
		// 创建临时表
		String sql1 = "CREATE TEMPORARY TABLE tmp as select u.duel_id from u_duel_ch as ch left join u_duel as u on ch.key_id = u.fch_id where u.duel_status = '1' and DATE_FORMAT(CONCAT(ch.stdate,' ',ch.sttime),'%Y-%c-%d %H:%i:%s') < now()";
		// 更新状态
		String sql2 = "update u_duel set duel_status = '3',effective_status = '2' where duel_id in (select duel_id from tmp) and duel_status = '1'";
		// 删除临时表
		String sql3 = "drop table tmp";
		baseDAO.executeSql(sql1);
		baseDAO.executeSql(sql2);
		baseDAO.executeSql(sql3);
		//修改LBS百度上传的约战状态
		baseDAO.getSessionFactory().getCurrentSession().flush();
//		List<HashMap<String, Object>> listDuel=baseDAO.findSQLMap("select u.duel_id,u.duel_status from u_duel_ch as ch left join u_duel as u on ch.key_id = u.fch_id where u.duel_status = '3' and DATE_FORMAT(CONCAT(ch.stdate,' ',ch.sttime),'%Y-%c-%d %H:%i:%s') < now()");
//		UDuel duel=null;
		HashMap<String, String> map=new HashMap<>();
		for (HashMap<String, Object> hashMap : listDuel) {
			baseDAO.getSessionFactory().getCurrentSession().flush();
//			duel=new UDuel();
//			duel.setDuelId((String)hashMap.get("duel_id"));
			map.put("duelId", (String)hashMap.get("duel_id"));
			this.updateDuelGeoData(map,"3");//修改LBS百度上传的约战状态
		}
//		//修改LBS百度上传的约战状态
//		baseDAO.getSessionFactory().getCurrentSession().flush();
//		UDuel duel=baseDAO.get(UDuel.class,map.get("duelId"));
//		this.insertDuelGeoData(map,duel,"2");//修改LBS百度上传的约战状态
	}

	@Override
	public int complementDuelPay(HashMap<String, String> map) throws Exception {
		UDuel duel = baseDAO.get(UDuel.class, map.get("duelId"));
		if ("1".equals(duel.getDuelStatus())) {
			return 1;
		} else {
			return 2;
		}
	}

	// 判断约战是否可以发起
	// -1只有队长才可发起 -2队伍人数需要大于7人 0-可发起
	private int checkaddDuel(HashMap<String, String> map, UTeam team)
			throws Exception {
		int returnType = 0; // 默认可响应
		if (null == team || null == team.getTeamId()
				|| "".equals(team.getTeamId())) {
			returnType = -1;
		} else {
			if (team.getTeamCount() < Public_Cache.TEAM_COUNT) {
				returnType = -2;
			}
		}
		return returnType;
	}

	@Override
	public HashMap<String, Object> checkAddDuelStatus(
HashMap<String, String> map) throws Exception {
		if (map.get("teamId") != null && !"".equals(map.get("teamId"))) {
			map.put("type", "5"); // 类型为约战
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);
			return WebPublicMehod.returnRet("success", checkaddDuel(map,uTeam));
		} else {
			return WebPublicMehod.returnRet("error", "您的APP版本过低，请升级版本");
		}
//		return WebPublicMehod.returnRet("success", -1);
	}

	@Override
	public HashMap<String, Object> checkDuelBK(HashMap<String, String> map)
			throws Exception {
		List<UOrder> li = uorderService.judgeReplenishmentOrder(map);
		int check = complementDuelPay(map);
		if(null == li || li.size() == 0 && check == 1){
			hash = uorderService.judgeOrderResp(map,"不能补款！");
			if(null == hash){
				return WebPublicMehod.returnRet("success", "1");
			}
		}
		return WebPublicMehod.returnRet("success", "-1");
	}
	
	/**
	 * 搜索获得约战列表sql（带排序）
	 * @param map
	 * .search 搜索条件
	 * .filter 是否有筛选条件
	 * 		.courtType 球场类型
	 * 		.payType 支付类型
	 * 		.status 约战状态
	 * 		.price 价格范围
	 * 		.service 服务
	 * 		.area 城市，区域
	 * 	.orderBy 排序
	 * @return
	 * @throws Exception
	 */
	private StringBuffer getDuelSqlByQuery(HashMap<String, String> map, HashMap<String, List<Object>> mapList, PageLimit page, StringBuffer selectSql) throws Exception {
		String str="";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and du.duel_status <> '3' and du.duel_status <> '5' and du.effective_status = '1' ");
//		StringBuffer orderbySql = new StringBuffer(" order by du.duel_recommend_status = '1' ");
		StringBuffer orderbySql = new StringBuffer();
		selectSql.append(sbSelect.toString() + sbJoin.toString());

		// 搜索：球场全称，球队全称，球队简称  
		if (map.get("search") != null) {
			selectSql.append(" left join u_team as t1 on t1.team_id = dusp.team_id "); // 响应方一定有发起方
			whereSql.append(" and ( locate( :search, t.name) > 0");
			whereSql.append(" or  locate( :search, t.short_name) > 0");
			whereSql.append(" or locate( :search, uc.name) > 0");
			whereSql.append(" or locate( :search, t1.name) > 0");
			whereSql.append(" or locate( :search, t1.short_name) > 0 ) ");
		}

		// 是否筛选
		if (map.get("filter") != null) {
			// 按球场类型
			if (map.get("courtType") != null && !"-1".equals(map.get("courtType"))) {
				
				if (map.get("courtType").equals("1")) {
					whereSql.append(" and uc.court_br_type = :courtType ");
					str+="court_type:1";
				} else {
					whereSql.append(" and uc.court_br_type != '1' ");	//第三方
					str+="court_type:2,3,4";
				}
			}else{
				str+="court_type:1,2,3,4";
			}
			// 按支付类型
			if (map.get("payType") != null && !"-1".equals(map.get("payType"))) {
				whereSql.append(" and udc.duel_pay_type = :payType ");
				str+="|pay_type:"+map.get("payType")+"";
			}else{
				str+="|pay_type:1,2,3";
			}
			// 按约战状态
			if (map.get("status") != null && !"-1".equals(map.get("status"))) {
				whereSql.append(" and du.duel_status = :status ");
				str+="|duel_status:"+map.get("status")+"";
			}
			// 按价格范围
			if (map.get("price") != null && !"-1".equals(map.get("price"))) {
				String price = map.get("price");
				if (price.equals("0")) {
					whereSql.append(" and uo.allprice = 0 ");
					str+="|price:0";
				} else if (price.equals("1")) {
					whereSql.append(" and uo.allprice <= 300 ");
					str+="|price:0,100";
				} else if (price.equals("2")) {
					whereSql.append(" and uo.allprice between 301 and 600 ");
					str+="|price:301,600";
				} else if (price.equals("3")) {
					whereSql.append(" and uo.allprice between 601 and 900 ");
					str+="|price:601,900";
				} else if (price.equals("4")) {
					whereSql.append(" and uo.allprice between 901 and 1200 ");
					str+="|price:901,1200";
				} else if (price.equals("5")) {
					whereSql.append(" and uo.allprice > 1200 ");
					str+="|price:1200,10000";
				}
			}
			else{
				str+="|price:0,10000";
			}
			// 按服务
			if (map.get("service") != null && !"-1".equals(map.get("service"))) {
				selectSql.append(" left join u_br_courtstaticservice as ucs on uc.subcourt_id = ucs.subcourt_id ");
				String service = map.get("service");
				if (service.equals("1")) {
					whereSql.append(" and ucs.orderbyno = '1' "); // 停车场
				} else if (service.equals("2")) {
					whereSql.append(" and ucs.orderbyno = '2' "); // 裁判
				} else if (service.equals("3")) {
					whereSql.append(" and ucs.orderbyno = '3'"); // 储物柜
				} else if (service.equals("4")) {
					whereSql.append(" and ucs.orderbyno = '4' ");
				} else if (service.equals("5")) {
					whereSql.append(" and ucs.orderbyno = '5' ");
				}
			}
			//按城市
			if (null != map.get("area") && !"-1".equals(map.get("area")) && !"".equals(map.get("area"))) {
				selectSql.append(" left join u_court ut on ut.court_id = uc.court_id ");
				String whereSql_regn = getDuelListByRegn(map, mapList);
				whereSql.append(whereSql_regn);
			}
		}
		
		//是否排序
		if (map.get("orderBy") != null) {
			if (map.get("orderBy").equals("1")) {
				orderbySql.append(" order by udc.stdate desc, udc.sttime desc "); // 距离开始时间最近
			} else if (map.get("orderBy").equals("3")) { // 价格最低
				if (map.get("status") != null && "-1".equals(map.get("status"))) {
					whereSql.append(" and du.duel_status = '1' ");
				}
				orderbySql.append(" order by udc.price asc ");
				orderbySql.append(",udc.pay_proportion desc ");
			} else if (map.get("orderBy").equals("4")) { // 获取激数最高
				orderbySql.append(" order by udc.wincount desc ");
			} else if (map.get("orderBy").equals("2")) { // 离我最近
				boolean baidusb = getDuelListByloc(map, page, whereSql, orderbySql, mapList,str);
				if (orderbySql == null || orderbySql.length() == 0) {
					orderbySql.append( " order by du.stdate desc,du.sttime desc "
					+ ",uc.court_br_type = '1', uc.court_br_type= '4', uc.court_br_type= '3' ,du.duel_id ");
				} else {
					orderbySql.append( " ,du.stdate desc,du.sttime desc "
					+ ",uc.court_br_type = '1', uc.court_br_type= '4', uc.court_br_type= '3' ,du.duel_id ");
				}
				selectSql.append(whereSql.toString() + sbGroup.toString() + orderbySql.toString());
				if (map.get("status") != null && "2".equals(map.get("status"))) {
					selectSql.append(" limit "+page.getLimit() + " offset " + page.getOffset());
				}
				return selectSql;
			}
		} else {
			orderbySql.append( " order by du.stdate desc,du.sttime desc "
			+ ",uc.court_br_type = '1', uc.court_br_type= '4', uc.court_br_type= '3' ,du.duel_id "); // 距离约战时间最近
		}
		selectSql.append(whereSql.toString() + sbGroup.toString() +orderbySql.toString() 
								+ " limit "+page.getLimit() + " offset " + page.getOffset());
		return selectSql;
	}
	
	/**
	 * 以城市或区域为单位搜索约战球场
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getDuelListByRegn(HashMap<String, String> map, HashMap<String, List<Object>> mapList)
			throws Exception {
		StringBuffer sbf = new StringBuffer();
		List<Object> listStr = new ArrayList<>();
		
		URegion region = baseDAO.get(URegion.class, map.get("area"));
		if (region != null && "2".equals(region.getType())) {
			sbf.append(" and ut.area in (select _id from u_region where parent =:area ) ");
			map.put("area", map.get("area"));
		} else {
			String area = map.get("area");
			String[] strs = area.split(",");
			
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			map.remove("area");
			mapList.put("area", listStr);
			sbf.append(" and ut.area in (:area ) ");
		}
		return sbf.toString();
	}
	
	/**
	 * 根据版本号判断
	 * 是否需要按照teamId获取队伍
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private UTeam getTeamById (HashMap<String, String> map) throws Exception{
		if (map.get("appCode") != null && !"".equals(map.get("appCode"))) {
			return uTeamService.findPlayerInfoById(map);
		} else {
			throw new Exception("您的APP版本过低，请升级版本");
		}
	}
	/**
	 * 
	 * TODO 约战达成里程碑数据添加-球员
	 * @param map
	 * @param duel
	 * @param duelResp
	 * xiaoying 2016年8月26日
	 */
	private void behaviorTypeDule(HashMap<String, String> map, UDuel duel, UDuelResp duelResp) throws Exception{
		map.put("type", "1");
		map.put("behaviorType","8");
		map.put("objectId", map.get("duelId"));
		map.put("teamId", duel.getUTeam()!=null?duel.getUTeam().getTeamId():"");
		publicService.updateBehavior(map);
		map.put("teamId", duelResp.getUTeam()!=null?duelResp.getUTeam().getTeamId():"");
		publicService.updateBehavior(map);
		
	}
	/**
	 * 
	 * TODO 约战达成里程碑数据添加-球队
	 * @param map
	 * @param duel
	 * @param duelResp
	 * xiaoying 2016年8月26日
	 */
	private void behaviorTypeTeamDule(HashMap<String, String> map, UDuel duel, UDuelResp duelResp) throws Exception{
		map.put("type", "2");
		map.put("behaviorType","14");
		map.put("objectId", map.get("duelId"));
		map.put("teamId", duel.getUTeam()!=null?duel.getUTeam().getTeamId():"");
		publicService.updateBehavior(map);
		map.put("teamId", duelResp.getUTeam()!=null?duelResp.getUTeam().getTeamId():"");
		publicService.updateBehavior(map);
		
	}
}
