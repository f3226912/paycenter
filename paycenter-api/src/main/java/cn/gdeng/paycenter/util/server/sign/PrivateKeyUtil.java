package cn.gdeng.paycenter.util.server.sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 密钥获取工具类
 * @author zhangnf20161109
 *
 */
public class PrivateKeyUtil {
	/**私钥文件名**/
	private static final String privateKeyStore="privateKey.keystore";
	private static final String publicKeyStore="publicKey.keystore";
	private static String privateKey = null;
	private static String publicKey = null;

	public static String getPrivateKey(String filePath)throws Exception {
		if(null == filePath){
			return null;
		}
		if(null == privateKey){
			privateKey = redFile(filePath+privateKeyStore);
		}
		return privateKey;
	}
	
	public static String getPublicKey(String filePath)throws Exception {
		if(null == filePath){
			return null;
		}
		if(null == publicKey){
			publicKey = redFile(filePath+publicKeyStore);
		}
		return publicKey;
	}

	private static String redFile(String filePath) throws Exception {
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String temp = null;
		StringBuffer sb = new StringBuffer();
		temp = br.readLine();
		while (temp != null) {
			sb.append(temp);
			temp = br.readLine();
		}
		br.close();
		return sb.toString();
	}
}
