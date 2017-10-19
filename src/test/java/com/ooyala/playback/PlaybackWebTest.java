package com.ooyala.playback;

import static java.net.URLDecoder.decode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.ooyala.facile.listners.IMethodListener;
import com.ooyala.facile.proxy.browsermob.BrowserMobProxyHelper;
import com.ooyala.facile.test.FacileTest;
import com.ooyala.playback.factory.PlayBackFactory;
import com.ooyala.playback.httpserver.SimpleHttpServer;
import com.ooyala.playback.live.LiveChannel;
import com.ooyala.playback.page.PlayBackPage;
import com.ooyala.playback.page.action.ChromeFlashUpdateAction;
import com.ooyala.playback.report.ExtentManager;
import com.ooyala.playback.updateSpreadSheet.TestCaseSheet;
import com.ooyala.playback.url.Testdata;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.playback.utils.CommandLineParameters;
import com.ooyala.playback.utils.JSScriptInjection;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

@Listeners(IMethodListener.class)
public abstract class PlaybackWebTest extends FacileTest {
    protected Logger logger = Logger.getLogger(PlaybackWebTest.class);
    protected String browser;
    protected ChromeDriverService service;
    protected PlayBackFactory pageFactory;
    protected ExtentTest extentTest;
    protected Testdata testData;
    protected UrlObject url;
    protected String[] jsUrl;
    protected LiveChannel liveChannel;
    protected RemoteWebDriver driver;
    protected static String v4Version;
    protected static String osNameAndOsVersion;
    private ChromeFlashUpdateAction chromeValidator;
    private static Map<String, ITestResult> testDetails = new HashMap<String, ITestResult>();
    public SoftAssert s_assert;

    public PlaybackWebTest() throws OoyalaException {
        liveChannel = new LiveChannel();
    }

