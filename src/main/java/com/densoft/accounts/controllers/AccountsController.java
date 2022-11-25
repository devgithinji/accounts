package com.densoft.accounts.controllers;

import com.densoft.accounts.config.AccountsConfig;
import com.densoft.accounts.model.*;
import com.densoft.accounts.repository.AccountsRepository;
import com.densoft.accounts.service.client.CardsFeignClient;
import com.densoft.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountsConfig accountsConfig;

    @Autowired
    LoansFeignClient loansFeignClient;

    @Autowired
    CardsFeignClient cardsFeignClient;

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        if (accounts != null) {
            return accounts;
        }
        return null;
    }

    @GetMapping("/account/properties")
    public String getProperties() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(), accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }

    @PostMapping("/myCustomerDetails")
    @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    @Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails customerDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestBody Customer customer) {
        logger.info("myCustomerDetails() method started");
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Cards> cardsList = cardsFeignClient.getCardDetails(correlationId, customer);
        List<Loans> loansList = loansFeignClient.getLoansDetails(correlationId, customer);
        logger.info("myCustomerDetails() method ended");
        return new CustomerDetails(accounts, loansList, cardsList);
    }


    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallBack")
    public String sayHello() {
        return "Hello, Welcome to Eazy Bank";
    }

    private String sayHelloFallBack() {
        return "Hi, Welcome to Eazy Bank";
    }


    private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("eazybank-correlation-id") String correlationId, Customer customer, Throwable throwable) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loansList = loansFeignClient.getLoansDetails(correlationId, customer);
        return new CustomerDetails(accounts, loansList);
    }
}
