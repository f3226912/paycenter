package cn.gdeng.paycenter.server.notify.impl;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.gdeng.paycenter.api.server.notify.NotifyService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.notify.NotifyBo;

@Service
public class NotifyServiceImpl implements NotifyService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BaseDao<?> baseDao;
	
	@Override
	public void notify(NotifyBo nobj) {
		System.out.println("ssssssssssssssssssss");
	}


}
