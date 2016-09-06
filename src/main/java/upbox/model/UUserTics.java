package upbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户物流信息表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_user_tics")
public class UUserTics implements java.io.Serializable {
	private static final long serialVersionUID = -564504145395506636L;
	private String ticsId;
	private UUser UUser;
	private String deliveryId;
	private String deliveryType;
	private String name;
	private String address;
	private String phone;
	private String deliveryStatus;

	@Id
	@Column(name = "tics_id", unique = true, nullable = false, length = 60)
	public String getTicsId() {
		return this.ticsId;
	}

	public void setTicsId(String ticsId) {
		this.ticsId = ticsId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public UUser getUUser()
	{
		return UUser;
	}

	public void setUUser(UUser uUser)
	{
		UUser = uUser;
	}

	@Column(name = "delivery_id", length = 60)
	public String getDeliveryId() {
		return this.deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	@Column(name = "delivery_type", length = 20)
	public String getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "phone", length = 38)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "delivery_status", length = 20)
	public String getDeliveryStatus() {
		return this.deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}