package com.software.controller;


import com.software.model.*;
import com.software.openbook.OpenbookApplication;
import com.software.service.AuthService;
import com.software.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UIController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @GetMapping("/user")
    public String userProfile(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";

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

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        List<Publication> publications = uiService.getAllPublications();

        model.addAttribute("publications", publications);


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

    @GetMapping("/editUser")
    public String editUser(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                Professor professor = (Professor) user;
                model.addAttribute("sessionUser",professor);
                return "ProfesorUI/editUser";
            case "student":
                Student student = (Student) user;
                model.addAttribute("sessionUser",student);
                return "StudentUI/editUser";
            case "curador":

                Curator curator = (Curator) user;
                model.addAttribute("sessionUser",curator);
                return "CuradorUI/editUser";
            default:
                return "redirect:/error";
        }

    }




    @GetMapping("/mochila")
    public String mochila(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";



        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        List<Publication> publications=  uiService.getUserMochila(user);

        model.addAttribute("publications", publications);

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

        if (email==null)
            return "redirect:/error";

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

    @GetMapping("/publication")
    public String getPublication(Model model, @RequestParam(required = false) Long id,  HttpSession session){

        String email = (String) session.getAttribute("EMAIL");

        Publication publication  =uiService.getPublicationById(id).get();
        if (email==null) {
            model.addAttribute("publication", publication);
            return "publication";
        }
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":

                model.addAttribute("publication", publication);
                return "ProfesorUI/publication";
            case "student":
                model.addAttribute("publication", publication);
                return "StudentUI/publication";

            default:
                return "redirect:/error";
        }


    }


    @GetMapping("/publicarContenido")
    public String publicarContenido(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");


        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/publicacionContenido";
            default:
                return "redirect:/error";
        }

    }





    @GetMapping("/publications")
    public String getMyPublications(Model model , HttpSession session){
        String email = (String) session.getAttribute("EMAIL");
        Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();


        if ("profesor".equals(tipo)) {
            List<Publication> publications = uiService.getPublicationsByProfessor((Professor) user);

            for (Publication p: publications
                 ) {
                log.info(p.getTitle());

            }
            model.addAttribute("publications", publications);
            return "ProfesorUI/publicaciones";
        }
        return "redirect:/error";

    }


    @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @GetMapping("/error")
    public String error(Model model){
        return "error";
    }


    @PostMapping("/saveInBackPack")
    public String saveInBackpack(Model model, @RequestParam(name = "p_id") Long p_id , HttpSession session){

        String email = (String) session.getAttribute("EMAIL");
        //Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        Publication publication = uiService.getPublicationById(p_id).get();

        uiService.saveInMochila(publication, user);
        return "redirect:/inicio";
    }
}
