package cn.gdeng.paycenter.dto.pay;

import cn.gdeng.paycenter.entity.pay.MemberBankcardInfoEntity;

public class MemberBankcardInfoDTO extends MemberBankcardInfoEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7259139896598499885L;
	
	private String auditDoStatus;

	public String getAuditDoStatus() {
		return auditDoStatus;
	}

	public void setAuditDoStatus(String auditDoStatus) {
		this.auditDoStatus = auditDoStatus;
	}
	
	

}
