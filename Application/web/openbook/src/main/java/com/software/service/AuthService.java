package com.software.service;

import com.software.model.Professor;
import com.software.model.Student;
import com.software.model.User;
import com.software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean verifyUser(User user){
        //TO DO
        Optional<User> optionalUser  = userRepository.findById(user.getEmail());
        return optionalUser.filter(value -> user.getPassword().equals(value.getPassword())).isPresent();

    }

    public Optional<User> doLoginUser(User user){
        if(!verifyUser(user))
            return Optional.empty();
        return userRepository.findById(user.getEmail());
        //   getUser(user.getEmail());

    }

    public void addUser(User user){
        userRepository.save(user);

    }

    public boolean registerUser(User user){
        if (verifyUser(user))
            return false;
        addUser(user);
        return true;

    }

    public boolean updateUser(User user) {

        Optional<User> optionalUser  = userRepository.findById(user.getEmail());

        if(optionalUser.isPresent()){
            String tipo = optionalUser.get().getTipo();

            switch (tipo){
                case "profesor":
                    Professor updatedProfesor = (Professor) user;
                    Professor professor = new Professor();
                    professor.setEmail(updatedProfesor.getEmail());
                    professor.setName(updatedProfesor.getName());
                    professor.setUsername(updatedProfesor.getUsername());
                    professor.setSurname(updatedProfesor.getSurname());
                    professor.setTitleId(updatedProfesor.getTitleId());
                    professor.setPassword(optionalUser.get().getPassword());
                    userRepository.save(professor);
                    return true;

                case "student":
                    Student updatedStudent = (Student) user;
                    Student student = new Student();
                    student.setEmail(updatedStudent.getEmail());
                    student.setName(updatedStudent.getName());
                    student.setUsername(updatedStudent.getUsername());
                    student.setSurname(updatedStudent.getSurname());
                    student.setPassword(optionalUser.get().getPassword());
                    userRepository.save(student);
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
