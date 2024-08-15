package com.nyaina.videocall.models;

import com.nyaina.videocall.enums.NotifType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Notif {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @Enumerated(EnumType.STRING)
    private NotifType type;
    @CreationTimestamp
    private Timestamp date;
}
