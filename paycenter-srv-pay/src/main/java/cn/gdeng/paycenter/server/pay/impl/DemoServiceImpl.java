package cn.gdeng.paycenter.server.pay.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.gudeng.framework.dba.transaction.annotation.Transactional;

import cn.gdeng.paycenter.api.server.pay.DemoService;
import cn.gdeng.paycenter.dao.BaseDao;
import cn.gdeng.paycenter.entity.pay.AccessSysConfigEntity;
import cn.gdeng.paycenter.entity.pay.FeeRecordEntity;
import cn.gdeng.paycenter.entity.pay.MemberBankcardInfoEntity;
import cn.gdeng.paycenter.enums.MsgCons;
import cn.gdeng.paycenter.util.server.mq.SendMessageUtil;
import cn.gdeng.paycenter.util.server.sign.ConvertUtil;
import cn.gdeng.paycenter.util.server.sign.PrivateKeyUtil;
import cn.gdeng.paycenter.util.server.sign.RSASignature;
import cn.gdeng.paycenter.util.web.api.ApiResult;
import cn.gdeng.paycenter.util.web.api.GdProperties;

@Service
public class DemoServiceImpl implements DemoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private BaseDao<?> baseDao;

	@Resource
	private SendMessageUtil sendMessage;
	@Autowired
    private GdProperties     gdProperties;

	@Override
	public ApiResult<String> getDemo(String demo) {
		ApiResult<String> apiResult = new ApiResult<String>();
		try {
			MemberBankcardInfoEntity pt = new MemberBankcardInfoEntity();
			
			pt.setInfoId(4);
			pt.setMemberId(1212122);
			pt.setRealName("测试账号");
			pt.setIdCard("400321290875908");
			pt.setDepositBankName("中央银行");
			pt.setSubBankName("中央银行深圳分行");
			pt.setBankCardNo("NO124");
			pt.setProvinceId(11111);
			pt.setCityId(22222);
			pt.setAreaId(33333);
			pt.setAccCardType(1);
			pt.setStatus("1");
			pt.setAuditStatus("0");
			pt.setCreateUserId("1212122");
			pt.setUpdateUserId("1212122");
			
			sendMessage.sendAsyncMessage("dev_nst_bank", "TAGS", pt, new SendCallback() {
				public void onSuccess(final SendResult sendResult) {

				}

				public void onException(final OnExceptionContext context) {

				}
			});
			apiResult.setCodeMsg(MsgCons.C_10000, demo);
		} catch (Exception e) {
			logger.error("demo异常:", e);
			apiResult.withError(MsgCons.C_50000, MsgCons.M_50000);
		}
		return apiResult;
	}

	@Override
	public String getDemo1(String demo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 签名测试
	 */
	public ApiResult<Object> sign() throws Exception {
		StringBuilder builder = new StringBuilder();
		/**
		 * 1 、添加新数据
		 */
		FeeRecordEntity entity = new FeeRecordEntity();
		entity.setFeeAmt(300.00);
		entity.setOrderNo("001");
		entity.setRemark("备注");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		Long id = (Long) baseDao.persistBySign(entity);
		builder.append("------"+id);
		
		/**
		 * 2、获取对象签名
		 * 请传入完整实体对象
		 */
		FeeRecordEntity entity2 = new FeeRecordEntity();
		entity2.setId(38);
		entity2 = baseDao.find(FeeRecordEntity.class,entity2);
		String sign = baseDao.getEntitySign(entity2);
		entity2.setSign(sign);
		//下一步更新实体对象
		//................
		builder.append("------"+sign);
		
		/**
		 * 3、获取对象签名
		 * 实体只用设置主键
		 */
		FeeRecordEntity entity4 = new FeeRecordEntity();
		entity4.setId(38);
		String sign4 = baseDao.getEntitySignByKey(entity4);
		//下一步更新实体对象
		//................
		builder.append("------"+sign4);
		
		/**
		 * 4、验证实体对象签名
		 * 实体只用设置主键
		 */
		FeeRecordEntity entity3 = new FeeRecordEntity();
		entity3.setId(id.intValue());
		boolean flag= false;
		try {
			flag = baseDao.validateSign(entity3);
		} catch (Exception e) {
			logger.error("",e);
		}
		builder.append("------"+flag);
		
		/**
		 * 5、验证实体对象签名
		 * 请传入完整实体对象
		 */
		FeeRecordEntity entity5 = new FeeRecordEntity();
		entity5.setId(38);
		entity5 = baseDao.find(FeeRecordEntity.class,entity5);
		boolean flag5 = baseDao.validateSign(entity5, FeeRecordEntity.class);
		builder.append("------"+flag5);
		
		/**
		 * 6、批量签名
		 * 传入实体对象 和随机UUID
		 */
		String uuid = "uuuid9237332139281";
		int rows = baseDao.batchSign(uuid,FeeRecordEntity.class);
		builder.append("------"+rows);
		
		
		
		/**
		 * 7、批量验证签名
		 */
		List<Long> ids = new ArrayList<Long>();
		ids.add(112L);
		ids.add(126L);
		List<Long> errorIds = baseDao.batchValidateSign(ids, FeeRecordEntity.class);
		builder.append("------"+errorIds.size());
		
		return new ApiResult<>().setResult(builder.toString());
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public ApiResult<Object> batchSign(Map<String,Object> paramMap) throws Exception {
		logger.debug(paramMap.toString()+" start................");
		long start = System.currentTimeMillis();
		List<Map<String,Object>> list = baseDao.queryForList("demon_map.queryAll", paramMap);
		if(null == list){
			return new ApiResult<Object>();
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
		int[] n = baseDao.batchUpdate("demon_map.updateById", mapAray);

		if(n.length!= list.size()){
			logger.debug(" 异常 更新数量不匹配：");
		}
		logger.debug(paramMap.toString()+" 更新耗时："+(System.currentTimeMillis()-start)+" 更新数据量："+n.length);
		return new ApiResult<Object>();
	}
	
	public ApiResult<Object> batchValSign(Map<String,Object> paramMap) throws Exception {
		logger.debug(paramMap.toString()+" start................");
		long start = System.currentTimeMillis();
		List<Map<String,Object>> list = baseDao.queryForList("demon_map.queryAll", paramMap);
		if(null == list){
			return new ApiResult<Object>();
		}
		AccessSysConfigEntity access = new AccessSysConfigEntity();
		access.setId(11);
		access = baseDao.find(AccessSysConfigEntity.class, access);
		String publicKey = access.getPublicKey();
		
		int error = 0;
		for(Map<String,Object> map : list){
			if(!map.containsKey("sign")
					|| null == map.get("sign")){
				error++;
				logger.debug("验证异常数据，签名不存在：\n"+JSON.toJSONString(paramMap)+"\n"+JSON.toJSONString(map));
				continue;
			}
			String sign = (String)map.get("sign");
			Boolean flag = RSASignature.doCheck(ConvertUtil.getMapValues(map),sign,publicKey);
			if(!flag){
				error++;
				logger.debug("验证异常数据：\n"+JSON.toJSONString(paramMap)+"\n"+JSON.toJSONString(map));
				logger.debug("\n原签名："+sign);
				String sign_new = RSASignature.sign(ConvertUtil.getMapValues(map),PrivateKeyUtil.getPrivateKey(gdProperties.rsaSignKeyPath()));
				logger.debug("\n重新名："+sign_new);
				logger.debug("\n签名比较结果： "+(sign_new.equals(sign)?"相等":"不等"));
			}
		}
		logger.debug(paramMap.toString()+" 验证耗时："+(System.currentTimeMillis()-start)+" 数据量："+list.size()+"  未通过数量:"+error);
		return new ApiResult<Object>();
	}

	public ApiResult<Object> batchValSignTime() throws Exception {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("tableName", "fee_record");
		List<Map<String,Object>> list = baseDao.queryForList("demon_map.queryAll", paramMap);
		List<Long> ids = new ArrayList<Long>();
		for(Map<String,Object> map : list){
			ids.add(((Integer) map.get("id")).longValue());
		}
		long start = System.currentTimeMillis();
		List<Long> errorIds = baseDao.batchValidateSign(ids, FeeRecordEntity.class);
		long end = System.currentTimeMillis()-start;
		logger.debug(paramMap.toString()+" 验证耗时："+end+"ms 总数据量："+list.size()+" 平均："+(list.size()*1000/end)+"条/秒  未通过数量:"+errorIds.size());
		return null;
	}

}
