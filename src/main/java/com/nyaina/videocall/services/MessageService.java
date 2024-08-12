package com.nyaina.videocall.services;

import com.nyaina.videocall.models.Message;
import com.nyaina.videocall.models.MessageToGroup;
import com.nyaina.videocall.models.MessageToUser;
import com.nyaina.videocall.repositories.MessageToGroupRepository;
import com.nyaina.videocall.repositories.MessageToUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageToUserRepository messageRepository;
    private final MessageToUserRepository messageToUserRepository;
    private final MessageToGroupRepository messageToGroupRepository;

    public void save(Message message) {
        if (message instanceof MessageToUser) {
            messageToUserRepository.save((MessageToUser) message);
        } else if (message instanceof MessageToGroup) {
            messageToGroupRepository.save((MessageToGroup) message);
        }
    }

    public List<MessageToUser> getHistoryBetweenUser(String user1 , String user2) {
        return messageRepository.findConversationHistory(user1,user2);
    }
}
