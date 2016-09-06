 package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 约战挑战发个图相册表
 * @author yc
 *
 * 13611929818
 */
@Entity
@Table(name = "u_duel_challenge_img")
public class UDuelChallengeImg implements java.io.Serializable {
	private static final long serialVersionUID = 6931531432200601216L;
	private String keyId;
	private String objectId;
	private String duelChallUsingType;
	private String imgurl;
	private int weight;	
	private String saveurl;	
	private String imgSaveType;
	private Date createtime;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	
	@Column(name = "object_id")
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	@Column(name = "duel_chall_using_type")
	public String getDuelChallUsingType() {
		return duelChallUsingType;
	}
	public void setDuelChallUsingType(String duelChallUsingType) {
		this.duelChallUsingType = duelChallUsingType;
	}
	
	@Column(name = "imgurl")
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	@Column(name = "weight")
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Column(name = "saveurl")
	public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}
	
	@Column(name = "img_save_type")
	public String getImgSaveType() {
		return imgSaveType;
	}
	public void setImgSaveType(String imgSaveType) {
		this.imgSaveType = imgSaveType;
	}

	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createtime", length = 20)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}