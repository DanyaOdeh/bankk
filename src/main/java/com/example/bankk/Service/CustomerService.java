package com.example.bankk.Service;

import com.example.bankk.model.Account;
import com.example.bankk.model.Customer;
import com.example.bankk.Repository.AccountRepository;
import com.example.bankk.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public Customer createCustomer(Customer customer) {
        Customer existing = customerRepository.findByNationalId(customer.getNationalId());
        if (existing != null) {
            throw new IllegalArgumentException("Customer already exists!");
        }

        Customer savedCustomer = customerRepository.save(customer);

        Account defaultAccount = new Account();
        defaultAccount.setAccountNumber(generateAccountNumber(savedCustomer.getId()));
        defaultAccount.setBalance(BigDecimal.ZERO);
        defaultAccount.setCustomer(savedCustomer);
        accountRepository.save(defaultAccount);

        return savedCustomer;
    }

    private String generateAccountNumber(Long customerId) {
        return "ACC" + customerId + (System.currentTimeMillis() % 10000);
    }
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
