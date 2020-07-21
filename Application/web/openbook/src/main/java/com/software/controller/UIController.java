package com.software.controller;


import com.software.model.*;
import com.software.openbook.OpenbookApplication;
import com.software.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UIController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private CategoryService catService;


    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

    @GetMapping("/user")
    public String userProfile(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");
        log.info(email);
        if (email==null)
            return "redirect:/error";


        Iterable<Category> categoryIterable = catService.getAllCategories();
        User user = authService.getUser(email).get();
        List <Publication> publications = new ArrayList<Publication>();
        String tipo = user.getTipo();
        List <Publication> propias = publicationService.getPublicationsFromProfessor(email);
        for (Publication publicacion:propias){
            publications.add(publicacion);
        }

        List <Publication> p_verified = new ArrayList<Publication>();
        List <Publication> p_process = new ArrayList<Publication>();
        List <Publication> p_notverified = new ArrayList<Publication>();
        List <Publication> p_rechazado = new ArrayList<Publication>();
        for (Publication publicacion:publications){
            switch (publicacion.getEstado()){
                case 0:
                    p_notverified.add(publicacion);
                    break;
                case 1:
                    p_process.add(publicacion);
                    break;
                case 2:
                    p_verified.add(publicacion);
                    break;
                case 3:
                    p_rechazado.add(publicacion);
                    break;
            }
        }

        switch (tipo){
            case "profesor":
                Professor professor = (Professor) user;
                model.addAttribute("sessionUser",professor);
                model.addAttribute( "publications",publications);
                model.addAttribute("p_notverified",p_notverified);
                model.addAttribute("p_process",p_process);
                model.addAttribute("p_verified",p_verified);
                model.addAttribute("p_rechazado",p_rechazado);
                model.addAttribute("categories", categoryIterable);

                return "ProfesorUI/user";
            case "student":
                Student student = (Student) user;
                model.addAttribute("sessionUser",student);
                model.addAttribute("categories", categoryIterable);
                return "StudentUI/user";
            case "curador":
                Curator curator = (Curator) user;
                model.addAttribute("sessionUser",curator);
                model.addAttribute("categories", categoryIterable);
                return "CuradorUI/user";
            case "admin":
                Admin admin = (Admin) user;
                model.addAttribute("sessionUser",admin);
                model.addAttribute("categories", categoryIterable);
                return "AdminUI/user";
            default:
                return "redirect:/error";
        }

    }

    @PostMapping("/saveToVerify")
    public String saveToVerify(Model model, @RequestParam(name = "p_id") Long p_id , HttpSession session, RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("EMAIL");
        //Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        Publication publication = uiService.getPublicationById(p_id).get();

        redirectAttributes.addAttribute("id", p_id);
        publicationService.savePublicationToCurate(p_id, email);
        return "redirect:/publication";
    }

    @PostMapping("/doVerify")
    public String doVerify(Model model, @RequestParam(name = "p_id") Long p_id , HttpSession session, RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("EMAIL");
        //Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        Publication publication = uiService.getPublicationById(p_id).get();

        redirectAttributes.addAttribute("id", p_id);
        publicationService.curatePublication(p_id, email);
        return "redirect:/publication";
    }

    @PostMapping("/doBlock")
    public String doBlock(Model model, @RequestParam(name = "p_id") Long p_id , HttpSession session, RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("EMAIL");
        //Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        Publication publication = uiService.getPublicationById(p_id).get();

        redirectAttributes.addAttribute("id", p_id);
        publicationService.blockPublication(p_id, email);
        return "redirect:/publication";
    }


    @RequestMapping("/searchContentCategory")
    public String searchContentByCategory(@RequestParam(required = false) int id, @RequestParam Map<String, Object> params, Model model, HttpSession session){
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if(messages == null){
            messages = new ArrayList<>();
        }

        model.addAttribute("sessionMessages", messages);

        Iterable<Category> categoryIterable = catService.getAllCategories();

        int page;
        Page<Publication> publications;
        if(params.get("page") == null) {
            page = 0;
            publications = publicationService.getPublicationsFromCategory(id, 0,20);
        } else {
            page = Integer.valueOf(params.get("page").toString())-1;
            publications = publicationService.getPublicationsFromCategory(id, page,20);
        }

        int totalPages = publications.getTotalPages();
        List<Integer> pages;

        if(totalPages == 1) {
            pages = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        } else if(totalPages == 2) {
            pages = IntStream.rangeClosed(1, 2).boxed().collect(Collectors.toList());
        } else {
            if (page == 0) {
                pages = IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList());
            } else if (page == totalPages - 1) {
                pages = IntStream.rangeClosed(totalPages - 2, totalPages).boxed().collect(Collectors.toList());
            } else {
                pages = IntStream.rangeClosed(page, page + 2).boxed().collect(Collectors.toList());
            }
        }


        String email = (String) session.getAttribute("EMAIL");

        if (email==null) {
            model.addAttribute("publications", publications);
            model.addAttribute("categories", categoryIterable);
            model.addAttribute("pages", pages);
            model.addAttribute("current", page+1);
            model.addAttribute("next", page+2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPages);
            model.addAttribute("id", id);
            return "searchCategory";
        }

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo) {
            case "profesor":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("id", id);
                return "ProfesorUI/searchCategory";
            case "student":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("id", id);
                return "StudentUI/searchCategory";
            case "curador":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("id", id);
                return "CuradorUI/searchCategory";
            case "admin":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("id", id);
                return "AdminUI/searchCategory";
            default:
                return "redirect:/error";
        }
    }


    @RequestMapping("/searchContent")
    public String searchContent(@RequestParam Map<String, Object> params, Model model, HttpSession session){
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        Iterable<Category> categoryIterable = catService.getAllCategories();

        if(messages == null){
            messages = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", messages);

        String keyword = (String) params.get("search");

        int page;
        Page<Publication> publications;
        if(params.get("page") == null) {
            page = 0;
            publications = publicationService.findPublicationByKeywords(keyword, PageRequest.of(0,20));
        } else {
            page = Integer.valueOf(params.get("page").toString())-1;
            publications = publicationService.findPublicationByKeywords(keyword, PageRequest.of(page,20));
        }

        int totalPages = publications.getTotalPages();
        List<Integer> pages;

        if(totalPages == 1) {
            pages = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        } else if(totalPages == 2) {
            pages = IntStream.rangeClosed(1, 2).boxed().collect(Collectors.toList());
        } else {
            if (page == 0) {
                pages = IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList());
            } else if (page == totalPages - 1) {
                pages = IntStream.rangeClosed(totalPages - 2, totalPages).boxed().collect(Collectors.toList());
            } else {
                pages = IntStream.rangeClosed(page, page + 2).boxed().collect(Collectors.toList());
            }
        }


        String email = (String) session.getAttribute("EMAIL");

        if (email==null) {
            model.addAttribute("publications", publications);
            model.addAttribute("categories", categoryIterable);
            model.addAttribute("pages", pages);
            model.addAttribute("current", page+1);
            model.addAttribute("next", page+2);
            model.addAttribute("prev", page);
            model.addAttribute("last", totalPages);
            model.addAttribute("keyword", keyword);
            return "search";
        }

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo) {
            case "profesor":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("keyword", keyword);
                return "ProfesorUI/search";
            case "student":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("keyword", keyword);
                return "StudentUI/search";
            case "curador":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("keyword", keyword);
                return "CuradorUI/search";
            case "admin":
                model.addAttribute("publications", publications);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("pages", pages);
                model.addAttribute("current", page+1);
                model.addAttribute("next", page+2);
                model.addAttribute("prev", page);
                model.addAttribute("last", totalPages);
                model.addAttribute("keyword", keyword);
                return "AdminUI/search";
            default:
                return "redirect:/error";
        }
    }

    @GetMapping("/revisionsToDo")
    public String revisionToDo(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        log.info(email);

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        List<Publication> publications = publicationService.getPublicationsToVerifyByCurator(email);

        model.addAttribute("publications", publications);

        switch (tipo){
            case "curador":
                return "CuradorUI/revisionsToDo";
            default:
                return "redirect:/error";
        }
    }


    @GetMapping("/getRevisions")
    public String getRevisions(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        log.info(email);

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        List<Publication> publications = publicationService.getPublicationsVerifiedByCurator(email);

        model.addAttribute("publications", publications);

        switch (tipo){
            case "curador":
                return "CuradorUI/revisions";
            default:
                return "redirect:/error";
        }
    }


    @GetMapping("/inicio")
    public String inicio(@RequestParam Map<String, Object> params, Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");

        log.info(email);

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        String tipo = user.getTipo();

        Iterable<Category> categoryIterable = catService.getAllCategories();

        int page;
        Page<Publication> publications;
        if(params.get("page") == null) {
            page = 0;
            publications = publicationService.getTopNPublications(0,20);
        } else {
            page = Integer.valueOf(params.get("page").toString())-1;
            publications = publicationService.getTopNPublications(Integer.valueOf(params.get("page").toString())-1,20);
        }
        Page<Publication> publicationsCarousel_0 = publicationService.getTopNPublications(0,3);
        Page<Publication> publicationsCarousel_1 = publicationService.getTopNPublications(1,3);
        int totalPages = publications.getTotalPages();
        List<Integer> pages;

        if(totalPages == 1) {
            pages = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        } else if(totalPages == 2) {
            pages = IntStream.rangeClosed(1, 2).boxed().collect(Collectors.toList());
        } else {
            if (page == 0) {
                pages = IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList());
            } else if (page == totalPages - 1) {
                pages = IntStream.rangeClosed(totalPages - 2, totalPages).boxed().collect(Collectors.toList());
            } else {
                pages = IntStream.rangeClosed(page, page + 2).boxed().collect(Collectors.toList());
            }
        }
        model.addAttribute("publications", publications);
        model.addAttribute("categories", categoryIterable);
        model.addAttribute("publicationsCarousel_0", publicationsCarousel_0);
        model.addAttribute("publicationsCarousel_1", publicationsCarousel_1);
        model.addAttribute("pages", pages);
        model.addAttribute("current", page+1);
        model.addAttribute("next", page+2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPages);

        switch (tipo){
            case "profesor":
                return "ProfesorUI/inicio";
            case "student":
                return "StudentUI/inicio";
            case "curador":
                return "CuradorUI/inicio";
            case "admin":
                return "AdminUI/inicio";
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


        Iterable<Category> categoryIterable = catService.getAllCategories();

        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                Professor professor = (Professor) user;
                model.addAttribute("sessionUser",professor);
                model.addAttribute("categories", categoryIterable);
                return "ProfesorUI/editUser";
            case "student":
                Student student = (Student) user;
                model.addAttribute("sessionUser",student);
                model.addAttribute("categories", categoryIterable);
                return "StudentUI/editUser";
            case "curador":
                Curator curator = (Curator) user;
                model.addAttribute("sessionUser",curator);
                model.addAttribute("categories", categoryIterable);
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


        Iterable<Category> categoryIterable = catService.getAllCategories();

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        List<Publication> publications=  uiService.getUserMochila(user);

        model.addAttribute("publications", publications);

        switch (tipo){
            case "profesor":
                model.addAttribute("categories", categoryIterable);
                return "ProfesorUI/mochila";
            case "student":
                model.addAttribute("categories", categoryIterable);
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

        Iterable<Category> categoryIterable = catService.getAllCategories();

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

        Iterable<Category> categoryIterable = catService.getAllCategories();

        List<Comment> comments_to_filter = commentService.getCommentsPublication(id);
        List<Comment> comments = new ArrayList<Comment>();

        for (Comment c: comments_to_filter) {
            if(c.getParentComment() == null){
                comments.add(c);
            }
        }

        List<Likes> likes = publicationService.getLikesFromPublication(id);
        String class_heart;
        String heart;

        if(publicationService.getLikeFromPublicationAndUser(id,email).size() == 0){
            class_heart = "bi bi-heart";
            heart = "M8 2.748l-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z";
        } else {
            class_heart = "bi bi-heart-fill";
            heart = "M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z";
        }

        Publication publication  = uiService.getPublicationById(id).get();
        long visitstmp = publication.getVisits();
        visitstmp = visitstmp + 1;
        publication.setVisits(visitstmp);
        float ranking = publication.getRanking();
        ranking = (float) (ranking + 0.05);
        publication.setRanking(ranking);
        publicationService.updatePublication(publication);

        if (email==null) {
            model.addAttribute("comments", comments);
            model.addAttribute("publication", publication);
            model.addAttribute("categories", categoryIterable);
            model.addAttribute("likes", likes);
            model.addAttribute("class_heart", class_heart);
            model.addAttribute("heart", heart);
            return "publication";
        }
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();


        switch (tipo){
            case "profesor":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("likes", likes);
                model.addAttribute("class_heart", class_heart);
                model.addAttribute("heart", heart);
                return "ProfesorUI/publication";
            case "student":
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("likes", likes);
                model.addAttribute("class_heart", class_heart);
                model.addAttribute("heart", heart);
                return "StudentUI/publication";

            case "curador":

                Curator curator = (Curator) user;
                model.addAttribute("sessionUser",curator);
                model.addAttribute("comments", comments);
                model.addAttribute("publication", publication);
                model.addAttribute("categories", categoryIterable);
                model.addAttribute("likes", likes);
                return "CuradorUI/publication";
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

        List<Category> categories = new ArrayList<Category>();
        Iterable<Category> categoryIterable = catService.getAllCategories();
        for(Category category: categoryIterable){
            categories.add(category);
        }
        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                model.addAttribute("categories", categories);
                model.addAttribute("categories", categoryIterable);
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

        Iterable<Category> categoryIterable = catService.getAllCategories();

        if ("profesor".equals(tipo)) {
            List<Publication> publications = uiService.getPublicationsByProfessor((Professor) user);

            for (Publication p: publications
                 ) {
                log.info(p.getTitle());

            }
            model.addAttribute("publications", publications);
            model.addAttribute("categories", categoryIterable);
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

    @GetMapping("/login_error")
    public String login_error(Model model){
        return "login_error";
    }

    @GetMapping("/register_error")
    public String register_error(Model model){
        return "register_error";
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

    @PostMapping(value = "/addLike")
    public String addLike(Model model, @RequestParam(name = "p_id") Long p_id , HttpSession session){

        String email = (String) session.getAttribute("EMAIL");
        User user = authService.getUser(email).get();
        String tipo = user.getTipo();

        System.out.println(email);
        System.out.println(p_id);

        publicationService.likePublication(p_id, email);
        String redirect = "redirect:/publication?id="+p_id;
        return redirect;
    }

    @RequestMapping("/addUserAdmin")
    public String addUserAdmin(Model model, HttpSession session){
        String email = (String) session.getAttribute("EMAIL");
        log.info(email);

        if (email==null)
            return "redirect:/error";

        User user = authService.getUser(email).get();

        Iterable<Category> categoryIterable = catService.getAllCategories();

        String tipo = user.getTipo();
        switch (tipo){
            case "admin":
                model.addAttribute("categories", categoryIterable);
                return "AdminUI/añadirUsuario";
            default:
                return "redirect:/error";
        }
    }

    @PostMapping(value = "/do_register_curator")
    public String do_register_profesor(@ModelAttribute Curator curator, RedirectAttributes redirectAttributes){
        if(authService.registerUser(curator)) {
            redirectAttributes
                    .addFlashAttribute("mensaje", "Registro Exitoso: El curador se ha añadido")
                    .addFlashAttribute("clase", "success");
            return "redirect:/addUserAdmin";
        }
        redirectAttributes
                .addFlashAttribute("mensaje", "Error: El usuario ya existe")
                .addFlashAttribute("clase", "danger");
        return "redirect:/addUserAdmin";
    }
}
