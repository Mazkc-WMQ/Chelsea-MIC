package upbox.interceptor;

import java.util.Date;
import java.util.HashMap;
//import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.ULoginLog;
import upbox.model.UUser;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.UUserService;


/**
 * 
 * @TODO 登录拦截器 【记录登录信息：登录时间，请求来源，版本号】
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月3日 下午5:26:05 
 * @version 1.0
 */
public class CheckLoginInterceptor extends MethodFilterInterceptor { 
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
        //获取传过来的userStatus【登录用户类型】
		String userStatus = request.getParameter("userStatus");
		//获取传过来的resource【请求来源】
		String resource = request.getParameter("resource");
		//获取传过来的code【版本号】
		String code = request.getParameter("code");
		
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
				if(null == token || "".equals(token) || "null".equals(token))
					return "tokenError";
				UUser user = uuserService.getUserinfoByToken(map);
				if (null == user) {
					return "tokenError";
				}else{
					if ("3".equals(user.getUserStatus())) {
						return "useStatusError";
					}
					//更新登录记录表
					ULoginLog uLoginLog = new ULoginLog();
					uLoginLog.setKey_id(WebPublicMehod.getUUID());
					uLoginLog.setUser_id(user.getUserId());
					uLoginLog.setAppcode(code);
					uLoginLog.setResource(resource);
					uLoginLog.setLogindate(new Date());
					
					baseDAO.saveOrUpdate(uLoginLog);
				}
			}else{
				 return "touristIsNullError";
			}
		}
		return ai.invoke();
	}
}
