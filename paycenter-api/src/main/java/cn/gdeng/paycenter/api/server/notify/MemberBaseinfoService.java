package cn.gdeng.paycenter.api.server.notify;

import cn.gdeng.paycenter.dto.pay.MemberSendDto;
/**
 * 功能描述：MemberBaseinfo增删改查实现类
 *
 */
public interface MemberBaseinfoService {

	public int updateMemberBaseinfoDTO(MemberSendDto mb) throws Exception ;
	
	public int addMember(MemberSendDto mb) throws Exception;
}