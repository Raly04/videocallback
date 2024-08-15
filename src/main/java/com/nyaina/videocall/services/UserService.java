package com.nyaina.videocall.services;

import com.nyaina.videocall.models.Group;
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

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;

    public Map<String, Object> authenticateUser(User user) throws IOException {
        Map<String, Object> response = new HashMap<>();
        var arrayResponse = new ArrayList<String>();
        var connectedUser = repository.findByUsername(user.getUsername());
        if (connectedUser.isEmpty()) throw new UsernameNotFoundException("User not found");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        arrayResponse.add(jwtUtils.generateToken(userDetails));
        arrayResponse.add(refreshTokenService.createRefreshToken(userDetails.getUsername()).getToken());
        if (bCryptPasswordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            response.put("content", arrayResponse);
            response.put("user", userDetails);
            return response;
        } else throw new BadCredentialsException("Incorrect password");
    }

    public User save(User user, String avatar) {
        var encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        Set<Group> groups = new HashSet<>();
        return repository.save(User.builder().avatar(avatar).mail(user.getMail()).groups(groups).password(encryptedPassword).username(user.getUsername()).build());
    }

    public void update(User user) {
        repository.save(user);
    }

    public Set<User> getContacts(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found")).getContacts();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
