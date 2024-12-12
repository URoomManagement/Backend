package com.example.myproject.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.myproject.Exceptions.DatabaseException;
import com.example.myproject.Exceptions.UserNotFoundException;
import com.example.myproject.Helper.JwtUtil;
import com.example.myproject.Login.LoginRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.AuthenticationException;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO getUserById(long id){
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO createUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return mapToDTO(userRepository.save(user));
        } catch(DataAccessException e){
            throw new DatabaseException("Failed to save user to the database: " + e.getMessage());
        }
    }

    public UserDTO updateUser(Long id, User updatedUser){
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return mapToDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getName());
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
