package upbox.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import com.org.pub.PublicMethod;

import upbox.model.UParameter;
import upbox.service.UParameterService;

/**
 * 前端参数action
 * @author dengqiuru
 *
 */
@Controller("uParameterAction")
@Scope("prototype")
public class UParameterAction extends OperAction implements ModelDriven<UParameter>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UParameterService uParameterService;
	private UParameter uParameter;
	private List<UParameter> uParameterList;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * 
	   TODO - 获取参数明细
	   @param uParameterList
	   		param的uParameterList
	   @return
	   		
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	public String getUParameterInfoListMethod(){
		try {
			List<Map<String, Object>> uParameterList = PublicMethod.parseJSON2List(super.getParams().get("uParameterList")); 
			hashMap = uParameterService.getUParameterInfoList(uParameterList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UParameterAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 角色列表
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public String getMemberTypeListByTeamId202Method(){
		try {
			hashMap = uParameterService.getMemberTypeListByTeamId202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UParameterAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 申请角色＋分配角色列表
	 * @return
	 * xiaoying 2016年8月8日
	 */
	public String getMemberTypeListByApplyJoinTeamMethod(){
		try {
			hashMap = uParameterService.getMemberTypeListByApplyJoinTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UParameterAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改排序
	   @return
	   2016年8月15日
	   dengqiuru
	 */
	public String updateRankMethod(){
		try {
			hashMap = uParameterService.updateRank(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UParameterAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	@Override
	public UParameter getModel() {
		if(null == uParameter)
			uParameter = new UParameter();
		return uParameter;
	}
	public UParameter getuParameter() {
		return uParameter;
	}
	public void setuParameter(UParameter uParameter) {
		this.uParameter = uParameter;
	}
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}

	public List<UParameter> getuParameterList() {
		return uParameterList;
	}

	public void setuParameterList(List<UParameter> uParameterList) {
		this.uParameterList = uParameterList;
	}
	
}
