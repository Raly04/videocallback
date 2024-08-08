package com.nyaina.videocall.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AvatarUploadDTORequest {
    private Long userId;
    private MultipartFile avatar;
}
