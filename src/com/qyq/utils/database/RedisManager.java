package com.qyq.utils.database;

import java.io.IOException;

import com.qyq.utils.LogManager.LoggerTool;
import com.qyq.utils.ssh.SessionUp;

import ch.ethz.ssh2.Connection;


/**
 * Redis数据库操作类
 * @author ptatg
 *
 */
public class RedisManager
{

	   private SessionUp sess;
	   private String serverIP;
	   private String dbport ;
	
	 
	public RedisManager(String ip, String dbport, String db, String dbuser, String dbpass)
	{
//		jedis = new Jedis(ip, Integer.parseInt(port));
//		jedis.auth(pass);
	
		serverIP = ip;
		this.dbport = dbport;
		try
        {
			
//	        sess = new SessionUp(ip,Utraffic.APP_LINUX_USER,Utraffic.APP_LINUX_PWD);
//	        sess.runCommand("cd /opt/redis/bin");
//	        sess.waitEnd(false);
//	        
//	        sess.runCommand("./redis-cli -h " + ip + " -p " + dbport + " -cipherdir /opt/redis/etc/cipher -a " + db + "@" + dbuser + "@" + dbpass);
//	        sess.waitNextInput(ip + ":" + dbport + ">", "keys *", 300);
        }
        catch (Exception e)
        {
	        // TODO 自动生成 catch 块
	        e.printStackTrace();
        }
		
		
	}
	
	
	
	public String getValue(String key) throws Exception
	{
		String value = null;
		
		
	        sess.waitNextInput(serverIP + ":" + dbport + ">", "hget " +  key ,  30);
	        sess.waitEnd(false);
	        String line ="";
	        if((line=sess.readNext()) != null)
	        {
	        	LoggerTool.info("value:" + line);
	        	value = line.trim().replace("\"", "");
	        }
      
		
		return value;
	}
	
	public void close()
	{
		sess.close();
	}
}
