package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 下属球场图片信息bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtimage")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UBrCourtimage implements java.io.Serializable {
	private static final long serialVersionUID = -9176243209894309277L;
	private String imageCourtId;
	private UBrCourt UBrCourt;
	private UCourt UCourt;
	private String imgSizeType;
	private String cimgUsingType;
	private String imgurl;
	private Integer imgWeight;
	private String saveurl;

	@Id	
	@Column(name = "image_court_id", unique = true, nullable = false, length = 60)
	public String getImageCourtId() {
		return this.imageCourtId;
	}

	public void setImageCourtId(String imageCourtId) {
		this.imageCourtId = imageCourtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getUBrCourt() {
		return this.UBrCourt;
	}

	public void setUBrCourt(UBrCourt UBrCourt) {
		this.UBrCourt = UBrCourt;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "court_id")
	public UCourt getUCourt() {
		return this.UCourt;
	}

	public void setUCourt(UCourt UCourt) {
		this.UCourt = UCourt;
	}

	@Column(name = "img_size_type", length = 20)
	public String getImgSizeType() {
		return this.imgSizeType;
	}

	public void setImgSizeType(String imgSizeType) {
		this.imgSizeType = imgSizeType;
	}

	@Column(name = "cimg_using_type", length = 20)
	public String getCimgUsingType()
	{
		return cimgUsingType;
	}

	public void setCimgUsingType(String cimgUsingType)
	{
		this.cimgUsingType = cimgUsingType;
	}
	
	@Column(name = "imgurl", length = 200)
	public String getImgurl() {
		return this.imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	@Column(name = "img_weight")
	public Integer getImgWeight() {
		return this.imgWeight;
	}

	public void setImgWeight(Integer imgWeight) {
		this.imgWeight = imgWeight;
	}

	@Column(name = "saveurl", length = 200)
	public String getSaveurl()
	{
		return saveurl;
	}

	public void setSaveurl(String saveurl)
	{
		this.saveurl = saveurl;
	}
}