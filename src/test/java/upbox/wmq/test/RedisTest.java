package upbox.wmq.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;
import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;
import com.org.pub.HttpsUtil;

import upbox.service.RedisTestService;

/**
 * Hibernate + Redis测试用例
 * @author wmq
 *
 * 15618777630
 */
@ContextConfiguration("applicationContext.xml")
public class RedisTest extends AbstractJUnit4SpringContextTests{
	@Resource
	private RedisTestService redisTestService;

	/**
	 * 
	 * 
	   TODO - 测试用户模块详情
	   KEY=WebPublicMehod.getHRedisKey(项目名+'Object');
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void testUUserInfo() throws Exception {
		redisTestService.testUUserInfo();
	}
	 
	/**
	 * 
	 * 
	   TODO - 测试用户模块列表
	   KEY=WebPublicMehod.getHRedisKey(项目名+"ListPage");
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void testUUserList() throws Exception {
		redisTestService.testUUserList();
	}
	
	
	/**
	 * 
	 * 
	   TODO - 缓存更新机制测试
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	@Test
	public void testRedisClean() throws Exception {
		redisTestService.testRedisClean();
	}
	
	/**
	 * 
	 * 
	   TODO - 列表缓存数据单独更新测试
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void signUpdateRedisList() throws Exception{
		redisTestService.signUpdateRedisList();
	}
	
	public static void main(String[] args) throws IOException {
//		HttpsUtil https = new HttpsUtil();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("X-OP-Id","33e02056-3dcb-4607-b132-6f43b1c91d90");
		map.put("X-OP-Key","4eb01a74-1d8e-488f-9991-27573525348d");
//		JSONObject json = https.httpRequest("https://openapi-staging.openplay.com/v1/seasons/", "GET", map,map);
//		System.out.println(json.toString());
		
		HttpUtil hp = new HttpUtil();
		HttpRespons resp = hp.sendGet("https://openapi-staging.openplay.com/v1/seasons/", map, map);
		System.out.println(resp.getContent());
	}
}
