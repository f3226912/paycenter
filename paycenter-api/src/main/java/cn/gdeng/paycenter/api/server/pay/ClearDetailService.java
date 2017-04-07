package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;

/**
 * 清分明细 
 * */
public interface ClearDetailService {
	
	/**
	 * 通过参数查询清分明细
	 * */
	public List<VClearDetailDto> queryByParams(Map<String, Object> params)throws BizException;

}
