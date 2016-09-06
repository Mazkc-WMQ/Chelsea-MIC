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
@Table(name = "u_jointeam")
public class UJoinTeam  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7510038130221213631L;
	
	private Integer id;
	private Date createtime;
	private Integer joinstatus;
	private Integer userid;
	private Integer teamid;
	private Integer pushstatus;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "joinstatus")
	public Integer getJoinstatus() {
		return joinstatus;
	}
	public void setJoinstatus(Integer joinstatus) {
		this.joinstatus = joinstatus;
	}
	
	@Column(name = "userid")
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	@Column(name = "teamid")
	public Integer getTeamid() {
		return teamid;
	}
	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}
	
	@Column(name = "pushstatus")
	public Integer getPushstatus() {
		return pushstatus;
	}
	public void setPushstatus(Integer pushstatus) {
		this.pushstatus = pushstatus;
	}

}
