package cn.gdeng.paycenter.util.server.sign;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description 发送邮件工具类
 * @Project gd-task-service
 * @ClassName MailUtil.java
 * @Author lidong(dli@gdeng.cn)
 * @CreationDate 2015年12月23日 下午5:14:27
 * @Version V2.0
 * @Copyright 谷登科技 2015-2015
 * @ModificationHistory
 */
public class MailUtil {

	private static final String AUTH = "true";
	private static final String HOST = "smtp.gdeng.cn";
	private static final String USER = "gdsystem@gdeng.cn";
	private static final String PASSWORD = "xHpwGcH8";
	private static final int PORT = 25;

	/**
	 * @Description 发送邮件
	 * @param subject
	 *            邮件主题（标题）
	 * @param content
	 *            邮件内容
	 * @param TOMail
	 *            收件人列表
	 * @param CCMail
	 *            抄送人列表(可选参数)
	 * @param BCCMail
	 *            密送人列表(可选参数)
	 * @param contentType
	 *            邮件内容类型(可选参数)
	 * @param conf
	 *            邮件服务器，发件邮箱账号等参数配置(可选参数)
	 * @return 发送成功则返回true
	 * @CreationDate 2015年12月23日 下午5:14:35
	 * @Author lidong(dli@gdeng.cn)
	 */
	public static boolean sendMail(String subject, String content, List<String> TOMail, List<String> CCMail, List<String> BCCMail, Map<String, Object> conf, String contentType) {
		// 配置发送邮件的环境属性
		final Properties props = new Properties();
		if (conf != null) {
			props.put("mail.smtp.auth", conf.get("auth") == null ? MailUtil.AUTH : conf.get("auth"));
			// 发件邮件服务器
			props.put("mail.smtp.host", conf.get("host") == null ? MailUtil.HOST : conf.get("host"));
			// 发件人的账号
			props.put("mail.user", conf.get("user") == null ? MailUtil.USER : conf.get("user"));
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", conf.get("password") == null ? MailUtil.PASSWORD : conf.get("password"));
			props.put("mail.smtp.port", conf.get("port") == null ? MailUtil.PORT : conf.get("port"));
		} else {
			props.put("mail.smtp.auth", MailUtil.AUTH);
			// 发件邮件服务器
			props.put("mail.smtp.host", MailUtil.HOST);
			// 发件人的账号
			props.put("mail.user", MailUtil.USER);
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", MailUtil.PASSWORD);
			props.put("mail.smtp.port", MailUtil.PORT);
		}
		// 构建授权信息，用于进行SMTP进行身份验证
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		try {
			// 设置发件人
			InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
			message.setFrom(form);
			// 设置收件人
			InternetAddress[] to = null;
			if (TOMail != null && TOMail.size() > 0) {
				to = new InternetAddress[TOMail.size()];
				for (int i = 0; i < TOMail.size(); i++) {
					to[i] = new InternetAddress(TOMail.get(i));
				}
			} else {
				return false;
			}
			message.setRecipients(Message.RecipientType.TO, to); // 收件人
			// 设置抄送
			InternetAddress[] cc = null;
			if (CCMail != null && CCMail.size() > 0) {
				cc = new InternetAddress[CCMail.size()];
				for (int i = 0; i < CCMail.size(); i++) {
					cc[i] = new InternetAddress(CCMail.get(i));
				}
			}
			message.setRecipients(RecipientType.CC, cc);
			// 设置密送，其他的收件人不能看到密送的邮件地址
			InternetAddress[] bcc = null;
			if (BCCMail != null && BCCMail.size() > 0) {
				bcc = new InternetAddress[BCCMail.size()];
				for (int i = 0; i < BCCMail.size(); i++) {
					bcc[i] = new InternetAddress(BCCMail.get(i));
				}
			}
			message.setRecipients(RecipientType.BCC, bcc);
			// 设置邮件标题
			message.setSubject(subject);
			// 设置邮件的内容体
			message.setContent(content, StringUtils.isEmpty(contentType) ? "text/html;charset=UTF-8" : contentType);
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		List<String> TOMail = new ArrayList<>();
		List<String> CCMail = new ArrayList<>();
		List<String> BCCMail = new ArrayList<>();
		TOMail.add("nfzhang@gdeng.cn");
		System.out.println(sendMail("主题测试-测试发送、抄送、密送", "测试", TOMail, CCMail, BCCMail, null, null));

	}
}
