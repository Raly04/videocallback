package com.nyaina.videocall.controllers;

import com.nyaina.videocall.models.FriendRequestNotif;
import com.nyaina.videocall.services.NotifService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotifController {
    private final NotifService notifService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/friendRequestNotif")
    public FriendRequestNotif send(@Payload FriendRequestNotif notif) {
        try {
            simpMessagingTemplate.convertAndSend("/notif/" + notif.getReceiver().getId() , notif);
            return (FriendRequestNotif) notifService.save(notif);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
