package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

public class PWDCommand extends BaseCommand {

    public PWDCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) {
        io.STDOUT.println(System.getProperty("user.dir"));
    }
}
