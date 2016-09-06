package upbox.model;

import java.io.Serializable;

public class RetMsgResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -521152064183414427L;
	
	private Integer ret;    //返回结果 标志位 1 成功 -1 失败 0 token无效
	private Object result;  //返回结果 对象
	private Object errorMsg; //错误信息
	private Object successMsg; //成功信息
	
	/**
	 * 操作成功，快速 设置返回结果
	 * @param operid
	 */
	public void setOperSuccess(Object result,int ret,Object successMsg){
		this.ret = ret;
		this.result = result;
		this.successMsg = successMsg;
	}
	
	/**
	 * 操作失败，快速 设置返回结果
	 * @param operid
	 */
	public void setOperFail(Object result,int ret,Object errorMsg) {
		this.result = result;
		this.ret = ret;
		this.errorMsg = errorMsg;
	}
	
	public Object getResult() {
		return result;
	}
	public Integer getRet() {
		return ret;
	}
	public void setResult(Object result) {
		this.result = result;
	} 
	public void setRet(Integer ret) {
		this.ret = ret;
	}

	/**
	 * @return the errorMsg
	 */
	public Object getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(Object errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * @return the successMsg
	 */
	public Object getSuccessMsg() {
		return successMsg;
	}

	/**
	 * @param successMsg the successMsg to set
	 */
	public void setSuccessMsg(Object successMsg) {
		this.successMsg = successMsg;
	}
}
