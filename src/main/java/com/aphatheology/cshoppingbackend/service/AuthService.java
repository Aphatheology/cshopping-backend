package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.dto.AuthenticationResponse;
import com.aphatheology.cshoppingbackend.dto.LoginDto;
import com.aphatheology.cshoppingbackend.dto.UserDto;
import com.aphatheology.cshoppingbackend.entity.Role;
import com.aphatheology.cshoppingbackend.entity.Users;
import com.aphatheology.cshoppingbackend.exception.ExistingEmailException;
import com.aphatheology.cshoppingbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApplicationEventPublisher publisher;

    public Users map2Entity(UserDto userDto) {
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setFullname(userDto.getFullname());
        user.setRole(Role.USER);
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        return user;
    }

    public AuthenticationResponse register(UserDto registerBody) {
        Optional<Users> existingUser = this.userRepository.findUserByEmail(registerBody.getEmail());

        if (existingUser.isPresent()) throw new ExistingEmailException("User with this Email already exist");

        Users user = map2Entity(registerBody);
        this.userRepository.save(user);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .role(user.getRole())
                .accessToken(this.jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse login(LoginDto loginBody) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword())
        );

        var user = this.userRepository.findUserByEmail(loginBody.getEmail()).orElseThrow();

        var jwtToken = this.jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .role(user.getRole())
                .accessToken(jwtToken)
                .build();
    }

}
