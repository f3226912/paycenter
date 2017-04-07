package cn.gdeng.paycenter.server.notify.impl;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gudeng.framework.dba.util.DalUtils;

import cn.gdeng.paycenter.api.server.notify.MemberBaseinfoService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.dto.pay.MemberSendDto;

/**
 *功能描述：MemberBaseinfo增删改查实现类
 *
 */
@Service
public class MemberBaseinfoServiceImpl implements MemberBaseinfoService{

	@Autowired
	private BaseDao<?> baseDao;
	
	@Override
	public int updateMemberBaseinfoDTO(MemberSendDto mb) throws Exception {
		return (int) baseDao.execute("MemberBaseinfo.updateMemberBaseinfo", mb);
	}

	@Override
	@Transactional
	public int addMember(MemberSendDto mb) throws Exception {
		Map<String, Object> params = DalUtils.convertToMap(mb);
		if(mb.getUpdateTime()!=null){
			String dt = params.get("updateTime")+"";
			SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			params.put("updateTime", sdf2.format(sdf1.parse(dt))); //将updateTime时间格式处理，Fri Nov 05 15:36:49 CST 2016 不行
		}
		int memberId = baseDao.execute("MemberBaseinfo.addMemberBaseinfo", params);
		return memberId;
	}

}
