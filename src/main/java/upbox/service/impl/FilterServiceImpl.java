package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;

import net.sf.json.JSONArray;
import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.BdLbsBean;
import upbox.model.CornerBean;
import upbox.model.PageLimit;
import upbox.model.UConfiguration;
import upbox.model.UPlayer;
import upbox.model.URegion;
import upbox.outModel.OutPlayerList;
import upbox.outModel.OutUteamList;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.service.CornerService;
import upbox.service.FilterService;
import upbox.service.LBSService;
import upbox.service.PublicService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.util.YHDCollectionUtils;

/**
 * 筛选接口实现
 * 
 * @author xiao
 *
 */
@Service("filterService")
public class FilterServiceImpl implements FilterService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UTeamService uteamService;
	@Resource
	private UPlayerService uplayerService;
	@Resource
	private PublicService publicService;
	@Resource
	private CornerService cornerService;
	@Resource
	private RedisOperDAOImpl redisDao;
	@Resource
	private UPlayerRoleService uPlayerRoleService;
	@Resource
	private LBSService lBSService;
	
	HashMap<String, Object> hashMap=new HashMap<>();
	int count = 0;

	@Override
	public HashMap<String, Object> findFilterTeam(HashMap<String, String> map) throws Exception {
		HashMap<String,List<Object>> mapList=new HashMap<>();
		List<Object> listStr=null;
		if (map.get("loginUserId") == null) {
			map.put("loginUserId", "");
		}
		hashMap = PublicMethod.Maps_Mapo(map);
		StringBuffer sql = new StringBuffer("select t.team_id_int teamIdInt,t.team_id teamId,t.name name,t.team_class teamClass,");
		sql.append("concat(cast(t.avg_age as decimal(18,1)),'') avgAge,");
		sql.append("concat(cast(t.avg_height as decimal(18,1)),'') avgHeight,");
		sql.append("concat(cast(t.avg_weight as decimal(18,1)),'') avgWeight,");
		sql.append("concat(format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,1),'') chances,");
		sql.append("t.remark remark, t.team_count teamCount,i.imgurl imgurl,p.player_id playerId,");
		sql.append("p.number,p.position,(d.all_match_count+l.all_match_count) allMatchCount,t.integral,t.rank,");
		sql.append("(d.ver_match_count+l.ver_match_count) verMatchCount,(d.draw_match_count+l.draw_match_count) drawMatchCount,");
		sql.append("(d.fail_match_count+l.fail_match_count) failMatchCount from u_team t");
		sql.append(" left join u_team_dek d on t.team_id=d.team_id ");
		sql.append(" left join u_team_duel l on t.team_id= l.team_id");
		sql.append(" left join u_team_match m on t.team_id= m.team_id ");
		sql.append(" left join u_team_img i on  t.team_id=i.team_id and i.timg_using_type='1' and i.img_size_type='1'");
		sql.append(" left join u_player p on p.team_id=t.team_id ");
		if(map.get("loginUserId")!=null&&!"".equals(map.get("loginUserId"))){
			sql.append(" and p.user_id=:loginUserId ");
		}
		//默认排除条件
		this.teamInfoRemove(sql,map);
		
		// 胜率
		this.filterInfoOption(map, count, sql, map.get("chances"),
				"format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,0) ");
		// 排名－特殊处理未进入，不能使用公共方法
		if (null != map.get("rank") && !"-1".equals(map.get("rank")) && !"".equals(map.get("rank"))) {
			String[] rankList = map.get("rank").split(",");
			if (rankList.length > 0) {
				count = 0;
				for (String string : rankList) {
					count++;
					if (count == 1) {
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (!"0".equals(string)) {
						map.put("orderByRank", "rank");
						this.filterInfoOptionPart(map,sql,string,"t.rank ");
					} else {
						sql.append("(t.rank is null ) ");
					}
					if (count >= rankList.length) {
						sql.append(" ) ");
					}

				}
			}
		}
		// 平均年龄
		this.filterInfoOption(map, count, sql, map.get("avgAge"), "format(t.avg_age,0) ");
		// 平均身高
		this.filterInfoOption(map, count, sql, map.get("avgHeight"), "format(t.avg_height,0) ");
		// 平均体重
		this.filterInfoOption(map, count, sql, map.get("avgWeight"), "format(t.avg_weight,0) ");
		// 球队人数
		this.filterInfoOption(map, count, sql, map.get("teamCount"), "t.team_count ");
		// 地区
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			hashMap.put("area", map.get("area"));
			URegion region = baseDAO.get(URegion.class, map.get("area"));
			if (region != null && "2".equals(region.getType())) {//全部
				sql.append(" and t.area in (select _id from u_region where parent=:area ) ");
			} else {
				String area = map.get("area");
				String[] strs = area.split(",");
				listStr = new ArrayList<>();
				for (int i = 0; i < strs.length; i++) {
					listStr.add(strs[i]);
				}
				map.remove("area");
				hashMap.remove("area");
				mapList.put("area", listStr);
				sql.append(" and t.area in (:area ) ");
			}
		}
		// 擂主
		if (null != map.get("champion")&&!"-1".equals(map.get("champion"))&&!"".equals(map.get("champion"))) {
			if ("1".equals(map.get("champion"))) {// 是擂主
				sql.append(" and t.team_id in(select team_id from u_champion where is_champion='1') ");
			} else {// 不是擂主
				sql.append(" and t.team_id not in(select team_id from u_champion where is_champion='1') ");
			}
		}
		// 加入我们应用时间
		if (null != map.get("createdate")&&!"-1".equals(map.get("createdate"))&&!"".equals(map.get("createdate"))) {
			// 今天
			if ("1".equals(map.get("createdate"))) {
				sql.append("and t.createdate = date_format(now(),'%Y-%m-%d')");
			}
			// 7天内
			if ("2".equals(map.get("createdate"))) {
				sql.append("and t.createdate>date_sub(date_format(now(),'%Y-%m-%d'), interval "
						+ Public_Cache.FILTER_TEAM_TIME + " day)");
			}
			// 30天内
			if ("3".equals(map.get("createdate"))) {
				sql.append("and t.createdate>date_sub(date_format(now(),'%Y-%m-%d'), interval "
						+ Public_Cache.FILTER_TEAM_TIMES + " day)");
			}
		}
		
		this.teamOrderByInfo(map,sql,mapList);// 分组+排序
		
		//分页
		hashMap.put("lbsScreenInSql","team");//标示
		List<HashMap<String, Object>> listTeam = this.getPageLimit(sql, hashMap, map,mapList);
		List<Object> listTeamId = new ArrayList<>();
		List<List<CornerBean>> cornerList = new ArrayList<>();
		if (null != listTeam && listTeam.size() > 0) {
			for (HashMap<String, Object> hashMap2 : listTeam) {
				String teamId = hashMap2.get("teamId").toString();
				listTeamId.add(teamId);
				uteamService.displayData(hashMap2,map);//数据处理
//				//填充角色
				if(hashMap2.get("playerId")!=null&&!"".equals(hashMap2.get("playerId"))){
					uPlayerRoleService.setMembertype202(hashMap2);
				}
			}
		}
		if (null != listTeamId && listTeamId.size() > 0) {
			cornerList = cornerService.getAllTeamCornerList(map, listTeamId);
		}
		// 站队列表用户信息
		hashMap = new HashMap<>();
		// 站队列表用户信息
		OutUteamList outUteamList = uteamService.findUteaminfoListHead(map);
		hashMap.put("outUteamList", outUteamList);
		hashMap.put("outUteamLists", listTeam);
		hashMap.put("cornerList", cornerList);
		return hashMap;
	}
	
	/**
	 * 筛选共同
	 * @param map －－扩展用
	 * @param count 初始化数据
	 * @param sql 主SQL
	 * @param strFilterOption 筛选项
	 * @param strSql 筛选项的SQL
	 * xiaoying 2016年6月17日
	 */
	private void filterInfoOption(HashMap<String, String> map, int count, StringBuffer sql,String strFilterOption,String strSql) {
		// 胜率 如果胜率chances没有值就跟着条件查询，否者就查询全部(等于不加条件)
		if (null != strFilterOption && !"-1".equals(strFilterOption) && !"".equals(strFilterOption)) {
			String[] chancesList = strFilterOption.split(",");
			if (chancesList.length > 0) {
				for (String string : chancesList) {
					count++;
					if (count == 1) { // 控制并且关系 （ 胜率和排名 and关系 胜率和胜率 or关系）
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (null != string.split("-")[0] && !"".equals(string.split("-")[0])) {
						hashMap.put("startValue", Integer.parseInt(string.split("-")[0]));
						if (string.split("-").length >= 2) {// 正常区间
							hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
							sql.append("( " + strSql + " >= " + hashMap.get("startValue"));
							sql.append(" and " + strSql + " <= " + hashMap.get("endValue") + ") ");
						} else {// 以上
							sql.append("( " + strSql + " > " + hashMap.get("startValue") + ") ");
						}
					} else {// 以下
						if (null != string.split("-")[1] && !"".equals(string.split("-")[1])) {
								hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
								sql.append("( " + strSql + " < " + hashMap.get("endValue") + ") ");
						}
					}
					if (count >= chancesList.length) {
						sql.append(" ) ");
					}
				}
			}
		}
	}
	/**
	 * 默认排除数据
	 * @param sql
	 * @param map lvlParent－二级城市ID，duelFilter－约战标示
	 */
	private void teamInfoRemove(StringBuffer sql, HashMap<String, String> map) throws Exception{
		sql.append(" where team_status='3' and team_use_status='2'  ");
		if(!"rank".equals(map.get("orderByRank"))){//天梯不排除满员
			sql.append(" and t.team_id not in(select team_id from u_team where team_count>50)");//排除满员
		}
		//排除定位的城市
//		if(map.get("area")!=null&&!"".equals(map.get("area"))&&!"rank".equals(map.get("orderByRank"))){//如果有二级城市，那么排除不是该城市的球队
//			URegion region = baseDAO.get(URegion.class, map.get("area"));
//			hashMap.put("lvlParent", map.get("area"));
//			if(region!=null&&"2".equals(region.getType())){
//				sql.append(" and t.area not in (select _id from u_region where type='3' and parent=:lvlParent ) ");
//			}
//			if(region!=null&&"3".equals(region.getType())){
//				hashMap.put("lvlParent", region.getParent());
//				sql.append(" and t.area not in (select _id from u_region where type='3' and parent=:lvlParent ) ");
//			}
//		}
		//约战
		if(map.get("duelFilter")!=null&&"duel".equals(map.get("duelFilter"))){
			sql.append("and t.team_count>=7 and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:loginUserId and pp.team_id is not null and pp.in_team='1' and pp.member_type='1') ");
		}else{ 
			if(!"rank".equals(map.get("orderByRank"))){//天梯不排除自己的球队
			sql.append(" and t.team_id not in (select pp.team_id from u_player pp where pp.user_id=:loginUserId  and pp.team_id is not null and pp.in_team='1' ) ");//排除加入队伍和自己队
			}
		}
	}
	/**
	 * 球队排序
	 * @param map
	 * @param sql
	 */
	private void teamOrderByInfo(HashMap<String, String> map, StringBuffer sql,HashMap<String, List<Object>> mapList) throws Exception {
//		String locationSql= "";
//		sql.append(" group by t.team_id order by ");// 分组
		//排序
		if(map.get("orderByTeam")!=null&&!"".equals(map.get("orderByTeam"))){
			//加入时间
			if("1".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			}
			//离我最近
			if("2".equals(map.get("orderByTeam"))){
//				sql.append(" and t.area is not null ");
//				hashMap.put("act", " group by t.team_id ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			}
			//球队活跃
			if("3".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" t.team_id ");
			}
			//激数排名
			if("4".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,t.team_id ");
			}
			//地区
			if("5".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" t.area desc,t.team_id ");
			}
			//胜率
			if("6".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" cast(format(if((ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))>0,((ifnull(d.ver_match_count,0)+ifnull(l.ver_match_count,0)+ifnull(m.ver_match_count,0))/(ifnull(d.all_match_count,0)+ifnull(l.all_match_count,0)+ifnull(m.all_match_count,0))),0)*100,1)as signed) desc,t.team_id ");
			}
			//平均年龄
			if("7".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" t.avg_age+0 desc,t.team_id ");
			}
			//人数
			if("8".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" t.team_count+0 desc,t.team_id ");
			}
			//是否是擂主
			if("9".equals(map.get("orderByTeam"))){
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" t.team_id in(select team_id from u_champion where is_champion='1') desc,t.team_id ");
			}
		}else{
			// 如果有排名就按排名排序或者天梯筛选列表排序
			if (map.get("orderByRank") != null && "rank".equals(map.get("orderByRank"))) {
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			} else if (map.get("orderByRank") != null && "fduel".equals(map.get("orderByRank"))) {//发起约战－选择对手 离我最近
//				sql.append(" and t.area is not null ");
//				hashMap.put("act", " group by t.team_id ");
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,t.team_id ");
			} else{
				sql.append(" group by t.team_id order by ");// 分组
				sql.append(" case when ifnull(t.createdate,'')='' then 0 else 1 end desc,t.createdate desc,t.createtime desc,case when ifnull(rank,'')='' then 0 else 1 end desc, rank asc,t.team_id  ");
			}
		}
	}
	
	/**
	 * 检索中心点多少公里下信息
	 * @param map location－百度经纬度
	 * @param tags  team－球队，user-用户（球员）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月23日
	 */
	private String lbsOrderSql (HashMap<String, String> map,String tags) throws Exception{
		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
		String orderSql="";
		String lbsBack="";
		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
		lbsMap.put("location", map.get("location"));//百度经纬度
		lbsMap.put("coord_type", "3");//百度坐标系
		lbsMap.put("radius", "500000");//半径范围
		lbsMap.put("tags", tags);//搜索球场
		lbsMap.put("sortby", "distance:1");//距离升序
		lbsMap.put("url", Public_Cache.LBS_LOCATION);
		lbsBack=lBSService.getNearBy(lbsMap);
		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);
		for(BdLbsBean lbstemp:lbsList){
			orderSql+="'"+lbstemp.getObject_id()+"',";
		}
		//如果没有球场排序就使用默认排序
		if(orderSql.length()>0){
			orderSql=orderSql.substring(0, orderSql.length()-1);
		}
		return orderSql;
	}
	

	@Override
	public HashMap<String, Object> findFilterPlayer(HashMap<String, String> map) throws Exception {
		HashMap<String,List<Object>> mapList=new HashMap<>();
		List<Object> listStr=null;
		hashMap = PublicMethod.Maps_Mapo(map);
		StringBuffer sql = new StringBuffer("select u.user_id_int userIdInt,u.user_id userId,p.player_id  playerId,u.sex sex,u.nickname nickname,u.realname realname,");
		sql.append("(year(now())-year(u.birthday)-1) + ( DATE_FORMAT(u.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) age,u.height,");
		sql.append( "u.weight,u.birthday birthday,u.remark remark,p.expert_position expertPosition,p.practice_foot practiceFoot,");
		sql.append("p.can_position canPosition,p.member_type memberType,p.position position,p.number number,uui.imgurl imgurl ");
		sql.append(" from u_player p left join u_user u on p.user_id = u.user_id");
		sql.append(" left join u_user_img uui on uui.user_id=p.user_id and uui.uimg_using_type='1' ");
		
		this.playerInfoRemove(sql,map);//排除条件
		
		// 擅长位子
		if (null != map.get("expertPosition") && !"-1".equals(map.get("expertPosition"))
				&& !"".equals(map.get("expertPosition"))) {
			String expertPosition = map.get("expertPosition");
			String[] strs = expertPosition.split(",");
			listStr = new ArrayList<>();
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			mapList.put("position", listStr);
			sql.append(" and expert_position in (:position ) ");
		}

		// 年龄  取出生年月算年龄
		this.filterInfoOption(map, count, sql, map.get("age"), "(year(now())-year(u.birthday)-1) + ( DATE_FORMAT(u.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) ");
		
		// 身高
		this.filterInfoOption(map, count, sql, map.get("height"), "u.height ");

		// 体重
		this.filterInfoOption(map, count, sql, map.get("weight"), "u.weight ");

		// 加入球队数-特殊处理未加入球队、加入一支球队
		if (null != map.get("count") && !"-1".equals(map.get("count")) && !"".equals(map.get("count"))) {
			String[] countList = map.get("count").split(",");
			if (countList.length > 0) {
				count = 0;
				for (String string : countList) {
					count++;
					if (count == 1) {
						sql.append(" and (");
					} else {
						sql.append(" or ");
					}
					if (!"0".equals(string) && !"1".equals(string)) {// 没有加入过队伍
						this.filterInfoOptionPart(map,sql,string,"(select count(p1.user_id) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ) ");
					} else {
						sql.append("(select count(p1.user_id) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ) = "+string);
					}
					if (count >= countList.length) {

						sql.append(" ) ");
					}

				}
			}
		}
		// 地区
		if (null != map.get("area") && !"".equals(map.get("area"))) {
			hashMap.put("area", map.get("area"));
			URegion region = baseDAO.get(URegion.class, map.get("area"));
			if (region != null && "2".equals(region.getType())) {//全部选项
				sql.append(" and u.area in (select _id from u_region where parent=:area ) ");
			} else {
				String area = map.get("area");
				String[] strs = area.split(",");
				listStr = new ArrayList<>();
				for (int i = 0; i < strs.length; i++) {
					listStr.add(strs[i]);
				}
				map.remove("area");
				hashMap.remove("area");
				mapList.put("area", listStr);
				sql.append(" and u.area in (:area ) ");
			}
		}
		// 球队归属
		if (null != map.get("teamBelonging") && !"-1".equals(map.get("teamBelonging"))
				&& !"".equals(map.get("teamBelonging"))) {
			String teamBelonging = map.get("teamBelonging");
			String[] strs = teamBelonging.split(",");
			listStr = new ArrayList<>();
			for (int i = 0; i < strs.length; i++) {
				listStr.add(strs[i]);
			}
			mapList.put("belonging", listStr);
			sql.append(" and team_belonging in (:belonging) ");

		}
		//排序 分组
		this.playerOrderByInfo(map,sql);
		
		// 分页
		hashMap.put("lbsScreenInSql","user");
		List<HashMap<String, Object>> listPlayer = this.getPageLimit(sql, hashMap, map,mapList);
		//角标
		List<Object> listPlayerid = new ArrayList<>();
		List<List<CornerBean>> cornerList = new ArrayList<>();
		OutPlayerList outPlayerList = new OutPlayerList();
		if(map.get("loginUserId")!=null&&!"".equals(map.get("loginUserId"))){
			if (null != listPlayer && listPlayer.size() > 0) {
				for (HashMap<String, Object> hashMap2 : listPlayer) {
					String playerId = hashMap2.get("playerId").toString();
					listPlayerid.add(playerId);
					uplayerService.displayData(hashMap2,map);//数据处理
				}
			}
			if (null != listPlayer && listPlayer.size() > 0) {
				cornerList = cornerService.getPlayerCorner(map, listPlayerid);
			}
		}
		
		UPlayer uPlayer = uplayerService.getUplayerByUserId(map);
		if (null != uPlayer) {
			outPlayerList = uplayerService.setOutPlayerList(null,uPlayer,map);
			//填充球员的身份
			outPlayerList = uPlayerRoleService.setMemberTypeByGetUplayerinfo202(outPlayerList,uPlayer,map);
		}
		
		hashMap = new HashMap<>();
		
		hashMap.put("cornerList", cornerList);//角标
		hashMap.put("outPlayerList", outPlayerList);
		hashMap.put("outPlayerLists", listPlayer);// 球员筛选
		return hashMap;
	}
	/**
	 * 筛选共同-部分
	 * @param map －－扩展用
	 * @param sql 主SQL
	 * @param strFilterOption 筛选项
	 * @param strSql 筛选项的SQL
	 * xiaoying 2016年6月17日
	 */
	private void filterInfoOptionPart(HashMap<String, String> map, StringBuffer sql, String string, String strSql) {
		if (null != string.split("-")[0] && !"".equals(string.split("-")[0])) {
			hashMap.put("startValue", Integer.parseInt(string.split("-")[0]));
			if (string.split("-").length >= 2) {// 正常区间
				hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
				sql.append("( " + strSql + " >= " + hashMap.get("startValue"));
				sql.append(" and " + strSql + " <=" + hashMap.get("endValue") + ") ");
			} else {// 以上
				sql.append("( " + strSql + " > " + hashMap.get("startValue") + ") ");
			}
		} else {// 以下
			if (null != string.split("-")[1] && !"".equals(string.split("-")[1])) {
					hashMap.put("endValue", Integer.parseInt(string.split("-")[1]));
					sql.append("( " + strSql + " < " + hashMap.get("endValue") + ") ");
			}
		}
		
	}
	/**
	 * 球员数据排除下项
	 * @param sql 主SQL
	 * @param map teamId－球队ID，type－标示
	 * @throws Exception
	 * xiaoying 2016年6月17日
	 */
	private void playerInfoRemove(StringBuffer sql, HashMap<String, String> map) throws Exception{
		String sql1=" where p.team_id is null and u.user_id is not null  ";
		if (publicService.StringUtil(map.get("teamId"))) {
			if ("1".equals(map.get("type"))) {//1:代表邀请人的球员列表
				sql1= " where p.user_id not in (select pp.user_id from u_player pp where pp.team_id=:teamId and pp.in_team='1') and u.user_id is not null and p.team_id is null ";//排除自己加入过的球队
			}else if ("2".equals(map.get("type"))){//2:代表管理球员的球员列表
				sql1= "  where p.team_id=:teamId and p.in_team='1' ";
			}
		}
		sql.append(sql1);
		
	}
	
	/**
	 * 球员筛选排序
	 * @param map
	 * @param sql
	 * @param groupSql 
	 */
	private void playerOrderByInfo(HashMap<String, String> map,StringBuffer sql) throws Exception{
		//排序
		if (map.get("orderByPlayer") != null && !"".equals(map.get("orderByPlayer"))) {
			// 加入时间
			if ("1".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}
			// 离我最近
			if ("2".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
//				sql.append(" and u.area is not null ");
//				hashMap.put("act", " group by p.user_id ");
			}
			// 球队活跃
			if ("3".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				 sql.append(" p.user_id asc");
			}
			// 积分排名
			if ("4".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" p.user_id asc");
			}
			// 地区
			if ("5".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" u.area desc,p.user_id asc ");
			}
			// 擅长位置
			if ("6".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" expert_position ,p.user_id asc ");
			}
			// 年龄
			if ("7".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" u.age+0 desc,p.user_id asc ");
			}
			// 加入球队数
			if ("8".equals(map.get("orderByPlayer"))) {
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" (select count(*) from u_player p1 where p1.user_id=p.user_id and p1.in_team ='1' ),p.user_id asc ");
			}
		} else {
			if("1".equals(map.get("type"))){
//				sql.append(" and u.area is not null ");
//				hashMap.put("act", " group by p.user_id ");
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}else{
				sql.append(" group by p.user_id order by ");//分组
				sql.append(" case when ifnull(u.createdate,'')='' then 0 else 1 end desc,u.createdate desc,p.user_id asc ");
			}
		}
	}
	/**
	 * 
	 * TODO 分页代码
	 * 
	 * @param sql
	 *            语句
	 * @param hashMap
	 *            查询参数
	 * @param map
	 *            分页参数
	 * @param count
	 *            总记录数
	 * @return
	 * @throws Exception
	 *             List<Object> xiao 2016年3月8日
	 */
	private List<HashMap<String, Object>> getPageLimit(StringBuffer sql, HashMap<String, Object> hashMap,
			HashMap<String, String> map,HashMap<String,List<Object>> mapList) throws Exception {
		// 分页
		if (null != map.get("page") && !"".equals(map.get("page"))) {
//			String sqlCopy=sql.toString();//复制一份没有加分组的SQL
//			if(hashMap.get("act")!=null&&!"".equals(hashMap.get("act"))){
//				sql.append(hashMap.get("act"));//加入分组
//			}
			List<HashMap<String, Object>> list = null;
			if(mapList!=null){
				list =baseDAO.findSQLMap(sql.toString(), hashMap, mapList);
			}else{
				list =baseDAO.findSQLMap(sql.toString(), hashMap);
			}
			System.out.println(list.size()+"--------------");
			PageLimit pa = new PageLimit(Integer.parseInt(map.get("page")), list.size());
//			if(hashMap.get("act")!=null&&!"".equals(hashMap.get("act"))){
//				String inSql= "";//百度地图返回的主键
//				if(hashMap.get("lbsScreenInSql")!=null&&"team".equals(hashMap.get("lbsScreenInSql"))){
//					inSql= this.lbsScreenInSqlTeam(pa, list, map);//球队
//				}else{
//					inSql= this.lbsScreenInSqlPlayer(pa, list, map);//用户（球员）
//				}
//				if(inSql!=null&&!"".equals(inSql)){
//					sqlCopy=sqlCopy+" and "+inSql +hashMap.get("act");
//				}
////				System.out.println();
////				System.out.println(sqlCopy);
////				System.out.println();
//				return baseDAO.findSQLMap(sqlCopy, hashMap,mapList);
//			}else{
				if (list != null && list.size() > 0) {
					hashMap.put("limit", pa.getLimit());
					hashMap.put("offset", pa.getOffset());
					sql.append(" limit :limit offset :offset");
					if(mapList!=null){
						return baseDAO.findSQLMap(sql.toString(), hashMap, mapList);
					}
					return baseDAO.findSQLMap(sql.toString(), hashMap);
				}
//			}
		}
		return null;
	}
	/**
	 * 处理lbs 拼接筛选 insql-球员
	 * @param page 前端分页对象
	 * @param li 所有数据抛给百度进行排序
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String lbsScreenInSqlPlayer(PageLimit page,List<HashMap<String, Object>> li,HashMap<String, String> map) throws Exception{
		String inSql="";
		HashMap<String, Object> hashTemp=null;
		List<Integer> listIntId = new ArrayList<Integer>();//筛选时的最后lbs排序条件传入的int主键
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { 
				hashTemp = (HashMap<String, Object>) li.get(index);
				listIntId.add((Integer)hashTemp.get("userIdInt"));
			}
		}
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "5000000", "user", "distance:1", null,"1",null);
		for(BdLbsBean lbstemp:lbsList){
			inSql+="'"+lbstemp.getObject_id()+"',";
		}
	
		//如果没有球场排序就使用默认排序
		if(inSql.length()>0){
			inSql=inSql.substring(0, inSql.length()-1);
			inSql=" u.user_id in ("+inSql+") ";
		}
		return inSql;
	}
	
	@Override
	public HashMap<String, Object> findFilterOption(HashMap<String, String> map) throws Exception{
		List<UConfiguration> listConfiguration=null;//
		List<HashMap<String, Object>> listInfo=null;
		List<HashMap<String, Object>> list=null;
		HashMap<String, Object> hashMap1=new HashMap<>();
		hashMap=new HashMap<>();
		//查询单个筛选
		if(map.get("configType")!=null&&!"".equals(map.get("configType"))){
			listConfiguration=baseDAO.find(map,"from UConfiguration where configType=:configType and configStatus='1'");
			this.buffterInfo(hashMap1,map.get("configType"));
		}else{//查询全部筛选
			listConfiguration=baseDAO.find(map,"from UConfiguration ");
			this.buffterInfo(hashMap1);
		}
		if(listConfiguration!=null&&listConfiguration.size()>0){
			for (UConfiguration uConfiguration : listConfiguration) {
				//大类  lvl=1
				map.put("configId", uConfiguration.getPkeyId());
				listInfo=baseDAO.findSQLMap(map,"select pkey,name,params,option_type,show_type from u_configuration_info where config_id=:configId and lvl='1' and config_info_status='1'  order by sm_sort ");
				
				//小类 lvl=2
				baseDAO.getSessionFactory().getCurrentSession().clear();
				for (HashMap<String, Object> hashMap2 : listInfo) {
					map.put("parentd", hashMap2.get("pkey")+"");
					list=baseDAO.findSQLMap(map,"select pkey,name,params,show_type,did_tag,tag_type from u_configuration_info where parent_id=:parentd and lvl='2' and config_info_status ='1' order by  config_sort ");
					hashMap2.put("list", list);
				}
				uConfiguration.setListInfo(listInfo);
			}
		}
		hashMap1.put("listConfiguration", listConfiguration);
		return hashMap1;
	}
	/**
	 * 地区缓存处理-全部的缓存
	 * @param hashMap 将处理好的（球队、球员、球场缓存集合放入hashMap）
	 * @throws Exception
	 */
	private void buffterInfo(HashMap<String, Object> hashMap1) throws Exception{
		Object redisDaoObject=null;
		URegion region=null;
		List<HashMap<String, Object>> listRedisDaoTeam=new ArrayList<>();
		List<HashMap<String, Object>> listRedisDaoPlayer=new ArrayList<>();
		List<HashMap<String, Object>> listRedisDaoCourt=new ArrayList<>();
		List<HashMap<String, Object>> listRegion=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_team t left join u_region r on t.area=r._id and type='3' where t.area is not null  and t.team_status='3' and t.team_use_status='2' group by r._id order by r.parent,r._id");
		for (HashMap<String, Object> hashMap2 : listRegion) {
			this.redisBuildDate("Team", redisDaoObject, listRedisDaoTeam, hashMap2, region);
		}
		listRegion = baseDAO.findSQLMap(
				"select r.parent,r._id,r.name from u_user t left join u_region r on t.area=r._id and type='3' where t.area is not null  and t.user_status='1'  group by r._id order by r.parent,r._id");
		for (HashMap<String, Object> hashMap2 : listRegion) {
			this.redisBuildDate("Player", redisDaoObject, listRedisDaoPlayer, hashMap2, region);
		}
		listRegion = baseDAO.findSQLMap(
				"select r.parent,r._id,r.name from u_court t left join u_region r on t.area=r._id and type='3' where t.area is not null group by r._id order by r.parent,r._id");
		for (HashMap<String, Object> hashMap2 : listRegion) {
			this.redisBuildDate("Court", redisDaoObject, listRedisDaoCourt, hashMap2, region);
		}
		hashMap1.put("listRedisDaoTeam", listRedisDaoTeam);
		hashMap1.put("listRedisDaoPlayer", listRedisDaoPlayer);
		hashMap1.put("listRedisDaoCourt", listRedisDaoCourt);
	}
	/**
	 * 地区缓存处理  根据类型取地区缓存
	 * @param hashMap 将处理好的（3=球队、2=球员、其他＝球场缓存集合放入hashMap）
	 * @throws Exception
	 */
	private void buffterInfo(HashMap<String, Object> hashMap1,String type) throws Exception{
		Object redisDaoObject=null;
		URegion region=null;
		List<HashMap<String, Object>> listRedisDao=new ArrayList<>();
		List<HashMap<String, Object>> listRegion=new ArrayList<>();
		if("3".equals(type)){//球队
			listRegion=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_team t left join u_region r on t.area=r._id and type='3' where t.area is not null and t.team_status='3' and t.team_use_status='2' group by r._id order by r.parent,r._id");
			for (HashMap<String, Object> hashMap2 : listRegion) {
				this.redisBuildDate("Team", redisDaoObject, listRedisDao, hashMap2, region);
			}
		}else if("2".equals(type)){//球员
			listRegion = baseDAO.findSQLMap(
					"select r.parent,r._id,r.name from u_user t left join u_region r on t.area=r._id and type='3' where t.area is not null  and t.user_status='1'  group by r._id order by r.parent,r._id");
			for (HashMap<String, Object> hashMap2 : listRegion) {
				this.redisBuildDate("Player", redisDaoObject, listRedisDao, hashMap2, region);
			}
		}else if("5".equals(type)){//约战
			listRegion = baseDAO.findSQLMap("select r.parent,r._id,r.name from u_duel u left join u_duel_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.court_id=ubc.subcourt_id left join "+
					" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and  u.duel_status <> '3' and u.duel_status <> '5'  and u.effective_status = '1'  group by r._id order by r.parent,r._id ");
			for (HashMap<String, Object> hashMap2 : listRegion) {
				this.redisBuildDate("Duel", redisDaoObject, listRedisDao, hashMap2, region);
			}
		}else if("6".equals(type)){//挑战
			listRegion = baseDAO.findSQLMap("select r.parent,r._id,r.name from u_challenge u left join u_challenge_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.br_court_id=ubc.subcourt_id left join "+
					" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' group by r._id order by r.parent,r._id ");
			for (HashMap<String, Object> hashMap2 : listRegion) {
				this.redisBuildDate("Challenge", redisDaoObject, listRedisDao, hashMap2, region);
			}
		}else {//球员
			listRegion = baseDAO.findSQLMap(
					"select r.parent,r._id,r.name from u_court t left join u_region r on t.area=r._id and type='3' where t.area is not null group by r._id order by r.parent,r._id");
			for (HashMap<String, Object> hashMap2 : listRegion) {
				this.redisBuildDate("Court", redisDaoObject, listRedisDao, hashMap2, region);
			}
		}
		hashMap1.put("listRedisDao", listRedisDao);
	}
	/**
	 * 获取地区缓存公共处理（球员、球队、球场）
	 * @param type 类型 （Team＝球员、Player＝球队、Court＝球场）
	 * @param redisDaoObject 缓存存放对象
	 * @param listRedisDao 将球员、球队、球场分别存到
	 * @param hashMap2 
	 * @throws Exception
	 */
	private void redisBuildDate(String type, Object redisDaoObject,
			List<HashMap<String, Object>> listRedisDao, 
			HashMap<String, Object> hashMap2,URegion region) throws Exception{
		if(hashMap2.get("parent")!=null){
			redisDaoObject=redisDao.getHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"FilterRegion", type),SerializeUtil.serialize(hashMap2.get("parent")));
			if (redisDaoObject != null && listRedisDao!=null) {
				listRedisDao.removeAll(YHDCollectionUtils.nullCollection());
				//处理缓存取出来的数据
				JSONArray arraylist = JSONArray.fromObject(redisDaoObject);
				hashMap=new HashMap<>();
				
				if(hashMap2.get("parent")!=null&&!"".equals(hashMap2.get("parent"))){
					hashMap.put(hashMap2.get("parent").toString(), arraylist);
					listRedisDao.add(hashMap);
				}
			}
		}
	}
	@Override
	public HashMap<String, Object> findFileTeamAreaBuffter(HashMap<String, String> map) throws Exception{
		List<HashMap<String, Object>> list=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_team t left join u_region r on t.area=r._id and type='3' where t.area is not null and t.team_status='3' and t.team_use_status='2'  group by r.parent order by r.parent,r._id");
		List<HashMap<String, Object>> list1=null;
		for (HashMap<String, Object> hashMap : list) {
			if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				list1=baseDAO.findSQLMap("select r._id,r.name from u_team t left join u_region r on t.area=r._id and type='3' where t.area is not null and r.parent=:parent  and t.team_status='3' and t.team_use_status='2' group by r._id order by r.parent,r._id",hashMap);
				this.HSetTeam(hashMap,"Team",list1);
				
			}
		}
		return null;
	}
	@Override
	public HashMap<String, Object> findFilePlayerAreaBuffter(HashMap<String, String> map) throws Exception{
		List<HashMap<String, Object>> list=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_user u left join u_region r on u.area=r._id and type='3' where u.area is not null  and u.user_status='1'  group by r.parent order by r.parent,r._id");
		List<HashMap<String, Object>> list1=null;
		for (HashMap<String, Object> hashMap : list) {
			if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				list1=baseDAO.findSQLMap("select r._id,r.name from u_user u left join u_region r on u.area=r._id and type='3' where u.area is not null and r.parent=:parent  and u.user_status='1'  group by r._id order by r.parent,r._id",hashMap);
				this.HSetTeam(hashMap,"Player",list1);
				
			}
		}
		return null;
	}
	@Override
	public HashMap<String, Object> findFileCourtAreaBuffter(HashMap<String, String> map) throws Exception{
		List<HashMap<String, Object>> list=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_court c left join u_region r on c.area=r._id and type='3' where c.area is not null  group by r.parent order by r.parent,r._id");
		List<HashMap<String, Object>> list1=null;
		for (HashMap<String, Object> hashMap : list) {
			if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				list1=baseDAO.findSQLMap("select r._id,r.name from u_court c left join u_region r on c.area=r._id and type='3' where c.area is not null and r.parent=:parent group by r._id order by r.parent,r._id",hashMap);
				this.HSetTeam(hashMap,"Court",list1);
				
			}
		}
		return null;
	}
	
	
	@Override
	public HashMap<String, Object> findFileChallengeAreaBuffter(HashMap<String, String> map) throws Exception{
		List<HashMap<String, Object>> list=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_challenge u left join u_challenge_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.br_court_id=ubc.subcourt_id left join "+
				" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' group by r.parent order by r.parent,r._id ");
		List<HashMap<String, Object>> list1=null;
		for (HashMap<String, Object> hashMap : list) {
			if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				list1=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_challenge u left join u_challenge_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.br_court_id=ubc.subcourt_id left join "+
				" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and u.challenge_status <> '3' and u.challenge_status <> '4' and u.effective_status='1' and r.parent=:parent group by r._id  order by r.parent,r._id ",hashMap);
				this.HSetTeam(hashMap,"Challenge",list1);
				
			}
		}
		return null;
	}
	
	
	@Override
	public HashMap<String, Object> findFileDuelAreaBuffter(HashMap<String, String> map) throws Exception{
		List<HashMap<String, Object>> list=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_duel u left join u_duel_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.court_id=ubc.subcourt_id left join "+
				" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and  u.duel_status <> '3' and u.duel_status <> '5'  and u.effective_status = '1'  group by r.parent order by r.parent,r._id ");
		List<HashMap<String, Object>> list1=null;
		for (HashMap<String, Object> hashMap : list) {
			if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				list1=baseDAO.findSQLMap("select r.parent,r._id,r.name from u_duel u left join u_duel_ch uch on u.fch_id=uch.key_id left join u_br_court ubc on uch.court_id=ubc.subcourt_id left join "+
						" u_court c on ubc.court_id=c.court_id left join u_region r on c.area=r._id and type='3' where c.area is not null and u.duel_status <> '3' and u.duel_status <> '5'  and u.effective_status = '1' and r.parent=:parent group by r._id  order by r.parent,r._id ",hashMap);
				this.HSetTeam(hashMap,"Duel",list1);
				
			}
		}
		return null;
	}
	
	/**
	 * 
	 * TODO 添加球队缓存 
	 * @param area 地区值
	 * @param type 类型（球队、球员、球场）
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年6月7日
	 */
	private void HSetTeam(HashMap<String, Object> hashMap,String type,List<HashMap<String, Object>> list1)throws Exception{
		//缓存处理
		if(hashMap.get("parent")!=null&&!"".equals(hashMap.get("parent"))){
				redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"FilterRegion",type),SerializeUtil.serialize(hashMap.get("parent")));
				redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"FilterRegion",type),SerializeUtil.serialize(hashMap.get("parent")),SerializeUtil.serialize(list1));
		}		
	}
	/**
	 * 处理lbs 拼接筛选 insql 球队
	 * @param page 前端分页对象
	 * @param li 所有数据抛给百度进行排序
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String lbsScreenInSqlTeam(PageLimit page,List<HashMap<String, Object>> li,HashMap<String, String> map) throws Exception{
		String inSql="";
		HashMap<String, Object> hashTemp=null;
		List<Integer> listIntId = new ArrayList<Integer>();//筛选时的最后lbs排序条件传入的int主键
		if (null != li && li.size() > 0) {
			for (int index = 0; index < li.size(); index++) { 
				hashTemp = (HashMap<String, Object>) li.get(index);
				listIntId.add((Integer)hashTemp.get("teamIdInt"));
			}
		}
		List<BdLbsBean> lbsList=lBSService.sendBdLbs(page, "1", map.get("location"), "5000000", "team", "distance:1", null,"3",null);
		for(BdLbsBean lbstemp:lbsList){
			inSql+="'"+lbstemp.getObject_id()+"',";
		}
	
		//如果没有球场排序就使用默认排序
		if(inSql.length()>0){
			inSql=inSql.substring(0, inSql.length()-1);
			inSql=" t.team_id in ("+inSql+") ";
		}
		return inSql;
	}
	/**
	 * 检索某个城市下信息
	 * @param map location－百度经纬度、region－地址（上海市，宝山区）
	 * @param tags   team－球队，user-用户（球员）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年6月23日
	 */
	private String checkScreen(HashMap<String, String> map,String tags) throws Exception {
		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
		String lbsBack="";
		String orderSql="";
		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
		lbsMap.put("location", map.get("location"));//百度经纬度
		lbsMap.put("coord_type", "3");//百度坐标系
		//lbsMap.put("radius", "10000000000");//半径范围
		lbsMap.put("tags", tags);//搜索球场
		lbsMap.put("region", map.get("region"));//地区地址
		lbsMap.put("sortby", "distance:1");//距离升序
		lbsMap.put("url", Public_Cache.LBS_LOCATION);
		lbsBack=lBSService.getNearByCity(lbsMap);//按区域搜索
		
		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);//组装接口返回对象
		for(BdLbsBean lbstemp:lbsList){
			orderSql+="'"+lbstemp.getObject_id()+"',";
		}
		//如果没有球场排序就使用默认排序
		if(orderSql.length()>0){
			orderSql=orderSql.substring(0, orderSql.length()-1);
		}
		return orderSql;
	}
	/**
	 * 
	 * 筛选条件--------作为条件使用（后期可能使用）
	 * @param map
	 * @return 
	 * @throws Exception 
	 */
	private void checkScreen(HashMap<String, String> map,String tags,HashMap<String, List<Object>> mapList) throws Exception {
		HashMap<String,Object> lbsMap=new HashMap<String,Object>();
		List<Object> lbsObj=new ArrayList<Object>();
		String lbsBack="";
		lbsMap.put("geotable_id", Constant.BAIDU_TABLE_ID);
		lbsMap.put("location", map.get("location"));//百度经纬度
		lbsMap.put("coord_type", "3");//百度坐标系
		//lbsMap.put("radius", "10000000000");//半径范围
		lbsMap.put("tags", tags);//搜索球场
		lbsMap.put("sortby", "distance:1");//距离升序
		lbsMap.put("url", Public_Cache.LBS_LOCATION);
		lbsMap.put("region", map.get("region"));
		lbsBack=lBSService.getNearByCity(lbsMap);//按区域搜索
		
		List<BdLbsBean> lbsList=lBSService.packLbsDate(lbsBack);//组装接口返回对象
		
		for(BdLbsBean lbstemp:lbsList){
			lbsObj.add(lbstemp.getObject_id());
		}
		if(map.get("region")!=null&&!"".equals(map.get("region"))){
//			sql=" t.team_id in :lbssubcourtId";
			if(lbsObj.size()>0){
				mapList.put("lbssubcourtId",lbsObj);
			}else{
				lbsObj.add("null");//因为此城市没有相应的球场  所以使筛选结果查询不到
				mapList.put("lbssubcourtId",lbsObj);
			}
		}
	}
	

}
