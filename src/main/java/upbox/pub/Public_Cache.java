package upbox.pub;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import upbox.model.UParameterInfo;
import upbox.model.UPublicPrpe;
import upbox.service.FilterService;
import upbox.service.ParamService;
import upbox.service.PublicPropretiesService;

/**
 * 公共缓存
 * 
 * @author charlescai
 */
@Controller
public class Public_Cache {
	@Resource
	private PublicPropretiesService publicPropreties;
	@Resource
	private FilterService filterService;

	@Resource
	protected ParamService paramService;
	
	public void setPublicPropreties(PublicPropretiesService publicPropreties) {
		this.publicPropreties = publicPropreties;
	}

	public static Public_Cache pp;
	// 推送服务器地址
	public static String PUSH_SERVER = "";
	// 场次插入周期
	public static int SESSION_WEEK = 0;
	// 场次商品有效天数
	public static int SESSION_DAY = 0;
	// upyun空间地址
	public static String UPYUN_BUCKET_NAME = "";
	// upyun此空间用户名
	public static String UPYUN_OPERATOR_NAME = "";
	// upyun此空间密码
	public static String UPYUN_OPERATOR_PWD = "";
	// upyun此空间秘钥
	public static String UPYUN_OPERATOR_KEY = "";
	// 短信、微信接口服务
	public static String JAVAWX_CODE = "";
	// 项目名
	public static String PROJECT_NAME = "";
	// 分页条数
	public static int PAGE_LIMIT = 0;
	// 发送短信user_id
	public static String MESSAGE_USERID = "";
	// 发送短信内容content
	public static String MESSAGE_CONTENT = "";

	// 支付后的回调
	public static String PAY_CALLBACK = "";
	// 天梯榜限制条数
	public static String RANKING_LIMIT = "";
	// 球队最大限制人数
	public static int TEAM_MAXIMUM = 0;
	// 订单过期时间
	public static int ORDER_TIME = 0;
	// 响应订单时效
	public static int ORDER_RESP_TIME = 0;
	//订单即将过期时效
	public static int ORDER_TIME_STATUS = 0;
	// 游客可访问接口配置
	public static HashMap<String, String> TOURIST_CONFIG = null;
	// 码表参数缓存
	public static HashMap<String, HashMap<String, String>> HASH_PARAMS = null;
	// 码表参数缓存-对象
	public static HashMap<String, HashMap<String, UParameterInfo>> HASH_PARAMS_OBJECT = null;
	// 订单类型页面分配-场地
	public static String ORDER_TYPE_COURT = "";
	// 订单类型页面分配-服务
	public static String ORDER_TYPE_SERVICE = "";
	//订单类型页面分配-场地
//	public static String ORDER_TYPE_COURT="";
//	//订单类型页面分配-服务
//	public static String ORDER_TYPE_SERVICE="";
	public static int TEAM_COUNT = 0; 
	//极光推送key
	public static String JPUSH_APPKEY ="";
	//极光推送密码
	public static String JPUSH_MASTERSC = "";
	//极光推送IOS环境 true 生产  false 开发
	public static String IOS_OPTIONS  = "";
	//挑战固定裁判
	public static String CHALLENGE_REFEREE="";
	//挑战固定摄像
	public static String CHALLENGE_CAMERAS="";
	
	//APP新版本发布时间
	public static String APP_COMMIT_TIME="";
	//筛选球队日期1
	public static String FILTER_TEAM_TIME;
	//筛选球队日期2
	public static String FILTER_TEAM_TIMES;
	//百度LBS的key
	public static String BAIDU_LBS;
	//Global推送的限制推送条数
	public static String GLOBAL_PUSH_LIMIT;
	//lbs组件访问地址
	public static String LBS_LOCATION;
	//招募球员通讯录录入地址
	public static String UPBOX_LOG_URL;
	//招募球员通讯录录入地址
//	public static int BEHAVIORCOUNT_INPLAYER = 0;
	
