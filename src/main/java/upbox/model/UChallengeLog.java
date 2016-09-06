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
 * 挑战日志表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_challenge_log")
public class UChallengeLog implements java.io.Serializable {

	private static final long serialVersionUID = -8870009943923086716L;
	
	private String keyId;
	//private UChallengeBs UChallengeBs;
	private Date date;
	private Date time;
	private UBUser UBUser;
	private String loginfo;
	
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	/*@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bs_id")
	public UChallengeBs getUChallengeBs() {
		return UChallengeBs;
	}
	public void setUChallengeBs(UChallengeBs uChallengeBs) {
		UChallengeBs = uChallengeBs;
	}*/
	
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "date", length = 13)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name = "time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UBUser getUBUser() {
		return UBUser;
	}
	public void setUBUser(UBUser uBUser) {
		UBUser = uBUser;
	}
	
	@Column(name = "loginfo", length = 200)
	public String getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}


	

	



	
	


	
	
	
}