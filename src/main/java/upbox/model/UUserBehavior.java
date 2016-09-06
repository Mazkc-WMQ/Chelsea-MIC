package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户首次行为记录表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_user_behavior")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UUserBehavior implements java.io.Serializable {

	private static final long serialVersionUID = -5974969891448264708L;
	private String keyId;
	private String userBehaviorType	;
	private String userBehaviorTypeName	;
	private UUser UUser;
	private Date createDate;
	private String objectId;
	private String objectType;
	private String remark;
	private String jsonStr;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@Column(name = "user_behavior_type", length = 20)
	/**
	 * @return the userBehaviorType
	 */
	public String getUserBehaviorType() {
		return userBehaviorType;
	}
	/**
	 * @param userBehaviorType the userBehaviorType to set
	 */
	public void setUserBehaviorType(String userBehaviorType) {
		this.userBehaviorType = userBehaviorType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return UUser;
	}
	
	public void setUUser(UUser uUser) {
		this.UUser = uUser;
	}
	
	@Column(name = "createdate", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "object_id", length = 20)
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	@Column(name = "object_type", length = 20)
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Transient
	public String getUserBehaviorTypeName() {
		return userBehaviorTypeName;
	}
	public void setUserBehaviorTypeName(String userBehaviorTypeName) {
		this.userBehaviorTypeName = userBehaviorTypeName;
	}
	@Column(name = "remark", length = 100)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Transient
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	@Override
	public String toString() {
		return "UUserBehavior [keyId=" + keyId + ", userFollowType=" + userBehaviorType + ", UUser=" + UUser
				+ ", createDate=" + createDate + ", objectId=" + objectId + "]";
	}
	
}