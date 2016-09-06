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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 约战比赛信息
 * @author xiao
 */
@Entity
@Table(name = "u_duel_bs")
public class UDuelBs {
	private String bsId;
	private UDuel UDuel;
	private UTeam UTeam;
	private UTeam XUTeam;
	private String fgoal;
	private String ffj;
	private String kgoal;
	private String kfj;
	private String matchStatus;
	private Date stdate;
	private String sttime;
	private Date enddate;	
	private String endtime;
	private URuleTopParameter URuleTopParameter;
	private List<UDuelPlayerinfo> listDuelPlayerinfo;
	private Set<UDuelPlayerinfo> UDuelPlayerinfo=new HashSet<UDuelPlayerinfo>();
	
	@Id
	@Column(name = "bs_id", unique = true, nullable = false, length = 60)
	public String getBsId() {
		return bsId;
	}
	public void setBsId(String bsId) {
		this.bsId = bsId;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "duel_id")
	public UDuel getUDuel() {
		return UDuel;
	}
	public void setUDuel(UDuel uDuel) {
		UDuel = uDuel;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fteam_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "xteam_id")
	public UTeam getXUTeam() {
		return XUTeam;
	}
	public void setXUTeam(UTeam xUTeam) {
		XUTeam = xUTeam;
	}

	@Column(name = "f_goal")
	public String getFgoal() {
		return fgoal;
	}
	public void setFgoal(String fgoal) {
		this.fgoal = fgoal;
	}
	@Column(name = "f_fj")
	public String getFfj() {
		return ffj;
	}
	public void setFfj(String ffj) {
		this.ffj = ffj;
	}
	@Column(name = "k_goal")
	public String getKgoal() {
		return kgoal;
	}
	public void setKgoal(String kgoal) {
		this.kgoal = kgoal;
	}
	@Column(name = "k_fj")
	public String getKfj() {
		return kfj;
	}
	public void setKfj(String kfj) {
		this.kfj = kfj;
	}
	@Column(name = "match_status")
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UDuelBs")
	//@OrderBy("date,time")
	public Set<UDuelPlayerinfo> getUDuelPlayerinfo() {
		return UDuelPlayerinfo;
	}
	public void setUDuelPlayerinfo(Set<UDuelPlayerinfo> uDuelPlayerinfo) {
		UDuelPlayerinfo = uDuelPlayerinfo;
	}
	
	@Transient
	public List<UDuelPlayerinfo> getListDuelPlayerinfo() {
		return listDuelPlayerinfo;
	}
	public void setListDuelPlayerinfo(List<UDuelPlayerinfo> listDuelPlayerinfo) {
		this.listDuelPlayerinfo = listDuelPlayerinfo;
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_top_id")
	public URuleTopParameter getURuleTopParameter() {
		return URuleTopParameter;
	}
	public void setURuleTopParameter(URuleTopParameter uRuleTopParameter) {
		URuleTopParameter = uRuleTopParameter;
	}
	
}
