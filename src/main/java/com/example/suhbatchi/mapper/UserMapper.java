package com.example.suhbatchi.mapper;

import com.example.suhbatchi.dto.response.UserInfo4PermitResponse;
import com.example.suhbatchi.dto.response.UserStatusResponse;
import com.example.suhbatchi.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserInfo4PermitResponse toUserInfo4Permit(User user) {
        UserInfo4PermitResponse response = new UserInfo4PermitResponse();
        response.setPhone(user.getPhoneNumber());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        if (user.getCompany() != null) {
            response.setEmail(user.getCompany().getEmail());
            response.setCompanyName(user.getCompany().getCompanyName());
        }
        if (user.getPricingPlan() != null) {
            response.setPricingName(user.getPricingPlan().getName());
        }
        return response;
    }

    public List<UserInfo4PermitResponse> toUserInfo4Permits(List<User> users) {
        List<UserInfo4PermitResponse> responses = new ArrayList<>();
        for (User user : users) {
            if (user.getPricingPlan() != null && user.getPricingPlan().getName() != null) {
                responses.add(toUserInfo4Permit(user));
            }
        }
        return responses;
    }


    public UserStatusResponse toUserStatusResponse(User user) {
        UserStatusResponse response = new UserStatusResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setIsActiveUser(user.getIsActiveUser());
        return response;
    }

}
