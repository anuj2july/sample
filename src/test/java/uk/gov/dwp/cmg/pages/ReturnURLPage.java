package uk.gov.dwp.cmg.pages;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import uk.gov.dwp.cmg.utils.ReadData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class ReturnURLPage extends BasePage {

    private static String confirmationMessage = ".govuk-panel__title";
    private static String yourReference = ".govuk-panel__body";
    private static String referenceNum = ".govuk-panel__body>strong";
    private static String paymentSummaryHeading = ".govuk-heading-m";
    private static String totalAmountHeader = ".govuk-table__header";
    private static String totalAmount = ".govuk-table__cell";

    // xPath for Siebel Application Page ***Start***
    private static String uname = "//input[@name='SWEUserName' and @type='text']";
    private static String pwd = "//input[@name='SWEPassword' and @type='password']";
    private static String submit = "//*[@class='siebui-login-btn']";
    private static String contact = "//span[@title='Contacts']";
    private static String querySearch = "//button[@type='button' and @title='Contacts:Query']";
    private static String scin = "//*[@id='1_s_1_l_CMEC_SCIN_Number']";
    private static String scinEnter = "//input[@id='1_CMEC_SCIN_Number']";
    private static String queryGo = "//button[@name=\"s_1_1_6_0\" and @data-display='Go']";
    private static String lname = "//*[@name='Last Name']";
    private static String queryServiceRequest = "//button[@type='button' and @title='Service Request:Query']";
    private static String srOpenDate = "//*[@id='1_s_3_l_SR_Open_Date']";
    private static String srOpenDateEnter = "//*[@id='1_SR_Open_Date']";
    private static String srOpenDateQuery = "//*[@id='s_3_1_8_0_Ctrl']";
    //   private static String sortOpenDate="//div[@class='ui-state-default ui-th-column ui-th-ltr ui-sortable-handle']";
    private static String sortOpenDate = "//div[@class='ui-jqgrid-sortable' and @id='jqgh_s_3_l_SR_Open_Date']";
    private static String sorting1 = "#s_S_A3_headerMenu>li:nth-child(1)";
    //private static String sorting1="//*[@class='ui-state-default ui-th-column ui-th-ltr ui-sortable-handle' and @id='s_3_l_SR_Open_Date']/div/span/span[2]";
    private static String sorting2 = "#s_S_A3_headerMenu>li:nth-child(1)>a";
    private static String recordList = "//*[@id='s_3_l']";
    private static String srTitle = "//*[@id='s_3_l']/tbody/tr[@role='row']/td[2][@title]";
    private static String srSCIN = "//*[@aria-label='SCIN Number']";
    private static String srTxnID = "//*[@aria-label='Reference Num']";
    private static String service = "//*[@aria-label='Service Selected']";
    private static String RefNumLabel = "//*[@id='CMEC_Reference_Num_Label']";
    private static String nextBtn = "//*[@class='siebui-icon-recnav_next_on']";
    private static String lName = "//*[@id='1_s_2_l_Last_Name']";
    private static String lastName ="//*[@aria-labelledby='LastName_Label']";

    private static final String siebelEnvURL = ReadData.readDataFromPropertyFile("siebelEnvURL");
    private static final String userName = ReadData.readDataFromPropertyFile("userName");
    private static final String siebelPwd = ReadData.readDataFromPropertyFile("siebelPwd");
    public static String  tempRefNum="";
    // xPath for Siebel Application Page ***End***



    public static void confirmPayment() throws InterruptedException
    {
        try {
            waitUntilElementIsVisible(confirmationMessage, 10);

            String ConfirmationMessageText = driver.findElement(By.cssSelector(confirmationMessage)).getText();
            System.out.println("@@@@@@@@@@@");
            System.out.println("Value of header received from Response " + ConfirmationMessageText);

            Assert.assertTrue(ConfirmationMessageText.equalsIgnoreCase("Your payment was successful"));

            //   hardWait(2000);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            String paymentSummaryHeadingText = driver.findElement(By.cssSelector(paymentSummaryHeading)).getText();

            Assert.assertTrue(paymentSummaryHeadingText.equalsIgnoreCase("Payment Summary"));

            String yourReferenceText = driver.findElement(By.cssSelector(yourReference)).getText();

            //   hardWait(2000);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Assert.assertTrue(yourReferenceText.contains("Your reference number"));
            tempRefNum = driver.findElement(By.cssSelector(referenceNum)).getText();
            System.out.println("Reference Number is " + tempRefNum);

        }
        catch(Exception e) {
            System.out.println("Exception: Reference Number not capture due to exception");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }
    }

    public static void launchSiebelBrowser() throws InterruptedException, Exception {
    try
    {
        driver.get(ReadData.readDataFromPropertyFile("siebelEnvURL"));
        hardWait(4000);
        String actualTitle = driver.getTitle();
        System.out.println("Page Title is " + actualTitle);
        String expectedTitle = "Siebel Public Sector Open UI6";
        hardWait(3000);

        if (expectedTitle.contentEquals(actualTitle)) {
            Assert.assertTrue(expectedTitle.equalsIgnoreCase(actualTitle));
            System.out.println("We are on Siebel Public Sector Opne UI6");
        } else {
            System.out.println("Please verify again. Its not Siebel Open UI page");
            throw new Exception();
        }
    }
     catch(Exception e) {
        System.out.println("Exception: Its not Siebel Open UI page");
        e.printStackTrace();
        Assert.fail("Exception " + e);
    }
    }

    public static void loginSiebelHome() throws InterruptedException, Exception {
    try {
        driver.findElement(By.xpath(uname)).sendKeys(ReadData.readDataFromPropertyFile("userName"));
        System.out.println("User Name Entered");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        driver.findElement(By.xpath(pwd)).sendKeys(ReadData.readDataFromPropertyFile("siebelPwd"));
        System.out.println("Password Entered");

        driver.findElement(By.xpath(submit)).click();
        System.out.println("Submit Button Clicked");
        hardWait(4000);
        String actualTitle = driver.getTitle();
        String expectedTitle = "Siebel Public Sector Home";

        if (expectedTitle.equalsIgnoreCase(actualTitle)) {
            Assert.assertTrue(expectedTitle.equalsIgnoreCase(actualTitle));
            System.out.println("We are on Siebel Public Sector Home Page");
        } else {
            System.out.println("Please verify again. Its not Siebel Public Sector Home Page");
            throw new Exception();
        }
    }
    catch(Exception e) {
        System.out.println("Exception: Its not Siebel Public Sector Home Page");
        e.printStackTrace();
        Assert.fail("Exception " + e);
    }

    }

    public static void contactSection() throws InterruptedException, Exception {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            WebElement contactBtn = driver.findElement(By.xpath(contact));
            jse.executeScript("arguments[0].click();", contactBtn);
            System.out.println("Contact Section Clicked");
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.xpath(querySearch)).click();
            hardWait(3000);
            String actualTitle = driver.getTitle();
            System.out.println("Title name is " + actualTitle);
            String expectedTitle = "All Contacts: 1034Altamirano";
            hardWait(3000);

            if (expectedTitle.equalsIgnoreCase(actualTitle)) {
                Assert.assertTrue(expectedTitle.equalsIgnoreCase(actualTitle));
                System.out.println("We are on Contacts Section");
            } else {
                System.out.println("Please verify again. Its not Contacts page");
                throw new Exception();
            }
        }
        catch(Exception e) {
            System.out.println("Exception: Its not Contacts page");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }
    }


    public static void clickScinLastName(String scinNumber) throws InterruptedException, Exception {
        try {
            driver.findElement(By.xpath(scin)).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.xpath(scinEnter)).sendKeys(scinNumber);
            driver.findElement(By.xpath(queryGo)).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            String LastName = driver.findElement(By.xpath(lname)).getText();
            driver.findElement(By.xpath(lname)).click();

            String actualTitle = driver.getTitle();
            System.out.println("Title name is " + actualTitle);
            String expectedTitle = ("All Contacts: " + LastName);
            //   hardWait(3000);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


            if (expectedTitle.equalsIgnoreCase(actualTitle)) {
                Assert.assertTrue(expectedTitle.equalsIgnoreCase(actualTitle));
                System.out.println("We are on SCIN Details Screen");
            } else {
                System.out.println("Please verify again. Its not SCIN Details page");
                throw new Exception();
            }
        }
         catch(Exception e) {
                System.out.println("Exception: Its not SCIN Details page");
                e.printStackTrace();
                Assert.fail("Exception " + e);
            }

    }


    public static void queryAndSortRecords() throws InterruptedException, Exception {
        try {
            driver.findElement(By.xpath(queryServiceRequest)).click();
            hardWait(2000);
            driver.findElement(By.xpath(srOpenDate)).click();
            hardWait(2000);
            driver.findElement(By.xpath(srOpenDateEnter)).sendKeys("today()");
            hardWait(2000);
            driver.findElement(By.xpath(srOpenDateQuery)).click();
            hardWait(2000);
            driver.findElement(By.xpath(sortOpenDate)).click();
            hardWait(2000);
            driver.findElement(By.cssSelector(sorting1)).click();
            hardWait(2000);

            driver.findElement(By.xpath(sortOpenDate)).click();
            hardWait(2000);

            driver.findElement(By.cssSelector(sorting2)).click();
            hardWait(2000);
            List<WebElement> srTable = driver.findElements(By.xpath(srTitle));
            System.out.println("@@@@@@ Size of List is @@@@@@ " + srTable.size());



      //      String LastName = driver.findElement(By.xpath(lastName)).getText();
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            String LastName = (String) jse.executeScript("return arguments[0].value;", driver.findElement(By.xpath(lastName)));
            String actualTitle = driver.getTitle();
            System.out.println("Title name is :-" + actualTitle);
            String expectedTitle = ("Contact Summary: " + LastName);
            hardWait(2000);


            if (expectedTitle.equalsIgnoreCase(actualTitle) && srTable.size()>0) {
                Assert.assertTrue(expectedTitle.equalsIgnoreCase(actualTitle));
                System.out.println("We are on Contact Summary Screen");
            }
            else
            {
                System.out.println("Please verify again. Its not Contact Summary page");
                throw new Exception();
            }
        }
        catch(Exception e) {
            System.out.println("Exception: Its not Contact Summary page");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }

    }


    public static void srDetailsVerification() throws InterruptedException, Exception {
    try {
        List<WebElement> srTable = driver.findElements(By.xpath(srTitle));
        System.out.println("@@@@@@ Size of List is @@@@@@ " + srTable.size());
        if (srTable.size() > 0) {
            Iterator<WebElement> itr = srTable.iterator();
            while (itr.hasNext()) {
                System.out.println("Value of SR in List is " + itr.next().getText());
            }
            WebDriverWait wait = new WebDriverWait(driver, 20);
            for (int i = 1; i <= srTable.size(); i++) {
                System.out.println("@@@@@@ Loop Part @@@@@@ " + i);
                driver.findElement(By.xpath(sortOpenDate)).click();
                Thread.sleep(2000);

                driver.findElement(By.cssSelector(sorting1)).click();
                Thread.sleep(2000);

                driver.findElement(By.xpath(sortOpenDate)).click();
                Thread.sleep(2000);

                driver.findElement(By.cssSelector(sorting2)).click();
                Thread.sleep(2000);


                srTable = driver.findElements(By.xpath(srTitle));
                wait.until(ExpectedConditions.visibilityOf(srTable.get(i - 1)));
                srTable.get(i - 1).click();
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                System.out.print(i + " element clicked\t--");
                System.out.println("Round" + i + "Clear");
                //   Thread.sleep(2000);
                Thread.sleep(2000);

                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                String TxnID = driver.findElement(By.xpath(RefNumLabel)).getText();
                System.out.println("RefNum LABEL is " + TxnID);
                String serviceReq = driver.findElement(By.xpath(service)).getText();
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                String RefNum = (String) jse.executeScript("return arguments[0].value;", driver.findElement(By.xpath(srTxnID)));

                System.out.println("txnref is " + RefNum);
                Thread.sleep(3000);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


                if (tempRefNum.equalsIgnoreCase(RefNum)) {
                    System.out.println("ABCDEFGHIJKLMNOP");
                    System.out.println("SR Validated Successfully");
                    break;

                } else {
                    driver.navigate().back();
                    Thread.sleep(3000);
                }
            }
        }
        else
        {
            System.out.println("No SR Present");
            throw new Exception();
        }
    }
     catch (Exception e)
     {
         System.out.println("SR is not available in Siebel SR List");
         Assert.fail("Exception " + e);
        e.printStackTrace();
    }
    }
}

