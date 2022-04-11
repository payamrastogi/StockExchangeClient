package com.coddicted.stockexchange.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@ToString
public class Message {
    private String companyId;
    private String companyName;
    private Integer price;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime timestamp;
}