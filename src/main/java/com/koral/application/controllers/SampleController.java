package com.koral.application.controllers;

import com.koral.application.model.PremiumStatus;
import com.koral.application.model.User;
import com.koral.application.repository.PremiumRepository;
import com.koral.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("premium")
@RestController
public class SampleController {

    @Autowired
    PremiumRepository premiumRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/all")
    public List<PremiumStatus> getpremium(){
        return premiumRepository.findAll();
    }

    @GetMapping(value = "update")
    public List<PremiumStatus> update(){

        User user = new User("Karol", "Komorniczak", "karol.komorniczak@gmail.com", "dupa");
        user.setPremiumStatus(new PremiumStatus("true", 15));
        userRepository.save(user);

        userRepository.findAll().get(0).setEmail("haha@gmail.com");

        userRepository.save(user);




        return premiumRepository.findAll();
    }
}
