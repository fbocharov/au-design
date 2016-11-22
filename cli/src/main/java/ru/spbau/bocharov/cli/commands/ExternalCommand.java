package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.io.PrintStream;
import java.util.StringJoiner;

import static ru.spbau.bocharov.cli.utils.IOUtils.pipeStream;

/**
 * Class to execute unknown command in external shell
 */
public class ExternalCommand extends BaseCommand {

    public ExternalCommand(String commandName) {
        super(commandName);
    }

    /**
     * Runs command provided in constructor in external shell, piping
     * io.STDIN and io.STDOUT to it
     *
     * @param io stdin, stdout and stderr of command
     * @param context some variables defined earlie
     */
    @Override
    public void execute(IO io, Context context) {
        try {
            Process process = Runtime.getRuntime().exec(createShellCommand());
            pipeStream(io.STDIN, process.getOutputStream());
            process.waitFor();
            pipeStream(process.getInputStream(), io.STDOUT);
        } catch (IOException e) {
            e.printStackTrace(new PrintStream(io.STDERR));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String createShellCommand() {
        StringJoiner joiner = new StringJoiner(" ");

        joiner.add(name);
        for (String arg: arguments) {
            joiner.add(arg);
        }

        return joiner.toString();
    }
}
