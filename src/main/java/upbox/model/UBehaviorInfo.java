package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 队伍首次行为记录表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_behavior_info")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UBehaviorInfo implements java.io.Serializable {
	private static final long serialVersionUID = 4601915095600776884L;
	private String keyId;
	private String behaviorId;
	private Date createDate;
	private String score;
	private Integer wcount;
	private Integer pcount;
	private Integer scount;
	private Integer gintegral;
	private Integer sintegral;
	private String eventsType;
	private String eventsTypeName;
	private String teamId;

	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "behavior_id", length = 20)
	public String getBehaviorId() {
		return behaviorId;
	}
	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}
	
	@Column(name = "score", length = 20)
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	@Column(name = "wcount", length = 20)
	public Integer getWcount() {
		return wcount;
	}
	public void setWcount(Integer wcount) {
		this.wcount = wcount;
	}
	
	@Column(name = "pcount", length = 20)
	public Integer getPcount() {
		return pcount;
	}
	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}
	
	@Column(name = "scount", length = 20)
	public Integer getScount() {
		return scount;
	}
	public void setScount(Integer scount) {
		this.scount = scount;
	}
	
	@Column(name = "gintegral", length = 20)
	public Integer getGintegral() {
		return gintegral;
	}
	public void setGintegral(Integer gintegral) {
		this.gintegral = gintegral;
	}
	
	@Column(name = "sintegral", length = 20)
	public Integer getSintegral() {
		return sintegral;
	}
	public void setSintegral(Integer sintegral) {
		this.sintegral = sintegral;
	}

	@Column(name = "events_type", length = 20)
	public String getEventsType() {
		return eventsType;
	}
	public void setEventsType(String eventsType) {
		this.eventsType = eventsType;
	}
	@Column(name = "team_id", length = 60)
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	@Transient
	public String getEventsTypeName() {
		return eventsTypeName;
	}
	public void setEventsTypeName(String eventsTypeName) {
		this.eventsTypeName = eventsTypeName;
	}
	@Override
	public String toString() {
		return "UBehaviorInfo [keyId=" + keyId + ", behaviorId=" + behaviorId + ", createDate=" + createDate
				+ ", score=" + score + ", wcount=" + wcount + ", pcount=" + pcount + ", scount=" + scount
				+ ", gintegral=" + gintegral + ", sintegral=" + sintegral + ", eventsType=" + eventsType
				+ ", eventsTypeName=" + eventsTypeName + ", teamId=" + teamId + "]";
	}
	
}