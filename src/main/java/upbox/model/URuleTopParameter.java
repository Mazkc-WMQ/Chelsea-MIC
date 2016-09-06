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
 * 规则顶级表 所有规则总命名
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_rule_top_params")
public class URuleTopParameter implements java.io.Serializable {

	private static final long serialVersionUID = -156091301500833698L;
	
	private String ruleTopId;
	private String name;
	private Date createdate;
	private Date createtime;
	private UBUser createuser;
	private String isUse;
	
	private Set<URuleClassParameter> URuleClassParameters = new HashSet<URuleClassParameter>(0);
	
	@Id
	@Column(name = "rule_top_id", unique = true, nullable = false, length = 60)
	public String getRuleTopId() {
		return ruleTopId;
	}
	public void setRuleTopId(String ruleTopId) {
		this.ruleTopId = ruleTopId;
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
	
	@Column(name = "is_use", length = 20)
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "URuleTopParameter")
	public Set<URuleClassParameter> getURuleClassParameters() {
		return URuleClassParameters;
	}
	public void setURuleClassParameters(Set<URuleClassParameter> uRuleClassParameters) {
		URuleClassParameters = uRuleClassParameters;
	}

	
	
	

	


	
	
	
}