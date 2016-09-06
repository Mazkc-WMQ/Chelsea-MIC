package upbox.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户登陆位置信息记录表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_loginlbs")
public class ULoginlbs implements java.io.Serializable {
	private static final long serialVersionUID = -4955805414930274249L;
	private String keyId;
	private String userId;
	private String position;
	private Date createtime;
	private String poi;
	private String lbsLogType;
	private String city;
	private String district;
	private String country;
	private String address;
	private String cityid;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	/**
	 * @return the keyId
	 */
	public String getKeyId() {
		return keyId;
	}

	/**
	 * @param keyId the keyId to set
	 */
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "user_id")
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the createtime
	 */
	public Date getCreatetime() {
		return createtime;
	}

	/**
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	/**
	 * @return the poi
	 */
	public String getPoi() {
		return poi;
	}

	/**
	 * @param poi the poi to set
	 */
	public void setPoi(String poi) {
		this.poi = poi;
	}

	@Column(name = "lbs_log_type")
	/**
	 * @return the lbsLogType
	 */
	public String getLbsLogType() {
		return lbsLogType;
	}

	/**
	 * @param lbsLogType the lbsLogType to set
	 */
	public void setLbsLogType(String lbsLogType) {
		this.lbsLogType = lbsLogType;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the cityid
	 */
	public String getCityid() {
		return cityid;
	}

	/**
	 * @param cityid the cityid to set
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	
}