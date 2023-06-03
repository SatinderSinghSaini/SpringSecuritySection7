package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.Accounts;
import com.learnspringsecurity.model.Customer;
import com.learnspringsecurity.repository.AccountsRepository;
import com.learnspringsecurity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping(path = "account")
    public Accounts getAccount(@RequestParam String email){
        List<Customer> customers = customerRepository.findByEmail(email);
        if(customers.size() !=0){
            Customer customer = customers.get(0);
            Accounts accounts = accountsRepository.findByCustomerId(customer.getId());

            if(accounts != null){
                return accounts;
            }else{
                return null;
            }
        }
        return null;

    }
}
