package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;
/**
 * 约战条件信息
 * @author xiao
 */
@Entity
@Table(name = "u_duel_ch")
public class UDuelCh {
	private String keyId;
	private UUser UUser;
	private Date stdate;
	private String teamId;
	private String sttime;
	private UBrCourt UBrCourt;
	private String remark;
	private String duelPayType;
	private String duelAppoint;
	private String duelChStatus;
	private String payProportion;
	private String price;
	private String endtime;
	private String wincount;
	
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
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate",length = 13)
	public Date getStdate() {
		return stdate;
	}
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "court_id")
	public UBrCourt getUBrCourt() {
		return UBrCourt;
	}
	public void setUBrCourt(UBrCourt uBrCourt) {
		UBrCourt = uBrCourt;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "duel_pay_type")
	public String getDuelPayType() {
		return duelPayType;
	}
	public void setDuelPayType(String duelPayType) {
		this.duelPayType = duelPayType;
	}
	
	@Column(name = "duel_appoint")
	/**
	 * @return the duelAppoint
	 */
	public String getDuelAppoint() {
		return duelAppoint;
	}
	/**
	 * @param duelAppoint the duelAppoint to set
	 */
	public void setDuelAppoint(String duelAppoint) {
		this.duelAppoint = duelAppoint;
	}
	
	@Column(name = "duel_ch_status")
	/**
	 * @return the duelChStatus
	 */
	public String getDuelChStatus() {
		return duelChStatus;
	}
	/**
	 * @param duelChStatus the duelChStatus to set
	 */
	public void setDuelChStatus(String duelChStatus) {
		this.duelChStatus = duelChStatus;
	}
	
	@Column(name = "team_id")
	/**
	 * @return the team_id
	 */
	public String getTeamId() {
		return teamId;
	}
	/**
	 * @param team_id the team_id to set
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	@Column(name = "pay_proportion")
	/**
	 * @return the payProportion
	 */
	public String getPayProportion() {
		return payProportion;
	}
	/**
	 * @param payProportion the payProportion to set
	 */
	public void setPayProportion(String payProportion) {
		this.payProportion = payProportion;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the sttime
	 */
	public String getSttime() {
		return sttime;
	}
	/**
	 * @param sttime the sttime to set
	 */
	public void setSttime(String sttime) {
		this.sttime = sttime;
	}
	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}
	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	@Column(name = "wincount")
	public String getWincount() {
		return wincount;
	}
	public void setWincount(String wincount) {
		this.wincount = wincount;
	}
}
