package ru.spbau.bocharov.cli.parser;


/**
 * Parser utility class
 */
class ParseUtils {

    static final char STRONG_QUOTE = '\'';
    static final char WEAK_QUOTE = '"';

    static boolean isEscapedChar(String line, int i) {
        int backslashCount = 0;
        for (int j = i - 1; j >= 0 && line.charAt(j) == '\\'; --j) {
            backslashCount++;
        }
        return (backslashCount % 2) == 1;
    }

    static boolean isQuote(char c) {
        return c == STRONG_QUOTE || c == WEAK_QUOTE;
    }
}
