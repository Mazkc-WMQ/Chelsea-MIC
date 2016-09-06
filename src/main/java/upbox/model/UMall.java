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
 * 商城基础信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_mall")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UMall implements java.io.Serializable {
	private static final long serialVersionUID = 5297327121078945478L;
	private String proId;
	private UBUser UBUser;
	private String productType;
	private String productSubtype;
	private String productDetailtype;
	private String name;
	private String remark;
	private String size;
	private String isUser;
	private String userId;
	private String saleType;
	private String priceType;
	private Double price;
	private Integer saleCeiling;
	private Integer allStock;
	private String productStatus;
	private Date createdate;
	private Date createtime;
	private String similarId;
	private Integer saleCount; //售卖数量
	private Double allPrice; //总价 商品售卖总价 = 商品售卖数量 * 商品单价
	private int mallCount; //库存

	@Id
	@Column(name = "pro_id", unique = true, nullable = false, length = 60)
	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUBUser() {
		return this.UBUser;
	}

	public void setUBUser(UBUser UBUser) {
		this.UBUser = UBUser;
	}

	@Column(name = "product_type", length = 20)
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "product_subtype", length = 20)
	public String getProductSubtype() {
		return this.productSubtype;
	}

	public void setProductSubtype(String productSubtype) {
		this.productSubtype = productSubtype;
	}

	@Column(name = "product_detailtype", length = 20)
	public String getProductDetailtype() {
		return this.productDetailtype;
	}

	public void setProductDetailtype(String productDetailtype) {
		this.productDetailtype = productDetailtype;
	}

	@Column(name = "name", length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "size", length = 50)
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

	@Column(name = "sale_type", length = 20)
	public String getSaleType() {
		return this.saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	@Column(name = "price_type", length = 20)
	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column(name = "price")
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "sale_ceiling")
	public Integer getSaleCeiling() {
		return this.saleCeiling;
	}

	public void setSaleCeiling(Integer saleCeiling) {
		this.saleCeiling = saleCeiling;
	}

	@Column(name = "all_stock")
	public Integer getAllStock() {
		return this.allStock;
	}

	public void setAllStock(Integer allStock) {
		this.allStock = allStock;
	}

	@Column(name = "product_status", length = 20)
	public String getProductStatus() {
		return this.productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
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
	@Column(name = "similar_id", length = 60)
	public String getSimilarId() {
		return similarId;
	}
	public void setSimilarId(String similarId) {
		this.similarId = similarId;
	}
	@Transient
	public Integer getSaleCount()
	{
		return saleCount;
	}

	public void setSaleCount(Integer saleCount)
	{
		this.saleCount = saleCount;
	}
	
	@Transient
	public Double getAllPrice()
	{
		return allPrice;
	}

	public void setAllPrice(Double allPrice)
	{
		this.allPrice = allPrice;
	}
	@Transient
	public int getMallCount()
	{
		return mallCount;
	}

	public void setMallCount(int mallCount)
	{
		this.mallCount = mallCount;
	}
}