package upbox.movetest.bean;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UCourtprice entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_courtprice")
public class UCourtprice implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6126306762319282908L;
	// Fields

	private Integer id;
	private Date createtime;
	private Integer courtid;
	private String worktime;
	private Integer price;
	private Integer userid;
	private String week;
	private Integer hascp;

	 

	public Integer getHascp() {
		return hascp;
	}

	public void setHascp(Integer hascp) {
		this.hascp = hascp;
	}

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "createtime")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "courtid")
	public Integer getCourtid() {
		return this.courtid;
	}

	public void setCourtid(Integer courtid) {
		this.courtid = courtid;
	}

	@Column(name = "worktime", length = 50)
	public String getWorktime() {
		return this.worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	@Column(name = "price")
	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "week", length = 50)
	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

}