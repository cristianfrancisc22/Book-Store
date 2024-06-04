package com.banz.bookstore.bookstore.service;

import com.banz.bookstore.bookstore.exceptions.UserAlreadyExistsException;
import com.banz.bookstore.bookstore.model.User;
import com.banz.bookstore.bookstore.payload.request.CreateUserDTO;
import com.banz.bookstore.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByUsername(createUserDTO.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User(
                createUserDTO.getUsername(),
                createUserDTO.getPassword()
        );

        userRepository.save(user);
    }
}
