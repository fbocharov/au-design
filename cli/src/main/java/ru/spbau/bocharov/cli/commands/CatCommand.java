package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
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
        if (io.STDIN == null && arguments.isEmpty()) {
            io.STDERR.println("can't execute cat with empty input");
            return;
        }

        if (!arguments.isEmpty()) {
            for (String filePath : arguments) {
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.forEach(io.STDOUT::println);
                } catch (IOException e) {
                    e.printStackTrace(io.STDERR);
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

                io.STDOUT.println(line);
            }
        }

    }
}
