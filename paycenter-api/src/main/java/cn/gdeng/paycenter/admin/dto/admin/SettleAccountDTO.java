package cn.gdeng.paycenter.admin.dto.admin;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;
import cn.gdeng.paycenter.entity.pay.RemitRecordErrorEntity;
import cn.gdeng.paycenter.enums.SettleAccountStatusEnum;

public class SettleAccountDTO extends RemitRecordEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4661776264253521109L;

	/**
	 * 转结算时间
	 */
	private String changeTimeBeginTime;
	
	private String changeTimeEndTime;
	/**
	 * 代付时间
	 */
	private String payTimeBeginTime;
	
	private String payTimeEndTime;
	
	/**
	 * 异常标记次数
	 */
	private Integer exceptionMackCount;
	
	/**
	 * 用户账号
	 */
	private String account;
	
	/**
	 * 收款方手机
	 */
	private String mobile;
	
	/**
	 * 持卡人姓名
	 */
	private String realName;
	
	/**
	 * 代付时间
	 */
	private String generationPayTime;
	
	/**
	 * 第三方支付流水
	 */
	private String thirdPayNumber;
	
	/**
	 * 异常标记集合
	 */
	private List<RemitRecordErrorEntity> listError;
	
	/**
	 * 持卡人姓名
	 */
	private String bankCardRealName;
	
	/**
	 * 商铺ID
	 */
	private String businessId;
	/**
	 * 商铺名称
	 */
	private String businessName;
	/**
	 * 所属市场
	 */
	private String marketName;
	
	/**
	 * 总金额
	 */
	private Double paidAmt;
	
	/**
	 * 用户银行卡号
	 */
	private String userBankCardNo;
	
	/**
	 * 开户地址
	 */
	private String address;
	
	/**
	 * 付款方银行卡号
	 * */
	private String payerBankCardNo;
	
	public String getChangeTimeBeginTime() {
		return changeTimeBeginTime;
	}

	public void setChangeTimeBeginTime(String changeTimeBeginTime) {
		this.changeTimeBeginTime = changeTimeBeginTime;
	}

	public String getChangeTimeEndTime() {
		return changeTimeEndTime;
	}

	public void setChangeTimeEndTime(String changeTimeEndTime) {
		this.changeTimeEndTime = changeTimeEndTime;
	}

	public String getPayTimeBeginTime() {
		return payTimeBeginTime;
	}

	public void setPayTimeBeginTime(String payTimeBeginTime) {
		this.payTimeBeginTime = payTimeBeginTime;
	}

	public String getPayTimeEndTime() {
		return payTimeEndTime;
	}

	public void setPayTimeEndTime(String payTimeEndTime) {
		this.payTimeEndTime = payTimeEndTime;
	}

	public Integer getExceptionMackCount() {
		return exceptionMackCount;
	}

	public void setExceptionMackCount(Integer exceptionMackCount) {
		this.exceptionMackCount = exceptionMackCount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public List<RemitRecordErrorEntity> getListError() {
		return listError;
	}

	public void setListError(List<RemitRecordErrorEntity> listError) {
		this.listError = listError;
	}

	public String getGenerationPayTime() {
		if(generationPayTime!=null&&generationPayTime.indexOf('.')>0){
			generationPayTime=generationPayTime.substring(0, generationPayTime.indexOf('.'));
		}
		return generationPayTime;
	}

	public void setGenerationPayTime(String generationPayTime) {
		this.generationPayTime = generationPayTime;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}
	public String getGenerationBankStr(){
		if(StringUtils.isNotBlank(getPayerBankName())&&StringUtils.isNotBlank(getBankCardNo())){
			StringBuffer buffer=new StringBuffer();
			buffer.append(getPayerBankName());
			buffer.append("(尾号");
			buffer.append(getBankCardNo().substring(getBankCardNo().length()-4, getBankCardNo().length()));
			buffer.append(")");
			return buffer.toString();
		}
		return null;
	}

	public String getBankCardRealName() {
		return bankCardRealName;
	}

	public void setBankCardRealName(String bankCardRealName) {
		this.bankCardRealName = bankCardRealName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getChangeTimeStr(){
		if(getChangeTime() == null){
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(getChangeTime());
	}
	
	public String getPayTimeStr(){
		if(getPayTime() == null){
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(getPayTime());
	}
	
	public String getPayStatusStr(){
		return SettleAccountStatusEnum.getNameByCode(Byte.valueOf(getPayStatus()));
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public String getUserBankCardNo() {
		return userBankCardNo;
	}

	public void setUserBankCardNo(String userBankCardNo) {
		this.userBankCardNo = userBankCardNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPayerBankCardNo() {
		return payerBankCardNo;
	}

	public void setPayerBankCardNo(String payerBankCardNo) {
		this.payerBankCardNo = payerBankCardNo;
	}
	
	
}
