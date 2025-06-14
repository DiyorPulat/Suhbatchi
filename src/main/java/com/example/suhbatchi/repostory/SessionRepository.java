package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionRepository extends JpaRepository<SessionModel, Long> {
        @Query(value = "SELECT * FROM session_model WHERE phoneNumber = :phoneNumber ORDER BY created_date DESC LIMIT 1", nativeQuery = true)
        SessionModel getByPhoneNumber(String phoneNumber);
}
