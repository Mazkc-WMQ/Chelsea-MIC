package upbox.model;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.HashSet;
import java.util.Set;
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
 * 队伍表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTeam implements java.io.Serializable {
	private static final long serialVersionUID = -3733813406332854008L;
	private String teamId;
	private UUser UUser;
	private String teamType;
	private String name;
	private String remark;
	private Integer teamCount;
	private Integer historyCount;
	private String avgAge;
	private String avgHeight;
	private String avgWeight;
	private Date createdate;
	private Date createtime;
	private Date holdDate;
	private Date holdTime;
	private Date disDate;
	private Date disTime;
	private String teamStatus;
	private Integer integral;
	private Integer historyIntegral;
	private Integer rank;
	private Integer historyRank;
	private String reason; 
	private String teamUseStatus;
	private String userCount;
	private String teamsource;
	private String dremark;
	private String shortName;
	private URegion uRegion;
	private Set<URegion> uRegionSet = new HashSet<>(0);//给前端返回区域的整体信息
	private String teamClass;
	private String homeTeamShirts;
	private String awayTeamShirts;
	private String subCourtType;
	private UBrCourt uBrCourt;
	private String chances;//胜率
	private String ver;//胜
	private String draw;//平
	private String fail;//负
	private String event;//场次
	private String isMyself;//判断是否是查看自己的球员信息 1：是  2：不是
	private String isFollow;//是否关注 1：是  2：不是
	private String isTeamLeader;//是否队长 1：是  2：不是
	private Integer maximum;//球队限制人数
	private String province;
	private String city;
	private String county;
	private String recommendTeam;
	private Set<UTeamDek> UTeamDeks = new HashSet<UTeamDek>(0);
	private Set<UPlayer> UPlayers = new HashSet<UPlayer>(0);
	private Set<UTeamImg> UTeamImgs = new HashSet<UTeamImg>(0);
	private Set<UTeamLog> UTeamLogs = new HashSet<UTeamLog>(0);
	private Set<UTeamDuel> UTeamDuels = new HashSet<UTeamDuel>(0);
	private int old_key_id;
	private String createQd;
//	private int team_id_int;
	
	
	@Id
	@Column(name = "team_id", unique = true, nullable = false, length = 60)
	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
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

	@Column(name = "team_type", length = 20)
	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	@Column(name = "name", length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "team_count")
	public Integer getTeamCount() {
		return teamCount;
	}
	public void setTeamCount(Object teamCount) {
		if(teamCount instanceof String){
			this.teamCount = Integer.parseInt((String)teamCount);
		}else if(teamCount instanceof Integer){
			this.teamCount = (Integer)teamCount;
		}
	}

	@Column(name = "history_count")
	public Integer getHistoryCount() {
		return this.historyCount;
	}

	public void setHistoryCount(Integer historyCount) {
		this.historyCount = historyCount;
	}

	@Column(name = "avg_age", length = 30)
	public String getAvgAge() {
		return this.avgAge;
	}

	public void setAvgAge(String avgAge) {
		this.avgAge = avgAge;
	}

	@Column(name = "avg_height", length = 30)
	public String getAvgHeight() {
		return this.avgHeight;
	}

	public void setAvgHeight(String avgHeight) {
		this.avgHeight = avgHeight;
	}

	@Column(name = "avg_weight", length = 30)
	public String getAvgWeight() {
		return this.avgWeight;
	}

	public void setAvgWeight(String avgWeight) {
		this.avgWeight = avgWeight;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}
	
	public void setCreatedate(Date createdate) throws Exception{
		this.createdate = createdate;
	}
//	public void setCreatedate(Object createdate) throws Exception{
//		if(createdate instanceof String){
//			this.createdate = PublicMethod.getStringToDate((String)createdate, "yyyy-MM-dd");
//		}else if(createdate instanceof Integer){
//			this.createdate = (Date)createdate;
//		}else{
//			this.createdate = (Date)createdate;
//		}
//	}

	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "hold_date", length = 13)
	public Date getHoldDate() {
		return this.holdDate;
	}

	public void setHoldDate(Date holdDate) {
		this.holdDate = holdDate;
	}

	@Column(name = "hold_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getHoldTime() {
		return this.holdTime;
	}

	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "dis_date", length = 13)
	public Date getDisDate() {
		return this.disDate;
	}

	public void setDisDate(Date disDate) {
		this.disDate = disDate;
	}

	@Column(name = "dis_time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getDisTime() {
		return this.disTime;
	}

	public void setDisTime(Date disTime) {
		this.disTime = disTime;
	}

	@Column(name = "team_status", length = 20)
	public String getTeamStatus() {
		return this.teamStatus;
	}

	public void setTeamStatus(String teamStatus) {
		this.teamStatus = teamStatus;
	}

	@Column(name = "integral")
	public Integer getIntegral() {
		return this.integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	@Column(name = "history_integral")
	public Integer getHistoryIntegral() {
		return this.historyIntegral;
	}

	public void setHistoryIntegral(Integer historyIntegral) {
		this.historyIntegral = historyIntegral;
	}

	@Column(name = "rank")
	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Object rank) {
		if(rank instanceof String){
			this.rank = Integer.parseInt((String)rank);
		}else if(rank instanceof Integer){
			this.rank = (Integer)rank;
		}
		
	}

	@Column(name = "history_rank")
	public Integer getHistoryRank() {
		return this.historyRank;
	}

	public void setHistoryRank(Integer historyRank) {
		this.historyRank = historyRank;
	}
	@Column(name = "reason", length = 200)
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name="team_use_status")
	public String getTeamUseStatus() {
		return teamUseStatus;
	}
	public void setTeamUseStatus(String teamUseStatus) {
		this.teamUseStatus = teamUseStatus;
	}

	@Transient
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
	}

	/**
	 * @return the teamsource
	 */
	public String getTeamsource() {
		return teamsource;
	}

	/**
	 * @param teamsource the teamsource to set
	 */
	public void setTeamsource(String teamsource) {
		this.teamsource = teamsource;
	}

	/**
	 * @return the dremark
	 */
	public String getDremark() {
		return dremark;
	}

	/**
	 * @param dremark the dremark to set
	 */
	public void setDremark(String dremark) {
		this.dremark = dremark;
	}

	@Column(name = "short_name", length = 100)
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "area")
	public URegion getuRegion() {
		return uRegion;
	}

	public void setuRegion(URegion uRegion) {
		this.uRegion = uRegion;
	}

	@Transient
	public Set<URegion> getuRegionSet() {
		return uRegionSet;
	}

	public void setuRegionSet(Set<URegion> uRegionSet) {
		this.uRegionSet = uRegionSet;
	}

	@Column(name = "team_class", length = 30)
	public String getTeamClass() {
		return teamClass;
	}

	public void setTeamClass(String teamClass) {
		this.teamClass = teamClass;
	}

	@Column(name = "home_team_shirts", length = 30)
	public String getHomeTeamShirts() {
		return homeTeamShirts;
	}

	public void setHomeTeamShirts(String homeTeamShirts) {
		this.homeTeamShirts = homeTeamShirts;
	}

	@Column(name = "away_team_shirts", length = 30)
	public String getAwayTeamShirts() {
		return awayTeamShirts;
	}

	public void setAwayTeamShirts(String awayTeamShirts) {
		this.awayTeamShirts = awayTeamShirts;
	}

	@Column(name = "subcourt_type", length = 30)
	public String getSubCourtType() {
		return subCourtType;
	}

	public void setSubCourtType(String subCourtType) {
		this.subCourtType = subCourtType;
	}

	@Column(name = "recommend_team", length = 30)
	public String getRecommendTeam() {
		return recommendTeam;
	}

	public void setRecommendTeam(String recommendTeam) {
		this.recommendTeam = recommendTeam;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getuBrCourt() {
		return uBrCourt;
	}

	public void setuBrCourt(UBrCourt uBrCourt) {
		this.uBrCourt = uBrCourt;
	}

	@Transient
	public String getChances() {
		return chances;
	}

	public void setChances(String chances) {
		this.chances = chances;
	}

	@Transient
	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	@Transient
	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	@Transient
	public String getFail() {
		return fail;
	}

	public void setFail(String fail) {
		this.fail = fail;
	}

	@Transient
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Transient
	/**
	 * @return the uTeamDeks
	 */
	public Set<UTeamDek> getUTeamDeks() {
		return UTeamDeks;
	}

	/**
	 * @param uTeamDeks the uTeamDeks to set
	 */
	public void setUTeamDeks(Set<UTeamDek> uTeamDeks) {
		UTeamDeks = uTeamDeks;
	}

	@Transient
	/**
	 * @return the uPlayers
	 */
	public Set<UPlayer> getUPlayers() {
		return UPlayers;
	}

	/**
	 * @param uPlayers the uPlayers to set
	 */
	public void setUPlayers(Set<UPlayer> uPlayers) {
		UPlayers = uPlayers;
	}

	@Transient
	/**
	 * @return the uTeamImgs
	 */
	public Set<UTeamImg> getUTeamImgs() {
		return UTeamImgs;
	}

	/**
	 * @param uTeamImgs the uTeamImgs to set
	 */
	public void setUTeamImgs(Set<UTeamImg> uTeamImgs) {
		UTeamImgs = uTeamImgs;
	}

	@Transient
	/**
	 * @return the uTeamLogs
	 */
	public Set<UTeamLog> getUTeamLogs() {
		return UTeamLogs;
	}

	/**
	 * @param uTeamLogs the uTeamLogs to set
	 */
	public void setUTeamLogs(Set<UTeamLog> uTeamLogs) {
		UTeamLogs = uTeamLogs;
	}

	@Transient
	/**
	 * @return the uTeamDuels
	 */
	public Set<UTeamDuel> getUTeamDuels() {
		return UTeamDuels;
	}

	/**
	 * @param uTeamDuels the uTeamDuels to set
	 */
	public void setUTeamDuels(Set<UTeamDuel> uTeamDuels) {
		UTeamDuels = uTeamDuels;
	}

	@Transient
	public String getIsMyself() {
		return isMyself;
	}

	public void setIsMyself(String isMyself) {
		this.isMyself = isMyself;
	}

	@Transient
	public String getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}

	@Column(name = "maximum")
	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	@Transient
	public String getIsTeamLeader() {
		return isTeamLeader;
	}

	public void setIsTeamLeader(String isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}

	@Transient
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Transient
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Transient
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return the old_key_id
	 */
	public int getOld_key_id() {
		return old_key_id;
	}

	/**
	 * @param old_key_id the old_key_id to set
	 */
	public void setOld_key_id(int old_key_id) {
		this.old_key_id = old_key_id;
	}

	@Column(name = "create_qd", length = 30)
	public String getCreateQd() {
		return createQd;
	}

	public void setCreateQd(String createQd) {
		this.createQd = createQd;
	}

//	/**
//	 * @return the team_id_int
//	 */
//	public int getTeam_id_int() {
//		return team_id_int;
//	}
//
//	/**
//	 * @param team_id_int the team_id_int to set
//	 */
//	public void setTeam_id_int(int team_id_int) {
//		this.team_id_int = team_id_int;
//	}

	
}