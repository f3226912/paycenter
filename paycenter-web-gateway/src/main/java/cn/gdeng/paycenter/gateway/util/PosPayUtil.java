package cn.gdeng.paycenter.gateway.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.GuangxiPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.NanningPayNotifyDto;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.server.MathUtil;

public class PosPayUtil {

	public static PosPayNotifyDto build4Nanning(NanningPayNotifyDto dto) throws BizException{
		PosPayNotifyDto newDto = new PosPayNotifyDto();
		newDto.setOrderNo(dto.getOrderno());
		newDto.setOrderAmt(0d);
		newDto.setPayAmt(0d);
		newDto.setRateAmt(0d);
		//单位分转成元
		if(!StringUtils.isEmpty(dto.getOrderfee())){
			newDto.setOrderAmt(MathUtil.div(Double.valueOf(dto.getOrderfee()), 100.0));
		}
		if(StringUtils.isNotEmpty(dto.getPayfee())){
			newDto.setPayAmt(MathUtil.div(Double.valueOf(dto.getPayfee()), 100.0));
		}
		if(StringUtils.isNotEmpty(dto.getRatefee())){
			newDto.setRateAmt(MathUtil.div(Double.valueOf(dto.getRatefee()), 100.0));
		}
		newDto.setBusinessNo(dto.getMerchantnum());
		newDto.setBankCardNo(dto.getPaycardno());
		
		newDto.setPosClientNo(dto.getMachinenum());
		try {
			newDto.setTransDate(DateUtils.parseDate(dto.getTransdate() + dto.getTranstime(), 
					"yyyyMMddHHmmss"));
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20000, "解析交易日期失败");
		}
		newDto.setTransNo(dto.getTransseqno());
		newDto.setTransType(dto.getTransype());
		newDto.setAppKey(dto.getAppKey());
		return newDto;
	}
	
	public static PosPayNotifyDto build4Guangxi(GuangxiPayNotifyDto dto) throws BizException{
		PosPayNotifyDto newDto = new PosPayNotifyDto();
		newDto.setOrderNo(dto.getOrderno());
		newDto.setOrderAmt(0d);
		newDto.setPayAmt(0d);
		newDto.setRateAmt(0d);
		//单位分转成元
		if(!StringUtils.isEmpty(dto.getOrderfee())){
			newDto.setOrderAmt(MathUtil.div(Double.valueOf(dto.getOrderfee()), 100.0));
		}
		if(StringUtils.isNotEmpty(dto.getPayfee())){
			newDto.setPayAmt(MathUtil.div(Double.valueOf(dto.getPayfee()), 100.0));
		}
		if(StringUtils.isNotEmpty(dto.getRatefee())){
			newDto.setRateAmt(MathUtil.div(Double.valueOf(dto.getRatefee()), 100.0));
		}
		newDto.setBusinessNo(dto.getMerchantnum());
		newDto.setBankCardNo(dto.getPaycardno());
		
		newDto.setPosClientNo(dto.getMachinenum());
		try {
			newDto.setTransDate(DateUtils.parseDate(dto.getTransdate() + dto.getTranstime(), 
					"yyyyMMddHHmmss"));
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20000, "解析交易日期失败");
		}
		newDto.setTransNo(dto.getTransseqno());
		newDto.setTransType(dto.getTransype());
		newDto.setAppKey(dto.getAppKey());
		return newDto;
	}
}
