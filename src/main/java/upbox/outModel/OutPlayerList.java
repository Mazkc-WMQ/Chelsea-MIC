package upbox.outModel;

import java.io.Serializable;
import java.util.List;

/**
 * 球员列表输出model
 * @author mercideng
 *
 */
public class OutPlayerList implements Serializable{
	private static final long serialVersionUID = 778763757316866997L;
	private String playerId;
	private String nickname;
	private String realname;
	private String sex;
	private String sexName;
	private String age;
	private String birthday;
	private String height;
	private String weight;
	private String remark;
	private String imgurl;
	private String practiceFoot;
	private String practiceFootName;
	private String canPosition;
	private String canPositionName;
	private String expertPosition;
	private String expertPositionName;
	private String memberType;
	private String memberTypeName;
	private String memberTypeIsUnique;//第一身份是否唯一
	private String memberTypeIsUnique2;//第二身份是否唯一
	private String memberType2;
	private String memberTypeName2;
	private String number;
	private String position;
	private String isMyself;//判断是否是查看自己的球员信息 1：是  2：不是
	private String positionName;
	private String worthCount;//身价
	private Integer followCount;//关注数
	private List<OutPlayerList> outPlayerLists;
	public String getMemberTypeIsUnique() {
		return memberTypeIsUnique;
	}
	public void setMemberTypeIsUnique(String memberTypeIsUnique) {
		this.memberTypeIsUnique = memberTypeIsUnique;
	}
	public String getMemberTypeIsUnique2() {
		return memberTypeIsUnique2;
	}
	public void setMemberTypeIsUnique2(String memberTypeIsUnique2) {
		this.memberTypeIsUnique2 = memberTypeIsUnique2;
	}
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
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getPracticeFoot() {
		return practiceFoot;
	}
	public void setPracticeFoot(String practiceFoot) {
		this.practiceFoot = practiceFoot;
	}
	public String getCanPosition() {
		return canPosition;
	}
	public void setCanPosition(String canPosition) {
		this.canPosition = canPosition;
	}
	public String getExpertPosition() {
		return expertPosition;
	}
	public void setExpertPosition(String expertPosition) {
		this.expertPosition = expertPosition;
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
	public List<OutPlayerList> getOutPlayerLists() {
		return outPlayerLists;
	}
	public void setOutPlayerLists(List<OutPlayerList> outPlayerLists) {
		this.outPlayerLists = outPlayerLists;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPracticeFootName() {
		return practiceFootName;
	}
	public void setPracticeFootName(String practiceFootName) {
		this.practiceFootName = practiceFootName;
	}
	public String getExpertPositionName() {
		return expertPositionName;
	}
	public void setExpertPositionName(String expertPositionName) {
		this.expertPositionName = expertPositionName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMemberTypeName() {
		return memberTypeName;
	}
	public void setMemberTypeName(String memberTypeName) {
		this.memberTypeName = memberTypeName;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIsMyself() {
		return isMyself;
	}
	public void setIsMyself(String isMyself) {
		this.isMyself = isMyself;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getCanPositionName() {
		return canPositionName;
	}
	public void setCanPositionName(String canPositionName) {
		this.canPositionName = canPositionName;
	}
	public String getWorthCount() {
		return worthCount;
	}
	public void setWorthCount(String worthCount) {
		this.worthCount = worthCount;
	}
	public Integer getFollowCount() {
		return followCount;
	}
	public void setFollowCount(Integer followCount) {
		this.followCount = followCount;
	}
	
}
