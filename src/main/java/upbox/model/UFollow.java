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
 * 关注记录表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_follow")
public class UFollow implements java.io.Serializable {
	
	private static final long serialVersionUID = -7119270826382560763L;
	
	private String keyId;
	private String userFollowType;
	private UUser UUser;
	private Date createdate; //创建时间	
	private String objectId;
	private String followStatus;

	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@Column(name = "user_follow_type")
	/**
	 * @return the followBehaviorType
	 */
	public String getUserFollowType() {
		return userFollowType;
	}
	/**
	 * @param userFollowType the userFollowType to set
	 */
	public void setUserFollowType(String userFollowType) {
		this.userFollowType = userFollowType;
	}	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return UUser;
	}

	public void setUUser(UUser uUser) {
		UUser = uUser;
	}

	@Column(name = "object_id")
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Column(name = "follow_status")
	public String getFollowStatus() {
		return followStatus;
	}
	public void setFollowStatus(String followStatus) {
		this.followStatus = followStatus;
	}
	
	

	
	
	
}