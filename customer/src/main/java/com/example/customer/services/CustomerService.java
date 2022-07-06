package com.example.customer.services;

import com.example.customer.dto.CustomerRegistrationRequest;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.dto.FraudCheckResponse;
import com.example.customer.entity.Customer;
import org.apache.el.util.ReflectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public record CustomerService(CustomerRepository customerRepository, RestTemplate restTemplate) {

    public Customer register(CustomerRegistrationRequest request) {
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
        assert fraudCheckResponse != null;
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification

        return customer;
    }

    public Customer getCustomer(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.orElseThrow();// orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return  customerRepository.findAll();
    }

    public void deleteCustomer(Integer customerId) {
        customerRepository.deleteById(customerId);
    }

    public Customer updateCustomer(Integer customerId, CustomerRegistrationRequest request) {
        Customer customer = getCustomer(customerId);
        customer.setFirstName(request.fistName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        return customerRepository.saveAndFlush(customer);
    }

    public Customer updateCustomer(Integer customerId, Map<Object, Object> partial) {
        Customer customer = getCustomer(customerId);
        partial.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Customer.class, String.valueOf(k));
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, customer, v);
        });
        return customerRepository.saveAndFlush(customer);
    }
}
