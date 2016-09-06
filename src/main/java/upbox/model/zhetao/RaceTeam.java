package upbox.model.zhetao;

public class RaceTeam {
	private Integer id;//球队ID
	private String name;//球队名称
	private String teamid;//大系统球队ID
	private String regdate;//添加日期
	private String userName;//发布人
	private Integer showorder;//显示顺序
	private Integer status;//状态,1-OK,2-已删除
	private String logo;//球队头像
	private String description;//介绍
	private Integer gstatus;//0-没获取(数据更新时更新),1-已获取(接口访问时更新)
	private String updatedate;//最后更新日期
	private Integer createenroll;//是否报名中创建	是否报名中创建1-是
	private String createuid;//创建人ID
	private String createphone;//创建人电话
	private String leaderphone;//队长电话
	private String upteamname;//Upbox球队名称
	private String upteamlogo;//Upbox球队LOGO
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamid() {
		return teamid;
	}
	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getShoworder() {
		return showorder;
	}
	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getCreateenroll() {
		return createenroll;
	}
	public void setCreateenroll(Integer createenroll) {
		this.createenroll = createenroll;
	}
	public String getCreateuid() {
		return createuid;
	}
	public void setCreateuid(String createuid) {
		this.createuid = createuid;
	}
	public String getCreatephone() {
		return createphone;
	}
	public void setCreatephone(String createphone) {
		this.createphone = createphone;
	}
	public String getLeaderphone() {
		return leaderphone;
	}
	public void setLeaderphone(String leaderphone) {
		this.leaderphone = leaderphone;
	}
	public String getUpteamname() {
		return upteamname;
	}
	public void setUpteamname(String upteamname) {
		this.upteamname = upteamname;
	}
	public String getUpteamlogo() {
		return upteamlogo;
	}
	public void setUpteamlogo(String upteamlogo) {
		this.upteamlogo = upteamlogo;
	}
	
	
}
