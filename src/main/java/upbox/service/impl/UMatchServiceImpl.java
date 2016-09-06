package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.pub.PublicMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UFollow;
import upbox.model.UMatchBs;
import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.zhetao.MatchBean;
import upbox.pub.Public_Cache;
import upbox.pub.URLConnectionUtil;
import upbox.pub.WebPublicMehod;
import upbox.service.UCourtService;
import upbox.service.UMatchService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 赛事接口实现
 * 
 * @author yc
 *
 *         13611929818
 */
@Service("umatchService")
public class UMatchServiceImpl implements UMatchService {

	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private UUserService uuserService;

	@Resource
	private UTeamService uteamService;

	@Resource
	private UCourtService ucourtService;

	private HashMap<String, Object> hashMap = new HashMap<String, Object>();

	private HashMap<String, Object> hash;

	@Resource
	private UPlayerService uplayerService;

	private StringBuffer sbSelect = new StringBuffer(
			"select "
					+ " u.main_match_name, "
					+ " u.bs_id, "
					+ // 小场次id
					" date_format(u.stdate,'%y年%m月%d日') as bsstdate, "
					+ // 小场次开始日期
					" u.sttime as bssttime, "
					+ // 小场次开始时间
					" date_format(u.endDate,'%y年%m月%d日') as bsenddate, "
					+ // 小场次结束日期
					" u.endtime as bsendtime, "
					+ // 小场次结束时间
					" uc.name as courtname, "
					+ // 球场名称
					"(select uuf.follow_status  from u_follow uuf where uuf.object_id=u.bs_id and uuf.user_follow_type='5' and uuf.follow_status='1' and uuf.user_id=:userId) as follow_status, "
					+ // 是否推荐
					"(select name from u_team ut where ut.team_id=u.fteam_id ) as home_teamname, "
					+ // 主队名
					"(select short_name from u_team ut where ut.team_id=u.fteam_id ) as home_short_teamname, "
					+ // 主队简称
					"(select imgurl from u_team_img ti where ti.team_id=u.fteam_id and ti.timg_using_type='1' and ti.img_size_type='1') as home_teamimgurl, "
					+ // 主队图片
					"(select name from u_team ut where ut.team_id=u.xteam_id ) as away_teamname, "
					+ // 客队名
					"(select short_name from u_team ut where ut.team_id=u.xteam_id ) as away_short_teamname, "
					+ // 客队名
					"(select imgurl from u_team_img ti where ti.team_id=u.xteam_id and ti.timg_using_type='1' and ti.img_size_type='1') as away_teamimgurl, "
					+ // 客队图片
					" u.fteam_id," + " u.xteam_id," + " u.f_fj, " + // 主队获得激数
					" u.k_fj, " + // 主队获得激数
					" u.f_goal, " + // 主队进球
					" u.k_goal "); // 客队进球

	private StringBuffer sbJoin = new StringBuffer(" from u_match_bs u "
			+ " left join u_br_court uc on u.br_court_id = uc.subcourt_id  ");

