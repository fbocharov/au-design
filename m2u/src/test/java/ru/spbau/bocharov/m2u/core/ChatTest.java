package ru.spbau.bocharov.m2u.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ru.spbau.bocharov.m2u.network.IIOService;
import ru.spbau.bocharov.m2u.protocol.Protocol;

import java.io.IOException;
import java.util.*;

public class ChatTest {

    private static final String USERNAME1 = "basta";
    private static final String USERNAME2 = "guf";
    private static final String MESSAGE1 = "yo man";

    private static final String IP1 = "127.0.0.1";
    private static final short PORT1 = 9001;
    private static final long CONNECTION_ID1 = 1;

    private static final String IP2 = "127.0.0.2";
    private static final short PORT2 = 9002;
    private static final long CONNECTION_ID2 = 2;

    @Test
    public void shouldNotifyOnlyWhenNewRoomCreated() throws IOException {
        IIOService io = mockIO();
        List<Long> notifiedIds = new LinkedList<>();
        IChatRoomListener listener = new IChatRoomListener() {

            @Override
            public void processUsernameChange(String newUsername) {}

            @Override
            public void processRoomChange(ChatRoom room) {
                notifiedIds.add(room.getId());
            }
        };

        Chat chat = new Chat(io, USERNAME1);
        chat.registerChatRoomListener(listener);

        chat.createRoom(IP1, PORT1);
        chat.createRoom(IP2, PORT2);

        assertEquals(
                Arrays.asList(CONNECTION_ID1, CONNECTION_ID2),
                notifiedIds);
    }

    @Test
    public void shouldNotifyWhenUsernameChanged() throws IOException {
        IIOService io = mockIO();
        Chat chat = new Chat(io, USERNAME1);
        IChatRoomListener listener = new IChatRoomListener() {
            @Override
            public void processUsernameChange(String newUsername) {
                assertEquals(USERNAME2, newUsername);
            }

            @Override
            public void processRoomChange(ChatRoom room) {}
        };

        chat.registerChatRoomListener(listener);
        chat.setUsername(USERNAME2);
    }

    @Test
    public void shouldNotifyOnMessageToNewRoom() throws IOException {
        IIOService io = mockIO();
        Chat chat = new Chat(io, USERNAME1);
        IChatRoomListener listener = new IChatRoomListener() {
            @Override
            public void processUsernameChange(String newUsername) {
                assertEquals(USERNAME1, newUsername);
            }

            @Override
            public void processRoomChange(ChatRoom room) {
                assertEquals(CONNECTION_ID1, room.getId());
                assertEquals(USERNAME1, room.getUsername());
                assertEquals(
                        Collections.singletonList(MESSAGE1),
                        room.getHistory());
            }
        };

        chat.registerChatRoomListener(listener);
        chat.receive(CONNECTION_ID1, createMessage(USERNAME1, MESSAGE1));
    }

    @Test
    public void shouldNotifyOnMessageToExistingRoom() throws IOException {
        IIOService io = mockIO();
        Chat chat = new Chat(io, USERNAME1);
        chat.createRoom(IP1, PORT1);

        IChatRoomListener listener = new IChatRoomListener() {
            @Override
            public void processUsernameChange(String newUsername) {
                assertEquals(USERNAME1, newUsername);
            }

            @Override
            public void processRoomChange(ChatRoom room) {
                assertEquals(CONNECTION_ID1, room.getId());
                assertEquals(USERNAME1, room.getUsername());
                assertEquals(
                        Collections.singletonList(MESSAGE1),
                        room.getHistory());
            }
        };

        chat.registerChatRoomListener(listener);
        chat.receive(CONNECTION_ID1, createMessage(USERNAME1, MESSAGE1));
    }

    private static IIOService mockIO() throws IOException {
        IIOService io = mock(IIOService.class);
        when(io.connect(IP1, PORT1)).thenReturn(CONNECTION_ID1);
        when(io.connect(IP2, PORT2)).thenReturn(CONNECTION_ID2);
        return io;
    }

    private static Protocol.Message createMessage(String user, String text) {
        Protocol.Message.Builder builder = Protocol.Message.newBuilder();
        builder.setUsername(user);
        builder.setText(text);
        return builder.build();
    }
}