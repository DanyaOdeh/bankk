package com.example.bankk.Service;

import com.example.bankk.model.*;
import com.example.bankk.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public Customer addCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Account addAccount(Account account) {
        Customer customer = customerRepo.findById(account.getCustomer().getId()).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        account.setCustomer(customer);
        return accountRepo.save(account);
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public List<Customer> searchCustomer(String keyword) {
        return customerRepo.findByNameContainingIgnoreCase(keyword);
    }

    private void recordTransaction(Account sender, Account receiver, BigDecimal amount, String description) {
        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setDate(LocalDateTime.now());
        transactionRepo.save(tx);
    }

    // إيداع بدون تحقق كلمة السر
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account target = accountRepo.findByAccountNumber(accountNumber);
        if (target == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        target.setBalance(target.getBalance().add(amount));
        accountRepo.save(target);

        recordTransaction(null, target, amount, "Deposit");

        return target;
    }

    // سحب بدون تحقق كلمة السر
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account source = accountRepo.findByAccountNumber(accountNumber);
        if (source == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (source.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        source.setBalance(source.getBalance().subtract(amount));
        accountRepo.save(source);

        recordTransaction(source, null, amount, "Withdraw");

        return source;
    }

    // تحويل بدون تحقق كلمة سر المرسل
    public Account transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount, String description) {
        Account sender = accountRepo.findByAccountNumber(senderAccountNumber);
        Account receiver = accountRepo.findByAccountNumber(receiverAccountNumber);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender or receiver account not found");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepo.save(sender);
        accountRepo.save(receiver);

        recordTransaction(sender, receiver, amount, description);

        return sender;
    }
}