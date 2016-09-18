package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.Context;
import ru.spbau.bocharov.cli.common.IO;

import java.util.StringJoiner;

public class EchoCommand extends BaseCommand {

    public EchoCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io, Context context) {
        StringJoiner joiner = new StringJoiner(" ", "", "\n");
        for (String arg: arguments) {
            joiner.add(removeQuotes(arg));
        }
        io.STDOUT.print(joiner.toString());
    }

    private String removeQuotes(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '"') {
                continue;
            }

            if (str.charAt(i) != '\\') {
                builder.append(str.charAt(i));
            } else if (i + 1 < str.length() && str.charAt(i + 1) == '"') {
                // skip escaper
                builder.append(str.charAt(i + 1));
                ++i;
            }
        }

        return builder.toString();
    }
}
