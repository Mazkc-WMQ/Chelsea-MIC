package upbox.movetest.bean;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UZgoods entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_zgoods")
public class UZgoods implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3166180038047037711L;
	private Integer id;
	private Integer zgoodstype;
	private String name;
	private Date createtime;
	private String size;
	private String unit;
	private Integer count;
	private String remark;
	private Integer headimage;
	private Integer sale;
	private Integer goodsstatus;
	private Double price;

	// Constructors

	 
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "zgoodstype")
	public Integer getZgoodstype() {
		return this.zgoodstype;
	}

	public void setZgoodstype(Integer zgoodstype) {
		this.zgoodstype = zgoodstype;
	}

	@Column(name = "name", length = 40)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "createtime")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "size", length = 20)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "unit", length = 20)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "headimage")
	public Integer getHeadimage() {
		return this.headimage;
	}

	public void setHeadimage(Integer headimage) {
		this.headimage = headimage;
	}

	@Column(name = "sale")
	public Integer getSale() {
		return this.sale;
	}

	public void setSale(Integer sale) {
		this.sale = sale;
	}

	@Column(name = "goodsstatus")
	public Integer getGoodsstatus() {
		return this.goodsstatus;
	}

	public void setGoodsstatus(Integer goodsstatus) {
		this.goodsstatus = goodsstatus;
	}

	@Column(name = "price" )
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}