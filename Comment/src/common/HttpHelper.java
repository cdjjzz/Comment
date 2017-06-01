package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
	static int defaultConnectTimeout = 30000;
	static int defaultConnectionRequestTimeout = 5000;
	static int defaultSocketTimeout = 30000;
	static String defaultCharset = "UTF-8";
	static PoolingHttpClientConnectionManager cm;
	static CloseableHttpClient httpClient;
	static CloseableHttpClient httpsClient;
	static String EMPTY = "";
	static ContentType APPLICATION_ATOM_XML = ContentType.create(
			"application/atom+xml", Consts.UTF_8);
	static ContentType APPLICATION_FORM_URLENCODED = ContentType.create(
			"application/x-www-form-urlencoded", Consts.UTF_8);
	static ContentType APPLICATION_JSON = ContentType.create(
			"application/json", Consts.UTF_8);
	static ContentType APPLICATION_OCTET_STREAM = ContentType.create(
			"application/octet-stream", (Charset) null);
	static ContentType APPLICATION_SVG_XML = ContentType.create(
			"application/svg+xml", Consts.UTF_8);
	static ContentType APPLICATION_XHTML_XML = ContentType.create(
			"application/xhtml+xml", Consts.UTF_8);
	static ContentType APPLICATION_XML = ContentType.create("application/xml",
			Consts.UTF_8);
	static ContentType MULTIPART_FORM_DATA = ContentType.create(
			"multipart/form-data", Consts.UTF_8);
	static ContentType TEXT_HTML = ContentType
			.create("text/html", Consts.UTF_8);
	static ContentType TEXT_PLAIN = ContentType.create("text/plain",
			Consts.UTF_8);
	static ContentType TEXT_XML = ContentType.create("text/xml", Consts.UTF_8);
	static ContentType DEFAULT_TEXT = TEXT_PLAIN;
	static ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
	static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

		@Override
		public String handleResponse(final HttpResponse response) {
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null && status >= 200 && status < 300) {
				try {
					return EntityUtils.toString(entity, defaultCharset);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return EMPTY;
		}

	};
	static RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(defaultConnectTimeout)
			.setConnectionRequestTimeout(defaultConnectionRequestTimeout)
			.setSocketTimeout(defaultSocketTimeout).build();

	static ConnectionKeepAliveStrategy keepAlive = new DefaultConnectionKeepAliveStrategy() {
		@Override
		public long getKeepAliveDuration(HttpResponse response,
				HttpContext context) {
			long keepAlive = super.getKeepAliveDuration(response, context);
			if (keepAlive == -1) {
				// Keep connections alive 5 seconds if a keep-alive value
				// has not be explicitly set by the server
				keepAlive = 5000;
			}
			return keepAlive;
		}
	};

	static {
		cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(100);
		cm.setValidateAfterInactivity(1000);
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		// Increase max connections for
		IdleConnectionMonitorThread imt = new IdleConnectionMonitorThread(cm);
		imt.start();

		httpClient = HttpClients.custom().setConnectionManager(cm)
				.setKeepAliveStrategy(keepAlive).evictExpiredConnections()
				.evictIdleConnections(5L, TimeUnit.SECONDS).build();

	}

	public static String sendGet(String url) {
		return sendGet(url, null);
	}

	public static String sendGet(String url, Map<String, String> args) {
		try {
			URIBuilder builder = new URIBuilder(url);
			if (args != null) {
				for (String key : args.keySet()) {
					String value = args.get(key);
					builder.setParameter(key, value);
				}
			}
			HttpGet httpget = new HttpGet(builder.build());
			httpget.setConfig(requestConfig);
			return httpClient.execute(httpget, responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		return EMPTY;
	}

	public static String sendPost(String url) {
		return sendPost(url, (Map<String, String>) null);
	}

	public static String sendPost(String url, String data) {
		try {
			HttpPost httpPost = new HttpPost(url);

			StringEntity entity = new StringEntity(data, Consts.UTF_8);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(entity);
			return httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;

	}

	public static String sendPost(String url, String key, String value) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		return sendPost(url, map);
	}

	public static String sendPost(String url, Map<String, String> args) {
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : args.keySet()) {
				String value = args.get(key);
				formparams.add(new BasicNameValuePair(key, value));
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
					Consts.UTF_8);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(entity);
			return httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static String sendPost(String url, String data, String cert,
			String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException,
			KeyManagementException {
		try {
			if (httpsClient == null) {
				httpsClient = getHttpsClient(cert, password);
			}

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(data, defaultCharset));
			return httpsClient.execute(httpPost, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	private static CloseableHttpClient getHttpsClient(String cert,
			String password) throws KeyStoreException, FileNotFoundException,
			IOException, NoSuchAlgorithmException, CertificateException,
			KeyManagementException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream instream = new FileInputStream(new File(cert));
		keyStore.load(instream, password.toCharArray());
		instream.close();
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
				.loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		return HttpClients.custom()
				.setConnectionManager(new BasicHttpClientConnectionManager())
				.setSSLSocketFactory(sslsf).build();
	}

	public static String fluentGet(String url) {
		return fluentGet(url, defaultSocketTimeout, defaultSocketTimeout);
	}

	public static String fluentGet(String url, int socketTimeOut) {
		return fluentGet(url, socketTimeOut, defaultSocketTimeout);
	}

	public static String fluentGet(String url, int connectTimeOut,
			int socketTimeOut) {
		return fluentGet(url, connectTimeOut, socketTimeOut, null, null);
	}

	public static String fluentGet(String url, int connectTimeOut,
			int socketTimeOut, Map<String, String> headers,
			Map<String, String> args) {
		try {
			URIBuilder builder = new URIBuilder(url);
			if (args != null) {
				for (String key : args.keySet()) {
					String value = args.get(key);
					builder.setParameter(key, value);
				}
			}
			Request request = Request.Get(builder.build());
			if (headers != null) {
				for (String name : headers.keySet()) {
					String value = headers.get(name);
					request.addHeader(name, value);
				}
			}
			return request.connectTimeout(connectTimeOut)
					.socketTimeout(socketTimeOut).execute()
					.handleResponse(responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static String fluentPost(String url) {
		return fluentPost(url, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null, null);
	}

	public static String fluentPost(String url, String key, String value) {
		Map<String, String> args = new HashMap<String, String>();
		args.put(key, value);
		return fluentPost(url, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null, args);
	}

	public static String fluentPost(String url, Map<String, String> args) {
		return fluentPost(url, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null, args);
	}

	public static String fluentPost(String url, Map<String, String> args,
			Map<String, String> headers) {
		return fluentPost(url, defaultSocketTimeout,
				defaultConnectionRequestTimeout, headers, args);
	}

	public static String fluentPost(String url, int socketTimeOut) {
		return fluentPost(url, socketTimeOut, defaultConnectionRequestTimeout,
				null, null);
	}

	public static String fluentPost(String url, int socketTimeOut,
			int connectTimeOut) {
		return fluentPost(url, socketTimeOut, connectTimeOut, null, null);
	}

	public static String fluentPost(String url, int socketTimeOut,
			int connectTimeOut, Map<String, String> headers) {
		return fluentPost(url, socketTimeOut, connectTimeOut, headers, null);
	}

	public static String fluentPost(String url, int socketTimeOut,
			int connectTimeOut, Map<String, String> headers,
			Map<String, String> args) {

		Request request = Request.Post(url).useExpectContinue()
				.connectTimeout(connectTimeOut).socketTimeout(socketTimeOut);
		if (headers != null) {
			for (String name : headers.keySet()) {
				String value = headers.get(name);
				request.addHeader(name, value);
			}
		}
		if (args != null) {
			Form form = Form.form();
			for (String name : args.keySet()) {
				String value = args.get(name);
				form.add(name, value);
			}
			request.bodyForm(form.build());

		}
		try {
			return request.execute().handleResponse(responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static String fluentPostText(String url, String text) {
		return fluentPostText(url, text, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostText(String url, String text,
			int socketTimeOut) {
		return fluentPostText(url, text, socketTimeOut,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostText(String url, String text,
			int socketTimeOut, int connectTimeOut) {
		return fluentPostText(url, text, socketTimeOut, connectTimeOut, null);
	}

	public static String fluentPostText(String url, String text,
			int socketTimeOut, int connectTimeOut, Map<String, String> headers) {
		Request request = Request.Post(url).useExpectContinue()
				.connectTimeout(connectTimeOut).socketTimeout(socketTimeOut);
		if (headers != null) {
			for (String name : headers.keySet()) {
				String value = headers.get(name);
				request.addHeader(name, value);
			}
		}
		try {
			return request.bodyString(text, DEFAULT_TEXT).execute()
					.handleResponse(responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static String fluentPostXml(String url, String xml) {
		return fluentPostXml(url, xml, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostXml(String url, String xml, int socketTimeOut) {
		return fluentPostXml(url, xml, socketTimeOut,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostXml(String url, String xml,
			int socketTimeOut, int connectTimeOut) {
		return fluentPostXml(url, xml, socketTimeOut, connectTimeOut, null);
	}

	public static String fluentPostXml(String url, String xml,
			int socketTimeOut, int connectTimeOut, Map<String, String> headers) {
		Request request = Request.Post(url).useExpectContinue()
				.connectTimeout(connectTimeOut).socketTimeout(socketTimeOut);
		if (headers != null) {
			for (String name : headers.keySet()) {
				String value = headers.get(name);
				request.addHeader(name, value);
			}
		}
		try {
			request = request.bodyString(xml, APPLICATION_XML);
			return request.execute().handleResponse(responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static String fluentPostJson(String url, String json) {
		return fluentPostJson(url, json, defaultSocketTimeout,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostJson(String url, String json,
			int socketTimeOut) {
		return fluentPostJson(url, json, socketTimeOut,
				defaultConnectionRequestTimeout, null);
	}

	public static String fluentPostJson(String url, String json,
			int socketTimeOut, int connectTimeOut) {
		return fluentPostJson(url, json, socketTimeOut, connectTimeOut, null);
	}

	public static String fluentPostJson(String url, String json,
			int socketTimeOut, int connectTimeOut, Map<String, String> headers) {
		Request request = Request.Post(url).useExpectContinue()
				.connectTimeout(connectTimeOut).socketTimeout(socketTimeOut);
		if (headers != null) {
			for (String name : headers.keySet()) {
				String value = headers.get(name);
				request.addHeader(name, value);
			}
		}
		try {
			return request.bodyString(json, APPLICATION_JSON).execute()
					.handleResponse(responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					PoolStats s = cm.getTotalStats();
					System.out.println(s);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		for (int i = 0; i < 100; i++) {
			String x = HttpHelper.sendGet("https://www.baidu.com");
			System.out.println(x);
		}
		// PoolStats s = cm.getTotalStats();
		// System.out.println(s.getMax());
		// System.out.println(s.getPending());
		// System.out.println(s.getAvailable());
	}
}

class IdleConnectionMonitorThread extends Thread {
	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// Close expired connections
					connMgr.closeExpiredConnections();
					// Optionally, close connections
					// that have been idle longer than 30 sec
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			// terminate
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}