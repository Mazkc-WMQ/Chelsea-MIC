package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 球员角色位置信息表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_player_role")
public class UPlayerRole implements java.io.Serializable {

	private static final long serialVersionUID = -113129188854531650L;
	private String keyId;
	private UPlayer UPlayer;
	private String memberType;
	private Date changeDate;
	private String memberTypeUseStatus;
	private String oldMemberTypes;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "player_id")
	public UPlayer getUPlayer() {
		return UPlayer;
	}
	public void setUPlayer(UPlayer uPlayer) {
		UPlayer = uPlayer;
	}
	@Column(name = "member_type", length = 20)
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	@Column(name = "change_date", length = 20)
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	@Column(name = "member_type_use_status", length = 20)
	public String getMemberTypeUseStatus() {
		return memberTypeUseStatus;
	}
	public void setMemberTypeUseStatus(String memberTypeUseStatus) {
		this.memberTypeUseStatus = memberTypeUseStatus;
	}
	@Override
	public String toString() {
		return "UPlayerRole [keyId=" + keyId + ", UPlayer=" + UPlayer + ", memberType=" + memberType + ", changeDate="
				+ changeDate + ", memberTypeUseStatus=" + memberTypeUseStatus + "]";
	}
	@Transient
	public String getOldMemberTypes() {
		return oldMemberTypes;
	}
	public void setOldMemberTypes(String oldMemberTypes) {
		this.oldMemberTypes = oldMemberTypes;
	}
	
}