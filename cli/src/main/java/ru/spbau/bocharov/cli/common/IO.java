package ru.spbau.bocharov.cli.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class IO {
    public final InputStream STDIN;
    public final OutputStream STDOUT;
    public final OutputStream STDERR;

    public IO(InputStream in, OutputStream out, OutputStream err) {
        STDIN = in;
        STDOUT = new PrintStream(out);
        STDERR = new PrintStream(err);
    }
}
