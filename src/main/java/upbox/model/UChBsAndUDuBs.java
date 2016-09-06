 package upbox.model;

import java.util.Date;

/**
 * 整合挑战及擂主展示实体类
 * @author yc
 *
 * 13611929818
 */

public class UChBsAndUDuBs implements java.io.Serializable {
	

	private static final long serialVersionUID = 4401191215460646087L;
	
	private String mainId;	
	private UChallenge UChallenge;
	private UTeam fteam;
	private UTeam xteam;
	private String fqGoal;
	private String fqFj;
	private String xyGoal;
	private String xyFj;
	private Date stdate;
	private String sttime;
	private Date enddate;	
	private String endtime;
	private String chTypeName;
	private String numTypeName;
	private Date descDate;
	private String courtName;
	private URuleTopParameter tp;
	
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public UChallenge getUChallenge() {
		return UChallenge;
	}
	public void setUChallenge(UChallenge uChallenge) {
		UChallenge = uChallenge;
	}
	public UTeam getFteam() {
		return fteam;
	}
	public void setFteam(UTeam fteam) {
		this.fteam = fteam;
	}
	public UTeam getXteam() {
		return xteam;
	}
	public void setXteam(UTeam xteam) {
		this.xteam = xteam;
	}
	public String getFqGoal() {
		return fqGoal;
	}
	public void setFqGoal(String fqGoal) {
		this.fqGoal = fqGoal;
	}
	public String getFqFj() {
		return fqFj;
	}
	public void setFqFj(String fqFj) {
		this.fqFj = fqFj;
	}
	public String getXyGoal() {
		return xyGoal;
	}
	public void setXyGoal(String xyGoal) {
		this.xyGoal = xyGoal;
	}
	public String getXyFj() {
		return xyFj;
	}
	public void setXyFj(String xyFj) {
		this.xyFj = xyFj;
	}
	public Date getStdate() {
		return stdate;
	}
	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}
	public String getSttime() {
		return sttime;
	}
	public void setSttime(String sttime) {
		this.sttime = sttime;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getChTypeName() {
		return chTypeName;
	}
	public void setChTypeName(String chTypeName) {
		this.chTypeName = chTypeName;
	}
	public Date getDescDate() {
		return descDate;
	}
	public void setDescDate(Date descDate) {
		this.descDate = descDate;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getNumTypeName() {
		return numTypeName;
	}
	public void setNumTypeName(String numTypeName) {
		this.numTypeName = numTypeName;
	}

	public URuleTopParameter getTp() {
		return tp;
	}
	public void setTp(URuleTopParameter tp) {
		this.tp = tp;
	}


	
	
	
	
}