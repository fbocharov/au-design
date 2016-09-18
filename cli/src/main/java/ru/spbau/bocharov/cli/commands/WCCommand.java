package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class WCCommand extends BaseCommand {

    public WCCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) throws IOException {
        if (io.STDIN == null && arguments.isEmpty()) {
            io.STDERR.println("can't execute wc with empty input");
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
                    e.printStackTrace(io.STDERR);
                }
                totalCounters.add(counters);

                printCounters(io, counters);
            }

            if (arguments.size() > 1) {
                printCounters(io, totalCounters);
            }
        } else {
            Counters counters = new Counters();
            Scanner sc = new Scanner(io.STDIN);
            int emptyLineCount = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    emptyLineCount++;
                    if (emptyLineCount == 2) {
                        break;
                    }
                }

                counters.lineCount++;
                counters.wordCount += line.split(" ").length;
                counters.byteCount += line.length();
            }
            printCounters(io, counters);
        }
    }

    private void printCounters(IO io, Counters counters) {
        io.STDOUT.format("%d %d %d\n",
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