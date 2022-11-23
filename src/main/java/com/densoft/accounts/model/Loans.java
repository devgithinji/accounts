package com.densoft.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Loans {
    private int loanNumber;
    private int customerId;
    private Date startDt;
    private String loanType;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
    private String createDt;
}
