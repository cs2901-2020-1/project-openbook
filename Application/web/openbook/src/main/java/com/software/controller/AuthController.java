package com.software.controller;

import com.software.model.*;
import com.software.openbook.OpenbookApplication;
import com.software.service.AuthService;
import com.software.service.PublicationService;
import com.software.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AuthController {

    public String FILE_PATH = "src/main/resources/files/";

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @Autowired
    private PublicationService publiService;

    @RequestMapping("/login")
    public String login(Model model){
        //Return the login page
        return "login";
    }


    @RequestMapping("/dashboard")
    public String dashboard(Model model){
        //Return the login page
        return "StudentUI/dashboard";
    }


    @PostMapping(value = "/do_login")
    public String do_login(@ModelAttribute User user, HttpServletRequest request){

        boolean isUser= authService.verifyUser(user);
        if(!isUser) {
            return "redirect:/login_error";
        }
        String tipo = authService.getUser(user.getEmail()).get().getTipo();

        request.getSession().setAttribute("EMAIL", user.getEmail());

        log.info(tipo);
        return "redirect:/inicio";

        //verify the tipo atribute of user
    }



    @RequestMapping("/register")
    public String register(Model model){
        return "register";
    }


    @PostMapping(value = "/do_register_student")
    public String do_register_student(@ModelAttribute Student student,  RedirectAttributes redirectAttributes){
        // TO DO

        if(authService.registerUser(student)){
            redirectAttributes
                    .addFlashAttribute("mensaje", "¡Registro Exitoso¡ Ya puedes acceder a nuestra plataforma")
                    .addFlashAttribute("clase", "success");
            return "redirect:/login";
        }

        return "error";
    }

    @PostMapping(value = "/do_register_profesor")
    public String do_register_profesor(@ModelAttribute Professor professor, RedirectAttributes redirectAttributes){
        // TO DO

        professor.setPhotoPath("src/main/resources/static/images/picture_default.jpg");
        if(authService.registerUser(professor)) {
            redirectAttributes
                    .addFlashAttribute("mensaje", "Registro Exitoso: Ya puedes acceder a nuestra plataforma")
                    .addFlashAttribute("clase", "success");
            return "redirect:/login";
        }
        return "error";
    }

    @PostMapping(value = "/updateProfesor")
    public String updateProfesor(@ModelAttribute Professor professor, RedirectAttributes redirectAttributes,  HttpSession session){
        // TO DO
        String email = (String) session.getAttribute("EMAIL");
        professor.setEmail(email);

        if(authService.updateUser(professor)) {
            redirectAttributes
                    .addFlashAttribute("mensaje", "Actualización Exitosa: Sus datos se actualizaron correctamente")
                    .addFlashAttribute("clase", "success");
            return "redirect:/user";
        }
        return "error";
    }

    @PostMapping(value = "/updateStudent")
    public String updateProfesor(@ModelAttribute Student student, RedirectAttributes redirectAttributes,  HttpSession session){
        // TO DO
        String email = (String) session.getAttribute("EMAIL");
        student.setEmail(email);

        if(authService.updateUser(student)) {
            redirectAttributes
                    .addFlashAttribute("mensaje", "Actualización Exitosa: Sus datos se actualizaron correctamente")
                    .addFlashAttribute("clase", "success");
            return "redirect:/user";
        }
        return "error";
    }

    @PostMapping(value = "/updateDescription")
    public String updateProfesorDescription(@ModelAttribute prof professor, RedirectAttributes redirectAttributes, HttpSession session, Model model){
        // TO DO
        String email = (String) session.getAttribute("EMAIL");
        Logger log = LoggerFactory.getLogger(OpenbookApplication.class);


        Professor updateprofessor = new Professor();
        updateprofessor.setEmail(email);
        updateprofessor.setDescription(professor.getDescription());
        log.info(professor.getDescription());

        //Guardar la foto
        MultipartFile image_file = professor.getPhotoFile();


        log.info("Actualizando");
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image_file.getOriginalFilename()));
        if (!fileName.equals("")) {
            String resourcePath = FILE_PATH + fileName;

            log.info(resourcePath);

            updateprofessor.setPhotoPath(resourcePath);

            Path path = Paths.get(FILE_PATH + fileName);
            try {
                Files.copy(image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            updateprofessor.setPhotoPath("");
        }

        if(authService.updateProfesorDescription(updateprofessor)) {
            User user = authService.getUser(email).get();

            model.addAttribute("sessionUser",(Professor) user);
            redirectAttributes
                    .addFlashAttribute("mensaje", "Actualización Exitosa: Sus datos se actualizaron correctamente")
                    .addFlashAttribute("clase", "success");
            return "redirect:/user";
        }
        return "error";
    }



    @PostMapping(value = "/do_register_curador")
    public String do_register_curador(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        // TO DO
        if(authService.registerUser(user)){
            redirectAttributes
                    .addFlashAttribute("mensaje", "<strong>Registro Exitoso</strong> Ya puedes acceder a nuestra plataforma")
                    .addFlashAttribute("clase", "success");
            return "redirect:/login";
        }
        return "error";
    }

    @GetMapping("/")
    public String process(@RequestParam Map<String, Object> params, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", messages);

        int page;
        Page<Publication> publications;
        if(params.get("page") == null) {
            page = 0;
            publications = publiService.getLastNPublications(0,20);
        } else {
            page = Integer.valueOf(params.get("page").toString())-1;
            publications = publiService.getLastNPublications(Integer.valueOf(params.get("page").toString())-1,20);
        }
        Page<Publication> publicationsCarousel_0 = publiService.getLastNPublications(0,3);
        Page<Publication> publicationsCarousel_1 = publiService.getLastNPublications(1,3);
        int totalPages = publications.getTotalPages();
        List<Integer> pages;

        if(totalPages == 1) {
            pages = IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        } else if(totalPages == 2) {
            pages = IntStream.rangeClosed(1, 2).boxed().collect(Collectors.toList());
        } else {
            if(page == 0) {
                pages = IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList());
            } else if(page == totalPages-1) {
                pages = IntStream.rangeClosed(totalPages-2, totalPages).boxed().collect(Collectors.toList());
            } else {
                pages = IntStream.rangeClosed(page, page+2).boxed().collect(Collectors.toList());
            }
        }
        model.addAttribute("publications", publications);
        model.addAttribute("publicationsCarousel_0", publicationsCarousel_0);
        model.addAttribute("publicationsCarousel_1", publicationsCarousel_1);
        model.addAttribute("pages", pages);
        model.addAttribute("current", page+1);
        model.addAttribute("next", page+2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPages);
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
