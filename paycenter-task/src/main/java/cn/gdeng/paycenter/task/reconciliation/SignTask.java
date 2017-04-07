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

public class SignTask {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private BaseDao<?> baseDao;
	@Autowired
    private GdProperties gdProperties;
	private static StringBuilder mailMsg;

	/**
	 * 批量验证签名
	 */
	public void batchValSign() {
			logger.info("批量签名验证开始.....");
			mailMsg = new StringBuilder();
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
			
			if(!SignTableUtil.getSignValOnOff(gdProperties.rsaSignKeyPath())){
				logger.info("批量签名验证结束 无操作.....");
				return;
			}
			
			for(String tableName: list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("tableName", tableName);
				map.put("pageSize", 20000);
				int start = 0;
				for(;;){
					map.put("start", start);
					try {
						if(doBatchValSign(map) < 1){
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
			logger.info("\n总验证耗时："+(System.currentTimeMillis()-startTime)+"ms result:\n"+result.toString());
			SignMailUtil.sendMail("数据签名定时验证", mailMsg.toString());
			mailMsg = null;
	}
	
	/**
	 * 批量验证签名
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int doBatchValSign(Map<String,Object> paramMap) throws Exception {
		logger.info(paramMap.toString()+" start................");
		long start = System.currentTimeMillis();
		List<Map<String,Object>> list = baseDao.queryForList("sign_map.queryByPage", paramMap);
		if(null == list
				|| list.size() < 1){
			return 0;
		}
		String publicKey = PrivateKeyUtil.getPublicKey(gdProperties.rsaSignKeyPath());
		for(Map<String,Object> map : list){
			if(!map.containsKey("sign")
					|| null == map.get("sign")){
				insertSignError((String)paramMap.get("tableName"),JSON.toJSONString(map),"签名不存在");
				continue;
			}
			String sign = (String)map.get("sign");
			Boolean flag = RSASignature.doCheck(ConvertUtil.getMapValues(map),sign,publicKey);
			if(!flag){
				insertSignError((String)paramMap.get("tableName"),JSON.toJSONString(map)+" sign="+sign,"签名验证失败");
			}
		}
		logger.info(paramMap.toString()+" 验证耗时："+(System.currentTimeMillis()-start)+" 数据量："+list.size());
		return list.size();
	}
	
	/**
	 * 签名验证异常记录
	 * @param tableName
	 * @param content
	 * @param error
	 */
	private void insertSignError(String tableName, String content, String error){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableName", tableName);
		map.put("content", content);
		map.put("error", error);
		map.put("bizType", 0);
		baseDao.execute("sign_map.insertSignError", map);
		mailMsg.append(map.toString()+"<br/>");
	}
	
}