	@Override
	public HashMap<String, Object> getUMatchBsListWithTeam(
			HashMap<String, String> map) throws Exception {
		UUser user = this.uuserService.getUserinfoByToken(map);
		String userId = "";
		if(user != null&&user.getUserId()!=null ){
			userId = user.getUserId();
		}
		map.put("userId",userId);
		if (map.get("teamId") != null && !"".equals("teamId")
				&& map.get("page") != null && !"".equals(map.get("page"))) {
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			StringBuffer newSql = new StringBuffer();
			hash = new HashMap<String, Object>();
			newSql.append(sbSelect + " " + sbJoin + "  "
					+ "where (u.fteam_id = :teamId or u.xteam_id = :teamId)  "
					+ "order by u.stdate desc,u.sttime desc  limit "
					+ page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
					newSql.toString());
			hash.put("UMatchBsListWithTeam", li);
			return hash;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getUMatchBsListWithPlayer(
			HashMap<String, String> map) throws Exception {
		String userId = map.get("userId");
		if (userId != null && !"".equals(userId) && map.get("page") != null
				&& !"".equals(map.get("page"))) {
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			hash = new HashMap<String, Object>();
			StringBuffer newSql = new StringBuffer();
			map.put("userId", userId);
			String sql = "select team_id from u_player where user_id = :userId and team_id is not null  and in_team='1' "; // 查询球员所在的所有球队
			newSql.append(sbSelect + " " + sbJoin + "where (u.fteam_id in ("
					+ sql + ") or u.xteam_id in (" + sql + ")) "
					+ "order by u.stdate desc,u.sttime desc limit "
					+ page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
					newSql.toString());
			hash.put("UMatchBsListWithPlayer", li);
			return hash;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getUMatchBs(HashMap<String, String> map)
			throws Exception {
		String bsId = map.get("bsId");
		if (bsId != null && !"".equals(bsId)) {
			hash = new HashMap<String, Object>();
			hash.put("bsId", bsId);
			StringBuffer newSql = new StringBuffer();
			if (getTeamId(map) != null) {
				map.put("teamId", getTeamId(map).getTeamId());
			} else {
				map.put("teamId", "");
			}
			map.put("userId", getUserId(map));

			newSql.append(sbSelect + " " + sbJoin + "where  u.bs_id=:bsId");
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
					newSql.toString());

			if (li != null && li.size() > 0) {
				HashMap<String, String> temp = (HashMap<String, String>) li.get(0);
				temp.put(
						"respMatch",
						checkRespMatch(map, temp.get("fteam_id"),
								temp.get("xteam_id")));
				hash.put("UMatchBs", temp);
			} else {
				hash.put("UMatchBs", null);
			}
			return hash;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整");
		}
	}

	@Override
	public UMatchBs getUMatchBs(String bsId) throws Exception {
		if(StringUtils.isEmpty(bsId)){
			return null;
		}
		return this.baseDAO.get(UMatchBs.class,bsId);
	}

	/**
	 * 
	 * 
	 TODO - 验证进入页面人的响应状态
	 * 
	 * @param map
	 * @param fteamId
	 *            发起方队伍ID
	 * @param xteamId
	 *            响应放队伍ID
	 * @return
	 * @throws Exception
	 *             2016年4月21日 mazkc
	 */
	private String checkRespMatch(HashMap<String, String> map, String fteamId,
			String xteamId) throws Exception {
		/*
		 * // respDuel = -1 发起方队长进入 -2响应方队长进入 -3第三方进入 -4发起方队员 -5响应防队员 String
		 * returnStr = ""; map.put("teamId", fteamId); UPlayer player =
		 * uplayerService.getUteamByuserId(map); if(null != player &&
		 * "2".equals(player.getMemberType())){ returnStr = "-4"; }else{ if(null
		 * != player && "1".equals(player.getMemberType())){ returnStr = "-1";
		 * }else{ player = null; map.put("teamId", xteamId); player =
		 * uplayerService.getUteamByuserId(map); if(null != player &&
		 * "2".equals(player.getMemberType())){ returnStr = "-5"; }else{ if(null
		 * != player && "1".equals(player.getMemberType())){ returnStr = "-2";
		 * }else{ returnStr = "-3"; } } } } return returnStr;
		 */
		// respDuel = -1 发起方队长进入 -2响应方队长进入 -3第三方进入 -4发起方队员 -5响应防队员
		boolean bool = false;
		String returnStr = "";
		map.put("teamId", fteamId);
		UPlayer player = uplayerService.getUteamByuserId(map);
		if (null != player) {
			if ("1".equals(player.getMemberType())) {
				returnStr = "-1";
				bool = true;
			} else {
				returnStr = "-4";
				bool = true;
			}
		}
		player = null;
		map.put("teamId", xteamId);
		if (bool == false) {
			player = uplayerService.getUteamByuserId(map);
			if (null != player) {
				if ("1".equals(player.getMemberType())) {
					returnStr = "-2";
				} else {
					returnStr = "-5";
				}
			}
		}
		return returnStr;
	}

	@Override
	public HashMap<String, Object> getUMatchBsListWithFollow(
			HashMap<String, String> map) throws Exception {
		if (map.get("page") != null && !"".equals(map.get("page"))) {
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			map.put("userId", getUserId(map));
			StringBuffer newSql = new StringBuffer();
			hash = new HashMap<String, Object>();
			newSql.append(sbSelect
					+ " "
					+ sbJoin
					+ " left join u_follow uf on u.bs_id=uf.object_id "
					+ "where uf.user_follow_type='5'  and uf.follow_status='1' and uf.user_id=:userId "
					+ "order by u.stdate desc,u.sttime desc  limit "
					+ page.getOffset() + "," + page.getLimit());
			ArrayList<Object> li = (ArrayList<Object>) baseDAO.findSQLMap(map,
					newSql.toString());
			return WebPublicMehod.returnRet("matchList", li);
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	@Override
	public HashMap<String, Object> getUMatchBsBody(HashMap<String, String> map)
			throws Exception {
		String bsId = map.get("bsId");
		if (bsId != null && !"".equals(bsId)) {
			UMatchBs umaBs = baseDAO.get(UMatchBs.class, bsId);

			if (umaBs != null && umaBs.getUBrCourt() != null) {
				map.put("subcourt_id", umaBs.getUBrCourt().getSubcourtId());

			} else {
				map.put("subcourt_id", "");

			}
			hashMap.clear();
			if (umaBs != null) {
				hashMap.put(
						"time",
						PublicMethod.getDateToString(umaBs.getStdate(),
								"MM月dd日")
								+ " "
								+ umaBs.getSttime()
								+ "-"
								+ umaBs.getEndtime());
			} else {
				hashMap.put("time", "");
			}
			map.put("self", "self");
			hashMap.putAll(ucourtService.queryCourtDetailByOrderid(map));

			return hashMap;
		}
		return WebPublicMehod.returnRet("error", "请求参数不完整！");
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
		return team;
	}

	@Override
	public HashMap<String, Object> isFollowMatch(HashMap<String, String> map)
			throws Exception {
		String bsId = map.get("bsId");
		String type = map.get("type");
		String msg = "";
		UFollow uf1 = null;
		if (bsId != null && !"".equals(bsId)) {
			hashMap.put("objectId", bsId);
			hashMap.put("type", type);
			hashMap.put("userId",map.get("loginUserId"));
			List<UFollow> uf = baseDAO
					.find("from UFollow where objectId=:objectId and UUser.userId=:userId and userFollowType='5' ",
							hashMap);
			if (uf != null && uf.size() > 0) {
				uf1 = uf.get(0);
				uf1.setFollowStatus(type);
				baseDAO.update(uf1);
			} else {
				uf1 = new UFollow();
				uf1.setKeyId(WebPublicMehod.getUUID());
				uf1.setCreatedate(new Date());
				uf1.setFollowStatus(type);
				uf1.setObjectId(bsId);
				uf1.setUserFollowType("5");
				uf1.setUUser(uuserService.getUserinfoByToken(map));
				baseDAO.save(uf1);
			}
			if (type.equals("1")) {
				msg = "关注成功！";
			} else {
				msg = "取消关注成功！";
			}
			return WebPublicMehod.returnRet("success", msg);
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}
	
	@Override
	public HashMap<String, Object> getOtherMatchAllList(HashMap<String, String> map) throws Exception {
		if ( map.get("page") != null && !"".equals(map.get("page"))) {
			Gson gson = new Gson();  
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			String path="";
			path=Public_Cache.MATCH_SYSTEM_URL+"matchjsondata.oms?omsv=matchlist&matchp="+page.getPage()+"&matchps="+page.getLimit();
			path=Public_Cache.MATCH_SYSTEM_URL+"matchjsondata.oms?omsv=matchlist&teamids=73ddee71-7a82-40dd-ae8c-dbdedca45e0a";
			String json=URLConnectionUtil.sendGet(path, null);
			JSONObject matchJson= JSONObject.fromObject(json);
			JSONArray matchs = matchJson.getJSONArray("matchs");
			List<MatchBean> temp=gson.fromJson(matchs.toString(), new TypeToken<List<MatchBean>>() {}.getType());  
			hashMap.put("test", temp);
			return hashMap;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	/**
	 * 统一处理游客模式的userid
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getUserId(HashMap<String, String> map) throws Exception {
		UUser user = uuserService.getUserinfoByUserId(map);
		if (user != null) {
			return user.getUserId();
		} else {
			return "";
		}

	}

}
