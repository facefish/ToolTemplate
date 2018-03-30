package com.qyq.utils.WebGUI;

import java.io.File;

import org.openqa.selenium.WebDriver;

public class GUIConstUtil {
	
	

	public static String OSlangName = "zh";

	public static WebDriver driver = null;  //通过Browser类进行赋值
	
	public final static long TIMEOUT= 20;  //定位元素最长等待时间 

	public final static String DRIVER_PATH = System.getProperty("user.dir") + File.separator + "datas\\driver"; //默认浏览器driver目录
	public final static String Extensions_PATH = System.getProperty("user.dir") + File.separator + "datas\\driver"; //默认浏览器插件目录


	
}
