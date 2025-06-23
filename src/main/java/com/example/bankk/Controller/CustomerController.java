package com.example.bankk.Controller;

import com.example.bankk.Service.CustomerService;
import com.example.bankk.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer saved = customerService.createCustomer(customer);
        return ResponseEntity.ok(saved);
    }

}
