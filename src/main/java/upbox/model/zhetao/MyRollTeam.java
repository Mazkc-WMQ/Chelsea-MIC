package upbox.model.zhetao;

/**
 * 底标5实体类   报名的赛事
 * @author yuancao
 *
 * 13611929818
 */
public class MyRollTeam {
	private String name;//队名
	private Integer id;//球队id
	private String description;//介绍
	private Integer status;//状态
	private Integer showorder;//显示顺序
	private String logo;//球队头像
	private String teamid;//upbox球队id
	private String upteamname;//upbox球队名
	private String upteamlogo;//upbox球队头像
	private String leaderphone;//队长电话
	private Integer createenroll;//是否报名中创建
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getShoworder() {
		return showorder;
	}
	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getTeamid() {
		return teamid;
	}
	public void setTeamid(String teamid) {
		this.teamid = teamid;
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
	public String getLeaderphone() {
		return leaderphone;
	}
	public void setLeaderphone(String leaderphone) {
		this.leaderphone = leaderphone;
	}
	public Integer getCreateenroll() {
		return createenroll;
	}
	public void setCreateenroll(Integer createenroll) {
		this.createenroll = createenroll;
	}
	
	
	
}
