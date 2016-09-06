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
@Table(name = "u_br_courtpro")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UBrCourtpro implements java.io.Serializable {
	private static final long serialVersionUID = -4280351093884636928L;
	private String proCourtId;
	private UBrCourtsession UBrCourtsession;
	private String productType;
	private String productId;
	private String productPriceType;

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

	@Column(name = "product_type", length = 20)
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "product_id", length = 60)
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "product_price_type", length = 20)
	public String getProductPriceType() {
		return this.productPriceType;
	}

	public void setProductPriceType(String productPriceType) {
		this.productPriceType = productPriceType;
	}

}