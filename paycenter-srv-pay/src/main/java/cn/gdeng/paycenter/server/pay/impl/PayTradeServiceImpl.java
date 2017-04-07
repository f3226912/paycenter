package cn.gdeng.paycenter.server.pay.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.config.annotation.Service;
import com.gudeng.framework.dba.transaction.annotation.Transactional;
import com.gudeng.framework.dba.util.DalUtils;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.order.OrderbaseInfoHessianService;
import cn.gdeng.paycenter.api.server.pay.PayTradeService;
import cn.gdeng.paycenter.constant.NotifyStatus;
import cn.gdeng.paycenter.constant.PayStatus;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.PayTradeDto;
import cn.gdeng.paycenter.dto.pos.OrderBaseinfoHessianDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeExtendEntity;
import cn.gdeng.paycenter.entity.pay.PayTradePosEntity;
import cn.gdeng.paycenter.enums.AppKeyOrderTypeEnum;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.server.pay.util.DBDateTimeUtil;
import cn.gdeng.paycenter.util.web.api.RandomIdGenerator;

@Service
public class PayTradeServiceImpl implements PayTradeService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BaseDao<PayTradeEntity> baseDao;
	
	@Resource
	private OrderbaseInfoHessianService orderbaseInfoHessianService;

	@Override
	public String getPayCenterNumber() {
		Long number = baseDao.queryForObject("PayTrade.queryPayCenterNumber", null, Long.class);
		//

		String id = RandomIdGenerator.randomId(number);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date())+id;
	}


	@Override
	@Transactional
	public void addPayTradeEntity(PayTradeEntity paytrade)throws BizException {

		paytrade.setUpdateTime(new Date());
		paytrade.setUpdateUserId("sys");
		paytrade.setVersion(1);
		paytrade.setPayStatus(PayStatus.READY_PAY);
		if(StringUtils.isEmpty(paytrade.getNotifyStatus())){
			paytrade.setNotifyStatus(NotifyStatus.NO_NOTIFY);//不可通知
		}
		

		Assert.notNull(paytrade.getOrderNo(), "订单号不能为空");
		//先查询订单
		Map<String,Object> map = new HashMap<>();
		map.put("appKey", paytrade.getAppKey());
		map.put("requestNo", paytrade.getRequestNo());
		map.put("orderNo", paytrade.getOrderNo());
		List<PayTradeEntity> list = queryPayTrade(map);
		try {
			if(null == list || list.size()==0){
				paytrade.setCreateUserId("sys");
				paytrade.setCreateTime(new Date());
				baseDao.persistBySign(paytrade);
			} else {
				paytrade.setId(list.get(0).getId());
				baseDao.dynamicMerge(paytrade);
			}
			//写交易扩展表 先判断是否存在
			map.clear();
			map.put("orderNo", paytrade.getOrderNo());
			map.put("appKey", paytrade.getAppKey());
			PayTradeExtendEntity old = baseDao.queryForObject("PayTrade.getPayTradeExtend", map, PayTradeExtendEntity.class);
			if(old == null){
				PayTradeExtendEntity extend = new PayTradeExtendEntity();
				extend.setOrderNo(paytrade.getOrderNo());
				extend.setAppKey(paytrade.getAppKey());
				extend.setOrderType(AppKeyOrderTypeEnum.getCodeByAppKey(paytrade.getAppKey()));
				try {
					OrderBaseinfoHessianDto orderBaseDto = orderbaseInfoHessianService.getByOrderNo(paytrade.getOrderNo());
					extend.setMarketId(orderBaseDto.getMarketId());
					extend.setBusinessId(orderBaseDto.getBusinessId());
					extend.setOrderType(orderBaseDto.getOrderType());
					extend.setBusinessName(orderBaseDto.getShopName());
					extend.setMarketName(orderBaseDto.getMarketName());
					extend.setVersion(orderBaseDto.getValidPosNum());
				} catch (Exception e) {
					//有异常直接忽略，NST，获取不到订单，可能会报错，所以直接忽略 
					if("nsy".equals(paytrade.getAppKey())){
						throw new BizException(MsgCons.C_20001, "从gd-order获取订单信息失败，请稍后重试");
					} else {
						logger.error("获取订单["+paytrade.getOrderNo()+"][appKey="+paytrade.getAppKey()+"]失败，直接忽略");
					}
					
				}
				baseDao.execute("PayTrade.insertPayTradeExtend", extend);
			}
		} catch (Exception e) {
			logger.error("写入支付流水表失败:"+e.getMessage(),e);
			throw new BizException(MsgCons.C_60000, "写入支付流水表失败");
		}

	}

	@Override
	public List<PayTradeEntity> queryPayTrade(Map<String, Object> map) {
		
		List<PayTradeEntity> list = baseDao.queryForList("PayTrade.queryPayTrade", map, PayTradeEntity.class);
//		if(null != list && list.size() > 0){
//			List<Long> ids = new ArrayList<>();
//			for(PayTradeEntity pay : list){
//				ids.add((long)pay.getId());
//			}
//			try {
//				baseDao.batchValidateSign(ids, PayTradeEntity.class);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//			
//		}

		return list;
	}

	@Override
	public void updatePayTrade(PayTradeEntity payTrade){
		Map<String,Object> pa =DalUtils.convertToMap(payTrade);
		//处理时间
		//pa.put("updateTime", paytrade.getUpdateTime());
//		try {
//			queryPayTradeByPayCenterNumber(payTrade.getPayCenterNumber());//验证签名
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
		
		//String uuid = IdCreater.newId();
		pa.put("payTime", DBDateTimeUtil.getDBDateTime(payTrade.getPayTime()));
		pa.put("updateTime", DBDateTimeUtil.getDBDateTime(payTrade.getUpdateTime()));
		pa.put("closeTime", DBDateTimeUtil.getDBDateTime(payTrade.getCloseTime()));
		pa.put("orderTime", DBDateTimeUtil.getDBDateTime(payTrade.getOrderTime()));
		//pa.put("sign",uuid);
	
		baseDao.execute("PayTrade.updateTradeByPayCenterNumber", pa);
//		//更新签名
//		try {
//			baseDao.batchSign(uuid, PayTradeEntity.class);
//		} catch (Exception e) {
//			//logger.error(e.getMessage(),e);
//			throw new RuntimeException(e);
//		}
		
	}
	
	@Override
	public void updateByOrderNo(PayTradeEntity payTrade){
		Map<String,Object> pa =DalUtils.convertToMap(payTrade);
		pa.put("payTime", DBDateTimeUtil.getDBDateTime(payTrade.getPayTime()));
		pa.put("updateTime", DBDateTimeUtil.getDBDateTime(payTrade.getUpdateTime()));
		pa.put("closeTime", DBDateTimeUtil.getDBDateTime(payTrade.getCloseTime()));
		pa.put("orderTime", DBDateTimeUtil.getDBDateTime(payTrade.getOrderTime()));	
		baseDao.execute("PayTrade.updateByOrderNo", pa);
		
	}


	@Override
	public PayTradeEntity getById(Integer id) throws BizException{
		PayTradeEntity entity = new PayTradeEntity();
		entity.setId(id);
		PayTradeEntity res = baseDao.find(entity);
//		try {
//			baseDao.validateSign(res,PayTradeEntity.class);
//		}catch (BizException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			logger.error(e.getMessage(),e);
//			throw new BizException(MsgCons.C_60003, MsgCons.M_60003);
//		}
		
		return res;

	}


	@Override
	public List<PayTradeEntity> queryPayTradeByPayCenterNumber(String payCenterNumber) throws BizException {
		Map<String,Object> param = new HashMap<>();
		param.put("payCenterNumber", payCenterNumber);
		List<PayTradeEntity> list =  queryPayTrade(param);
		if(list == null || list.size() == 0){
			throw new BizException(MsgCons.C_20001, "平台流水号["+payCenterNumber+"]不存在");
		}
//		PayTradeEntity res = list.get(0);
//		try {
//			baseDao.validateSign(res, PayTradeEntity.class);
//		} 
//		catch (BizException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			logger.error(e.getMessage(),e);
//			throw new BizException(MsgCons.C_60003, MsgCons.M_60003);
//		}
		
		return list;
	}


	@Override
	public void addPayTrade(PayTradeDto dto) throws BizException {
		PayTradeEntity entity = new PayTradeEntity();
		BeanUtils.copyProperties(dto, entity);
		try {
			baseDao.persistBySign(entity);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new BizException(MsgCons.C_20000, "写支付流水表失败");
		}
		
		if(null != dto.getPayTradeExtendEntity()){
			PayTradeExtendEntity entity2 = dto.getPayTradeExtendEntity();
			baseDao.execute("PayTrade.insertPayTradeExtend", entity2);
			
		}

		if(null != dto.getPayTradePosEntity()){
			PayTradePosEntity entity2 = dto.getPayTradePosEntity();
			baseDao.execute("PayTrade.insertPayTradePos", entity2);
		}
	}


	@Override
	public void addPayTrade(List<PayTradeEntity> list) throws BizException {
		if(null != list){
			for(PayTradeEntity entity : list){
				addPayTradeEntity(entity);
			}
		}
		
	}


	@Override
	public void deletePayTrade(String appKey,String orderNo,String requestNo) throws BizException {
		//先查询订单
		Map<String,Object> map = new HashMap<>();
		map.put("appKey", appKey);
		map.put("orderNo", orderNo);
		map.put("requestNo", requestNo);
		List<PayTradeEntity> list = queryPayTrade(map);
		if(list == null || list.size() == 0){
			return;
		} else if(list.size() > 1){
			throw new BizException(MsgCons.C_20001, "数据有误appKey=["+appKey+"]orderNo=["+orderNo+"]rquestNo=["+requestNo+"]");
		} else {
			
			String payCenterNumber = list.get(0).getPayCenterNumber();
			deletePayTrade(payCenterNumber);
		}
		
	}


	@Override
	public void deletePayTrade(String payCenterNumber) throws BizException {
		Map<String,Object> map = new HashMap<>();
		map.put("payCenterNumber", payCenterNumber);
		baseDao.execute("PayTrade.deletePayTrade", map);
		baseDao.execute("PayTrade.deletePayTradePos", map);
		
	}
}
