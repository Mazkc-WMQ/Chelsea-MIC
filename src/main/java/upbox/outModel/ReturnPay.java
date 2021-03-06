package upbox.outModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 支付组件返回加密对象
 * @author yuancao
 *
 */
public class ReturnPay implements Serializable{
	
	private static final long serialVersionUID = -1120053093818519798L;
	
	private String _package;
	private String appid;
	private String sign;
	private String partnerid;
	private String prepayid;
	private String noncestr;
	private String timestamp;
	public String get_package() {
		return _package;
	}
	public void set_package(String _package) {
		this._package = _package;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
}
