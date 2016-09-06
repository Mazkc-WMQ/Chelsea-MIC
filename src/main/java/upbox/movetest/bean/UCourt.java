package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

/**
 * UCourt entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_court")
@DynamicUpdate(true)
public class UCourt implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4115597238093613937L;
	private Integer cid;
	private String name;
	private String address;
	private String phone;
	private Integer courtimage;
	private Integer adimage;
	private String remark;
	private String reimage;
	private Date createtime;
	private String worktime;
	private String traffic;
	private Integer costatus;
	private Float price;
	private String score;
	private Integer peocount;
	private Integer tolscore;
	private Integer viewstatus;
	
	private String iconurl;
	
	
	
	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getGpsx() {
		return gpsx;
	}

	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}

	public String getGpsy() {
		return gpsy;
	}

	public void setGpsy(String gpsy) {
		this.gpsy = gpsy;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getShortlabel() {
		return shortlabel;
	}

	public void setShortlabel(String shortlabel) {
		this.shortlabel = shortlabel;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	private String gpsx;
	private String gpsy;
	private String spec;
	private String shortlabel;
	private String others;
	

	// Constructors
 
	 

	// Property accessors
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	@Column(name = "name", length = 250)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "phone", length = 50)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "courtimage")
	public Integer getCourtimage() {
		return this.courtimage;
	}

	public void setCourtimage(Integer courtimage) {
		this.courtimage = courtimage;
	}

	@Column(name = "adimage")
	public Integer getAdimage() {
		return this.adimage;
	}

	public void setAdimage(Integer adimage) {
		this.adimage = adimage;
	}

	@Column(name = "remark", length = 1255)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "reimage")
	public String getReimage() {
		return this.reimage;
	}

	public void setReimage(String reimage) {
		this.reimage = reimage;
	}

	@Column(name = "createtime")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "worktime", length = 50)
	public String getWorktime() {
		return this.worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	@Column(name = "traffic", length = 850)
	public String getTraffic() {
		return this.traffic;
	}

	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	@Column(name = "costatus")
	public Integer getCostatus() {
		return this.costatus;
	}

	public void setCostatus(Integer costatus) {
		this.costatus = costatus;
	}

	@Column(name = "price", precision = 8, scale = 8)
	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Column(name = "score")
	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Column(name = "peocount")
	public Integer getPeocount() {
		return this.peocount;
	}

	public void setPeocount(Integer peocount) {
		this.peocount = peocount;
	}

	@Column(name = "tolscore")
	public Integer getTolscore() {
		return this.tolscore;
	}

	public void setTolscore(Integer tolscore) {
		this.tolscore = tolscore;
	}

	@Column(name = "viewstatus")
	public Integer getViewstatus() {
		return this.viewstatus;
	}

	public void setViewstatus(Integer viewstatus) {
		this.viewstatus = viewstatus;
	}

}