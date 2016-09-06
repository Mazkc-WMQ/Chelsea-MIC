package upbox.model;


/**
 * 页面展示的实体类激数bean
 * @author yuancao
 *
 */
public class IntegralBean implements java.io.Serializable {

	private static final long serialVersionUID = -8950742692522174288L;

	private String body_name;//中间展示文字
	private String f_Integral;//发起球队获得激数
	private String x_Integral;//响应球队获得激数
	private String f_body_num;//发起球队获得数字事件
	private String x_body_num;//响应球队获得数字事件
	private int weight;
	
	public String getBody_name() {
		return body_name;
	}
	public void setBody_name(String body_name) {
		this.body_name = body_name;
	}
	public String getF_Integral() {
		return f_Integral;
	}
	public void setF_Integral(String f_Integral) {
		this.f_Integral = f_Integral;
	}
	public String getX_Integral() {
		return x_Integral;
	}
	public void setX_Integral(String x_Integral) {
		this.x_Integral = x_Integral;
	}
	public String getF_body_num() {
		return f_body_num;
	}
	public void setF_body_num(String f_body_num) {
		this.f_body_num = f_body_num;
	}
	public String getX_body_num() {
		return x_body_num;
	}
	public void setX_body_num(String x_body_num) {
		this.x_body_num = x_body_num;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}

	
}