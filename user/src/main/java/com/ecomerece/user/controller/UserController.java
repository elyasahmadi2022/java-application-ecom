package com.ecomerece.user.controller;
import com.ecomerece.user.dto.UserRequest;
import com.ecomerece.user.dto.UserResponse;
import com.ecomerece.user.model.User;
import com.ecomerece.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(  "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<com.ecomerece.user.dto.UserResponse>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequest user) {
        System.out.println(user.getFirstName());
        User createdUser = userService.createUser(user);
        if (createdUser != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return new ResponseEntity<>(
                userService.getUser(id),
                HttpStatus.OK) ;
    }
    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody UserRequest user) {
        boolean updated = userService.updateUser(id, user);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
