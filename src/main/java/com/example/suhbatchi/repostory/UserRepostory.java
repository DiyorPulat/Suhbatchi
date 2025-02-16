package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepostory extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    User findByClientId(String clientId);
}
