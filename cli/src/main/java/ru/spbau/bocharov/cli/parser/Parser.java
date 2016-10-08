package ru.spbau.bocharov.cli.parser;

import ru.spbau.bocharov.cli.commands.CommandFactory;
import ru.spbau.bocharov.cli.commands.Command;
import ru.spbau.bocharov.cli.common.CommandWithArguments;
import ru.spbau.bocharov.cli.common.QuoteString;
import ru.spbau.bocharov.cli.parser.quotes.ComplexQuoteString;
import ru.spbau.bocharov.cli.parser.quotes.StrongQuoteString;
import ru.spbau.bocharov.cli.parser.quotes.WeakQuoteString;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.spbau.bocharov.cli.parser.ParseUtils.*;

public class Parser {

    static final char PIPE  = '|';
    static final char SPACE = ' ';
    static final char ASSIGNMENT = '=';

    public List<CommandWithArguments> parse(String input) throws LexerException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<CommandWithArguments> result = new LinkedList<>();

        Lexer lexer = new Lexer();
        CommandFactory factory = CommandFactory.getInstance();

        for (String token: lexer.tokenize(input, PIPE)) {
            List<String> cmdWithArgs = lexer.tokenize(token, SPACE);
            assert !cmdWithArgs.isEmpty();

            String cmd = cmdWithArgs.get(0);
            Command command;
            List<String> arguments;
            if (cmd.indexOf(ASSIGNMENT) != -1) {
                command = factory.createCommand(String.valueOf(ASSIGNMENT));
                arguments = lexer.tokenize(cmd, ASSIGNMENT);
            } else {
                command = factory.createCommand(cmd);
                arguments = cmdWithArgs.subList(1, cmdWithArgs.size());
            }
            result.add(
                    new CommandWithArguments(
                            command,
                            arguments.stream().map(Parser::toQuoteString).collect(Collectors.toList())
                    ));
        }

        return result;
    }

    private static QuoteString toQuoteString(String str) {
        List<QuoteString> quoteStrings = new LinkedList<>();

        StringBuilder quote = new StringBuilder();
        boolean insideQuote = false;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            quote.append(c);
            if (isQuote(c) && !isEscapedChar(str, i)) {
                insideQuote = !insideQuote;

                if (!insideQuote) {
                    quoteStrings.add(createQuoteString(quote.toString()));
                    quote = new StringBuilder();
                }
            }
        }
        if (!quote.toString().isEmpty()) {
            quoteStrings.add(createQuoteString(quote.toString()));
        }

        return quoteStrings.size() == 1 ?
                quoteStrings.get(0) : new ComplexQuoteString(quoteStrings);
    }

    private static QuoteString createQuoteString(String bodyWithQuotes) {
        String body = bodyWithQuotes;
        if (isQuote(bodyWithQuotes.charAt(0))) {
            body = bodyWithQuotes.substring(1, bodyWithQuotes.length() - 1);
        }
        return body.charAt(0) == ParseUtils.STRONG_QUOTE ?
                new StrongQuoteString(removeBackslashes(body)) :
                new WeakQuoteString(removeBackslashes(body));
    }

    private static String removeBackslashes(String str) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c != '\\' || isEscapedChar(str, i)) {
                result.append(c);
            }
        }

        return result.toString();
    }
}
