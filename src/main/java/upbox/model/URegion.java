package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 国家行政区域
 * @author yuancao
 *
 */
@Entity
@Table(name = "u_region")
public class URegion implements java.io.Serializable {

	private static final long serialVersionUID = -1875366126224160825L;
	
	private String _id;
	private String name;
	private String type;	
	private String parent;
	private String code;
	private Integer rank;
	
	@Id
	@Column(name = "_id", unique = true, nullable = false, length = 20)
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	@Column(name = "name", length = 60)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "type", length = 20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "parent", length = 20)
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Transient
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "rank", length = 11)
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	

	
}