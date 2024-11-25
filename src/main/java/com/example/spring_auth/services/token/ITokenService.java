package com.example.spring_auth.services.token;

import com.example.spring_auth.models.Token;
import com.example.spring_auth.models.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, User user) throws Exception;
}