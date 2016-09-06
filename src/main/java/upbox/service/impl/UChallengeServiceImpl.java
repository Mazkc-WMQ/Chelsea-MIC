package upbox.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.org.pub.PublicMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.BdLbsBean;
import upbox.model.PageLimit;
import upbox.model.PageLimitPhoto;
import upbox.model.UBaidulbs;
import upbox.model.UBrCourt;
import upbox.model.UChallenge;
import upbox.model.UChallengeBs;
import upbox.model.UChallengeCh;
import upbox.model.UChallengeResp;
import upbox.model.UChampion;
import upbox.model.UCourt;
import upbox.model.UDuelChallengeImg;
import upbox.model.UFollow;
import upbox.model.UMall;
import upbox.model.UOrder;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UTeamActivity;
import upbox.model.UTeamBehavior;
import upbox.model.UTeamDek;
import upbox.model.UTeamImg;
import upbox.model.UUser;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.RankingListService;
import upbox.service.UChallengeService;
import upbox.service.UCourtService;
import upbox.service.UOrderService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 挑战接口实现
 * @author yc
 *
 * 13611929818
 */
@Service("challengeService")
public class UChallengeServiceImpl implements UChallengeService{

	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UTeamService uTeamService;
	@Resource
	private UCourtService uCourtService;
	@Resource
	private UOrderService uOrderService;
	@Resource
	private UUserService uuserService;
	@Resource
	private UCourtService ucourtService;
	@Resource
	private PublicService publicServiceImpl;
	@Resource
	private PublicPushService publicPushService;
	@Resource
	private MessageService messageService;
	@Resource
	private UPlayerService uplayerService;
	@Resource
	private LBSService lBSService;
	@Resource
	private PublicService publicService;
	
	@Resource
	private RankingListService rankingListService;
	
	
	
	private HashMap<String, Object> hashMap = new HashMap<String, Object>();
	
	private HashMap<String, Object> hash;
	
	
	private StringBuffer sbSelect=new StringBuffer(
			"select "+
			" b.bs_id, "+									//小场次id(用来区分大场次对象和小场次对象)
			" u.challenge_id, "+							//大场次id
			" date_format(ch.stdate,'%y年%m月%d日') as stdate, "+								//大场次开始日期
			" ch.sttime, "+								//大场次开始时间
			" date_format(ch.enddate,'%y年%m月%d日') as enddate, "+								//大场次结束日期
			" ch.endtime, "+								//大场次结束时间
			" date_format(b.stdate,'%y年%m月%d日') as bsstdate, "+						//小场次开始日期
			" b.sttime as bssttime, "+						//小场次开始时间
			" date_format(b.endDate,'%y年%m月%d日') as bsenddate, "+					//小场次结束日期
			" b.endtime as bsendtime, "+					//小场次结束时间
			" date_format(u.stdate,'%y年%m月%d日') as fstdate, "+						//擂主创建日期
			" date_format(u.sttime,'%H:%i:%s') as fsttime, "+						//擂主创建时间
			" uc.name as courtname, "+						//球场名称
			" ch_recommend_status, "+						//是否推荐
			"(select name from u_team ut where ut.team_id=u.f_team_id ) as home_teamname, "+//主队名
			"(select short_name from u_team ut where ut.team_id=u.f_team_id ) as home_short_teamname, "+//主队简称
			"(select imgurl from u_team_img ti where ti.team_id=u.f_team_id and ti.timg_using_type='1' and ti.img_size_type='1') as home_teamimgurl, "+//主队图片
			"(select name from u_team ut where ut.team_id=r.team_id and r.challenge_resp_status='1' ) as away_teamname, "+//客队名
			"(select short_name from u_team ut where ut.team_id=r.team_id and r.challenge_resp_status='1') as away_short_teamname, "+//客队名
			"(select imgurl from u_team_img ti where ti.team_id=r.team_id and ti.timg_using_type='1' and ti.img_size_type='1'  and r.challenge_resp_status='1') as away_teamimgurl, "+//客队图片
			" ch.wincount, "+								//战胜获得积分
			" u.challenge_status, "+						//当前挑战状态
			" (select upai.name from u_parameter upa left join u_parameter_info upai on upa.pkey_id = upai.pkey_id where upa.params = 'challenge_status' and upai.params = u.challenge_status) challenge_status_name,"+
			" (select upai.name from u_parameter upa left join u_parameter_info upai on upa.pkey_id = upai.pkey_id where upa.params = 'challenge_pay_type' and upai.params = ch.challenge_pay_type) challenge_pay_type_name,"+
			" ch.challenge_pay_type,"+
			" u.f_team_id,"+
			" u.x_team_id,"+
			" b.f_fj, "+									//主队获得激数
			" b.k_fj, "+									//主队获得激数
			" uo.order_id,"+
			" uo.allprice,"+
			" uo.payment_ratio,"+							//支付比例
			" uo.price,"+
			" b.f_goal, "+									//主队进球
			" b.k_goal ");									//客队进球

	private StringBuffer sbJoin=new StringBuffer(
			" from u_challenge u left join u_team as t on u.f_team_id = t.team_id "+
			" left join u_challenge_bs b on u.challenge_id= b.challenge_id "+
			" left join u_challenge_ch ch on u.fch_id= ch.key_id "+
			" left join u_challenge_resp r on u.xch_id= r.key_id "+
			" left join u_order uo on uo.order_id = u.order_id "+
			" left join u_br_courtsession sess on u.order_id = sess.order_id  "+
			" left join u_br_court uc on ch.br_court_id = uc.subcourt_id  ");
	
	@Override
	public List<UChallengeCh> getChList() throws Exception {
		return	baseDAO.find("from UChallengeCh where isChampion='1' order by stdate desc,sttime desc");
	}

	@Override
	public List<UChallengeBs> getChallengeBsList() throws Exception {
		return baseDAO.find("from UChallengeBs");
	}

	@Override
	public UChallengeBs getChallengeBs(String bsId) throws Exception {
		return baseDAO.get(UChallengeBs.class,bsId);
	}

