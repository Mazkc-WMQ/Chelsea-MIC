package upbox.outModel;

import java.io.Serializable;

/**
 * 该球队球场轴输出model
 * @author mercideng
 *
 */
public class OutUbCourtMap implements Serializable{
	private static final long serialVersionUID = 8304606920853834381L;
	private String idStr;
	private String nameStr;
	private String iconType;
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	public String getNameStr() {
		return nameStr;
	}
	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}
	public String getIconType() {
		return iconType;
	}
	public void setIconType(String iconType) {
		this.iconType = iconType;
	}
	
}
