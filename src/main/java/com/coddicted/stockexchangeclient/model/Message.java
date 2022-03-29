package com.coddicted.stockexchangeclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Message {
    private String companyId;
    private String companyName;
    private Integer price;
    private ZonedDateTime timestamp;
}
