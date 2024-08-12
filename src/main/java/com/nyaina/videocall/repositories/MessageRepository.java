package com.nyaina.videocall.repositories;

import com.nyaina.videocall.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m")
    List<Message> findConversationHistory(@Param("user1") String user1, @Param("user2") String user2);
}
