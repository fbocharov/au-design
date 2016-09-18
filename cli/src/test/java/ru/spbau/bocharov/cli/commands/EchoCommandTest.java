package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.StringJoiner;

import static org.junit.Assert.*;

public class EchoCommandTest extends CommandTestBase {

    @Test
    public void shouldEchoSimpleText() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        String input = "this is simple text";
        command.addArguments(input);

        command.execute(new IO(null, out, err));

        assertEquals(
                input + "\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldLeaveEscapedQuotes() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        String input = "\\\"string \\\" with escaped \\\" quotes\\\"";
        command.addArguments(input);

        command.execute(new IO(null, out, err));

        assertEquals(
                "\"string \" with escaped \" quotes\"\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldRemoveUnescapedQuotes() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        String input = "\"string \" with unescaped \" quotes\"";
        command.addArguments(input);

        command.execute(new IO(null, out, err));

        assertEquals(
                "string  with unescaped  quotes\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldRemoveOnlyUnescapedQuotes() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        String input = "\"string \\\" with different \" quotes\\\"";
        command.addArguments(input);

        command.execute(new IO(null, out, err));

        assertEquals(
                "string \" with different  quotes\"\n",
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Test
    public void shouldConcatenateOutput() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ICommand command = createCommand();
        String input1 = "hello";
        String input2 = "world";
        String input3 = "!";
        command.addArguments(input1, input2, input3);

        command.execute(new IO(null, out, err));

        assertEquals(
                new StringJoiner(" ", "", "\n").add(input1).add(input2).add(input3).toString(),
                out.toString());
        assertTrue(err.toString().isEmpty());
    }

    @Override
    protected ICommand createCommand() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return CommandFactory.getInstance().createCommand("echo");
    }
}