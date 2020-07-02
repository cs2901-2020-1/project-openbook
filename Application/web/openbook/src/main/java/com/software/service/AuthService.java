package com.software.service;

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

    public void deleteUser(User user){
        userRepository.deleteById(user.getEmail());
    }

    public Optional<User> getUser(String email){
        return userRepository.findById(email);
    }


}
