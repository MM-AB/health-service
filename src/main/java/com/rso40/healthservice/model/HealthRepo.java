package com.rso40.healthservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "healthRepo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HealthRepo {
    private String serviceName;
    private long timestamp;
    private Health health;

}
