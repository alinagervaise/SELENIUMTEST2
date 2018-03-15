/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

import staging.rcibsp.ConstantUtils;
import staging.rcibsp.Country;
import staging.rcibsp.DriverType;
import staging.rcibsp.ExcelReader;
import staging.rcibsp.GUIException;
import staging.rcibsp.Loader;
import staging.rcibsp.WebDriverFactory;


/**
 * @author galinabikoro
 *
 */
public class TestSouscriptionPLMultiExcel {
	private WebDriver driver;
	private WebDriverWait wait;
	private final String BASE_URL = "https://staging-store-rcibsp.demandware.net";
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private java.text.SimpleDateFormat sf;
	private Calendar c;
	private JavascriptExecutor jse2;
	private DesiredCapabilities capabilities;
	 public static Logger LOGGER = Logger.getLogger(TestSouscriptionPLMultiExcel.class.getName());  
	 public FileHandler fileHandler;  
	 String errorMessage = "";
	/**
	 * @throws java.lang.Exception
	 */


	@Before
	public void setUp() throws Exception {
		
		 driver = new WebDriverFactory().getDriver(DriverType.FIREFOX);
		 driver.manage().timeouts().implicitlyWait(ConstantUtils.IMPLICIT_WAIT_TIME, TimeUnit.MINUTES);
		 wait = new WebDriverWait(driver, 1);
		 sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		 c = Calendar.getInstance();
		 jse2 = (JavascriptExecutor)driver;
		 
		 java.text.SimpleDateFormat sf0 = new java.text.SimpleDateFormat("dd_MM_yyyy");
		 Date date = new Date(System.currentTimeMillis());
		 String currentDateStr = sf0.format(date);
		 String logFile = "errLogPL"+currentDateStr+".log";
		 String handlerLogFile = String.join(ConstantUtils.SCREENHOT_FOLDER_PATH, logFile);
		 fileHandler = new FileHandler( handlerLogFile, true);  
		 
	     LOGGER.addHandler(fileHandler);
	     SimpleFormatter formatter = new SimpleFormatter();  
	     fileHandler.setFormatter(formatter);
	}

	  @Test
	  public void testCaseSouscriptionUserExist() throws IOException,ParseException, InterruptedException {
		  try{
			  ExcelReader objExcelFile = new ExcelReader();
			  Loader loader = new Loader();
			  loader.setReader(new ExcelReader());
			  List<Map<String, String>> result = loader.readFile(ConstantUtils.INPUT_FILE_PATH_PL, Country.PL);
			  
			  for (Map m : result){
				  errorMessage = "";
				  if (m.isEmpty()){
					  continue;
				  }
				 
				  LOGGER.info("Execute Souscription for :\n"+ m +"\n");
				  try{
				    	runSelenium(m);
				  }
				  catch(GUIException e){
					  if (e != null){
				
						  generateLog(e);
					
					  LOGGER.log(Level.WARNING, e.getMessage());
					  logout(driver);
					  continue;
					  }
					  
				  }
				  catch(Exception ex){
					  LOGGER.log(Level.SEVERE, ex.getClass().getName()+ "   "+ex.getMessage());
					  generateLog(ex);
				  
				  }
				  
		  }
		  }catch(IOException ex){
			  generateLog(ex);
		  }
	   
	  }

	private void generateLog(Exception ex) throws IOException {
		File errFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		  java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd_MM_yyyy_HHmmss");
		  Date date = new Date(System.currentTimeMillis());
		  String currentDateStr = sf.format(date);
		  String screenshotFile = "screenshotPL"+currentDateStr+".png";
		  String outputPath = String.join(FileSystems.getDefault().getSeparator(),
				  							ConstantUtils.SCREENHOT_FOLDER_PATH,
				  							screenshotFile);
		  Files.copy( errFile, new File(outputPath));
		  
	}

