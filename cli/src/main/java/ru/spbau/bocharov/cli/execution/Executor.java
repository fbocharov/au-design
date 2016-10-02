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

//    private static final char WEAK_QUOTE = '"';
//    private static final char STRONG_QUOTE = '\'';
//    private static final char VARIABLE_CHAR = '$';
//
//    private static String substitute(String arg, Context context) {
//        StringBuilder result = new StringBuilder();
//
//        boolean insideStrongQuote = false;
//        for (int i = 0; i < arg.length(); ++i) {
//            if (arg.charAt(i) == STRONG_QUOTE) {
//                insideStrongQuote = !insideStrongQuote;
//                continue;
//            }
//
//            if (arg.charAt(i) == WEAK_QUOTE) {
//                continue;
//            }
//
//            if (arg.charAt(i) == WEAK_QUOTE) {
//                i++;
//                while (i < arg.length() && arg.charAt(i) != WEAK_QUOTE) {
//                    if (arg.charAt(i) == VARIABLE_CHAR) {
//                        StringBuilder varName = new StringBuilder();
//                        while (i + 1 < arg.length() && Character.isLetterOrDigit(arg.charAt(i + 1))) {
//                            i++;
//                            varName.append(arg.charAt(i));
//                        }
//
//                        result.append(context.get(varName.toString()));
//                    } else {
//                        result.append(arg.charAt(i));
//                    }
//                }
//            } else if (arg.charAt(i) != STRONG_QUOTE) {
//                result.append(arg.charAt(i));
//            }
//        }
//
//        return result.toString();
//    }
}
