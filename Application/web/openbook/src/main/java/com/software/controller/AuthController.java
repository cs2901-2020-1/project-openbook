package com.software.controller;

import com.software.model.Professor;
import com.software.model.Publication;
import com.software.model.Student;
import com.software.model.User;
import com.software.service.AuthService;
import com.software.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @RequestMapping("/login")
    public String login(Model model){
        //Return the login page
        return "login";
    }



    @PostMapping(value = "/do_login")
    public String do_login(@ModelAttribute User user, HttpServletRequest request){

        boolean isUser= authService.verifyUser(user);
        if(!isUser) {
            return "redirect:/error";
        }
        String tipo = authService.getUser(user.getEmail()).get().getTipo();

        request.getSession().setAttribute("EMAIL", user.getEmail());

        return "redirect:/inicio";

        //verify the tipo atribute of user
    }



    @RequestMapping("/register")
    public String register(Model model){
        return "register";
    }


    @PostMapping(value = "/do_register_student")
    public String do_register_student(@ModelAttribute Student student){
        // TO DO

        if(authService.registerUser(student))
            return "index";
        return "error";
    }

    @PostMapping(value = "/do_register_profesor")
    public String do_register_profesor(@ModelAttribute Professor professor, RedirectAttributes redirectAttributes){
        // TO DO
        if(authService.registerUser(professor)) {
            redirectAttributes
                    .addFlashAttribute("mensaje", "Registrado Correctamente")
                    .addFlashAttribute("clase", "success");
            return "redirect:/login";
        }
        return "error";
    }

    @PostMapping(value = "/do_register_curador")
    public String do_register_curador(@ModelAttribute User user){
        // TO DO
        if(authService.registerUser(user))
            return "redirect:/login";
        return "error";
    }




    @GetMapping("/")
    public String process(Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", messages);

        List<Publication> publications = uiService.getAllPublications();

        model.addAttribute("publications", publications);
        return "index";
    }

    @PostMapping("/persistMessage")
    public String persistMessage(@RequestParam("msg") String msg, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
        if (messages == null) {
            messages = new ArrayList<>();
            request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
        }
        messages.add(msg);
        request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
        return "redirect:/";
    }



}
