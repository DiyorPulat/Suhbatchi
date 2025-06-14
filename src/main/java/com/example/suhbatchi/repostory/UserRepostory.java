package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepostory extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    User findByClientId(String clientId);

    @Query(value = "SELECT * FROM users WHERE IsActiveUser = 0",nativeQuery = true)
    List<User> findUserByIsActiveUser(Boolean isActiveUser);


    Boolean existsByClientId(String clientId);
}
