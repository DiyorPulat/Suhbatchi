package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.UserActivateRequest;
import com.example.suhbatchi.dto.response.UserInfo4PermitResponse;
import com.example.suhbatchi.dto.response.UserStatusResponse;
import com.example.suhbatchi.service.AuthService;
import com.example.suhbatchi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ProjectConstants.USER_CONTROL)
@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    @Operation(
            summary = "Auth token kerak emas",
            description = "Barcha aktiv bo'lmagan va company va tarifni tanlab so'rov yuborgan userlar ro'yxati",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli ma'lumotlar olindi",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserInfo4PermitResponse.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping(ProjectConstants.GET_ALL_USER)
    public List<UserInfo4PermitResponse> getAllUserInfo() {
        return userService.getAllUserInfo();
    }


    @Operation(
            summary = "Auth token kerak",
            description = "Copmany va tarifni tanlab so'rov yuborish uchun api",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli malumotlar junatildi"
                    )
            }
    )
    @PostMapping(ProjectConstants.USER_ACTIVATE)
    public void performUser(@RequestBody UserActivateRequest userActivateRequest,HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.isValidPhoneNumber(phoneNumber);
        userService.UserUpdate4Activate(phoneNumber, userActivateRequest);
    }


    @Operation(
            summary = "Auth token kerak emas",
            description = "Admin uchun api Mijoz tanlagan Copmany va tarifni tasdiqlash uchun",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli malumotlar tasdiqlandi"
                    )
            }
    )
    @PostMapping(ProjectConstants.PERFORM_USER_STATUS)
    public void performUserStatus(@RequestBody PhoneNumberRequest request) {
        userService.approve4User(request);
    }



    @Operation(
            summary = "Auth token kerak",
            description = "Mijozni statusi haqida malumot olish uchun active yoki inactive",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Muvaffaqiyatli malumotlar olindi"
                    )
            }
    )
    @GetMapping(ProjectConstants.GET_USER_STATUS)
    public UserStatusResponse getUserStatus(HttpServletRequest request) {
        String phoneNumber = authService.getPhoneNumberFromToken(request);
        authService.isValidPhoneNumber(phoneNumber);
        return userService.getInfoStatusInfo(phoneNumber);
    }
}
