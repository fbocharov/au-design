package ru.spbau.bocharov.cli.execution;

import org.junit.Test;
import ru.spbau.bocharov.cli.commands.ICommand;
import ru.spbau.bocharov.cli.common.IO;
import ru.spbau.bocharov.cli.parser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ExecutorTest {

    @Test
    public void shouldExecuteSimpleCommandWithArgs() throws Exception {
        OutputStream stdout = new ByteArrayOutputStream();
        OutputStream stderr = new ByteArrayOutputStream();
        IO io = new IO(null, stdout, stderr);

        List<ICommand> commands = new Parser().parse("echo hello, world!");
        createExecutor().execute(io, null, commands);

        assertEquals("hello, world!\n", stdout.toString());
    }

    @Test
    public void shouldExecuteSimplePipeline() throws Exception {
        OutputStream stdout = new ByteArrayOutputStream();
        OutputStream stderr = new ByteArrayOutputStream();
        IO io = new IO(null, stdout, stderr);

        List<ICommand> commands = new Parser().parse("echo hello, world! | wc");
        createExecutor().execute(io, null, commands);

        assertEquals("1 2 13\n", stdout.toString());
    }

    private Executor createExecutor() {
        return new Executor();
    }
}