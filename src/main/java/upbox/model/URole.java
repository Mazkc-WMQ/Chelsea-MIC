package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 后端用户角色表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_role")
public class URole implements java.io.Serializable {
	private static final long serialVersionUID = -6589993382923479263L;
	private String roleId;
	private String createUser;
	private String name;
	private Date createdate;
	private Date createtime;
	private String isStatus;

	@Id
	@Column(name = "role_id", unique = true, nullable = false, length = 60)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "createuser", length = 60)
	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "createtime", length = 15)  
	@JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "is_status", length = 30)
	public String getIsStatus()
	{
		return isStatus;
	}

	public void setIsStatus(String isStatus)
	{
		this.isStatus = isStatus;
	}
}