package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 球员角色位置信息表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_player_rolelog")
public class UPlayerRolelog implements java.io.Serializable {

	private static final long serialVersionUID = -113129188854531650L;
	private String keyId;
	private UUser zUUser;
	private UUser bUUser;
	private UTeam UTeam;
	private String memberType;
	private Date createDate;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "zuser_id")
	public UUser getzUUser() {
		return zUUser;
	}
	public void setzUUser(UUser zUUser) {
		this.zUUser = zUUser;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "buser_id")
	public UUser getbUUser() {
		return bUUser;
	}
	public void setbUUser(UUser bUUser) {
		this.bUUser = bUUser;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return UTeam;
	}
	public void setUTeam(UTeam uTeam) {
		UTeam = uTeam;
	}
	
	@Column(name = "createtime", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "member_type", length = 20)
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	@Override
	public String toString() {
		return "UUserBehavior [keyId=" + keyId + ", zUUser=" + zUUser+""
				+ ", bUUser = " + bUUser + ", UTeam="+ UTeam + ", createDate=" + createDate + ", memberType=" + memberType + "]";
	}
	
}