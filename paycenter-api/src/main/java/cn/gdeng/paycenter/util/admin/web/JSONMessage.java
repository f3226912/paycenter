package cn.gdeng.paycenter.util.admin.web;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: TODO(json消息体)
 * @author mpan
 * @date 2015年12月4日 下午1:22:43
 */
public class JSONMessage {
	
	public static String SUCCESS = "1";
	public static String FAIL = "0";
	public static String TIMEOUT = "2";
	
	private String flag; // 1成功 0失败 2超时
	private String msg; // 消息
	private Object data; // 数据
	private Object res;
	
	public JSONMessage() {
		flag = SUCCESS;
		msg = "成功";
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getRes() {
		return res;
	}

	public void setRes(Object res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
