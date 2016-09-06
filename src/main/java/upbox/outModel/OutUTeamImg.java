package upbox.outModel;

import java.io.Serializable;

/**
 * 球队相册输出model
 * @author mercideng
 *
 */
public class OutUTeamImg implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String teamImgId;
	private String imgurl;
	private String saveurl;
	private String timgUsingType;
	public String getTeamImgId() {
		return teamImgId;
	}
	public void setTeamImgId(String teamImgId) {
		this.teamImgId = teamImgId;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}
	public String getTimgUsingType() {
		return timgUsingType;
	}
	public void setTimgUsingType(String timgUsingType) {
		this.timgUsingType = timgUsingType;
	}
	
}
