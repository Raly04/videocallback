package com.nyaina.videocall.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
