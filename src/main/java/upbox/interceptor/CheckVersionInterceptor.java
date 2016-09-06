package upbox.interceptor;

import java.util.HashMap;
//import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.pub.Public_Cache;
import upbox.service.UUserService;


/**
 * 验证token是否有效
 * @author wmq
 *
 * 15618777630
 */
public class CheckVersionInterceptor extends MethodFilterInterceptor { 
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private RedisOperDAOImpl redisDao;
	@Resource
	private UUserService uuserService;
	private static final long serialVersionUID = -5050333380560947030L;

	@Override
	protected String doIntercept(ActionInvocation ai) throws Exception {
		Public_Cache.pp.getPublicPropretiesInfo();
		ActionContext ctx = ai.getInvocationContext();       
        HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        //获取传过来的token
		String token = request.getParameter("token");
        //获取传过来的token
		String userStatus = request.getParameter("userStatus");
		//获取接口名称
//		String actionMethod = request.getServletPath();
		HashMap<String,String> map = new HashMap<>();
		//状态是否为空
		if (null == userStatus || "".equals(userStatus) || "null".equals(userStatus)) {
			 return "touristIsNullError";
		}else{
			if ("-1".equals(userStatus)) {
				return ai.invoke();
			}else if ("1".equals(userStatus)) {//1就是登陆用户
				map.put("token", token);
				// 传过来的token不能为空
				if(null == token || "".equals(token) || "null".equals(token)) return "tokenError";  
				String version = String.valueOf(redisDao.getHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token","Version"),map.get("token")));
				if (null == version || "".equals(version) || "null".equals(version) ) {
					redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token","Version"),map.get("token"), "1.0.0");//新建版本号 （key,userId）
				}else{
					redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token","Version"),map.get("token"));// 登录时token不一样删除缓存里的版本号
					redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token","Version"),map.get("token"), "1.0.0");//新建版本号 （key,userId）
				}
			}else{
				 return "touristIsNullError";
			}
		}
		return ai.invoke();
	}
}
