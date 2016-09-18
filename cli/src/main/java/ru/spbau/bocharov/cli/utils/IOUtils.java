package ru.spbau.bocharov.cli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

    public static void pipeStream(InputStream input, OutputStream output)
            throws IOException {
        byte buffer[] = new byte[1024];
        int numRead = 0;

        do {
            numRead = input.read(buffer);
            output.write(buffer, 0, numRead);
        } while (input.available() > 0);

        output.flush();
    }
}
