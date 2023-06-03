package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.Customer;
import com.learnspringsecurity.repository.CustomerRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class Register {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/user")
    public Customer getCustomerAfterLogin(Authentication authentication){
        List<Customer> customers = customerRepository.findByEmail(authentication.getName());
        if(customers.size()>0){
            return customers.get(0);
        }else{
            return null;
        }
    }
}
