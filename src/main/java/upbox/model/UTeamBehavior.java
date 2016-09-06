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
import javax.persistence.Transient;
import javax.print.DocFlavor.STRING;

/**
 * 队伍首次行为记录表
 * 【里程碑】
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_team_behavior")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTeamBehavior implements java.io.Serializable {
	private static final long serialVersionUID = -2785681356786612076L;
	private String keyId;
	private String teamBehaviorType;
	private String teamBehaviorTypeName;
	private UTeam uTeam;
	private Date createDate;
	private Date createtime;
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
	
	
	@Column(name = "team_behavior_type", length = 20)
	/**
	 * @return the teamBehaviorType
	 */
	public String getTeamBehaviorType() {
		return teamBehaviorType;
	}
	/**
	 * @param teamBehaviorType the teamBehaviorType to set
	 */
	public void setTeamBehaviorType(String teamBehaviorType) {
		this.teamBehaviorType = teamBehaviorType;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getuTeam() {
		return uTeam;
	}
	public void setuTeam(UTeam uTeam) {
		this.uTeam = uTeam;
	}
	
//	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
//	@Column(name = "createdate", length = 20)
//	public Date getCreateDate() {
//		return createDate;
//	}
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
	
	@Temporal(TemporalType.DATE)
//	@JSON(format = "yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "createtime", length = 15)
	@JSON(format = "HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	public String getTeamBehaviorTypeName() {
		return teamBehaviorTypeName;
	}
	public void setTeamBehaviorTypeName(String teamBehaviorTypeName) {
		this.teamBehaviorTypeName = teamBehaviorTypeName;
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
}