package com.qyq.utils.WebGUI;

import com.qyq.utils.LogManager.LoggerTool;

/**
 * 文本框类型Web实例化类, 提供文本输入框的基本操作
 * <p>
 * 定制了文本输入，获取文本框已有内容，清空文本框的操作
 * 
 * @author y00358428
 *
 */
public class WebInput extends WebObject
{

	public WebInput(String ID)
	{
		super(ID);
	}

	public WebInput(String property, String value)
	{
		super(property, value);
	}

	
	@Override
	public String getElementType()
	{
		return "Input";
	}

	/** 
	 * 输入文本，覆盖原有的文本
	 */
	public void input(String value)
	{
		if (this.webElem.isEnabled())
		{
			this.webElem.clear();
			this.webElem.sendKeys(value);
			actionLog("输入文本:" + value);
		}
		else{
			LoggerTool.error("文本框不可输入");
		}

	}
	

	/** 
	 * 获取文本框的内容
	 */
	public String getText()
	{
		return this.webElem.getText();
	}

	/** 清除文本框的内容
	 * 
	 */
	public void Clear()
	{
		if (this.webElem.isEnabled())
		{
			this.webElem.clear();
		}
		else
		{
			LoggerTool.error("文本框不可用");
		}
	}
	
	
	//输入文本，输入完毕敲回车
		public void inputWithEnter(String value){
			input(value);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			KeyBoard.Enter();;
		}

}
