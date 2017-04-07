package cn.gdeng.paycenter.entity.pay;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 结算管理异常记录entity
 * @author jianhuahuang
 * @Date:2016年11月8日下午4:59:29
 */
@Entity(name = "remit_record_error")
public class RemitRecordErrorEntity  implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 447462342909309808L;

	/**
     *
     */
    private Long id;

    /**
     *用户结算记录表id
     */
    private Integer remitRecordId;

    /**
     *备注
     */
    private String comment;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String createUserId;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private String updateUserId;

    /**
     *签名
     */
    private String sign;

    @Id
    @Column(name = "id")
    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    @Column(name = "remit_record_id")
    public Integer getRemitRecordId(){

        return this.remitRecordId;
    }
    public void setRemitRecordId(Integer remitRecordId){

        this.remitRecordId = remitRecordId;
    }
    @Column(name = "comment")
    public String getComment(){

        return this.comment;
    }
    public void setComment(String comment){

        this.comment = comment;
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
    @Column(name = "sign")
    public String getSign(){

        return this.sign;
    }
    public void setSign(String sign){

        this.sign = sign;
    }
}

