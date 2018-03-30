package com.qyq.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
	private Connection con;
	/**
	 * 
	 * @param dbPath  sqlite db文件绝对路径
	 */
	public SQLiteManager(String dbPath)
	{
		con = getConnection(dbPath);
	}
	
	
	public Connection getCon()
	{
		return con;
	}
	

	
	protected  Connection getConnection(String dbPath){
		Connection conn = null;
		String databasePath = dbPath;
		try{
			Class.forName("org.sqlite.JDBC").newInstance();
			String DbUrl = "jdbc:sqlite:" + databasePath;			
			conn = DriverManager.getConnection(DbUrl, "", "");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
	
	  /**
     * 建立一个sqlite数据连接，并执行一条查询sql语句
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
     * 建立一个gp数据连接，并执行一条sql语句
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
