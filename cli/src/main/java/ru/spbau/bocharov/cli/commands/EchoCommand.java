package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.util.StringJoiner;


/**
 * Class representing UNIX echo command
 */
public class EchoCommand extends BaseCommand {

    public EchoCommand(String commandName) {
        super(commandName);
    }

    /**
     * Just prints its argument to io.STDOUT
     *
     * @param io stdin, stdout and stderr of command
     * @param context some variables defined earlier
     * @throws IOException if no stdout or any print errors occurs
     */
    @Override
    public void execute(IO io, Context context) throws IOException {
        StringJoiner joiner = new StringJoiner(" ", "", "\n");
        for (String arg: arguments) {
            joiner.add(arg);
        }
        io.STDOUT.write(joiner.toString().getBytes());
    }
}
