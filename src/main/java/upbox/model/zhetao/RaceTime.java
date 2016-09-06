package upbox.model.zhetao;

/**
 * 正在进行的比赛
 * @author yuancao
 *
 * 13611929818
 */
public class RaceTime {
	private Integer id;//ID
	private Integer rid;//比赛ID
	private Integer rrid;//赛制ID
	private Integer rtid;//轮次(赛制中轮次下拉选择)
	private String startdate;//比赛开始日期(包括小时分钟)
	private String enddate;//比赛结束日期(包括小时分钟)
	private String regdate;//添加日期
	private Integer maingqid;//本系统球队分组ID,主场
	private Integer subgqid;//本系统球队分组ID,客场
	private String place;//比赛地点
	private String timeraw;//比赛时间直输(保留)
	private String result;//比赛结果(拆分下面两条)
	private Integer scorem;//主场得分
	private Integer scores;//客场得分
	private String straw;//硬输入状态(待用)
	private String liveurl;//直播URL
	private String allurl;//全场URL
	private String pickurl;//精华URL
	private String picurl;//图文战报??URL?
	private String picfor;//图文前瞻??URL??
	private String mainoqid;//未知主场球队ID
	private String suboqid;//未知客场球队ID
	private String userName;//发布人
	private String pageurl;//在赛果中有一个数据统计页面连接(未知内容)
	private Integer wltype;//胜负类型,0-普通,1-点球决胜,2-??,3-罢赛,4-取消
	private Integer ttype;//赛程类型,0-正常,1-轮空
	private Integer mtimeid;//主场分组为该赛程的赢家,待用
	private Integer stimeid;//客场分组为该赛程的赢家, 待用
	private String name;//赛程名称
	private String timemgroup;//人工设置主场名称
	private String timesgroup;//人工设置客场名称
	private Integer recomflag;//是否推荐标志,1-为推荐
	private Integer gstatus;//0-没获取(数据更新时更新),1-已获取(接口访问时更新)
	private String updatedate;//最后更新日期	
	private Integer mpid;//场地ID
	private Integer recomorder;//推荐顺序
	private Integer mtimeidrev;//主场分组为该赛程的赢家,待用(双循环反场)
	private Integer stimeidrev;//客场分组为该赛程的赢家, 待用(双循环反场)
	private Integer mtimeflag;//以赛程为主球队输/赢标志,动脉0-赢家,1-输家
	private Integer stimeflag;//以赛程为客队,输赢标志0-赢家,1-输家
	private Integer resultflag;//结果录入状态,0-未知,1-结果已经完成输入
	
    /** 轮次显示名称, 人为定义 **/
	private String turnshow;
	
	private ChonTeam mainGroup;
	
	private ChonTeam subGroup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Integer getRrid() {
		return rrid;
	}

	public void setRrid(Integer rrid) {
		this.rrid = rrid;
	}

	public Integer getRtid() {
		return rtid;
	}

	public void setRtid(Integer rtid) {
		this.rtid = rtid;
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

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public Integer getMaingqid() {
		return maingqid;
	}

	public void setMaingqid(Integer maingqid) {
		this.maingqid = maingqid;
	}

	public Integer getSubgqid() {
		return subgqid;
	}

	public void setSubgqid(Integer subgqid) {
		this.subgqid = subgqid;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTimeraw() {
		return timeraw;
	}

	public void setTimeraw(String timeraw) {
		this.timeraw = timeraw;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getScorem() {
		return scorem;
	}

	public void setScorem(Integer scorem) {
		this.scorem = scorem;
	}

	public Integer getScores() {
		return scores;
	}

	public void setScores(Integer scores) {
		this.scores = scores;
	}

	public String getStraw() {
		return straw;
	}

	public void setStraw(String straw) {
		this.straw = straw;
	}

	public String getLiveurl() {
		return liveurl;
	}

	public void setLiveurl(String liveurl) {
		this.liveurl = liveurl;
	}

	public String getAllurl() {
		return allurl;
	}

	public void setAllurl(String allurl) {
		this.allurl = allurl;
	}

	public String getPickurl() {
		return pickurl;
	}

	public void setPickurl(String pickurl) {
		this.pickurl = pickurl;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getPicfor() {
		return picfor;
	}

	public void setPicfor(String picfor) {
		this.picfor = picfor;
	}

	public String getMainoqid() {
		return mainoqid;
	}

	public void setMainoqid(String mainoqid) {
		this.mainoqid = mainoqid;
	}

	public String getSuboqid() {
		return suboqid;
	}

	public void setSuboqid(String suboqid) {
		this.suboqid = suboqid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPageurl() {
		return pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	public Integer getWltype() {
		return wltype;
	}

	public void setWltype(Integer wltype) {
		this.wltype = wltype;
	}

	public Integer getTtype() {
		return ttype;
	}

	public void setTtype(Integer ttype) {
		this.ttype = ttype;
	}

	public Integer getMtimeid() {
		return mtimeid;
	}

	public void setMtimeid(Integer mtimeid) {
		this.mtimeid = mtimeid;
	}

	public Integer getStimeid() {
		return stimeid;
	}

	public void setStimeid(Integer stimeid) {
		this.stimeid = stimeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimemgroup() {
		return timemgroup;
	}

	public void setTimemgroup(String timemgroup) {
		this.timemgroup = timemgroup;
	}

	public String getTimesgroup() {
		return timesgroup;
	}

	public void setTimesgroup(String timesgroup) {
		this.timesgroup = timesgroup;
	}

	public Integer getRecomflag() {
		return recomflag;
	}

	public void setRecomflag(Integer recomflag) {
		this.recomflag = recomflag;
	}

	public Integer getGstatus() {
		return gstatus;
	}

	public void setGstatus(Integer gstatus) {
		this.gstatus = gstatus;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public Integer getMpid() {
		return mpid;
	}

	public void setMpid(Integer mpid) {
		this.mpid = mpid;
	}

	public Integer getRecomorder() {
		return recomorder;
	}

	public void setRecomorder(Integer recomorder) {
		this.recomorder = recomorder;
	}

	public Integer getMtimeidrev() {
		return mtimeidrev;
	}

	public void setMtimeidrev(Integer mtimeidrev) {
		this.mtimeidrev = mtimeidrev;
	}

	public Integer getStimeidrev() {
		return stimeidrev;
	}

	public void setStimeidrev(Integer stimeidrev) {
		this.stimeidrev = stimeidrev;
	}

	public Integer getMtimeflag() {
		return mtimeflag;
	}

	public void setMtimeflag(Integer mtimeflag) {
		this.mtimeflag = mtimeflag;
	}

	public Integer getStimeflag() {
		return stimeflag;
	}

	public void setStimeflag(Integer stimeflag) {
		this.stimeflag = stimeflag;
	}

	public Integer getResultflag() {
		return resultflag;
	}

	public void setResultflag(Integer resultflag) {
		this.resultflag = resultflag;
	}

	public String getTurnshow() {
		return turnshow;
	}

	public void setTurnshow(String turnshow) {
		this.turnshow = turnshow;
	}

	public ChonTeam getMainGroup() {
		return mainGroup;
	}

	public void setMainGroup(ChonTeam mainGroup) {
		this.mainGroup = mainGroup;
	}

	public ChonTeam getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(ChonTeam subGroup) {
		this.subGroup = subGroup;
	}
	
	
}
