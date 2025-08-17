package com.app.swiftKart.controller;

import com.app.swiftKart.dto.UserRequest;
import com.app.swiftKart.dto.UserResponse;
import com.app.swiftKart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    // GET request
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PostMapping
    public ResponseEntity<String> addUser( @RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("user added successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> fetchUser(@PathVariable Long id){
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest){
        Boolean updatedFlag= userService.updateUser(id,userRequest);
        if(updatedFlag) return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }
}
