package upbox.movetest.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * USocial entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_social")
public class USocial implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1175717243285453548L;
	private Integer id;
	private Integer gzuser;
	private Date createtime;
	private Date updatetime;
	private Integer bgzuser;
	private Integer gzstatus;
	private Integer type;

	// Constructors
	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "gzuser")
	public Integer getGzuser() {
		return this.gzuser;
	}

	public void setGzuser(Integer gzuser) {
		this.gzuser = gzuser;
	}

	@Column(name = "createtime",updatable=false)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "updatetime")
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "bgzuser")
	public Integer getBgzuser() {
		return this.bgzuser;
	}

	public void setBgzuser(Integer bgzuser) {
		this.bgzuser = bgzuser;
	}

	@Column(name = "gzstatus")
	public Integer getGzstatus() {
		return this.gzstatus;
	}

	public void setGzstatus(Integer gzstatus) {
		this.gzstatus = gzstatus;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}