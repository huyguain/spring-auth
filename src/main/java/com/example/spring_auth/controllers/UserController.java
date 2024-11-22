package com.example.spring_auth.controllers;

import com.example.spring_auth.components.LocalizationUtils;
import com.example.spring_auth.dtos.UserDTO;
import com.example.spring_auth.models.User;
import com.example.spring_auth.responses.ResponseObject;
import com.example.spring_auth.responses.UserResponse;
import com.example.spring_auth.services.user.IUserService;
import com.example.spring_auth.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final LocalizationUtils localizationUtils;
    private final IUserService userService;

    @GetMapping("/login")
    public String login() throws Exception {
        try {
            return "Success!";
        } catch (Exception e) {
            return "Error!";
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(errorMessage.toString())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("User registered successfully")
                        .status(HttpStatus.OK)
                        .data(UserResponse.fromUser(user))
                        .build()
        );
    }
}
