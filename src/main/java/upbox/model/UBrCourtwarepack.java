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
 * 下属球场关联商品包
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtwarepack")
public class UBrCourtwarepack implements java.io.Serializable {
	private static final long serialVersionUID = 1754605265690979495L;
	private String wareCourtPkId;
	private UBrCourt UBrCourt;
	private UMall umall;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private Date stdate;
	private String sttime;
	private Date enddate;
	private String endtime;

	
	@Id
	@Column(name = "ware_court_pk_id", unique = true, nullable = false, length = 60)
	public String getWareCourtPkId()
	{
		return wareCourtPkId;
	}

	public void setWareCourtPkId(String wareCourtPkId)
	{
		this.wareCourtPkId = wareCourtPkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getUBrCourt() {
		return this.UBrCourt;
	}

	public void setUBrCourt(UBrCourt UBrCourt) {
		this.UBrCourt = UBrCourt;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	public UMall getUmall()
	{
		return umall;
	}

	public void setUmall(UMall umall)
	{
		this.umall = umall;
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

	@Column(name = "createuser", length = 60)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "modifydate", length = 13)
	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	@Column(name = "modifytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getModifytime() {
		return this.modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Column(name = "modifyuser", length = 60)
	public String getModifyuser() {
		return this.modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return this.stdate;
	}

	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	
	@Column(name = "sttime", length = 20)
	public String getSttime()
	{
		return sttime;
	}

	public void setSttime(String sttime)
	{
		this.sttime = sttime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "enddate", length = 13)
	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@Column(name = "endtime", length = 20)
	public String getEndtime()
	{
		return endtime;
	}
}