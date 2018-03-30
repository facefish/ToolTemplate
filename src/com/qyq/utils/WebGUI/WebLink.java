package com.qyq.utils.WebGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 超连接型Web实例化类, 提供超链接的基本操作, 一般为<a>标签
 * <p>
 * 定制了单击，获取超链接url, 获取超链接文本内容等操作
 * 
 * @author y00358428
 *
 */
public class WebLink extends WebObject
{

	public WebLink(String ID)
	{
		super(ID);
	}

	public WebLink()
	{

	}

	/**
	 * 基础报表专用
	 * @param text
	 */
	public void clickByText(String tagNames) {
	
		String[] texts = tagNames.split("\\|");
		List<WebElement> list = this.webElem.findElements(By.tagName("a"));
		for (int i = 0; i < getLinkCount(); i++) 
		{
			String atxt = list.get(i).getText().trim();
			for(String expect: texts)
			{
				if(atxt.equals(expect.trim()))
				{
						try {
								if (list.get(i).isEnabled()) {
									super.webElem=list.get(i);
									super.click();
								} else {
									waitForElementEnabled();
									super.webElem=list.get(i);
									super.click();
								}
								return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					  break;
				}
			}
		}

	}
	
	public WebLink(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "Link";
	}


	/**
	 * 获取超链接显示的文本内容
	 * @return
	 */
	public String getLinkText()
	{
		String text = super.getAttribute("value");
		if (text.equals(""))
		{
			text = super.getAttribute("text");
		}
		return text;
	}

	

	public int getLinkCount()
	{
		List<WebElement> counts = this.webElem.findElements(By.tagName("a"));
		return counts.size();
	}

	/**
	 * 获取超链接的url
	 * @return
	 */
	public String getLinkURL()
	{
		return this.webElem.getAttribute("href");
	}

}
