package com.example.fraud.container;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Container
public class FraudCheckHistory {
    @Id
    @GeneratedValue
    private String id;
    private Integer customerId;
    private Boolean isFraudster;
    private ZonedDateTime createdAt;
}
