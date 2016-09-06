package upbox.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * UParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_parameter")
public class UParameter implements java.io.Serializable {
	private static final long serialVersionUID = 7423882179815757560L;
	private String pkeyId;
	private String name;
	private String params;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private Set<UParameterInfo> UParameterInfos = new HashSet<UParameterInfo>(0);

	@Id
	@Column(name = "pkey_id", unique = true, nullable = false, length = 60)
	public String getPkeyId() {
		return this.pkeyId;
	}

	public void setPkeyId(String pkeyId) {
		this.pkeyId = pkeyId;
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

	@Transient//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "UParameter")
	public Set<UParameterInfo> getUParameterInfos()
	{
		return UParameterInfos;
	}

	public void setUParameterInfos(Set<UParameterInfo> uParameterInfos)
	{
		UParameterInfos = uParameterInfos;
	}
}