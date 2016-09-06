package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 下属球场运营态信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_br_courtoper")
public class UBrCourtoper implements java.io.Serializable {
	private static final long serialVersionUID = -2446739531284726352L;
	private String operCourtId;
	private UBrCourt UBrCourt;
	private String title;
	private String jumpurl;
	private String imgurl;
	private Integer imgWeight;
	private Date date;
	private String saveurl;

	@Id
	@Column(name = "oper_court_id", unique = true, nullable = false, length = 60)
	public String getOperCourtId() {
		return this.operCourtId;
	}

	public void setOperCourtId(String operCourtId) {
		this.operCourtId = operCourtId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcourt_id")
	public UBrCourt getUBrCourt() {
		return this.UBrCourt;
	}

	public void setUBrCourt(UBrCourt UBrCourt) {
		this.UBrCourt = UBrCourt;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "jumpurl", length = 200)
	public String getJumpurl() {
		return this.jumpurl;
	}

	public void setJumpurl(String jumpurl) {
		this.jumpurl = jumpurl;
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

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "date", length = 13)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSaveurl()
	{
		return saveurl;
	}

	public void setSaveurl(String saveurl)
	{
		this.saveurl = saveurl;
	}	
}