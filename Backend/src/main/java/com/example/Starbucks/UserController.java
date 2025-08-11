package com.example.Starbucks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.*;

@CrossOrigin
@RestController
public class UserController {

    private final JwtUtil jwtUtil;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add-user")
    public ResponseEntity<Object> register(@RequestBody User user) {

        if (user.getUserPassword() == null ||
                user.getUserName() == null ||
                user.getUserEmail() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong DATA!");
        }

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

        //TODO: check data in DB

        // Если всё ок — создаём токен
        Response response = new Response();
        response.setUserData(user.getUserEmail(), user.getUserName());
        response.setUserToken(jwtUtil.generateToken(user.getUserEmail(), user.getUserName()));
        response.setMessage("Login Successful :)");

        return ResponseEntity.ok(response);
    }
}
