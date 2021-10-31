package uk.gov.dwp.cmg.cucumber;

import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(Cucumber.class)
@CucumberOptions

        (
//                tags = {"@PortalOnlineGovPay,@PortalOnlineGovPayCardFailure,@IVRpayment,@AgentAssistPayment"}) // Specify which feature files or scenarios to run or ignore
                tags = {"@IVRpayment, @AgentAssistPayment"})
//@AgentAssistPayment,
public class TestRunner extends BaseRunner {
    @AfterClass
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File("extent-config.xml"));
        Reporter.setSystemInfo("Customer Name", System.getProperty("user.name"));
        Reporter.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
        Reporter.setSystemInfo("Machine", System.getProperty("os.name"));
        Reporter.setSystemInfo("Selenium", "3.7.0");
        Reporter.setSystemInfo("Maven", "3.5.2");
        Reporter.setSystemInfo("Java Version", "1.8.0_151");
    }

}

