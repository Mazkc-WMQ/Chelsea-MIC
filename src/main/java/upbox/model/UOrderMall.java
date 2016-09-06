package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 订单关联商品
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_order_mall")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UOrderMall implements java.io.Serializable {
	private static final long serialVersionUID = -6495626378203147348L;
	private String pkId;
	private UOrder UOrder;
	private String productType;
	private String priceType;
	private String name;
	private String remark;
	private String size;
	private String isUser;
	private String userId;
	private String isTraining;
	private String trainingId;
	private String saleType;
	private Double price;
	private Integer count;
	private String productSubtype;
	private String productDetailtype;
	private String proPId;
	private String sessionId;
	private UMallImg UMallImg;//存放数据不关联表
	
	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 60)
	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUOrder() {
		return this.UOrder;
	}

	public void setUOrder(UOrder UOrder) {
		this.UOrder = UOrder;
	}

	@Column(name = "product_type", length = 20)
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "price_type", length = 20)
	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column(name = "name", length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "size", length = 30)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "is_user", length = 20)
	public String getIsUser() {
		return this.isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}

	@Column(name = "user_id", length = 60)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "is_training", length = 20)
	public String getIsTraining() {
		return this.isTraining;
	}

	public void setIsTraining(String isTraining) {
		this.isTraining = isTraining;
	}

	@Column(name = "training_id", length = 60)
	public String getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	@Column(name = "sale_type", length = 20)
	public String getSaleType() {
		return this.saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	@Column(name = "price", precision = 10)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "product_subtype")
	public String getProductSubtype()
	{
		return productSubtype;
	}

	public void setProductSubtype(String productSubtype)
	{
		this.productSubtype = productSubtype;
	}

	@Column(name = "product_detailtype")
	public String getProductDetailtype()
	{
		return productDetailtype;
	}

	public void setProductDetailtype(String productDetailtype)
	{
		this.productDetailtype = productDetailtype;
	}

	@Column(name = "pro_p_id")
	public String getProPId()
	{
		return proPId;
	}

	public void setProPId(String proPId)
	{
		this.proPId = proPId;
	}

	@Column(name = "session_id")
	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	@Transient
	public UMallImg getUMallImg() {
		return UMallImg;
	}
	public void setUMallImg(UMallImg uMallImg) {
		UMallImg = uMallImg;
	}
}