package staging.rcibsp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class WebDriverFactory {
	
	
	public  WebDriver getDriver( DriverType driverType){
		WebDriver _Driver = null;
		DesiredCapabilities capabilities;
		String osName = System.getProperty("os.name");
		switch(driverType){
			case FIREFOX:
				if (osName.toLowerCase().contains("windows")){
					 System.setProperty("webdriver.gecko.driver", ConstantUtils.FIREFOX_DRIVER_PATH_WIN);
					
				}
				else{
					System.setProperty("webdriver.gecko.driver", ConstantUtils.FIREFOX_DRIVER_PATH_LINUX);
				}
				capabilities=DesiredCapabilities.firefox();
				//capabilities= new DesiredCapabilities("firefox", "", Platform.WINDOWS);
				capabilities.setBrowserName("firefox");
				capabilities.setCapability("marionette", false);
				//capabilities.setCapability("version", "59.0");
				//capabilities.setCapability("version", "47.0");
				capabilities.setCapability("firefox binary", ConstantUtils.FIREFOXPATH);
				
			    _Driver = new FirefoxDriver(capabilities);
//				try {
//					_Driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				//FirefoxBinary firefoxBinary = new FirefoxBinary();
				//firefoxBinary.addCommandLineOptions("--headless");
				//FirefoxOptions firefoxOptions = new FirefoxOptions();
				//firefoxOptions.setBinary(firefoxBinary);
				//_Driver = new FirefoxDriver(firefoxOptions);
			  
			     break;
			case CHROME:
				if (osName.toLowerCase().contains("windows")){
					System.setProperty("webdriver.chrome.driver", ConstantUtils.CHROME_DRIVER_PATH_WIN);
				}
				else{
					System.setProperty("webdriver.chrome.driver", ConstantUtils.CHROME_DRIVER_PATH_LINUX);
				}
				 ChromeOptions options = new ChromeOptions();
				 options.addArguments("--headless");
				 options.addArguments("--start-maximized");
				 _Driver = new ChromeDriver(options);
				 break;	
			case INTERNETEXPLORER:
				if (osName.toLowerCase().contains("windows")){
				 System.setProperty("webdriver.ie.driver", ConstantUtils.IE_DRIVER_PATH_WIN);
				}
				else{
					System.setProperty("webdriver.ie.driver", ConstantUtils.IE_DRIVER_PATH_LINUX);
				}
				 capabilities = DesiredCapabilities.internetExplorer();
				 capabilities.setCapability(InternetExplorerDriver.
						 INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
				 capabilities.setVersion("11");
			     _Driver = new InternetExplorerDriver(capabilities);
			    
			     break;
			case HTMLUNITDRIVER:
				//_Driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER_11);
				_Driver = new HtmlUnitDriver();
				
				break;
			case PHANTOMEJS:
				String phantomJsBinaryPath = System.setProperty("phantomjs.binary.path",
										ConstantUtils.FANTOME_DRIVER_PATH_WIN);
				 DesiredCapabilities caps = new DesiredCapabilities();
			     caps.setCapability(
			                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
			                phantomJsBinaryPath);
			     
			     caps.setJavascriptEnabled(true);
			     
			     ArrayList<String> cliArgsCap = new ArrayList();
			     cliArgsCap.add("--web-security=false");
			     cliArgsCap.add("--ssl-protocol=any");
			     cliArgsCap.add("--ignore-ssl-errors=true");
			     cliArgsCap.add("--webdriver-loglevel=ERROR");

			     caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
				 caps.setCapability("takesScreenshot", true);
			     _Driver = new PhantomJSDriver(caps);
			   _Driver.manage().window().maximize();
			
		}
		return _Driver;
	}

}
