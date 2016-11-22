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
 * Class representing UNIX cat command
 */
public class CatCommand extends BaseCommand {

    public CatCommand(String commandName) {
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
            stderr.println("can't execute cat with empty input");
            return;
        }

        if (!arguments.isEmpty()) {
            for (String filePath : arguments) {
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.forEach(stdout::println);
                }
            }
        } else {
            assert io.STDIN != null;
            IOUtils.interactive(io.STDIN, stdout::println);
        }
    }
}
