package upbox.movetest.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 支付bean
 * @author mazkc
 *
 */
@Entity
@Table(name = "u_pay")
public class UUpay implements java.io.Serializable {
	private static final long serialVersionUID = 3146457204409841200L;
	private Integer id;
	private Date createtime; //创建时间
	private String topenid; //公众号openid
	private String body; //产品内容
	private String out_trade_no; //订单号
	private String fee_type; //货币类型
	private int total_fee; //交易金额
	private String spbill_create_ip; //客户端IP;
	private String notify_url; //回调地址
	private String trade_type; //API类型
	private String pay_success_url; //支付成功后跳转的URL链接
	private String pay_fail_url; //支付失败后跳转的URL链接 
	private int userid; //操作用户ID

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
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

	public String getTopenid()
	{
		return topenid;
	}

	public void setTopenid(String topenid)
	{
		this.topenid = topenid;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getOut_trade_no()
	{
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no)
	{
		this.out_trade_no = out_trade_no;
	}

	@Column(name = "fee_type",length = 50)
	public String getFee_type()
	{
		return fee_type;
	}

	public void setFee_type(String fee_type)
	{
		this.fee_type = fee_type;
	}

	public int getTotal_fee()
	{
		return total_fee;
	}

	public void setTotal_fee(int total_fee)
	{
		this.total_fee = total_fee;
	}
	@Column(name = "spbill_create_ip",length = 150)
	public String getSpbill_create_ip()
	{
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip)
	{
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url()
	{
		return notify_url;
	}

	public void setNotify_url(String notify_url)
	{
		this.notify_url = notify_url;
	}
	@Column(name = "trade_type",length = 50)
	public String getTrade_type()
	{
		return trade_type;
	}

	public void setTrade_type(String trade_type)
	{
		this.trade_type = trade_type;
	}

	public String getPay_success_url()
	{
		return pay_success_url;
	}

	public void setPay_success_url(String pay_success_url)
	{
		this.pay_success_url = pay_success_url;
	}

	public String getPay_fail_url()
	{
		return pay_fail_url;
	}

	public void setPay_fail_url(String pay_fail_url)
	{
		this.pay_fail_url = pay_fail_url;
	}

	public int getUserid()
	{
		return userid;
	}

	public void setUserid(int userid)
	{
		this.userid = userid;
	}
}