	@Override
	public boolean getTeamChStatus(String teamId,String brCourtId) throws Exception {
		hashMap.put("teamId", teamId);
		hashMap.put("brCourtId", brCourtId);
		UChampion cp= (UChampion) baseDAO.get("from UChampion where UTeam.teamId=:teamId and isChampion='1' and  UBrCourt.subcourtId=:brCourtId",hashMap);
		if(cp!=null){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public boolean getTeamChStatusWithCourt(String teamId,String brCourtId) throws Exception {
		hashMap.put("teamId", teamId);
		hashMap.put("brCourtId", brCourtId);
		UChampion cp= (UChampion) baseDAO.get("from UChampion where UTeam.teamId=:teamId and isChampion='1' and brCourtId=:brCourtId",hashMap);
		if(cp!=null){
			return true;
		}else{
			return false;
		}
	}

	//预留
	@Override
	public List<UChallengeCh> getSysSendCh() throws Exception {

		return null;
	}

	@Override
	public UChampion getNowCh(String brCourtId) throws Exception {
		hashMap.put("brCourtId", brCourtId);
		UChampion cp= (UChampion) baseDAO.get("from UChampion where UBrCourt.subcourtId=:brCourtId and isChampion='1' ",hashMap);
		return cp;
	}

	@Override
	public List<UChampion> getOldChList(String brCourtId) throws Exception {
		hashMap.put("brCourtId", brCourtId);
		List<UChampion> cpList= baseDAO.find("from UChampion where UBrCourt.subcourtId=:brCourtId and isChampion='0' order by createDate ",hashMap);
		return cpList;
	}

	@Override
	public UChampion getCp(String id) throws Exception {
		return baseDAO.get(UChampion.class, id);
	}

	@Override
	public Date getFirstCh(String teamId,String userFollowType) throws Exception {
		hashMap.put("teamId", teamId);
		hashMap.put("userFollowType", userFollowType);
		List<UTeamBehavior> be= baseDAO.find("from UTeamBehavior where UTeam.teamId=:teamId and userFollowType=:userFollowType ",hashMap);
		if(be!=null&&be.size()>0){
			return be.get(0).getCreateDate();
		}
		return null;
	}

	

	@Override
	public HashMap<String, Object> isFollowCh(HashMap<String, String> map) throws Exception {
		String challengeId = map.get("challengeId");
		String type = map.get("type");
		String msg="";
		UFollow uf1=null;
		if(challengeId!=null&&!"".equals(challengeId)&&type!=null&&!"".equals(type)){
			UUser uuser=uuserService.getUserinfoByToken(map);
			hashMap.clear();
			hashMap.put("objectId", challengeId);
			hashMap.put("type", type);
			hashMap.put("userId", uuser.getUserId());
			List<UFollow> uf= baseDAO.find("from UFollow where objectId=:objectId and userFollowType='6' and UUser.userId=:userId ",hashMap);
			if(uf!=null&&uf.size()>0){
				uf1=uf.get(0);
				uf1.setFollowStatus(type);
				baseDAO.update(uf1);
			}else{
				uf1=new UFollow();
				uf1.setKeyId(WebPublicMehod.getUUID());
				uf1.setCreatedate(new Date());
				uf1.setFollowStatus(type);
				uf1.setObjectId(challengeId);
				uf1.setUserFollowType("6");
				uf1.setUUser(uuser);
				baseDAO.save(uf1);
			}
			if(type.equals("1")){
				msg="关注成功！";
			}else{
				msg="取消关注成功！";
			}
			return WebPublicMehod.returnRet("success",msg);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getChListAndBsList(HashMap<String, String> map) throws Exception {
		if ( map.get("page") != null && !"".equals(map.get("page"))) {
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			String sql_temp = "";
			String inSql="";
			String selOutSql="";//拼接的下拉选排序
			String whereSql="";//where条件sql
			boolean isGetData=false;//是否直接得到结果 true需要百度lbs过滤排序 false不需要过滤排序
			ArrayList<Object> returnList = new ArrayList<Object>();
			HashMap<String, String> hashTemp=null;
			HashMap<String, List<Object>> mapList = new HashMap<String, List<Object>>();
			HashMap<String,String> strMap=null;//拼接各条件后的sql拼接语句
			StringBuffer newSql=new StringBuffer();
			StringBuffer newLbsSql=new StringBuffer();//lbs 排序sql
			hash = new HashMap<String, Object>();
			
			//拼接筛选结果
			strMap=appendSql(map, sql_temp, selOutSql, whereSql, mapList);
			sql_temp=strMap.get("sql_temp");
			selOutSql=strMap.get("selOutSql");
			whereSql=strMap.get("whereSql");
			if(map.get("orderByChallenge")!=null&&map.get("orderByChallenge").equals("2")){//需要lbs排序数据
				isGetData=true;
			}
			if(null != map.get("serachType") && "2".equals(map.get("serachType"))){//筛选配合百度lbs
				List<HashMap<String,Object>> arrayTeamList = baseDAO.findSQLMap(map,"select team_id from u_player where user_id = :loginUserId and team_id is not null and in_team='1' ");
				
				if(isGetData){//需要lbs处理排序
					inSql=lbsScreenInSql(page, map);//拼接lbs sql
					if(inSql.length()>0){
						newLbsSql.append(sbSelect +" "+ sbJoin + " where  "+inSql);
						ArrayList<Object> newli = (ArrayList<Object>) baseDAO.findSQLMap(map,newLbsSql.toString(),mapList);
						if (null != newli && newli.size() > 0) {
							for (int index = 0; index < newli.size(); index++) { 
								hashTemp = (HashMap<String, String>) newli.get(index);
								hashTemp.put("is_my", checkTeamIsMy(hashTemp,arrayTeamList));
								returnList.add(hashTemp);
							}
						}
					}
				}else{//不需要lbs处理排序
					String limitStr=limitStr=" limit " + page.getOffset() + ","+ page.getLimit();
					
					newSql.append(sbSelect +",u.challenge_id_int "+ sbJoin + "left join u_court ut on ut.court_id=uc.court_id where u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' "
							+whereSql
							+sql_temp
							+selOutSql
							+limitStr);
					
					map.remove("challengeStatus");//map mapList防止有同一字段造成冲突
					
					ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,newSql.toString(),mapList);
					if (null != li && li.size() > 0) {
						for (int index = 0; index < li.size(); index++) { 
							hashTemp = (HashMap<String, String>) li.get(index);
							hashTemp.put("is_my", checkTeamIsMy(hashTemp,arrayTeamList));
							returnList.add(hashTemp);
						}
					}
				}
			}else /*if(null != map.get("serachType") && "1".equals(map.get("serachType")))*/ {//兼容2.0版本使用  单纯搜索
				List<HashMap<String,Object>> arrayTeamList = baseDAO.findSQLMap(map,"select team_id from u_player where user_id = :loginUserId and team_id is not null and in_team='1' ");
				
				newSql.append(sbSelect +" "+ sbJoin + " where u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' "+sql_temp
						+ " order by challenge_status,ch_recommend_status desc, ch.stdate desc,ch.sttime desc,b.stdate desc,b.sttime desc,uo.price asc,ch.wincount desc  limit " + page.getOffset() + ","
						+ page.getLimit());
			
			
				ArrayList<Object> newli = (ArrayList<Object>) baseDAO.findSQLMap(map,newSql.toString());
				if (null != newli && newli.size() > 0) {
					for (int index = 0; index < newli.size(); index++) { 
						hashTemp = (HashMap<String, String>) newli.get(index);
						hashTemp.put("is_my", checkTeamIsMy(hashTemp,arrayTeamList));
						returnList.add(hashTemp);
					}
				}
				
			}
			
			
			hash.clear();
			hash.put("ChListAndBsList", returnList);
			return hash;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}
	
	
	/**
	 * 拼接筛选结果
	 * @param map 接口接受参数
	 * @param sql_temp 筛选的where条件
	 * @param selOutSql 排序条件
	 * @param whereSql where条件
	 * @param mapList 查询需要的状态数组
	 * @param isGetData 是否直接得到结果 true需要百度lbs过滤排序 false不需要过滤排序
	 * @throws Exception
	 */
	private HashMap<String,String> appendSql(HashMap<String, String> map,String sql_temp,String selOutSql,String whereSql,HashMap<String, List<Object>> mapList) throws Exception{
		HashMap<String,String> strMap=new HashMap<String,String>();
		if (null != map.get("serachType") && "1".equals(map.get("serachType"))) {
			sql_temp = checkChallengeSerach(map,sql_temp);
		}else if(null != map.get("serachType") && "2".equals(map.get("serachType"))){
			
			sql_temp = checkChallengeScreen(map, sql_temp,mapList);//筛选地区
		
			//拼接的下拉选排序
			if(map.get("orderByChallenge")!=null&&map.get("orderByChallenge").equals("1")){
				selOutSql=" order by ch.stdate desc,ch.sttime desc,b.stdate desc,b.sttime desc ";
			}else if(map.get("orderByChallenge")!=null&&map.get("orderByChallenge").equals("2")){
				
			}else if(map.get("orderByChallenge")!=null&&map.get("orderByChallenge").equals("3")){
				whereSql=" and u.order_id is not null ";
				if(map.get("challengeStatus")!=null&&map.get("challengeStatus").equals("-1")){
					whereSql+=" and u.challenge_status='1' ";
				}
				selOutSql=" order by uo.price asc ";
			}else if(map.get("orderByChallenge")!=null&&map.get("orderByChallenge").equals("4")){
				selOutSql=" order by ch.wincount desc ";
			}else{//else里为排序的默认排序条件 分为三种情况 1.取得到定位地址  2.取得到定位地址但没有搜索到球场 3.取不到定位地址
				selOutSql=" order by challenge_status,ch_recommend_status desc, ch.stdate desc,ch.sttime desc,b.stdate desc,b.sttime desc,uo.price asc,ch.wincount desc ";
			}
		}else{//兼容2.0版本使用
			sql_temp = checkChallengeSerach(map,sql_temp);
		}
		strMap.put("sql_temp", sql_temp);
		strMap.put("selOutSql", selOutSql);
		strMap.put("whereSql", whereSql);
		return strMap;
	}

	/**
	 * 处理lbs 拼接筛选 insql
	 * @param page 前端分页对象
	 * @param li 所有数据抛给百度进行排序
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String lbsScreenInSql(PageLimit page,HashMap<String, String> map) throws Exception{
		String inSql="";
		String filter=getLbsArea(map);
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "5000000", "challenge", "distance:1", null,"5",filter);
		for(BdLbsBean lbstemp:lbsList){
			inSql+="'"+lbstemp.getChallegeid()+"',";
		}
		
		//如果没有球场排序就使用默认排序
		if(inSql.length()>0){
			inSql=inSql.substring(0, inSql.length()-1);
			inSql=" u.challenge_id in ("+inSql+") order by FIELD(u.challenge_id,"+inSql+")";
		}
		return inSql;
	}
	
	/**
	 * 处理lbs 拼接下拉搜索insql
	 * @param page 前端分页对象
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String lbsDropInsql(PageLimit page,HashMap<String, String> map) throws Exception{
		String inSql="";
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "500000", "challege", "distance:1",null,"5",null);
		for(BdLbsBean lbstemp:lbsList){
			inSql+="'"+lbstemp.getObject_id()+"',";
		}
	
		//如果没有球场排序就使用默认排序
		if(inSql.length()>0){
			inSql=inSql.substring(0, inSql.length()-1);
			inSql=" and u.challenge_id in ("+inSql+") ";
		}
		return inSql;
	}
	/**
	 * 下拉地区搜索项
	 * @param location 经纬度
	 * @return
	 * @throws Exception
	 */
	private String lbsOrderSql (String location) throws Exception{
		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
		String orderSql="";
		String lbsBack="";
		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
		lbsMap.put("location", location);//百度经纬度
		lbsMap.put("coord_type", "3");//百度坐标系
		lbsMap.put("radius", "10000000000");//半径范围
		lbsMap.put("tags", "court");//搜索球场
		lbsMap.put("sortby", "distance:1");//距离升序
		lbsMap.put("url", Public_Cache.LBS_LOCATION);
		lbsMap.put("page_index","0");
		lbsMap.put("page_size","50");
		
		
		lbsBack=lBSService.getNearBy(lbsMap);
		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);
		for(BdLbsBean lbstemp:lbsList){
			orderSql+="'"+lbstemp.getObject_id()+"',";
		}
		
		//如果没有球场排序就使用默认排序
		if(orderSql.length()>0){
			orderSql=orderSql.substring(0, orderSql.length()-1);
			orderSql=" field(uc.court_id,"+orderSql+") ";
		}
		return orderSql;
	}
	
	/**
	 * 挑战列表搜索条件
	 * @param map
	 * @param sql_temp
	 * @return
	 */
	private String checkChallengeSerach(HashMap<String, String> map,String sql_temp) {
		StringBuffer sbf = new StringBuffer();
		if (null != map.get("serach")) { // 球场筛选不为空
			sbf.append(" and (t.name like :serch or t.short_name like :serch or (select t1.name from u_team as t1 where t1.team_id =  r.team_id) like :serch "
					+ "or (select t1.short_name from u_team as t1 where t1.team_id =  r.team_id) like :serch or uc.name like :serch) ");
			map.put("serch", "%" + map.get("serach") + "%");
		}
		sql_temp = sbf.toString();
		return sql_temp;
	}
	
	
	/**
	 * 
	 * 挑战约战列表筛选条件
	 * @param map
	 *            challengeStatus 1=待响应、2=已成功、3=已过期、4=已取消 5-已达成
	 * @return 
	 * @throws Exception 
	 */
	private String checkChallengeScreen(HashMap<String, String> map,String sql_temp,HashMap<String, List<Object>> mapList) throws Exception {
		StringBuffer sbf = new StringBuffer();
		List<Object> listStr = new ArrayList<>();
		List<Object> challengeStatus = new ArrayList<Object>(); 
		
		if (null != map.get("challengeStatus") && !"".equals(map.get("challengeStatus"))&&!"-1".equals(map.get("challengeStatus"))) {
			String challTemp=map.get("challengeStatus");
			challengeStatus.add(challTemp);
			if (null != challengeStatus && challengeStatus.size() > 0) { // 球场筛选不为空
				sbf.append(" and u.challenge_status in :challengeStatus ");
				mapList.put("challengeStatus", challengeStatus);	
			}
		}
		
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			hashMap.put("area", map.get("area"));
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
				hashMap.remove("area");
				mapList.put("area", listStr);
				sbf.append(" and ut.area in (:area ) ");
			}
		}
		
		sql_temp = sbf.toString();
		return sql_temp;
	}
	
	/**
	 * 拼接挑战lbs筛选2.0.3
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getLbsArea(HashMap<String, String> map) throws Exception{
		String strArea="";
		List<URegion> reList=new ArrayList<>();
		List<Integer> areaLi=new ArrayList<>();
		String challTemp=map.get("challengeStatus");
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			URegion region = baseDAO.get(URegion.class, map.get("area"));
			if (region != null && "2".equals(region.getType())) {
				reList=baseDAO.find("from URegion where parent = '"+map.get("area")+"'");
			} else if(region != null && "3".equals(region.getType())){
				reList.add(region);
			}
		}
		for(URegion re:reList){
			areaLi.add(Integer.parseInt(re.get_id()));
		}
		if(challTemp!=null&&challTemp.equals("-1")){
			challTemp="1,2,3,4,5";
		}
		if(areaLi!=null&&areaLi.size()>0){
			strArea="|areaid:"+areaLi.toString().replaceAll(" ", "");
		}
		String lbsfilter="challege_status:["+challTemp+"]"+strArea;
		return lbsfilter;
	}
	@Override
	public HashMap<String, Object> getChListAndBsListWithTeam(HashMap<String, String> map) throws Exception {
		if(map.get("teamId")!=null&&!"".equals("teamId")&&map.get("page")!=null&&!"".equals(map.get("page"))){
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			StringBuffer newSql=new StringBuffer();
			hash = new HashMap<String, Object>();
			newSql.append(
						sbSelect+" "+ sbJoin + "  "
						+"where (u.f_team_id = :teamId or u.x_team_id = :teamId) and u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' "
						+"order by ch_recommend_status desc, u.stdate desc,u.sttime desc  limit " + 
						page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			hash.put("ChListAndBsListWithTeam", li);
			return hash;
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getChListAndBsListWithUser(HashMap<String, String> map) throws Exception {
		if(map.get("page")!=null&&!"".equals(map.get("page"))){
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			hash = new HashMap<String, Object>();
			StringBuffer newSql=new StringBuffer();
			map.put("userId", uuserService.getUserinfoByToken(map).getUserId());
			String sql = "select team_id from u_player where user_id = :userId and team_id is not null and in_team='1' "; //查询个人所在的所有球队
			newSql.append(
				sbSelect+" "+sbJoin
				+ "where (u.f_team_id in (" + sql + ") or u.x_team_id in (" + sql + ")) "
				+ "order by ch_recommend_status desc, u.stdate desc,u.sttime desc limit " 
				+ page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			hash.put("ChListAndBsListWithUser", li);
			return hash;
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	//(预留接口)
	@Override
	public HashMap<String, Object> getChListAndBsListWithPlayer(HashMap<String, String> map) throws Exception {
		String userId=map.get("userId");
		if(userId!=null&&!"".equals(userId)&&map.get("page")!=null&&!"".equals(map.get("page"))){
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			hash = new HashMap<String, Object>();
			StringBuffer newSql=new StringBuffer();
			map.put("userId", userId);
			String sql = "select team_id from u_player where user_id = :userId and team_id is not null and in_team='1'  "; //查询球员所在的所有球队
			newSql.append(
				sbSelect+" "+sbJoin
				+ "where (u.f_team_id in (" + sql + ") or u.x_team_id in (" + sql + ")) and u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' "
				+ "order by ch_recommend_status desc, u.stdate desc,u.sttime desc limit " 
				+ page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			hash.put("ChListAndBsListWithPlayer", li);
			return hash;
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> chCheckCourtSession(HashMap<String, String> map,List<UMall> mallList) throws Exception {
		String uBrCourtsessionId=map.get("brCourtSessionId");
		String price=map.get("price");
		if(uBrCourtsessionId!=null&&!"".equals(uBrCourtsessionId)&&price!=null&&!"".equals(price)){
			if(!uCourtService.sessionBeOrdered(uBrCourtsessionId)){//预留调用接口
				
			}else{
				return WebPublicMehod.returnRet("error", "该场次已被预订！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
		return null;
	}

/*	@Override
	public HashMap<String, Object> responseCh(HashMap<String, String> map) throws Exception {
		String challengeId=map.get("challengeId");
		String teamId=map.get("teamId");
		if(challengeId!=null&&teamId!=null){
			UChallenge ch=baseDAO.get(UChallenge.class,challengeId);
			if(ch!=null){
				if(ch.getXteam()!=null&&ch.getUChallengeResp()!=null){
					return WebPublicMehod.returnRet("error", "设擂场次已被预订！");
				}else if(ch.getXteam()==null&&ch.getUChallengeResp()==null){
					saveResponseCh(map, ch);
					return WebPublicMehod.returnRet("success", "响应成功！");
				}else{
					return WebPublicMehod.returnRet("error", "库内擂主数据响应异常！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "设擂场次不存在！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}*/
	
	/**
	 * 保存响应者
	 * @param map
	 * 			teamId 球队id
	 * @param uch 擂主对象
	 * @throws Exception
	 */
	private void saveResponseCh(HashMap<String, String> map,UChallenge uch,UOrder order,UTeam uteam) throws Exception{
		UChallengeResp resp = new UChallengeResp();
		resp.setKeyId(WebPublicMehod.getUUID());
		resp.setUTeam(uteam);
		resp.setStdate(new Date());
		resp.setSttime(new Date());
		resp.setChallengeRespStatus("2");
		baseDAO.save(resp);
		uch.setXteam(uteam);
		uch.setUChallengeResp(resp);
		//uch.setChallengeStatus("1");//订单未支付还是待响应
		uch.setPerOrder(order);
		baseDAO.update(uch);
	}

	@Override
	public List<UChallengeCh> getChListWithTeam(String teamId) throws Exception {
		hashMap.put("teamId", teamId);
		List<UChallengeCh> chList= baseDAO.find("from UChallengeCh where isChampion='1' and UTeam.teamId=:teamId order by stdate desc,sttime desc",hashMap);
		return	chList;
	}

	@Override
	public HashMap<String, Object> startChallenge(HashMap<String, String> map) throws Exception {
		UOrder order=null;
		List<UDuelChallengeImg> list = null;
		String msg=map.get("msg");
		String brCourtId=map.get("brCourtId");
		String orderId=map.get("orderId");
		String stdate=map.get("stdate");
		String sttime=map.get("sttime");
		String enddate=map.get("enddate");
		String endtime=map.get("endtime");
		String addChallengeType=map.get("addChallengeType");
		String teamId=map.get("teamId");
		if(brCourtId!=null&&orderId!=null&&!"".equals(brCourtId)&&!"".equals(orderId)&&stdate!=null&&!"".equals(stdate)&&
				sttime!=null&&!"".equals(sttime)&&enddate!=null&&!"".equals(enddate)&&endtime!=null&&!"".equals(endtime)&&addChallengeType!=null&&!"".equals(addChallengeType)){
			
			map.put("userId", getUserId(map));
			//UTeam uTeam = getThisTeam(map);
			checkAppCode(map);//版本升级控制
			UTeam uTeam = uTeamService.findPlayerInfoById(map);
			
			if(uTeam.getTeamCount()<Public_Cache.TEAM_COUNT){
				return WebPublicMehod.returnRet("error", "-48_您的球队总人数小于"+Public_Cache.TEAM_COUNT+"人！");
			}
			
			Object obj= uOrderService.findOrderinfo(map).get("success");
			map.put("userId", uuserService.getUserinfoByToken(map).getUserId());
			if(obj!=null){
				order=(UOrder)obj;
			}
			//重发业务
			if(addChallengeType.equals("1")){
				if(map.get("challengeId")!=null&&!"".equals(map.get("challengeId"))){
					UChallenge cl=baseDAO.get(UChallenge.class,map.get("challengeId"));
					if(cl.getPerOrder()!=null){
						if(cl.getPerOrder().getOrderstatus().equals("4")){
							return WebPublicMehod.returnRet("error", "挑战已被响应，响应方正在支付中，不能重发！");
						}else 
						if(cl.getPerOrder().getOrderstatus().equals("1")){
							return WebPublicMehod.returnRet("error", "挑战已被响应，订单已支付，不能重发！");
						}else{
							baseDAO.executeHql(
									"update UChallenge set effectiveStatus = '2',challengeStatus = '4' where challengeId = :challengeId",
									PublicMethod.Maps_Mapo(map));
						}
					}else{
						baseDAO.executeHql(
								"update UChallenge set effectiveStatus = '2',challengeStatus = '4' where challengeId = :challengeId",
								PublicMethod.Maps_Mapo(map));
					}
					
					if(!delChallengeLbs(map.get("challengeId"))){//重发删除原有lbs数据
						lBSService.createUbaidulbsDataError(map.get("challengeId"), "5", "app端删除挑战lbs数据异常");
					}
				}else{
					return WebPublicMehod.returnRet("error", "重发走一个请求参数不完整！");
				}
				
				
				return startAgain(map, list, obj, uTeam, teamId, order, orderId, stdate, sttime, enddate, endtime, brCourtId, msg);
			}else{
				return startOne(map, list, obj, uTeam, teamId, order, orderId, stdate, sttime, enddate, endtime, brCourtId, msg);
			}
			

		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}
	


	/**
	 * 第一次发起
	 * @param map
	 * @param list 相册列表
	 * @param obj 订单obiect对象
	 * @param uTeam 球队对象
	 * @param teamId 球队id
	 * @param order 订单对象
	 * @param orderId 订单id
	 * @param stdate 开始日期
	 * @param sttime 开始时间
	 * @param enddate 结束日期
	 * @param endtime 结束时间
	 * @param brCourtId 下属球场id
	 * @param msg 稍句话
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> startOne(HashMap<String, String> map,List<UDuelChallengeImg> list,Object obj,UTeam uTeam,String teamId,UOrder order,String orderId,String stdate,String sttime,String enddate,String endtime,String brCourtId,String msg) throws Exception {
		if(selEffective(teamId)){
		if(obj!=null&&order.getOrderstatus().equals("1")){//判断订单是否支付
			hashMap.put("orderId", orderId);
			UChallenge cl=baseDAO.get("from UChallenge where effectiveStatus='1' and uorder.orderId=:orderId",hashMap);
			if(cl==null){
				hashMap.put("brCourtId", brCourtId);
				hashMap.put("teamId", teamId);
				UChampion cp=baseDAO.get("from UChampion where isChampion='1' and UBrCourt.subcourtId=:brCourtId and UTeam.teamId=:teamId",hashMap);
				if(cp!=null){
					UChallengeCh ch=saveCh(teamId,brCourtId, msg, cp.getKeyId(),stdate,sttime,enddate,endtime);//保存擂主挑战条件信息
					UChallenge ucp=saveChallenge(ch, order,teamId);//保存擂主大场次信息
					map.put("challengeId", ucp.getChallengeId());
					if(map.get("fimgList")!=null){
						JSONArray jsonarray = JSONArray.fromObject(map.get("fimgList"));
						list = (List)JSONArray.toList(jsonarray, UDuelChallengeImg.class); 
						saveChImgList(list, ch.getKeyId(),"1");//保存图片
					}
					saveUTeamActivity(uTeam.getTeamId());
					hashMap.clear();
					hashMap.put("f_team_name", ucp.getFteam().getName());
					hashMap.put("f_team_id", ucp.getFteam().getTeamId());
					hashMap.put("x_team_id", "");
					hashMap.put("challengeId", ucp.getChallengeId());
					hashMap.put("success", "走一个保存成功！");
					//saveBehavior("2", "10", teamId, ucp.getChallengeId(), null);
					if(!saveOrUpdateChallengeLbs(ucp, "1")){//放入百度lbs
						lBSService.createUbaidulbsDataError(map.get("challengeId"), "5", "app端新增挑战lbs数据异常");
					}
					return hashMap;
				}else{
					return WebPublicMehod.returnRet("error", "此球队当前不为此下属球场擂主！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "此场次订单正在使用，如需使用请先取消！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "主订单未支付！");
		}
		}else{
			return WebPublicMehod.returnRet("error", "已有正在发起的挑战！");
		}
	}
	
	/**
	 * 第二次发起
	 * @param map
	 * @param list 相册列表
	 * @param obj 订单obiect对象
	 * @param uTeam 球队对象
	 * @param teamId 球队id
	 * @param order 订单对象
	 * @param orderId 订单id
	 * @param stdate 开始日期
	 * @param sttime 开始时间
	 * @param enddate 结束日期
	 * @param endtime 结束时间
	 * @param brCourtId 下属球场id
	 * @param msg 稍句话
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> startAgain(HashMap<String, String> map,List<UDuelChallengeImg> list,Object obj,UTeam uTeam,String teamId,UOrder order,String orderId,String stdate,String sttime,String enddate,String endtime,String brCourtId,String msg) throws Exception {
		if(obj!=null&&order.getOrderstatus().equals("1")){//判断订单是否支付
			hashMap.put("orderId", orderId);
			hashMap.put("brCourtId", brCourtId);
			hashMap.put("teamId", teamId);
			UChampion cp=baseDAO.get("from UChampion where isChampion='1' and UBrCourt.subcourtId=:brCourtId and UTeam.teamId=:teamId",hashMap);
			if(cp!=null){
				UChallengeCh ch=saveCh(teamId,brCourtId, msg, cp.getKeyId(),stdate,sttime,enddate,endtime);//保存擂主挑战条件信息
				UChallenge ucp=saveChallenge(ch, order,teamId);//保存擂主大场次信息
				map.put("challengeId", ucp.getChallengeId());
				if(map.get("fimgList")!=null){
					JSONArray jsonarray = JSONArray.fromObject(map.get("fimgList"));
					list = (List)JSONArray.toList(jsonarray, UDuelChallengeImg.class); 
					saveChImgList(list, ch.getKeyId(),"1");//保存图片
				}
				saveUTeamActivity(uTeam.getTeamId());
				hashMap.clear();
				hashMap.put("f_team_name", ucp.getFteam().getName());
				hashMap.put("f_team_id", ucp.getFteam().getTeamId());
				hashMap.put("x_team_id", "");
				hashMap.put("challengeId", ucp.getChallengeId());
				hashMap.put("success", "走一个保存成功！");
				saveBehavior("2", "9", teamId, ucp.getChallengeId(), null);
				saveBehavior("1", "10", teamId, ucp.getChallengeId(), null);
				if(!delChallengeLbs(map.get("challengeId"))){
					lBSService.createUbaidulbsDataError(map.get("challengeId"), "5", "app端重发删除旧挑战lbs数据异常");
				}
				if(!saveOrUpdateChallengeLbs(ucp, "1")){//放入百度lbs
					lBSService.createUbaidulbsDataError(ucp.getChallengeId(), "5", "app端重发挑战lbs数据异常");
				}
				return hashMap;
			}else{
				return WebPublicMehod.returnRet("error", "此球队当前不为此下属球场擂主！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "主订单未支付！");
		}
	}
	/**
	 * 保存活跃度
	 * @param stdate 开始日期
	 * @param sttime 开始时间
	 * @param enddate 结束日期
	 * @param endtime 结束时间
	 * @throws Exception
	 */
	private void saveUTeamActivity(String teamId) throws Exception{
		UTeamActivity ac=new UTeamActivity();
		ac.setKeyId(WebPublicMehod.getUUID());
		ac.setActivityStatus("2");
		ac.setTeamId(teamId);
		ac.setCreatetime(new Date());
		baseDAO.save(ac);
	}
	/**
	 * 插入首次事件
	 * @param type  		1:用户 2：球队
	 * @param behaviorType	事件类型
	 * @param userId		用户id
	 * @param teamId		球队Id
	 * @param objectId		对应事件的Id
	 * @param objectType	事件类型对应的简称
	 */
	@Override
	public void saveBehavior(String type,String behaviorType,String teamId,String objectId,String objectType) throws Exception{
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("type", type);
		map.put("behaviorType", behaviorType);
		map.put("teamId", teamId);
		map.put("objectId", objectId);
		map.put("objectType", "");
		publicServiceImpl.updateBehavior(map);
	}
	
	/**
	 * 保存擂主历史发起表
	 * @param teamId 球队id
	 * @param stdate 挑战开始日期
	 * @param sttime 挑战开始时间
	 * @param enddate 挑战结束日期
	 * @param endtime 挑战结束时间
	 * @param brCourtId 下属球场id
	 * @param msg 捎句话内容
	 * @param cpId 擂主id
	 * @return 
	 * 		UChallengeCh 擂主历史发起对象
	 * @throws Exception
	 */
	private UChallengeCh saveCh(String teamId,String brCourtId,String msg,String cpId,String stdate,String sttime,String enddate,String endtime) throws Exception{
		//hashMap.put("sessionId", sessionId);
		UChallengeCh ch=new UChallengeCh();
		UTeam team=new UTeam();
		UBrCourt br=baseDAO.get("from UBrCourt where subcourtId='"+brCourtId+"'");
		//UBrCourtsession brc=baseDAO.get("from UBrCourtsession where sessionId=:sessionId",hashMap);
		team.setTeamId(teamId);
		ch.setKeyId(WebPublicMehod.getUUID());
		ch.setUTeam(team);
		ch.setStdate(PublicMethod.getStringToDate(stdate, "yyyy-MM-dd"));
		ch.setSttime(sttime);
		ch.setEnddate(PublicMethod.getStringToDate(enddate, "yyyy-MM-dd"));
		ch.setEndtime(endtime);
		ch.setUBrCourt(br);
		ch.setRemark(msg);
		ch.setChampionId(cpId);
		ch.setIsChampion("1");
		ch.setChallengePayType("2");
		ch.setWincount("15");//默认发起15激数
		baseDAO.save(ch);
		return ch;
	}
	
	/**
	 * 保存大场次待响应数据
	 * @param ch 擂主历史发起对象
	 * @param order 订单对象
	 * @return
	 * 		UChallenge 大场次待响应对象
	 * @throws Exception
	 */
	private UChallenge saveChallenge(UChallengeCh ch,UOrder order,String teamId) throws Exception{
		UChallenge cl=new UChallenge();
		UTeam ut=new UTeam();
		ut.setTeamId(teamId);
		cl.setChallengeId(WebPublicMehod.getUUID());
		cl.setChallengeType("2");
		cl.setStdate(new Date());
		cl.setSttime(new Date());
		cl.setUChallengeCh(ch);
		cl.setChallengeStatus("1");
		cl.setUorder(order);
		cl.setEffectiveStatus("1");
		cl.setChRecommendStatus("0");
		cl.setFteam(ut);
		baseDAO.save(cl);
		return cl;
	}
	
	/**
	 * 保存发起方或响应方图片相册
	 * @param imgList 图片对象
	 * @param objectId 挑战信息表id
	 * @param challType 响应类型
	 * @throws Exception
	 */
	private void saveChImgList(List<UDuelChallengeImg> imgList,String objectId,String challType) throws Exception{
		for(UDuelChallengeImg uimg:imgList){
			uimg.setKeyId(WebPublicMehod.getUUID());
			uimg.setDuelChallUsingType(challType);
			uimg.setImgSaveType("2");
			uimg.setCreatetime(new Date());
			uimg.setObjectId(objectId);
			baseDAO.save(uimg);
		}	
	}
	
	/**
	 * 	验证挑战条件信息表中是否有待完成数据
	 *  @param map 
	 *  		userId 用户ID
	 *  @return 
	 *  		UChallengeCh 擂主信息记录对象
	 *  @throws Exception 
	 */
	private UChallengeCh checkChallengeCh(String userId) throws Exception{
		hashMap.put("userId", userId);
		UChallengeCh UChallengeCh = baseDAO.get("from UChallengeCh where userId = :userId and challengeChStatus = '2'",hashMap);
		return UChallengeCh;
	}

	@Override
	public HashMap<String, Object> startAgainChallenge(HashMap<String, String> map) throws Exception {
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)){
			UChallenge cl=baseDAO.get(UChallenge.class,challengeId);
			if(cl.getPerOrder()==null){
				cl.setEffectiveStatus("2");//设为无效
				cl.setChallengeStatus("4");
				//cl.getUChallengeCh().setIsChampion("0");//设历史发起方为无效
				baseDAO.update(cl);
				map.put("orderId", cl.getUorder().getOrderId());
				return uOrderService.cancelChallengeOrder(map);//取消订单释放场次
			}else{
				return WebPublicMehod.returnRet("error", "挑战已被响应不能取消！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> responseChMsgImg(HashMap<String, String> map) throws Exception {
		String keyId=map.get("respId");
		String msg=map.get("msg");
		JSONArray jsonarray = JSONArray.fromObject(map.get("ximgList"));
		List<UDuelChallengeImg> ximgList = (List)JSONArray.toList(jsonarray, UDuelChallengeImg.class); 
		if(keyId!=null&&!"".equals(keyId)){
			UChallengeResp resp=baseDAO.get(UChallengeResp.class,keyId);
				if(resp!=null&&"1".equals(resp.getChallengeRespStatus())){
				resp.setRemark(msg);
				baseDAO.update(resp);
				saveChImgList(ximgList,keyId,"2");//保存图片
				hashMap.put("respKeyId", resp.getKeyId());
				UChallenge cl=baseDAO.get("from UChallenge where UChallengeResp.keyId=:respKeyId",hashMap);
				hashMap.clear();
				hashMap.put("x_team_name", cl.getXteam().getName());
				hashMap.put("f_team_id", cl.getFteam().getTeamId());
				hashMap.put("x_team_id", cl.getXteam().getTeamId());
				hashMap.put("challengeId", cl.getChallengeId());
				hashMap.put("challengeType", "1");
				hashMap.put("success", "保存成功！");
				return hashMap;
			}else{
				return WebPublicMehod.returnRet("error", "-50_响应方订单未支付！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getFollowChList(HashMap<String, String> map) throws Exception {
		if(map.get("page")!=null&&!"".equals(map.get("page"))){
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			map.put("userId", uuserService.getUserinfoByToken(map).getUserId());
			StringBuffer newSql=new StringBuffer();
			hash = new HashMap<String, Object>();
			newSql.append(
						sbSelect+" "+sbJoin +" left join u_follow uf on u.challenge_id=uf.object_id "
						+"where uf.user_follow_type='6' and uf.follow_status='1' and uf.user_id=:userId group by u.challenge_id  "
						+"order by ch_recommend_status desc, u.stdate desc,u.sttime desc  limit " + 
						page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			return WebPublicMehod.returnRet("clList", li);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getFollowChBsList(HashMap<String, String> map) throws Exception {
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)){
			hashMap.put("challengeId", challengeId);
			StringBuffer newSql=new StringBuffer();
			hash = new HashMap<String, Object>();
			newSql.append(
						sbSelect+" "+sbJoin+" "
						+" where u.challenge_id=:challengeId "
						+" order by b.sttime desc ");
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			return WebPublicMehod.returnRet("bsList", li);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public List<UChampion> getTeamChList(String teamId) throws Exception {
		hashMap.put("teamId", teamId);
		return baseDAO.find("from UChampion where isChampion='1' and UTeam.teamId=:teamId order by createDate desc",hashMap);
	}

	@Override
	public HashMap<String, Object> saveChallengePerOrder(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> orderMap=null;
		UOrder or=null;
		String orderId=map.get("orderId");
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)&&orderId!=null&&!"".equals(orderId)){
			Object obj=new Object(); 
			hashMap.put("challengeId", challengeId);
			UChallenge cl=baseDAO.get("from UChallenge where effectiveStatus='1' and challengeId=:challengeId",hashMap);
			/*JSONArray jsonarray = JSONArray.fromObject(map.get("xmallList"));
			xmallList = (List<UMall>)JSONArray.toList(jsonarray, UMall.class); */
			map.put("userId", getUserId(map));
			checkAppCode(map);//版本升级控制
			map.put("type", "6");
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);
			map.remove("type");
			//UTeam uTeam = getThisTeam(map);
			if(cl!=null){
				String error=checkRespThings(cl, uTeam);
				if(!"success".equals(error)){
					hashMap.clear();
					hashMap.put("checkType", error.split("_")[0]);
					hashMap.put("errorMsg", error.split("_")[1]);
					return hashMap;
				}
				
				orderMap=uOrderService.saveAppendDuelAndChallOrder(map);
				obj=orderMap.get("order");
				if(obj!=null){
					or=(UOrder) obj;
					saveResponseCh(map,cl,or,uTeam);
					//saveBehavior("2", "9", teamId, cl.getChallengeId(), "攻擂");
					saveUTeamActivity(uTeam.getTeamId());
					hashMap.clear();
					hashMap.put("checkType", "1");
					hashMap.put("orderId", cl.getUorder().getOrderId());
					hashMap.put("orderPerId", or.getOrderId());
					hashMap.put("orderNum", or.getOrdernum());
					hashMap.put("brCourtNam", orderMap.get("brCourtNam"));
					hashMap.put("price", orderMap.get("price"));
					return hashMap;
				}else{
					hashMap.clear();
					hashMap.put("checkType", "-50");
					hashMap.put("errorMsg", "响应订单生成失败！");
					return hashMap;
				}
			}else{
				hashMap.clear();
				hashMap.put("checkType", "-50");
				hashMap.put("errorMsg", "此挑战不存在或无效！");
				return hashMap;
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> checkChallengePerOrder(HashMap<String, String> map) throws Exception {
		String orderId=map.get("orderId");
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)&&orderId!=null&&!"".equals(orderId)){
			hashMap.put("challengeId", challengeId);
			UChallenge cl=baseDAO.get("from UChallenge where effectiveStatus='1' and challengeId=:challengeId",hashMap);
			map.put("userId", getUserId(map));
			checkAppCode(map);//版本升级控制
			map.put("type", "6");
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);//是否有资格
			map.remove("type");
			//UTeam uTeam = getThisTeam(map);
			if(cl!=null){
				String error=checkRespThings(cl, uTeam);
				if(!"success".equals(error)){
					hashMap.clear();
					hashMap.put("checkType", error.split("_")[0]);
					hashMap.put("errorMsg", error.split("_")[1]);
					return hashMap;
				}
				
				hashMap.clear();
				hashMap.put("checkType", "1");
				hashMap.put("orderId", cl.getUorder().getOrderId());
				return hashMap;
			}else{
				hashMap.clear();
				hashMap.put("checkType","-50");
				hashMap.put("errorMsg","此挑战不存在或无效！");
				return hashMap;
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}
	
	/**
	 * 判断响应条件
	 * @param cl 挑战大场次
	 * @param uTeam 响应球队
	 * @return
	 * @throws Exception
	 */
	private String checkRespThings(UChallenge cl,UTeam uTeam) throws Exception{
		String error="success";
		if(uTeam!=null){
			if(cl.getChallengeStatus().equals("2")||cl.getChallengeStatus().equals("5")){
				error="-50_此挑战已经被响应！";
			}else if(cl.getChallengeStatus().equals("3")){
				error="-50_此挑战已过期！";
			}else if(cl.getChallengeStatus().equals("4")){
				error="-50_此挑战已取消！";
			}else if(WebPublicMehod.compare_date(PublicMethod.getStringToDate(PublicMethod.getDateToString(
					cl.getUChallengeCh().getStdate(), "yyyy-MM-dd") + " " + cl.getUChallengeCh().getSttime(), "yyyy-MM-dd HH:mm"),
					new Date()) == -1){
				cl.setChallengeStatus("3");
				baseDAO.save(cl);
				error="-50_此挑战已过期！";
			}else if(uTeam.getTeamId()==null){
				error="-49_您不是球队队长无法应战！";
			}else if(cl.getFteam().getTeamId().equals(uTeam.getTeamId())){
				error="-50_您不能响应自己球队发起的挑战！";
			}else if(uTeam.getTeamCount()<Public_Cache.TEAM_COUNT){
				error="-48_您的球队总人数小于"+Public_Cache.TEAM_COUNT+"人！";
			}else if(cl.getUorder()==null){
				error="-50_挑战主订单不存在！";
			}else if(cl.getPerOrder()!=null){
				if(cl.getPerOrder().getOrderstatus().equals("1")){
					error="-50_此挑战正在被响应中！";
				}
			}
		}else{
			error="-49_您不是球队队长无法应战！";
		}
		return error;
	}
	
	@Override
	public HashMap<String, Object> delPerOrder(HashMap<String, String> map, List<UOrder> perOrderList) throws Exception {
		for(UOrder or:perOrderList){
			hashMap.put("perId", or.getOrderId());
			UChallenge cl=baseDAO.get("from UChallenge where perOrder.orderPerId=:perId", hashMap);
			cl.setPerOrder(null);
			cl.setChallengeStatus("1");
			baseDAO.update(cl);
		}
		return WebPublicMehod.returnRet("success", "取消响应订单成功！");
	}

	@Override
	public HashMap<String, Object> getChallengeAndBs(HashMap<String, String> map) throws Exception {
		String respChallenge="";
		StringBuffer newSql=new StringBuffer();
		String challengeId=map.get("challengeId");
		String challengeType=map.get("challengeType");
		String floowStatus = "2";
		String whereStr="";
		int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));//int值的版本号
		if(challengeId!=null&&!"".equals(challengeId)&&challengeType!=null&&!"".equals(challengeType)){
			
			map.put("userId", getUserId(map));
			UFollow uf = baseDAO.get(map, "from UFollow where UUser.userId = :userId and userFollowType = '6' and objectId = :challengeId"); //查询关注表
			if(null != uf){
				floowStatus = uf.getFollowStatus();
			}
			


			if(challengeType.equals("1")){
				whereStr=" where u.challenge_id=:challengeId ";
			}else if(challengeType.equals("2")){
				whereStr=" where b.bs_id=:bsId ";
			}
			newSql.append(sbSelect+" "+sbJoin+" "+whereStr);
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map, newSql.toString());
			hashMap.clear();
			if(li!=null&&li.size()>0){
				HashMap<String,String> ci=(HashMap<String, String>) li.get(0);
				if(intAppCode<202){//版本切换
					respChallenge=checkRespChallenge(map,ci.get("f_team_id"),ci.get("x_team_id"));
				}else{
					respChallenge=newCheckRespChallenge(map,ci.get("f_team_id"),ci.get("x_team_id"));
				}
				
				//此挑战是否是本人发起
				UChallenge uc=baseDAO.get(UChallenge.class,challengeId); 
				if(uc!=null&&uc.getUorder()!=null&&uc.getUorder().getUUser()!=null){
					if(map.get("userId").equals(uc.getUorder().getUUser().getUserId())){
						ci.put("fuser", "1");
					}else{
						ci.put("fuser", "0");
					}
				}else{
					ci.put("fuser", "0");
				}
				
				ci.put("floowStatus", floowStatus);
				ci.put("respChallenge", respChallenge);
				hashMap.put("ChallengeInfo", ci);
			}else{
				hashMap.put("ChallengeInfo", null);
			}
			
			
			
			
			return hashMap;
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	/**
	 * 
	 * 
	   TODO -  验证进入页面人的响应状态（老接口按照新接口重构）
	   @param map
	   @param fteamId 发起方队伍ID
	   @param xteamId 响应放队伍ID
	   @return
	   @throws Exception
	   yc
	 */
	private String checkRespChallenge(HashMap<String, String> map,String fteamId,String xteamId) throws Exception {
		// respDuel = -1 发起方队长进入 -2响应方队长进入 -3第三方进入 -4发起方队员  -5响应防队员
		String checkTemp="";
		boolean ftop=false;
		boolean flow=false;
		boolean xtop=false;
		boolean xlow=false;
		boolean zteam=false;
		map.put("type", "6");//挑战权限
		List<HashMap<String, Object>> teamMap=uTeamService.getUteamRoleByUserId(map);
		map.remove("type");
		for(HashMap<String, Object> temp:teamMap){
			String teamId=(String) temp.get("teamId");
			String challenge=(String) temp.get("challenge");
			if(teamId.equals(fteamId)){
				if(challenge.equals("1")){
					ftop=true;
				}else if(challenge.equals("-1")){
					flow=true;
				}
			}else if(teamId.equals(xteamId)){
				if(challenge.equals("1")){
					xtop=true;
				}else if(challenge.equals("-1")){
					xlow=true;
				}
			}else{
				zteam=true;
			}
		}
		//老接口优先级排序
		if(ftop){
			checkTemp="-1";
		}else if(xtop){
			checkTemp="-2";
		}else if(flow){
			checkTemp="-4";
		}else if(xlow){
			checkTemp="-5";
		}else if(zteam){
			checkTemp="-3";
		}
	
		return checkTemp;
	}
		
	/**
	 * 
	 * 
	   TODO -  验证进入页面人的响应状态
	   @param map
	   @param fteamId 发起方队伍ID
	   @param xteamId 响应放队伍ID
	   @return
	   @throws Exception 
	   yc
	 */
	private String newCheckRespChallenge(HashMap<String, String> map,String fteamId,String xteamId) throws Exception {
		// respDuel = -1 发起方队长进入 -2响应方队长进入 -3第三方进入 -4发起方队员  -5响应防队员
		String checkTemp="";
		boolean ftop=false;
		boolean flow=false;
		boolean xtop=false;
		boolean xlow=false;
		boolean zteam=false;
		map.put("type", "6");//挑战权限
		List<HashMap<String, Object>> teamMap=uTeamService.getUteamRoleByUserId(map);
		map.remove("type");
		for(HashMap<String, Object> temp:teamMap){
			String teamId=(String) temp.get("teamId");
			String challenge=(String) temp.get("challenge");
			if(teamId.equals(fteamId)){
				if(challenge.equals("1")){
					ftop=true;
				}else if(challenge.equals("-1")){
					flow=true;
				}
			}else if(teamId.equals(xteamId)){
				if(challenge.equals("1")){
					xtop=true;
				}else if(challenge.equals("-1")){
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
	
	
	/**
	 * 获得  -1 发起放 -2响应方 -3第三方(不使用)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getRespType (HashMap<String, String> map) throws Exception{
		String respChallengeSql = "select case when du.f_team_id = :teamId then '-1' else '0' end as fdu_status,"
				+ "case dur.team_id when :teamId then '-2' else '-3' end as xdu_status "
				+ "from u_challenge as du left join u_challenge_resp as dur on du.xch_id = dur.key_id "
				+ "where du.challenge_id = :challengeId "
				+ "group by fdu_status";
		//respDuel = -1 发起放 -2响应方 -3第三方
		List<Object> respChallengelList = baseDAO.findSQLMap(map, respChallengeSql);
		String respChallenge=checkRespChallenge(respChallengelList);
		return respChallenge;
	}
	/**
	 * 
	 * 
	   TODO - 获取TeamId
	   @param map
	   			userId 用户ID
	   @return
	   2016年3月8日
	   mazkc
	 * @throws Exception 
	 */
	/*private UTeam getTeamId(HashMap<String, String> map) throws Exception{
		map.put("userId", map.get("loginUserId"));
		UTeam team = (UTeam) uteamService.getUteaminfoByUserId(map).get("uTeam");
		return team;
	}*/
	
	//验证进入页面人的响应状态
	private String checkRespChallenge(List<Object> respDuelList){
		String returnStr = "";
		HashMap<String,Object> hash = null;
		if(null != respDuelList && respDuelList.size() > 0){
			hash = (HashMap<String, Object>) respDuelList.get(0);
			String fdu_status = (String) hash.get("fdu_status");
			String xdu_status = (String) hash.get("xdu_status");
			if("-1".equals(fdu_status)){
				returnStr = "-1";
			}else{
				returnStr = xdu_status; //-2 or -3
			}
		}
		return returnStr;
	}
	
	@Override
	public HashMap<String, Object> getChallengeAndBsBody(HashMap<String, String> map) throws Exception {
		String challenge_Id=map.get("challengeId");
		String bsId=map.get("bsId");
		List<HashMap<String, Object>> list = null;
		HashMap<String, Object> mapList = null;
		HashMap<String, Object> or= new HashMap<String, Object>();
		if(challenge_Id!=null&&!"".equals(challenge_Id)){
			UChallenge cl=baseDAO.get(UChallenge.class,challenge_Id);
			UChallengeBs bs=baseDAO.get(UChallengeBs.class,bsId);
			if(cl!=null){
				if(cl.getUorder()!=null){//为历史比赛数据使用
					map.put("orderId", cl.getUorder().getOrderId());
					or= uOrderService.findOrderinfoDuelAndDek(map);
				}else{
					map.put("orderId", "");
					map.put("subcourt_id", "");
					if(cl.getUChallengeCh().getUBrCourt()!=null){
						map.put("subcourt_id", cl.getUChallengeCh().getUBrCourt().getSubcourtId());
					}
					list = new ArrayList<>();
					
					for (int i = 0; i < 3; i++) {
						mapList = new HashMap<String, Object>();
						mapList.put("imgurl", "");
						if (i == 0) { // 球场名
							mapList.put("content", cl.getUChallengeCh().getUBrCourt()
									.getName());
							mapList.put("name", "name");
						} else if (i == 1) { // 地址
							mapList.put("content", cl.getUChallengeCh().getUBrCourt()
									.getUCourt().getAddress());
							mapList.put("name", "address");
						} else if (i == 2) { // 时间
							mapList.put("content",PublicMethod.getDateToString(cl.getUChallengeCh().getStdate(), "MM月dd日")+" "+cl.getUChallengeCh().getSttime()+"-"+cl.getUChallengeCh().getEndtime());
							mapList.put("name", "dateTime");
						}
						list.add(mapList);
						or.put("list", list);
					}
					// 支付方式
					mapList = new HashMap<String, Object>();
					mapList.put("imgurl", "");
					mapList.put(
							"content",
							Public_Cache.HASH_PARAMS("challenge_pay_type").get(
									cl.getUChallengeCh().getChallengePayType()));
					mapList.put("name", "duelPayTypeName");
					or.put("duelPayTypeNameList", mapList);
					// 价格
					mapList = new HashMap<String, Object>();
					mapList.put("imgurl", "");
					mapList.put("content", null);
					mapList.put("name", "price");
					or.put("priceList", mapList);
				}
				if(bs!=null){
					mapList = new HashMap<String, Object>();
					list=(List<HashMap<String, Object>>) or.get("list");
					mapList.put("content",PublicMethod.getDateToString(bs.getStdate(), "MM月dd日")+" "+bs.getSttime()+"-"+bs.getEndtime());
					mapList.put("name", "dateTime");
					list.remove(2);
					list.add(mapList);
					or.put("list", list);
				}
				
				hashMap.put("objectId", cl.getUChallengeCh().getKeyId());
				List<UDuelChallengeImg> imgList=baseDAO.find("from UDuelChallengeImg where imgSaveType='2' and duelChallUsingType='1' and objectId=:objectId",hashMap);
				hashMap.clear();
				hashMap.put("order", or);
				hashMap.put("imgList", imgList);
				hashMap.put("remark", cl.getUChallengeCh().getRemark());
				map.put("self", "self");
				if(ucourtService.queryCourtDetailByOrderid(map)!=null){
					hashMap.putAll(ucourtService.queryCourtDetailByOrderid(map));
				}
				return hashMap;
			}else{
				return WebPublicMehod.returnRet("error", "挑战或订单不存在！");
			}
		}
		return WebPublicMehod.returnRet("error", "请求参数不完整！");
	}
	
	
	@Override
	public HashMap<String, Object> getChallengePhotoList(HashMap<String, String> map) throws Exception {
		String type=map.get("type");
		String challengeId=map.get("challengeId");
		if(map.get("type")!=null&&!"".equals(map.get("type"))&&map.get("challengeId")!=null&&!"".equals(map.get("challengeId"))){
			PageLimitPhoto page = new PageLimitPhoto(Integer.parseInt(map.get("page")), 0);
			hashMap.put("type", type);
			hashMap.put("challengeId", challengeId);
			hashMap.put("objectId","");
			UChallenge ucp=baseDAO.get(UChallenge.class,map.get("challengeId"));
			if(type.equals("1")){
				if(ucp!=null&&ucp.getUChallengeCh()!=null){
					hashMap.put("objectId", ucp.getUChallengeCh().getKeyId());
				}else{
					return WebPublicMehod.returnRet("error", "挑战发起方不存在！");
				}
			}else if(type.equals("2")){
				if(ucp!=null&&ucp.getUChallengeResp()!=null){
					hashMap.put("objectId", ucp.getUChallengeResp().getKeyId());
				}else{
					return WebPublicMehod.returnRet("error", "挑战响应方方不存在！");
				}
			}
			List<UDuelChallengeImg> imgList=baseDAO.findByPage("from UDuelChallengeImg where imgSaveType='2' and duelChallUsingType=:type and objectId=:objectId",hashMap, page.getLimit(), page.getOffset());
			hashMap.clear();
			return WebPublicMehod.returnRet("photoList", imgList);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}
	
	@Override
	public boolean getRecommendStatus(String teamId) throws Exception {
		hashMap.put("teamId", teamId);
		List<UChallenge> clList=  baseDAO.get("from UChallenge where fteam.teamId=:teamId  and  chRecommendStatus='1' and effectiveStatus='1' ",hashMap);
		if(clList.size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void respChallengePayCallBack(HashMap<String, String> map) throws Exception {
		UChallenge cl=baseDAO.get(map, " from UChallenge where perOrder.orderId=:orderId");
		String fteamId="";
		String xteamId="";
		String courtName="";
		if(cl!=null){
			cl.setChallengeStatus("5");
			cl.getUChallengeResp().setChallengeRespStatus("1");
			baseDAO.update(cl);
			if(cl.getUChallengeResp().getUTeam()!=null){
				xteamId=cl.getUChallengeResp().getUTeam().getTeamId();
			}
			if(cl.getUChallengeCh().getUTeam()!=null){
				fteamId=cl.getUChallengeCh().getUTeam().getTeamId();
			}
			saveBehavior("2", "9", xteamId, cl.getChallengeId(), null);//达成里程碑
			saveBehavior("2", "9", fteamId, cl.getChallengeId(), null);
			saveBehavior("2", "15", xteamId, cl.getChallengeId(), null);
			saveBehavior("2", "15", fteamId, cl.getChallengeId(), null);
			saveBehavior("1", "11", xteamId, cl.getChallengeId(), null);
			saveBehavior("1", "11", fteamId, cl.getChallengeId(), null);
			if(cl.getUChallengeCh()!=null&&cl.getUChallengeCh().getUBrCourt()!=null){
				courtName=cl.getUChallengeCh().getUBrCourt().getName();
			}
			
			if(!saveOrUpdateChallengeLbs(cl, "2")){//放入百度lbs
				lBSService.createUbaidulbsDataError(map.get("challengeId"), "5", "app端修改挑战lbs数据异常");
			}
			
			appPushFightToPlayerOnTeamByType(cl.getChallengeId(), fteamId, "", fteamId, xteamId,courtName,cl.getXteam().getName());
			appMessagePushIntegral(cl.getChallengeId(), fteamId, "", fteamId, xteamId,courtName);
		}
	}

	/**
	 * app推送
	 * @param teamId 球队id
	 * @throws Exception
	 */
	private void appPushFightToPlayerOnTeamByType(String challengeId,String teamId,String bsId,String f_team_id,String x_team_id,String courtName,String contentName) throws Exception{
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("challengeId", challengeId);
		map.put("mes_type", "ftAnswerdek");
		map.put("jump", "b07");
		map.put("teamId", teamId);
		map.put("bs_id", bsId);
		map.put("f_team_id", f_team_id);
		map.put("x_team_id", x_team_id);
		map.put("courtName", courtName);
		map.put("contentName", contentName);
		map.put("challengeType", "1");
		map.put("is_phone", "1");
		messageService.pushFightToPlayerOnTeamByType(map);
	}
	
	/**
	 * app message推送
	 * @param teamId 球队id
	 * @throws Exception
	 */
	private void appMessagePushIntegral(String challengeId,String teamId,String bsId,String f_team_id,String x_team_id,String courtName) throws Exception{
		HashMap<String,String> map=new HashMap<String,String>();
		JSONObject json=new JSONObject();
		json.put("challengeId", challengeId);
		json.put("jump", "b07");
		json.put("f_team_id", f_team_id);
		json.put("x_team_id", x_team_id);
		json.put("challengeType", "1");
		json.put("bs_id", bsId);
		map.put("type", "fight");
		map.put("mes_type", "ftAnswerdek");
		map.put("contentName", courtName);
		map.put("teamId", teamId);
		map.put("params", json.toString());
		messageService.addMoreMessageByType(map);
	}
	
	@Override
	public HashMap<String, Object> checkChampion(HashMap<String, String> map) throws Exception {
		String brCourtId=map.get("brCourtId");
		if(brCourtId!=null&&!"".equals(brCourtId)){
			map.put("userId", getUserId(map));
			//UTeam uTeam = getThisTeam(map);
			checkAppCode(map);//版本升级控制
			map.put("type", "6");
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);//直接返回擂主球队对象
			map.remove("type");
			if(uTeam!=null&&uTeam.getTeamId()!=null&&!"".equals(uTeam.getTeamId())){
				if(getTeamChStatus(uTeam.getTeamId(), brCourtId)){
					if(selEffectiveCourt(uTeam.getTeamId(),brCourtId)){
						hashMap.clear();
						hashMap.put("success", "可以发起守擂");
					}else{
						return WebPublicMehod.returnRet("error", "-49_已有正在发起的挑战！");
					}
				}else{
					return WebPublicMehod.returnRet("error", "-50_你不是当前球场擂主\n无法发起守擂战！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "-50_你不是当前球队队长！");
			} 
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
		return hashMap;
	}

	@Override
	public HashMap<String, Object> checkChampionWithCourt(HashMap<String, String> map) throws Exception {
			map.put("userId", getUserId(map));
			map.put("type", "6");
			checkAppCode(map);
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);
			map.remove("type");
			if(uTeam!=null&&uTeam.getTeamId()!=null&&!"".equals(uTeam.getTeamId())){
				hashMap.put("teamId", uTeam.getTeamId());
				List<UChampion> uchList=baseDAO.find("from UChampion where UTeam.teamId=:teamId and isChampion='1' ",hashMap);//防止错误数据出现查询list
				if(uchList!=null&&uchList.size()>0){
					if(selEffective(uTeam.getTeamId())){
						hashMap.clear();
						if(uchList.get(0).getUTeam()!=null){
							hashMap.put("teamId", uchList.get(0).getUTeam().getTeamId());
						}else{
							return WebPublicMehod.returnRet("error", "-50_擂主球队数据异常");
						}
						if(uchList.get(0).getUBrCourt()!=null){
							hashMap.put("brCourtId", uchList.get(0).getUBrCourt().getSubcourtId());
						}else{
							return WebPublicMehod.returnRet("error", "-50_擂主下属球场数据异常");
						}
						hashMap.put("jumpType", "1");
						hashMap.put("success", "可以发起守擂");
					}else{
						UChallenge cl=baseDAO.get("from UChallenge where fteam.teamId=:teamId and effectiveStatus='1' and (challengeStatus='1' or challengeStatus='5') ",hashMap);
						hashMap.clear();
						hashMap.put("challenge_id", cl.getChallengeId());
						hashMap.put("x_team_id", "");
						hashMap.put("challenge_status", cl.getChallengeStatus());
						hashMap.put("f_team_id", cl.getFteam().getTeamId());
						if(cl.getXteam()!=null){
							hashMap.put("x_team_id", cl.getXteam().getTeamId());
						}
						hashMap.put("jumpType", "2");
						hashMap.put("success", "跳转守擂详细页");
						if(uchList.get(0).getUBrCourt()!=null){
							hashMap.put("brCourtId", uchList.get(0).getUBrCourt().getSubcourtId());
						}else{
							return WebPublicMehod.returnRet("error", "-50_擂主下属球场数据异常");
						}
						return hashMap;
					}
				}else{
					return WebPublicMehod.returnRet("error", "-50_您的球队当前不是擂主");
				}
			}else{
				return WebPublicMehod.returnRet("error", "-50_您不是球队队长");
			}
		return hashMap;
	}
	
	@Override
	public HashMap<String, Object> checkChampionWithCourtPage(HashMap<String, String> map) throws Exception {
		checkAppCode(map);
		String brCourtId = map.get("brCourtId");
		String teamId = map.get("teamId");
		if (brCourtId != null && !"".equals(brCourtId)&&teamId != null && !"".equals(teamId)) {	
			map.put("userId", getUserId(map));
			UTeam uTeam = uTeamService.getUteamByUserIdTeamId(map);//直接返回带关联擂主的球队数据
			if (uTeam != null && uTeam.getTeamId() != null && !"".equals(uTeam.getTeamId())) {
				hashMap.put("teamId", uTeam.getTeamId());
				hashMap.put("brCourtId", brCourtId);
				List<UChampion> uchList = baseDAO.find("from UChampion where UTeam.teamId=:teamId and isChampion='1' and UBrCourt.subcourtId=:brCourtId ",hashMap);// 防止错误数据出现查询list
				if (uchList != null && uchList.size() > 0) {
					if (selEffectiveCourt(uTeam.getTeamId(),brCourtId)) {
						hashMap.clear();
						if (uchList.get(0).getUTeam() != null) {
							hashMap.put("teamId", uchList.get(0).getUTeam().getTeamId());
						} else {
							return WebPublicMehod.returnRet("error", "-50_擂主球队数据异常");
						}
						if (uchList.get(0).getUBrCourt() != null) {
							hashMap.put("brCourtId", uchList.get(0).getUBrCourt().getSubcourtId());
						} else {
							return WebPublicMehod.returnRet("error", "-50_擂主下属球场数据异常");
						}
						hashMap.put("jumpType", "1");
						hashMap.put("success", "可以发起守擂");
					} else {
						UChallenge cl = baseDAO.get("from UChallenge where fteam.teamId=:teamId and UChallengeCh.UBrCourt.subcourtId=:brCourtId and  effectiveStatus='1'  and (challengeStatus='1' or challengeStatus='5') ", hashMap);
						hashMap.clear();
						hashMap.put("challenge_id", cl.getChallengeId());
						hashMap.put("x_team_id", "");
						hashMap.put("challenge_status", cl.getChallengeStatus());
						hashMap.put("f_team_id", cl.getFteam().getTeamId());
						if (cl.getXteam() != null) {
							hashMap.put("x_team_id", cl.getXteam().getTeamId());
						}
						hashMap.put("jumpType", "2");
						hashMap.put("success", "跳转守擂详细页");
						if (uchList.get(0).getUBrCourt() != null) {
							hashMap.put("brCourtId", uchList.get(0).getUBrCourt().getSubcourtId());
						} else {
							return WebPublicMehod.returnRet("error", "-50_擂主下属球场数据异常");
						}
						return hashMap;
					}
				} else {
					return WebPublicMehod.returnRet("error", "-50_您的球队当前不是此球场擂主");
				}
			} else {
				//return WebPublicMehod.returnRet("error", "-50_您不是球队队长");
				return WebPublicMehod.returnRet("error", "-50_您的球队当前不是此球场擂主");
			}
			return hashMap;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}

	@Override
	public HashMap<String, Object> getChampionInfoWithCourt(HashMap<String, String> map) throws Exception {
		String brCourtId=map.get("brCourtId");
		if(brCourtId!=null&&!"".equals(brCourtId)){
			List<HashMap<String,Object>> oldCpList=new ArrayList<HashMap<String,Object>>();//旧擂主对象
			UChampion cp=getNowCh(brCourtId);
			List<UChampion> cpList=getOldChList(brCourtId);
			for(UChampion oldCp:cpList){
				if(oldCp.getUTeam()!=null){
					HashMap<String,Object> hash=new HashMap<String,Object>();
					hash.put("teamId", oldCp.getUTeam().getTeamId());
					UTeamImg imgs=baseDAO.get("from UTeamImg where imgSizeType='1' and timgUsingType='1' and UTeam.teamId=:teamId",hash);
					hash.put("oldTeamName", oldCp.getUTeam().getName());
					if(imgs!=null){
						hash.put("oldTeamImg", imgs.getImgurl());
					}else{
						hash.put("oldTeamImg", "");
					}
					hash.put("oldTeamDate", PublicMethod.getDateToString(oldCp.getCreateDate(), "YY年MM月"));
					oldCpList.add(hash);
				}
			}
			if(cp!=null&&cp.getUTeam()!=null){
				hashMap.put("teamId", cp.getUTeam().getTeamId());
				UTeamDek dek=baseDAO.get("from UTeamDek where UTeam.teamId=:teamId",hashMap);
				if(dek==null){
					dek=new UTeamDek();
				}
				UTeamImg teamImg=baseDAO.get("from UTeamImg where imgSizeType='1' and timgUsingType='1' and UTeam.teamId=:teamId",hashMap);
				UChallenge uch=baseDAO.get("from UChallenge where fteam.teamId=:teamId and challengeStatus='1' and effectiveStatus='1' ",hashMap);
				hashMap.clear();
				hashMap.put("teamName", cp.getUTeam().getName());
				if(teamImg!=null){
					hashMap.put("teamImg", teamImg.getImgurl());
				}else{
					hashMap.put("teamImg", "");
				}
				hashMap.put("rank", cp.getUTeam().getRank());
				hashMap.put("integral", cp.getUTeam().getIntegral());
				
				hashMap.put("teamCount", cp.getUTeam().getTeamCount());
				if(cp.getUTeam().getAvgAge()!=null){
					 BigDecimal bd = new BigDecimal(Double.parseDouble(cp.getUTeam().getAvgAge()));  
					 hashMap.put("avgAge",String.valueOf(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()));  
				}else{
					hashMap.put("avgAge","");  
				}
				if(cp.getUTeam().getAvgWeight()!=null){
					 BigDecimal bd = new BigDecimal(Double.parseDouble(cp.getUTeam().getAvgWeight()));  
					 hashMap.put("avgWeight",String.valueOf(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()));  
				}else{
					hashMap.put("avgWeight","");  
				}
				if(cp.getUTeam().getAvgHeight()!=null){
					 BigDecimal bd = new BigDecimal(Double.parseDouble(cp.getUTeam().getAvgHeight()));  
					 hashMap.put("avgHeight",String.valueOf(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()));  
				}else{
					hashMap.put("avgHeight","");  
				}

				if(uch!=null){
					hashMap.put("x_team_id", "");
					hashMap.put("f_team_id", uch.getFteam().getTeamId());
					if(uch.getXteam()!=null){
						hashMap.put("x_team_id", uch.getXteam().getTeamId());
					}
					hashMap.put("isFight", true);
					hashMap.put("challengeId", uch.getChallengeId());
					hashMap.put("corner", uch.getChRecommendStatus());
				}else{
					hashMap.put("f_team_id", "");
					hashMap.put("x_team_id", "");
					hashMap.put("isFight", false);
					hashMap.put("challengeId", "");
					hashMap.put("corner", "");
				}
				
				hashMap.put("oldCpList", oldCpList);
				hashMap.put("isNowChampion", "1");
				hashMap.put("success", "本球场有擂主");
				hashMap.putAll(rankingListService.getPublicRankingInfo(cp.getUTeam().getTeamId()));
				return hashMap;
			}else{
				hashMap.clear();
				hashMap.put("oldCpList", oldCpList);
				hashMap.put("isNowChampion", "0");
				hashMap.put("success", "本下属球场无擂主");
				return hashMap;
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}

	@Override
	public HashMap<String, Object> getChallengeAgainInfo(HashMap<String, String> map) throws Exception {
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)){
			UChallenge cl=baseDAO.get(UChallenge.class,challengeId);
			hashMap.put("objectId", cl.getUChallengeCh().getKeyId());
			// 球场信息
			StringBuffer selectSql = new StringBuffer(
				"select du.order_id,ubcs.session_id,uci.imgurl,uc.subcourt_id,uc.name,uc.score,ch.challenge_pay_type,date_format(ch.stdate,'%Y-%m-%d') as stdate,ch.sttime,ch.endtime,ch.remark "
						+ "from u_challenge as du left join u_challenge_ch as ch on du.fch_id = ch.key_id "
						+ "left join u_br_court as uc on ch.br_court_id = uc.subcourt_id "
						+ "left join u_br_courtimage as uci on uci.subcourt_id = uc.subcourt_id "
						+ "left join u_br_courtsession as ubcs on du.order_id = ubcs.order_id "
						+ "where uci.img_size_type = '1' and uci.cimg_using_type = '2' and du.challenge_id = :challengeId");
			
			List<UDuelChallengeImg> imgList = baseDAO.find("from UDuelChallengeImg where objectId = :objectId and duelChallUsingType = '1' and imgSaveType = '2'",hashMap);
			List<Object> li = baseDAO.findSQLMap(map, selectSql.toString());
			hashMap.clear();
			hashMap.put("courtList", li);
			hashMap.put("imgList", imgList);
			if(cl!=null&&cl.getFteam()!=null){
				hashMap.put("teamId", cl.getFteam().getTeamId());
			}else{
				hashMap.put("teamId", "");
			}
			return WebPublicMehod.returnRet("ChallengeInfo", hashMap);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}
	
	@Override
	public HashMap<String, Object> getRespAgainInfo(HashMap<String, String> map) throws Exception {
		String challengeId=map.get("challengeId");
		if(challengeId!=null&&!"".equals(challengeId)){
			UChallenge cl=baseDAO.get(UChallenge.class,challengeId);
			// 球场信息
			StringBuffer selectSql = new StringBuffer(
				"select du.order_id,du.order_per_id,er.price,resp.key_id as resp_id,ubcs.session_id,uci.imgurl,uc.subcourt_id,uc.name,uc.score,"
						+ "ch.challenge_pay_type,date_format(ch.stdate,'%Y-%m-%d') as stdate,ch.sttime,ch.endtime,resp.remark, "
						+ "date_format(resp.stdate,'%Y-%m-%d') as resp_date,date_format(resp.sttime,'%T') as resp_time "
						+ "from u_challenge as du left join u_challenge_ch as ch on du.fch_id = ch.key_id "
						+ "left join u_challenge_resp as resp on du.xch_id = resp.key_id "
						+ "left join u_br_court as uc on ch.br_court_id = uc.subcourt_id "
						+ "left join u_br_courtimage as uci on uci.subcourt_id = uc.subcourt_id "
						+ "left join u_br_courtsession as ubcs on du.order_id = ubcs.order_id "
						+ "left join u_order as er on du.order_per_id = er.order_id "
						+ "where uci.img_size_type = '1' and uci.cimg_using_type = '2' and du.challenge_id = :challengeId");
			
			List<UDuelChallengeImg> imgList = baseDAO.find(map,"from UDuelChallengeImg where objectId = :challengeId and duelChallUsingType = '2' and imgSaveType = '2'");
			List<Object> li = baseDAO.findSQLMap(map, selectSql.toString());
			hashMap.clear();
			hashMap.put("courtList", li);
			hashMap.put("imgList", imgList);
			if(cl!=null&&cl.getXteam()!=null){
				hashMap.put("teamId", cl.getXteam().getTeamId());
			}else{
				hashMap.put("teamId", "");
			}
			return WebPublicMehod.returnRet("ChallengeInfo", hashMap);
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}

	@Override
	public HashMap<String, Object> getChallengeOtherInfo(HashMap<String, String> map) throws Exception {
		List<UDuelChallengeImg> fimgList=null;
		List<UDuelChallengeImg> ximgList=null;
		
		UChallenge ucp=baseDAO.get(UChallenge.class,map.get("challengeId"));
		//String type=checkRespChallenge(map,fteamId,xteamId);
		hashMap.put("remark", "");
		map.put("objectId", "");
		map.put("duelChallUsingType", "");
		map.put("fobjectId", "");
		map.put("fduelChallUsingType", "");
		if(ucp!=null){
		//if(type.equals("-1")||type.equals("-4")){
			if(ucp.getUChallengeResp()!=null){
				map.put("objectId", ucp.getUChallengeResp().getKeyId());
			}
			map.put("duelChallUsingType", "2");
			if(ucp!=null&&ucp.getUChallengeResp()!=null){
				hashMap.put("xremark", ucp.getUChallengeResp().getRemark());
			}
		//}else if(type.equals("-2")||type.equals("-5")){
			if(ucp.getUChallengeCh()!=null){
				map.put("fobjectId", ucp.getUChallengeCh().getKeyId());
			}
			map.put("fduelChallUsingType", "1");
			if(ucp!=null&&ucp.getUChallengeCh()!=null){
				hashMap.put("remark", ucp.getUChallengeCh().getRemark());
			}
		//}
		}
		ximgList=baseDAO.find(map,"from UDuelChallengeImg where objectId = :objectId and duelChallUsingType = :duelChallUsingType and imgSaveType = '2'");
		fimgList=baseDAO.find(map,"from UDuelChallengeImg where objectId = :fobjectId and duelChallUsingType = :fduelChallUsingType and imgSaveType = '2'");
		hashMap.put("imgList", fimgList);
		hashMap.put("ximgList", ximgList);
		return hashMap;
	}

	/**
	 * 查询是否已经发起过有效挑战
	 * @param teamId 球队id
	 * @return  true 可以发起
	 * 			false 不可发起
	 * @throws Exception 
	 */
	private boolean selEffective (String teamId) throws Exception{
		hashMap.put("teamId", teamId);
		int count=baseDAO.count("select count(challengeId) from UChallenge where fteam.teamId=:teamId and effectiveStatus='1' and (challengeStatus='1' or challengeStatus='5')",hashMap,true);
		if(count>0){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 查询是否已经发起过有效挑战(球场下用)
	 * @param teamId 球队id
	 * @return  true 可以发起
	 * 			false 不可发起
	 * @throws Exception 
	 */
	private boolean selEffectiveCourt (String teamId,String brCourtId) throws Exception{
		hashMap.put("teamId", teamId);
		hashMap.put("brCourtId", brCourtId);
		int count=baseDAO.count("select count(challengeId) from UChallenge where fteam.teamId=:teamId and UChallengeCh.UBrCourt.subcourtId=:brCourtId and effectiveStatus='1' and (challengeStatus='1' or challengeStatus='5')",hashMap,true);
		if(count>0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public List<HashMap<String, Object>> getWithCourtInfo(List<HashMap> sessionList) throws Exception {
		HashMap<String,Object> hash=new HashMap<String,Object>();
		int index=0;
		String sql="";
		for(HashMap<String, Object> maptemp:sessionList){
			hash.put("sessionId"+index, maptemp.get("session_id"));
			sql +=" select uch.wincount,u.f_team_id,u.x_team_id,u.challenge_status as status,u.challenge_id as champid,ur.allprice/2 as responseprice,"+
					" (select name from u_team ut where ut.team_id=u.f_team_id ) as home_teamname,"+
					" (select short_name from u_team ut where ut.team_id=u.f_team_id ) as home_short_teamname,"+
					" (select imgurl from u_team_img ti where ti.team_id=u.f_team_id and ti.timg_using_type='1' and ti.img_size_type='1') as home_teamimgurl,"+
					" (select name from u_team ut where ut.team_id=u.x_team_id ) as away_teamname,"+
					" (select short_name from u_team ut where ut.team_id=u.x_team_id ) as away_short_teamname,"+
					" (select imgurl from u_team_img ti where ti.team_id=u.x_team_id and ti.timg_using_type='1' and ti.img_size_type='1') as away_teamimgurl"+
					" from u_br_courtsession ub "+
					" left join u_challenge u on u.order_id=ub.order_id "+
					" left join u_order ur on u.order_id=ur.order_id "+
					" left join u_challenge_ch uch on uch.key_id=u.fch_id "+
					" where ub.session_id=:sessionId"+index+" union all";
			index++;
		}
		if(sql.length()>0){
			sql=sql.substring(0, sql.length()-9);
		}
		List<HashMap<String, Object>> li =  baseDAO.findSQLMap(sql,hash);
		return  li;
	}

	@Override
	public boolean returnOrderStatus(String orderId) throws Exception {
		if(orderId!=null&&!"".equals(orderId)){
			hashMap.put("orderId", orderId);
			List<UChallenge> list=baseDAO.find("from UChallenge where effectiveStatus='1' and challengeStatus='2' and uorder.orderId=:orderId",hashMap);//有效且已成功
			if(list.size()>0){
				return true;//这笔订单已达成则可新下订单
			}else{
				return false;//数据库中如没有找到此笔订单的使用情况 则认为不可发起第二笔挑战订单
			}
		}else{
			return true;
		}
	}

	/**
	 * 统一处理游客模式的userid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getUserId(HashMap<String, String> map) throws Exception{
		UUser user=uuserService.getUserinfoByUserId(map);
		if(user!=null){
			return user.getUserId();
		}else{
			return "";
		}
		
	}

	/**
	 * 统一处理球队
	 * @param map
	 * @return
	 * @throws Exception
	 */
	/*public UTeam getThisTeam(HashMap<String, String> map) throws Exception{
		if(uteamService.getUteaminfoByUserId(map).get("uTeam")!=null){
			UTeam uteam=(UTeam) uteamService.getUteaminfoByUserId(map).get("uTeam");
			return uteam;
		}else{
			UTeam uteam=new UTeam();
			return uteam;
		}
	}*/
	
	/**
	 * 
	 * 
	   TODO - 验证约战是否是自己发起的
	   @param hashMap
	   @return 1-是 -1 否
	   2016年4月22日
	   mazkc
	 */
	private String checkTeamIsMy(HashMap<String, String> hashMap,List<HashMap<String,Object>> arrayTeamList){
		long count = 0;
		String fteamid = hashMap.get("f_team_id"); //发起方队伍ID
		String xteamid = hashMap.get("x_team_id"); //响应放队伍ID
		if(null != arrayTeamList && arrayTeamList.size() > 0){
			count = arrayTeamList.stream().filter(v -> v.get("team_id").equals(fteamid) || v.get("team_id").equals(xteamid)).count();
			if(count > 0){
				return "1";
			}
		}
		return "-1";
	}

	@Override
	public void updateChallengeByTimeOut() throws Exception {
		String challengeId="";//lbs需要删除的挑战id
		//创建临时表
		String sql1 = "CREATE TEMPORARY TABLE tmp_c as select u.challenge_id from u_challenge_ch as ch left join u_challenge as u on ch.key_id = u.fch_id where DATE_FORMAT(CONCAT(ch.stdate,' ',ch.sttime),'%Y-%c-%d %H:%i:%s') < now() ";
		//更新状态
		String sql2 = "update u_challenge set challenge_status = '3',effective_status = '2' where challenge_id in (select challenge_id from tmp_c) and challenge_status = '1'";
		//删除临时表
		String sql3 = "drop table tmp_c";
		
		//定时器修改百度lbs标志位
		String lbsSql="select u.challenge_id from u_challenge_ch as ch left join u_challenge as u on ch.key_id = u.fch_id where DATE_FORMAT(CONCAT(ch.stdate,' ',ch.sttime),'%Y-%c-%d %H:%i:%s') < now() ";
		List<HashMap<String,Object>> arrayTeamList=baseDAO.findSQLMap(lbsSql);
		for(HashMap<String,Object> map:arrayTeamList){
			challengeId=(String) map.get("challengeId");
			if(!delChallengeLbs(challengeId)){//删除原有lbs数据
				lBSService.createUbaidulbsDataError(challengeId, "5", "app端调用定时器接口删除挑战lbs数据异常");
			}
		}
		baseDAO.executeSql(sql1);
		baseDAO.executeSql(sql2);
		baseDAO.executeSql(sql3);
	}

	@Override
	public HashMap<String, Object> checkChallengeWithTeam(HashMap<String, String> map) throws Exception {
		String teamId=map.get("teamId");
		if(teamId!=null&&!"".equals(teamId)){
			List<UChampion> uchList=baseDAO.find(map,"from UChampion where UTeam.teamId=:teamId and isChampion='1' ");//防止错误数据出现查询list
			if(uchList!=null&&uchList.size()>0){
				UChallenge cl=baseDAO.get(map, "from UChallenge where fteam.teamId=:teamId and effectiveStatus='1' and (challengeStatus='1' or challengeStatus='5') ");
				if(cl!=null){
					hashMap.clear();
					if(uchList.get(0).getUTeam()!=null){
						hashMap.put("f_team_id", uchList.get(0).getUTeam().getTeamId());
					}else{
						return WebPublicMehod.returnRet("error", "-50_擂主球队数据异常");
					}
					if(uchList.get(0).getUBrCourt()!=null){
						hashMap.put("brCourtId", uchList.get(0).getUBrCourt().getSubcourtId());
					}else{
						return WebPublicMehod.returnRet("error", "-50_擂主下属球场数据异常");
					}
					if(cl.getXteam()!=null){
						hashMap.put("x_team_id", cl.getXteam().getTeamId());
					}
					hashMap.put("challenge_status", cl.getChallengeStatus());
					hashMap.put("challenge_id", cl.getChallengeId());
					hashMap.put("success", "可以跳转详细页");
					return hashMap;
				}else{
					return WebPublicMehod.returnRet("error", "-50_此擂主当前没有发起擂主战");
				}
			}else{
				return WebPublicMehod.returnRet("error", "-50_此球队当前不是擂主");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}
	
	/**
	 * 判断app版本
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Object> checkAppCode (HashMap<String, String> map) throws Exception {
		int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));
		if(intAppCode<202){
			return WebPublicMehod.returnRet("error", "当前版本过低无法使用此功能，请先升级版本！");
		}else{
			return null;
		}
	}
	


	/**
	 * 上报百度lbs挑战记录 包括新增和修改
	 * @param ucl 挑战对象
	 * @param saveType 1 新增lbs 2 修改lbs
	 * @return boolean true 上报成功 false 上报失败 			
	 */
	private boolean saveOrUpdateChallengeLbs(UChallenge ucl,String saveType) {
		UCourt court=null;
		String bdLbsId=null;
		Map<String,Object> map = null;
		if(ucl!=null){
			try{//捕捉lbs上传异常错位,进行日志表保存,后续手动补录
				if(ucl.getUChallengeCh()!=null&&ucl.getUChallengeCh().getUBrCourt()!=null){
					court=ucl.getUChallengeCh().getUBrCourt().getUCourt();
				}
				int clIntId= this.getEntityIntId("select challenge_id_int as intId from u_challenge where challenge_id = '"+ucl.getChallengeId()+"'");
				int challegeStatus=Integer.parseInt(ucl.getChallengeStatus());//挑战状态
				String challegeid=ucl.getChallengeId();
				if(court!=null&&challegeid!=null){
					if(saveType.equals("1")){//新增lbs
						map = PublicMethod.parseJSON2Map(lBSService.createGeodata(this.setChallengeLbsMap(court, clIntId, challegeStatus, challegeid, "挑战数据")));
					}else if(saveType.equals("2")){//修改lbs
						HashMap<String,Object> data=this.setChallengeLbsMap(court, clIntId, challegeStatus, challegeid, "挑战数据");
						UBaidulbs temp= baseDAO.get("from UBaidulbs where objectId = '"+challegeid+"' and lbsTable = '"+Constant.BAIDU_CHALLENGE_TABLE_ID+"'");
						if(temp!=null){
							data.put("id", temp.getLbsData());
							map = PublicMethod.parseJSON2Map(lBSService.updateGeodata(data));
						}else{
							return false;
						}
					}
					if(map!=null){
						Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
						if(retMap!=null){//0代表 百度lbs端成功记录
							int status=(int)retMap.get("status");
							if(status==0){
								bdLbsId=retMap.get("id").toString();
								if(saveType.equals("1")){
									lBSService.createUbaidulbsData(challegeid, "5", bdLbsId);
								}
								return true;
							}else{
								return false;
							}
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}catch(Exception ex){
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 删除百度lbs数据 以及本地lbs数据
	 * @param challegeid
	 * @return
	 */
	private boolean delChallengeLbs(String challegeid) {
		try{//捕捉lbs上传异常错位,进行日志表保存,后续手动补录
			HashMap<String,Object> data=new HashMap<String,Object>();
			UBaidulbs temp= baseDAO.get("from UBaidulbs where objectId = '"+challegeid+"' and lbsTable = '"+Constant.BAIDU_CHALLENGE_TABLE_ID+"'");
			if(temp!=null){
				data.put("id", temp.getLbsData());
				data.put("url",Public_Cache.LBS_LOCATION);
				data.put("geotable_id", Constant.BAIDU_CHALLENGE_TABLE_ID);
				Map<String,Object> map = PublicMethod.parseJSON2Map(lBSService.deleteDateByID(data));
				if(map!=null){
					Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
					if(retMap!=null){
						int status=(int)retMap.get("status");
						if(status==0){
							data.put("objectId", challegeid);
							lBSService.delUbaidulbsData(data);
							return true;
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception ex){
			return false;
		}
	}
	/**
	 * 封装百度挑战lbs数据
	 * @param court
	 * @param clIntId
	 * @param challegeStatus
	 * @param challegeid
	 * @param title
	 * @return
	 * @throws Exception
	 */
	private HashMap<String,Object> setChallengeLbsMap(UCourt court,int clIntId,int challegeStatus,String challegeid,String title) throws Exception  {
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("address",court.getAddress());
		hashMap.put("url",Public_Cache.LBS_LOCATION);
		hashMap.put("title", title);
		hashMap.put("latitude",court.getBdPoi());
		hashMap.put("longitude",court.getBdPosition());
		hashMap.put("coord_type","3");
		hashMap.put("geotable_id", Constant.BAIDU_CHALLENGE_TABLE_ID);
		hashMap.put("areaid",court.getArea());
		hashMap.put("cl_intid",clIntId);
		hashMap.put("challegeid",challegeid);
		hashMap.put("challege_status",challegeStatus);
		hashMap.put("tags","challenge");
		hashMap.put("date",PublicMethod.getDateToString(new Date(),"yyyy-MM-dd"));
		return hashMap;
	}
	

	
	//获取表中对应的int ID字段,sql中对应 intId
	private int getEntityIntId(String sql) throws Exception {
		List<Map<String,Object>> list = this.baseDAO.findSQLMap(sql);
		if(CollectionUtils.isNotEmpty(list)){
			return Integer.parseInt(list.get(0).get("intId").toString());
		}
		return 0;
	}
}
