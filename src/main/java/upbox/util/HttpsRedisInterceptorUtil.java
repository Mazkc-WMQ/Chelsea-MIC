package upbox.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.org.pub.PublicMethod;
import com.org.redis.dao.RedisUtilDao;

/**
 * 加载http拦截配置放入缓存
 * @author wmq
 *
 * 15618777630
 */
@Controller
public class HttpsRedisInterceptorUtil {
	@Resource
	private RedisUtilDao redisDao;
	private static HttpsRedisInterceptorUtil httpsRedisInterceptorUtil;  
	
	/** 
     * 构造方法执行后调用 init() 
     */  
    @PostConstruct  
    public void init() {
          httpsRedisInterceptorUtil = this;  
          httpsRedisInterceptorUtil.redisDao = this.redisDao;  
    }  
    
	/**
	 * 
	 * 
	   TODO - 解析HTTPS拦截的XML数据放入redis缓存
	   @throws Exception
	   2016年1月21日
	   mazkc
	 */
	public static void getXmlValue(String PROJECT_NAME,String httpsXml) throws Exception {
		ArrayList<Object> list = new ArrayList<>();//接收
		HashMap<String,String> hashMap = new HashMap<>();
		//删除缓存中的https
		httpsRedisInterceptorUtil.redisDao.removePatternKeys(PublicMethod.getHRedisKey(PROJECT_NAME,"Https"));
		//xml的路径
		String fileName = Thread.currentThread().getContextClassLoader()  
				 .getResource(httpsXml).getPath(); 
		hashMap.put("method", "method");
		//解析xml的数据
		list = PublicMethod.parserXml(fileName, hashMap);
		if (0 < list.size()) {
			for (Object object : list) {
				//Object转map，以Map<String, List<String>>形式接收
				Map<String, List<String>> objectTomap = objectToMap(object);
				//method的listvalue
				List<String> methodList = objectTomap.get("method");
				if (0 < methodList.size()) {
					//循环遍历methodList
					for (String methodVal : methodList) {
						String action = objectTomap.get("action").toString();
						//添加到缓存中去
						httpsRedisInterceptorUtil.redisDao.setHSet(PublicMethod.getHRedisKey(PROJECT_NAME,"Https",action.substring(1, action.lastIndexOf("]"))), methodVal, methodVal);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - Object转Map
	   @param object  {method=[ref, login], action=uuser}
	   @return
	   @throws Exception
	   2016年1月20日
	   dengqiuru
	 */
	private static Map<String, List<String>> objectToMap(Object object) throws Exception {
		//返回的map
		 Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		 String str = object.toString();
		 	//去掉最外面的大括号
			String strtmp= str.substring(1, str.lastIndexOf("}"));
			//以],进行分割
			String[] strs = strtmp.split("],");
			for (String string : strs) {
				 List<String> strList = new ArrayList<>();
				String[] keys = string.split("=");
				String values= keys[1].substring(keys[1].lastIndexOf("[")+1);
				String[] value = values.split(",");
				for (String strValue : value) {
					strList.add(strValue.trim());
				}
				resultMap.put(keys[0].trim(), strList);
			}
	        return resultMap;
	}
}
