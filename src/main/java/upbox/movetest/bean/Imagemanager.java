package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Imagemanager entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "imagemanager")
public class Imagemanager implements java.io.Serializable {
	private static final long serialVersionUID = -3010452325247958711L;
	private Integer id;
	private String imageurl;
	private String imagepath;
	private String imagename;
	private String name;
	private String imagesize;
	private String type;
	private Date createtime;
	private Date updatetime;

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@Column(name = "imageurl")
	public String getImageurl() {
		return this.imageurl;
	}
	
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	@Column(name = "imagepath")
	public String getImagepath() {
		return this.imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	@Column(name = "imagename", length = 250)
	public String getImagename() {
		return this.imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	@Column(name = "name", length = 500)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "imagesize", length = 20)
	public String getImagesize() {
		return this.imagesize;
	}

	public void setImagesize(String imagesize) {
		this.imagesize = imagesize;
	}

	@Column(name = "type", length = 10)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "createtime", length = 29)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "updatetime", length = 29)
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}