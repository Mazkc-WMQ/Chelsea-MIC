package upbox.movetest.bean;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UCourtorder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_courtorder")
public class UCourtorder implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer teamid;
	private String price;
	private Integer courtid;
	private String ordernum;
	private Date createtime;
	private Date year;
	private Integer userid;

	 
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "teamid")
	public Integer getTeamid() {
		return this.teamid;
	}

	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}

	@Column(name = "price", length = 50)
	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Column(name = "courtid")
	public Integer getCourtid() {
		return this.courtid;
	}

	public void setCourtid(Integer courtid) {
		this.courtid = courtid;
	}

	@Column(name = "ordernum", length = 36)
	public String getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "createtime" )
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

 
	@Column(name = "year" )
	public Date getYear() {
		return this.year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}