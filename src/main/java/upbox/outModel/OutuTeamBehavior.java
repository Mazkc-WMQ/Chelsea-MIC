package upbox.outModel;

import java.io.Serializable;

/**
 * 球队时间轴输出model
 * @author mercideng
 *
 */
public class OutuTeamBehavior implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String keyId;
	private String createDate;
	private String teamBehaviorType;
	private String teamBehaviorTypeName;
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
	public String getTeamBehaviorType() {
		return teamBehaviorType;
	}
	public void setTeamBehaviorType(String teamBehaviorType) {
		this.teamBehaviorType = teamBehaviorType;
	}
	public String getTeamBehaviorTypeName() {
		return teamBehaviorTypeName;
	}
	public void setTeamBehaviorTypeName(String teamBehaviorTypeName) {
		this.teamBehaviorTypeName = teamBehaviorTypeName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	
}
