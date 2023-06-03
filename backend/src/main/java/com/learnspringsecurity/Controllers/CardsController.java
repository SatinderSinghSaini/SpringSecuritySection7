package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.Cards;
import com.learnspringsecurity.model.Customer;
import com.learnspringsecurity.repository.CardsRepository;
import com.learnspringsecurity.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {
    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping(path = "cards")
    public List<Cards> getCards(@RequestParam String email){
        List<Customer> customers = customerRepository.findByEmail(email);
        if(customers.size() !=0){
            Customer customer = customers.get(0);
            List<Cards> cards = cardsRepository.findByCustomerId(customer.getId());
            if(cards != null){
                return cards;
            }else{
                return null;
            }
        }
        return null;
    }
}
