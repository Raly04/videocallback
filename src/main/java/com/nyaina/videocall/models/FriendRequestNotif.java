package com.nyaina.videocall.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friend_request_notifs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestNotif extends Notif {
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    private Boolean accepted;
}
