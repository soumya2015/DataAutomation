package com.lloyds.data.automation.pojo;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UncleanData {
    @CsvBindByName
    private int id;
    @CsvBindByName
    private String name;
    @CsvBindByName(column = "Transaction Date")
    private String transactionDate;
    @CsvBindByName
    private Double amount;
}
