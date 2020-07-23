package com.software.controller;


import com.software.model.*;
import com.software.openbook.OpenbookApplication;
import com.software.service.AuthService;
import com.software.service.CategoryService;
import com.software.service.PublicationService;
import com.software.service.UIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileController {

    public String FILE_PATH = "src/main/resources/files/";

    @Autowired
    private AuthService authService;

    @Autowired
    private UIService uiService;

    @Autowired
    private CategoryService catService;

    @Autowired
    private PublicationService publicationService;


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

        User user  =  authService.getUser(id).get();


        String tipo = user.getTipo();

        switch (tipo){
            case "profesor":
                String image_path = ((Professor) user).getPhotoPath();
                Path path = Paths.get(image_path);
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            default:
                Path path2 = Paths.get("src/main/resources/static/images/picture_default.jpg");
                ByteArrayResource resource2 = new ByteArrayResource(Files.readAllBytes(path2));
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource2);

        }
    }



    @GetMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadFileFromLocal(String param, @RequestParam(required = true) Long id) {
        Publication publication = uiService.getPublicationById(id).get();
        Path path = Paths.get(publication.getResource_path());

        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert resource != null;
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/uploadPublication")
    public String uploadToLocalFileSystem(@ModelAttribute pub pub, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {

        Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

        MultipartFile file = pub.getFile();

        MultipartFile image_file = pub.getImage_file();

        String title = pub.getTitle();
        String description = pub.getDescription();
        int category_id= pub.getCategory_id();

        int rank = 0;

        log.info(pub.getTitle());
        log.info(pub.getDescription());



        Publication publication = new Publication();

        publication.setTitle(title);
        publication.setDescription(description);
        publication.setRanking(rank);
        String email = (String) session.getAttribute("EMAIL");

        if (email==null)
            return "redirect:/error";

        Professor p = (Professor) authService.getUser(email).get();
        Category c = catService.getCategory(category_id).get();
        publication.setProfessor(p);
        publication.setCategory(c);
        log.info(p.getEmail());


        // Guardar el archivo
        // pdf
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        String resourcePath = FILE_PATH + email + "_" + fileName;

        int i = 1;
        while(publicationService.verify_content(resourcePath,p)){
            resourcePath = FILE_PATH + email + "_" + i + "_" + fileName;
            i++;
        }
        publication.setResource_path(resourcePath);


        Path path = Paths.get(resourcePath);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar el archivo
        // imagen
        if(image_file.isEmpty()){
            resourcePath = "src/main/resources/files/logo.jpg";

        } else {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(image_file.getOriginalFilename()));

            resourcePath = FILE_PATH + email + "_" + fileName;

            i = 1;
            while(publicationService.verify_content(resourcePath,p)){
                resourcePath = FILE_PATH + email + "_" + i + "_" + fileName;
                i++;
            }
        }

        publication.setImage_path(resourcePath);

        uiService.postPublication(publication);

        path = Paths.get(resourcePath);
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


    @GetMapping(value = "/zip-download", produces="application/zip")
    public void zipDownload( HttpServletResponse response, HttpSession session) throws IOException {

        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

        List<Publication> mochilaPublications = (List<Publication>) uiService.getUserMochila(user);


        for (Publication publication : mochilaPublications) {
            FileSystemResource resource = new FileSystemResource(publication.getResource_path());
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "mochila.zip" + "\"");
    }

    @GetMapping(value = "/zip-downloadCurator", produces="application/zip")
    public void zipDownloadCurator( HttpServletResponse response, HttpSession session) throws IOException {

        String email = (String) session.getAttribute("EMAIL");

        User user = authService.getUser(email).get();

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

        List<Publication> publications = publicationService.getPublicationsToVerifyByCurator(email);


        for (Publication publication : publications) {
            FileSystemResource resource = new FileSystemResource(publication.getResource_path());
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "mochila.zip" + "\"");
    }

    @GetMapping(value = "/zip-publication-download", produces="application/zip")
    public void zipDownload(@RequestParam Long id, HttpServletResponse response, HttpSession session) throws IOException {

        Publication publication  =uiService.getPublicationById(id).get();

        String resource_path = publication.getResource_path();



        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());



        FileSystemResource resource = new FileSystemResource(resource_path);
        ZipEntry zipEntry = new ZipEntry(resource.getFilename());
        zipEntry.setSize(resource.contentLength());
        zipOut.putNextEntry(zipEntry);
        StreamUtils.copy(resource.getInputStream(), zipOut);
        zipOut.closeEntry();

        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "publication.zip" + "\"");
    }


}