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
 * 擂主设擂历史记录表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_challenge_ch")
public class UChallengeCh implements java.io.Serializable {
	
	private static final long serialVersionUID = -6822740656567061778L;
	
	private String keyId;
	private UBUser UBUser;
	private UTeam UTeam;
	private Date stdate;
	private String sttime;
	private Date enddate;
	private String endtime;
	private UBrCourt UBrCourt;
	private String saleprice;
	private String costprice;
	private String wincount;
	private String remark;
	private String isChampion;
	private String championId;
	private String challengePayType;
	
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
	@JoinColumn(name = "br_court_id")
	public UBrCourt getUBrCourt() {
		return UBrCourt;
	}
	

	public void setUBrCourt(UBrCourt uBrCourt) {
		UBrCourt = uBrCourt;
	}
	
	@Column(name = "saleprice", length = 60)
	public String getSaleprice() {
		return saleprice;
	}
	
	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}
	
	@Column(name = "costprice", length = 60)
	public String getCostprice() {
		return costprice;
	}
	public void setCostprice(String costprice) {
		this.costprice = costprice;
	}
	
	@Column(name = "wincount", length = 60)
	public String getWincount() {
		return wincount;
	}
	public void setWincount(String wincount) {
		this.wincount = wincount;
	}
	
	@Column(name = "remark", length = 200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "is_champion", length = 30)
	public String getIsChampion() {
		return isChampion;
	}
	public void setIsChampion(String isChampion) {
		this.isChampion = isChampion;
	}
	
	@Column(name = "champion_id", length = 60) 
	public String getChampionId() {
		return championId;
	}
	public void setChampionId(String championId) {
		this.championId = championId;
	}

	@Column(name="challenge_pay_type")
	public String getChallengePayType() {
		return challengePayType;
	}
	public void setChallengePayType(String challengePayType) {
		this.challengePayType = challengePayType;
	}

	
	
}