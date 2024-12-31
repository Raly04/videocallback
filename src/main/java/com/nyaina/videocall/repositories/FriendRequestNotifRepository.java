package com.nyaina.videocall.repositories;

import com.nyaina.videocall.enums.NotifType;
import com.nyaina.videocall.models.FriendRequestNotif;
import com.nyaina.videocall.models.Notif;
import com.nyaina.videocall.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestNotifRepository
    extends JpaRepository<FriendRequestNotif, Long> {
    Boolean existsBySenderAndReceiverAndType(
        User sender,
        User receiver,
        NotifType type
    );
    Optional<FriendRequestNotif> findBySenderAndReceiverAndType(
        User sender,
        User receiver,
        NotifType type
    );
    List<Notif> findByReceiver(User receiver);
}
