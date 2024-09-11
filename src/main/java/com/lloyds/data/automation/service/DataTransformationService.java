package com.lloyds.data.automation.service;

import com.lloyds.data.automation.pojo.SampleData;
import com.lloyds.data.automation.pojo.UncleanData;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public class DataTransformationService {
    /**
     * Loads sample data from a specified CSV file and returns a list of SampleData objects.
     *
     * @param fileName The path to the CSV file that contains the sample data.
     * @return A list of {@link SampleData} objects populated from the CSV file.
     * If the file is empty or cannot be read, an empty list may be returned.
     * @throws FileNotFoundException If there is an error reading the file.
     */
    public List<UncleanData> loadUncleanData(String fileName) throws FileNotFoundException {
        //log.info("Loading Sample Data");
        final String filenameWithPath = "input/".concat(fileName);
        List<UncleanData> uncleanDataList = new CsvToBeanBuilder(new FileReader(filenameWithPath))
                .withType(UncleanData.class).build().parse();
        return uncleanDataList;
    }

    /**
     * Standardizes the provided date string to the 'YYYY-MM-DD' format.
     * <p>
     * This method attempts to parse the given date string, which can be in different formats,
     * and converts it into the standard format 'YYYY-MM-DD'.
     *
     * @param dateString The date string to be standardized.
     * @return The date string formatted as 'YYYY-MM-DD'.
     * @throws ParseException If the input date string cannot be parsed.
     */
    public String standardiseDateFormat(String dateString) throws ParseException {
        // Add all possible date format.
        String[] strArray1 = new String[]{"yyyy-MM-dd", "yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd"};
        Date formattedDate = DateUtils.parseDateStrictly(dateString, strArray1);
        log.info("formattedDate " + formattedDate);
        return DateFormatUtils.format(formattedDate, "yyyy-MM-dd");
    }

    public static void listDuplicateValues(List<UncleanData> compositeKeyValues) {
        List<UncleanData> duplicates = compositeKeyValues.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue().intValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        log.info("Duplicate Value size: {}" , duplicates.size());
        for (UncleanData uncleanData : duplicates) {
            log.info("listDuplicateValues Data ID:" + uncleanData.getId()
                    + ",Name:" + uncleanData.getName() + " + Trans Date: " + uncleanData.getTransactionDate() + " Amount: " + uncleanData.getAmount());
        }
    }

    /*
     * Normalizes the given text by performing trimming whitespace, converting to lowercase
     * @param text the input text to be normalized; should not be {@code null}
     * @return a normalized version of the input text, with whitespace trimmed and converted to lowercase

     */
    public static String normalizeText(String text) {
        if (text == null) {
            return null; // Handle null values
        }
        // Trim leading/trailing whitespace and convert to lowercase
        return text.trim().toLowerCase();
    }

    /**
     * Processes a list of {@link UncleanData} objects to handle missing numeric values.
     *
     * @param uncleanDataList a {@link List} of {@link UncleanData} objects that may contain
     *                        missing or incomplete numeric data. This list can be {@code null},
     *                        in which case the method will return an empty list.
     * @return a {@link List} of {@link UncleanData} objects with missing numeric values handled
     * according to the predefined strategies. The returned list will not be {@code null},
     * but it may be empty if the input list was empty or all data was removed or handled.
     */
    public static List<UncleanData> handleMissingNumeric(List<UncleanData> uncleanDataList) {
        // Calculate the mean of non-null numbers
        double sum = 0;
        int size = 0;

        for (UncleanData uncleanData : uncleanDataList) {
            if (uncleanData.getAmount() != null) {
                sum = sum + uncleanData.getAmount();
                size++;
            }
        }
        double average = sum / size;
        log.info("Average value for the amount: " + average);

        for (int i = 0; i < uncleanDataList.size(); i++) {
            if (uncleanDataList.get(i).getAmount() == null) {
                uncleanDataList.get(i).setAmount(average);
            }
        }
        return uncleanDataList;
    }

    /**
     * Identifies and returns a list of duplicate records based on the provided list of SampleData objects.
     * <p>
     * This method processes the given list of SampleData objects and checks for duplicate records.
     * Duplicate records are determined based on specific fields in the SampleData object (e.g., ID, name, etc.).
     * The method returns a list of string representations of the duplicate records found in the dataset.
     * If no duplicates are found, an empty list is returned.
     *
     * @param dataList The list of {@link UncleanData} objects to check for duplicates.
     * @return A list of string representations of duplicate records. If no duplicates are found,
     * an empty list is returned.
     */
    public List<String> findDuplicateRecords(List<UncleanData> dataList) {

        List<String[]> dataArrList = new ArrayList<>();
        List<String> duplicateList = new ArrayList<>();

        for (UncleanData data1 : dataList) {
            String[] valueArr = new String[]{String.valueOf(data1.getId()).trim(), data1.getName().trim(),
                    data1.getTransactionDate().trim(), String.valueOf(data1.getAmount()).trim()};
            dataArrList.add(valueArr);
        }

        log.info("dataList:" + dataList);

        // Set to track unique rows
        Set<String> uniqueRows = new HashSet<>();
        boolean hasDuplicates = false;

        // Check each row in the data
        for (String[] row : dataArrList) {

            // Convert row array to a single string for comparison
            String rowString = Arrays.toString(row);

            // Check if this row already exists in the set
            if (!uniqueRows.add(rowString)) {
                log.info("Duplicate found: " + rowString);
                duplicateList.add(rowString);
            }
        }

        if (!hasDuplicates) {
            log.info("No duplicates found.");
        }
        return duplicateList;
    }

    /**
     * Writes a list of {@link UncleanData} objects to a CSV file.
     *
     * @param uncleanDataList a {@link List} of {@link UncleanData} objects to be written to
     *                        the CSV file. This list can be {@code null}, in which case
     *                        the method will not write any data.
     */
    public void writeToCSV(List<UncleanData> uncleanDataList, String fileName) {
        final String filenameWithPath = "output/".concat(fileName);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filenameWithPath))) {
            // Write header of the "Clean_Data.csv" file.
            String[] header = {"ID", "NAME", "TRANSACTION DATE", "AMOUNT"};
            writer.writeNext(header);

            // Write data into "Clean_Data.csv" file.
            for (UncleanData finalData : uncleanDataList) {
                String[] data = {
                        String.valueOf(finalData.getId()),
                        finalData.getName(),
                        finalData.getTransactionDate(),
                        String.valueOf(finalData.getAmount())
                };
                writer.writeNext(data);
            }
            log.info("Final CSV file {} has been created.", fileName);
        } catch (IOException e) {
            log.error("Exception occurred while creating new file." + e.getMessage());
        }

    }

}