package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.PrintStream;

public class PWDCommand extends BaseCommand {

    public PWDCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) {
        new PrintStream(io.STDOUT).println(System.getProperty("user.dir"));
    }
}
