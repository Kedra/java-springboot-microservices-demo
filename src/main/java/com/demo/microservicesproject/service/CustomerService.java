package com.demo.microservicesproject.service;

import com.demo.microservicesproject.model.Customer;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long customerId);
}
