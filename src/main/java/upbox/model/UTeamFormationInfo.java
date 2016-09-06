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
import javax.persistence.Transient;

/**
 * 阵型详细表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_team_formationinfo")
public class UTeamFormationInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 3416009162734374745L;
	
	private String keyId;
	private UTeamFormation UTeamFormation;
	private String x;
	private String y;
	private Date createdate;
	private Integer num;
	private String pos;
	private UPlayer player;
	private String captainStatus;
	private String homeStatus;//0-主队 1-客队
	private String location;
	private String playerId;
	

	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Column(name = "x", length = 20)
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	
	@Column(name = "y", length = 20)
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	
	@Column(name = "num")
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	@Column(name = "pos", length = 20)
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playerid")
	public UPlayer getPlayer() {
		return player;
	}
	public void setPlayer(UPlayer player) {
		this.player = player;
	}
	
	@Column(name = "captain_status", length = 20)
	public String getCaptainStatus() {
		return captainStatus;
	}
	public void setCaptainStatus(String captainStatus) {
		this.captainStatus = captainStatus;
	}
	
	@Column(name = "home_status", length = 20)
	public String getHomeStatus() {
		return homeStatus;
	}
	public void setHomeStatus(String homeStatus) {
		this.homeStatus = homeStatus;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "formation_id")
	public UTeamFormation getUTeamFormation() {
		return UTeamFormation;
	}
	public void setUTeamFormation(UTeamFormation uTeamFormation) {
		UTeamFormation = uTeamFormation;
	}
	
	@Transient
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
	@Column(name = "location", length = 20)
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
	
	
	
	
	

	
	
}