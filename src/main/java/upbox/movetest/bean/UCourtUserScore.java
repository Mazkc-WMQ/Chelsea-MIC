package upbox.movetest.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "u_courtuserscore")
@DynamicUpdate(true)
public class UCourtUserScore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2232193303855035927L;
	
	private Integer id;
	private Integer userid;
	private Integer courtid;
	private float  score;
	
	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getCourtid() {
		return courtid;
	}
	public void setCourtid(Integer courtid) {
		this.courtid = courtid;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	
	

}