	public void runSelenium(Map<String, String> resultSet) throws ParseException, InterruptedException, GUIException {
		driver.get(BASE_URL + "/s/RCI_PL/");
	
	    this.selecProduct(driver);
	    login(driver, resultSet);
	    String URL = driver.getCurrentUrl();
	   
	    By bySaveBill = By.cssSelector("button[name=\"dwfrm_billing_save\"]");
	    //By bySaveBill = By.name("dwfrm_billing_save");
		new FluentWait<WebDriver>(driver)
	    .withTimeout(2, TimeUnit.MINUTES)
	    .pollingEvery(2, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.presenceOfElementLocated(bySaveBill));
		
		WebElement billingSaveElment = driver.findElement(bySaveBill);
		jse2.executeScript("arguments[0].scrollIntoView()", billingSaveElment); 
		billingSaveElment.click();
	    getSouscription(driver, resultSet);

	    makePayment(driver, resultSet);
		
	   // WebElement el = driver.findElement(By.xpath("//div[@class='header-banner-right']/ul/li/a/i"));
	    
	    new FluentWait<WebDriver>(driver)
	    .withTimeout(1, TimeUnit.MINUTES)
	    .pollingEvery(2, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.urlToBe("https://staging-store-rcibsp.demandware.net/s/RCI_PL/orderconfirmed"));
	    
	    logout(driver);
	   
	}
	private void logout(WebDriver driver) {
		 driver.findElement(By.xpath("//div[@class='header-banner-right']/ul/li/a/i")).click();
		 driver.findElement(By.xpath("//span[@class='account-logout']/a")).click();
	}
	private void getSouscription(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
		
		
		WebElement datepicker = driver.findElement(By.cssSelector("img.ui-datepicker-trigger"));
		datepicker.click();

	    String strCarDate = resultSet.get("Vehicle Insurance Date");
		Date dateVehicle =  sf.parse(strCarDate);
	 	c.setTime(dateVehicle);
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-year"))).selectByValue(Integer.toString(c.get(Calendar.YEAR)));
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-month"))).selectByValue(Integer.toString(c.get(Calendar.MONTH)));
	    driver.findElement(By.linkText(""+c.get(Calendar.DATE))).click();
		
	    WebElement vinElement = driver.findElement(By.id("dwfrm_billing_subscriptionInformation_tyreinsurance_vehicleInfoVIN"));
	    jse2.executeScript("arguments[0].scrollIntoView()", vinElement); 
		vinElement.clear();
	    vinElement.sendKeys(resultSet.get("VIN"));
	 
	    String registration = resultSet.get("Registration number");
	    WebElement plate1Element = driver.findElement(By.id("dwfrm_billing_subscriptionInformation_tyreinsurance_vehicleInfoPlateNo"));
	    plate1Element.clear();
	    plate1Element.sendKeys(registration);
	    
	  
	    driver.findElement(By.xpath("(//img[@alt='...'])[2]")).click();
	    
	    String strDate1 = resultSet.get("Date first registration");
	    Date dateVehicle1 =  sf.parse(strDate1);
	 	c.setTime(dateVehicle1);
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-year"))).selectByValue(Integer.toString(c.get(Calendar.YEAR)));
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-month"))).selectByValue(Integer.toString(c.get(Calendar.MONTH)));
	    driver.findElement(By.linkText(""+c.get(Calendar.DATE))).click();
		
	    By byDateProduction = By.id("dwfrm_billing_subscriptionInformation_tyreinsurance_vehicleModelYear");
	    new FluentWait<WebDriver>(driver)
	    .withTimeout(1, TimeUnit.MINUTES)
	    .pollingEvery(2, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.presenceOfElementLocated(byDateProduction));
	 
	    WebElement criteriaConfirmationElement = driver.findElement(byDateProduction);
	    jse2.executeScript("arguments[0].scrollIntoView()", criteriaConfirmationElement); 
	    criteriaConfirmationElement.sendKeys(resultSet.get("Manufacturing Year"));
	    
	    new Select(driver.findElement(By.id("dwfrm_billing_subscriptionInformation_tyreinsurance_vehicleInfoBrand")))
	    	.selectByValue(resultSet.get("brand"));
	    new Select(driver.findElement(By.id("dwfrm_billing_subscriptionInformation_tyreinsurance_vehicleInfoModel")))
	    	.selectByValue(resultSet.get("Model").toUpperCase());
	  
	    WebElement termsElement = driver.findElement(By.id("tyreinsurancepersonaldatalegaltext"));
	    jse2.executeScript("arguments[0].scroll(0, 500)", termsElement); 
	    
	    
	    By byTermConditions = By.cssSelector("input[id=\"dwfrm_billing_subscriptionInformation_tyreinsurance_termsconditions\"]");
	    WebElement termConditionsElement = driver.findElement( byTermConditions);
	    jse2.executeScript("arguments[0].scrollIntoView()", termConditionsElement); 
	    new FluentWait<WebDriver>(driver)
	    .withTimeout(1, TimeUnit.MINUTES)
	    .pollingEvery(2, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.elementToBeClickable(termConditionsElement));
	    jse2.executeScript("arguments[0].click()", termConditionsElement); 
	    
	    driver.findElement(By.id("continue-to-place-order")).click();
		//logError(driver);
	}
	
