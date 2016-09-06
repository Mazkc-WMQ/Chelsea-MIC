package upbox.movetest.bean;

import java.io.Serializable;

/**
 * 微信支付回调信息基类
 * @author mazkc
 *
 */
public class BaseWxPay implements Serializable{
	private static final long serialVersionUID = -2833034272138947338L;
	private String return_code;
	private String return_msg;
	
	
	public String getReturn_code()
	{
		return return_code;
	}
	public void setReturn_code(String return_code)
	{
		this.return_code = return_code;
	}
	public String getReturn_msg()
	{
		return return_msg;
	}
	public void setReturn_msg(String return_msg)
	{
		this.return_msg = return_msg;
	}
}
