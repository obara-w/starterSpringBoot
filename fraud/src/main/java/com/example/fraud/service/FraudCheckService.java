package com.example.fraud.service;

import com.example.fraud.container.FraudCheckHistory;
import com.example.fraud.repository.FraudCheckHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

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
                        .createdAt(ZonedDateTime.now())
                        .build()
        );

        final FraudCheckHistory savedCheck = checkMono.block();
        assert savedCheck != null;
        log.info(savedCheck.toString());
        return false;
    }

    public List<FraudCheckHistory> getCustomerFraudChecks(Integer customerId) {
        Flux<FraudCheckHistory> fraudCheckHistoriesByCustomerId = fraudCheckHistoryRepository.findFraudCheckHistoriesByCustomerId(customerId);
        return fraudCheckHistoriesByCustomerId.collectList().block();
    }

    public List<FraudCheckHistory> getAllFraudChecks() {
        Flux<FraudCheckHistory> fraudCheckHistoriesByCustomerId = fraudCheckHistoryRepository.findAll();
        return fraudCheckHistoriesByCustomerId.collectList().block();
    }

    public void deleteCustomerFraudChecks(Integer customerId) {
        fraudCheckHistoryRepository.deleteByCustomerId(customerId).block();
    }

    public void deleteAllCustomerFraudChecks() {
        fraudCheckHistoryRepository.deleteAll().block();
    }
}
