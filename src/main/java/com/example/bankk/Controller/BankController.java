package com.example.bankk.Controller;

import com.example.bankk.model.Account;
import com.example.bankk.model.Customer;
import com.example.bankk.Service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/accounts")
    public Account addAccount(@RequestBody Account account) {
        return bankService.addAccount(account);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return bankService.getAllCustomers();
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return bankService.getAllAccounts();
    }

    @PostMapping("/withdraw")
    public Account withdraw(@RequestParam String accountNumber,
                            @RequestParam BigDecimal amount) {
        return bankService.withdraw(accountNumber, amount);
    }

}
