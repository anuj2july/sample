package uk.gov.dwp.cmg.steps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import uk.gov.dwp.cmg.cucumber.Hooks;
import uk.gov.dwp.cmg.pages.BasePage;
import uk.gov.dwp.cmg.pages.ConfirmationPage;
import uk.gov.dwp.cmg.pages.NextURLPage;
import uk.gov.dwp.cmg.pages.ReturnURLPage;
import uk.gov.dwp.cmg.requests.CPS_Request;
import uk.gov.dwp.cmg.responses.CPS_Response;
import uk.gov.dwp.cmg.utils.DataGenerator;
import uk.gov.dwp.cmg.utils.KafkaAPI;
import uk.gov.dwp.cmg.utils.ReadData;

import java.time.Instant;
import java.util.Date;

import static uk.gov.dwp.cmg.responses.CPS_Response.*;

public class CardPaymentServiceSteps extends BasePage
{
    @When("^user initiates payment with portal parameters \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void user_initiates_payment_with_parameters(int amount, String customerRefNum, String additionalRefNum, String description, String return_url, String orderChannel, String sourceSystem, String account) throws Throwable
    {
        CPS_Request.cpsPostRequest(amount, customerRefNum, additionalRefNum, description, return_url, orderChannel, sourceSystem, account);
    }

    @When("^agent initiates payment with a reference number \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void agent_initiates_payment_with_a_reference_number(int amount, String customerRefNum, String orderChannel, String sourceSystem, String account, String sourceObjectId) throws Throwable
    {
        CPS_Request.pciPostRequest( amount, customerRefNum, orderChannel, sourceSystem, account, sourceObjectId);
    }

    @When("^user initiates payment with his reference number \"([^\"]*)\"$")
    public void user_initiates_payment_with_his_reference_number(String customerRefNum) throws Throwable
    {
        CPS_Request.ivrPostRequest( customerRefNum );
    }

    @Then("^verify that response status is (\\d+)$")
    public void verifyThatResponseStatusIs(int statusCode)
    {
        assertResponseStatus(statusCode);
        System.out.println("####### Response status code verified #######");
    }

    @Then("^verify that ivr payment is not initiated and payment is not allowed \"([^\"]*)\", \"([^\"]*)\"$")
    public void verify_that_ivr_payment_is_not_initiated_and_payment_is_not_allowed(String dateOfBirth, boolean paymentAllowed) throws Throwable {

        CPS_Response.assertIvrBolleanAttribute("success", true);
        CPS_Response.assertIvrBolleanAttribute("paymentAllowed", paymentAllowed);
        CPS_Response.assertIvrJsonAttribute("code", "CPSIVR200");
        CPS_Response.assertIvrJsonAttribute("customerDetails.dateOfBirth", dateOfBirth);
    }


    @And("^verify the initial status for created payment$")
    public void verifyTheInitialStatusForCreatedPayment()
    {
        CPS_Response.assertJsonAttribute("status", "created");
        CPS_Response.assertJsonBolleanAttribute("success", true);
        CPS_Response.assertJsonAttribute("description", "Your request completed successfully!");
        CPS_Response.assertJsonAttribute("code", "CPS201");
        assertInitialFinishedResponse(Boolean.getBoolean("false"));
    }

    @Then("^verify that payment is initiated and transaction reference is generated$")
    public void verify_that_payment_is_initiated_and_transaction_reference_is_generated() throws Throwable
    {
        CPS_Response.assertAgentAssistJsonAttribute("status", "pending");
        CPS_Response.assertAgentAssistJsonBolleanAttribute("success", true);
        CPS_Response.assertAgentAssistJsonAttribute("description", "Your request completed successfully!");
        CPS_Response.assertAgentAssistJsonAttribute("code", "CPS201");
        CPS_Response.assertAgentAssistJsonBolleanAttribute("finished", false);
        CPS_Response.isAgentAssistAttributePresent("transactionID");
        CPS_Response.isAgentAssistAttributePresent("loginUrl");
        CPS_Response.isAgentAssistAttributePresent("accessToken");
        CPS_Response.isAgentAssistAttributePresent("refreshToken");
        CPS_Response.isAgentAssistAttributePresent("linkId");
        CPS_Response.isAgentAssistAttributePresent("sessionId");
        CPS_Response.isAgentAssistAttributePresent("timeStamp");
    }

    @Then("^verify that ivr payment is initiated and transaction reference is generated \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\"$")
    public void verify_that_ivr_payment_is_initiated_and_transaction_reference_is_generated(String customerRefNum, String account, String dateOfBirth, boolean paymentAllowed) throws Throwable
    {
        CPS_Response.assertIvrBolleanAttribute("success", true);
        CPS_Response.assertIvrJsonAttribute("code", "CPSIVR201");

        if (customerRefNum.startsWith("821000101997"))
        {
            CPS_Response.assertIvrJsonAttribute("customerReference", "121008527161");
        }
        else
            {
                CPS_Response.assertIvrJsonAttribute("customerReference", customerRefNum);
            }
        CPS_Response.assertIvrJsonAttribute("account", account);
        CPS_Response.assertIvrJsonAttribute("source", "IVR");

        CPS_Response.assertIvrJsonAttribute("customerDetails.dateOfBirth", dateOfBirth);
        CPS_Response.assertIvrBolleanAttribute("paymentAllowed", paymentAllowed);
//        CPS_Response.isIvrAttributePresent("customerDetails.dateOfBirth");
//        CPS_Response.isIvrAttributePresent("createdDate");
//        CPS_Response.isIvrAttributePresent("paymentReference");

        System.out.println("####### All the attributes in IVR payment initiation response are now asserted #######");


    }

    @When("^verify that ivr payment is initiated and transaction reference is generated \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\"$")
    public void verify_that_ivr_payment_is_initiated_and_transaction_reference_is_generated(String customerRefNum, String account, boolean paymentAllowed) throws Throwable
    {
        CPS_Response.assertIvrBolleanAttribute("success", true);
        CPS_Response.assertIvrJsonAttribute("code", "CPSIVR201");
        CPS_Response.assertIvrJsonAttribute("customerReference", customerRefNum);
        CPS_Response.assertIvrJsonAttribute("account", account);
        CPS_Response.assertIvrBolleanAttribute("paymentAllowed", paymentAllowed);
        CPS_Response.isIvrAttributePresent("createdDate");
        CPS_Response.isIvrAttributePresent("customerDetails.payeeRef");
    }

    @Given("^the consumer is created and subscribed to a kafka topic\"([^\"]*)\", \"([^\"]*)\"$")
    public void the_consumer_is_created_and_subscribed_to_a_kafka_topic(String name, String testReference) throws Throwable
    {
        CPS_Request.createConsumerRequest( name );
        CPS_Request.subscribeConsumerRequest( testReference );
    }

    @Then("^verify that same has been recorded in initiated topic$")
    public void verify_that_same_has_been_recorded_in_initiated_topic() throws Throwable
    {
        CPS_Request.consumeDataFromTopic(ReadData.readDataFromPropertyFile(Hooks.targetENV+".target.sitRestProxyConsumeRecords"));
        System.out.println("######## Records fetched from initiated topic ########");

        CPS_Request.deleteConsumer(ReadData.readDataFromPropertyFile(Hooks.targetENV+".target.deleteConsumer"));
        System.out.println("\"######## Consumer is now DELETED ########\"");
    }

    @Then("^pcipal notifies and notification lambda puts the notification recieved in completed topic \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void pcipal_notifies_and_notification_lambda_puts_the_notification_recieved_in_completed_topic(int amount, String account, String merchantId, String status, String customerReference, String authorisedDate, String createdDate, String processorId, String cardType, String lastFourDigits, String firstSixDigits, String authorizationId, String source) throws Throwable
    {
        CPS_Request.pcipalNotificationRequest(amount, account,merchantId, status, customerReference, authorisedDate, createdDate, processorId, cardType, lastFourDigits, firstSixDigits, authorizationId, source);
    }

    @Then("^then pcipal calls auth lambda and gets the token$")
    public void then_pcipal_calls_auth_lambda_and_gets_the_token() throws Throwable {
         CPS_Request.authLambdaRequest();
    }


    @Then("^pcipal notifies and notification lambda puts the payment status in completed topic\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void pcipal_notifies_and_notification_lambda_puts_the_payment_status_in_completed_topic(int amount, String account, String merchantId, String status, String customerReference, String processorId, String cardType, String lastFourDigits, String firstSixDigits, String authorizationId, String source) throws Throwable
    {
        CPS_Request.ivrNotificationRequest(amount, account,merchantId, status, customerReference, processorId, cardType, lastFourDigits, firstSixDigits, authorizationId, source);
    }

    @Then("^verify that client SR has been created in siebel$")
    public void verify_that_client_SR_has_been_created_in_siebel() throws Throwable {
        CPS_Request.verifyIvrSrCreation();
    }

    @Then("^verify that payment SR for employer has been created in siebel$")
    public void verify_that_payment_SR_for_employer_has_been_created_in_siebel() throws Throwable {
        CPS_Request.verifyEmployerSrCreation();
    }

    @Then("^verify that employer self service SR has been created in siebel$")
    public void verify_that_employer_self_service_SR_has_been_created_in_siebel() throws Throwable {
        CPS_Request.verifyAgentAssistEmployerSrCreation();
    }

    @Then("^verify that client has been notified for the payment done \"([^\"]*)\"$")
    public void verify_that_client_has_been_notified_for_the_payment_done( String topic) throws Throwable {
     new KafkaAPI().getRecords(new KafkaAPI().getMaxInitialOffset(topic), topic);

    }


    @Given("^verify that the transaction is notified to gov pay \"([^\"]*)\"$")
    public void verify_that_the_transaction_is_notified_to_gov_pay(String account) throws Throwable
    {
        CPS_Request.getGovPayDetails( account);
    }

    @Then("^verify that client self service SR has been created in siebel$")
    public void verify_that_client_self_service_SR_has_been_created_in_siebel() throws Throwable
    {
        CPS_Request.verifyAgentAssistSrCreation();
    }

    @And("^CPS response verification for initial finished value before payment submit$")
    public void cpsResponseFinishedVerify()
    {
        assertInitialFinishedResponse(Boolean.getBoolean("false"));
    }

    @Then("^launch the NextURL for filling card details$")
    public void launchNextURL() throws Throwable
    {
        launchNextUrl();
    }

    @Then("^enter the card details and click continue$")
    public void enterTheCardDetailsAndClickContinue() throws Throwable
    {
        enterCardDetails();
    }

    @Then("^verify the confirmation page and click confirm payment$")
    public void verifyAndConfirmPayment() throws Throwable
    {
        confirmPayment();
    }

    @And("^validate that you land on return url$")
    public void returnURLValidation() throws Throwable
    {
        validateReturnURL();
    }

    @And("^verify payment status after POST request$")
    public void getPaymentStatus() throws Throwable
    {
        CPS_Request.getTransactionDetails();
    }

    @And("^verify all attributes received in GET request \"([^\"]*)\", \"([^\"]*)\"$")
    public void getPaymentEndPoints(int amount, String account) throws Throwable
    {
        CPS_Request.fetchGetMethod(amount, account);
    }

    @Then("^enter the card details and click continue for payment decline$")
    public void enterTheCardDetailsAndClickContinueForPaymentDecline() throws Throwable
    {
        enterCardDetailsForDeclinePayment();
    }

    @Then("^verify the confirmation page for decline payment$")
    public void declinePaymentAccount() throws Throwable
    {
        PaymentWithDeclineAccount();
    }

    @Then("^enter not supported card while payment$")
    public void notSupportedCard() throws Throwable
    {
        NextURLPage.enterGovPayCardDetailsNotSupported();
    }

    @And("^user enters card number \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\",$")
    public void userEntersCardNumber(String CardNumber, String ExpiryMonth, String ExpiryYear, String NameOnCard, String SecurityCode) throws Throwable
    {
        enterAllCardDetails(CardNumber, ExpiryMonth, ExpiryYear, NameOnCard, SecurityCode);
    }

    @And("^user is presented technical problem page$")
    public void userIsPresentedTechnicalProblemPage() throws InterruptedException
    {
        ConfirmationPage.generalErrorPage();
    }

    @And("^user is presented with card not accepted page$")
    public void userIsPresentedWithCardNotAcceptedPage()
    {
        Assert.assertTrue(driver.getCurrentUrl().startsWith("https://www.payments.service.gov.uk/card_details/"));
        ConfirmationPage.maestroCardMessage();
    }

    @And("^Open siebel environment$")
    public void openSiebelEnvironment() throws Throwable
    {
        launchSiebel();
    }

    @Then("^enter login credentials and click submit$")
    public void loginSiebelAndSubmit() throws Throwable
    {
        loginSiebel();
    }

    @And("^Go to the contacts section on homepage$")
    public void goToContactSectionOnHomepage() throws Throwable
    {
        contactSiebel();
    }

    @And("^Move through query search button and enter SCIN \"([^\"]*)\" & click last name$")
    public void goToScinLastName(String additionalRefNum) throws Throwable
    {
        ReturnURLPage.clickScinLastName(additionalRefNum);
    }

    @And("^Query for today records and sort with latest records$")
    public void queryTodaysRecordAndSort() throws Throwable
    {
        queryTodaysRecordAndSorting();
    }

    @Then("^verify the SR details for completed record$")
    public void srDetailsScreen() throws Throwable
    {
        srVerification();
    }

}