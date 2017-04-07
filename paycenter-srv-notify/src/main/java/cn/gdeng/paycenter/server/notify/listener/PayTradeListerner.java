package cn.gdeng.paycenter.server.notify.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import cn.gdeng.paycenter.api.server.pay.PosPayService;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

public class PayTradeListerner  implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private PosPayService posPayService;

	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive: " + message.getMsgID());
		try {
			String topic = message.getTopic();
			
			// 获得数据
			String msg = SerializeUtil.unserialize(message.getBody()).toString();
			logger.info("接收到订单生成MQ发送的信息,topic: " + topic+",msg:"+msg);
			String[] msgs = msg.split("#");

			posPayService.orderSync(msgs[0],msgs[1]);
			logger.info("接收到订单生成MQ发送的信息,处理成功,topic: " + topic);
			return Action.CommitMessage;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("处理订单生成MQ发送的信息出错：" + e.getMessage());
			// 消费失败
			return Action.ReconsumeLater;
		}
	}

}
