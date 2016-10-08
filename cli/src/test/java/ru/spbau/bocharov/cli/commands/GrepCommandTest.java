package ru.spbau.bocharov.cli.commands;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Test;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class GrepCommandTest extends CommandTestBase{

    @Test
    public void shouldGrepSimpleExpressionFromFile() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(null, out, err);

        Command cmd = createCommand();
        cmd.addArguments("a", getFilePath("test1.txt"));
        cmd.execute(io, null);

        assertEquals(
                "a b c\n",
                out.toString());
    }

    @Test
    public void shouldGrepSimpleExpressionFromStdin() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("e i f\na b c".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("a");
        cmd.execute(io, null);

        assertEquals(
                "a b c\n",
                out.toString());
    }

    @Test
    public void shouldIgnoreCase() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("e i f\na b c".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("-i", "A B C");
        cmd.execute(io, null);

        assertEquals(
                "a b c\n",
                out.toString());
    }

    @Test
    public void shouldNotIgnoreCase() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("e i f\na b c".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("B C");
        cmd.execute(io, null);

        assertEquals(
                "",
                out.toString());
    }

    @Test
    public void shouldGrepWholeWords() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("this is test line\nthis is test1 line".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("-w", "test");
        cmd.execute(io, null);

        assertEquals(
                "this is test line\n",
                out.toString());
    }

    @Test
    public void shouldGrepWordsAndIgnoreCase() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("this is test line\nthis is test1 line".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("-w", "-i", "TEST1");
        cmd.execute(io, null);

        assertEquals(
                "this is test1 line\n",
                out.toString());
    }

    @Test
    public void shouldPrintOnlyNLines() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("this is test line\nthis is test1 line".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("-A 1", "this is");
        cmd.execute(io, null);

        assertEquals(
                "this is test line\n",
                out.toString());
    }

    @Test
    public void shouldGrepWithRegex() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("123\nabc\n,[)".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        IO io = new IO(in, out, err);

        Command cmd = createCommand();
        cmd.addArguments("\\d");
        cmd.execute(io, null);

        assertEquals(
                "123\n",
                out.toString());
    }

    @Override
    protected Command createCommand() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return CommandFactory.getInstance().createCommand("grep");
    }
}