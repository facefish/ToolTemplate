package com.qyq.utils.WebGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qyq.utils.LogManager.LoggerTool;



/**Web控件查找类
 * 
 * @author y00358428
 *注意：抛 [stale element reference: element is not attached to the page document]一般为 driver 已经不再该元素所在的frame上，或者网页已刷新
 */
public class ElementFinder
{

     
      final static long timeOut = GUIConstUtil.TIMEOUT;
      
      
      /**
       * Web元素查找 ，主要对外的
       * @param property
       * @param value
       * @return
       */
      public static WebElement findElement(String property, String value)
      {
    	
	    	return findElement(property, value, timeOut);
      }
      
      
      /**
       * Web元素查找,主要对外的
       * 支持或查找，条件使用“|” 隔开
       * @param property
       * @param value
       * @param timeOut, 查找超时时间，单位为秒
       * @return
       */
      public static WebElement findElement(String property, String value, long timeOut)
      {
    	
	    	 WebElement ele = null;
	    	if(Browser.isCallQuit())
		  	{
		  			LoggerTool.error("---------ERROR: driver has call quit() or is null, when find WebElement:" + property + "=" + value);
		  			return null;	
		  	}
	  		String[] values = value.split("\\|");
	  		
	  		long end = System.currentTimeMillis() + Long.valueOf(timeOut) * 1000;
			do{
				try
				{
					for (String v : values)
			  		{
			  			ele = findElementInIframes( property,  v);
			  			if (ele != null)
			  			{
			  				break;
			  			}
			  		}
					
					if (ele != null)
		  			{
		  				break;
		  			}
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			} while (System.currentTimeMillis() < end);

	  		
	  		
	  		return ele;
      }
      
       
	

	/**
	 * 遍历页面里所有的iframe查找控件
	 * 
	 * @param by
	 * @return 返回找到的WebElement， 没有找到返回null
	 */
	public static WebElement findElementInIframes(String property, String value)
	{
		By by = getByInstance(property, value);
		WebDriver driver = Browser.getDriver();
		WebElement findElement = null;
		driver.switchTo().defaultContent();
		try
		{
			findElement = driver.findElement(by);
			if (findElement != null)
			{
				return findElement;
			}
		}
		catch (NoSuchElementException e1)
		{
			findElement = null;
		}
		
		//如果默认页面没找到，则遍历里面的frame控件查找
		if (findElement == null)
		{
			List<WebElement> frameList = driver.findElements(By.tagName("iframe"));
			frameList.addAll(driver.findElements(By.tagName("frame")));

			//LoggerTool.info("============frame 个数：  " + frameList.size());
			
			ListIterator<WebElement> frameEnum = frameList.listIterator();
			while (frameEnum.hasNext())
			{
				WebElement frame = frameEnum.next();
				if (frame.isDisplayed() != true)
				{
					continue;
				}
				driver.switchTo().defaultContent();

				findElement = findElementInIframe( frame, by);
				if (findElement != null && findElement.isDisplayed())
				{
					break;
				}
			}

		}

		return findElement;

	}

	/**
	 * 在指定iframe下面查找元素
	 * 
	 * @param dr
	 *            浏览器驱动
	 * @param frameElement
	 *            对应的Frame页面框架
	 * @param by
	 *            By对象
	 * @return   
	 *          返回找到的Web元素， 没有找到返回null
	 */
	private static WebElement findElementInIframe(WebElement frameElement, By by)
	{
		WebDriver driver = Browser.getDriver();
		WebElement findElement = null;
	    //LoggerTool.info("-----------frameid:" + frameElement.getAttribute("id"));
		driver.switchTo().frame(frameElement);
		
		try
		{
			findElement = driver.findElement(by);
		}
		catch (NoSuchElementException e1)
		{
			findElement = null;
			List<WebElement> frameList = driver.findElements(By.tagName("iframe"));
			frameList.addAll(driver.findElements(By.tagName("frame")));
			
			ListIterator<WebElement> frameEnum = frameList.listIterator();
			while (frameEnum.hasNext())
			{
				WebElement frame = frameEnum.next();
				if (frame.isDisplayed() != true)
				{
					continue;
				}

				findElement = findElementInIframe( frame, by);
				if (findElement != null )
				{
					break;
				}
			}
		}

		if(findElement == null)  //没有控件之后要切回父iframe
		{
			driver.switchTo().parentFrame();
		}

		return findElement;
	}

	
	
	
	//===============================查找元素集合==============================================================================
	
	  /**
     * Web元素查找
     * @param property
     * @param value
     * @return List<WebElement>，没有找到返回空集合
     */
    public static List<WebElement> findElements(String property, String value, long timeOut)
    {
  	
    	 List<WebElement> eles = new ArrayList<>();
	    	if(Browser.isCallQuit())
		  	{
		  			LoggerTool.error("---------ERROR: driver has call quit() or is null, when find WebElement:" + property + "=" + value);
		  			return eles;	
		  	}
	  		String[] values = value.split("\\|");
	  		
	  		long end = System.currentTimeMillis() + Long.valueOf(timeOut) * 1000;
			do{
				try
				{
					for (String v : values)
			  		{
			  			eles = findElementsInIframes(property, v);
			  			if (eles.size() > 0)
			  			{
			  				return eles;
			  			}
			  		}
					
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			} while (System.currentTimeMillis() < end);

	  		
	  		
	  		return eles;
    }
	
	
	
	/**
	 * 遍历页面里所有的iframe查找符合条件控件集合
	 * 
	 * @param by
	 * @return 返回找到的WebElement 集合， 没有找到返回size为0的list
	 */
	public static List<WebElement> findElementsInIframes(String property, String value)
	{
		By by = getByInstance(property, value);
		WebDriver driver = Browser.getDriver();
		List<WebElement> findElements = new ArrayList<>();
		driver.switchTo().defaultContent();
		try
		{
			findElements = driver.findElements(by);
			if (findElements.size() > 0)
			{
				return findElements;
			}
		}
		catch (NoSuchElementException e1)
		{
			findElements.clear();
		}
		
		//如果默认页面没找到，则遍历里面的frame控件查找
		if (findElements.size() == 0)
		{
			List<WebElement> frameList = driver.findElements(By.tagName("iframe"));
			frameList.addAll(driver.findElements(By.tagName("frame")));

			//LoggerTool.info("============frame 个数：  " + frameList.size());
			
			ListIterator<WebElement> frameEnum = frameList.listIterator();
			while (frameEnum.hasNext())
			{
				WebElement frame = frameEnum.next();
				if (frame.isDisplayed() != true)
				{
					continue;
				}
				driver.switchTo().defaultContent();

				findElements = findElementsInIframe( frame, by);
				if (findElements.size() > 0 )
				{
					break;
				}
			}

		}

		return findElements;

	}

	/**
	 * 在指定iframe下面查找符合条件的元素集合
	 * 
	 * @param dr
	 *            浏览器驱动
	 * @param frameElement
	 *            对应的Frame页面框架
	 * @param by
	 *            By对象
	 * @return   
	 *          返回找到的Web元素集合， 没有找到返回null
	 */
	public static List<WebElement> findElementsInIframe(WebElement frameElement, By by)
	{
		WebDriver driver = Browser.getDriver();
		 List<WebElement> findElements = new ArrayList<>();
	    //LoggerTool.info("-----------frameid:" + frameElement.getAttribute("id"));
		driver.switchTo().frame(frameElement);
		
		try
		{
			findElements = driver.findElements(by);
		}
		catch (NoSuchElementException e1)
		{
			findElements.clear();
			List<WebElement> frameList = driver.findElements(By.tagName("iframe"));
			frameList.addAll(driver.findElements(By.tagName("frame")));
			
			ListIterator<WebElement> frameEnum = frameList.listIterator();
			while (frameEnum.hasNext())
			{
				WebElement frame = frameEnum.next();
				if (frame.isDisplayed() != true)
				{
					continue;
				}

				findElements = findElementsInIframe( frame, by);
				if ( findElements.size() > 0 )
				{
					break;
				}
			}
		}

		if(findElements.size() == 0)  //没有控件之后要切回父iframe
		{
			driver.switchTo().parentFrame();
		}

		return findElements;
	}

	
	
	/**
	 * 根据不同的定位属性来确定相应的By对象
	 * 
	 * @param Opetype
	 *            定位元素的属性类型  支持 id, Xpath,className, Anchor, cssSelector, name， partialLinkText, tagName, select 
	 * @param Opevalue
	 *            定位元素类型的属性值
	 * @return 返回相应的By对象, 没有匹配上返回null
	 */
	public static  By getByInstance(String Opetype, String Opevalue)
	{
		By by = null;
		if (Opetype.equalsIgnoreCase("id"))
		{
			by = By.id(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("Xpath"))
		{
			by = By.xpath(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("Anchor"))
		{
			by = By.linkText(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("className"))
		{
			by = By.className(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("cssSelector"))
		{
			by = By.cssSelector(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("name"))
		{
			by = By.name(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("partialLinkText"))
		{
			by = By.partialLinkText(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("tagName"))
		{
			by = By.tagName(Opevalue);
		}
		else if (Opetype.equalsIgnoreCase("select"))
		{
			by = By.id(Opevalue);
		}else{
			LoggerTool.error("\n========ERROR:NO match By ,  property=" + Opetype  + " ======================\n") ;
		}
		return by;
	}

}
