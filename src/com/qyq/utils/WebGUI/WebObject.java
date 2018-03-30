package com.qyq.utils.WebGUI;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.qyq.utils.LogManager.LoggerTool;



/**Web控件元素基类，提供控件元素的查找
 * <p>
 * 为抽象类，所有Web实例化控件类必须继承自本类
 * <p>
 *作为Web控件的父类，提供控件元素的查找、初始化，以及公共方法如单击、双击、右键单击，
 *获取控件基本属性如text、tagName等
 *
 *<p>
 *【注意事项】<br>
 *1、控件如果初始化（即查找）失败，都会关掉打开的浏览器，WebDriver也会退出
 *
 * @author y00358428
 * @since 2017/2/17
 * @version 1.0
 */
public abstract class WebObject
{

	private static int theTestObjNo = 0; // 增加控件查找计数功能，方便后续分析用例日志
	private static int thetestObjactionNo = 0;  //控件操作计数

	protected WebElement webElem;
	protected WebDriver driver = null;  



	protected WebObject()
	{
		
	}
	

	/**
	 * WebObject初始化，使用id定位元素
	 * 
	 * @param ID，支持多id，使用"|"分隔
	 */
	public WebObject(String ID)
	{
		this("id",ID);
	}

	/**
	 * WebObject初始化， 使用id,xpath,name等定位元素,优先使用id,其次为xpath
	 * <p>
	 * 支持 id, Xpath,className, Anchor, cssSelector, name， partialLinkText, tagName, select 
	 * 
	 * @throws NoSuchElementException  超过最长查找时间也没有找到控件时抛出
	 * @param property
	 * @param value ， 支持多个值匹配 使用"|"分隔， 主要用于中英文场景
	 */
	public WebObject(String property, String value)
	{
		driver = Browser.getDriver();
		webElem = ElementFinder.findElement(property, value);
		
		if (webElem != null)
		{
			foundLog(property, value);
		}
		else
		{
			throw new NoSuchElementException("未找到元素" + getElementType() + "["
					+ property + "=" + value + "]");
		}
	}

	/**
	 * 使用一有WebElement初始华WebObject
	 * @param webElem
	 */
	public WebObject(WebElement webElem)
	{
		this.webElem = webElem;
	}
	

