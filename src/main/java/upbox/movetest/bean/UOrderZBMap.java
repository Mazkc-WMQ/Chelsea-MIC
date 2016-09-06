package upbox.movetest.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author kevin
 *
 */
@Entity
@Table(name = "u_orderzbmap")
public class UOrderZBMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6222810746255888684L;
	/*
	 * CREATE TABLE "u_orderzbmap" (
	"id" int4 NOT NULL DEFAULT nextval('u_orderzbmap_id_seq'::regclass),
	"ordernum" varchar(60),
	"cpid" int4,
	"zbid" int4,
	"count" int4
)
	 * 
	 * */

	private Integer id;
	private String ordernum;
	private Integer cpid;
	private Integer zbid;
	private Integer count;
	
	
	
	

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public Integer getCpid() {
		return cpid;
	}
	public void setCpid(Integer cpid) {
		this.cpid = cpid;
	}
	public Integer getZbid() {
		return zbid;
	}
	public void setZbid(Integer zbid) {
		this.zbid = zbid;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
	
	
}
