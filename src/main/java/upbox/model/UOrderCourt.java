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
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

/**
 * 场次预定信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_order_court")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UOrderCourt implements java.io.Serializable {
	private static final long serialVersionUID = 1320432288408490302L;
	private String orderCourtId;
	private UBrCourt UBrCourt;
	private String createuser;
	private Date stdate;
	private Date enddate;
	private String sttime;
	private String endtime;
	private String sessionDuration;
	private Double sessionPrice;
	private Double discountPrice;
	private Double memberPrice;
	private Double favPrice;
	private Double activityPrice;
	private String sessionStatus;
	private UOrder UOrder;
	private String sessionId;
	private String week;
	private String sessionUseingStatus;
	private UBrCourtsession UBrCourtsession;//存放数据 不关联
	private UBrCourtimage UBrCourtimage;//存放数据不关联表
	
	@Id
	@Column(name = "order_court_id", unique = true, nullable = false, length = 60)
	public String getOrderCourtId()
	{
		return orderCourtId;
	}

	public void setOrderCourtId(String orderCourtId)
	{
		this.orderCourtId = orderCourtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getUBrCourt()
	{
		return UBrCourt;
	}

	public void setUBrCourt(UBrCourt uBrCourt)
	{
		UBrCourt = uBrCourt;
	}

	@Column(name = "createuser", length = 60)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return this.stdate;
	}

	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "enddate", length = 13)
	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getSttime()
	{
		return sttime;
	}

	public void setSttime(String sttime)
	{
		this.sttime = sttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	@Column(name = "session_duration", length = 20)
	public String getSessionDuration() {
		return this.sessionDuration;
	}

	public void setSessionDuration(String sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

	@Column(name = "session_price", precision = 10)
	public Double getSessionPrice() {
		return this.sessionPrice;
	}

	public void setSessionPrice(Double sessionPrice) {
		this.sessionPrice = sessionPrice;
	}

	@Column(name = "discount_price", precision = 10)
	public Double getDiscountPrice() {
		return this.discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	@Column(name = "member_price", precision = 10)
	public Double getMemberPrice() {
		return this.memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	@Column(name = "fav_price", precision = 10)
	public Double getFavPrice() {
		return this.favPrice;
	}

	public void setFavPrice(Double favPrice) {
		this.favPrice = favPrice;
	}

	@Column(name = "activity_price", precision = 10)
	public Double getActivityPrice() {
		return this.activityPrice;
	}

	public void setActivityPrice(Double activityPrice) {
		this.activityPrice = activityPrice;
	}

	@Column(name = "session_status", length = 20)
	public String getSessionStatus() {
		return this.sessionStatus;
	}

	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUOrder()
	{
		return UOrder;
	}

	public void setUOrder(UOrder uOrder)
	{
		UOrder = uOrder;
	}

	@Column(name = "session_id", length = 60)
	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	public String getWeek()
	{
		return week;
	}

	public void setWeek(String week)
	{
		this.week = week;
	}

	@Column(name = "session_useing_status", length = 20)
	public String getSessionUseingStatus()
	{
		return sessionUseingStatus;
	}

	public void setSessionUseingStatus(String sessionUseingStatus)
	{
		this.sessionUseingStatus = sessionUseingStatus;
	}
	@Transient
	public UBrCourtsession getUBrCourtsession() {
		return UBrCourtsession;
	}
	public void setUBrCourtsession(UBrCourtsession uBrCourtsession) {
		UBrCourtsession = uBrCourtsession;
	}
	@Transient
	public UBrCourtimage getUBrCourtimage() {
		return UBrCourtimage;
	}
	public void setUBrCourtimage(UBrCourtimage uBrCourtimage) {
		UBrCourtimage = uBrCourtimage;
	}
}