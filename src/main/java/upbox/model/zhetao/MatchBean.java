package upbox.model.zhetao;

import java.util.List;

public class MatchBean {
	private Integer matchstatus;
	private String address;
	private String name;
	private Integer id;
	private String description;
	private Integer groups;
	private Integer status;
	private Integer mtype;
	private Integer mstatus;
	private String logo;
	private String startdate;
	private String enddate;
	private String rid;
	private String banner;
	private String sname;
	private Integer curruleid;
	private Integer curturn;
	private Integer enrollflag;
	private Integer curtimeid;
	private Integer mages;
	private Integer mrule;
	private String enrollstart;
	private String enrollend;
	private Integer mteams;
	private Integer rankruleid;
	private List<TeamRank> allranktop;//整个赛事排行前3名(注:该数据应该不对,当前系统排名只有按赛制进行排名)  , 结构请参考UBRaceRank表结构
	private List<MyRollTeam> myenrollteams;//我的球队报名参加的球队列表(需要传入有效的teamids或playerids), 结构请参考UBRaceTeam
	private List<RaceTime> matchontimes;//正在进行的比赛赛程列表(UBRaceTime), 赛程结构见后说明(UBRaceTime)
    private RaceTime nexttime;//下一场即将开始的比赛, 只有matchstatus=1,正在进行的比赛没有时才出现!
    private List<RaceGroup> mymatchgroups; //我的球队在本赛事中的分组情况, 参考表结构 UBRaceGroup
    private List<TeamRank> myteamranks;//我的球队在本赛事中全部排名列表(注有多赛制则有多条记录)

	
	
	public Integer getMatchstatus() {
		return matchstatus;
	}
	public void setMatchstatus(Integer matchstatus) {
		this.matchstatus = matchstatus;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getGroups() {
		return groups;
	}
	public void setGroups(Integer groups) {
		this.groups = groups;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getMtype() {
		return mtype;
	}
	public void setMtype(Integer mtype) {
		this.mtype = mtype;
	}
	public Integer getMstatus() {
		return mstatus;
	}
	public void setMstatus(Integer mstatus) {
		this.mstatus = mstatus;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Integer getCurruleid() {
		return curruleid;
	}
	public void setCurruleid(Integer curruleid) {
		this.curruleid = curruleid;
	}
	public Integer getCurturn() {
		return curturn;
	}
	public void setCurturn(Integer curturn) {
		this.curturn = curturn;
	}
	public Integer getEnrollflag() {
		return enrollflag;
	}
	public void setEnrollflag(Integer enrollflag) {
		this.enrollflag = enrollflag;
	}
	public Integer getCurtimeid() {
		return curtimeid;
	}
	public void setCurtimeid(Integer curtimeid) {
		this.curtimeid = curtimeid;
	}
	public Integer getMages() {
		return mages;
	}
	public void setMages(Integer mages) {
		this.mages = mages;
	}
	public Integer getMrule() {
		return mrule;
	}
	public void setMrule(Integer mrule) {
		this.mrule = mrule;
	}
	public String getEnrollstart() {
		return enrollstart;
	}
	public void setEnrollstart(String enrollstart) {
		this.enrollstart = enrollstart;
	}
	public String getEnrollend() {
		return enrollend;
	}
	public void setEnrollend(String enrollend) {
		this.enrollend = enrollend;
	}
	public Integer getMteams() {
		return mteams;
	}
	public void setMteams(Integer mteams) {
		this.mteams = mteams;
	}
	public Integer getRankruleid() {
		return rankruleid;
	}
	public void setRankruleid(Integer rankruleid) {
		this.rankruleid = rankruleid;
	}
	public List<TeamRank> getAllranktop() {
		return allranktop;
	}
	public void setAllranktop(List<TeamRank> allranktop) {
		this.allranktop = allranktop;
	}
	public List<MyRollTeam> getMyenrollteams() {
		return myenrollteams;
	}
	public void setMyenrollteams(List<MyRollTeam> myenrollteams) {
		this.myenrollteams = myenrollteams;
	}
	public List<RaceTime> getMatchontimes() {
		return matchontimes;
	}
	public void setMatchontimes(List<RaceTime> matchontimes) {
		this.matchontimes = matchontimes;
	}

	public List<RaceGroup> getMymatchgroups() {
		return mymatchgroups;
	}
	public void setMymatchgroups(List<RaceGroup> mymatchgroups) {
		this.mymatchgroups = mymatchgroups;
	}
	public List<TeamRank> getMyteamranks() {
		return myteamranks;
	}
	public void setMyteamranks(List<TeamRank> myteamranks) {
		this.myteamranks = myteamranks;
	}
	public RaceTime getNexttime() {
		return nexttime;
	}
	public void setNexttime(RaceTime nexttime) {
		this.nexttime = nexttime;
	}
	
    
}
