package com.nyaina.videocall.controllers;

import com.nyaina.videocall.dtos.HistoryRequest;
import com.nyaina.videocall.models.Message;
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

    @MessageMapping("/send")
    public void sendMessage(@Payload Message message) {
        try {
            if (message.getRecipient().startsWith("group:")) {
                // Send to group
                simpMessagingTemplate.convertAndSend("/topic/groups/" + message.getRecipient().substring(6), message);
            } else {
                // Send to individual user
                simpMessagingTemplate.convertAndSend("/queue/"+message.getRecipient(), message);
                System.out.println("SEND TO :"+message.getRecipient()+ "/queue/messages" + message.getContent());
            }
            messageService.save(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/message/getHistoryBetweenTwoUser")
    public ResponseEntity<?> getHistory(@RequestBody HistoryRequest request) {
        try{
            return ResponseEntity.ok(messageService.getHistory(request.getFrom(), request.getTo()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}
