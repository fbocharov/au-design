package ru.spbau.bocharov.cli.parser;

import java.util.LinkedList;
import java.util.List;

import static ru.spbau.bocharov.cli.parser.ParseUtils.isEscapedChar;
import static ru.spbau.bocharov.cli.parser.ParseUtils.isQuote;

public class Lexer {

    public List<String> tokenize(String line, char separator) throws LexerException {
        List<String> result = new LinkedList<>();

        boolean insideQuotes = false;
        char prevQuote = 0;
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            boolean currentIsEscaped = isEscapedChar(line, i);

            if (c == separator && !currentIsEscaped && !insideQuotes) {
                String t = token.toString().trim();
                if (!t.isEmpty()) {
                    result.add(t);
                }
                token.setLength(0);
                continue;
            }

            if (!currentIsEscaped && isQuote(c)) {
                insideQuotes = !insideQuotes;
                // check that quotes are the same at the both ends
                if (!insideQuotes && prevQuote != c) {
                    throw new LexerException("wrong closing quote: " + c);
                }
                prevQuote = c;
            }

            token.append(c);
        }
        String t = token.toString().trim();
        if (!t.isEmpty()) {
            result.add(t);
        }

        if (insideQuotes) {
            throw new LexerException("quote left open: " + prevQuote);
        }

        return result;
    }
}
