package com.nyaina.videocall.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nyaina.videocall.enums.NotifType;
import com.nyaina.videocall.models.FriendRequestNotif;
import com.nyaina.videocall.models.Notif;
import com.nyaina.videocall.models.User;
import com.nyaina.videocall.repositories.FriendRequestNotifRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotifService {

    private final FriendRequestNotifRepository friendRequestNotifRepository;

    public Boolean existsBySenderAndReceiverAndType(
        User sender,
        User receiver,
        NotifType type
    ) {
        return friendRequestNotifRepository.existsBySenderAndReceiverAndType(
            sender,
            receiver,
            type
        );
    }

    public List<Notif> findByUser(User user) {
        return friendRequestNotifRepository.findByReceiver(user);
    }

    public Notif save(Notif notif) {
        if (notif instanceof FriendRequestNotif _notif) {
            Boolean alreadyExist =
                friendRequestNotifRepository.existsBySenderAndReceiverAndType(
                    _notif.getSender(),
                    _notif.getReceiver(),
                    _notif.getType()
                );
            if (alreadyExist) {
                return friendRequestNotifRepository
                    .findBySenderAndReceiverAndType(
                        _notif.getSender(),
                        _notif.getReceiver(),
                        _notif.getType()
                    )
                    .orElseThrow();
            } else {
                return friendRequestNotifRepository.save(_notif);
            }
        } else {
            throw new RuntimeException("Unsupported notification type");
        }
    }
}
