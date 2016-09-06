package upbox.servlet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.pub.GetPropertiesInfo;
import upbox.pub.Public_Cache;
import upbox.service.ParamService;


/**
 * 初始化数据
 * 服务器启动时加载初始化数据
 * @author wmq
 *
 * 15618777630
 */
@Controller("initServlet")
@Scope("prototype")
public class InitServlet extends HttpServlet{
	@Resource
	private RedisOperDAOImpl redisDao;
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private ParamService paramService;
	private static final long serialVersionUID = 1707875016398549678L;
	
	/**
	 * 启动时初始化
	 */
	@Override
	public void init() throws ServletException
	{
		super.init();
		try {
//			paramService.getParamCache();
			paramService.initParamCache();
			//httpsInterceptorInit(); //https拦截请求分析初始化
			setTourstConfig(); //设置游客访问接口初始化 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	   TODO - https拦截请求分析初始化
	   2016年1月21日
	   mazkc 
	private void httpsInterceptorInit(){
		COM_ORG_FinalArgs.PROJECT_NAME = Public_Cache.PROJECT_NAME;
		try {
			 HttpsRedisInterceptorUtil.getXmlValue(Public_Cache.PROJECT_NAME,"https_config.xml"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 
	 * 
	   TODO - 设置游客访问接口初始化 
	   2016年1月21日
	   mazkc
	 */
	private void setTourstConfig(){
		Public_Cache.TOURIST_CONFIG = GetPropertiesInfo.getTouristPropertiesInfo();
	}
}
