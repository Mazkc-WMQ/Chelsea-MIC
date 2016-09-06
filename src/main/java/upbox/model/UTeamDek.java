package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 球队挑战信息
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team_dek")
public class UTeamDek implements java.io.Serializable {
	private static final long serialVersionUID = 7536300182864687297L;
	private String teamDekId;
	private UTeam UTeam;
	private Integer allDekCount;
	private Integer verDekCount;
	private Integer drawDekCount;
	private Integer failDekCount;
	private Integer allMatchCount;
	private Integer verMatchCount;
	private Integer drawMatchCount;
	private Integer failMatchCount;
	private Integer mostGoal;
	private Integer mostFumble;
	private Integer allGoal;
	private Integer allFumble;
	private Integer gd;
	private Integer historyGd;
	private String chances;
	private String historyChances;
	private Integer straCount;
	private Integer strafCount;
	private Integer mostStraCount;
	private Integer mostStrafCount;

	@Id
	@Column(name = "team_dek_id", unique = true, nullable = false, length = 60)
	public String getTeamDekId() {
		return this.teamDekId;
	}

	public void setTeamDekId(String teamDekId) {
		this.teamDekId = teamDekId;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return this.UTeam;
	}

	public void setUTeam(UTeam UTeam) {
		this.UTeam = UTeam;
	}

	@Column(name = "all_dek_count")
	public Integer getAllDekCount() {
		return this.allDekCount;
	}

	public void setAllDekCount(Integer allDekCount) {
		this.allDekCount = allDekCount;
	}

	@Column(name = "ver_dek_count")
	public Integer getVerDekCount() {
		return this.verDekCount;
	}

	public void setVerDekCount(Integer verDekCount) {
		this.verDekCount = verDekCount;
	}

	@Column(name = "draw_dek_count")
	public Integer getDrawDekCount() {
		return this.drawDekCount;
	}

	public void setDrawDekCount(Integer drawDekCount) {
		this.drawDekCount = drawDekCount;
	}

	@Column(name = "fail_dek_count")
	public Integer getFailDekCount() {
		return this.failDekCount;
	}

	public void setFailDekCount(Integer failDekCount) {
		this.failDekCount = failDekCount;
	}

	@Column(name = "all_match_count")
	public Integer getAllMatchCount() {
		return this.allMatchCount;
	}

	public void setAllMatchCount(Integer allMatchCount) {
		this.allMatchCount = allMatchCount;
	}

	@Column(name = "ver_match_count")
	public Integer getVerMatchCount() {
		return this.verMatchCount;
	}

	public void setVerMatchCount(Integer verMatchCount) {
		this.verMatchCount = verMatchCount;
	}

	@Column(name = "draw_match_count")
	public Integer getDrawMatchCount() {
		return this.drawMatchCount;
	}

	public void setDrawMatchCount(Integer drawMatchCount) {
		this.drawMatchCount = drawMatchCount;
	}

	@Column(name = "fail_match_count")
	public Integer getFailMatchCount() {
		return this.failMatchCount;
	}

	public void setFailMatchCount(Integer failMatchCount) {
		this.failMatchCount = failMatchCount;
	}

	@Column(name = "most_goal")
	public Integer getMostGoal() {
		return this.mostGoal;
	}

	public void setMostGoal(Integer mostGoal) {
		this.mostGoal = mostGoal;
	}

	@Column(name = "most_fumble")
	public Integer getMostFumble() {
		return this.mostFumble;
	}

	public void setMostFumble(Integer mostFumble) {
		this.mostFumble = mostFumble;
	}

	@Column(name = "all_goal")
	public Integer getAllGoal() {
		return this.allGoal;
	}

	public void setAllGoal(Integer allGoal) {
		this.allGoal = allGoal;
	}

	@Column(name = "all_fumble")
	public Integer getAllFumble() {
		return this.allFumble;
	}

	public void setAllFumble(Integer allFumble) {
		this.allFumble = allFumble;
	}

	@Column(name = "gd")
	public Integer getGd() {
		return this.gd;
	}

	public void setGd(Integer gd) {
		this.gd = gd;
	}

	@Column(name = "history_gd")
	public Integer getHistoryGd() {
		return this.historyGd;
	}

	public void setHistoryGd(Integer historyGd) {
		this.historyGd = historyGd;
	}

	@Column(name = "chances", length = 30)
	public String getChances() {
		return this.chances;
	}

	public void setChances(String chances) {
		this.chances = chances;
	}

	@Column(name = "history_chances", length = 30)
	public String getHistoryChances() {
		return this.historyChances;
	}

	public void setHistoryChances(String historyChances) {
		this.historyChances = historyChances;
	}

	@Column(name = "stra_count")
	public Integer getStraCount() {
		return this.straCount;
	}

	public void setStraCount(Integer straCount) {
		this.straCount = straCount;
	}

	@Column(name = "straf_count")
	public Integer getStrafCount() {
		return this.strafCount;
	}

	public void setStrafCount(Integer strafCount) {
		this.strafCount = strafCount;
	}

	@Column(name = "most_stra_count")
	public Integer getMostStraCount() {
		return this.mostStraCount;
	}

	public void setMostStraCount(Integer mostStraCount) {
		this.mostStraCount = mostStraCount;
	}

	@Column(name = "most_straf_count")
	public Integer getMostStrafCount() {
		return this.mostStrafCount;
	}

	public void setMostStrafCount(Integer mostStrafCount) {
		this.mostStrafCount = mostStrafCount;
	}

}