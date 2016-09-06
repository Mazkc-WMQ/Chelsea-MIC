package upbox.wmq.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;
 

/**
 * 测试短信
 * @author wmq
 *
 * 15618777630
 */
@ContextConfiguration("applicationContext.xml")
public class SendMessageTest extends AbstractJUnit4SpringContextTests{
	private HttpUtil http = new HttpUtil();
	private HttpRespons response;
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
	public void printInfo(String temp,Object obj){
		//System.out.println(temp + JSON.toJSONString(obj));
	}
	
	@Test
	public void testUUserInfo() throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("extend", "123456");
		map.put("sms_free_sign_name", "注册验证");
		map.put("sms_param", "{\"code\":\"123456\",\"product\":\"UPBOX激战联盟\"}");
		map.put("rec_num", "15618777630");
		map.put("sms_template_code", "SMS_7215107");
		response = http.sendPost("http://app.upbox.com.cn/push/push_pushDYMes.do", map);
		HashMap<String, Object> hmap = (HashMap<String, Object>) PublicMethod.parseJSON2Map(response.getContent());
		printInfo("------------------->", response.getContent());
	}
}
