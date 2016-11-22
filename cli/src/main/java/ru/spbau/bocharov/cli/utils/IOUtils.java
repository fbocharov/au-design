package ru.spbau.bocharov.cli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Some commonly used io utils.
 */

public class IOUtils {

    /**
     * Copies all bytes from input to output.
     *
     * @param input where to get data
     * @param output where to put data
     * @throws IOException if any io errors occurs
     */
    public static void pipeStream(InputStream input, OutputStream output)
            throws IOException {
        byte buffer[] = new byte[1024];

        while (input.available() > 0) {
            int numRead = input.read(buffer);
            output.write(buffer, 0, numRead);
        }

        output.flush();
    }


    /**
     * Reads lines from input until 2 endlines.
     * Executes lineProcessor on each line.
     *
     * @param input where to get lines
     * @param lineProcessor function to run on each line
     */
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
