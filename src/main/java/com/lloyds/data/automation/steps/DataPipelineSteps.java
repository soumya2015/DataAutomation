package com.lloyds.data.automation.steps;

import com.lloyds.data.automation.pojo.UncleanData;
import com.lloyds.data.automation.service.DataTransformationService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.lloyds.data.automation.service.DataTransformationService.handleMissingNumeric;
import static com.lloyds.data.automation.service.DataTransformationService.normalizeText;
import static com.lloyds.data.automation.service.QualityCheckService.isValidDateFormat;

@Slf4j
@NoArgsConstructor
public class DataPipelineSteps {
    private DataTransformationService transformationService;
    List<UncleanData> uncleanDataList;

    @Given("the unclean dataset {string} is loaded")
    public void the_unclean_dataset_is_loaded(String string) throws FileNotFoundException {
        log.info("loaded the uncleandata.csv file {}", string);
        transformationService = new DataTransformationService();
        uncleanDataList = transformationService.loadUncleanData(string);
        log.info("Unclean", uncleanDataList);
        for (UncleanData uncleanData : uncleanDataList) {
            log.info("Uncleandaat ID :" + uncleanData.getId() + ",Name :" + uncleanData.getName()
                    + ",Date :" + uncleanData.getTransactionDate() + ",Amount :", uncleanData.getAmount());
        }
        Assert.notNull(uncleanDataList, "Unclean Data file not Available");
    }

    @When("the data quality checks are applied to the dataset")
    public void the_data_quality_checks_are_applied_to_the_dataset() {
        //Verify the Data Completeness: Ensure no missing values in specified columns.
        int counter = 0;
        for (UncleanData uncleanData : uncleanDataList) {
            counter++;

            // Log missing Data
            logMissingData(counter, uncleanData);

            //Data Consistency: Verify that date columns follow the format `YYYY-MM-DD`.
            boolean dateFormat = true;
            if (isValidDateFormat(uncleanData.getTransactionDate())) {
                log.info("Date format is correct for row {}", counter);
            } else {
                log.info("Incorrect Date Format");
            }
            // Data Uniqueness: Check for duplicate entries based on a unique identifier column.
            List<String> duplicateList = transformationService.findDuplicateRecords(uncleanDataList);

            if (duplicateList.size() > 0) {
                for (String data : duplicateList) {
                    log.info("Duplicate Found for " + data);
                }
            } else {
                log.info("Data Uniqueness check passed. No duplicate entry found");
            }
        }
    }

    private static void logMissingData(int counter, UncleanData uncleanData) {
        if (uncleanData.getId() <= 0) {
            log.info("Id is missing for row {}", counter);
        }
        if (StringUtils.isEmpty(uncleanData.getName())) {
            log.info("Missing name in row {}", counter);
        }
        if (StringUtils.isEmpty(uncleanData.getTransactionDate())) {
            log.info("Transaction date is missing in row {}", counter);
        }
        if ((uncleanData.getAmount() == null)) {
            log.info("Amount is missing in row {}", counter);
        }
    }

    @When("the data transformation and cleaning are applied to the loaded dataset")
    public void the_data_transformation_and_cleaning_are_applied_to_the_loaded_dataset() throws ParseException {

        for (UncleanData uncleanData : uncleanDataList) {
            // Standardise date formats to `YYYY-MM-DD`.
            String standardiseDate = transformationService.standardiseDateFormat(uncleanData.getTransactionDate());
            log.info("Original: " + uncleanData.getTransactionDate() + " -> Standardized: " + standardiseDate);
            uncleanData.setTransactionDate(standardiseDate);
            String trimName = normalizeText(uncleanData.getName());
            log.info("Original format: " + uncleanData.getName() + " -> Trim Name: " + trimName);
            uncleanData.setName(trimName);
        }
        List<UncleanData> uncleanDataList1 = handleMissingNumeric(uncleanDataList);
        for (UncleanData uncleanData : uncleanDataList1) {
            log.info("Amount: " + uncleanData.getAmount() + "For "
                    + uncleanData.getName() + "ID :" + uncleanData.getId() + " Date :"
                    + uncleanData.getTransactionDate());
        }
    }

    @Then("a new file {string} should be created successfully")
    public void a_new_file_should_be_created_successfully(String string) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        transformationService.writeToCSV(uncleanDataList, string);
    }
}
