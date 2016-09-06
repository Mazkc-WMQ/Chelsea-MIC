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
 * 挑战响应表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_challenge_resp")
public class UChallengeResp implements java.io.Serializable {
	
	private static final long serialVersionUID = 3767515110693979294L;

	private String keyId;
	private UBUser UBUser;
	private UTeam UTeam;
	private Date stdate;
	private Date sttime;
	private String remark;
	private String challengeRespStatus;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UBUser getUBUser() {
		return UBUser;
	}
	public void setUBUser(UBUser uBUser) {
		UBUser = uBUser;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return stdate;
	}
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	
	@Column(name = "sttime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getSttime() {
		return sttime;
	}
	public void setSttime(Date sttime) {
		this.sttime = sttime;
	}
	

	
	@Column(name = "remark", length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "challenge_resp_status", length = 20)
	public String getChallengeRespStatus() {
		return challengeRespStatus;
	}
	public void setChallengeRespStatus(String challengeRespStatus) {
		this.challengeRespStatus = challengeRespStatus;
	}

	
	
	
	
	


	
	
	
}