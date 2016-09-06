package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 用户身价表
 * @author xiaoying
 *
 */
@Entity
@Table(name = "u_upbox_increment.u_worth")
public class UWorth implements java.io.Serializable{
	private static final long serialVersionUID = 1909364587633025192L;
	private String keyid;
	private UUser UUser;
	private Integer count;
	private Date createdate;
	private String countnum;
	
	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public UUser getUUser() {
		return UUser;
	}
	public void setUUser(UUser uUser) {
		UUser = uUser;
	}
	@Column(name="count")
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column(name="createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@Column(name="countnum")
	public String getCountnum() {
		return countnum;
	}
	public void setCountnum(String countnum) {
		this.countnum = countnum;
	}
	
	

	
	
}
