package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 约战操作记录
 * @author xiao
 */
@Entity
@Table(name = "u_duel_log")
public class UDuelLog {
	private String keyId;
	private String duelId;
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
	
	@Column(name = "duel_id")
	public String getDuelId() {
		return duelId;
	}
	public void setDuelId(String duelId) {
		this.duelId = duelId;
	}
	@Column(name = "date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name = "time")
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
	@Column(name = "loginfo")
	public String getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	
}
