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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthController {

    public String FILE_PATH = "src/main/resources/files/";


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
