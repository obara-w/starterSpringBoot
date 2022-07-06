package com.example.fraud.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.example.fraud.container.FraudCheckHistory;

//public interface FraudCheckHistoryRepository extends JpaRepository<FraudCheckHistory, Integer> {
//}

public interface FraudCheckHistoryRepository extends ReactiveCosmosRepository<FraudCheckHistory, String> {

}
