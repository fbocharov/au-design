package ru.spbau.bocharov.cli.commands;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class CommandFactoryTest {

    @Test
    public void shouldReturnSameFactory() {
        assertEquals(
                CommandFactory.getInstance(),
                CommandFactory.getInstance());
    }

    @Test
    public void shouldCreateCommands() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        CommandFactory factory = CommandFactory.getInstance();

        assertTrue(factory.createCommand("cat") instanceof CatCommand);
        assertTrue(factory.createCommand("echo") instanceof EchoCommand);
        assertTrue(factory.createCommand("pwd") instanceof PWDCommand);
        assertTrue(factory.createCommand("wc") instanceof WCCommand);
        assertTrue(factory.createCommand("ls") instanceof ExternalCommand);
    }
}