package ru.spbau.bocharov.m2u.core;

public interface IChatRoomListener {

    void processUsernameChange(String newUsername);

    void processRoomChange(ChatRoom room);
}
