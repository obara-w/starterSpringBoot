package com.example.fraud.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.example.fraud.container.FraudCheckHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FraudCheckHistoryRepository extends ReactiveCosmosRepository<FraudCheckHistory, String> {
    Flux<FraudCheckHistory> findFraudCheckHistoriesByCustomerId(Integer customerId);
    Mono<Void> deleteByCustomerId(Integer customerId);
}
