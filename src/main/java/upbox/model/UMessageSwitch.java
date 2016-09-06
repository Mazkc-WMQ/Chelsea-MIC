package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 前端消息记录表
 * @author charlescai
 *
 * 13120501633
 */
@Entity
@Table(name = "u_message_switch")
public class UMessageSwitch implements java.io.Serializable {
	private static final long serialVersionUID = -6589993382923479263L;
	private String keyId;
	private Date createtime;
	private String switchStauts;
	private String userId;
	private String type;
//	private UMessageSwitchinfo UMessageSwitchinfo;

	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "createtime", length = 15)  
	@JSON(format="yyyy年MM月dd日 HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "user_id", length = 200)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "type", length = 30)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "switch_status", length = 30)
	public String getSwitchStauts() {
		return switchStauts;
	}

	public void setSwitchStauts(String switchStauts) {
		this.switchStauts = switchStauts;
	}
	
//	@NotFound(action=NotFoundAction.IGNORE)
//	@OneToMany(fetch = FetchType.EAGER)
//	@JoinColumn(name = "switchinfo_id")
//	public UMessageSwitchinfo getUMessageSwitchinfo() {
//		return UMessageSwitchinfo;
//	}
//
//	public void setUMessageSwitchinfo(UMessageSwitchinfo uMessageSwitchinfo) {
//		UMessageSwitchinfo = uMessageSwitchinfo;
//	}

}