	//赛事后台调用地址
	public static String MATCH_SYSTEM_URL ="";
	
	//角色等级-一级
	public static String MEMBER_TYPE_LVL1 ="";
	//角色等级-二级
	public static String MEMBER_TYPE_LVL2 ="";
	//申请角色7天后拒绝
	public static int APPLAY_REFUSE=0;
	
	@PostConstruct
	public void init() {
		//System.out.println("I'm  init  method  using  @PostConstrut....");
		pp = this;
		pp.publicPropreties = this.publicPropreties;

		pp.paramService = this.paramService;
		// 获取参数
		getPublicPropretiesInfo();
		try {
			filterService.findFileTeamAreaBuffter(null);//球队地区缓存
			filterService.findFilePlayerAreaBuffter(null);//球员地区缓存
			filterService.findFileCourtAreaBuffter(null);//球场地区缓存
			filterService.findFileChallengeAreaBuffter(null);//挑战地区缓存
			filterService.findFileDuelAreaBuffter(null);//挑战地区缓存
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 码表小类参数缓存
	public static HashMap<String, String> HASH_PARAMS(String params){
		try {
			return pp.paramService.getParamInfoMap(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 码表小类参数缓存-对象
	public static HashMap<String, UParameterInfo> HASH_PARAMS_OBJECT(String params){
		try {
			return pp.paramService.getParamInfoMapEntity(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	// 调用service
	public void getPublicPropretiesInfo() {
		List<UPublicPrpe> publicPrpeList = pp.publicPropreties.getPublicPropreties();
		HashMap<String, String> propreMap = new HashMap<String, String>();
		for (int i = 0; i < publicPrpeList.size(); i++) {
			propreMap.put(publicPrpeList.get(i).getName(), publicPrpeList.get(i).getValue());
		}
		// 推送服务器地址
		PUSH_SERVER = propreMap.get("PUSH_SERVER");
		//百度LBS的key
		BAIDU_LBS = propreMap.get("BAIDU_LBS");
		// 场次插入周期
		SESSION_WEEK = Integer.parseInt(propreMap.get("SESSION_WEEK"));
		// 场次商品有效天数
		SESSION_DAY = Integer.parseInt(propreMap.get("SESSION_DAY"));
		// 队伍有效人数
		TEAM_COUNT = Integer.parseInt(propreMap.get("TEAM_COUNT"));
		// upyun空间地址
		UPYUN_BUCKET_NAME = propreMap.get("UPYUN_BUCKET_NAME");
		// upyun此空间用户名
		UPYUN_OPERATOR_NAME = propreMap.get("UPYUN_OPERATOR_NAME");
		// upyun此空间密码
		UPYUN_OPERATOR_PWD = propreMap.get("UPYUN_OPERATOR_PWD");
		// upyun此空间秘钥
		UPYUN_OPERATOR_KEY = propreMap.get("UPYUN_OPERATOR_KEY");
		// 短信、微信接口服务
		JAVAWX_CODE = propreMap.get("JAVAWX_CODE");
		// 项目名
		PROJECT_NAME = propreMap.get("PROJECT_NAME");
		// 分页条数
		PAGE_LIMIT = Integer.parseInt(propreMap.get("PAGE_LIMIT"));
		// 发送短信user_id
		MESSAGE_USERID = propreMap.get("MESSAGE_USERID");
		// 发送短信内容content
		MESSAGE_CONTENT = propreMap.get("MESSAGE_CONTENT");
		// 支付后的回调
		PAY_CALLBACK = propreMap.get("PAY_CALLBACK");
		// 天梯榜限制条数
		RANKING_LIMIT = propreMap.get("RANKING_LIMIT");
		// 球队最大限制人数
		TEAM_MAXIMUM = Integer.parseInt(propreMap.get("TEAM_MAXIMUM"));
		// 订单失效时间
		ORDER_TIME = Integer.parseInt(propreMap.get("ORDER_TIME"));
		// 订单类型页面分配-场地
		ORDER_TYPE_COURT = propreMap.get("ORDER_TYPE_COURT");
		// 订单类型页面分配-服务
		ORDER_TYPE_SERVICE = propreMap.get("ORDER_TYPE_SERVICE");
		
		TEAM_MAXIMUM = Integer.parseInt(propreMap.get("TEAM_MAXIMUM"));
		//订单失效时间
		ORDER_TIME = Integer.parseInt(propreMap.get("ORDER_TIME"));
		// 响应订单时效
		ORDER_RESP_TIME = Integer.parseInt(propreMap.get("ORDER_RESP_TIME"));
		//订单即将过期时效
	    ORDER_TIME_STATUS = Integer.parseInt(propreMap.get("ORDER_TIME_STATUS"));
		//订单类型页面分配-场地
		ORDER_TYPE_COURT=propreMap.get("ORDER_TYPE_COURT");
		//订单类型页面分配-服务
		ORDER_TYPE_SERVICE=propreMap.get("ORDER_TYPE_SERVICE");
		//挑战固定裁判
		CHALLENGE_REFEREE=propreMap.get("CHALLENGE_REFEREE");
		//挑战固定摄像
		CHALLENGE_CAMERAS=propreMap.get("CHALLENGE_CAMERAS");
		//极光推送key
		JPUSH_APPKEY = propreMap.get("JPUSH_APPKEY");
		//极光推送密码
		JPUSH_MASTERSC = propreMap.get("JPUSH_MASTERSC");
		//极光推送IOS环境 true 生产  false 开发
		IOS_OPTIONS  = propreMap.get("IOS_OPTIONS");
		//APP新版本发布时间
		APP_COMMIT_TIME= propreMap.get("APP_COMMIT_TIME");
		//筛选球队日期1
		FILTER_TEAM_TIME  = propreMap.get("FILTER_TEAM_TIME");
		//筛选球队日期2
		FILTER_TEAM_TIMES= propreMap.get("FILTER_TEAM_TIMES");
		
		//Global推送的限制推送条数
		GLOBAL_PUSH_LIMIT = propreMap.get("GLOBAL_PUSH_LIMIT");
		
		//lbs组件访问地址
		LBS_LOCATION = propreMap.get("LBS_LOCATION");

		//招募球员通讯录录入地址
		UPBOX_LOG_URL = propreMap.get("UPBOX_LOG_URL");

		//球员详情里里程碑显示数
//		BEHAVIORCOUNT_INPLAYER = Integer.parseInt(propreMap.get("BEHAVIORCOUNT_INPLAYER"));
		
		//赛事后台调用地址
		MATCH_SYSTEM_URL = propreMap.get("MATCH_SYSTEM_URL");
		
		//角色等级-一级
		MEMBER_TYPE_LVL1=propreMap.get("MEMBER_TYPE_LVL1");
		//角色等级-二级
		MEMBER_TYPE_LVL2=propreMap.get("MEMBER_TYPE_LVL2");
		//申请角色7天后拒绝
		APPLAY_REFUSE=Integer.parseInt(propreMap.get("APPLAY_REFUSE"));
	}

	/**
	 * 通知文案
	 * 
	 * @param type
	 * @param mesType
	 * @return.
	 */
	public static String getMessageCon(String type, String mesType) {
		String _temp = "";
		if (type.equals("order")) {
			switch (mesType) {
			case "orBook":
				_temp = "你已成功预订XXX球场，赶紧通知激友们";
				break;
			case "orPay":
				_temp = "你的订单XXX已成功支付";
				break;
			case "orTimeOver":
				_temp = "你的订单XXX已过期，请重新发起";
				break;
			case "orTimeQu":
				_temp = "你的订单XXX即将过期，请尽快完成支付";
				break;
			default:
				break;
			}
		} else if (type.equals("team")) {
			switch (mesType) {
			case "tmJoin":
				_temp = "球星XXX已签约球队";
				break;
			case "tmKick":
				_temp = "你已被队长踢出XXX";
				break;
			case "tmExit":
				_temp = "球星XXX已与球队解约";
				break;
			case "tmDissolve":
				_temp = "XXX已申请破产";
				break;
			case "tmInvite":
				_temp = "你已被邀请加入XXX";
				break;
			case "tmUnImg":
				_temp = "您的球队XXX队徽违规";
				break;
			case "tmTransfer":
				_temp = "XXX";
				break;
			case "tmUnName":
				_temp = "您的球队XXX队名违规";
				break;
			case "tmUnShortName":
				_temp = "您的球队XXX队伍简称违规";
				break;
			case "tmUnRemark":
				_temp = "您的球队XXX队伍简介徽违规";
				break;
			case "tmApply":
				_temp = "XXX";
				break;
			case "tmApplyYet":
				_temp = "XXX";
				break;
			case "tmApplyRefuse":
				_temp = "XXX";
				break;
			case "tmApplyAgree":
				_temp = "XXX";
				break;
			case "tmApplyAllot":
				_temp = "XXX";
				break;
			default:
				break;
			}
		} else if (type.equals("fight")) {
			switch (mesType) {
			case "ftAnswerduel":
				_temp = "XXX已响应你的约战";
				break;
			case "ftAnswerdek":
				_temp = "XXX已确认攻擂";
				break;
			case "ftDataFinish":
				_temp = "约战数据已为你拍马送到";
				break;
			default:
				break;
			}
		} else if (type.equals("topic")){
			switch (mesType){
				case "topicTeam":
					_temp = "XXX在您的球队下发布了话题";
					break;
				case "topicUser":
					_temp = "XXX发布了关于您的话题";
					break;
			}
		}
		return _temp;
	}

	/**
	 * 推送文案
	 * @param type
	 * @return
	 */
	public static String getPushCon(String type) {
		String _temp = "";
		switch (type) {
		case "pushResponse":
			_temp = "你发起的约战还没有人响应，去分享让更多人知道吧。";
			break;
		case "pushUnSponsor":
			_temp = "你尚未在预定的球场中发起约战，请前往发起。";
			break;
		case "pushRemindReach":
			_temp = "你在XXX球场的预定即将开始，请提前到场准备哦~";
			break;
		case "ftAnswerduel":
			_temp = "XXX球队已响应你的约战";
			break;
		case "ftAnswerdek":
			_temp = "XXX球队已确认攻擂";
			break;
		case "ftDataFinish":
			_temp = "约战数据已为你拍马送到";
			break;
		case "orPay":
			_temp = "你的订单XXX球场已成功支付";
			break;
		case "orTimeOver":
			_temp = "你的订单XXX球场已过期，请重新发起";
			break;
		case "orWaitPay":
			_temp = "你有一笔球场订单待付款，请尽快完成支付";
			break;
		case "orTimeQu":
			_temp = "你的订单XXX球场即将过期";
			break;
		case "tmJoin":
			_temp = "球星XXX已签约球队";
			break;
		case "tmKick":
			_temp = "你已被队长踢出XXX";
			break;
		case "tmExit":
			_temp = "球星XXX已与球队解约";
			break;
		case "tmDissolve":
			_temp = "XXX已申请破产";
			break;
		case "tmInvite":
			_temp = "你已被邀请加入XXX";
			break;
		case "tmTransfer":
			_temp = "XXX";
			break;
		case "version":
			_temp = "激战联盟APP当前版本正式退役，更强更全能的2.0版本已全面上线！赶快更新换他上场吧！";
			break;
		case "repairPlayer":
			_temp = "马上要约战了，可是作为球队大牌的你，履历还是空白！队长希望你赶快更新简历，先吓唬一下对手！";
			break;
		case "repairTeam":
			_temp = "你的球队简介和球队相册还是空白，过于神秘的球队没有人敢约战，快去更新一下吧！";
			break;
		case "sponsorDuel":
			_temp = "7人就能组成球队，一键立即发起约战，赶快成立自己的球队来一场说踢就踢的约战吧！";
			break;
		case "repairTeamAndPlayer":
			_temp = "激战联盟APP更强更全能的2.0版已全面上线！你的球队简介还是空白，你的个人履历平淡无奇，赶快更新一下迎战更强的对手！";
			break;
		case "topicComment":
			_temp = "XXX回复了您的话题";
			break;
		case "topicThumbs":
			_temp = "XXX点赞了您发布的话题";
			break;
		case "commentToComment":
			_temp = "XXX回复了您的评论";
			break;
		case "topicTeam":
			_temp = "XXX在您的球队下发布了话题";
			break;
		case "topicUser":
			_temp = "XXX发布了关于您的话题";
			break;
		case "tmApply":
			_temp = "XXX";
			break;
		case "tmApplyYet":
			_temp = "XXX";
			break;
		case "tmApplyRefuse":
			_temp = "XXX";
			break;
		case "tmApplyAgree":
			_temp = "XXX";
			break;
		case "tmApplyAllot":
			_temp = "XXX";
			break;
			
		default:
			break;
		}
		return _temp;
	}
	
	/**
	 * 
	 * @TODO 短信文案
	 * @Title: getSPCon 
	 * @param type
	 * @return
	 * @author charlesbin
	 * @date 2016年6月3日 下午1:44:53
	 */
	public static String getSPCon(String type) {
		String _temp = "";
		switch (type) {
		case "repairTeamAndPlayer":
			_temp = "激战联盟2.0已上线！赶快更新信息约战强敌！ http://t.cn/R4Abm8U ";
			break;
		case "tmRecruitPlayer":
			_temp = "您的好友XXX邀您加入他的足球队，请与他并肩作战！http://t.cn/R4Rz8Me ";
			break;
		case "tmUserByAge":
			_temp = "夏不为利球场见！周一至周五16-20点，暑期学生免费场正式上线！http://t.cn/R4Abm8U ";
			break;
		default:
			break;
		}
		return _temp;
	}
	
	/**
	 * 短信推送短信签名
	 * @param type
	 * @return
	 */
	public static String getPhoneSignName(String type) {
		String _temp = "";
		switch (type) {
		case "pushResponse":
			_temp = "约战分享通知";
			break;
		case "pushUnSponsor":
			_temp = "发起约战通知";
			break;
		case "pushRemindReach":
			_temp = "到场踢球通知2";
			break;
		case "ftAnswerduel":
			_temp = "约战响应通知";
			break;
		case "ftAnswerdek":
			_temp = "挑战响应通知";
			break;
		default:
			break;
		}
		return _temp;
	}
	
	/**
	 * 短信推送短信模板ID
	 * @param type
	 * @return
	 */
	public static String getPhoneTemplateCode(String type) {
		String _temp = "";
		switch (type) {
		case "pushResponse":
			_temp = "SMS_7761461";
			break;
		case "pushUnSponsor":
			_temp = "SMS_7771405";
			break;
		case "pushRemindReach":
			_temp = "SMS_8190352";
			break;
		case "ftAnswerduel":
			_temp = "SMS_8205356";
			break;
		case "ftAnswerdek":
			_temp = "SMS_8205355";
			break;
		case "orPay":
			_temp = "SMS_8130639";
			break;
		case "orTimeOver":
			_temp = "SMS_8185641";
			break;
		case "orWaitPay":
			_temp = "SMS_8135726";
			break;
		case "orTimeQu":
			_temp = "SMS_8155939";
		default:
			break;
		}
		return _temp;
	}
}
