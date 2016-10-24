package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class CatCommandTest extends CommandTestBase {

    @Test
    public void shouldCatFile() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        Command command = createCommand();
        command.addArguments(getFilePath("test1.txt"));

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                "a b c\n" +
                "d e f\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldStopOnTwoEmptyLines() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("hello\nworld\n!\n\n\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        createCommand().execute(new IO(in, out, err), createContext());

        assertEquals(
                "hello\n" +
                "world\n" +
                "!\n\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }


    @Override
    protected Command createCommand() throws Exception {
        return CommandFactory.getInstance().createCommand("cat");
    }
}