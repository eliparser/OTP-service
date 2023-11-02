package com.example.OTP_Exercise.service;

import com.example.OTP_Exercise.models.UserData;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService {
    private final Gson gson;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UserService(StringRedisTemplate stringRedisTemplate) {
        gson = new Gson();
        this.redisTemplate = stringRedisTemplate;
    }

    public void saveUserData(UserData userData) {
        String userDataJson = serializationLogic(userData);

        log.info("email: {}", userData.getMail());
        redisTemplate.opsForValue().set(userData.getMail(), userDataJson);
    }

    public UserData getUserData(String mail) {
        String userDataJson = redisTemplate.opsForValue().get(mail);

        if (userDataJson != null) {
            log.info("email: {}", mail);
            return deserializationLogic(userDataJson);
        } else {

            return null;
        }
    }

    private String serializationLogic(UserData userData) {
        return gson.toJson(userData);
    }

    private UserData deserializationLogic(String userDataJson) {
        return gson.fromJson(userDataJson, UserData.class);
    }
}
