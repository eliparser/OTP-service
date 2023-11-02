package com.example.OTP_Exercise.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

import static com.example.OTP_Exercise.utils.Utils.START_URL;
import static com.example.OTP_Exercise.utils.Utils.URL_END;
import static com.example.OTP_Exercise.utils.Utils.extractTempFromGson;
import static com.example.OTP_Exercise.utils.Utils.getRandomCity;

@Service
@RequiredArgsConstructor
@Log4j2
public class WeatherApiService {
    private OkHttpClient okHttpClient;
    private Request request;
    private Response response;
    private String cityTemperature;

    @Autowired
    private final EmailService emailService;

    public String getCodeFromWeatherApi(String userMailAddress) {
        StringBuilder stringBuilder = new StringBuilder();
        String finalCode;

        for (int i = 0; i < 3; i++) {
            String cityTemperature = getCityTemperature();

            stringBuilder.append(cityTemperature);
        }

        finalCode = stringBuilder.toString();

        log.info("code generated from weather api is - {}", finalCode);
        sendEmail(finalCode, userMailAddress);

        return finalCode;
    }

    public String getCityTemperature() {
        String city = getRandomCity();
        String url = START_URL + city + URL_END;
        DecimalFormat df = new DecimalFormat("00");

        int temperature = getTemperatureFromAPI(url);

        if (temperature < 10) {
            cityTemperature = df.format(temperature);
        } else {
            cityTemperature = String.valueOf(temperature);
        }

        log.info("city:  {}, temperature: - {}", city, temperature);
        return cityTemperature;
    }

    public int getTemperatureFromAPI(String path) {
        try {
            okHttpClient = new OkHttpClient();
            request = new Request.Builder()
                    .url(path)
                    .get()
                    .build();
            response = okHttpClient.newCall(request).execute();
            String data = response.body().string();

            return extractTempFromGson(data);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String sendEmail(String finalCode, String userMail) {

        String subject = "this is code creator from weather of three cities";
        String body = "This is a code: \n" + finalCode;

        emailService.sendEmail(userMail, subject, body);

        return "Email sent successfully!";
    }
}
