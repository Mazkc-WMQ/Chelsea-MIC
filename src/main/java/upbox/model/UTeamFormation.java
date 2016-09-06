 package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 球队阵型表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_team_formation")
public class UTeamFormation implements java.io.Serializable {
	
	private static final long serialVersionUID = -5169758165967634503L;
	
	
	
	private String keyId;
	private UTeam UTeam;	
	private String remark;	
	private Date createdate;
	private String userid;
	private String formation;
	private String imgurl;
	private Integer formationNum;
	private String delStatus;
	private String isDefault;
	private String initStatus;

	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	
	@Column(name = "remark", length = 150)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Column(name = "userid", length = 60)
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Column(name = "formation", length = 30)
	public String getFormation() {
		return formation;
	}
	public void setFormation(String formation) {
		this.formation = formation;
	}
	
	@Column(name = "imgurl", length = 250)
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	@Column(name = "del_status", length = 20)
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}
	
	
	@Column(name = "formation_num", length = 20)
	public Integer getFormationNum() {
		return formationNum;
	}
	public void setFormationNum(Integer formationNum) {
		this.formationNum = formationNum;
	}
	
	@Column(name = "is_default", length = 20)
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@Column(name = "init_status", length = 20)
	public String getInitStatus() {
		return initStatus;
	}
	public void setInitStatus(String initStatus) {
		this.initStatus = initStatus;
	}
	
	
	

	
	
}