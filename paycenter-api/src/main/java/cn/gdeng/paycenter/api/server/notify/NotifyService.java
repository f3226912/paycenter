package cn.gdeng.paycenter.api.server.notify;

import cn.gdeng.paycenter.dto.notify.NotifyBo;

public interface NotifyService {
	
	public void notify(NotifyBo nobj) throws Exception;
	
	
}