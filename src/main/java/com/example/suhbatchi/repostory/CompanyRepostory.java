package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepostory extends JpaRepository<Company, Long> {

}
