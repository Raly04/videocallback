package com.nyaina.videocall.services;

import com.nyaina.videocall.models.RefreshToken;
import com.nyaina.videocall.repositories.RefreshTokenRepository;
import com.nyaina.videocall.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Value("${jwt.refresh.token.expire.date}")
    private Long refreshTokenExpireDate;

    public RefreshToken createRefreshToken(String username){
        var user = userRepository.findByUsername(username).orElseThrow();
        if(refreshTokenRepository.findByUser(user).isPresent()){
            return refreshTokenRepository.findByUser(user).get();
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expireDate(Instant.now().plusMillis(refreshTokenExpireDate)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }
    }



    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        System.out.println("VERIFY EXPIRATION : "+ token.getExpireDate());
        if(token.getExpireDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}