package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 菜单角色映射表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_menu_role")
public class UMenuRole implements java.io.Serializable{
	private static final long serialVersionUID = -337507633986146178L;
	private String keyId;
	private String menuId;
	private String roleId;
	private Date createtime;
	private Date createdate;
	private String menuOerid;
	private String menuOperStatus;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId()
	{
		return keyId;
	}
	public void setKeyId(String keyId)
	{
		this.keyId = keyId;
	}
	@Column(name = "menu_id", length = 60)
	public String getMenuId()
	{
		return menuId;
	}
	public void setMenuId(String menuId)
	{
		this.menuId = menuId;
	}
	@Column(name = "role_id", length = 60)
	public String getRoleId()
	{
		return roleId;
	}
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createtime", length = 13)
	public Date getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate()
	{
		return createdate;
	}
	public void setCreatedate(Date createdate)
	{
		this.createdate = createdate;
	}
	@Column(name = "menu_oerid", length = 60)
	public String getMenuOerid()
	{
		return menuOerid;
	}
	public void setMenuOerid(String menuOerid)
	{
		this.menuOerid = menuOerid;
	}
	@Column(name = "menu_oper_status", length = 30)
	public String getMenuOperStatus()
	{
		return menuOperStatus;
	}
	public void setMenuOperStatus(String menuOperStatus)
	{
		this.menuOperStatus = menuOperStatus;
	}
}
