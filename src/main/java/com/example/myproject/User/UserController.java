package com.example.myproject.User;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.myproject.User.dto.PasswordUpdateDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET: Fetch all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET: Fetch a user by ID
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // POST: Create a new user
    @PostMapping("/register")
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT: Update an existing user
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // PUT: Update user's password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO passwordRequest
    ) {
        try {
            userService.updatePassword(
                    id,
                    passwordRequest.currentPassword(),
                    passwordRequest.newPassword()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    // DELETE: Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
