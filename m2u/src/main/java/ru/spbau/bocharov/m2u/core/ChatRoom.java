package ru.spbau.bocharov.m2u.core;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class ChatRoom {

    @Getter
    private String username;
    @Getter
    private final String id;
    @Getter
    private final List<String> history = new LinkedList<>();

    ChatRoom(String roomId, String user) {
        id = roomId;
        username = user;
    }

    void appendMessage(String message) {
        history.add(message);
    }

    void setUsername(String name) {
        username = name;
    }
}
