package cn.gdeng.paycenter.server.pay.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pos.MergePayTradeRequestDto;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.MD5;

public class PayTradeUtil {
	
	public static String getStrFromDouble(Double d){
		if(d == null){
			return "";
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(d);
	}
	
	public static String getPayUuid(MergePayTradeRequestDto dto){
		List<String> orders = new ArrayList<>();
		
		for(MergePayTradeRequestDto.OrderInfo orderInfo : dto.getOrderInfoList()){

			orders.add(orderInfo.getOrderNo()+"-"+orderInfo.getRequestNo());
		}
		Collections.sort(orders);//排序
		String payUID = MD5.crypt(Arrays.toString(orders.toArray()));
		return payUID;
	}
	
//	public static void validateDecialmal(double d,int length,String msg)throws BizException{
//		String str = d+"";
//		System.out.println(str);
//		int ind = str.indexOf(".");
//		if(ind >-1){
//			if(str.substring(ind+1).length()>length){
//				throw new BizException(MsgCons.C_20001, msg);
//			};
//		}
//	}
	public static void main(String[] args) throws BizException {
		//validateDecialmal(11111111d,2,"222");
		validateDecialmal(11111111111.11d);
	}
	
	public static void validateDecialmal(String str,int length,String msg)throws BizException{
		if(StringUtils.isEmpty(str)){
			return;
		}
		int ind = str.indexOf(".");
		if(ind >-1){
			if(str.substring(ind+1).length()>length){
				throw new BizException(MsgCons.C_20001, msg);
			};
		}
	}
	
	/**
	 * 校验double 是否只有两位小数
	 * @param dt
	 * @param msg
	 * @throws BizException
	 */
	public static void validateDecialmal(double dt)throws BizException{
		DecimalFormat df = new DecimalFormat("#0");
		double temp = dt*100;
		String d1 = df.format(temp);
		if(Double.valueOf(d1) != temp){
			throw new BizException(MsgCons.C_20001, "最多只能有两位小数");

		}
	}

	/**
	 * 保留两位小数
	 * @param list
	 * @return
	 */
	public static double getSumPayAmt(List<PayTradeEntity> list){
		BigDecimal big = new BigDecimal(0);
		for(PayTradeEntity old : list){
			big = big.add(new BigDecimal(old.getPayAmt()));
		}
		big.setScale(2, BigDecimal.ROUND_HALF_UP);
		return big.doubleValue();
	}
	
	public static String getOrderNos(List<PayTradeEntity> list){
		StringBuilder sb = new StringBuilder();
		for(PayTradeEntity old : list){
			sb.append(old.getOrderNo()).append(",");
		}
		
		return sb.substring(0, sb.length()-1);
	}
}
