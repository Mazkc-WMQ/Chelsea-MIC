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
 * 球场用户映射表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_court_user")
public class UCourtUser implements java.io.Serializable{
	private static final long serialVersionUID = -337507633986146178L;
	private String keyId;
	private String courtId;
	private String userId;
	private Date createtime;
	private Date createdate;
	private String createuser;
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
	@Column(name = "court_id", length = 60)
	public String getCourtId()
	{
		return courtId;
	}
	public void setCourtId(String courtId)
	{
		this.courtId = courtId;
	}
	@Column(name = "user_id", length = 60)
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
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
	@Column(name = "createuser", length = 60)
	public String getCreateuser()
	{
		return createuser;
	}
	public void setCreateuser(String createuser)
	{
		this.createuser = createuser;
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
