package com.qyq.utils.DataTyeUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供字符串的常用处理方法
 * @author yuqingquan
 *
 */
public class StringOperation
{

	/**
	 * 提取字符串中的数字
	 * 只能把数字全部提取到一起，不能分别提取, 如有其它需要，请自行改进
	 */
	public static int getInt(String str)
	{
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);   
		
		int result = Integer.parseInt(m.replaceAll("").trim());
		return result;
	}
	
}
