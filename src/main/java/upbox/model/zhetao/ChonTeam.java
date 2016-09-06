package upbox.model.zhetao;

public class ChonTeam {
    /** 球队名称 **/
	private String name;
    /** 球队在赛事中的分组ID **/
	private Integer id;
    /** 球队分组号, 1-A,2-B...依次,0-未分组 **/
	private Integer rgroup;
    /** 球队LOGO **/
    private String teamlogo;
    /** 球队Upbox ID **/
    private String upboxTeamid;
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
	public Integer getRgroup() {
		return rgroup;
	}
	public void setRgroup(Integer rgroup) {
		this.rgroup = rgroup;
	}
	public String getTeamlogo() {
		return teamlogo;
	}
	public void setTeamlogo(String teamlogo) {
		this.teamlogo = teamlogo;
	}
	public String getUpboxTeamid() {
		return upboxTeamid;
	}
	public void setUpboxTeamid(String upboxTeamid) {
		this.upboxTeamid = upboxTeamid;
	}
    
    
}
