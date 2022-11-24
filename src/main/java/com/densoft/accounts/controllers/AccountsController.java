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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AccountsController {
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
    public CustomerDetails customerDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Cards> cardsList = cardsFeignClient.getCardDetails(customer);
        List<Loans> loansList = loansFeignClient.getLoansDetails(customer);
        return new CustomerDetails(accounts, loansList, cardsList);
    }

    private CustomerDetails myCustomerDetailsFallBack(Customer customer, Throwable throwable) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loansList = loansFeignClient.getLoansDetails(customer);
        return new CustomerDetails(accounts, loansList);
    }
}
