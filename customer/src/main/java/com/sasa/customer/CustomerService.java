package com.sasa.customer;

import com.sasa.clients.fraud.FraudCheckResponse;
import com.sasa.clients.fraud.FraudClient;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              FraudClient fraudClient) {
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // toDO: check if email valid
        // toDO: check if email not taken

        customerRepository.saveAndFlush(customer);

        // check if fraudster
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Fraudster");
        }

        // toDO: send notification
    }
}