	public void selecProduct(WebDriver driver){
		
		 //driver.findElement(By.linkText("Securplus")).click();
		 driver.findElement(By.cssSelector("ul.menu-category.level-1 li:nth-of-type(2) a")).click();
		 
		 driver.findElement(By.xpath("//div[@data-itemid='PL-P-TYREINSURANCE']")).click();
		 
		 By bySubProduct = By.cssSelector("button.add-all-to-cart.product-UBEZPIECZENIEOPON-Maxi");
		 new FluentWait<WebDriver>(driver)
		    .withTimeout(1, TimeUnit.MINUTES)
		    .pollingEvery(2, TimeUnit.SECONDS)
		    .ignoring(WebDriverException.class)
		    .until(ExpectedConditions.elementToBeClickable(driver.findElement(bySubProduct)));
		 WebElement subProductElement = driver.findElement(bySubProduct);
		 jse2.executeScript("arguments[0].scrollIntoView()", subProductElement);
		 subProductElement.click();
		 
		
		 driver.findElement(By.cssSelector("#ui-id-2 > div.actions > a.action.dialog-cart-show")).click();
		 driver.findElement(By.name("dwfrm_cart_checkoutCart")).click();
		 
	}
	private void logError(WebDriver driver) throws GUIException {
		String errorMessage = getGUIError(driver);
		if ((errorMessage != null )&&(!errorMessage.isEmpty())){
			throw new GUIException(errorMessage);
		}
	}
	private void makePayment(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
		By byMakePayment = By.name("dwfrm_billing_save");
		 new FluentWait<WebDriver>(driver)
		    .withTimeout(1, TimeUnit.MINUTES)
		    .pollingEvery(2, TimeUnit.SECONDS)
		    .ignoring(WebDriverException.class)
		    .until(ExpectedConditions.elementToBeClickable(byMakePayment));
		WebElement billingSaveElment = driver.findElement(byMakePayment);
		jse2.executeScript("arguments[0].scrollIntoView()", billingSaveElment); 
		billingSaveElment.click();
		
		
	    driver.findElement(By.id("is-WorldPay")).click();
	    
	    WebElement placeOrderElement = driver.findElement(By.id("placeOrder"));
	    jse2.executeScript("arguments[0].scrollIntoView()", placeOrderElement); 
	    placeOrderElement.click();
	    
	    driver.findElement(By.id("cardNumber")).clear();
	    //System.out.println(""+resultSet.get("Card number"));
	    driver.findElement(By.id("cardNumber")).sendKeys(resultSet.get("Card number"));
	    
	    new Select(driver.findElement(By.id("expiryMonth"))).selectByValue(String.format("%02d",
	    		Integer.parseInt(resultSet.get("Expiry Month"))));
		
	    new Select(driver.findElement(By.id("expiryYear"))).selectByValue(resultSet.get("Expiry Year"));
	   
	    driver.findElement(By.id("securityCode")).clear();
	    driver.findElement(By.id("securityCode")).sendKeys(resultSet.get("Security number"));
	    
	    WebElement submitElement = driver.findElement(By.id("submitButton"));
	    jse2.executeScript("arguments[0].scrollIntoView()", submitElement); 
	    submitElement.click();
	    //driver.findElement(By.id("submitButton")).click();
	    logError(driver);
	    
	}
	private void login(WebDriver driver, Map<String, String> resultSet) throws GUIException {
		driver.findElement(By.xpath("//form[@id='dwfrm_login']/fieldset/div/div/input")).clear();
	    driver.findElement(By.xpath("//form[@id='dwfrm_login']/fieldset/div/div/input")).sendKeys(resultSet.get("email"));
	    driver.findElement(By.xpath("//form[@id='dwfrm_login']/fieldset/div[2]/div/input")).clear();

	    driver.findElement(By.xpath("//form[@id='dwfrm_login']/fieldset/div[2]/div/input")).sendKeys(resultSet.get("password").trim());
	    //driver.findElement(By.name("dwfrm_login_login")).click();
	    WebElement loginElement = driver.findElement(By.name("dwfrm_login_login"));
	    jse2.executeScript("arguments[0].scrollIntoView()", loginElement); 
	    loginElement.click();
	    //logError(driver);
	   
	}

	 
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString) || (errorMessage != null)&& !errorMessage.isEmpty()) {
	    	LOGGER.log(Level.SEVERE, verificationErrorString);
	    	LOGGER.log(Level.SEVERE, errorMessage);
	        fail(verificationErrorString);
	    	
	    }
	  }
	private void enterUserInfo(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
		
		    new Select(driver.findElement(By.id("dwfrm_billing_title"))).selectByVisibleText("Miss");
		    java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		    Calendar c = Calendar.getInstance();
		    
		    String strCarDate = resultSet.get("date of birth");
		    //System.out.println("date of birth"+strCarDate);
			Date dateVehicle =  sf.parse(strCarDate);
			// System.out.println("date of birth"+dateVehicle +"   type "+dateVehicle.getClass().getTypeName());
		 	c.setTime(dateVehicle);
		 	// System.out.println("date of birth"+strCarDate);
		    WebElement datepicker = driver.findElement(By.cssSelector("img.ui-datepicker-trigger"));
		    //JavascriptExecutor jse2 = (JavascriptExecutor)driver;
			jse2.executeScript("arguments[0].scrollIntoView()", datepicker);
		    datepicker.click();
		    
		    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-year"))).selectByValue(Integer.toString(c.get(Calendar.YEAR)));
		    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-month"))).selectByValue(Integer.toString(c.get(Calendar.MONTH)));
		    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(""+c.get(Calendar.DATE))));
		    driver.findElement(By.linkText(""+c.get(Calendar.DATE))).click();
		   

		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_mobilephone")).clear();
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_mobilephone")).sendKeys(resultSet.get("phone number"));
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address1")).clear();
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address1")).sendKeys(resultSet.get("Property number / Name"));
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address2")).clear();
		    WebElement streetElement = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address2"));
		    jse2.executeScript("arguments[0].scrollIntoView()", streetElement);
		    streetElement.sendKeys(resultSet.get("Street"));
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_postal")).clear();
		    WebElement postalCodeElement = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_postal"));
		    jse2.executeScript("arguments[0].scrollIntoView()", postalCodeElement);
		    postalCodeElement.sendKeys(resultSet.get("Postal Code"));
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_city")).clear();
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_city")).sendKeys(resultSet.get("City"));
		    
		    logError(driver);
	}
	private String getGUIErrorHelper(WebElement element) {
		String errorMessage = "";
		String msg = null;
		if (element != null){
			msg = element.getAttribute("value") ;
			if (msg != null){
				errorMessage += String.join("\n", msg);
			}
			msg = element.getText();
			if (msg != null){
				errorMessage += String.join("\n", msg);
			}
			msg = element.getAttribute("innerHTML") ;
			if (msg != null){
				errorMessage += String.join("\n", msg);
			}
			return errorMessage;
		}
		return errorMessage;
	}
	private String getGUIError(WebDriver driver) {
		errorMessage = "";
		if (driver.findElements(By.xpath("//*[contains(@class, 'error')]")).size() > 0 ){
			errorMessage += getGUIErrorHelper(driver.findElement(By.xpath("//*[contains(@class, 'error')]")) );
		}
		if (driver.findElements(By.xpath("//span[contains(@class, 'error')]")).size() >0){
			errorMessage += getGUIErrorHelper(driver.findElement(By.xpath("//span[contains(@class, 'error')]")));
		}
		if (driver.findElements(By.xpath("//div[contains(@class, 'error')]")).size() > 0){
			errorMessage += getGUIErrorHelper(driver.findElement(By.xpath("//div[contains(@class, 'error')]")));
		}
		return errorMessage;
		}
	public static boolean isClickable(WebElement el, WebDriver driver) 
	{
		try{
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(el));
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }
}