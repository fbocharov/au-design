package ru.spbau.bocharov.cli.commands;

import ru.spbau.bocharov.cli.common.IO;

public class EchoCommand extends BaseCommand {

    public EchoCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(IO io) {
        for (String arg: arguments) {
            io.STDOUT.print(removeQuotes(arg));
        }
        io.STDOUT.print('\n');
    }

    private String removeQuotes(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '"') {
                continue;
            }

            builder.append(str.charAt(i));
            if (str.charAt(i) == '\\' && i + 1 < str.length() &&
                    str.charAt(i + 1) == '"') {
                builder.append(str.charAt(i + 1));
                ++i;
            }
        }

        return builder.toString();
    }
}
