package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringJoiner;

public class ExternalCommand extends BaseCommand {

    public ExternalCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) {
        try {
            Process process = Runtime.getRuntime().exec(createShellCommand());
            pipeStream(io.STDIN, process.getOutputStream());
            pipeStream(process.getInputStream(), io.STDOUT);
        } catch (IOException e) {
            e.printStackTrace(io.STDERR);
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

    private static void pipeStream(InputStream input, OutputStream output)
            throws IOException {
        byte buffer[] = new byte[1024];
        int numRead = 0;

        do {
            numRead = input.read(buffer);
            output.write(buffer, 0, numRead);
        } while (input.available() > 0);

        output.flush();
    }
}
