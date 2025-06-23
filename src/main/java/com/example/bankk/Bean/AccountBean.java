package com.example.bankk.Bean;

import com.example.bankk.Service.BankService;
import com.example.bankk.Service.TransactionService;
import com.example.bankk.model.Account;
import com.example.bankk.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named("accountBean")
@ViewScoped
@Component
public class AccountBean implements Serializable {

    @Autowired
    private BankService bankService;

    @Autowired
    private TransactionService transactionService;

    private Account account = new Account();
    private List<Account> accounts;
    private List<Customer> customers;

    // For operations
    private String operationAccountNumber;
    private BigDecimal operationAmount;

    // For transfer
    private Long senderAccountId;
    private Long receiverAccountId;
    private BigDecimal transferAmount;
    private String transferDescription;

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        accounts = bankService.getAllAccounts();
        customers = bankService.getAllCustomers();
    }

    public void createAccount() {
        try {
            if (account.getCustomer() == null || account.getCustomer().getId() == null) {
                addMessage("Error", "Please select a customer.");
                return;
            }
            bankService.addAccount(account);
            addMessage("Success", "Account created successfully!");
            account = new Account();
            account.setCustomer(new Customer());
            loadData();
        } catch (Exception e) {
            addMessage("Error", "Failed to create account: " + e.getMessage());
        }
    }
    public void deposit() {
        try {
            bankService.deposit(operationAccountNumber, operationAmount);
            addMessage("Success", "Deposit successful!");
            clearOperationFields();
            loadData();
        } catch (Exception e) {
            addMessage("Error", "Deposit failed: " + e.getMessage());
        }
    }

    public void withdraw() {
        try {
            bankService.withdraw(operationAccountNumber, operationAmount);
            addMessage("Success", "Withdrawal successful!");
            clearOperationFields();
            loadData();
        } catch (Exception e) {
            addMessage("Error", "Withdrawal failed: " + e.getMessage());
        }
    }

    public void transfer() {
        try {
            transactionService.transfer(senderAccountId, receiverAccountId, transferAmount, transferDescription);
            addMessage("Success", "Transfer successful!");
            clearTransferFields();
            loadData();
        } catch (Exception e) {
            addMessage("Error", "Transfer failed: " + e.getMessage());
        }
    }

    private void clearOperationFields() {
        operationAccountNumber = null;
        operationAmount = null;
    }

    private void clearTransferFields() {
        senderAccountId = null;
        receiverAccountId = null;
        transferAmount = null;
        transferDescription = null;
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    // Getters and Setters
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public String getOperationAccountNumber() {
        return operationAccountNumber;
    }

    public void setOperationAccountNumber(String operationAccountNumber) {
        this.operationAccountNumber = operationAccountNumber;
    }

    public BigDecimal getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(BigDecimal operationAmount) {
        this.operationAmount = operationAmount;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferDescription() {
        return transferDescription;
    }

    public void setTransferDescription(String transferDescription) {
        this.transferDescription = transferDescription;
    }
}