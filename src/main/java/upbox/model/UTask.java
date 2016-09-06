package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 发布任务表
 * @author xiaoying
 *
 */
@Entity
@Table(name = "u_upbox_increment.u_task")
public class UTask implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String keyid;
	private String taskType;	
	private Date releaseDate;	
	private Date begintime;	
	private Date endtime;	
	private String createuser;	
	private String remark;	
	private Integer count;	
	private String taskBehavior;
	private Integer rewardsj;	
	private Integer rewardhb;	
	private String taskUseStatus;	
	private String imgurl;
	private String weburl;
	
	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	@Column(name = "task_type")
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	@Column(name = "release_date")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	@Column(name = "begintime")
	public Date getBegintime() {
		return begintime;
	}
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
	@Column(name = "endtime")
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	@Column(name = "createuser")
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name = "task_behavior")
	public String getTaskBehavior() {
		return taskBehavior;
	}
	public void setTaskBehavior(String taskBehavior) {
		this.taskBehavior = taskBehavior;
	}
	@Column(name = "rewardsj")
	public Integer getRewardsj() {
		return rewardsj;
	}
	public void setRewardsj(Integer rewardsj) {
		this.rewardsj = rewardsj;
	}
	@Column(name = "rewardhb")
	public Integer getRewardhb() {
		return rewardhb;
	}
	public void setRewardhb(Integer rewardhb) {
		this.rewardhb = rewardhb;
	}
	@Column(name = "task_use_status")
	public String getTaskUseStatus() {
		return taskUseStatus;
	}
	public void setTaskUseStatus(String taskUseStatus) {
		this.taskUseStatus = taskUseStatus;
	}
	@Column(name = "imgurl")
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	@Column(name = "weburl")
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
}
