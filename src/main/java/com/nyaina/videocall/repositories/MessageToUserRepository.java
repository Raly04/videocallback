package com.nyaina.videocall.repositories;

import com.nyaina.videocall.models.MessageToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageToUserRepository extends JpaRepository<MessageToUser, Long> {
    @Query("SELECT m FROM MessageToUser m WHERE " +
            "(m.sender.username = :user1 AND m.receiver.username = :user2) OR " +
            "(m.sender.username = :user2 AND m.receiver.username = :user1) " +
            "ORDER BY m.date")
    List<MessageToUser> findConversationHistory(String user1, String user2);
}
