package com.nyaina.videocall.repositories;

import com.nyaina.videocall.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message , Long> {
}
