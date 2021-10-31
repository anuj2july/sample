package uk.gov.dwp.cmg.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import uk.gov.dwp.cmg.utils.ReadData;

import java.util.concurrent.TimeUnit;


public class ConfirmationPage extends BasePage
{

    public static String browser = ReadData.readDataFromPropertyFile("browser");

    private static String confirmPageHeader = ".govuk-heading-xl";
    private static String confirmPayment = "confirm";
    private static String declineCardMessage = ".govuk-heading-l";
    private static String maestroNonValidCard = "//*[@id=\"card-no-lbl\"]/span";
    private static String continueButton =".govuk-button";
    private static String retryPaymentButton ="#return-url";

    public static void submitPayment() throws InterruptedException {

        String HeaderText = driver.findElement(By.cssSelector(confirmPageHeader)).getText();
        System.out.println("@@@@@@@@@@@");
        hardWait(2000);
        System.out.println("Value of header received from Response " + HeaderText);
        Assert.assertTrue(HeaderText.equalsIgnoreCase("Confirm your payment"));
        driver.findElement(By.id(confirmPayment)).click();
       // hardWait(2000);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


    }

    public static void submitWithDecineCard() throws InterruptedException {

        String HeaderText = driver.findElement(By.cssSelector(declineCardMessage)).getText();
        System.out.println("@@@@@@@@@@@");
        System.out.println("Value of header received from Response " + HeaderText);
        Assert.assertTrue(HeaderText.equalsIgnoreCase("Your payment has been declined"));
        driver.findElement(By.cssSelector(continueButton)).click();
        hardWait(1000);

    }

    public static void generalErrorPage() throws InterruptedException {

        String HeaderText = driver.findElement(By.cssSelector(declineCardMessage)).getText();
        System.out.println("Value of header received from Response " + HeaderText);
        Assert.assertTrue(HeaderText.equalsIgnoreCase("We’re experiencing technical problems"));
        driver.findElement(By.cssSelector(retryPaymentButton)).click();
        hardWait(1000);

    }

    public static void maestroCardMessage()
    {
        if (browser.equalsIgnoreCase("chrome")) {
            String HeaderText = driver.findElement(By.xpath(maestroNonValidCard)).getText();
            System.out.println("Maestro not supported text " + HeaderText);
            Assert.assertEquals("This card type is not accepted", HeaderText);
        }
        else if (browser.equalsIgnoreCase("htmlunit")) {
            String HeaderText = driver.findElement(By.xpath(maestroNonValidCard)).getText();
            System.out.println("Maestro not supported text " + HeaderText);
            Assert.assertEquals("Card number", HeaderText);
        }

    }
}
