package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 场次关联商品
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtpack")
public class UBrCourtpack implements java.io.Serializable {
	private static final long serialVersionUID = -4280351093884636928L;
	private String proCourtId;
	private UBrCourtsession UBrCourtsession;
	private String productPkId;

	@Id
	@Column(name = "pro_court_id", unique = true, nullable = false, length = 60)
	public String getProCourtId() {
		return this.proCourtId;
	}

	public void setProCourtId(String proCourtId) {
		this.proCourtId = proCourtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "session_id")
	public UBrCourtsession getUBrCourtsession() {
		return this.UBrCourtsession;
	}

	public void setUBrCourtsession(UBrCourtsession UBrCourtsession) {
		this.UBrCourtsession = UBrCourtsession;
	}
	
	@Column(name = "product_pk_id", length = 60)
	public String getProductPkId()
	{
		return productPkId;
	}

	public void setProductPkId(String productPkId)
	{
		this.productPkId = productPkId;
	}
}