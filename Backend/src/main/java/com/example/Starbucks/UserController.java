package com.example.Starbucks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.*;

@CrossOrigin
@RestController
public class UserController {

    private final UserRepository userDataRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userDataRepository, JwtUtil jwtUtil) {
        this.userDataRepository = userDataRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add-user")
    public ResponseEntity<Object> register(@RequestBody User user) {
        if (user.getUserPassword() == null ||
                user.getUserName() == null ||
                user.getUserEmail() == null ||
                user.getUserPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong DATA!");
        }

        if (userDataRepository.existsByUserEmail(user.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!");
        }

        //проверочка
        System.out.println(user.getUserName());
        System.out.println(user.getUserEmail());
        System.out.println(user.getUserPassword());

        userDataRepository.save(user);

        Response response = new Response();
        response.setUserData(user.getUserEmail(), user.getUserName());
        response.setUserToken(jwtUtil.generateToken(user.getUserEmail()));
        response.setMessage("Register Successful :)");

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

        // Ищем пользователя по email
        User existingUser = userDataRepository.findByUserEmail(user.getUserEmail());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        // !!! Временная проверка — сравнение открытых паролей
        // В будущем нужно сделать BCrypt
        if (!user.getUserPassword().equals(existingUser.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password!");
        }

        //проверочка
        System.out.println(user.getUserEmail());
        System.out.println(user.getUserPassword());

        // Если всё ок — создаём токен
        Response response = new Response();
        response.setUserData(existingUser.getUserEmail(), existingUser.getUserName());
        response.setUserToken(jwtUtil.generateToken(existingUser.getUserEmail()));
        response.setMessage("Login Successful :)");

        return ResponseEntity.ok(response);
    }
}
