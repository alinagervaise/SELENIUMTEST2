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
public class TestSouscriptionUKMultiExcel {
	private WebDriver driver;
	private WebDriverWait wait;
	private final String BASE_URL = "https://staging-store-rcibsp.demandware.net";
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private java.text.SimpleDateFormat sf;
	private Calendar c;
	private JavascriptExecutor jse2;
	private DesiredCapabilities capabilities;
	 public static Logger LOGGER = Logger.getLogger(TestSouscriptionUKMultiExcel.class.getName());  
	 public FileHandler fileHandler;  
	 String errorMessage = "";
	/**
	 * @throws java.lang.Exception
	 */


	@Before
	public void setUp() throws Exception {
		
		 driver = new WebDriverFactory().getDriver(DriverType.FIREFOX);
		 //driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		 LOGGER.log(Level.INFO,"===================DRIVER"+driver);
		 driver.manage().timeouts().implicitlyWait(ConstantUtils.IMPLICIT_WAIT_TIME, TimeUnit.MINUTES);
		 wait = new WebDriverWait(driver, 1);
		 sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		 c = Calendar.getInstance();
		 jse2 = (JavascriptExecutor)driver;
		 
		 java.text.SimpleDateFormat sf0 = new java.text.SimpleDateFormat("dd_MM_yyyy");
		 Date date = new Date(System.currentTimeMillis());
		 String currentDateStr = sf0.format(date);
		 String logFile = "errLogUK"+currentDateStr+".log";
		 String handlerLogFile = String.join(FileSystems.getDefault().getSeparator(),
				 						ConstantUtils.SCREENHOT_FOLDER_PATH, logFile);
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
			  List<Map<String, String>> result = loader.readFile(ConstantUtils.INPUT_FILE_PATH_UK, Country.UK);
			  String infotest = "Execute "+ this.getClass().getSimpleName()+ "\n";
			  infotest += "Reading data from file : "+ ConstantUtils.INPUT_FILE_PATH_UK +"\n";
			  LOGGER.info(infotest);
			  int count = 0;
			  for (Map m : result){
				  errorMessage = "";
				  if (m.isEmpty()){
					  continue;
				  }
				  count += 1;
				  LOGGER.info("Beginning Test :  testCaseSouscriptionUserExist \n");
				  LOGGER.info("for data set " + count +":"+ m +"\n");
				  try{
				    	runSelenium(m);
				    	LOGGER.info("End of Test: testCaseSouscriptionUserExist for data set " + count +" OK!\n");
				  }
				  catch(GUIException e){
					  if (e != null){
						  takeScreenshot(e);
						  logout(driver);
						  LOGGER.info("End of Test: testCaseSouscriptionUserExist for data set " + count +" ERROR!\n");
						  continue;
					  }  
				  }
				  catch(Exception ex){
					  LOGGER.log(Level.SEVERE, ex.getClass().getName()+ "   "+ex.getMessage());
					  logout(driver);
					  continue;
				  }
		  }
		  }catch(IOException ex){
			  LOGGER.log(Level.SEVERE, ex.getClass().getName()+ "   "+ex.getMessage());
		  }
	   
	  }

	private void takeScreenshot(Exception ex) throws IOException {
		File errFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		  java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd_MM_yyyy_HHmmss");
		  Date date = new Date(System.currentTimeMillis());
		  String currentDateStr = sf.format(date);
		  String screenshotFile = "screenshotUK"+currentDateStr+".png";
		  String outputPath = String.join(FileSystems.getDefault().getSeparator(),
				  							ConstantUtils.SCREENHOT_FOLDER_PATH,
				  							screenshotFile);
		  Files.copy( errFile, new File(outputPath));
		  
	}

	public void runSelenium(Map<String, String> resultSet) throws ParseException, InterruptedException, GUIException {
		driver.get(BASE_URL + "/s/RCI_UK/");
		By menuBy =By.xpath("//nav[@id='navigation']/ul/li[2]/a");
	    new FluentWait<WebDriver>(driver)
	    .withTimeout(1, TimeUnit.MINUTES)
	    .pollingEvery(5, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.and(
	    		ExpectedConditions.visibilityOfElementLocated(menuBy),
	    		ExpectedConditions.elementToBeClickable(menuBy)
	    		));
	    WebElement menuProductElement = driver.findElement(menuBy);
	    menuProductElement.click();
	    driver.findElement(By.xpath("//button[@class='add-all-to-cart product-0']")).click();
	    driver.findElement(By.xpath("//div[@id='ui-id-1']/div[2]/a[2]")).click();
	    driver.findElement(By.xpath("(//form[@id='checkout-form']/fieldset/button)[2]")).click();
	    LOGGER.log(Level.INFO, "Product was seleted ");
	    login(driver, resultSet);
	    
	    
	    //checkUserInfo(driver, resultSet);
	   
	   
	    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='dwfrm_billing_save']"))).click();
	    getSouscription(driver, resultSet);

	    makePayment(driver, resultSet);
		
	    WebElement el = driver.findElement(By.xpath("//div[@class='header-banner-right']/ul/li/a/i"));


	    new FluentWait<WebDriver>(driver)
	    .withTimeout(3, TimeUnit.MINUTES)
	    .pollingEvery(5, TimeUnit.SECONDS)
	    .ignoring(WebDriverException.class)
	    .until(ExpectedConditions.urlToBe("https://staging-store-rcibsp.demandware.net/s/RCI_UK/orderconfirmed"));
	    
	    LOGGER.log(Level.INFO,"Payment Completed successfully\n");
	
	    logout(driver);
	   
	}


	private void checkCurrentURL(String expectedURL) {
		String URL = driver.getCurrentUrl();
	    Assert.assertEquals(expectedURL, URL);
	    LOGGER.log(Level.INFO, "Now in page:"+ expectedURL + "\n");
	    LOGGER.log(Level.INFO, "URL OK"+ "\n");
	}
	private void logout(WebDriver driver) {
		removeProduct(driver);
	    LOGGER.log(Level.INFO,"Logout begining. To start a new set of data \n");
		String xPath = "//div[@class='header-banner-right']/ul/li/a/i";
		 new FluentWait<WebDriver>(driver)
		    .withTimeout(300, TimeUnit.SECONDS)
		    .pollingEvery(2, TimeUnit.MILLISECONDS)
		    .ignoring(WebDriverException.class)
		    .until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
		    driver.findElement(By.xpath(xPath)).click();
		 xPath = "//span[@class='account-logout']/a";
		 if (driver.findElements(By.xpath(xPath)).size() > 0){
			 WebElement accountElement = driver.findElement(By.xpath(xPath));
			 accountElement.click();
			 LOGGER.log(Level.INFO,"LOG OUT : OK\n");
		 }
		
		
	
	}
	private void removeProduct(WebDriver driver) {
	    LOGGER.log(Level.INFO,"REMOVE PRODUCT \n");
		String xPath = "//div[@id='mini-cart']/div/a/i";
		WebElement cartElement = driver.findElement(By.xpath(xPath));
		jse2.executeScript("arguments[0].scrollIntoView()", cartElement);
		cartElement.click();
		 
		xPath = "//div[@id='mini-cart']/div/a/span";
		WebElement cartQtyElement = driver.findElement(By.xpath(xPath));
		if (cartQtyElement.getText().trim().equalsIgnoreCase("1")){
			xPath = "//button[@name='dwfrm_cart_shipments_i0_items_i0_deleteProduct']";
			WebElement removeProductElement = driver.findElement(By.xpath(xPath));
			removeProductElement.click();
			LOGGER.log(Level.INFO,"REMOVE PRODUCT DONE : OK\n");
		}
		
	}
	private void getSouscription(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
		String errorMessage = "";
		String successMessage = "";
		boolean errorExist = false;
		String field = "";
		String xPath = "";
		String URL = driver.getCurrentUrl();
		String expectedUrl = "https://staging-store-rcibsp.demandware.net/s/RCI_UK/subscription";
		this.checkCurrentURL(expectedUrl);
	
		
		field = "Registration";
		successMessage = "Setting "+ field +" field to " + resultSet.get("Registration");
		xPath = "//*[@id='dwfrm_billing_subscriptionInformation_plate']";
	    WebElement plateElement = driver.findElement(By.xpath(xPath));
	    jse2.executeScript("arguments[0].scrollIntoView()", plateElement); 
	    plateElement.clear();
	    plateElement.sendKeys(resultSet.get("Registration"));
	    plateElement.sendKeys(Keys.TAB);
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !( errorMessage.isEmpty());
	    
	   
	    field = "Brand";
	    successMessage = "Setting "+ field +" field to " + resultSet.get("Brand").toUpperCase();
	    xPath = "//*[@id='dwfrm_billing_subscriptionInformation_vehicleInfoBrand']";
	    new Select(driver.findElement(By.xpath(xPath))).selectByVisibleText(resultSet.get("Brand").toUpperCase());
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage.isEmpty());
	    
	    field = "Model";
	    successMessage = "Setting "+ field +" field to " + resultSet.get("Model").toUpperCase();
	    xPath = "//*[@id='dwfrm_billing_subscriptionInformation_vehicleInfoModel']";
	    new Select(driver.findElement(By.xpath(xPath))).selectByVisibleText(resultSet.get("Model"));
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage.isEmpty());
	
	   
	    String strCarDate = resultSet.get("Vehicle Insurance Date");
	    field = "Vehicle insurance Date";
	    LOGGER.log(Level.INFO, "Setting "+ field +" field to " + strCarDate+"\n");
		Date dateVehicle =  sf.parse(strCarDate);
	   
	 	c.setTime(dateVehicle);
	 	xPath = "//img[@class='ui-datepicker-trigger']";
	    WebElement datepicker = driver.findElement(By.xpath("//img[@class='ui-datepicker-trigger']"));
	    datepicker.click();
	   
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-year"))).selectByValue(Integer.toString(c.get(Calendar.YEAR)));
	    new Select(driver.findElement(By.cssSelector("select.ui-datepicker-month"))).selectByValue(Integer.toString(c.get(Calendar.MONTH)));
	    driver.findElement(By.linkText(""+c.get(Calendar.DATE))).click();
	    
		 
	    
		By byAgreeTerms = By.name("dwfrm_billing_subscriptionInformation_agreeTerms"); 
		
		 new FluentWait<WebDriver>(driver)
		    .withTimeout(2, TimeUnit.MINUTES)
		    .pollingEvery(2, TimeUnit.SECONDS)
		    .ignoring(WebDriverException.class)
		    .until(ExpectedConditions.elementToBeClickable(byAgreeTerms));
		 WebElement agreeTermsElment = driver.findElement(byAgreeTerms); 
		 jse2.executeScript("arguments[0].scrollIntoView()", agreeTermsElment);

		 jse2.executeScript("arguments[0].click()", agreeTermsElment);
		 LOGGER.log(Level.INFO, "Click Agree on terms: OK!\n");
		 raiseError(errorExist);
	}

	private void raiseError(boolean errorMessageExist) throws GUIException {
		if (errorMessageExist){
			throw new GUIException();
		}
	}
	private String logError(WebDriver driver, String xPath, String field, String successMessage) throws GUIException {
		String errorMessage = "";
		errorMessage = getGUIError(driver, xPath);
		if ((errorMessage != null )&&(!errorMessage.isEmpty())){
			LOGGER.log(Level.SEVERE, "ERROR on field: '"+ field + "' :"+errorMessage+ "\n");
		}
		else {
			errorMessage = "";
			LOGGER.log(Level.INFO, successMessage+ ": OK! \n");
		}
		return errorMessage;
	}
	private void makePayment(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
		String field = "";
		String xPath = "";
		String errorMessage = "";
		String successMessage = "";
		boolean errorExist = false;
		
		field = "Continue to order";
		LOGGER.log(Level.INFO, "Click on button '"+ field + "' \n");
		xPath = "//*[@name='dwfrm_billing_save']";
		WebElement billingSaveElment = driver.findElement(By.xpath(xPath));
		jse2.executeScript("arguments[0].scrollIntoView()", billingSaveElment); 
		billingSaveElment.click();
		String expectedURL = "https://staging-store-rcibsp.demandware.net/s/RCI_UK/payment";
		this.checkCurrentURL(expectedURL);
		
		WebElement billingSaveElment1 = driver.findElement(By.name("dwfrm_billing_save"));
		jse2.executeScript("arguments[0].scrollIntoView()", billingSaveElment1); 
		billingSaveElment1.click();
		expectedURL = "https://staging-store-rcibsp.demandware.net/s/RCI_UK/placeorder";
		this.checkCurrentURL(expectedURL);
		
	    field = "Visa Card";
	    LOGGER.log(Level.INFO, "Click on button '"+ field +"' \n");
	    xPath = "//*[@name='dwfrm_billing_paymentMethods_selectedPaymentMethodID']";
		WebElement paymentMethodElment = driver.findElement(By.xpath(xPath));
		jse2.executeScript("arguments[0].scrollIntoView()", paymentMethodElment); 
		paymentMethodElment.click();
	    
	    field = "Place Order";
	    LOGGER.log(Level.INFO, "Click on button '"+ field +"' \n");
	    driver.findElement(By.id("placeOrder")).click();
	    
	    field = "Card number";
	    xPath = "//input[@name='cardNumber']";
	    successMessage = "Set value of  '"+ field +"' to: " +resultSet.get("Card number");
	    WebElement cardNumberElement = driver.findElement(By.xpath(xPath));
	    cardNumberElement.clear();
	    cardNumberElement.sendKeys(resultSet.get("Card number"));
	    cardNumberElement.sendKeys(Keys.RETURN);
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage == null || errorMessage.isEmpty());
	 
	    String strDate = resultSet.get("Expiry date");
		Date date =  sf.parse(strDate);
		c.setTime(date);
		
		field = "Expiration Month";
		xPath = "//select[@id='expiryMonth']";
		successMessage = "ENTER  '"+ field +"' to: " +String.format("%02d", c.get(Calendar.MONTH));
	    new Select(driver.findElement(By.xpath(xPath))).selectByValue(String.format("%02d", c.get(Calendar.MONTH)));
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage == null || errorMessage.isEmpty());
	    
	    field = "Expiration Year";
	    xPath = "//select[@id='expiryYear']";
	    successMessage = "ENTER  '"+ field +"' to: " +String.format("%02d", c.get(Calendar.YEAR));
	    new Select(driver.findElement(By.xpath(xPath))).selectByValue(Integer.toString(c.get(Calendar.YEAR)));
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage == null || errorMessage.isEmpty());
	    
	    field = "Security Code";
	    xPath = "//input[@id='securityCode']";
	    successMessage = "ENTER '"+ field +"' to: " +resultSet.get("Security number");
	    WebElement securityCodeElement = driver.findElement(By.xpath(xPath));
	    securityCodeElement.clear();
	    securityCodeElement.sendKeys(resultSet.get("Security number"));
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage == null || errorMessage.isEmpty());
	    
	    field = "Submit";
	    LOGGER.log(Level.INFO, "Click on button '"+ field +"' \n");
	    driver.findElement(By.id("submitButton")).click();
	    raiseError(errorExist);
	    
	}
	private void login(WebDriver driver, Map<String, String> resultSet) throws GUIException {
		String errorMessage = "";
		String successMessage = "";
		String xPath = "";
		boolean errorExist = false;
		String expectedURL= "https://staging-store-rcibsp.demandware.net/s/RCI_UK/login";
		String URL = driver.getCurrentUrl();
	    Assert.assertTrue(URL.startsWith(expectedURL));
	    LOGGER.log(Level.INFO, "Now in page:"+ URL + "\n");
	    
		String field = "Username";
		successMessage = "ENTER  '"+ field +"' to: " + resultSet.get("email");
		xPath = "//form[@id='dwfrm_login']/fieldset/div/div/input";
		WebElement loginElement = driver.findElement(By.xpath(xPath));
		loginElement.clear();
	    loginElement.sendKeys(resultSet.get("email"));
	    loginElement.sendKeys(Keys.TAB);
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage.isEmpty() );
	    
	    field = "Password";
	    successMessage ="ENTER '"+ field +"' to: " + resultSet.get("password");
	    xPath = "//form[@id='dwfrm_login']/fieldset/div[2]/div/input";
	    WebElement passwordElement = driver.findElement(By.xpath(xPath));
	    passwordElement.clear();
	    passwordElement.sendKeys(resultSet.get("password"));
	    passwordElement.sendKeys(Keys.TAB);
	    errorMessage = logError(driver, xPath, field, successMessage);
	    errorExist  |= !(errorMessage.isEmpty());

	    field = "Login";
	    WebElement loginButtonElement = driver.findElement(By.name("dwfrm_login_login"));
	    jse2.executeScript("arguments[0].scrollIntoView()", loginButtonElement); 
	    loginButtonElement.click();
	    raiseError(errorExist);
	    LOGGER.log(Level.INFO, "Click on button '"+ field +errorExist+"'OK! \n");
	    
	    checkCurrentURL("https://staging-store-rcibsp.demandware.net/s/RCI_UK/shipping");
	    LOGGER.log(Level.INFO, "User "+resultSet.get("email")+ "  is connected\n");
	    
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
	private void checkUserInfo(WebDriver driver, Map<String, String> resultSet) throws ParseException, GUIException{
			String actualValue = "";
			String errorMessage= "Fail to fill field: ";
			String field;
		    new Select(driver.findElement(By.id("dwfrm_billing_title"))).selectByVisibleText("Miss");
		    
		    java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		    Calendar c = Calendar.getInstance();
		    
		    field = "Date of birth";
		    String strCarDate = resultSet.get("date of birth");
			Date dateVehicle =  sf.parse(strCarDate);
		 	c.setTime(dateVehicle);
		    WebElement datepicker = driver.findElement(By.cssSelector("img.ui-datepicker-trigger"));
		   
			jse2.executeScript("arguments[0].scrollIntoView()", datepicker);
		   
		    String value = String.format("%02d",c.get(Calendar.DATE))+"-"
		    				+String.format("%02d",c.get(Calendar.MONTH))+"-"
		    				+Integer.toString(c.get(Calendar.YEAR));

		    String xPath = "//input[@id='dwfrm_billing_billingAddress_addressFields_birthday']";
            String dateOfBirth = driver.findElement(By.xpath(xPath)).getText();
            Assert.assertEquals(errorMessage+field, value, dateOfBirth);
            
		    field = "Mobilephone";
		    actualValue = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_mobilephone")).getText();
		    Assert.assertEquals(errorMessage+field, resultSet.get("phone number"), actualValue);
		    
		    field ="Address 1";
		    actualValue = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address1")).getText();
		    Assert.assertEquals(errorMessage+field, resultSet.get("Property number / Name"), actualValue);
		    
		    field ="Address 2";
		    WebElement streetElement = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_address2"));
		    jse2.executeScript("arguments[0].scrollIntoView()", streetElement);
		    actualValue = streetElement .getText();
		    Assert.assertEquals(errorMessage+field, resultSet.get("Street"), actualValue);
		    
		    
		    driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_postal")).clear();
		    WebElement postalCodeElement = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_postal"));
		    jse2.executeScript("arguments[0].scrollIntoView()", postalCodeElement);
		    postalCodeElement.sendKeys(resultSet.get("Postal Code"));
		    
		    
		    field = "city";
		    actualValue = driver.findElement(By.id("dwfrm_billing_billingAddress_addressFields_city")).getText();
		    Assert.assertEquals(errorMessage+field, resultSet.get("City"), actualValue);
		  
	}
	private String getGUIErrorHelper(WebElement element) {
		String errorMessage = "";
		String msg = null;
		if (element != null){
			msg = element.getAttribute("innerHTML") ;
			if (msg != null){
				errorMessage = String.join("\n", msg);
			}
			return errorMessage;
		}
		return errorMessage;
	}
	
	private String getGUIError(WebDriver driver, String fieldXpath) {
		String errorMessage = "";
		String xPath = "";
		xPath = fieldXpath+"/following-sibling::span[contains(@class, 'error')]";
		if (driver.findElements(By.xpath(xPath)).size() >0){
			errorMessage = getGUIErrorHelper(driver.findElement(By.xpath(xPath)));
			if (errorMessage.isEmpty()){
				return errorMessage;
			}
		}
		xPath = fieldXpath+"/following-sibling::div[contains(@class, 'error')]";
		if (driver.findElements(By.xpath(xPath)).size() > 0){
			errorMessage = getGUIErrorHelper(driver.findElement(By.xpath(xPath)));
		}
		return errorMessage;
		}
	private String getGUIError(WebDriver driver) {
		String errorMessage = "";
	
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