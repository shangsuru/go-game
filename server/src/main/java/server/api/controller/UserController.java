package server.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import server.api.model.User;
import server.api.repository.UserRepository;
import server.api.security.JWTUtils;

import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtils jwtUtils;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(jwtUtils.createJWT(user.getUsername()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> userData = userRepository.findByUsername(user.getUsername());
        if (userData.isPresent() && bCryptPasswordEncoder.matches(user.getPassword(), userData.get().getPassword())) {
            return new ResponseEntity<>(jwtUtils.createJWT(user.getUsername()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong username or password", HttpStatus.BAD_REQUEST);
        }
    }

   @GetMapping("/me")
    public String getCurrentUserInfo() {
        // TODO
        return "You shall not pass";
    }
}
