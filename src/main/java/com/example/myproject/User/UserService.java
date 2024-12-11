package com.example.myproject.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.myproject.Exceptions.DatabaseException;
import com.example.myproject.Exceptions.InvalidCredentialsException;
import com.example.myproject.Exceptions.UserNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO getUserById(long id){
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new UserNotFoundException("User with ID " +  " not found"));
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
                .orElseThrow(() -> new UserNotFoundException("User with ID " + " not found"));
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getName());
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return "Login successful";
    }
}
