package upbox.outModel;

import java.io.Serializable;
import java.util.List;

/**
 * 球队列表输出model
 * @author mercideng
 *
 */
public class OutUteamList implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String teamId;
	private String name;
	private String shortName;
	private String teamClass;
	private String teamClassName;
	private String imgurl;
	private String avgAge;
	private String avgHeight;
	private String avgWeight;
	private String rank;
	private Integer teamCount;
	private String remark;
	private String chances;
	private String integral;
	private String memberType;
	private String memberTypeName;
	private String memberType2;
	private String memberTypeName2;
	private String number;
	private String position;
	private String positionName;
	private List<OutUteamList> outUteamLists;
	private String ver;//胜
	private String draw;//平
	private String fail;//负
	private String event;//场次
	private String isMyself;//判断是否是查看自己的球员信息 1：是  2：不是
	private String isNull;//判断对象的信息是不是为空 1：是  2：不是
	private String isFollow;//是否关注 1：是  2：不是
	private String isTeamLeader;//是否队长 1：是  2：不是
	private Integer maximum;//球队限制人数
	private String province;
	private String city;
	private String county;
	private String recommendTeam;
	private String followStatus;
	public String getMemberType2() {
		return memberType2;
	}
	public void setMemberType2(String memberType2) {
		this.memberType2 = memberType2;
	}
	public String getMemberTypeName2() {
		return memberTypeName2;
	}
	public void setMemberTypeName2(String memberTypeName2) {
		this.memberTypeName2 = memberTypeName2;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getFollowStatus() {
		return followStatus;
	}
	public void setFollowStatus(String followStatus) {
		this.followStatus = followStatus;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	public String getFail() {
		return fail;
	}
	public void setFail(String fail) {
		this.fail = fail;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getIsMyself() {
		return isMyself;
	}
	public void setIsMyself(String isMyself) {
		this.isMyself = isMyself;
	}
	public String getIsFollow() {
		return isFollow;
	}
	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}
	public String getIsTeamLeader() {
		return isTeamLeader;
	}
	public void setIsTeamLeader(String isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}
	public Integer getMaximum() {
		return maximum;
	}
	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getRecommendTeam() {
		return recommendTeam;
	}
	public void setRecommendTeam(String recommendTeam) {
		this.recommendTeam = recommendTeam;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamClass() {
		return teamClass;
	}
	public void setTeamClass(String teamClass) {
		this.teamClass = teamClass;
	}
	public String getTeamClassName() {
		return teamClassName;
	}
	public void setTeamClassName(String teamClassName) {
		this.teamClassName = teamClassName;
	}
	public String getAvgAge() {
		return avgAge;
	}
	public void setAvgAge(String avgAge) {
		this.avgAge = avgAge;
	}
	public String getAvgHeight() {
		return avgHeight;
	}
	public void setAvgHeight(String avgHeight) {
		this.avgHeight = avgHeight;
	}
	public String getAvgWeight() {
		return avgWeight;
	}
	public void setAvgWeight(String avgWeight) {
		this.avgWeight = avgWeight;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Integer getTeamCount() {
		return teamCount;
	}
	public void setTeamCount(Integer teamCount) {
		this.teamCount = teamCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChances() {
		return chances;
	}
	public void setChances(String chances) {
		this.chances = chances;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public List<OutUteamList> getOutUteamLists() {
		return outUteamLists;
	}
	public void setOutUteamLists(List<OutUteamList> outUteamLists) {
		this.outUteamLists = outUteamLists;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getMemberTypeName() {
		return memberTypeName;
	}
	public void setMemberTypeName(String memberTypeName) {
		this.memberTypeName = memberTypeName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
}
