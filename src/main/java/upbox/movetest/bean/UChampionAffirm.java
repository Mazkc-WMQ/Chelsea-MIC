package upbox.movetest.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "u_champion_affirm")
public class UChampionAffirm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3739049272503056549L;
	
	private String id;
	private String ordernum;
	private String champion_id;
	private Integer team_id;
	
	@Id
	@Column(name = "id")
	//@GeneratedValue(strategy = GenerationType.AUTO) 根据序列自动产生
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "ordernum")
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	
	@Column(name = "champion_id")
	public String getChampion_id() {
		return champion_id;
	}
	public void setChampion_id(String champion_id) {
		this.champion_id = champion_id;
	}
	
	@Column(name = "team_id")
	public Integer getTeam_id() {
		return team_id;
	}
	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}

}
