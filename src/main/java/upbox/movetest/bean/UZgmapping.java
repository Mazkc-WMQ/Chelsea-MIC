package upbox.movetest.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UZgmapping entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_zgmapping")
public class UZgmapping implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4908596724005975817L;
	// Fields

	private Integer id;
	private Integer zgid;
	private String zgnum;
	private Integer count;

	// Constructors

	

	// Property accessors
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "zgid")
	public Integer getZgid() {
		return this.zgid;
	}

	public void setZgid(Integer zgid) {
		this.zgid = zgid;
	}

	@Column(name = "zgnum", length = 100)
	public String getZgnum() {
		return this.zgnum;
	}

	public void setZgnum(String zgnum) {
		this.zgnum = zgnum;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}