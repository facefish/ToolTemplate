package com.qyq.utils.WebGUI;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.cookie.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.qyq.utils.LogManager.LoggerTool;
import com.qyq.utils.cmd.CommonFunction;



/**
 * 浏览器类，用于浏览器的启动、打开网页、关闭等操作
 * 
 * <p>
 * 不设置浏览器类型时，默认使用chrome
 * @author y00358428
 * @since 2017/2/18
 * @version 1.0
 */
public class Browser
{
	public static final int BROWSER_FIREFOX = 0;  //firefox浏览器序号
	public static final int BROWSER_IE = 1;       //IE浏览器序号
	public static final int BROWSER_CHROME = 2;    //chrome浏览器序号
	private  int browserType =2;  //浏览器类型序号,默认为chrome
	
	private final String DRIVER_PATH = GUIConstUtil.DRIVER_PATH; //默认浏览器driver目录
	private final String Extensions_PATH = GUIConstUtil.Extensions_PATH; //默认浏览器插件目录
	private final static String CHROME_PATH = null;//chrome浏览器chrome.exe文件目录
	private String profileName;  //配置文件

	private static WebDriver webDriver = null;

	/**
	 * 启动chrome浏览器，并打开网页
	 * @param url 
	 */
	public Browser(String url)
	{
       this(url, CHROME_PATH, null);
	}
	
	/**
	 * 指定浏览器路径启动chrome浏览器并打开网页
	 * @param url
	 * @param browserPath
	 * @param lang
	 */
	public Browser(String url, String browserPath, String lang)
	{
		this(2,url, browserPath,lang);

	}
	
	/**
	 * 指定浏览器类型，路径，语言，启动浏览器并打开网页
	 * @param type
	 * @param url
	 * @param browserPath
	 * @param lang
	 */
	public Browser(int type, String url, String browserPath, String lang)
	{
		this(type, url, null, browserPath, lang);
	}

	/**
	 * 指定浏览器类型,配置文件，浏览器路径，语言启动浏览器并打开网页
	 * @param type
	 * @param url
	 * @param profileName
	 * @param browserPath
	 * @param lang
	 */
	public Browser(int type, String url, String profileName, 
			String browserPath, String lang)
	{
		killAllChromedriver();
		killAllChrome();
		this.browserType = type;
		this.profileName = profileName;
		webDriver = openBrowser(browserPath, lang);
		GUIConstUtil.driver = webDriver;
		openUrl(url);

	}

	private void killAllChrome()
    {
	    // TODO 自动生成的方法存根
		CommonFunction.executeCMD("taskkill /IM chromedriver.exe /F",false);
    }

	/**
	 * 杀死所有chromedirver.exe进程
	 */
	private void killAllChromedriver()
	{
		CommonFunction.executeCMD("taskkill /IM chrome.exe /F",false);
	}
	
