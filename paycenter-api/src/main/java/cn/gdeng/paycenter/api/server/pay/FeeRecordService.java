package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.admin.dto.admin.AdminPageDTO;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.FeeRecordDTO;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.PayTradeEntity;
import cn.gdeng.paycenter.entity.pay.PayTypeEntity;
import cn.gdeng.paycenter.util.web.api.ApiResult;

/**
 * 手续费接口
 * 
 * @author Ailen
 *
 */
public interface FeeRecordService {

	/**
	 * 查询手续费
	 * 
	 * @param params
	 * @return
	 */
	public List<FeeRecordDTO> queryByCondition(Map<String, Object> params); //代收
	public List<FeeRecordDTO> queryFeePayList(Map<String,Object>	params);//代付

	/**
	 * 根据ID获得数据
	 * 
	 * @param id
	 * @return
	 */
	public FeeRecordDTO queryById(Integer id);

	/**
	 * 获得数量
	 * 
	 * @param params
	 * @return
	 */
	public Integer queryCount(Map<String, Object> params);
	public Integer queryPayCount(Map<String,Object>	params);

	/**
	 * 添加手续费
	 * 
	 * @param feeRecordDTO
	 */
	public void addFeeRecord(FeeRecordDTO feeRecordDTO);

	/**
	 * 获得分页数据
	 * 
	 * @param params
	 * @return
	 */
	public ApiResult<AdminPageDTO> queryPage(Map<String, Object> params);
	public ApiResult<AdminPageDTO> queryPayListPage(Map<String, Object> params); 
	
	/**
	 * 修改手续费
	 * 
	 * @param feeRecordDTO
	 */
	public void updateFeeRecord(FeeRecordDTO feeRecordDTO)throws Exception;
	
	/**
	 * 添加手续费
	 * 
	 * @param params
	 */
	public void addFeeRecord(Map<String, Object> params);
	
	/**
	 * 修改手续费
	 * 
	 * @param params
	 */
	public void updateFeeRecord(Map<String, Object> params);
	
	/**
	 * 通过流水号 获取手续费详情
	 * */
	public PayTradeEntity queryPayTradeByPayCenterNumber(String payCenterNumber) throws BizException;
	
	/**
	 * 通过付款类型，获取付款类型信息
	 * */
	public PayTypeEntity queryPayTypeByType(String payType) throws BizException;
	
	/**
	 * 更新手续费
	 * */
	public void updateFeeDetailAndAppKey(String appKey,FeeRecordEntity	feeRecordEntity)throws Exception;
	
	/**
	 * 获取系统 access_sys_config 
	 * */
	public List<AccessSysConfigEntity> getAccSysConfigs()throws Exception;

}
