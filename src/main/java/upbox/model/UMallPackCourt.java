package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 商品包球场关系映射表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_mall_package upbox._court")
public class UMallPackCourt implements java.io.Serializable {
	private static final long serialVersionUID = 5297327121078945478L;
	private String mallpackCourtId;
	private int courtCount;
	private String courtId;
	private UMallPackage UMallPackage;
	
	@Id
	@Column(name = "mallpack_court_id", unique = true, nullable = false, length = 60)
	public String getMallpackCourtId()
	{
		return mallpackCourtId;
	}
	public void setMallpackCourtId(String mallpackCourtId)
	{
		this.mallpackCourtId = mallpackCourtId;
	}
	
	@Column(name = "court_count")
	public int getCourtCount()
	{
		return courtCount;
	}
	public void setCourtCount(int courtCount)
	{
		this.courtCount = courtCount;
	}
	@Column(name = "court_id")
	public String getCourtId()
	{
		return courtId;
	}
	public void setCourtId(String courtId)
	{
		this.courtId = courtId;
	}
	
	/**
	 * @return the uMallPackage
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mall_pack_id")
	public UMallPackage getUMallPackage() {
		return UMallPackage;
	}
	/**
	 * @param uMallPackage the uMallPackage to set
	 */
	public void setUMallPackage(UMallPackage uMallPackage) {
		UMallPackage = uMallPackage;
	}
}