package com.example.fraud.controller;

import com.example.fraud.container.FraudCheckHistory;
import com.example.fraud.dto.FraudCheckResponse;
import com.example.fraud.service.FraudCheckService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudCheckController {

    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "check/{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId) {
        boolean isFraudulentCustomer = fraudCheckService.isFraudulentCustomer(customerId);
        log.info("fraud check requested from customer {}", customerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }

    @GetMapping(value = "{customerId}")
    public List<FraudCheckHistory> getCustomerFraudChecks(@PathVariable("customerId") Integer customerId) {
        List<FraudCheckHistory> customerFraudChecks = fraudCheckService.getCustomerFraudChecks(customerId);
        log.info("customerFraudChecks: {}", customerFraudChecks);
        return customerFraudChecks;
    }

    @GetMapping
    public List<FraudCheckHistory> getAllFraudChecks() {
        return fraudCheckService.getAllFraudChecks();
    }

    @DeleteMapping(value = "{customerId}")
    public void deleteCustomerFraudChecks(@PathVariable("customerId") Integer customerId) {
        fraudCheckService.deleteCustomerFraudChecks(customerId);
    }

    @DeleteMapping
    public  void deleteAllFraudChecks() {
        fraudCheckService.deleteAllCustomerFraudChecks();
    }
}
