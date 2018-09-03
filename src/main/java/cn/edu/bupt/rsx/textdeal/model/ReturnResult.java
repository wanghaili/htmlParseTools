package cn.edu.bupt.rsx.textdeal.model;

import java.io.Serializable;

import cn.edu.bupt.rsx.textdeal.tools.ResponseUtils;

public class ReturnResult implements Serializable{

	private static final long serialVersionUID = 5001787130057732916L;

	private int errcode = 0;
	
	private String errMsg = "请求成功";
	
	private Object result;
	
	
	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
		this.errMsg = ResponseUtils.getMsg(errcode);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
 
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReturnResult [errcode=");
		builder.append(errcode);
		builder.append(", errMsg=");
		builder.append(errMsg);
		builder.append(", result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}
	
	
}
