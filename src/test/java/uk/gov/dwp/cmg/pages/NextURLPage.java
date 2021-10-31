package uk.gov.dwp.cmg.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import uk.gov.dwp.cmg.testdata.Keywords;

import java.util.concurrent.TimeUnit;

public class NextURLPage extends BasePage {

    private static String cardNum = "#card-no";
    private static String expMonth = "expiry-month";
    private static String expYear = "expiry-year";
    private static String nameOnCard = "cardholder-name";
    private static String cvv = "cvc";
    private static String continueButton = "submit-card-details";
    public static String notAcceptedCard = "card-no-lbl";
    private static String scinEnter = "//input[@id='1_CMEC_SCIN_Number']";


    public static void setEnter_cardNumber(String CardNumber) {
        driver.findElement(By.cssSelector(cardNum)).sendKeys(CardNumber);
    }

    public static void setEnter_month(String ExpiryMonth) {
        driver.findElement(By.id(expMonth)).sendKeys(ExpiryMonth);
    }

    public static void setEnter_expiredYear(String ExpiryYear) {
        driver.findElement(By.id(expYear)).sendKeys(ExpiryYear);
    }

    public static void setEnter_nameOnCard(String NameOnCard) {
        driver.findElement(By.id(nameOnCard)).sendKeys(NameOnCard);
    }

    public static void setEnter_SecurityCode(String SecurityCode) {
        driver.findElement(By.id(cvv)).sendKeys(SecurityCode);
    }

    public static void setContinueButton() {
        driver.findElement(By.id(continueButton)).submit();
    }


    public static void enterGovPayCardDetails() throws InterruptedException {

        waitUntilElementIsVisible(cardNum,10);

        driver.findElement(By.cssSelector(cardNum)).sendKeys(Keywords.accountNum);
        driver.findElement(By.id(expMonth)).sendKeys(Keywords.expMonth);
        driver.findElement(By.id(expYear)).sendKeys(Keywords.expYear);
        driver.findElement(By.id(nameOnCard)).sendKeys(Keywords.nameOnCard);
        driver.findElement(By.id(cvv)).sendKeys(Keywords.cvv);
        driver.findElement(By.id(continueButton)).submit();

        hardWait(2000);
        System.out.println("@@@@@@@@@@@");
    }

    public static void enterGovPayCardDetailsForDeclinePayment() throws InterruptedException {

        waitUntilElementIsVisible(cardNum,10);
        driver.findElement(By.cssSelector(cardNum)).sendKeys(Keywords.declineAccountNum);
        driver.findElement(By.id(expMonth)).sendKeys(Keywords.expMonth);
        driver.findElement(By.id(expYear)).sendKeys(Keywords.expYear);
        driver.findElement(By.id(nameOnCard)).sendKeys(Keywords.nameOnCard);
        driver.findElement(By.id(cvv)).sendKeys(Keywords.cvv);

        driver.findElement(By.id(continueButton)).submit();
        hardWait(2000);
        System.out.println("@@@@@@@@@@@");
    }


    public static void enterGovPayCardDetailsNotSupported() throws InterruptedException {

        waitUntilElementIsVisible(cardNum,10);
        driver.findElement(By.cssSelector(cardNum)).sendKeys(Keywords.cardNotSupported);
        driver.findElement(By.id(expMonth)).sendKeys(Keys.TAB);

        hardWait(2000);
        System.out.println("@@@@@@@@@@@");
        String HeaderText = driver.findElement(By.id(notAcceptedCard)).getText();
        System.out.println("@@@@@@@@@@@");

        System.out.println("Value of Card header message is " + HeaderText);
        Assert.assertTrue(HeaderText.equalsIgnoreCase("Maestro is not supported"));

    }

    public static void setEnter_scinNumbner(String scinNumber) {
        driver.findElement(By.cssSelector(scinEnter)).sendKeys(scinNumber);
    }
}
