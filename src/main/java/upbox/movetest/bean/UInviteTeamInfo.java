package upbox.movetest.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "u_inviteteaminfo")
public class UInviteTeamInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9034009563044852301L;
	private String id;
	private Integer playerid;
	private Date createtime;
	private Integer invstatus;
	private Integer userid;
	private Integer teamid;
	private Integer invpushstatus; //邀请推送状态 1-成功 -1-不成功 0-待推送
	private Integer joinpushstatus; //邀请推送状态 1-成功 -1-不成功 0-待推送
	
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "playerid")
	public Integer getPlayerid() {
		return playerid;
	}
	public void setPlayerid(Integer playerid) {
		this.playerid = playerid;
	}
	
	@Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "invstatus")
	public Integer getInvstatus() {
		return invstatus;
	}
	public void setInvstatus(Integer invstatus) {
		this.invstatus = invstatus;
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
	
	@Column(name = "invpushstatus")
	public Integer getInvpushstatus() {
		return invpushstatus;
	}
	public void setInvpushstatus(Integer invpushstatus) {
		this.invpushstatus = invpushstatus;
	}
	
	@Column(name = "joinpushstatus")
	public Integer getJoinpushstatus() {
		return joinpushstatus;
	}
	public void setJoinpushstatus(Integer joinpushstatus) {
		this.joinpushstatus = joinpushstatus;
	}

}
