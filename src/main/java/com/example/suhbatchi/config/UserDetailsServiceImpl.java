package com.example.suhbatchi.config;

import com.example.suhbatchi.repostory.UserRepostory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepostory userRepostory;

    public UserDetailsServiceImpl(UserRepostory userRepostory) {
        this.userRepostory = userRepostory;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepostory.findByPhoneNumber(phoneNumber)
                .map(user -> new User(
                        user.getClientId(),
                        user.getPasswordHash(),
                        new ArrayList<>())
                )
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phoneNumber));
    }
}
