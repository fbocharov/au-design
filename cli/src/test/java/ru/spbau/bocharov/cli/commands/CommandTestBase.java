package ru.spbau.bocharov.cli.commands;

import org.junit.Test;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public abstract class CommandTestBase {

    @Test
    public void shouldPrintErrorIfNoStdinOrArguments() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        createCommand().execute(new IO(null, stream, stream), createContext());
        assertTrue(!stream.toString().isEmpty());
    }

    protected abstract Command createCommand() throws Exception;

    protected String getFilePath(String filename) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(filename);
        return resource == null ? null : resource.getFile();
    }

    protected Context createContext() {
        return new Context();
    }
}
