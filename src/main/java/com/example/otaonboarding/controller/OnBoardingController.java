package com.example.otaonboarding.controller;

import com.example.otaonboarding.dto.ValidationResponse;
import com.example.otaonboarding.services.OnBoardingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OnBoardingController {
    @Autowired
    private OnBoardingService onBoardingService;

    @PostMapping(value = "/login")
    public String startRegistration() throws JsonProcessingException {
        return onBoardingService.login();

    }

    @PostMapping(value = "/generateOtp")
    public List<String> generateOtp(@RequestParam String otpNum, @RequestHeader("Authorization") String vat ) throws JsonProcessingException {
        return onBoardingService.generateOTP(otpNum, vat);
    }

    @PostMapping(value = "/validate")
    public ValidationResponse validateOTP(@RequestParam String otp, @RequestHeader("Authorization") String vat) throws JsonProcessingException {

        return onBoardingService.ValidateOtp(otp, vat);
    }
}