package com.nyaina.videocall.services;

import com.nyaina.videocall.models.User;
import com.nyaina.videocall.repositories.UserRepository;
import com.nyaina.videocall.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Map<String,Object> authenticateUser(User user) {
        Map<String,Object> response = new HashMap<>();
        if(repository.findByUsername(user.getUsername()).isEmpty()) throw new UsernameNotFoundException("User not found");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        if(bCryptPasswordEncoder.matches(user.getPassword(),userDetails.getPassword())) {
            response.put("content",jwt);
            response.put("user",userDetails);
            return response;
        } else throw new BadCredentialsException("Incorrect password");
    }

    public User save(User user) {
        var encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        return repository.save(User.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .mail(user.getMail())
                        .password(encryptedPassword)
                        .username(user.getUsername())
                .build());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
