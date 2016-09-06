package upbox.service.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UBrCourt;
import upbox.model.UEquipment;
import upbox.model.UOrderCourt;
import upbox.model.UTeamImg;
import upbox.model.UUserImg;
import upbox.movetest.bean.Imagemanager;
import upbox.movetest.bean.UOrder;
import upbox.movetest.bean.UPTmapping;
import upbox.movetest.bean.UPlayer;
import upbox.movetest.bean.UTeam;
import upbox.movetest.bean.UUeqnumber;
import upbox.movetest.bean.UUser;
import upbox.movetest.bean.UUserAndTeamAlbum;
import upbox.pub.WebPublicMehod;
import upbox.service.MoveDBTestService;

@Service("moveDBTestService")
public class MoveDBTestServiceImpl implements MoveDBTestService {
	@Resource
	private OperDAOImpl baseDAO;

	@Override
	public void moveUser(List<UUser> li, Statement stmt) throws Exception {
		//baseDAO.executeSql("delete from u_user");
		if (null != li && li.size() > 0) {
			upbox.model.UUser _user = null;
			UPlayer p = null;
			String sql = "";
			int i = 0;

			HashMap<String, String> map = new HashMap<String, String>();
			for (UUser u : li) {
				upbox.model.UUser uus = baseDAO.get("from UUser where phone = '" + u.getPhone() + "'");
				if(null == uus){
					if ((null == u.getPhone() || "".equals(u.getPhone()))
							|| (u.getPhone().equals(map.get(u.getPhone())))) {

					} else {
						sql = "select * from u_player where userid = " + u.getId();
						p = SerializeUtil.unSerializeToBean(
								rsToJsonObj(stmt.executeQuery(sql)), UPlayer.class);
						_user = new upbox.model.UUser(); // 新库USER
						_user.setHeight(p.getHeight());
						_user.setWeight(p.getWeight());
						_user.setUserId(WebPublicMehod.getUUID());
						_user.setUsername(u.getNickname());
						_user.setNickname(u.getNickname());
						_user.setPhone(u.getPhone());
						_user.setEmail(u.getEmail());
						_user.setPwd(u.getLoginpassword());
						_user.setUserStatus("1");
						_user.setSex(String.valueOf(u.getSex()));
						_user.setBirthday(null != u.getBirthday() ? checkBirthday(u
								.getBirthday()) : new Date());
						_user.setUserType(null == u.getIdentity() ? "1"
								: checkUserType(u.getIdentity()));
						_user.setCreatedate(u.getCreatetime());
						_user.setOpenid(u.getOpenid());
						_user.setRemark(u.getRemark());
						_user.setWeibologintoken(u.getWeibologintoken());
						_user.setWechatlogintoken(u.getWechatlogintoken());
						_user.setQqlogintoken(u.getQqlogintoken());
						_user.setRegi_resouce(String.valueOf(u.getResource()));
						_user.setRealname("");
						_user.setNumberid(String.valueOf(u.getNumberid()));
						_user.setOld_key_id(u.getId());
						_user.setIdcode(u.getPhonecode());
						_user.setCodetime(u.getPhonetime());
						baseDAO.save(_user);
						map.put(u.getPhone(), u.getPhone());
						if (i % 50 == 0) {
							baseDAO.getSessionFactory().getCurrentSession().flush();
						}
						System.out
								.println("-------------------------------------------"
										+ i
										+ "-------------------------------------------");
						i++;

					}
				}
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	/**
	 * 
	 * 
	 TODO - 验证用户类型
	 * 
	 * @return 2015年12月18日 mazkc
	 */
	private String checkUserType(int userType) {
		String type = "";
		if (1 == userType || 2 == userType || 3 == userType)
			type = "1";
		else if (4 == userType)
			type = "3";
		else if (5 == userType)
			type = "5";
		return type;
	}

	/**
	 * 
	 * 
	 TODO - 验证生日是否符合格式
	 * 
	 * @param str
	 * @return 2015年12月18日 mazkc
	 * @throws ParseException
	 */
	private Date checkBirthday(String str) throws ParseException {
		Date d = null;
		if (str.indexOf("/") != -1) {
			str = str.replaceAll("/", "-");
			d = PublicMethod.getStringToDate(str, "yyyy-MM-dd");
		} else if (str.indexOf("/") == -1 && str.indexOf("-") == -1) {
			d = new Date();
		} else {
			d = PublicMethod.getStringToDate(str, "yyyy-MM-dd");
		}
		return d;
	}

	@Override
	public void moveEnumber(List<UUeqnumber> li) throws Exception {
		//baseDAO.executeSql("delete from u_equipment");
		if (null != li && li.size() > 0) {
			int i = 0;
			UEquipment ueq = null;
			upbox.model.UUser user = null;
			List<upbox.model.UUser> list = null;
			for (UUeqnumber q : li) {
				ueq = new UEquipment();
				ueq.setKeyId(String.valueOf(q.getId()));
				ueq.setCode(q.getCode());
				ueq.setCpu(q.getCpu());
				ueq.setSystem(q.getPhoneType());
				// list = baseDAO.find("from UUser where numberid = '" +
				// q.getId() + "'");
				// if(null != list && list.size() > 0)
				// user = list.get(0);
				// ueq.setUUser(user);
				baseDAO.save(ueq);

				if (i % 100 == 0) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
				}

				i++;
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
	}

	@Override
	public void moveUserHead(JSONArray li) throws Exception {
		//baseDAO.executeSql("delete from u_user_img");
		if (null != li && li.size() > 0) {
			JSONObject o = null;
			UUserImg us = null;
			upbox.model.UUser u = null;
			for (int i = 0; i < li.size(); i++) {
				o = li.getJSONObject(i);
				// System.out.println(o.toString());
				if (null != o.getString("imageurl")) {
					us = new UUserImg();
					u = baseDAO.get("from UUser where old_key_id = "
							+ o.getInt("id") );
					us.setPkId(WebPublicMehod.getUUID());
					us.setUUser(u);
					us.setImgSizeType("1");
					us.setUimgUsingType("1");
					if (o.getString("imageurl").contains("http:")) {
						us.setImgurl(o.getString("imageurl"));
					} else {
						us.setImgurl("http://img.upbox.com.cn"
								+ o.getString("imageurl"));
					}
					baseDAO.save(us);

					if (i % 100 == 0) {
						baseDAO.getSessionFactory().getCurrentSession().flush();
					}
				}
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	@Override
	public void moveOrder(List<UOrder> li, Statement stmt) throws Exception {
		baseDAO.executeSql("delete from u_order_court where createdate < '2016-05-13'");
		baseDAO.executeSql("delete from u_order where createdate < '2016-05-13'");
		if (null != li && li.size() > 0) {
			upbox.model.UOrder uo = null;
			upbox.model.UUser user = null;
			UBrCourt ubc = null;
			UOrderCourt uoc = null;
			int i = 0;
			for (UOrder o : li) {
				uo = new upbox.model.UOrder();
				uoc = new UOrderCourt();
				user = baseDAO.get("from UUser where old_key_id = "
						+ o.getUserid());
				ubc = baseDAO.get("from UBrCourt where name = '" +  o.getCourtname()  + "'");
				
				uo.setOrderId(WebPublicMehod.getUUID());
				uo.setOrderType("1"); 
				uo.setUUser(user);
				uo.setCreatedate(o.getCreatetime());
				uo.setCreatetime(o.getCreatetime());
				uo.setPrice(Double.valueOf(o.getPrice()));
				uo.setResource(String.valueOf(o.getSource()));
				uo.setPaytype(String.valueOf(o.getPaytype()));
				uo.setJpusho(String.valueOf(o.getJpusho()));
				uo.setJpushp(String.valueOf(o.getJpushp()));
				uo.setOrderstatus(checkOrderstatus(o.getOrderstatus()));
				uo.setPerId("-1");
				uo.setOrdernum(o.getOrdernum());
				uo.setRelationType("3");
				baseDAO.save(uo);

				uoc.setOrderCourtId(WebPublicMehod.getUUID());
				uoc.setUBrCourt(ubc);
				uoc.setUOrder(uo);
				uoc.setCreateuser(user.getUserId());
				uoc.setSessionDuration("4");
				uoc.setSessionStatus("1");
				uoc.setSessionUseingStatus("2");
				baseDAO.save(uoc);
				
				if (i % 100 == 0) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
				}

				i++;
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	/**
	 * 
	 * 
	 TODO - 验证订单状态
	 * 
	 * @return 2015年12月18日 mazkc
	 */
	private String checkOrderstatus(int orderstatus) {
		String type = "";
		if (1 == orderstatus)
			type = "1";
		else if (-1 == orderstatus)
			type = "2";
		else if (2 == orderstatus)
			type = "3";
		else if (3 == orderstatus)
			type = "4";
		else if (4 == orderstatus)
			type = "5";
		return type;
	}

	@Override
	public void movPlayer(List<UPlayer> li, Statement stmt) throws Exception {
		//baseDAO.executeSql("delete from u_player where team_id is null");
		if (null != li && li.size() > 0) {
			upbox.model.UPlayer p = null;
			upbox.model.UUser user = null;
			upbox.model.UTeam t = null;
			List<UPTmapping> upt = null;
			String sql = "";
			upbox.model.UPlayer pp1 = null;
			int i = 0;
			for (UPlayer pp : li) {
				t = new upbox.model.UTeam();
				user = baseDAO.get("from UUser where old_key_id = "
						+ pp.getUserid());
				sql = "select * from u_pt_mapping where playerid = "
						+ pp.getId();
				upt = (List<UPTmapping>) SerializeUtil.unSerializeToList(
						rsToJsonList(stmt.executeQuery(sql)), UPTmapping.class);
				if (null != user) {
					if (null != upt && upt.size() > 0) {
						pp1 = baseDAO.get("from UPlayer where UTeam.teamId is null and UUser.userId = '" + user.getUserId() + "'");
						for (UPTmapping p1 : upt) {
							p = new upbox.model.UPlayer();
							t = baseDAO.get("from UTeam where old_key_id = "
									+ p1.getTeamid());
							if (null != t && null != t.getTeamId()) {
								upbox.model.UPlayer pp2 = baseDAO.get("from UPlayer where UTeam.teamId = '" + t.getTeamId() + "' and UUser.userId = '" + user.getUserId() + "'");
								if(null == pp2){
									p.setPlayerId(WebPublicMehod.getUUID());
									p.setUUser(user);
									p.setNumber(pp.getNumber());
									p.setUTeam(t);
									p.setTeamBelonging("1");
									p.setMemberType(String.valueOf(p1.getType()));
									p.setInTeam("1");
									p.setPosition(null);
									p.setCanPosition(checkPlayerSeatstatus(pp
											.getSeat()));
									baseDAO.save(p);
								}
								
								if(null == pp1){
									p = new upbox.model.UPlayer();
									p.setPlayerId(WebPublicMehod.getUUID());
									p.setUUser(user);
									p.setNumber(pp.getNumber());
									p.setTeamBelonging("2");
									p.setInTeam("2");
									p.setCanPosition(checkPlayerSeatstatus(pp
											.getSeat()));
									p.setPosition(null);
									baseDAO.save(p);
								}
							}
						}
					} else {
						if(null == pp1){
							p = new upbox.model.UPlayer();
							p.setPlayerId(WebPublicMehod.getUUID());
							p.setUUser(user);
							p.setNumber(pp.getNumber());
							p.setTeamBelonging("2");
							p.setInTeam("2");
							p.setPosition(null);
							baseDAO.save(p);
						}
					}

					if (i % 100 == 0) {
						baseDAO.getSessionFactory().getCurrentSession().flush();
					}

					i++;
				}
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	@Override
	public void moveTeam(List<UTeam> li, Statement stmt) throws Exception {
//		baseDAO.executeSql("delete from u_team");
//		baseDAO.executeSql("delete from u_team_img");
		if (null != li && li.size() > 0) {
			upbox.model.UTeam t = null;
			upbox.model.UUser user = null;
			upbox.model.UTeamImg uti = null;
			String sql = "";
			Imagemanager im = null;
			int i = 0;
			for (UTeam tt : li) {
				uti = new UTeamImg();
				t = new upbox.model.UTeam();
				user = baseDAO.get("from UUser where old_key_id = "
						+ tt.getUserid());
				t.setUUser(user);
				t.setTeamId(WebPublicMehod.getUUID());
				t.setTeamType("1");
				t.setName(tt.getName());
				t.setRemark(tt.getRemark());
				t.setCreatedate(tt.getCreatetime());
				t.setCreatetime(tt.getCreatetime());
				t.setTeamStatus(checkTeamstatus(tt.getReviewstatus()));
				t.setIntegral(tt.getCount());
				t.setTeamsource(String.valueOf(tt.getTeamsource()));
				t.setOld_key_id(tt.getId());
				t.setMaximum(50);
				t.setRecommendTeam("0");
				t.setTeamUseStatus(checkTeamstatus2(tt.getTeamstatus()));
				t.setTeamCount(getTeamCount(t.getOld_key_id(), stmt));
				t.setHistoryCount(t.getTeamCount());
				t = getTeamAvg(t.getOld_key_id(), stmt, t);
				t = getHis(t.getOld_key_id(), stmt, t);
				baseDAO.save(t);

				sql = "select * from imagemanager where id = "
						+ tt.getImageid();
				im = SerializeUtil
						.unSerializeToBean(rsToJsonObj(stmt.executeQuery(sql)),
								Imagemanager.class);
				if (null != im && null != im.getImageurl()
						&& !"".equals(im.getImageurl())) {
					uti.setTeamImgId(WebPublicMehod.getUUID());
					uti.setTimgUsingType("1");
					if (im.getImageurl().contains("http:")) {
						uti.setImgurl(im.getImageurl());
					} else {
						uti.setImgurl("http://img.upbox.com.cn"
								+ im.getImageurl());
					}
					uti.setImgSizeType("1");
					uti.setUTeam(t);
					baseDAO.save(uti);
				}
				if (i % 100 == 0) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
				}

				i++;
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	private upbox.model.UTeam getHis(int teamid, Statement stmt,
			upbox.model.UTeam t) throws Exception {
		String sql = "select ranktable.rankNo,ranktable.count from (select "
				+ "row_number() over(order by tm.count desc,(select count(fieldnum) from u_rule_record where team_id = tm.id) desc,(select sum( cast ( realgoalnum as numeric )) from u_rule_record where team_id = tm.id ) desc,tm.createtime asc) as rankNo,"
				+ "tm.id,tm.name,tm.count,img.imageurl,tm.createtime,(select count(team_id) from u_rule_record where team_id = tm.id) as fieldsum,"
				+ "(select sum( cast ( realgoalnum as numeric )) from u_rule_record where team_id = tm.id ) as realgoalsum "
				+ "from u_team tm left join imagemanager img on tm.imageid = img.id "
				+ "left join u_rule_record urr on tm.id = urr.team_id where tm.teamstatus <> -1 and tm.reviewstatus <>-1 and tm.count is not null "
				+ "group by tm.id,tm.name,tm.remark,tm.teamstatus,tm.count,img.imageurl "
				+ "order by tm.count desc,fieldsum desc,realgoalsum desc,tm.createtime asc ) as ranktable where ranktable.id = "
				+ teamid;
		JSONObject rs = rsToJson(stmt.executeQuery(sql));
		if (null != rs && rs.size() > 0) {
			if (rs.containsKey("rankno")) {
				t.setRank(Integer.parseInt(null == rs.getString("rankno") ? "0"
						: rs.getString("rankno")));
				t.setHistoryRank(t.getRank());
			} else {
				t.setRank(0);
				t.setHistoryRank(t.getRank());
			}
			if (rs.containsKey("count")) {
				t.setIntegral(Integer.parseInt(null == rs.getString("count") ? "0"
						: rs.getString("count")));
				t.setHistoryIntegral(t.getIntegral());
			} else {
				t.setIntegral(0);
				t.setHistoryIntegral(t.getIntegral());
			}
		}
		return t;
	}

	// 球队人数
	private int getTeamCount(int teamid, Statement stmt) throws Exception {
		JSONObject rs = rsToJson(stmt
				.executeQuery("select count(*) as count from u_pt_mapping where teamid = "
						+ teamid));
		int count = 0;
		if (null != rs && rs.size() > 0) {
			count = (int) rs.getInt("count");
		}
		return count;
	}

	// 平均
	private upbox.model.UTeam getTeamAvg(int teamid, Statement stmt,
			upbox.model.UTeam t) throws Exception {
		JSONArray rs = ToJsonList(stmt
				.executeQuery("select uu.birthday,pl.height,pl.weight from u_team as t left join u_pt_mapping as pp on t.id = pp.teamid "
						+ "left join u_player as pl on pp.playerid = pl.id left join u_user as uu on pl.userid = uu.id where t.id = "
						+ teamid));
		JSONObject o = null;
		int height = 0;
		int weight = 0;
		int age = 0;
		int index = 0;
		if (null != rs && rs.size() > 0) {
			for (int i = 0; i < rs.size(); i++) {
				o = rs.getJSONObject(i);
				index++;
				if (null != o && o.size() > 0) {
					weight += Double
							.valueOf(null == o.getString("weight") ? "0" : o
									.getString("weight").trim());
					height += Double
							.valueOf(null == o.getString("height") ? "0" : o
									.getString("height").trim());
					if (o.containsKey("birthday")) {
						age += Integer.parseInt(PublicMethod.getDateToString(
								new Date(), "yyyy"))
								- Integer.parseInt(null == o
										.getString("birthday") ? "0" : o
										.getString("birthday").substring(0, 4));
					}
				}
			}
		}
		if (age < 0) {
			age = 20;
		}
		t.setAvgAge(String.valueOf(age / index));
		t.setAvgHeight(String.valueOf(height / index));
		t.setAvgWeight(String.valueOf(weight / index));
		return t;
	}

	/**
	 * 
	 * 
	 TODO - 验证队伍状态
	 * 
	 * @return 2015年12月18日 mazkc
	 */
	private String checkTeamstatus(int orderstatus) {
		String type = "";
		if (1 == orderstatus)
			type = "1";
		else if (2 == orderstatus)
			type = "3";
		else if (-1 == orderstatus)
			type = "2";
		return type;
	}

	/**
	 * 
	 * 
	 TODO - 验证队伍状态2
	 * 
	 * @return 2015年12月18日 mazkc
	 */
	private String checkTeamstatus2(int orderstatus) {
		String type = "";
		if (1 == orderstatus)
			type = "2";
		else if (-1 == orderstatus)
			type = "1";
		return type;
	}

	/**
	 * 
	 * 
	 TODO - 验证球员状态
	 * 
	 * @return 2015年12月18日 mazkc
	 */
	private String checkPlayerSeatstatus(String orderstatus) {
		StringBuffer type = new StringBuffer();
		if (null != orderstatus) {
			String[] temp = orderstatus.split(",");
			if (null != temp && temp.length > 0) {
				for (int i = 0; i < temp.length; i++) {
					type.append(checkPlayerSeat(temp[i]) + ",");
				}
			}
		}
		return type.toString().length() > 1 ? type.toString().substring(0,
				type.toString().length() - 1) : type.toString();
	}

	private String checkPlayerSeat(String orderstatus) {
		String type = "";
		if ("1".equals(orderstatus))
			type = "1";
		else if ("2".equals(orderstatus))
			type = "5";
		else if ("3".equals(orderstatus))
			type = "4";
		else if ("4".equals(orderstatus))
			type = "3";
		return type;
	}

	/**
	 * 
	 * 
	 TODO - 将查询结果JSON Object
	 * 
	 * @param rs
	 * @return 2015年12月17日 mazkc
	 */
	public String rsToJsonObj(ResultSet rs) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String columnName = "";
		Object value = null;
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				columnName = metaData.getColumnLabel(i);
				value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
		}
		return jsonObj.toString();
	}

	/**
	 * 
	 * 
	 TODO - 将查询结果JSON Object
	 * 
	 * @param rs
	 * @return 2015年12月17日 mazkc
	 */
	public JSONObject rsToJson(ResultSet rs) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String columnName = "";
		Object value = null;
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				columnName = metaData.getColumnLabel(i);
				value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
		}
		return jsonObj;
	}

	/**
	 * 
	 * 
	 TODO - 将查询结果JSON list
	 * 
	 * @param rs
	 * @return 2015年12月17日 mazkc
	 */
	public String rsToJsonList(ResultSet rs) throws Exception {
		// json数组
		JSONArray array = new JSONArray();
		String columnName = "";
		Object value = null;
		JSONObject jsonObj = null;
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			jsonObj = new JSONObject();
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				columnName = metaData.getColumnLabel(i);
				value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.add(jsonObj);
		}
		return array.toString();
	}

	public JSONArray ToJsonList(ResultSet rs) throws Exception {
		// json数组
		JSONArray array = new JSONArray();
		String columnName = "";
		Object value = null;
		JSONObject jsonObj = null;
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			jsonObj = new JSONObject();
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				columnName = metaData.getColumnLabel(i);
				value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.add(jsonObj);
		}
		return array;
	}

	/**
	 * 
	 * 
	 TODO - 将查询结果JSON list
	 * 
	 * @param rs
	 * @return 2015年12月17日 mazkc
	 */
	public JSONArray rsToJsonArray(ResultSet rs) throws Exception {
		// json数组
		JSONArray array = new JSONArray();
		String columnName = "";
		Object value = null;
		JSONObject jsonObj = null;
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			jsonObj = new JSONObject();
			// 遍历每一列
			for (int i = 1; i <= columnCount; i++) {
				columnName = metaData.getColumnLabel(i);
				value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.add(jsonObj);
		}
		return array;
	}

	@Override
	public void moveUserHeadXC(List<UUserAndTeamAlbum> li) throws Exception {
		if (null != li && li.size() > 0) {
			upbox.model.UUserImg _userimg = null;
			int i = 0;
			upbox.model.UUser u = null;
			for (UUserAndTeamAlbum uimg : li) {
				baseDAO.executeSql("delete from u_user_img where user_id = (select user_id from u_user where old_key_id = " + uimg.getUserid() + ") and uimg_using_type = '2' and img_size_type = '1'");
				_userimg = new UUserImg();
				u = baseDAO.get("from UUser where old_key_id = "
						+ uimg.getUserid());
				_userimg.setPkId(WebPublicMehod.getUUID());
				_userimg.setUUser(u);
				_userimg.setImgSizeType("1");
				_userimg.setUimgUsingType("2");
				_userimg.setWeight(0);
				_userimg.setImgurl(uimg.getImageurl());
				baseDAO.save(_userimg);
				if (i % 50 == 0) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
				}
				i++;

			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}

	@Override
	public void TeamXCMoveDB(List<UUserAndTeamAlbum> li) throws Exception {
		if (null != li && li.size() > 0) {
			upbox.model.UTeamImg _teamimg = null;
			int i = 0;
			upbox.model.UTeam t = null;
			for (UUserAndTeamAlbum uimg : li) {
				baseDAO.executeSql("delete from u_team_img where team_id = (select team_id from u_team where old_key_id = " + uimg.getTeamid() + ") and timg_using_type = '2' and img_size_type = '1'");
				_teamimg = new UTeamImg();
				t = baseDAO.get("from UTeam where old_key_id = "
						+ uimg.getTeamid());
				_teamimg.setTeamImgId(WebPublicMehod.getUUID());
				_teamimg.setImgSizeType("1");
				_teamimg.setUTeam(t);
				_teamimg.setImgurl(uimg.getImageurl());
				_teamimg.setTimgUsingType("2");
				_teamimg.setImgWeight(0);
				baseDAO.save(_teamimg);
				if (i % 50 == 0) {
					baseDAO.getSessionFactory().getCurrentSession().flush();
				}
				i++;

			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
			baseDAO.getSessionFactory().getCurrentSession().clear();
		}
	}
}
