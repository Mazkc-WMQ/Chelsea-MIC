package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import upbox.model.UTeamImg;
import upbox.service.UTeamFormationService;
import upbox.service.UTeamImgService;

/**
 * 球队阵型action
 * @author yuancao
 *
 * 13611929818
 */
@Controller("uTeamFormationAction")
@Scope("prototype")
public class UTeamFormationAction extends OperAction {
	
	private static final long serialVersionUID = -6416191641792759289L;
	
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UTeamImgService uTeamImgService;
	
	@Resource
	private UTeamFormationService uTeamFormationService;
	
	private HashMap<String,Object> hashMap;
	

	/**
	 * 获取此球队所有阵型的下拉选内容
	 * @return 
	 * @throws Exception
	 */
	public String getAllFormationSelListMethod(){
		try {
			hashMap = uTeamFormationService.getAllFormationSelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 获取当前阵型的详细内容
	 * @return 
	 * @throws Exception
	 */
	public String getFormationInfoMethod(){
		try {
			hashMap = uTeamFormationService.getFormationInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 保存初始化的当前阵型
	 * @return 
	 * @throws Exception
	 */
	public String saveInitFormationInfoMethod(){
		try {
			hashMap = uTeamFormationService.saveInitFormationInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 修改当前阵型
	 * @return 
	 * @throws Exception
	 */
	public String updateFormationInfoMethod(){
		try {
			hashMap = uTeamFormationService.updateFormationInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 删除当前阵型
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String delFormationInfoMethod(){
		try {
			hashMap = uTeamFormationService.delFormationInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 查询符合添加阵型的球员
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String selectFormationPlayersMethod(){
		try {
			hashMap = uTeamFormationService.selectFormationPlayers(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamFormationAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	
	
	
	
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}


	
}
