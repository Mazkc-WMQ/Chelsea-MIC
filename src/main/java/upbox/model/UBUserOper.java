package upbox.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 后端用户登录操作基类
 * @author wmq
 *
 * 15618777630
 */
public class UBUserOper implements Serializable{
	private static final long serialVersionUID = 5372256952187522272L;
	private String userOper;
	private List<HashMap<String,String>> sexList; //性别list 默认下标0的数据为选中数据
	private HashMap<String,Object> roleList; //角色list 默认下标0的数据为选中数据
	private List<HashMap<String,String>> qxList; //权限list 默认下标0的数据为选中数据
	
	public String getUserOper()
	{
		return userOper;
	}
	public void setUserOper(String userOper)
	{
		this.userOper = userOper;
	}
	public List<HashMap<String, String>> getSexList()
	{
		return sexList;
	}
	public void setSexList(List<HashMap<String, String>> sexList)
	{
		this.sexList = sexList;
	}
	public HashMap<String, Object> getRoleList()
	{
		return roleList;
	}
	public void setRoleList(HashMap<String, Object> roleList)
	{
		this.roleList = roleList;
	}
	public List<HashMap<String, String>> getQxList()
	{
		return qxList;
	}
	public void setQxList(List<HashMap<String, String>> qxList)
	{
		this.qxList = qxList;
	}
}
