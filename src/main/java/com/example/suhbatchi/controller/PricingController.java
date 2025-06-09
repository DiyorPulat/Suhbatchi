package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PricingPlanRequest;
import com.example.suhbatchi.dto.response.PricingPlanReponse;
import com.example.suhbatchi.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProjectConstants.PRICING_PLAN)
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api tarif yaratish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli tarif yaratildi"
                    )
            }
    )
    @PostMapping(ProjectConstants.CREATE_PRICING_PLAN)
    public void createPricingPlan(@RequestBody PricingPlanRequest request) {
        pricingService.createPricingPlan(request);
    }

    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api tarifni o'zgartirish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli tarifni o'zgartirildi"
                    )
            }
    )
    @PutMapping(ProjectConstants.UPDATE_PRICING_PLAN + "/{id}")
    public void updatePricingPlan(@PathVariable(name = "id") Long id, PricingPlanRequest request) {
        pricingService.updatePricingPlan(request, id);
    }

    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api tarifni o'chirish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli tarifni o'chirildi"
                    )
            }
    )
    @DeleteMapping(ProjectConstants.DELETE_PRICING_PLAN + "/{id}")
    public void deletePricingPlan(@PathVariable(name = "id") Long id) {
        pricingService.deletePricingPlanById(id);
    }

    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api  id buyicha tarifni olish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli tarif ma'lumot olindi"
                    )
            }
    )
    @GetMapping(ProjectConstants.GET_PRICING_PLAN + "/{id}")
    public PricingPlanReponse getPricingPlan(@PathVariable(name = "id") Long id) {
        return pricingService.getPricingPlanById(id);
    }


    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api  barcha tariflarni olish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli barcha tariflar ma'lumot olindi"
                    )
            }
    )
    @GetMapping(ProjectConstants.GET_ALL_PRICING_PLAN)
    public List<PricingPlanReponse> getAllPricingPlan() {
        return pricingService.getAllPricingPlans();
    }


}
