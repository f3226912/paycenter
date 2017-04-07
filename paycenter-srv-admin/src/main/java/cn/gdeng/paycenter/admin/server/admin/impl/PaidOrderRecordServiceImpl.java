package cn.gdeng.paycenter.admin.server.admin.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.gdeng.paycenter.admin.dto.admin.PaidOrderRecordDTO;
import cn.gdeng.paycenter.admin.service.admin.PaidOrderRecordService;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;
import cn.gdeng.paycenter.enums.OrderTypeEnum;
import cn.gdeng.paycenter.enums.UserTypeEnum;
import cn.gdeng.paycenter.util.server.MathUtil;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 代支付订单记录
 * 
 * @author xiaojun
 *
 */
@Service
public class PaidOrderRecordServiceImpl implements PaidOrderRecordService {
	@Autowired
	private BaseDao<?> baseDao;
	/**
	 * 验证签名失败常量
	 */
	private static final boolean VERIFY_FALSE=false;
	/**
	 * 验证签名成功常量
	 */
	private static final boolean VERIFY_TRUE=true;

	@Override
	public List<PaidOrderRecordDTO> queryPaidOrderRecordList(Map<String, Object> map) throws Exception {
		// 获取结果集
		List<PaidOrderRecordDTO> list = baseDao.queryForList("PaidRecord.queryPaidOrderRecordList", map,
				PaidOrderRecordDTO.class);
		// 处理结果集
		handleResult(list);
		return list;
	}
	@Override
	public Boolean isVerifySignature(List<PaidOrderRecordDTO> list) throws Exception {
		if (list==null || (list!=null && list.size()==0)) {
			return VERIFY_TRUE;
		}
		//pay_trade 
		List<Long> payTradeIds=new ArrayList<>();
		//remit_record
		List<Long> remitRecordIds=new ArrayList<>();
		for (PaidOrderRecordDTO pord : list) {
			if (pord.getId()!=null) {
				payTradeIds.add(Long.valueOf(pord.getId().longValue()));
			}
			if (pord.getRemitRecordId()!=null) {
				remitRecordIds.add(Long.valueOf(pord.getRemitRecordId().longValue()));
			}
		}
		List<Long> errorPayTradeIds=new ArrayList<>();
		if (payTradeIds.size()!=0) {
			errorPayTradeIds=baseDao.batchValidateSign(payTradeIds, PayTradeEntity.class);
		}
		List<Long> errorRemitRecordIds=new ArrayList<>();
		if (remitRecordIds.size()!=0) {
			errorPayTradeIds=baseDao.batchValidateSign(remitRecordIds, RemitRecordEntity.class);
		}
		if (errorPayTradeIds.size()!=0 || errorRemitRecordIds.size()!=0) {
			return VERIFY_FALSE;
		}
		return VERIFY_TRUE;
	}

	@Override
	public Integer queryPaidOrderRecordListTotal(Map<String, Object> map) throws BizException {
		Integer toatl = baseDao.queryForObject("PaidRecord.queryPaidOrderRecordListTotal", map, Integer.class);
		return toatl;
	}

	/**
	 * 对结果集进行处理
	 * 
	 * @param list
	 */
	private void handleResult(List<PaidOrderRecordDTO> list) {
		for (PaidOrderRecordDTO dto : list) {
			calculatePaidAmt(dto);
			dto.setUserTypeStr(UserTypeEnum.getNameByCode(dto.getUserType()));
			dto.setOrderTypeStr(OrderTypeEnum.getNameByCode(dto.getOrderType()));
		}
	}

	/**
	 * 计算代付款金额 代付款金额=订单金额-谷登代收手续费-平台服务佣金+补贴
	 */
	private void calculatePaidAmt(PaidOrderRecordDTO dto) {
		// 应付
		Double shouldPay = MathUtil.add(Double.valueOf(dto.getTotalAmt()), Double.valueOf(dto.getSubsidyAmt()));
		// 应收
		Double shouldRece = MathUtil.add(MathUtil.add(Double.valueOf(dto.getFeeAmt()), Double.valueOf(dto.getCommissionAmt())), dto.getPlatCommissionAmt());
		// 代付款金额 (应付-应收)
		Double paidAmt = MathUtil.sub(shouldPay, shouldRece);
		// 设置代付款金额 保留2位小数
		DecimalFormat df=new DecimalFormat("0.00");
		dto.setPaidAmt(df.format(paidAmt));
	}

}
