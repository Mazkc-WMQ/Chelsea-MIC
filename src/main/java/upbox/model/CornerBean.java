package upbox.model;

public class CornerBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5418397698132219692L;
	
	//1.我的球队 2.签约球队 3.推荐擂主 4.推荐约战 5.挑战中 6.约战中
	private Integer corId;//角标主键 
	private Integer corType;//角标主键 
	private String corName;//角标名称
	private Integer corPriorityId;//角标相同时的优先标识
	private Integer corWinCount;//战胜获得积分
	private Integer corMemberType;//队内角色
	private String corMemberTypeName;//队内角色名称
	private String corMemberTypeName1;//队内角色名称
	private Integer corNumber;//队内背号
	private Integer corPosition;//队内位置
	private String corPositionName;//队内位置名称
	
	public Integer getCorId() {
		return corId;
	}
	public Integer getCorType() {
		return corType;
	}
	public void setCorType(Integer corType) {
		this.corType = corType;
	}
	public void setCorId(Integer corId) {
		this.corId = corId;
	}
	public String getCorName() {
		return corName;
	}
	public void setCorName(String corName) {
		this.corName = corName;
	}
	public Integer getCorPriorityId() {
		return corPriorityId;
	}
	public void setCorPriorityId(Integer corPriorityId) {
		this.corPriorityId = corPriorityId;
	}
	public Integer getCorWinCount() {
		return corWinCount;
	}
	public void setCorWinCount(Integer corWinCount) {
		this.corWinCount = corWinCount;
	}
	public Integer getCorMemberType() {
		return corMemberType;
	}
	public void setCorMemberType(Integer corMemberType) {
		this.corMemberType = corMemberType;
	}
	public Integer getCorNumber() {
		return corNumber;
	}
	public void setCorNumber(Integer corNumber) {
		this.corNumber = corNumber;
	}
	public Integer getCorPosition() {
		return corPosition;
	}
	public void setCorPosition(Integer corPosition) {
		this.corPosition = corPosition;
	}
	public String getCorMemberTypeName() {
		return corMemberTypeName;
	}
	public void setCorMemberTypeName(String corMemberTypeName) {
		this.corMemberTypeName = corMemberTypeName;
	}
	public String getCorPositionName() {
		return corPositionName;
	}
	public void setCorPositionName(String corPositionName) {
		this.corPositionName = corPositionName;
	}
	public String getCorMemberTypeName1() {
		return corMemberTypeName1;
	}
	public void setCorMemberTypeName1(String corMemberTypeName1) {
		this.corMemberTypeName1 = corMemberTypeName1;
	}

}
