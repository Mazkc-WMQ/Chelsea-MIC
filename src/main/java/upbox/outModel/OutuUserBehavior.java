package upbox.outModel;

import java.io.Serializable;

/**
 * 球队时间轴输出model
 * @author mercideng
 *
 */
public class OutuUserBehavior implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String keyId;
	private String createDate;
	private String userFollowType;
	private String userFollowTypeName;
	private String objectType;
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getuserFollowType() {
		return userFollowType;
	}
	public void setUserFollowType(String userFollowType) {
		this.userFollowType = userFollowType;
	}
	public String getuserFollowTypeName() {
		return userFollowTypeName;
	}
	public void setUserFollowTypeName(String userFollowTypeName) {
		this.userFollowTypeName = userFollowTypeName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
}
