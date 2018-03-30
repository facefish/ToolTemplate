package com.qyq.utils.httpInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.qyq.utils.LogManager.LoggerTool;




/*
 * 接口模拟下发，默认不允许重定向
 */
public class WebClient
{
	
	public static HttpContext httpContext = new BasicHttpContext();  //HttpContext实际上是客户端用来在多次请求响应的交互中,保持状态信息的 
	
	public static void setKeyStore(String keyStore, String keyPass)
	{
		HttpClientFactory.setKeyStore(keyStore, keyPass);
	}
	
	private static Host getHost(URI uri)
	 {
	    Host host = new Host(uri.getHost(), uri.getPort());
	    return host;
	 }
	
	
	/**
	 * get方法， query参数直接放在url里， 不允许自动转发
	 * @param url
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static WebResponse get(String url, Map<String, String> headers) throws Exception
	{
		
		return get(url, headers, null);
	}
	
	
	
	/**
	 * get方法， query参数直接放在url里
	 * @param url
	 * @param headers
	 * @param config   是否允许转发, 填null表示不允许转发
	 * @return
	 * @throws Exception
	 */
	public static WebResponse get(String url, Map<String, String> headers,
			RequestConfig config) throws Exception
	{
		return get(url, headers, null, config);
	}
	
	
	 /**
	  * get方法,query参数单独传入
	  * @param url  不带query参数
	  * @param headers   请求头
	  * @param params     query参数，采用map保存，没有可填null
	  * @param config     是否允许转发, 填null表示不允许转发
	  * @return WebResponse
	  * @throws Exception
	  */
	 public static WebResponse get(String url, Map<String, String> headers, Map<String, String> queryParams, RequestConfig config)
	    throws Exception
	  {
		     if ((queryParams != null) && (queryParams.size() != 0))
		    {
		      url = url + "?" + encodeParams(queryParams);
		    }
	
		    URI uri = new URI(url);
		    HttpClient httpClient = HttpClientFactory.getHttpClient(getHost(uri));
		    HttpGet request = new HttpGet();
	
		    request.setURI(uri);
		 
		    if (config == null)
			{
				 config = RequestConfig.custom().setRedirectsEnabled(false).build();
			}
			request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
	
			 
		    if (headers != null)
		    {
		      for (String key : headers.keySet())
		      {
		        request.addHeader(key, (String)headers.get(key));
		      }
		      if (!headers.containsKey("Content-Type"))
		      {
		        request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		      }
		      
		    
		    }
		    
		    HttpContext httpContext = new BasicHttpContext();  
		    HttpResponse response = httpClient.execute(request,httpContext);
		    WebResponse res = convert(response,httpContext);  
		    return res;
	  }
	 
	 
	 /**
		 * put方法，body为map格式，不允许重定向
		 * @param url
		 * @param headers
		 * @param params
		 * @return
		 * @throws Exception
		 */
		public static WebResponse put(String url, Map<String, String> headers,
				Map<String, String> body) throws Exception
		{
			return put(url,headers,body,null);
		}
		
		
		/**
		 * put方法， body参数为json格式，不允许重定向
		 * @param url
		 * @param headers
		 * @param body
		 * @return
		 * @throws Exception
		 */
		public static WebResponse put(String url, Map<String, String> headers,
				String body) throws Exception
		{
			return put(url,headers,body,null);
		}


	
	/**
	 * put方法， body参数为json格式
	 * 可通过RequestConfig参数来配置是否允许重定向
	 * @param url
	 * @param headers
	 * @param body  json格式
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static WebResponse put(String url, Map<String, String> headers,
			String body, RequestConfig config) throws Exception
	{
				
				if (headers != null)
				{
					if(!headers.containsKey("Content-Type"))
					{
						headers.put("Content-Type", "application/json; charset=UTF-8") ;
					}			
				}
				
				HttpClient httpClient = HttpClientFactory.getHttpClient();
				HttpPut request = new HttpPut();
				request.setURI(new URI(url));
		
				if (config == null)
				{
					 config = RequestConfig.custom().setRedirectsEnabled(false).build();
				}
				request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
				
		
				if (body != null && body.length() > 0)
				{
					StringEntity stringEntity = new StringEntity(body, "UTF-8") ;
					request.setEntity(stringEntity);
				}
		
		
				if (headers != null)
				{
					for (String key : headers.keySet())
					{
						request.addHeader(key, headers.get(key));
					}
				}
		
				HttpContext httpContext = new BasicHttpContext();  
			    HttpResponse response = httpClient.execute(request,httpContext);
			    WebResponse res = convert(response,httpContext);  
			    return res;
	}

	
	
	/**
	 * put方法，body参数为键值对（map）格式
	 * 可通过RequestConfig参数来配置是否允许重定向
	 * @param url
	 * @param headers
	 * @param body
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static WebResponse put(String url, Map<String, String> headers,
			Map<String, String> body, RequestConfig config) throws Exception
	{
				if (headers != null)
				{
					if(!headers.containsKey("Content-Type"))
					{
						headers.put("Content-Type", "application/x-www-form-urlencoded") ;
					}	
				}
				
				HttpClient httpClient = HttpClientFactory.getHttpClient();
				HttpPut request = new HttpPut();
				request.setURI(new URI(url));
		
				if (config == null)
				{
					 config = RequestConfig.custom().setRedirectsEnabled(false).build();
				}
				 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
				
		
				if (body != null)
				{
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					for (String key : body.keySet())
					{
						paramList.add(new BasicNameValuePair(key, body.get(key)));
					}
					request.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
				}
		
				if (headers != null)
				{
					for (String key : headers.keySet())
					{
						request.addHeader(key, headers.get(key));
					}
				}
		
				HttpContext httpContext = new BasicHttpContext();  
			    HttpResponse response = httpClient.execute(request,httpContext);
			    WebResponse res = convert(response,httpContext);  
			    return res;
	}

	
	
	
	
	
	/**post方法,body参数为json格式， 不允许重定向
	 * 
	 * @param url
	 * @param headers
	 * @param body  body格式为json
	 * @return
	 * @throws Exception
	 */
	public static WebResponse post(String url, Map<String, String> headers,String body)throws Exception
	{
		return post(url, headers, body, null);
	}
	
	
	/**post方法,body参数为Map格式， 不允许重定向
	 * 
	 * @param url
	 * @param headers
	 * @param body  body格式为Map<String,String>格式
	 * @return
	 * @throws Exception
	 */
	public static WebResponse post(String url, Map<String, String> headers,Map<String,String> body)throws Exception
	{
		return post(url, headers, body, null);
	}
	
	
	/**post方法， body参数为json格式
	 * 可通过RequestConfig参数配置是否允许重定向
	 * @param url
	 * @param headers ，不配置Content-Type时默认为json
	 * @param body
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static WebResponse post(String url, Map<String, String> headers,String body, RequestConfig config) throws Exception
	{
		
		if (headers != null)
		{
			if(!headers.containsKey("Content-Type"))
			{
				headers.put("Content-Type", "application/json; charset=UTF-8") ;
			}			
		}
		else
		{
			headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json; charset=UTF-8") ;
		}
		
		HttpClient httpClient = HttpClientFactory.getHttpClient();
		HttpPost request = new HttpPost();
		request.setURI(new URI(url));

		if (config == null)
		{
			config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		

		if (body != null && body.length() > 0)
		{
			StringEntity stringEntity = new StringEntity(body, "UTF-8") ;
			request.setEntity(stringEntity);
		}
	
		if (headers != null)
		{
			for (String key : headers.keySet())
			{
				request.addHeader(key, headers.get(key));
			}
		}

		HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
	}
	
	/**
	 * post方法， body参数为map格式
	 * 可通过RequestConfig参数配置是否允许重定向
	 * @param url
	 * @param headers
	 * @param body
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static WebResponse post(String url, Map<String, String> headers,
			Map<String, String> body, RequestConfig config) throws Exception
	{
		
		if (headers != null)
		{
			if(!headers.containsKey("Content-Type"))
			{
				headers.put("Content-Type", "application/x-www-form-urlencoded") ;
			}	
		}
		HttpClient httpClient = HttpClientFactory.getHttpClient();
		HttpPost request = new HttpPost();
		request.setURI(new URI(url));

		if (config == null)
		{
			 config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		

		if (body != null)
		{
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : body.keySet())
			{
				paramList.add(new BasicNameValuePair(key, body.get(key)));
			}
			request.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		}

		if (headers != null)
		{
			for (String key : headers.keySet())
			{
				request.addHeader(key, headers.get(key));
			}
			
		}
		
		HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
		
	}
	
	
	
	
	/**
	 * Delete方法， body参数为JSON格式
	 * 可通过RequestConfig参数配置是否允许重定向
	 * @param url
	 * @param headers
	 * @param body  为Json格式字符串，没有填""
	 * @param config   如  RequestConfig  config = RequestConfig.custom().setRedirectsEnabled(false).build();
	 * @return WebResponse
	 * @throws Exception
	 */
	public static WebResponse delete(String url, Map<String, String> headers, String body, 
			RequestConfig config) throws Exception
	{
	
		if (headers != null)
		{
				if(!headers.containsKey("Content-Type"))
				{
					headers.put("Content-Type", "application/json; charset=UTF-8") ;
				}	
		}
		HttpClient httpClient = HttpClientFactory.getHttpClient();
		RestfulHttpDelete request = new RestfulHttpDelete();
		request.setURI(new URI(url));

		if (config == null)
		{
			 config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		
		
		if (body != null && body.length() > 0)
		{
			StringEntity stringEntity = new StringEntity(body, "UTF-8") ;
			request.setEntity(stringEntity);
		}

		if (headers != null)
		{
			for (String key : headers.keySet())
			{
				request.addHeader(key, headers.get(key));
			}
		}

		HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
	}
	

	/**
	 * Delete方法， body参数为Map格式
	 * 可通过RequestConfig参数配置是否允许重定向
	 * @param url
	 * @param headers
	 * @param body  为Map<String,String>格式，没有填""
	 * @param config   如  RequestConfig  config = RequestConfig.custom().setRedirectsEnabled(false).build();
	 * @return WebResponse
	 * @throws Exception
	 */
	public static WebResponse delete(String url, Map<String, String> headers,
			Map<String, String> body, RequestConfig config) throws Exception
	{
		
		if (headers != null)
		{
			if(!headers.containsKey("Content-Type"))
			{
				headers.put("Content-Type", "application/x-www-form-urlencoded") ;
			}	
		}
		HttpClient httpClient = HttpClientFactory.getHttpClient();
		RestfulHttpDelete request = new RestfulHttpDelete();
		request.setURI(new URI(url));

		if (config == null)
		{
			 config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		

		if (body != null)
		{
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : body.keySet())
			{
				paramList.add(new BasicNameValuePair(key, body.get(key)));
			}
			request.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		}

		if (headers != null)
		{
			for (String key : headers.keySet())
			{
				request.addHeader(key, headers.get(key));
			}
		}

		HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
	}

	
	public static WebResponse convert(HttpResponse response, HttpContext httpContext)
	{
		WebResponse webResponse = new WebResponse();
		webResponse.setHeaders(response.getAllHeaders());
		webResponse.setHttpContext(httpContext);

		if(!"HTTP/1.1 204 No Content".equals(response.getStatusLine().toString())){
			try
			{
				BufferedReader bReader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent(), Charset.forName("UTF8")));
				StringBuffer sb = new StringBuffer();
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = bReader.readLine()) != null)
				{
					sb.append(line + NL);
				}
				webResponse.setContent(sb.toString());
				bReader.close();
			}
			catch (Exception e)
			{
				LoggerTool.error( e);
			}
			finally
			{
				try
				{
					response.getEntity().getContent().close();
				}
				catch (Exception e2)
				{
				}
			}
		}else{
			webResponse.setContent("");
		}
		
		
		webResponse.setStatusLine(response.getStatusLine());

		return webResponse;
	}


    /** 
     * 上传文件 
     *  
     * @throws ParseException 
     * @throws IOException 
     */  
    public static WebResponse postFile(String url, String fileName, Map<String, String> headers,RequestConfig config) throws ClientProtocolException, IOException {  
        HttpClient httpClient = HttpClientFactory.getHttpClient();
        HttpPost request = new HttpPost(url);  
        FileBody fileBody = new FileBody(new File(fileName));  
        StringBody stringBody = new StringBody("uploadfile");  
        MultipartEntity entity = new MultipartEntity();  
        entity.addPart("uploadfile", fileBody);  
        entity.addPart("desc", stringBody);  
        request.setEntity(entity);  
        
        if (config == null)
		{
			 config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		 

        if (headers != null)
        {
            for (String key : headers.keySet())
            {    
                if(!key.contains("Content-Type"))
                request.addHeader(key, headers.get(key));
            }
        }
        HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
    } 
    
    
    public static WebResponse postFile(String url, String name,String fileName, Map<String, String> headers,RequestConfig config) throws ClientProtocolException, IOException {  
        HttpClient httpClient = HttpClientFactory.getHttpClient();
        HttpPost request = new HttpPost(url);  
        FileBody fileBody = new FileBody(new File(fileName));  
        StringBody stringBody = new StringBody("upload");  
        MultipartEntity entity = new MultipartEntity();  
//        entity.addPart("file", fileBody);  
        entity.addPart(name, fileBody);  
        entity.addPart("desc", stringBody);  
        request.setEntity(entity);  
        
        if (config == null)
		{
			 config = RequestConfig.custom().setRedirectsEnabled(false).build();
		}
		 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
		 

        if (headers != null)
        {
            for (String key : headers.keySet())
            {    
                if(!key.contains("Content-Type"))
                request.addHeader(key, headers.get(key));
            }
        }
        HttpContext httpContext = new BasicHttpContext();  
	    HttpResponse response = httpClient.execute(request,httpContext);
	    WebResponse res = convert(response,httpContext);  
	    return res;
    } 
	  
    /**
     * post以Content-Type = multipart/form-data上传文件
     * 常见与导入license接口
     * @param url
     * @param builder
     * @param headers
     * @return
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static WebResponse postFile(String url,MultipartEntityBuilder builder, Map<String, String> headers) throws ClientProtocolException, IOException
    {
    	
    		   HttpClient httpClient = HttpClientFactory.getHttpClient();
    		   HttpPost request = new HttpPost(url);  
    		
               HttpEntity multipart = builder.build();
    		   
               if (headers != null)
               {
                   for (String key : headers.keySet())
                   {                         
                       request.addHeader(key, headers.get(key));
                   }
               }
    	
    		   request.setEntity(multipart);

    		HttpContext httpContext = new BasicHttpContext();  
   		    HttpResponse response = httpClient.execute(request,httpContext);
   		    WebResponse res = convert(response,httpContext);  
   		    return res;
    
    }
    
    
    /**
	 * query参数URL编码处理
	 * @param parm
	 * @return
	 * @throws Exception
	 */
	private static String encodeParams(Map<String, String> parm) throws Exception
	  {
	    String value = null;
	    boolean bHasParma = false;
	    StringBuilder builder = new StringBuilder();
	    try
	    {
	      for (String key : parm.keySet())
	      {
	        value = (String)parm.get(key);
	        if (value == null)
	        {
	          value = "";
	        }
	        String str = null;

	        if (bHasParma)
	        {
	          str = String.format("&%s=%s", new Object[] { URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(value, "UTF-8") });
	        }
	        else
	        {
	          bHasParma = true;
	          str = String.format("%s=%s", new Object[] { URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(value, "UTF-8") });
	        }
	        builder.append(str);
	      }
	    }
	    catch (UnsupportedEncodingException ex)
	    {
	      LoggerTool.error(ex);
	    }

	    return builder.toString();
	  }
	
	
	public static WebResponse post(String url, Map<String, String> headers, MultipartEntity entity, RequestConfig config)
		    throws Exception
		   {
				  
				    HttpPost request = new HttpPost();
				    URI uri = new URI(url);
				    request.setURI(uri);
				
				    if (config == null)
					{
						 config = RequestConfig.custom().setRedirectsEnabled(false).build();
					}
					 request.setConfig(RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());	
					
				
				    if (entity != null)
				    {
				      request.setEntity(entity);
				    }
				
				    if (headers != null)
				    {
				      for (String key : headers.keySet())
				      {
				        request.addHeader(key, (String)headers.get(key));
				      }
				    }
				    request.addHeader(entity.getContentType());
				    HttpClient httpClient = HttpClientFactory.getHttpClient(getHost(uri));

				    HttpContext httpContext = new BasicHttpContext();  
				    HttpResponse response = httpClient.execute(request,httpContext);
				    return convert(response,httpContext);  

		  }
			
  
}
