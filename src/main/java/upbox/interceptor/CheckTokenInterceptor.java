package upbox.interceptor;

import java.util.HashMap;
//import java.util.Map;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import upbox.aes.CipherHelper;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.UUser;
import upbox.pub.Public_Cache;
import upbox.service.UUserService;


/**
 * 验证token是否有效
 * @author wmq
 *
 * 15618777630
 */
public class CheckTokenInterceptor extends MethodFilterInterceptor {
	@Resource
	private OperDAOImpl baseDAO;
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
//				String actionM = actionMethod.substring(1, actionMethod.lastIndexOf("."));
//				Map<String, String> hashMap = Public_Cache.TOURIST_CONFIG;
//				if (null == hashMap.get(actionM) || "".equals(hashMap.get(actionM)) || "null".equals(hashMap.get(actionM))) {
//					 return "touristError"; 
//				}
				return ai.invoke();
			}else if ("1".equals(userStatus)) {//1就是登陆用户
				map.put("token", token);
				// 传过来的token不能为空
				if(null == token || "".equals(token) || "null".equals(token)) return "tokenError";
//				String redisToken = redisDao.getHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"), token);
				UUser user = uuserService.getUserinfoByToken(map);
				//UUser user = baseDAO.get(map, "from UUser where token=:token");
				if (null == user) {
					return "tokenError";
				}else{
					if ("3".equals(user.getUserStatus())) {
						return "loginFalse";
					}
				}
			}else{
				 return "touristIsNullError";
			}
		}
		return ai.invoke();
	}
	public static void main(String[] args) {
		System.out.println(CipherHelper.md5(CipherHelper.md5("111111")));
	}
}
