package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**对账日志明细实体
 * @author wjguo
 *
 * datetime:2016年11月11日 下午3:32:55
 */
@Entity(name = "bill_check_log")
public class BillCheckLogEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8576208940660635066L;

	/**
     *
     */
    private Long id;

    /**
     *支付方式 ALIPAY_H5：支付宝 WEIXIN_APP：微信 PINAN：平安银行
     */
    private String payType;

    /**
     *对账日期
     */
    private Date checkTime;

    /**
     *交易日期
     */
    private Date payTime;

    /**
     *对账笔数
     */
    private Integer checkNum;

    /**
     *对账金额
     */
    private Double checkAmt;

    /**
     *对账成功笔数
     */
    private Integer checkSuccessNum;

    /**
     *对账成功金额
     */
    private Double checkSuccessAmt;

    /**
     *对账失败笔数
     */
    private Integer checkFailNum;

    /**
     *对账失败金额
     */
    private Double checkFailAmt;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建用户
     */
    private String createUserId;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     *更新用户
     */
    private String updateUserId;

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    @Column(name = "payType")
    public String getPayType(){

        return this.payType;
    }
    public void setPayType(String payType){

        this.payType = payType;
    }
    @Column(name = "checkTime")
    public Date getCheckTime(){

        return this.checkTime;
    }
    public void setCheckTime(Date checkTime){

        this.checkTime = checkTime;
    }
    @Column(name = "payTime")
    public Date getPayTime(){

        return this.payTime;
    }
    public void setPayTime(Date payTime){

        this.payTime = payTime;
    }
    @Column(name = "checkNum")
    public Integer getCheckNum(){

        return this.checkNum;
    }
    public void setCheckNum(Integer checkNum){

        this.checkNum = checkNum;
    }
    @Column(name = "checkAmt")
    public Double getCheckAmt(){

        return this.checkAmt;
    }
    public void setCheckAmt(Double checkAmt){

        this.checkAmt = checkAmt;
    }
    @Column(name = "checkSuccessNum")
    public Integer getCheckSuccessNum(){

        return this.checkSuccessNum;
    }
    public void setCheckSuccessNum(Integer checkSuccessNum){

        this.checkSuccessNum = checkSuccessNum;
    }
    @Column(name = "checkSuccessAmt")
    public Double getCheckSuccessAmt(){

        return this.checkSuccessAmt;
    }
    public void setCheckSuccessAmt(Double checkSuccessAmt){

        this.checkSuccessAmt = checkSuccessAmt;
    }
    @Column(name = "checkFailNum")
    public Integer getCheckFailNum(){

        return this.checkFailNum;
    }
    public void setCheckFailNum(Integer checkFailNum){

        this.checkFailNum = checkFailNum;
    }
    @Column(name = "checkFailAmt")
    public Double getCheckFailAmt(){

        return this.checkFailAmt;
    }
    public void setCheckFailAmt(Double checkFailAmt){

        this.checkFailAmt = checkFailAmt;
    }
    @Column(name = "createTime")
    public Date getCreateTime(){

        return this.createTime;
    }
    public void setCreateTime(Date createTime){

        this.createTime = createTime;
    }
    @Column(name = "createUserId")
    public String getCreateUserId(){

        return this.createUserId;
    }
    public void setCreateUserId(String createUserId){

        this.createUserId = createUserId;
    }
    @Column(name = "updateTime")
    public Date getUpdateTime(){

        return this.updateTime;
    }
    public void setUpdateTime(Date updateTime){

        this.updateTime = updateTime;
    }
    @Column(name = "updateUserId")
    public String getUpdateUserId(){

        return this.updateUserId;
    }
    public void setUpdateUserId(String updateUserId){

        this.updateUserId = updateUserId;
    }
}

