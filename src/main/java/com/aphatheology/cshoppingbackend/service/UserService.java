package com.aphatheology.cshoppingbackend.service;

import com.aphatheology.cshoppingbackend.dto.UpdateUserDto;
import com.aphatheology.cshoppingbackend.dto.UserDto;
import com.aphatheology.cshoppingbackend.dto.UserResponseDto;
import com.aphatheology.cshoppingbackend.entity.Role;
import com.aphatheology.cshoppingbackend.entity.Users;
import com.aphatheology.cshoppingbackend.exception.ResourceNotFoundException;
import com.aphatheology.cshoppingbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UserResponseDto map2Dto(Users user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setRole(user.getRole().toString());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());
        return userResponseDto;
    }

    public Users map2Entity(UserDto userDto) {
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setRole(Role.USER);
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        return user;
    }

    public List<UserResponseDto> getAllUsers() {
        List<Users> users = userRepository.findAll();

        return users.stream().map(this::map2Dto).toList();
    }

    public Users createUser(UserDto userBody) {
        Users user = map2Entity(userBody);
        return userRepository.save(user);
    }

    public UserResponseDto getUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found"));
        return map2Dto(user);
    }

    public UserResponseDto updateUser(Long userId, UpdateUserDto userBody) {
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found"));

        this.modelMapper.map(userBody, user);
        userRepository.save(user);
        return map2Dto(user);
    }

    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User Not Found"));
        userRepository.delete(user);
    }
}
