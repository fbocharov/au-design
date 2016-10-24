package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.StringJoiner;

import static org.junit.Assert.*;

public class EchoCommandTest extends CommandTestBase {

    @Test
    public void shouldEchoSimpleText() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        Command command = createCommand();
        String input = "this is simple text";
        command.addArguments(input);

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                input + "\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldEchoTextWithWeakQuotes() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        Command command = createCommand();
        String input = "text with \"weak\" quotes";
        command.addArguments(input);

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                input + "\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldEchoTextWithStrongQuotes() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        Command command = createCommand();
        String input = "text with 'strong' quotes";
        command.addArguments(input);

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                input + "\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldConcatenateOutput() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        Command command = createCommand();
        String input1 = "hello";
        String input2 = "world";
        String input3 = "!";
        command.addArguments(input1, input2, input3);

        command.execute(new IO(null, out, err), createContext());

        assertEquals(
                new StringJoiner(" ", "", "\n").add(input1).add(input2).add(input3).toString(),
                out.toString());
        assertTrue(err.toString().isEmpty());
    }


    @Override
    protected Command createCommand() throws Exception {
        return CommandFactory.getInstance().createCommand("echo");
    }
}