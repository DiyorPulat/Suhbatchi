package com.example.suhbatchi.service;

import com.example.suhbatchi.dto.request.PricingPlanRequest;
import com.example.suhbatchi.dto.request.PricingUpdateRequest;
import com.example.suhbatchi.dto.response.PricingPlanReponse;
import com.example.suhbatchi.entity.PricingPlan;
import com.example.suhbatchi.mapper.PricingMapper;
import com.example.suhbatchi.repostory.PricingRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PricingService {
    private final PricingRepostory pricingRepository;
    private final PricingMapper pricingMapper;


    public PricingService(PricingRepostory pricingRepository, PricingMapper pricingMapper) {
        this.pricingRepository = pricingRepository;
        this.pricingMapper = pricingMapper;
    }

    public void createPricingPlan(PricingPlanRequest request) {
        PricingPlan pricingPlan = pricingMapper.toPricingPlan(request);
        try {
            pricingRepository.save(pricingPlan);

        } catch (Exception e) {
            log.error("In saving PricingPlan error,{}", e.getMessage());
        }
    }

    public List<PricingPlanReponse> getAllPricingPlans() {
        List<PricingPlan> pricingPlans = pricingRepository.findAll();
        return pricingMapper.toPricingPlanReponses(pricingPlans);
    }

    public PricingPlanReponse getPricingPlanById(Long id) {
        PricingPlan pricingPlan = pricingRepository.getReferenceById(id);
        return pricingMapper.toPricingPlanReponse(pricingPlan);
    }

    public void deletePricingPlanById(Long id) {
        pricingRepository.deleteById(id);
    }

    public void updatePricingPlan(PricingPlanRequest request, Long id) {
        PricingPlan pricingPlan = pricingMapper.toPricingPlan(request,id);
        try {
            pricingRepository.save(pricingPlan);
        } catch (Exception e) {
            log.error("In updating PricingPlan error ,{}", e.getMessage());
        }
    }
}



