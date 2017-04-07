package cn.gdeng.paycenter.server.pay.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AccessSysConfigService;
import cn.gdeng.paycenter.api.server.pay.AccessSysSignService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;

/**
 * 接入验证服务
 *
 */
@Service
public class AccessSysSignServiceImpl implements AccessSysSignService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private AccessSysConfigService accessSysConfigService;
	
	@Resource
	private ThirdPayConfigService thirdPayConfigService;
	
	private static double MAX_AMT = 100000000.00;
	
	public void accessSysSign(Map<String , String> paramMap,String sign) throws BizException{
		//输入参数验证
		validate(paramMap);
		
		//验证appKey是否存在
		AccessSysConfigEntity asce = accessSysConfigService.queryByAppKey(paramMap.get("appKey"));
		if(asce == null){
			logger.info("系统接入appKey:" + paramMap.get("appKey") + "不存在");
			throw new BizException(MsgCons.C_20002, MsgCons.M_20002);
		}
		//验证接入系统参数签名正确性			
		boolean isVerify = AccessSysSignUtil.verifySign(paramMap, sign, asce);		
		if(!isVerify){
			logger.info("系统接入appKey:" + paramMap.get("appKey") + "签名" + sign + "验签失败");
			throw new BizException(MsgCons.C_20003, MsgCons.M_20003);	
		}		
	}
	
	private void validate(Map<String , String> paramMap) throws BizException{
		if(StringUtils.isEmpty(paramMap.get("appKey"))){
			logger.info("系统接入appKey为空");
			throw new BizException(MsgCons.C_20011, MsgCons.M_20011);
		}

		if(StringUtils.isEmpty(paramMap.get("timeOut"))){
			logger.info("系统接入appKey:" + paramMap.get("appKey") + " timeOut参数为空");
			throw new BizException(MsgCons.C_20013, MsgCons.M_20013);
		}
		if(StringUtils.isEmpty(paramMap.get("requestIp"))){
			logger.info("系统接入appKey:" + paramMap.get("appKey") + " requestIp参数为空");
			throw new BizException(MsgCons.C_20021, MsgCons.M_20021);
		}	
		//合并付款，不校验
		if(StringUtils.hasLength(paramMap.get("orderInfos"))){
			if(StringUtils.isEmpty(paramMap.get("sumPayAmt"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + "sumPayAmt参数为空");
				throw new BizException(MsgCons.C_20001, "sumPayAmt参数为空");
			}
			double sumPayAmt = 0;
			try{
				sumPayAmt = Double.parseDouble(paramMap.get("sumPayAmt"));
				if(sumPayAmt<= 0){
					throw  new BizException(MsgCons.C_20001, "sumPayAmt参数错误");
				}
				
			}catch(Exception e){
				throw new BizException(MsgCons.C_20001, "sumPayAmt参数错误");
			}
			if(sumPayAmt>MAX_AMT){
				throw  new BizException(MsgCons.C_20001, "sumPayAmt参数错误");
			}
			validateDecialmal(paramMap.get("sumPayAmt"),2,"交易金额最多有两位小数");
		} else {
			if(StringUtils.isEmpty(paramMap.get("orderNo"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " orderNo参数为空");
				throw new BizException(MsgCons.C_20012, MsgCons.M_20012);
			}
			if(StringUtils.isEmpty(paramMap.get("title"))){			
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " title参数为空");
				throw new BizException(MsgCons.C_20034, MsgCons.M_20034);
			}
			if(StringUtils.isEmpty(paramMap.get("payerUserId"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " payerUserId参数为空");
				throw new BizException(MsgCons.C_20014, MsgCons.M_20014);
			}
			//供应商金牌会员没有传,爱兵已添加
			if(StringUtils.isEmpty(paramMap.get("payeeUserId"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " payeeUserId参数为空");
				throw new BizException(MsgCons.C_20015, MsgCons.M_20015);
			}

			if(StringUtils.isEmpty(paramMap.get("totalAmt"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " totalAmt参数为空");
				throw new BizException(MsgCons.C_20017, MsgCons.M_20017);
			}
			double tamt = 0;
			try{
				tamt = Double.parseDouble(paramMap.get("totalAmt"));
				
			}catch(Exception e){
				throw new BizException(MsgCons.C_20022, "totalAmt不是一个数字");
			}
			if(tamt>MAX_AMT){
				throw  new BizException(MsgCons.C_20022, "totalAmt过大");
			}
			
//			if(Double.parseDouble(paramMap.get("totalAmt")) <= 0){
//				throw new BizException(MsgCons.C_20022, MsgCons.M_20022);
//			}
			
			if(StringUtils.isEmpty(paramMap.get("payAmt"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " payAmt参数为空");
				throw new BizException(MsgCons.C_20018, MsgCons.M_20018);
			}
			
			double pamt = 0;
			try{
				pamt = Double.parseDouble(paramMap.get("payAmt"));
			}catch(Exception e){
				throw new BizException(MsgCons.C_20023, "payAmt不是一个数字");
			}
			if(pamt>MAX_AMT){
				throw new BizException(MsgCons.C_20023, "支付金额过大");
			}
			
			if(Double.parseDouble(paramMap.get("payAmt")) <= 0){
				throw new BizException(MsgCons.C_20023, "支付金额应该大于0");
			}
			validateDecialmal(paramMap.get("payAmt"),2,"交易金额最多有两位小数");
			
			if(StringUtils.isEmpty(paramMap.get("returnUrl"))){
				logger.info("系统接入appKey:" + paramMap.get("appKey") + " returnUrl参数为空");
				throw new BizException(MsgCons.C_20019, MsgCons.M_20019);
			}
			
			String requestNo = paramMap.get("rquestNo");
			if(StringUtils.hasText(requestNo)){
				String payCount = paramMap.get("payCount");
				if(!StringUtils.hasLength(payCount)){
					throw new BizException(MsgCons.C_20001, "payCount不能为空");
				}
			}
		}

	}
	
	private void validateDecialmal(String str,int length,String msg)throws BizException{
		int ind = str.indexOf(".");
		if(ind >-1){
			if(str.substring(ind+1).length()>length){
				throw new BizException(MsgCons.C_20001, msg);
			};
		}
	}
}
