package upbox.model;

import java.util.Date;
import java.util.HashMap;

import org.apache.struts2.json.annotations.JSON;

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

/**
 * UOrder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_order")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UOrder implements java.io.Serializable {
	private static final long serialVersionUID = 4982889117617700779L;
	private String orderId;
	private UUser UUser;
	private String orderType;
	private String trainingId;
	private String title;
	private Double price;
	private String resource;
	private Date createdate;
	private Date createtime;
	private Date paydate;
	private Date paytime;
	private Date outdate;
	private Date outtime;
	private Date cancedate;
	private Date cancetime;
	private String paytype;
	private String jpusho;
	private String jpushp;
	private String orderstatus;
	private String perId;
	private String ordernum;
	private Double allprice;
	private String displayStatus;
	private String paymentRatio;
	private String relationType;
	private String teamId;
	private String teamName;//主队队名
	private String teamCName;//客队队名
	private String duelPayType;//约战支付类型
	private Double refundPrice;//退款金额
//	private Set<UOrderMall> UOrderMall = new HashSet<UOrderMall>(0);//订单商品(多对一)
//	private Set<UOrderCourt> UOrderCourt = new HashSet<UOrderCourt>(0);//订单场次-根据场次找到球场（多对一）
//	private Set<UOrderPkmall> UOrderPkmall = new HashSet<UOrderPkmall>(0);//订单商品包(多对一)
	private List<HashMap<String, Object>> UOrderMall;
	private List<HashMap<String, Object>> UOrderCourt;
	private String orderTypeName;
	private String orderstatusName;
	private String duelPayTypeName;//约战支付类型
	private String paytypeName;//订单支付方式
	private String payTypeStatus;
	private List<HashMap<String, Object>> listOrderRelate;
	private String nickname;
	private String realname;
	private HashMap<String, Object> productType;
	
	@Id
	@Column(name = "order_id", unique = true, nullable = false, length = 60)
	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return this.UUser;
	}

	public void setUUser(UUser UUser) {
		this.UUser = UUser;
	}

	@Column(name = "order_type", length = 20)
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Column(name = "training_id", length = 60)
	public String getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "price", precision = 10)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "resource", length = 20)
	public String getResource() {
		return this.resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
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

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "paydate", length = 13)
	public Date getPaydate() {
		return this.paydate;
	}

	public void setPaydate(Date paydate) {
		this.paydate = paydate;
	}

	@Column(name = "paytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getPaytime() {
		return this.paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "outdate", length = 13)
	public Date getOutdate() {
		return this.outdate;
	}

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	@Column(name = "outtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getOuttime() {
		return this.outtime;
	}

	public void setOuttime(Date outtime) {
		this.outtime = outtime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "cancedate", length = 13)
	public Date getCancedate() {
		return this.cancedate;
	}

	public void setCancedate(Date cancedate) {
		this.cancedate = cancedate;
	}

	@Column(name = "cancetime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCancetime() {
		return this.cancetime;
	}

	public void setCancetime(Date cancetime) {
		this.cancetime = cancetime;
	}

	@Column(name = "paytype", length = 20)
	public String getPaytype() {
		return this.paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	@Column(name = "jpusho", length = 20)
	public String getJpusho() {
		return this.jpusho;
	}

	public void setJpusho(String jpusho) {
		this.jpusho = jpusho;
	}

	@Column(name = "jpushp", length = 20)
	public String getJpushp() {
		return this.jpushp;
	}

	public void setJpushp(String jpushp) {
		this.jpushp = jpushp;
	}

	@Column(name = "orderstatus", length = 20)
	public String getOrderstatus() {
		return this.orderstatus;
	}

	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

//	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "UOrder")
//	public Set<UOrderMall> getUOrderMall()
//	{
//		return UOrderMall;
//	}
//
//	public void setUOrderMall(Set<UOrderMall> uOrderMall)
//	{
//		UOrderMall = uOrderMall;
//	}
//	
//	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "UOrder")
//	public Set<UOrderPkmall> getUOrderPkmall() {
//		return UOrderPkmall;
//	}
//
//	public void setUOrderPkmall(Set<UOrderPkmall> uOrderPkmall) {
//		UOrderPkmall = uOrderPkmall;
//	}
//
//	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "UOrder")
//	public Set<UOrderCourt> getUOrderCourt()
//	{
//		return UOrderCourt;
//	}
//
//	public void setUOrderCourt(Set<UOrderCourt> uOrderCourt)
//	{
//		UOrderCourt = uOrderCourt;
//	}

	@Column(name = "per_id", length = 60)
	public String getPerId()
	{
		return perId;
	}

	public void setPerId(String perId)
	{
		this.perId = perId;
	}

	/**
	 * @return the ordernum
	 */
	@Column(name = "ordernum", length = 60)
	public String getOrdernum() {
		return ordernum;
	}

	/**
	 * @param ordernum the ordernum to set
	 */
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	@Column(name = "allprice")
	public Double getAllprice() {
		return allprice;
	}
	public void setAllprice(Double allprice) {
		this.allprice = allprice;
	}
	
	@Transient
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	@Transient
	public String getTeamCName() {
		return teamCName;
	}
	public void setTeamCName(String teamCName) {
		this.teamCName = teamCName;
	}
	@Column(name="display_status")
	public String getDisplayStatus() {
		return displayStatus;
	}
	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}
	@Column(name="payment_ratio")
	public String getPaymentRatio() {
		return paymentRatio;
	}
	public void setPaymentRatio(String paymentRatio) {
		this.paymentRatio = paymentRatio;
	}
	@Column(name="relation_type")
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	@Transient
	public List<HashMap<String, Object>> getUOrderMall() {
		return UOrderMall;
	}
	public void setUOrderMall(List<HashMap<String, Object>> uOrderMall) {
		UOrderMall = uOrderMall;
	}
	@Transient
	public List<HashMap<String, Object>> getUOrderCourt() {
		return UOrderCourt;
	}
	public void setUOrderCourt(List<HashMap<String, Object>> uOrderCourt) {
		UOrderCourt = uOrderCourt;
	}
	@Transient
	public String getDuelPayType() {
		return duelPayType;
	}
	public void setDuelPayType(String duelPayType) {
		this.duelPayType = duelPayType;
	}
	@Transient
	public Double getRefundPrice() {
		return refundPrice;
	}
	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}
	@Transient
	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	@Transient
	public String getOrderstatusName() {
		return orderstatusName;
	}

	public void setOrderstatusName(String orderstatusName) {
		this.orderstatusName = orderstatusName;
	}
	@Transient
	public String getDuelPayTypeName() {
		return duelPayTypeName;
	}

	public void setDuelPayTypeName(String duelPayTypeName) {
		this.duelPayTypeName = duelPayTypeName;
	}
	@Transient
	public String getPaytypeName() {
		return paytypeName;
	}

	public void setPaytypeName(String paytypeName) {
		this.paytypeName = paytypeName;
	}

	@Column(name="pay_type_status")
	public String getPayTypeStatus() {
		return payTypeStatus;
	}
	public void setPayTypeStatus(String payTypeStatus) {
		this.payTypeStatus = payTypeStatus;
	}
	@Column(name="team_id")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	@Transient
	public List<HashMap<String, Object>> getListOrderRelate() {
		return listOrderRelate;
	}
	public void setListOrderRelate(List<HashMap<String, Object>> listOrderRelate) {
		this.listOrderRelate = listOrderRelate;
	}
	@Transient
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Transient
	public HashMap<String, Object> getProductType() {
		return productType;
	}
	public void setProductType(HashMap<String, Object> productType) {
		this.productType = productType;
	}
	@Transient
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
		
}