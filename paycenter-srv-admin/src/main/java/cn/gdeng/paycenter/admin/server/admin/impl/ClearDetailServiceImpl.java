package cn.gdeng.paycenter.admin.server.admin.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import cn.gdeng.paycenter.api.server.pay.ClearDetailService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.VClearDetailDto;

@Service
public class ClearDetailServiceImpl implements ClearDetailService{
	
	@Autowired
	public BaseDao<?> 	baseDao;

	@Override
	public List<VClearDetailDto> queryByParams(Map<String, Object> params) {
		List<VClearDetailDto> list = this.baseDao.queryForList("VClearDetail.queryByParams", params,VClearDetailDto.class);
		return list;
	}

}
