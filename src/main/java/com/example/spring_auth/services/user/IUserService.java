package com.example.spring_auth.services.user;

import com.example.spring_auth.dtos.UserDTO;
import com.example.spring_auth.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;
}
