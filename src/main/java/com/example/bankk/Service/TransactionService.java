package com.example.bankk.Service;
import com.example.bankk.Repository.AccountRepository;
import com.example.bankk.Repository.TransactionRepository;
import com.example.bankk.model.Account;
import com.example.bankk.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction transfer(Long senderId, Long receiverId, BigDecimal amount, String description) {
        Account sender = accountRepository.findById(senderId).orElseThrow();
        Account receiver = accountRepository.findById(receiverId).orElseThrow();

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description != null ? description : "Transfer");

        transactionRepository.save(transaction);
        accountRepository.save(sender);
        accountRepository.save(receiver);

        return transaction;
    }

}

