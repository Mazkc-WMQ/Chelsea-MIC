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
 * 游客记录表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_inviteteam")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UInviteteam implements java.io.Serializable {
	private static final long serialVersionUID = -466875579625176847L;
	private String id;
	private UPlayer uPlayer;
	private Date createTime;
	private String invStatus;//0-待回复 1-接受 -1-拒绝 
	private UUser uUser;
	private UTeam uTeam;
	
	
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 60)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playerid")
	public UPlayer getuPlayer() {
		return uPlayer;
	}

	public void setuPlayer(UPlayer uPlayer) {
		this.uPlayer = uPlayer;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd hh:mm:ss")
	@Column(name = "createTime", length = 13)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "invstatus", length = 20)
	public String getInvStatus() {
		return invStatus;
	}

	public void setInvStatus(String invStatus) {
		this.invStatus = invStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public UUser getuUser() {
		return uUser;
	}

	public void setuUser(UUser uUser) {
		this.uUser = uUser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teamid")
	public UTeam getuTeam() {
		return uTeam;
	}

	public void setuTeam(UTeam uTeam) {
		this.uTeam = uTeam;
	}

	@Override
	public String toString() {
		return "uInviteteam [id=" + id + ", uPlayer=" + uPlayer + ", createTime=" + createTime + ", invStatus="
				+ invStatus + ", uUser=" + uUser + "]";
	}

	
}