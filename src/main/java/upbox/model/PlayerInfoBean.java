package upbox.model;


/**
 * 页面展示的实体类事件bean
 * @author yuancao
 *
 */
public class PlayerInfoBean implements java.io.Serializable {

	private static final long serialVersionUID = -1791227795695198168L;
	
	private String shirtsNum;//背号
	private String realName;//真实姓名
	private String nickName;//真实姓名
	private String time;//事件时间
	private String eventType;//时间类型
	
	public String getShirtsNum() {
		return shirtsNum;
	}
	public void setShirtsNum(String shirtsNum) {
		this.shirtsNum = shirtsNum;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	

	
}