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
 * 擂主表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_champion")
public class UChampion implements java.io.Serializable {
	
	private static final long serialVersionUID = 1842592111565384808L;
	
	private String keyId;
	private UTeam UTeam;
	private Date createDate;
	private UBrCourt UBrCourt;
	private String isChampion;
	
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "br_court_id")
	public UBrCourt getUBrCourt() {
		return UBrCourt;
	}
	

	public void setUBrCourt(UBrCourt uBrCourt) {
		UBrCourt = uBrCourt;
	}
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	@Column(name = "is_champion", length = 60)
	public String getIsChampion() {
		return isChampion;
	}
	public void setIsChampion(String isChampion) {
		this.isChampion = isChampion;
	}

	
	
	
	
	


	
	
	
}