	/**
	 * 控件基本操作：获取元素类型，由各个子控件类自行实现
	 * @return String
	 */
	public abstract String getElementType();

	
	/**
	 * 控件元素基本操作：模拟鼠标左键单击控件
	 */
	public void click()
	{
		if (webElem.isEnabled())
		{
			try
			{
				String text = getText();
				webElem.click();
				actionLog("click " + getElementType() + ":" +text);
			}
			catch (ElementNotVisibleException e)
			{
				e.printStackTrace();
			}
			catch (WebDriverException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			waitForElementEnabled();
			String text = getText();
			webElem.click();
			actionLog("click " + getElementType() + ":" +text);
		}
	}

	
	/**
	 * 控件基本操作：元素是否可用
	 * @return boolean
	 */
	public boolean isEnabled()
	{
		return webElem.isEnabled();
	}

	/**
	 * 元素是否已在界面显示
	 */
	public boolean isDisplayed()
	{
		return webElem.isDisplayed();
	}


	/** 
	 * 控件基本操作：模拟鼠标左键双击
	 */
	public void doubleClick()
	{
		
		Actions action = null;
		
			action = new Actions(driver);
			action.doubleClick(webElem).perform();
			actionLog("doubleClick " + getElementType());

	}


	/**
	 * 获取当前的WebDrver实例
	 * 
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	
	/** 
	 * 控件元素基本操作：获取元素的文本内容
	 * @return String
	 */
	public String getText()
	{
		return this.webElem.getText();
	}

	

	/** 
	 * 控件基本操作：获取TagName属性值
	 * @return
	 */
	public String getTagName()
	{
		return this.webElem.getTagName();
	}

	/** 
	 * 控件基本操作：获取控件元素的特定属性值
	 * @param AttributeName , class, text, id等
	 * @return String
	 */
	public String getAttribute(String AttributeName)
	{
		return this.webElem.getAttribute(AttributeName);
	}

	public WebElement getWebObject()
	{
		return webElem;
	}

	/**
	 * 获取当前元素的指定条件的子元素集合
	 * @param property  id, tagName等
	 * @param value
	 * @return
	 */
	public  List<WebElement> getChildElements(String property, String value)
	{
		List<WebElement> list = null;
		
		list = webElem.findElements(ElementFinder.getByInstance(property, value));
		
		return list;
	}

	
	/** 
	 * 控件定位日志,用于记录控件的定位日志，便于分析
	 * @param PropertyName1
	 * @param PropertyValue1
	 */
	protected void foundLog(String PropertyName1, String PropertyValue1)
	{
		theTestObjNo++;
		String msgLog = "【控件定位日志---[objNo=" + theTestObjNo + "];[类型="
				+ getElementType() + "];[" + PropertyName1 + "=" + PropertyValue1
				+ "]";

			LoggerTool.info(msgLog);
		
	}

	/**
	 * 控件操作日志API 
	 * <p>
	 * 用于会改建控件状态或者会影响页面效果的操作的日志记录，如单击、双击、输入、等
	 * @param action 控件操作描述
	 */
	protected void actionLog(String action)
	{
		thetestObjactionNo++;
		String msgLog = "-------------[actionNo=" + thetestObjactionNo
				+ "];[操作=" + action + "]";
		
		LoggerTool.info(msgLog);
	}

	
	

	/** 
	 * 模拟鼠标右键点击
	 */
	public void rightClick()
	{
		if (webElem.isEnabled())
		{
			String text = getText();
			(new Actions(driver)).contextClick(webElem).perform();
			actionLog("rightClick + " + getElementType() + ":" + text);
		}
		else
		{
			LoggerTool.error("元素不可点击");
		}
	}

	/**
	 * 模拟鼠标左键点住不放
	 */
	public void clickAndHold()
	{
		if (webElem.isEnabled())
		{
			String text = getText();
			(new Actions(driver)).clickAndHold(webElem).perform();
			actionLog("clickAndHold " +getElementType() + ":" + text);
		}
		else
		{
			LoggerTool.error("元素不可点击");
		}
	}



	/**
	 * 将鼠标光标移动至元素的上面位置
	 */
	public void moveToElement()
	{
		(new Actions(driver)).moveToElement(webElem).perform();
	}

	/**
	 *  拖拽本控件到另一个控件的位置
	 * @param source
	 * @param target
	 */
	public void dragToElement( WebElement target)
	{
		(new Actions(driver)).dragAndDrop(this.webElem, target).perform();
	}

	
	/**
	 * 根据元素切换frame
	 * @param element
	 */
	public void switchToframe(WebElement element)
	{
		try
		{
			driver.switchTo().frame(element);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	
	/** 
	 * 等待元素使能，最高等待30s
	 */
	protected void waitForElementEnabled()
	{
		try
		{
			int i = 1;
			while (!webElem.isEnabled() && i <= 10)
			{
				Thread.sleep(3 * 1000);
				i++;
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		if (!webElem.isEnabled())
		{
			LoggerTool.error("元素未使能");
		}
		
	}
	// 切换Windows
		public void switchToWindow(String windowTitle) {
			Set<String> allWindowsId = driver.getWindowHandles();
			for (String windowId : allWindowsId) {
				if (driver.switchTo().window(windowId).getTitle().contains(
						windowTitle)) {
					driver.switchTo().window(windowId);
					break;
				}
			}
		}
   

	/**
	 * 重置控件定位和操作日志计数
	 */
	public static void resetLogCount()
	{
		 theTestObjNo = 0; // 增加控件查找计数功能，方便后续分析用例日志
		 thetestObjactionNo = 0;  //控件操作计数
	}
}
