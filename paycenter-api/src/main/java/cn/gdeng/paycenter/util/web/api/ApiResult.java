package cn.gdeng.paycenter.util.web.api;

import java.io.Serializable;

import cn.gdeng.paycenter.enums.MsgCons;

public class ApiResult<T> implements Serializable {
	private static final long serialVersionUID = 229227289014288108L;
	private Boolean isSuccess;
	private Integer code;
	private String msg;
	private String token;
	private T result;

	public ApiResult() {
		this.isSuccess = Boolean.valueOf(true);
		this.code = MsgCons.C_10000;
		this.msg = MsgCons.M_10000;
	}

	public ApiResult(T result, Integer code, String msg) {
		this.isSuccess = Boolean.valueOf(true);
		this.result = result;
		this.code = code;
		this.msg = msg;
	}
	public boolean isSuccess() {
		return this.isSuccess.booleanValue();
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public T getResult() {
		return (T) this.result;
	}

	public ApiResult<T> setResult(T result) {
		this.result = result;
		return this;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public ApiResult<T> setCodeMsg(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
		return this;
	}

	public ApiResult<T> withResult(T result) {
		setIsSuccess(Boolean.valueOf(true));
		setResult(result);
		return this;
	}

	public ApiResult<T> withError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
		setIsSuccess(Boolean.valueOf(false));
		return this;
	}
}