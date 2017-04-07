package cn.gdeng.paycenter.server.pay.component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayAccountService;
import cn.gdeng.paycenter.api.server.pay.ThirdPayConfigService;
import cn.gdeng.paycenter.api.server.pay.WeChatAccountService;
import cn.gdeng.paycenter.constant.SubPayType;
import cn.gdeng.paycenter.dto.account.AccountQueryAccountLogVO;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.AlipayAccountPageDto;
import cn.gdeng.paycenter.dto.account.WeChatAccountParams;
import cn.gdeng.paycenter.dto.account.WeChatAccountRequest;
import cn.gdeng.paycenter.dto.account.WeChatAccountResponse;
import cn.gdeng.paycenter.dto.alipay.AlipayAccountRequest;
import cn.gdeng.paycenter.dto.pay.BillCheckDetailDto;
import cn.gdeng.paycenter.dto.pay.BillChecks;
import cn.gdeng.paycenter.entity.pay.ThirdPayConfigEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.enums.PayWayEnum;
import cn.gdeng.paycenter.server.pay.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;
import cn.gdeng.paycenter.util.web.api.StringUtil;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil;
import cn.gdeng.paycenter.util.web.api.WeChatSignUtil;
import cn.gdeng.paycenter.util.web.api.AlipayConfigUtil.AlipayConfig;
import cn.gdeng.paycenter.util.web.api.WeChatConfigUtil.WeChatConfig;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

@Component
public class WeChatAccountComponent {

	@Resource
	private WeChatAccountService<WeChatAccountRequest> wechatAccountService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 按天来查
	 * 
	 * @param appKey
	 * @param billDate
	 *            yyyyMMdd
	 * @return
	 */
	public BillChecks getAccount(AccountRquestDto dto) {
		logger.info("调用微信对帐接口，入参:" + JSONObject.toJSONString(dto));
		BillChecks check = new BillChecks();
		WeChatAccountRequest req = _getAccount(dto);

		List<WeChatAccountResponse> list = req.getResponseList();
		List<BillCheckDetailDto> tlist = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (WeChatAccountResponse vo : list) {
				BillCheckDetailDto bill = new BillCheckDetailDto();
				bill.setBillInfo(vo.getInfo());
				bill.setGoodsName(vo.getTitle());
				bill.setPayCenterNumber(vo.getOutTradeNo());
				bill.setPayAmt(Double.valueOf(vo.getTotalFee()));
				bill.setThirdPayNumber(vo.getTransactionId());
				bill.setTrans_date(getSimpleDate(vo.getTradeDate()));
				//
				if (StringUtils.isNotEmpty(vo.getRate())) {
					bill.setRate(Double.valueOf(vo.getRate().replace("%","")));
				}
				if(StringUtils.isNotBlank(vo.getFee())){
					bill.setFeeAmt(Double.valueOf(vo.getFee()));
				}

				tlist.add(bill);
			}
			check.setBillDetail(tlist);
		}
		return check;
	}

	/**
	 * 按天来查
	 * 
	 * @param appKey
	 * @param billDate
	 *            yyyyMMdd
	 * @return
	 * @throws Exception
	 */
	public WeChatAccountRequest _getAccount(AccountRquestDto dto) {
		WeChatAccountRequest request = null;
		try {
			validateDto(dto);
			request = wechatAccountService.getAccountRequest(dto);
			String requestStr = createXmlParams(request.getParams());
			logger.info("调用微信对账接口,地址:" + request.getUrl() + " xml参数：" + requestStr);
			String reponseText = HttpUtil.httpClientPost(request.getUrl(), requestStr);
			if (StringUtils.isEmpty(reponseText)) {
				throw new BizException(MsgCons.C_50000, "调用微信下载对账单接口服务失败");
			}
			logger.info("调用微信对账接口，返回报文:" + reponseText);
			if (reponseText.startsWith(request.getReponseStart())) {
				setFailDetail(request, reponseText);
			} else {
				parseReponseText(request, reponseText);
			}
		} catch (BizException e) {
			logger.error("调用微信对账失败" + e.getMsg(), e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("调用微信对账失败" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return request;
	}

	public void setFailDetail(WeChatAccountRequest req, String reponseText) throws Exception {
		try {
			Document doc = DocumentHelper.parseText(reponseText);
			Element root = doc.getRootElement();
			req.setReturnCode(root.element("return_code").getText());
			req.setReturnMsg(root.element("return_msg").getText());
		} catch (DocumentException e) {
			throw new BizException(MsgCons.C_20040, e.getMessage());
		}

	}

	public WeChatAccountRequest parseReponseText(WeChatAccountRequest request, String reponseText) throws BizException {

		StringReader sr = new StringReader(reponseText);
		String line;
		BufferedReader br = null;
		try {
			br = new BufferedReader(sr);
			boolean isFirst = true;
			List<WeChatAccountResponse> logList = new ArrayList<WeChatAccountResponse>();
			while ((line = br.readLine()) != null) {
				// 表头
				// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
				// 交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,代金券或立减优惠金额,微信退款单号,商户退款单号,退款金额,代金券或立减优惠退款金额，退款类型，退款状态,商品名称,商户数据包,手续费,费率
				if (!isFirst) {
					WeChatAccountResponse log = new WeChatAccountResponse();
					String[] cols = line.split(",");
					if (cols != null && cols.length == request.getColsLength()) {
						log.setInfo(line);
						log.setTradeDate(cols[0].substring(1));
						log.setTitle(cols[20].substring(1));
						log.setTotalFee(cols[12].substring(1));
						log.setTransactionId(cols[5].substring(1));
						log.setOutTradeNo(cols[6].substring(1));
						log.setAppId(cols[1].substring(1));
						log.setMchId(cols[2].substring(1));
						log.setRate(cols[23].substring(1));
						log.setFee(cols[22].substring(1));
						logList.add(log);
					}
				} else {
					isFirst = false;
				}
			}
			request.setResponseList(logList);

		} catch (IOException e) {
			logger.error("微信对账响应文本解析失败" + e.getMessage(), e);
			throw new BizException(MsgCons.C_20041, e.getMessage());
		} finally {
			if (sr != null) {
				sr.close();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return request;
	}

	public String createXmlParams(WeChatAccountParams params) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		BeanInfo bean = Introspector.getBeanInfo(WeChatAccountParams.class);
		PropertyDescriptor[] descriptors = bean.getPropertyDescriptors();
		for (PropertyDescriptor property : descriptors) {
			Method method = property.getReadMethod();
			String name = property.getName();
			Object value = method.invoke(params);
			if (value instanceof String) {
				sb.append("<" + name + "><![CDATA[" + value + "]]></" + name + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public void validateDto(AccountRquestDto dto) throws BizException {
		if (StringUtils.isEmpty(dto.getAppKey())) {
			throw new BizException(MsgCons.C_20001, "appKey不能为空");
		}
		if (StringUtils.isEmpty(dto.getBillDate())) {
			throw new BizException(MsgCons.C_20001, "对账日期不能为空");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			sdf.parse(dto.getBillDate());
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20026, "billDate[" + dto.getBillDate() + "]格式不正确,应为yyyyMMdd");
		}
	}

	private Date getSimpleDate(String date) {
		if (null == date || "".equals(date)) {
			return null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException("将[" + date + "]转换成时间出错");
			}
		}
	}
}
