package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;
import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.entity.pay.BillClearSumEntity;

public interface ClearSumService {
	
	public List<BillClearSumEntity>	queryByParams(Map<String, Object> params)throws BizException;
	public int queryCountByParams(Map<String, Object> params)throws BizException;
	public BillClearSumEntity getClearSumById(long id);

}
