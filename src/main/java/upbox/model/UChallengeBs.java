 package upbox.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.apache.struts2.json.annotations.JSON;

/**
 * 小场次表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_challenge_bs")
public class UChallengeBs implements java.io.Serializable {
	

	private static final long serialVersionUID = -1829304861536714435L;
	
	private String bsId;	
	private UChallenge UChallenge;
	private UTeam bsFteam;
	private UTeam bsXteam;
	private String fqGoal;
	private String fqFj;
	private String xyGoal;
	private String xyFj;
	private String matchStatus;
	private Date stdate;
	private String sttime;
	private Date enddate;	
	private String endtime;
	private URuleTopParameter URuleTopParameter;
	private List<UChallengePlayerInfo> plList;
	private Set<UChallengePlayerInfo> plSet = new HashSet<UChallengePlayerInfo>(0);
	
	@Id
	@Column(name = "bs_id", unique = true, nullable = false, length = 60)
	public String getBsId() {
		return bsId;
	}
	public void setBsId(String bsId) {
		this.bsId = bsId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "challenge_id")
	public UChallenge getUChallenge() {
		return UChallenge;
	}
	public void setUChallenge(UChallenge uChallenge) {
		UChallenge = uChallenge;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fteam_id")
	public UTeam getBsFteam() {
		return bsFteam;
	}
	public void setBsFteam(UTeam bsFteam) {
		this.bsFteam = bsFteam;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "xteam_id")
	public UTeam getBsXteam() {
		return bsXteam;
	}
	public void setBsXteam(UTeam bsXteam) {
		this.bsXteam = bsXteam;
	}
	
	@Column(name = "f_goal", length = 10)
	public String getFqGoal() {
		return fqGoal;
	}
	public void setFqGoal(String fqGoal) {
		this.fqGoal = fqGoal;
	}
	
	@Column(name = "f_fj", length = 10)
	public String getFqFj() {
		return fqFj;
	}
	public void setFqFj(String fqFj) {
		this.fqFj = fqFj;
	}
	
	@Column(name = "k_goal", length = 10)
	public String getXyGoal() {
		return xyGoal;
	}
	public void setXyGoal(String xyGoal) {
		this.xyGoal = xyGoal;
	}
	
	@Column(name = "k_fj", length = 10)
	public String getXyFj() {
		return xyFj;
	}
	public void setXyFj(String xyFj) {
		this.xyFj = xyFj;
	}
	
	@Column(name = "match_status", length = 20)
	public String getMatchStatus() {
		return matchStatus;
	}
	
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return stdate;
	}
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	
	@Column(name = "sttime", length = 20)
	public String getSttime() {
		return sttime;
	}
	public void setSttime(String sttime) {
		this.sttime = sttime;
	}
	
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "enddate", length = 13)
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	@Column(name = "endtime", length = 20)
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	@Transient
	public List<UChallengePlayerInfo> getPlList() {
		return plList;
	}
	public void setPlList(List<UChallengePlayerInfo> plList) {
		this.plList = plList;
	}
	
	@Transient
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UChallengeBs")
	public Set<UChallengePlayerInfo> getPlSet() {
		return plSet;
	}
	public void setPlSet(Set<UChallengePlayerInfo> plSet) {
		this.plSet = plSet;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_top_id")
	public URuleTopParameter getURuleTopParameter() {
		return URuleTopParameter;
	}
	public void setURuleTopParameter(URuleTopParameter uRuleTopParameter) {
		URuleTopParameter = uRuleTopParameter;
	}
	
	
	
	
	



	
	


	
	
	
}