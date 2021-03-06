package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 场次通用规则记录信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtsession_role")
public class UBrCourtsessionRole implements java.io.Serializable {
	private static final long serialVersionUID = 1320432288408490302L;
	private String sessionRoleId;
	private UBrCourt UBrCourt;
	private String createuser;
	private String sttime;
	private String endtime;
	private String sessionDuration;
	private Double sessionPrice;
	private Double discountPrice;
	private Double memberPrice;
	private Double favPrice;
	private Double activityPrice;
	private String sessionStatus;
	private Date createdate;
	private String sessionUseingStatus;
	private int sort;
	private String sessionId;
	
	@Id
	@Column(name = "session_role_id", unique = true, nullable = false, length = 60)
	public String getSessionRoleId()
	{
		return sessionRoleId;
	}

	public void setSessionRoleId(String sessionRoleId)
	{
		this.sessionRoleId = sessionRoleId;
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

	public Date getCreatedate()
	{
		return createdate;
	}

	public void setCreatedate(Date createdate)
	{
		this.createdate = createdate;
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

	public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
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
}