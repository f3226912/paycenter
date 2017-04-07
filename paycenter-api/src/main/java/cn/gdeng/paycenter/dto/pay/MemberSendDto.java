package cn.gdeng.paycenter.dto.pay;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

/**
 * 会员信息DTO
 * 请将此对象序列化后二进制数据推送至队列
 * 属性均为非空
 * @author zhangnf20160803
 *
 */
public class MemberSendDto implements Serializable{

	private static final long serialVersionUID = -4380556273298959249L;
	/**会员ID**/
	private Integer memberId;
	/**操作类型：0 创建 1 更新 **/
	private Integer crud;
	/**真实姓名**/
	private String realName;
	/**手机号码**/
	private String mobile;
	/**账号**/
	private String account;
	
	/**会员类别
	 * （1谷登农批，2农速通，3农商友，4产地供应商，5门岗，余待补）'
	 * **/
	private int level; 
	/**注册来源
	 * 0管理后台
	 * 1谷登农批网* 
	 * 2农速通* 
	 * 3农商友* 
	 * 4农商友-农批商* 
	 * 5农批友* 
	 * 6供应商* 
	 * 7POS刷卡
	 * **/
	private Integer regetype;
	/**状态 0未启用  1启用**/
	private Integer status;
	/**修改时间
	 * 如果是新数据则和创建时间一致
	 **/
	private Date updateTime;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getRegetype() {
		return regetype;
	}
	public void setRegetype(Integer regetype) {
		this.regetype = regetype;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getCrud() {
		return crud;
	}
	public void setCrud(Integer crud) {
		this.crud = crud;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
}
