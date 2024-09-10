package com.lloyds.data.automation.runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features= "../DataAutomation/features",
        glue = "com.lloyds.data.automation.steps",
        monochrome=true,
        plugin = {"pretty","junit:target/JUnitReports/report.xml",
                "html:target/HtmlReports/report.html"}
)
public class CukesRunner {}