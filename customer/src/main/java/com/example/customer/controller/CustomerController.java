package com.example.customer.controller;

import com.example.customer.dto.CustomerRegistrationRequest;
import com.example.customer.entity.Customer;
import com.example.customer.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Customer registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration as {}", customerRegistrationRequest);
        return customerService.register(customerRegistrationRequest);
    }

    @GetMapping(value = "{customerId}")
    @ResponseStatus(code = HttpStatus.OK)
    public Customer getCustomer(@PathVariable("customerId") Integer customerId) {
        try {
            return customerService.getCustomer(customerId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No customer found with id %d", customerId));
        }
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping(value = "{customerId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
        try {
            customerService.deleteCustomer(customerId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No customer found with id %d", customerId));
        }
    }

    @PutMapping(value = "{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") Integer customerId, @RequestBody CustomerRegistrationRequest request) {
        try {
            Customer customer = customerService.updateCustomer(customerId, request);
            return ResponseEntity.status(HttpStatus.OK).body(customer);
        } catch (NoSuchElementException e) {
            log.info("Customer with id {} doesnt exist, create a new one", customerId);
            Customer customer = registerCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        }
    }

    @PatchMapping(value = "{customerId}")
    @ResponseStatus(code = HttpStatus.OK)
    public Customer updatePartialCustomer(@PathVariable("customerId") Integer customerId, @RequestBody Map<Object, Object> partial) {
        return customerService.updateCustomer(customerId, partial);
    }
}
