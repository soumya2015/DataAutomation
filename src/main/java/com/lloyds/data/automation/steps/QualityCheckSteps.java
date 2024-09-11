package com.lloyds.data.automation.steps;

import com.lloyds.data.automation.pojo.LogFile;
import com.lloyds.data.automation.pojo.SampleData;
import com.lloyds.data.automation.service.QualityCheckService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lloyds.data.automation.service.QualityCheckService.isValidDateFormat;

/**
 * This class holds step definition.
 */
@Slf4j
@NoArgsConstructor
public class QualityCheckSteps {

    private QualityCheckService qualityCheckService;

    private List<LogFile> logFileList;

    List<SampleData> sampleDataList;

    private LogFile logFile;

    @Given("the dataset is loaded from {string}")
    public void the_dataset_is_loaded_from(String string) throws FileNotFoundException {
        log.info("Loading data file {} ", string);
        qualityCheckService = new QualityCheckService();
        sampleDataList = qualityCheckService.loadSampleData(string);
        log.info("Sample" + sampleDataList);
        LogFile logFile = LogFile.builder().creationDate(LocalDateTime.now()).log("File " + string + " has loaded successfully.").build();
        logFileList = new ArrayList<>();
        logFileList.add(logFile);
        Assert.notNull(sampleDataList, "Sample Data file not Available");
    }

    @When("the dataset has the following required columns:")
    public void the_dataset_has_the_following_required_columns(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asList().forEach(System.out::println);
        List<String> columnList = dataTable.asList();
    }

    @Then("there are no missing values in the file for specified columns")
    public void there_are_no_missing_values_in_the_file_for_specified_columns() {
        int counter = 0;
        for (SampleData sampleData : sampleDataList) {
            counter++;
            log.info("Id()> " + sampleData.getId()
                    + " Name()> " + sampleData.getName()
                    + " Amount() >" + sampleData.getAmount()
                    + " Date() >" + sampleData.getDate());

            logMissingData(counter, sampleData);
        }
    }


    @Then("Date column follows the format `YYYY-MM-DD`")
    public void date_column_follows_the_format_yyyy_mm_dd() {
        boolean dateFormat = true;
        for (SampleData sampleData : sampleDataList) {
            // Check the date format
            if (isValidDateFormat(sampleData.getDate())) {
                log.info("Date format is correct");
                logFile = LogFile.builder().creationDate(LocalDateTime.now()).
                        log("Data Consistency check passed. Date Format is correct").build();
                logFileList.add(logFile);
            } else {
                log.info("Incorrect Date Format");

                logFile = LogFile.builder().creationDate(LocalDateTime.now()).
                        log("Data Consistency check failed. Date Format is incorrect").build();
                logFileList.add(logFile);
            }

        }
    }

    @Then("there are no duplicate entries based on a unique identifier column")
    public void there_are_no_duplicate_entries_based_on_a_unique_identifier_column() {
        List<String> duplicateList = qualityCheckService.findDuplicateRecords(sampleDataList);
        logDuplicate(duplicateList);
    }

    @Then("the results are logged in {string}")
    public void the_results_are_logged_in(String string) throws IOException {
        qualityCheckService.writeToFile(logFileList);
    }

    private void logMissingData(int counter, SampleData sampleData) {
        if (sampleData.getId() <= 0) {
            log.info("Id is missing for row {}", counter);
            addToFailedLog("Id", counter);
        } else {
            addToLog("ID", counter);
        }
        if (StringUtils.isEmpty(sampleData.getName())) {
            log.info("Name is missing for row {}", counter);
            addToFailedLog("Name", counter);
        } else {
            addToLog("Name", counter);
        }
        if (StringUtils.isEmpty(sampleData.getDate())) {
            log.info("Date is missing for row {}", counter);
            addToFailedLog("Date", counter);
        } else {
            addToLog("Date", counter);
        }
        if (sampleData.getAmount() == null) {
            log.info("Amount is missing for row {}", counter);
            addToFailedLog("Amount", counter);
        } else {
            addToLog("Amount", counter);
        }
    }

    private void addToLog(String s, int row) {
        LogFile logFile = LogFile.builder().creationDate(LocalDateTime.now()).
                log("Data Completeness check passed. " + s + " column  has no missing value for row " + row).build();
        logFileList.add(logFile);
    }

    private void addToFailedLog(String s, int row) {
        LogFile logFile = LogFile.builder().creationDate(LocalDateTime.now()).
                log("Data Completeness check failed. " + s + " column  has missing value for row " + row).build();

        logFileList.add(logFile);
    }

    private void logDuplicate(List<String> duplicateList) {
        if (duplicateList.size() > 0) {
            for (String data : duplicateList) {
                logFile = LogFile.builder().creationDate(LocalDateTime.now()).log("Duplicate Found for " + data).build();
            }
        } else {
            logFile = LogFile.builder().creationDate(LocalDateTime.now()).log("Data Uniqueness check passed. No duplicate entry found").build();
        }
        logFileList.add(logFile);
    }

}
