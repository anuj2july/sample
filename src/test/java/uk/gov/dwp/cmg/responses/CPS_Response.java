package uk.gov.dwp.cmg.responses;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.cmg.base.BaseAPI;
import uk.gov.dwp.cmg.pages.ConfirmationPage;
import uk.gov.dwp.cmg.pages.NextURLPage;
import uk.gov.dwp.cmg.pages.ReturnURLPage;
import java.io.IOException;
import static uk.gov.dwp.cmg.pages.BasePage.launchBrowser;

public class CPS_Response extends BaseAPI {

    public WebDriver driver;
    private static Logger logger = LoggerFactory.getLogger(BaseAPI.class);


    public void assertInitialCpsStatusResponse(String response_body_verification) {
        ResponseBody body = response.getBody();
        System.out.println("Response Body is: " + body.asString());

        // By using the ResponseBody.asString() method, we can convert the  body
        // into the string representation.

        JsonPath jsonPathEvaluator = response.jsonPath();
        String resStatus = jsonPathEvaluator.get("status");
        System.out.println("Value of status received from Response " + resStatus);
        System.out.println("City received from Response " + jsonPathEvaluator.get("transactionID"));
        Assert.assertTrue(resStatus.equalsIgnoreCase("created"));
        String URL = jsonPathEvaluator.get("next_url");
        System.out.println("Value of next_url received from Response " + URL);
    }

    public static void assertInitialFinishedResponse(Boolean response_body_verification) {

        JsonPath jsonPathEvaluator = response.jsonPath();
        boolean resFinished = jsonPathEvaluator.get("finished");
        System.out.println("Value of finished received from Response " + resFinished);
        Assert.assertFalse(resFinished);
    }

    public static void launchNextUrl() throws InterruptedException, IOException {

        ResponseBody postRes = response.getBody();
        System.out.println("Response Body is: " + postRes.asString());
        // By using the ResponseBody.asString() method, we can convert the  body
        // into the string representation.
        Thread.sleep(1000);
        JsonPath jsonPathEvaluator = response.jsonPath();
         String URL = jsonPathEvaluator.get("next_url");
        System.out.println("Value of next_url received from Response " + URL);

        launchBrowser(URL);
    }

    public static void assertResponseStatus(Integer statusCode) {
        Assert.assertEquals("Expected Status Code", statusCode, BaseAPI.statusCode);
        logger.info("Expected value: " + statusCode + ", Actual value: " + BaseAPI.statusCode);
    }

    public static void assertJsonAttribute(String attribute, String expectedValue) {
        JsonPath jsonPathEvaluator = response.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertTrue(attributeValue.equalsIgnoreCase(expectedValue));
    }

    public static void assertAgentAssistJsonAttribute(String attribute, String expectedValue) {
        JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertTrue(attributeValue.equalsIgnoreCase(expectedValue));
    }

    public static void assertIvrJsonAttribute(String attribute, String expectedValue) {
        JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertTrue(attributeValue.equalsIgnoreCase(expectedValue));
    }


    public static void isPresent(String attribute) {
        JsonPath jsonPathEvaluator = response.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        Assert.assertNotNull(attributeValue);
    }

    public static void isAgentAssistAttributePresent(String attribute) {
        JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        Assert.assertNotNull(attributeValue);
    }

    public static void isIvrAttributePresent(String attribute) {
        JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
        String attributeValue = jsonPathEvaluator.get(attribute);
        Assert.assertNotNull(attributeValue);
    }

    public static void assertJsonBolleanAttribute(String attribute, boolean expectedValue) {
        JsonPath jsonPathEvaluator = response.jsonPath();
        boolean attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertSame(attributeValue, expectedValue);
    }

    public static void assertAgentAssistJsonBolleanAttribute(String attribute, boolean expectedValue) {
        JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
        boolean attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertSame(attributeValue, expectedValue);
    }

    public static void assertIvrBolleanAttribute(String attribute, boolean expectedValue) {
        JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
        boolean attributeValue = jsonPathEvaluator.get(attribute);
        logger.info("Expected value: " + expectedValue + ", Actual value: " + attributeValue);
        Assert.assertSame(attributeValue, expectedValue);
    }



    public static void enterCardDetails() throws InterruptedException {
        NextURLPage.enterGovPayCardDetails();
    }

    public static void confirmPayment() throws InterruptedException {
        ConfirmationPage.submitPayment();
    }

    public static void validateReturnURL() throws InterruptedException {
        ReturnURLPage.confirmPayment();
    }

    public static void enterCardDetailsForDeclinePayment() throws InterruptedException {
        NextURLPage.enterGovPayCardDetailsForDeclinePayment();
    }

    public static void PaymentWithDeclineAccount() throws InterruptedException {
        ConfirmationPage.submitWithDecineCard();
    }

    public static void PaymentStatus() throws InterruptedException {
        ConfirmationPage.submitWithDecineCard();
    }

    public static void enterAllCardDetails(String CardNumber, String ExpiryMonth, String ExpiryYear, String NameOnCard, String SecurityCode) {
        NextURLPage.setEnter_cardNumber(CardNumber);
        NextURLPage.setEnter_month(ExpiryMonth);
        NextURLPage.setEnter_expiredYear(ExpiryYear);
        NextURLPage.setEnter_nameOnCard(NameOnCard);
        NextURLPage.setEnter_SecurityCode(SecurityCode);
        NextURLPage.setContinueButton();
    }
    public static void launchSiebel() throws Exception {
        ReturnURLPage.launchSiebelBrowser();
    }

    public static void loginSiebel() throws Exception {
        ReturnURLPage.loginSiebelHome();
    }

    public static void contactSiebel() throws Exception {
        ReturnURLPage.contactSection();
    }

    public static void clickScinLastName(String scinNumber) throws InterruptedException {
        NextURLPage.setEnter_scinNumbner(scinNumber);
    }

    public static void queryTodaysRecordAndSorting() throws Exception {
        ReturnURLPage.queryAndSortRecords();
    }

    public static void srVerification() throws Exception {
        ReturnURLPage.srDetailsVerification();
    }
}

