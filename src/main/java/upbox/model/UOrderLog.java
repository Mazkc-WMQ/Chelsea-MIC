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
 * 订单操作记录
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_order_log")
public class UOrderLog implements java.io.Serializable {
	private static final long serialVersionUID = 2757586198552011148L;
	private String pkId;
	private UBUser UUser;
	private UOrder UOrder;
	private Date createdate;
	private Date createtime;
	private String log;

	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 60)
	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUUser() {
		return this.UUser;
	}

	public void setUUser(UBUser UUser) {
		this.UUser = UUser;
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

	@Column(name = "log", length = 250)
	public String getLog()
	{
		return log;
	}

	public void setLog(String log)
	{
		this.log = log;
	}
}