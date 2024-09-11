package com.lloyds.data.automation.steps;

import com.lloyds.data.automation.pojo.UncleanData;
import com.lloyds.data.automation.service.DataTransformationService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.lloyds.data.automation.service.DataTransformationService.handleMissingNumeric;
import static com.lloyds.data.automation.service.DataTransformationService.normalizeText;

@Slf4j
public class DataTransformationSteps {

    private DataTransformationService dataTransformationService;
    List<UncleanData> uncleanDataList;

    @Given("the dataset is loaded from {string} for data transformation and cleaning")
    public void the_dataset_is_loaded_from_for_data_transformation_and_cleaning(String string) throws FileNotFoundException {
        log.info("Loading data file {} ", string);
        dataTransformationService = new DataTransformationService();
        uncleanDataList = dataTransformationService.loadUncleanData(string);
        log.info("Unclean" + uncleanDataList);

        for (UncleanData uncleanData : uncleanDataList) {
            log.info("Unclean Data ID:" + uncleanData.getId()
                    + ",Name:" + uncleanData.getName() + " + Trans Date: " + uncleanData.getTransactionDate() + " Amount: " + uncleanData.getAmount());
        }
        Assert.notNull(uncleanDataList, "Unclean Data file not Available");
    }

    @Then("Check the dataset for duplicate values based on the {string} column")
    public void check_the_dataset_for_duplicate_values_based_on_the_column(String string) throws FileNotFoundException {
        dataTransformationService.findDuplicateRecords(uncleanDataList);
    }

    @Then("remove duplicate rows from the CSV file")
    public void remove_duplicate_rows_from_the_csv_file() {
    }

    @Then("standardize the {string} column to {string} format")
    public void standardize_the_column_to_format(String string, String string2) throws ParseException {
        for (UncleanData uncleanData : uncleanDataList) {
            // Check the date format
            String standardiseDate = dataTransformationService.standardiseDateFormat(uncleanData.getTransactionDate());
            log.info("Original: " + uncleanData.getTransactionDate() + " -> Standardized: " + standardiseDate);
            uncleanData.setTransactionDate(standardiseDate);
        }
    }

    @Then("trim whitespace and convert all text in the {string} column to lowercase")
    public void trim_whitespace_and_convert_all_text_in_the_column_to_lowercase(String string) {
        for (UncleanData uncleanData : uncleanDataList) {
            // Check the date format
            String trimName = normalizeText(uncleanData.getName());
            log.info("Original format: " + uncleanData.getName() + " -> Trim Name: " + trimName);
            uncleanData.setName(trimName);
        }
    }

    @Then("replace missing values in the Amount column with the median of the non-missing values")
    public void replace_missing_values_in_the_amount_column_with_the_median_of_the_non_missing_values() {
        uncleanDataList = handleMissingNumeric(uncleanDataList);
        for (UncleanData uncleanData : uncleanDataList) {
            log.info("Amount: " + uncleanData.getAmount() + "For "
                    + uncleanData.getName() + "ID :" + uncleanData.getId() + " Date :"
                    + uncleanData.getTransactionDate());
        }
    }

    @Then("save the cleaned dataset to {string}")
    public void save_the_cleaned_dataset_to(String string) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        dataTransformationService.writeToCSV(uncleanDataList, string);
    }

}
