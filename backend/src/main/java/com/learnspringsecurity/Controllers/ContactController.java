package com.learnspringsecurity.Controllers;

import com.learnspringsecurity.model.Contact;
import com.learnspringsecurity.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;
    @PostMapping(path = "contact")
    @PostFilter("filterObject.contactName!='test'")
    public List<Contact> saveContact(@RequestBody Contact contact){
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        Contact contactInDb = contactRepository.save(contact);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contactInDb);
        return contacts;
    }

    private String getServiceReqNumber(){
        Random random = new Random();
        int randNumber = random.nextInt(999999999-9999)+9999;
        return "SR"+randNumber;
    }
}
