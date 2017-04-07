package cn.gdeng.paycenter.dto.alipay;

import org.apache.commons.lang3.StringUtils;

/**
 * 交易查询请求DTO
 * @author sss
 *
 */
public class AlipayOpenTradeQueryRequest  extends AlipayOpenRequest{


	private static final long serialVersionUID = 1L;
	
	private String method="alipay.trade.query";

	private String out_trade_no;
	
	private String trade_no;
	
	private String biz_content;

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	
	public void setMethod(String method) {
		this.method = method;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	@Override
	public String getBiz_content() {
		if(this.biz_content == null){
			String nullString = "";
			if(!StringUtils.isEmpty(out_trade_no)){
				nullString += ",\"out_trade_no\":\""+out_trade_no+"\"";
			}
			if(!StringUtils.isEmpty(trade_no)){
				nullString += ",\"trade_no\":\""+trade_no+"\"";
			}
			if(nullString.length()>0){
				nullString = nullString.substring(1);
			}
			return "{"+nullString+"}";
		}
		return this.biz_content;
	}


	public String getOut_trade_no() {
		return out_trade_no;
	}


	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}


	public String getTrade_no() {
		return trade_no;
	}


	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	
	
}
