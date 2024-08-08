package com.nyaina.videocall.services;

import com.nyaina.videocall.models.Message;
import com.nyaina.videocall.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message save (Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getHistory(String user1 , String user2) {
        return messageRepository.findConversationHistory(user1,user2);
    }
}
