 package upbox.model;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 球场表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_court")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UCourt implements java.io.Serializable {
	private static final long serialVersionUID = -1027440962326880779L;
	private String courtId;
	private Integer courtIdInt;
	private UBUser UBUser;
	private String courtType;
	private String name;
	private Date createdate;
	private Date createtime;
	private int area;
	private String address;
	private String position;
	private String poi;
	private String telephone;
	private String traffic;
	private Integer brCourtCount;
	private String courtStatus;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private String score;
	private Set<UBrCourtimage> UBrCourtimages = new HashSet<UBrCourtimage>(0);
	private Set<UBrCourt> UBrCourts = new HashSet<UBrCourt>(0);
	private Set<UCourtOperlog> UCourtOperlogs = new HashSet<UCourtOperlog>(0);

	private String bdPosition;
	private String bdPoi;




	@Id
	@Column(name = "court_id", unique = true, nullable = false, length = 60)
	public String getCourtId() {
		return this.courtId;
	}

	public void setCourtId(String courtId) {
		this.courtId = courtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUBUser() {
		return this.UBUser;
	}

	public void setUBUser(UBUser UBUser) {
		this.UBUser = UBUser;
	}

	@Column(name = "court_type", length = 20)
	public String getCourtType() {
		return this.courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	@Column(name = "name", length = 120)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "area")
	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "position", length = 200)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "poi", length = 50)
	public String getPoi() {
		return this.poi;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	@Column(name = "telephone", length = 50)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "traffic")
	public String getTraffic() {
		return this.traffic;
	}

	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	@Column(name = "br_court_count")
	public Integer getBrCourtCount() {
		return this.brCourtCount;
	}

	public void setBrCourtCount(Integer brCourtCount) {
		this.brCourtCount = brCourtCount;
	}

	@Column(name = "court_status", length = 20)
	public String getCourtStatus() {
		return this.courtStatus;
	}

	public void setCourtStatus(String courtStatus) {
		this.courtStatus = courtStatus;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "modifydate", length = 13)
	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	@Column(name = "modifytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getModifytime() {
		return this.modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Column(name = "modifyuser", length = 20)
	public String getModifyuser() {
		return this.modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	@Column(name = "score", length = 30)
	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Column(name = "bd_position", length = 200)
	public String getBdPosition() {
		return bdPosition;
	}

	public void setBdPosition(String bdPosition) {
		this.bdPosition = bdPosition;
	}

	@Column(name = "bd_poi", length = 50)
	public String getBdPoi() {
		return bdPoi;
	}

	public void setBdPoi(String bdPoi) {
		this.bdPoi = bdPoi;
	}

	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UCourt")
	public Set<UBrCourtimage> getUBrCourtimages() {
		return this.UBrCourtimages;
	}

	public void setUBrCourtimages(Set<UBrCourtimage> UBrCourtimages) {
		this.UBrCourtimages = UBrCourtimages;
	}

	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UCourt")
	public Set<UBrCourt> getUBrCourts() {
		return this.UBrCourts;
	}

	public void setUBrCourts(Set<UBrCourt> UBrCourts) {
		this.UBrCourts = UBrCourts;
	}

	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UCourt")
	public Set<UCourtOperlog> getUCourtOperlogs() {
		return this.UCourtOperlogs;
	}

	public void setUCourtOperlogs(Set<UCourtOperlog> UCourtOperlogs) {
		this.UCourtOperlogs = UCourtOperlogs;
	}
	@Column(name = "court_id_int", length = 20)
	public Integer getCourtIdInt() {
		return courtIdInt;
	}

	public void setCourtIdInt(Integer courtIdInt) {
		this.courtIdInt = courtIdInt;
	}
}