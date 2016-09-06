package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 球队Bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team")
public class UTeam implements java.io.Serializable {
	private static final long serialVersionUID = -1049749713295328912L;
	private Integer id;
	private String name;
	private Integer imageid;
	private Date createtime;
	private String remark;
	private String openid;
	private Integer reviewstatus;
	private Integer teamstatus;
	private String reviewcon;
	private Integer count;
	private Integer userid;
	private Integer type;
	private Integer teamsource;

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 24)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "imageid")
	public Integer getImageid() {
		return this.imageid;
	}

	public void setImageid(Integer imageid) {
		this.imageid = imageid;
	}

	@Column(name = "createtime", length = 29)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "reviewstatus")
	public Integer getReviewstatus() {
		return this.reviewstatus;
	}

	public void setReviewstatus(Integer reviewstatus) {
		this.reviewstatus = reviewstatus;
	}

	@Column(name = "teamstatus")
	public Integer getTeamstatus() {
		return this.teamstatus;
	}

	public void setTeamstatus(Integer teamstatus) {
		this.teamstatus = teamstatus;
	}

	@Column(name = "reviewcon", length = 300)
	public String getReviewcon() {
		return this.reviewcon;
	}

	public void setReviewcon(String reviewcon) {
		this.reviewcon = reviewcon;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "teamsource")
	public Integer getTeamsource() {
		return this.teamsource;
	}

	public void setTeamsource(Integer teamsource) {
		this.teamsource = teamsource;
	}

}