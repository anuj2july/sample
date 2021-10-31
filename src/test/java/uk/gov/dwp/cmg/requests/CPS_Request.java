package uk.gov.dwp.cmg.requests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.calendar.BaseCalendar;
import uk.gov.dwp.cmg.base.BaseAPI;
import uk.gov.dwp.cmg.cucumber.Hooks;
import uk.gov.dwp.cmg.responses.CPS_Response;
import uk.gov.dwp.cmg.utils.DataGenerator;
import uk.gov.dwp.cmg.utils.ReadData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.*;

import static uk.gov.dwp.cmg.utils.DataGenerator.formatDateAsUTC;

public class CPS_Request extends BaseAPI {

    private static Logger logger = LoggerFactory.getLogger(BaseAPI.class);
   // private static Integer transactionID = 0;
    public static String TxnID;

    //DB Connection Details
    private static final String DBconnectionURL = ReadData.readDataFromPropertyFile(Hooks.targetENV +".connectionURL");

    private static final String DBuserName = ReadData.readDataFromPropertyFile(Hooks.targetENV +".conUserName");
    private static final String DBpwd = ReadData.readDataFromPropertyFile(Hooks.targetENV +".conPwd");




    public static void cpsPostRequest(int amount, String customerRefNum, String additionalRefNum, String description, String return_url, String orderChannel, String sourceSystem, String account) throws IOException {
        JSONObject request = new JSONObject();

        request.put("amount", amount);
        request.put("customerRefNum", customerRefNum);
        request.put("additionalRefNum", additionalRefNum);
        request.put("description", description);
        request.put("return_url", return_url);
        request.put("orderChannel", orderChannel);
        request.put("sourceSystem", sourceSystem);
        request.put("account", account);

        postRequest(request, ReadData.readDataFromPropertyFile("target.resource"));
    }

    public static void pciPostRequest( int amount, String customerRefNum, String orderChannel, String sourceSystem, String account, String sourceObjectId) throws IOException {
        JSONObject request = new JSONObject();
        request.put("amount", amount);
        request.put("customerRefNum", customerRefNum);
        request.put("orderChannel", orderChannel);
        request.put("sourceSystem", sourceSystem);
        request.put("account", account);
        request.put("sourceObjectId", sourceObjectId);

        postAgentAssistRequest(request, ReadData.readDataFromPropertyFile("target.motoInitition"));
    }

    public static void ivrPostRequest( String customerRefNum) throws IOException {
        JSONObject request = new JSONObject();
        request.put("customerReference", customerRefNum);
        System.out.println("Target IVR Initiion:"+ReadData.readDataFromPropertyFile(Hooks.targetENV + ".target.ivrInitition"));
        postIvrRequest(request, ReadData.readDataFromPropertyFile(Hooks.targetENV + ".target.ivrInitition"));
//        ReadData.readDataFromPropertyFile(targetENV + ".target.ivrInitition").trim();
    }

    public static void pcipalNotificationRequest( int amount, String account, String merchantId, String status, String customerReference, String authorisedDate, String createdDate, String processorId, String cardType, String lastFourDigits, String firstSixDigits, String authorizationId, String source) throws IOException {
        JSONObject request = new JSONObject();
        JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
        String transactionCreatedDate = jsonPathEvaluator.get("timeStamp");

        request.put("amount", amount);
        request.put("account", account);
        request.put("merchantId", merchantId);
        request.put("status", status);
        request.put("customerReference", customerReference);
        request.put("authorisedDate", authorisedDate);
        request.put("createdDate", transactionCreatedDate);
        request.put("processorId", processorId);
        request.put("cardType", cardType);
        request.put("lastFourDigits", lastFourDigits);
        request.put("firstSixDigits", firstSixDigits);
        request.put("authorizationId", authorizationId);
        request.put("source", source);
        request.put("providerId", DataGenerator.processorIdGenerator());

        String transactionID = jsonPathEvaluator.get("transactionID");
        request.put("transactionReference", transactionID);

        postNotificationRequest(request, ReadData.readDataFromPropertyFile("target.pcipalNotification"));
    }

