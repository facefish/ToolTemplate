package com.qyq.utils.cmd;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.qyq.utils.LogManager.LoggerTool;





/**
 * Window本机的一些基本操作，如执行cmd命令、获取本机IP
 * @author ptatg
 *
 */
public class CommonFunction {
	
	
	/**
	 * 执行windows的CMD命令，并在日志/控制台输出命令输出
	 * @param command
	 */
	public static void executeCMD(String command)
	{
		executeCMD(command,true);
	}
	
	/**
	 * 执行windows的CMD命令
	 * @param command
	 * @param isOutput 是否输出命令的执行结果
	 */
	public static void executeCMD(String command,Boolean isOutput)
	{
		InputStream in = null;
		try {
			Process ps =  Runtime.getRuntime().exec(command);
			
			if(isOutput == null || isOutput == true)
			{
				LoggerTool.info("run Command: " + command);
			}
			
			
			//防止阻塞
			 in = ps.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				if(isOutput == null || isOutput == true)
				{
				    LoggerTool.info(line);
				}
			}		
			
			 in = ps.getErrorStream();
			BufferedReader Ebr = new BufferedReader(new InputStreamReader(in));
			 line = null;
			while ((line = Ebr.readLine()) != null) {
				if(isOutput == null || isOutput == true)
				{
				    LoggerTool.info(line);
				}
			}
			
			try {
				 ps.waitFor();
			    } catch (InterruptedException e) {

				 e.printStackTrace();
			    }
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally
		{
			try {
				if(in != null)
				{
				 in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void  executeCMD(String[]cmd)
	{
		executeCMD(cmd, null, null);
	}
	
	/**
	 * 带参数和指定路径执行dos命令
	 * @param command 命令
	 * @param env  执行参数，一般填null
	 * @param file  目录的File对象
	 */
	public static void executeCMD(String command, String[]env, File file)
	{
		InputStream in = null;
		InputStream err = null;
		try {
			Process ps =  Runtime.getRuntime().exec(command, env, file);
            LoggerTool.info("run Command: " + command);
			
			//防止阻塞
			 in = ps.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				LoggerTool.info(line);
			}			
			
			 err = ps.getErrorStream();
			BufferedReader Ebr = new BufferedReader(new InputStreamReader(err));
			 line = null;
			while ((line = Ebr.readLine()) != null) {
				LoggerTool.info(line);
			}
			
			try {
				 ps.waitFor();
			    } catch (InterruptedException e) {
				 e.printStackTrace();
			    }
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally
		{
			try {
				if(in != null)
				{
				 in.close();
				}
				if(err != null) err.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	
	/**
	 * 带参数和制定路径执行dos命令
	 * @param command
	 * @param env
	 * @param file 
	 */
	public static void executeCMD(String[] command, String[]env, File file)
	{
		StringBuffer fullCMD = new StringBuffer();
		if(command == null)
		{
			throw new RuntimeException("cmd is null");
		}
		else
		{
			for(String str : command)
			{
				fullCMD.append("  ");
				fullCMD.append(str);
			}
		}
		InputStream in = null;
		InputStream err = null;
		try {
			Process ps =  Runtime.getRuntime().exec(command, env, file);
            LoggerTool.info("run Command: " + fullCMD.toString());
			
			//防止阻塞
			 in = ps.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				LoggerTool.info(line);
			}			
			
			 err = ps.getErrorStream();
			BufferedReader Ebr = new BufferedReader(new InputStreamReader(err));
			 line = null;
			while ((line = Ebr.readLine()) != null) {
				LoggerTool.info(line);
			}
			
			try {
				 ps.waitFor();
			    } catch (InterruptedException e) {
				 e.printStackTrace();
			    }
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally
		{
			try {
				if(in != null)
				{
				 in.close();
				}
				if(err != null) err.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	
	
	
	/**
	 * 获取本机IP
	 * @return
	 */
	public static String getIP()
	{
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
         String   ip=addr.getHostAddress();
         return ip;
	}
	
	
	public static void killCmd()
	{
		String command = "taskkill /F /IM cmd.exe";
		executeCMD(command);
	}
	

}
