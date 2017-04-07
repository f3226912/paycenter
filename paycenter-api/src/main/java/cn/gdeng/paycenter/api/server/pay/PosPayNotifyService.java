package cn.gdeng.paycenter.api.server.pay;

import java.util.List;
import java.util.Map;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.PosPayNotifyDto;
import cn.gdeng.paycenter.entity.pay.PosPayNotifyEntity;

public interface PosPayNotifyService {

	int addPosPayNotify(PosPayNotifyDto dto) throws BizException;
	
	void dynamicUpdatePosPayNotify(PosPayNotifyEntity entity);
	
	List<PosPayNotifyDto> queryByCondition(Map<String,Object> params);
}
