package upbox.movetest.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UShare entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_share")
public class UShare implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2462081331266798L;
	// Fields

	private String id;
	private String type;
	private String linkurl;
	private String relid;
	private String content;
	private String shortmessage;
	private Date createtime;
	private String remark;
	private String fromuserid;

	 
	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 100)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "type", length = 40)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "linkurl", length = 200)
	public String getLinkurl() {
		return this.linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	@Column(name = "relid", length = 100)
	public String getRelid() {
		return this.relid;
	}

	public void setRelid(String relid) {
		this.relid = relid;
	}

	@Column(name = "content", length = 400)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "shortmessage", length = 200)
	public String getShortmessage() {
		return this.shortmessage;
	}

	public void setShortmessage(String shortmessage) {
		this.shortmessage = shortmessage;
	}

	@Column(name = "createtime")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "fromuserid", length = 100)
	public String getFromuserid() {
		return this.fromuserid;
	}

	public void setFromuserid(String fromuserid) {
		this.fromuserid = fromuserid;
	}

}