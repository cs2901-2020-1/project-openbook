package com.software.service;


import com.software.model.*;
import com.software.repository.MochilaRepository;
//import com.software.repository.PublicationRepository;
import com.software.repository.PublicationRepository;
import com.software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UIService {


    @Autowired
    private MochilaRepository mochilaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    public List<Publication> getPublicationsByProfessor(Professor professor){
        //TO DO
        List<Publication> publications = publicationRepository.findAll();

        List<Publication> publicationList = new ArrayList<>();

        for (Publication p:publications)
        {
            if (p.getProfessor().getEmail().equals(professor.getEmail())){
                publicationList.add(p);
            }

        }

        return publicationList;
    }

    public List<Publication> getAllPublications(){

        return publicationRepository.findAll();
    }

    public Optional<Publication> getPublicationById(Long id){

        return publicationRepository.findById(id);


    }

    public List<Publication> getPublicationsByCategory(Category category){
        return null;

    }

    public void postPublication(Publication publication){
        publicationRepository.save(publication);
    }


    public void saveInMochila(Publication publication, User user){

        Mochila mochila = new Mochila();

        mochila.setPublication_id(publication);
        mochila.setUser_id(user);

        mochilaRepository.save(mochila);

    }



    public List<Publication> getUserMochila(User user){

        List<Long> publications_id = new ArrayList<>();
        Iterable<Mochila> mochilas= mochilaRepository.findAll();

        for (Mochila m: mochilas) {
            if(m.getUser_id().getEmail().equals(user.getEmail())){

                publications_id.add(m.getPublication_id().getId());
            }
        }

        Iterable<Publication> publications = publicationRepository.findAll();

        List<Publication> publicationsByUser = new ArrayList<>();

        for (Publication p: publications) {
            if (publications_id.contains(p.getId())){
                publicationsByUser.add(p);
            }
        }
        return publicationsByUser;
    }

    public List<Publication> getPublicationByCategory(Publication publication, List<Category> categories){
        return null;
    }




}
