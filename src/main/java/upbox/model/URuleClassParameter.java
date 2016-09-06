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
 * 规则大类分支表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_rule_class_params")
public class URuleClassParameter implements java.io.Serializable {

	private static final long serialVersionUID = -8633246471636209811L;
	
	private String ruleClassId;	
	private String name;
	private Date createdate;
	private Date createtime;
	private UBUser createuser;
	private URuleTopParameter URuleTopParameter;
	private Set<URuleParameter> URuleParameters = new HashSet<URuleParameter>(0);
	
	@Id
	@Column(name = "rule_class_id", unique = true, nullable = false, length = 60)
	public String getRuleClassId() {
		return ruleClassId;
	}
	public void setRuleClassId(String ruleClassId) {
		this.ruleClassId = ruleClassId;
	}
	
	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getCreateuser() {
		return createuser;
	}
	public void setCreateuser(UBUser createuser) {
		this.createuser = createuser;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_top_id")
	public URuleTopParameter getURuleTopParameter() {
		return URuleTopParameter;
	}
	public void setURuleTopParameter(URuleTopParameter uRuleTopParameter) {
		URuleTopParameter = uRuleTopParameter;
	}
	
	
	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "URuleClassParameter")
	//@OrderBy(clause = "rule_weight")
	public Set<URuleParameter> getURuleParameters() {
		return URuleParameters;
	}
	public void setURuleParameters(Set<URuleParameter> uRuleParameters) {
		URuleParameters = uRuleParameters;
	}


	
	
	
	

	


	
	
	
}