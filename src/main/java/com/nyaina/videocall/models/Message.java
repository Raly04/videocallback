package com.nyaina.videocall.models;

import com.nyaina.videocall.enums.MessageType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @CreationTimestamp
    private Timestamp date;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    // Getters and setters
}