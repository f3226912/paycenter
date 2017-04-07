package cn.gdeng.paycenter.gateway.exception;

/**控制层异常
 * @author wjguo
 * datetime 2016年8月3日 上午11:27:29  
 *
 */
@SuppressWarnings("serial")
public class ControllerException extends RuntimeException {
	/**
	 * 错误码
	 */
	private Integer code;
	/**
	 * 消息
	 */
	private String msg;
	public ControllerException() {
		super();
	}
	public ControllerException(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public ControllerException(String msg, Throwable cause) {
		super(msg, cause);
		this.msg = msg;
	}
	public ControllerException(String msg) {
		super(msg);
		this.msg = msg;
	}
	public ControllerException(Throwable cause) {
		super(cause);
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
