package com.qyq.utils.WebGUI;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.qyq.utils.LogManager.LoggerTool;



public class KeyBoard
{

	
	
	/** 
	 * 模拟键盘Enter按下(一次）
	 */
	public static void Enter()
	{
		Actions action = new Actions(Browser.getDriver());
		action.sendKeys(Keys.ENTER).perform();
		actionLog("Keyboard:Enter");
	}
	
	
	/** 
	 * 模拟键盘Up按下一次
	 */
	public static void Up()
	{
		Actions action = new Actions(Browser.getDriver());
		action.sendKeys(Keys.ARROW_UP).perform();
		actionLog("Keyboard:Up");
	}
	
	/** 
	 * 模拟键盘Down按下一次
	 */
	public static void Down()
	{
		Actions action = new Actions(Browser.getDriver());
		action.sendKeys(Keys.ARROW_DOWN).perform();
		actionLog("Keyboard:Down");
	}
	
	/** 
	 * 模拟键盘右方向右键按下一次
	 */
	public static  void Right()
	{
		Actions action = new Actions(Browser.getDriver());
		action.sendKeys(Keys.ARROW_RIGHT).perform();
		actionLog("Keyboard:Right");
	}
	
	
	/** 
	 * 模拟键盘右方向左键按下一次
	 */
	public static  void Left()
	{
		Actions action = new Actions(Browser.getDriver());
		action.sendKeys(Keys.ARROW_LEFT).perform();
		actionLog("Keyboard:Left");
	}
	
	
	/**
	 * 控件操作日志API 
	 * <p>
	 * 用于会改建控件状态或者会影响页面效果的操作的日志记录，如单击、双击、输入、等
	 * @param action 控件操作描述
	 */
	protected static void actionLog(String action)
	{
		
		String msgLog = "-------------KeyBoard----" + "[操作=" + action + "]";
		
		LoggerTool.info(msgLog);
	}
}
