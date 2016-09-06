package upbox.model.zhetao;

public class RaceGroup {
	private Integer id;//ID
	private Integer rid;//比赛ID
	private Integer rrid;//赛制ID---不再使用0112
	private String name;//球队名称
	private Integer rqid;//球队ID(201602修改类型)
	private Integer rgroup;//分组ID(1,2,3..最大为Race中的rrgroup)
	private Integer showorder;//权重
	private Integer gstatus;//0-没获取(数据更新时更新),1-已获取(接口访问时更新)
	private String updatedate;//最后更新日期
	private RaceTeam team;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Integer getRrid() {
		return rrid;
	}
	public void setRrid(Integer rrid) {
		this.rrid = rrid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRqid() {
		return rqid;
	}
	public void setRqid(Integer rqid) {
		this.rqid = rqid;
	}
	public Integer getRgroup() {
		return rgroup;
	}
	public void setRgroup(Integer rgroup) {
		this.rgroup = rgroup;
	}
	public Integer getShoworder() {
		return showorder;
	}
	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}
	public Integer getGstatus() {
		return gstatus;
	}
	public void setGstatus(Integer gstatus) {
		this.gstatus = gstatus;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public RaceTeam getTeam() {
		return team;
	}
	public void setTeam(RaceTeam team) {
		this.team = team;
	}
	
	
}
