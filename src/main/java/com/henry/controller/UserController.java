package com.henry.controller;

import com.henry.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private List<User> users = new ArrayList<>();
    private Long currentUserId = 1L; // Used to assign incremental IDs to users

    @PostMapping
    public User addUser(@RequestBody User user) {
        // Assign an incremental ID to the user before saving
        user.setId(currentUserId++);

        // Save the user to the in-memory list
        users.add(user);
        return user;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Search for the user in the in-memory list based on the provided ID
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
