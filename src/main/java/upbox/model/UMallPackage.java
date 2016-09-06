package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UMallpackage upbox.entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_mall_package")
public class UMallPackage implements java.io.Serializable {
	private static final long serialVersionUID = 5878205693290306316L;
	private String proPackId;
	private UBUser UBUser;
	private UCourt UCourt;
	private String name;
	private String remark;
	private String size;
	private String saleType;
	private Double price;
	private Integer saleCeiling;
	private Integer allStock;
	private Integer courtStock;
	private String proPackStatus;
	private Date createdate;
	private Date createtime;
	private String priceType;
	private Integer weight;

	@Id
	@Column(name = "pro_pack_id", unique = true, nullable = false, length = 60)
	public String getProPackId() {
		return this.proPackId;
	}

	public void setProPackId(String proPackId) {
		this.proPackId = proPackId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUBUser() {
		return this.UBUser;
	}

	public void setUBUser(UBUser UBUser) {
		this.UBUser = UBUser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "court_id")
	public UCourt getUCourt() {
		return this.UCourt;
	}

	public void setUCourt(UCourt UCourt) {
		this.UCourt = UCourt;
	}

	@Column(name = "name", length = 50)
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

	@Column(name = "court_stock")
	public Integer getCourtStock() {
		return this.courtStock;
	}

	public void setCourtStock(Integer courtStock) {
		this.courtStock = courtStock;
	}

	@Column(name = "pro_pack_status", length = 20)
	public String getProPackStatus() {
		return this.proPackStatus;
	}

	public void setProPackStatus(String proPackStatus) {
		this.proPackStatus = proPackStatus;
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

	@Column(name = "price_type", length = 20)
	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	@Column(name = "weight", length = 20)
	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}
}