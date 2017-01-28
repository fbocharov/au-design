package ru.spbau.bocharov.m2u.core;

import lombok.Getter;
import ru.spbau.bocharov.m2u.network.IIOService;
import ru.spbau.bocharov.m2u.network.IMessageReceiver;
import ru.spbau.bocharov.m2u.protocol.Protocol;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chat implements IMessageReceiver {

    @Getter
    private String username;
    private final Map<Long, ChatRoom> rooms = new ConcurrentHashMap<>();
    private final List<IChatRoomListener> roomListeners = new CopyOnWriteArrayList<>();

    private final IIOService ioService;

    public Chat(IIOService io, String user) {
        ioService = io;
        username = user;

        ioService.registerMessageReceiver(this);
    }

    public void start() throws IOException {
        ioService.start();
    }

    public void createRoom(String ip, short port) throws IOException {
        long connectionId = ioService.connect(ip, port);
        ChatRoom room = new ChatRoom(connectionId, String.format("%s:%s", ip, port));
        rooms.put(connectionId, room);
        notifyRoomChanged(room);
    }

    public void sendMessage(long roomId, String text) {
        try {
            ioService.send(roomId, createMessage(username, text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String name) {
        username = name;
        for (ChatRoom room: rooms.values()) {
            try {
                ioService.send(room.getId(), createMessage(username, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyUsernameChanged();
    }

    public void closeRoom(long roomId) {
        try {
            ioService.close(roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rooms.remove(roomId);
    }

    public void registerChatRoomListener(IChatRoomListener listener) {
        roomListeners.add(listener);
    }

    @Override
    public void receive(long connectionId, Protocol.Message message) {
        if (!rooms.containsKey(connectionId)) {  // roomId == userId
            rooms.put(connectionId, new ChatRoom(connectionId, message.getUsername()));
        }

        ChatRoom room = rooms.get(connectionId);
        String username = message.getUsername();
        if (!Objects.equals(room.getUsername(), username)) {
            room.setUsername(username);
        }

        if (!message.getText().isEmpty()) {
            room.appendMessage(message.getText());
        }
        notifyRoomChanged(room);
    }

    private void notifyUsernameChanged() {
        for (IChatRoomListener listener: roomListeners) {
            listener.processUsernameChange(username);
        }
    }

    private void notifyRoomChanged(ChatRoom room) {
        for (IChatRoomListener listener: roomListeners) {
            listener.processRoomChange(room);
        }
    }

    private static Protocol.Message createMessage(String user, String text) {
        Protocol.Message.Builder builder = Protocol.Message.newBuilder();
        builder.setUsername(user);
        builder.setText(text);
        return builder.build();
    }
}
