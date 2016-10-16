package ru.spbau.bocharov.cli.commands.ex;

import java.io.IOException;

/**
 * Exception which {@link ru.spbau.bocharov.cli.commands.Command} may throws if file type does not match with expected
 *
 * @author Vitaliy.Bibaev
 */
public class WrongFileTypeException extends IOException {
    /**
     * Constructs a new instance of {@link WrongFileTypeException}
     *
     * @param message the message why this exception was thrown
     */
    public WrongFileTypeException(String message) {
        super(message);
    }
}
