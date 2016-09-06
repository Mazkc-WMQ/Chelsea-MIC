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
 * 擂主表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team_lz")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTeamLz implements java.io.Serializable {
	private static final long serialVersionUID = -1253967238083015637L;
	private String keyId;
	private String isLz;
	private Date stdate;
	private Date sttime;
	private Date enddate;
	private Date edntime;
	private UTeam uTeam;
	

	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "is_lz", length = 20)
	public String getIsLz() {
		return isLz;
	}

	public void setIsLz(String isLz) {
		this.isLz = isLz;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "stdate", length = 13)
	public Date getStdate() {
		return stdate;
	}

	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}

	@Column(name = "sttime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getSttime() {
		return sttime;
	}

	public void setSttime(Date sttime) {
		this.sttime = sttime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "enddate", length = 13)
	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@Column(name = "edntime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getEdntime() {
		return edntime;
	}

	public void setEdntime(Date edntime) {
		this.edntime = edntime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getuTeam() {
		return uTeam;
	}

	public void setuTeam(UTeam uTeam) {
		this.uTeam = uTeam;
	}

}