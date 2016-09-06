package upbox.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 筛选可配置 小类
 * @author xiao
 *
 */
@Entity
@Table(name = "u_configuration_info")
public class UConfigurationInfo {
	 private String pkey;
	 private String name;
	 private String params;
	 private UConfiguration UConfiguration;
	 private String parentd;
	 private String imgurl;
	 private int smSort;
	 private int configSort;
	 private int lvl;
	 private String showType;
	 private String didTag;
	 private String tagType;
	 private String configInfoStatus;
	 private List<UConfigurationInfo> ListConfigurationInfo;
	@Id
	@Column(name = "pkey", unique = true, nullable = false, length = 60)
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	@Column(name = "name",length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "params",length=60)
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "config_id")
	public UConfiguration getUConfiguration() {
		return UConfiguration;
	}
	public void setUConfiguration(UConfiguration uConfiguration) {
		UConfiguration = uConfiguration;
	}
	@Column(name = "parent_id",length=60)
	public String getParentd() {
		return parentd;
	}
	public void setParentd(String parentd) {
		this.parentd = parentd;
	}
	@Column(name = "imgurl",length=300)
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	@Column(name = "sm_sort",length=4)
	public int getSmSort() {
		return smSort;
	}
	public void setSmSort(int smSort) {
		this.smSort = smSort;
	}
	@Column(name = "config_sort",length=4)
	public int getConfigSort() {
		return configSort;
	}
	public void setConfigSort(int configSort) {
		this.configSort = configSort;
	}
	@Column(name = "lvl",length=4)
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	@Column(name = "show_type",length=20)
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	@Column(name = "did_tag",length=20)
	public String getDidTag() {
		return didTag;
	}
	public void setDidTag(String didTag) {
		this.didTag = didTag;
	}
	@Column(name = "tag_type",length=20)
	public String getTagType() {
		return tagType;
	}
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	@Transient
	public List<UConfigurationInfo> getListConfigurationInfo() {
		return ListConfigurationInfo;
	}
	public void setListConfigurationInfo(List<UConfigurationInfo> listConfigurationInfo) {
		ListConfigurationInfo = listConfigurationInfo;
	}
	@Column(name="config_info_status")
	public String getConfigInfoStatus() {
		return configInfoStatus;
	}
	public void setConfigInfoStatus(String configInfoStatus) {
		this.configInfoStatus = configInfoStatus;
	}
	

	 
	 
}
