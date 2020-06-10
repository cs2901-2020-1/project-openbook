package com.software.controller;

import com.software.model.User;
import com.software.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/login")
    public String login(){
        //Return the login page
        return "login.html";
    }

    @PostMapping(value = "/do_login", consumes = "application/json")
    public String do_login(@RequestBody User user){

        boolean isUser= authService.verifyUser(user);
        if(!isUser)
            return "user is not registered";
        Optional<User> u;
        u = authService.getUser(user.getEmail());

        //verify the tipo atribute of user

        return "login page";
    }



    @RequestMapping("/register")
    public String register(){
        // TO DO
        //Return the register page
        return "Registration page";
    }


    @PostMapping(value = "/do_register", consumes = "application/json")
    public String do_register(@RequestBody User user){
        // TO DO
        if(authService.registerUser(user))
            return "User registered";
        return "This email is used by other user";

    }

}
