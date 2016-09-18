package ru.spbau.bocharov.cli.execution;

import ru.spbau.bocharov.cli.commands.ICommand;
import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class Executor {

    public void execute(IO io, Context context, List<ICommand> commands) throws Exception {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        if (commands.size() == 1) {
            commands.get(0).execute(io, context);
        } else {
            InputStream stdin = io.STDIN;
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();

            for (ICommand cmd : commands) {
                IO newIo = new IO(stdin, stdout, stderr);

                cmd.execute(newIo, context);

                if (!stderr.toString().isEmpty()) {
                    io.STDERR.write(stderr.toByteArray());
                    io.STDERR.flush();
                    return;
                }

                stdin = new ByteArrayInputStream(stdout.toByteArray());
                stdout.reset();
            }
        }
        io.STDOUT.write(stdout.toByteArray());
        io.STDOUT.flush();
    }
}
