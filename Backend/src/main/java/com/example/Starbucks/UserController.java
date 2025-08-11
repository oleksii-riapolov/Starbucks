package com.example.Starbucks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @GetMapping("/test-mongo")
    public String testMongoConnection() {
        long count = userRepository.count();
        return "Всего пользователей в базе: " + count;
    }

    @PostMapping("/add-user")
    public ResponseEntity<Object> register(@RequestBody User user) {

        if (user.getUserPassword() == null ||
                user.getUserName() == null ||
                user.getUserEmail() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing DATA!");
        }

        if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        userRepository.save(user);

        //проверочка
        System.out.println(user.getUserName());
        System.out.println(user.getUserEmail());
        System.out.println(user.getUserPassword());


        Response response = new Response();
        response.setUserData(user.getUserEmail(), user.getUserName());
        response.setUserToken(jwtUtil.generateToken(user.getUserEmail(), user.getUserName()));
        response.setMessage("Register Successful :)");
        System.out.println(response.getUserToken());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login-user")
    public ResponseEntity<Object> login(@RequestBody User user) {
        if (user.getUserEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required!");
        }
        if (user.getUserPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required!");
        }


        //проверочка
        System.out.println(user.getUserEmail());
        System.out.println(user.getUserPassword());

        //looking for user
        User existingUser = userRepository.findByUserEmail(user.getUserEmail());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        //checking user password
        if (!existingUser.getUserPassword().equals(user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        // Если всё ок — создаём токен
        Response response = new Response();
        response.setUserData(existingUser.getUserEmail(), existingUser.getUserName());
        response.setUserToken(jwtUtil.generateToken(existingUser.getUserEmail(), existingUser.getUserName()));
        response.setMessage("Login Successful :)");

        return ResponseEntity.ok(response);
    }
}
