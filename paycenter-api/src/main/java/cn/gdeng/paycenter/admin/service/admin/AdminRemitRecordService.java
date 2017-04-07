package cn.gdeng.paycenter.admin.service.admin;

import java.util.Map;

import cn.gdeng.paycenter.entity.pay.RemitRecordEntity;

/**   
 * 付款记录service接口
 */
public interface AdminRemitRecordService {

	/**
	 * 新增复核记录
	 */
	public void insertAudit(Map<String, Object> map) throws Exception;
	
	/**
	 * 修改转账记录
	 */
	public void updateTransfer(Map<String, Object> map) throws Exception;
	
	/**
	 * 新增转账记录
	 */
	public void insertTransfer(Map<String, Object> map) throws Exception;
	
	/**
	 * 修改复核记录
	 */
    public void updateAudit(Map<String, Object> map) throws Exception;
}






