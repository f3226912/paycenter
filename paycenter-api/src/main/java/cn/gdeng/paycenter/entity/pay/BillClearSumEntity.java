package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;



/**
 * autho：gaofeng
 * 清分汇总
 * */
@Entity(name="bill_clear_sum")
public class BillClearSumEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 * */
	private Long id;
	/**
	 * 支付方式 ALIPAY_H5：支付宝 WEIXIN_APP：微信 POS：POS刷卡 
	 * */
	private String payType;
	/**
	 * 交易日期 
	 * */
	private String payTime;
	/**
	 * 清分笔数 
	 * */
	private int clearNum;
	/**
	 * 清分金额 
	 * */
	private double clearAmt;
	/**
	 * 清分成功笔数 
	 * */
	private int clearSuccessNum;
	/**
	 * 清分成功金额 
	 * */
	private double clearSuccessAmt;
	/**
	 * 清分失败笔数 
	 * */
	private int clearFailNum;
	/**
	 * 清分失败金额 
	 * */
	private double clearFailAmt;
	/**
	 * 创建时间 
	 * */
	private String createTime;
	/**
	 * 创建用户 
	 * */
	private String createUserId;
	/**
	 * 更新时间 
	 * */
	private String updateTime;
	/**
	 * 更新用户 
	 * */
	private String updateUserId;
	/**
	 * 签名 
	 * */
	private String sign;
	
	@Id
    @Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "payType")
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name = "payTime")
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	@Column(name = "clearNum")
	public int getClearNum() {
		return clearNum;
	}
	public void setClearNum(int clearNum) {
		this.clearNum = clearNum;
	}
	@Column(name = "clearAmt")
	public double getClearAmt() {
		return clearAmt;
	}
	public void setClearAmt(double clearAmt) {
		this.clearAmt = clearAmt;
	}
	@Column(name = "clearSuccessNum")
	public int getClearSuccessNum() {
		return clearSuccessNum;
	}
	public void setClearSuccessNum(int clearSuccessNum) {
		this.clearSuccessNum = clearSuccessNum;
	}
	@Column(name = "clearSuccessAmt")
	public double getClearSuccessAmt() {
		return clearSuccessAmt;
	}
	public void setClearSuccessAmt(double clearSuccessAmt) {
		this.clearSuccessAmt = clearSuccessAmt;
	}
	@Column(name = "clearFailNum")
	public int getClearFailNum() {
		return clearFailNum;
	}
	public void setClearFailNum(int clearFailNum) {
		this.clearFailNum = clearFailNum;
	}
	@Column(name = "clearFailAmt")
	public double getClearFailAmt() {
		return clearFailAmt;
	}
	public void setClearFailAmt(double clearFailAmt) {
		this.clearFailAmt = clearFailAmt;
	}
	@Column(name = "createTime")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "createUserId")
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "updateUserId")
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	@Column(name = "sign")
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
