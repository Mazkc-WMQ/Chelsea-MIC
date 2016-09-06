package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 场次通用规则关联商品信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtpro_role")
public class UBrCourtproRole implements java.io.Serializable {
	private static final long serialVersionUID = -4280351093884636928L;
	private String proCourtId;
	private UBrCourtsessionRole UBrCourtsessionRole;
	private String productId;
	private String subcourtId;

	@Id
	@Column(name = "pro_court_id", unique = true, nullable = false, length = 60)
	public String getProCourtId() {
		return this.proCourtId;
	}

	public void setProCourtId(String proCourtId) {
		this.proCourtId = proCourtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "session_role_id")
	public UBrCourtsessionRole getUBrCourtsessionRole()
	{
		return UBrCourtsessionRole;
	}

	public void setUBrCourtsessionRole(UBrCourtsessionRole uBrCourtsessionRole)
	{
		UBrCourtsessionRole = uBrCourtsessionRole;
	}


	@Column(name = "product_id", length = 60)
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "subcourt_id", length = 60)
	public String getSubcourtId()
	{
		return subcourtId;
	}

	public void setSubcourtId(String subcourtId)
	{
		this.subcourtId = subcourtId;
	}
}