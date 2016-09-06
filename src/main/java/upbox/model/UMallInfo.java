package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品包和商品映射表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_mall_info")
public class UMallInfo implements java.io.Serializable {
	private static final long serialVersionUID = -2552750205234595282L;
	private String pkId;
	private Integer weight;
	private String priceType;
	private Double price;
	private String name;
	private String remark;
	private String size;
	private String saleType;
	private int count;

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

	@Column(name = "price_type")
	public String getPriceType()
	{
		return priceType;
	}

	public void setPriceType(String priceType)
	{
		this.priceType = priceType;
	}

	@Column(name = "price",precision = 10)
	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
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

	public String getSaleType()
	{
		return saleType;
	}

	@Column(name = "sale_type")
	public void setSaleType(String saleType)
	{
		this.saleType = saleType;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}