package upbox.model;

import java.util.Date;

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

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "u_duel")
public class UDuel implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String duelId;
	private String duelType;
	private UDuelCh UDuelCh;
	private UUser UUser;
	private UTeam UTeam;
	private Date stdate;
	private Date sttime;
	private String duelStatus;
	private UOrder UOrder;
	private String duelRecommendStatus;	
	private String effectiveStatus;
	private String jpusho;
	
	/**
	 * @return the duelId
	 */
	@Id
	@Column(name = "duel_id", unique = true, nullable = false, length = 60)
	public String getDuelId() {
		return duelId;
	}
	/**
	 * @param duelId the duelId to set
	 */
	public void setDuelId(String duelId) {
		this.duelId = duelId;
	}
	/**
	 * @return the duelType
	 */
	@Column(name = "duel_type", length = 20)
	public String getDuelType() {
		return duelType;
	}
	/**
	 * @param duelType the duelType to set
	 */
	public void setDuelType(String duelType) {
		this.duelType = duelType;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fch_id")
	/**
	 * @return the uDuelCh
	 */
	public UDuelCh getUDuelCh() {
		return UDuelCh;
	}
	/**
	 * @param uDuelCh the uDuelCh to set
	 */
	public void setUDuelCh(UDuelCh uDuelCh) {
		UDuelCh = uDuelCh;
	}
	/**
	 * @return the stdate
	 */
	@Temporal(TemporalType.DATE)  
	@JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return stdate;
	}
	/**
	 * @param stdate the stdate to set
	 */
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	/**
	 * @return the sttime
	 */
	@Column(name = "sttime", length = 15)   
	@JSON(format="HH:mm:ss")
	public Date getSttime() {
		return sttime;
	}
	/**
	 * @param sttime the sttime to set
	 */
	public void setSttime(Date sttime) {
		this.sttime = sttime;
	}
	/**
	 * @return the duelStatus
	 */
	@Column(name = "duel_status", length = 20)
	public String getDuelStatus() {
		return duelStatus;
	}
	/**
	 * @param duelStatus the duelStatus to set
	 */
	public void setDuelStatus(String duelStatus) {
		this.duelStatus = duelStatus;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUOrder() {
		return UOrder;
	}
	public void setUOrder(UOrder uOrder) {
		UOrder = uOrder;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "f_user_id")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "f_team_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	
	
	/**
	 * @return the duelRecommendStatus
	 */
	@Column(name = "duel_recommend_status", length = 20)
	public String getDuelRecommendStatus() {
		return duelRecommendStatus;
	}
	/**
	 * @param duelRecommendStatus the duelRecommendStatus to set
	 */
	public void setDuelRecommendStatus(String duelRecommendStatus) {
		this.duelRecommendStatus = duelRecommendStatus;
	}
	
	@Column(name = "effective_status", length = 20)
	/**
	 * @return the effectiveStatus
	 */
	public String getEffectiveStatus() {
		return effectiveStatus;
	}
	/**
	 * @param effectiveStatus the effectiveStatus to set
	 */
	public void setEffectiveStatus(String effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}
	/**
	 * @return the jpusho
	 */
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
