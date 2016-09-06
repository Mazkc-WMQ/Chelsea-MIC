package upbox.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 筛选可配置 大类
 * 
 * @author xiao
 *
 */
@Entity
@Table(name = "u_configuration")
public class UConfiguration {
	private String pkeyId;
	private String params;
	private Date createdate;
	private Date modifydate;
	private String createuser;
	private String modifyuser;
	private String configType;
	private String imgurl;
	private String configStatus;
	private List<HashMap<String, Object>> listInfo;
	@Id
	@Column(name = "pkey_id", unique = true, nullable = false, length = 60)
	public String getPkeyId() {
		return pkeyId;
	}

	public void setPkeyId(String pkeyId) {
		this.pkeyId = pkeyId;
	}
	@Column(name = "params", length = 60)
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	@Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@Column(name = "modifydate")
	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	@Column(name = "createuser", length = 60)
	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	@Column(name = "modifyuser", length = 60)
	public String getModifyuser() {
		return modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}
	@Column(name = "config_type", length = 60)
	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}
	@Column(name = "imgurl", length = 60)
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	@Transient
	public List<HashMap<String, Object>> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<HashMap<String, Object>> listInfo) {
		this.listInfo = listInfo;
	}
	@Column(name="config_status")
	public String getConfigStatus() {
		return configStatus;
	}

	public void setConfigStatus(String configStatus) {
		this.configStatus = configStatus;
	}

}
