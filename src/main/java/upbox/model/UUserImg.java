package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 前端用户图片信息bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_user_img")
public class UUserImg implements java.io.Serializable {
	private static final long serialVersionUID = 7966335903658415139L;
	private String pkId;
	private UUser UUser;
	private String imgSizeType;
	private String uimgUsingType;
	private String imgurl;
	private Integer weight;
	private String saveurl;
	
	@Id
	@Column(name = "pk_id", unique = true, nullable = false, length = 60)
	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser()
	{
		return UUser;
	}

	public void setUUser(UUser uUser)
	{
		UUser = uUser;
	}

	@Column(name = "img_size_type", length = 20)
	public String getImgSizeType() {
		return this.imgSizeType;
	}

	public void setImgSizeType(String imgSizeType) {
		this.imgSizeType = imgSizeType;
	}

	@Column(name = "uimg_using_type", length = 20)
	public String getUimgUsingType()
	{
		return uimgUsingType;
	}

	public void setUimgUsingType(String uimgUsingType)
	{
		this.uimgUsingType = uimgUsingType;
	}
	
	@Column(name = "imgurl")
	public String getImgurl() {
		return this.imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	@Column(name = "weight")
	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@Column(name = "saveurl")
	public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}

}