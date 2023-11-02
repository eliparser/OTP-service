package com.example.OTP_Exercise.service;

import org.junit.jupiter.api.Test;

import static com.example.OTP_Exercise.utils.Utils.extractTempFromGson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WeatherApiServiceTest {

    private EmailService emailService = mock(EmailService.class);

    private WeatherApiService weatherApiService = new WeatherApiService(emailService);

    @Test
    void testGetCodeFromWeatherApi() {
        doAnswer(doNothing -> {
            return "";
        })
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        String code = weatherApiService.getCodeFromWeatherApi("test@example.com");

        verify(emailService).sendEmail(anyString(), anyString(), anyString());
        assertEquals(6, code.length());
    }


    @Test
    void testExtractTempFromJson() {
        String json = "{\"currentConditions\": {\"temp\": 20}}";
        int temp = extractTempFromGson(json);

        assertEquals(20, temp);
    }

    @Test
    void testSendEmail() {
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        String response = weatherApiService.sendEmail("123456", "test@test.com");

        verify(emailService).sendEmail(eq("test@test.com"), anyString(), anyString());
        assertEquals("Email sent successfully!", response);
    }

}