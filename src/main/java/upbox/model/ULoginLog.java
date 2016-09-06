package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @TODO 登录记录表
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月3日 下午5:53:57 
 * @version 1.0
 */
@Entity
@Table(name = "u_upbox_log.u_login_log")
public class ULoginLog implements java.io.Serializable {
	private static final long serialVersionUID = 426128958083250535L;
	private String key_id;
	private String user_id;
	private String appcode;
	private String resource;
	private Date logindate;
	

	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKey_id() {
		return key_id;
	}
	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}
	
	@Column(name = "user_id", length = 60)
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	@Column(name = "appcode", length = 30)
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	
	@Column(name = "resource", length = 30)
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@Column(name = "logindate", length = 15)
	public Date getLogindate() {
		return logindate;
	}
	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}
	
}