package com.philosup.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepo;

    @GetMapping
    public String getAccounts(){
        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // return gson.toJson(accountRepo.findAll());
        return accountRepo.findAll().toString();
    }

    @GetMapping("/insert")
    public String insertAccounts(@RequestParam("id") String id, @RequestParam("name") String name, @RequestParam("email") String email){
        Account newAccount = new Account();
        newAccount.setId(id);
        newAccount.setUsername(name);
        newAccount.setEmail(email);
        accountRepo.insert(newAccount);
        return "insert : " + newAccount.toString();
    }
}
