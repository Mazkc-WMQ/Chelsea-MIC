package upbox.movetest.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "u_pt_mapping")
public class UPTmapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1688872903280517108L;
	
	private Integer id;
	private Integer playerid;
	private Integer teamid;
	private Integer type;
	
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "playerid")
	public Integer getPlayerid() {
		return playerid;
	}
	public void setPlayerid(Integer playerid) {
		this.playerid = playerid;
	}
	
	@Column(name = "teamid")
	public Integer getTeamid() {
		return teamid;
	}
	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
