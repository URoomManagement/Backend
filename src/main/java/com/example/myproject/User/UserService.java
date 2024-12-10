package com.example.myproject.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Optional<UserDTO> getUserById(long id){
        return userRepository.findById(id)
                .map(this::mapToDTO);
    }

    public UserDTO createUser(User user){
        return mapToDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, User updatedUser){
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return mapToDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserDTO mapToDTO(User user){
        return new UserDTO(user.getId(), user.getEmail(), user.getName());
    }
}
