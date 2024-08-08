package com.nyaina.videocall.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {
    @Value("${upload.avatar.dir}")
    private String uploadUserAvatarDirectory;

    // Save image in a local directory
    public String saveImageToStorage(String uploadDirectory, MultipartFile imageFile) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        Path uploadPath = Path.of(uploadDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    // To view an image
    public Resource getImage(String filename) throws IOException {
        Path filePath = Paths.get(uploadUserAvatarDirectory).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new NoSuchFileException(filename);
        }
    }

    // Delete an image
    public String deleteImage(String imageDirectory, String imageName) throws IOException {
        Path imagePath = Path.of(imageDirectory, imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }
}