    public static void ivrNotificationRequest( int amount, String account, String merchantId, String status, String customerReference, String processorId, String cardType, String lastFourDigits, String firstSixDigits, String authorizationId, String source) throws IOException {
        JSONObject request = new JSONObject();
        JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
       // String transactionCreatedDate = jsonPathEvaluator.get("timeStamp");

        request.put("amount", amount);
        request.put("account", account);
        request.put("merchantId", merchantId);
        request.put("status", status);
        request.put("customerReference", customerReference);

        request.put("authorisedDate", ( formatDateAsUTC(Date.from(Instant.now()))));

        request.put("createdDate", formatDateAsUTC(Date.from(Instant.now())));
        request.put("processorId", processorId);
        request.put("cardType", cardType);
        request.put("lastFourDigits", lastFourDigits);
        request.put("firstSixDigits", firstSixDigits);
        request.put("authorizationId", authorizationId);
        request.put("source", source);
        request.put("providerId", DataGenerator.processorIdGenerator());

        System.out.println("transactionID:"+jsonPathEvaluator.get("paymentReference"));
        String transactionID = jsonPathEvaluator.get("paymentReference").toString();
        request.put("transactionReference", transactionID);

        postNotificationRequest(request, ReadData.readDataFromPropertyFile("target.pcipalNotification"));
    }

    public static void  authLambdaRequest() throws IOException {
        JSONObject request = new JSONObject();

        request.put("username", "cmg-moto-payment");
        request.put("password", "secret");
        request.put("grant_type", "cmg_client_credentials");
        request.put("scope", "cmg-moto");

        postAuthLambda(request, ReadData.readDataFromPropertyFile("target.authLambda"));
    }

    public static void getTransactionDetails() throws IOException, InterruptedException
    {
        ResponseBody getRes = response.getBody();
        System.out.println("Response Body in GET Method: " + getRes.asString());
        Thread.sleep(2000);
        JsonPath jsonPathEvaluator = response.jsonPath();
        Thread.sleep(2000);
        boolean resSuccess = jsonPathEvaluator.get("success");
        String resDescription = jsonPathEvaluator.get("description");
        String resCode = jsonPathEvaluator.get("code");
        String resStatus = jsonPathEvaluator.get("status");
        boolean resFinished = jsonPathEvaluator.get("finished");
        TxnID = jsonPathEvaluator.get("transactionID");
        System.out.println("Succcess received from Response " + resSuccess);
        System.out.println("Value of description received from Response " + resCode);
        System.out.println("Code received from Response " + resCode);
        System.out.println("Status received from Response " + resStatus);
        System.out.println("Finished received from Response " + resFinished);
        Assert.assertFalse("Finished Message is showing properly", resFinished);
        Assert.assertTrue("Success Message  is showing properly", resSuccess);
        Assert.assertTrue(resStatus.equals(("created")));
    }

    public static void fetchGetMethod(int amount, String account) throws InterruptedException {

        getRequest(ReadData.readDataFromPropertyFile("target.get.reference") + "?account=" + account + "&reference=" + TxnID);
        Thread.sleep(2000);
        ResponseBody getResEndPoints = responseGet.getBody();
        System.out.println("Response Body in GET Method: " + getResEndPoints.asString());
        Thread.sleep(1000);
        JsonPath jsonPathEvaluator = responseGet.jsonPath();
        Thread.sleep(2000);
        TxnID = jsonPathEvaluator.get("transactionID");
        System.out.println("TXNID is %%%%%%" + TxnID);
        int getAmountValue = jsonPathEvaluator.get("amount");
        System.out.println("Amount value is %%%%%%" + getAmountValue);
        boolean finishedStatus = jsonPathEvaluator.get("state.finished");
        System.out.println("finished is %%%%%%" + finishedStatus);
        String requestStatus = jsonPathEvaluator.get("state.status");
        System.out.println("status is %%%%%%" + requestStatus);
        Assert.assertEquals(TxnID, TxnID);
        Assert.assertEquals(amount, getAmountValue);
    }

