package cn.gdeng.paycenter.api.server.pay;

import cn.gdeng.paycenter.entity.pay.MemberBankcardInfoEntity;

/**
 * 用户银行卡信息接口
 * @author Ailen
 *
 */
public interface MemberBankcardInfoService {

	/**
	 * 查询用户银行卡信息 
	 * 根据infoId 来自外界
	 * @param paramMap
	 * @return
	 */
	public MemberBankcardInfoEntity query(Integer infoId);

	/**
	 * 添加银行卡信息
	 * @param entity
	 */
	public void add(MemberBankcardInfoEntity entity);

	/**
	 * 修改银行卡信息
	 * @param entity
	 */
	public void update(MemberBankcardInfoEntity entity);

	/**
	 * 综合方法，消费者调用
	 * @param entity
	 */
	void addOrEditMemberBankcardInfo(MemberBankcardInfoEntity entity);

}
