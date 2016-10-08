package ru.spbau.bocharov.cli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class IOUtils {

    public static void pipeStream(InputStream input, OutputStream output)
            throws IOException {
        byte buffer[] = new byte[1024];

        while (input.available() > 0) {
            int numRead = input.read(buffer);
            output.write(buffer, 0, numRead);
        }

        output.flush();
    }


    public static void interactive(InputStream input, Consumer<String> lineProcessor) {
        Scanner sc = new Scanner(input);
        int emptyLineCount = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                emptyLineCount++;
                if (emptyLineCount == 2) {
                    break;
                }
            }

            lineProcessor.accept(line);
        }
    }
}
