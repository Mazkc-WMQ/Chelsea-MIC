package upbox.movetest.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "u_champion")
public class UChampion implements java.io.Serializable {
	
	private static final long serialVersionUID = -7017527838162350065L;
	
	private String champion_id;
	private Integer court_id;
	private Integer team_id;
	private String checktime;
	private Integer ischampion;
	private String teamabstract;
	private String honor;
	private String video_url;
	private String canceltype;
	private String cancelreason;
	
	
	@Id
	@Column(name = "champion_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public String getChampion_id() {
		return champion_id;
	}
	public void setChampion_id(String champion_id) {
		this.champion_id = champion_id;
	}
	
	@Column(name = "court_id")
	public Integer getCourt_id() {
		return court_id;
	}
	public void setCourt_id(Integer court_id) {
		this.court_id = court_id;
	}
	
	@Column(name = "team_id")
	public Integer getTeam_id() {
		return team_id;
	}
	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}
	
	@Column(name = "checktime")
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	
	@Column(name = "ischampion")
	public Integer getIschampion() {
		return ischampion;
	}
	public void setIschampion(Integer ischampion) {
		this.ischampion = ischampion;
	}
	
	@Column(name = "teamabstract")
	public String getTeamabstract() {
		return teamabstract;
	}
	public void setTeamabstract(String teamabstract) {
		this.teamabstract = teamabstract;
	}
	
	@Column(name = "honor")
	public String getHonor() {
		return honor;
	}
	public void setHonor(String honor) {
		this.honor = honor;
	}
	
	@Column(name = "video_url")
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	
	@Column(name = "canceltype")
	public String getCanceltype() {
		return canceltype;
	}
	public void setCanceltype(String canceltype) {
		this.canceltype = canceltype;
	}
	
	@Column(name = "cancelreason")
	public String getCancelreason() {
		return cancelreason;
	}
	public void setCancelreason(String cancelreason) {
		this.cancelreason = cancelreason;
	}
	
}
