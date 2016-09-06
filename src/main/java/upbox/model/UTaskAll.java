package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 任务完成总表
 * @author xiaoying
 *
 */
@Entity
@Table(name = "u_upbox_increment.u_task_all")
public class UTaskAll implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String keyid;
	private UTask UTask;
	private UUser UUser;
	private String teamid;
	private Date createtime;
	private Integer count;
	private String handleDate;
	
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
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@Column(name = "teamid")
	public String getTeamid() {
		return teamid;
	}
	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}
	@Column(name="count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name="createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Transient
	public String getHandleDate() {
		return handleDate;
	}
	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}
}
