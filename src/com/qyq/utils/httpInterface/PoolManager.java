package com.qyq.utils.httpInterface;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;


public class PoolManager {

	
	private static Logger logger = Logger.getLogger(PoolManager.class);
	  private static PoolManager inst = new PoolManager();

	  private Map<Host, PoolingHttpClientConnectionManager> cmMap = null;

	  private PoolingHttpClientConnectionManager defaultCm = null;

	  public static PoolManager getPoolManager()
	  {
	    return inst;
	  }

	  private PoolingHttpClientConnectionManager initCm(KeyStore keyStore, String keyPass, KeyStore trustStore, int maxTotal, int maxRoute)
	  {
	    PoolingHttpClientConnectionManager cm = null;
	    try
	    {
	      logger.debug("Init PoolingHttpClientConnectionManager, keyStore=" + keyStore + ",trustStore=" + trustStore);
	      SSLContextBuilder builder = SSLContexts.custom().useProtocol("TLSv1.2");

	      builder.loadTrustMaterial(trustStore, new TrustStrategy()
	      {
	        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
	        {
	          return true;
	        }
	      });
	      if (keyPass == null)
	      {
	        keyPass = "";
	      }
	      builder.loadKeyMaterial(keyStore, keyPass.toCharArray());

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
	      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, verfier);

	      Registry socketFactoryRegistry = RegistryBuilder.create()
	        .register("https", sslsf).register("http", PlainConnectionSocketFactory.INSTANCE).build();
	      cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	      cm.setMaxTotal(maxTotal);
	      cm.setDefaultMaxPerRoute(maxRoute);
	    }
	    catch (Exception e)
	    {
	      logger.error("Error when init connection manager", e);
	    }

	    return cm;
	  }

	  private PoolManager()
	  {
	    this.defaultCm = initCm(null, null, null, 300, 30);
	    this.cmMap = new HashMap();
	  }

	  public void addCm(Host host, KeyStore keyStore, String keyPass, KeyStore trustStore)
	  {
	    if (this.cmMap.containsKey(host))
	      return;
	    PoolingHttpClientConnectionManager cm = initCm(keyStore, keyPass, trustStore, 30, 2);

	    if (cm == null)
	    {
	      logger.error("Add new connection manager failed.");
	      return;
	    }

	    this.cmMap.put(host, cm);
	  }

	  public PoolingHttpClientConnectionManager getCm(Host host)
	  {
	    if (this.cmMap.containsKey(host))
	    {
	      return (PoolingHttpClientConnectionManager)this.cmMap.get(host);
	    }

	    return this.defaultCm;
	  }
}
