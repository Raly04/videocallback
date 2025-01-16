package com.nyaina.videocall.models;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nyaina.videocall.enums.NotifType;

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
@JsonIgnoreProperties(ignoreUnknown = true)
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

    private Boolean seen;


    @CreationTimestamp
    private Timestamp date;
}
