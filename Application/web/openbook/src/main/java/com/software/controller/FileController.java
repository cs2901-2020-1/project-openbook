package com.software.controller;


import com.software.model.Professor;
import com.software.model.Publication;
import com.software.model.User;
import com.software.model.pub;
import com.software.openbook.OpenbookApplication;
import com.software.service.AuthService;
import com.software.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class FileController {

    public String FILE_PATH = "src/main/resources/files/";

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;




    @GetMapping(value = "/getPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> download(String param, @RequestParam(required = true) Long id) throws IOException {

        Publication publication  =uiService.getPublicationById(id).get();

        String pdf_path = publication.getResource_path();

        Path path = Paths.get(pdf_path);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE )
    public ResponseEntity<Resource> download_image(String param, @RequestParam(required = true) Long id) throws IOException {

        Publication publication  =uiService.getPublicationById(id).get();

        String image_path = publication.getImage_path();

        Path path = Paths.get(image_path);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping(value = "/getImageProfesor", produces = MediaType.IMAGE_JPEG_VALUE )
    public ResponseEntity<Resource> download_image_professor(String param, @RequestParam(required = true) String id ) throws IOException {

        Professor user  = (Professor) authService.getUser(id).get();

        String image_path = user.getPhotoPath();

        Path path = Paths.get(image_path);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }



    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFileFromLocal() {
        Path path = Paths.get("src/main/resources/static/test.pdf");

        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert resource != null;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(path.getFileName().toString()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/uploadPublication")
    public String uploadToLocalFileSystem(@ModelAttribute pub pub, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {

        Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

        MultipartFile file = pub.getFile();

        MultipartFile image_file = pub.getImage_file();

        String title = pub.getTitle();
        String description = pub.getDescription();
        int rank = 0;

        log.info(pub.getTitle());
        log.info(pub.getDescription());
        log.info(pub.getCategory());


        Publication publication = new Publication();

        publication.setTitle(title);
        publication.setDescription(description);
        publication.setRanking(rank);

        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";

        Professor p = (Professor) authService.getUser(email).get();

        publication.setProfessor( p);
        log.info(p.getEmail());


        // Guardar el archivo
        // pdf
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        String resourcePath = FILE_PATH + fileName;

        publication.setResource_path(resourcePath);


        Path path = Paths.get(FILE_PATH + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar el archivo
        // imagen

        fileName = StringUtils.cleanPath(Objects.requireNonNull(image_file.getOriginalFilename()));

        resourcePath = FILE_PATH + fileName;

        publication.setImage_path(resourcePath);

        uiService.postPublication(publication);

        path = Paths.get(FILE_PATH + fileName);
        try {
            Files.copy(image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        redirectAttributes
                .addFlashAttribute("mensaje", "¡Publicación Exitosa! El contenido acaba de publicarse")
                .addFlashAttribute("clase", "success");

        return "redirect:/publicarContenido";
    }

}