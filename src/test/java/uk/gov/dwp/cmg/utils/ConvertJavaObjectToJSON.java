package uk.gov.dwp.cmg.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ConvertJavaObjectToJSON {

    public static ObjectMapper objectMapper;
    private static Logger logger = LoggerFactory.getLogger(ConvertJavaObjectToJSON.class);


    static {
        objectMapper = new ObjectMapper();
    }

    public static String convertToJson(Object object) {

        String JsonResult = "";

        try {

            JsonResult = objectMapper.writeValueAsString(object);

        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        return JsonResult;
    }

}

