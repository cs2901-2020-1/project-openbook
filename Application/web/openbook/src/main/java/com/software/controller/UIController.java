package com.software.controller;


import com.software.model.Curator;
import com.software.model.Professor;
import com.software.model.Student;
import com.software.model.User;
import com.software.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UIController {

    @Autowired
    private AuthService authService;

    @GetMapping("/user")
    public String userProfile(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");
        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                Professor professor = (Professor) user;
                model.addAttribute("sessionUser",professor);
                return "ProfesorUI/user";
            case "student":
                Student student = (Student) user;
                model.addAttribute("sessionUser",student);
                return "StudentUI/user";
            case "curador":

                Curator curator = (Curator) user;
                model.addAttribute("sessionUser",curator);
                return "CuradorUI/user";
            default:
                return "redirect:/error";
        }


    }



    @GetMapping("/inicio")
    public String inicio(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/inicio";
            case "student":
                return "StudentUI/inicio";
            case "curador":
                return "redirect:/curador";
            default:
                return "redirect:/error";
        }
    }


    @GetMapping("/mochila")
    public String mochila(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/mochila";
            case "student":
                return "StudentUI/mochila";
            default:
                return "redirect:/error";
        }

    }

    @GetMapping("/notifications")
    public String notificaciones(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/notifications";
            case "student":
                return "StudentUI/notifications";
            default:
                return "redirect:/error";
        }

    }


    @GetMapping("/publicarContenido")
    public String publicarContenido(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/publicacion";
            default:
                return "redirect:/error";
        }

    }

    @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }




}
