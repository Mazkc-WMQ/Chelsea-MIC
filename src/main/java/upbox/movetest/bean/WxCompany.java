package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WxCompany entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "wx_company")
public class WxCompany implements java.io.Serializable {
	private static final long serialVersionUID = 6835575913930265161L;
	private Integer comId; //主键
	private String name; //名称
	private String topenid; //微信原始ID
	private String adress; //地址
	private Date createtime; //创建时间
	private String tel; //电话
	private String appid; 
	private String appsecret;
	private String remark; //描述
	private Integer status; //状态
	private String aes; //微信加密key
	private String mchId; //商户号
	private String apikey; //微信支付key

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getComId()
	{
		return comId;
	}

	public void setComId(Integer comId)
	{
		this.comId = comId;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "topenid", length = 200)
	public String getTopenid() {
		return this.topenid;
	}

	public void setTopenid(String topenid) {
		this.topenid = topenid;
	}

	@Column(name = "adress", length = 200)
	public String getAdress() {
		return this.adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public Date getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}

	@Column(name = "tel", length = 50)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "appid", length = 200)
	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@Column(name = "appsecret", length = 200)
	public String getAppsecret() {
		return this.appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "aes", length = 500)
	public String getAes() {
		return this.aes;
	}

	public void setAes(String aes) {
		this.aes = aes;
	}

	@Column(name = "mch_id", length = 300)
	public String getMchId() {
		return this.mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	@Column(name = "apikey", length = 300)
	public String getApikey() {
		return this.apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}