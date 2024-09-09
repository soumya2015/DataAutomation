package com.lloyds.data.automation.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SampleData {
    @CsvBindByName
    private int id;
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String date;
    @CsvBindByName
    private Double amount;
}
