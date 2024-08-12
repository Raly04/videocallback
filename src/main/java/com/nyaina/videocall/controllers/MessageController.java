package com.nyaina.videocall.controllers;

import com.nyaina.videocall.dtos.HistoryRequest;
import com.nyaina.videocall.models.MessageToGroup;
import com.nyaina.videocall.models.MessageToUser;
import com.nyaina.videocall.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendToUser")
    public void sendMessage(@Payload MessageToUser message) {
        try {
            System.out.println("MESSAGE : "+message.getSender().getId());
            // Send to individual user
            simpMessagingTemplate.convertAndSend("/queue/" + message.getReceiver().getId(), message);
            messageService.save(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/sendToGroup")
    public void sendMessage(@Payload MessageToGroup message) {
        try {
            // Send to group
            simpMessagingTemplate.convertAndSend("/topic/groups/" + message.getReceiver().getId(), message);
            messageService.save(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/message/getHistoryBetweenTwoUser")
    public ResponseEntity<?> getHistory(@RequestBody HistoryRequest request) {
        try {
            return ResponseEntity.ok(messageService.getHistoryBetweenUser(request.getFrom(), request.getTo()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
