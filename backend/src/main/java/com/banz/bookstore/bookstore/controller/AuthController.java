package com.banz.bookstore.bookstore.controller;


import com.banz.bookstore.bookstore.exceptions.UserAlreadyExistsException;
import com.banz.bookstore.bookstore.payload.request.CreateUserDTO;
import com.banz.bookstore.bookstore.service.TokenService;
import com.banz.bookstore.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;

        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
        try {
            log.info("Token requested for user: '{}'", authentication.getName());
            String token = tokenService.createToken(authentication);
            log.info("Token generated for user '{}'", authentication.getName());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@ModelAttribute CreateUserDTO createUserDTO) {
        try {
             userService.createUser(createUserDTO);
            return new ResponseEntity<>("User created successfully",HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
