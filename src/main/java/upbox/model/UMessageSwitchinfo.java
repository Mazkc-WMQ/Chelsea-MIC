package upbox.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前端消息记录表
 * @author charlescai
 *
 * 13120501633
 */
@Entity
@Table(name = "u_message_switchinfo")
public class UMessageSwitchinfo implements java.io.Serializable {
	private static final long serialVersionUID = -6589993382923479263L;
	private String keyId;
	private String type;
	private String mesType;

	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "mes_type", length = 30)
	public String getMesType() {
		return mesType;
	}

	public void setMesType(String mesType) {
		this.mesType = mesType;
	}

	@Column(length = 30)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}