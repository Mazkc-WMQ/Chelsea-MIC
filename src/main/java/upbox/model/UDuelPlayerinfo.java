package upbox.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 约战比赛详情
 * @author xiao
 */
@Entity
@Table(name = "u_duel_playerinfo")
public class UDuelPlayerinfo {

	private String keyId;
	private UDuelBs UDuelBs;
	private UTeam UTeam;
	private UUser UUser;
	private String time;
	private String duelBsType;
	private String info;
	private String duelBsTypeCh;
	private String bsTypeTime;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_id")
	public UDuelBs getUDuelBs() {
		return UDuelBs;
	}
	public void setUDuelBs(UDuelBs uDuelBs) {
		UDuelBs = uDuelBs;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fteam_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}

	@Column(name = "time")
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Column(name = "duel_bs_type")
	public String getDuelBsType() {
		return duelBsType;
	}
	public void setDuelBsType(String duelBsType) {
		this.duelBsType = duelBsType;
	}
	@Column(name = "info")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Transient
	public String getDuelBsTypeCh() {
		return duelBsTypeCh;
	}
	public void setDuelBsTypeCh(String duelBsTypeCh) {
		this.duelBsTypeCh = duelBsTypeCh;
	}
	@Transient
	public String getBsTypeTime() {
		return bsTypeTime;
	}
	public void setBsTypeTime(String bsTypeTime) {
		this.bsTypeTime = bsTypeTime;
	}
	
	
}
