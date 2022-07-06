package com.example.customer.services;

import com.example.customer.dto.CustomerRegistrationRequest;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.dto.FraudCheckResponse;
import com.example.customer.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate) {

    public void register(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.fistName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);
        // todo: check if fraudster
        String fraudEndPoint = System.getenv("FRAUD_ENDPOINT");
        String url = fraudEndPoint + "/{customerId}";
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                url,
                FraudCheckResponse.class,
                customer.getId()
        );
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification
    }
}
