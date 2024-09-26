package com.example.rest_practice.service;

import com.example.rest_practice.model.UserEntity;
import com.example.rest_practice.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null || userEntity.getUsername() == null){
            throw new RuntimeException("Invalid arguments");
        }

        final String username = userEntity.getUsername();

        if(userRepository.existsByUsername(username)){
            log.warn("Username already exists : {}", username);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String username, final String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
