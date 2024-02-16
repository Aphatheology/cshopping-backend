package com.aphatheology.cshoppingbackend.dto;

import com.aphatheology.cshoppingbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String email;
    private String fullname;
    private Role role;
    private String accessToken;
}
