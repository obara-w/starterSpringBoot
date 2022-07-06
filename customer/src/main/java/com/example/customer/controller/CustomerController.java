package com.example.customer.controller;

import com.example.customer.dto.CustomerRegistrationRequest;
import com.example.customer.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration as {}", customerRegistrationRequest);
        customerService.register(customerRegistrationRequest);
    }
}
