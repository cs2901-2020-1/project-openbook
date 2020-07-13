package com.software.controller;


import com.software.model.*;
import com.software.openbook.OpenbookApplication;
import com.software.service.AuthService;
import com.software.service.CommentService;
import com.software.service.PublicationService;
import com.software.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UIController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PublicationService publiService;

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
        Page<Publication> publicationsCarousel_0 = publiService.getLastNPublications(0,3);
        Page<Publication> publicationsCarousel_1 = publiService.getLastNPublications(1,3);

        model.addAttribute("publications", publications);
        model.addAttribute("publicationsCarousel_0", publicationsCarousel_0);
        model.addAttribute("publicationsCarousel_1", publicationsCarousel_1);


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

        List<Comment> comments_to_filter = commentService.getCommentsPublication(id);

        List<Comment> comments = new ArrayList<Comment>();

        for (Comment c: comments_to_filter) {
            if(c.getParentComment() == null){
                comments.add(c);
            }
        }

        Publication publication  =uiService.getPublicationById(id).get();
        if (email==null) {
            model.addAttribute("comments", comments);
            model.addAttribute("publication", publication);
            return "publication";
        }
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();




        switch (tipo){
            case "profesor":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "ProfesorUI/publication";
            case "student":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "StudentUI/publication";

            default:
                return "redirect:/error";
        }



    }

    @GetMapping("/dashboardui")
    public String dashboard(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                return "ProfesorUI/dashboard";

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


    @PostMapping("/postComment")
    public String postComment(@ModelAttribute commentFile commentFile, Model model, HttpSession session){

        Comment comment = new Comment();

        Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

        String email = (String) session.getAttribute("EMAIL");

        Long id_pub = commentFile.getId_pub();

        Publication publication  =uiService.getPublicationById(id_pub).get();

        if (email==null) {
            // alerta de que no está logueado
            model.addAttribute("publication", publication);
            return "publication";
        }


        User user_author = authService.getUser(email).get();



        comment.setText_comment(commentFile.getText_comment());
        comment.setCommentAuthor(user_author);
        comment.setPublication(new Publication());

        commentService.addCommentToPublication(id_pub, comment);



        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        List<Comment> comments_to_filter = commentService.getCommentsPublication(id_pub);

        List<Comment> comments = new ArrayList<Comment>();

        for (Comment c: comments_to_filter) {
            if(c.getParentComment() == null){
                comments.add(c);
            }
        }

        switch (tipo){
            case "profesor":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "ProfesorUI/publication";
            case "student":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "StudentUI/publication";

            default:
                return "redirect:/error";
        }

    }


    @PostMapping("/replyComment")
    public String replyComment(@ModelAttribute commentFile commentFile, Model model, HttpSession session){

        Long id_comment_to = commentFile.getId_comment_to();

        Comment comment = new Comment();
        comment.setText_comment(commentFile.getText_comment());

        //se necesita el id del comentario y el id del comentario a responder

        String email = (String) session.getAttribute("EMAIL");
        User user = authService.getUser(email).get();
        comment.setCommentAuthor(user);
        commentService.addCommentToComment(id_comment_to, comment);


        String tipo = user.getTipo();

        Long id_pub = commentFile.getId_pub();

        Publication publication  =uiService.getPublicationById(id_pub).get();

        List<Comment> comments_to_filter = commentService.getCommentsPublication(id_pub);

        List<Comment> comments = new ArrayList<Comment>();

        for (Comment c: comments_to_filter) {
            if(c.getParentComment() == null){
                comments.add(c);
            }
        }



        switch (tipo){
            case "profesor":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "ProfesorUI/publication";
            case "student":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                return "StudentUI/publication";

            default:
                return "redirect:/error";
        }


    }

    @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
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
