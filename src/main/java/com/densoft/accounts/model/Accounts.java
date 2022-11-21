package com.densoft.accounts.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Accounts {

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_number")
    private long accountNumber;

    @Column(name = "account_type", length = 100,nullable = false)
    private String accountType;


    @Column(name = "branch_address", length = 200, nullable = false)
    private String branchAddress;

    @Column(name = "create_dt")
    private LocalDate createDt;

}
