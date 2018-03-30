package com.qyq.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.qyq.utils.LogManager.LoggerTool;


public class Constants {
	
	
	private	static Properties pps = new Properties();
	static
	{
		try {
			pps.load(new InputStreamReader(new FileInputStream("config\\config.properties"), "UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LoggerTool.error("conf\\config.properties文件不存在，请确认", e);
		} catch (IOException e) {
			e.printStackTrace();
			LoggerTool.error(e.getMessage());
		}
	}
	
	
	public final static String SERVER_IP = pps.getProperty("serverIP");
	public final static String SERVER_USER = pps.getProperty("serverUser");
	public final static String SERVER_PWD = pps.getProperty("serverPwd");
	
	
	
	public final static String DEFAULT_IP = pps.getProperty("defaultIP");
	public final static String ROOT_DIR = pps.getProperty("rootDir");
	public final static String URL_LIST_PATH = pps.getProperty("url_List_Path");
	
	
	public final static String UTRAFFIC_TYPE="Normal";
	
	public static Properties getProperties()
	{
		return pps;
	}
}
