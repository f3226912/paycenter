package cn.gdeng.paycenter.util.server.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;

import cn.gdeng.paycenter.util.web.api.GSONUtils;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

public class SendMessageUtil {

	private ProducerBean producer;

	/**
	 * 同步发送
	 * 
	 * @param topic
	 * @param tag
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean sendMessage(String topic, String tag, Object msg) {
		Message message = new Message(topic, tag, SerializeUtil.serialize(GSONUtils.toJson(msg, false)));
		boolean isSuccess = false;
		SendResult sendResult = null;
		try {
			sendResult = producer.send(message);
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
		}
		return isSuccess;
	}
	
	public boolean sendMessage(String topic, String tag, String key,Object msg) {
		Message message = new Message(topic, tag, SerializeUtil.serialize(GSONUtils.toJson(msg, false)));
		boolean isSuccess = false;
		message.setKey(key);
		SendResult sendResult = null;
		try {
			sendResult = producer.send(message);
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * 异步发送
	 * 
	 * @param topic
	 * @param tag
	 * @param msg
	 * @param sendCallback
	 */
	public void sendAsyncMessage(String topic, String tag, Object msg, SendCallback sendCallback) {
		Message message = new Message(topic, tag, SerializeUtil.serialize(GSONUtils.toJson(msg, false)));
		producer.sendAsync(message, sendCallback);
	}
	
	public void sendAsyncMessage(String topic, String tag, String key,Object msg, SendCallback sendCallback) {
		Message message = new Message(topic, tag, SerializeUtil.serialize(GSONUtils.toJson(msg, false)));
		message.setKey(key);
		producer.sendAsync(message, sendCallback);
	}

	public ProducerBean getProducer() {
		return producer;
	}

	public void setProducer(ProducerBean producer) {
		this.producer = producer;
	}

}
