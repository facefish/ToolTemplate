package com.qyq.utils.WebGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 列表型Web实例化类, 提供列表的基本操作
 * <p>
 * 定制了单击，获子控件列表等操作
 * 
 * @author y00358428
 *
 */
public class WebList extends WebObject
{

	public WebList(String ID)
	{
		super(ID);
	}

	public WebList(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "List";
	}

	
	/** 
	 *  获取ListCount
	 * @return
	 */
	public int getListCount()
	{
		List<WebElement> Counts = this.webElem.findElements(By.tagName("li"));
		return Counts.size();
	}

	/**
	 * 获取子控件列表
	 * @return
	 */
	public List<WebElement> getList()
	{
		return this.webElem.findElements(By.tagName("li"));
	}

	public List<WebElement> findElements(By by)
	{
		return this.webElem.findElements(by);
	}

}
