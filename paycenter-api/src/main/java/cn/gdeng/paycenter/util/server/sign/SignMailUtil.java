package cn.gdeng.paycenter.util.server.sign;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SignMailUtil {
	private static final String[] PRD_MAILS = {"nfzhang@gdeng.cn","yxzhang@gdeng.cn","mpan@gdeng.cn","yyqiu@gdeng.cn","lpliu@gdeng.cn","ymsun@gdeng.cn","wwji@gdeng.cn","xxzhang@gdeng.cn","kxiong@gdeng.cn"};
	private static final String[] TEST_MAILS = {"nfzhang@gdeng.cn","yxzhang@gdeng.cn","mpan@gdeng.cn","xxzhang@gdeng.cn"};
	private static final String[] DEV_MAILS = {"nfzhang@gdeng.cn"};
	
	/**
	 * 签名验证异常邮件
	 * @param subject
	 * @param mailMsg
	 */
	public static void sendMail(String subject,String mailMsg){
		String content = "";
		if(null == mailMsg
				|| "".equals(mailMsg.trim())){
			subject += "-正常";
			content += "未发现异常数据";
		}else{
			subject += "-警告-发现异常";
			content += "异常数据如下(也可查看表sign_error):<br>";
		}
		MailUtil.sendMail(subject+getLocalIPS(), content+mailMsg, getToMailList(), new ArrayList<String>(), new ArrayList<String>(), null, null);
	}
	/**
	 * 批量签名邮件
	 * @param subject
	 * @param mailMsg
	 */
	public static void sendBatchSignMail(String subject,String mailMsg){
		subject += "(如果非运维人员正常上线操作，请查明原因)";
		String content = " 如果非运维人员正常上线操作，请查明原因<br/>被重新签名的表如下:<br/>";
		MailUtil.sendMail(subject+getLocalIPS(), content+mailMsg, getToMailList(), new ArrayList<String>(), new ArrayList<String>(), null, null);
	}
	
	private static List<String> getToMailList(){
		String[] mails ;
		String ip = getLocalIPS();
		if(ip.indexOf("10.17.") > 1){
			if(ip.indexOf("10.17.1.192")>1
					|| ip.indexOf("10.17.1.225")>1){
				mails = TEST_MAILS;
			}else{
				mails = DEV_MAILS;
			}
		}else{
			mails = PRD_MAILS;
		}
		List<String> TOMail = new ArrayList<>();
		for(String mail : mails){
			TOMail.add(mail);
		}
		return TOMail;
	}
	/**
	 * 此方法描述的是：获得服务器的IP地址(多网卡)
	 */
	public static String getLocalIPS() {
		InetAddress ip = null;
		StringBuilder ipList = new StringBuilder("ip:");
		try {
			Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
					.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = (InetAddress) ips.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
						ipList.append(ip.getHostAddress()+" ");
					}
				}
			}
		} catch (Exception e) {
			ipList.append("unknownHost");
		}
		return ipList.toString();
	}
	public static void main(String[] args){
		System.out.println(getLocalIPS()+"--"+getLocalIPS().indexOf("i"));
	}
	
}
