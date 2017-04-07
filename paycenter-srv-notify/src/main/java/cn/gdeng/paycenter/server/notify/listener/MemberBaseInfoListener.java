package cn.gdeng.paycenter.server.notify.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import cn.gdeng.paycenter.api.server.notify.MemberBaseinfoService;
import cn.gdeng.paycenter.dto.pay.MemberSendDto;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

/**
 * 接收运营后台新增或修改用户操作异步接收
 * 
 * @author Wesley
 *
 */
public class MemberBaseInfoListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MemberBaseinfoService memberBaseinfoService;


	@Override
	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive: " + message.getMsgID());
		try {
			String topic = message.getTopic();
			logger.info("topic: " + topic);

			// 获得数据
			MemberSendDto memberSendDto = JSONObject
					.parseObject(SerializeUtil.unserialize(message.getBody()).toString(), MemberSendDto.class);

			/*
			 * 0,创建，1 更新
			 */
			if (memberSendDto.getCrud()==0) {
				memberBaseinfoService.addMember(memberSendDto);
				logger.debug("同步新增数据信息，MemberId为："+memberSendDto.getMemberId());
			}if (memberSendDto.getCrud()==1) {
				memberBaseinfoService.updateMemberBaseinfoDTO(memberSendDto); 
				logger.debug("同步更新数据信息，MemberId为："+memberSendDto.getMemberId());
			}
			else { // 同步失败 数据会员id 不存在
				logger.error("同步失败， 数据会员id【memberId】 为空");
			}

			return Action.CommitMessage;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("金牌会员接收支付中心结果失败：" + e.getMessage());
			// 消费失败
			return Action.ReconsumeLater;
		}
	}

}
