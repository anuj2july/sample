package uk.gov.dwp.cmg.pages;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.dwp.cmg.utils.ReadData;

import java.net.MalformedURLException;
import java.net.URL;

public class BasePage {

    public static WebDriver driver;
    public static final String USERNAME = "jordanwood2";
    public static final String AUTOMATE_KEY = "zsBv3ENXqqzoaWh8amv3";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static String path = System.getProperty("user.dir");
    public static String os = System.getProperty("os.name");

    public static String chromeMacDriverPath = path + ReadData.readDataFromPropertyFile("chromeDriver_mac").trim();
    public static String chromeWinDriverPath = path + ReadData.readDataFromPropertyFile("chromeDriver_win").trim();
    public static String ieDriverPath = path + ReadData.readDataFromPropertyFile("ieDriver").trim();

    public static String browser = ReadData.readDataFromPropertyFile("browser");

    public static void launchBrowser(String url) throws IOException {


        System.out.println("############# Executing test cases on " + browser + " browser ###############");

        if (os.contains("Linux"))
           {
            if (browser.equalsIgnoreCase("htmlunit")) {
                htmlUnitDriver();
            }
           }

        else
            if (os.contains("Mac"))
            {
             if (browser.equalsIgnoreCase("chrome")) {
                chromeDriverMac();
             }
             if (browser.equalsIgnoreCase("htmlunit")) {
                htmlUnitDriver();
             }

             if (browser.equalsIgnoreCase("browserStack"))
             {
                browserStackBrowser();
             }
            }
            else
                if (os.contains("windows"))
                {
                    if (browser.equalsIgnoreCase("htmlunit")) {
                        htmlUnitDriver();
                    }
                    if (browser.equalsIgnoreCase("chrome")) {
                        chromeDriverWin();
                    }
                    if (browser.equalsIgnoreCase("ie")) {
                        ieDriver();
                    }
                }

        driver.get(url);
        driver.manage().window().maximize();
    }


    public static void chromeDriverMac()
    {
        System.out.println("&&&&&& launching chrome browser &&&&&&");
        System.setProperty("webdriver.chrome.driver", chromeMacDriverPath);
        driver = new ChromeDriver();
    }

    public static void chromeDriverWin()
    {
        System.out.println("&&&&&& launching chrome browser &&&&&&");
        System.setProperty("webdriver.chrome.driver", chromeWinDriverPath);
        driver = new ChromeDriver();
    }

    public static void ieDriver()
    {
        System.out.println("&&&&&& launching IE browser &&&&&&");
        System.setProperty("webdriver.ie.driver",ieDriverPath );
        driver = new InternetExplorerDriver();
    }

    public static void htmlUnitDriver()
    {
        System.out.println("&&&&&&& launching headless browser &&&&&&&&");
        driver = new HtmlUnitDriver(BrowserVersion.CHROME);
    }

    public static void browserStackBrowser() throws MalformedURLException
    {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", "OS X");
        caps.setCapability("os_version", "Mojave");
        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "77.0");
        driver = new RemoteWebDriver(new URL(URL), caps);
    }

    public static void waitUntilElementIsVisible( String element,int sec )
    {
        WebDriverWait wait=new WebDriverWait(driver,sec);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element)));
    }

    public static void hardWait(int miliSec) throws InterruptedException
    {
       Thread.sleep(miliSec);
    }
}