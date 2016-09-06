package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import upbox.model.UPlayerRole;
import upbox.service.UPlayerRoleService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uPlayerRoleAction")
@Scope("prototype")
public class UPlayerRoleAction extends OperAction implements ModelDriven<UPlayerRole>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UPlayerRoleService UPlayerRoleService;
	private UPlayerRole uPlayerRole;
	private HashMap<String,Object> hashMap;
	

	/**
	 * 
	 * 
	   TODO - 角色转让
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public String memberTypeTransfer202Method(){
		try {
			hashMap = UPlayerRoleService.memberTypeTransfer202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * TODO 角色分配
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String editMemberTypeAllotMethod(){
		try {
			hashMap = UPlayerRoleService.editMemberTypeAllot(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 申请角色－弹出提示
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String checkApplyPlayerMemberTypeMethod(){
		try {
			hashMap = UPlayerRoleService.checkApplyPlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 申请角色
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String applyPlayerMemberTypeMethod(){
		try {
			hashMap = UPlayerRoleService.applyPlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 申请角色－同意
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String applyCheckAgreePlayerMemberTypeMethod(){
		try {
			hashMap = UPlayerRoleService.applyCheckAgreePlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 申请角色－拒绝
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String applyCheckRefusePlayerMemberTypeMethod(){
		try {
			hashMap = UPlayerRoleService.applyCheckRefusePlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * TODO 批量处理没有审核的申请数据
	 * @return
	 * xiaoying 2016年8月24日
	 */
	public String batchApplyRefusePlayerMemberTypeMethod(){
		try {
			hashMap = UPlayerRoleService.batchApplyRefusePlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	/**
	 * @return the hashMap
	 */
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}

	/**
	 * @param hashMap the hashMap to set
	 */
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}
	public UPlayerRole getUPlayerRole() {
		return uPlayerRole;
	}

	public void setUPlayerRole(UPlayerRole uPlayerRole) {
		this.uPlayerRole = uPlayerRole;
	}

	@Override
	public UPlayerRole getModel() {
		if(null == uPlayerRole)
			uPlayerRole = new UPlayerRole();
		return uPlayerRole;
	}

}