    public static void getGovPayDetails(String account) throws InterruptedException {

        JsonPath jsonPathEvaluator = response.jsonPath();
        String transactionID = jsonPathEvaluator.get("transactionReference");

        getGovPayRequest(ReadData.readDataFromPropertyFile("target.govPayUrl") + "?account=" + account + "&reference=" + transactionID);
        Thread.sleep(2000);
        ResponseBody getResEndPoints = responseGet.getBody();
        System.out.println("Response Body in GET Method: " + getResEndPoints.asString());
        Thread.sleep(1000);

    }

    public static void cpsPostSiebelRequest(int amount, String customerRefNum, String return_url, String orderChannel, String sourceSystem, String account) throws IOException {
        JSONObject request = new JSONObject();

        request.put("amount", amount);
        request.put("customerRefNum", customerRefNum);
        request.put("return_url", return_url);
        request.put("orderChannel", orderChannel);
        request.put("sourceSystem", sourceSystem);
        request.put("account", account);

        postRequest(request, ReadData.readDataFromPropertyFile("target.resource"));
    //    postSiebelRequest(request, ReadData.readDataFromPropertyFile("target.resource"));
    }

    public static void createConsumerRequest(String name) throws IOException {
        JSONObject request = new JSONObject();
        request.put("name", name);
        request.put("format", "json");
        request.put("auto.offset.reset", "latest");

        postKafkaRequest(request, ReadData.readDataFromPropertyFile(Hooks.targetENV+".target.sitRestProxy"));
    }

    public static void subscribeConsumerRequest(String testReference) throws IOException
    {
        JSONObject request = new JSONObject();
        JSONArray ServiceRequestObject = new JSONArray();
        String JsonFile = ReadData.readDataFromPropertyFile("topicJsonPath");
        InputStream inputStream = new FileInputStream(JsonFile);
        String jsonArrayFileData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONArray jsonArrayData = new JSONArray(jsonArrayFileData);
        boolean testScenarioMatch = false;

        for (int i = 0; i < jsonArrayData.length(); i++)
        {
            JSONObject testScenario = jsonArrayData.getJSONObject(i);

            if (testScenario.get("testReference").equals(testReference))
            {
                testScenarioMatch = true;
                System.out.println("111111" +ServiceRequestObject);
                ServiceRequestObject = testScenario.getJSONArray("topics");
                System.out.println("222222" +request);
                request.put("topics", ServiceRequestObject);
                System.out.println("333333" +request);
                break;
            }
        }
        if (testScenarioMatch)
        {
            logger.info("\n Kafka topic subscribe request is " + request);
            postKafkaRequest(request, ReadData.readDataFromPropertyFile(Hooks.targetENV+".target.sitRestProxySubscribeTopic"));
        }
        else logger.info("Test Reference not present in JSON file");
    }

