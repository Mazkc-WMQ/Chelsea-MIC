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
 * 球场操作日志bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_court_operlog")
public class UCourtOperlog implements java.io.Serializable {
	private static final long serialVersionUID = 109349768566259367L;
	private String courtOperlogId;
	private UBUser UBUser;
	private UBrCourt UBrCourt;
	private UCourt UCourt;
	private Date createdate;
	private Date createtime;
	private String log;

	@Id
	@Column(name = "court_operlog_id", unique = true, nullable = false, length = 60)
	public String getCourtOperlogId() {
		return this.courtOperlogId;
	}

	public void setCourtOperlogId(String courtOperlogId) {
		this.courtOperlogId = courtOperlogId;
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
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getUBrCourt() {
		return this.UBrCourt;
	}

	public void setUBrCourt(UBrCourt UBrCourt) {
		this.UBrCourt = UBrCourt;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "court_id")
	public UCourt getUCourt() {
		return this.UCourt;
	}

	public void setUCourt(UCourt UCourt) {
		this.UCourt = UCourt;
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

	public String getLog()
	{
		return log;
	}

	public void setLog(String log)
	{
		this.log = log;
	}
}