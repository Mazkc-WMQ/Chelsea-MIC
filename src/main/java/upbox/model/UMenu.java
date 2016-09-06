package upbox.model;

import java.util.Date;
import org.apache.struts2.json.annotations.JSON;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 后端菜单bean
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_menu")
public class UMenu implements java.io.Serializable {
	private static final long serialVersionUID = -7968169287825426306L;
	private String menuId;
	private UBUser UUser;
	private String name;
	private String perid;
	private Integer lvl;
	private Date createtime;
	private Integer sort;
	private Date createdate;
	private String menuType;
	private String menuImg;
	private String menuUrl;

	@Id
	@Column(name = "menu_id", unique = true, nullable = false, length = 60)
	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createuser")
	public UBUser getUUser() {
		return this.UUser;
	}

	public void setUUser(UBUser UUser) {
		this.UUser = UUser;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "perid", length = 60)
	public String getPerid() {
		return this.perid;
	}

	public void setPerid(String perid) {
		this.perid = perid;
	}

	@Column(name = "lvl")
	public Integer getLvl() {
		return this.lvl;
	}

	public void setLvl(Integer lvl) {
		this.lvl = lvl;
	}

	@Column(name = "createtime", length = 15)   @JSON(format="HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "sort")
	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "createdate", length = 13)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "menu_type", length = 20)
	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "menu_img", length = 60)
	public String getMenuImg()
	{
		return menuImg;
	}

	public void setMenuImg(String menuImg)
	{
		this.menuImg = menuImg;
	}

	@Column(name = "menu_url", length = 200)
	public String getMenuUrl()
	{
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl)
	{
		this.menuUrl = menuUrl;
	}
}