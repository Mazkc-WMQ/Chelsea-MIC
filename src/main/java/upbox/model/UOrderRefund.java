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
 * 订单退款记录表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_order_refund")
public class UOrderRefund implements java.io.Serializable {
	private static final long serialVersionUID = 2665942673358963034L;
	private String pkId;
	private UOrder UOrder;
	private Date aplDate;
	private Date aplTime;
	private Date doDate;
	private Date doTime;
	private Date overDate;
	private Date overTime;
	private Double price;
	private String way;
	private String remark;

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

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "apl_date", length = 13)
	public Date getAplDate() {
		return this.aplDate;
	}

	public void setAplDate(Date aplDate) {
		this.aplDate = aplDate;
	}

	@Column(name = "apl_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getAplTime() {
		return this.aplTime;
	}

	public void setAplTime(Date aplTime) {
		this.aplTime = aplTime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "do_date", length = 13)
	public Date getDoDate() {
		return this.doDate;
	}

	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}

	@Column(name = "do_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getDoTime() {
		return this.doTime;
	}

	public void setDoTime(Date doTime) {
		this.doTime = doTime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "over_date", length = 13)
	public Date getOverDate() {
		return this.overDate;
	}

	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}

	@Column(name = "over_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getOverTime() {
		return this.overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	@Column(name = "price", precision = 10)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "way", length = 20)
	public String getWay() {
		return this.way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}