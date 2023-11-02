package com.example.OTP_Exercise.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("UserData")
@Data
public class UserData implements Serializable {

    public UserData(String mail, String code, long time) {
        this.mail = mail;
        this.code = code;
        this.time = time;
    }

    @Id
    private String mail;
    private String code;
    private long time;
}
