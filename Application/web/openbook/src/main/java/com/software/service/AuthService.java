package com.software.service;

import com.software.model.Professor;
import com.software.model.Student;
import com.software.model.User;
import com.software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean verifyUser(User user){
        //TO DO
        Optional<User> optionalUser  = userRepository.findById(user.getEmail());

        //for encryption
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return optionalUser.filter(value -> passwordEncoder.matches(user.getPassword(), value.getPassword())).isPresent();
    }

    public Optional<User> doLoginUser(User user){
        if(!verifyUser(user))
            return Optional.empty();
        return userRepository.findById(user.getEmail());
        //   getUser(user.getEmail());

    }

    public void addUser(User user){
        //for encryption
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

    }

    public boolean registerUser(User user){
        Optional<User> optionalUser  = userRepository.findById(user.getEmail());
        if (optionalUser.isPresent())
            return false;
        addUser(user);
        return true;
    }

    public boolean updateProfesorDescription(User user) {

        Optional<User> optionalUser  = userRepository.findById(user.getEmail());

        if(optionalUser.isPresent()){

            Professor currentProfesor = (Professor)optionalUser.get();

            Professor updatedProfesor = (Professor) user;
            if (!updatedProfesor.getPhotoPath().equals("")){
                currentProfesor.setPhotoPath(updatedProfesor.getPhotoPath());
            }
            currentProfesor.setDescription(updatedProfesor.getDescription());
            userRepository.save(currentProfesor);
            return true;
        }
        return false;

    }


    public boolean updateUser(User user) {

        Optional<User> optionalUser  = userRepository.findById(user.getEmail());
        //for encryption
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword;

        if(optionalUser.isPresent()){
            String tipo = optionalUser.get().getTipo();

            switch (tipo){
                case "profesor":

                    Professor currentProfesor =  (Professor) optionalUser.get();
                    Professor updatedProfesor = (Professor) user;
                    currentProfesor.setEmail(updatedProfesor.getEmail());
                    currentProfesor.setName(updatedProfesor.getName());
                    currentProfesor.setUsername(updatedProfesor.getUsername());
                    currentProfesor.setSurname(updatedProfesor.getSurname());
                    currentProfesor.setTitleId(updatedProfesor.getTitleId());

                    encodedPassword = passwordEncoder.encode(updatedProfesor.getPassword());
                    currentProfesor.setPassword(encodedPassword);

                    userRepository.save(currentProfesor);
                    return true;

                case "student":
                    Student currentStudent = (Student) optionalUser.get();
                    Student updatedStudent = (Student) user;

                    currentStudent.setEmail(updatedStudent.getEmail());
                    currentStudent.setName(updatedStudent.getName());
                    currentStudent.setUsername(updatedStudent.getUsername());
                    currentStudent.setSurname(updatedStudent.getSurname());
                    currentStudent.setPassword(updatedStudent.getPassword());

                    encodedPassword = passwordEncoder.encode(updatedStudent.getPassword());
                    updatedStudent.setPassword(encodedPassword);

                    userRepository.save(currentStudent);
                    return true;

            }

        }
        return false;
    }

    public void deleteUser(User user){
        userRepository.deleteById(user.getEmail());
    }

    public Optional<User> getUser(String email){
        return userRepository.findById(email);
    }


}
