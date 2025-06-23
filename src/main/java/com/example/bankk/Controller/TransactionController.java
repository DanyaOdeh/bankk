package com.example.bankk.Controller;

import com.example.bankk.Service.TransactionService;
import com.example.bankk.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestParam Long senderId,
                                @RequestParam Long receiverId,
                                @RequestParam BigDecimal amount,
                                @RequestParam(required = false) String description) {
        return transactionService.transfer(senderId, receiverId, amount, description);
    }

}
