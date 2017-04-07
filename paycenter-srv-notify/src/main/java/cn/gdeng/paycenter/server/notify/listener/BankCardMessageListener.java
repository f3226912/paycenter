package cn.gdeng.paycenter.server.notify.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import cn.gdeng.paycenter.api.server.pay.MemberBankcardInfoService;
import cn.gdeng.paycenter.entity.pay.MemberBankcardInfoEntity;
import cn.gdeng.paycenter.util.web.api.GSONUtils;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

/**
 * 用户银行卡消费者，处理银行卡变更
 * 
 * @author Ailen
 *
 */
public class BankCardMessageListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MemberBankcardInfoService bankcardInfoService;

	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive: " + message.getMsgID());
		try {
			String topic = message.getTopic();
			logger.info("topic: " + topic);

			// 获得数据
			MemberBankcardInfoEntity memberBankcardInfoEntity = (MemberBankcardInfoEntity) GSONUtils.fromJsonToObject(
					SerializeUtil.unserialize(message.getBody()).toString(), MemberBankcardInfoEntity.class);

			// 数据为空 抛出异常
			if (memberBankcardInfoEntity == null)
				throw new RuntimeException("获得用户银行信息为空！");

			// 设置修改人为本人
			if (memberBankcardInfoEntity.getMemberId() != null)
				memberBankcardInfoEntity.setUpdateUserId(memberBankcardInfoEntity.getMemberId().toString());

			// 添加数据库同步
			bankcardInfoService.addOrEditMemberBankcardInfo(memberBankcardInfoEntity);

			return Action.CommitMessage;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("同步用户银行卡信息失败：" + e.getMessage());
			// 消费失败
			return Action.ReconsumeLater;
		}
	}

}
