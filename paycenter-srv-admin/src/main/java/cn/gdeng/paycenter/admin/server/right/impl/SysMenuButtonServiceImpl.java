package cn.gdeng.paycenter.admin.server.right.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.gdeng.paycenter.admin.service.right.SysMenuButtonService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.SysMenuButton;
import cn.gdeng.paycenter.util.web.api.CommonConstant;

/**
 * 按钮操作实现类;
 * 
 */
@Service
public class SysMenuButtonServiceImpl implements SysMenuButtonService {
	@Autowired
	private BaseDao<?> baseDao;

//	@Autowired
//	private SysMenuButtonMapper sysMenuButtonMapper;

	@Override
	public SysMenuButton getSysButtonByID(String btnId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("btnID", btnId);
		return baseDao.queryForObject("SysMenuButton.getSysButtonByID", map,
				SysMenuButton.class);
	}

	@Override
	public int getTotal(Map<String, Object> map) {
		return baseDao.queryForObject("SysMenuButton.getTotal", map,
				Integer.class);
	}

	@Override
	public List<SysMenuButton> getByCondition(Map<String, Object> map) {
		return baseDao.queryForList("SysMenuButton.getByCondition", map,
				SysMenuButton.class);
	}

	@Override
	public String insert(SysMenuButton sysMenuButton) {
		// SysButtonCheck biz = new SysButtonCheck();
		// biz.checkButtonCode(sysMenuButtonMapper, sysMenuButton);
		// if (biz.hasError()) {
		// return biz.getErrorMsg();
		// }
		// sysMenuButtonMapper.insert(sysMenuButton);
		// return CommonConstant.COMMON_AJAX_SUCCESS;
		int i = baseDao.execute("SysMenuButton.insert", sysMenuButton);
		return i > 0 ? CommonConstant.COMMON_AJAX_SUCCESS
				: "sysmgr.sysmenu.buttonCodeExists";
	}

	@Override
	public String update(SysMenuButton sysMenuButton) {
		// SysButtonCheck biz = new SysButtonCheck();
		// biz.checkButtonCode(sysMenuButtonMapper, sysMenuButton);
		// if (biz.hasError()) {
		// return biz.getErrorMsg();
		// }
		// sysMenuButtonMapper.update(sysMenuButton);
		// return CommonConstant.COMMON_AJAX_SUCCESS;

		int i = baseDao.execute("SysMenuButton.update", sysMenuButton);
		return i > 0 ? CommonConstant.COMMON_AJAX_SUCCESS
				: "sysmgr.sysmenu.buttonCodeExists";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String delete(List<String> buttonList) {
		// SysButtonCheck biz = new SysButtonCheck();
		// biz.checkButtonIsApplyRole(sysMenuButtonMapper, buttonList);
		// if (biz.hasError()) {
		// return biz.getErrorMsg();
		// }
		// sysMenuButtonMapper.delete(buttonList);
		// return CommonConstant.COMMON_AJAX_SUCCESS;

		int len = buttonList.size();
		Map<String, Object>[] batchValues = new HashMap[len];
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("btnID", StringUtils.trim(buttonList.get(i)));
			batchValues[i] = map;
		}
		return baseDao.batchUpdate("SysMenuButton.delete", batchValues).length > 0 ? CommonConstant.COMMON_AJAX_SUCCESS
				: "sysmgr.sysmenu.buttonCodeExists";

	}
}
