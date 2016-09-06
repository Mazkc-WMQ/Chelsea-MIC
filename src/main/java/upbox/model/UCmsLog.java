package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 队伍首次行为记录表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_behavior_info")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UCmsLog implements java.io.Serializable {
	private static final long serialVersionUID = 4601915095600776884L;
	private String keyId;
	private String cmsId;
	private String teamId;
	private String name;
	private String url;
	private Date createDate;

	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "team_id", length = 60)
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	@Column(name = "cms_id", length = 60)
	public String getCmsId() {
		return cmsId;
	}
	public void setCmsId(String cmsId) {
		this.cmsId = cmsId;
	}
	@Column(name = "name", length = 100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "url", length = 200)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "UCmsLog [keyId=" + keyId + ", cmsId=" + cmsId + ", teamId=" + teamId + ", name=" + name + ", url=" + url
				+ ", createDate=" + createDate + "]";
	}
	
}