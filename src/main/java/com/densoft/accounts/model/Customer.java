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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private int customerId;
    @Column(name = "name", nullable = false,length = 100)
    private String name;
    @Column(nullable = false,length = 100)
    private String email;
    @Column(name = "mobile_number",nullable = false,length = 20)
    private String mobileNumber;
    @Column(name = "create_dt")
    private LocalDate createDt;
}
