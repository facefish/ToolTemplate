package com.qyq.utils.httpInterface;


import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;





/**
 * 目前登录uTraffic R6C00以及R6C10版本
 * @author y00358428
 *
 */
public class SimuLoginClient {
	

	private String loginCookie = "";
	static String value="";
	static String cookieString="";
	static String location="";
	private String loginTicket= "";
    private Map<String,String> loginHeaders = null;
    public String set_cookie = "";
    
	private Header[] headers = null;

	public SimuLoginClient(String ip, String port,String user, String pass)
	{		
		
		
	
		
	}
	
	
	
	
		
	public String getCookie()
	{
		return loginCookie;
	}
	
	public String getTicket()
	{
		return loginTicket;
	}
	
	public Map<String,String> getHeaders()
	{
		
       for(Header h: headers)
       {
    	   loginHeaders.put(h.getName(), h.getValue());
       }
       
       return loginHeaders;
	}

	/**
	 * 获取登录过程中的set-cookie，注意不是登录cookie
	 * @return
	 */
	public String getSetCookie()
	{
		return set_cookie;
	}
	
	public void exit()
    {
	    // TODO 自动生成的方法存根
	    
    }

}
