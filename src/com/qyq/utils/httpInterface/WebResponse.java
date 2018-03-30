package com.qyq.utils.httpInterface;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;

public class WebResponse
{
	private StatusLine statusLine ;
	private String content ;
	private Header[] headers  ;
	private int statusCode;
	private HttpContext context;
	public StatusLine getStatusLine()
	{
		return statusLine;
	}

	public void setStatusLine(StatusLine statusLine)
	{
		this.statusLine = statusLine;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Header[] getHeaders()
	{
		return headers;
	}
	

	public void setHeaders(Header[] headers)
	{
		this.headers = headers;
	}

	public int getStatusCode(){
		return this.statusCode = statusLine.getStatusCode();
	}
	
	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}
	
	public HttpContext getHttpContext(){
		return this.context;
	}
	
	public void setHttpContext(HttpContext context){
		this.context = context;
	}

}
