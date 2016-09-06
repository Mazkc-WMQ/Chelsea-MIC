package upbox.model.zhetao;

import java.util.List;

import upbox.model.UPlayerApply;
/**
 * 角色修改实体
 * @author xiaoying
 *
 */
public class ParameterInfos {
	private String isChoice;
	private int lvl;
	private String pikey_id;
	private String isUnique;
	private String isMyselfSelect;
	private Object remark;
	private String isSelect;
	private String params;
	private String isApply;
	private String imgurl;
	private String isOtherSelect;
	private String alreadyApply;
	private String name;
	private List<UPlayerApply> listApply;
	private String act;

	public String getIsChoice() {
		return isChoice;
	}

	public void setIsChoice(String isChoice) {
		this.isChoice = isChoice;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getPikey_id() {
		return pikey_id;
	}

	public void setPikey_id(String pikey_id) {
		this.pikey_id = pikey_id;
	}

	public String getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}

	public String getIsMyselfSelect() {
		return isMyselfSelect;
	}

	public void setIsMyselfSelect(String isMyselfSelect) {
		this.isMyselfSelect = isMyselfSelect;
	}

	public Object getRemark() {
		return remark;
	}

	public void setRemark(Object remark) {
		this.remark = remark;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getIsApply() {
		return isApply;
	}

	public void setIsApply(String isApply) {
		this.isApply = isApply;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getIsOtherSelect() {
		return isOtherSelect;
	}

	public void setIsOtherSelect(String isOtherSelect) {
		this.isOtherSelect = isOtherSelect;
	}

	public String getAlreadyApply() {
		return alreadyApply;
	}

	public void setAlreadyApply(String alreadyApply) {
		this.alreadyApply = alreadyApply;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UPlayerApply> getListApply() {
		return listApply;
	}

	public void setListApply(List<UPlayerApply> listApply) {
		this.listApply = listApply;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

}