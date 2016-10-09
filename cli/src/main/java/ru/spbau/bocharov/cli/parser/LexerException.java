package ru.spbau.bocharov.cli.parser;

/**
 * Class which #{@link Lexer} throws if any lexer error occurs
 */
public class LexerException extends Exception {

    public LexerException(String what) {
        super(what);
    }
}
