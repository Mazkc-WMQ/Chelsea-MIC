package upbox.action;

import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import upbox.model.UIntergralInfo;
import upbox.service.IntegralService;

/**
 * 激数action
 * @author yc
 *
 */
@Controller("integralAction")
@Scope("prototype")
public class IntegralAction extends OperAction implements ModelDriven<UIntergralInfo>{
	

	private static final long serialVersionUID = -491177893595167078L;
	
	@Resource
	private IntegralService integralService;
	private HashMap<String,Object> hashMap;
	UIntergralInfo UIntergralInfo;
	
	/**
	 * 所有的激数列表
	 * @return
	 * @throws Exception 
	 */
	public String getIntegralListWithBsMethod() throws Exception{
		try
		{
			hashMap =integralService.getIntegralListWithBs(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	
	@Override
	public UIntergralInfo getModel()
	{
		if(null == UIntergralInfo)
			UIntergralInfo = new UIntergralInfo();
		return UIntergralInfo;
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}


	
	

	
	
}
