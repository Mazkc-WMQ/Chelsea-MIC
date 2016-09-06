package upbox.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "u_br_courtsession")
public class UBrCourtsession implements java.io.Serializable {
	private static final long serialVersionUID = 1320432288408490302L;
	private String sessionId;
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
	private String orderStatus;
	private Date createdate;
	private String week;
	private String sessionUseingStatus;
	private List<String> mallList; //商品ID List
	private List<String> mallPkList; //商品包ID List
	private List<UMall> umallList; //关联商品 List
	//private int sort;
	private String singUpdateStatus;
	private String isOrder; //是否被预定
	private String orderId; //订单ID
	
	@Id
	@Column(name = "session_id", unique = true, nullable = false, length = 60)
	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	@Column(name = "order_status", length = 20)
	public String getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public Date getCreatedate()
	{
		return createdate;
	}

	public void setCreatedate(Date createdate)
	{
		this.createdate = createdate;
	}

	@Transient
	public List<String> getMallList()
	{
		return mallList;
	}

	public void setMallList(List<String> mallList)
	{
		this.mallList = mallList;
	}

	@Transient
	public List<String> getMallPkList()
	{
		return mallPkList;
	}

	public void setMallPkList(List<String> mallPkList)
	{
		this.mallPkList = mallPkList;
	}

	public String getWeek()
	{
		return week;
	}

	public void setWeek(String week)
	{
		this.week = week;
	}

	@Column(name = "session_br_useing_status", length = 20)
	public String getSessionUseingStatus()
	{
		return sessionUseingStatus;
	}

	public void setSessionUseingStatus(String sessionUseingStatus)
	{
		this.sessionUseingStatus = sessionUseingStatus;
	}

	@Transient
	public List<UMall> getUmallList()
	{
		return umallList;
	}

	public void setUmallList(List<UMall> umallList)
	{
		this.umallList = umallList;
	}

	/*public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}*/

	@Column(name = "sing_update_status", length = 20)
	public String getSingUpdateStatus()
	{
		return singUpdateStatus;
	}

	public void setSingUpdateStatus(String singUpdateStatus)
	{
		this.singUpdateStatus = singUpdateStatus;
	}

	@Transient
	public String getIsOrder()
	{
		return isOrder;
	}

	public void setIsOrder(String isOrder)
	{
		this.isOrder = isOrder;
	}

	@Column(name = "order_id", length = 60)
	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
}