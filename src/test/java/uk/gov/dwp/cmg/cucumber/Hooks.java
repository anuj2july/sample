package uk.gov.dwp.cmg.cucumber;

import com.aventstack.extentreports.utils.FileUtil;
import com.cucumber.listener.Reporter;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.cmg.pages.BasePage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import uk.gov.dwp.cmg.utils.ReadData;
import static uk.gov.dwp.cmg.base.BaseAPI.*;
import static uk.gov.dwp.cmg.base.BaseAPI.getRequest;
import static uk.gov.dwp.cmg.responses.CPS_Response.assertResponseStatus;

public class Hooks extends BasePage {

    public static String targetURL;
    public static String targetENV;
    private static Logger logger = LoggerFactory.getLogger(Hooks.class);


    public Hooks() throws IOException {

        String envType = ReadData.readDataFromPropertyFile("targetEnvironment").trim();
        targetENV = System.getProperty("targetEnv", envType);
        logger.info("Target environment is '" + targetENV + "'.");
        targetURL = ReadData.readDataFromPropertyFile(targetENV + ".url").trim();
        logger.info("Target URL is '" + targetURL + "'.");

    }

    @Before
    public void beforeScenario(Scenario scenario) throws IOException {

        String scenarioName = scenario.getName();

        System.out.println("######## Now in Before Scenario ############");
        System.out.println("#################### Geting the HEALTH of the API #########################");

     //   getRequest(ReadData.readDataFromPropertyFile("target.health"));
      //  getHealthCheckResponse(200);

        System.out.println("#################### HEALTH Check DONE #########################");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Started Scenario: " + scenarioName + " %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    @After
    public void afterScenario(Scenario scenario) {

        System.out.println("######## Now in After Scenario ############");

        LocalTime timestamp = null;

//        if (scenario.isFailed()) {
//
//            System.out.println("Taking screenshot if test execution failed");
//            String scrFolder = System.getProperty("user.dir")
//                    + "/"+"FailedSnapshots";
//            new File(scrFolder).mkdir();
//
//            System.out.println(scrFolder);
//            TakesScreenshot scrShot =((TakesScreenshot)driver);
//            File sourceFile = scrShot.getScreenshotAs(OutputType.FILE);
//
//            File destinationPath = new File(scrFolder +"/"+ scenario.getName()+new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(
//                    Calendar.getInstance().getTime()).toString() + ".png");
//
//            try {
//                FileUtils.copyFile(sourceFile,destinationPath);
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        System.out.println("%%%%%%%%%%%%%%%%%%% End of Scenario: " + scenario.getName() + " %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        if (driver != null) {
            driver.quit();
        }

    }

    public void getHealthCheckResponse(int statusCode) {
        System.out.println("££££££££££££££££££££££");
        assertResponseStatus(statusCode);
        System.out.println("111111111111111111111");

    }
}


