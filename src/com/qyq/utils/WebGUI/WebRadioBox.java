package com.qyq.utils.WebGUI;

import com.qyq.utils.LogManager.LoggerTool;

/**
 * 单选框型Web实例化类, 提供单选框的基本操作
 * <p>
 * 定制了单击，勾选, 去勾选，获取单选框文本内容等操作
 * 
 * @author y00358428
 *
 */
public class WebRadioBox extends WebObject
{

	WebRadioBox(String ID)
	{
		super(ID);
	}

	public WebRadioBox(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "RadioBox";
	}

	/** 
	 * 单击单选框
	 * <br> 注意是单击，不是选中
	 */
	public void click()
	{
		super.click();
	}

	/**
	 * 勾选该单选框
	 * @param flag
	 */
	public void select()
	{
		if (this.webElem.isSelected())
		{
			LoggerTool.info("CheckBox is checked.");
		}
		else
		{
			String text = getText();
			super.click();
			actionLog("勾选单选框" + text );
		}
	}
	
	/**
	 * 去勾选该单选框
	 */
	public void deSelect()
	{
		if (!this.webElem.isSelected())
		{
			LoggerTool.info("CheckBox is unchecked.");
		}
		else
		{
			String text = getText();
			super.click();
			actionLog("去勾选单选框" + text );
		}
	}
	
	

	
	/** 
	 *  获取使能状态
	 */
	public boolean isEnabled()
	{
		return super.isEnabled();
	}

}
