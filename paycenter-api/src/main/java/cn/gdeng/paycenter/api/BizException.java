package cn.gdeng.paycenter.api;

/**
 * 业务异常定义
 * 
 * @author zhangnf
 *
 */
public class BizException extends Exception {

	private static final long serialVersionUID = -4841089165626408293L;
	/** 编码 **/
	private Integer code;
	/** 描述 **/
	private String msg;

	public BizException(Integer code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
		
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
