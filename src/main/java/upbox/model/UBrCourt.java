package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 球场信息bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_court")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UBrCourt implements java.io.Serializable {
	private static final long serialVersionUID = 4683861804824533763L;
	private String subcourtId;
	private UCourt UCourt;
	private String name;
	private String busSttime;
	private String busEndtime;
	private String subcourtType;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private String score;
	private String courtType;
	private String courtStatus;
	private String busStatus;
	private String recommendCourt;
	private String popularityCourt;
	private int subcourtIdInt;

	@Id
	@Column(name = "subcourt_id", unique = true, nullable = false, length = 60)
	public String getSubcourtId() {
		return this.subcourtId;
	}

	public void setSubcourtId(String subcourtId) {
		this.subcourtId = subcourtId;
	}

	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "court_id")
	public UCourt getUCourt() {
		return this.UCourt;
	}

	public void setUCourt(UCourt UCourt) {
		this.UCourt = UCourt;
	}

	@Column(name = "name", length = 120)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "bus_status", length = 20)
	public String getBusStatus()
	{
		return busStatus;
	}

	public void setBusStatus(String busStatus)
	{
		this.busStatus = busStatus;
	}

	@Column(name = "subcourt_type", length = 20)
	public String getSubcourtType() {
		return this.subcourtType;
	}

	public void setSubcourtType(String subcourtType) {
		this.subcourtType = subcourtType;
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

	@Column(name = "createuser", length = 60)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
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

	@Column(name = "modifyuser", length = 60)
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
	
	@Column(name = "court_br_type", length = 20)
	public String getCourtType() {
		return this.courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}
	
	@Column(name = "court_status", length = 20)
	public String getCourtStatus() {
		return this.courtStatus;
	}

	public void setCourtStatus(String courtStatus) {
		this.courtStatus = courtStatus;
	}

	@Column(name = "bus_sttime", length = 20)
	public String getBusSttime()
	{
		return busSttime;
	}

	public void setBusSttime(String busSttime)
	{
		this.busSttime = busSttime;
	}

	@Column(name = "bus_endtime", length = 20)
	public String getBusEndtime()
	{
		return busEndtime;
	}

	public void setBusEndtime(String busEndtime)
	{
		this.busEndtime = busEndtime;
	}

	@Column(name = "recommend_court", length = 20)
	/**
	 * @return the recommendCourt
	 */
	public String getRecommendCourt() {
		return recommendCourt;
	}

	/**
	 * @param recommendCourt the recommendCourt to set
	 */
	public void setRecommendCourt(String recommendCourt) {
		this.recommendCourt = recommendCourt;
	}

	@Column(name = "popularity_court", length = 20)
	/**
	 * @return the popularityCourt
	 */
	public String getPopularityCourt() {
		return popularityCourt;
	}

	/**
	 * @param popularityCourt the popularityCourt to set
	 */
	public void setPopularityCourt(String popularityCourt) {
		this.popularityCourt = popularityCourt;
	}

	@Column(name = "subcourt_id_int", length = 20)
	public int getSubcourtIdInt() {
		return subcourtIdInt;
	}

	public void setSubcourtIdInt(int subcourtIdInt) {
		this.subcourtIdInt = subcourtIdInt;
	}
	
	
}