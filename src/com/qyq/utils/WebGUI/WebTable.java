package com.qyq.utils.WebGUI;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.qyq.utils.LogManager.LoggerTool;


/**
 * 表格的Web实例化类，提供以表格为模型的基本操作
 * 定制了表格的 获取行数 获取列数，获取指定行，列，单元格内容等操作
 * 注意：只适用于由<table>, <thead>, <tbody>等标签结构的表格
 * @author y00358428
 * 
 */
public class WebTable extends WebObject
{

	/**
	 * 
	 * @param ID  <table>元素的id属性
	 */
	public WebTable(String ID)
	{
		super(ID);
	}

	public WebTable(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "Table";
	}

	/** 
	 *  获取表格行数，表头算一行
	 *  
	 * @return
	 */
	public int getRowCount()
	{
		List<WebElement> rowCounts = this.webElem.findElements(By.tagName("tr"));
		return rowCounts.size();
	}

	/** 获取表格列数
	 * 
	 * @return
	 */
	public int getcolCount()
	{
		List<WebElement> colCounts = this.webElem.findElements(By.tagName("td"));
		return colCounts.size();
	}

	/**
	 * 获取表格tbody
	 * @return
	 */
	public WebElement getTbody()
	{
		WebElement tbody = this.webElem.findElement(By.tagName("tbody"));
		return tbody;
	}

	public List<WebElement> getCells()
	{
		List<WebElement> arrayOfWebElement = this.webElem.findElements(By
				.tagName("td"));
		return arrayOfWebElement;
	}

	public List<WebElement> getRows()
	{
		List<WebElement> arrayOfWebElement = this.webElem.findElements(By
				.tagName("tr"));
		return arrayOfWebElement;
	}

	/**
	 * 获取表头元素
	 * @return
	 */
	public List<WebElement> getHeaders()
	{
		List<WebElement> arrayOfWebElement = this.webElem.findElements(By
				.tagName("th"));
		arrayOfWebElement.remove(0);
		return arrayOfWebElement;
	}

	/**
	 * 获取行集合，不含表头那一行
	 * 通过<tr>标签判断行
	 * @return
	 */
	public List<WebElement> getRowsWithoutHeader()
	{
		List<WebElement> elems = this.webElem.findElements(By.tagName("tr"));
		elems.remove(0);
		return elems;
	}

	public WebElement getRowByCellText(String paramString, boolean paramBoolean)
	{
		WebElement localWebElement = null;
		localWebElement = getCellByCellText(paramString, paramBoolean);
		return localWebElement == null ? null : localWebElement;
	}

	/**
	 * 通过文本获取单元格
	 * @param paramString  
	 * @param paramBoolean
	 * @return
	 */
	public WebElement getCellByCellText(String paramString, boolean paramBoolean)
	{
		List<WebElement> arrayOfWebElement1 = getCells();
		WebElement localObject = null;
		List<WebElement> arrayOfWebElement2 = arrayOfWebElement1;
		int i = arrayOfWebElement2.size();
		for (int j = 0; j < i; j++)
		{
			WebElement localWebElement = (WebElement) arrayOfWebElement2.get(j);
			String str = localWebElement.getText();
			str = str == null ? "" : str;
			str = paramBoolean ? str.trim() : str;
			if (!paramString.equals(str))
				continue;
			localObject = localWebElement;
			break;
		}

		return localObject;
	}

	/** 获取指定行的列数
	 * 
	 * @param rowIndex
	 * @return
	 */
	public int getColCount(int rowIndex)
	{
		List<WebElement> rowCounts = this.webElem.findElements(By.tagName("tr"));
		// 取得当前的tr
		WebElement rowNum = rowCounts.get(rowIndex);
		// 计算当前的td敄1�7
		List<WebElement> colCounts = rowNum.findElements(By.tagName("td"));
		return colCounts.size();
	}

	/** 
	 *  获取指定单元格的内容
	 * @param rowIndex  从1开始
	 * @param colIndex  从1开始
	 * @return
	 */
	public String getCellText(int rowIndex, int colIndex)
	{
		String text = "";
		try
		{
			List<WebElement> rowCounts = this.webElem
					.findElements(By.tagName("tr"));
			WebElement currentRow = rowCounts.get(rowIndex);
			List<WebElement> td = currentRow.findElements(By.tagName("td"));
			WebElement cell = td.get(colIndex-1);
			text = cell.getText();
		}
		catch (IndexOutOfBoundsException e)
		{
			LoggerTool.error("超出table边界");
		}
		return text;
	}

	/** 
	 *  获取指定行的内容,表头为第一行
	 * @param rowIndex 从1开始
	 * @return
	 */
	public String getRowText(int rowIndex)
	{
		String text = "";
		try
		{
			List<WebElement> rowCounts = this.webElem
					.findElements(By.tagName("tr"));
			WebElement currentRow = rowCounts.get(rowIndex);
			text = currentRow.getText();
		}
		catch (IndexOutOfBoundsException e)
		{
			LoggerTool.error("超出边界");
		}
		return text;
	}

	/** 
	 *  选择某一行
	 * @param rowIndex 从1开始
	 */
	public void selectRow(int rowIndex)
	{
		List<WebElement> rowCounts = this.webElem.findElements(By.tagName("tr"));
		WebElement currentRow = rowCounts.get(rowIndex);
		currentRow.click();
		actionLog("点击第" + rowIndex + "行" );
	}

	/** 
	 * 选择/单击单元格
	 * 
	 * @param rowIndex  从1开始
	 * @param columnIndex  从1开始
	 */
	public void selectCell(int rowIndex, int columnIndex)
	{
		List<WebElement> rowCounts = this.webElem.findElements(By.tagName("tr"));
		WebElement currentRow = rowCounts.get(rowIndex);
		List<WebElement> td = currentRow.findElements(By.tagName("td"));
		WebElement cell = td.get(columnIndex-1);
		cell.click();
	}

	/** 
	 * 设置单元格内容
	 * 
	 * @param rowIndex  从1开始
	 * @param columnIndex  从1开始
	 * @param content
	 */
	public void inputCell(int rowIndex, int columnIndex, String content)
	{
		List<WebElement> rowCounts = this.webElem.findElements(By.tagName("tr"));
		WebElement currentRow = rowCounts.get(rowIndex);
		List<WebElement> td = currentRow.findElements(By.tagName("td"));
		WebElement cell = td.get(columnIndex);
		cell.sendKeys(content);
	}

	public boolean isChecked()
	{
		String checked = this.webElem.getAttribute("checked");
		if (checked == null)
		{
			return false;
		}
		if (checked.equals("true"))
			return true;
		return checked.equals("checked");
	}

	
}
