package com.example.bankk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonBackReference("sent")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonBackReference("received")
    private Account receiver;

    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
}
