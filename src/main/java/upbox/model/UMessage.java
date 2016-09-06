package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

/**
 * 前端消息记录表
 * @author charlescai
 *
 * 13120501633
 */
@Entity
@Table(name = "u_message")
public class UMessage implements java.io.Serializable {
	private static final long serialVersionUID = -6589993382923479263L;
	private String keyId;
	private Date createtime;
	private String content;
	private String url;
	private String mesReadStauts;
	private String type;
	private String params;
	private String userId;
	private String mesType;
	private String isShow;

	@Id
	@Column(name = "keyid", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@Column(name = "createtime", length = 15)  
	@JSON(format="yyyy年MM月dd日 HH:mm:ss")
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	
	@Column(name = "content", length = 300)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "url", length = 300)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "mes_read_stauts", length = 20)
	public String getMesReadStauts() {
		return mesReadStauts;
	}

	public void setMesReadStauts(String mesReadStauts) {
		this.mesReadStauts = mesReadStauts;
	}

	@Column(name = "type", length = 20)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "params", length = 200)
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "userid", length = 200)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "mes_type", length = 20)
	public String getMesType() {
		return mesType;
	}

	public void setMesType(String mesType) {
		this.mesType = mesType;
	}
	
	@Column(name = "is_show", length = 20)
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}