 package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;

import java.util.HashSet;
import java.util.Set;
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
 * 小规则表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_rule_params")
public class URuleParameter implements java.io.Serializable {
	
	private static final long serialVersionUID = -5198925789601957642L;
	
	private String ruleId;
	private String name;
	private String appName;
	private String params;
	private Date createdate;
	private Date createtime;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private String isRule;
	private UBUser UBUser;
	private String isAddnum;
	private String ruleWeight;
	private String ruleShow;
	private String selStr;
	private URuleClassParameter URuleClassParameter;
	private Set<URuleParameterInfo> URuleParameterInfos = new HashSet<URuleParameterInfo>(0);
	
	
	
	@Id
	@Column(name = "rule_id", unique = true, nullable = false, length = 60)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "params", length = 150)
	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
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

	

	@Column(name = "is_rule", length = 30)
	public String getIsRule() {
		return isRule;
	}

	public void setIsRule(String isRule) {
		this.isRule = isRule;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUBUser()
	{
		return UBUser;
	}

	public void setUBUser(UBUser uBUser)
	{
		UBUser = uBUser;
	}

	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "URuleParameter")
	//@OrderBy(clause = "rule_info_weight")
	public Set<URuleParameterInfo> getURuleParameterInfos() {
		return URuleParameterInfos;
	}

	public void setURuleParameterInfos(Set<URuleParameterInfo> uRuleParameterInfos) {
		URuleParameterInfos = uRuleParameterInfos;
	}

	@Column(name = "is_addnum", length = 30)
	public String getIsAddnum() {
		return isAddnum;
	}

	public void setIsAddnum(String isAddnum) {
		this.isAddnum = isAddnum;
	}

	@Column(name = "rule_weight", length = 30)
	public String getRuleWeight() {
		return ruleWeight;
	}

	public void setRuleWeight(String ruleWeight) {
		this.ruleWeight = ruleWeight;
	}
	
	@Column(name = "rule_show", length = 30)
	public String getRuleShow() {
		return ruleShow;
	}

	public void setRuleShow(String ruleShow) {
		this.ruleShow = ruleShow;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_class_id")
	public URuleClassParameter getURuleClassParameter() {
		return URuleClassParameter;
	}

	public void setURuleClassParameter(URuleClassParameter uRuleClassParameter) {
		URuleClassParameter = uRuleClassParameter;
	}

	@Transient
	public String getSelStr() {
		return selStr;
	}

	public void setSelStr(String selStr) {
		this.selStr = selStr;
	}

	@Column(name = "app_name", length = 60)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	
	
	
	
}