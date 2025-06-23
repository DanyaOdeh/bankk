package com.example.bankk.Bean;

import com.example.bankk.Service.BankService;
import com.example.bankk.Service.CustomerService;
import com.example.bankk.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("customerBean")
@ViewScoped
@Component
public class CustomerBean implements Serializable {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private BankService bankService;

    private Customer customer = new Customer();
    private List<Customer> customers;
    private List<Customer> allCustomers; // للاحتفاظ بالقائمة الكاملة للعملاء
    private String searchKeyword;

    @PostConstruct
    public void init() {
        loadCustomers();
        System.out.println("Loaded customers: " + (customers != null ? customers.size() : 0));
    }


    public void loadCustomers() {
        try {
            allCustomers = bankService.getAllCustomers();
            customers = allCustomers;
        } catch (Exception e) {
            addMessage("Error", "Failed to load customers: " + e.getMessage());
            addErrorMessage("Error", "Failed to load customers: " + e.getMessage());
        }
    }

    public void createCustomer() {
        try {

            if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                addErrorMessage("Validation Error", "Customer name is required!");
                return;
            }


            String nationalId = customer.getNationalId();
            if (nationalId == null || !nationalId.matches("\\d{10}")) {
                addErrorMessage("Validation Error", "National ID must be exactly 10 digits!");
                return;
            }


            boolean exists = customers.stream()
                    .anyMatch(c -> c.getNationalId().equals(nationalId));
            if (exists) {
                addErrorMessage("Validation Error", "The customer already exists!");
                return;
            }


            String email = customer.getEmail();
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                addErrorMessage("Validation Error", "Invalid email address!");
                return;
            }

            customerService.createCustomer(customer);
            addSuccessMessage("Success", "Customer created successfully!");
            customer = new Customer(); // Reset form
            loadCustomers();
        } catch (Exception e) {
            addErrorMessage("Error", "Failed to create customer: " + e.getMessage());
        }
    }

    public void searchCustomers() {
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {

            customers = allCustomers;
        } else {
            try {

                String keyword = searchKeyword.toLowerCase().trim();
                customers = allCustomers.stream()
                        .filter(c ->
                                (c.getName() != null && c.getName().toLowerCase().contains(keyword)) ||
                                        (c.getNationalId() != null && c.getNationalId().toLowerCase().contains(keyword))
                        )
                        .collect(Collectors.toList());



                if (customers.isEmpty()) {
                    addInfoMessage("Search Result", "No customers found matching your search criteria.");
                } else {
                    addInfoMessage("Search Result", "Found " + customers.size() + " customer(s) matching your search.");
                }
            } catch (Exception e) {
                addErrorMessage("Search Error", "Failed to search customers: " + e.getMessage());
                customers = allCustomers; // إذا فشل البحث، عرض جميع العملاء
            }
        }
    }

    public void clearSearch() {
        searchKeyword = null;
        customers = allCustomers;
        addInfoMessage("Search Cleared", "Showing all customers.");
    }

    public void deleteCustomer(Customer customer) {
        try {
            customerService.deleteCustomer(customer.getId());
            addSuccessMessage("Deleted", "Customer deleted successfully!");
            loadCustomers();
        } catch (Exception e) {
            addErrorMessage("Error", "Failed to delete customer: " + e.getMessage());
        }
    }



    private void addSuccessMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }


    private void addMessage(String summary, String detail) {
        addSuccessMessage(summary, detail);
    }

    // Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    // Getter للحصول على عدد العملاء (للاستخدام في الواجهة)
    public int getCustomersCount() {
        return customers != null ? customers.size() : 0;
    }
}