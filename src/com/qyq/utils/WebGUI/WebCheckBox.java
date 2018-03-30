package com.qyq.utils.WebGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.qyq.utils.LogManager.LoggerTool;


/**
 * 复选框的Web实例化类，提供一复选框为模型的基本操作
 * 定制了复选框的点击， 勾选，去勾选，勾选所有，去勾选所有操作
 * @author y00358428
 * 
 *
 */
public class WebCheckBox extends WebObject
{

	public WebCheckBox(String ID)
	{
		super(ID);
	}

	public WebCheckBox(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "CheckBox";
	}

	/**
	 * 点击复选框，改变其勾选状态
	 */
	@Override
	public void click()
	{
		String text = getText();
		this.webElem.click();
		actionLog("点击复选框" + text + "，更改勾选状态");
	}

	/**
	 * 勾选该复选框
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
			this.webElem.click();
			actionLog("设置复选框" + text + "，为选择状态");
		}
	}
	
	/**
	 * 去勾选该复选框
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
			this.webElem.click();
			actionLog("设置复选框" + text + "，为未选择状态");
		}
	}
	
	
	/**
	 * 去勾选所有
	 */
	public void deselectAll()
	{
		List<WebElement> checkboxes = this.webElem.findElements(By
				.cssSelector("input[type=checkbox]"));
		for (WebElement checkbox : checkboxes)
		{
			if (checkbox.isEnabled())
			{
				if (checkbox.isSelected())
					checkbox.click();
			}
		}
	}

	

	/**
	 * 是否勾选
	 * @return
	 */
	public boolean isSelected()
	{
		return this.webElem.isSelected();
	}

	public boolean isEnabled()
	{
		return this.webElem.isEnabled();
	}
}
