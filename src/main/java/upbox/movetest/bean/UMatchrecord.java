package upbox.movetest.bean;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UMatchrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "u_matchrecord")
public class UMatchrecord implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4661674510600636907L;
	private Integer id;
	private Integer useridhome;
	private Integer useridvisiting;
	private Integer teamidhome;
	private Integer teamidvisiting;
	private String teamnamehome;
	private String teamnamevisiting;
	private Integer teamscorehome;
	private Integer teamscorevisiting;
	private Integer type;
	private String ordernum;
	private Integer courtid;
	private String courtname;
	private String courtaddress;
	private Date matchdate;
	private String matchtime;
	private Integer status;
	private Date createtime;
	private Integer createuserid;
	private String others;
	private Integer cid;
	private Float cscores;
	private String cname;
	private Integer homescore;
	private Integer visitscore;

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

	@Column(name = "useridhome")
	public Integer getUseridhome() {
		return this.useridhome;
	}

	public void setUseridhome(Integer useridhome) {
		this.useridhome = useridhome;
	}

	@Column(name = "useridvisiting")
	public Integer getUseridvisiting() {
		return this.useridvisiting;
	}

	public void setUseridvisiting(Integer useridvisiting) {
		this.useridvisiting = useridvisiting;
	}

	@Column(name = "teamidhome")
	public Integer getTeamidhome() {
		return this.teamidhome;
	}

	public void setTeamidhome(Integer teamidhome) {
		this.teamidhome = teamidhome;
	}

	@Column(name = "teamidvisiting")
	public Integer getTeamidvisiting() {
		return this.teamidvisiting;
	}

	public void setTeamidvisiting(Integer teamidvisiting) {
		this.teamidvisiting = teamidvisiting;
	}

	@Column(name = "teamnamehome", length = 200)
	public String getTeamnamehome() {
		return this.teamnamehome;
	}

	public void setTeamnamehome(String teamnamehome) {
		this.teamnamehome = teamnamehome;
	}

	@Column(name = "teamnamevisiting", length = 200)
	public String getTeamnamevisiting() {
		return this.teamnamevisiting;
	}

	public void setTeamnamevisiting(String teamnamevisiting) {
		this.teamnamevisiting = teamnamevisiting;
	}

	@Column(name = "teamscorehome")
	public Integer getTeamscorehome() {
		return this.teamscorehome;
	}

	public void setTeamscorehome(Integer teamscorehome) {
		this.teamscorehome = teamscorehome;
	}

	@Column(name = "teamscorevisiting")
	public Integer getTeamscorevisiting() {
		return this.teamscorevisiting;
	}

	public void setTeamscorevisiting(Integer teamscorevisiting) {
		this.teamscorevisiting = teamscorevisiting;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "ordernum")
	public String getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "courtid")
	public Integer getCourtid() {
		return this.courtid;
	}

	public void setCourtid(Integer courtid) {
		this.courtid = courtid;
	}

	@Column(name = "courtname", length = 200)
	public String getCourtname() {
		return this.courtname;
	}

	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}

	@Column(name = "courtaddress", length = 200)
	public String getCourtaddress() {
		return this.courtaddress;
	}

	public void setCourtaddress(String courtaddress) {
		this.courtaddress = courtaddress;
	}

	@Column(name = "matchdate" )
	public Date getMatchdate() {
		return this.matchdate;
	}

	public void setMatchdate(Date matchdate) {
		this.matchdate = matchdate;
	}

	@Column(name = "matchtime")
	public String getMatchtime() {
		return this.matchtime;
	}

	public void setMatchtime(String matchtime) {
		this.matchtime = matchtime;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "createtime" )
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "createuserid")
	public Integer getCreateuserid() {
		return this.createuserid;
	}

	public void setCreateuserid(Integer createuserid) {
		this.createuserid = createuserid;
	}

	@Column(name = "others", length = 200)
	public String getOthers() {
		return this.others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	@Column(name = "cid")
	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	@Column(name = "cscores", precision = 8, scale = 8)
	public Float getCscores() {
		return this.cscores;
	}

	public void setCscores(Float cscores) {
		this.cscores = cscores;
	}

	@Column(name = "cname")
	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Column(name = "homescore")
	public Integer getHomescore() {
		return this.homescore;
	}

	public void setHomescore(Integer homescore) {
		this.homescore = homescore;
	}

	@Column(name = "visitscore")
	public Integer getVisitscore() {
		return this.visitscore;
	}

	public void setVisitscore(Integer visitscore) {
		this.visitscore = visitscore;
	}

}