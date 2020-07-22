package server.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import server.api.dto.UserDTO;
import server.api.model.User;
import server.api.repository.GameRepository;
import server.api.repository.UserRepository;
import server.api.security.JWTUtils;

import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtils jwtUtils;

    public UserController(UserRepository userRepository, GameRepository gameRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        // Check if username or email already exists
        if (!userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).isEmpty()) {
            return new ResponseEntity<>("Username or email already taken", HttpStatus.BAD_REQUEST);
        }

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
    public UserDTO getCurrentUserInfo() {
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new UserDTO(user, gameRepository);
    }

    @PatchMapping("/me")
    public UserDTO updateCurrentUserInfo(@RequestBody JsonNode updates) {
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (updates.has("email")) {
            user.setEmail(updates.get("email").asText());
        }
        if (updates.has("password")) {
            user.setPassword((updates.get("password").asText()));
        }
        if (updates.has("country")) {
            user.setCountry(updates.get("country").asText());
        }
        if (updates.has("location")) {
            user.setLocation(updates.get("location").asText());
        }
        if (updates.has("biography")) {
            user.setBiography(updates.get("biography").asText());
        }
        if (updates.has("givenName")) {
            user.setGivenName(updates.get("givenName").asText());
        }
        if (updates.has("surName")) {
            user.setSurName(updates.get("surName").asText());
        }
        userRepository.save(user);
        return new UserDTO(user, gameRepository);
    }

    @GetMapping("/{name}")
    private ResponseEntity<UserDTO> getUserInfo(@PathVariable String name) {
        Optional<User> userOptional = userRepository.findByUsername(name);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new UserDTO(userOptional.get(), gameRepository), HttpStatus.OK);
    }
}
