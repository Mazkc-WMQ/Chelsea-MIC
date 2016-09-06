package upbox.model;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 前端用户信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_user")
//@Document(collection="u_user")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UUser implements java.io.Serializable {
	private static final long serialVersionUID = 8677645148221759091L;
	private String userId;
	private String userType;
	private String username;
	private String nickname;
	private String phone;
	private String email;
	private String pwd;
	private String problem;
	private String answer;
	private String userStatus;
	private String realname;
	private String sex;
	private String sexName;
	private Date birthday;
	private Integer age;
	private String horo;
	private String bloodType;
	private String height;
	private String weight;
	private String bust;
	private String waist;
	private String hipline;
	private String country;
	private String city;
	private URegion uRegion;
	private Set<URegion> uRegionSet = new HashSet<>(0);//给前端返回区域的整体信息
	private String address;
	private String code;
	private Date createdate;
	private String idcode;
	private Date codetime;
	private String openid;
	private String remark;
	private String weibologintoken;
	private String qqlogintoken;
	private String wechatlogintoken;
	private String regi_resouce;
	private String numberid;
	private int old_key_id;
	private int user_id_int;
	private String token;
	private String version;//版本号
//	private String imgcode;
	private String useStatus;//设置用户状态  1:用户；2：游客
	private String regiStatus;//用户第三方状态  1：注册接口   未走关联直接注册 2：关联接口  没有关联已经注册 3：关联接口   没有关联没有注册
	private Set<UUserImg> UUserImg = new HashSet<UUserImg>(0);

	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 60)
	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	@Column(name = "user_type", length = 20)
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "username", length = 100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "nickname", length = 100)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "phone", length = 38)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "email", length = 150)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "pwd", length = 150)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "problem", length = 150)
	public String getProblem() {
		return this.problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	@Column(name = "answer", length = 150)
	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Transient
	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	
	@Column(name = "user_status", length = 20)
	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	@Transient
	public String getRegiStatus() {
		return regiStatus;
	}

	public void setRegiStatus(String regiStatus) {
		this.regiStatus = regiStatus;
	}

	@Column(name = "realname", length = 50)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
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

	@Column(name = "age")
	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "horo", length = 20)
	public String getHoro() {
		return this.horo;
	}

	public void setHoro(String horo) {
		this.horo = horo;
	}

	@Column(name = "blood_type", length = 20)
	public String getBloodType() {
		return this.bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	@Column(name = "height", length = 20)
	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "weight", length = 20)
	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return the bust
	 */
	public String getBust() {
		return bust;
	}

	/**
	 * @param bust the bust to set
	 */
	public void setBust(String bust) {
		this.bust = bust;
	}

	/**
	 * @return the waist
	 */
	public String getWaist() {
		return waist;
	}

	/**
	 * @param waist the waist to set
	 */
	public void setWaist(String waist) {
		this.waist = waist;
	}

	/**
	 * @return the hipline
	 */
	public String getHipline() {
		return hipline;
	}

	/**
	 * @param hipline the hipline to set
	 */
	public void setHipline(String hipline) {
		this.hipline = hipline;
	}

	@Column(name = "country", length = 30)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "city", length = 30)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "area")
	public URegion getuRegion() {
		return uRegion;
	}

	public void setuRegion(URegion uRegion) {
		this.uRegion = uRegion;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "code", length = 30)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}
	
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "idcode")
	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	@Column(name = "codetime")
	public Date getCodetime() {
		return codetime;
	}

	public void setCodetime(Date codetime) {
		this.codetime = codetime;
	}

	@Column(name = "token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Transient
	public Set<URegion> getuRegionSet() {
		return uRegionSet;
	}

	public void setuRegionSet(Set<URegion> uRegionSet) {
		this.uRegionSet = uRegionSet;
	}

	/**
	 * @return the openid
	 */
	@Column(name = "openid", length = 20)
	public String getOpenid() {
		return openid;
	}

	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "remark", length = 20)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the weibologintoken
	 */
	@Column(name = "weibologintoken", length = 20)
	public String getWeibologintoken() {
		return weibologintoken;
	}

	/**
	 * @param weibologintoken the weibologintoken to set
	 */
	public void setWeibologintoken(String weibologintoken) {
		this.weibologintoken = weibologintoken;
	}

	/**
	 * @return the qqlogintoken
	 */
	@Column(name = "qqlogintoken", length = 20)
	public String getQqlogintoken() {
		return qqlogintoken;
	}

	/**
	 * @param qqlogintoken the qqlogintoken to set
	 */
	public void setQqlogintoken(String qqlogintoken) {
		this.qqlogintoken = qqlogintoken;
	}

	/**
	 * @return the wechatlogintoken
	 */
	@Column(name = "wechatlogintoken", length = 20)
	public String getWechatlogintoken() {
		return wechatlogintoken;
	}

	/**
	 * @param wechatlogintoken the wechatlogintoken to set
	 */
	public void setWechatlogintoken(String wechatlogintoken) {
		this.wechatlogintoken = wechatlogintoken;
	}

	/**
	 * @return the regi_resouce
	 */
	@Column(name = "regi_resouce", length = 20)
	public String getRegi_resouce() {
		return regi_resouce;
	}

	/**
	 * @param regi_resouce the regi_resouce to set
	 */
	public void setRegi_resouce(String regi_resouce) {
		this.regi_resouce = regi_resouce;
	}

	/**
	 * @return the numberid
	 */
	@Column(name = "numberid", length = 20)
	public String getNumberid() {
		return numberid;
	}

	/**
	 * @param numberid the numberid to set
	 */
	public void setNumberid(String numberid) {
		this.numberid = numberid;
	}

	/**
	 * @return the old_key_id
	 */
	@Column(name = "old_key_id", length = 20)
	public int getOld_key_id() {
		return old_key_id;
	}

	public void setOld_key_id(int old_key_id) {
		this.old_key_id = old_key_id;
	}


	@Transient
	/**
	 * @return the uUserImg
	 */
	public Set<UUserImg> getUUserImg() {
		return UUserImg;
	}

	/**
	 * @param uUserImg the uUserImg to set
	 */
	public void setUUserImg(Set<UUserImg> uUserImg) {
		UUserImg = uUserImg;
	}


	@Transient
	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	/**
	 * @return the user_id_int
	 */
	public int getUser_id_int() {
		return user_id_int;
	}

	/**
	 * @param user_id_int the user_id_int to set
	 */
	public void setUser_id_int(int user_id_int) {
		this.user_id_int = user_id_int;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
//	@Column(name = "imgcode", length = 20)
//	public String getImgcode() {
//		return imgcode;
//	}
//
//	public void setImgcode(String imgcode) {
//		this.imgcode = imgcode;
//	}
	/**
	 * @return the old_key_id
	 
	public int getOld_key_id() {
		return old_key_id;
	}

	/**
	 * @param old_key_id the old_key_id to set
	 
	public void setOld_key_id(int old_key_id) {
		this.old_key_id = old_key_id;
	}*/
	
	
}