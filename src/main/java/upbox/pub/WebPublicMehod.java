package upbox.pub;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import net.sf.json.JSONObject;

/**
 * 公共方法
 * 
 * @author wmq
 *
 *         15618777630
 */
public class WebPublicMehod {
	/*
	 * 生成UUID 用于数据库主键策略
	 */
	public static String getUUID() throws Exception {
		return UUID.randomUUID().toString();
	}

	/**
	 * 通用返回MAP结构
	 * 
	 * @return
	 */
	public static HashMap<String, Object> returnRet(String key, Object val)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, val);
		if ("error".equals(key)) {
			throw new Exception(val.toString());
		}
		return map;
	}

	/**
	 * 
	 * 
	 TODO - 发送验证码到手机
	 * 
	 * @param mobile
	 *            手机号码
	 * @param verifyCode
	 *            验证码
	 * @return 2015年12月16日 dengqiuru
	 */
	public static HashMap<String, String> send(String mobile,
			HashMap<String, String> sendMap, HashMap<String, String> hashMap) {
		HashMap<String, String> resultMap = new HashMap<>();
		String result = null;
		try {
			HttpUtil http = new HttpUtil();
			// 发送短信的参数
			hashMap.put("extend", hashMap.get("extend")); // 回调参数
			hashMap.put("sms_free_sign_name", hashMap.get("sms_free_sign_name")); // 签名
			hashMap.put("sms_param", JSON.toJSONString(sendMap)); // 参数
			hashMap.put("rec_num", hashMap.get("rec_num")); // 手机号
			hashMap.put("sms_template_code", hashMap.get("sms_template_code")); // 模板ID
			// HttpRespons response =
			// http.sendPost("http://192.168.1.174:8080/PushPublic/push_pushDYMes.do",
			// hashMap);
			HttpRespons response = http.sendPost(Public_Cache.PUSH_SERVER
					+ "/push_pushDYMes.do", hashMap);// http://app.upbox.com.cn/push/push_pushDYMes.do
			HashMap<String, Object> map = (HashMap<String, Object>) PublicMethod
					.parseJSON2Map(response.getContent());
			if ("操作成功".equals(map.get("msg"))) {
				result = "发送成功！";
				resultMap.put("ret", "1");
				resultMap.put("result", result);
			}
		} catch (Exception e) {
			result = "发送失败！";
			resultMap.put("ret", "-1");
			resultMap.put("result", result);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 生成订单号
	 * 
	 * @return
	 */
	public static String getOrderNum() {
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		int random = new Random().nextInt(10000);
		return date + random;
	}
	/**
	 * 账户号码
	 * 
	 * @return
	 */
	public static String getCountNum(String phone) {
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		if(phone!=null&&phone.length()==11){
			return date + phone.substring(phone.length()-4);
		}
		return date;
		
	}

	/**
	 * 
	 * 
	 TODO - 获取码表参数缓存
	 * 
	 * @param str
	 *            1，2，3... 码表数值
	 * @param key
	 *            码表key
	 * @return xxx，xxx，xxx.... 2016年3月29日 mazkc
	 */
	public static String getCacheParams(String key, String str) {
		StringBuffer sb = new StringBuffer();
		if (null != str && !"".equals(str)) {
			String[] params = str.split(",");
			for (int i = 0; i < params.length; i++) {
				if (i < params.length - 1) {
					sb.append(Public_Cache.HASH_PARAMS(key).get(params[i])
							+ ",");
				} else {
					sb.append(Public_Cache.HASH_PARAMS(key).get(params[i]));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 时间字符串优化时间(加 年 月 日) 根据指定格式的时间字符串获取年份，月份，日期
	 * 
	 * @param pTime
	 *            时间字符串 = 2017/12/25
	 * @param format
	 *            指定格式 = /
	 * @return result
	 * @Exception 发生异常
	 */
	public static String getYMDByFormat(String pTime, String format)
			throws Exception {
		String result = pTime.trim();
		result = pTime.replaceFirst(format, "年");
		result = result.replaceFirst(format, "月");
		result = result + '日';
		return result;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(Object obj) {
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name,
						@SuppressWarnings("rawtypes") Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 判断日期
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return 1 DATE1在DATE2前 -1 DATE1在DATE2后
	 */
	public static int compare_date(Date DATE1, Date DATE2) {
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			// Date dt1 = DATE1.parse(DATE1);
			// Date dt2 = DATE2.parse(DATE2);
			if (DATE1.getTime() > DATE2.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (DATE1.getTime() < DATE2.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * TODO 转换星期:如输入1，返回周一
	 * 
	 * @param week
	 * @return String 2016年5月6日
	 */
	public static String checkWeekString(String week) throws Exception {
		String returnWeek = "";
		switch (week) {
		case "1":
			returnWeek = "周一";
			break;
		case "2":
			returnWeek = "周二";
			break;
		case "3":
			returnWeek = "周三";
			break;
		case "4":
			returnWeek = "周四";
			break;
		case "5":
			returnWeek = "周五";
			break;
		case "6":
			returnWeek = "周六";
			break;
		case "7":
			returnWeek = "周日";
			break;
		default:
			break;
		}
		return returnWeek;
	}

	/**
	 * 解析LBS组件返回
	 * 
	 * @param str
	 * @return
	 */
	public static HashMap<String, Object> checkLBS_Response(String str) {
		HashMap<String, Object> hash = new HashMap<>();
		JSONObject json = null;
		if (null != str && !"".equals(str)) {
			json = JSONObject.fromObject(str);
			hash = (HashMap<String, Object>) PublicMethod.parseJSON2Map(json
					.getString("result"));
		}
		return hash;
	}

	// 判断是不是英文字母
	public static boolean isEnglish(String charaString) {
		return charaString.matches("^[a-zA-Z]*");
	}
	
	
	/**
	 * 转化版本号为int
	 * @param appCode
	 * @return
	 */
	public static int intAppCode(String appCode){
		int intAppCode=0;
		if(appCode!=null){
			appCode=appCode.replace(".", "");
			intAppCode=Integer.parseInt(appCode);
		}
		return intAppCode;
	}
}
