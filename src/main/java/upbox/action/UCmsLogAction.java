package upbox.action;

import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import upbox.model.UCmsLog;
import upbox.service.UCmsLogService;
/**
 * 前端CMSaction
 * 
 *
 */
@Controller("uCmsLogAction")
@Scope("prototype")
public class UCmsLogAction extends OperAction implements ModelDriven<UCmsLog>  {
	private static final long serialVersionUID = -2137605138380569250L;
	@Resource
	private UCmsLogService uCmsLogService;
	private UCmsLog uCmsLog;
	private HashMap<String, Object> hashMap;

	/**
	 * 
	 * 
	 TODO - 新建第三方球场
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String getCmsListByTeamIdMethod() {
		try {
			 hashMap = uCmsLogService.getCmsListByTeamId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCmsLogAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	@Override
	public UCmsLog getModel() {
		if (null == uCmsLog)
			uCmsLog = new UCmsLog();
		return uCmsLog;
	}
	
	
}
