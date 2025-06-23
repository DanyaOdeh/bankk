package com.example.bankk.Converter;

import com.example.bankk.Repository.CustomerRepository;
import com.example.bankk.model.Customer;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FacesConverter(value = "customerConverter", managed = true)
@Component
public class CustomerConverter implements Converter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return customerRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Customer customer) {
        if (customer == null) {
            return "";
        }
        return customer.getId().toString();
    }
}