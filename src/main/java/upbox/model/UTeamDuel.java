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
 * 球队约战信息
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team_duel")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTeamDuel implements java.io.Serializable {
	private static final long serialVersionUID = 5225546562317938283L;
	private String teamDuelId;
	private UTeam UTeam;
	private Integer allDuelCount;
	private Integer verDuelCount;
	private Integer drawDuelCount;
	private Integer failDuelCount;
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
	@Column(name = "team_duel_id", unique = true, nullable = false, length = 60)
	public String getTeamDuelId() {
		return this.teamDuelId;
	}

	public void setTeamDuelId(String teamDuelId) {
		this.teamDuelId = teamDuelId;
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

	@Column(name = "all_duel_count")
	public Integer getAllDuelCount() {
		return this.allDuelCount;
	}

	public void setAllDuelCount(Integer allDuelCount) {
		this.allDuelCount = allDuelCount;
	}

	@Column(name = "ver_duel_count")
	public Integer getVerDuelCount() {
		return this.verDuelCount;
	}

	public void setVerDuelCount(Integer verDuelCount) {
		this.verDuelCount = verDuelCount;
	}

	@Column(name = "draw_duel_count")
	public Integer getDrawDuelCount() {
		return this.drawDuelCount;
	}

	public void setDrawDuelCount(Integer drawDuelCount) {
		this.drawDuelCount = drawDuelCount;
	}

	@Column(name = "fail_duel_count")
	public Integer getFailDuelCount() {
		return this.failDuelCount;
	}

	public void setFailDuelCount(Integer failDuelCount) {
		this.failDuelCount = failDuelCount;
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