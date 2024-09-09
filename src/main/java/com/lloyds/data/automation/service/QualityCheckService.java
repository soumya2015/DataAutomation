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

    public List<SampleData> loadSampleData(String filePath) throws FileNotFoundException {
        log.info("Loading Sample Data");
        List<SampleData> sampleDataList = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(SampleData.class).build( ).parse( );
        log.info("Sample data loaded successfully.");
        return sampleDataList;
    }


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

    public static List<Integer> listDuplicateId(List<Integer> list) {
        Set<Integer> elements = new HashSet<Integer>( );
        return list.stream( )
                .filter(n -> !elements.add(n))
                .collect(Collectors.toList( ));
    }



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
