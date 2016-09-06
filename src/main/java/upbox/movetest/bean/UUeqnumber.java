package upbox.movetest.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户设备号信息表
 * @author mazkc
 *
 */
@Entity
@Table(name="u_eqnumber")
public class UUeqnumber implements Serializable{
	private static final long serialVersionUID = 300648086310504178L;
	private int id; //主键ID
	private Date createtime; //创建时间
	private String code; //设备号
	private String phoneType; //设备类型 小米.华为.....
	private String cpu; //设备系统版本 
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Date getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}
	@Column(name = "code",length = 200)
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	@Column(name = "phonetype",length = 50)
	public String getPhoneType()
	{
		return phoneType;
	}
	public void setPhoneType(String phoneType)
	{
		this.phoneType = phoneType;
	}
	public String getCpu()
	{
		return cpu;
	}
	public void setCpu(String cpu)
	{
		this.cpu = cpu;
	}
}
