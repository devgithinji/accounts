package com.densoft.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CustomerDetails {
    private Accounts accounts;
    private List<Loans> loans;
    private List<Cards> cards;
}
