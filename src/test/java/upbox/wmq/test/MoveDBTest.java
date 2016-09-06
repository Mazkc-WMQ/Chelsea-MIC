package upbox.wmq.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import upbox.movetest.bean.UOrder;
import upbox.movetest.bean.UPlayer;
import upbox.movetest.bean.UTeam;
import upbox.movetest.bean.UUeqnumber;
import upbox.movetest.bean.UUser;
import upbox.movetest.bean.UUserAndTeamAlbum;
import upbox.service.MoveDBTestService;

import com.alibaba.fastjson.JSON;

/**
 * 数据切换倒库测试
 * 
 * @author wmq
 *
 *         15618777630
 */
@ContextConfiguration("applicationContext.xml")
public class MoveDBTest extends AbstractJUnit4SpringContextTests {
	//2016-04-28  15：30
	//user old_key_id 10258
	//team old_key_id 1220
	@Resource
	private MoveDBTestService moveDBTestService;
	private static String url = "jdbc:postgresql://app.upbox.com.cn:5432/upbox";
	private static String username = "upbox";
	private static String password = "12345ewq";
	private static Connection con;
	private Statement stmt;

	/**
	 * 
	 * 
	 TODO - JDBC获取数据库连接 2015年12月16日 mazkc
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@BeforeClass
	public static void getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, username, password);
	}

	/**
	 * 
	 * 
	 TODO - Test运行结束后关闭资源
	 * 
	 * @throws SQLException
	 *             2015年12月16日 mazkc
	 */
	@After
	public void closeConnection() throws SQLException {
		if (null != con) {
			con.close();
		}
	}

	/**
	 * 
	 * 
	 TODO - 数据库查询语句 非预编译
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 *             2015年12月16日 mazkc
	 */
	@Ignore
	public ResultSet executeQuery(String sql) throws SQLException {
		stmt = con.createStatement();
		return stmt.executeQuery(sql);
	}
	
	/**
	 * 
	 * 
	 TODO - 数据库执行语句 非预编译
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 *             2015年12月16日 mazkc
	 */
	@Ignore
	public void executeExecute(String sql) throws SQLException {
		stmt = con.createStatement();
		stmt.execute(sql);
	}

	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	   mazkc
	 */
	public void printInfo(String temp, Object obj) {
		//System.out.println(temp + JSON.toJSONString(obj));
	}

	@Ignore
	/**
	 * 
	 * 
	   TODO - 将查询结果JSON list
	   @param rs
	   @return
	   2015年12月17日
	   mazkc
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
	
	@Ignore
	/**
	 * 
	 * 
	   TODO - 将查询结果JSON list
	   @param rs
	   @return
	   2015年12月17日
	   mazkc
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
	
	@Ignore
	/**
	 * 
	 * 
	   TODO - 将查询结果JSON Object
	   @param rs
	   @return
	   2015年12月17日
	   mazkc
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
	
	@Test
	public void runMoveDB() throws Exception{
		//testUserMoveDB();
		//testEnumberMoveDB();
		//testUserHeadMoveDB();
		//testUserTeamMoveDB();
		//testUserPlayerMoveDB();
		//testOrderMoveDB();
		/*testUserHeadXCMoveDB();
		testTeamXCMoveDB();*/
	}
	
	/**
	 * 
	 * 
	 TODO - 反序列化-List<T>
	 * 
	 * @param val
	 * @param clazz
	 * @return 2015年11月25日 mazkc
	 */
	public static List<?> unSerializeToList(String val, Class<?> clazz) {
		return JSON.parseArray(val, clazz);
	}

	/**
	 * 
	 * 
	   TODO - 用户信息迁移（1）
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testUserMoveDB() throws Exception {
		//老库的用户数据
		List<UUser> li = (List<UUser>) unSerializeToList(rsToJsonList(executeQuery("select * from u_user where id > 10258 order by id asc")), UUser.class);
		moveDBTestService.moveUser(li,stmt);
	}
	
	/**
	 * 
	 * 
	   TODO - 用户设备号信息迁移（2）
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testEnumberMoveDB() throws Exception {
		//老库的设备号数据
		List<UUeqnumber> li = (List<UUeqnumber>) unSerializeToList(rsToJsonList(executeQuery("select * from u_eqnumber where id > 9999 order by id asc")), UUeqnumber.class);
		moveDBTestService.moveEnumber(li);
	}
	
	/**
	 * 
	 * 
	   TODO - 用户头像信息迁移（3）
	 * @throws SQLException 
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testUserHeadMoveDB() throws SQLException, Exception {
		//老库的用户头像数据
		JSONArray li = rsToJsonArray(executeQuery("select u.id,i.imageurl from u_user as u "
				+ "left join imagemanager as i on u.headid = i.id where u.id > 10258 and u.headid is not null order by u.id asc"));
		moveDBTestService.moveUserHead(li);
	}

	/**
	 * 
	 * 
	   TODO - 用户相册迁移
	 * @throws SQLException 
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testUserHeadXCMoveDB() throws SQLException, Exception {
		//老库的用户头像数据
		List<UUserAndTeamAlbum> li = (List<UUserAndTeamAlbum>)unSerializeToList(rsToJsonList(executeQuery("select * from u_userandteamalbum where type = 'useralbum' and usestatus = 1 ")), UUserAndTeamAlbum.class);
		moveDBTestService.moveUserHeadXC(li);
	}
	
	/**
	 * 
	 * 
	   TODO - 用户球队信息迁移（4）
	 * @throws SQLException 
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testUserTeamMoveDB() throws SQLException, Exception {
		//老库的用户球队数据
		List<UTeam> li = (List<UTeam>) unSerializeToList(rsToJsonList(executeQuery("select * from u_team where id > 1220 order by id asc")), UTeam.class);
		moveDBTestService.moveTeam(li,stmt);
	}
	
	/**
	 * 
	 * 
	   TODO - 用户球员信息迁移（5）
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testUserPlayerMoveDB() throws Exception {
		//老库的用户球员数据
		List<UPlayer> li = (List<UPlayer>) unSerializeToList(rsToJsonList(executeQuery("select * from u_player as p left join u_user as u on p.userid = u.id where u.id > 10258 order by p.id asc")), UPlayer.class);
		moveDBTestService.movPlayer(li,stmt);
	}
	
	/**
	 * 
	 * 
	   TODO - 球队相册迁移（5）
	   @throws Exception
	   2015年12月17日
	   mazkcq
	 */
	@Ignore
	public void testTeamXCMoveDB() throws Exception {
		List<UUserAndTeamAlbum> li = (List<UUserAndTeamAlbum>)unSerializeToList(rsToJsonList(executeQuery("select * from u_userandteamalbum where type = 'teamalbum' and usestatus = 1 ")), UUserAndTeamAlbum.class);
		moveDBTestService.TeamXCMoveDB(li);
	}
	
	/**
	 * 
	 * 
	   TODO - 订单信息迁移（1）
	   @throws Exception
	   2015年12月17日
	   mazkc
	 */
	@Ignore
	public void testOrderMoveDB() throws Exception {
		//老库的订单数据
		List<UOrder> li = (List<UOrder>) unSerializeToList(rsToJsonList(executeQuery("select o.*,c.name as courtname from u_order as o left join u_courtorder as uc on o.ordernum = uc.ordernum left join u_court as c on uc.courtid = c.id where o.createtime < '2016-05-13' order by o.id asc")), UOrder.class);
		moveDBTestService.moveOrder(li,stmt);
	}
}
