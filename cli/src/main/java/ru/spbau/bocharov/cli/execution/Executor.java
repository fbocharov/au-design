package ru.spbau.bocharov.cli.execution;

import ru.spbau.bocharov.cli.commands.ICommand;
import ru.spbau.bocharov.cli.common.CommandWithArguments;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class Executor {

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
        ICommand cmd = cmdWithArgs.command;
        cmdWithArgs.arguments.forEach(arg -> {
            cmd.addArguments(arg.substitute(context));
        });
        cmd.execute(io, context);
    }
}
