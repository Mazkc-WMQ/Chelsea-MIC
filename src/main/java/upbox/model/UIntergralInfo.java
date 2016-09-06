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

/**
 * 球场表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_integral_info")
public class UIntergralInfo implements java.io.Serializable {


	private static final long serialVersionUID = -8381148114272890981L;
	
	private String keyId;
	private UChallengeBs UChallengeBs;
	private UDuelBs UDuelBs;
	private UMatchBs UMatchBs;
	private Date createdate;
	private Date createtime;
	private Date modifydate;
	private Date modifytime;
	private URuleParameter URuleParameter;
	private String info;
	private UTeam uteam;
	private URuleParameterInfo URuleParameterInfo;
	private String appShow;
	
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_challenge_id")
	public UChallengeBs getUChallengeBs() {
		return UChallengeBs;
	}
	public void setUChallengeBs(UChallengeBs uChallengeBs) {
		UChallengeBs = uChallengeBs;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_duel_id")
	public UDuelBs getUDuelBs() {
		return UDuelBs;
	}
	public void setUDuelBs(UDuelBs uDuelBs) {
		UDuelBs = uDuelBs;
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
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "modifydate", length = 13)
	public Date getModifydate() {
		return modifydate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	
	@Column(name = "modifytime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getModifytime() {
		return modifytime;
	}
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_id")
	public URuleParameter getURuleParameter() {
		return URuleParameter;
	}
	public void setURuleParameter(URuleParameter uRuleParameter) {
		URuleParameter = uRuleParameter;
	}
	
	@Column(name = "info", length = 60)
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUteam() {
		return uteam;
	}
	public void setUteam(UTeam uteam) {
		this.uteam = uteam;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rule_info_id")
	public URuleParameterInfo getURuleParameterInfo() {
		return URuleParameterInfo;
	}
	public void setURuleParameterInfo(URuleParameterInfo uRuleParameterInfo) {
		URuleParameterInfo = uRuleParameterInfo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_match_id")
	public UMatchBs getUMatchBs() {
		return UMatchBs;
	}
	public void setUMatchBs(UMatchBs uMatchBs) {
		UMatchBs = uMatchBs;
	}
	
	@Column(name = "app_show")
	public String getAppShow() {
		return appShow;
	}
	public void setAppShow(String appShow) {
		this.appShow = appShow;
	}
}