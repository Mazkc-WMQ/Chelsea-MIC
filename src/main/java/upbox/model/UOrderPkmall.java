package upbox.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 订单商品包bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_order_pkmall")
public class UOrderPkmall implements java.io.Serializable {
	private static final long serialVersionUID = -6000529146692222052L;
	private String pkId;
	private Integer weight;
	private String name;
	private String remark;
	private String size;
	private String saleType;
	private String priceType;
	private Double price;
	private int count;
	private UOrder UOrder;
	private String proPackId;
	private Set<UOrderPkmallInfo> UOrderPkmall = new HashSet<UOrderPkmallInfo>(0);

	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 60)
	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	@Column(name = "weight")
	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	@Column(name = "sale_type")
	public String getSaleType()
	{
		return saleType;
	}

	public void setSaleType(String saleType)
	{
		this.saleType = saleType;
	}

	@Column(name = "price_type")
	public String getPriceType()
	{
		return priceType;
	}

	public void setPriceType(String priceType)
	{
		this.priceType = priceType;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id")
	public UOrder getUOrder() {
		return this.UOrder;
	}

	public void setUOrder(UOrder UOrder) {
		this.UOrder = UOrder;
	}
	
	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "UOrderPkmall")
	public Set<UOrderPkmallInfo> getUOrderPkmall() {
		return UOrderPkmall;
	}

	public void setUOrderPkmall(Set<UOrderPkmallInfo> uOrderPkmall) {
		UOrderPkmall = uOrderPkmall;
	}

	@Column(name = "pro_pack_id")
	public String getProPackId()
	{
		return proPackId;
	}

	public void setProPackId(String proPackId)
	{
		this.proPackId = proPackId;
	}
}