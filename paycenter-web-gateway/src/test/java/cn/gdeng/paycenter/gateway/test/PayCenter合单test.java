package cn.gdeng.paycenter.gateway.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.Base64;

public class PayCenter合单test {

	@Test
	 public void test两个订单() {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("version", "222");
			paramMap.put("appKey", "nsy");
			paramMap.put("keyType", "RSA");
			paramMap.put("timeOut", "5");
			paramMap.put("sumPayAmt", "0.02");
			paramMap.put("requestIp", "8.8.8.8");
			paramMap.put("returnUrl", "http://www.baidu.com");
			
			Map<String, String> p1 = new HashMap<String, String>();
			p1.put("orderNo", "412016000216200");
			p1.put("title", "大白菜");
			p1.put("orderTime", "2016-11-09 11:11:11");
			p1.put("payerUserId", "225");
			p1.put("payeeUserId", "226");
			p1.put("logisticsUserId", "227");
			p1.put("totalAmt", "0.01");
			p1.put("payAmt", "0.01");

			p1.put("reParam", "{\"h\":\"5\"}");
			p1.put("requestNo", "2");
			p1.put("payCount", "2");
			Map<String, String> p2 = new HashMap<String, String>();
			p2.put("orderNo", "412016000216201");
			p2.put("title", "大白菜");
			p2.put("orderTime", "2016-11-09 11:11:11");
			p2.put("payerUserId", "225");
			p2.put("payeeUserId", "226");
			p2.put("logisticsUserId", "227");
			p2.put("totalAmt", "0.01");
			p2.put("payAmt", "0.01");
			p2.put("reParam", "{\"h\":\"5\"}");
			p2.put("requestNo", "2");
			p2.put("payCount", "2");
			
			List<Map> list = new ArrayList<>();
			list.add(p1);
			list.add(p2);
			String orderInfos = JSON.toJSONString(list);
			
			paramMap.put("orderInfos", orderInfos);

			String link = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(paramMap));
			String sign = "";
				String keyType = "RSA";
				// String keyType = "MD5";
				String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJxxUh7LPQOdnTwW7pFfbmvtMx2K2DwEyynGq+HY97R7PZpWzX0PozFrD1EkR6jZMwtBEHO6ljtabgxSVfGqVdCS4Y4NFc2KGuF7DsZCME41UcmX2+4ySpKAfi/VQYfG/zP03Kov64K2FpKIik6RlhAaLqHJFIymfiXQo9E9hiWtAgMBAAECgYANsdnXekEgFOcTwIonzavT5NLJrkLZli3WvV6EicK9WOB+p0SbwwetJssTdlKTlFfkj1CKYPYPwg3KJFcDrb6PPZJ8YerAjDQKk3kDdkLbGxmYCGbp9Rhn+skEUnphKgZdtmlGE/BmzRdQl1453oQGFksGt+GIPhVGajlgG5AKXQJBAMnOKs57StEL1t1w0TGcmjkhWcWNj1dNZ4kxLajMhd0Auj4wpg6ZHC4qWaRGXkLjWeiO3vJhV7rBHHgF1rFBKocCQQDGdIh8iKQO138DX/q4NcGNZ5Wd91ciovtvM7PiLHeQ2+eVm3B24v1J785kQFBNXTuDGZSRs8gDog/tk6f0RzcrAkEAuQ3egSPcMqA5syxeGLFzVwo3KQortLHdTJgVN6H86vFc1+SCkgZjKg5Zz2Nb0thqvPAuj3MmILIu6fzop/iLBwJBALlJ8mj7ltl4oFEBWvCE2DrzPMEpPwTK0SDpUAla5SOt/dI0N9P4aO6QZM47Mf6Zjsf/qTREe/nQZTi3RPgRSdMCQQCQ8TrhfkVMjMfyj9npfOSMDxPi1ck2nL6YD8pb6OB22I4edruJb7iaAdmmYLejXSXHD7JKzCMlhuPgBToDnU1R";
				//String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOU5lvCfM42Cz2LXz6+4T4iNzU2gZDOGMtWhxYi8HjBmJaPhZBJBy1vXRBAvVOEB7YYej3xbn7hR5NhiOjcAnRgvus9Quk3XcZFjlrFVPsbSQeBXP6Xsigyw13uwlwA7nkN50I2XJ8xlXUO1VBly/2t2AN3N2z2mU2dTEZmVP6SPAgMBAAECgYEAiESkJod97jJFd000ehHnthYWZn9jEf5FWhrBg3fRupP+7F2hT/ktG3vtSnHY06qBuamRQyx01u5YTQqX8Pq8Z/RJP0pqUlqlTieznTeuXEsQXr0rW4uw4chADxKOC7u6q4MxJPqMhaQa/r6BKGweeDYyQ4sC4PTzqJrNkrpv7gECQQD6jwxBKVutvjRVM8CaFKuMaY6YgLPcOZazAcJj8XWncYbNKbUzfpZ9R/cUzmApPOYBanvsTtkTjCtrCN1S+bK/AkEA6jPwWWlv3rCtZdiNaTB+Wg1z0xNPeydD41RBo+0mAclxhJ4rru07DdJqbCqh92UiY5eUd9jZ9Lt5oT0bqt0SMQJBAJe46X3H0okqBTyWwwlKfPrgrIVyYfd4M+YfqZulik4js7ImkGAnRm+ElXzW32n5Q3oWBgZ5VH++wEfnLUZih7cCQCo42ODVsFReV7RfuQa4tl63xCTOe2rBZmzKWNcbvtldnriA53Eu1ZGlWIhNm0uFkRCRWDPPBWQolEHsSVuhZVECQQCH+im+aQY1XPEbCT1oxtPDkh26seyH5CuLplg0UxlX2T2i5HUANTgFFMAHpYrneERzCDKizEwMy0LACivRRlJz";
				// String privateKey = "123456";
				String oldSign = AccessSysSignUtil.sign(link, keyType, privateKey);
				sign = Base64.encode(oldSign.getBytes());
			//System.out.println("http://10.17.1.203:8311/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("http://10.17.1.207:8311/gw/initPage?" + link + "&sign=" + sign);
			System.out.println("http://localhost:8080/paycenter-web-gateway/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("http://10.17.1.193:8311/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("https://pay.gdeng.cn/gw/initPage?" + link + "&sign=" + sign);
		}
	
	
	@Test
	public void test一个订单() {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("version", "222");
			paramMap.put("appKey", "nsy");
			paramMap.put("keyType", "RSA");
			paramMap.put("timeOut", "5");
			paramMap.put("sumPayAmt", "0.01");
			paramMap.put("requestIp", "8.8.8.8");
			paramMap.put("returnUrl", "http://www.baidu.com");
			
			Map<String, String> p1 = new HashMap<String, String>();
			p1.put("orderNo", "412016000216200");
			p1.put("title", "大白菜");
			p1.put("orderTime", "2016-11-09 11:11:11");
			p1.put("payerUserId", "225");
			p1.put("payeeUserId", "226");
			p1.put("logisticsUserId", "227");
			p1.put("totalAmt", "0.01");
			p1.put("payAmt", "0.01");

			p1.put("reParam", "{\"h\":\"5\"}");
			p1.put("requestNo", "2");
			p1.put("payCount", "2");
			List<Map> list = new ArrayList<>();
			list.add(p1);
			String orderInfos = JSON.toJSONString(list);
			
			paramMap.put("orderInfos", orderInfos);

			String link = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(paramMap));
			String sign = "";
				String keyType = "RSA";
				// String keyType = "MD5";
				String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJxxUh7LPQOdnTwW7pFfbmvtMx2K2DwEyynGq+HY97R7PZpWzX0PozFrD1EkR6jZMwtBEHO6ljtabgxSVfGqVdCS4Y4NFc2KGuF7DsZCME41UcmX2+4ySpKAfi/VQYfG/zP03Kov64K2FpKIik6RlhAaLqHJFIymfiXQo9E9hiWtAgMBAAECgYANsdnXekEgFOcTwIonzavT5NLJrkLZli3WvV6EicK9WOB+p0SbwwetJssTdlKTlFfkj1CKYPYPwg3KJFcDrb6PPZJ8YerAjDQKk3kDdkLbGxmYCGbp9Rhn+skEUnphKgZdtmlGE/BmzRdQl1453oQGFksGt+GIPhVGajlgG5AKXQJBAMnOKs57StEL1t1w0TGcmjkhWcWNj1dNZ4kxLajMhd0Auj4wpg6ZHC4qWaRGXkLjWeiO3vJhV7rBHHgF1rFBKocCQQDGdIh8iKQO138DX/q4NcGNZ5Wd91ciovtvM7PiLHeQ2+eVm3B24v1J785kQFBNXTuDGZSRs8gDog/tk6f0RzcrAkEAuQ3egSPcMqA5syxeGLFzVwo3KQortLHdTJgVN6H86vFc1+SCkgZjKg5Zz2Nb0thqvPAuj3MmILIu6fzop/iLBwJBALlJ8mj7ltl4oFEBWvCE2DrzPMEpPwTK0SDpUAla5SOt/dI0N9P4aO6QZM47Mf6Zjsf/qTREe/nQZTi3RPgRSdMCQQCQ8TrhfkVMjMfyj9npfOSMDxPi1ck2nL6YD8pb6OB22I4edruJb7iaAdmmYLejXSXHD7JKzCMlhuPgBToDnU1R";
				//String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOU5lvCfM42Cz2LXz6+4T4iNzU2gZDOGMtWhxYi8HjBmJaPhZBJBy1vXRBAvVOEB7YYej3xbn7hR5NhiOjcAnRgvus9Quk3XcZFjlrFVPsbSQeBXP6Xsigyw13uwlwA7nkN50I2XJ8xlXUO1VBly/2t2AN3N2z2mU2dTEZmVP6SPAgMBAAECgYEAiESkJod97jJFd000ehHnthYWZn9jEf5FWhrBg3fRupP+7F2hT/ktG3vtSnHY06qBuamRQyx01u5YTQqX8Pq8Z/RJP0pqUlqlTieznTeuXEsQXr0rW4uw4chADxKOC7u6q4MxJPqMhaQa/r6BKGweeDYyQ4sC4PTzqJrNkrpv7gECQQD6jwxBKVutvjRVM8CaFKuMaY6YgLPcOZazAcJj8XWncYbNKbUzfpZ9R/cUzmApPOYBanvsTtkTjCtrCN1S+bK/AkEA6jPwWWlv3rCtZdiNaTB+Wg1z0xNPeydD41RBo+0mAclxhJ4rru07DdJqbCqh92UiY5eUd9jZ9Lt5oT0bqt0SMQJBAJe46X3H0okqBTyWwwlKfPrgrIVyYfd4M+YfqZulik4js7ImkGAnRm+ElXzW32n5Q3oWBgZ5VH++wEfnLUZih7cCQCo42ODVsFReV7RfuQa4tl63xCTOe2rBZmzKWNcbvtldnriA53Eu1ZGlWIhNm0uFkRCRWDPPBWQolEHsSVuhZVECQQCH+im+aQY1XPEbCT1oxtPDkh26seyH5CuLplg0UxlX2T2i5HUANTgFFMAHpYrneERzCDKizEwMy0LACivRRlJz";
				// String privateKey = "123456";
				String oldSign = AccessSysSignUtil.sign(link, keyType, privateKey);
				sign = Base64.encode(oldSign.getBytes());
			//System.out.println("http://10.17.1.203:8311/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("http://10.17.1.207:8311/gw/initPage?" + link + "&sign=" + sign);
			System.out.println("http://localhost:8080/paycenter-web-gateway/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("http://10.17.1.193:8311/gw/initPage?" + link + "&sign=" + sign);
			//System.out.println("https://pay.gdeng.cn/gw/initPage?" + link + "&sign=" + sign);
		}

}
