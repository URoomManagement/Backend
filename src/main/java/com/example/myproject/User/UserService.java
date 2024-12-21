package com.example.myproject.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.myproject.Exceptions.DatabaseException;
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
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }
                    return mapToDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void updatePassword(Long userId, String currentPassword, String newPassword){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserDTO mapToDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getName());
    }
}
