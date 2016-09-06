package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 商品球场关系映射表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_mall_court")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UMallCourt implements java.io.Serializable {
	private static final long serialVersionUID = 5297327121078945478L;
	private String mallcourtId;
	private int courtCount;
	private String courtId;
	private UMall UMall;
	
	@Id
	@Column(name = "mallcourt_id", unique = true, nullable = false, length = 60)
	public String getMallcourtId()
	{
		return mallcourtId;
	}
	public void setMallcourtId(String mallcourtId)
	{
		this.mallcourtId = mallcourtId;
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
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mall_id")
	public UMall getUMall()
	{
		return UMall;
	}
	public void setUMall(UMall uMall)
	{
		UMall = uMall;
	}
}