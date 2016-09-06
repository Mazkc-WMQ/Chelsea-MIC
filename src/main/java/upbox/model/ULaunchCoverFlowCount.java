package upbox.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UShare entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_launchcoverflow_count")
public class ULaunchCoverFlowCount implements java.io.Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2462081331266798L;
	// Fields

	private String act;
	private String createdate;
	private String id;
	private Integer launchid;
	
	private String requestfrom;
	private Long times;
	
	
	public String getAct() {
		return act;
	}
	public String getCreatedate() {
		return createdate;
	}
	@Id
	public String getId() {
		return id;
	}
	public Integer getLaunchid() {
		return launchid;
	}
	public String getRequestfrom() {
		return requestfrom;
	}
	public Long getTimes() {
		return times;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setLaunchid(Integer launchid) {
		this.launchid = launchid;
	}
	public void setRequestfrom(String requestfrom) {
		this.requestfrom = requestfrom;
	}
	public void setTimes(Long times) {
		this.times = times;
	}
	 
	
	
	
	
	
	
	
	
	
}