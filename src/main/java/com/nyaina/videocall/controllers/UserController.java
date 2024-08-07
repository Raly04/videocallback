package com.nyaina.videocall.controllers;

import com.nyaina.videocall.dtos.JwtRefreshTokenRequest;
import com.nyaina.videocall.dtos.JwtRefreshTokenResponse;
import com.nyaina.videocall.models.RefreshToken;
import com.nyaina.videocall.models.User;
import com.nyaina.videocall.services.RefreshTokenService;
import com.nyaina.videocall.services.UserService;
import com.nyaina.videocall.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final Path rootLocation = Paths.get("");

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        var arrayResponse = new ArrayList<String>();
        response.put("user", user);
        try {
            return ResponseEntity.ok(service.authenticateUser(user));
        } catch (BadCredentialsException e) {
            arrayResponse.add( "Incorrect password");
            response.put("content",arrayResponse);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            arrayResponse.add( "Username not found");
            response.put("content", arrayResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody User user, UriComponentsBuilder builder) {
        try {
            var savedUser = service.save(user);
            URI location = builder
                    .path("/user/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e){ 
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody JwtRefreshTokenRequest request){
        String refreshToken = request.getToken();
        System.out.println("RefreshToken : "+refreshToken);
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    return ResponseEntity.ok(JwtRefreshTokenResponse.builder()
                            .accessToken(jwtUtils.generateToken(user))
                            .refreshToken(refreshToken)
                            .build());
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @PostMapping("/saveFile")
    public ResponseEntity<?> saveFile(@RequestBody MultipartFile file) {
        System.out.println(rootLocation);
        return ResponseEntity.ok("HAHAHA");
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(service.findById(id));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
