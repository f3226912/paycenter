package cn.gdeng.paycenter.admin.dto.admin;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;
import cn.gdeng.paycenter.enums.SettleAccountStatusEnum;

public class SettleAccountExportDTO extends RemitRecordEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4661776264253521109L;

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
	 * 银行预留手机号
	 */
	private String  userMobile;
	/**
	 * 开户行名称
	 */
	private String  userDepositBankName;
	/**
	 * 开户行所在地
	 */
	private String address;
	/**
	 * 支行名称
	 */
	private String userSubBankName;
	/**
	 * 用户卡号
	 */
	private String userBankCardNo;
	
	/**
	 * 异常标记次数
	 */
	private Integer exceptionMackCount;
	
	/**
	 * 第三方支付流水
	 */
	private String thirdPayNumber;
	
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

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserDepositBankName() {
		return userDepositBankName;
	}

	public void setUserDepositBankName(String userDepositBankName) {
		this.userDepositBankName = userDepositBankName;
	}


	public String getUserSubBankName() {
		return userSubBankName;
	}

	public void setUserSubBankName(String userSubBankName) {
		this.userSubBankName = userSubBankName;
	}

	public String getUserBankCardNo() {
		return userBankCardNo;
	}

	public void setUserBankCardNo(String userBankCardNo) {
		this.userBankCardNo = userBankCardNo;
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

	public Integer getExceptionMackCount() {
		return exceptionMackCount;
	}

	public void setExceptionMackCount(Integer exceptionMackCount) {
		this.exceptionMackCount = exceptionMackCount;
	}

	public String getThirdPayNumber() {
		return thirdPayNumber;
	}

	public void setThirdPayNumber(String thirdPayNumber) {
		this.thirdPayNumber = thirdPayNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getFeeAmtFormat() {
		if(getFeeAmt()==null){
			return null;
		}
		return new DecimalFormat("0.00").format(getFeeAmt());
	}
	
	public String getDueAmtFormat() {
		if(getDueAmt()==null){
			return null;
		}
		return new DecimalFormat("0.00").format(getDueAmt());
	}
	
}
