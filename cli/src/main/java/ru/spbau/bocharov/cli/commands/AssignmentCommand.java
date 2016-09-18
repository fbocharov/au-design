package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

public class AssignmentCommand extends BaseCommand {

    public AssignmentCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) throws Exception {
        if (arguments.size() != 2) {
            throw new Exception("wrong number of arguments for assignment");
        }

        if (context == null) {
            throw new Exception("can't add variables to null context");
        }

        context.put(arguments.get(0), arguments.get(1));
    }
}
