package cn.gdeng.paycenter.util.server.jodis;

import cn.gdeng.paycenter.util.web.api.SerializeUtil;

public class JodisTemplate extends AbstractJodisTemplate {

	/**
	 * redis 序列化转成object
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(byte[] key) {
		byte[] bytes = super.get(key);
		Object obj =null;
		if (bytes!=null && bytes.length>0) {
			obj= SerializeUtil.unserialize(bytes);
		}
		return obj;
	}
}
