package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前端用户图片信息bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_equipment")
public class UEquipment implements java.io.Serializable {
	private static final long serialVersionUID = 7966335903658415139L;
	private String keyId;
	private String code;
	private String cpu;
	private String system;
	private String ip;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "code", length = 150)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "cpu", length = 50)
	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	@Column(name = "system", length = 50)
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	@Column(name = "ip", length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}