package uk.gov.dwp.cmg.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadData {

    static File file;
    static Properties prop;
    static ReadData a;

    public static String readDataFromPropertyFile(String key) {
        try {
            file = new File("App.properties");
            FileInputStream fileInput;
            prop = new Properties();
            fileInput = new FileInputStream(file);
            prop.load(fileInput);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}

