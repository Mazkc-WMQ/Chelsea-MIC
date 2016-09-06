package upbox.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 前端消息记录表
 * @author charlescai
 *
 * 13120501633
 */
@Entity
@Table(name = "u_push_message")
public class UPushMessage implements java.io.Serializable {
	private static final long serialVersionUID = -6589993382923479263L;
	private String push_id;
	private String object_id;
	private String type;
	private String event;
	private String push_status;
	private String push_type;
	private Date createtime;
	

	@Id
	@Column(name = "push_id", unique = true, nullable = false, length = 60)
	public String getPush_id() {
		return push_id;
	}

	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}

	@Column(name = "object_id", length = 60)
	public String getObject_id() {
		return object_id;
	}

	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}

	@Column(name = "type", length = 20)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	@Column(name = "event", length = 20)
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Column(name = "push_status", length = 20)
	public String getPush_status() {
		return push_status;
	}

	public void setPush_status(String push_status) {
		this.push_status = push_status;
	}

	@Column(name = "push_type", length = 20)
	public String getPush_type() {
		return push_type;
	}

	public void setPush_type(String push_type) {
		this.push_type = push_type;
	}

	@Column(name = "createtime", length = 20)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date creattime) {
		this.createtime = creattime;
	}

}