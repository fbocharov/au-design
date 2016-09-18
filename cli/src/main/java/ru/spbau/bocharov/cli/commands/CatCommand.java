package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class CatCommand extends BaseCommand {

    public CatCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) {
        PrintStream stdout = new PrintStream(io.STDOUT);
        PrintStream stderr = new PrintStream(io.STDERR);

        if (io.STDIN == null && arguments.isEmpty()) {
            stderr.println("can't execute cat with empty input");
            return;
        }

        if (!arguments.isEmpty()) {
            for (String filePath : arguments) {
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.forEach(stdout::println);
                } catch (IOException e) {
                    e.printStackTrace(stderr);
                }
            }
        } else {
            assert io.STDIN != null;

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

                stdout.println(line);
            }
        }

    }
}
