package cn.gdeng.paycenter.api.server.api;

import java.util.List;

import cn.gdeng.paycenter.api.BizException;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;

/**
 * 暴露给外部系统的服务
 * @author sss
 *
 */
public interface ApiService {

	List<VClearDetailDto> getVClearList(VClearDetailDto dto) throws BizException;
}
