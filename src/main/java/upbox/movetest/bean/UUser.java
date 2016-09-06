package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户注册信息表
 * @author mazkc
 *
 */
@Entity
@Table(name = "u_user")
public class UUser implements java.io.Serializable {
	private static final long serialVersionUID = -6775871798416686546L;
	private Integer id;
	private String nickname;
	private Integer headid;
	private Date createtime;
	private String phone;
	private Integer identity;
	private String openid;
	private Integer identityid;
	private Integer userstatus;
	private String remark;
	private String birthday;
	private Integer sex;
	private Integer bdstatus;
	private Date phonetime;
	private String phonecode;
	private String email;
	private String loginname;
	private String loginpassword;
	private Date apptokentime;
	private String apptokencode;
	private String weibologintoken;
	private String qqlogintoken;
	private String wechatlogintoken;
	private Integer resource;
	private Integer loginstatus;
	private int numberid;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "nickname", length = 24)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "headid")
	public Integer getHeadid() {
		return this.headid;
	}

	public void setHeadid(Integer headid) {
		this.headid = headid;
	}

	@Column(name = "createtime", length = 29)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "phone", length = 11)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "identity")
	public Integer getIdentity() {
		return this.identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "identityid")
	public Integer getIdentityid() {
		return this.identityid;
	}

	public void setIdentityid(Integer identityid) {
		this.identityid = identityid;
	}

	@Column(name = "userstatus")
	public Integer getUserstatus() {
		return this.userstatus;
	}

	public void setUserstatus(Integer userstatus) {
		this.userstatus = userstatus;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "birthday", length = 50)
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name = "sex")
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "bdstatus")
	public Integer getBdstatus() {
		return this.bdstatus;
	}

	public void setBdstatus(Integer bdstatus) {
		this.bdstatus = bdstatus;
	}

	@Column(name = "phonetime", length = 29)
	public Date getPhonetime() {
		return this.phonetime;
	}

	public void setPhonetime(Date phonetime) {
		this.phonetime = phonetime;
	}

	@Column(name = "phonecode", length = 50)
	public String getPhonecode() {
		return this.phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "loginname", length = 100)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "loginpassword", length = 100)
	public String getLoginpassword() {
		return this.loginpassword;
	}

	public void setLoginpassword(String loginpassword) {
		this.loginpassword = loginpassword;
	}

	@Column(name = "apptokentime", length = 29)
	public Date getApptokentime() {
		return this.apptokentime;
	}

	public void setApptokentime(Date apptokentime) {
		this.apptokentime = apptokentime;
	}

	@Column(name = "apptokencode", length = 80)
	public String getApptokencode() {
		return this.apptokencode;
	}

	public void setApptokencode(String apptokencode) {
		this.apptokencode = apptokencode;
	}

	@Column(name = "weibologintoken", length = 100)
	public String getWeibologintoken() {
		return this.weibologintoken;
	}

	public void setWeibologintoken(String weibologintoken) {
		this.weibologintoken = weibologintoken;
	}

	@Column(name = "qqlogintoken", length = 100)
	public String getQqlogintoken() {
		return this.qqlogintoken;
	}

	public void setQqlogintoken(String qqlogintoken) {
		this.qqlogintoken = qqlogintoken;
	}

	@Column(name = "wechatlogintoken", length = 100)
	public String getWechatlogintoken() {
		return this.wechatlogintoken;
	}

	public void setWechatlogintoken(String wechatlogintoken) {
		this.wechatlogintoken = wechatlogintoken;
	}

	@Column(name = "resource")
	public Integer getResource() {
		return this.resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}

	public Integer getLoginstatus()
	{
		return loginstatus;
	}

	public void setLoginstatus(Integer loginstatus)
	{
		this.loginstatus = loginstatus;
	}

	public int getNumberid()
	{
		return numberid;
	}

	public void setNumberid(int numberid)
	{
		this.numberid = numberid;
	}
	
}