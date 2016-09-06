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
 * 用户身价获取记录表
 * @author xiaoying
 *
 */
@Entity
@Table(name = "u_upbox_increment.u_worth_log")
public class UWorthLog implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String keyid;
	private UUser UUser;	
	private Integer count;	
	private Date logdate;		
	private String taskid;
	private String project;	
	private String resource;	
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
	@JoinColumn(name = "userid")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@Column(name = "count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name = "logdate")
	public Date getLogdate() {
		return logdate;
	}
	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}
	@Column(name = "taskid")
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(name = "project")
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	@Column(name = "resource")
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
