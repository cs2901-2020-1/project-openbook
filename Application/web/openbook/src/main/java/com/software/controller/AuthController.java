package com.software.controller;

import com.software.model.User;
import com.software.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/login")
    public String login(Model model){
        //Return the login page
        return "login";
    }


    @GetMapping("/student")
    public String student(Model model){
        return "student";
    }

    @GetMapping("/profesor")
    public String profesor(Model model){
        return "profesor";
    }
    @GetMapping("/curador")
    public String curador(Model model){
        return "curador";
    }

    @PostMapping(value = "/do_login")
    public String do_login(@ModelAttribute User user){

        boolean isUser= authService.verifyUser(user);
        if(!isUser) {
            return "redirect:/error";
        }
        String tipo = authService.getUser(user.getEmail()).get().getTipo();

        switch (tipo){
            case "profesor":
                return "redirect:/profesor";
            case "student":
                return "redirect:/student";
            case "curador":
                return "redirect:/curador";
            default:
                return "redirect:/error";
        }
        //verify the tipo atribute of user
    }



    @RequestMapping("/register")
    public String register(){
        // TO DO
        //Return the register page
        return "Registration page";
    }


    @PostMapping(value = "/do_register")
    public String do_register(@ModelAttribute User user){
        // TO DO
        if(authService.registerUser(user))
            return "User Register";
        return "This email is used by other user";

    }

}
