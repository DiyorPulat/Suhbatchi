package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.OtpTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTemplateRepostory extends JpaRepository<OtpTemplate, Long> {
}
