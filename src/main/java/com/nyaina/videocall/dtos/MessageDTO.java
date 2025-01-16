package com.nyaina.videocall.dtos;

import java.sql.Timestamp;

import com.nyaina.videocall.enums.MessageType;

import lombok.Data;

@Data
public abstract class MessageDTO {
    private Long id;
    private Long senderId;
    private String content;
    private Timestamp date;
    private MessageType type;
}