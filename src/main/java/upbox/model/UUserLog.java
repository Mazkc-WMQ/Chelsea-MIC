package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 前端用户操作记录bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_user_log")
public class UUserLog implements java.io.Serializable {
	private static final long serialVersionUID = 1610989881627536089L;
	private String pkId;
	private UUser UUser;
	private Date createdate;
	private Date createtime;
	private String regiType;
	private String regiResource;
	private Date BDate;
	private Date BTime;
	private String BUser;
	private Date FDate;
	private Date FTime;
	private Date logindate;
	private Date logintime;
	private String loginSource;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;


	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 60)
	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser()
	{
		return UUser;
	}

	public void setUUser(UUser uUser)
	{
		UUser = uUser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "regi_type", length = 20)
	public String getRegiType() {
		return this.regiType;
	}

	public void setRegiType(String regiType) {
		this.regiType = regiType;
	}

	@Column(name = "regi_resource", length = 20)
	public String getRegiResource() {
		return this.regiResource;
	}

	public void setRegiResource(String regiResource) {
		this.regiResource = regiResource;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "b_date", length = 13)
	public Date getBDate() {
		return this.BDate;
	}

	public void setBDate(Date BDate) {
		this.BDate = BDate;
	}

	@Column(name = "b_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getBTime() {
		return this.BTime;
	}

	public void setBTime(Date BTime) {
		this.BTime = BTime;
	}

	@Column(name = "b_user", length = 60)
	public String getBUser() {
		return this.BUser;
	}

	public void setBUser(String BUser) {
		this.BUser = BUser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "f_date", length = 13)
	public Date getFDate() {
		return this.FDate;
	}

	public void setFDate(Date FDate) {
		this.FDate = FDate;
	}

	@Column(name = "f_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getFTime() {
		return this.FTime;
	}

	public void setFTime(Date FTime) {
		this.FTime = FTime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "logindate", length = 13)
	public Date getLogindate() {
		return this.logindate;
	}

	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}

	@Column(name = "logintime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getLogintime() {
		return this.logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	@Column(name = "login_source", length = 20)
	public String getLoginSource() {
		return this.loginSource;
	}

	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "modifydate", length = 13)
	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	@Column(name = "modifytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getModifytime() {
		return this.modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Column(name = "modifyuser", length = 60)
	public String getModifyuser() {
		return this.modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

}