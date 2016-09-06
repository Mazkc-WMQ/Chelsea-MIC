package upbox.pub;

import java.util.LinkedList;

/**
 * 声明常量专用
 * 
 */

public class Constant {
	
	//图像地址
	public static final String IMG_URL = "imgurl";
	//图标地址
	public static final String ICON_URL = "iconurl";
	//球场ID
	public static final String SUBCOURT_ID = "subcourt_id";
	//球场类型
	public static final String COURT_BR_TYPE = "court_br_type";
	//第一个球场ID
	public static final String FIRST_COURT_SUBCOURT_ID = "subcourt_id_tmp";
	//通过subcourt_id获取球场服务
	public static final String SUBCOURT_SERVICE_BY_ID = "scourtid";
	//服务列表
	public static final String SERVICE_LIST = "servicelist";
	//SQL查询获得数据条数
	public static final String SQL_COUNT_NUM = "cntno";
	//折扣场次数
	public static final String LABEL_B = "labelb";
	//页
	public static final String PAGE = "page";
	//用户名
	public static final String USER_NAME = "username";
	//用户图像？
	public static final String USER_IMG = "userimg";
	//用户状态
	public static final String USER_STATUS = "userStatus";
	//特殊处理球场标示
	public static final String ICON_FIRST = "iconFirst";
	//用户ID
	public static final String USER_ID = "user_id";
	//用户ID
	public static final String USERID = "userId";
	//当前日期
	public static final String TODAY = "today";
	//截止日期
	public static final String END_DATE = "enddate";
	//日期
	public static final String DATE = "date";
	
	
	//正常用户
	public static final String NORMAL_USER = "1";
	//游客
	public static final String VISITOR = "-10000";
	
	
	//主队全名 = 主队队名+主队简称
	public static final String HOME_TEAM_FULL_NAME = "fromteamname";
	//主队队名
	public static final String HOME_TEAM_NAME = "home_teamname";
	//主队简称
	public static final String HOME_TEAM_SHORT_NAME = "home_short_teamname";
	//主队队标
	public static final String HOME_TEAM_IMG = "home_teamimgurl";
	//主队ID
	public static final String HOME_TEAM_ID = "fromteamid";
	//主队ID
	public static final String F_TEAM_ID = "f_team_id";
	//客队全名 = 客队队名+客队简称
	public static final String AWAY_TEAM_FULL_NAME = "toteamname";
	//客队队名
	public static final String AWAY_TEAM_NAME = "away_teamname";
	//客队简称
	public static final String AWAY_TEAM_SHORT_NAME = "away_short_teamname";
	//客队队标
	public static final String AWAY_TEAM_IMG = "away_teamimgurl";
	//客队ID
	public static final String AWAY_TEAM_ID = "toteamid";
	//客队ID
	public static final String X_TEAM_ID = "x_team_id";
	//昵称
	public static final String NICKNAME = "nickname";
	
	
	//响应价格
	public static final String RESPONSE_PRICE = "responseprice";
	//状态
	public static final String STATUS = "status";
	//胜利次数
	public static final String WIN_COUNT = "wincount";
	//冠军ID
	public static final String CHAMP_ID = "champid";
	
	
	//session订单信息
	public static final String SESSION_ORDER_INFO = "sessionorderinfo";
	//session ID
	public static final String SESSION_ID = "session_id";
	//多个sessionID
	public static final String SESSION_IDS = "sessionids";
	//当前日期
	public static final String CURRENT_DATE = "cdate";
	//当前时间
	public static final String CURRENT_TIME = "ctime";
	
	
	//空场
	public static final String EMPTYS= "emptys";
	//挑战
	public static final String CHALLENGES = "challenges";
	//约战
	public static final String DUELS = "duels";
	//对战分数
	public static final String VS_SCORE = "vsscore";
	//成功（返回）
	public static final String SUCCESS = "success";
	
	//自营
	public static final String UPBOX = "1";
	//联营
	public static final String ASSOCIATES = "2";
	
	
	//订单ID
	public static final String ORDER_ID = "orderId";
	/**
	 * u_order.order_type
	 */
	//订单类型：球场
	public static final String COURT = "1";
	//订单类型：挑战
	public static final String CHALLENGE = "2";
	//订单类型：约战
	public static final String DUEL = "3";
	//订单类型：青训
	public static final String YOUTH_TRAINING = "4";
	//订单类型：成训
	public static final String ADULT_TRAINING = "5";
	//订单类型：商城
	public static final String MALL = "6";
	//订单类型：赛事
	public static final String TOURNAMENT = "7";
	
	/**
	 * u_br_courtsession.order_status
	 */
	//订单状态：已预定
	public static final String ORDER_BOOKED = "1";
	//订单状态：空场
	public static final String ORDER_EMPTY = "2";
	//订单状态：预定未付款
	public static final String ORDER_UNPAYED = "3";
	//订单状态：预定保护
	public static final String ORDER_PROTECTED = "4";
	//百度数据表ID
//	public static final String BAIDU_TABLE_ID = "143996";
	public static final String BAIDU_TABLE_ID = "142434";
	public static final String BAIDU_CHALLENGE_TABLE_ID = "145221";
	public static final String BAIDU_DUEL_TABLE_ID = "145219";
	public static final String BAIDU_TEAM_TABLE_ID = "145217";
	public static final String BAIDU_PLAYER_TABLE_ID = "145164";
	public static final String BAIDU_COURT_TABLE_ID = "146823";
	//操作记录请求地址
//	public static String UPBOX_LOG_URL = "http://10.4.36.175:9125/UpboxLog";//正式
//	public static String UPBOX_LOG_URL = "http://10.10.2.178:9091/UpboxLog";//蔡彬
	public static String UPBOX_LOG_URL = "http://183.131.153.79:9125/UpboxLog";//测试
	
	public static LinkedList<String> PUSH_CHECK_LIST = new LinkedList<>();
}
