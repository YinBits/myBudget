package com.mybudget.Entity;

import com.mybudget.model.TransactionType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne
//    private Category category_id;
//
//    @ManyToOne
//    private Account account;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentMethod paymentMethod;
//
//    private Boolean isRecurring;
//
//    private String recurrencePatern;

}
