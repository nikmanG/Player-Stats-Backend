package io.github.nikmang.playerinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String AUTH_LOGIN_URL = "/authenticate";

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${admin_password}")
    private String password;
    
    @Value("${admin_username}")
    private String username;
    
    // JWT token defaults
    private String tokenHeader = "Authorization";
    private String tokenPrefix = "Bearer ";
    private String tokenType = "JWT";
    private String tokenIssuer = "secure-api";
    private String tokenAudience = "secure-app";

    public byte[] getJwtSecret() {
        return jwtSecret.getBytes();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public String getTokenAudience() {
        return tokenAudience;
    }
}
