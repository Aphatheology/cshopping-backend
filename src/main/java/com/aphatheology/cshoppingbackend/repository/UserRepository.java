package com.aphatheology.cshoppingbackend.repository;

import com.aphatheology.cshoppingbackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByEmail(String email);
    Optional<Users> findUserByEmailOrUsername(String email, String username);
}
