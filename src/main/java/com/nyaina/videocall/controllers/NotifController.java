package com.nyaina.videocall.controllers;

import com.nyaina.videocall.enums.NotifType;
import com.nyaina.videocall.models.FriendRequestNotif;
import com.nyaina.videocall.models.Notif;
import com.nyaina.videocall.models.User;
import com.nyaina.videocall.services.NotifService;
import com.nyaina.videocall.services.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NotifController {
    private final NotifService notifService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/notif/getByReceiver/{id}")
    public ResponseEntity<?> getNotifByReceiverId(@PathVariable Long id) {
        try {
            User receiver = userService.findById(id);
            List<Notif> notifs = notifService.findByReceiver(receiver);
            return ResponseEntity.ok(notifs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @MessageMapping("/friendRequestNotif")
    public void send(@Payload FriendRequestNotif notif) {
        try {
            if (
                !notifService.existsBySenderAndReceiverAndType(
                    notif.getSender(),
                    notif.getReceiver(),
                    NotifType.FRIEND_REQUEST
                )
            ) {
                simpMessagingTemplate.convertAndSend(
                    "/notif/" + notif.getReceiver().getId(),
                    notif
                );
                notifService.save(notif);
            } else {
                throw new RuntimeException("Notification already exists");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
