package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class WCCommandTest extends CommandTestBase {

    @Test
        public void shouldCountInInputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(new ByteArrayInputStream("a b c\nd e f".getBytes()), out, err);

        createCommand().execute(io, createContext());

        assertEquals(
                "2 6 10\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldCountInFiles() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        command.addArguments(
                getFilePath("test1.txt"),
                getFilePath("test2.txt"));

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                "2 6 10\n" +
                "120 1164 2258\n" +
                "122 1170 2268\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }


    @Override
    protected ICommand createCommand() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return CommandFactory.getInstance().createCommand("wc");
    }
}