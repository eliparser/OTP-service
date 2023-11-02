package com.example.OTP_Exercise.controller;

import com.example.OTP_Exercise.models.UserData;
import com.example.OTP_Exercise.service.UserService;
import com.example.OTP_Exercise.service.WeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class OTPControllerTest {
    @InjectMocks
    private OTPController otpController;
    @Mock
    private WeatherApiService weatherApiService;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(otpController).build();
    }

    @Test
    public void testGenerateCode() throws Exception {
        when(weatherApiService.getCodeFromWeatherApi("test@test.com")).thenReturn("123456");

        doNothing().when(userService).saveUserData(Mockito.any(UserData.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/email")
                        .param("email", "test@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Code: 123456. generated for email test@test.com"));

        verify(weatherApiService).getCodeFromWeatherApi("test@test.com");

        ArgumentCaptor<UserData> argumentCaptor = ArgumentCaptor.forClass(UserData.class);
        verify(userService).saveUserData(argumentCaptor.capture());
        assertEquals("", "test@test.com", argumentCaptor.getValue().getMail());
        assertEquals("", "123456", argumentCaptor.getValue().getCode());
    }

    @Test
    public void testGenerateCodeWithNoEmail() throws Exception {
        // Perform the POST request to /email without an email parameter
        mockMvc.perform(MockMvcRequestBuilders.post("/email"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetCode() throws Exception {
        UserData userData = new UserData("test@test.com", "123456", System.currentTimeMillis());
        when(weatherApiService.getCodeFromWeatherApi("test@test.com")).thenReturn("123456");

        doNothing().when(userService).saveUserData(Mockito.any(UserData.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/email")
                        .param("email", "test@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Code: 123456. generated for email test@test.com"));

        when(userService.getUserData("test@test.com")).thenReturn(userData);

        mockMvc.perform(MockMvcRequestBuilders.get("/code/test@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Code: 123456"));
    }

    @Test
    public void testGetCodeWithNoCode() throws Exception {
        when(userService.getUserData("test@test.com")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/code/test@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("There is no code yet for this email address\n" +
                        "Please send the email address first"));
    }
}
