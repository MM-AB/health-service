package com.rso40.healthservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "health")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Health {

    private String status;
    private Components components;



}