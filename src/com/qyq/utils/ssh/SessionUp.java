package com.qyq.utils.ssh;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.qyq.utils.LogManager.LoggerTool;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * 用于java程序执行远程机器上的Shell脚本。基于ch.ethz.ssh2.Session的封装。
 * 
 * <p>
 * 基本用法：
 * <p>
 * 1.获取实例(注意：Connection和SessionUp用来之后要关闭~~)
 * <p>
 * 示例代码：</br> SessionUpsess = null; </br> try { </br> &nbsp;&nbsp; sess = new
 * SessionUp(ip,username,password); </br> } catch (IOException e) { </br>
 * &nbsp;&nbsp; e.printStackTrace(); </br> } finally {</br> &nbsp;&nbsp;
 * sess.close(); </br> &nbsp;&nbsp; conn.close(); </br> } </br>
 * 
 * <p>
 * 2.运行命令行用于切换用户，或者用户登陆
 * <p>
 * 注意事项：与用户登录相关的命令运行，都要用
 * {@link com.huawei.SessionUp.SessionUp#switchUser(String, String)}
 * <p>
 * 例如：su - ossuser,密码是:password（切换用户名登录）
 * <p>
 * 示例代码：</br>sess.switchUser("su - ossuser",password);//没有密码时，第二个参数填null
 * 
 * <p>
 * 3.运行命令行，直接读取结果（中间没有提示信息，不需要二次输入）
 * <p>
 * 注意事项：{@link com.huawei.SessionUp.SessionUp#runCommand(String)}，之后必须调用
 * {@link com.huawei.SessionUp.SessionUp#waitEnd(boolean)}等待命令执行，否则运行之后的命令会报错。
 * <p>
 * 例如：ps -ef | grep java（查看进程）
 * <p>
 * 示例代码：</br>sess.runCommand("ps -ef | grep java");//运行命令</br>
 * sess.waitEnd(true);//等待命令执行</br> String line;</br> while ((line =
 * sess.readNext()) != null) {//获得结果</br> &nbsp;&nbsp; //处理结果</br> } </br>
 * 
 * 4.运行命令行，中间需要输入信息后才能获得结果
 * <p>
 * 注意事项：中间提示信息输入调用
 * {@link com.huawei.SessionUp.SessionUp#waitNextInput(String, String, long)}
 * <p>
 * 例如：初始化数据库，提示输入密码，输入错误密码，提示密码错误，是否显示细节，输入是，获取细节内容
 * <p>
 * 示例代码：</br>sess.runCommand(
 * " ./startclient.sh storage -ip 127.0.0.1 -port 12212 -username admin -InitDatabase"
 * );</br> sess.waitEnd(true);//等待命令执行</br>
 * sess.waitNextInput("Enter the MSuite login password[]:", "password",
 * 60);//提示输入密码（输入错误的密码）</br> sess.waitEnd(true);//等待命令执行</br>
 * sess.waitNextInput("Show Details?[y:Yes, n:No, c:Cancel, y]:", "y",
 * 60);//提示是否显示细节</br> sess.waitEnd(true);//等待命令执行</br> String line;</br> while
 * ((line = sess.readNext()) != null) {</br> &nbsp;&nbsp; //处理结果</br> }</br>
 * 
 * 注意：如果需要执行长目录的脚本或命令，请采用 绝对路径的方式执行，不要先cd到该长目录然后使用./执行
 * 注意：命令输出结果超过2000行会导致内存溢出，需要优化命令把输出行数控制在2000行内
 */
public class SessionUp
{

	private int BUFFERLEN = 1024;
	private int outputMaxLine = 2000;

	char errorbyte[] = { 0x08 };
	String errorstring = new String(errorbyte);
	char c[] = new char[BUFFERLEN];
	String command;

	private Session sess;
	private Connection con;
	private InputStreamReader out;
	private OutputStreamWriter in;

	// 保存当前的输出值。
	// 如果处理完毕，需要将outTemp设置为null
	private StringBuffer outTemp = new StringBuffer();
	private List<String> result = new ArrayList<String>();
	private StringBuffer resultSB = new StringBuffer();
	private int index = 0;
	// 记录下一行命令开始的标识符
	private Pattern nextCommandStartId;

	/**
	 * 新建SessionUp， 注意使用完毕后请调用close方法关闭连接
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @throws IOException
	 */
	public SessionUp(String ip, String username, String password) throws IOException
	{
		con = RemoteConnect.getConnetion(ip, username, password);
		this.sess = con.openSession();

		sess.requestDumbPTY();
		sess.startShell();

		out = new InputStreamReader(sess.getStdout());
		in = new OutputStreamWriter(sess.getStdin());

		rechangeEnv();
	}

	/**
	 * SessionUp初始化
	 * 
	 * @param conn
	 *            远程连接对象
	 * @throws IOException
	 */
	public SessionUp(Connection conn) throws IOException
	{
		con = conn;
		this.sess = conn.openSession();

		sess.requestDumbPTY();
		sess.startShell();

		out = new InputStreamReader(sess.getStdout());
		in = new OutputStreamWriter(sess.getStdin());

		rechangeEnv();
	}

	/**
	 * 命令行标识符发生改变，需要切换
	 * 
	 * @throws IOException
	 */
	private void rechangeEnv() throws IOException
	{

		long timeend = System.currentTimeMillis() + 50000;
		String outT = null;
		while (System.currentTimeMillis() <= timeend)
		{

			outTemp = readString(outTemp, out);

			int index = outTemp.lastIndexOf("\n");
			outT = outTemp.substring(index + 1);
			outTemp.setLength(0);
			outTemp = readString(outTemp, out);
			if (outTemp.length() == 0 && !outT.equals(""))
			{
				break;
			}
			else
			{
				continue;
			}
		}

		outTemp.append(outT);
		String pattern = outT.replaceFirst("~", ".*").replaceAll("\\$", "\\\\\\$").replaceAll("\\{", "\\\\\\{")
		        .replaceAll("\\}", "\\\\\\}");

		// 匹配root用户
		if (pattern.indexOf("#") > -1) // 规避切换用户后cd导致目录切换，以及cd长目录会导致匹配异常的问题
		{
			// LoggerTool.info(pattern);
			String dir = pattern.substring(0, pattern.indexOf("#"));
			// LoggerTool.info(dir);
			pattern = pattern.replace(dir, ".*");
			// LoggerTool.info(pattern);
		}

		// 匹配其他用户如 sopuser，ossadm, 以及数据连接状态
		if (pattern.indexOf(">") > -1) // 规避切换用户后cd导致目录切换，以及cd长目录会导致匹配异常的问题
		{
			// LoggerTool.info(pattern);
			String dir = pattern.substring(0, pattern.indexOf(">"));
			// LoggerTool.info(dir);
			pattern = pattern.replace(dir, ".*");
			// LoggerTool.info(pattern);
		}
		nextCommandStartId = Pattern.compile(pattern);
		// System.out.println("下一个命令开始标识为：" + nextCommandStartId);
	}

	/**
	 * 切换用户
	 * 
	 * <p>
	 * 切换用户的命令行运行必须使用本函数，直接调用runCommand(String)会抛异常。
	 * 
	 * @param command
	 *            切换用户或者用户登录的命令，如su ossadm
	 * @param password
	 *            需要输入的密码，没有密码为null
	 * @throws IOException
	 */
	public void switchUser(String command, String password) throws IOException
	{
		runCommand(command);
		if (password != null)
			sendLine(password);

		rechangeEnv();
	}

	/**
	 * 读行并判断命令语句是否执行结束。等待时间的单位：秒
	 * <p>
	 * 结束的标志是遇到下一行命令换行符，或者遇到提示信息。
	 * <p>
	 * 超时处理：抛超时异常
	 * 
	 * @param timeout
	 *            读行总时间，单位秒
	 * @return 有换行符或者提示输入信息，返回行内容；outTemp没有内容，返回null
	 * @throws IOException
	 */
	private String readLine(long timeout) throws IOException
	{
		long timeend = System.currentTimeMillis() + timeout * 1000;
		String before = "";
		int i = 0;
		while (System.currentTimeMillis() <= timeend)
		{
			outTemp = readString(outTemp, out);
			if (null == outTemp)
			{
				try
				{
					Thread.sleep(300);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else if (outTemp.indexOf("\r") != -1 || outTemp.indexOf("\n") != -1)
			{
				String[] s = replaceUnreadableString(outTemp.toString()).split("\r\n|\r|\n", 2);
				outTemp = new StringBuffer(s[1]);
				System.gc();
				return s[0];
			}
			else if (nextCommandStartId.matcher(outTemp).matches())
			{
				return null;
			}
			else
			{
				// 碰到等待输入的提示信息返回,否则持续等待
				if (outTemp.length() != 0)
				{
					if (outTemp.indexOf(before) != -1)
					{
						if (i > 5 && (outTemp.toString().endsWith(":")))
						{
							i = 0;
							LoggerTool.error("临时输出：" + outTemp);
							return null;
						}
						i++;

						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}

					}
					else
					{
						i = 0;
						before = outTemp.toString();
					}
				}
				else
				{
					i = 0;
					try
					{
						Thread.sleep(300);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

				}
			}
		}
		LoggerTool.error("程序可能没有运行结束，但是到达等待时间：" + timeout);
		LoggerTool.error("临时输出：" + outTemp);
		return null;

		// throw new TimeoutException((int) timeout, "临时输出是:" + outTemp);

	}

	/**
	 * 向终端中输出文本，提供换行和flush功能，使下发后能够迅速到伪终端中。
	 * 
	 * @param inString
	 *            命令参数，参数中不需要换行符，程序运行会自动加上。
	 * @throws IOException
	 */
	private void sendLine(String inString) throws IOException
	{
		LoggerTool.info("Run Command: " + nextCommandStartId + inString);
		in.write(inString + "\n");
		in.flush();

		outTemp.setLength(0);// 将outTemp长度设置为0，目的是读行时能读到输入inString后的结果
		while (nextCommandStartId.matcher(outTemp).matches())
		{
			outTemp = readString(outTemp, out);
		}
		readLine(500);
		result.clear();
		resultSB.setLength(0);
		index = 0;
	}

	/**
	 * 运行命令
	 * <p>
	 * 该方法主要负责检查是否可以开始进行下一个命令。 如果检查到上一个命令已经结束，则运行下一个命令。返回true
	 * 否则不能运行下一个命令，返回false 同时下发命令后，将读取掉该行，以准备给程序使用。 command中不需要提供回车功能。
	 * 
	 * @throws IOException
	 * 
	 */
	public boolean runCommand(String command) throws IOException
	{
		outTemp = readString(outTemp, out);
		this.command = command;

		if (nextCommandStartId.matcher(outTemp).matches())
		{
			sendLine(command);
			waitEnd(true, 1);
			return true;
		}
		else
		{
			throw new RuntimeException("上一程序未结束：" + outTemp);
		}
	}

	/**
	 * 将shell命令的返回结果存入缓冲区。
	 * 
	 * @param curString
	 *            缓冲区
	 * @param reader
	 *            输入流
	 * @return 缓冲区，类型为StringBuffer
	 * @throws IOException
	 */
	private StringBuffer readString(StringBuffer curString, InputStreamReader reader) throws IOException
	{
		// char c[] = new char[BUFFERLEN];
		while (reader.ready())
		{
			int len = reader.read(c);

			if (len < 0)
				break;
			curString = curString.append(new String(c, 0, len));
		}

		return curString;

	}

	/**
	 * 关闭Session
	 */
	public void close()
	{
		sess.close();
		if (con != null)
		{
			con.close();
		}
	}

	/**
	 * 替换不可读字符{0x08}
	 * <p>
	 * 将line中字符{0x08}去掉，该字符在日志中打印不出来
	 * 
	 * @param line
	 * @return 返回去掉不可读字符后的字符串
	 */
	private String replaceUnreadableString(String line)
	{

		return line.replaceAll(errorstring, "");
	}

	/**
	 * 等待输入。提示需要输入信息时，调用该函数。等待时间单位：秒
	 * 
	 * @param word
	 *            提示或者警告信息的内容,word为null时，直接输入，不匹配
	 * @param sendWord
	 *            需要输入的内容
	 * @param timeout
	 *            等待总时间，单位秒
	 * @return 信息输入，返回true;超时，抛出超时异常
	 * @throws Exception
	 */
	public boolean waitNextInput(String word, String sendWord, long timeout) throws Exception
	{
		if (word != null)
			System.out.println("匹配信息：" + word);
		long timeend = System.currentTimeMillis() + timeout * 1000;
		while (System.currentTimeMillis() <= timeend)
		{
			outTemp = readString(outTemp, out);
			System.out.print(outTemp);
			if (word == null || outTemp.indexOf(word) != -1)
			{
				sendLine(sendWord);
				return true;
			}
			else
			{
				try
				{
					Thread.sleep(300);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		int ind = outTemp.lastIndexOf("\n");
		outTemp.delete(0, ind + 1);
		throw new Exception(timeout + ",超时临时输出是:" + outTemp);
	}

	/**
	 * 等待命令执行结束。最长等待10分钟
	 * <p>
	 * 在每次运行命令{@link com.huawei.SessionUp.SessionUp#runCommand(String)}
	 * 必须调用该函数来等待命令执行结束。
	 * 
	 * @param isStdout
	 *            是否需要将执行过程打印到控制台，需要设置true，不需要设置false
	 * @throws IOException
	 */
	public void waitEnd(boolean isStdout) throws IOException
	{

		waitEnd(isStdout, 600);// 默认等待时间为10分钟
	}

	/**
	 * 等待命令执行结束， 可设置最大等待时间
	 * 
	 * @param isStdout
	 * @param time
	 *            单位为秒
	 * @throws IOException
	 */
	public void waitEnd(boolean isStdout, long maxWaitTime) throws IOException
	{
		String line;
		while ((line = readLine(maxWaitTime)) != null)
		{
			if (!line.trim().equals("") || !nextCommandStartId.matcher(line).matches())
			{
				if (line.startsWith("<") && !line.endsWith(">")) // 规避如果命令过长，获取到的结果前几行为乱码（以<开头）,为不误伤xml格式，添加不以>结尾的条件
				{
					result.clear();
					resultSB.setLength(0);
					continue;
				}
				result.add(line);
				if (result.size() > outputMaxLine)
				{
					LoggerTool.warn("======警告：命令输出超过" + outputMaxLine + "行，容易造成内存溢出，请优化命令，当前已输出行数:" + result.size()
					        + "======命令是：" + command);
				}
				resultSB.append(line + "\n");
			}
			if (isStdout)
			{
				LoggerTool.info(line);
			}
		}

	}

	/**
	 * 获取命令执行结果的函数,读取一行
	 * <p>
	 * 在调用本函数之前必须调用{@link com.huawei.SessionUp.SessionUp#waitEnd(boolean)}
	 * 来等待命令执行获得结果集。
	 * 
	 * @return 结果集的行内容，结果集全部读完返回null。
	 */
	public String readNext()
	{
		String line = null;
		if (index >= 0 && index < result.size())
		{
			line = result.get(index);
			index++;
			return line;
		}
		else
		{
			index = 0;
			return null;
		}
	}

	/**
	 * 获取命令的执行所有 结果/输出; 没有输出返回null
	 * 
	 * @return
	 */
	public String getAllOutput()
	{

		int enterIndex = resultSB.lastIndexOf("\n");
		if (enterIndex == -1)
		{
			return null;
		}

		return resultSB.substring(0, enterIndex);
	}

}
