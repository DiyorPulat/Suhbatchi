package com.example.suhbatchi.service;

import com.example.suhbatchi.dto.request.PhoneNumberRequest;
import com.example.suhbatchi.dto.request.UserActivateRequest;
import com.example.suhbatchi.dto.response.UserInfo4PermitResponse;
import com.example.suhbatchi.dto.response.UserStatusResponse;
import com.example.suhbatchi.entity.Company;
import com.example.suhbatchi.entity.PricingPlan;
import com.example.suhbatchi.entity.User;
import com.example.suhbatchi.mapper.UserMapper;
import com.example.suhbatchi.repostory.CompanyRepostory;
import com.example.suhbatchi.repostory.PricingRepostory;
import com.example.suhbatchi.repostory.UserRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepostory userRepostory;
    private final CompanyRepostory companyRepostory;
    private final PricingRepostory pricingRepostory;
    private final UserMapper userMapper;

    public UserService(UserRepostory userRepostory, CompanyRepostory companyRepostory, PricingRepostory pricingRepostory, UserMapper userMapper) {
        this.userRepostory = userRepostory;
        this.companyRepostory = companyRepostory;
        this.pricingRepostory = pricingRepostory;

        this.userMapper = userMapper;
    }


    public void UserUpdate4Activate(String phoneNumber, UserActivateRequest request) {

        Optional<User> userEntity = userRepostory.findByPhoneNumber(phoneNumber);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            Company company = companyRepostory.getReferenceById(request.companyId());
            PricingPlan pricingPlan = pricingRepostory.getReferenceById(request.pricingId());
            user.setCompany(company);
            user.setPricingPlan(pricingPlan);
            userRepostory.save(user);
        }
    }


    public List<UserInfo4PermitResponse> getAllUserInfo() {
        List<User> users = userRepostory.findUserByIsActiveUser(false);
        List<UserInfo4PermitResponse> responses = userMapper.toUserInfo4Permits(users);
        return responses;
    }

    public void approve4User(PhoneNumberRequest request) {
        Optional<User> userEntity = userRepostory.findByPhoneNumber(request.phoneNumber());
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            user.setIsActiveUser(true);
            userRepostory.save(user);
        }
    }

    public UserStatusResponse getInfoStatusInfo(String phoneNumber) {
        Optional<User> userEntity = userRepostory.findByPhoneNumber(phoneNumber);
        UserStatusResponse response = new UserStatusResponse();
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            response = userMapper.toUserStatusResponse(user);
        }
      return response;
    }

}



