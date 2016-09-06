package upbox.interceptor;

import java.util.HashMap;
//import java.util.Map;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UTeam;
import upbox.pub.Public_Cache;
import upbox.service.PublicService;
import upbox.service.UTeamService;
import upbox.service.UUserService;


/**
 * 验证球队是否有效
 * @author wmq
 *
 * 15618777630
 */
public class CheckTeamStatusInterceptor extends MethodFilterInterceptor {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UUserService uuserService;
	@Resource
	private UTeamService uTeamService;
	
	@Resource
	private PublicService publicService;
	private static final long serialVersionUID = -5050333380560947030L;

	@Override
	protected String doIntercept(ActionInvocation ai) throws Exception {
		Public_Cache.pp.getPublicPropretiesInfo();
		ActionContext ctx = ai.getInvocationContext();       
        HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);
        HashMap<String, String> map = new HashMap<String, String>();
        //获取传过来的teamId
		String teamId = request.getParameter("teamId");
		//获取接口名称
		String actionMethod = request.getServletPath();
		String actionM = actionMethod.substring(1, actionMethod.lastIndexOf("."));
		Map<String, String> hashMap = Public_Cache.TOURIST_CONFIG;
		if (publicService.StringUtil(teamId)) {
			//不为空  验证球队类型
			map.put("teamId", teamId);
			UTeam uTeam = uTeamService.findPlayerInfoById(map);
			if (null != uTeam) {
				if ("1".equals(uTeam.getTeamStatus()) || "3".equals(uTeam.getTeamStatus())) {
					if ("1".equals(uTeam.getTeamUseStatus())) {
						return "uTeamDisAble";
					}else if ("3".equals(uTeam.getTeamUseStatus())) {
						return "uTeamDisBand";
					}else{
						return ai.invoke();
					}
				}else if ("2".equals(uTeam.getTeamStatus())) {//球队审核未通过
					return "uTeamNoPassCheck";
				}else{
					return "uTeamDisBand";
				}
			}
		}else{
			return ai.invoke();
		}
		return ai.invoke();
	}
}