    @BeforeMethod(alwaysRun = true)
    public void handleTestMethodName(Method method, Object[] testData) {
        s_assert = new SoftAssert();
        try {
            if (testData != null && testData.length >= 1) {
                logger.info("*** Test " + testData[0].toString() + " started *********");
                extentTest = ExtentManager.startTest(testData[0].toString());
                url = (UrlObject) testData[1];
                logger.info("*** URL " + url.getUrl() + " *********");
                extentTest.log(LogStatus.INFO, "URL : " + url.getUrl());
                if (testData[0].toString().contains("OSMF") && getBrowser().contains("chrome")) {
    				driver.get("chrome://components/");
    				chromeValidator = new ChromeFlashUpdateAction(driver);
    				chromeValidator.isFlashPluginUpdated();
                }
            } else {
                logger.info("*** Test " + getClass().getSimpleName() + " started *********");
                extentTest = ExtentManager.startTest(getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field[] fs = this.getClass().getDeclaredFields();
            fs[0].setAccessible(true);
            for (Field property : fs) {
                if (property.getType().getSuperclass()
                        .isAssignableFrom(PlayBackPage.class)) {
                    property.setAccessible(true);
                    property.set(this,
                            pageFactory.getObject(property.getType()));
                    Method[] allMethods = property.get(this).getClass()
                            .getMethods();
                    for (Method function : allMethods) {
                        if (function.getName()
                                .equalsIgnoreCase("setExtentTest"))
                            function.invoke(property.get(this), extentTest);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void getJSFile(String jsFile) throws Exception {
        logger.info("************Getting the JS file*************");
        String[] jsFiles;
        try {
            if (jsFile.contains(",")) {
                jsFiles = jsFile.split(",");
            } else {
                jsFiles = new String[1];
                jsFiles[0] = jsFile;
            }
            if (jsFiles != null && jsFiles.length > 0) {
                jsUrl = new String[jsFiles.length];
                for (int i = 0; i < jsFiles.length; i++) {
                    InetAddress inetAdd = InetAddress.getLocalHost();
                    jsUrl[i] = "http://" + inetAdd.getHostAddress() + ":"
                            + SimpleHttpServer.portNumber + "/js?fileName="
                            + jsFiles[i];
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTestCaseName(Method method, Object[] testData) {
        String testCase = "";
        try {
            if (testData != null && testData.length > 0) {
                for (Object testParameter : testData) {
                    if (testParameter instanceof String) {
                        String testCaseParams = (String) testParameter;
                        testCase = testCaseParams;
                        break;
                    }
                }
                testCase = String.format("%s(%s)", method.getName(), testCase);
            } else
                testCase = method.getName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return testCase;
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteInPlaybackWeb() throws OoyalaException {
        try {
            int portNumber = SimpleHttpServer.getRandomOpenPort();
            SimpleHttpServer.startServer(portNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuiteInPlaybackWeb() throws Exception {
        String updateSheet = System.getProperty("updateSheet");
        try {
            if (updateSheet != null && !updateSheet.isEmpty() && updateSheet.equalsIgnoreCase("true")) {
                TestCaseSheet.update(testDetails, osNameAndOsVersion, browser, "", v4Version);
            }
            SimpleHttpServer.stopServer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeWebdriver() throws Exception {
        try {
            webDriverFacile = getDriver(browser);
            driver = webDriverFacile.get();
            if (webDriverFacile.get() != null)
                logger.info("Driver initialized successfully");
            else {
                logger.error("Driver is not initialized successfully");
                throw new OoyalaException("Driver is not initialized successfully");
            }
            pageFactory = new PlayBackFactory(webDriverFacile.get(), extentTest);
            logger.info("PLATFORM : " + getPlatform());
            if (!getPlatform().equalsIgnoreCase("android")) {
                maximizeMe(webDriverFacile.get());
            }
            if (osNameAndOsVersion == null || osNameAndOsVersion.isEmpty())
                osNameAndOsVersion = getOsNameAndOsVersion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @BeforeTest(alwaysRun = true)
    public void initializeThreads(ITestContext context) {
        try {
            browser = System.getProperty(CommandLineParameters.browser);
            if (browser == null || browser.equals(""))
                browser = "firefox";
            logger.info("browser is " + browser);
            if (browser.equalsIgnoreCase("safari") || browser.toLowerCase().contains("edge") || browser.toLowerCase().contains("internet") || browser.equalsIgnoreCase("ie")
                    || System.getProperty(CommandLineParameters.platform).equalsIgnoreCase("android")) {
                // safari driver does not allow parallel execution of tests
                // ie because the browser hangs - this is a temp soln.
                context.getCurrentXmlTest().setParallel("false");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"testData", "xmlFilePkg", "jsFile"})
    public void setUp(@Optional String xmlFile, @Optional String xmlFilePkg, String jsFile) throws Exception {
        logger.info("************Inside setup*************");
        browser = System.getProperty(CommandLineParameters.browser);
        if (browser == null || browser.equals(""))
            browser = "firefox";
        logger.info("browser is " + browser);
        try {
            v4Version = System.getProperty(CommandLineParameters.v4Version);
            if (v4Version == null || v4Version.equals("")) {
                v4Version = "Candidate/latest";
            }
            parseXmlFileData(xmlFile, xmlFilePkg);
            initializeWebdriver();
            getJSFile(jsFile);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(ITestResult result) throws Exception {
        try {
            boolean driverNotNullFlag = false;
            logger.info("****** Inside @AfterMethod*****");
            logger.info(webDriverFacile.get());
            
    		/*if (browser.equalsIgnoreCase("safari") && webDriverFacile.get().toString().contains("(null)")) {

    			logger.error("Browser closed during the test run. Renitializing the driver as the test failed during the test");
    			extentTest.log(LogStatus.INFO, "Browser closed during the test.");
    			pageFactory.destroyInstance();
    			initializeWebdriver();
    			driverNotNullFlag = false;

    		} else*/
            if (!browser.equalsIgnoreCase("safari") && webDriverFacile.get() != null && (webDriverFacile.get().getSessionId() == null
                    || webDriverFacile.get().getSessionId().toString().isEmpty())) {
                logger.error("Browser closed during the test run. Renitializing the driver as the test failed during the test");
                extentTest.log(LogStatus.INFO, "Browser closed during the test.");
                initializeWebdriver();
                driverNotNullFlag = false;

            } else if (webDriverFacile.get() == null) {
                logger.error("Browser closed during the test run. Renitializing the driver as the test failed during the test");
                extentTest.log(LogStatus.INFO, "Browser closed during the test.");
                driverNotNullFlag = false;
                initializeWebdriver();
            } else {
                driverNotNullFlag = true;
            }

            if (result.getStatus() == ITestResult.FAILURE) {
                if (driverNotNullFlag) {
                    String fileName = takeScreenshot(extentTest.getTest().getName());
                    extentTest.log(LogStatus.INFO,
                            "Snapshot is " + extentTest.addScreenCapture(fileName));
                }

                extentTest.log(LogStatus.FAIL, result.getThrowable().getMessage());
                logger.error("**** Test " + extentTest.getTest().getName()
                        + " failed ******");
            } else if (result.getStatus() == ITestResult.SKIP) {
                extentTest.log(LogStatus.SKIP, extentTest.getTest().getName()
                        + " Test skipped " + result.getThrowable());
                logger.info("**** Test" + extentTest.getTest().getName()
                        + " Skipped ******");
            } else if (result.getStatus() == ITestResult.SUCCESS) {

                logger.info("**** Test" + extentTest.getTest().getName()
                        + " passed ******");
            } else {
                extentTest.log(LogStatus.FAIL, result.getThrowable());
                logger.error("**** Test " + extentTest.getTest().getName()
                        + " failed ******");
            }

            testDetails.put(extentTest.getTest().getName().split(" - ")[1], result);

        } catch (Exception ex) {
            extentTest.log(LogStatus.INFO, ex.getMessage());
            logger.warn(ex.getMessage());
            initializeWebdriver();
        }
        ExtentManager.endTest(extentTest);
        ExtentManager.flush();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        try {
            logger.info("OS Name and Version is : " + getOsNameAndOsVersion());
            if (isBrowserMobProxyEnabled())
                BrowserMobProxyHelper.stopBrowserMobProxyServer();

            logger.info("************Inside tearDown*************");
            if (webDriverFacile.get() != null) {
                webDriverFacile.get().quit();
            } else {
                logger.info("Driver is already null");
            }
            logger.info("Assigning the neopagefactory instance to null");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void parseXmlFileData(String xmlFile, String xmlFilePkg) {
        try {
            if (xmlFile == null || xmlFile.isEmpty()) {
                xmlFile = getClass().getSimpleName();
                String packagename = getClass().getPackage().getName();

                if (packagename.contains(xmlFilePkg)) {
                    xmlFile = xmlFilePkg + "/" + xmlFile + ".xml";
                } else
                    xmlFile = xmlFile + ".xml";

                logger.info("XML test data file:" + xmlFile);
            }
            File file = new File("src/main/resources/testdata/" + xmlFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(Testdata.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            testData = (Testdata) jaxbUnmarshaller.unmarshal(file);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(e.getMessage());
            if (e instanceof NullPointerException) {
                extentTest
                        .log(LogStatus.FAIL,
                                "The test data file should be mentioned as part of the testng parameter "
                                        + "or should be renamed to the name of the class using the test data!");
            }
        }
    }

    public void injectScript() throws Exception {
        try {
            new JSScriptInjection(jsUrl, extentTest, webDriverFacile.get(),url).injectScript();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("inject sript error");
            extentTest.log(LogStatus.ERROR,"inject sript error");
        }
    }

    public String getPlatform() {
        String platformName = null;
        try {
            Capabilities cap = ((RemoteWebDriver) webDriverFacile.get()).getCapabilities();
            platformName = cap.getPlatform().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return platformName;
    }

    public String getBrowser() {
        String browser = null;
        try {
            Capabilities cap = ((RemoteWebDriver) webDriverFacile.get()).getCapabilities();
            browser = cap.getBrowserName().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return browser;
    }

    public String getBrowserVersion() {
        String version = null;
        try {
            Capabilities cap = ((RemoteWebDriver) webDriverFacile.get()).getCapabilities();
            version = cap.getVersion().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return version;
    }

    public String takeScreenshot(String fileName) {
        try {
            logger.info("Taking Screenshot");
            File destDir = new File("images/");
            if (!destDir.exists())
                destDir.mkdir();
            File scrFile = ((TakesScreenshot) webDriverFacile.get()).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File("images/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Not able to take the screenshot");
            }
        } catch (UnreachableBrowserException ex) {
            logger.info(ex.getMessage());
            try {
                initializeWebdriver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "images/" + fileName;
    }

    @DataProvider(name = "testUrls")
    public Object[][] getTestData() {
        String version;
        Map<String, UrlObject> urls;
        Object[][] output = null;
        logger.info("inside data provider method");
        try {
            if (!browser.equalsIgnoreCase("MicrosoftEdge")) {
                version = getBrowserVersion();
            } else {
                version = "";
            }
            urls = new UrlGenerator().parseXmlDataProvider(getClass().getSimpleName(), testData, browser, version);
            String testName = getClass().getSimpleName();
            output = new Object[urls.size()][2];
            Iterator<Map.Entry<String, UrlObject>> entries = urls.entrySet().iterator();
            int i = 0;
            while (entries.hasNext()) {
                Map.Entry<String, UrlObject> entry = entries.next();
                output[i][0] = testName + " - " + entry.getKey();
                output[i][1] = entry.getValue();
                i++;
            }
        }catch (Exception ex){
            logger.error("Error in data provider method"+ex);
        }
        return output;
    }

    protected String removeSkin(String url) {
        return url
                .replace(
                        "http%3A%2F%2Fplayer.ooyala.com%2Fstatic%2Fv4%2Fcandidate%2Flatest%2Fskin-plugin%2Fhtml5-skin.min.js",
                        "");
    }

    protected Object executeScript(String script) {
        return ((JavascriptExecutor) webDriverFacile.get()).executeScript(script);
    }

    protected String getOsNameAndOsVersion() {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(driver.executeScript("return navigator.userAgent;").toString());
        if (!agent.getOperatingSystem().getName().toLowerCase().matches(".*\\d+.*")) {
            return agent.getOperatingSystem().getName() + " " + agent.getOperatingSystem().getVersionNumber().toVersionString();
        } else {
            return agent.getOperatingSystem().getName();
        }
    }

    protected WebDriver getWebdriver(String browser) throws OoyalaException {
        RemoteWebDriver driver = getDriver(browser).get();
        if (driver != null)
            logger.info("Driver initialized successfully");
        else {
            logger.error("Driver is not initialized successfully");
            throw new OoyalaException("Driver is not initialized successfully");
        }

        if (!getPlatform().equalsIgnoreCase("android")) {
            maximizeMe(driver);
        }
        return driver;
    }

    protected String getUserAgent() {
        return (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
    }
    
    protected String  getAutoPlayFlag() throws Exception {

          JSONParser parser = new JSONParser();
          JSONObject obj = (JSONObject) parser.parse(url.getPlayerParameter());
          if(obj.containsKey("autoplay")) {
        	  	return obj.get("autoplay").toString();
          }
          logger.info("autoplay is not mentioned in player params");
          extentTest.log(LogStatus.INFO, "autoplay is not mentioned in player params");
          return "false";
    }
    
    protected String  getAutoplayUpNextDiscoveryVideosFlag() throws Exception {

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(url.getPlayerParameter());
        if(obj.containsKey("autoPlayUpNextVideosOnly")) {
      	  return obj.get("autoPlayUpNextVideosOnly").toString();
        }
        logger.info("upnextdiscovery flag is not mentioned in player params");
    	    extentTest.log(LogStatus.INFO, "upnextdiscovery flag is not mentioned in player params");
        return "false";
    }
    
    protected String  getLoopFlag() throws Exception {

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(url.getPlayerParameter());
        if(obj.containsKey("loop")) {
      	  return obj.get("loop").toString();
        }
        logger.info("loop is not mentioned in player params");
        extentTest.log(LogStatus.INFO, "loop is not mentioned in player params");
        return "false";
    }
    
    protected String[] getVideoPlugins() {
     String videoPlugins = url.getVideoPlugins();
    	if(videoPlugins == null) {
    		logger.error("video plugin is not set");
    		extentTest.log(LogStatus.FATAL, "video plugin is not set");
    		return null;
    	}
    		
    	 return videoPlugins.split(",");
    	
    }
}