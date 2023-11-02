package com.example.OTP_Exercise.controller;

import com.example.OTP_Exercise.models.UserData;
import com.example.OTP_Exercise.service.UserService;
import com.example.OTP_Exercise.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OTPController {
    @Autowired
    private final WeatherApiService weatherApiService;

    @Autowired
    private final UserService userService;
    private String code;
    private UserData userData;

    @PostMapping("/email")
    public String generateCode(@RequestParam String email) {
        code = weatherApiService.getCodeFromWeatherApi(email);
        long messageTime = System.currentTimeMillis();

        userData = new UserData(email, code, messageTime);
        userService.saveUserData(userData);

        log.info("user data {}", userData);
        return "Code: " + code + ". generated for email " + email;
    }

    @GetMapping("/code/{email}")
    public String getCode(@PathVariable String email) {
        if (code == null || userData == null) {
            return "There is no code yet for this email address\n" +
                    "Please send the email address first";
        }

        userData = userService.getUserData(email);
        code = userData.getCode();
        log.info("user data: {}", userData);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - userData.getTime();
        long elapsedMinutes = elapsedTime / 1000 / 60;

        if (elapsedMinutes < 2) {
            log.info("code-  {}", code);
            return "Code: " + code;
        } else {
            return "More than 5 minutes have passed, please resend your email to receive a new code";
        }
    }
}
