package com.example.bankk.Bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("accountAuthBean")
@ViewScoped
public class AccountAuthBean implements Serializable {
    private String password;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String checkPassword() {
        if ("danya".equals(password)) { // غيّر كلمة السر كما تريد
            return "accountManagement.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Wrong password!"));
            return null;
        }
    }
}