package com.example.suhbatchi.mapper;

import com.example.suhbatchi.dto.request.PricingPlanRequest;
import com.example.suhbatchi.dto.request.PricingUpdateRequest;
import com.example.suhbatchi.dto.response.PricingPlanReponse;
import com.example.suhbatchi.entity.PricingPlan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PricingMapper {
    public PricingPlan toPricingPlan(PricingPlanRequest request) {
        PricingPlan pricingPlan = new PricingPlan();
        pricingPlan.setName(request.name());
        pricingPlan.setDescription(request.description());
        pricingPlan.setTimeOfUse(request.timeOfUse());
        pricingPlan.setMonthOfUse(request.monthOfUse());
        pricingPlan.setDescription(request.description());
        pricingPlan.setPlanStatus(request.planStatus());
        pricingPlan.setCost(request.cost());
        return pricingPlan;
    }

    public PricingPlanReponse toPricingPlanReponse(PricingPlan pricingPlan) {
        PricingPlanReponse pricingPlanReponse = new PricingPlanReponse();
        pricingPlanReponse.setPlanId(pricingPlan.getId());
        pricingPlanReponse.setName(pricingPlan.getName());
        pricingPlanReponse.setDescription(pricingPlan.getDescription());
        pricingPlanReponse.setTimeOfUse(pricingPlan.getTimeOfUse());
        pricingPlanReponse.setMonthOfUse(pricingPlan.getMonthOfUse());
        pricingPlanReponse.setPlanStatus(pricingPlan.getPlanStatus());
        pricingPlanReponse.setCost(pricingPlan.getCost());
        return pricingPlanReponse;
    }

    public List<PricingPlanReponse> toPricingPlanReponses(List<PricingPlan> pricingPlans) {
        List<PricingPlanReponse> pricingPlanReponses = new ArrayList<>();
        for (PricingPlan pricingPlan : pricingPlans) {
            pricingPlanReponses.add(toPricingPlanReponse(pricingPlan));
        }
        return pricingPlanReponses;
    }

    public PricingPlan toPricingPlan(PricingPlanRequest request, Long planId) {
        PricingPlan pricingPlan = new PricingPlan();
        pricingPlan.setId(planId);
        pricingPlan.setName(request.name());
        pricingPlan.setDescription(request.description());
        pricingPlan.setTimeOfUse(request.timeOfUse());
        pricingPlan.setMonthOfUse(request.monthOfUse());
        pricingPlan.setPlanStatus(request.planStatus());
        pricingPlan.setCost(request.cost());
        return pricingPlan;
    }


}
