package cn.gdeng.paycenter.server.pay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public final class HttpUtil {

	private HttpUtil() {
	}

	public static final String httpClientGET(String url) {
		String result = "";
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
		GetMethod getMethod = new GetMethod(url);
		try {
			client.executeMethod(getMethod);
			result = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}

	public static final String httpClientPost(String url, ArrayList<NameValuePair> list) {
		String result = "";
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
		PostMethod postMethod = new PostMethod(url);
		try {
			NameValuePair[] params = new NameValuePair[list.size()];
			for (int i = 0; i < list.size(); i++) {
				params[i] = list.get(i);
			}
			postMethod.addParameters(params);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	public static final String httpClientPost(String url, Map<String, String> map) {
		String result = "";
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
		PostMethod postMethod = new PostMethod(url);
		try {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				list.add(new NameValuePair(key, map.get(key)));
			}

			NameValuePair[] params = new NameValuePair[list.size()];
			for (int i = 0; i < list.size(); i++) {
				params[i] = list.get(i);
			}
			postMethod.addParameters(params);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	public static final String httpClientPost(String url, String params) {
		String result = "";
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
		PostMethod postMethod = new PostMethod(url);
		try {
			// postMethod.addRequestHeader("Content-Type",
			// "text/html;charset=UTF-8");
			RequestEntity requestEntity = new StringRequestEntity(params, "text/html", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	/**
	 * 双向认证 请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @param certName
	 *            退款证书的路径
	 * @param certKey
	 *            证书秘钥
	 * @return
	 * @throws Exception
	 */
	public static final String httpCertClientPost(String url, String requestXml, String certUrl, String certKey) {

		StringBuilder sb = new StringBuilder();
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(certUrl));
			try {
				keyStore.load(instream, certKey.toCharArray());
			} finally {
				instream.close();
			}
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certKey.toCharArray()).build();
			// Allow TLSv1 protocol only
			@SuppressWarnings("deprecation")
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			try {

				HttpPost httpPost = new HttpPost(url);

				StringEntity reqEntity = new StringEntity(requestXml);

				// 设置类型
				reqEntity.setContentType("text/html");
				httpPost.setEntity(reqEntity);

				CloseableHttpResponse response = httpclient.execute(httpPost);
				try {
					HttpEntity entity = response.getEntity();

					System.out.println(response.getStatusLine());
					if (entity != null) {
						System.out.println("Response content length: " + entity.getContentLength());
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String text;
						while ((text = bufferedReader.readLine()) != null) {
							sb.append(text);
						}

					}
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