    public static void verifyIvrSrCreation() throws InterruptedException, Exception {
        try {
            System.out.println("DBconnectionURL:"+DBconnectionURL);
            System.out.println("DBuserName:"+DBuserName);
            System.out.println("DBpwd:"+DBpwd);

            System.out.println("####### Now establishing a connection with database to check for SR creation ######");

            // Connection Creation for Selenium-DB connectivity
            Connection con = DriverManager.getConnection(DBconnectionURL, DBuserName, DBpwd);

            //Statement is a interface and it is used to sending a SQL query to the database
            Statement stmt = con.createStatement();

            // Fetch the Transaction Reference Number of current transaction
            JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
            String transactionID = jsonPathEvaluator.get("paymentReference").toString();

            // Prepare SQL statement that need to be execute
            String s =  "select * from s_srv_req where ROW_ID = (select  ROW_ID from s_srv_req_X where X_mop_reference_no='" + transactionID + "')";

            System.out.println("###### SQL Query for checking SR creation in siebel is : "+ s );
            Thread.sleep(2000);
            //JDBC ResultSet contains records again execute query
            ResultSet rs = stmt.executeQuery(s);
            Thread.sleep(2000);
            rs.next();
            Thread.sleep(2000);
            System.out.print("Row id :" + rs.getString("ROW_ID") + " ");
            System.out.println(" After Result Set:");
            Thread.sleep(5000);
//            System.out.print("SR_NUM:" + rs.getString("SR_NUM") + " ");
//            System.out.print("SR_SUBTYPE_CD:" + rs.getString("SR_SUBTYPE_CD") + " ");
//            System.out.print("SR_SUB_AREA :" + rs.getString("SR_SUB_AREA") + " ");
//            System.out.print("SR_AREA :" + rs.getString("SR_AREA") + " ");



//            System.out.println(" After Thread RS Value is:"+rs.next());
//            while (rs.next())
//            {
                String srType = rs.getString("SR_SUBTYPE_CD");
                Thread.sleep(1000);
                String srSubArea = rs.getString("SR_SUB_AREA");
                Thread.sleep(1000);
                String srArea = rs.getString("SR_AREA");
                Thread.sleep(1000);
                String srStatId = rs.getString("SR_STAT_ID");
                Thread.sleep(1000);
                String srSubStatId = rs.getString("SR_SUB_STAT_ID");
                Thread.sleep(1000);
                 String InsProduct = rs.getString("INS_PRODUCT");
            Thread.sleep(1000);

                System.out.println("srType for our IVR payment SR is " + srType);
                System.out.println("srSubArea for our IVR payment SR is " + srSubArea);
                System.out.println("Area for our IVR payment SR is " + srArea);
                System.out.println("Status for our IVR payment SR is " + srStatId);
                System.out.println("Sub status for our IVR payment SR is " + srSubStatId);
            System.out.println("Sub status for our INS Product  " + InsProduct);

                Assert.assertEquals("Sr Type is ", "IVR", srType);
                Thread.sleep(1000);
                Assert.assertEquals("Sr Sub area is ",  "Client credit/debit card" , srSubArea);
                Thread.sleep(1000);
                Assert.assertEquals("Sr Area is ",  "One-off payment" , srArea);
                Thread.sleep(3000);
                Assert.assertEquals( "Closed", srStatId);
                Thread.sleep(1000);
                Assert.assertEquals( "Complete", srSubStatId);
                Thread.sleep(1000);

//            }

            con.close();
            System.out.println("Connection closed for SQL Statement");
        }
        catch (Exception e)
        {
            System.out.println("Exception: Not able to verify SR creation.");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }
    }

    public static void verifyAgentAssistSrCreation() throws InterruptedException, Exception {
        try {
            System.out.println("DBconnectionURL:" + DBconnectionURL);
            System.out.println("DBuserName:" + DBuserName);
            System.out.println("DBpwd:" + DBpwd);
//            Thread.sleep(5000);
            System.out.println("####### Now establishing a connection with database to check for SR creation ######");

            // Connection Creation for Selenium-DB connectivity
            Connection con = DriverManager.getConnection(DBconnectionURL, DBuserName, DBpwd);
            Thread.sleep(5000);
            //Statement is a interface and it is used to sending a SQL query to the database
            Statement stmt = con.createStatement();

            // Fetch the Transaction Reference Number of current transaction
            JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
            String transactionID = jsonPathEvaluator.get("transactionID");

            // Prepare SQL statement that need to be execute
            String s = "select * from s_srv_req where ROW_ID = (select  ROW_ID from s_srv_req_X where X_mop_reference_no='" + transactionID + "')";

            System.out.println("###### SQL Query for checking SR creation in siebel is : " + s);

            //JDBC ResultSet contains records again execute query
            ResultSet rs = stmt.executeQuery(s);
            Thread.sleep(2000);
            while (rs.next()) {
            Thread.sleep(2000);
            System.out.print("Row id :" + rs.getString("ROW_ID") + " ");
            System.out.println(" After Result Set:");
            Thread.sleep(5000);
//            while (rs.next()) {
                System.out.println(" After Thread:");
                String srType = rs.getString("SR_SUBTYPE_CD");
                Thread.sleep(1000);

                String srSubArea = rs.getString("SR_SUB_AREA");
                Thread.sleep(1000);

                Thread.sleep(1000);
                String srStatId = rs.getString("SR_STAT_ID");

                Thread.sleep(1000);
                String srSubStatId = rs.getString("SR_SUB_STAT_ID");

                System.out.println("srType for our Agent assist payment SR is " + srType);
                System.out.println("srSubArea for our agent assist payment SR is " + srSubArea);

                Assert.assertEquals("Sr Type is matched ", "Self Service", srType);
                Assert.assertEquals("Sr Sub area is matched ", "Client credit/debit card", srSubArea);
                Assert.assertEquals("Closed", srStatId);
                Assert.assertEquals("Complete", srSubStatId);

                Thread.sleep(5000);
            }

                con.close();
                System.out.println("Connection closed for SQL Statement");

        }

        catch (Exception e)
        {
            System.out.println("Exception: Not able to verify SR creation.");
            e.printStackTrace();
            Assert.fail("Exception " + e);

        }
    }


