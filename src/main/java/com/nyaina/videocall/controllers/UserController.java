package com.nyaina.videocall.controllers;

import com.nyaina.videocall.dtos.AvatarUploadDTOResponse;
import com.nyaina.videocall.dtos.JwtRefreshTokenRequest;
import com.nyaina.videocall.dtos.JwtRefreshTokenResponse;
import com.nyaina.videocall.models.RefreshToken;
import com.nyaina.videocall.models.User;
import com.nyaina.videocall.repositories.UserRepository;
import com.nyaina.videocall.services.FileService;
import com.nyaina.videocall.services.RefreshTokenService;
import com.nyaina.videocall.services.UserService;
import com.nyaina.videocall.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
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
    private final FileService fileService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    @Value("${upload.avatar.dir}")
    private String uploadDirectory;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        var arrayResponse = new ArrayList<String>();
        response.put("user", user);
        try {
            return ResponseEntity.ok(service.authenticateUser(user));
        } catch (BadCredentialsException e) {
            arrayResponse.add("Incorrect password");
            response.put("content", arrayResponse);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            arrayResponse.add("Username not found");
            response.put("content", arrayResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody User user, UriComponentsBuilder builder) {
        try {
            var savedUser = service.save(user, "default.jpg");
            URI location = builder.path("/user/{id}").buildAndExpand(savedUser.getId()).toUri();
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
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(service.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<?> getContacts(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getContacts(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody JwtRefreshTokenRequest request) {
        String refreshToken = request.getToken();
        System.out.println("RefreshToken : " + refreshToken);
        return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            return ResponseEntity.ok(JwtRefreshTokenResponse.builder().accessToken(jwtUtils.generateToken(user)).refreshToken(refreshToken).build());
        }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @PostMapping("/uploadUserAvatar")
    public ResponseEntity<?> saveFile(@RequestParam("userId") String userId, @RequestParam("avatar") MultipartFile avatar) {
        try {
            //find user by id
            var user = userService.findById(Long.valueOf(userId));
            //save into storage
            var uuid = fileService.saveImageToStorage(uploadDirectory, avatar);
            //delete old avatar if exists
            fileService.deleteImage(user.getAvatar());
            user.setAvatar(uuid + "_" + avatar.getOriginalFilename());
            //update user with new avatar
            userService.update(user);
            return ResponseEntity.ok(AvatarUploadDTOResponse.builder().content("File is saved successfully").build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/uploadTest")
    public ResponseEntity<?> uploadTest() throws IOException {
        return ResponseEntity.ok("You can continue");
    }

    @GetMapping("/avatar/{userId}")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable Long userId) throws IOException {
        // Fetch the user from the repository
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Get the avatar filename from the user entity
        String avatarFilename = user.getAvatar();

        // Use the fileService to load the image as a Resource
        Resource avatarResource = fileService.getImage(avatarFilename);

        // Determine the content type dynamically
        String contentType;
        try {
            // Determine the content type of the file
            contentType = Files.probeContentType(Paths.get(avatarResource.getURI()));
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default if unknown
            }
        } catch (IOException ex) {
            throw new IOException("Could not determine file type.", ex);
        }

        // Return the image as a ResponseEntity with the correct content type
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + user.getUsername() + "-avatar\"").body(avatarResource);
    }
}
