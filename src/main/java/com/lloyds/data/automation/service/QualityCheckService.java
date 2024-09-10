package com.lloyds.data.automation.service;

import com.lloyds.data.automation.pojo.LogFile;
import com.lloyds.data.automation.pojo.SampleData;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QualityCheckService {
    /**
     * Loads sample data from a specified CSV file and returns a list of SampleData objects.
     *
     * @param filePath The path to the CSV file that contains the sample data.
     * @return A list of {@link SampleData} objects populated from the CSV file.
     *         If the file is empty or cannot be read, an empty list may be returned.
     * @throws FileNotFoundException If there is an error reading the file.
     */
    public List<SampleData> loadSampleData(String filePath) throws FileNotFoundException {
        log.info("Loading Sample Data");
        List<SampleData> sampleDataList = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(SampleData.class).build( ).parse( );
        log.info("Sample data loaded successfully.");
        return sampleDataList;
    }
    /**
     * Checks if the given string is in a valid date format.
     *
     * This method validates whether the provided string follows the expected date format (e.g., YYYY-MM-DD).
     * The exact format can depend on the implementation details of the method.
     *
     * @param value The string representing the date to be validated.
     * @return {@code true} if the string is in a valid date format; {@code false} otherwise.
     */

    public static boolean isValidDateFormat(String value) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            //log.error(ex.getMessage());
        }
        return date != null;
    }
    /**
     * Identifies and returns a list of duplicate IDs from the given list of integers.
     *
     * This method processes the provided list of integers and checks for duplicate values.
     * If duplicates are found, they are collected and returned as a list. If no duplicates
     * are found, the method returns an empty list.
     *
     * @param list The list of integer IDs to check for duplicates.
     * @return A list of duplicate IDs found in the provided list. If no duplicates are found,
     *         an empty list is returned.
     */
    public static List<Integer> listDuplicateId(List<Integer> list) {
        Set<Integer> elements = new HashSet<Integer>( );
        return list.stream( )
                .filter(n -> !elements.add(n))
                .collect(Collectors.toList( ));
    }

    /**
     * Identifies and returns a list of duplicate names from the given list of strings.
     *
     * This method processes the provided list of strings, checks for duplicate values (names),
     * and collects all duplicates. If any duplicate names are found, they are returned as a list.
     * If no duplicates are found, the method returns an empty list.
     *
     * @param list The list of names (strings) to check for duplicates.
     * @return A list of duplicate names found in the provided list. If no duplicates are found,
     *         an empty list is returned.
     */

    public static List<Integer> listDuplicateName(List<String> list) {
       return null;
    }

    public void writeToFile(List<LogFile> logFileList) throws IOException
    {
        // Write to a text file.
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

        for(LogFile logFile: logFileList) {
            writer.write(logFile.getCreationDate() + ","+logFile.getLog() + System.lineSeparator());
        }
        writer.close( );
    }
    /**
     * Identifies and returns a list of duplicate records based on the provided list of SampleData objects.
     *
     * This method processes the given list of SampleData objects and checks for duplicate records.
     * Duplicate records are determined based on specific fields in the SampleData object (e.g., ID, name, etc.).
     * The method returns a list of string representations of the duplicate records found in the dataset.
     * If no duplicates are found, an empty list is returned.
     *
     * @param dataList The list of {@link SampleData} objects to check for duplicates.
     * @return A list of string representations of duplicate records. If no duplicates are found,
     *         an empty list is returned.
     */
    public List<String> findDuplicateRecords(List<SampleData> dataList)
    {

        List<String[]> dataArrList = new ArrayList<>();
        List<String> duplicateList = new ArrayList<>();

        for (SampleData data1: dataList)
        {
            String[] valueArr = new String [] {String.valueOf(data1.getId()).trim(),data1.getName().trim(),
                    data1.getDate().trim(), String.valueOf(data1.getAmount()).trim()};
            dataArrList.add(valueArr);
        }

        log.info("dataList:"+dataList);

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
}
