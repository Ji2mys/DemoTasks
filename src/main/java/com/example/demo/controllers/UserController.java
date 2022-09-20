package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("UsersController")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("users")
    public ResponseEntity<User> postUser(@RequestBody User newUser) {
        User postedUser = this.userService.save(newUser);
        return new ResponseEntity<>(postedUser, HttpStatus.OK);
    }

    @PatchMapping("users/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, (String) key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user.get(), value);
                }
            });
            User updatedUser = userService.save(user.get());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("users/{username}")
    public ResponseEntity<List<User>> getUsersByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }
}
