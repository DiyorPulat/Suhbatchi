package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.OtpTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTemplateRepostory extends JpaRepository<OtpTemplate, Long> {
    @Query(value = "SELECT * FROM otp WHERE phone_number = :phoneNumber ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    OtpTemplate findOtpTemplateByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
