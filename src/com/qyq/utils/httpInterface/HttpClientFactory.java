package com.qyq.utils.httpInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;


public class HttpClientFactory
{
	private static KeyStore trustStore = null ;
	private static KeyStore keyStore = null ;
	
	private static PoolingHttpClientConnectionManager cm = null;
	private static Logger logger =Logger.getLogger(HttpClientFactory.class); 
	
	private static HttpHost proxy = null ;
	private static boolean isShutdown = false ;

	private static Map<Host, CloseableHttpClient> clients = new HashMap();
	public static void setHttpProxy(HttpHost proxy)
	{
		HttpClientFactory.proxy = proxy ;
	}
	
	public synchronized static void setTrustStore(String cert, String pass)
	{
		if (trustStore == null)
		{
			if (pass != null ||  cert != null )
			{
				// 读取keystore
				FileInputStream instream = null;
				try
				{
					instream = new FileInputStream(new File(cert));
					trustStore = KeyStore
							.getInstance(KeyStore.getDefaultType());
					trustStore.load(instream, pass.toCharArray());
					
				} catch (Exception e)
				{
					logger.error("Error when load trustStore file.", e);
					trustStore = null ;
				}
				finally
				{
					if (instream != null)
					{
						try
						{
							instream.close();
						} catch (Exception e2)
						{
							logger.error("Error when close trustStore file.", e2);
						}
					}
				}
			}
		}
	}
	
	public static synchronized CloseableHttpClient getHttpClient(Host host)
	  {
	    CloseableHttpClient httpClient = (CloseableHttpClient)clients.get(host);

	    if (httpClient == null)
	    {
	      try
	      {
	        PoolingHttpClientConnectionManager cm = PoolManager.getPoolManager().getCm(host);

	        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec("ignoreCookies")
	          .setRedirectsEnabled(false).setConnectTimeout(2000).build();

	        HttpClientBuilder buildert = HttpClients.custom().setConnectionManager(cm).disableConnectionState();

	        httpClient = buildert.setDefaultRequestConfig(globalConfig).build();
	        clients.put(host, httpClient);
	        new IdleConnectionMonitorThread(cm).start();
	      }
	      catch (Exception e)
	      {
	        logger.error("Error when get http client ", e);
	      }

	    }

	    return httpClient;
	  }

	
	public synchronized static void setKeyStore(String cert, String pass)
	{
		if (keyStore == null)
		{
			if (pass != null ||  cert != null )
			{
				// 读取keystore
				FileInputStream instream = null;
				try
				{
					instream = new FileInputStream(new File(cert));
					keyStore = KeyStore
							.getInstance("PKCS12");
					keyStore.load(instream, pass.toCharArray());
					
				} catch (Exception e)
				{
					logger.error("Error when load keystore file.", e);
					keyStore = null ;
				}
				finally
				{
					if (instream != null)
					{
						try
						{
							instream.close();
						} catch (Exception e2)
						{
							logger.error("Error when close keystore file.", e2);
						}
					}
				}
			}
		}
	}
	
	public static class IdleConnectionMonitorThread extends Thread
	{
		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown = false;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr)
		{
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					synchronized (this)
					{
						wait(1000);
						connMgr.closeExpiredConnections();
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex)
			{
				logger.error("Error in IdleConnectionMonitorThread", ex);
			}
		}

		public void shutdown()
		{
			shutdown = true;
			synchronized (this)
			{
				notifyAll();
			}
		}
	}

	private static CloseableHttpClient httpClient = null;

	public static synchronized CloseableHttpClient getHttpClient()
	{
		if (cm == null)
		{
			try
			{
				// logger.info("First time getHttpClient.");
				// 获得密匙库实???
				isShutdown = false ;

				SSLContextBuilder builder = SSLContexts.custom().useProtocol("TLSv1.2") ;
				builder.loadTrustMaterial(trustStore, new TrustStrategy()
				{
					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException
					{
						return true;
					}
				});
				
				builder.loadKeyMaterial(keyStore, "Changeme_123".toCharArray()) ;
				
				SSLContext sslContext = builder.build();
				X509HostnameVerifier verfier = new X509HostnameVerifier()
				{
					public boolean verify(String arg0, SSLSession arg1)
					{
						return true;
					}

					public void verify(String arg0, SSLSocket arg1)
							throws IOException
					{
					}

					public void verify(String arg0, X509Certificate arg1)
							throws SSLException
					{
					}

					public void verify(String arg0, String[] arg1, String[] arg2)
							throws SSLException
					{
					}
				};

				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslContext, verfier);

				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory> create()
						.register("https", sslsf)
						.register("http", PlainConnectionSocketFactory.INSTANCE)
						.build();
				cm = new PoolingHttpClientConnectionManager(
						socketFactoryRegistry);

				// Increase max total connection to 200
				cm.setMaxTotal(100);
				// Increase default max connection per route to 20
				cm.setDefaultMaxPerRoute(100);

				RequestConfig globalConfig = RequestConfig.custom()
						.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
				HttpClientBuilder buildert = HttpClients.custom().setConnectionManager(cm) ;
				buildert.disableConnectionState() ;
				if (proxy != null)
				{
					DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

					httpClient = buildert.setRoutePlanner(routePlanner).
							setDefaultRequestConfig(globalConfig).build();
				}
				else 
				{
					httpClient = buildert.
							setDefaultRequestConfig(globalConfig).build();
				}
				
			} catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}

		}
		return httpClient;
	}

	public static synchronized void shutdown()
	{
		try
		{
			httpClient.close();
			cm.close();

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		finally
		{
			cm = null ;
			trustStore = null ;
			keyStore = null ;
			isShutdown = true ;
		}
	}

}
