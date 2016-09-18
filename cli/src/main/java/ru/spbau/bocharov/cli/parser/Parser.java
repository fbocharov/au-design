package ru.spbau.bocharov.cli.parser;

import ru.spbau.bocharov.cli.commands.CommandFactory;
import ru.spbau.bocharov.cli.commands.ICommand;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    public static final char PIPE  = '|';
    public static final char SPACE = ' ';
    public static final char ASSIGNMENT = '=';

    public List<ICommand> parse(String input) throws LexerException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<ICommand> result = new LinkedList<>();

        Lexer lexer = new Lexer();
        CommandFactory factory = CommandFactory.getInstance();

        for (String token: lexer.tokenize(input, PIPE)) {
            List<String> cmdWithArgs = lexer.tokenize(token, SPACE);
            assert !cmdWithArgs.isEmpty();

            String cmd = cmdWithArgs.get(0);
            if (cmd.indexOf(ASSIGNMENT) != -1) {
                ICommand assignment = factory.createCommand(String.valueOf(ASSIGNMENT));
                assignment.addArguments(lexer.tokenize(cmd, ASSIGNMENT));
                result.add(assignment);
            } else {
                // TODO: substitute
                ICommand command = factory.createCommand(cmd);
                if (cmdWithArgs.size() > 1) {
                    command.addArguments(cmdWithArgs.subList(1, cmdWithArgs.size()));
                }
                result.add(command);
            }
        }

        return result;
    }
}
