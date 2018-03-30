package com.qyq.utils.WebGUI;




/**
 * 标签类型Web实例化类, 提供标签的基本操作
 * <p>
 * 定制了单击， 获取标签文本内容的操作
 * 
 * @author y00358428
 *
 */
public class WebLabel extends WebObject
{

	public WebLabel(String ID)
	{
		super(ID);
	}

	public WebLabel(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "Label";
	}

	/**
	 * 获取标签内容
	 */
	public String getText()
	{
		String text = "";
		try
		{
			text = this.webElem.getText();
		}
		catch (Exception e)
		{
			if (text.equals(""))
			{
				text = getTagName();
			}
		}
		return text;
	}

}
