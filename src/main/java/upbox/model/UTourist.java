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
import javax.persistence.Transient;

/**
 * 游客记录表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_tourist")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTourist implements java.io.Serializable {
	private static final long serialVersionUID = 8677645148221759091L;
	private String pikeyId;
	private UEquipment UEquipment;
	private String curType;
	private String log;
	private Date createdate;
	private String useStatus;//设置用户状态  1:用户；2：游客
	private String touristName;
	private String touristUrl;

	@Id
	@Column(name = "pikey_id", unique = true, nullable = false, length = 60)
	public String getPikeyId() {
		return pikeyId;
	}

	public void setPikeyId(String pikeyId) {
		this.pikeyId = pikeyId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "eq_id")
	public UEquipment getUEquipment() {
		return UEquipment;
	}

	public void setUEquipment(UEquipment uEquipment) {
		UEquipment = uEquipment;
	}

	@Column(name = "cur_type", length = 20)
	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	@Column(name = "log", length = 200)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "tourist_name", length = 200)
	public String getTouristName() {
		return touristName;
	}

	public void setTouristName(String touristName) {
		this.touristName = touristName;
	}

	@Column(name = "tourist_url", length = 200)
	public String getTouristUrl() {
		return touristUrl;
	}

	public void setTouristUrl(String touristUrl) {
		this.touristUrl = touristUrl;
	}

	@Transient
	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	@Override
	public String toString() {
		return "pikeyId=" + pikeyId + ", UEquipment=" + UEquipment + ", curType=" + curType + ", log="
				+ log + ", createdate=" + createdate+ ", touristName=" + touristName + ", touristUrl=" + touristUrl + ", useStatus=" + useStatus ;
	}
}