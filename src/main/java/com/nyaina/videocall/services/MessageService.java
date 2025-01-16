package com.nyaina.videocall.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nyaina.videocall.dtos.MessageToUserDTO;
import com.nyaina.videocall.mappers.MessageMapper;
import com.nyaina.videocall.models.Message;
import com.nyaina.videocall.models.MessageToGroup;
import com.nyaina.videocall.models.MessageToUser;
import com.nyaina.videocall.repositories.MessageToGroupRepository;
import com.nyaina.videocall.repositories.MessageToUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageToUserRepository messageRepository;
    private final MessageToUserRepository messageToUserRepository;
    private final MessageToGroupRepository messageToGroupRepository;
    private final MessageMapper messageMapper;

    public void save(Message message) {
        if (message instanceof MessageToUser) {
            messageToUserRepository.save((MessageToUser) message);
        } else if (message instanceof MessageToGroup) {
            messageToGroupRepository.save((MessageToGroup) message);
        }
    }

    public List<MessageToUserDTO> getHistoryBetweenUser(String user1 , String user2) {
        return messageRepository.findConversationHistory(user1,user2).stream()
                .map(message -> messageMapper.toDTO(message))
                .toList();
    }
}
