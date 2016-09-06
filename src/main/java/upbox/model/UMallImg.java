package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 商品图片信息
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name="u_mall_img")
public class UMallImg  implements java.io.Serializable {
	private static final long serialVersionUID = -3646391264638004340L;
	private String proImgId;
    private String similarId;
    private UMallPackage UMallPackage;
    private String imgSizeType;
    private String mimgUsingType;
    private String imgurl;
    private Integer weight;
    private String saveurl;


    @Id 
    @Column(name="pro_img_id", unique=true, nullable=false, length=60)
    public String getProImgId() {
        return this.proImgId;
    }
    
    public void setProImgId(String proImgId) {
        this.proImgId = proImgId;
    }
    @Column(name="similar_id")
	public String getSimilarId() {
		return similarId;
	}
	public void setSimilarId(String similarId) {
		this.similarId = similarId;
	}
    
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="pro_pack_id")
    public UMallPackage getUMallPackage() {
        return this.UMallPackage;
    }
    
    public void setUMallPackage(UMallPackage UMallPackage) {
        this.UMallPackage = UMallPackage;
    }
    
    @Column(name="img_size_typwe", length=20)
    public String getImgSizeType() {
        return this.imgSizeType;
    }
    
    public void setImgSizeType(String imgSizeType) {
        this.imgSizeType = imgSizeType;
    }
    
    @Column(name="mimg_using_type", length=20)
    public String getMimgUsingType()
	{
		return mimgUsingType;
	}

	public void setMimgUsingType(String mimgUsingType)
	{
		this.mimgUsingType = mimgUsingType;
	}
    
    @Column(name="imgurl")
    public String getImgurl() {
        return this.imgurl;
    }

	public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    
    @Column(name="weight")
    public Integer getWeight() {
        return this.weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Column(name="saveurl")
    public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl)
	{
		this.saveurl = saveurl;
	}
}