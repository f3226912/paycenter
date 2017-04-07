package cn.gdeng.paycenter.task.reconciliation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.util.server.sign.ConvertUtil;
import cn.gdeng.paycenter.util.server.sign.PrivateKeyUtil;
import cn.gdeng.paycenter.util.server.sign.RSASignature;
import cn.gdeng.paycenter.util.server.sign.SignMailUtil;
import cn.gdeng.paycenter.util.server.sign.SignTableUtil;
import cn.gdeng.paycenter.util.web.api.GdProperties;

public class SignInitTask {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private BaseDao<?> baseDao;
	@Autowired
    private GdProperties gdProperties;

	/**
	 * 批量签名
	 */
	public void batchSign() {
			logger.info("批量签名检测开始.....");
			long startTime = System.currentTimeMillis();
			StringBuilder result = new StringBuilder();
			List<String> list = new ArrayList<String>();
			list.add("bank_rate_config");
			list.add("bill_check_detail");
			list.add("bill_check_log");
			list.add("bill_check_sum");
			list.add("clear_detail");
			list.add("fee_record");
			list.add("market_bank_acc_info");
			list.add("member_bankcard_info");
			list.add("pay_log_record");
			list.add("pay_trade");
			list.add("pay_trade_extend");
			list.add("pay_trade_pos");
			list.add("pay_type");
			list.add("pay_type_config");
			list.add("pos_machine_config");
			list.add("pos_pay_notify");
			list.add("ref_clear_relate");
			list.add("refund_record");
			list.add("remit_record");
			list.add("remit_record_error");
			
			if(!SignTableUtil.getSignOnOff(gdProperties.rsaSignKeyPath())){
				logger.info("批量签名检测结束 无操作.....");
				return;
			}
			
			List<String> listTmp = SignTableUtil.getReSignTables(gdProperties.rsaSignKeyPath());
			if(null != listTmp
					&& listTmp.size() >0){
				list = listTmp;
			}
			
			for(String tableName: list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("tableName", tableName);
				map.put("pageSize", 20000);
				int start = 0;
				for(;;){
					map.put("start", start);
					try {
						if(doBatchSign(map) < 1){
							break;
						}
					} catch (Exception e) {
						result.append(map.toString()+"error\n");
						logger.error(map.toString(),e);
					}
					start += 19999;
				}
				result.append(tableName+" complent!\n");
			}
			logger.info("\n签名总耗时："+(System.currentTimeMillis()-startTime)+"ms result:\n"+result.toString());
			SignMailUtil.sendBatchSignMail("批量数据签名", JSON.toJSONString(list));
	}
	
	@SuppressWarnings("unchecked")
	public int doBatchSign(Map<String,Object> paramMap) throws Exception {
		logger.info(paramMap.toString()+" start................");
		long start = System.currentTimeMillis();
		List<Map<String,Object>> list = baseDao.queryForList("sign_map.queryByPage", paramMap);
		if(null == list
				|| list.size() < 1){
			return 0;
		}
		int index = 0;
		String privateKey = PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath());
		Map<String,Object>[] mapAray = new HashMap[list.size()];
		for(Map<String,Object> map : list){
			String sign = RSASignature.sign(ConvertUtil.getMapValues(map),privateKey);
			Map<String,Object> rowMap = new HashMap<String,Object>(); 
			rowMap.put("sign", sign);
			rowMap.put("id", map.get("id"));
			rowMap.put("tableName", paramMap.get("tableName"));
			mapAray[index++] = rowMap;
		}
		int[] n = baseDao.batchUpdate("sign_map.updateById", mapAray);

		logger.debug(paramMap.toString()+" 签名耗时："+(System.currentTimeMillis()-start)+" 更新数据量："+n.length);
		return list.size();
	}
	
}
