package com.example.bankk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    private BigDecimal balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonManagedReference("sent")
    private List<Transaction> sentTransactions;

    @JsonIgnore
    @JsonManagedReference("received")
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions;
}