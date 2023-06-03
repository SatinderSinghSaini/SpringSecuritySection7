package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.Customer;
import com.learnspringsecurity.model.Loans;
import com.learnspringsecurity.repository.CustomerRepository;
import com.learnspringsecurity.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(path = "loans")
    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoans(@RequestParam String email){
        List<Customer> customers = customerRepository.findByEmail(email);
        if(customers.size() !=0){
            Customer customer = customers.get(0);
            List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getId());
            if(loans != null){
                return loans;
            }else{
                return null;
            }
        }
        return null;
    }
}
