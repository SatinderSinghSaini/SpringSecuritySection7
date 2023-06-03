package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.AccountTransactions;
import com.learnspringsecurity.model.Customer;
import com.learnspringsecurity.repository.AccountTransactionsRepository;
import com.learnspringsecurity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    @Autowired
    private AccountTransactionsRepository accountTransactionsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "balance")
    public List<AccountTransactions> getBalance(@RequestParam String email){
        List<Customer> customers = customerRepository.findByEmail(email);
        if(customers.size() !=0){
            Customer customer = customers.get(0);
            List<AccountTransactions> accountTransactions = accountTransactionsRepository.findByCustomerIdOrderByTransactionDtDesc(customer.getId());
            if(accountTransactions != null){
                return accountTransactions;
            }else{
                return null;
            }
        }
        return null;
    }
}
