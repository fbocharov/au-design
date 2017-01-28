package ru.spbau.bocharov.m2u.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ChatRoomTest {

    private static final long ROOM_ID = 228;
    private static final String USERNAME1 = "basta";
    private static final String USERNAME2 = "guf";

    private static final String MESSAGE1 = "yo man";
    private static final String MESSAGE2 = "wazza";

    @Test
    public void shouldReturnCorrectIdAndUsername() {
        ChatRoom room = new ChatRoom(ROOM_ID, USERNAME1);

        assertEquals(ROOM_ID, room.getId());
        assertEquals(USERNAME1, room.getUsername());
    }

    @Test
    public void shouldUpdateUsername() {
        ChatRoom room = new ChatRoom(ROOM_ID, USERNAME1);
        assertEquals(USERNAME1, room.getUsername());

        room.setUsername(USERNAME2);
        assertEquals(USERNAME2, room.getUsername());
    }

    @Test
    public void shouldOnlyAppendToHistory() {
        ChatRoom room = new ChatRoom(ROOM_ID, USERNAME1);
        List<String> history = room.getHistory();
        assertTrue(history.isEmpty());

        room.appendMessage(MESSAGE1);
        history = room.getHistory();
        assertEquals(
                Collections.singletonList(MESSAGE1),
                history);

        room.appendMessage(MESSAGE2);
        history = room.getHistory();
        assertEquals(
                Arrays.asList(MESSAGE1, MESSAGE2),
                history);
    }
}