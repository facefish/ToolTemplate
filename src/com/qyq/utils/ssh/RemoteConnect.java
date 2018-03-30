package com.qyq.utils.ssh;


import ch.ethz.ssh2.Connection;

/*import java.io.BufferedReader;
import java.io.BufferedWriter;*/
import java.io.IOException;
import java.util.Map;

import com.qyq.utils.LogManager.LoggerTool;



public class RemoteConnect
{
/*  private static BufferedReader out = null;
  private static BufferedReader err = null;
  private static BufferedWriter in = null;*/

  public static Connection getConnetion(Map<String, String> serverInfo)
  {
    Connection conn = null;
    String ip = serverInfo.get("ip");
    String username = serverInfo.get("username");
    String password = serverInfo.get("password");

    conn = new Connection(ip);
    try {
      conn.connect();

      boolean isconn = conn.authenticateWithPassword(username, password);
      if (!isconn){
        //break label92;
      System.out.println("用户名称或者是密码不正确");
      throw new IOException("Authentication failed.");
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return conn;
  }

	public static Connection getConnetion(String serverIP, String username, String password)
	{
		LoggerTool.info("Connection IP = " + serverIP);
		LoggerTool.info("Connection Login user = " + username);
		LoggerTool.info("Connection Login Password = " + password);
		
		Connection conn = null;
		conn = new Connection(serverIP);
		try
		{
			conn.connect();

			boolean isconn = conn.authenticateWithPassword(username, password);
			if (!isconn)
			{
				LoggerTool.error("用户名称或者是密码不正确");
				throw new IOException("Authentication failed.");
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return conn;

	}
}
