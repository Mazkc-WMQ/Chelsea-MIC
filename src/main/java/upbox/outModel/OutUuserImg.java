package upbox.outModel;

import java.io.Serializable;

/**
 * 球队相册输出model
 * @author mercideng
 *
 */
public class OutUuserImg implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String pkId;
	private String imgurl;
	private String saveurl;
	private String uimgUsingType;
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
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
	public String getUimgUsingType() {
		return uimgUsingType;
	}
	public void setUimgUsingType(String uimgUsingType) {
		this.uimgUsingType = uimgUsingType;
	}
	
}
