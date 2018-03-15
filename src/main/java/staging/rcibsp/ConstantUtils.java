package staging.rcibsp;

import java.nio.file.FileSystems;

public  class ConstantUtils {
	public final static String INPUT_FILE_PATH = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","excelExportAndFileIO");
	
	public final static String INPUT_FILE_PATH_UK = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","excelExportAndFileIO", 
													"jeu_de_test_UK.xlsx");
	public final static String INPUT_FILE_PATH_BR = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","excelExportAndFileIO", 
													"jeu_de_test_BR.xlsx");
	public final static String INPUT_FILE_PATH_PL = String.join(FileSystems.getDefault().getSeparator(),
														System.getProperty("user.dir"),
														"src","excelExportAndFileIO", 
														"jeu_de_test_PL.xlsx");
	public final static String INPUT_FILE_PATH_DE = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","excelExportAndFileIO", 
													"jeu_de_test_DE.xlsx");
	public final static String SCREENHOT_FOLDER_PATH =  String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","errorScreenshots");
	public final static String DRIVER_FOLDER_PATH = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","SELENIUM_DRIVERS");
	public final static String FIREFOX_DRIVER_PATH_WIN = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","SELENIUM_DRIVERS", 
													"geckodriver-v0.19.1-win64", "geckodriver.exe");
	public final static String FANTOME_DRIVER_PATH_WIN = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
														"src","SELENIUM_DRIVERS", 
														"phantomjs-2.1.1-windows", 
														"bin","phantomjs.exe");
	public final static String CHROME_DRIVER_PATH_WIN = String.join(FileSystems.getDefault().getSeparator(),
														System.getProperty("user.dir"),
														"src","SELENIUM_DRIVERS", 
														"chromedriver_win32", "chromedriver.exe");
	public final static String IE_DRIVER_PATH_WIN = String.join(FileSystems.getDefault().getSeparator(),
													System.getProperty("user.dir"),
													"src","SELENIUM_DRIVERS", 
												"IEDriverServer_Win32_3.8.0", "IEDriverServer.exe");

	public final static String IE_DRIVER_PATH_LINUX ="";
	public final static String FIREFOX_DRIVER_PATH_LINUX = String.join(FileSystems.getDefault().getSeparator(),
										"usr", "bin","geckodriver");

	public final static String CHROME_DRIVER_PATH_LINUX  = String.join(FileSystems.getDefault().getSeparator(),
										"usr", "bin","chromedriver");
	public final static String FIREFOXPATH_59 = String.join(FileSystems.getDefault().getSeparator(),
							"C:", "Program Files (x86)","Mozilla Firefox", "firefox.exe");
	public final static String FIREFOXPATH = String.join(FileSystems.getDefault().getSeparator(),
			"C:", "Firefox", "firefox.exe");
	
	public final static int IMPLICIT_WAIT_TIME = 10;



}
