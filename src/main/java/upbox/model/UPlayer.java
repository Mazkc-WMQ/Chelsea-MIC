package upbox.model;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 球队球员表
 * 
 * @author wmq
 *
 *         15618777630
 */
@Entity
@Table(name = "u_player")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UPlayer implements java.io.Serializable {
	private static final long serialVersionUID = 1798873735200099213L;
	private String playerId;
	private UUser UUser;
	private UTeam UTeam;
	private String teamBelonging;
	private String memberType;
	private String memberTypeName;
	private String memberType2;
	private String memberTypeName2;
	private String position;
	private String positionName;
	private Integer number;
	private String nickname;
	private String remark;
	private Date adddate;
	private Date addtime;
	private Date exitdate;
	private Date exittime;
	private String inTeam;
	private String practiceFoot;
	private String practiceFootName;
	private String exitType;
	private String canPosition;
	private String canPositionName;
	private String expertPosition;
	private String expertPositionName;
	private String isMyself;//判断是否是查看自己的球员信息 1：是  2：不是
	private String isFollow;//是否关注 1：是  2：不是
	private String isMyTeam;//该球员是否是自己队 1：是  2：不是
	private String isTeamLeader;//查看人是否是队长 1：是  2：不是
	private String addqd;
	private String defaultUteam;

	@Id
	@Column(name = "player_id", unique = true, nullable = false, length = 60)
	public String getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return this.UUser;
	}

	public void setUUser(UUser UUser) {
		this.UUser = UUser;
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

	@Column(name = "team_belonging", length = 20)
	public String getTeamBelonging() {
		return this.teamBelonging;
	}

	public void setTeamBelonging(String teamBelonging) {
		this.teamBelonging = teamBelonging;
	}

	@Column(name = "member_type", length = 20)
	public String getMemberType() {
		return this.memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	@Column(name = "position", length = 50)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "number")
	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.DATE)
	@JSON(format = "yyyy-MM-dd")
	@Column(name = "adddate", length = 13)
	public Date getAdddate() {
		return this.adddate;
	}

	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}

	@Column(name = "addtime", length = 15)
	@JSON(format = "HH:mm:ss")
	public Date getAddtime() {
		return this.addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	@Temporal(TemporalType.DATE)
	@JSON(format = "yyyy-MM-dd")
	@Column(name = "exitdate", length = 13)
	public Date getExitdate() {
		return this.exitdate;
	}

	public void setExitdate(Date exitdate) {
		this.exitdate = exitdate;
	}

	@Column(name = "exittime", length = 15)
	@JSON(format = "HH:mm:ss")
	public Date getExittime() {
		return this.exittime;
	}

	public void setExittime(Date exittime) {
		this.exittime = exittime;
	}

	@Column(name = "in_team", length = 20)
	public String getInTeam() {
		return this.inTeam;
	}

	public void setInTeam(String inTeam) {
		this.inTeam = inTeam;
	}


	@Column(name = "practice_foot", length = 20)
	public String getPracticeFoot() {
		return practiceFoot;
	}

	public void setPracticeFoot(String practiceFoot) {
		this.practiceFoot = practiceFoot;
	}

	@Column(name = "exit_type", length = 20)
	public String getExitType() {
		return exitType;
	}

	public void setExitType(String exitType) {
		this.exitType = exitType;
	}

	@Column(name = "can_position", length = 20)
	public String getCanPosition() {
		return canPosition;
	}

	public void setCanPosition(String canPosition) {
		this.canPosition = canPosition;
	}

	@Column(name = "expert_position", length = 20)
	public String getExpertPosition() {
		return expertPosition;
	}

	public void setExpertPosition(String expertPosition) {
		this.expertPosition = expertPosition;
	}

	@Transient
	public String getIsMyself() {
		return isMyself;
	}

	public void setIsMyself(String isMyself) {
		this.isMyself = isMyself;
	}

	@Transient
	public String getMemberType2() {
		return memberType2;
	}

	public void setMemberType2(String memberType2) {
		this.memberType2 = memberType2;
	}

	@Transient
	public String getMemberTypeName2() {
		return memberTypeName2;
	}

	public void setMemberTypeName2(String memberTypeName2) {
		this.memberTypeName2 = memberTypeName2;
	}

	@Transient
	public String getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}

	@Transient
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	@Transient
	public String getMemberTypeName() {
		return memberTypeName;
	}

	public void setMemberTypeName(String memberTypeName) {
		this.memberTypeName = memberTypeName;
	}

	@Transient
	public String getCanPositionName() {
		return canPositionName;
	}

	public void setCanPositionName(String canPositionName) {
		this.canPositionName = canPositionName;
	}

	@Transient
	public String getPracticeFootName() {
		return practiceFootName;
	}

	public void setPracticeFootName(String practiceFootName) {
		this.practiceFootName = practiceFootName;
	}

	@Transient
	public String getExpertPositionName() {
		return expertPositionName;
	}

	public void setExpertPositionName(String expertPositionName) {
		this.expertPositionName = expertPositionName;
	}

	@Transient
	public String getIsMyTeam() {
		return isMyTeam;
	}

	public void setIsMyTeam(String isMyTeam) {
		this.isMyTeam = isMyTeam;
	}

	@Transient
	public String getIsTeamLeader() {
		return isTeamLeader;
	}

	public void setIsTeamLeader(String isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}

	@Column(name = "addqd", length = 30)
	public String getAddqd() {
		return addqd;
	}

	public void setAddqd(String addqd) {
		this.addqd = addqd;
	}

	@Column(name = "default_uteam", length = 30)
	public String getDefaultUteam() {
		return defaultUteam;
	}

	public void setDefaultUteam(String defaultUteam) {
		this.defaultUteam = defaultUteam;
	}

	public UPlayer() {
		// TODO Auto-generated constructor stub
	}

	public UPlayer(String playerId, upbox.model.UUser uUser, upbox.model.UTeam uTeam, String teamBelonging,
			String memberType, String position, Integer number, String nickname, String remark, Date adddate,
			Date addtime, Date exitdate, Date exittime, String inTeam,String practiceFoot,String expertPosition,
			String canPosition,String exitType) {
		super();
		this.playerId = playerId;
		UUser = uUser;
		UTeam = uTeam;
		this.teamBelonging = teamBelonging;
		this.memberType = memberType;
		this.position = position;
		this.number = number;
		this.nickname = nickname;
		this.remark = remark;
		this.adddate = adddate;
		this.addtime = addtime;
		this.exitdate = exitdate;
		this.exittime = exittime;
		this.inTeam = inTeam;
		this.practiceFoot = practiceFoot;
		this.exitType = exitType;
		this.canPosition = canPosition;
		this.expertPosition = expertPosition;
	}

}