    public static void verifyEmployerSrCreation() throws InterruptedException, Exception {
        try {

            System.out.println("####### Now establishing a connection with database to check for SR creation ######");

            // Connection Creation for Selenium-DB connectivity
            Connection con = DriverManager.getConnection(DBconnectionURL, DBuserName, DBpwd);

            //Statement is a interface and it is used to sending a SQL query to the database
            Statement stmt = con.createStatement();

            // Fetch the Transaction Reference Number of current transaction
            JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
            String transactionID = jsonPathEvaluator.get("paymentReference");

            // Prepare SQL statement that need to be execute
            String s =  "select * from s_srv_req where ROW_ID = (select  ROW_ID from s_srv_req_X where X_mop_reference_no='" + transactionID + "')";

            System.out.println("###### SQL Query for checking SR creation in siebel is : "+ s );

            //JDBC ResultSet contains records again execute query
            ResultSet rs = stmt.executeQuery(s);

            Thread.sleep(3000);
            while (rs.next())
            {
                String srType = rs.getString("SR_SUBTYPE_CD");
                Thread.sleep(1000);
                String srSubArea = rs.getString("SR_SUB_AREA");
                Thread.sleep(1000);
                System.out.println("srType for our IVR payment SR is " + srType);
                System.out.println("srSubArea for our IVR payment SR is " + srSubArea);

                Assert.assertEquals("Sr Type is ", "IVR", srType);
                Assert.assertEquals("Sr Type is ",  "Employer credit/debit card" , srSubArea);

                Thread.sleep(1000);

            }

            con.close();
            System.out.println("Connection closed for SQL Statement");
        }
        catch (Exception e)
        {
            System.out.println("Exception: Not able to verify SR creation.");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }
    }

    public static void verifyAgentAssistEmployerSrCreation() throws InterruptedException, Exception {
        try {

            System.out.println("####### Now establishing a connection with database to check for SR creation ######");

            // Connection Creation for Selenium-DB connectivity
            Connection con = DriverManager.getConnection(DBconnectionURL, DBuserName, DBpwd);

            //Statement is a interface and it is used to sending a SQL query to the database
            Statement stmt = con.createStatement();

            // Fetch the Transaction Reference Number of current transaction
            JsonPath jsonPathEvaluator = agentAssistResponse.jsonPath();
            String transactionID = jsonPathEvaluator.get("transactionID");

            // Prepare SQL statement that need to be execute
            String s =  "select * from s_srv_req where ROW_ID = (select  ROW_ID from s_srv_req_X where X_mop_reference_no='" + transactionID + "')";

            System.out.println("###### SQL Query for checking SR creation in siebel is : "+ s );

            //JDBC ResultSet contains records again execute query
            ResultSet rs = stmt.executeQuery(s);

            Thread.sleep(3000);
            while (rs.next())
            {
                String srType = rs.getString("SR_SUBTYPE_CD");
                Thread.sleep(1000);
                String srSubArea = rs.getString("SR_SUB_AREA");
                Thread.sleep(1000);
                System.out.println("srType for our IVR payment SR is " + srType);
                System.out.println("srSubArea for our IVR payment SR is " + srSubArea);

                Assert.assertEquals("Sr Type is ", "Self Service", srType);
                Assert.assertEquals("Sr Type is ",  "Employer credit/debit card" , srSubArea);

                Thread.sleep(1000);

            }

            con.close();
            System.out.println("Connection closed for SQL Statement");
        }
        catch (Exception e)
        {
            System.out.println("Exception: Not able to verify SR creation.");
            e.printStackTrace();
            Assert.fail("Exception " + e);
        }
    }

    }
