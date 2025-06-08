package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PricingPlanRequest;
import com.example.suhbatchi.dto.request.PricingUpdateRequest;
import com.example.suhbatchi.dto.response.PricingPlanReponse;
import com.example.suhbatchi.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProjectConstants.PRICING_PLAN)
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }


    @PostMapping(ProjectConstants.CREATE_PRICING_PLAN)
    public void createPricingPlan(@RequestBody PricingPlanRequest request) {
        pricingService.createPricingPlan(request);
    }

    @PutMapping(ProjectConstants.UPDATE_PRICING_PLAN + "/{id}")
    public void updatePricingPlan(@PathVariable(name = "id") Long id,PricingPlanRequest request) {
        pricingService.updatePricingPlan(request,id);
    }

    @DeleteMapping(ProjectConstants.DELETE_PRICING_PLAN + "/{id}")
    public void deletePricingPlan(@PathVariable(name = "id") Long id) {
        pricingService.deletePricingPlanById(id);
    }

    @GetMapping(ProjectConstants.GET_PRICING_PLAN + "/{id}")
    public PricingPlanReponse getPricingPlan(@PathVariable(name = "id") Long id) {
        return pricingService.getPricingPlanById(id);
    }

    @GetMapping(ProjectConstants.GET_ALL_PRICING_PLAN)
    public List<PricingPlanReponse> getAllPricingPlan() {
        return pricingService.getAllPricingPlans();
    }


}
