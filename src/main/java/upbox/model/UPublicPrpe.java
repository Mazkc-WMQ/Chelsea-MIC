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
@Table(name = "u_publicprpe")
public class UPublicPrpe implements java.io.Serializable {
	private static final long serialVersionUID = -7793104373805170204L;
	private String publicId;
	private String name;
	private String value;
	private String remark;

	@Id
	@Column(name = "pulic_id", unique = true, nullable = false, length = 60)
	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
	@Column(name = "name", length = 200)  
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "value", length = 200)  
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "remark", length = 200)  
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}