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
 * 球场操作记录
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_mall_log")
public class UMallLog implements java.io.Serializable {
	private static final long serialVersionUID = 1542411088228171203L;
	private String proLogId;
	private UMall UMall;
	private UMallPackage UMallpackage;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private String log;

	@Id
	@Column(name = "pro_log_id", unique = true, nullable = false, length = 60)
	public String getProLogId() {
		return this.proLogId;
	}

	public void setProLogId(String proLogId) {
		this.proLogId = proLogId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pro_id")
	public UMall getUMall() {
		return this.UMall;
	}

	public void setUMall(UMall UMall) {
		this.UMall = UMall;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	/**
	 * @return the uMallpackage
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pro_pack_id")
	public UMallPackage getUMallpackage() {
		return UMallpackage;
	}

	/**
	 * @param uMallpackage the uMallpackage to set
	 */
	public void setUMallpackage(UMallPackage uMallpackage) {
		UMallpackage = uMallpackage;
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

	@Column(name = "createuser ", length = 60)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getLog()
	{
		return log;
	}

	public void setLog(String log)
	{
		this.log = log;
	}
}