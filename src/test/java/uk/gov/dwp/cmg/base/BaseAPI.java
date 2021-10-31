package uk.gov.dwp.cmg.base;

import cucumber.api.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.cmg.cucumber.Hooks;
import uk.gov.dwp.cmg.responses.CPS_Response;
import uk.gov.dwp.cmg.utils.JwtHelper;
import uk.gov.dwp.cmg.utils.ParseDynamicArray;
import uk.gov.dwp.cmg.utils.ReadData;

import java.util.HashMap;

public class BaseAPI {

    private static Logger logger = LoggerFactory.getLogger(BaseAPI.class);
    public static Integer statusCode = 0;
    private static String resource1 = null;
    public static Response response;
    public static Response agentAssistResponse;
    public static Response ivrResponse;
    public static Response authResponse;
    public static Response responseGet;

    public static JSONObject request;

    //Get ApiToken from LAS by calling JWTHelper

    public static String jwtApiToken = JwtHelper.generateTestJwt();
    public static String notificationApiToken = JwtHelper.generateMototJwt();
    public static String ivrApiToken = JwtHelper.generateIvrtJwt();

    public static String govPayApiToken = ReadData.readDataFromPropertyFile("DT_GOVPAY_API_KEY_GB_MOTO");


    public static void postRequest(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;

        System.out.println("POST Request Token is " +jwtApiToken);


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", jwtApiToken);

        try {
            response = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post(Hooks.targetURL + resource1)
                    .then()
                    .extract()
                    .response();
            response.prettyPrint();
            statusCode = response.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void postAgentAssistRequest(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;

        System.out.println("POST Request Token is " +jwtApiToken);


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", jwtApiToken);

        try {
            agentAssistResponse = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post(Hooks.targetURL + resource1)
                    .then()
                    .extract()
                    .response();
            agentAssistResponse.prettyPrint();
            statusCode = agentAssistResponse.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void postIvrRequest(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;

        System.out.println("IVR payment initiation api token is " +ivrApiToken);


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", ivrApiToken);

        try {
            RestAssured.useRelaxedHTTPSValidation();

            ivrResponse = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post( resource1)
                    .then()
                    .extract()
                    .response();
            ivrResponse.prettyPrint();
            System.out.println(ivrResponse);
            statusCode = ivrResponse.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }


    public static void postNotificationRequest(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;
        JsonPath jsonPathEvaluator = authResponse.jsonPath();

        String accessToken    = jsonPathEvaluator.get("access_token");

        System.out.println("Auth lambda Request Token for pcipal notification is : " +accessToken);
        System.out.println("Notification request coming from pcipal is  : " +request);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Accept", "application/json");
        headerMap.put("Authorization", accessToken);

        try {
            response = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post(Hooks.targetURL + resource1)
                    .then()
                    .extract()
                    .response();
            response.prettyPrint();
            statusCode = response.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }


    public static void postAuthLambda(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        try {
            authResponse = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post(Hooks.targetURL + resource1)
                    .then()
                    .extract()
                    .response();
            System.out.println("token from auth lambda is:  " );
            authResponse.prettyPrint();
            statusCode = authResponse.getStatusCode();
            System.out.println("Status code for auth lambda is:  "+  statusCode.toString()  );
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void getRequest(String resource) {
        resource1 = resource;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", jwtApiToken);
       try
        {
            RestAssured.useRelaxedHTTPSValidation();
            responseGet = RestAssured.given()
                    .headers(headerMap)
                    .get(Hooks.targetURL + resource1)
                    .then()
                    .extract()
                    .response();
            responseGet.prettyPrint();
            statusCode = responseGet.getStatusCode();
        }
       catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void consumeDataFromTopic(String resource) {
        resource1 = resource;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/vnd.kafka.json.v2+json");
        try
        {
            RestAssured.useRelaxedHTTPSValidation();
            responseGet = RestAssured.given()
                    .headers(headerMap)
                    .get(resource1)
                    .then()
                    .extract()
                    .response();
            responseGet.prettyPrint();

            System.out.println("Records in initiated topic are : " + responseGet);
            statusCode = responseGet.getStatusCode();

            JSONArray kafkaObjArray = new JSONArray(responseGet.prettyPrint());

            System.out.println("KAFKA Full Array is " + kafkaObjArray);
            System.out.println("KAFKA Full Array size " + kafkaObjArray.length());

            for (int i = 0; i < kafkaObjArray.length(); i++) {

                JsonPath jsonPathEvaluator = response.jsonPath();

                Long transactionID = jsonPathEvaluator.get("paymentReference");
                String customerReference = jsonPathEvaluator.get("customerReference");

                System.out.println("The ivr response TransactionReference is : "+ transactionID);
                JSONObject kafkaArray = kafkaObjArray.getJSONObject(i);
                System.out.println("The Kafka Array is : "+ kafkaArray);
                ParseDynamicArray.getKey(kafkaArray,"transactionNumber");
                System.out.println("ZZZZZZZZZZZ " +ParseDynamicArray.js);

                if ((ParseDynamicArray.js).equals(transactionID))
                {
                    System.out.println("Applying assertions for the transaction we initiated");
                    CPS_Response.isPresent("createdDate");
                    CPS_Response.isPresent("account");
                    CPS_Response.isPresent("status");
                    CPS_Response.assertJsonAttribute("customerRefNumber", customerReference);
                }
            }
        }
        catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void getGovPayRequest(String govPayEndPoint) {
        resource1 = govPayEndPoint;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer "+govPayApiToken);
        try
        {
            RestAssured.useRelaxedHTTPSValidation();
            responseGet = RestAssured.given()
                    .headers(headerMap)
                    .get(resource1)
                    .then()
                    .extract()
                    .response();
            responseGet.prettyPrint();
            statusCode = responseGet.getStatusCode();
        }
        catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void postKafkaRequest(JSONObject requestBody, String resource) {
        resource1 = resource;
        request = requestBody;

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/vnd.kafka.v2+json");
        headerMap.put("Accept", "application/vnd.kafka.v2+json");

        try {
            response = RestAssured.given()
                    .headers(headerMap)
                    .body(request.toString())
                    .post( resource1)
                    .then()
                    .extract()
                    .response();
            response.prettyPrint();
            statusCode = response.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

    public static void deleteConsumer(String resource) {
        resource1 = resource;


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/vnd.kafka.v2+json");

        try {
            response = RestAssured.given()
                    .headers(headerMap)
                    .delete( resource1)
                    .then()
                    .extract()
                    .response();
            response.prettyPrint();
            statusCode = response.getStatusCode();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
        }
    }

}

