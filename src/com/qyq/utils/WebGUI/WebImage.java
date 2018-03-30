package com.qyq.utils.WebGUI;

import org.openqa.selenium.Point;

/**
 * 图片类型的Web实例化类
 * <p>
 * 定制了图片类型的获取图片url，获取图片大小
 * @author y00358428
 *
 */
public class WebImage extends WebObject
{

	public WebImage(String ID)
	{
		super(ID);
	}

	public WebImage(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "Image";
	}

	/**
	 * 
	 * @return
	 */
	public String getImageURL()
	{
		return this.webElem.getAttribute("src");
	}

	public String getSize()
	{
		return this.webElem.getSize().toString();
	}

	public Point getLocation()
	{
		return this.webElem.getLocation();
	}
}