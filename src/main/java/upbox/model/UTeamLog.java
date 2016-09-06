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
 * 球队操作记录表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team_log")
public class UTeamLog implements java.io.Serializable {
	private static final long serialVersionUID = -6550840723555611369L;
	private String teamLogId;
	private String ubuserid;
	private UTeam UTeam;
	private Date inputDate;
	private Date inputTime;
	private String log;
	
	@Id
	@Column(name = "team_log_id", unique = true, nullable = false, length = 60)
	public String getTeamLogId() {
		return this.teamLogId;
	}

	public void setTeamLogId(String teamLogId) {
		this.teamLogId = teamLogId;
	}


	@Column(name = "input_user", length = 60)
	public String getUbuserid()
	{
		return ubuserid;
	}

	public void setUbuserid(String ubuserid)
	{
		this.ubuserid = ubuserid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return this.UTeam;
	}

	public void setUTeam(UTeam UTeam) {
		this.UTeam = UTeam;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "input_date", length = 13)
	public Date getInputDate() {
		return this.inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	@Column(name = "input_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getInputTime() {
		return this.inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	
	@Column(name = "log", length = 255)
	public String getLog() {
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}

}