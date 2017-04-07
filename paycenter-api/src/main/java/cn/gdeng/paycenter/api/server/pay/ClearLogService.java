package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.BillClearLogEntity;

public interface ClearLogService {
	
	public List<BillClearLogEntity>	queryByParams(Map<String, Object> params)throws BizException;
	public int queryCountByParams(Map<String, Object> params)throws BizException;
}
