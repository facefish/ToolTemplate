package com.qyq.utils.LogManager;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;


public class LoggerTool
{
    private static Logger logger = Logger.getLogger(LoggerTool.class);    
	
	
    public static void error(Object str)
	{
		logger.error(String.valueOf(str));
	}
    
    
    public static void warn(Object str)
	{
		logger.warn(String.valueOf(str));
	}
    
    
    public static void info(Object str)
	{
		logger.info(String.valueOf(str));
	}
	
	public static void debug(Object str)
	{
		logger.debug(String.valueOf(str));
	}


	public static void error(String string, FileNotFoundException e) {
		// TODO Auto-generated method stub
		logger.error(e);
		logger.error(string);
	}

	
	
}
