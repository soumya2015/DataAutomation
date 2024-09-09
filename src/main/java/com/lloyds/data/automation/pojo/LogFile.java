package com.lloyds.data.automation.pojo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogFile {

    private LocalDateTime creationDate;
    private String log;
}
