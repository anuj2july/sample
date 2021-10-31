package uk.gov.dwp.cmg.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ConnectionConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.cmg.cucumber.Hooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static uk.gov.dwp.cmg.base.BaseAPI.ivrResponse;

public class KafkaAPI {

    public static RequestSpecification request;
    public static Response response;
    private static Logger logger = LoggerFactory.getLogger(KafkaAPI.class);
    private static String groupName = null;
    public static String kafkaURL = null;

    ConnectionConfig connectionConfig = new ConnectionConfig();
    static KafkaAPI kp = new KafkaAPI();


    public void createConsumer() throws IOException {
        kafkaURL = ReadData.readDataFromPropertyFile(Hooks.targetENV+".kafkaURL");

        System.out.println("Kafka URL::"+kafkaURL);
        //create a request
        request = given();
        groupName = DataGenerator.idGenerator();

        //create consumer body
        HashMap<String, String> autoOffet = new HashMap<>();

        autoOffet.put("auto.offset.reset", "earliest");
        autoOffet.put("name", "testId");
        autoOffet.put("format", "json");
        autoOffet.put("auto.commit.enable", "true");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(autoOffet);

        request.contentType("application/vnd.kafka.v2+json").body(jsonString);
        response = request.when().post(kafkaURL + "/consumers/" + groupName + "");
        System.out.println("Created Consumer:"+response);
        connectionConfig.closeIdleConnectionsAfterEachResponse();

        logger.info("******** Consumer is now created ********");

    }

    public void subscribeConsumer(String topicName) throws IOException {

        SubscribeConsumer obj = new SubscribeConsumer(topicName);
        request = given();
        request.contentType("application/vnd.kafka.v2+json").body(ConvertJavaObjectToJSON.convertToJson(obj));
        response = request.when().post(kafkaURL + "/consumers/" + groupName + "/instances/"
                + "testId" + "/subscription");
        //response.then().statusCode(204);

        logger.info("******** Consumer is now subscribed to "+topicName+ " topic ********");


    }

    public int getMaxInitialOffset(String topic) throws Exception {

        kp.createConsumer();
        kp.subscribeConsumer(topic);
        request = given();
        request.accept("application/vnd.kafka.json.v2+json");

        int j = 0;
        do {
            System.out.println("Kafka Request URL:"+kafkaURL + "/consumers/" + groupName + "/instances/" + "testId/" + "records");
            response = request.when().get(kafkaURL + "/consumers/" + groupName + "/instances/" + "testId/" + "records");

            System.out.println("Kafka response:"+response.asString());

        } while (j++ < 5 && response.then().extract().jsonPath().getList("$").size() <= 0);

        JSONArray jsonArray = new JSONArray(response.asString());
        int maxOffset = 0;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ArrayList<String> offsets = new ArrayList<String>();
            offsets.add(jsonObject.get("offset").toString());
            maxOffset = Integer.parseInt(Collections.max(offsets));
        }
        return maxOffset;
    }

    public void getRecords(int maxoffset, String topic) throws Exception {
        JsonPath jsonPathEvaluator = ivrResponse.jsonPath();
        String transactionID = jsonPathEvaluator.get("paymentReference");

        kp.createConsumer();
        kp.subscribeConsumer(topic);
        request = given();
        request.accept("application/vnd.kafka.json.v2+json");

        logger.info("******** Fetching records from the Consumer ********");

        int j = 0;

        do {
            response = request.when().get(kafkaURL + "/consumers/" + groupName + "/instances/" + "testId/" + "records");
        }
        while (j++ < 5 && response.then().extract().jsonPath().getList("$").size() <= 0);

        JSONArray jsonArray = new JSONArray(response.asString());

        int currentMaxOffset;

        for
        (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ArrayList<String> offsets = new ArrayList<String>( );
            offsets.add(jsonObject.get("offset").toString());
            currentMaxOffset = Integer.parseInt(Collections.max(offsets));
            JSONObject obj = jsonObject.getJSONObject("value");

            if (currentMaxOffset >= maxoffset) {
                System.out.println("Kafka Value:"+obj.toString());

                JsonPath jsonPath = new JsonPath(obj.toString());
                Thread.sleep(10000);

                switch (topic) {
                    case "PersonsNotificationDelivered":

                        logger.info("Verifying the data for the " + topic+ " topic" );
//                        Assert.assertTrue(jsonPath.getString("metadata.transaction_child_id").startsWith("75"));

                        Assert.assertEquals(transactionID, jsonPath.getString("metadata.transaction_child_id"));
                        Assert.assertEquals("CARD PAYMENT CONFIRMATION", jsonPath.getString("business_interest.source"));

                        logger.info("The transaction_child_id is " + jsonPath.getString("metadata.transaction_child_id"));
                        logger.info("The transaction date is " + jsonPath.getString("metadata.transaction_date"));
                        logger.info("The notification is sent via : " + jsonPath.getString("business_interest.notification_channel") + " channel");

                        logger.info("The data is verified for "+ topic + " topic" );
                        break;

                    case "PersonsNotificationUnDelivered":

                        logger.info("Verifying the data for the " + topic+ " topic" );
                        Assert.assertTrue(jsonPath.getString("metadata.transaction_child_id").startsWith("75"));
                        Assert.assertEquals(transactionID, jsonPath.getString("metadata.transaction_child_id"));

                        Assert.assertEquals("CARD PAYMENT CONFIRMATION", jsonPath.getString("business_interest.source"));
                        Assert.assertEquals("Permanent failure from Gov-Notify", jsonPath.getString("business_interest.failure_reason"));


                        logger.info("The permanent failure reason is " + jsonPath.getString("business_interest.failure_reason"));
                        logger.info("The transaction_child_id is " + jsonPath.getString("metadata.transaction_child_id"));
                        logger.info("The transaction date is " + jsonPath.getString("metadata.transaction_date"));
                        logger.info("The notification permanently failed to be sent via : " + jsonPath.getString("business_interest.notification_channel") + " channel");

                        logger.info("The data is verified for "+ topic + " topic" );

                        break;

                    case "PersonsNotificationDeliveryFailed":

                        logger.info("Verifying the data for the " + topic+ " topic" );
                        Assert.assertTrue(jsonPath.getString("metadata.transaction_child_id").startsWith("75"));
                        Assert.assertEquals(transactionID, jsonPath.getString("metadata.transaction_child_id"));

                        Assert.assertEquals("CARD PAYMENT CONFIRMATION", jsonPath.getString("business_interest.source"));
                        Assert.assertEquals("Temporary/Technical failure from Gov-Notify", jsonPath.getString("business_interest.failure_reason"));


                        logger.info("The temporary failure reason is " + jsonPath.getString("business_interest.failure_reason"));
                        logger.info("The transaction_child_id is " + jsonPath.getString("metadata.transaction_child_id"));
                        logger.info("The transaction date is " + jsonPath.getString("metadata.transaction_date"));
                        logger.info("The notification temporarily failed to be sent via : " + jsonPath.getString("business_interest.notification_channel") + " channel");

                        logger.info("The data is verified for "+ topic + " topic" );

                        break;

                }
            }

        }
    }
}



