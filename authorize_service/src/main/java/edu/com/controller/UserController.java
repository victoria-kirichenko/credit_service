package edu.com.controller;

import edu.com.model.UserRegistrationDto;
import edu.com.exception.UserAlreadyExistException;
import edu.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            System.out.println(registrationDto.getUsername());
            userService.registerNewUserAccount(registrationDto.getUsername(), registrationDto.getEmail(),
                    registrationDto.getPassword());
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/homepage")
    public ResponseEntity<?> homepageSuccess() {
        try {
            return new ResponseEntity<>("User login successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

