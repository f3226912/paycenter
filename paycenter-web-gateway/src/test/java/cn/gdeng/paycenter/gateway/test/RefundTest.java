package cn.gdeng.paycenter.gateway.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.gdeng.paycenter.gateway.util.HttpUtil;
import cn.gdeng.paycenter.util.web.api.AccessSysSignUtil;
import cn.gdeng.paycenter.util.web.api.Base64;

/**
 * 退款测试
 * @author sss
 *
 * since:2016年12月12日
 * version 1.0.0
 */
public class RefundTest {

	//private static String URL = "http://localhost:8080/paycenter-web-gateway/";
	//private static String URL = "http://10.17.1.203:8311/";
	private static String URL = "http://127.0.0.1:8080/paycenter-web-gateway/";
	
	@Test
	public void test退款(){
		Map<String,String> params = new HashMap<>();
		params.put("appKey", "nst_car");
		params.put("payCenterNumber", "2017021605224059");
		params.put("refundRequestNo", "0");
		params.put("refundAmt", "0.01");
		params.put("refundReason", "退款申请");
		params.put("orderNo", "442017021622000006");
		params.put("refundUserId", "nsy");
		params.put("sellerRefundAmt", "0");
		params.put("platRefundAmt", "0");
		params.put("logisRefundAmt", "0");
		String link = AccessSysSignUtil.createLinkString(AccessSysSignUtil.paraFilter(params));
		String sign = "";
		String keyType = "RSA";
		// String keyType = "MD5";
		String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJxxUh7LPQOdnTwW7pFfbmvtMx2K2DwEyynGq+HY97R7PZpWzX0PozFrD1EkR6jZMwtBEHO6ljtabgxSVfGqVdCS4Y4NFc2KGuF7DsZCME41UcmX2+4ySpKAfi/VQYfG/zP03Kov64K2FpKIik6RlhAaLqHJFIymfiXQo9E9hiWtAgMBAAECgYANsdnXekEgFOcTwIonzavT5NLJrkLZli3WvV6EicK9WOB+p0SbwwetJssTdlKTlFfkj1CKYPYPwg3KJFcDrb6PPZJ8YerAjDQKk3kDdkLbGxmYCGbp9Rhn+skEUnphKgZdtmlGE/BmzRdQl1453oQGFksGt+GIPhVGajlgG5AKXQJBAMnOKs57StEL1t1w0TGcmjkhWcWNj1dNZ4kxLajMhd0Auj4wpg6ZHC4qWaRGXkLjWeiO3vJhV7rBHHgF1rFBKocCQQDGdIh8iKQO138DX/q4NcGNZ5Wd91ciovtvM7PiLHeQ2+eVm3B24v1J785kQFBNXTuDGZSRs8gDog/tk6f0RzcrAkEAuQ3egSPcMqA5syxeGLFzVwo3KQortLHdTJgVN6H86vFc1+SCkgZjKg5Zz2Nb0thqvPAuj3MmILIu6fzop/iLBwJBALlJ8mj7ltl4oFEBWvCE2DrzPMEpPwTK0SDpUAla5SOt/dI0N9P4aO6QZM47Mf6Zjsf/qTREe/nQZTi3RPgRSdMCQQCQ8TrhfkVMjMfyj9npfOSMDxPi1ck2nL6YD8pb6OB22I4edruJb7iaAdmmYLejXSXHD7JKzCMlhuPgBToDnU1R";
		//String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOU5lvCfM42Cz2LXz6+4T4iNzU2gZDOGMtWhxYi8HjBmJaPhZBJBy1vXRBAvVOEB7YYej3xbn7hR5NhiOjcAnRgvus9Quk3XcZFjlrFVPsbSQeBXP6Xsigyw13uwlwA7nkN50I2XJ8xlXUO1VBly/2t2AN3N2z2mU2dTEZmVP6SPAgMBAAECgYEAiESkJod97jJFd000ehHnthYWZn9jEf5FWhrBg3fRupP+7F2hT/ktG3vtSnHY06qBuamRQyx01u5YTQqX8Pq8Z/RJP0pqUlqlTieznTeuXEsQXr0rW4uw4chADxKOC7u6q4MxJPqMhaQa/r6BKGweeDYyQ4sC4PTzqJrNkrpv7gECQQD6jwxBKVutvjRVM8CaFKuMaY6YgLPcOZazAcJj8XWncYbNKbUzfpZ9R/cUzmApPOYBanvsTtkTjCtrCN1S+bK/AkEA6jPwWWlv3rCtZdiNaTB+Wg1z0xNPeydD41RBo+0mAclxhJ4rru07DdJqbCqh92UiY5eUd9jZ9Lt5oT0bqt0SMQJBAJe46X3H0okqBTyWwwlKfPrgrIVyYfd4M+YfqZulik4js7ImkGAnRm+ElXzW32n5Q3oWBgZ5VH++wEfnLUZih7cCQCo42ODVsFReV7RfuQa4tl63xCTOe2rBZmzKWNcbvtldnriA53Eu1ZGlWIhNm0uFkRCRWDPPBWQolEHsSVuhZVECQQCH+im+aQY1XPEbCT1oxtPDkh26seyH5CuLplg0UxlX2T2i5HUANTgFFMAHpYrneERzCDKizEwMy0LACivRRlJz";
		// String privateKey = "123456";
		String oldSign = AccessSysSignUtil.sign(link, keyType, privateKey);
		sign = Base64.encode(oldSign.getBytes());
		params.put("sign", sign);
		String str = HttpUtil.httpClientPost(URL+"gw/refund", params);
		System.out.println(str);
	}
	
	
	@Test
	public void test退款查询(){
		Map<String,String> params = new HashMap<>();
		params.put("appKey", "nsy");
		params.put("refundRequestNo", "0");
		params.put("orderNo", "412016000216543");
		String str = HttpUtil.httpClientPost(URL+"api/refundQuery", params);
		System.out.println(str);
	}
}
