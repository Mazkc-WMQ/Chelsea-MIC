package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * album javabean
 * @author kevin
 *
 */
@Entity
@Table(name = "u_userandteamalbum")
public class UUserAndTeamAlbum implements java.io.Serializable {
	private static final long serialVersionUID = -8936824218012691445L;
	
	private String albumid;
	private Integer usestatus;
	private String iconurl;
	private String imageurl;
	private Integer userid;
	private String type;
	private Date createtime;
	
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private Integer playerid;
	private Integer teamid;
	
	@Id
	@Column(name="id")
	public String getAlbumid() {
		return albumid;
	}
	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}
	public Integer getUsestatus() {
		return usestatus;
	}
	public void setUsestatus(Integer usestatus) {
		this.usestatus = usestatus;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getPlayerid() {
		return playerid;
	}
	public void setPlayerid(Integer playerid) {
		this.playerid = playerid;
	}
	public Integer getTeamid() {
		return teamid;
	}
	public void setTeamid(Integer teamid) {
		this.teamid = teamid;
	}



 

}