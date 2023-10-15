package com.demo.microservicesproject.service;

import com.demo.microservicesproject.exception.ResourceNotFoundException;
import com.demo.microservicesproject.model.Customer;
import com.demo.microservicesproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Override
    public Customer getCustomerById(Long customerId) {
        Optional<Customer> customer = this.customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
    }
}
