package com.demo.microservicesproject.repository;

import com.demo.microservicesproject.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
