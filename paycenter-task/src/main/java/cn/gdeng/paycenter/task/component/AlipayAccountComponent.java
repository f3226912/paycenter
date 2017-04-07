package cn.gdeng.paycenter.task.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.api.server.pay.AlipayAccountService;
import cn.gdeng.paycenter.dto.account.AccountQueryAccountLogVO;
import cn.gdeng.paycenter.dto.account.AccountRquestDto;
import cn.gdeng.paycenter.dto.account.AlipayAccountPageDto;
import cn.gdeng.paycenter.dto.alipay.AlipayAccountRequest;
import cn.gdeng.paycenter.dto.pay.BillCheckDetailDto;
import cn.gdeng.paycenter.dto.pay.BillChecks;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.task.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.AlipaySignUtil;

@Component
public class AlipayAccountComponent {
	
	@Resource	
	private AlipayAccountService<AlipayAccountRequest> alipayAccountService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * 按天来查
	 * @param appKey
	 * @param billDate  yyyy-MM-dd
	 * @return
	 */
	public BillChecks getAccount(AccountRquestDto dto){
		logger.info("调用阿里对帐接口，入参:"+JSONObject.toJSONString(dto));
		BillChecks check = new BillChecks();
		AlipayAccountPageDto page = _getAccount(dto);
		check.setHasNext(false);
		if("T".equals(page.getHas_next_page())){
			check.setHasNext(true); 
		}
		List<AccountQueryAccountLogVO> list = page.getAccount_log_list();
		List<BillCheckDetailDto> tlist = new ArrayList<>();
		
		for(AccountQueryAccountLogVO vo : list){
			BillCheckDetailDto bill = new BillCheckDetailDto();
			bill.setBillInfo(vo.getXml());//XML
			bill.setGoodsName(vo.getGoods_title());
			bill.setPayCenterNumber(vo.getMerchant_out_order_no());
			bill.setPayAmt(Double.valueOf(vo.getTotal_fee()));
			bill.setThirdPayNumber(vo.getTrade_no());
			bill.setTrans_date(getSimpleDate(vo.getTrans_date()));
			//
			if(StringUtils.isNotEmpty(vo.getRate())){
				bill.setRate(Double.valueOf(vo.getRate()));
			}
	
			tlist.add(bill);
		}
		check.setBillDetail(tlist);
		
		return check;
	}

