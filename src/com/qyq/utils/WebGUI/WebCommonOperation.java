package com.qyq.utils.WebGUI;

import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qyq.utils.LogManager.LoggerTool;


/**
 *Web公共操作类操作
 * @author y00358428
 *
 */
public class WebCommonOperation
{

	private final static  int checkTimeOut = 3;
	
	/**
	 * 判断当前浏览器页面是否存在该Web控件
	 * @param id  控件id
	 * @return
	 */
	public static boolean isExist(String id)
	{
		boolean isExist = true;
		isExist = isExist("ID", id);
		return isExist;
	}
	
	/**
	 * 判断当前浏览器页面是否存在该Web控件，指定时间内是否存在
	 * @param id  控件id值
	 * @param timeOut  超时时间，单位为秒
	 * @return
	 */
	public static boolean isExist(String id, int timeOut)
	{
		boolean isExist = true;
		isExist = isExist("ID", id, timeOut);
		return isExist;
	}
	
	
	/**
	 * 判断元素是否存在， 推荐使用id或这xpath属性 ， 最多等待默认时间（单位为秒）
	 * @param property  id, xpath 等
	 * @param value
	 * @return
	 */
	public static boolean isExist(String property, String value)
	{
		return isExist(property, value, checkTimeOut);
	}
	
	/**
	 * 指定时间内判断元素是否存在， 推荐使用xpath， 指定时间内是否存在(可以定位到，但不可见也算不存在）
	 * @param property  id, xpath 等
	 * @param value
	 * @param timeOut  超时时间，单位为秒
	 * @return
	 */
	public static boolean isExist(String property, String value, int timeOut)
	{
		boolean isExist = true;
		WebElement ele = null;
		
		ele = ElementFinder.findElement(property, value,timeOut);

		if(null == ele)
		{
			isExist = false;
		}else{
			if(ele.isDisplayed())
			{
				isExist = true;
			}else{
				isExist = false;
			}
			
		}
       
		return isExist;
	}
	
	
	/** 
	 * 切换浏览器标签页面
	 * @param tabTile, 支持部分匹配和或匹配， 使用|分隔
	 */
	public static void switchToWindow(String tabTitle)
	{
		Set<String> allWindowsId = Browser.getDriver().getWindowHandles();
		
		String[] titles = tabTitle.split("\\|");
		
		boolean isSuccess = false;
		
		for(String title :titles)
		{
			for (String windowId : allWindowsId)
			{
				if (Browser.getDriver().switchTo().window(windowId).getTitle().contains(title.trim())) 
				{
					Browser.getDriver().switchTo().window(windowId);
					LoggerTool.info("-------------[actionNo=Special];[操作=switchToWindow:" + Browser.getDriver().getTitle() +"]");
					isSuccess = true;
					return;
				}
			}
		}
		
		if(!isSuccess)
		{
			LoggerTool.error("===========ERROR: driver switch to tab title=" + tabTitle + " failed ================================");
		}
	}
	

	/**
	 * 在当前页面执行javascript脚本 
	 * @param javaScript
	 */
	public static void executeScript(String javaScript)
	{
		JavascriptExecutor js = (JavascriptExecutor) Browser.getDriver();
		js.executeScript(javaScript);
	
	}
	
	

	 // 切换frame
	public static void switchToFrame(String frameId) {
			try {
				Browser.getDriver().switchTo().frame(frameId);
			} catch (NoSuchFrameException e) {
				e.printStackTrace();
			}
		}
	
}
