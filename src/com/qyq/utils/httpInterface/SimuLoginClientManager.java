package com.qyq.utils.httpInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;




public class SimuLoginClientManager {

private static Logger logger = Logger.getLogger(SimuLoginClientManager.class);
	
	private static Map<String, SimuLoginClient> clients = new HashMap<String, SimuLoginClient>();
	
//	public static SimuLoginClient getClient(String clientId)
//	{
//		if( clients.get(clientId) == null)
//		{
//			createClient(ip, port, user, pass)
//		}
//	}
	public static SimuLoginClient getClient(String clientId)
	{
		return clients.get(clientId);
	}
	
	/**
	 * @author tWX463561 田亚强
	 * @date 2017-06-09-17:32
	 * @todo 调试文件上传时，登录失效的问题，使其每次都重新登录，不再复用前一次登录的状态
	 * @comment	注释如下行号代码：40，41,43
	 */
	public static SimuLoginClient getClient(String ip, String user, String pwd, String port)
	{
		String clientId = String.format("#%s#%s#%s#", ip, port, user);
		
//		if( clients.get(clientId) == null)
//		{
			createClient(ip, port, user, pwd);
//		}
		
		return clients.get(clientId);
	}
	
	public static Set<String> getAllClientId()
	{
		return clients.keySet();
	}
	
	public static synchronized void deleteClient(String clientId)
	{
		if (clientId ==  null)
		{
			return ;
		}
		try
		{
			logger.info("Delete client:" + clientId);
			clients.get(clientId).exit();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		clients.remove(clientId) ;
		
	}
	
	public static String createClient(String ip, String port, String user, String pass)
	{
		String clientId = "" ;
		try
		{
	
			SimuLoginClient simuClient = new SimuLoginClient(ip, port,  user, pass);
			if(!simuClient.getCookie().equals(""))
			{
	           clientId = String.format("#%s#%s#%s#", ip, port, user);
			   clients.put(clientId, simuClient) ;
			  logger.debug("Create simuLoginClient Success: " + clientId );
			}
			else
			{
		    	logger.debug("Create simuLoginClient Failed: " + clientId );
			}
		}
		catch (Exception e)
		{
			logger.error("Error when login", e);
		}

		return clientId;
	}


}
