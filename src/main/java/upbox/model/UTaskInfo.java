package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 任务完成明细表
 * @author xiaoying
 *
 */
@Entity
@Table(name = "u_upbox_increment.u_task_info")
public class UTaskInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String keyid;
	private UTask UTask;
	private Date createtime;
	private UUser UUser;	
	private String teamid;	
	private String playerid;	
	private String duelid;
	private String challegeid;	
	private String matchid;
	private String remark;	
	
	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "taskid")
	public UTask getUTask() {
		return UTask;
	}
	public void setUTask(UTask uTask) {
		UTask = uTask;
	}
	@Column(name="createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@Column(name="teamid")
	public String getTeamid() {
		return teamid;
	}
	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}
	@Column(name="playerid")
	public String getPlayerid() {
		return playerid;
	}
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	@Column(name="duelid")
	public String getDuelid() {
		return duelid;
	}
	public void setDuelid(String duelid) {
		this.duelid = duelid;
	}
	@Column(name="challegid")
	public String getChallegeid() {
		return challegeid;
	}
	public void setChallegeid(String challegeid) {
		this.challegeid = challegeid;
	}
	@Column(name="matchid")
	public String getMatchid() {
		return matchid;
	}
	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
