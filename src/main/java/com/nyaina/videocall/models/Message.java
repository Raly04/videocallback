package com.nyaina.videocall.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nyaina.videocall.enums.MessageType;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public abstract class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User sender;

    @CreationTimestamp
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    private MessageType type;
    // Getters and setters
}
