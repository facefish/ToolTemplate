package com.qyq.utils.WebGUI;

import java.awt.Rectangle;
import java.io.File;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver.Window;



/**
 * 对话框实例化类，提供普通弹出框、JS对话框文件上传下载对话框的处理
 * <p>
 *注意：由于对话框的类型较多，且不能直接用，需要先判断类型后在调用对应的方法
 *
 * @author y00358428
 *
 */
public class WebDialog extends WebObject
{

	public static int defaultWaitSeconds = 30;

	public WebDialog(String ID)
	{
		super(ID);
	}


	public WebDialog(String property, String value)
	{
		super(property, value);
	}

	@Override
	public String getElementType()
	{
		return "Dialog";
	}



	// JS弹出框确认
	public void alertConfirm()
	{
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	// JS弹出框取消
	public void alertCancel()
	{
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	// 获取JS弹出框内容
	public String getPromptMessage()
	{
		Alert alert = driver.switchTo().alert();
		return alert.getText();
	}

	// 设置prompt弹框内容
	public void setPromptMessage(String parameter)
	{
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(parameter);
	}






	
}
