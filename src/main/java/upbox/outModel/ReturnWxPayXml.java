package upbox.outModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 处理完成后告知微信的bean
 * @author yuancao
 *
 */
public class ReturnWxPayXml implements Serializable{
	
	private static final long serialVersionUID = 4891335341222091673L;
	
	private String return_code;
	private String return_msg;
	
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	
}
