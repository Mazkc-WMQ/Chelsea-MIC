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
 * 后端用户Bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_buser")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UBUser extends UBUserOper implements java.io.Serializable {
	private static final long serialVersionUID = 7264159445112847675L;
	private String userId;
	private URole URole;
	private String username;
	private String email;
	private String phone;
	private Date logindate;
	private Date logintime;
	private String dept;
	private String rank;
	private String loginname;
	private String loginpassword;
	private String remark;
	private String userStatus;
	private String sex;
	private Date birthday;
	private Date createtime;
	private Integer type;
	private Integer createtype;
	private Integer createfrom;
	private Date entertime;
	private String enterusername;
	private String loginfrom;
	private Date modifytime;
	private String modifyusername;
	private String adminType;

	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 60)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	public URole getURole() {
		return this.URole;
	}

	public void setURole(URole URole) {
		this.URole = URole;
	}

	@Column(name = "username", length = 30)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "email", length = 48)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone", length = 38)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	@Column(name = "dept", length = 30)
	public String getDept() {
		return this.dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@Column(name = "rank", length = 20)
	public String getRank() {
		return this.rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Column(name = "loginname", length = 100)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "loginpassword", length = 150)
	public String getLoginpassword() {
		return this.loginpassword;
	}

	public void setLoginpassword(String loginpassword) {
		this.loginpassword = loginpassword;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "user_br_status", length = 20)
	public String getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	@Column(name = "sex", length = 20)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "birthday", length = 13)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "createtime", length = 29)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "createtype")
	public Integer getCreatetype() {
		return this.createtype;
	}

	public void setCreatetype(Integer createtype) {
		this.createtype = createtype;
	}

	@Column(name = "createfrom")
	public Integer getCreatefrom() {
		return this.createfrom;
	}

	public void setCreatefrom(Integer createfrom) {
		this.createfrom = createfrom;
	}

	@Column(name = "entertime", length = 29)
	public Date getEntertime() {
		return this.entertime;
	}

	public void setEntertime(Date entertime) {
		this.entertime = entertime;
	}

	@Column(name = "enterusername", length = 60)
	public String getEnterusername() {
		return this.enterusername;
	}

	public void setEnterusername(String enterusername) {
		this.enterusername = enterusername;
	}

	@Column(name = "loginfrom", length = 120)
	public String getLoginfrom() {
		return this.loginfrom;
	}

	public void setLoginfrom(String loginfrom) {
		this.loginfrom = loginfrom;
	}

	@Column(name = "modifytime", length = 29)
	public Date getModifytime() {
		return this.modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Column(name = "modifyusername", length = 60)
	public String getModifyusername() {
		return this.modifyusername;
	}

	public void setModifyusername(String modifyusername) {
		this.modifyusername = modifyusername;
	}

	@Column(name = "admin_type", length = 30)
	public String getAdminType()
	{
		return adminType;
	}

	public void setAdminType(String adminType)
	{
		this.adminType = adminType;
	}
}