package ru.spbau.bocharov.cli.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.bocharov.cli.commands.Command;
import ru.spbau.bocharov.cli.common.CommandWithArguments;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Class representing pipeline executor.
 * Runs commands one by one passing output of one command to input of another.
 */
public class Executor {

    private static final Logger log = LogManager.getLogger(Executor.class);

    /**
     * Runs command passing bytes from output to input.
     * Also substitute variables from context to command arguments.
     * Takes io.STDIN as input for first command and io.STDOUT as output for last command.
     *
     * @param io input-output for commands
     * @param context variables values
     * @param commands commands to run in right order
     * @throws Exception if any errors during execution occurs
     */
    public void execute(IO io, Context context, List<CommandWithArguments> commands) throws Exception {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        if (commands.size() == 1) {
            executeOneCommand(commands.get(0), io, context);
        } else {
            InputStream stdin = io.STDIN;
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();

            for (CommandWithArguments cmd : commands) {
                stdout.reset();
                IO newIo = new IO(stdin, stdout, stderr);

                executeOneCommand(cmd, newIo, context);

                if (!stderr.toString().isEmpty()) {
                    io.STDERR.write(stderr.toByteArray());
                    io.STDERR.flush();
                    return;
                }

                stdin = new ByteArrayInputStream(stdout.toByteArray());
            }
        }
        io.STDOUT.write(stdout.toByteArray());
        io.STDOUT.flush();
    }

    private void executeOneCommand(CommandWithArguments cmdWithArgs, IO io, Context context) throws Exception {
        log.info("Executing command " + cmdWithArgs.command.getName());

        Command cmd = cmdWithArgs.command;
        cmdWithArgs.arguments.forEach(arg -> {
            cmd.addArguments(arg.substitute(context));
        });
        cmd.execute(io, context);
    }
}
