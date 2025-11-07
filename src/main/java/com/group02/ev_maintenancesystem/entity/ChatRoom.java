package com.group02.ev_maintenancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group02.ev_maintenancesystem.enums.ChatRoomStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "chat_rooms")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;


    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ChatRoomStatus status;

    @ManyToMany
    @JoinTable(
            name = "chatroom_members",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<User> members = new HashSet<>();

}
