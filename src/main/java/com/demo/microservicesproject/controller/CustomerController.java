package com.demo.microservicesproject.controller;


import com.demo.microservicesproject.enums.AccountType;
import com.demo.microservicesproject.exception.ResourceNotFoundException;
import com.demo.microservicesproject.misc.BankAccount;
import com.demo.microservicesproject.misc.TransactionResponse;
import com.demo.microservicesproject.model.Customer;
import com.demo.microservicesproject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/api/v1/account")
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody Customer customer) {

        Map<String, Object> responseMap = new HashMap<>();

        if (customer.getCustomerName() == null || customer.getCustomerName().isEmpty()) {
            TransactionResponse response = new TransactionResponse(400, "Customer Name is required field");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        }

        if (customer.getCustomerMobile() == null || customer.getCustomerMobile().isEmpty()) {
            TransactionResponse response = new TransactionResponse(400, "Customer Mobile is required field");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        }

        if (customer.getCustomerEmail() == null || customer.getCustomerEmail().isEmpty()) {
            TransactionResponse response = new TransactionResponse(400, "Email is required field");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        } else if (!customer.isValidEmail()) {
            TransactionResponse response = new TransactionResponse(400, "Email is not a valid email address");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        }

        if (customer.getAddress1() == null || customer.getAddress1().isEmpty()) {
            TransactionResponse response = new TransactionResponse(400, "Customer Address 1 is required field");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        }

        if (customer.getAccountType() == null) {
            TransactionResponse response = new TransactionResponse(400, "Customer Account Type is required field");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.badRequest().body(responseMap);
        }

        System.out.println("abbrev string: " + customer.getAccountType().getAbbreviation());

        // Init creation of customer.
        Customer createdCustomer = this.customerService.createCustomer(customer);

        TransactionResponse response = new TransactionResponse(201, "Customer account created");

        responseMap.put("customerNumber", createdCustomer.getCustomerNumber());
        responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
        responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());

        return ResponseEntity.status(201).body(responseMap);
    }

    @GetMapping("/api/v1/account/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long id) {

        Map<String, Object> responseMap = new HashMap<>();

        Customer customer = null;

        try {
            customer = customerService.getCustomerById(id);
        } catch (ResourceNotFoundException ex) {
            TransactionResponse response = new TransactionResponse(401, "Customer not found");
            responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
            responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());
            return ResponseEntity.status(401).body(responseMap);
        }

        responseMap.put("customerNumber", customer.getCustomerNumber());
        responseMap.put("customerName", customer.getCustomerName());
        responseMap.put("customerMobile", customer.getCustomerMobile());
        responseMap.put("customerEmail", customer.getCustomerEmail());
        responseMap.put("address1", customer.getAddress1());
        responseMap.put("address2", customer.getAddress2());

        // generate random account number and available balance that is not specified to the API Specification
        long MIN_ACCOUNT_NUMBER = 1000000000L;
        long MAX_ACCOUNT_NUMBER = 9999999999L;
        BigDecimal MIN_BALANCE = new BigDecimal("100.00");
        BigDecimal MAX_BALANCE = new BigDecimal("1000000.00");

        long accountNumber = ThreadLocalRandom.current().nextLong(MIN_ACCOUNT_NUMBER, MAX_ACCOUNT_NUMBER);
        double balance = MIN_BALANCE.doubleValue() + (MAX_BALANCE.doubleValue() - MIN_BALANCE.doubleValue()) * ThreadLocalRandom.current().nextDouble();

        BankAccount savings = new BankAccount(String.valueOf(accountNumber), customer.getAccountType().toString(), balance);

        responseMap.put("savings", savings);
        TransactionResponse response = new TransactionResponse(302, "Customer Account found");
        responseMap.put("transactionStatusCode", response.getTransactionStatusCode());
        responseMap.put("transactionStatusDescription", response.getTransactionStatusDescription());

        return ResponseEntity.ok().body(responseMap);
    }

    private boolean isValidAccountType(AccountType accountType) {
        return accountType == AccountType.Savings || accountType == AccountType.Checking;
    }
}
