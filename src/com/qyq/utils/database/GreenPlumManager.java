package com.qyq.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GreenPlumManager {
	 // 创建静态全局变量    
	private Connection con;
    
	 
    /* 获取数据库连接的函数*/    
    public  Connection getConnection(String ip, String port, String db, String user, String pass) {    
        Connection con = null;  //创建用于连接数据库的Connection对象    
        try {    
            Class.forName("org.postgresql.Driver");// 加载greenplum数据驱动    
                
            con = DriverManager.getConnection(    
                    "jdbc:postgresql://"+ip+":"+port +"/" + db, user, pass);// 创建数据连接    
                
        } catch (Exception e) {    
        	 System.out.println("数据库连接失败,请检查用户名密码是否正确，或数据库是否启动"); 
        	e.printStackTrace();  
        }    
        return con; //返回所建立的数据库连接    
    }
    
    
    /**
	 * 
	 * 
	 */
	public GreenPlumManager(String ip, String DBPort, String dbName, String DBUser, String DBpass)
	{
		con = getConnection( ip,  DBPort,  dbName,  DBUser,  DBpass);
	}
	
	
	public Connection getCon()
	{
		return con;
	}
	

	
	
	  /**
     * 建立一个gp数据连接，并执行一条查询sql语句
     * @param sql 
     * @return 执行结果  ResultSet
     */
    public  ResultSet RunQuerySql(String sql)
    {
    	 
    	ResultSet rs = null;    	
    	try
        {
	        Statement st = con.createStatement();
	        rs = st.executeQuery(sql);
        }
        catch (SQLException e)
        {
	        // TODO 自动生成 catch 块
	        e.printStackTrace();
        }
    	
    	return rs;    		
    }
    
    
    /**
     * 建立一个GP数据连接，并执行一条sql语句
     * @param sql
     * @return 执行结果  true or false
     */
    public  boolean RunSql(String sql)
    {
    	boolean isSuccess = false;
    	try
        {
	        Statement st = con.createStatement();
	        isSuccess =  st.execute(sql);
        }
        catch (SQLException e)
        {
	        // TODO 自动生成 catch 块
	        e.printStackTrace();
        }
    	
    	return isSuccess;    		
    }
    
    
    public void close()
    {
    	try
        {
    		if(con != null)
    		{
	         con.close();
    		}
        }
        catch (SQLException e)
        {
	        // TODO 自动生成 catch 块
	        e.printStackTrace();
        }
    }

}
