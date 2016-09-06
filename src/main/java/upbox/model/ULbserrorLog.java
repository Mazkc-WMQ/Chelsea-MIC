package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 百度LBS对照表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_upbox_log.u_lbserror_log")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class ULbserrorLog implements java.io.Serializable {
	private static final long serialVersionUID = -2026887026175161599L;
	private String keyId;
	private String objectId;
	private String lbsType;
	private Date createtime;
	private String lbsTable;
	private String lbsData;
	private String errorMsg;
	

	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@Column(name = "object_id", length = 60)
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	@Column(name = "lbs_type", length = 20)
	public String getLbsType() {
		return lbsType;
	}
	public void setLbsType(String lbsType) {
		this.lbsType = lbsType;
	}
	
	@Column(name = "createtime", length = 20)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "lbs_table", length = 60)
	public String getLbsTable() {
		return lbsTable;
	}
	public void setLbsTable(String lbsTable) {
		this.lbsTable = lbsTable;
	}

	@Column(name = "lbs_data", length = 60)
	public String getLbsData() {
		return lbsData;
	}
	public void setLbsData(String lbsData) {
		this.lbsData = lbsData;
	}
	
	@Column(name = "error_msg", length = 60)
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Override
	public String toString() {
		return "UBaidulbs [keyId=" + keyId + ", objectId=" + objectId + ", lbsType=" + lbsType + ", createtime="
				+ createtime + ", lbsTable=" + lbsTable + ", lbsData=" + lbsData + "]";
	}

}