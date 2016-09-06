package upbox.movetest.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 球员BEAN
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_player")
public class UPlayer implements java.io.Serializable {
	private static final long serialVersionUID = -8936824218012691445L;
	private Integer id;
	private Integer isstatus;
	private String height;
	private String weight;
	private String seat;
	private Integer number;
	private String openid;
	private String gseat;
	private Integer userid;



	@Column(name = "isstatus")
	public Integer getIsstatus() {
		return this.isstatus;
	}

	public void setIsstatus(Integer isstatus) {
		this.isstatus = isstatus;
	}

	@Column(name = "height", length = 10)
	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "weight", length = 10)
	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@Column(name = "seat", length = 20)
	public String getSeat() {
		return this.seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	@Column(name = "number")
	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "gseat", length = 20)
	public String getGseat() {
		return this.gseat;
	}

	public void setGseat(String gseat) {
		this.gseat = gseat;
	}

	@Column(name = "userid")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}