	/**
	 * 启动浏览器
	 * @param browserPath
	 * @param lang
	 * @return
	 */
	private WebDriver openBrowser(String browserPath, String lang)
	{
		WebDriver driver = null;
		switch (this.browserType)
		{
            
			default:
				System.setProperty("webdriver.chrome.driver", DRIVER_PATH + File.separator + "chromedriver.exe");
				System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");

				ChromeOptions options = new ChromeOptions();
				if ((browserPath != null) && (!(browserPath.isEmpty())) )
				{
					options.setBinary(browserPath);
				}
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.default_content_settings.popups", 1);
				options.setExperimentalOption("prefs", prefs);

				options.addArguments("-test-type");
				options.addArguments(new String[] { "--lang=" + lang });

				// 自动安装插件WebTest-eCapturer
				//options.addExtensions(new File(Extensions_PATH + File.separator+ "WebTest-eCapturer.crx"));

				// 判断是否固定Chrome个人资料路径（路径为：chrome.exe文件同级目录的user-data子目录）
				// 不配置VAR_Fixed_Profile_Path或参数为False则不固定
				if (profileName != null && !profileName.trim().equals(""))
				{
					options.addArguments("userDataDir=" + profileName);
				}

				options.addArguments(new String[] { "start-maximized" });

				driver = new ChromeDriver(options);

				break;
				
			case 1:
				System.setProperty("webdriver.ie.driver", DRIVER_PATH+ File.separator + "IEDriverServer64.exe");
				System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");

				InternetExplorerDriverService service = InternetExplorerDriverService
						.createDefaultService();
				DesiredCapabilities capabilities = DesiredCapabilities
						.internetExplorer();
				capabilities.setJavascriptEnabled(true);
				capabilities.setCapability("ignoreZoomSetting", true);
				capabilities.setCapability("nativeEvents", false);
				capabilities.setCapability("ignoreProtectedModeSettings", true);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
						true);
				if ((browserPath != null) && (!(browserPath.isEmpty())))
				{
					System.setProperty("webdriver.ie.bin", browserPath);
				}

				driver = new InternetExplorerDriver(service, capabilities);

				break;
			case 0:
				System.setProperty("org.apache.commons.logging.Log",
						"org.apache.commons.logging.impl.NoOpLog");

				// 指定firefox的安装路径(启动指定路径下的Firefox)
				if ((browserPath != null) && (!(browserPath.isEmpty())))
				{
					System.setProperty("webdriver.firefox.bin", browserPath);
				}// else:启动默认安装路径下的Firefox

				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("intl.accept_languages", lang);
				
				profile.setPreference(
								"browser.helperApps.neverAsk.saveToDisk",
								"application/octet-stream, application/vnd.ms-excel, text/csv, application/zip");
				profile.setPreference("browser.download.useDownloadDir", false);
				profile.setPreference("browser.helperApps.alwaysAsk.force",
						false);
				profile.setPreference(
						"browser.download.manager.showWhenStarting", false);

				if (profileName == null || profileName.trim().equals(""))
				{

					driver = new FirefoxDriver(profile);

				}
				else
				{
					ProfilesIni profileIni = new ProfilesIni();
					FirefoxProfile profile2 = profileIni.getProfile(profileName);
					

					driver = new FirefoxDriver(profile2);

				}

		}

		
			 if (!(driver instanceof ChromeDriver)) {
			    	WebDriver.Options options = driver.manage();
			    	WebDriver.Window window = options.window();
			    	window.maximize();
			    }
			    
	
		return driver;
	}


	/*
	 * 打开网页
	 * @param url
	 */
	public WebDriver openUrl(String url)
	{
		webDriver.get(url);
		LoggerTool.info("Open URL : " + url);
		return webDriver;
	}

	
	/** 
	 * 切换浏览器标签页面
	 * @param tabTile, 支持部分匹配和或匹配， 使用|分隔
	 */
	public static void switchToWindow(String tabTitle)
	{
		Set<String> allWindowsId = Browser.getDriver().getWindowHandles();
		
		String[] titles = tabTitle.split("\\|");
		
		boolean isSuccess = false;
		
		for(String title :titles)
		{
			for (String windowId : allWindowsId)
			{
				if (Browser.getDriver().switchTo().window(windowId).getTitle().contains(title.trim())) 
				{
					Browser.getDriver().switchTo().window(windowId);
					LoggerTool.info("-------------[actionNo=Special];[操作=switchToWindow:" + Browser.getDriver().getTitle() +"]");
					isSuccess = true;
					return;
				}
			}
		}
		
		if(!isSuccess)
		{
			LoggerTool.error("===========ERROR: driver switch to tab title=" + tabTitle + " failed ================================");
		}
	}
	
	
	/**
	 * 浏览器操作，后退
	 */
	public void back()
	{
		webDriver.navigate().back();
	}

	/**
	 * 浏览器操作，向前
	 */
	public void forward()
	{
		webDriver.navigate().forward();
	}

	/**
	 * 浏览器操作刷新
	 */
	public void refresh()
	{
		webDriver.navigate().refresh();
	}

	/**
	 * 最大化浏览器
	 */
	public void maximize()
	{
		webDriver.manage().window().maximize();
	}

	/**
	 * 关闭浏览器
	 */
	public void closeBrowser()
	{
		if(webDriver != null)
		{
		      webDriver.quit();
		}
	}

	
	public void  closeCurrentPage()
	{
		webDriver.close();
	}
	
	/**
	 * 按照标题关闭浏览器的标签
	 */
	public void closePage(String title)
	{
		if (title == null)
		{
			webDriver.quit();
		}
		String[] titles = title.split("\\|");
		
			for (String t : titles)
			{
				Set<String> handles = webDriver.getWindowHandles();
				for (String handle : handles)
				{
					String temp = webDriver.switchTo().window(handle).getTitle();
					if (temp.equals(t))
					{
						webDriver.close();
					}
				}
			}
		
	}

	public static WebDriver getDriver()
	{
		return webDriver;
	}

	/**
	 * 滚屏到网页顶端
	 */
	public void scrollToTop()
	{
		String js = "window.scrollTo(0, 1)";
		((JavascriptExecutor) webDriver).executeScript(js);
	}

	/**
	 * 滚屏到网页底端
	 */
	public void scrollToEnd()
	{
		String js = "window.scrollTo(0, 10000)";
		((JavascriptExecutor) webDriver).executeScript(js);
	}

	/** 
	 * 获取当前浏览器的cookie
	 * @return
	 */
	public Map<String, String> getCookies()
	{
		Map<String, String> newCookies = new HashMap<String, String>();
		Set<org.openqa.selenium.Cookie> cookies = webDriver.manage().getCookies();
		for (org.openqa.selenium.Cookie cookie : cookies)
		{
			newCookies.put(cookie.getName(), cookie.getValue());
		}
		return newCookies;
	}

	/**
	 *  获取指定cookie
	 * @param name 
	 * @return
	 */
	public String getCookie(String name)
	{
		org.openqa.selenium.Cookie cookie = webDriver.manage().getCookieNamed(name);
		if (cookie == null)
		{
			return "null";
		}
		return cookie.getValue();
	}

	
	/**
	 * webdriver是否已经调用了quit方法(即浏览器已关闭）
	 * 调用了quit方法的webdriver的toString方法返回为null,如ChromeDriver: chrome on XP (null)
	 * @return
	 */
	public static boolean isCallQuit()
	{
		boolean is = false;

		if(webDriver == null || webDriver.toString().contains("(null)"))
		{
			is = true;
		}
		return is;
	}
	
}