	/**
	 * 按天来查
	 * @param appKey
	 * @param billDate  yyyy-MM-dd
	 * @return
	 */
	public AlipayAccountPageDto _getAccount(AccountRquestDto dto){
		try {
			validateDto(dto);
			AlipayAccountRequest req = new AlipayAccountRequest();
			req.setAppKey(dto.getAppKey());
			if(StringUtils.isNotEmpty(dto.getBillDate())){
				Date bd = getBillDate(dto.getBillDate());
				bd.setHours(0);
				bd.setMinutes(0);
				bd.setSeconds(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				req.setGmt_start_time(sdf.format(bd));
				
				bd.setDate(bd.getDate()+1);
				req.setGmt_end_time(sdf.format(bd));
			}
			req.setMerchant_out_order_no(dto.getPayCenterNumber());
			
			req = alipayAccountService.getAccountRequest(req);
			Map<String,String> param = req.buildMap();

			logger.info("调用阿里对账接口,地址:"+AlipaySignUtil.buildUrl(req.getGateWay(), param));
			String xmlMsg = HttpUtil.httpClientPost(req.getGateWay(), param);
			logger.info("调用阿里对账接口，返回xml报文:"+xmlMsg);
			AlipayAccountPageDto pageDto = parseAlipayAccountXml(dto.getAppKey(),xmlMsg);
			logger.info(JSON.toJSONString(pageDto));
			return pageDto;
		} catch (BizException e) {
			logger.error("调用阿里对账失败"+e.getMsg(),e);
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * xml结构
	 * 	<alipay>
  			<is_success>T</is_success>
  			<error>ILLEGAL_SIGN</error> 失败会有
  			<request>
  				<param name="sign">03458b197bc81fefcd915d55d39031e3</param>
  			</request>
  			<response>
  				<account_page_query_result>
  					<account_log_list>
  						<AccountQueryAccountLogVO>
  							<balance>5.77</balance>
  							<goods_title>大白菜</goods_title>
  						</AccountQueryAccountLogVO>
  					</account_log_list>
  					<has_next_page>F</has_next_page>
  					<page_no>1</page_no>
  					<page_size>5000</page_size>
  				</account_page_query_result>
  			</response>
  			<sign>5c707f81606ccd972be05fcced66875e</sign>
  			<sign_type>MD5</sign_type>
  		</alipay>
	 * @param xmlMsg
	 */
	private AlipayAccountPageDto parseAlipayAccountXml(String appKey,String xmlMsg){
		
		try {
			AlipayAccountPageDto dto = new AlipayAccountPageDto();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new ByteArrayInputStream(xmlMsg.getBytes("GBK")));
			//获取根节点
			Element root = doc.getRootElement();
			
			String isSuccess = root.element("is_success").getText();
			if(StringUtils.equals(isSuccess, "T")){
				//签名验证
				Map<String,String> para = new HashMap<>();
				para.put("sign_type", root.elementText("sign_type"));
				para.put("sign", root.elementText("sign"));
				//获取response/account_page_query_result 节点
				Element accEle = root.element("response").element("account_page_query_result");
				para.put("has_next_page", accEle.elementText("has_next_page"));
				para.put("page_no", accEle.elementText("page_no"));
				para.put("page_size", accEle.elementText("page_size"));
				para.put("account_log_list", accEle.element("account_log_list").asXML());
				//调用服务验签
				//OutputFormat format = new OutputFormat();
				//format.setExpandEmptyElements(true);
				//StringWriter accountLogListContent = new StringWriter();
				//XMLWriter writer = new XMLWriter(accountLogListContent, format);
				//writer.write(accEle.element("account_log_list"));
				//para.put("account_log_list", accountLogListContent.toString());
				if(alipayAccountService.signResult(appKey, para)){
					//解析账务明细
					List<AccountQueryAccountLogVO> list = parseAccoutLogVo(accEle.element("account_log_list"));
					//设置值
					dto.setIs_success("T");
					dto.setHas_next_page(accEle.elementText("has_next_page"));
					dto.setPage_no(accEle.elementText("page_no"));
					dto.setPage_size(accEle.elementText("page_size"));
					dto.setAccount_log_list(list);
					//dto.setXml(xmlMsg);
					return dto;
				} else {
					throw new RuntimeException("对账务结果验签失败,请检查密钥");
				}
			} else {
				String error = root.element("error").getText();
				throw new RuntimeException("调用阿里账务失败，错误码:"+error);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException("不支持utf-8编码",e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException("文档异常",e);
		} catch (BizException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException(e.getCode()+","+e.getMsg(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
	}
	
	private List<AccountQueryAccountLogVO> parseAccoutLogVo(Element ele){
		List<AccountQueryAccountLogVO> list = new ArrayList<>();
		Iterator<Element> it = ele.elementIterator();
		while(it.hasNext()){
			Element voEle = it.next();
			AccountQueryAccountLogVO vo = new AccountQueryAccountLogVO();
			vo.setBalance(voEle.elementText("balance"));
			vo.setIncome(voEle.elementText("income"));
			vo.setOutcome(voEle.elementText("outcome"));
			vo.setTrans_date(voEle.elementText("trans_date"));
			vo.setTrans_code_msg(voEle.elementText("trans_code_msg"));
			vo.setMerchant_out_order_no(voEle.elementText("merchant_out_order_no"));
			vo.setBank_name(voEle.elementText("bank_name"));
			vo.setBank_account_no(voEle.elementText("bank_account_no"));
			vo.setBank_account_name(voEle.elementText("bank_account_name"));
			vo.setBuyer_account(voEle.elementText("buyer_account"));
			vo.setSeller_account(voEle.elementText("seller_account"));
			vo.setSeller_fullname(voEle.elementText("seller_fullname"));
			vo.setGoods_title(voEle.elementText("goods_title"));
			vo.setPartner_id(voEle.elementText("partner_id"));
			vo.setTotal_fee(voEle.elementText("total_fee"));
			vo.setTrade_no(voEle.elementText("trade_no"));
			vo.setTrade_refund_amount(voEle.elementText("trade_refund_amount"));
			vo.setSign_product_name(voEle.elementText("sign_product_name"));
			vo.setRate(voEle.elementText("rate"));
			vo.setXml(voEle.asXML());
			list.add(vo);
		}
		return list;
	}
	
	private Date getBillDate(String billDate) throws BizException{
		if(null == billDate || "".equals(billDate)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(billDate);
			return date;
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20026, "billDate["+billDate+"]格式不正确,应为yyyy-MM-dd");
		}
	}
	
	private void validateDto(AccountRquestDto dto) throws BizException{
		if(StringUtils.isEmpty(dto.getAppKey())){
			throw new BizException(MsgCons.C_20001, "appKey不能为空");
		}
		if(StringUtils.isEmpty(dto.getBillDate()) && StringUtils.isEmpty(dto.getPayCenterNumber())){
			throw new BizException(MsgCons.C_20001, "billDate和payCenterNumber不能同时为空");
		}
		if(dto.getBillDate() == null){
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.parse(dto.getBillDate());
		} catch (Exception e) {
			throw new BizException(MsgCons.C_20026, "billDate["+dto.getBillDate()+"]格式不正确,应为yyyy-MM-dd");
		}
	}
	
	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			// 定义要解析的XML字符串
			String transMessage = "<?xml version=\"1.0\" encoding=\"GBK\"?><message>" + "<body>" + "<ticketNotify>"
					+ "<ticket id=\"6000012007051000000231\" dealTime=\"20070510165423\" status=\"0000\" message=\"成功,系统处理正常\"/>"
					+ "<ticket id=\"6000012007051000000232\" dealTime=\"20070510165424\" status=\"2012\" message=\"禁止倍投\"/>"
					+ "</ticketNotify>" + "</body>"
					+ "<a><sub_trans_code_msg>快速支付,支付给个人，支付宝帐户全额</sub_trans_code_msg></a></message>";
			// 创建xml解析对象
			SAXReader reader = new SAXReader();
			// 定义一个文档
			Document document = null;
			// 将字符串转换为
			document = reader.read(new ByteArrayInputStream(transMessage.getBytes("GBK")));
			// 得到xml的根节点(message)
			Element root = document.getRootElement();
			// 定义子循环体的变量
			Element ticket = null;
			//List ls = document.selectNodes("message/body");
			
			System.out.println(root.element("a").elementText("sub_trans_code_msg"));
			// Element.asXML方法，获得包括该标签的所有XML数据
			System.out.println(root.element("body").asXML());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Date getSimpleDate(String date){
		if(null == date || "".equals(date)){
			return null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException("将["+date+"]转换成时间出错");
			}
		}
	}
}
