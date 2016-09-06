package upbox.model;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 小规则下拉选表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_rule_params_info")
public class URuleParameterInfo implements java.io.Serializable {

	
	private static final long serialVersionUID = -2819845756219044497L;
	
	private String ruleInfoId;
	private URuleParameter URuleParameter;
	private String name;
	private String appName;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private String rulenums;
	private String ruleInfoWeight;
	private List<URuleParameterInfo> uRuleParameterInfoList;
	@Id
	@Column(name = "rule_info_id", unique = true, nullable = false, length = 60)
	public String getRuleInfoId() {
		return ruleInfoId;
	}

	public void setRuleInfoId(String ruleInfoId) {
		this.ruleInfoId = ruleInfoId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_id")
	public URuleParameter getURuleParameter() {
		return URuleParameter;
	}

	public void setURuleParameter(URuleParameter uRuleParameter) {
		URuleParameter = uRuleParameter;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "createuser", length = 60)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "modifydate", length = 13)
	public Date getModifydate() {
		return this.modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	@Column(name = "modifytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getModifytime() {
		return this.modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	@Column(name = "modifyuser", length = 60)
	public String getModifyuser() {
		return this.modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}



	@Column(name = "rulenums")
	public String getRulenums() {
		return rulenums;
	}

	public void setRulenums(String rulenums) {
		this.rulenums = rulenums;
	}

	@Column(name = "rule_info_weight")
	public String getRuleInfoWeight() {
		return ruleInfoWeight;
	}

	public void setRuleInfoWeight(String ruleInfoWeight) {
		this.ruleInfoWeight = ruleInfoWeight;
	}


	@Column(name = "app_name")
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	@Transient
	public List<URuleParameterInfo> getuRuleParameterInfoList() {
		return uRuleParameterInfoList;
	}

	public void setuRuleParameterInfoList(List<URuleParameterInfo> uRuleParameterInfoList) {
		this.uRuleParameterInfoList = uRuleParameterInfoList;
	}
	
	
}