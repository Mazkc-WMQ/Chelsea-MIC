package upbox.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 响应约战信息
 * @author xiao
 */
@Entity
@Table(name = "u_duel_resp")
public class UDuelResp {
	private String keyId;
	private UDuel UDuel;
	private UUser UUser;
	private UTeam UTeam;
	private Date stdate;
	private Date sttime;
	private String challengeRespStatus;
	private String remark;
	private String respDuelStatus;
	private String act;
	private UOrder UOrder;
	private List<UDuelBs> listDuelBs;
	private String rRespDuelStatus;
	private String fchId;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	@Column(name = "stdate")
	public Date getStdate() {
		return stdate;
	}
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	@Column(name = "sttime")
	public Date getSttime() {
		return sttime;
	}
	public void setSttime(Date sttime) {
		this.sttime = sttime;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "challenge_resp_status")
	public String getChallengeRespStatus() {
		return challengeRespStatus;
	}
	public void setChallengeRespStatus(String challengeRespStatus) {
		this.challengeRespStatus = challengeRespStatus;
	}
	@Column(name = "resp_duel_status")
	public String getRespDuelStatus() {
		return respDuelStatus;
	}
	public void setRespDuelStatus(String respDuelStatus) {
		this.respDuelStatus = respDuelStatus;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUOrder() {
		return UOrder;
	}
	public void setUOrder(UOrder uOrder) {
		UOrder = uOrder;
	}
	@Transient
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	@Transient
	public List<UDuelBs> getListDuelBs() {
		return listDuelBs;
	}
	public void setListDuelBs(List<UDuelBs> listDuelBs) {
		this.listDuelBs = listDuelBs;
	}
	@Column(name = "r_resp_duel_status")
	/**
	 * @return the rRespDuelStatus
	 */
	public String getrRespDuelStatus() {
		return rRespDuelStatus;
	}
	/**
	 * @param rRespDuelStatus the rRespDuelStatus to set
	 */
	public void setrRespDuelStatus(String rRespDuelStatus) {
		this.rRespDuelStatus = rRespDuelStatus;
	}
	@Column(name = "fch_id")
	/**
	 * @return the fchId
	 */
	public String getFchId() {
		return fchId;
	}
	/**
	 * @param fchId the fchId to set
	 */
	public void setFchId(String fchId) {
		this.fchId = fchId;
	}
}
