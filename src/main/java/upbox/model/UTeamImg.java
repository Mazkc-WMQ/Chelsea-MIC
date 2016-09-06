package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 球队图片表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_team_img")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UTeamImg implements java.io.Serializable {
	private static final long serialVersionUID = 3750842960725090229L;
	private String teamImgId;
	private UTeam UTeam;
	private String imgSizeType;
	private String timgUsingType;
	private String imgurl;
	private Integer imgWeight;
	private String saveurl;
	public UUser UUser;

	@Id
	@Column(name = "team_img_id", unique = true, nullable = false, length = 60)
	public String getTeamImgId() {
		return this.teamImgId;
	}

	public void setTeamImgId(String teamImgId) {
		this.teamImgId = teamImgId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	public UTeam getUTeam() {
		return this.UTeam;
	}

	public void setUTeam(UTeam UTeam) {
		this.UTeam = UTeam;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser() {
		return UUser;
	}

	public void setUUser(UUser uUser) {
		UUser = uUser;
	}

	@Column(name = "img_size_type", length = 20)
	public String getImgSizeType() {
		return this.imgSizeType;
	}

	public void setImgSizeType(String imgSizeType) {
		this.imgSizeType = imgSizeType;
	}

	@Column(name = "timg_using_type", length = 20)
	public String getTimgUsingType()
	{
		return timgUsingType;
	}

	public void setTimgUsingType(String timgUsingType)
	{
		this.timgUsingType = timgUsingType;
	}

	@Column(name = "imgurl")
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
	@Column(name = "saveurl")
	public String getSaveurl() {
		return saveurl;
	}
	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}
	
	public UTeamImg() {
		// TODO Auto-generated constructor stub
	}

	public UTeamImg(String teamImgId, UTeam uTeam, String imgSizeType, String timgUsingType, String imgurl,
			Integer imgWeight) {
		super();
		this.teamImgId = teamImgId;
		UTeam = uTeam;
		this.imgSizeType = imgSizeType;
		this.timgUsingType = timgUsingType;
		this.imgurl = imgurl;
		this.imgWeight = imgWeight;
	}
	

}