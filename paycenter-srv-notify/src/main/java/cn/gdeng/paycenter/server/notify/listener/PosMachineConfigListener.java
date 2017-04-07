package cn.gdeng.paycenter.server.notify.listener;

import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import cn.gdeng.paycenter.admin.service.admin.PosMachineConfigService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PosMachineConfigEntity;
import cn.gdeng.paycenter.util.web.api.SerializeUtil;

/**
 * 接收运营后台新增或修改pos终端信息操作异步接收
 * 
 * @author Wesley
 *
 */
public class PosMachineConfigListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BaseDao<?> baseDao;
	@Override
	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive: " + message.getMsgID());
		try {
			String topic = message.getTopic();
			logger.info("topic: " + topic);

			// 获得数据
			PosMachineConfigEntity posMachineConfigEntity = JSONObject
					.parseObject(SerializeUtil.unserialize(message.getBody()).toString(), PosMachineConfigEntity.class);
			Object businessId=posMachineConfigEntity.getBusinessId(); //商铺Id
			//如果传过来的参数中有state这个字段，并且字段值为0，则说明，是delete操作
			if (null!=posMachineConfigEntity.getBusinessId()) {
				if (null!=posMachineConfigEntity.getState()&&posMachineConfigEntity.getState().equals("0")) {
					Map<String,Object> params=new HashMap<>();
					params.put("businessId",businessId);
					params.put("machineNum",posMachineConfigEntity.getMachineNum());
					int id=(int) baseDao.execute("PosMachineConfig.deleteByBusinessId",params);
					logger.debug("成功删除businessId为:"+businessId+",的数据id为:"+id);
					logger.debug("同步删除pos终端信息，machineNum为："+posMachineConfigEntity.getMachineNum());
				}else{
					/*int count= (int)baseDao.execute("PosMachineConfig.deleteByBusinessId", params);
					logger.debug("删除businessId为:"+businessId+",的数据条数为:"+count);*/
					posMachineConfigEntity.setState("1");
					int id=(int) baseDao.execute("PosMachineConfig.insert",posMachineConfigEntity );
					logger.debug("成功插入businessId为:"+businessId+",的数据id为:"+id);
					logger.debug("同步pos终端信息，machineNum为："+posMachineConfigEntity.getMachineNum());
				}
				
			}
			else { // 同步失败 数据会员id 不存在
				logger.error("同步失败， 商铺信息不存在");
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
