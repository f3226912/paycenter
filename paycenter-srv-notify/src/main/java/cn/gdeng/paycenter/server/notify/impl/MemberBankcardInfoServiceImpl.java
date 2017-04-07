package cn.gdeng.paycenter.server.notify.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gudeng.framework.dba.util.DalUtils;

import cn.gdeng.paycenter.api.server.pay.MemberBankcardInfoService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.MemberBankcardInfoEntity;

/**
 * 用户银行信息服务
 * @author Ailen
 *
 */
@Service
public class MemberBankcardInfoServiceImpl implements MemberBankcardInfoService{

	@Resource
	private BaseDao<?> baseDao;
	
	@Override
	public MemberBankcardInfoEntity query(Integer infoId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("infoId", infoId);
		return baseDao.queryForObject("MemberBankcardInfo.query", paramMap, MemberBankcardInfoEntity.class);
	}

	@Override
	public void add(MemberBankcardInfoEntity entity) {
		Map<String, Object> params = DalUtils.convertToMap(entity);
		baseDao.execute("MemberBankcardInfo.add", params);
	}

	@Override
	public void update(MemberBankcardInfoEntity entity) {
		baseDao.execute("MemberBankcardInfo.update", entity);
	}
	
	@Override
	public void addOrEditMemberBankcardInfo(MemberBankcardInfoEntity entity) {
		
		/*
		 * 查询是否存在指定的用户信息
		 */
		if(query(entity.getInfoId())!=null) { //存在 则修改
			update(entity);
		} else { //不存在添加
			
			//设置创建人为本人
			entity.setCreateUserId(entity.getUpdateUserId());
			add(entity);
		}
		
	}

}
