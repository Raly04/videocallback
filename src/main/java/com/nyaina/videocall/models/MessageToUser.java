package com.nyaina.videocall.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages_to_user")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
@NoArgsConstructor
@AllArgsConstructor
public class MessageToUser extends Message {

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User receiver;
}
