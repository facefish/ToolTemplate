package com.qyq.utils.WebGUI;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;



/**
 * 下拉框的Web实例化类
 * 定制了下拉框的单击，获取当前选择的子项，选择指定子项，获取下拉框选项列表等操作， 
 * 
 * <p>
 * 注意用于元素tagName 为"select"的下拉框
 * @author y00358428
 * 
 *
 */
public class WebComboBox extends WebObject
{

	public WebComboBox(String ID)
	{
		super(ID);
	}

	public WebComboBox(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "ComboBox";
	}

	
	/** 
	 * 根据子项索引选中下拉框子项
	 * @param index
	 */
	public void selectByIndex(int index)
	{
		Select select = new Select(this.webElem);
		select.selectByIndex(index);
		actionLog("选择" + index);
	}

	/** 
	 *  根据子项文本内容选中下拉框子项
	 * @param text
	 */
	public void selectByText(String text)
	{
		Select select = new Select(this.webElem);
		select.selectByVisibleText(text);
		actionLog("选择子项" + text);
	}

	/**
	 * 根据子项的value属性选择
	 * @param value
	 */
	public void selectByValue(String value)
	{
		Select select = new Select(this.webElem);
		select.selectByValue(value);
		actionLog("选择" + value);
	}

	/**
	 * 根据子项索引去掉选中select子去顄1�7
	 * 
	 * @param index
	 */
	public void deselectByIndex(int index)
	{
		Select select = new Select(this.webElem);
		select.deselectByIndex(index);
		actionLog("去掉选择" + index);
	}

	/** 
	 * 根据子项名称去掉选中select子项
	 * @param text
	 */
	public void deselectByVisibleText(String text)
	{
		Select select = new Select(this.webElem);
		select.deselectByVisibleText(text);
		actionLog("去掉选择" + text);
	}

	public void deselectByValue(String value)
	{
		Select select = new Select(this.webElem);
		select.deselectByValue(value);
		actionLog("去掉选择" + value);
	}

	/** 
	 *  去掉select所有选中选项
	 */
	public void deselectAll()
	{
		Select select = new Select(this.webElem);
		select.deselectAll();
		actionLog("去掉所有选择");
	}

	/** 
	 * 获取select中第一个选中项的值
	 * 
	 * @return
	 */
	public String getFirstSelectedOption()
	{
		Select select = new Select(this.webElem);
		return select.getFirstSelectedOption().toString();
	}

	/** 获取select中所有所有选中的子选项
	 * 
	 * @return
	 */
	public String getAllSelectedOptions()
	{
		Select select = new Select(this.webElem);
		return select.getAllSelectedOptions().toString();
	}
	
	/** 获取select中所有子项
	 * 
	 * @return
	 */
	public String getAllOptions()
	{
		Select select = new Select(this.webElem);
		return select.getOptions().toString();
	}

	/**
	 * 获取下拉选择框当前选中子项的内容
	 */
	public String getSelectedText()
	{
		String text = null;
		Select select = new Select(this.webElem);
		List<WebElement> options = select.getOptions();
		for (WebElement webElement : options)
		{
			if (webElement.isSelected())
			{
				text = webElement.getText();
			}
		}
		return text;
	}

	/** 获取select下拉菜单中可选项的个数
	 * 
	 * @return
	 */
	public int getItemCount()
	{
		Select select = new Select(this.webElem);
		return select.getOptions().size();
	}

	/** 判断select下拉选项中是否包含某选项
	 * 
	 * @param itemName
	 * @return
	 */
	public boolean isContainItem(String itemName)
	{
		boolean flag = false;
		Select select = new Select(this.webElem);
		List<WebElement> options = select.getOptions();
		for (WebElement webElement : options)
		{
			if (webElement.getText().contains(itemName))
			{
				flag = true;
			}
			else
				flag = false;
		}
		return flag;
	}

	public boolean isEnabled()
	{
		return this.webElem.isEnabled();
	}

	/** 
	 *  判断是否为多选
	 * @return
	 */
	public boolean isMultiple()
	{
		Select select = new Select(this.webElem);
		if (select.isMultiple())
		{
			return true;
		}
		else
			return false;
	}
}
