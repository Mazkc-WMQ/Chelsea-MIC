package upbox.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 支付bean
 * @author yuancao
 *
 */
@Entity
@Table(name = "u_pay")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UUpay implements java.io.Serializable {
	
	private static final long serialVersionUID = 2528521872838383868L;
	
	private String id;
	private Date createtime; //创建时间
	private String topenid; //公众号openid
	private String openid; //个人openid
	private String body; //产品内容
	private String outTradeNo; //订单号
	private String feeType; //货币类型
	private double totalFee; //交易金额
	private String spbillCreateIp; //客户端IP;
	private String notifyUrl; //回调地址
	private String tradeType; //API类型
	private String paySuccessUrl; //支付成功后跳转的URL链接
	private String payFailUrl; //支付失败后跳转的URL链接 
	private String userId;

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 60)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createtime")
	public Date getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}

	@Column(name = "topenid")
	public String getTopenid()
	{
		return topenid;
	}

	public void setTopenid(String topenid)
	{
		this.topenid = topenid;
	}

	@Column(name = "body")
	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "out_trade_no")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Column(name = "fee_type")
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	@Column(name = "total_fee")
	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	@Column(name = "spbill_create_ip")
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	@Column(name = "notify_url")
	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	@Column(name = "trade_type")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@Column(name = "pay_success_url")
	public String getPaySuccessUrl() {
		return paySuccessUrl;
	}

	public void setPaySuccessUrl(String paySuccessUrl) {
		this.paySuccessUrl = paySuccessUrl;
	}

	@Column(name = "pay_fail_url")
	public String getPayFailUrl() {
		return payFailUrl;
	}

	public void setPayFailUrl(String payFailUrl) {
		this.payFailUrl = payFailUrl;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}