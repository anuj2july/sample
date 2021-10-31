package uk.gov.dwp.cmg.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions

        (features = "src/test/resources/Features", // Folder location of 'feature' files (relative to project root)
                glue = {"uk.gov.dwp.cmg.steps", // Folder location containing 'steps' files (relative to project java root)
                        "uk.gov.dwp.cmg.cucumber"
                },
                plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"},


                snippets = SnippetType.CAMELCASE, // Specify camelCase for generated method names.


                monochrome = true
                //tags = {"~@ignore"}) // Which tags to ignore
        )
public class BaseRunner {
}

