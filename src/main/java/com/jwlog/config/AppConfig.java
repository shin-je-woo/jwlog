package com.jwlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Data
@ConfigurationProperties(prefix = "jwlog-auth")
public class AppConfig {

    private String jwtKey;

    public byte[] getJwtKey() {
        return Base64.getDecoder().decode(jwtKey);
    }
}
