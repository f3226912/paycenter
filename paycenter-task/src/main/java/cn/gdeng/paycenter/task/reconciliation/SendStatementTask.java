package cn.gdeng.paycenter.task.reconciliation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.api.server.pay.PayTypeService;
import cn.gdeng.paycenter.api.server.pay.PosPayNotifyService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.entity.pay.PosPayNotifyEntity;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.web.api.EnvUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;

public class SendStatementTask {
	
	@Autowired(required=false)
	private PosPayNotifyService posPayNotifyService;
	
	@Resource
	private SendMessageUtil sendMessage;
	
	@Resource
	private GdProperties gdProperties;
	
	@Autowired(required=false)
	private PayTypeService payTypeService;
	
	private String payNsyCreateTopic;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void execute(){
		
		if(EnvUtil.isSingleNode()){//暂时解决，该问题需要分布式锁 + 读取数据并更改状态，释放锁，+更新时间才能解决
			return;//单节点
		}
		Map<String,Object> params = new HashMap<>();
		params.put("status", NotifyStatus.READ_NOTIFY);
		List<PosPayNotifyDto> list = posPayNotifyService.queryByCondition(params);
		if(list != null && list.size() > 0){
			for(PosPayNotifyDto dto : list){
				try {
					String topic = getPayNsyCreateTopic();
					//获取第三方收款方帐号
					params.clear();
					params.put("payType", dto.getPayChannelCode());
					List<PayTypeEntity> plist = payTypeService.queryByCondition(params);
					if(null == list || list.size() == 0){
						throw new RuntimeException( "支付方式["+dto.getPayChannelCode()+"]不存在");
					}

					dto.setGdBankCardNo(plist.get(0).getGdBankCardNo());
					logger.info("POS刷卡通知(反向)开始发送MQ：topic="+ topic);
					sendMessage.sendMessage(topic, "TAGS", dto.getPayCenterNumber(),dto);
					
					PosPayNotifyEntity entity = new PosPayNotifyEntity();
					entity.setId(dto.getId());
					entity.setStatus(NotifyStatus.NOTIFIED);
					posPayNotifyService.dynamicUpdatePosPayNotify(entity);
					logger.info("POS刷卡通知(反向)发送MQ成功：topic="+ topic);
				} catch (Exception e) {
					//记录日志
					logger.info("POS刷卡(反向)通知发送MQ失败,"+e.getMessage(),e);
				}
			}
			
		}
	}
	
	private String getPayNsyCreateTopic(){
		if(null == payNsyCreateTopic){
			payNsyCreateTopic = gdProperties.getProperties().getProperty("payNsyCreateTopic");
		}
		return payNsyCreateTopic;
		
	}
}
