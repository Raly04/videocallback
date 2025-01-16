package com.nyaina.videocall.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageToUserDTO extends MessageDTO {
    private Long receiverId;
}