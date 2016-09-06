package upbox.movetest.bean;

 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

/**
 * 约战订单表
 * @author mazkc
 *
 */
@Entity
@Table(name = "u_dorder")
@DynamicUpdate(true)
public class UDOrder implements java.io.Serializable {
	private static final long serialVersionUID = -7077034103435121652L;
	private Integer id;
	private String price;
	private Date createtime;
	private Integer orderstatus;
	private Integer userid;
	private Integer paytype;
	private String ordernum;
	private String cgoodsnum;
	private String zgoodsnum;
	private String lgoodsnum;
	private String courtnum;
	private Integer paystatus;
	private Integer otype;
	private String paytime;
	private Integer source;
	private String trainnum;
	private int jpusho;
	private int jpushp;
	
 
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "price", length = 20)
	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Column(name = "createtime", length = 29)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "orderstatus")
	public Integer getOrderstatus() {
		return this.orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "paytype")
	public Integer getPaytype() {
		return this.paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	@Column(name = "ordernum", length = 36)
	public String getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "cgoodsnum", length = 36)
	public String getCgoodsnum() {
		return this.cgoodsnum;
	}

	public void setCgoodsnum(String cgoodsnum) {
		this.cgoodsnum = cgoodsnum;
	}

	@Column(name = "zgoodsnum", length = 36)
	public String getZgoodsnum() {
		return this.zgoodsnum;
	}

	public void setZgoodsnum(String zgoodsnum) {
		this.zgoodsnum = zgoodsnum;
	}

	@Column(name = "lgoodsnum", length = 36)
	public String getLgoodsnum() {
		return this.lgoodsnum;
	}

	public void setLgoodsnum(String lgoodsnum) {
		this.lgoodsnum = lgoodsnum;
	}

	@Column(name = "courtnum", length = 36)
	public String getCourtnum() {
		return this.courtnum;
	}

	public void setCourtnum(String courtnum) {
		this.courtnum = courtnum;
	}

	@Column(name = "paystatus")
	public Integer getPaystatus() {
		return this.paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	@Column(name = "otype")
	public Integer getOtype() {
		return this.otype;
	}

	public void setOtype(Integer otype) {
		this.otype = otype;
	}

	@Column(name = "paytime", length = 100)
	public String getPaytime() {
		return this.paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	@Column(name = "source")
	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Column(name = "trainnum", length = 36)
	public String getTrainnum() {
		return this.trainnum;
	}

	public void setTrainnum(String trainnum) {
		this.trainnum = trainnum;
	}

	public int getJpusho()
	{
		return jpusho;
	}

	public void setJpusho(int jpusho)
	{
		this.jpusho = jpusho;
	}

	public int getJpushp()
	{
		return jpushp;
	}

	public void setJpushp(int jpushp)
	{
		this.jpushp = jpushp;
	}
}