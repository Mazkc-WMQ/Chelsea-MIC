package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 球员角色位置信息表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_player_apply")
public class UPlayerApply implements java.io.Serializable {
	private static final long serialVersionUID = -113129188854531650L;
	private String keyId;
	private String userId;
	private UPlayer UPlayer;
	private UTeam UTeam;
	private String memberType;
	private String applyStatus;//1-待确认 2-同意 3-拒绝
	private String buserId;
	private String bplayerId;
	private Date applydate;
	private Date agreedate;
	private Date refusedate;
	
	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playerid")
	public UPlayer getUPlayer() {
		return UPlayer;
	}
	public void setUPlayer(UPlayer uPlayer) {
		UPlayer = uPlayer;
	}
	@Column(name = "member_type", length = 20)
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	@Column(name = "userid")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teamid")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	@Column(name = "apply_status")
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	@Column(name = "buserid")
	public String getBuserId() {
		return buserId;
	}
	public void setBuserId(String buserId) {
		this.buserId = buserId;
	}
	@Column(name = "bplayerid")
	public String getBplayerId() {
		return bplayerId;
	}
	public void setBplayerId(String bplayerId) {
		this.bplayerId = bplayerId;
	}
	@Column(name = "applydate")
	public Date getApplydate() {
		return applydate;
	}
	public void setApplydate(Date applydate) {
		this.applydate = applydate;
	}
	@Column(name = "agreedate")
	public Date getAgreedate() {
		return agreedate;
	}
	public void setAgreedate(Date agreedate) {
		this.agreedate = agreedate;
	}
	@Column(name = "refusedate")
	public Date getRefusedate() {
		return refusedate;
	}
	public void setRefusedate(Date refusedate) {
		this.refusedate = refusedate;
	}

	
	
	
}