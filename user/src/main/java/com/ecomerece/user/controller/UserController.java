package com.ecomerece.user.controller;
import com.ecomerece.user.dto.UserRequest;
import com.ecomerece.user.dto.UserResponse;
import com.ecomerece.user.model.User;
import com.ecomerece.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(  "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping
    public ResponseEntity<List<com.ecomerece.user.dto.UserResponse>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequest user) {
        User createdUser = userService.createUser(user);
        if (createdUser != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        log.info("Request received for user {}", id);
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
