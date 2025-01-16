package com.nyaina.videocall.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nyaina.videocall.enums.MessageType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

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
