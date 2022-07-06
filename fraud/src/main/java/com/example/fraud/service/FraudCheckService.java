package com.example.fraud.service;

import com.example.fraud.container.FraudCheckHistory;
import com.example.fraud.repository.FraudCheckHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class FraudCheckService {

    private final FraudCheckHistoryRepository fraudCheckHistoryRepository;

    public boolean isFraudulentCustomer(Integer customerId) {
        Mono<FraudCheckHistory> checkMono = fraudCheckHistoryRepository.save(
                FraudCheckHistory.builder()
                        .customerId(customerId)
                        .isFraudster(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        final FraudCheckHistory savedCheck = checkMono.block();
        log.info(savedCheck.toString());
        return false;
    }
}
