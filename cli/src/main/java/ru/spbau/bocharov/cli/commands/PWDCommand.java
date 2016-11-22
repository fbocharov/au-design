package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.PrintStream;

/**
 * Class representing UNIX pwd command
 */
public class PWDCommand extends BaseCommand {

    public PWDCommand(String commandName) {
        super(commandName);
    }

    /**
     * Just prints to stdout current working directory
     *
     * @param io stdin, stdout and stderr of command
     * @param context unused
     */
    @Override
    public void execute(IO io, Context context) {
        new PrintStream(io.STDOUT).println(System.getProperty("user.dir"));
    }
}
