package com.example.myproject.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User AS u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);  
}
