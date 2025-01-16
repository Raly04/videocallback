package com.nyaina.videocall.mappers;

import org.springframework.stereotype.Component;

import com.nyaina.videocall.dtos.MessageToUserDTO;
import com.nyaina.videocall.models.MessageToUser;
import com.nyaina.videocall.models.User;
import com.nyaina.videocall.services.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserService userService;

    public MessageToUser toEntity(MessageToUserDTO dto) {
        if (dto == null) return null;

        MessageToUser message = new MessageToUser();
        message.setId(dto.getId());
        message.setContent(dto.getContent());
        message.setType(dto.getType());

        User sender = userService.findById(dto.getSenderId());
        User receiver = userService.findById(dto.getReceiverId());

        message.setSender(sender);
        message.setReceiver(receiver);

        return message;
    }

    public MessageToUserDTO toDTO(MessageToUser entity) {
        if (entity == null) return null;

        MessageToUserDTO dto = new MessageToUserDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setDate(entity.getDate());
        dto.setType(entity.getType());
        
        if (entity.getSender() != null) {
            dto.setSenderId(entity.getSender().getId());
        }
        
        if (entity.getReceiver() != null) {
            dto.setReceiverId(entity.getReceiver().getId());
        }

        return dto;
    }
}