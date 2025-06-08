package com.example.suhbatchi.controller;

import com.example.suhbatchi.consts.ProjectConstants;
import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.UserActivateRequest;
import com.example.suhbatchi.dto.response.UserInfo4PermitResponse;
import com.example.suhbatchi.dto.response.UserStatusResponse;
import com.example.suhbatchi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(ProjectConstants.USER_CONTROL)
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(ProjectConstants.GET_ALL_USER)
    public List<UserInfo4PermitResponse> getAllUserInfo() {
        return userService.getAllUserInfo();
    }

    @PostMapping(ProjectConstants.USER_ACTIVATE)
    public void performUser(@RequestBody UserActivateRequest request) {
        userService.UserUpdate4Activate("sdds", request);
    }

    @PostMapping(ProjectConstants.PERFORM_USER_STATUS)
    public void performUserStatus(@RequestBody PhoneNumberRequest request) {
        userService.approve4User(request);
    }

    @GetMapping(ProjectConstants.GET_USER_STATUS)
    public UserStatusResponse getUserStatus() {
        return userService.getInfoStatusInfo("93489438943895489");
    }

}
