package com.example.OTP_Exercise.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Random;

@UtilityClass
public class Utils {
    public static final String START_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    public static final String URL_END = "?unitGroup=metric&key=HS735P6CXEBFHRC8V9JQ6GQP3&contentType=json";

    public static int extractTempFromGson(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(data);

            JsonNode currentConditions = rootNode.path("currentConditions");

            int temperature = (int) currentConditions.path("temp").asInt();

            if (temperature < 0) {
                temperature = temperature * -1;
            }

            return temperature;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getRandomCity() {
        List<String> cities = getCities();

        Random rand = new Random();
        int randomIndex = rand.nextInt(cities.size());
        return cities.get(randomIndex);
    }

    public static List<String> getCities() {
        return List.of("Amsterdam",
                "Paris",
                "London",
                "Jerusalem",
                "Toronto",
                "Budapest",
                "Santiago",
                "Beijing",
                "Dublin",
                "Rome",
                "Amman",
                "Sofia",
                "Prague",
                "Berlin",
                "Lisbon",
                "Singapore"
        );
    }
}
