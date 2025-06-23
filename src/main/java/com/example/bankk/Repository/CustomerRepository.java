package com.example.bankk.Repository;

import com.example.bankk.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNameContainingIgnoreCase(String name);
    Customer findByNationalId(String nationalId);
    List<Customer> findByCountryContainingIgnoreCase(String country);

}
