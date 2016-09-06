package upbox.model;

import java.util.Date;
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
 * 参数详情信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_parameter_info")
public class UParameterInfo implements java.io.Serializable {
	private static final long serialVersionUID = -6218566459409078155L;
	private String pikeyId;
	private UParameter UParameter;
	private String name;
	private String params;
	private Date createdate;
	private Date createtime;
	private String createuser;
	private Date modifydate;
	private Date modifytime;
	private String modifyuser;
	private String remark;
	private String imgurl;
	private String isUnique;
	private String isOtherSelect;
	private String isMyselfSelect;
	private String paramsIsShow;
	private Integer rankWeight;
	@Id
	@Column(name = "pikey_id", unique = true, nullable = false, length = 60)
	public String getPikeyId() {
		return this.pikeyId;
	}

	public void setPikeyId(String pikeyId) {
		this.pikeyId = pikeyId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pkey_id")
	public UParameter getUParameter() {
		return this.UParameter;
	}

	public void setUParameter(UParameter UParameter) {
		this.UParameter = UParameter;
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

	@Column(name = "remark", length = 100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "imgurl", length = 100)
	public String getImgurl() {
		return imgurl;
	}


	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	@Column(name = "is_unique", length = 20)
	public String getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}

	@Transient
	public String getIsOtherSelect() {
		return isOtherSelect;
	}

	public void setIsOtherSelect(String isOtherSelect) {
		this.isOtherSelect = isOtherSelect;
	}

	@Transient
	public String getIsMyselfSelect() {
		return isMyselfSelect;
	}

	public void setIsMyselfSelect(String isMyselfSelect) {
		this.isMyselfSelect = isMyselfSelect;
	}

	@Column(name = "params_is_show", length = 20)
	public String getParamsIsShow() {
		return paramsIsShow;
	}

	public void setParamsIsShow(String paramsIsShow) {
		this.paramsIsShow = paramsIsShow;
	}

	@Column(name = "rank_weight", length = 20)
	public Integer getRankWeight() {
		return rankWeight;
	}

	public void setRankWeight(Integer rankWeight) {
		this.rankWeight = rankWeight;
	}

}