package ru.spbau.bocharov.m2u.core;

import ru.spbau.bocharov.m2u.network.IIOService;
import ru.spbau.bocharov.m2u.network.IMessageReceiver;
import ru.spbau.bocharov.m2u.protocol.Protocol;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Chat implements IMessageReceiver {

    private String username;
    private final String thisUserId;
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    private final IIOService ioService;
    private final Map<String, Long> userToConnection = new ConcurrentHashMap<>();

    public Chat(IIOService io, String userId) {
        ioService = io;
        thisUserId = userId;

        ioService.registerMessageReceiver(this);
    }

    public void start() throws IOException {
        ioService.start();
    }

    public void sendMessage(String roomId, String text) {
        long connection = userToConnection.get(roomId); // userId == roomId

        try {
            ioService.send(connection, createMessage(thisUserId, username, text));
        } catch (Exception e) {
            // TODO: print error?
            e.printStackTrace();
        }
    }

    public void setUsername(String name) {
        username = name;
        for (long connection: userToConnection.values()) {
            try {
                ioService.send(connection, createMessage(thisUserId, username, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyUsernameChanged();
    }

    public void closeRoom(String roomId) {
        long connection = userToConnection.get(roomId); // userId == roomId
        try {
            ioService.close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rooms.remove(roomId);
        userToConnection.remove(roomId);
    }

    @Override
    public void receive(long connectionId, Protocol.Message message) {
        String userId = message.getUserId();
        if (!rooms.containsKey(userId)) {  // roomId == userId
            userToConnection.put(userId, connectionId);
            rooms.put(userId, new ChatRoom(userId, message.getUsername()));
        }

        ChatRoom room = rooms.get(userId);
        String username = message.getUsername();
        if (!Objects.equals(room.getUsername(), username)) {
            room.setUsername(username);
            notifyRoomChanged(room);
        }

        if (!message.getText().isEmpty()) {
            room.appendMessage(message.getText());
            notifyRoomChanged(room);
        }
    }

    private void notifyUsernameChanged() {
        // TODO: implement me!!
    }

    private void notifyRoomChanged(ChatRoom room) {
        // TODO: implement me!!
    }

    private static Protocol.Message createMessage(String userId, String user, String text) {
        Protocol.Message.Builder builder = Protocol.Message.newBuilder();
        builder.setUserId(userId);
        builder.setUsername(user);
        builder.setText(text);
        return builder.build();
    }
}
