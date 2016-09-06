package upbox.movetest.bean;

import java.util.Date;
 
import javax.persistence.Entity;
 
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 球员BEAN
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_activity_second")
public class ActivitySecond implements java.io.Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5574901938195194945L;
	private String id; 
	private String type; 
	private String createusername; 
	private String createuserphone; 
	private String createteamname; 
	private String createteamremark; 
	private String createteamimageurl1; 
	private String createteamimageurl2; 
	private String askuserphone; 
	private String createothers1; 
	private String createothers2; 
	private Date createtime; 
	private Date modifyteamcreatetime; 
	private String modifyteamname; 
			
	private Integer modifyteamid; 
	
	private String modifyteamenglishname; 
	
	private String modifyteamremark; 
	
	private String modifyshape1; 
	private String modifyshape2; 
	private String modifycolor1; 
	private String modifycolor2; 
	private String modifysugest; 
	private String modifyteamimageurl1; 
	private String modifyteamimageurl2; 
	private String modifyothers1; 
	private String modifyothers2; 
	private String modifyuserphone;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreateusername() {
		return createusername;
	}
	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}
	public String getCreateuserphone() {
		return createuserphone;
	}
	public void setCreateuserphone(String createuserphone) {
		this.createuserphone = createuserphone;
	}
	public String getCreateteamname() {
		return createteamname;
	}
	public void setCreateteamname(String createteamname) {
		this.createteamname = createteamname;
	}
	public String getCreateteamremark() {
		return createteamremark;
	}
	public void setCreateteamremark(String createteamremark) {
		this.createteamremark = createteamremark;
	}
	public String getCreateteamimageurl1() {
		return createteamimageurl1;
	}
	public void setCreateteamimageurl1(String createteamimageurl1) {
		this.createteamimageurl1 = createteamimageurl1;
	}
	public String getCreateteamimageurl2() {
		return createteamimageurl2;
	}
	public void setCreateteamimageurl2(String createteamimageurl2) {
		this.createteamimageurl2 = createteamimageurl2;
	}
	public String getAskuserphone() {
		return askuserphone;
	}
	public void setAskuserphone(String askuserphone) {
		this.askuserphone = askuserphone;
	}
	public String getCreateothers1() {
		return createothers1;
	}
	public void setCreateothers1(String createothers1) {
		this.createothers1 = createothers1;
	}
	public String getCreateothers2() {
		return createothers2;
	}
	public void setCreateothers2(String createothers2) {
		this.createothers2 = createothers2;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getModifyteamcreatetime() {
		return modifyteamcreatetime;
	}
	public void setModifyteamcreatetime(Date modifyteamcreatetime) {
		this.modifyteamcreatetime = modifyteamcreatetime;
	}
	public String getModifyteamname() {
		return modifyteamname;
	}
	public void setModifyteamname(String modifyteamname) {
		this.modifyteamname = modifyteamname;
	}
	public Integer getModifyteamid() {
		return modifyteamid;
	}
	public void setModifyteamid(Integer modifyteamid) {
		this.modifyteamid = modifyteamid;
	}
	public String getModifyteamenglishname() {
		return modifyteamenglishname;
	}
	public void setModifyteamenglishname(String modifyteamenglishname) {
		this.modifyteamenglishname = modifyteamenglishname;
	}
	public String getModifyteamremark() {
		return modifyteamremark;
	}
	public void setModifyteamremark(String modifyteamremark) {
		this.modifyteamremark = modifyteamremark;
	}
	public String getModifyshape1() {
		return modifyshape1;
	}
	public void setModifyshape1(String modifyshape1) {
		this.modifyshape1 = modifyshape1;
	}
	public String getModifyshape2() {
		return modifyshape2;
	}
	public void setModifyshape2(String modifyshape2) {
		this.modifyshape2 = modifyshape2;
	}
	public String getModifycolor1() {
		return modifycolor1;
	}
	public void setModifycolor1(String modifycolor1) {
		this.modifycolor1 = modifycolor1;
	}
	public String getModifycolor2() {
		return modifycolor2;
	}
	public void setModifycolor2(String modifycolor2) {
		this.modifycolor2 = modifycolor2;
	}
	public String getModifysugest() {
		return modifysugest;
	}
	public void setModifysugest(String modifysugest) {
		this.modifysugest = modifysugest;
	}
	public String getModifyteamimageurl1() {
		return modifyteamimageurl1;
	}
	public void setModifyteamimageurl1(String modifyteamimageurl1) {
		this.modifyteamimageurl1 = modifyteamimageurl1;
	}
	public String getModifyteamimageurl2() {
		return modifyteamimageurl2;
	}
	public void setModifyteamimageurl2(String modifyteamimageurl2) {
		this.modifyteamimageurl2 = modifyteamimageurl2;
	}
	public String getModifyothers1() {
		return modifyothers1;
	}
	public void setModifyothers1(String modifyothers1) {
		this.modifyothers1 = modifyothers1;
	}
	public String getModifyothers2() {
		return modifyothers2;
	}
	public void setModifyothers2(String modifyothers2) {
		this.modifyothers2 = modifyothers2;
	}
	public String getModifyuserphone() {
		return modifyuserphone;
	}
	public void setModifyuserphone(String modifyuserphone) {
		this.modifyuserphone = modifyuserphone;
	} 
	
	
	
	 
	
}