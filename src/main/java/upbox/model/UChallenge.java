 package upbox.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 大场次响应信息表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_challenge")
public class UChallenge implements java.io.Serializable {
	private static final long serialVersionUID = 319413799441311782L;
	private String challengeId;
	private String challengeType;
	private UTeam fteam;
	private Date stdate;
	private Date sttime;
	private UTeam xteam;
	private String challengeStatus;
	private UOrder uorder;
	private UOrder perOrder;
	private UChallengeCh UChallengeCh;
	private UChallengeResp UChallengeResp;
	private Set<UChallengeBs> bsSet = new HashSet<UChallengeBs>(0);
	private String effectiveStatus;
	private String chRecommendStatus;
	private String jpusho;
	
	
	@Id
	@Column(name = "challenge_id", unique = true, nullable = false, length = 60)
	public String getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}
	
	@Column(name = "challenge_type", length = 20)
	public String getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(String challengeType) {
		this.challengeType = challengeType;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "f_team_id")
	public UTeam getFteam() {
		return fteam;
	}

	public void setFteam(UTeam fteam) {
		this.fteam = fteam;
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
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "x_team_id")
	public UTeam getXteam() {
		return xteam;
	}
	public void setXteam(UTeam xteam) {
		this.xteam = xteam;
	}
	
	@Column(name = "challenge_status", length = 20)
	public String getChallengeStatus() {
		return challengeStatus;
	}
	public void setChallengeStatus(String challengeStatus) {
		this.challengeStatus = challengeStatus;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUorder() {
		return uorder;
	}
	public void setUorder(UOrder uorder) {
		this.uorder = uorder;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fch_id")
	public UChallengeCh getUChallengeCh() {
		return UChallengeCh;
	}
	public void setUChallengeCh(UChallengeCh uChallengeCh) {
		UChallengeCh = uChallengeCh;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "xch_id")
	public UChallengeResp getUChallengeResp() {
		return UChallengeResp;
	}
	public void setUChallengeResp(UChallengeResp uChallengeResp) {
		UChallengeResp = uChallengeResp;
	}
	
	@Transient
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UChallenge")
	//@OrderBy(clause = "sttime")
	public Set<UChallengeBs> getBsSet() {
		return bsSet;
	}
	public void setBsSet(Set<UChallengeBs> bsSet) {
		this.bsSet = bsSet;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_per_id")
	public UOrder getPerOrder() {
		return perOrder;
	}
	public void setPerOrder(UOrder perOrder) {
		this.perOrder = perOrder;
	}
	
	@Column(name = "effective_status", length = 20) 
	public String getEffectiveStatus() {
		return effectiveStatus;
	}
	public void setEffectiveStatus(String effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}
	
	@Column(name = "ch_recommend_status", length = 20) 
	public String getChRecommendStatus() {
		return chRecommendStatus;
	}
	public void setChRecommendStatus(String chRecommendStatus) {
		this.chRecommendStatus = chRecommendStatus;
	}
	/**
	 * @return the jpusho
	 */
	@Column(name = "jpusho", length = 60) 
	public String getJpusho() {
		return jpusho;
	}
	/**
	 * @param jpusho the jpusho to set
	 */
	public void setJpusho(String jpusho) {
		this.jpusho = jpusho;
	}
	

}