package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.PosMachineConfigEntity;
import cn.gdeng.paycenter.enums.PosUserTypeEnum;
import cn.gdeng.paycenter.util.admin.web.DateUtil;

public class PosMachineConfigDTO extends PosMachineConfigEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4588659384181424276L;
	
	private String account;
	
	private String mobile;
	
	private String level;
	
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUserTypeStr(){
		return PosUserTypeEnum.getNameByCode(getLevel());
	}
	
	public String getCreateTimeStr(){
		if(getCreateTime() != null && getCreateTime() != ""){
			return DateUtil.formatStrDate(getCreateTime(), DateUtil.DATE_FORMAT_DATETIME);
		}
		return "";
	}
}
