 package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 赛事球员事件记录表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_match_playerinfo")
public class UMatchPlayerInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -3413999713415776029L;
	
	private String keyId;
	private UTeam uteam;
	private UUser user;
	private String time;
	private String matchPlayerType;
	private String info;
	private UMatchBs UMatchBs;	
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fteam_id")
	public UTeam getUteam() {
		return uteam;
	}
	public void setUteam(UTeam uteam) {
		this.uteam = uteam;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUser() {
		return user;
	}
	public void setUser(UUser user) {
		this.user = user;
	}
	
	
	@Column(name = "time", length = 20)  
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	@Column(name = "info", length = 20)
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Column(name = "match_player_type", length = 20)
	public String getMatchPlayerType() {
		return matchPlayerType;
	}
	public void setMatchPlayerType(String matchPlayerType) {
		this.matchPlayerType = matchPlayerType;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_id")
	public UMatchBs getUMatchBs() {
		return UMatchBs;
	}
	public void setUMatchBs(UMatchBs uMatchBs) {
		UMatchBs = uMatchBs;
	}
	

	


	
	
	


	



	
	


	
	
	
}