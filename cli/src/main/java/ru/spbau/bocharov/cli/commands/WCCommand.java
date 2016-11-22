package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;
import ru.spbau.bocharov.cli.utils.IOUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Class representing UNIX wc command
 */
public class WCCommand extends BaseCommand {

    public WCCommand(String commandName) {
        super(commandName);
    }

    /**
     * Uses arguments as file names if any were provided,
     * otherwise reads data from io.STDIN
     *
     * @param io stdin, stdout and stderr of command
     * @param context some variables defined earlier
     */

    @Override
    public void execute(IO io, Context context) throws IOException {
        PrintStream stdout = new PrintStream(io.STDOUT);
        PrintStream stderr = new PrintStream(io.STDERR);

        if (io.STDIN == null && arguments.isEmpty()) {
            stderr.println("can't execute wc with empty input");
            return;
        }

        if (!arguments.isEmpty()) {
            Counters totalCounters = new Counters();
            for (String fileName: arguments) {
                Counters counters = new Counters();
                try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
                    lines.forEach(line -> {
                        counters.lineCount++;
                        counters.wordCount += line.split(" ").length;
                        counters.byteCount += line.length();
                    });
                } catch (IOException e) {
                    e.printStackTrace(stderr);
                    return;
                }
                totalCounters.add(counters);

                printCounters(stdout, counters);
            }

            if (arguments.size() > 1) {
                printCounters(stdout, totalCounters);
            }
        } else {
            Counters counters = new Counters();
            IOUtils.interactive(io.STDIN, line -> {
                counters.lineCount++;
                counters.wordCount += line.split(" ").length;
                counters.byteCount += line.length();
            });
            printCounters(stdout, counters);
        }
    }

    private void printCounters(PrintStream stdout, Counters counters) {
        stdout.format("%d %d %d\n",
                counters.lineCount, counters.wordCount, counters.byteCount);
    }

    private class Counters {
        private int lineCount = 0;
        private int wordCount = 0;
        private int byteCount = 0;

        private void add(Counters others) {
            lineCount += others.lineCount;
            wordCount += others.wordCount;
            byteCount += others.byteCount;
        }
    }
}
