package com.aphatheology.cshoppingbackend.controller;

import com.aphatheology.cshoppingbackend.dto.ApiResponse;
import com.aphatheology.cshoppingbackend.dto.AuthenticationResponse;
import com.aphatheology.cshoppingbackend.dto.LoginDto;
import com.aphatheology.cshoppingbackend.dto.UserDto;
import com.aphatheology.cshoppingbackend.exception.ExistingEmailException;
import com.aphatheology.cshoppingbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid UserDto registerBody) {
        try {
            AuthenticationResponse user = authService.register(registerBody);

            return new ResponseEntity<>(new ApiResponse(true, "Registration Successful", user), HttpStatus.CREATED);
        } catch(ExistingEmailException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginDto loginBody) {
        try {
            AuthenticationResponse user = authService.login(loginBody);

            return new ResponseEntity<>(new ApiResponse(true, "Login Successful", user), HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
