package com.example.suhbatchi.repostory;

import com.example.suhbatchi.entity.PricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepostory extends JpaRepository<PricingPlan, Long> {

}
