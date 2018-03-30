package com.qyq.utils.WebGUI;




/**
 * 按钮类，一般使用该类对按钮进行点击，获取按钮使能状态，获取按钮文本操作
 * 
 * 
 * @author y00358428
 *
 */
public class WebButton extends WebObject
{

	public WebButton(String ID)
	{
		super(ID);
	}

	public WebButton(String property, String value)
	{
		super(property, value);
	}


	/**
	 * 获取按钮上的文本
	 * 
	 * @param
	 * @return 按钮上文本
	 * @throws
	 * @see
	 */
	public String getBtnText()
	{
		String text = super.getAttribute("value");
		if (text.equals(""))
		{
			text = super.getAttribute("text");
		}
		return text;
	}

	
	@Override
	public String getElementType()
	{
		return "Button";
	}

}
